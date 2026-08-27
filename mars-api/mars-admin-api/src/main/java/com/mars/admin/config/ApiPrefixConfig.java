package com.mars.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * API 前缀配置
 * 给所有 @RestController 添加 /api 前缀，避免与前端路由冲突
 */
@Configuration
public class ApiPrefixConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 给所有 @RestController 注解的控制器添加 /api 前缀
        // Warm-Flow 设计器使用固定路径 /warm-flow、/warm-flow-ui，不能加 /api 前缀
        configurer.addPathPrefix("/api", c ->
                c.isAnnotationPresent(RestController.class)
                        && !c.getPackageName().startsWith("org.dromara.warm.flow"));
    }
}
