package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysRole;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysRoleService;
import com.mybatisflex.core.paginate.Page;
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
     * 分页查询角色列表（重写BaseController方法以支持搜索条件）
     */
    @GetMapping("/pageList")
    @Operation(summary = "分页查询角色列表", description = "分页查询角色列表，支持多条件搜索")
    public Result<Page<SysRole>> pageList(
            @Parameter(description = "当前页", example = "1") @RequestParam(value = "current", defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", example = "10") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "角色名称") @RequestParam(value = "roleName", required = false) String roleName,
            @Parameter(description = "角色编码") @RequestParam(value = "roleCode", required = false) String roleCode,
            @Parameter(description = "角色权限字符") @RequestParam(value = "roleKey", required = false) String roleKey,
            @Parameter(description = "状态") @RequestParam(value = "status", required = false) Integer status) {
        
        // 构建查询条件
        SysRole queryCondition = new SysRole();
        queryCondition.setRoleName(roleName);
        queryCondition.setRoleCode(roleCode);
        queryCondition.setRoleKey(roleKey);
        queryCondition.setStatus(status);
        
        // 分页查询
        Page<SysRole> page = sysRoleService.selectRolePage(Page.of(current, size), queryCondition);
        return Result.success(page);
    }

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
     * 分配角色菜单权限
     */
    @PutMapping("/authMenu")
    @Operation(summary = "分配角色菜单权限", description = "分配角色菜单权限")
    public Result<Void> authMenu(@RequestBody SysRole role) {
        boolean result = sysRoleService.assignMenus(role.getId(), role.getMenuIds().toArray(new Long[0]));
        return result ? Result.success() : Result.error("分配菜单权限失败");
    }

    /**
     * 分配角色数据权限
     */
    @PutMapping("/authDept")
    @Operation(summary = "分配角色数据权限", description = "分配角色数据权限")
    public Result<Void> authDept(@RequestBody SysRole role) {
        boolean result = sysRoleService.assignDepts(role.getId(), role.getDeptIds().toArray(new Long[0]));
        return result ? Result.success() : Result.error("分配数据权限失败");
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

    /**
     * 根据ID删除角色（重写BaseController的方法）
     */
    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "根据角色ID删除角色")
    public Result<Boolean> deleteById(@Parameter(description = "角色ID", required = true) @PathVariable Long id) {
        boolean success = sysRoleService.deleteRoles(new Long[]{id});
        return Result.of(success, "删除成功", "删除失败");
    }

    /**
     * 批量删除角色（重写BaseController的方法）
     */
    @Override
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除角色", description = "批量删除角色")
    public Result<Boolean> deleteBatch(@Parameter(description = "ID列表", required = true) @RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("ID列表不能为空");
        }
        boolean success = sysRoleService.deleteRoles(ids.toArray(new Long[0]));
        return Result.of(success, "批量删除成功", "批量删除失败");
    }
} 