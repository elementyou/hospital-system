package com.hospital.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.admin.entity.Department;
import com.hospital.admin.service.DepartmentService;
import com.hospital.admin.vo.DashboardVO;
import com.hospital.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@Tag(name = "科室管理")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    // 分页查询（后台管理表格，查全部，包含禁用）
    @GetMapping("/page")
    @Operation(summary = "分页查询科室")
    public Result<Page<Department>> page(@RequestParam Long pageNum,
                                         @RequestParam Long pageSize,
                                         String name) {
        Page<Department> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        if(name != null && !name.isEmpty()){
            wrapper.like(Department::getName, name);
        }
        return Result.success(departmentService.page(page, wrapper));
    }

    @PostMapping
    public Result<Boolean> add(@RequestBody Department department){
        return Result.success(departmentService.save(department));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Department department){
        return Result.success(departmentService.updateById(department));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id){
        return Result.success(departmentService.removeById(id));
    }

    @GetMapping("/enable")
    public Result<List<Department>> enableList(){
        return Result.success(departmentService.listEnableDepartment());
    }


    // 看板统计
    @GetMapping("/stat")
    @Operation(summary = "科室统计")
    public List<DashboardVO.DepartmentStatVO> stat(@RequestParam String startDate, @RequestParam String endDate){
        return departmentService.statDepartment(startDate, endDate);
    }
}
