package com.mars.admin.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP配置类
 * 启用AspectJ自动代理，支持操作日志切面功能
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
public class AspectConfig {
    // 启用AspectJ自动代理，支持切面编程
    // exposeProxy = true 表示暴露代理对象，允许在方法内部获取代理对象
} 