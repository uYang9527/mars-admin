package com.mars.admin.common.vo.monitor;

import lombok.Data;
import java.util.List;

/**
 * 系统监控信息VO
 * 
 * 提供全面的系统监控信息，包括服务器、CPU、内存、JVM、磁盘、网络等
 *
 * @author Mars
 */
@Data
public class SysMonitorVO {

    /**
     * 服务器信息
     */
    private ServerInfoVO server;

    /**
     * CPU信息
     */
    private CpuInfoVO cpu;

    /**
     * 内存信息
     */
    private MemoryInfoVO memory;

    /**
     * JVM信息
     */
    private JvmInfoVO jvm;

    /**
     * 磁盘状态信息
     */
    private List<DiskInfoVO> disk;

    /**
     * 网络信息
     */
    private NetworkInfoVO network;

    /**
     * 线程信息
     */
    private ThreadInfoVO thread;

    /**
     * 系统负载信息
     */
    private SystemLoadVO systemLoad;

    /**
     * 数据采集时间戳
     */
    private Long timestamp;

} 