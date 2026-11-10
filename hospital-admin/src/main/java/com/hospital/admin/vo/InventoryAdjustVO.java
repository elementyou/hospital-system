package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "库存调整返回VO")
public class InventoryAdjustVO {
    private Long medicineId;
    private Integer previousStock;
    private Integer adjustQuantity;
    private Integer currentStock;
}
