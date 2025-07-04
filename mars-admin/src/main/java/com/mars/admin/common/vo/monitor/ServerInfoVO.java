package com.mars.admin.common.vo.monitor;

import lombok.Data;

/**
 * 服务器信息VO
 * 
 * 包含服务器的基本信息和运行状态
 *
 * @author Mars
 */
@Data
public class ServerInfoVO {

    /**
     * 服务器名称
     */
    private String computerName;

    /**
     * 服务器IP
     */
    private String computerIp;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;

    /**
     * 操作系统版本
     */
    private String osVersion;

    /**
     * 服务器运行时长
     */
    private String uptime;

    /**
     * 时区
     */
    private String timezone;

    /**
     * Java版本
     */
    private String javaVersion;

    /**
     * 进程ID
     */
    private String processId;

    /**
     * 可用处理器数量
     */
    private Integer availableProcessors;
} 