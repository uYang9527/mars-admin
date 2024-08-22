package com.mars.easy.admin.modules.system.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mars.easy.admin.modules.system.pojo.po.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统角色-菜单表 Mapper 接口
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-08-21
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    void insertBatchSysRoleMenu(@Param("menuIds") List<String> menuIds, @Param("roleId") Integer roleId);

}
