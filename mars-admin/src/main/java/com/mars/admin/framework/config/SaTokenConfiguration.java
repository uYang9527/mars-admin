package com.mars.admin.framework.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SaToken 配置类
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Configuration
public class SaTokenConfiguration implements WebMvcConfigurer {

    /**
     * 注册 Sa-Token 拦截器，打开注解式鉴权功能
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 指定一条 match 规则
            SaRouter
                    // 拦截的 path 列表，可以写多个 */
                    .match("/**")
                    // 排除掉的 path 列表，只放行登录和登出接口
                    .notMatch(
                            "/auth/login",      // 登录接口
                            "/auth/logout",     // 登出接口
                            "/auth/register",   // 注册接口
                            "/auth/captcha",    // 验证码接口
                            "/api/doc",         // API文档地址
                            "/api/info",        // 系统信息
                            "/test/**",         // 测试接口
                            "/diagnostic/**",   // 诊断接口
                            "/doc.html",        // knife4j文档
                            "/swagger-ui.html", // swagger文档
                            "/swagger-ui/**",   // swagger ui 静态资源
                            "/swagger-resources/**",
                            "/webjars/**",
                            "/v3/api-docs",     // OpenAPI 3 文档
                            "/v3/api-docs/**",  // OpenAPI 3 文档子路径
                            "/v2/api-docs",     // 兼容旧版本
                            "/favicon.ico",
                            "/error"
                    )
                    // 要执行的校验动作，可以写完整的 lambda 表达式
                    .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }
} 