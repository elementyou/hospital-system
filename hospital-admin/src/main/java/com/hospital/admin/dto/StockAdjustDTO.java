package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "药品库存调整DTO")
public class StockAdjustDTO {
    @Schema(description = "药品id")
    private Long medicineId;
    @Schema(description = "调整数量：正数入库、负数出库")
    private Integer adjustNum;
    @Schema(description = "调整备注")
    private String remark;
}
