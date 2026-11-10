package com.hospital.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.admin.dto.AppointmentDTO;
import com.hospital.admin.vo.AppointmentVO;
import com.hospital.admin.entity.Appointment;
import com.hospital.common.Result;

public interface AppointmentService extends IService<Appointment> {

    /**
     * 创建预约挂号
     * @param dto 前端传来的预约参数
     * @return 统一返回结果
     */
    Result<?> createAppointment(AppointmentDTO dto);

    /**
     * 取消预约
     * @param id 预约id
     * @return 统一返回结果
     */
    Result<?> cancelAppointment(Long id);

    /**
     * 查询我的预约（分页）
     * @param patientId 患者id
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 分页VO
     */
    Page<AppointmentVO> getMyAppointment(Long patientId, Long pageNum, Long pageSize);

}
