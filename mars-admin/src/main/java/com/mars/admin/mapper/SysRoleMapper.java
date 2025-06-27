package com.mars.admin.mapper;

import com.mars.admin.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统角色Mapper接口
 * 继承 BasePlusMapper 获得更多便捷方法
 *
 * @author Mars
 */
@Mapper
public interface SysRoleMapper extends BasePlusMapper<SysRole> {

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    default SysRole selectByRoleCode(String roleCode) {
        return selectOneByField("role_code", roleCode);
    }

    /**
     * 根据角色权限字符串查询角色
     *
     * @param roleKey 角色权限字符串
     * @return 角色信息
     */
    default SysRole selectByRoleKey(String roleKey) {
        return selectOneByField("role_key", roleKey);
    }

    /**
     * 分页查询角色列表
     *
     * @param role 查询条件
     * @return 角色列表
     */
    List<SysRole> selectRoleList(SysRole role);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 查询所有正常状态的角色
     *
     * @return 角色列表
     */
    @Select("SELECT * FROM sys_role WHERE status = 1 AND is_deleted = 0 ORDER BY role_sort")
    List<SysRole> selectNormalRoles();

    /**
     * 查询角色详情（包含菜单、部门信息）
     *
     * @param roleId 角色ID
     * @return 角色详情
     */
    SysRole selectRoleDetailById(@Param("roleId") Long roleId);

    /**
     * 根据菜单ID查询角色列表
     *
     * @param menuId 菜单ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据状态查询角色列表
     *
     * @param status 状态
     * @return 角色列表
     */
    default List<SysRole> selectByStatus(Integer status) {
        return selectListByField("status", status);
    }

    /**
     * 检查角色编码是否存在
     *
     * @param roleCode 角色编码
     * @return 是否存在
     */
    default boolean existsByRoleCode(String roleCode) {
        return existsByField("role_code", roleCode);
    }

    /**
     * 检查角色权限字符串是否存在
     *
     * @param roleKey 角色权限字符串
     * @return 是否存在
     */
    default boolean existsByRoleKey(String roleKey) {
        return existsByField("role_key", roleKey);
    }
} 