package com.hospital.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.admin.entity.Schedule;
import com.hospital.admin.service.ScheduleService;
import com.hospital.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@Tag(name = "排班管理接口")
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    @GetMapping("/page")
    @Operation(summary = "排班分页查询（带医生姓名）")
    public Result<Page<Schedule>> page(
            @RequestParam Long pageNum,
            @RequestParam Long pageSize,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) String workDate
    ){
        Page<Schedule> page = scheduleService.getSchedulePage(pageNum, pageSize, doctorId, workDate);
        return Result.success(page);
    }

    @PostMapping("/add")
    @Operation(summary = "新增排班")
    public Result<String> add(@RequestBody Schedule schedule){
        scheduleService.save(schedule);
        return Result.success("ok");
    }

    @PutMapping("/update")
    @Operation(summary = "修改排班")
    public Result<String> update(@RequestBody Schedule schedule){
        scheduleService.updateById(schedule);
        return Result.success("ok");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除排班（逻辑删除）")
    public Result<String> delete(@PathVariable Long id){
        scheduleService.removeById(id);
        return Result.success("ok");
    }
}
