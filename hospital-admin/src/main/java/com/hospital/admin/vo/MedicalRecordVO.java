package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "病历返回VO")
public class MedicalRecordVO {
    private Long id;
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;

    // 👇 加上这一行 👇
    private String doctorName;

    private String chiefComplaint;
    private String diagnosis;
    private Integer status;
    private String statusText;
    private LocalDateTime createdAt;
}
