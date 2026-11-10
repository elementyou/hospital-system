package com.hospital.admin.service;

import com.hospital.admin.vo.LoginVO;

public interface AuthService {
    LoginVO login(String username, String password);
}
