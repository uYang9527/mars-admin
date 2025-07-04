package com.mars.admin.common.vo.monitor;

import lombok.Data;

/**
 * 类加载器信息VO
 * 
 * 包含JVM类加载的统计信息
 *
 * @author Mars
 */
@Data
public class ClassLoaderInfoVO {

    /**
     * 当前加载的类数量
     */
    private Integer loadedClassCount;

    /**
     * 总共加载过的类数量
     */
    private Long totalLoadedClassCount;

    /**
     * 已卸载的类数量
     */
    private Long unloadedClassCount;

    /**
     * 是否支持类卸载
     */
    private Boolean verbose;
} 