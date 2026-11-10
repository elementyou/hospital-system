package com.hospital.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hospital.admin.dto.PrescriptionCreateDTO;
import com.hospital.admin.dto.PrescriptionDispenseDTO;
import com.hospital.admin.entity.*;
import com.hospital.admin.mapper.*;
import com.hospital.admin.service.PrescriptionService;
import com.hospital.admin.vo.PrescriptionDetailVO;
import com.hospital.common.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Resource
    private PrescriptionMapper prescriptionMapper;
    @Resource
    private PrescriptionItemMapper prescriptionItemMapper;
    @Resource
    private MedicineMapper medicineMapper;
    @Resource
    private PatientMapper patientMapper;

    @Resource
    private DoctorMapper doctorMapper;


    // ========== 1、开具处方（之前调通的） ==========
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> createPrescription(PrescriptionCreateDTO dto) {
        Prescription prescription = new Prescription();
        prescription.setMedicalRecordId(dto.getMedicalRecordId());
        prescription.setPatientId(dto.getPatientId());
        prescription.setDoctorId(dto.getDoctorId());
        prescription.setPrescriptionNo("CF" + System.currentTimeMillis());
        prescription.setStatus(0); // 0 待审核
        prescription.setCreatedAt(LocalDateTime.now());
        prescription.setUpdatedAt(LocalDateTime.now());
        prescription.setDeleted(0);
        prescriptionMapper.insert(prescription);

        List<PrescriptionItem> itemList = dto.getPrescriptionItems().stream().map(itemDto -> {
            Medicine medicine = medicineMapper.selectById(itemDto.getMedicineId());
            PrescriptionItem item = new PrescriptionItem();
            item.setPrescriptionId(prescription.getId());
            item.setMedicineId(medicine.getId());
            item.setMedicineName(medicine.getName());
            item.setDosage(itemDto.getDosage());
            item.setUsage(itemDto.getUsage());
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(medicine.getUnitPrice());
            item.setAmount(medicine.getUnitPrice().multiply(new BigDecimal(itemDto.getQuantity())));
            item.setCreatedAt(LocalDateTime.now());
            item.setUpdatedAt(LocalDateTime.now());
            item.setDeleted(0);
            return item;
        }).toList();

// 只在这里定义一次 totalAmount
        BigDecimal totalAmount = itemList.stream()
                .map(PrescriptionItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        // 批量插入明细
        for (PrescriptionItem item : itemList) {
            prescriptionItemMapper.insert(item);
        }
        // 更新总金额
        prescription.setTotalAmount(totalAmount);
        prescriptionMapper.updateById(prescription);

        return Result.success(prescription,"处方开具成功");
    }

    // ========== 3、处方审核（新增） ==========
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> audit(Long prescriptionId) {
        Prescription prescription = prescriptionMapper.selectById(prescriptionId);

        if (prescription == null || prescription.getDeleted() == 1) {
            throw new RuntimeException("处方不存在");
        }

        if (!prescription.getStatus().equals(0)) {
            throw new RuntimeException("处方状态不是待审核，无法审核");
        }

        prescription.setStatus(1); // 审核通过：待发药
        prescription.setUpdatedAt(LocalDateTime.now());
        prescriptionMapper.updateById(prescription);

        return Result.success(prescription,"审核通过，待发药");
    }



    // ========== 2、处方发药（新增） ==========
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> dispense(PrescriptionDispenseDTO dto) {
        Long prescriptionId = dto.getPrescriptionId();

        // 1. 查询处方
        Prescription prescription = prescriptionMapper.selectById(prescriptionId);
        if (prescription == null || prescription.getDeleted() == 1) {
            throw new RuntimeException("处方不存在");
        }
        // 状态判断：必须是待发药（1）
        if (!prescription.getStatus().equals(1)) {
            throw new RuntimeException("处方状态不是待发药，无法发药");
        }

        // 2. 查询处方明细
        LambdaQueryWrapper<PrescriptionItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(PrescriptionItem::getPrescriptionId, prescriptionId)
                .eq(PrescriptionItem::getDeleted,0);
        List<PrescriptionItem> itemList = prescriptionItemMapper.selectList(itemWrapper);
        if(itemList.isEmpty()){
            throw new RuntimeException("处方无药品明细");
        }

        //3. 循环扣库存
        for (PrescriptionItem item : itemList) {
            Medicine medicine = medicineMapper.selectById(item.getMedicineId());
            if(medicine == null || medicine.getDeleted() ==1){
                throw new RuntimeException("药品【"+item.getMedicineName()+"】不存在");
            }
            // 判断库存
            if(medicine.getStock() < item.getQuantity()){
                throw new RuntimeException("药品【"+medicine.getName()+"】库存不足！剩余："+medicine.getStock());
            }
            // 扣减库存
            medicine.setStock(medicine.getStock() - item.getQuantity());
            medicineMapper.updateById(medicine);
        }

        //4. 修改处方状态为已发药 2
        prescription.setStatus(2);
        prescription.setUpdatedAt(LocalDateTime.now());
        prescriptionMapper.updateById(prescription);
        return Result.success(null, "发药成功");
    }

    @Override
    public Result<PrescriptionDetailVO> getDetailById(Long id) {
        // 1. 查询处方主表
        Prescription prescription = prescriptionMapper.selectById(id);
        if (prescription == null || prescription.getDeleted() == 1) {
            throw new RuntimeException("处方不存在");
        }

        // 2. 查询处方明细
        LambdaQueryWrapper<PrescriptionItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(PrescriptionItem::getPrescriptionId, id)
                .eq(PrescriptionItem::getDeleted, 0);
        List<PrescriptionItem> itemList = prescriptionItemMapper.selectList(itemWrapper);

        // 3. 查询患者、医生
        Patient patient = patientMapper.selectById(prescription.getPatientId());
        Doctor doctor = doctorMapper.selectById(prescription.getDoctorId());

        // 4. 组装明细items（连带药品名称）
        List<PrescriptionDetailVO.PrescriptionItemVO> itemVOList = itemList.stream().map(item -> {
            PrescriptionDetailVO.PrescriptionItemVO itemVO = new PrescriptionDetailVO.PrescriptionItemVO();
            itemVO.setId(item.getId());
            itemVO.setMedicineId(item.getMedicineId());
            // 查询药品
            Medicine medicine = medicineMapper.selectById(item.getMedicineId());
            if(medicine != null){
                itemVO.setMedicineName(medicine.getName());
            }
            itemVO.setDosage(item.getDosage());
            itemVO.setUsage(item.getUsage());
            itemVO.setQuantity(item.getQuantity());
            itemVO.setUnitPrice(item.getUnitPrice());
            itemVO.setAmount(item.getAmount());
            return itemVO;
        }).toList();

        // 5. 状态文本映射
        String statusText = switch (prescription.getStatus()) {
            case 0 -> "待审核";
            case 1 -> "待发药";
            case 2 -> "已发药";
            default -> "未知状态";
        };

        // 6. 封装VO
        PrescriptionDetailVO vo = new PrescriptionDetailVO();
        vo.setId(prescription.getId());
        vo.setMedicalRecordId(prescription.getMedicalRecordId());
        vo.setPatientId(prescription.getPatientId());
        vo.setPatientName(patient == null ? "" : patient.getName());
        vo.setDoctorId(prescription.getDoctorId());
        vo.setDoctorName(doctor == null ? "" : doctor.getName());
        vo.setPrescriptionNo(prescription.getPrescriptionNo());
        vo.setStatus(prescription.getStatus());
        vo.setStatusText(statusText);
        vo.setTotalAmount(prescription.getTotalAmount());
        vo.setPharmacistId(prescription.getPharmacistId());
        vo.setDispenseTime(prescription.getDispenseTime());
        vo.setCreatedAt(prescription.getCreatedAt());
        vo.setUpdatedAt(prescription.getUpdatedAt());
        vo.setItems(itemVOList);

        return Result.success(vo);
    }

}
