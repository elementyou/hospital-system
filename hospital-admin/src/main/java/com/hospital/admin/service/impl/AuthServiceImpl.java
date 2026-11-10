package com.hospital.admin.service.impl;

import com.hospital.admin.service.AuthService;
import com.hospital.admin.util.JwtUtil;
import com.hospital.admin.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public LoginVO login(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        String token = jwtUtil.generateToken(auth.getName());
        long expiresIn = 7200L;

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setExpiresIn(expiresIn);

        LoginVO.UserInfoVO userInfo = new LoginVO.UserInfoVO();
        userInfo.setId(1L);
        userInfo.setUsername("admin");
        userInfo.setRealName("管理员");
        userInfo.setRole("ADMIN");
        userInfo.setRoleName("管理员");
        vo.setUser(userInfo);
        return vo;
    }
}
