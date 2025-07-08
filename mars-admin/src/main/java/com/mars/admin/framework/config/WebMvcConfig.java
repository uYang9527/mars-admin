package com.mars.admin.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
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