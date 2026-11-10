package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("medicine_stock_log")
@Schema(description = "药品库存变动日志")
public class MedicineStockLog extends BaseEntity {
    @Schema(description = "药品ID")
    private Long medicineId;
    @Schema(description = "调整类型:in入库/out出库/check盘点")
    private String adjustType;
    @Schema(description = "调整前库存")
    private Integer previousStock;
    @Schema(description = "调整数量")
    private Integer adjustQuantity;
    @Schema(description = "调整后库存")
    private Integer currentStock;
    @Schema(description = "备注原因")
    private String remark;
}
