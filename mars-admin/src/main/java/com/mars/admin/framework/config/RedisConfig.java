package com.mars.admin.framework.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Redis配置类
 * 配置RedisTemplate，确保与SaToken兼容
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Configuration
public class RedisConfig {

    /**
     * 兼容序列化器 - 能够处理JDK序列化和Jackson序列化两种格式
     */
    public static class CompatibleRedisSerializer implements RedisSerializer<Object> {
        
        private final Jackson2JsonRedisSerializer<Object> jsonSerializer;
        
        public CompatibleRedisSerializer() {
            this.jsonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
            ObjectMapper om = new ObjectMapper();
            om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
            
            // 配置JavaTime模块处理LocalDateTime等
            om.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            // 配置日期序列化格式
            om.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            
            jsonSerializer.setObjectMapper(om);
        }
        
        @Override
        public byte[] serialize(Object obj) throws SerializationException {
            // 新数据统一使用Jackson序列化
            return jsonSerializer.serialize(obj);
        }
        
        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            
            // 首先尝试Jackson反序列化
            try {
                return jsonSerializer.deserialize(bytes);
            } catch (Exception jsonException) {
                // Jackson失败，尝试JDK反序列化（兼容旧数据）
                try {
                    return deserializeJdk(bytes);
                } catch (Exception jdkException) {
                    // 两种方式都失败，抛出原始异常
                    throw new SerializationException(
                        "无法反序列化缓存数据。Jackson错误: " + jsonException.getMessage() + 
                        ", JDK错误: " + jdkException.getMessage(), jsonException);
                }
            }
        }
        
        /**
         * JDK反序列化
         */
        private Object deserializeJdk(byte[] bytes) throws Exception {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            try {
                return ois.readObject();
            } finally {
                ois.close();
                bis.close();
            }
        }
    }

    /**
     * 配置RedisTemplate
     * 使用String序列化key，Jackson序列化value
     * 支持SaToken和缓存功能，兼容特殊字符
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会抛出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置StringRedisTemplate
     * 用于简单的字符串操作，与SaToken兼容
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    /**
     * 配置Spring Cache Manager
     * 使用兼容序列化器，能够处理JDK和Jackson两种序列化格式
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 使用兼容序列化器
        CompatibleRedisSerializer compatibleSerializer = new CompatibleRedisSerializer();

        // 配置缓存序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer(compatibleSerializer))
                .disableCachingNullValues(); // 不缓存null值

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
} 