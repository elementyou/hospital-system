package com.hospital.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.admin.dto.MedicalRecordQueryDTO;
import com.hospital.common.Result;
import com.hospital.admin.dto.MedicalRecordSaveDTO;
import com.hospital.admin.dto.MedicalRecordSubmitDTO;
import com.hospital.admin.service.MedicalRecordService;
import com.hospital.admin.vo.MedicalRecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-record")
@Tag(name = "电子病历模块")
public class MedicalRecordController {

    @Resource
    private MedicalRecordService medicalRecordService;

    @PostMapping
    @Operation(summary = "创建/保存病历")
    public Result<MedicalRecordVO> saveRecord(@Valid @RequestBody MedicalRecordSaveDTO dto) {
        MedicalRecordVO vo = medicalRecordService.saveOrUpdateRecord(dto);
        return Result.success(vo,"病历保存成功");
    }

    @PutMapping("/submit")
    @Operation(summary = "提交病历，草稿转为已完成")
    public Result<Boolean> submit(@Valid @RequestBody MedicalRecordSubmitDTO dto) {
        Boolean res = medicalRecordService.submitRecord(dto.getRecordId());
        return Result.success( res,"病历提交成功");
    }

    @Operation(summary = "查询患者就诊记录（分页）")
    @GetMapping("/patient-list")
    public Result<Page<MedicalRecordVO>> patientRecordList(MedicalRecordQueryDTO dto) {
        return medicalRecordService.listPatientRecord(dto);
    }

}
