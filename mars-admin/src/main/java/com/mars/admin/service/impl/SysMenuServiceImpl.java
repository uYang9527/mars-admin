package com.mars.admin.service.impl;

import com.mars.admin.entity.SysMenu;
import com.mars.admin.mapper.SysMenuMapper;
import com.mars.admin.service.ISysMenuService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.mars.admin.entity.table.SysMenuTableDef.SYS_MENU;

/**
 * 系统菜单Service实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars
 */
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public SysMenu selectByMenuCode(String menuCode) {
        return sysMenuMapper.selectByMenuCode(menuCode);
    }

    @Override
    public SysMenu selectByPerms(String perms) {
        return sysMenuMapper.selectByPerms(perms);
    }

    @Override
    public Page<SysMenu> selectMenuPage(Page<SysMenu> page, SysMenu menu) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_MENU)
                .where(SYS_MENU.IS_DELETED.eq(0));

        if (StringUtils.hasText(menu.getMenuName())) {
            query.and(SYS_MENU.MENU_NAME.like(menu.getMenuName()));
        }
        if (StringUtils.hasText(menu.getMenuCode())) {
            query.and(SYS_MENU.MENU_CODE.like(menu.getMenuCode()));
        }
        if (StringUtils.hasText(menu.getMenuType())) {
            query.and(SYS_MENU.MENU_TYPE.eq(menu.getMenuType()));
        }
        if (menu.getStatus() != null) {
            query.and(SYS_MENU.STATUS.eq(menu.getStatus()));
        }
        if (menu.getParentId() != null) {
            query.and(SYS_MENU.PARENT_ID.eq(menu.getParentId()));
        }

        query.orderBy(SYS_MENU.PARENT_ID.asc(), SYS_MENU.ORDER_NUM.asc());

        return this.page(page, query);
    }

    @Override
    public List<SysMenu> selectMenusByUserId(Long userId) {
        return sysMenuMapper.selectMenusByUserId(userId);
    }

    @Override
    public List<SysMenu> selectMenusByRoleId(Long roleId) {
        return sysMenuMapper.selectMenusByRoleId(roleId);
    }

    @Override
    public List<SysMenu> selectNormalMenus() {
        return sysMenuMapper.selectNormalMenus();
    }

    @Override
    public List<SysMenu> selectChildrenByParentId(Long parentId) {
        return sysMenuMapper.selectChildrenByParentId(parentId);
    }

    @Override
    public List<SysMenu> selectMenuTree() {
        // 获取所有菜单数据
        List<SysMenu> allMenus = sysMenuMapper.selectMenuTree();
        
        // 构建树形结构
        return buildMenuTree(allMenus, 0L);
    }
    
    /**
     * 构建菜单树形结构
     * 
     * @param menus 所有菜单列表
     * @param parentId 父菜单ID
     * @return 树形菜单列表
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menus, Long parentId) {
        List<SysMenu> treeMenus = new ArrayList<>();
        
        for (SysMenu menu : menus) {
            if (menu.getParentId() != null && menu.getParentId().equals(parentId)) {
                // 递归查找子菜单
                List<SysMenu> children = buildMenuTree(menus, menu.getId());
                menu.setChildren(children);
                treeMenus.add(menu);
            }
        }
        
        return treeMenus;
    }

    @Override
    public List<String> selectPermsByUserId(Long userId) {
        return sysMenuMapper.selectPermsByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertMenu(SysMenu menu) {
        return this.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(SysMenu menu) {
        return this.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenus(Long[] menuIds) {
        return this.removeByIds(Arrays.asList(menuIds));
    }

    @Override
    public boolean checkMenuCodeUnique(String menuCode, Long menuId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_MENU.MENU_CODE.eq(menuCode))
                .and(SYS_MENU.IS_DELETED.eq(0));

        if (menuId != null) {
            query.and(SYS_MENU.ID.ne(menuId));
        }

        return this.count(query) == 0;
    }

    @Override
    public boolean checkPermsUnique(String perms, Long menuId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_MENU.PERMS.eq(perms))
                .and(SYS_MENU.IS_DELETED.eq(0));

        if (menuId != null) {
            query.and(SYS_MENU.ID.ne(menuId));
        }

        return this.count(query) == 0;
    }
} 