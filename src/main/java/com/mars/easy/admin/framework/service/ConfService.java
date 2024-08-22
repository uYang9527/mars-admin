package com.mars.easy.admin.framework.service;

/**
 * 系统配置获取
 *
 * @ClassName ConfService
 * @Author 程序员Mars
 * @Date 2024/1/9 15:25
 * @Version 1.0
 */
public interface ConfService {

    /**
     * 验证配置是否存在
     * @param key
     * @return
     */
    boolean hasConfKey(String key);

    /**
     * 根据配置key获取value
     * @param key
     * @return
     */
    String getConfValue(String key);

}
