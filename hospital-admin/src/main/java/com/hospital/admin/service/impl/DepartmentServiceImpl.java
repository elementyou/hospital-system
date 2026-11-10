package com.hospital.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.admin.entity.Department;
import com.hospital.admin.mapper.DepartmentMapper;
import com.hospital.admin.service.DepartmentService;
import com.hospital.admin.vo.DashboardVO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    // ...
    @Autowired
    private DepartmentMapper departmentMapper;


    @Override
    public List<Department> listEnableDepartment() {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getStatus, 1);
        return list(wrapper);
    }

    // 补上这个！实现接口的statDepartment
    @Override
    public List<DashboardVO.DepartmentStatVO> statDepartment(String startDate, String endDate) {
        return departmentMapper.statDepartment(startDate, endDate);
    }
}
