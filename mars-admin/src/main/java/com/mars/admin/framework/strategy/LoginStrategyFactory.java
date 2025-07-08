package com.mars.admin.framework.strategy;

import com.mars.admin.common.enums.LoginType;
import com.mars.admin.framework.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 登录策略工厂
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-01-16
 */
@Slf4j
@Component
public class LoginStrategyFactory {

    private final Map<LoginType, LoginStrategy> strategyMap;

    /**
     * 构造函数，自动注入所有登录策略实现
     */
    @Autowired
    public LoginStrategyFactory(List<LoginStrategy> loginStrategies) {
        this.strategyMap = loginStrategies.stream()
            .collect(Collectors.toMap(
                LoginStrategy::getLoginType,
                Function.identity(),
                (existing, replacement) -> {
                    log.warn("发现重复的登录策略: {}, 使用: {}", 
                        existing.getClass().getSimpleName(), 
                        replacement.getClass().getSimpleName());
                    return replacement;
                }
            ));
        
        log.info("登录策略工厂初始化完成，支持的登录类型: {}", 
            strategyMap.keySet().stream()
                .map(LoginType::getDescription)
                .collect(Collectors.joining(", ")));
    }

    /**
     * 根据登录类型获取对应的登录策略
     *
     * @param loginType 登录类型
     * @return 登录策略实例
     */
    public LoginStrategy getStrategy(LoginType loginType) {
        LoginStrategy strategy = strategyMap.get(loginType);
        if (strategy == null) {
            throw BusinessException.of("不支持的登录类型: " + loginType.getDescription());
        }
        return strategy;
    }

    /**
     * 根据登录类型代码获取对应的登录策略
     *
     * @param loginTypeCode 登录类型代码
     * @return 登录策略实例
     */
    public LoginStrategy getStrategy(String loginTypeCode) {
        try {
            LoginType loginType = LoginType.fromCode(loginTypeCode);
            return getStrategy(loginType);
        } catch (IllegalArgumentException e) {
            throw BusinessException.of("不支持的登录类型: " + loginTypeCode);
        }
    }

    /**
     * 检查是否支持指定的登录类型
     *
     * @param loginType 登录类型
     * @return 是否支持
     */
    public boolean isSupported(LoginType loginType) {
        return strategyMap.containsKey(loginType);
    }

    /**
     * 检查是否支持指定的登录类型代码
     *
     * @param loginTypeCode 登录类型代码
     * @return 是否支持
     */
    public boolean isSupported(String loginTypeCode) {
        try {
            LoginType loginType = LoginType.fromCode(loginTypeCode);
            return isSupported(loginType);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 获取所有支持的登录类型
     *
     * @return 支持的登录类型列表
     */
    public List<LoginType> getSupportedLoginTypes() {
        return strategyMap.keySet().stream()
            .sorted((a, b) -> a.getCode().compareTo(b.getCode()))
            .collect(Collectors.toList());
    }
} 