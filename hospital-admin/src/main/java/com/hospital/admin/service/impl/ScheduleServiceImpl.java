package com.hospital.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.admin.entity.Schedule;
import com.hospital.admin.mapper.ScheduleMapper;
import com.hospital.admin.service.ScheduleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> listScheduleByDoctorAndDate(Long doctorId, String workDate) {
        LocalDate date = LocalDate.parse(workDate);
        return this.lambdaQuery()
                .eq(Schedule::getDoctorId, doctorId)
                .eq(Schedule::getWorkDate, date)
                .eq(Schedule::getStatus, 1)
                .list();
    }

    @Override
    public Page<Schedule> getSchedulePage(Long pageNum, Long pageSize, Long doctorId, String workDate) {
        Page<Schedule> page = new Page<>(pageNum, pageSize);
        return scheduleMapper.selectSchedulePage(page, doctorId, workDate);
    }
}
