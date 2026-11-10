
package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电子病历表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("medical_record")
@Schema(description = "电子病历")
public class MedicalRecord extends BaseEntity {

    @Schema(description = "预约ID")
    private Long appointmentId;

    @Schema(description = "患者ID")
    private Long patientId;

    @Schema(description = "接诊医生ID")
    private Long doctorId;

    // 👇 新增：医生姓名 👇
    @TableField(exist = false)
    @Schema(description = "接诊医生姓名")
    private String doctorName;

    @Schema(description = "主诉")
    private String chiefComplaint;

    @Schema(description = "现病史")
    private String presentIllness;

    @Schema(description = "既往史")
    private String pastHistory;

    @Schema(description = "过敏记录")
    private String allergyRecord;

    @Schema(description = "诊断")
    private String diagnosis;

    @Schema(description = "医嘱")
    private String advice;

    @Schema(description = "状态: 0草稿 1已完成")
    private Integer status;

    // 👇 新增：状态文字说明 👇
    @Schema(description = "状态文字（如待接诊/已完成）")
    @TableField(exist = false)
    private String statusText;

} // 记得保留类结尾的大括号