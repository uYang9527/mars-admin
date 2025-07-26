package com.mars.admin.modules.system.service.impl;

import com.mars.admin.modules.system.entity.SysRole;
import com.mars.admin.modules.system.entity.SysRoleDept;
import com.mars.admin.modules.system.entity.SysRoleMenu;
import com.mars.admin.framework.exception.BusinessException;
import com.mars.admin.modules.system.mapper.SysRoleDeptMapper;
import com.mars.admin.modules.system.mapper.SysRoleMapper;
import com.mars.admin.modules.system.mapper.SysRoleMenuMapper;
import com.mars.admin.modules.system.service.ISysRoleService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.CacheableServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mars.admin.modules.system.entity.table.SysRoleDeptTableDef.SYS_ROLE_DEPT;
import static com.mars.admin.modules.system.entity.table.SysRoleMenuTableDef.SYS_ROLE_MENU;
import static com.mars.admin.modules.system.entity.table.SysRoleTableDef.SYS_ROLE;


/**
 * 系统角色Service实现类
 * 继承 CacheableServiceImpl 获得更多便捷方法和缓存功能
 *
 * @author Mars
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "role")
public class SysRoleServiceImpl extends CacheableServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;

    @Override
    public SysRole selectByRoleCode(String roleCode) {
        return sysRoleMapper.selectByRoleCode(roleCode);
    }

    @Override
    public SysRole selectByRoleKey(String roleKey) {
        return sysRoleMapper.selectByRoleKey(roleKey);
    }

    @Override
    public SysRole selectRoleDetailById(Long roleId) {
        return sysRoleMapper.selectRoleDetailById(roleId);
    }

    @Override
    public Page<SysRole> selectRolePage(Page<SysRole> page, SysRole role) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_ROLE)
                .where(SYS_ROLE.IS_DELETED.eq(0));

        if (StringUtils.hasText(role.getRoleName())) {
            query.and(SYS_ROLE.ROLE_NAME.like(role.getRoleName()));
        }
        if (StringUtils.hasText(role.getRoleCode())) {
            query.and(SYS_ROLE.ROLE_CODE.like(role.getRoleCode()));
        }
        if (StringUtils.hasText(role.getRoleKey())) {
            query.and(SYS_ROLE.ROLE_KEY.like(role.getRoleKey()));
        }
        if (role.getStatus() != null) {
            query.and(SYS_ROLE.STATUS.eq(role.getStatus()));
        }

        query.orderBy(SYS_ROLE.ROLE_SORT.asc(), SYS_ROLE.CREATE_TIME.desc());

        return this.page(page, query);
    }

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        return sysRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    public List<SysRole> selectNormalRoles() {
        return sysRoleMapper.selectNormalRoles();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertRole(SysRole role) {
        return this.save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(SysRole role) {
        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoles(Long[] roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            return false;
        }

        try {
            for (Long roleId : roleIds) {
                // 先检查是否系统内置角色，避免删除系统角色
                SysRole role = this.getById(roleId);
                if (role != null && role.getIsSystem() != null && role.getIsSystem() == 1) {
                    throw new BusinessException("系统内置角色不能删除");
                }

                // 删除角色菜单关联
                QueryWrapper roleMenuQuery = QueryWrapper.create()
                        .where(SYS_ROLE_MENU.ROLE_ID.eq(roleId));
                sysRoleMenuMapper.deleteByQuery(roleMenuQuery);

                // 删除角色部门关联
                QueryWrapper roleDeptQuery = QueryWrapper.create()
                        .where(SYS_ROLE_DEPT.ROLE_ID.eq(roleId));
                sysRoleDeptMapper.deleteByQuery(roleDeptQuery);

                // 设置删除时间
                SysRole deleteRole = new SysRole();
                deleteRole.setId(roleId);
                deleteRole.setDeleteTime(LocalDateTime.now());
                this.updateById(deleteRole);
            }

            // 批量逻辑删除角色
            return this.removeByIds(Arrays.asList(roleIds));
        } catch (BusinessException e) {
            log.error("删除角色失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("删除角色失败: {}", e.getMessage(), e);
            throw new BusinessException("删除角色失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeStatus(Long roleId, Integer status) {
        SysRole role = new SysRole();
        role.setId(roleId);
        role.setStatus(status);
        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenus(Long roleId, Long[] menuIds) {
        if (roleId == null) {
            throw new BusinessException("角色ID不能为空");
        }

        try {
            // 删除角色原有菜单
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(SYS_ROLE_MENU.ROLE_ID.eq(roleId));
            sysRoleMenuMapper.deleteByQuery(queryWrapper);

            // 如果没有分配菜单，直接返回
            if (menuIds == null || menuIds.length == 0) {
                return true;
            }

            // 批量插入新的角色菜单关联
            List<SysRoleMenu> roleMenus = new ArrayList<>();
            for (Long menuId : menuIds) {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenu.setCreateBy(roleId); // 当前登录用户ID，可从上下文获取
                roleMenus.add(roleMenu);
            }

            return sysRoleMenuMapper.insertBatch(roleMenus) > 0;
        } catch (Exception e) {
            log.error("分配菜单失败: {}", e.getMessage(), e);
            throw new BusinessException("分配菜单失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignDepts(Long roleId, Long[] deptIds) {
        if (roleId == null) {
            throw new BusinessException("角色ID不能为空");
        }

        try {
            // 删除角色原有部门
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(SYS_ROLE_DEPT.ROLE_ID.eq(roleId));
            sysRoleDeptMapper.deleteByQuery(queryWrapper);

            // 如果没有分配部门，直接返回
            if (deptIds == null || deptIds.length == 0) {
                return true;
            }

            // 批量插入新的角色部门关联
            List<SysRoleDept> roleDepts = new ArrayList<>();
            for (Long deptId : deptIds) {
                SysRoleDept roleDept = new SysRoleDept();
                roleDept.setRoleId(roleId);
                roleDept.setDeptId(deptId);
                roleDept.setCreateBy(roleId); // 当前登录用户ID，可从上下文获取
                roleDepts.add(roleDept);
            }

            return sysRoleDeptMapper.insertBatch(roleDepts) > 0;
        } catch (Exception e) {
            log.error("分配部门失败: {}", e.getMessage(), e);
            throw new BusinessException("分配部门失败: " + e.getMessage());
        }
    }

    @Override
    public boolean checkRoleCodeUnique(String roleCode, Long roleId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_ROLE.ROLE_CODE.eq(roleCode))
                .and(SYS_ROLE.IS_DELETED.eq(0));

        if (roleId != null) {
            query.and(SYS_ROLE.ID.ne(roleId));
        }

        return this.count(query) == 0;
    }

    @Override
    public boolean checkRoleKeyUnique(String roleKey, Long roleId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_ROLE.ROLE_KEY.eq(roleKey))
                .and(SYS_ROLE.IS_DELETED.eq(0));

        if (roleId != null) {
            query.and(SYS_ROLE.ID.ne(roleId));
        }

        return this.count(query) == 0;
    }

    @Override
    public List<SysRole> exportRole(SysRole role) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_ROLE)
                .where(SYS_ROLE.IS_DELETED.eq(0));

        if (StringUtils.hasText(role.getRoleName())) {
            query.and(SYS_ROLE.ROLE_NAME.like(role.getRoleName()));
        }
        if (role.getStatus() != null) {
            query.and(SYS_ROLE.STATUS.eq(role.getStatus()));
        }

        return this.list(query);
    }

    @Override
    public List<SysRole> selectRolesByMenuId(Long menuId) {
        return sysRoleMapper.selectRolesByMenuId(menuId);
    }

    @Override
    public void refreshCache() {
        log.info("清空系统角色缓存");
        // @CacheEvict注解会自动清除sysRole缓存空间的所有数据
    }
}
