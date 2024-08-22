package com.mars.easy.admin.modules.system.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mars.easy.admin.modules.system.pojo.po.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户-角色关联表 Mapper 接口
 * </p>
 *
 * @Author 程序员Mars
 * @since 2023-08-29
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    void insertBatchSysUserRole(@Param("roleIds") List<Long> roleIds, @Param("userId") Long userId);

    List<String> queryMenuIdByUserId(@Param("userId") Long userId);

    List<String> queryPermissionByUserId(@Param("userId") Long userId);

}
