package com.mars.admin.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务配置
 * 专门用于操作日志等异步处理任务
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {

    /**
     * 操作日志专用线程池
     * 采用独立线程池，避免影响其他异步任务
     */
    @Bean("threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数：建议根据CPU核数配置，一般为CPU核数
        executor.setCorePoolSize(2);
        
        // 最大线程数：核心线程数的2倍
        executor.setMaxPoolSize(4);
        
        // 队列容量：设置较大的队列，避免日志丢失
        executor.setQueueCapacity(1000);
        
        // 线程空闲时间：60秒
        executor.setKeepAliveSeconds(60);
        
        // 线程名前缀
        executor.setThreadNamePrefix("thread-pool-task-");
        
        // 拒绝策略：使用调用者线程执行，确保日志不丢失
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 关闭时等待任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        
        // 等待时间
        executor.setAwaitTerminationSeconds(30);
        
        // 初始化
        executor.initialize();
        
        log.info("操作日志异步执行器初始化完成: corePoolSize={}, maxPoolSize={}, queueCapacity={}", 
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getQueueCapacity());
        
        return executor;
    }
} 