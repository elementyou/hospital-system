package com.hospital.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.admin.dto.InventoryAdjustDTO;
import com.hospital.admin.dto.PharmacistDispenseDTO;
import com.hospital.admin.dto.PharmacistReviewDTO;

import com.hospital.admin.vo.InventoryAdjustVO;
import com.hospital.admin.vo.LowStockVO;
import com.hospital.admin.vo.PharmacistPrescriptionVO;
import com.hospital.common.Result;

import java.util.List;

public interface PharmacistService {
    Result<Page<PharmacistPrescriptionVO>> pendingList(Integer pageNum, Integer pageSize);
    Result<Boolean> review(PharmacistReviewDTO dto);
    Result<Boolean> dispense(PharmacistDispenseDTO dto);
    Result<List<LowStockVO>> lowStockList();
    Result<InventoryAdjustVO> inventoryAdjust(InventoryAdjustDTO dto);
}
