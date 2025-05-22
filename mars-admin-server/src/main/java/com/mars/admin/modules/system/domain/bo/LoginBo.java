package com.mars.admin.modules.system.domain.bo;

import lombok.Data;

@Data
public class LoginBo {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 授权类型
     */
    private String grantType;

    /**
     * 租戶id
     */
    private String tenantId;

    /**
     * 微信小承轩登录code
     */
    private String code;

}
