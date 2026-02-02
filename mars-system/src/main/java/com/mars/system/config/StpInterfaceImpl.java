package com.mars.system.config;

import cn.dev33.satoken.stp.StpInterface;
import com.mars.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Sa-Token权限认证实现
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final SysUserService userService;

    /**
     * 获取权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return userService.getPermissions(Long.parseLong(loginId.toString()));
    }

    /**
     * 获取角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return userService.getRoleCodes(Long.parseLong(loginId.toString()));
    }
}
