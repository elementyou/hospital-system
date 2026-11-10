package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "提交病历DTO")
public class MedicalRecordSubmitDTO {
    @NotNull(message = "病历ID不能为空")
    private Long recordId;
}
