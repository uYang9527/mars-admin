package com.mars.admin.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sa-Token 配置属性类
 * 用于读取配置文件中的Sa-Token相关配置
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Data
@Component
@ConfigurationProperties(prefix = "sa-token")
public class SaTokenProperties {

    /**
     * 白名单路径列表 - 这些路径不需要Token验证
     * 对应配置文件中的 sa-token.exclude-paths
     */
    private List<String> excludePaths;
} 