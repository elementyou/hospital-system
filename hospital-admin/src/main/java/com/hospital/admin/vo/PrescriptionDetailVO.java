package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "处方详情VO")
public class PrescriptionDetailVO {
    private Long id;
    private Long medicalRecordId;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String prescriptionNo;
    private Integer status;
    private String statusText;
    private BigDecimal totalAmount;
    private Long pharmacistId;
    private LocalDateTime dispenseTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PrescriptionItemVO> items;

    @Data
    @Schema(description = "处方明细VO")
    public static class PrescriptionItemVO {
        private Long id;
        private Long medicineId;
        private String medicineName;
        private String dosage;
        private String usage;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
    }
}
