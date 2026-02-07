package com.mars.auth;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.mars.system.entity.SysUser;
import com.mars.system.service.SysLoginLogService;
import com.mars.system.service.SystemConfigHelper;
import com.mars.system.util.IpUtils;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 登录辅助工具
 * 提供登录后的公共逻辑：Token生成、Session写入、日志记录
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginHelper {

    private final SysLoginLogService loginLogService;
    private final SystemConfigHelper configHelper;

    /**
     * 执行登录并构建结果（通用流程）
     */
    public LoginResult doLogin(SysUser user) {
        HttpServletRequest request = getRequest();
        String ip = IpUtils.getIpAddr(request);
        UserAgent ua = UserAgentUtil.parse(request.getHeader("User-Agent"));
        String browser = ua != null ? ua.getBrowser().getName() : "Unknown";
        String os = ua != null ? ua.getOs().getName() : "Unknown";

        // 单点登录：踢掉其他设备
        if (configHelper.isSingleLogin()) {
            StpUtil.logout(user.getId());
        }

        // Sa-Token 登录
        StpUtil.login(user.getId());

        // 写入Session
        SaSession session = StpUtil.getSession();
        session.set("loginName", user.getUsername());
        session.set("ipaddr", ip);
        session.set("loginLocation", IpUtils.getAddressByIp(ip));
        session.set("browser", browser);
        session.set("os", os);
        session.set("status", 1);
        session.set("loginTime", System.currentTimeMillis());

        // 记录登录日志
        loginLogService.recordLog(user.getUsername(), 0, "登录成功", ip, browser, os);

        // 构建结果
        return LoginResult.of(
                StpUtil.getTokenValue(),
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getAvatar()
        );
    }

    /**
     * 记录登录失败日志
     */
    public void recordFailLog(String username, String message) {
        HttpServletRequest request = getRequest();
        String ip = IpUtils.getIpAddr(request);
        UserAgent ua = UserAgentUtil.parse(request.getHeader("User-Agent"));
        String browser = ua != null ? ua.getBrowser().getName() : "Unknown";
        String os = ua != null ? ua.getOs().getName() : "Unknown";
        loginLogService.recordLog(username, 1, message, ip, browser, os);
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }
}
