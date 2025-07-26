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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * APP登录策略
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-01-16
 */
@Slf4j
@Component
public class AppLoginStrategy implements LoginStrategy {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysLoginInfoService sysLoginInfoService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CAPTCHA_KEY_PREFIX = "app:captcha:";
    private static final int CAPTCHA_EXPIRE_TIME = 5; // 验证码过期时间（分钟）

    @Override
    public LoginType getLoginType() {
        return LoginType.APP;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 1. 验证登录参数
        if (!validateLoginRequest(loginRequest)) {
            throw BusinessException.of("APP登录参数验证失败");
        }

        // 2. 验证验证码
        if (!validateCaptcha(loginRequest.getMobile(), loginRequest.getCaptcha())) {
            throw BusinessException.of("验证码错误或已过期");
        }

        // 3. 获取用户标识
        String mobile = getUserIdentifier(loginRequest);

        // 4. 查询或创建用户
        SysUser user = findOrCreateUser(mobile, loginRequest);

        // 5. 校验用户状态
        checkUserStatus(user, mobile);

        // 6. 执行登录
        StpUtil.login(user.getId());

        // 7. 获取 Token 信息
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 8. 更新用户登录信息
        updateUserLoginInfo(user, loginRequest);

        // 9. 记录登录成功日志
        recordLoginInfo(mobile, "0", "APP登录成功");

        // 10. 清除验证码
        clearCaptcha(mobile);

        // 11. 构建返回结果
        LoginResponse response = new LoginResponse();
        response.setToken(tokenInfo.getTokenValue());
        response.setTokenType("Bearer");
        response.setExpiresIn(tokenInfo.getTokenTimeout());
        response.setLoginType(getLoginType().getCode());

        // 清除敏感信息
        user.setPassword(null);
        response.setUserInfo(user);

        // APP端扩展信息
        Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("loginTime", LocalDateTime.now());
        extraInfo.put("clientType", "APP");
        extraInfo.put("deviceId", loginRequest.getDeviceId());
        extraInfo.put("clientInfo", loginRequest.getClientInfo());
        response.setExtraInfo(extraInfo);

        log.info("APP用户 {} 登录成功，用户ID: {}", mobile, user.getId());
        return response;
    }

    @Override
    public boolean validateLoginRequest(LoginRequest loginRequest) {
        // APP登录需要手机号和验证码
        return StringUtils.hasText(loginRequest.getMobile())
            && StringUtils.hasText(loginRequest.getCaptcha());
    }

    @Override
    public String getUserIdentifier(LoginRequest loginRequest) {
        return loginRequest.getMobile().trim();
    }

    /**
     * 验证验证码
     */
    private boolean validateCaptcha(String mobile, String captcha) {
        if (!StringUtils.hasText(mobile) || !StringUtils.hasText(captcha)) {
            return false;
        }

        String cacheKey = CAPTCHA_KEY_PREFIX + mobile;
        String cachedCaptcha = redisTemplate.opsForValue().get(cacheKey);

        return captcha.equals(cachedCaptcha);
    }

    /**
     * 查询或创建用户
     */
    private SysUser findOrCreateUser(String mobile, LoginRequest loginRequest) {
        // 首先尝试通过手机号查找用户
        SysUser user = sysUserMapper.selectByPhone(mobile);

        if (user == null) {
            // 如果用户不存在，创建新用户
            user = createNewUser(mobile, loginRequest);
        } else {
            // 如果用户存在，更新设备信息
            updateDeviceInfo(user, loginRequest);
        }

        return user;
    }

    /**
     * 创建新用户
     */
    private SysUser createNewUser(String mobile, LoginRequest loginRequest) {
        SysUser user = new SysUser();
        user.setPhone(mobile);
        user.setUsername("app_" + mobile); // 生成默认用户名
        user.setNickname("APP用户");
        user.setStatus(1); // 启用状态
        user.setIsDeleted(0);
        user.setUserType(2); // 普通用户
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        sysUserMapper.insert(user);
        return user;
    }

    /**
     * 更新设备信息
     */
    private void updateDeviceInfo(SysUser user, LoginRequest loginRequest) {
        // 这里可以记录设备信息到用户表或单独的设备表
        // 为了简化，暂时不做处理
        user.setUpdateTime(LocalDateTime.now());
        sysUserMapper.update(user);
    }

    /**
     * 校验用户状态
     */
    private void checkUserStatus(SysUser user, String mobile) {
        if (user == null) {
            recordLoginInfo(mobile, "0", "用户不存在");
            throw BusinessException.of("用户不存在");
        }

        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() == 0) {
            recordLoginInfo(mobile, "0", "用户已被禁用");
            throw BusinessException.of("用户已被禁用，请联系管理员");
        }

        // 检查用户是否被删除
        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            recordLoginInfo(mobile, "0", "用户已被删除");
            throw BusinessException.of("用户不存在");
        }
    }

    /**
     * 更新用户登录信息
     */
    private void updateUserLoginInfo(SysUser user, LoginRequest loginRequest) {
        try {
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(getClientIp());

            // 更新登录次数
            Integer loginCount = user.getLoginCount();
            user.setLoginCount(loginCount == null ? 1 : loginCount + 1);

            sysUserMapper.update(user);
        } catch (Exception e) {
            log.error("更新用户登录信息失败", e);
        }
    }

    /**
     * 记录登录日志
     */
    private void recordLoginInfo(String identifier, String status, String message) {
        try {
            sysLoginInfoService.recordLoginInfo(identifier, status, message, getClientIp());
        } catch (Exception e) {
            log.error("记录登录日志失败", e);
        }
    }

    /**
     * 清除验证码
     */
    private void clearCaptcha(String mobile) {
        try {
            String cacheKey = CAPTCHA_KEY_PREFIX + mobile;
            redisTemplate.delete(cacheKey);
        } catch (Exception e) {
            log.error("清除验证码失败", e);
        }
    }

    /**
     * 发送验证码（供外部调用）
     */
    public boolean sendCaptcha(String mobile) {
        if (!StringUtils.hasText(mobile)) {
            return false;
        }

        try {
            // 生成6位随机验证码
            String captcha = generateCaptcha();

            // 存储到Redis，设置过期时间
            String cacheKey = CAPTCHA_KEY_PREFIX + mobile;
            redisTemplate.opsForValue().set(cacheKey, captcha, CAPTCHA_EXPIRE_TIME, TimeUnit.MINUTES);

            // 这里应该调用短信服务发送验证码
            // 为了演示，只是打印日志
            log.info("发送验证码到手机号 {}: {}", mobile, captcha);

            return true;
        } catch (Exception e) {
            log.error("发送验证码失败", e);
            return false;
        }
    }

    /**
     * 生成验证码
     */
    private String generateCaptcha() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp() {
        // 这里可以从request中获取IP，为了简化暂时返回固定值
        return "127.0.0.1";
    }
}
