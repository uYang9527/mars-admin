package com.mars.admin.common.vo.monitor;

import lombok.Data;

/**
 * 线程信息VO
 * 
 * 包含JVM线程的详细统计信息
 *
 * @author Mars
 */
@Data
public class ThreadInfoVO {

    /**
     * 活动线程数
     */
    private Integer liveThreads;

    /**
     * 守护线程数
     */
    private Integer daemonThreads;

    /**
     * 峰值线程数
     */
    private Integer peakThreads;

    /**
     * 总启动线程数
     */
    private Long totalStartedThreads;

    /**
     * 死锁线程数
     */
    private Integer deadlockedThreads;

    /**
     * 新建状态线程数
     */
    private Integer newThreads;

    /**
     * 可运行状态线程数
     */
    private Integer runnableThreads;

    /**
     * 阻塞状态线程数
     */
    private Integer blockedThreads;

    /**
     * 等待状态线程数
     */
    private Integer waitingThreads;

    /**
     * 超时等待状态线程数
     */
    private Integer timedWaitingThreads;

    /**
     * 终止状态线程数
     */
    private Integer terminatedThreads;
} 