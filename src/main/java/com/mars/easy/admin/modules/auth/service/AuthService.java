package com.mars.easy.admin.modules.auth.service;


import com.mars.easy.admin.modules.auth.entity.LoginInfo;
import com.mars.easy.admin.modules.auth.entity.LoginVO;

/**
 * @ClassName AuthService
 * @Author 程序员Mars
 * @Date 2024/1/22 17:24
 * @Version 1.0
 */
public interface AuthService {

    LoginVO loginClient(LoginInfo info);
}
