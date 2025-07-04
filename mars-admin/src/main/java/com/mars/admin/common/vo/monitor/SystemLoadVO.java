package com.mars.admin.common.vo.monitor;

import lombok.Data;

/**
 * 系统负载信息VO
 * 
 * 包含系统负载和进程统计信息
 *
 * @author Mars
 */
@Data
public class SystemLoadVO {

    /**
     * 1分钟平均负载
     */
    private Double load1;

    /**
     * 5分钟平均负载
     */
    private Double load5;

    /**
     * 15分钟平均负载
     */
    private Double load15;

    /**
     * 系统启动时间戳
     */
    private Long bootTime;

    /**
     * 系统运行时间（秒）
     */
    private Long uptime;

    /**
     * 总进程数
     */
    private Integer processCount;

    /**
     * 运行中进程数
     */
    private Integer runningProcesses;

    /**
     * 休眠进程数
     */
    private Integer sleepingProcesses;

    /**
     * 停止进程数
     */
    private Integer stoppedProcesses;

    /**
     * 僵尸进程数
     */
    private Integer zombieProcesses;

    /**
     * 系统负载级别（低/中/高）
     */
    private String loadLevel;

    /**
     * 负载百分比
     */
    private Double loadPercent;
} 