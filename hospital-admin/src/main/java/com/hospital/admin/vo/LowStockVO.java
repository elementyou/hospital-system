package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "库存预警药品VO")
public class LowStockVO {
    private Long id;
    private String name;
    private String code;
    private String specification;
    private Integer stock;
    private Integer warningStock;
    private Integer status;
}
