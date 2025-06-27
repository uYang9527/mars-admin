package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysMenu;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/menu")
@Tag(name = "系统菜单管理", description = "系统菜单管理相关接口")
public class SysMenuController extends BaseController<SysMenu, Long> {

    @Autowired
    private ISysMenuService sysMenuService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/menu/list           - 获取所有菜单
    // GET    /system/menu/{id}           - 根据ID获取菜单
    // GET    /system/menu/page           - 分页查询菜单
    // POST   /system/menu                - 新增菜单
    // PUT    /system/menu                - 更新菜单
    // DELETE /system/menu/{id}           - 删除菜单
    // DELETE /system/menu/batch          - 批量删除菜单

    /**
     * 查询菜单树结构
     */
    @GetMapping("/tree")
    @Operation(summary = "查询菜单树结构", description = "查询菜单树结构")
    public Result<List<SysMenu>> tree() {
        List<SysMenu> menuTree = sysMenuService.selectMenuTree();
        return Result.success(menuTree);
    }

    /**
     * 根据用户ID查询菜单
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "根据用户ID查询菜单", description = "根据用户ID查询菜单")
    public Result<List<SysMenu>> getMenusByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        List<SysMenu> menus = sysMenuService.selectMenusByUserId(userId);
        return Result.success(menus);
    }

    /**
     * 根据角色ID查询菜单
     */
    @GetMapping("/role/{roleId}")
    @Operation(summary = "根据角色ID查询菜单", description = "根据角色ID查询菜单")
    public Result<List<SysMenu>> getMenusByRoleId(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<SysMenu> menus = sysMenuService.selectMenusByRoleId(roleId);
        return Result.success(menus);
    }

    /**
     * 根据用户ID查询权限
     */
    @GetMapping("/permissions/{userId}")
    @Operation(summary = "根据用户ID查询权限", description = "根据用户ID查询权限")
    public Result<List<String>> getPermsByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        List<String> perms = sysMenuService.selectPermsByUserId(userId);
        return Result.success(perms);
    }

    /**
     * 校验菜单编码是否唯一
     */
    @GetMapping("/checkMenuCodeUnique")
    @Operation(summary = "校验菜单编码是否唯一", description = "校验菜单编码是否唯一")
    public Result<Boolean> checkMenuCodeUnique(
            @Parameter(description = "菜单编码") @RequestParam String menuCode,
            @Parameter(description = "菜单ID") @RequestParam(required = false) Long menuId) {
        boolean unique = sysMenuService.checkMenuCodeUnique(menuCode, menuId);
        return Result.success(unique);
    }

    /**
     * 校验权限标识是否唯一
     */
    @GetMapping("/checkPermsUnique")
    @Operation(summary = "校验权限标识是否唯一", description = "校验权限标识是否唯一")
    public Result<Boolean> checkPermsUnique(
            @Parameter(description = "权限标识") @RequestParam String perms,
            @Parameter(description = "菜单ID") @RequestParam(required = false) Long menuId) {
        boolean unique = sysMenuService.checkPermsUnique(perms, menuId);
        return Result.success(unique);
    }
} 