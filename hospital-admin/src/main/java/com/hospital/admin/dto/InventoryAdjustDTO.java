package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "药品库存调整DTO")
public class InventoryAdjustDTO {
    @NotNull
    private Long medicineId;
    @NotBlank
    private String adjustType; // in / out / check
    @NotNull
    private Integer adjustQuantity;
    private String remark;
}

