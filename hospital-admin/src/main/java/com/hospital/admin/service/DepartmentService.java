package com.hospital.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.admin.entity.Department;
import com.hospital.admin.vo.DashboardVO;

import java.util.List;

public interface DepartmentService extends IService<Department> {
    // 查询所有启用的科室
    List<Department> listEnableDepartment();

    List<DashboardVO.DepartmentStatVO> statDepartment(String startDate, String endDate);
}
