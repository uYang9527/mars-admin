package com.mars.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mars.admin.framework.common.Result;
import com.mars.admin.common.request.LoginRequest;
import com.mars.admin.common.response.LoginResponse;
import com.mars.admin.service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Tag(name = "用户认证", description = "用户登录、登出等认证相关操作")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户账号密码登录接口")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Parameter(description = "登录请求", required = true)
                                       @Validated @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return Result.success("登录成功", response);
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出", description = "用户退出登录接口")
    @PostMapping("/logout")
    public Result<String> logout() {
        authService.logout();
        return Result.success("登出成功");
    }

    /**
     * 获取当前登录用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/userinfo")
    public Result<Object> getUserInfo() {
        Object currentUser = authService.getCurrentUser();
        return Result.success("获取用户信息成功", currentUser);
    }

    /**
     * 检查登录状态
     */
    @Operation(summary = "检查登录状态", description = "检查当前用户是否已登录")
    @GetMapping("/check")
    public Result<Map<String, Object>> checkLogin() {
        Map<String, Object> data = new HashMap<>();
        data.put("isLogin", StpUtil.isLogin());

        if (StpUtil.isLogin()) {
            data.put("loginId", StpUtil.getLoginId());
            data.put("tokenValue", StpUtil.getTokenValue());
            data.put("tokenTimeout", StpUtil.getTokenTimeout());
        }

        return Result.success("检查登录状态成功", data);
    }

    /**
     * 获取 Token 信息
     */
    @Operation(summary = "获取Token信息", description = "获取当前用户的Token详细信息")
    @GetMapping("/token-info")
    public Result<Object> getTokenInfo() {
        if (!StpUtil.isLogin()) {
            return Result.error("用户未登录");
        }

        Object tokenInfo = StpUtil.getTokenInfo();
        return Result.success("获取Token信息成功", tokenInfo);
    }
} 