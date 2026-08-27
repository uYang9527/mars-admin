package com.mars.admin.controller.workflow;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.system.annotation.RepeatSubmit;
import com.mars.workflow.service.WorkflowService;
import com.mars.workflow.vo.WorkflowInstanceVo;
import com.mars.workflow.vo.WorkflowTaskVo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.dromara.warm.flow.core.entity.Instance;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 流程任务管理
 */
@RestController
@RequestMapping("/workflow/task")
@RequiredArgsConstructor
public class FlowTaskController {

    private final WorkflowService workflowService;

    @GetMapping("/todo/page")
    @SaCheckPermission("workflow:task:list")
    public Result<PageResult<WorkflowTaskVo>> todoPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String flowName) {
        return Result.ok(workflowService.todoPage(page, pageSize, flowName));
    }

    @GetMapping("/done/page")
    @SaCheckPermission("workflow:task:list")
    public Result<PageResult<WorkflowTaskVo>> donePage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String flowName) {
        return Result.ok(workflowService.donePage(page, pageSize, flowName));
    }

    @PostMapping("/approve")
    @SaCheckPermission("workflow:task:approve")
    @RepeatSubmit
    @Log(title = "流程审批", businessType = BusinessType.UPDATE)
    public Result<Instance> approve(@RequestBody TaskHandleRequest request) {
        return Result.ok(workflowService.approve(request.getTaskId(), request.getMessage(), request.getVariable()));
    }

    @PostMapping("/reject")
    @SaCheckPermission("workflow:task:approve")
    @RepeatSubmit
    @Log(title = "流程驳回", businessType = BusinessType.UPDATE)
    public Result<Instance> reject(@RequestBody TaskHandleRequest request) {
        return Result.ok(workflowService.reject(request.getTaskId(), request.getMessage(), request.getVariable()));
    }

    @Data
    public static class TaskHandleRequest {
        private Long taskId;
        private String message;
        private Map<String, Object> variable;
    }
}
