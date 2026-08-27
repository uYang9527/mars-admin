package com.mars.workflow.service;

import com.mars.common.result.PageResult;
import com.mars.workflow.vo.WorkflowInstanceVo;
import com.mars.workflow.vo.WorkflowTaskVo;
import org.dromara.warm.flow.core.entity.Definition;
import org.dromara.warm.flow.core.entity.Instance;

import java.util.List;
import java.util.Map;

/**
 * 工作流服务
 */
public interface WorkflowService {

    PageResult<Definition> definitionPage(Integer page, Integer pageSize, String flowName, String flowCode);

    Definition getDefinition(Long id);

    boolean publishDefinition(Long id);

    boolean unpublishDefinition(Long id);

    boolean removeDefinition(List<Long> ids);

    boolean activeDefinition(Long id);

    boolean unActiveDefinition(Long id);

    boolean copyDefinition(Long id);

    PageResult<WorkflowTaskVo> todoPage(Integer page, Integer pageSize, String flowName);

    PageResult<WorkflowTaskVo> donePage(Integer page, Integer pageSize, String flowName);

    PageResult<WorkflowInstanceVo> instancePage(Integer page, Integer pageSize, String flowName, String flowStatus);

    Instance startInstance(String flowCode, String businessId, Map<String, Object> variable);

    Instance approve(Long taskId, String message, Map<String, Object> variable);

    Instance reject(Long taskId, String message, Map<String, Object> variable);

    Instance terminate(Long instanceId, String message);

    List<Definition> listPublishedDefinitions();
}
