package com.mars.admin.controller;

import com.mars.admin.common.vo.monitor.SysMonitorVO;
import com.mars.admin.common.vo.monitor.HealthCheckVO;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 系统监控Controller
 * 
 * 提供全面的系统监控功能，包括：
 * - 实时系统监控数据获取
 * - 系统健康状态检查
 * - 告警阈值管理
 * - 性能指标统计
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/monitor")
@Tag(name = "系统监控管理", description = "系统监控相关接口")
public class SysMonitorController {

    @Autowired
    private ISysMonitorService sysMonitorService;

    /**
     * 获取系统监控信息
     * 
     * 返回包含CPU、内存、磁盘、JVM、网络、线程等完整监控数据
     */
    @GetMapping("/info")
    @Operation(summary = "获取系统监控信息", description = "获取服务器、CPU、内存、JVM、磁盘、网络、线程等全面监控信息")
    public Result<SysMonitorVO> getMonitorInfo() {
        try {
            SysMonitorVO monitorInfo = sysMonitorService.getMonitorInfo();
            return Result.success(monitorInfo);
        } catch (Exception e) {
            return Result.error("获取系统监控信息失败: " + e.getMessage());
        }
    }

    /**
     * 系统健康检查
     * 
     * 快速检查系统关键指标是否正常
     */
    @GetMapping("/health")
    @Operation(summary = "系统健康检查", description = "检查系统关键指标状态，返回健康状态评估")
    public Result<HealthCheckVO> healthCheck() {
        try {
            HealthCheckVO healthCheck = sysMonitorService.performHealthCheck();
            return Result.success(healthCheck);
        } catch (Exception e) {
            return Result.error("系统健康检查失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统性能摘要
     * 
     * 返回核心性能指标的简化版本
     */
    @GetMapping("/summary")
    @Operation(summary = "获取系统性能摘要", description = "获取系统核心性能指标摘要")
    public Result<Map<String, Object>> getPerformanceSummary() {
        try {
            Map<String, Object> summary = sysMonitorService.getPerformanceSummary();
            return Result.success(summary);
        } catch (Exception e) {
            return Result.error("获取性能摘要失败: " + e.getMessage());
        }
    }

    /**
     * 获取告警信息
     * 
     * 检查是否有超过阈值的监控指标
     */
    @GetMapping("/alerts")
    @Operation(summary = "获取告警信息", description = "获取当前系统告警信息")
    public Result<Map<String, Object>> getSystemAlerts(
        @Parameter(description = "CPU使用率阈值", example = "80")
        @RequestParam(defaultValue = "80") Double cpuThreshold,
        
        @Parameter(description = "内存使用率阈值", example = "85") 
        @RequestParam(defaultValue = "85") Double memoryThreshold,
        
        @Parameter(description = "磁盘使用率阈值", example = "90")
        @RequestParam(defaultValue = "90") Double diskThreshold,
        
        @Parameter(description = "JVM堆内存使用率阈值", example = "80")
        @RequestParam(defaultValue = "80") Double jvmThreshold
    ) {
        try {
            Map<String, Object> alerts = sysMonitorService.checkSystemAlerts(
                cpuThreshold, memoryThreshold, diskThreshold, jvmThreshold
            );
            return Result.success(alerts);
        } catch (Exception e) {
            return Result.error("获取告警信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统运行时间统计
     */
    @GetMapping("/uptime")
    @Operation(summary = "获取系统运行时间", description = "获取系统和JVM运行时间统计")
    public Result<Map<String, Object>> getSystemUptime() {
        try {
            Map<String, Object> uptime = sysMonitorService.getSystemUptime();
            return Result.success(uptime);
        } catch (Exception e) {
            return Result.error("获取运行时间失败: " + e.getMessage());
        }
    }
} 