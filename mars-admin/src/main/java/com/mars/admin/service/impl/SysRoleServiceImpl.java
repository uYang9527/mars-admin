package com.mars.admin.service.impl;

import com.mars.admin.entity.SysRole;
import com.mars.admin.mapper.SysRoleMapper;
import com.mars.admin.service.ISysRoleService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.mars.admin.entity.table.SysRoleTableDef.SYS_ROLE;

/**
 * 系统角色Service实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

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
        return this.removeByIds(Arrays.asList(roleIds));
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
        // TODO: 实现角色菜单分配逻辑，需要创建SysRoleMenu的mapper和service
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignDepts(Long roleId, Long[] deptIds) {
        // TODO: 实现角色部门分配逻辑，需要创建SysRoleDept的mapper和service
        return true;
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
} 