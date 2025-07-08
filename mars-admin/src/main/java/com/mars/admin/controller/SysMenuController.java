package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysMenu;
import com.mars.admin.framework.common.Result;
import com.mars.admin.framework.common.annotation.OperationLog;
import com.mars.admin.framework.enums.BusinessType;
import com.mars.admin.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * 根据ID删除菜单（重写BaseController的方法）
     *
     * @param id 主键ID
     * @return 是否成功
     */
    @Operation(summary = "根据ID删除菜单", description = "根据主键ID删除菜单及关联数据")
    @DeleteMapping("/{id}")
    @OperationLog(title = "菜单管理", businessType = BusinessType.DELETE, 
                  description = "删除菜单")
    @Override
    public Result<Boolean> deleteById(@Parameter(description = "主键ID", required = true) @PathVariable Long id) {
        // 检查是否有子菜单
        if (sysMenuService.hasChildMenu(id)) {
            return Result.error("存在子菜单，不能删除");
        }
        
        boolean success = sysMenuService.deleteMenuById(id);
        return Result.of(success, "删除成功", "删除失败");
    }

    /**
     * 批量删除菜单（重写BaseController的方法）
     *
     * @param ids ID列表
     * @return 删除结果
     */
    @Operation(summary = "批量删除菜单", description = "根据ID列表批量删除菜单及关联数据")
    @DeleteMapping("/batch")
    @OperationLog(title = "菜单管理", businessType = BusinessType.DELETE, 
                  description = "批量删除菜单")
    public Result<Boolean> deleteBatch(@Parameter(description = "ID列表", required = true) @RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("ID列表不能为空");
        }
        
        // 检查每个菜单是否可以删除
        for (Long id : ids) {
            // 检查是否有子菜单
            if (sysMenuService.hasChildMenu(id)) {
                return Result.error("菜单ID:" + id + "存在子菜单，不能删除");
            }
        }
        
        Long[] menuIds = ids.toArray(new Long[0]);
        boolean success = sysMenuService.deleteMenus(menuIds);
        return Result.of(success, "批量删除成功", "批量删除失败");
    }

    /**
     * 刷新菜单缓存
     */
    @DeleteMapping("/refreshCache")
    @Operation(summary = "刷新菜单缓存", description = "刷新菜单缓存")
    @OperationLog(title = "菜单管理", businessType = BusinessType.CLEAN, description = "刷新菜单缓存")
    public Result<Void> refreshCache() {
        sysMenuService.refreshCache();
        return Result.success();
    }

    /**
     * 新增菜单（重写BaseController的方法）
     */
    @PostMapping
    @Operation(summary = "新增菜单", description = "新增菜单信息")
    @OperationLog(title = "菜单管理", businessType = BusinessType.INSERT, 
                  description = "新增菜单")
    @Override
    public Result<Boolean> save(@RequestBody SysMenu entity) {
        // 校验菜单编码是否唯一
        if (!sysMenuService.checkMenuCodeUnique(entity.getMenuCode(), null)) {
            return Result.error("菜单编码已存在，请重新输入");
        }
        // 校验权限标识是否唯一
        if (StringUtils.isNotEmpty(entity.getPerms()) && !sysMenuService.checkPermsUnique(entity.getPerms(), null)) {
            return Result.error("权限标识已存在，请重新输入");
        }
        boolean success = sysMenuService.insertMenu(entity);
        return Result.of(success, "新增成功", "新增失败");
    }

    /**
     * 修改菜单（重写BaseController的方法）
     */
    @PutMapping
    @Operation(summary = "修改菜单", description = "修改菜单信息")
    @OperationLog(title = "菜单管理", businessType = BusinessType.UPDATE, 
                  description = "修改菜单")
    @Override
    public Result<Boolean> update(@RequestBody SysMenu entity) {
        // 校验菜单编码是否唯一
        if (!sysMenuService.checkMenuCodeUnique(entity.getMenuCode(), entity.getId())) {
            return Result.error("菜单编码已存在，请重新输入");
        }
        // 校验权限标识是否唯一
        if (StringUtils.isNotEmpty(entity.getPerms()) && !sysMenuService.checkPermsUnique(entity.getPerms(), entity.getId())) {
            return Result.error("权限标识已存在，请重新输入");
        }
        boolean success = sysMenuService.updateMenu(entity);
        return Result.of(success, "修改成功", "修改失败");
    }
} 