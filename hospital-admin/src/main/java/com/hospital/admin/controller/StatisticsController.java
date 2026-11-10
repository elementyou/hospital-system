package com.hospital.admin.controller;

import com.hospital.admin.dto.DashboardDTO;
import com.hospital.admin.service.StatisticsService;
import com.hospital.admin.vo.DashboardVO;
import com.hospital.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@Tag(name = "统计看板模块")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    @Operation(summary = "获取看板统计数据")
    public Result<DashboardVO> dashboard(@Valid DashboardDTO dto){
        DashboardVO vo = statisticsService.getDashboard(dto.getStartDate(), dto.getEndDate());
        return Result.success(vo);
    }
}
