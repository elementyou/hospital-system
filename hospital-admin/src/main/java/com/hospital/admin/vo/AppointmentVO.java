package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "我的预约返回VO")
public class AppointmentVO {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private Long scheduleId;
    private LocalDate visitDate;
    private String timeSlot;
    private Integer status;
    private LocalDateTime createdAt;
}
