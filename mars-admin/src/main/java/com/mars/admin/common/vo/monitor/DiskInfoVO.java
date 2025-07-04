package com.mars.admin.common.vo.monitor;

import lombok.Data;

/**
 * 磁盘信息VO
 *
 * @author Mars
 */
@Data
public class DiskInfoVO {

    /**
     * 盘符路径
     */
    private String dirName;

    /**
     * 盘符类型
     */
    private String sysTypeName;

    /**
     * 文件系统
     */
    private String typeName;

    /**
     * 总大小
     */
    private String total;

    /**
     * 剩余大小
     */
    private String free;

    /**
     * 已用大小
     */
    private String used;

    /**
     * 已用百分比
     */
    private double usage;
} 