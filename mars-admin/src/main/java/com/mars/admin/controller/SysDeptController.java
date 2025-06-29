package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysDept;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统部门Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/dept")
@Tag(name = "系统部门管理", description = "系统部门管理相关接口")
@AllArgsConstructor
public class SysDeptController extends BaseController<SysDept, Long> {

    private final ISysDeptService sysDeptService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/dept/list           - 获取所有部门
    // GET    /system/dept/{id}           - 根据ID获取部门
    // GET    /system/dept/page           - 分页查询部门
    // POST   /system/dept                - 新增部门
    // PUT    /system/dept                - 更新部门
    // DELETE /system/dept/{id}           - 删除部门
    // DELETE /system/dept/batch          - 批量删除部门

    /**
     * 新增部门
     */
    @PostMapping
    @Operation(summary = "新增部门", description = "新增部门")
    public Result<Boolean> save(@Parameter(description = "部门信息", required = true) @RequestBody SysDept dept) {
        boolean success = sysDeptService.insertDept(dept);
        return Result.of(success, "新增成功", "新增失败");
    }

    /**
     * 更新部门
     */
    @PutMapping
    @Operation(summary = "更新部门", description = "更新部门")
    public Result<Boolean> update(@Parameter(description = "部门信息", required = true) @RequestBody SysDept dept) {
        boolean success = sysDeptService.updateDept(dept);
        return Result.of(success, "更新成功", "更新失败");
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/removeDept/{id}")
    @Operation(summary = "删除部门", description = "删除部门")
    public Result<Boolean> removeDept(@Parameter(description = "部门ID") @PathVariable Long id) {
        // 检查是否有子部门
        if (sysDeptService.hasChildDept(id)) {
            return Result.error("存在子部门，不能删除");
        }
        // 检查是否有关联用户
        if (sysDeptService.hasAssociatedUser(id)) {
            return Result.error("部门存在关联用户，不能删除");
        }
        boolean success = sysDeptService.removeById(id);
        return Result.of(success, "删除成功", "删除失败");
    }

    /**
     * 批量删除部门
     */
    @DeleteMapping("/batchRemove")
    @Operation(summary = "批量删除部门", description = "批量删除部门")
    public Result<Boolean> batchRemove(@Parameter(description = "部门ID数组") @RequestBody Long[] ids) {
        // 检查每个部门是否可以删除
        for (Long id : ids) {
            // 检查是否有子部门
            if (sysDeptService.hasChildDept(id)) {
                return Result.error("部门ID:" + id + "存在子部门，不能删除");
            }

            // 检查是否有关联用户
            if (sysDeptService.hasAssociatedUser(id)) {
                return Result.error("部门ID:" + id + "存在关联用户，不能删除");
            }
        }

        boolean success = sysDeptService.deleteDepts(ids);
        return Result.of(success, "批量删除成功", "批量删除失败");
    }

    /**
     * 查询部门树结构
     */
    @GetMapping("/tree")
    @Operation(summary = "查询部门树结构", description = "查询部门树结构，支持搜索")
    public Result<List<SysDept>> tree(
            @Parameter(description = "部门名称") @RequestParam(required = false) String deptName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {

        // 如果有搜索条件，返回扁平列表；否则返回树形结构
        if (deptName != null || status != null) {
            SysDept searchDept = new SysDept();
            searchDept.setDeptName(deptName);
            searchDept.setStatus(status);
            List<SysDept> deptList = sysDeptService.selectDeptList(searchDept);
            return Result.success(deptList);
        } else {
            List<SysDept> deptTree = sysDeptService.selectDeptTree();
            return Result.success(deptTree);
        }
    }

    /**
     * 根据用户ID查询部门
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户ID查询部门", description = "根据用户ID查询部门")
    public Result<List<SysDept>> getDeptsByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        List<SysDept> depts = sysDeptService.selectDeptsByUserId(userId);
        return Result.success(depts);
    }

    /**
     * 根据角色ID查询部门
     */
    @GetMapping("/role/{roleId}")
    @Operation(summary = "根据角色ID查询部门", description = "根据角色ID查询部门")
    public Result<List<SysDept>> getDeptsByRoleId(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<SysDept> depts = sysDeptService.selectDeptsByRoleId(roleId);
        return Result.success(depts);
    }

    /**
     * 校验部门编码是否唯一
     */
    @GetMapping("/checkDeptCodeUnique")
    @Operation(summary = "校验部门编码是否唯一", description = "校验部门编码是否唯一")
    public Result<Boolean> checkDeptCodeUnique(
            @Parameter(description = "部门编码") @RequestParam String deptCode,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId) {
        boolean unique = sysDeptService.checkDeptCodeUnique(deptCode, deptId);
        return Result.success(unique);
    }

    /**
     * 校验部门名称是否唯一
     */
    @GetMapping("/checkDeptNameUnique")
    @Operation(summary = "校验部门名称是否唯一", description = "校验部门名称是否唯一")
    public Result<Boolean> checkDeptNameUnique(
            @Parameter(description = "部门名称") @RequestParam String deptName,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId) {
        boolean unique = sysDeptService.checkDeptNameUnique(deptName, deptId);
        return Result.success(unique);
    }
}
