package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.dto.sysrole.SysRoleCreateDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysrole.SysRoleListDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysrole.SysRoleUpdateDTO;
import com.mars.easy.admin.modules.system.pojo.po.SysRole;
import com.mars.easy.admin.framework.entity.PageResult;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @Author 程序员Mars
 * @since 2022-10-01
 */
public interface SysRoleService extends IService<SysRole> {

    void create(SysRoleCreateDTO dto);

    void update(SysRoleUpdateDTO dto);

    void remove(SelectIdsDTO dto);

    void removeByMenuId(SelectIdsDTO dto);

    PageResult<SysRole> list(SysRoleListDTO dto);
}
