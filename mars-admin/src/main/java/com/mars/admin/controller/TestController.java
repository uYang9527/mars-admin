package com.mars.admin.controller;

import com.mars.admin.framework.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于测试跨域配置和基本功能
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@RestController
@RequestMapping("/test")
@Tag(name = "测试接口", description = "用于测试跨域配置和基本功能")
public class TestController {

    /**
     * 测试GET请求
     */
    @GetMapping("/get")
    @Operation(summary = "测试GET请求", description = "测试跨域GET请求")
    public Result<Map<String, Object>> testGet() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "GET请求成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "GET");
        return Result.success(data);
    }

    /**
     * 测试POST请求
     */
    @PostMapping("/post")
    @Operation(summary = "测试POST请求", description = "测试跨域POST请求")
    public Result<Map<String, Object>> testPost(@RequestBody(required = false) Map<String, Object> requestData) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "POST请求成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "POST");
        data.put("requestData", requestData);
        return Result.success(data);
    }

    /**
     * 测试PUT请求
     */
    @PutMapping("/put")
    @Operation(summary = "测试PUT请求", description = "测试跨域PUT请求")
    public Result<Map<String, Object>> testPut(@RequestBody(required = false) Map<String, Object> requestData) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "PUT请求成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "PUT");
        data.put("requestData", requestData);
        return Result.success(data);
    }

    /**
     * 测试DELETE请求
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "测试DELETE请求", description = "测试跨域DELETE请求")
    public Result<Map<String, Object>> testDelete(@PathVariable Long id) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "DELETE请求成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "DELETE");
        data.put("deletedId", id);
        return Result.success(data);
    }

    /**
     * 测试OPTIONS请求（预检请求）
     */
    @RequestMapping(value = "/options", method = RequestMethod.OPTIONS)
    @Operation(summary = "测试OPTIONS请求", description = "测试跨域OPTIONS预检请求")
    public Result<Map<String, Object>> testOptions() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "OPTIONS请求成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "OPTIONS");
        return Result.success(data);
    }

    /**
     * 测试带认证的请求（需要登录）
     */
    @GetMapping("/auth")
    @Operation(summary = "测试认证请求", description = "测试需要登录的跨域请求")
    public Result<Map<String, Object>> testAuth() {
        Map<String, Object> data = new HashMap<>();
        data.put("message", "认证请求成功");
        data.put("timestamp", LocalDateTime.now());
        data.put("method", "GET");
        data.put("needAuth", true);
        return Result.success(data);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查服务是否正常运行")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now());
        data.put("service", "Mars Admin");
        data.put("version", "1.0.0");
        return Result.success(data);
    }
} 