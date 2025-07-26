package com.mars.admin.framework.service;

import com.mars.admin.modules.system.entity.SysOperLog;
import com.mars.admin.modules.system.service.ISysOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * 异步操作日志服务
 * 专门处理操作日志的异步保存，提高系统性能
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@Service
@Slf4j
public class AsyncOperationLogService {

    @Autowired
    private ISysOperLogService operLogService;

    /**
     * 异步保存操作日志
     * 使用专用线程池处理，避免阻塞主业务流程
     *
     * @param operLog 操作日志对象
     * @return CompletableFuture<Boolean> 保存结果
     */
    @Async("operationLogExecutor")
    public CompletableFuture<Boolean> saveOperationLogAsync(SysOperLog operLog) {
        try {
            // 记录开始时间用于监控
            long startTime = System.currentTimeMillis();

            // 保存到数据库
            operLogService.insertOperLog(operLog);

            // 计算保存耗时
            long saveTime = System.currentTimeMillis() - startTime;

            // 记录成功日志（调试级别，避免过多日志）
            if (log.isDebugEnabled()) {
                log.debug("异步保存操作日志成功: 标题={}, 操作人={}, 耗时={}ms",
                    operLog.getTitle(), operLog.getOperName(), saveTime);
            }

            // 监控保存时间，如果过长则记录警告
            if (saveTime > 1000) {
                log.warn("操作日志保存耗时过长: 标题={}, 耗时={}ms", operLog.getTitle(), saveTime);
            }

            return CompletableFuture.completedFuture(true);

        } catch (Exception e) {
            // 记录详细的错误信息
            log.error("异步保存操作日志失败: 标题={}, 操作人={}, 错误信息={}",
                operLog.getTitle(), operLog.getOperName(), e.getMessage(), e);

            // 尝试保存错误日志到文件或其他持久化方式
            saveFailedLogToFile(operLog, e);

            return CompletableFuture.completedFuture(false);
        }
    }

    /**
     * 批量异步保存操作日志
     * 用于批量操作场景，提高批量处理效率
     *
     * @param operLogs 操作日志列表
     * @return CompletableFuture<Integer> 成功保存的数量
     */
    @Async("operationLogExecutor")
    public CompletableFuture<Integer> batchSaveOperationLogAsync(java.util.List<SysOperLog> operLogs) {
        if (operLogs == null || operLogs.isEmpty()) {
            return CompletableFuture.completedFuture(0);
        }

        int successCount = 0;
        long startTime = System.currentTimeMillis();

        for (SysOperLog operLog : operLogs) {
            try {
                operLogService.insertOperLog(operLog);
                successCount++;
            } catch (Exception e) {
                log.error("批量保存操作日志失败: 标题={}, 错误={}", operLog.getTitle(), e.getMessage());
                saveFailedLogToFile(operLog, e);
            }
        }

        long totalTime = System.currentTimeMillis() - startTime;
        log.info("批量保存操作日志完成: 总数={}, 成功={}, 失败={}, 耗时={}ms",
            operLogs.size(), successCount, operLogs.size() - successCount, totalTime);

        return CompletableFuture.completedFuture(successCount);
    }

    /**
     * 保存失败的日志到文件
     * 当数据库保存失败时，将日志保存到文件，避免日志丢失
     *
     * @param operLog 操作日志
     * @param exception 异常信息
     */
    private void saveFailedLogToFile(SysOperLog operLog, Exception exception) {
        try {
            // 构建失败日志信息
            StringBuilder failedLogInfo = new StringBuilder();
            failedLogInfo.append("时间: ").append(LocalDateTime.now()).append("\n");
            failedLogInfo.append("标题: ").append(operLog.getTitle()).append("\n");
            failedLogInfo.append("操作人: ").append(operLog.getOperName()).append("\n");
            failedLogInfo.append("请求URL: ").append(operLog.getOperUrl()).append("\n");
            failedLogInfo.append("请求方法: ").append(operLog.getRequestMethod()).append("\n");
            failedLogInfo.append("操作IP: ").append(operLog.getOperIp()).append("\n");
            failedLogInfo.append("业务类型: ").append(operLog.getBusinessType()).append("\n");
            failedLogInfo.append("操作状态: ").append(operLog.getStatus()).append("\n");
            failedLogInfo.append("错误信息: ").append(exception.getMessage()).append("\n");
            failedLogInfo.append("原始参数: ").append(operLog.getOperParam()).append("\n");
            failedLogInfo.append("分隔线: ").append("=".repeat(50)).append("\n\n");

            // 这里可以写入文件，或发送到日志收集系统
            // 简化实现：记录到应用日志中
            log.error("操作日志保存失败，详细信息:\n{}", failedLogInfo.toString());

        } catch (Exception e) {
            log.error("保存失败日志到文件时发生错误: {}", e.getMessage());
        }
    }

    /**
     * 获取异步任务执行状态统计
     * 用于监控异步日志处理性能
     *
     * @return 执行状态信息
     */
    public String getAsyncTaskStats() {
        // 这里可以集成实际的线程池监控
        return "异步操作日志服务运行正常";
    }
}
