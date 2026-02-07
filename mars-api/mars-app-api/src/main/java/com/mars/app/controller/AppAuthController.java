package com.mars.app.controller;

import com.mars.auth.LoginRequest;
import com.mars.auth.LoginResult;
import com.mars.auth.LoginStrategyFactory;
import com.mars.auth.enums.ClientType;
import com.mars.auth.enums.LoginType;
import com.mars.common.exception.BusinessException;
import com.mars.common.result.Result;
import com.mars.sms.SmsServiceFactory;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * App端认证控制器
 * 支持：小程序登录、手机验证码登录、三方授权登录
 */
@RestController
@RequestMapping("/api/app/auth")
@RequiredArgsConstructor
public class AppAuthController {

    private final LoginStrategyFactory loginStrategyFactory;
    private final SmsServiceFactory smsServiceFactory;
    private final StringRedisTemplate redisTemplate;

    private static final String SMS_CODE_KEY = "sms:login:";

    /**
     * 统一登录接口
     *
     * @param request loginType: miniprogram / sms / social
     */
    @PostMapping("/login")
    public Result<LoginResult> login(@RequestBody LoginRequest request) {
        request.setClientType(ClientType.APP);
        if (request.getLoginType() == null) {
            request.setLoginType(LoginType.MINIPROGRAM);
        }
        LoginResult result = loginStrategyFactory.login(request);
        return Result.ok(result);
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms-code")
    public Result<Void> sendSmsCode(@RequestBody SmsCodeRequest request) {
        String phone = request.getPhone();
        if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
            throw new BusinessException("请输入正确的手机号");
        }

        String limitKey = "sms:limit:" + phone;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(limitKey))) {
            throw new BusinessException("发送太频繁，请稍后再试");
        }

        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        boolean success = smsServiceFactory.sendCode(phone, code);
        if (!success) {
            throw new BusinessException("短信发送失败");
        }

        redisTemplate.opsForValue().set(SMS_CODE_KEY + phone, code, 5, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(limitKey, "1", 60, TimeUnit.SECONDS);
        return Result.ok();
    }

    @Data
    public static class SmsCodeRequest {
        private String phone;
    }
}
