package com.mars.admin.controller.auth;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.mars.common.exception.BusinessException;
import com.mars.common.result.Result;
import com.mars.system.entity.SysMenu;
import com.mars.system.entity.SysRole;
import com.mars.system.entity.SysUser;
import com.mars.system.entity.SysUserRole;
import com.mars.system.service.*;
import com.mars.system.service.sms.SmsServiceFactory;
import com.mars.system.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserService userService;
    private final SysMenuService menuService;
    private final SysLoginLogService loginLogService;
    private final SysRoleService roleService;
    private final SysUserRoleService userRoleService;
    private final SystemConfigHelper configHelper;
    private final StringRedisTemplate redisTemplate;
    private final SmsServiceFactory smsServiceFactory;

    private static final String LOGIN_RETRY_KEY = "login:retry:";
    private static final String CAPTCHA_KEY = "captcha:";
    private static final String SMS_CODE_KEY = "sms:login:";

    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public Result<Map<String, Object>> captcha() {
        String uuid = IdUtil.simpleUUID();
        String captchaType = configHelper.getCaptchaType();
        String code;
        String imageBase64;

        // 根据配置生成不同类型的验证码
        switch (captchaType) {
            case "math":
                // 数学验证码 (简单加减法)
                int a = (int) (Math.random() * 10);
                int b = (int) (Math.random() * 10);
                code = String.valueOf(a + b);
                cn.hutool.captcha.generator.MathGenerator mathGenerator = new cn.hutool.captcha.generator.MathGenerator(1);
                LineCaptcha mathCaptcha = CaptchaUtil.createLineCaptcha(130, 48);
                mathCaptcha.setGenerator(mathGenerator);
                mathCaptcha.createCode();
                code = mathCaptcha.getCode(); // MathGenerator 会自动计算结果
                imageBase64 = "data:image/png;base64," + mathCaptcha.getImageBase64();
                break;
            case "circle":
                CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(130, 48, 4, 20);
                code = circleCaptcha.getCode();
                imageBase64 = "data:image/png;base64," + circleCaptcha.getImageBase64();
                break;
            case "shear":
                ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(130, 48, 4, 4);
                code = shearCaptcha.getCode();
                imageBase64 = "data:image/png;base64," + shearCaptcha.getImageBase64();
                break;
            default:
                // 默认线条干扰验证码
                LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(130, 48, 4, 50);
                code = lineCaptcha.getCode();
                imageBase64 = "data:image/png;base64," + lineCaptcha.getImageBase64();
        }

        // 保存到Redis，5分钟有效
        redisTemplate.opsForValue().set(CAPTCHA_KEY + uuid, code.toLowerCase(), 5, TimeUnit.MINUTES);

        Map<String, Object> result = new HashMap<>();
        result.put("uuid", uuid);
        result.put("img", imageBase64);
        return Result.ok(result);
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms-code")
    public Result<Void> sendSmsCode(@RequestBody SmsCodeRequest request) {
        String phone = request.getPhone();
        
        // 验证手机号格式
        if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException("请输入正确的手机号");
        }

        // 检查是否频繁发送（1分钟内只能发送一次）
        String limitKey = "sms:limit:" + phone;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(limitKey))) {
            throw new BusinessException("发送太频繁，请稍后再试");
        }

        // 生成6位数字验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        // 调用短信服务发送
        boolean success = smsServiceFactory.sendCode(phone, code);
        if (!success) {
            throw new BusinessException("短信发送失败，请稍后重试");
        }

        // 保存验证码到Redis，5分钟有效
        redisTemplate.opsForValue().set(SMS_CODE_KEY + phone, code, 5, TimeUnit.MINUTES);
        // 设置发送限制，60秒内不能重复发送
        redisTemplate.opsForValue().set(limitKey, "1", 60, TimeUnit.SECONDS);

        return Result.ok();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        // 验证码校验
        if (configHelper.isCaptchaEnabled()) {
            String captchaType = configHelper.getCaptchaType();

            if ("image".equals(captchaType)) {
                // 图片验证码校验
                if (request.getUuid() == null || request.getCode() == null) {
                    throw new BusinessException("请输入验证码");
                }
                String cacheCode = redisTemplate.opsForValue().get(CAPTCHA_KEY + request.getUuid());
                redisTemplate.delete(CAPTCHA_KEY + request.getUuid());
                if (cacheCode == null || !cacheCode.equalsIgnoreCase(request.getCode())) {
                    throw new BusinessException("验证码错误或已过期");
                }
            } else if ("slider".equals(captchaType)) {
                // 滑块验证码（前端已验证，后端简单校验标识）
                if (!"slider_verified".equals(request.getCode())) {
                    throw new BusinessException("请完成滑块验证");
                }
            } else if ("sms".equals(captchaType)) {
                // 短信验证码校验
                if (request.getPhone() == null || request.getCode() == null) {
                    throw new BusinessException("请输入手机号和验证码");
                }
                String cacheCode = redisTemplate.opsForValue().get("sms:login:" + request.getPhone());
                if (cacheCode == null || !cacheCode.equals(request.getCode())) {
                    throw new BusinessException("短信验证码错误或已过期");
                }
                redisTemplate.delete("sms:login:" + request.getPhone());
            }
        }

        String ip = IpUtils.getIpAddr(httpRequest);
        String userAgentStr = httpRequest.getHeader("User-Agent");
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        String browser = userAgent != null ? userAgent.getBrowser().getName() : "Unknown";
        String os = userAgent != null ? userAgent.getOs().getName() : "Unknown";

        // 检查登录重试次数
        String retryKey = LOGIN_RETRY_KEY + request.getUsername();
        String retryCountStr = redisTemplate.opsForValue().get(retryKey);
        int retryCount = retryCountStr != null ? Integer.parseInt(retryCountStr) : 0;
        int maxRetry = configHelper.getMaxRetryCount();
        int lockTime = configHelper.getLockTime();

        if (retryCount >= maxRetry) {
            Long ttl = redisTemplate.getExpire(retryKey, TimeUnit.MINUTES);
            loginLogService.recordLog(request.getUsername(), 1, "账号已锁定", ip, browser, os);
            throw new BusinessException("账号已锁定，请" + ttl + "分钟后重试");
        }

        SysUser user = userService.getByUsername(request.getUsername());
        if (user == null) {
            incrementRetryCount(retryKey, lockTime);
            loginLogService.recordLog(request.getUsername(), 1, "用户不存在", ip, browser, os);
            throw new BusinessException("用户名或密码错误");
        }
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            incrementRetryCount(retryKey, lockTime);
            loginLogService.recordLog(request.getUsername(), 1, "密码错误", ip, browser, os);
            int remaining = maxRetry - retryCount - 1;
            String msg = remaining > 0 ? "用户名或密码错误，还剩" + remaining + "次机会" : "用户名或密码错误，账号已锁定";
            throw new BusinessException(msg);
        }
        if (user.getStatus() != 1) {
            loginLogService.recordLog(request.getUsername(), 1, "用户已被禁用", ip, browser, os);
            throw new BusinessException("用户已被禁用");
        }

        // 清除重试次数
        redisTemplate.delete(retryKey);

        // 单点登录：踢掉其他设备
        if (configHelper.isSingleLogin()) {
            StpUtil.logout(user.getId());
        }

        // 登录
        StpUtil.login(user.getId());
        // 记录登录成功日志
        loginLogService.recordLog(request.getUsername(), 0, "登录成功", ip, browser, os);
        // 返回token和用户信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", StpUtil.getTokenValue());
        user.setPassword(null);
        result.put("user", user);
        return Result.ok(result);
    }

    /**
     * 增加重试次数
     */
    private void incrementRetryCount(String key, int lockTime) {
        String countStr = redisTemplate.opsForValue().get(key);
        int count = countStr != null ? Integer.parseInt(countStr) + 1 : 1;
        redisTemplate.opsForValue().set(key, String.valueOf(count), lockTime, TimeUnit.MINUTES);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.ok();
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> info() {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = userService.getDetail(userId);
        List<String> roles = userService.getRoleCodes(userId);
        List<String> permissions = userService.getPermissions(userId);
        List<SysMenu> menus = menuService.getUserMenuTree(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("roles", roles);
        result.put("permissions", permissions);
        result.put("menus", menus);
        return Result.ok(result);
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/profile")
    public Result<SysUser> profile() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.ok(userService.getDetail(userId));
    }

    /**
     * 更新个人信息
     */
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody SysUser user) {
        Long userId = StpUtil.getLoginIdAsLong();
        userService.updateProfile(userId, user);
        return Result.ok();
    }

    /**
     * 修改密码
     */
    @PostMapping("/password")
    public Result<Void> updatePassword(@RequestBody PasswordRequest request) {
        // 验证密码规则
        configHelper.validatePassword(request.getNewPassword());

        Long userId = StpUtil.getLoginIdAsLong();
        userService.updatePassword(userId, request.getOldPassword(), request.getNewPassword());
        return Result.ok();
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterRequest request) {
        // 检查是否开放注册
        if (!configHelper.isRegisterEnabled()) {
            throw new BusinessException("系统暂未开放注册");
        }

        // 验证码校验（注册也需要验证码）
        if (configHelper.isCaptchaEnabled()) {
            if (request.getUuid() == null || request.getCode() == null) {
                throw new BusinessException("请输入验证码");
            }
            String cacheCode = redisTemplate.opsForValue().get(CAPTCHA_KEY + request.getUuid());
            redisTemplate.delete(CAPTCHA_KEY + request.getUuid());
            if (cacheCode == null || !cacheCode.equalsIgnoreCase(request.getCode())) {
                throw new BusinessException("验证码错误或已过期");
            }
        }

        // 验证必填字段
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }

        // 验证用户名格式
        if (!request.getUsername().matches("^[a-zA-Z0-9_]{4,20}$")) {
            throw new BusinessException("用户名只能包含字母、数字、下划线，长度4-20位");
        }

        // 检查用户名是否已存在
        SysUser existUser = userService.getByUsername(request.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 验证密码规则
        configHelper.validatePassword(request.getPassword());

        // 邮箱验证
        if (configHelper.isRegisterVerifyEmail()) {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                throw new BusinessException("请输入邮箱");
            }
            // TODO: 邮箱验证码校验
        }

        // 手机验证
        if (configHelper.isRegisterVerifyPhone()) {
            if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
                throw new BusinessException("请输入手机号");
            }
            // TODO: 手机验证码校验
        }

        // 创建用户
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword()));
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setGender(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 根据配置决定用户初始状态
        if (configHelper.isRegisterNeedAudit()) {
            user.setStatus(2); // 待审核
        } else {
            user.setStatus(1); // 正常
        }

        userService.save(user);

        // 分配默认角色
        String defaultRoleCode = configHelper.getRegisterDefaultRole();
        if (defaultRoleCode != null && !defaultRoleCode.isEmpty()) {
            SysRole role = roleService.getByCode(defaultRoleCode);
            if (role != null) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(role.getId());
                userRoleService.save(userRole);
            }
        }

        if (configHelper.isRegisterNeedAudit()) {
            return Result.ok();
        }
        return Result.ok();
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
        private String uuid;  // 验证码key
        private String code;  // 验证码
        private String phone; // 手机号（短信验证码时使用）
        private Boolean rememberMe; // 记住我
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String nickname;
        private String email;
        private String phone;
        private String uuid;  // 验证码key
        private String code;  // 验证码
    }

    @Data
    public static class PasswordRequest {
        private String oldPassword;
        private String newPassword;
    }

    @Data
    public static class SmsCodeRequest {
        private String phone;
    }
}
