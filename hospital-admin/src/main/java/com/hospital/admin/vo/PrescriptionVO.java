package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "处方VO")
public class PrescriptionVO {
    private Long id;
    private Long medicalRecordId;
    private Long patientId;
    private Long doctorId;
    private String prescriptionNo;
    private Integer status;
    private String statusText;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<PrescriptionItemVO> items;

    @Data
    @Schema(description = "处方明细VO")
    public static class PrescriptionItemVO {
        private String medicineName;
        private String dosage;
        private String usage;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
    }
}
