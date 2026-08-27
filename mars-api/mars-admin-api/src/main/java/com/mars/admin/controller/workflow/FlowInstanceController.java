package com.mars.admin.controller.workflow;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.system.annotation.RepeatSubmit;
import com.mars.workflow.service.WorkflowService;
import com.mars.workflow.vo.WorkflowInstanceVo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.dromara.warm.flow.core.entity.Instance;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 流程实例管理
 */
@RestController
@RequestMapping("/workflow/instance")
@RequiredArgsConstructor
public class FlowInstanceController {

    private final WorkflowService workflowService;

    @GetMapping("/page")
    @SaCheckPermission("workflow:instance:list")
    public Result<PageResult<WorkflowInstanceVo>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String flowName,
            @RequestParam(required = false) String flowStatus) {
        return Result.ok(workflowService.instancePage(page, pageSize, flowName, flowStatus));
    }

    @PostMapping("/start")
    @SaCheckPermission("workflow:instance:start")
    @RepeatSubmit
    @Log(title = "流程实例", businessType = BusinessType.INSERT)
    public Result<Instance> start(@RequestBody StartInstanceRequest request) {
        return Result.ok(workflowService.startInstance(
                request.getFlowCode(),
                request.getBusinessId(),
                request.getVariable()));
    }

    @PostMapping("/terminate/{id}")
    @SaCheckPermission("workflow:instance:terminate")
    @RepeatSubmit
    @Log(title = "流程实例", businessType = BusinessType.UPDATE)
    public Result<Instance> terminate(@PathVariable Long id, @RequestBody(required = false) TerminateRequest request) {
        String message = request != null ? request.getMessage() : "终止流程";
        return Result.ok(workflowService.terminate(id, message));
    }

    @Data
    public static class StartInstanceRequest {
        private String flowCode;
        private String businessId;
        private Map<String, Object> variable;
    }

    @Data
    public static class TerminateRequest {
        private String message;
    }
}
