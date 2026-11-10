package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
/*审核处方*/
@Data
@Schema(description = "药师审核处方DTO")
public class PharmacistReviewDTO {
    @NotNull(message = "处方id不能为空")
    private Long prescriptionId;
    @NotBlank(message = "审核结果不能为空")
    private String result; // approve / reject
    @Schema(description = "审核意见，最长200")
    private String remark;
}

