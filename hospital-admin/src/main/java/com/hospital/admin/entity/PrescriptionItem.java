package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("prescription_item")
@Schema(description = "处方药品明细")
public class PrescriptionItem extends BaseEntity {

    @Schema(description = "处方ID")
    private Long prescriptionId;

    @Schema(description = "药品ID")
    private Long medicineId;

    @Schema(description = "药品名称(冗余)")
    private String medicineName;

    // 将 price 改为 unitPrice
    @Schema(description = "单价")
    private BigDecimal unitPrice;

    // 将 num 改为 quantity
    @Schema(description = "数量")
    private Integer quantity;

    // 将 subTotal 改为 amount
    @Schema(description = "小计金额")
    private BigDecimal amount;

    // 补充缺失的 usage
    @Schema(description = "用法用量")
    @TableField("`usage`")
    private String usage;

    // 补充缺失的 dosage
    @Schema(description = "剂量")
    private String dosage;
}