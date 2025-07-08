package com.mars.admin.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SQL日志配置属性
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@Data
@Component
@ConfigurationProperties(prefix = "sql-log")
public class SqlLogProperties {
    
    /**
     * SQL日志页面登录用户名
     */
    private String username = "admin";
    
    /**
     * SQL日志页面登录密码
     */
    private String password = "admin";
} 