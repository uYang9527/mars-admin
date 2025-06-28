package com.mars.admin.framework.oss.factory;

import com.mars.admin.framework.oss.strategy.FileUploadStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件上传策略工厂
 *
 * @author Mars.wq
 */
@Slf4j
@Component
public class FileUploadStrategyFactory {

    private final Map<String, FileUploadStrategy> strategyMap = new ConcurrentHashMap<>();

    @Autowired
    public FileUploadStrategyFactory(List<FileUploadStrategy> strategies) {
        for (FileUploadStrategy strategy : strategies) {
            strategyMap.put(strategy.getType(), strategy);
            log.info("注册文件上传策略: {}", strategy.getType());
        }
    }

    /**
     * 获取文件上传策略
     *
     * @param type 策略类型
     * @return 文件上传策略
     */
    public FileUploadStrategy getStrategy(String type) {
        FileUploadStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的文件上传类型: " + type);
        }
        return strategy;
    }

    /**
     * 获取所有策略类型
     *
     * @return 策略类型列表
     */
    public java.util.Set<String> getAllTypes() {
        return strategyMap.keySet();
    }
} 