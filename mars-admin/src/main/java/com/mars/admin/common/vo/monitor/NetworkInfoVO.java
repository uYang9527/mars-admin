package com.mars.admin.common.vo.monitor;

import lombok.Data;
import java.util.List;

/**
 * 网络信息VO
 * 
 * 包含网络接口的详细信息和流量统计
 *
 * @author Mars
 */
@Data
public class NetworkInfoVO {

    /**
     * 网络接口列表
     */
    private List<NetworkInterfaceVO> interfaces;

    /**
     * 总发送字节数
     */
    private Long totalBytesSent;

    /**
     * 总接收字节数
     */
    private Long totalBytesReceived;

    /**
     * 总发送包数
     */
    private Long totalPacketsSent;

    /**
     * 总接收包数
     */
    private Long totalPacketsReceived;

    /**
     * 发送速率（bytes/s）
     */
    private Long sendRate;

    /**
     * 接收速率（bytes/s）
     */
    private Long receiveRate;

    /**
     * 网络接口信息VO
     */
    @Data
    public static class NetworkInterfaceVO {
        
        /**
         * 接口名称
         */
        private String name;

        /**
         * 显示名称
         */
        private String displayName;

        /**
         * MAC地址
         */
        private String macAddress;

        /**
         * IP地址列表
         */
        private List<String> ipAddresses;

        /**
         * 接口类型
         */
        private String type;

        /**
         * 接口状态
         */
        private String status;

        /**
         * 接口速度（Mbps）
         */
        private Long speed;

        /**
         * 是否启用
         */
        private Boolean enabled;

        /**
         * 发送字节数
         */
        private Long bytesSent;

        /**
         * 接收字节数
         */
        private Long bytesReceived;

        /**
         * 发送包数
         */
        private Long packetsSent;

        /**
         * 接收包数
         */
        private Long packetsReceived;

        /**
         * 发送错误数
         */
        private Long sendErrors;

        /**
         * 接收错误数
         */
        private Long receiveErrors;
    }
} 