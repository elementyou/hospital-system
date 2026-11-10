package com.hospital.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.admin.entity.Medicine;
import com.hospital.admin.service.MedicineService;
import com.hospital.common.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicine")
// 权限：只有管理员admin可以访问，你可以用 @PreAuthorize("hasRole('admin')")
// @PreAuthorize("hasRole('admin')")
public class MedicineController {

    @Resource
    private MedicineService medicineService;

    // 1. 分页查询药品列表（管理员查看）
    @GetMapping("/page")
    public Result<Page<Medicine>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name
    ){
        Page<Medicine> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Medicine> wrapper = new LambdaQueryWrapper<>();
        if(name != null && !name.isEmpty()){
            wrapper.like(Medicine::getName, name);
        }
        Page<Medicine> res = medicineService.page(page, wrapper);
        return Result.success(res);
    }

    // 2. 根据id查询单条
    @GetMapping("/{id}")
    public Result<Medicine> getById(@PathVariable Long id){
        Medicine medicine = medicineService.getById(id);
        return Result.success(medicine);
    }

    // 3. 新增药品
    @PostMapping("/add")
    public Result<Void> add(@RequestBody Medicine medicine){
        medicineService.save(medicine);
        return Result.success();
    }

    // 4. 修改药品
    @PutMapping("/update")
    public Result<Void> update(@RequestBody Medicine medicine){
        medicineService.updateById(medicine);
        return Result.success();
    }

    // 5. 删除（逻辑删除，mybatis-plus自动处理deleted字段）
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id){
        medicineService.removeById(id);
        return Result.success();
    }

    // 可选：查询库存预警药品（stock <= warningStock）给管理员看板用
    @GetMapping("/warning")
    public Result<List<Medicine>> getWarningMedicine(){
        LambdaQueryWrapper<Medicine> wrapper = new LambdaQueryWrapper<>();
        // 重点这一行！
        wrapper.apply("stock <= warning_stock");
        return Result.success(medicineService.list(wrapper));
    }
}
