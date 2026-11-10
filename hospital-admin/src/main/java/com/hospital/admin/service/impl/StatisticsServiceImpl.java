package com.hospital.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hospital.admin.entity.Appointment;
import com.hospital.admin.entity.Department;
import com.hospital.admin.entity.Doctor;
import com.hospital.admin.entity.PrescriptionItem;
import com.hospital.admin.mapper.AppointmentMapper;
import com.hospital.admin.mapper.DepartmentMapper;
import com.hospital.admin.mapper.DoctorMapper;
import com.hospital.admin.mapper.PrescriptionItemMapper;
import com.hospital.admin.service.StatisticsService;
import com.hospital.admin.vo.DashboardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final AppointmentMapper appointmentMapper;
    private final DepartmentMapper departmentMapper;
    private final DoctorMapper doctorMapper;
    private final PrescriptionItemMapper prescriptionItemMapper;

    @Override
    public DashboardVO getDashboard(String startDate, String endDate) {
        DashboardVO vo = new DashboardVO();

        // 1. 总预约数
        Long totalAppointments = appointmentMapper.selectCount(
                Wrappers.lambdaQuery(Appointment.class)
                        .apply("DATE(created_at) BETWEEN {0} AND {1}", startDate, endDate)
                        .eq(Appointment::getDeleted, 0)
        );
// 已完成预约：status=1 已接诊
        Long completedAppointments = appointmentMapper.selectCount(
                Wrappers.lambdaQuery(Appointment.class)
                        .apply("DATE(created_at) BETWEEN {0} AND {1}", startDate, endDate)
                        .eq(Appointment::getStatus, 1)
                        .eq(Appointment::getDeleted, 0)
        );
// 取消预约：status=2 已取消
        Long cancelCount = appointmentMapper.selectCount(
                Wrappers.lambdaQuery(Appointment.class)
                        .apply("DATE(created_at) BETWEEN {0} AND {1}", startDate, endDate)
                        .eq(Appointment::getStatus, 2)
                        .eq(Appointment::getDeleted, 0)
        );

// 取消率
        BigDecimal cancelRate;
        if (totalAppointments == null || totalAppointments <= 0) {
            cancelRate = BigDecimal.ZERO;
        } else {
            cancelRate = new BigDecimal(cancelCount)
                    .divide(new BigDecimal(totalAppointments), 3, RoundingMode.HALF_UP);
        }
        vo.setTotalAppointments(totalAppointments);
        vo.setCompletedAppointments(completedAppointments);
        vo.setCancelRate(cancelRate);


        // 2. 科室统计
        List<DashboardVO.DepartmentStatVO> deptStatList = departmentMapper.statDepartment(startDate, endDate);
        vo.setDepartmentStats(deptStatList);

        //3. 医生工作量
        List<DashboardVO.DoctorWorkloadVO> doctorWorkList = doctorMapper.statDoctorWork(startDate, endDate);
        vo.setDoctorWorkload(doctorWorkList);

        //4. 药品消耗
        List<DashboardVO.MedicineConsumeVO> medicineList = prescriptionItemMapper.statMedicineConsume(startDate, endDate);
        vo.setMedicineConsumption(medicineList);

        return vo;
    }
}
