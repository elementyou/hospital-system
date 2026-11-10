package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "预约挂号请求")
public class AppointmentDTO {
    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

    @NotNull(message = "排班ID不能为空")
    private Long scheduleId;

    @NotNull(message = "患者ID不能为空")
    private Long patientId;
}
