package com.hospital.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.admin.dto.InventoryAdjustDTO;
import com.hospital.admin.dto.PharmacistDispenseDTO;
import com.hospital.admin.dto.PharmacistReviewDTO;

import com.hospital.admin.service.PharmacistService;
import com.hospital.admin.vo.InventoryAdjustVO;
import com.hospital.admin.vo.LowStockVO;
import com.hospital.admin.vo.PharmacistPrescriptionVO;
import com.hospital.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pharmacist")
@Tag(name = "药房药师模块")
public class PharmacistController {
    @Resource
    private PharmacistService pharmacistService;

    @Operation(summary = "4.1 待审核处方列表")
    @GetMapping("/pending-list")
    public Result<Page<PharmacistPrescriptionVO>> pendingList(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize
    ){
        return pharmacistService.pendingList(pageNum, pageSize);
    }

    @Operation(summary = "4.2 药师审核处方")
    @PutMapping("/review")
    public Result<Boolean> review(@Valid @RequestBody PharmacistReviewDTO dto){
        return pharmacistService.review(dto);
    }

    @Operation(summary = "4.3 发药")
    @PutMapping("/dispense")
    public Result<Boolean> dispense(@Valid @RequestBody PharmacistDispenseDTO dto){
        return pharmacistService.dispense(dto);
    }

    @Operation(summary = "4.4 库存预警列表")
    @GetMapping("/low-stock")
    public Result<List<LowStockVO>> lowStock(){
        return pharmacistService.lowStockList();
    }

    @Operation(summary = "4.5 库存调整")
    @PutMapping("/inventory-adjust")
    public Result<InventoryAdjustVO> inventoryAdjust(@Valid @RequestBody InventoryAdjustDTO dto){
        return pharmacistService.inventoryAdjust(dto);
    }
}
