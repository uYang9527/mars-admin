package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.dto.sysmenu.SysUserRoleDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysUserDataRole;
import com.mars.easy.admin.modules.system.pojo.vo.sysuser.SysUserRoleVO;

/**
 * <p>
 * 系统用户-数据角色关联表 Service
 * </p>
 *
 * @Author 程序员Mars-admin
 * @since 2024-07-11
 */
public interface SysUserDataRoleService extends IService<SysUserDataRole> {


    void changeRole(SysUserRoleDTO dto);

    SysUserRoleVO queryRoleMenu(Long userId);
}
