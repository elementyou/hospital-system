package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "药师待审核处方VO")
public class PharmacistPrescriptionVO {
    private Long id;
    private String prescriptionNo;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private Integer itemCount;
    private BigDecimal totalAmount;
    private Integer status;
    private String statusText;
    private LocalDateTime createdAt;
}
