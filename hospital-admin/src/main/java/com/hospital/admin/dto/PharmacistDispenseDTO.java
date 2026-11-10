package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "发药DTO")
public class PharmacistDispenseDTO {
    @NotNull
    private Long prescriptionId;
}
