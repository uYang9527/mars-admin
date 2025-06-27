package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.SysUser;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统用户Controller
 * 继承BaseController获得基础的增删改查功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/user")
@Tag(name = "系统用户管理", description = "系统用户管理相关接口")
public class SysUserController extends BaseController<SysUser, Long> {

    @Autowired
    private ISysUserService sysUserService;

    // 继承BaseController后自动拥有基础的增删改查功能：
    // GET    /system/user/list           - 获取所有用户
    // GET    /system/user/{id}           - 根据ID获取用户
    // GET    /system/user/page           - 分页查询用户（重写以支持查询条件）
    // POST   /system/user                - 新增用户
    // PUT    /system/user                - 更新用户
    // DELETE /system/user/{id}           - 删除用户
    // DELETE /system/user/batch          - 批量删除用户
    
    // 基础的增删改查功能已由BaseController提供，这里重写部分方法以支持业务逻辑

    /**
     * 分页查询用户（重写以支持查询条件）
     */
    @GetMapping("/pageList")
    @Operation(summary = "分页查询用户", description = "分页查询用户列表，支持条件筛选")
    public Result<com.mybatisflex.core.paginate.Page<SysUser>> pageList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名") @RequestParam(required = false) String userName,
            @Parameter(description = "昵称") @RequestParam(required = false) String nickName,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email,
            @Parameter(description = "性别") @RequestParam(required = false) Integer userGender,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {

        // 构造查询条件
        SysUser queryUser = new SysUser();
        queryUser.setUsername(userName);
        queryUser.setNickname(nickName);
        queryUser.setPhone(phone);
        queryUser.setEmail(email);
        queryUser.setGender(userGender);
        queryUser.setStatus(status);

        // 分页查询
        com.mybatisflex.core.paginate.Page<SysUser> page = com.mybatisflex.core.paginate.Page.of(current, size);
        com.mybatisflex.core.paginate.Page<SysUser> result = sysUserService.selectUserPage(page, queryUser);

        return Result.success(result);
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/resetPwd")
    @Operation(summary = "重置用户密码", description = "重置用户密码")
    // @PreAuthorize("hasAuthority('system:user:resetPwd')")
    public Result<Void> resetPwd(@RequestBody SysUser user) {
        boolean result = sysUserService.resetPassword(user.getId(), user.getPassword());
        return result ? Result.success() : Result.error("重置密码失败");
    }

    /**
     * 修改用户状态
     */
    @PutMapping("/changeStatus")
    @Operation(summary = "修改用户状态", description = "修改用户状态")
    // @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> changeStatus(@RequestBody SysUser user) {
        boolean result = sysUserService.changeStatus(user.getId(), user.getStatus());
        return result ? Result.success() : Result.error("修改状态失败");
    }

    /**
     * 分配用户角色
     */
    @PutMapping("/authRole")
    @Operation(summary = "分配用户角色", description = "分配用户角色")
    // @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> authRole(@RequestBody SysUser user) {
        Long[] roleIds = user.getRoleIds().toArray(new Long[0]);
        boolean result = sysUserService.assignRoles(user.getId(), roleIds);
        return result ? Result.success() : Result.error("分配角色失败");
    }

    /**
     * 导出用户数据
     */
    @PostMapping("/export")
    @Operation(summary = "导出用户数据", description = "导出用户数据")
    // @PreAuthorize("hasAuthority('system:user:export')")
    public Result<List<SysUser>> export(@RequestBody SysUser user) {
        List<SysUser> list = sysUserService.exportUser(user);
        return Result.success(list);
    }

    /**
     * 导入用户数据
     */
    @PostMapping("/importData")
    @Operation(summary = "导入用户数据", description = "导入用户数据")
    // @PreAuthorize("hasAuthority('system:user:import')")
    public Result<String> importData(
            @RequestBody List<SysUser> userList,
            @Parameter(description = "是否更新已存在数据") @RequestParam(defaultValue = "false") boolean updateSupport) {
        String message = sysUserService.importUser(userList, updateSupport);
        return Result.success(message);
    }

    /**
     * 校验用户名是否唯一
     */
    @GetMapping("/checkUsernameUnique")
    @Operation(summary = "校验用户名是否唯一", description = "校验用户名是否唯一")
    public Result<Boolean> checkUsernameUnique(
            @Parameter(description = "用户名") @RequestParam String username,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        boolean unique = sysUserService.checkUsernameUnique(username, userId);
        return Result.success(unique);
    }

    /**
     * 校验邮箱是否唯一
     */
    @GetMapping("/checkEmailUnique")
    @Operation(summary = "校验邮箱是否唯一", description = "校验邮箱是否唯一")
    public Result<Boolean> checkEmailUnique(
            @Parameter(description = "邮箱") @RequestParam String email,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        boolean unique = sysUserService.checkEmailUnique(email, userId);
        return Result.success(unique);
    }

    /**
     * 校验手机号是否唯一
     */
    @GetMapping("/checkPhoneUnique")
    @Operation(summary = "校验手机号是否唯一", description = "校验手机号是否唯一")
    public Result<Boolean> checkPhoneUnique(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        boolean unique = sysUserService.checkPhoneUnique(phone, userId);
        return Result.success(unique);
    }
} 