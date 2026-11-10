
package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 医生表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("doctor")
@Schema(description = "医生信息")
public class Doctor extends BaseEntity {

    @Schema(description = "医生姓名")
    private String name;

    @Schema(description = "所属科室ID")
    private Long departmentId;

    @Schema(description = "职称: 主任医师/副主任医师/主治医师")
    private String title;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "医生简介")
    private String introduction;

    @Schema(description = "状态: 1在岗 0停用")
    private Integer status;
}