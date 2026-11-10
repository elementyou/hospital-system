package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "接诊/叫号入参")
public class AppointmentIdDTO {
    @NotNull(message = "预约id不能为空")
    @Schema(description = "预约id")
    private Long appointmentId;
}
