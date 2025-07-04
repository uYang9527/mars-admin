package com.mars.admin.common.vo.monitor;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 系统健康检查VO
 * 
 * 包含系统整体健康状态和各项指标检查结果
 *
 * @author Mars
 */
@Data
public class HealthCheckVO {

    /**
     * 整体健康状态
     */
    private String overallStatus;

    /**
     * 健康评分 (0-100)
     */
    private Double healthScore;

    /**
     * 检查时间戳
     */
    private Long checkTime;

    /**
     * 各项指标检查结果
     */
    private Map<String, HealthMetric> metrics;

    /**
     * 告警信息列表
     */
    private List<AlertInfo> alerts;

    /**
     * 建议信息列表
     */
    private List<String> recommendations;

    /**
     * 健康指标检查结果
     */
    @Data
    public static class HealthMetric {
        
        /**
         * 指标名称
         */
        private String name;

        /**
         * 指标状态 (HEALTHY, WARNING, CRITICAL)
         */
        private String status;

        /**
         * 当前值
         */
        private Double currentValue;

        /**
         * 阈值
         */
        private Double threshold;

        /**
         * 检查结果描述
         */
        private String description;

        /**
         * 指标重要程度 (1-5, 5为最重要)
         */
        private Integer priority;
    }

    /**
     * 告警信息
     */
    @Data
    public static class AlertInfo {
        
        /**
         * 告警级别 (INFO, WARNING, ERROR, CRITICAL)
         */
        private String level;

        /**
         * 告警类型
         */
        private String type;

        /**
         * 告警消息
         */
        private String message;

        /**
         * 相关指标
         */
        private String metric;

        /**
         * 当前值
         */
        private Object currentValue;

        /**
         * 阈值
         */
        private Object threshold;

        /**
         * 告警时间
         */
        private Long timestamp;
    }
} 