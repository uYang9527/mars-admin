package com.mars.admin.framework.strategy.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.mars.admin.common.enums.LoginType;
import com.mars.admin.common.request.LoginRequest;
import com.mars.admin.common.response.LoginResponse;
import com.mars.admin.modules.system.entity.SysUser;
import com.mars.admin.framework.exception.BusinessException;
import com.mars.admin.framework.strategy.LoginStrategy;
import com.mars.admin.modules.system.mapper.SysUserMapper;
import com.mars.admin.modules.system.service.ISysLoginInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * PC端登录策略
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-01-16
 */
@Slf4j
@Component
public class PcLoginStrategy implements LoginStrategy {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysLoginInfoService sysLoginInfoService;

    @Override
    public LoginType getLoginType() {
        return LoginType.PC;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 1. 验证登录参数
        if (!validateLoginRequest(loginRequest)) {
            throw BusinessException.of("登录参数验证失败");
        }

        // 2. 获取用户标识
        String username = getUserIdentifier(loginRequest);

        // 3. 查询用户
        SysUser user = sysUserMapper.selectByUsername(username);

        // 4. 校验用户信息
        checkUserStatus(user, username, loginRequest.getPassword());

        // 5. 执行登录
        StpUtil.login(user.getId());

        // 6. 获取 Token 信息
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 7. 更新用户登录信息
        updateUserLoginInfo(user);

        // 8. 记录登录成功日志
        recordLoginInfo(username, "0", "PC端登录成功");

        // 9. 构建返回结果
        LoginResponse response = new LoginResponse();
        response.setToken(tokenInfo.getTokenValue());
        response.setTokenType("Bearer");
        response.setExpiresIn(tokenInfo.getTokenTimeout());
        response.setLoginType(getLoginType().getCode());

        // 清除敏感信息
        user.setPassword(null);
        response.setUserInfo(user);

        // PC端扩展信息
        Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("loginTime", LocalDateTime.now());
        extraInfo.put("clientType", "PC");
        response.setExtraInfo(extraInfo);

        log.info("PC端用户 {} 登录成功，用户ID: {}", username, user.getId());
        return response;
    }

    @Override
    public boolean validateLoginRequest(LoginRequest loginRequest) {
        // PC端登录需要用户名和密码
        return StringUtils.hasText(loginRequest.getUsername())
            && StringUtils.hasText(loginRequest.getPassword());
    }

    @Override
    public String getUserIdentifier(LoginRequest loginRequest) {
        return loginRequest.getUsername().trim();
    }

    /**
     * 校验用户状态
     */
    private void checkUserStatus(SysUser user, String username, String password) {
        if (user == null) {
            recordLoginInfo(username, "0", "用户不存在");
            throw BusinessException.of("用户名或密码错误");
        }

        // 验证密码
        if (!checkPassword(password, user.getPassword())) {
            recordLoginInfo(username, "0", "密码错误");
            throw BusinessException.of("用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() == 0) {
            recordLoginInfo(username, "0", "用户已被禁用");
            throw BusinessException.of("用户已被禁用，请联系管理员");
        }

        // 检查用户是否被删除
        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            recordLoginInfo(username, "0", "用户已被删除");
            throw BusinessException.of("用户不存在");
        }
    }

    /**
     * 验证密码
     */
    private boolean checkPassword(String inputPassword, String storedPassword) {
        if (!StringUtils.hasText(inputPassword) || !StringUtils.hasText(storedPassword)) {
            return false;
        }
        String encryptedPassword = DigestUtils.md5DigestAsHex(inputPassword.getBytes(StandardCharsets.UTF_8));
        return encryptedPassword.equals(storedPassword);
    }

    /**
     * 更新用户登录信息
     */
    private void updateUserLoginInfo(SysUser user) {
        try {
            user.setLastLoginTime(LocalDateTime.now());
            sysUserMapper.update(user);
        } catch (Exception e) {
            log.error("更新用户登录信息失败", e);
        }
    }

    /**
     * 记录登录日志
     */
    private void recordLoginInfo(String username, String status, String message) {
        try {
            sysLoginInfoService.recordLoginInfo(username, status, message, getClientIp());
        } catch (Exception e) {
            log.error("记录登录日志失败", e);
        }
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp() {
        // 这里可以从request中获取IP，为了简化暂时返回固定值
        return "127.0.0.1";
    }
}
