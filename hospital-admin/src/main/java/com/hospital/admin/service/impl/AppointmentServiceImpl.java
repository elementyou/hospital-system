package com.hospital.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.admin.dto.AppointmentDTO;
import com.hospital.admin.vo.AppointmentVO;
import com.hospital.admin.entity.Appointment;
import com.hospital.admin.entity.Doctor;
import com.hospital.admin.entity.Schedule;
import com.hospital.admin.mapper.AppointmentMapper;
import com.hospital.admin.mapper.ScheduleMapper;
import com.hospital.admin.service.AppointmentService;
import com.hospital.admin.service.DoctorService;
import com.hospital.admin.service.ScheduleService;
import com.hospital.common.Result;
import com.hospital.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {

    private final ScheduleMapper scheduleMapper;
    private final StringRedisTemplate redisTemplate;
    private final ScheduleService scheduleService;
    private final DoctorService doctorService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> createAppointment(AppointmentDTO dto) {
        // Redis分布式锁：同一患者同一排班串行处理，防止并发超约
        String lockKey = "appointment:lock:" + dto.getPatientId() + ":" + dto.getScheduleId();
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", Duration.ofSeconds(10));
        if (!Boolean.TRUE.equals(locked)) {
            throw new BusinessException("操作过于频繁，请稍后再试");
        }

        try {
            // 2. 校验排班是否存在且正常
            Schedule schedule = scheduleMapper.selectById(dto.getScheduleId());
            if (schedule == null || schedule.getStatus() != 1) {
                throw new BusinessException("排班不存在或已停诊");
            }

            // 3. 同一患者同一时段不能重复预约
            Long count = this.lambdaQuery()
                    .eq(Appointment::getPatientId, dto.getPatientId())
                    .eq(Appointment::getScheduleId, dto.getScheduleId())
                    .in(Appointment::getStatus, 0, 1)
                    .count();
            if (count > 0) {
                throw new BusinessException("您已预约过该时段，请勿重复预约");
            }

            // 4. 乐观锁扣减号源：remain_count > 0 才扣，防超约
            UpdateWrapper<Schedule> uw = new UpdateWrapper<>();
            uw.eq("id", dto.getScheduleId())
                    .gt("remain_count", 0)
                    .setSql("remain_count = remain_count - 1");
            int rows = scheduleMapper.update(null, uw);
            if (rows == 0) {
                throw new BusinessException("号源不足，预约失败");
            }

            // 5. 生成预约记录
            Appointment appointment = new Appointment();
            appointment.setPatientId(dto.getPatientId());
            appointment.setDoctorId(dto.getDoctorId());
            appointment.setScheduleId(dto.getScheduleId());
            appointment.setVisitDate(schedule.getWorkDate());
            appointment.setTimeSlot(schedule.getTimeSlot());
            appointment.setStatus(0);        // 待就诊
            appointment.setQueueNumber(null); // 排队号就诊时分配
            this.save(appointment);

            return Result.success(appointment, "预约成功");
        } finally {
            // 释放锁
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> cancelAppointment(Long id) {
        String lockKey = "lock:cancel:" + id;
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 5, TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(lock)) {
            throw new BusinessException("操作频繁，请稍后重试");
        }
        try {
            Appointment appointment = this.getById(id);
            if (appointment == null) {
                throw new BusinessException("预约记录不存在");
            }
            // 状态：0待就诊，1已完成，2已取消
            if (!appointment.getStatus().equals(0)) {
                throw new BusinessException("该预约无法取消");
            }
            // 查询排班
            Schedule schedule = scheduleService.getById(appointment.getScheduleId());
            if (schedule == null) {
                throw new BusinessException("排班不存在");
            }
            // 归还号源
            schedule.setRemainCount(schedule.getRemainCount() + 1);
            scheduleService.updateById(schedule);
            // 修改预约状态为已取消
            appointment.setStatus(2);
            this.updateById(appointment);
            return Result.success(null, "取消成功");
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    public Page<AppointmentVO> getMyAppointment(Long patientId, Long pageNum, Long pageSize) {
        Page<Appointment> page = this.lambdaQuery()
                .eq(Appointment::getPatientId, patientId)
                .orderByDesc(Appointment::getCreatedAt)
                .page(new Page<>(pageNum, pageSize));

        Page<AppointmentVO> voPage = new Page<>();
        voPage.setCurrent(page.getCurrent());
        voPage.setSize(page.getSize());
        voPage.setTotal(page.getTotal());
        voPage.setPages(page.getPages());

        List<AppointmentVO> voList = page.getRecords().stream()
                .map(app -> {
                    AppointmentVO vo = new AppointmentVO();
                    BeanUtils.copyProperties(app, vo);
                    Doctor doctor = doctorService.getById(app.getDoctorId());
                    if (doctor != null) {
                        vo.setDoctorName(doctor.getName());
                    }
                    return vo;
                })
                .collect(Collectors.toList());

        voPage.setRecords(voList);
        return voPage;
    }

}
