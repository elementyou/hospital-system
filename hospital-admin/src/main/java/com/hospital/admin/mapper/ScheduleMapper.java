package com.hospital.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.admin.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    @Select("SELECT s.*, d.name doctor_name " +
            "FROM doctor_schedule s " +
            "LEFT JOIN doctor d ON s.doctor_id = d.id " +
            "WHERE s.deleted = 0 " +
            "AND (#{doctorId} IS NULL OR s.doctor_id = #{doctorId}) " +
            "AND (#{workDate} IS NULL OR s.work_date = #{workDate})")
    Page<Schedule> selectSchedulePage(Page<Schedule> page,
                                      @Param("doctorId") Long doctorId,
                                      @Param("workDate") String workDate);
}
