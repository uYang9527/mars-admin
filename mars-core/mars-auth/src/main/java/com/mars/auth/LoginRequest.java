package com.mars.auth;

import com.mars.auth.enums.ClientType;
import com.mars.auth.enums.LoginType;
import lombok.Data;

/**
 * 统一登录请求
 */
@Data
public class LoginRequest {

    /**
     * 登录方式
     */
    private LoginType loginType;

    /**
     * 客户端类型
     */
    private ClientType clientType;

    // ========== 密码登录 ==========
    private String username;
    private String password;
    private String uuid;       // 验证码key
    private String code;       // 验证码
    private Boolean rememberMe;

    // ========== 手机号登录 ==========
    private String phone;
    private String smsCode;

    // ========== 小程序登录 ==========
    private String wxCode;     // 小程序 wx.login() 的 code
    private String phoneCode;  // 获取手机号的 code

    // ========== 三方登录 ==========
    private String platform;   // 三方平台：wechat_mp / alipay / apple
    private String authCode;   // 授权码
}
