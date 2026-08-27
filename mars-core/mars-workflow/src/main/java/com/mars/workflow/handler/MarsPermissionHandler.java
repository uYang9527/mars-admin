package com.mars.workflow.handler;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mars.system.entity.SysRole;
import com.mars.system.entity.SysUser;
import com.mars.system.mapper.SysUserRoleMapper;
import com.mars.system.service.SysRoleService;
import com.mars.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.dromara.warm.flow.core.handler.PermissionHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Warm-Flow 办理人权限处理器（对接 Sa-Token + Mars 用户体系）
 */
@Component
@RequiredArgsConstructor
public class MarsPermissionHandler implements PermissionHandler {

    private final SysUserService userService;
    private final SysRoleService roleService;
    private final SysUserRoleMapper userRoleMapper;

    @Override
    public List<String> permissions() {
        if (!StpUtil.isLogin()) {
            return new ArrayList<>();
        }
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser user = userService.getById(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        List<String> permissionList = new ArrayList<>();
        permissionList.add(String.valueOf(userId));

        List<SysRole> roles = roleService.listByUserId(userId);
        for (SysRole role : roles) {
            permissionList.add("role:" + role.getId());
        }

        if (user.getDeptId() != null) {
            permissionList.add("dept:" + user.getDeptId());
        }

        List<Long> postIds = userService.getPostIds(userId);
        for (Long postId : postIds) {
            permissionList.add("post:" + postId);
        }

        return permissionList;
    }

    @Override
    public String getHandler() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        return String.valueOf(StpUtil.getLoginIdAsLong());
    }

    @Override
    public List<String> convertPermissions(List<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return permissions;
        }

        Set<String> userIds = new HashSet<>();
        for (String permission : permissions) {
            if (permission == null || permission.isEmpty()) {
                continue;
            }
            if (permission.startsWith("role:")) {
                Long roleId = Long.parseLong(permission.substring(5));
                List<Long> ids = userRoleMapper.selectUserIdsByRoleId(roleId);
                ids.forEach(id -> userIds.add(String.valueOf(id)));
            } else if (permission.startsWith("dept:")) {
                Long deptId = Long.parseLong(permission.substring(5));
                List<SysUser> users = userService.list(new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getDeptId, deptId)
                        .eq(SysUser::getStatus, 1));
                users.forEach(u -> userIds.add(String.valueOf(u.getId())));
            } else if (permission.startsWith("post:")) {
                Long postId = Long.parseLong(permission.substring(5));
                List<SysUser> users = userService.list(new LambdaQueryWrapper<SysUser>()
                        .inSql(SysUser::getId,
                                "SELECT user_id FROM sys_user_post WHERE post_id = " + postId));
                users.forEach(u -> userIds.add(String.valueOf(u.getId())));
            } else {
                userIds.add(permission);
            }
        }
        return userIds.stream().collect(Collectors.toList());
    }
}
