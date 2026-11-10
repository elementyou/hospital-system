package com.hospital.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.admin.entity.Doctor;
import com.hospital.admin.mapper.DoctorMapper;
import com.hospital.admin.service.DoctorService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

    @Resource
    private DoctorMapper doctorMapper;

    @Override
    public List<Doctor> listDoctorsByDeptId(Long deptId) {
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Doctor::getDepartmentId, deptId)
                .eq(Doctor::getStatus, 1);
        return doctorMapper.selectList(wrapper);
    }

    @Override
    public Page<Doctor> getDoctorPage(Long pageNum, Long pageSize, String name) {
        Page<Doctor> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        if(name != null && !name.isBlank()){
            wrapper.like(Doctor::getName, name);
        }
        return this.page(page, wrapper);
    }
}
