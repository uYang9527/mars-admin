package com.mars.admin.framework.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CORS跨域过滤器
 * 处理预检请求和跨域请求
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 允许的源域名
        String origin = request.getHeader("Origin");
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else {
            response.setHeader("Access-Control-Allow-Origin", "*");
        }

        // 允许的请求方法
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        
        // 允许的请求头
        response.setHeader("Access-Control-Allow-Headers", 
            "Origin, X-Requested-With, Content-Type, Accept, Authorization, Cache-Control, " +
            "Pragma, Expires, Last-Modified, If-Modified-Since, If-None-Match, " +
            "X-Forwarded-For, X-Real-IP, User-Agent, Referer");
        
        // 暴露的响应头
        response.setHeader("Access-Control-Expose-Headers", 
            "Authorization, Content-Type, X-Total-Count, Cache-Control, Content-Language, " +
            "Content-Length, Last-Modified");
        
        // 是否允许发送Cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        // 预检请求的缓存时间
        response.setHeader("Access-Control-Max-Age", "3600");

        // 处理OPTIONS预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // 初始化方法
    }

    @Override
    public void destroy() {
        // 销毁方法
    }
} 