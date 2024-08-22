package com.mars.easy.admin.framework.security;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.mars.easy.admin.framework.configuration.WebMvcConfiguration;
import com.mars.easy.admin.framework.interceptor.MySaInterceptor;
import com.mars.easy.admin.framework.properties.WhitelistProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * @ClassName SaTokenConfig
 * @Author 程序员Mars
 * @Date 2024/1/22 16:36
 * @Version 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SaTokenConfig extends WebMvcConfiguration {

    private final WhitelistProperties whitelistProperties;

    @Bean
    public StpLogic getStpLogicJwt() {
        // Sa-Token 整合 jwt (简单模式)
        return new StpLogicJwtForSimple();
    }

    /**
     * 注册 Sa-Token 路由拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 路由拦截鉴权 - 登录校验
        registry.addInterceptor(new SaInterceptor(r -> {
            StpUtil.checkLogin();
        }).isAnnotation(false)).addPathPatterns("/**").excludePathPatterns(whitelistProperties.getWhitelist());

        // 打开注解鉴权
        registry.addInterceptor(new MySaInterceptor()).addPathPatterns("/**").excludePathPatterns(whitelistProperties.getWhitelist());
    }

}
