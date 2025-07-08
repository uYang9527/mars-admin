package com.mars.admin.framework.config;

import cn.dev33.satoken.stp.StpInterface;
import com.mars.admin.service.ISysMenuService;
import com.mars.admin.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Sa-Token权限接口实现类
 * 用于告诉Sa-Token当前用户具有哪些权限和角色
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 返回指定用户的权限列表
     * 
     * @param loginId   用户ID
     * @param loginType 登录类型
     * @return 权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        try {
            if (loginId == null) {
                log.warn("获取权限列表失败: loginId为空");
                return new ArrayList<>();
            }

            Long userId = Long.valueOf(loginId.toString());
            List<String> permissions = sysMenuService.selectPermsByUserId(userId);
            
            // 过滤掉空的权限标识
            List<String> filteredPermissions = new ArrayList<>();
            if (permissions != null) {
                for (String permission : permissions) {
                    if (permission != null && !permission.trim().isEmpty()) {
                        filteredPermissions.add(permission.trim());
                    }
                }
            }
            
            log.info("用户ID: {} 的权限列表: {}", userId, filteredPermissions);
            return filteredPermissions;
        } catch (Exception e) {
            log.error("获取用户权限列表失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 返回指定用户的角色列表
     * 
     * @param loginId   用户ID  
     * @param loginType 登录类型
     * @return 角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        try {
            if (loginId == null) {
                log.warn("获取角色列表失败: loginId为空");
                return new ArrayList<>();
            }

            Long userId = Long.valueOf(loginId.toString());
            // 这里可以根据实际需求实现角色获取逻辑
            // 目前项目主要使用权限控制，角色列表可以返回空或者实现具体逻辑
            
            log.info("用户ID: {} 的角色列表: {}", userId, new ArrayList<>());
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("获取用户角色列表失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
} 