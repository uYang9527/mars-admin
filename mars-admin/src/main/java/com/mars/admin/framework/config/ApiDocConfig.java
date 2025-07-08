package com.mars.admin.framework.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * API文档配置类
 * 在生产环境下禁用接口文档访问
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-01-16
 */
@Configuration
public class ApiDocConfig {

    /**
     * 生产环境下禁用接口文档访问的Web配置
     */
    @Configuration
    @Profile("prod")
    static class ProductionApiDocConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // 生产环境下移除swagger-ui相关的资源映射
            // 不添加任何swagger相关的资源处理器
        }
    }

    /**
     * 开发环境下启用接口文档的Web配置
     */
    @Configuration
    @Profile({"dev", "test"})
    static class DevelopmentApiDocConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // 开发和测试环境保持默认的swagger资源映射
            registry.addResourceHandler("swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
            registry.addResourceHandler("doc.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
        }
    }
} 