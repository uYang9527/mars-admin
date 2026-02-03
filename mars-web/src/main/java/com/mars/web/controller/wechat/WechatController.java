package com.mars.web.controller.wechat;

import com.mars.common.result.Result;
import com.mars.system.service.wechat.WechatMiniProgramService;
import com.mars.system.service.wechat.WechatMpService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信相关接口控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/wechat")
@RequiredArgsConstructor
public class WechatController {

    private final WechatMiniProgramService miniProgramService;
    private final WechatMpService mpService;

    // ============ 小程序接口 ============

    /**
     * 小程序登录
     */
    @PostMapping("/miniprogram/login")
    public Result<Map<String, Object>> miniProgramLogin(@RequestBody MiniProgramLoginRequest request) {
        try {
            WechatMiniProgramService.MiniProgramLoginResult result = miniProgramService.login(request.getCode());
            
            Map<String, Object> data = new HashMap<>();
            data.put("openId", result.getOpenId());
            data.put("unionId", result.getUnionId());
            // sessionKey 不要返回给前端，用于后续解密等操作
            
            return Result.ok(data);
        } catch (Exception e) {
            log.error("小程序登录失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取小程序用户手机号
     */
    @PostMapping("/miniprogram/phone")
    public Result<Map<String, String>> getMiniProgramPhone(@RequestBody GetPhoneRequest request) {
        try {
            String phone = miniProgramService.getPhoneNumber(request.getCode());
            Map<String, String> data = new HashMap<>();
            data.put("phone", phone);
            return Result.ok(data);
        } catch (Exception e) {
            log.error("获取手机号失败", e);
            return Result.fail(e.getMessage());
        }
    }

    // ============ 公众号接口 ============

    /**
     * 公众号消息回调验证（GET请求）
     */
    @GetMapping("/callback")
    public String mpCallback(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr) {
        
        if (mpService.checkSignature(signature, timestamp, nonce)) {
            log.info("公众号回调验证成功");
            return echostr;
        }
        log.warn("公众号回调验证失败");
        return "fail";
    }

    /**
     * 公众号消息回调处理（POST请求）
     */
    @PostMapping("/callback")
    public String mpMessageCallback(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestBody String xmlContent) {
        
        if (!mpService.checkSignature(signature, timestamp, nonce)) {
            log.warn("公众号消息回调签名验证失败");
            return "";
        }
        
        return mpService.handleMessage(xmlContent);
    }

    /**
     * 获取公众号OAuth授权URL
     */
    @GetMapping("/mp/oauth-url")
    public Result<String> getOAuthUrl(
            @RequestParam("redirectUri") String redirectUri,
            @RequestParam(value = "state", defaultValue = "") String state,
            @RequestParam(value = "scope", defaultValue = "snsapi_userinfo") String scope) {
        try {
            String url = mpService.getOAuthUrl(redirectUri, state, scope);
            return Result.ok(url);
        } catch (Exception e) {
            log.error("获取OAuth授权URL失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 公众号OAuth登录
     */
    @PostMapping("/mp/oauth-login")
    public Result<Map<String, Object>> mpOAuthLogin(@RequestBody MpOAuthLoginRequest request) {
        try {
            WechatMpService.MpOAuthResult result = mpService.oauthLogin(request.getCode());
            
            Map<String, Object> data = new HashMap<>();
            data.put("openId", result.getOpenId());
            data.put("unionId", result.getUnionId());
            data.put("nickname", result.getNickname());
            data.put("headImgUrl", result.getHeadImgUrl());
            data.put("sex", result.getSex());
            
            return Result.ok(data);
        } catch (Exception e) {
            log.error("公众号OAuth登录失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 创建公众号菜单
     */
    @PostMapping("/mp/menu/create")
    public Result<Void> createMenu(@RequestBody String menuJson) {
        try {
            mpService.createMenu(menuJson);
            return Result.ok();
        } catch (Exception e) {
            log.error("创建菜单失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取公众号菜单
     */
    @GetMapping("/mp/menu")
    public Result<String> getMenu() {
        try {
            String menu = mpService.getMenu();
            return Result.ok(menu);
        } catch (Exception e) {
            log.error("获取菜单失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 删除公众号菜单
     */
    @DeleteMapping("/mp/menu")
    public Result<Void> deleteMenu() {
        try {
            mpService.deleteMenu();
            return Result.ok();
        } catch (Exception e) {
            log.error("删除菜单失败", e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 同步菜单到微信（根据配置创建）
     */
    @PostMapping("/mp/menu/sync")
    public Result<Void> syncMenu(@RequestBody SyncMenuRequest request) {
        try {
            mpService.createMenu(request.getMenuConfig());
            return Result.ok();
        } catch (Exception e) {
            log.error("同步菜单失败", e);
            return Result.fail(e.getMessage());
        }
    }

    // ============ 请求对象 ============

    @Data
    public static class MiniProgramLoginRequest {
        private String code;
    }

    @Data
    public static class GetPhoneRequest {
        private String code;
    }

    @Data
    public static class MpOAuthLoginRequest {
        private String code;
    }

    @Data
    public static class SyncMenuRequest {
        private String menuConfig;
    }
}
