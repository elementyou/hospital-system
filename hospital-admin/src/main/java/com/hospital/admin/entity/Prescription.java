package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime; // 👈 新增导入时间类

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("prescription")
@Schema(description = "处方主表")
public class Prescription extends BaseEntity {

    @Schema(description = "病历ID")
    private Long medicalRecordId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "患者ID")
    private Long patientId;

    @Schema(description = "处方总金额")
    private BigDecimal totalAmount;

    @Schema(description = "状态：0待发药 1已发药 2已作废")
    private Integer status;

    @Schema(description = "医嘱备注")
    private String remark;

    @Schema(description = "处方编号")
    private String prescriptionNo;

    // 👇 以下是补充的两个字段 👇
    @Schema(description = "发药药师ID")
    private Long pharmacistId;

    @Schema(description = "发药时间")
    private LocalDateTime dispenseTime;
}