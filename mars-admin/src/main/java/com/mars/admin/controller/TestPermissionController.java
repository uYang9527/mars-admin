package com.mars.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mars.admin.framework.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试权限控制器
 * 用于验证Sa-Token权限配置是否正常工作
 *
 * @author Mars
 */
@Tag(name = "权限测试", description = "测试权限配置是否正常")
@RestController
@RequestMapping("/test-permission")
public class TestPermissionController {

    /**
     * 无需登录的测试接口
     */
    @Operation(summary = "无需登录的测试接口", description = "这个接口应该可以直接访问")
    @GetMapping("/public")
    public Result<String> publicTest() {
        return Result.success("这是一个公开接口，无需登录即可访问");
    }

    /**
     * 需要登录的测试接口
     */
    @Operation(summary = "需要登录的测试接口", description = "这个接口需要Token才能访问")
    @GetMapping("/private")
    public Result<Map<String, Object>> privateTest() {
        // 检查登录状态
        if (!StpUtil.isLogin()) {
            return Result.error("用户未登录");
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "这是一个需要登录的接口");
        data.put("loginId", StpUtil.getLoginId());
        data.put("tokenValue", StpUtil.getTokenValue());
        
        return Result.success("访问成功", data);
    }

    /**
     * 检查当前登录状态
     */
    @Operation(summary = "检查登录状态", description = "检查当前用户的登录状态")
    @GetMapping("/check-login")
    public Result<Map<String, Object>> checkLogin() {
        Map<String, Object> data = new HashMap<>();
        data.put("isLogin", StpUtil.isLogin());
        
        if (StpUtil.isLogin()) {
            data.put("loginId", StpUtil.getLoginId());
            data.put("tokenValue", StpUtil.getTokenValue());
            data.put("tokenTimeout", StpUtil.getTokenTimeout());
        }
        
        return Result.success("检查完成", data);
    }
} 