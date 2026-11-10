package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "简易候诊队列VO")
public class SimpleQueueVO {
    @Schema(description = "预约id")
    private Long id;
    @Schema(description = "患者id")
    private Long patientId;
    @Schema(description = "患者姓名")
    private String patientName;
    @Schema(description = "排队号")
    private Integer queueNumber;
    @Schema(description = "预约状态")
    private Integer status;
    @Schema(description = "状态文字")
    private String statusText;
}
