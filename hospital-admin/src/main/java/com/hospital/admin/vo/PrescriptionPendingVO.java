package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "待审核处方分页VO")
public class PrescriptionPendingVO {
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
