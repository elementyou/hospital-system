package com.hospital.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.admin.entity.Doctor;
import com.hospital.admin.service.DoctorService;
import com.hospital.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@Tag(name = "医生管理接口")
public class DoctorController {

    @Resource
    private DoctorService doctorService;

    @GetMapping("/page")
    @Operation(summary = "分页查询医生")
    public Result<Page<Doctor>> page(
            @RequestParam Long pageNum,
            @RequestParam Long pageSize,
            @RequestParam(required = false) String name
    ){
        Page<Doctor> page = doctorService.getDoctorPage(pageNum, pageSize, name);
        return Result.success(page);
    }

    @GetMapping("/listAll")
    @Operation(summary = "全部医生下拉列表")
    public Result<List<Doctor>> listAll(){
        List<Doctor> list = doctorService.list();
        return Result.success(list);
    }

    @PostMapping("/add")
    @Operation(summary = "新增医生")
    public Result<String> add(@RequestBody Doctor doctor){
        doctorService.save(doctor);
        return Result.success("ok");
    }

    @PutMapping("/update")
    @Operation(summary = "修改医生")
    public Result<String> update(@RequestBody Doctor doctor){
        doctorService.updateById(doctor);
        return Result.success("ok");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除医生（逻辑删除）")
    public Result<String> delete(@PathVariable Long id){
        doctorService.removeById(id);
        return Result.success("ok");
    }
}
