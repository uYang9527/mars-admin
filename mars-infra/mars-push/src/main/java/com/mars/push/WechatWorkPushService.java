package com.mars.push;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 企业微信消息推送服务
 */
@Slf4j
public class WechatWorkPushService implements PushService {

    public static final String PROVIDER_TYPE = "wechat_work";

    private final String signName;
    private final String tokenId;

    public WechatWorkPushService(String signName, String tokenId) {
        this.signName = signName;
        this.tokenId = tokenId;
        log.info("初始化企业微信消息推送服务, signName: {}", signName);
    }

    @Override
    public String getProviderType() {
        return PROVIDER_TYPE;
    }

    @Override
    public String getProviderName() {
        return "企业微信";
    }

    @Override
    public boolean pushToUser(String userId, String title, String content, Map<String, String> extras) {
        // TODO: 实现企业微信消息推送API调用
        log.info("【企业微信】推送给用户: {}, 标题: {}, 内容: {}", userId, title, content);
        return true;
    }

    @Override
    public boolean pushToUsers(List<String> userIds, String title, String content, Map<String, String> extras) {
        // TODO: 实现企业微信消息推送API调用
        log.info("【企业微信】推送给多个用户: {}, 标题: {}, 内容: {}", userIds, title, content);
        return true;
    }

    @Override
    public boolean pushToAll(String title, String content, Map<String, String> extras) {
        // TODO: 实现企业微信消息推送API调用
        log.info("【企业微信】推送给所有用户, 标题: {}, 内容: {}", title, content);
        return true;
    }

    @Override
    public boolean pushToTags(List<String> tags, String title, String content, Map<String, String> extras) {
        // TODO: 实现企业微信消息推送API调用
        log.info("【企业微信】推送给标签: {}, 标题: {}, 内容: {}", tags, title, content);
        return true;
    }

    @Override
    public boolean pushToDevice(String registrationId, String title, String content, Map<String, String> extras) {
        // TODO: 实现企业微信消息推送API调用
        log.info("【企业微信】推送给设备: {}, 标题: {}, 内容: {}", registrationId, title, content);
        return true;
    }
}
