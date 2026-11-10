
package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 患者表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("patient")
@Schema(description = "患者信息")
public class Patient extends BaseEntity {

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别: 1男 2女")
    private Integer gender;

    @Schema(description = "出生日期")
    private LocalDate birthday;

    @Schema(description = "过敏史")
    private String allergyInfo;

    @Schema(description = "状态: 1启用 0禁用")
    private Integer status;
}