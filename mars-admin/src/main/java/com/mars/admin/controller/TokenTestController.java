package com.mars.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mars.admin.framework.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Token测试控制器
 * 用于验证Sa-Token的Token验证功能
 *
 * @author Mars
 */
@Slf4j
@Tag(name = "Token测试", description = "测试Token验证功能")
@RestController
@RequestMapping("/token-test")
public class TokenTestController {

    /**
     * 需要Token的接口
     */
    @Operation(summary = "需要Token的接口", description = "这个接口需要在Header中携带Token才能访问")
    @GetMapping("/protected")
    public Result<Map<String, Object>> protectedEndpoint() {
        log.info("访问受保护的接口");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Token验证成功！");
        data.put("userId", StpUtil.getLoginId());
        data.put("tokenValue", StpUtil.getTokenValue());
        data.put("isLogin", StpUtil.isLogin());
        
        return Result.success("访问成功", data);
    }

    /**
     * 公开接口（在白名单中）
     */
    @Operation(summary = "公开接口", description = "这个接口无需Token即可访问")
    @GetMapping("/public")
    public Result<String> publicEndpoint() {
        log.info("访问公开接口");
        return Result.success("这是公开接口，无需Token即可访问");
    }

    /**
     * 获取当前登录状态
     */
    @Operation(summary = "获取登录状态", description = "获取当前用户的登录状态信息")
    @GetMapping("/status")
    public Result<Map<String, Object>> getLoginStatus() {
        log.info("获取登录状态");
        
        Map<String, Object> data = new HashMap<>();
        data.put("isLogin", StpUtil.isLogin());
        
        if (StpUtil.isLogin()) {
            data.put("userId", StpUtil.getLoginId());
            data.put("tokenValue", StpUtil.getTokenValue());
            data.put("tokenTimeout", StpUtil.getTokenTimeout());
        }
        
        return Result.success("获取状态成功", data);
    }
} 