package com.mars.admin.framework.config;

import com.mars.admin.framework.interceptor.ProductionReadOnlyInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置类
 *
 * @author Mars.wq
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:./upload}")
    private String uploadPath;

    @Autowired
    private ProductionReadOnlyInterceptor productionReadOnlyInterceptor;

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加生产环境只读拦截器
        registry.addInterceptor(productionReadOnlyInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns(
                        "/static/**",           // 静态资源
                        "/public/**",           // 公共资源
                        "/webjars/**",          // webjars资源
                        "/favicon.ico",         // 网站图标
                        "/error",               // 错误页面
                        "/doc.html",            // API文档
                        "/swagger-ui.html",     // Swagger UI
                        "/swagger-ui/**",       // Swagger UI资源
                        "/v3/api-docs/**",      // OpenAPI文档
                        "/knife4j/**",          // Knife4j资源
                        "/druid/**",            // Druid监控
                        "/actuator/**",         // Spring Boot监控
                        "/sqlLog.html",         // SQL日志页面
                        "/sqlLog/**"            // SQL日志资源
                );
        
        log.info("生产环境只读拦截器已配置");
    }

    /**
     * 配置静态资源访问
     * 将上传文件目录映射为静态资源路径，以便直接访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置本地文件上传目录映射，直接映射到upload目录
        String filePath = "file:" + uploadPath + "/";
        log.info("配置本地上传文件目录映射: {} -> /file/**", filePath);
        registry.addResourceHandler("/file/**")
                .addResourceLocations(filePath)
                .setCachePeriod(3600) // 缓存一小时
                .resourceChain(true);
                
        // 增加一个日志输出便于调试
        log.info("上传目录物理路径: {}", uploadPath);
    }
} 