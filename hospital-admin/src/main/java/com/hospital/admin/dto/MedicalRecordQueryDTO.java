package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "患者就诊记录查询条件")
public class MedicalRecordQueryDTO {
    @Schema(description = "患者ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long patientId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "开始日期 yyyy-MM-dd")
    private String startDate;

    @Schema(description = "结束日期 yyyy-MM-dd")
    private String endDate;

    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页条数")
    private Integer pageSize = 10;
}
