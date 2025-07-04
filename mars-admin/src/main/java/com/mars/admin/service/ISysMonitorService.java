package com.mars.admin.service;

import com.mars.admin.common.vo.monitor.SysMonitorVO;
import com.mars.admin.common.vo.monitor.HealthCheckVO;

import java.util.Map;

/**
 * 系统监控服务接口
 * 
 * 提供全面的系统监控功能：
 * - 实时系统数据监控
 * - 系统健康状态检查  
 * - 性能指标统计分析
 * - 告警阈值检测
 * - 系统运行时间统计
 *
 * @author Mars
 */
public interface ISysMonitorService {

    /**
     * 获取系统监控信息
     *
     * @return SysMonitorVO 完整的系统监控信息
     * @throws Exception 获取信息异常
     */
    SysMonitorVO getMonitorInfo() throws Exception;

    /**
     * 执行系统健康检查
     * 
     * @return HealthCheckVO 健康检查结果
     * @throws Exception 健康检查异常
     */
    HealthCheckVO performHealthCheck() throws Exception;

    /**
     * 获取系统性能摘要
     * 
     * @return Map<String, Object> 性能摘要数据
     * @throws Exception 获取摘要异常
     */
    Map<String, Object> getPerformanceSummary() throws Exception;

    /**
     * 检查系统告警
     * 
     * @param cpuThreshold CPU使用率阈值
     * @param memoryThreshold 内存使用率阈值  
     * @param diskThreshold 磁盘使用率阈值
     * @param jvmThreshold JVM堆内存使用率阈值
     * @return Map<String, Object> 告警信息
     * @throws Exception 检查告警异常
     */
    Map<String, Object> checkSystemAlerts(Double cpuThreshold, Double memoryThreshold, 
                                         Double diskThreshold, Double jvmThreshold) throws Exception;

    /**
     * 获取系统运行时间统计
     * 
     * @return Map<String, Object> 运行时间统计
     * @throws Exception 获取统计异常
     */
    Map<String, Object> getSystemUptime() throws Exception;
} 