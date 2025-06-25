package com.mars.admin.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.mars.admin.common.request.LoginRequest;
import com.mars.admin.common.response.LoginResponse;
import com.mars.admin.entity.User;
import com.mars.admin.framework.exception.BusinessException;
import com.mars.admin.mapper.UserMapper;
import com.mars.admin.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 认证服务实现类
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 1. 参数校验
        if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            throw BusinessException.of("用户名或密码不能为空");
        }

        // 2. 查询用户
        User user = userMapper.selectByUsername(loginRequest.getUsername());
        if (user == null) {
            throw BusinessException.of("用户不存在");
        }

        // 3. 验证密码
        if (!checkPassword(loginRequest.getUsername(), loginRequest.getPassword())) {
            throw BusinessException.of("用户名或密码错误");
        }

        // 4. 检查用户状态
        if (Boolean.TRUE.equals(user.getIsDelete())) {
            throw BusinessException.of("用户已被禁用");
        }

        // 5. 执行登录
        StpUtil.login(user.getId());

        // 6. 获取 Token 信息
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 7. 构建返回结果
        LoginResponse response = new LoginResponse();
        response.setToken(tokenInfo.getTokenValue());
        response.setExpiresIn(tokenInfo.getTokenTimeout());
        
        // 清除敏感信息
        user.setPassword(null);
        response.setUserInfo(user);

        log.info("用户 {} 登录成功", loginRequest.getUsername());
        return response;
    }

    @Override
    public void logout() {
        if (StpUtil.isLogin()) {
            Object loginId = StpUtil.getLoginId();
            StpUtil.logout();
            log.info("用户 {} 登出成功", loginId);
        }
    }

    @Override
    public Object getCurrentUser() {
        if (!StpUtil.isLogin()) {
            throw BusinessException.of("用户未登录");
        }

        Object loginId = StpUtil.getLoginId();
        User user = userMapper.selectOneById((Long) loginId);
        if (user == null) {
            throw BusinessException.of("用户不存在");
        }

        // 清除敏感信息
        user.setPassword(null);
        return user;
    }

    @Override
    public boolean checkPassword(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return false;
        }

        // 这里使用简单的 MD5 加密，实际项目中建议使用 BCrypt 等更安全的方式
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        return encryptedPassword.equals(user.getPassword());
    }

    /**
     * 密码加密工具方法
     *
     * @param password 明文密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }
} 