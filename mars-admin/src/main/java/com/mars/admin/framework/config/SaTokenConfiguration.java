package com.mars.admin.framework.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

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

    @Autowired(required = false)
    private SaTokenProperties saTokenProperties;


    /**
     * 注册 Sa-Token 拦截器
     * 实现Token验证机制：
     * 1. 用户登录后，Token存储在Redis中
     * 2. 后续请求必须在Header中携带Token才能访问
     * 3. 白名单中的接口无需Token验证
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 获取白名单路径（优先从配置文件读取，否则使用默认值）
        List<String> excludePaths = saTokenProperties.getExcludePaths();

        // 注册 Sa-Token 拦截器
        registry.addInterceptor(new SaInterceptor(handle -> {
                    SaRouter.match("/**")
                            .notMatch(excludePaths.toArray(new String[0]))
                            .check(r -> {
                                // 检查用户是否登录（Token是否有效）
                                StpUtil.checkLogin();

                                log.info("Token验证通过 - 用户ID: {}，Token：{}", StpUtil.getLoginId(), StpUtil.getTokenInfo().getTokenValue());
                            });
                }))
                .addPathPatterns("/**")     // 拦截所有路径
                .order(-100);               // 设置高优先级，确保Sa-Token拦截器优先执行

        log.info("Sa-Token拦截器已注册，白名单路径: {}", excludePaths);
    }
} 