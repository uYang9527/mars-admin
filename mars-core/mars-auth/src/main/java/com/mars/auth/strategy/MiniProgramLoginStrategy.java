package com.mars.auth.strategy;

import com.mars.auth.LoginHelper;
import com.mars.auth.LoginRequest;
import com.mars.auth.LoginResult;
import com.mars.auth.LoginStrategy;
import com.mars.auth.enums.ClientType;
import com.mars.auth.enums.LoginType;
import com.mars.common.exception.BusinessException;
import com.mars.mall.entity.MallMember;
import com.mars.mall.service.MallMemberService;
import com.mars.wechat.WechatMiniProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信小程序登录策略（App端）
 * 通过 wx.login() 的 code 换取 openId 进行登录
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MiniProgramLoginStrategy implements LoginStrategy {

    private final WechatMiniProgramService wechatMiniProgramService;
    private final MallMemberService memberService;
    private final LoginHelper loginHelper;

    @Override
    public LoginType getType() {
        return LoginType.MINIPROGRAM;
    }

    @Override
    public ClientType[] supportedClients() {
        return new ClientType[]{ClientType.APP};
    }

    @Override
    public LoginResult login(LoginRequest request) {
        if (request.getWxCode() == null || request.getWxCode().isEmpty()) {
            throw new BusinessException("微信授权码不能为空");
        }

        // 1. code 换取 openId
        WechatMiniProgramService.MiniProgramLoginResult wxResult = wechatMiniProgramService.login(request.getWxCode());
        String openId = wxResult.getOpenId();

        // 2. 查找或创建会员
        MallMember member = memberService.getByOpenId(openId);
        if (member == null) {
            member = memberService.createByWechat(openId, wxResult.getUnionId());
            log.info("小程序新会员注册: openId={}", openId);
        }

        if (member.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        // 3. 获取手机号（如果有 phoneCode）
        if (request.getPhoneCode() != null && !request.getPhoneCode().isEmpty()) {
            try {
                String phoneNumber = wechatMiniProgramService.getPhoneNumber(request.getPhoneCode());
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    memberService.bindPhone(member.getId(), phoneNumber);
                }
            } catch (Exception e) {
                log.warn("获取手机号失败: {}", e.getMessage());
            }
        }

        // 4. 构建结果（小程序使用 member ID）
        LoginResult result = new LoginResult();
        result.setToken(null); // 小程序不使用 Sa-Token 的 token
        result.setUserId(member.getId());
        result.setNickname(member.getNickname());
        result.setAvatar(member.getAvatar());

        // 这里可以生成自定义的 app token
        // result.setToken(xxx);

        return result;
    }
}
