package com.mars.easy.admin.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.mars.easy.admin.modules.system.pojo.po.SysDataRole;
import com.mars.easy.admin.modules.system.pojo.vo.sysdatarole.SysDataRoleMenuVO;
import com.mars.easy.admin.framework.entity.SelectIdsDTO;
import com.mars.easy.admin.framework.entity.PageResult;
import java.util.List;

import com.mars.easy.admin.modules.system.pojo.dto.sysdatarole.SysDataRoleCreateDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysdatarole.SysDataRoleUpdateDTO;
import com.mars.easy.admin.modules.system.pojo.dto.sysdatarole.SysDataRoleListDTO;
import com.mars.easy.admin.modules.system.pojo.vo.sysdatarole.SysDataRoleVO;

/**
 * <p>
 * 数据权限管理 Service
 * </p>
 *
 * @Author 程序员Mars-admin
 * @since 2024-07-09
 */
public interface SysDataRoleService extends IService<SysDataRole> {

    void create(SysDataRoleCreateDTO dto);

    void update(SysDataRoleUpdateDTO dto);

    PageResult<SysDataRoleVO> page(SysDataRoleListDTO dto);

    List<SysDataRoleVO> list(SysDataRoleListDTO dto);

    void remove(SelectIdsDTO dto);

    SysDataRoleVO detail(Object id);

    SysDataRoleMenuVO queryDataRoleMenu();
}
