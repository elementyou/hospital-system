package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "处方审核DTO")
public class PrescriptionReviewDTO {
    @Schema(description = "处方id")
    private Long prescriptionId;
    @Schema(description = "审核结果：1通过，2驳回")
    private Integer reviewStatus;
    @Schema(description = "审核备注（驳回原因）")
    private String reviewRemark;
}
