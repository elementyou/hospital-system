package com.hospital.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.admin.dto.AppointmentDTO;
import com.hospital.admin.vo.AppointmentVO;
import com.hospital.admin.entity.Department;
import com.hospital.admin.entity.Doctor;
import com.hospital.admin.entity.Schedule;
import com.hospital.admin.service.AppointmentService;
import com.hospital.admin.service.DepartmentService;
import com.hospital.admin.service.DoctorService;
import com.hospital.admin.service.ScheduleService;
import com.hospital.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@Tag(name = "预约挂号模块")
public class AppointmentController {
    @Resource
    private DepartmentService departmentService;

    @GetMapping("/departments")
    @Operation(summary = "查询科室列表")
    //@PreAuthorize("hasAnyAuthority('PATIENT','DOCTOR','ADMIN', 'admin')")
    public Result<List<Department>> getDepartmentList() {
        List<Department> list = departmentService.listEnableDepartment();
        return Result.success(list);
    }

    @Resource
    private DoctorService doctorService;

    @GetMapping("/doctors")
    @Operation(summary = "根据科室ID查询医生列表")
    //@PreAuthorize("hasAnyAuthority('PATIENT','DOCTOR','ADMIN', 'admin')")
    public Result<List<Doctor>> getDoctorList(@RequestParam("deptId") Long deptId) {
        List<Doctor> list = doctorService.listDoctorsByDeptId(deptId);
        return Result.success(list);
    }

    @Resource
    private ScheduleService scheduleService;

    @GetMapping("/schedules")
    @Operation(summary = "查询医生排班")
    public Result<List<Schedule>> getScheduleList(
            @RequestParam Long doctorId,
            @RequestParam String workDate
    ) {
        List<Schedule> list = scheduleService.listScheduleByDoctorAndDate(doctorId, workDate);
        return Result.success(list);
    }

    @Resource
    private AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "预约挂号")
    public Result<?> createAppointment(@Valid @RequestBody AppointmentDTO dto) {
        // 👇 只保留这一行打印！
        System.out.println("======== 收到创建预约请求了！路径是：/api/appointment ========");

        return appointmentService.createAppointment(dto);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消预约")
    public Result<?> cancel(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }

    @GetMapping("/my-list")
    @Operation(summary = "查询我的预约记录")
    public Result<Page<AppointmentVO>> myAppointmentList(
            @RequestParam Long patientId,
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize
    ) {
        Page<AppointmentVO> page = appointmentService.getMyAppointment(patientId, pageNum, pageSize);
        return Result.success(page);
    }
}
