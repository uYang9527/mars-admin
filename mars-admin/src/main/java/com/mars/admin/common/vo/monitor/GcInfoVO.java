package com.mars.admin.common.vo.monitor;

import lombok.Data;

/**
 * 垃圾回收信息VO
 * 
 * 包含各个垃圾收集器的统计信息
 *
 * @author Mars
 */
@Data
public class GcInfoVO {

    /**
     * 垃圾收集器名称
     */
    private String name;

    /**
     * 垃圾收集器类型（新生代/老年代）
     */
    private String type;

    /**
     * 收集次数
     */
    private Long collectionCount;

    /**
     * 收集总时间（毫秒）
     */
    private Long collectionTime;

    /**
     * 平均收集时间（毫秒）
     */
    private Double averageCollectionTime;

    /**
     * 最后一次收集时间
     */
    private String lastCollectionTime;

    /**
     * 收集频率（次/分钟）
     */
    private Double frequency;
} 