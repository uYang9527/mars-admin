package com.mars.workflow.service.impl;

import com.mars.common.exception.BusinessException;
import com.mars.common.result.PageResult;
import com.mars.system.entity.SysUser;
import com.mars.system.service.SysUserService;
import com.mars.workflow.handler.MarsPermissionHandler;
import com.mars.workflow.mapper.WorkflowMapper;
import com.mars.workflow.service.WorkflowService;
import com.mars.workflow.vo.WorkflowInstanceVo;
import com.mars.workflow.vo.WorkflowTaskVo;
import lombok.RequiredArgsConstructor;
import org.dromara.warm.flow.core.dto.FlowParams;
import org.dromara.warm.flow.core.entity.Definition;
import org.dromara.warm.flow.core.entity.Instance;
import org.dromara.warm.flow.core.enums.SkipType;
import org.dromara.warm.flow.core.service.DefService;
import org.dromara.warm.flow.core.service.InsService;
import org.dromara.warm.flow.core.service.TaskService;
import org.dromara.warm.flow.core.utils.page.Page;
import org.dromara.warm.flow.orm.entity.FlowDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流服务实现
 */
@Service
@RequiredArgsConstructor
public class WorkflowServiceImpl implements WorkflowService {

    private final DefService defService;
    private final InsService insService;
    private final TaskService taskService;
    private final WorkflowMapper workflowMapper;
    private final MarsPermissionHandler permissionHandler;
    private final SysUserService userService;

    @Override
    public PageResult<Definition> definitionPage(Integer page, Integer pageSize, String flowName, String flowCode) {
        FlowDefinition query = new FlowDefinition();
        if (flowName != null && !flowName.isEmpty()) {
            query.setFlowName(flowName);
        }
        if (flowCode != null && !flowCode.isEmpty()) {
            query.setFlowCode(flowCode);
        }
        Page<Definition> warmPage = new Page<>(page, pageSize);
        Page<Definition> result = defService.page(query, warmPage);
        return PageResult.of(result.getList(), result.getTotal(), (long) page, (long) pageSize);
    }

    @Override
    public Definition getDefinition(Long id) {
        Definition definition = defService.getById(id);
        if (definition == null) {
            throw new BusinessException("流程定义不存在");
        }
        return definition;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishDefinition(Long id) {
        return defService.publish(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unpublishDefinition(Long id) {
        return defService.unPublish(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeDefinition(List<Long> ids) {
        return defService.removeDef(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activeDefinition(Long id) {
        return defService.active(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unActiveDefinition(Long id) {
        return defService.unActive(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyDefinition(Long id) {
        return defService.copyDef(id);
    }

    @Override
    public PageResult<WorkflowTaskVo> todoPage(Integer page, Integer pageSize, String flowName) {
        List<String> permissions = permissionHandler.permissions();
        int offset = (page - 1) * pageSize;
        List<WorkflowTaskVo> list = workflowMapper.selectTodoPage(permissions, flowName, offset, pageSize);
        fillUserNames(list);
        long total = workflowMapper.countTodo(permissions, flowName);
        return PageResult.of(list, total, (long) page, (long) pageSize);
    }

    @Override
    public PageResult<WorkflowTaskVo> donePage(Integer page, Integer pageSize, String flowName) {
        String approver = permissionHandler.getHandler();
        int offset = (page - 1) * pageSize;
        List<WorkflowTaskVo> list = workflowMapper.selectDonePage(approver, flowName, offset, pageSize);
        fillUserNames(list);
        long total = workflowMapper.countDone(approver, flowName);
        return PageResult.of(list, total, (long) page, (long) pageSize);
    }

    @Override
    public PageResult<WorkflowInstanceVo> instancePage(Integer page, Integer pageSize, String flowName, String flowStatus) {
        int offset = (page - 1) * pageSize;
        List<WorkflowInstanceVo> list = workflowMapper.selectInstancePage(flowName, flowStatus, offset, pageSize);
        for (WorkflowInstanceVo vo : list) {
            if (vo.getCreateBy() != null) {
                try {
                    SysUser user = userService.getById(Long.parseLong(vo.getCreateBy()));
                    if (user != null) {
                        vo.setCreateByName(user.getNickname() != null ? user.getNickname() : user.getUsername());
                    }
                } catch (NumberFormatException ignored) {
                    vo.setCreateByName(vo.getCreateBy());
                }
            }
        }
        long total = workflowMapper.countInstance(flowName, flowStatus);
        return PageResult.of(list, total, (long) page, (long) pageSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Instance startInstance(String flowCode, String businessId, Map<String, Object> variable) {
        Definition definition = defService.getPublishByFlowCode(flowCode);
        if (definition == null) {
            throw new BusinessException("流程未发布或不存在: " + flowCode);
        }
        FlowParams flowParams = FlowParams.build()
                .flowCode(flowCode)
                .handler(permissionHandler.getHandler())
                .permissionFlag(permissionHandler.permissions());
        if (variable != null && !variable.isEmpty()) {
            flowParams.variable(variable);
        }
        return insService.start(businessId, flowParams);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Instance approve(Long taskId, String message, Map<String, Object> variable) {
        Map<String, Object> vars = variable != null ? variable : new HashMap<>();
        return taskService.pass(taskId, message, vars);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Instance reject(Long taskId, String message, Map<String, Object> variable) {
        Map<String, Object> vars = variable != null ? variable : new HashMap<>();
        return taskService.reject(taskId, message, vars);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Instance terminate(Long instanceId, String message) {
        FlowParams flowParams = FlowParams.build()
                .message(message)
                .handler(permissionHandler.getHandler())
                .permissionFlag(permissionHandler.permissions())
                .skipType(SkipType.NONE.getKey());
        return taskService.terminationByInsId(instanceId, flowParams);
    }

    @Override
    public List<Definition> listPublishedDefinitions() {
        FlowDefinition query = new FlowDefinition();
        query.setIsPublish(1);
        return defService.list(query);
    }

    private void fillUserNames(List<WorkflowTaskVo> list) {
        for (WorkflowTaskVo vo : list) {
            if (vo.getCreateBy() != null) {
                try {
                    SysUser user = userService.getById(Long.parseLong(vo.getCreateBy()));
                    if (user != null) {
                        vo.setApproverName(user.getNickname() != null ? user.getNickname() : user.getUsername());
                    }
                } catch (NumberFormatException ignored) {
                    vo.setApproverName(vo.getCreateBy());
                }
            }
        }
    }
}
