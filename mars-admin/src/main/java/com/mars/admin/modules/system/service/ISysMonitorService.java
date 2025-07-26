package com.mars.admin.modules.system.service;

import com.mars.admin.common.vo.monitor.SysMonitorVO;

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
     * 获取系统监控信息（VO版本）
     *
     * @return SysMonitorVO 完整的系统监控信息
     * @throws Exception 获取信息异常
     */
    SysMonitorVO getMonitorInfo() throws Exception;

    /**
     * 获取简化版系统监控信息（Map版本）
     *
     * @return Map<String, Object> 系统监控信息
     * @throws Exception 获取信息异常
     */
    Map<String, Object> getSimpleMonitorInfo() throws Exception;

    /**
     * 获取系统资源使用情况
     *
     * @return Map<String, Object> 系统资源使用情况
     * @throws Exception 获取资源使用情况异常
     */
    Map<String, Object> getResourceUsage() throws Exception;

    /**
     * 获取系统告警信息
     *
     * @param cpuThreshold CPU使用率阈值
     * @param memoryThreshold 内存使用率阈值
     * @param diskThreshold 磁盘使用率阈值
     * @param jvmThreshold JVM堆内存使用率阈值
     * @return 告警信息
     */
    Map<String, Object> getSystemAlerts(Double cpuThreshold, Double memoryThreshold,
                                       Double diskThreshold, Double jvmThreshold) throws Exception;

    /**
     * 获取系统性能总览
     */
    Map<String, Object> getPerformanceOverview() throws Exception;

    /**
     * 获取进程信息
     */
    Map<String, Object> getProcessInfo() throws Exception;

    /**
     * 获取垃圾回收信息
     */
    Map<String, Object> getGcInfo() throws Exception;
}
