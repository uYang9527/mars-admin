package com.mars.admin.service;

import com.mybatisflex.core.service.IService;

import java.io.Serializable;

/**
 * 通用服务接口
 * 继承 MyBatis-Flex 的 IService，提供基础的增删改查功能
 *
 * @param <T> 实体类型
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-06-23 17:35:08
 */
public interface BaseService<T> extends IService<T> {
    
    // 可以在这里添加自定义的通用方法
    // 例如：
    // boolean existsByField(String field, Object value);
    // List<T> findByField(String field, Object value);
    
} 