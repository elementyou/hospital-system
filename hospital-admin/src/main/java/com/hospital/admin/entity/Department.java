
package com.hospital.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 科室表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("department")
@Schema(description = "科室信息")
public class Department extends BaseEntity {

    @Schema(description = "科室名称")
    private String name;

    @Schema(description = "科室编码")
    private String code;

    @Schema(description = "科室说明")
    private String description;

    @Schema(description = "状态: 1启用 0禁用")
    private Integer status;
}