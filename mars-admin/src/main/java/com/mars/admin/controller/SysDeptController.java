package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysDept;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class SysDeptController extends BaseController<SysDept, Long> {

    @Autowired
    private ISysDeptService sysDeptService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/dept/list           - 获取所有部门
    // GET    /system/dept/{id}           - 根据ID获取部门
    // GET    /system/dept/page           - 分页查询部门
    // POST   /system/dept                - 新增部门
    // PUT    /system/dept                - 更新部门
    // DELETE /system/dept/{id}           - 删除部门
    // DELETE /system/dept/batch          - 批量删除部门

    /**
     * 查询部门树结构
     */
    @GetMapping("/tree")
    @Operation(summary = "查询部门树结构", description = "查询部门树结构")
    public Result<List<SysDept>> tree() {
        List<SysDept> deptTree = sysDeptService.selectDeptTree();
        return Result.success(deptTree);
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