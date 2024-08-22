package com.mars.easy.admin.framework.redis;


import com.mars.easy.admin.framework.constant.GlobalConstant;
import com.mars.easy.admin.framework.redis.listener.ServiceToWsListener;
import com.mars.easy.admin.framework.redis.listener.UserPermissionChangeListener;
import com.mars.easy.admin.framework.redis.listener.WsToServiceListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 消息订阅配置
 *
 * @Author 程序员Mars
 * @date 2023/9/8 15:13
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "redis.listener.enable", havingValue = "true")
public class RedisMessageConfiguration {

    private final RedisConnectionFactory redisConnectionFactory;

    private final WsToServiceListener wsToServiceListener;

    private final ServiceToWsListener serviceToWsListener;

    private final UserPermissionChangeListener userPermissionChangeListener;


    /**
     * 配置订阅关系
     */
    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        // 订阅的channel
        PatternTopic patternTopic1 = new PatternTopic(GlobalConstant.WS_TO_SERVICE);
        container.addMessageListener(wsToServiceListener, patternTopic1);
        PatternTopic patternTopic2 = new PatternTopic(GlobalConstant.SERVICE_TO_WS);
        container.addMessageListener(serviceToWsListener, patternTopic2);
        PatternTopic patternTopic3 = new PatternTopic(GlobalConstant.CHANGE_PERMISSIONS_SIGNAL);
        container.addMessageListener(userPermissionChangeListener, patternTopic3);
        return container;
    }

}
