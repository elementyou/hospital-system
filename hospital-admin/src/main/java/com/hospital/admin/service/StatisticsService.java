package com.hospital.admin.service;

import com.hospital.admin.vo.DashboardVO;

public interface StatisticsService {
    DashboardVO getDashboard(String startDate, String endDate);
}
