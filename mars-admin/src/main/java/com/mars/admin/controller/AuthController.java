package com.mars.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mars.admin.framework.common.Result;
import com.mars.admin.common.request.LoginRequest;
import com.mars.admin.common.request.UpdateProfileRequest;
import com.mars.admin.common.request.ChangePasswordRequest;
import com.mars.admin.common.response.LoginResponse;
import com.mars.admin.entity.SysUser;
import com.mars.admin.service.IAuthService;
import com.mars.admin.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * 认证控制器
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-07-05
 */
@Tag(name = "用户认证", description = "用户登录、登出等认证相关操作")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private ISysUserService sysUserService;

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
    public Result<SysUser> getUserInfo() {
        SysUser currentUser = authService.getCurrentUser();
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
    @GetMapping("/tokenInfo")
    public Result<Map<String, Object>> getTokenInfo() {
        if (!StpUtil.isLogin()) {
            return Result.error("用户未登录");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("tokenName", StpUtil.getTokenName());
        data.put("tokenValue", StpUtil.getTokenValue());
        data.put("isLogin", StpUtil.isLogin());
        data.put("loginId", StpUtil.getLoginId());
        data.put("loginType", StpUtil.getLoginType());
        data.put("tokenTimeout", StpUtil.getTokenTimeout());
        data.put("sessionTimeout", StpUtil.getSessionTimeout());
        data.put("tokenSessionTimeout", StpUtil.getTokenSessionTimeout());
        data.put("tokenActiveTimeout", StpUtil.getTokenActiveTimeout());

        return Result.success("获取Token信息成功", data);
    }

    /**
     * 生成验证码
     */
    @Operation(summary = "生成验证码", description = "生成图片验证码")
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成验证码
        String captcha = generateCaptcha();
        
        // 将验证码存储到session中
        request.getSession().setAttribute("captcha", captcha.toLowerCase());
        
        // 生成验证码图片
        BufferedImage image = createCaptchaImage(captcha);
        
        // 设置响应头
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // 输出图片
        ImageIO.write(image, "png", response.getOutputStream());
    }

    /**
     * 刷新Token
     */
    @Operation(summary = "刷新Token", description = "刷新当前用户的Token")
    @PostMapping("/refresh")
    public Result<Map<String, Object>> refreshToken() {
        if (!StpUtil.isLogin()) {
            return Result.error("用户未登录");
        }

        // 刷新Token
        StpUtil.renewTimeout(StpUtil.getTokenTimeout());
        
        Map<String, Object> data = new HashMap<>();
        data.put("tokenValue", StpUtil.getTokenValue());
        data.put("tokenTimeout", StpUtil.getTokenTimeout());

        return Result.success("Token刷新成功", data);
    }

    /**
     * 踢出用户
     */
    @Operation(summary = "踢出用户", description = "强制指定用户下线")
    @PostMapping("/kickout/{userId}")
    public Result<String> kickout(@Parameter(description = "用户ID") @PathVariable Long userId) {
        // 检查当前用户权限（这里可以添加权限验证）
        if (!StpUtil.isLogin()) {
            return Result.error("用户未登录");
        }

        // 踢出指定用户
        StpUtil.kickout(userId);
        
        return Result.success("用户已被踢出");
    }

    /**
     * 生成随机验证码
     */
    private String generateCaptcha() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }
        return captcha.toString();
    }

    /**
     * 创建验证码图片
     */
    private BufferedImage createCaptchaImage(String captcha) {
        int width = 120;
        int height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 20));

        // 生成随机颜色并绘制验证码
        Random random = new Random();
        for (int i = 0; i < captcha.length(); i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawString(String.valueOf(captcha.charAt(i)), 20 + i * 25, 25);
        }

        // 添加干扰线
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawLine(random.nextInt(width), random.nextInt(height), 
                      random.nextInt(width), random.nextInt(height));
        }

        g.dispose();
        return image;
    }

    /**
     * 更新个人信息
     */
    @Operation(summary = "更新个人信息", description = "当前用户更新自己的个人信息")
    @PutMapping("/updateProfile")
    public Result<String> updateProfile(@Parameter(description = "个人信息更新请求", required = true)
                                        @Validated @RequestBody UpdateProfileRequest request) {
        if (!StpUtil.isLogin()) {
            return Result.error("用户未登录");
        }

        // 获取当前用户ID
        Long currentUserId = Long.valueOf(StpUtil.getLoginId().toString());
        
        // 构建更新的用户信息
        SysUser updateUser = new SysUser();
        updateUser.setId(currentUserId);
        updateUser.setNickname(request.getNickname());
        updateUser.setRealName(request.getRealName());
        updateUser.setEmail(request.getEmail());
        updateUser.setPhone(request.getPhone());
        updateUser.setGender(request.getGender());
        updateUser.setAvatar(request.getAvatar());
        updateUser.setBirthday(request.getBirthday());
        updateUser.setRemark(request.getRemark());

        boolean success = sysUserService.updatePersonalInfo(updateUser);
        
        if (success) {
            return Result.success("个人信息更新成功");
        } else {
            return Result.error("个人信息更新失败");
        }
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码", description = "当前用户修改自己的密码")
    @PostMapping("/changePassword")
    public Result<String> changePassword(@Parameter(description = "修改密码请求", required = true)
                                         @Validated @RequestBody ChangePasswordRequest request) {
        if (!StpUtil.isLogin()) {
            return Result.error("用户未登录");
        }

        // 获取当前用户ID
        Long currentUserId = Long.valueOf(StpUtil.getLoginId().toString());
        
        boolean success = authService.changePassword(currentUserId, request.getOldPassword(), request.getNewPassword());
        
        if (success) {
            return Result.success("密码修改成功");
        } else {
            return Result.error("密码修改失败，请检查原密码是否正确");
        }
    }
} 