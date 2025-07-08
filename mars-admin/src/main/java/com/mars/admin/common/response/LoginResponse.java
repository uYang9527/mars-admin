package com.mars.admin.common.response;

import com.mars.admin.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * 登录响应DTO
 *
 * @author 【全栈学习关注公众号：程序员Mars】
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

    @Schema(description = "登录类型", example = "pc")
    private String loginType;

    @Schema(description = "用户信息")
    private SysUser userInfo;

    @Schema(description = "扩展信息")
    private Map<String, Object> extraInfo;

    public LoginResponse() {}

    public LoginResponse(String token, Long expiresIn, SysUser userInfo) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userInfo = userInfo;
    }

    public LoginResponse(String token, Long expiresIn, String loginType, SysUser userInfo, Map<String, Object> extraInfo) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.loginType = loginType;
        this.userInfo = userInfo;
        this.extraInfo = extraInfo;
    }
} 