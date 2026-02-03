package com.mars.system.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token配置
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * 注册Sa-Token拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 登录校验
            SaRouter.match("/**")
                    .notMatch(
                            "/auth/login",
                            "/auth/register",               // 用户注册
                            "/auth/captcha",                // 图片验证码
                            "/auth/sms-code",               // 短信验证码
                            "/api/mall/home",        // 小程序首页
                            "/api/mall/login",        // 小程序登录
                            "/api/mall/loginByPhone",        // 小程序登录
                            "/error",
                            "/crypto/**",                   // 加密配置
                            "/sys/config-group/public",     // 公开配置
                            "/file/**",                     // 文件访问
                            "/files/**"                     // 文件访问
                    )
                    .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
