package com.mars.admin.service;

import com.mars.admin.common.request.LoginRequest;
import com.mars.admin.common.response.LoginResponse;

/**
 * 认证服务接口
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
public interface IAuthService {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 用户登出
     */
    void logout();

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    Object getCurrentUser();

    /**
     * 检查用户名和密码
     *
     * @param username 用户名
     * @param password 密码
     * @return 是否验证成功
     */
    boolean checkPassword(String username, String password);
} 