package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "统计看板返回数据")
public class DashboardVO {
    @Schema(description = "总预约数")
    private Long totalAppointments;

    @Schema(description = "已完成预约数")
    private Long completedAppointments;


    @Schema(description = "取消率")
    private BigDecimal cancelRate;

    @Schema(description = "科室统计列表")
    private List<DepartmentStatVO> departmentStats;
    @Schema(description = "医生工作量统计")
    private List<DoctorWorkloadVO> doctorWorkload;
    @Schema(description = "药品消耗统计")
    private List<MedicineConsumeVO> medicineConsumption;


    @Data
    @Schema(description = "科室统计")
    public static class DepartmentStatVO {
        private String departmentName;
        private Integer appointmentCount;
        private Integer doctorCount;
    }

    @Data
    @Schema(description = "医生工作量")
    public static class DoctorWorkloadVO {
        private String doctorName;
        private Integer consultCount;
        private Integer prescriptionCount;
    }

    @Data
    @Schema(description = "药品消耗")
    public static class MedicineConsumeVO {
        private String medicineName;
        private Integer consumeCount;
        private BigDecimal consumeAmount;
    }
}
