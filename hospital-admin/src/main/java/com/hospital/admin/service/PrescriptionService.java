package com.hospital.admin.service;

import com.hospital.admin.dto.PrescriptionCreateDTO;
import com.hospital.admin.dto.PrescriptionDispenseDTO;
import com.hospital.admin.vo.PrescriptionDetailVO;
import com.hospital.common.Result;

public interface PrescriptionService {

    Result<?> createPrescription(PrescriptionCreateDTO dto);

    Result<?> audit(Long prescriptionId);

    Result<?> dispense(PrescriptionDispenseDTO dto);

    Result<PrescriptionDetailVO> getDetailById(Long id);

}
