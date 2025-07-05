package com.mars.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysMonitorService;

import java.util.Map;

/**
 * 系统监控控制器
 *
 * @author Mars
 */
@Slf4j
@RestController
@RequestMapping("/system/monitor")
@RequiredArgsConstructor
@Tag(name = "系统监控", description = "系统监控相关接口")
public class SysMonitorController {

    private final ISysMonitorService monitorService;

    /**
     * 获取系统监控信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取系统监控信息", description = "获取服务器、CPU、内存、JVM等监控信息")
    public Result<Map<String, Object>> getMonitorInfo() {
        try {
            Map<String, Object> monitorInfo = monitorService.getSimpleMonitorInfo();
            return Result.success(monitorInfo);
        } catch (Exception e) {
            log.error("获取系统监控信息失败", e);
            return Result.error("获取系统监控信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统资源使用情况（简化版）
     */
    @GetMapping("/resources")
    @Operation(summary = "获取系统资源使用情况", description = "获取CPU、内存、磁盘使用率")
    public Result<Map<String, Object>> getResourceUsage() {
        try {
            Map<String, Object> resourceInfo = monitorService.getResourceUsage();
            return Result.success(resourceInfo);
        } catch (Exception e) {
            log.error("获取系统资源信息失败", e);
            return Result.error("获取系统资源信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统告警信息
     * @param cpuThreshold CPU阈值
     * @param memoryThreshold 内存阈值
     * @param diskThreshold 磁盘阈值
     * @param jvmThreshold JVM阈值
     * @return 告警信息
     */
    @GetMapping("/alerts")
    @Operation(summary = "获取系统告警信息", description = "检查系统各项指标是否超出阈值")
    public Result<Map<String, Object>> getSystemAlerts(
            @Parameter(description = "CPU使用率阈值") @RequestParam(required = false) Double cpuThreshold,
            @Parameter(description = "内存使用率阈值") @RequestParam(required = false) Double memoryThreshold,
            @Parameter(description = "磁盘使用率阈值") @RequestParam(required = false) Double diskThreshold,
            @Parameter(description = "JVM堆内存使用率阈值") @RequestParam(required = false) Double jvmThreshold) {
        try {
            Map<String, Object> alerts = monitorService.getSystemAlerts(
                    cpuThreshold, memoryThreshold, diskThreshold, jvmThreshold);
            return Result.success(alerts);
        } catch (Exception e) {
            log.error("获取系统告警信息失败", e);
            return Result.error("获取告警信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统性能总览
     * @return 性能总览信息
     */
    @GetMapping("/overview")
    @Operation(summary = "获取系统性能总览", description = "获取系统健康分数和性能摘要")
    public Result<Map<String, Object>> getPerformanceOverview() {
        try {
            Map<String, Object> overview = monitorService.getPerformanceOverview();
            return Result.success(overview);
        } catch (Exception e) {
            log.error("获取系统性能总览失败", e);
            return Result.error("获取性能总览失败: " + e.getMessage());
        }
    }

    /**
     * 获取进程信息
     * @return 进程信息
     */
    @GetMapping("/process")
    @Operation(summary = "获取进程信息", description = "获取当前进程和系统TOP进程信息")
    public Result<Map<String, Object>> getProcessInfo() {
        try {
            Map<String, Object> processInfo = monitorService.getProcessInfo();
            return Result.success(processInfo);
        } catch (Exception e) {
            log.error("获取进程信息失败", e);
            return Result.error("获取进程信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取垃圾回收信息
     * @return GC信息
     */
    @GetMapping("/gc")
    @Operation(summary = "获取垃圾回收信息", description = "获取JVM垃圾回收器和内存池信息")
    public Result<Map<String, Object>> getGcInfo() {
        try {
            Map<String, Object> gcInfo = monitorService.getGcInfo();
            return Result.success(gcInfo);
        } catch (Exception e) {
            log.error("获取垃圾回收信息失败", e);
            return Result.error("获取GC信息失败: " + e.getMessage());
        }
    }
} 