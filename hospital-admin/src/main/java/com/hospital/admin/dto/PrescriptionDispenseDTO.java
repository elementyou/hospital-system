package com.hospital.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescriptionDispenseDTO {
    @NotNull(message = "处方id不能为空")
    private Long prescriptionId;
}
