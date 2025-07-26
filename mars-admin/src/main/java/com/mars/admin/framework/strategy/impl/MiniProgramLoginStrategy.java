package com.mars.admin.framework.strategy.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序登录策略
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-01-16
 */
@Slf4j
@Component
public class MiniProgramLoginStrategy implements LoginStrategy {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysLoginInfoService sysLoginInfoService;

    @Value("${wechat.miniprogram.appid:}")
    private String appId;

    @Value("${wechat.miniprogram.secret:}")
    private String appSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public LoginType getLoginType() {
        return LoginType.MINIPROGRAM;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 1. 验证登录参数
        if (!validateLoginRequest(loginRequest)) {
            throw BusinessException.of("小程序登录参数验证失败");
        }

        // 2. 获取微信用户信息
        String openId = getOpenIdFromWechat(loginRequest.getWxCode());

        // 3. 查询或创建用户
        SysUser user = findOrCreateUser(openId, loginRequest);

        // 4. 校验用户状态
        checkUserStatus(user, openId);

        // 5. 执行登录
        StpUtil.login(user.getId());

        // 6. 获取 Token 信息
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 7. 更新用户登录信息
        updateUserLoginInfo(user);

        // 8. 记录登录成功日志
        recordLoginInfo(openId, "0", "小程序登录成功");

        // 9. 构建返回结果
        LoginResponse response = new LoginResponse();
        response.setToken(tokenInfo.getTokenValue());
        response.setTokenType("Bearer");
        response.setExpiresIn(tokenInfo.getTokenTimeout());
        response.setLoginType(getLoginType().getCode());

        // 清除敏感信息
        user.setPassword(null);
        response.setUserInfo(user);

        // 小程序端扩展信息
        Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("loginTime", LocalDateTime.now());
        extraInfo.put("clientType", "MINIPROGRAM");
        extraInfo.put("openId", openId);
        response.setExtraInfo(extraInfo);

        log.info("小程序用户 {} 登录成功，用户ID: {}", openId, user.getId());
        return response;
    }

    @Override
    public boolean validateLoginRequest(LoginRequest loginRequest) {
        // 小程序登录需要微信code
        return StringUtils.hasText(loginRequest.getWxCode());
    }

    @Override
    public String getUserIdentifier(LoginRequest loginRequest) {
        // 小程序用户标识是openId，需要通过code获取
        return getOpenIdFromWechat(loginRequest.getWxCode());
    }

    /**
     * 从微信服务器获取OpenId
     */
    private String getOpenIdFromWechat(String code) {
        if (!StringUtils.hasText(appId) || !StringUtils.hasText(appSecret)) {
            throw BusinessException.of("微信小程序配置不完整");
        }

        try {
            String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appId, appSecret, code
            );

            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = JSON.parseObject(response);

            if (jsonObject.containsKey("errcode")) {
                String errMsg = jsonObject.getString("errmsg");
                log.error("微信登录失败: {}", errMsg);
                throw BusinessException.of("微信登录失败: " + errMsg);
            }

            String openId = jsonObject.getString("openid");
            if (!StringUtils.hasText(openId)) {
                throw BusinessException.of("获取微信用户信息失败");
            }

            return openId;
        } catch (Exception e) {
            log.error("调用微信API失败", e);
            throw BusinessException.of("微信登录失败");
        }
    }

    /**
     * 查询或创建用户
     */
    private SysUser findOrCreateUser(String openId, LoginRequest loginRequest) {
        // 首先尝试通过openId查找用户
        SysUser user = sysUserMapper.selectByOpenId(openId);

        if (user == null) {
            // 如果用户不存在，创建新用户
            user = createNewUser(openId, loginRequest);
        } else {
            // 如果用户存在，更新用户信息
            updateUserInfo(user, loginRequest);
        }

        return user;
    }

    /**
     * 创建新用户
     */
    private SysUser createNewUser(String openId, LoginRequest loginRequest) {
        SysUser user = new SysUser();
        user.setOpenId(openId);
        user.setUsername("wx_" + openId.substring(0, 8)); // 生成默认用户名
        user.setNickname("微信用户");
        user.setStatus(1); // 启用状态
        user.setIsDeleted(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 解析微信用户信息
        if (StringUtils.hasText(loginRequest.getWxUserInfo())) {
            try {
                JSONObject userInfo = JSON.parseObject(loginRequest.getWxUserInfo());
                user.setNickname(userInfo.getString("nickName"));
                user.setAvatar(userInfo.getString("avatarUrl"));
                user.setGender(userInfo.getInteger("gender"));
            } catch (Exception e) {
                log.warn("解析微信用户信息失败", e);
            }
        }

        sysUserMapper.insert(user);
        return user;
    }

    /**
     * 更新用户信息
     */
    private void updateUserInfo(SysUser user, LoginRequest loginRequest) {
        boolean needUpdate = false;

        // 更新微信用户信息
        if (StringUtils.hasText(loginRequest.getWxUserInfo())) {
            try {
                JSONObject userInfo = JSON.parseObject(loginRequest.getWxUserInfo());
                String nickName = userInfo.getString("nickName");
                String avatarUrl = userInfo.getString("avatarUrl");

                if (StringUtils.hasText(nickName) && !nickName.equals(user.getNickname())) {
                    user.setNickname(nickName);
                    needUpdate = true;
                }

                if (StringUtils.hasText(avatarUrl) && !avatarUrl.equals(user.getAvatar())) {
                    user.setAvatar(avatarUrl);
                    needUpdate = true;
                }
            } catch (Exception e) {
                log.warn("更新微信用户信息失败", e);
            }
        }

        if (needUpdate) {
            user.setUpdateTime(LocalDateTime.now());
            sysUserMapper.update(user);
        }
    }

    /**
     * 校验用户状态
     */
    private void checkUserStatus(SysUser user, String openId) {
        if (user == null) {
            recordLoginInfo(openId, "0", "用户不存在");
            throw BusinessException.of("用户不存在");
        }

        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() == 0) {
            recordLoginInfo(openId, "0", "用户已被禁用");
            throw BusinessException.of("用户已被禁用，请联系管理员");
        }

        // 检查用户是否被删除
        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            recordLoginInfo(openId, "0", "用户已被删除");
            throw BusinessException.of("用户不存在");
        }
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
    private void recordLoginInfo(String identifier, String status, String message) {
        try {
            sysLoginInfoService.recordLoginInfo(identifier, status, message, getClientIp());
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
