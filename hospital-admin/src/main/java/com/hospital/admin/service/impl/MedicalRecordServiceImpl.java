package com.hospital.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.admin.dto.MedicalRecordQueryDTO;
import com.hospital.admin.dto.MedicalRecordSaveDTO;
import com.hospital.admin.entity.Appointment;
import com.hospital.admin.entity.Doctor;
import com.hospital.admin.entity.MedicalRecord;
import com.hospital.admin.mapper.DoctorMapper;
import com.hospital.admin.mapper.MedicalRecordMapper;
import com.hospital.common.Result;
import com.hospital.admin.service.AppointmentService;
import com.hospital.admin.service.MedicalRecordService;
import com.hospital.admin.vo.MedicalRecordVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MedicalRecordServiceImpl extends ServiceImpl<MedicalRecordMapper, MedicalRecord> implements MedicalRecordService {

    // ================= 1. 依赖注入合并 =================
    @Resource
    private DoctorMapper doctorMapper;

    @Resource
    private AppointmentService appointmentService;

    // ================= 2. 方法一：分页查询 =================
    @Override
    public Result<Page<MedicalRecordVO>> listPatientRecord(MedicalRecordQueryDTO dto) {
        Page<MedicalRecord> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<MedicalRecord> wrapper = new LambdaQueryWrapper<>();

        // 必传：患者id
        wrapper.eq(MedicalRecord::getPatientId, dto.getPatientId());
        wrapper.eq(MedicalRecord::getDeleted, 0);

        // 可选：医生筛选
        if (dto.getDoctorId() != null) {
            wrapper.eq(MedicalRecord::getDoctorId, dto.getDoctorId());
        }

        // 时间范围
        if (StringUtils.hasText(dto.getStartDate())) {
            LocalDate start = LocalDate.parse(dto.getStartDate());
            wrapper.ge(MedicalRecord::getCreatedAt, LocalDateTime.of(start, LocalTime.MIN));
        }
        if (StringUtils.hasText(dto.getEndDate())) {
            LocalDate end = LocalDate.parse(dto.getEndDate());
            wrapper.le(MedicalRecord::getCreatedAt, LocalDateTime.of(end, LocalTime.MAX));
        }

        Page<MedicalRecord> recordPage = page(page, wrapper);
        List<MedicalRecordVO> voList = recordPage.getRecords().stream().map(record -> {
            MedicalRecordVO vo = new MedicalRecordVO();
            vo.setId(record.getId());
            vo.setAppointmentId(record.getAppointmentId());
            vo.setDoctorId(record.getDoctorId());
            Doctor doctor = doctorMapper.selectById(record.getDoctorId());
            vo.setDoctorName(doctor == null ? "" : doctor.getName());
            vo.setChiefComplaint(record.getChiefComplaint());
            vo.setDiagnosis(record.getDiagnosis());
            vo.setStatus(record.getStatus());
            // 状态文字映射
            String statusText = switch (record.getStatus()) {
                case 0 -> "待接诊";
                case 1 -> "已完成";
                default -> "未知";
            };
            vo.setStatusText(statusText);
            vo.setCreatedAt(record.getCreatedAt());
            return vo;
        }).toList();

        // 组装分页结果
        Page<MedicalRecordVO> resultPage = new Page<>();
        resultPage.setCurrent(recordPage.getCurrent());
        resultPage.setSize(recordPage.getSize());
        resultPage.setTotal(recordPage.getTotal());
        resultPage.setRecords(voList);
        return Result.success(resultPage);
    }

    // ================= 3. 方法二：新增/更新病历 =================
    @Override
    @Transactional
    public MedicalRecordVO saveOrUpdateRecord(MedicalRecordSaveDTO dto) {
        Appointment appointment = appointmentService.getById(dto.getAppointmentId());
        if (appointment == null || appointment.getDeleted() == 1) {
            throw new RuntimeException("预约记录不存在");
        }
        // 必须是已接诊 status=1
        if (!appointment.getStatus().equals(1)) {
            throw new RuntimeException("该预约尚未接诊，不能填写病历");
        }

        LambdaQueryWrapper<MedicalRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MedicalRecord::getAppointmentId, dto.getAppointmentId());
        MedicalRecord record = getOne(wrapper);
        boolean isNew = false;

        if (record == null) {
            record = new MedicalRecord();
            record.setAppointmentId(dto.getAppointmentId());
            record.setPatientId(appointment.getPatientId());
            record.setDoctorId(appointment.getDoctorId());
            record.setStatus(0);
            isNew = true;
        } else {
            if (record.getStatus() == 1) {
                throw new RuntimeException("病历已提交完成，禁止修改");
            }
        }

        record.setChiefComplaint(dto.getChiefComplaint());
        record.setPresentIllness(dto.getPresentIllness());
        record.setPastHistory(dto.getPastHistory());
        record.setAllergyRecord(dto.getAllergyRecord());
        record.setDiagnosis(dto.getDiagnosis());
        record.setAdvice(dto.getAdvice());

        if (isNew) {
            save(record);
        } else {
            updateById(record);
        }

        MedicalRecordVO vo = new MedicalRecordVO();
        vo.setId(record.getId());
        vo.setAppointmentId(record.getAppointmentId());
        vo.setPatientId(record.getPatientId());
        vo.setDoctorId(record.getDoctorId());
        vo.setChiefComplaint(record.getChiefComplaint());
        vo.setDiagnosis(record.getDiagnosis());
        vo.setStatus(record.getStatus());
        vo.setStatusText(record.getStatus() == 0 ? "草稿" : "已完成");
        vo.setCreatedAt(record.getCreatedAt());
        return vo;
    }

    // ================= 4. 方法三：提交病历 =================
    @Override
    @Transactional
    public Boolean submitRecord(Long recordId) {
        MedicalRecord record = getById(recordId);
        if (record == null || record.getDeleted() == 1) {
            throw new RuntimeException("病历不存在");
        }
        if (record.getStatus() == 1) {
            throw new RuntimeException("病历已经提交，请勿重复提交");
        }
        record.setStatus(1);
        updateById(record);
        return true;
    }
}