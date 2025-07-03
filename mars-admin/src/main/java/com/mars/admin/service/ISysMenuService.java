package com.mars.admin.service;

import com.mars.admin.entity.SysMenu;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * 系统菜单Service接口
 * 继承 BaseService 获得更多便捷方法
 *
 * @author Mars
 */
public interface ISysMenuService extends BaseService<SysMenu> {

    /**
     * 根据菜单编码查询菜单
     *
     * @param menuCode 菜单编码
     * @return 菜单信息
     */
    SysMenu selectByMenuCode(String menuCode);

    /**
     * 根据权限标识查询菜单
     *
     * @param perms 权限标识
     * @return 菜单信息
     */
    SysMenu selectByPerms(String perms);

    /**
     * 分页查询菜单列表
     *
     * @param page 分页参数
     * @param menu 查询条件
     * @return 菜单分页列表
     */
    Page<SysMenu> selectMenuPage(Page<SysMenu> page, SysMenu menu);

    /**
     * 根据用户ID查询菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenusByUserId(Long userId);

    /**
     * 根据角色ID查询菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenusByRoleId(Long roleId);

    /**
     * 查询所有正常状态的菜单
     *
     * @return 菜单列表
     */
    List<SysMenu> selectNormalMenus();

    /**
     * 根据父菜单ID查询子菜单列表
     *
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    List<SysMenu> selectChildrenByParentId(Long parentId);

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
    List<String> selectPermsByUserId(Long userId);

    /**
     * 新增菜单
     *
     * @param menu 菜单信息
     * @return 新增结果
     */
    boolean insertMenu(SysMenu menu);

    /**
     * 修改菜单
     *
     * @param menu 菜单信息
     * @return 修改结果
     */
    boolean updateMenu(SysMenu menu);

    /**
     * 删除单个菜单
     *
     * @param menuId 菜单ID
     * @return 删除结果
     */
    boolean deleteMenuById(Long menuId);

    /**
     * 删除菜单
     *
     * @param menuIds 菜单ID数组
     * @return 删除结果
     */
    boolean deleteMenus(Long[] menuIds);

    /**
     * 校验菜单编码是否唯一
     *
     * @param menuCode 菜单编码
     * @param menuId 菜单ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkMenuCodeUnique(String menuCode, Long menuId);

    /**
     * 校验权限标识是否唯一
     *
     * @param perms 权限标识
     * @param menuId 菜单ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkPermsUnique(String perms, Long menuId);

    /**
     * 检查菜单是否有子菜单
     *
     * @param menuId 菜单ID
     * @return 是否有子菜单
     */
    boolean hasChildMenu(Long menuId);

    /**
     * 刷新菜单缓存
     */
    void refreshCache();
} 