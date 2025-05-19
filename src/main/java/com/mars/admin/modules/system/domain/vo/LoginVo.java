package com.mars.admin.modules.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginVo {

    /**
     * 授权令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 授权令牌 access_token 的有效期
     */
    private Long expireIn;

    /**
     * 刷新令牌 refresh_token 的有效期
     */
    private Long refreshExpireIn;

    /**
     * 应用id
     */
    private String clientId;

    /**
     * 令牌权限
     */
    private String scope;

    /**
     * 用户 openid
     */
    private String openid;


}
