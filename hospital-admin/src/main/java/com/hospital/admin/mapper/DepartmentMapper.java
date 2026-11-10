package com.hospital.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.admin.entity.Department;
import com.hospital.admin.vo.DashboardVO; // 确认 VO 类的具体包路径
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 统计科室数据
     */
    // DepartmentMapper接口新增方法
    List<DashboardVO.DepartmentStatVO> statDepartment(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
