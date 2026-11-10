package com.hospital.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hospital.admin.entity.User;
import com.hospital.admin.mapper.UserMapper;
import com.hospital.admin.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public IPage<User> pageUser(Long pageNum, Long pageSize, String key) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (key != null && !key.isEmpty()) {
            wrapper.like(User::getUsername, key);
        }
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public boolean resetPwd(Long id) {
        User user = this.getById(id);
        if(user == null) return false;
        // 明文 123456，毕设先用明文，后面再加密
        user.setPassword("123456");
        return this.updateById(user);
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        User user = this.getById(id);
        if(user == null) return false;
        user.setStatus(status);
        return this.updateById(user);
    }
}
