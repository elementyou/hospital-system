package com.hospital.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hospital.admin.entity.User;

public interface UserService extends IService<User> {
    IPage<User> pageUser(Long pageNum, Long pageSize, String key);
    boolean resetPwd(Long id);
    boolean changeStatus(Long id, Integer status);
}
