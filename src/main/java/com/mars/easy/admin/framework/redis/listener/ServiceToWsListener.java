package com.mars.easy.admin.framework.redis.listener;


import com.mars.easy.admin.framework.entity.TransferMessage;
import com.mars.easy.admin.framework.redis.handler.ServiceToWsMsgHandler;
import com.mars.easy.admin.framework.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * redis消息listener, 用于service to websocket 消息的推送
 *
 * @Author 程序员Mars
 * @date 2023/9/8 10:12
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ServiceToWsListener implements MessageListener {

    private final List<ServiceToWsMsgHandler> serviceToWsMsgHandlers;

    private final RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("订阅到的消息");
        TransferMessage tm = (TransferMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.info(" [service-to-ws] tm = " + JsonUtils.toJsonString(tm));
        // 调用所有实现了TransferMessageHandler接口的处理器
        for (ServiceToWsMsgHandler handler : serviceToWsMsgHandlers) {
            handler.handleTransferMessage(tm);
        }
    }

}
