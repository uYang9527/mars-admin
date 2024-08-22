package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.dto.sysrolemenu.SysRoleMenuDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysRoleMenu;
import com.mars.easy.admin.modules.system.pojo.vo.sysrolemenu.SysRoleMenuVO;

/**
 * <p>
 * 系统角色-菜单表 服务类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2022-10-01
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    void change(SysRoleMenuDTO dto);

    SysRoleMenuVO queryRoleMenu(Integer roleId);
}
