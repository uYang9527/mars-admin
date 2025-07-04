package com.mars.admin.common.vo.monitor;

import lombok.Data;

/**
 * CPU信息VO
 * 
 * 包含CPU的详细信息和实时使用情况
 *
 * @author Mars
 */
@Data
public class CpuInfoVO {

    /**
     * CPU核心数（逻辑处理器）
     */
    private Integer cpuNum;

    /**
     * CPU物理核心数
     */
    private Integer physicalCores;

    /**
     * CPU总的使用率
     */
    private Double total;

    /**
     * CPU系统使用率
     */
    private Double sys;

    /**
     * CPU用户使用率
     */
    private Double used;

    /**
     * CPU当前等待率
     */
    private Double wait;

    /**
     * CPU当前空闲率
     */
    private Double free;

    /**
     * CPU中断使用率
     */
    private Double irq;

    /**
     * CPU软中断使用率
     */
    private Double softirq;

    /**
     * CPU型号信息
     */
    private String cpuModel;

    /**
     * CPU架构
     */
    private String architecture;

    /**
     * CPU频率（MHz）
     */
    private Long frequency;

    /**
     * CPU缓存大小（KB）
     */
    private Long cacheSize;

    /**
     * CPU使用率百分比（便于前端显示）
     */
    private Double usagePercent;
} 