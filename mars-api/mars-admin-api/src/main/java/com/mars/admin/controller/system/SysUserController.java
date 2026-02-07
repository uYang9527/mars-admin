package com.mars.admin.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.crypto.EncryptResponse;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.RepeatSubmit;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.system.entity.SysRole;
import com.mars.system.entity.SysUser;
import com.mars.common.exception.BusinessException;
import com.mars.system.service.SysRoleService;
import com.mars.system.service.SysUserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;
    private final SysRoleService roleService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("sys:user:list")
    @EncryptResponse
    public Result<PageResult<SysUser>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long deptId) {
        return Result.ok(userService.page(page, pageSize, username, status, deptId));
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("sys:user:list")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        SysUser user = userService.getDetail(id);
        List<SysRole> roles = roleService.listByUserId(id);
        List<Long> roleIds = roles.stream().map(SysRole::getId).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("roleIds", roleIds);
        return Result.ok(result);
    }

    /**
     * 创建
     */
    @PostMapping
    @SaCheckPermission("sys:user:add")
    @RepeatSubmit
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    public Result<Void> create(@RequestBody UserRequest request) {
        userService.create(request.getUser(), request.getRoleIds());
        return Result.ok();
    }

    /**
     * 更新
     */
    @PutMapping
    @SaCheckPermission("sys:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    public Result<Void> update(@RequestBody UserRequest request) {
        userService.update(request.getUser(), request.getRoleIds());
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("sys:user:delete")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.ok();
    }

    /**
     * 重置密码
     */
    @PostMapping("/{id}/reset-password")
    @SaCheckPermission("sys:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    public Result<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return Result.ok();
    }

    /**
     * 审核通过
     */
    @PostMapping("/{id}/approve")
    @SaCheckPermission("sys:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    public Result<Void> approve(@PathVariable Long id) {
        SysUser user = userService.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getStatus() != 2) {
            throw new BusinessException("该用户不在待审核状态");
        }
        user.setStatus(1);
        userService.updateById(user);
        return Result.ok();
    }

    /**
     * 审核拒绝
     */
    @PostMapping("/{id}/reject")
    @SaCheckPermission("sys:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    public Result<Void> reject(@PathVariable Long id) {
        SysUser user = userService.getById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getStatus() != 2) {
            throw new BusinessException("该用户不在待审核状态");
        }
        user.setStatus(3);
        userService.updateById(user);
        return Result.ok();
    }

    @Data
    public static class UserRequest {
        private SysUser user;
        private List<Long> roleIds;
    }
}
