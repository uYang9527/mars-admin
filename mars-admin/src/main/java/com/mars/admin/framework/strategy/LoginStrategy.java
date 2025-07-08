package com.mars.admin.framework.strategy;

import com.mars.admin.common.enums.LoginType;
import com.mars.admin.common.request.LoginRequest;
import com.mars.admin.common.response.LoginResponse;

/**
 * 登录策略接口
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-01-16
 */
public interface LoginStrategy {
    
    /**
     * 获取支持的登录类型
     *
     * @return 登录类型
     */
    LoginType getLoginType();
    
    /**
     * 执行登录
     *
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * 验证登录参数
     *
     * @param loginRequest 登录请求
     * @return 验证结果
     */
    boolean validateLoginRequest(LoginRequest loginRequest);
    
    /**
     * 获取用户标识
     * 不同登录类型可能有不同的用户标识获取方式
     *
     * @param loginRequest 登录请求
     * @return 用户标识
     */
    String getUserIdentifier(LoginRequest loginRequest);
} 