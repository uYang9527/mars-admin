package com.mars.easy.admin.modules.system.service;

import com.mars.easy.admin.modules.system.pojo.po.SysUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName SysPermissionService
 * @Author 程序员Mars
 * @Date 2024/2/4 15:12
 * @Version 1.0
 */
public interface SysPermissionService {
    Set<String> getMenuPermissions(SysUser sysUser);

    Set<String> getRoles(Long userId);

    Set<String> getRoles(SysUser sysUser);

    List<Long> getDepts(SysUser sysUser);

    List<Long> getDeptAndChildren(SysUser sysUser);

    Map<String,String> buildMenuRuleMap(SysUser sysUser, Set<String> findMenuIds);

}
