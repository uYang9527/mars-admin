package com.mars.admin.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求DTO
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-07-05
 */
@Schema(name = "登录请求", description = "用户登录请求参数")
@Data
public class LoginRequest {

    @Schema(description = "登录类型", example = "pc", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "登录类型不能为空")
    private String loginType = "pc";

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "密码", example = "123456")
    private String password;

    @Schema(description = "手机号", example = "13800138000")
    private String mobile;

    @Schema(description = "验证码", example = "1234")
    private String captcha;

    @Schema(description = "微信小程序code", example = "wx_code_123")
    private String wxCode;

    @Schema(description = "微信小程序用户信息", example = "{}")
    private String wxUserInfo;

    @Schema(description = "APP设备ID", example = "device_123")
    private String deviceId;

    @Schema(description = "APP推送token", example = "push_token_123")
    private String pushToken;

    @Schema(description = "记住我", example = "false")
    private Boolean rememberMe = false;

    @Schema(description = "客户端信息", example = "iOS 15.0")
    private String clientInfo;
} 