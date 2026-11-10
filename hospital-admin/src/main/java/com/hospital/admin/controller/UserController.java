package com.hospital.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hospital.admin.entity.User;
import com.hospital.admin.service.UserService;

import com.hospital.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理模块")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    @Operation(summary = "分页查询用户")
    public Result<IPage<User>> list(
            @RequestParam Long pageNum,
            @RequestParam Long pageSize,
            @RequestParam(required = false) String key
    ) {
        IPage<User> page = userService.pageUser(pageNum, pageSize, key);
        return Result.success(page);
    }

    @PostMapping("/add")
    @Operation(summary = "新增用户")
    public Result<String> add(@RequestBody User user) {
        boolean ok = userService.save(user);
        return ok ? Result.success("新增成功") : Result.fail("新增失败");
    }

    @PutMapping("/update")
    @Operation(summary = "编辑用户")
    public Result<String> update(@RequestBody User user) {
        // 编辑的时候不修改密码（前端传过来password是空的，不覆盖）
        if(user.getPassword() == null || user.getPassword().isEmpty()){
            user.setPassword(null);
        }
        boolean ok = userService.updateById(user);
        return ok ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @PostMapping("/resetPwd")
    @Operation(summary = "重置密码")
    public Result<String> resetPwd(@RequestBody User user) {
        boolean ok = userService.resetPwd(user.getId());
        return ok ? Result.success("重置成功") : Result.fail("重置失败");
    }

    @PostMapping("/status")
    @Operation(summary = "修改启用禁用状态")
    public Result<String> status(@RequestBody User user) {
        boolean ok = userService.changeStatus(user.getId(), user.getStatus());
        return ok ? Result.success("状态修改成功") : Result.fail("修改失败");
    }
}
