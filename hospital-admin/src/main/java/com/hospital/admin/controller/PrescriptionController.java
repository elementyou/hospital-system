package com.hospital.admin.controller;

import com.hospital.admin.dto.PrescriptionCreateDTO;
import com.hospital.admin.dto.PrescriptionDispenseDTO;
import com.hospital.admin.service.PrescriptionService;
import com.hospital.admin.vo.PrescriptionDetailVO;
import com.hospital.admin.vo.PrescriptionVO;
import com.hospital.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescription")
@Tag(name = "处方管理")
public class PrescriptionController {

    @Resource
    private PrescriptionService prescriptionService;

    /**
     * 开具处方
     */
    @PostMapping
    @Operation(summary = "开具处方")
    public Result<PrescriptionVO> create(@Valid @RequestBody PrescriptionCreateDTO dto) {
        Result<?> result = prescriptionService.createPrescription(dto);
        PrescriptionVO vo = (PrescriptionVO) result.getData();
        return Result.success(vo);
    }

    /**
     * 处方发药
     * ✅ 和ServiceImpl匹配：接收RequestBody DTO
     */
    @PostMapping("/dispense")
    @Operation(summary = "处方发药")
    public Result<String> dispense(@Valid @RequestBody PrescriptionDispenseDTO dto) {
        prescriptionService.dispense(dto);
        return Result.success("发药成功");
    }

    /**
     * 处方审核
     * ✅ @RequestParam 传prescriptionId，和service.audit(Long)匹配
     */
    @Operation(summary = "处方审核")
    @PostMapping("/audit")
    public Result<?> audit(@RequestParam Long prescriptionId) {
        return prescriptionService.audit(prescriptionId);
    }

    /**
     * 查询处方详情
     */
    @Operation(summary = "查询处方详情")
    @GetMapping("/{id}")
    public Result<PrescriptionDetailVO> getDetail(@PathVariable Long id) {
        return prescriptionService.getDetailById(id);
    }

}
