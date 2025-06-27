package com.mars.admin.service;

import com.mars.admin.entity.SysRole;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * 系统角色Service接口
 * 继承 BaseService 获得更多便捷方法
 *
 * @author Mars
 */
public interface ISysRoleService extends BaseService<SysRole> {

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    SysRole selectByRoleCode(String roleCode);

    /**
     * 根据角色权限字符串查询角色
     *
     * @param roleKey 角色权限字符串
     * @return 角色信息
     */
    SysRole selectByRoleKey(String roleKey);

    /**
     * 查询角色详情（包含菜单、部门信息）
     *
     * @param roleId 角色ID
     * @return 角色详情
     */
    SysRole selectRoleDetailById(Long roleId);

    /**
     * 分页查询角色列表
     *
     * @param page 分页参数
     * @param role 查询条件
     * @return 角色分页列表
     */
    Page<SysRole> selectRolePage(Page<SysRole> page, SysRole role);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 查询所有正常状态的角色
     *
     * @return 角色列表
     */
    List<SysRole> selectNormalRoles();

    /**
     * 新增角色
     *
     * @param role 角色信息
     * @return 新增结果
     */
    boolean insertRole(SysRole role);

    /**
     * 修改角色
     *
     * @param role 角色信息
     * @return 修改结果
     */
    boolean updateRole(SysRole role);

    /**
     * 删除角色
     *
     * @param roleIds 角色ID数组
     * @return 删除结果
     */
    boolean deleteRoles(Long[] roleIds);

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @param status 状态
     * @return 修改结果
     */
    boolean changeStatus(Long roleId, Integer status);

    /**
     * 分配角色菜单
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID数组
     * @return 分配结果
     */
    boolean assignMenus(Long roleId, Long[] menuIds);

    /**
     * 分配角色部门
     *
     * @param roleId 角色ID
     * @param deptIds 部门ID数组
     * @return 分配结果
     */
    boolean assignDepts(Long roleId, Long[] deptIds);

    /**
     * 校验角色编码是否唯一
     *
     * @param roleCode 角色编码
     * @param roleId 角色ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkRoleCodeUnique(String roleCode, Long roleId);

    /**
     * 校验角色权限字符串是否唯一
     *
     * @param roleKey 角色权限字符串
     * @param roleId 角色ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkRoleKeyUnique(String roleKey, Long roleId);

    /**
     * 导出角色数据
     *
     * @param role 查询条件
     * @return 角色列表
     */
    List<SysRole> exportRole(SysRole role);

    /**
     * 根据菜单ID查询角色列表
     *
     * @param menuId 菜单ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByMenuId(Long menuId);
} 