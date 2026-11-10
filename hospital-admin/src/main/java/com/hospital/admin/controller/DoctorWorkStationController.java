package com.hospital.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hospital.common.Result;
import com.hospital.admin.dto.AppointmentIdDTO;
import com.hospital.admin.entity.Appointment;
import com.hospital.admin.entity.Patient;
import com.hospital.admin.service.AppointmentService;
import com.hospital.admin.service.PatientService;
import com.hospital.admin.vo.CallNumberVO;
import com.hospital.admin.vo.QueuePatientVO;
import com.hospital.admin.vo.SimpleQueueVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctor")
@Tag(name = "医生工作站模块")
public class DoctorWorkStationController {

    @Resource
    private AppointmentService appointmentService;
    @Resource
    private PatientService patientService;

    /**
     * 2.1 GET /api/doctor/queue-list 查看当日候诊患者（详情版）
     */
    @GetMapping("/queue-list")
    @Operation(summary = "查看当日候诊患者详情")
    public Result<List<QueuePatientVO>> queueList(
            @RequestParam Long doctorId,
            @RequestParam String visitDate
    ){
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Appointment::getDoctorId, doctorId)
                .eq(Appointment::getVisitDate, visitDate)
                .eq(Appointment::getDeleted, 0)
                .orderByAsc(Appointment::getQueueNumber);
        List<Appointment> appointmentList = appointmentService.list(wrapper);

        List<QueuePatientVO> voList = appointmentList.stream().map(app -> {
            Patient patient = patientService.getById(app.getPatientId());
            QueuePatientVO vo = new QueuePatientVO();
            vo.setId(app.getId());
            vo.setPatientId(app.getPatientId());
            if(patient != null){
                vo.setPatientName(patient.getName());
                vo.setGender(patient.getGender());
                if(patient.getBirthday() != null){
                    vo.setAge(LocalDate.now().getYear() - patient.getBirthday().getYear());
                }
                vo.setPhone(patient.getPhone());
                vo.setAllergyInfo(patient.getAllergyInfo());
            }else{
                vo.setPatientName("未知患者");
            }
            vo.setTimeSlot(app.getTimeSlot());
            vo.setQueueNumber(app.getQueueNumber());
            vo.setStatus(app.getStatus());
            vo.setStatusText(app.getStatus() == 0 ? "待就诊" : "已接诊");
            vo.setCreatedAt(app.getCreatedAt());
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 2.2 PUT /api/doctor/consult 接诊
     */
    @PutMapping("/consult")
    @Operation(summary = "接诊，状态0待就诊→1已接诊")
    public Result<Boolean> consult(@Valid @RequestBody AppointmentIdDTO dto){
        Appointment appointment = appointmentService.getById(dto.getAppointmentId());
        if(appointment == null || appointment.getDeleted() == 1){
            return Result.fail("预约记录不存在");
        }
        if(appointment.getStatus() != 0){
            return Result.fail("当前状态不能接诊");
        }
        appointment.setStatus(1);
        appointmentService.updateById(appointment);
        return Result.success(true);
    }

    /**
     * 2.3 GET /api/doctor/waiting-list 简易候诊队列
     */
    @GetMapping("/waiting-list")
    @Operation(summary = "候诊简易队列")
    public Result<List<SimpleQueueVO>> waitingList(
            @RequestParam Long doctorId,
            @RequestParam String visitDate
    ){
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Appointment::getDoctorId, doctorId)
                .eq(Appointment::getVisitDate, visitDate)
                .eq(Appointment::getDeleted,0)
                .orderByAsc(Appointment::getQueueNumber);
        List<Appointment> list = appointmentService.list(wrapper);
        List<SimpleQueueVO> voList = list.stream().map(app -> {
            Patient p = patientService.getById(app.getPatientId());
            SimpleQueueVO vo = new SimpleQueueVO();
            vo.setId(app.getId());
            vo.setPatientId(app.getPatientId());
            if(p != null){
                vo.setPatientName(p.getName());
            }else{
                vo.setPatientName("未知患者");
            }
            vo.setQueueNumber(app.getQueueNumber());
            vo.setStatus(app.getStatus());
            vo.setStatusText(app.getStatus() ==0 ? "待就诊":"已接诊");
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 2.4 PUT /api/doctor/call-number 叫号分配排队号
     */
    @PutMapping("/call-number")
    @Operation(summary = "叫号，分配排队序号")
    public Result<CallNumberVO> callNumber(@Valid @RequestBody AppointmentIdDTO dto){
        Appointment appointment = appointmentService.getById(dto.getAppointmentId());
        if(appointment == null || appointment.getDeleted() ==1){
            return Result.fail("预约不存在");
        }
        if(appointment.getQueueNumber() != null){
            return Result.fail("已经叫过号");
        }
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Appointment::getDoctorId, appointment.getDoctorId())
                .eq(Appointment::getVisitDate, appointment.getVisitDate())
                .isNotNull(Appointment::getQueueNumber)
                .orderByDesc(Appointment::getQueueNumber)
                .last("limit 1");
        Appointment maxQueueApp = appointmentService.getOne(wrapper);
        int newNum = maxQueueApp == null ? 1 : maxQueueApp.getQueueNumber() + 1;
        appointment.setQueueNumber(newNum);
        appointmentService.updateById(appointment);
        return Result.success(new CallNumberVO(newNum));
    }
}
