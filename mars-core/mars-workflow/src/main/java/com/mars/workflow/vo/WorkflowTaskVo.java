package com.mars.workflow.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工作流任务视图对象
 */
@Data
public class WorkflowTaskVo {

    private Long id;
    private Long definitionId;
    private Long instanceId;
    private String nodeCode;
    private String nodeName;
    private String flowStatus;
    private String flowCode;
    private String flowName;
    private String businessId;
    private String createBy;
    private String approverName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
