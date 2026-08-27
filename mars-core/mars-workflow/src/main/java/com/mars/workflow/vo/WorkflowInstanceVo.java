package com.mars.workflow.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工作流实例视图对象
 */
@Data
public class WorkflowInstanceVo {

    private Long id;
    private Long definitionId;
    private String businessId;
    private String nodeCode;
    private String nodeName;
    private String flowStatus;
    private String flowCode;
    private String flowName;
    private String createBy;
    private String createByName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
