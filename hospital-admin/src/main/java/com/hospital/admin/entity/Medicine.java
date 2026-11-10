package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("medicine")
@Schema(description = "药品表")
public class Medicine extends BaseEntity {

    @Schema(description = "药品名称")
    private String name;

    @Schema(description = "药品编码")
    private String code;

    @Schema(description = "规格")
    private String specification;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "单价")
    private BigDecimal unitPrice;

    @Schema(description = "当前库存")
    private Integer stock;

    @Schema(description = "预警库存阈值")
    private Integer warningStock;

    @Schema(description = "有效期")
    private LocalDate expiryDate;

    @Schema(description = "状态：1启用 0停用")
    private Integer status;
}
