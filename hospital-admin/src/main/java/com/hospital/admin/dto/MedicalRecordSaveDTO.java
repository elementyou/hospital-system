package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "保存电子病历DTO")
public class MedicalRecordSaveDTO {
    @NotNull(message = "预约ID不能为空")
    private Long appointmentId;

    @NotBlank(message = "主诉不能为空")
    private String chiefComplaint;

    private String presentIllness;
    private String pastHistory;
    private String allergyRecord;

    @NotBlank(message = "诊断不能为空")
    private String diagnosis;

    private String advice;
}
