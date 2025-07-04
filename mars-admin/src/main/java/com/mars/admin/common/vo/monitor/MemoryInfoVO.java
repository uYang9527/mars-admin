package com.mars.admin.common.vo.monitor;

import lombok.Data;

/**
 * 内存信息VO
 * 
 * 包含物理内存和交换内存的详细信息
 *
 * @author Mars
 */
@Data
public class MemoryInfoVO {

    /**
     * 总内存（字节）
     */
    private Long total;

    /**
     * 已用内存（字节）
     */
    private Long used;

    /**
     * 剩余内存（字节）
     */
    private Long free;

    /**
     * 可用内存（字节）
     */
    private Long available;

    /**
     * 内存使用率（百分比）
     */
    private Double usage;

    /**
     * 缓冲区内存（字节）
     */
    private Long buffers;

    /**
     * 缓存内存（字节）
     */
    private Long cached;

    /**
     * 交换分区总大小（字节）
     */
    private Long swapTotal;

    /**
     * 交换分区已用（字节）
     */
    private Long swapUsed;

    /**
     * 交换分区空闲（字节）
     */
    private Long swapFree;

    /**
     * 交换分区使用率（百分比）
     */
    private Double swapUsage;

    /**
     * 总内存格式化显示
     */
    private String totalFormatted;

    /**
     * 已用内存格式化显示
     */
    private String usedFormatted;

    /**
     * 可用内存格式化显示
     */
    private String availableFormatted;

} 