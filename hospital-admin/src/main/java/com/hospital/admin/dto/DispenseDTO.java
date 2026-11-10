package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "处方发药DTO")
public class DispenseDTO {
    @Schema(description = "处方id")
    private Long prescriptionId;
    @Schema(description = "发药人id（药师id）")
    private Long pharmacistId;
}
