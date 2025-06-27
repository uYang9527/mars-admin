package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysRole;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/role")
@Tag(name = "系统角色管理", description = "系统角色管理相关接口")
public class SysRoleController extends BaseController<SysRole, Long> {

    @Autowired
    private ISysRoleService sysRoleService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/role/list           - 获取所有角色
    // GET    /system/role/{id}           - 根据ID获取角色
    // GET    /system/role/page           - 分页查询角色
    // POST   /system/role                - 新增角色
    // PUT    /system/role                - 更新角色
    // DELETE /system/role/{id}           - 删除角色
    // DELETE /system/role/batch          - 批量删除角色

    /**
     * 查询所有正常状态的角色
     */
    @GetMapping("/optionselect")
    @Operation(summary = "查询所有正常状态的角色", description = "查询所有正常状态的角色")
    public Result<List<SysRole>> optionselect() {
        List<SysRole> roles = sysRoleService.selectNormalRoles();
        return Result.success(roles);
    }

    // 基础的增删改查功能已由BaseController提供，这里只保留特有的业务方法

    /**
     * 修改角色状态
     */
    @PutMapping("/changeStatus")
    @Operation(summary = "修改角色状态", description = "修改角色状态")
    public Result<Void> changeStatus(@RequestBody SysRole role) {
        boolean result = sysRoleService.changeStatus(role.getId(), role.getStatus());
        return result ? Result.success() : Result.error("修改状态失败");
    }

    /**
     * 导出角色数据
     */
    @PostMapping("/export")
    @Operation(summary = "导出角色数据", description = "导出角色数据")
    public Result<List<SysRole>> export(@RequestBody SysRole role) {
        List<SysRole> list = sysRoleService.exportRole(role);
        return Result.success(list);
    }

    /**
     * 校验角色编码是否唯一
     */
    @GetMapping("/checkRoleCodeUnique")
    @Operation(summary = "校验角色编码是否唯一", description = "校验角色编码是否唯一")
    public Result<Boolean> checkRoleCodeUnique(
            @Parameter(description = "角色编码") @RequestParam String roleCode,
            @Parameter(description = "角色ID") @RequestParam(required = false) Long roleId) {
        boolean unique = sysRoleService.checkRoleCodeUnique(roleCode, roleId);
        return Result.success(unique);
    }

    /**
     * 校验角色权限字符是否唯一
     */
    @GetMapping("/checkRoleKeyUnique")
    @Operation(summary = "校验角色权限字符是否唯一", description = "校验角色权限字符是否唯一")
    public Result<Boolean> checkRoleKeyUnique(
            @Parameter(description = "角色权限字符") @RequestParam String roleKey,
            @Parameter(description = "角色ID") @RequestParam(required = false) Long roleId) {
        boolean unique = sysRoleService.checkRoleKeyUnique(roleKey, roleId);
        return Result.success(unique);
    }
} 