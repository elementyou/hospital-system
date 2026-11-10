package com.hospital.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.admin.entity.PrescriptionItem;
import com.hospital.admin.vo.DashboardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PrescriptionItemMapper extends BaseMapper<PrescriptionItem> {
    // PrescriptionItemMapper接口新增
    List<DashboardVO.MedicineConsumeVO> statMedicineConsume(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
