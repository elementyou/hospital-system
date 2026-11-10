package com.hospital.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "看板统计查询DTO")
public class DashboardDTO {
    @NotBlank(message = "开始日期不能为空")
    @Schema(description = "开始日期 yyyy-MM-dd")
    private String startDate;

    @NotBlank(message = "结束日期不能为空")
    @Schema(description = "结束日期 yyyy-MM-dd")
    private String endDate;
}
