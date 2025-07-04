package com.mars.admin.common.vo.monitor;

import lombok.Data;
import java.util.List;

/**
 * JVM信息VO
 * 
 * 包含JVM的详细运行信息，包括内存、垃圾回收、线程等
 *
 * @author Mars
 */
@Data
public class JvmInfoVO {

    /**
     * 堆内存总大小（字节）
     */
    private Long total;

    /**
     * 堆内存已用（字节）
     */
    private Long used;

    /**
     * 堆内存空闲（字节）
     */
    private Long free;

    /**
     * 堆内存使用率（百分比）
     */
    private Double usage;

    /**
     * JDK名称
     */
    private String name;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    /**
     * JVM启动时间
     */
    private String startTime;

    /**
     * JVM运行时长
     */
    private String runTime;

    /**
     * 非堆内存总大小（字节）
     */
    private Long nonHeapTotal;

    /**
     * 非堆内存已用（字节）
     */
    private Long nonHeapUsed;

    /**
     * 非堆内存使用率（百分比）
     */
    private Double nonHeapUsage;

    /**
     * 元空间总大小（字节）
     */
    private Long metaspaceTotal;

    /**
     * 元空间已用（字节）
     */
    private Long metaspaceUsed;

    /**
     * 元空间使用率（百分比）
     */
    private Double metaspaceUsage;

    /**
     * 压缩类空间总大小（字节）
     */
    private Long compressedClassSpaceTotal;

    /**
     * 压缩类空间已用（字节）
     */
    private Long compressedClassSpaceUsed;

    /**
     * JVM参数
     */
    private List<String> jvmArgs;

    /**
     * 类加载信息
     */
    private ClassLoaderInfoVO classLoader;

    /**
     * 垃圾回收信息
     */
    private List<GcInfoVO> gcInfo;

    /**
     * 堆内存格式化显示
     */
    private String heapFormatted;

    /**
     * 非堆内存格式化显示
     */
    private String nonHeapFormatted;

} 