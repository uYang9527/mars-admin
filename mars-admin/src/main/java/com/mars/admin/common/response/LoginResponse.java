package com.mars.admin.common.response;

import com.mars.admin.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录响应DTO
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-07-05
 */
@Schema(name = "登录响应", description = "用户登录响应数据")
@Data
public class LoginResponse {

    @Schema(description = "访问令牌", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "过期时间（秒）", example = "2592000")
    private Long expiresIn;

    @Schema(description = "用户信息")
    private SysUser userInfo;

    public LoginResponse() {}

    public LoginResponse(String token, Long expiresIn, SysUser userInfo) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userInfo = userInfo;
    }
} 