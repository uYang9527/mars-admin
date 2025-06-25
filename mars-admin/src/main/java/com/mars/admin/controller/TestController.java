package com.mars.admin.controller;

import com.mars.admin.framework.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于验证OpenAPI文档是否正常工作
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Tag(name = "测试接口", description = "用于测试OpenAPI文档功能")
@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(summary = "Hello World", description = "简单的测试接口，无需登录")
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello World! OpenAPI 3 正常工作！");
    }

    @Operation(summary = "获取当前时间", description = "获取服务器当前时间")
    @GetMapping("/time")
    public Result<Map<String, Object>> getCurrentTime() {
        Map<String, Object> data = new HashMap<>();
        data.put("currentTime", LocalDateTime.now());
        data.put("timestamp", System.currentTimeMillis());
        data.put("timezone", "Asia/Shanghai");
        return Result.success("获取时间成功", data);
    }

    @Operation(summary = "Echo测试", description = "回显输入的消息")
    @GetMapping("/echo")
    public Result<String> echo(
            @Parameter(description = "要回显的消息", required = true, example = "Hello")
            @RequestParam String message) {
        return Result.success("Echo: " + message);
    }

    @Operation(summary = "系统状态", description = "获取系统运行状态")
    @GetMapping("/status")
    public Result<Map<String, Object>> getSystemStatus() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "running");
        data.put("uptime", System.currentTimeMillis());
        data.put("javaVersion", System.getProperty("java.version"));
        data.put("springBootVersion", "3.2.0");
        data.put("openApiVersion", "3.0.3");
        return Result.success("系统运行正常", data);
    }
} 