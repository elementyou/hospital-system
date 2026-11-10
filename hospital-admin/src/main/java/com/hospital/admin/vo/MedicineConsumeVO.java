package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "药品消耗统计")
public class MedicineConsumeVO {
    private String medicineName;
    private Long consumeCount;
    private java.math.BigDecimal consumeAmount;
}
