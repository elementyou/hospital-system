package com.hospital.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.admin.dto.InventoryAdjustDTO;
import com.hospital.admin.dto.PharmacistDispenseDTO;
import com.hospital.admin.dto.PharmacistReviewDTO;
import com.hospital.admin.entity.*;
import com.hospital.admin.mapper.*;

import com.hospital.admin.service.PharmacistService;
import com.hospital.admin.vo.InventoryAdjustVO;
import com.hospital.admin.vo.LowStockVO;
import com.hospital.admin.vo.PharmacistPrescriptionVO;
import com.hospital.common.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PharmacistServiceImpl implements PharmacistService {

    @Resource
    private PrescriptionMapper prescriptionMapper;
    @Resource
    private PrescriptionItemMapper prescriptionItemMapper;
    @Resource
    private PatientMapper patientMapper;
    @Resource
    private DoctorMapper doctorMapper;
    @Resource
    private MedicineMapper medicineMapper;
    @Resource
    private BillingMapper billingMapper;

    // 4.1 待审核处方列表 status=0
    @Override
    public Result<Page<PharmacistPrescriptionVO>> pendingList(Integer pageNum, Integer pageSize) {
        Page<Prescription> page = new Page<>(pageNum == null ? 1 : pageNum, pageSize == null ? 10 : pageSize);
        LambdaQueryWrapper<Prescription> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Prescription::getStatus, 0)
                .eq(Prescription::getDeleted, 0)
                .orderByDesc(Prescription::getCreatedAt);
        Page<Prescription> prescriptionPage = prescriptionMapper.selectPage(page, wrapper);

        List<PharmacistPrescriptionVO> voList = prescriptionPage.getRecords().stream().map(pres -> {
            PharmacistPrescriptionVO vo = new PharmacistPrescriptionVO();
            vo.setId(pres.getId());
            vo.setPrescriptionNo(pres.getPrescriptionNo());
            vo.setPatientId(pres.getPatientId());
            Patient patient = patientMapper.selectById(pres.getPatientId());
            vo.setPatientName(patient == null ? "" : patient.getName());
            vo.setDoctorId(pres.getDoctorId());
            Doctor doctor = doctorMapper.selectById(pres.getDoctorId());
            vo.setDoctorName(doctor == null ? "" : doctor.getName());

            // 统计明细条数
            LambdaQueryWrapper<PrescriptionItem> itemW = new LambdaQueryWrapper<>();
            itemW.eq(PrescriptionItem::getPrescriptionId, pres.getId()).eq(PrescriptionItem::getDeleted,0);
            long count = prescriptionItemMapper.selectCount(itemW);
            vo.setItemCount((int) count);

            vo.setTotalAmount(pres.getTotalAmount());
            vo.setStatus(pres.getStatus());
            vo.setStatusText("待审核");
            vo.setCreatedAt(pres.getCreatedAt());
            return vo;
        }).toList();

        Page<PharmacistPrescriptionVO> resultPage = new Page<>();
        resultPage.setCurrent(prescriptionPage.getCurrent());
        resultPage.setSize(prescriptionPage.getSize());
        resultPage.setTotal(prescriptionPage.getTotal());
        resultPage.setRecords(voList);
        return Result.success(resultPage);
    }

    //4.2 药师审核处方
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> review(PharmacistReviewDTO dto) {
        Prescription prescription = prescriptionMapper.selectById(dto.getPrescriptionId());
        if(prescription == null || prescription.getDeleted() ==1){
            return Result.fail("处方不存在");
        }
        if(!prescription.getStatus().equals(0)){
            return Result.fail("仅待审核处方可以审核");
        }
        if("approve".equals(dto.getResult())){
            prescription.setStatus(1); // 已审核，待发药
        }else if("reject".equals(dto.getResult())){
            prescription.setStatus(3); // 作废，驳回
        }else{
            return Result.fail("审核结果只能是approve或者reject");
        }
        prescription.setUpdatedAt(LocalDateTime.now());
        prescriptionMapper.updateById(prescription);
        return Result.success(true, "approve".equals(dto.getResult()) ? "审核通过" : "驳回成功");
    }

    //4.3 发药（核心事务：库存扣减 + 更新处方 + 生成账单billing）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> dispense(PharmacistDispenseDTO dto) {
        Prescription prescription = prescriptionMapper.selectById(dto.getPrescriptionId());
        if(prescription == null || prescription.getDeleted() == 1){
            return Result.fail("处方不存在");
        }
        if(!prescription.getStatus().equals(1)){
            return Result.fail("只有【已审核】处方才能发药");
        }
        // 查询处方明细
        LambdaQueryWrapper<PrescriptionItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(PrescriptionItem::getPrescriptionId, dto.getPrescriptionId()).eq(PrescriptionItem::getDeleted,0);
        List<PrescriptionItem> itemList = prescriptionItemMapper.selectList(itemWrapper);

        // 校验库存 + 扣减库存
        for(PrescriptionItem item : itemList){
            Medicine medicine = medicineMapper.selectById(item.getMedicineId());
            if(medicine == null || medicine.getDeleted() ==1){
                throw new RuntimeException("药品id:"+item.getMedicineId()+"不存在");
            }
            if(medicine.getStock() < item.getQuantity()){
                throw new RuntimeException("药品【"+medicine.getName()+"】库存不足！当前库存："+medicine.getStock());
            }
            medicine.setStock(medicine.getStock() - item.getQuantity());
            medicineMapper.updateById(medicine);
        }
        // 更新处方状态：已发药
        prescription.setStatus(2);
        prescription.setDispenseTime(LocalDateTime.now());
        prescription.setUpdatedAt(LocalDateTime.now());
        prescriptionMapper.updateById(prescription);

        // 生成费用账单 billing
        Billing billing = new Billing();
        billing.setPatientId(prescription.getPatientId());
        billing.setPrescriptionId(prescription.getId());
        billing.setAmount(prescription.getTotalAmount());
        billing.setStatus(1); // 已结算
        billing.setCreatedAt(LocalDateTime.now());
        billingMapper.insert(billing);

        return Result.success(true, "发药成功");
    }

    //4.4 库存预警列表 stock < warningStock
    @Override
    public Result<List<LowStockVO>> lowStockList() {
        LambdaQueryWrapper<Medicine> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Medicine::getDeleted,0);
        List<Medicine> medList = medicineMapper.selectList(wrapper);
        List<LowStockVO> voList = medList.stream()
                .filter(med -> med.getStock() < med.getWarningStock())
                .map(med -> {
                    LowStockVO vo = new LowStockVO();
                    vo.setId(med.getId());
                    vo.setName(med.getName());
                    vo.setCode(med.getCode());
                    vo.setSpecification(med.getSpecification());
                    vo.setStock(med.getStock());
                    vo.setWarningStock(med.getWarningStock());
                    vo.setStatus(med.getStatus());
                    return vo;
                }).toList();
        return Result.success(voList);
    }

    //4.5 库存调整 in/out/check
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<InventoryAdjustVO> inventoryAdjust(InventoryAdjustDTO dto) {
        Medicine medicine = medicineMapper.selectById(dto.getMedicineId());
        if(medicine == null || medicine.getDeleted() == 1){
            return Result.fail("药品不存在");
        }
        Integer oldStock = medicine.getStock();
        Integer newStock;
        switch (dto.getAdjustType()){
            case "in":
                newStock = oldStock + dto.getAdjustQuantity();
                break;
            case "out":
                if(oldStock + dto.getAdjustQuantity() < 0){
                    throw new RuntimeException("出库后库存不能小于0");
                }
                newStock = oldStock + dto.getAdjustQuantity();
                break;
            case "check":
                newStock = dto.getAdjustQuantity();
                break;
            default:
                return Result.fail("调整类型仅支持 in/out/check");
        }
        medicine.setStock(newStock);
        medicineMapper.updateById(medicine);

        InventoryAdjustVO vo = new InventoryAdjustVO();
        vo.setMedicineId(dto.getMedicineId());
        vo.setPreviousStock(oldStock);
        vo.setAdjustQuantity(dto.getAdjustQuantity());
        vo.setCurrentStock(newStock);
        return Result.success(vo, "库存调整成功");
    }
}
