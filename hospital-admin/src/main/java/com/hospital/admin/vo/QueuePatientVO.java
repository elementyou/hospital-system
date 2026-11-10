package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "候诊患者详情VO")
public class QueuePatientVO {
    @Schema(description = "预约id")
    private Long id;
    @Schema(description = "患者id")
    private Long patientId;
    @Schema(description = "患者姓名")
    private String patientName;
    @Schema(description = "性别 1男 2女")
    private Integer gender;
    @Schema(description = "年龄")
    private Integer age;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "时段：上午/下午")
    private String timeSlot;
    @Schema(description = "排队号")
    private Integer queueNumber;
    @Schema(description = "预约状态 0待就诊 1已接诊")
    private Integer status;
    @Schema(description = "状态文字")
    private String statusText;
    @Schema(description = "过敏信息")
    private String allergyInfo;
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
