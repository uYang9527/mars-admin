package com.mars.easy.admin.framework.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @ClassName EventPublisher
 * @Author 程序员Mars
 * @Date 2024/2/29 15:54
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(BaseEvent<?> event) {
        eventPublisher.publishEvent(event);
    }

}
