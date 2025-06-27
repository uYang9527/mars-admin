package com.mars.admin.framework.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SaToken 配置类
 * 实现Token验证：用户登录后Token存入Redis，访问接口时Header必须携带Token
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Slf4j
@Configuration
public class SaTokenConfiguration implements WebMvcConfigurer {

    /**
     * 注册 Sa-Token 拦截器
     * 实现Token验证机制：
     * 1. 用户登录后，Token存储在Redis中
     * 2. 后续请求必须在Header中携带Token才能访问
     * 3. 白名单中的接口无需Token验证
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器
        registry.addInterceptor(new SaInterceptor(handle -> {
            SaRouter.match("/**")
                    .notMatch(
                            // 认证相关接口（无需Token）
                            "/auth/login",      // 登录接口
                            "/auth/logout",     // 登出接口
                            "/auth/register",   // 注册接口
                            "/auth/captcha",    // 验证码接口
                            
                            // API文档相关（无需Token）
                            "/doc.html",
                            "/swagger-ui.html",
                            "/swagger-ui/**",
                            "/swagger-resources/**",
                            "/webjars/**",
                            "/v3/api-docs",
                            "/v3/api-docs/**",
                            "/v2/api-docs",
                            
                            // 测试接口（无需Token）
                            "/token-test/public",
                            
                            // 静态资源（无需Token）
                            "/favicon.ico",
                            "/error"
                    )
                    .check(r -> {
                        // 打印调试信息
                        log.info("Sa-Token拦截器执行，开始验证Token");
                        
                        // 检查用户是否登录（Token是否有效）
                        StpUtil.checkLogin();
                        
                        log.info("Token验证通过 - 用户ID: {}", StpUtil.getLoginId());
                    });
        }))
        .addPathPatterns("/**")     // 拦截所有路径
        .order(-100);               // 设置高优先级，确保Sa-Token拦截器优先执行
    }
} 