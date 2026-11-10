package com.hospital.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.admin.dto.MedicalRecordQueryDTO;
import com.hospital.admin.dto.MedicalRecordSaveDTO;
import com.hospital.admin.entity.MedicalRecord;
import com.hospital.admin.vo.MedicalRecordVO;
import com.hospital.common.Result;

public interface MedicalRecordService extends IService<MedicalRecord> {
    MedicalRecordVO saveOrUpdateRecord(MedicalRecordSaveDTO dto);
    Boolean submitRecord(Long recordId);

    Result<Page<MedicalRecordVO>> listPatientRecord(MedicalRecordQueryDTO dto);

}
