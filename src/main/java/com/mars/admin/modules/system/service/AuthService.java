package com.mars.admin.modules.system.service;

import com.mars.admin.modules.system.domain.bo.LoginBo;
import com.mars.admin.modules.system.domain.vo.LoginVo;

public interface AuthService {

    /**
     * 登录
     *
     * @param info 登录请求参数
     * @return 登录结果
     */
    LoginVo login(LoginBo info);
}
