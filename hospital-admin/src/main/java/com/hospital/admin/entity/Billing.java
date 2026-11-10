package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("billing")
@Schema(description = "费用账单（发药自动生成，无对外接口）")
public class Billing extends BaseEntity {

    @Schema(description = "患者ID")
    private Long patientId;

    @Schema(description = "处方ID")
    private Long prescriptionId;

    @Schema(description = "账单总金额")
    private BigDecimal amount;

    @Schema(description = "账单状态：1 已结算")
    private Integer status;
}
