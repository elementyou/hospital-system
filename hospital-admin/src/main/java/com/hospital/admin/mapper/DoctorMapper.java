package com.hospital.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.admin.entity.Doctor;
import com.hospital.admin.vo.DashboardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {
    // DoctorMapper接口新增
    List<DashboardVO.DoctorWorkloadVO> statDoctorWork(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
