package com.mars.admin.controller;

import com.mars.admin.framework.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 诊断控制器
 * 用于检查系统配置和依赖状态
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Tag(name = "系统诊断", description = "系统配置和依赖状态检查")
@RestController
@RequestMapping("/diagnostic")
public class DiagnosticController {

    @Autowired
    private ApplicationContext applicationContext;

    @Operation(summary = "检查OpenAPI配置", description = "检查OpenAPI相关Bean是否正确注册")
    @GetMapping("/openapi")
    public Result<Map<String, Object>> checkOpenApiConfig() {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // 检查OpenAPI Bean
            boolean hasOpenAPI = applicationContext.containsBean("customOpenAPI");
            data.put("openApiBean", hasOpenAPI);
            
            // 检查SpringDoc相关Bean
            String[] springDocBeans = applicationContext.getBeanNamesForType(
                org.springdoc.core.configuration.SpringDocConfiguration.class);
            data.put("springDocBeans", springDocBeans.length > 0);
            data.put("springDocBeanNames", springDocBeans);
            
            // 检查Knife4j相关Bean
            String[] knife4jBeans = applicationContext.getBeanNamesForAnnotation(
                org.springframework.context.annotation.Configuration.class);
            data.put("configurationBeans", knife4jBeans.length);
            
            data.put("status", "success");
            
        } catch (Exception e) {
            data.put("status", "error");
            data.put("error", e.getMessage());
        }
        
        return Result.success("OpenAPI配置检查完成", data);
    }

    @Operation(summary = "检查依赖信息", description = "检查关键依赖的版本信息")
    @GetMapping("/dependencies")
    public Result<Map<String, Object>> checkDependencies() {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // 检查Spring Boot版本
            Package springBootPackage = org.springframework.boot.SpringBootVersion.class.getPackage();
            data.put("springBootVersion", springBootPackage.getImplementationVersion());
            
            // 检查Spring版本
            Package springPackage = org.springframework.core.SpringVersion.class.getPackage();
            data.put("springVersion", springPackage.getImplementationVersion());
            
            // 检查Java版本
            data.put("javaVersion", System.getProperty("java.version"));
            data.put("javaVendor", System.getProperty("java.vendor"));
            
            // 检查类路径中的关键类
            data.put("hasSwaggerV3", isClassPresent("io.swagger.v3.oas.annotations.Operation"));
            data.put("hasKnife4j", isClassPresent("com.github.xiaoymin.knife4j.spring.configuration.Knife4jAutoConfiguration"));
            data.put("hasSpringDoc", isClassPresent("org.springdoc.core.configuration.SpringDocConfiguration"));
            
            data.put("status", "success");
            
        } catch (Exception e) {
            data.put("status", "error");
            data.put("error", e.getMessage());
        }
        
        return Result.success("依赖检查完成", data);
    }

    @Operation(summary = "获取访问链接", description = "获取API文档的访问链接")
    @GetMapping("/links")
    public Result<Map<String, String>> getDocumentationLinks() {
        Map<String, String> links = new HashMap<>();
        
        String baseUrl = "http://localhost:9022";
        
        links.put("knife4j", baseUrl + "/doc.html");
        links.put("swaggerUI", baseUrl + "/swagger-ui.html");
        links.put("openApiJson", baseUrl + "/v3/api-docs");
        links.put("openApiYaml", baseUrl + "/v3/api-docs.yaml");
        links.put("testHello", baseUrl + "/test/hello");
        links.put("testStatus", baseUrl + "/test/status");
        
        return Result.success("文档链接获取成功", links);
    }

    private boolean isClassPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
} 