package com.hospital.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.admin.entity.Schedule;
import java.util.List;

public interface ScheduleService extends IService<Schedule> {
    // 原有
    List<Schedule> listScheduleByDoctorAndDate(Long doctorId, String workDate);

    // 新增：排班分页联查
    Page<Schedule> getSchedulePage(Long pageNum, Long pageSize, Long doctorId, String workDate);
}
