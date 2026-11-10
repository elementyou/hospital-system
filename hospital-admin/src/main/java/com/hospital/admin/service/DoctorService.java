package com.hospital.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.admin.entity.Doctor;
import java.util.List;

public interface DoctorService extends IService<Doctor> {
    // 原有
    List<Doctor> listDoctorsByDeptId(Long deptId);

    // 新增：分页查询医生（医生管理页面）
    Page<Doctor> getDoctorPage(Long pageNum, Long pageSize, String name);
}
