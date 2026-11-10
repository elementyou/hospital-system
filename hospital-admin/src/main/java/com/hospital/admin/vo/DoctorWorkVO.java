package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "医生工作量统计VO")
public class DoctorWorkVO {
    @Schema(description = "医生姓名")
    private String doctorName;
    @Schema(description = "接诊数量")
    private Long consultCount;
    @Schema(description = "处方数量")
    private Long prescriptionCount;
}

