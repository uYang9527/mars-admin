package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.po.SysUserRole;

import java.util.List;

/**
 * <p>
 * 系统用户-角色关联表 服务类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2022-10-01
 */
public interface SysUserRoleService extends IService<SysUserRole> {
    List<String> getUserRolesByUserId(Long userId);
}
