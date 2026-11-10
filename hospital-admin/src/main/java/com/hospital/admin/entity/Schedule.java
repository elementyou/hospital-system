package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("doctor_schedule")
@Schema(description = "医生排班号源")
public class Schedule extends BaseEntity {

    @Schema(description = "医生ID")
    private Long doctorId;

    @Schema(description = "出诊日期")
    private LocalDate workDate;

    @Schema(description = "时段：上午/下午")
    private String timeSlot;

    @Schema(description = "总号源")
    private Integer totalCount;

    @Schema(description = "剩余可预约号源")
    private Integer remainCount;

    @Schema(description = "状态 1正常 0停诊")
    private Integer status;

    // 联查返回，数据库不存在
    @TableField(exist = false)
    @Schema(description = "医生姓名")
    private String doctorName;
}
