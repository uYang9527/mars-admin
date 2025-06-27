package com.mars.admin.mapper;

import com.mars.admin.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统菜单Mapper接口
 * 继承 BasePlusMapper 获得更多便捷方法
 *
 * @author Mars
 */
@Mapper
public interface SysMenuMapper extends BasePlusMapper<SysMenu> {

    /**
     * 根据菜单编码查询菜单
     *
     * @param menuCode 菜单编码
     * @return 菜单信息
     */
    default SysMenu selectByMenuCode(String menuCode) {
        return selectOneByField("menu_code", menuCode);
    }

    /**
     * 根据权限标识查询菜单
     *
     * @param perms 权限标识
     * @return 菜单信息
     */
    default SysMenu selectByPerms(String perms) {
        return selectOneByField("perms", perms);
    }

    /**
     * 分页查询菜单列表
     *
     * @param menu 查询条件
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 根据用户ID查询菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenusByRoleId(@Param("roleId") Long roleId);

    /**
     * 查询所有正常状态的菜单
     *
     * @return 菜单列表
     */
    @Select("SELECT * FROM sys_menu WHERE status = 1 AND is_deleted = 0 ORDER BY parent_id, order_num")
    List<SysMenu> selectNormalMenus();

    /**
     * 根据父菜单ID查询子菜单列表
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    List<SysMenu> selectChildrenByParentId(@Param("parentId") Long parentId);

    /**
     * 查询菜单树
     *
     * @return 菜单树列表
     */
    List<SysMenu> selectMenuTree();

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectPermsByUserId(@Param("userId") Long userId);

    /**
     * 根据菜单类型查询菜单列表
     *
     * @param menuType 菜单类型
     * @return 菜单列表
     */
    default List<SysMenu> selectByMenuType(String menuType) {
        return selectListByField("menu_type", menuType);
    }

    /**
     * 根据父菜单ID查询子菜单列表（便捷方法）
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    default List<SysMenu> selectByParentId(Long parentId) {
        return selectListByField("parent_id", parentId);
    }

    /**
     * 根据状态查询菜单列表
     *
     * @param status 状态
     * @return 菜单列表
     */
    default List<SysMenu> selectByStatus(Integer status) {
        return selectListByField("status", status);
    }

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode 菜单编码
     * @return 是否存在
     */
    default boolean existsByMenuCode(String menuCode) {
        return existsByField("menu_code", menuCode);
    }

    /**
     * 检查权限标识是否存在
     *
     * @param perms 权限标识
     * @return 是否存在
     */
    default boolean existsByPerms(String perms) {
        return existsByField("perms", perms);
    }
} 