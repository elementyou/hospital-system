
package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预约表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("appointment")
@Schema(description = "预约信息")
public class Appointment extends BaseEntity {

    @Schema(description = "患者ID")
    private Long patientId;

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "排班ID")
    private Long scheduleId;

    @Schema(description = "就诊日期")
    private LocalDate visitDate;

    @Schema(description = "就诊时段")
    private String timeSlot;

    @Schema(description = "排队号")
    private Integer queueNumber;

    @Schema(description = "状态: 0待就诊 1已接诊 2已取消 3爽约")
    private Integer status;

    @Schema(description = "取消原因")
    private String cancelReason;
}