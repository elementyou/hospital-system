package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "开处方DTO")
public class PrescriptionCreateDTO {

    @NotNull(message = "病历ID不能为空")
    @Schema(description = "病历ID")
    private Long medicalRecordId;

    @NotNull(message = "患者id不能为空")
    private Long patientId;

    @NotNull(message = "医生id不能为空")
    private Long doctorId;


    @Valid
    @NotEmpty(message = "处方明细不能为空")
    @Schema(description = "处方明细列表")
    private List<PrescriptionItemDTO> prescriptionItems;

    @Data
    @Schema(description = "处方明细行入参")
    public static class PrescriptionItemDTO {
        @NotNull(message = "药品ID不能为空")
        private Long medicineId;
        private String dosage;
        private String usage;
        @NotNull(message = "数量不能为空")
        private Integer quantity;
    }
}
