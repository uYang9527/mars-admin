package com.mars.easy.admin.framework.redis;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @ClassName RedisTemplateClient
 * @Author 程序员Mars
 * @Date 2024/2/29 9:47
 * @Version 1.0
 */
public interface RedisTemplateClient {
    RedisTemplate getTemplate();
}
