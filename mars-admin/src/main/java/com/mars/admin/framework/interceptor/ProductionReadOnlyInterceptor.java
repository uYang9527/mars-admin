package com.mars.admin.framework.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.admin.framework.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 生产环境只读拦截器
 * 在生产环境下拦截所有增删改操作，返回演示环境提示
 *
 * @author Mars
 * @version 1.0
 * @date 2025-01-08
 */
@Slf4j
@Component
public class ProductionReadOnlyInterceptor implements HandlerInterceptor {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Value("${mars.production.read-only.enabled:false}")
    private boolean readOnlyEnabled;

    @Value("${mars.production.read-only.message:演示环境，不允许进行增删改操作}")
    private String readOnlyMessage;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 需要拦截的HTTP方法
     */
    private static final List<String> RESTRICTED_METHODS = Arrays.asList("POST", "PUT", "DELETE", "PATCH");

    /**
     * 白名单路径（即使在生产环境也允许的操作）
     */
    private static final List<String> WHITELIST_PATHS = Arrays.asList(
            "/auth/login",           // 登录
            "/auth/logout",          // 登出
            "/auth/refresh",         // 刷新token
            "/auth/captcha",         // 获取验证码
            "/file/download",        // 文件下载
            "/export",               // 导出功能
            "/health",               // 健康检查
            "/actuator"              // 监控端点
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是生产环境或未启用只读模式，直接放行
        if (!"prod".equals(activeProfile) || !readOnlyEnabled) {
            return true;
        }

        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        // 只拦截增删改操作
        if (!RESTRICTED_METHODS.contains(method)) {
            return true;
        }

        // 检查是否在白名单中
        if (isInWhitelist(requestURI)) {
            return true;
        }

        // 拦截操作并返回提示信息
        log.warn("生产环境拦截操作: {} {}", method, requestURI);
        writeErrorResponse(response);
        return false;
    }

    /**
     * 检查路径是否在白名单中
     */
    private boolean isInWhitelist(String requestURI) {
        return WHITELIST_PATHS.stream().anyMatch(requestURI::contains);
    }

    /**
     * 写入错误响应
     */
    private void writeErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Result<Void> result = Result.error(403, readOnlyMessage);
        String jsonResponse = objectMapper.writeValueAsString(result);
        
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
} 