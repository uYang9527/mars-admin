package com.mars.admin.controller.workflow;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.system.annotation.RepeatSubmit;
import com.mars.workflow.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.dromara.warm.flow.core.entity.Definition;
import org.dromara.warm.flow.core.entity.Instance;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流程定义管理
 */
@RestController
@RequestMapping("/workflow/definition")
@RequiredArgsConstructor
public class FlowDefinitionController {

    private final WorkflowService workflowService;

    @GetMapping("/page")
    @SaCheckPermission("workflow:definition:list")
    public Result<PageResult<Definition>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String flowName,
            @RequestParam(required = false) String flowCode) {
        return Result.ok(workflowService.definitionPage(page, pageSize, flowName, flowCode));
    }

    @GetMapping("/published")
    @SaCheckPermission("workflow:definition:list")
    public Result<List<Definition>> publishedList() {
        return Result.ok(workflowService.listPublishedDefinitions());
    }

    @GetMapping("/{id}")
    @SaCheckPermission("workflow:definition:query")
    public Result<Definition> getInfo(@PathVariable Long id) {
        return Result.ok(workflowService.getDefinition(id));
    }

    @PostMapping("/publish/{id}")
    @SaCheckPermission("workflow:definition:publish")
    @RepeatSubmit
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    public Result<Void> publish(@PathVariable Long id) {
        workflowService.publishDefinition(id);
        return Result.ok();
    }

    @PostMapping("/unpublish/{id}")
    @SaCheckPermission("workflow:definition:publish")
    @RepeatSubmit
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    public Result<Void> unpublish(@PathVariable Long id) {
        workflowService.unpublishDefinition(id);
        return Result.ok();
    }

    @DeleteMapping("/{ids}")
    @SaCheckPermission("workflow:definition:remove")
    @Log(title = "流程定义", businessType = BusinessType.DELETE)
    public Result<Void> remove(@PathVariable List<Long> ids) {
        workflowService.removeDefinition(ids);
        return Result.ok();
    }

    @PostMapping("/active/{id}")
    @SaCheckPermission("workflow:definition:edit")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    public Result<Void> active(@PathVariable Long id) {
        workflowService.activeDefinition(id);
        return Result.ok();
    }

    @PostMapping("/unactive/{id}")
    @SaCheckPermission("workflow:definition:edit")
    @Log(title = "流程定义", businessType = BusinessType.UPDATE)
    public Result<Void> unActive(@PathVariable Long id) {
        workflowService.unActiveDefinition(id);
        return Result.ok();
    }

    @PostMapping("/copy/{id}")
    @SaCheckPermission("workflow:definition:add")
    @RepeatSubmit
    @Log(title = "流程定义", businessType = BusinessType.INSERT)
    public Result<Void> copy(@PathVariable Long id) {
        workflowService.copyDefinition(id);
        return Result.ok();
    }
}
