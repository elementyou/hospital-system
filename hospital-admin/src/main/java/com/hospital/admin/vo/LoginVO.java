package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录返回VO")
public class LoginVO {

    @Schema(description = "JWT令牌")
    private String token;

    @Schema(description = "令牌类型")
    private String tokenType = "Bearer";

    @Schema(description = "有效期(秒)")
    private Long expiresIn;

    @Schema(description = "登录用户信息")
    private UserInfoVO user;

    @Data
    @Schema(description = "用户信息")
    public static class UserInfoVO {
        @Schema(description = "用户id")
        private Long id;
        @Schema(description = "账号名")
        private String username;
        @Schema(description = "真实姓名")
        private String realName;
        @Schema(description = "角色标识")
        private String role;
        @Schema(description = "角色名称")
        private String roleName;
    }
}
