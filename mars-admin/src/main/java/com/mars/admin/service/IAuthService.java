package com.mars.admin.service;

import com.mars.admin.common.request.LoginRequest;
import com.mars.admin.common.response.LoginResponse;
import com.mars.admin.entity.SysUser;

/**
 * 认证服务接口
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-07-05
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
    SysUser getCurrentUser();

    /**
     * 检查密码是否正确
     *
     * @param inputPassword 输入的密码
     * @param storedPassword 存储的密码
     * @return 是否验证成功
     */
    boolean checkPassword(String inputPassword, String storedPassword);

    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);
} 