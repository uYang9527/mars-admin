package com.mars.admin.modules.system.service.impl;

import com.mars.admin.modules.system.entity.SysDept;
import com.mars.admin.modules.system.mapper.SysDeptMapper;
import com.mars.admin.modules.system.mapper.SysRoleDeptMapper;
import com.mars.admin.modules.system.mapper.SysUserDeptMapper;
import com.mars.admin.modules.system.service.ISysDeptService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.CacheableServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.mars.admin.modules.system.entity.table.SysDeptTableDef.SYS_DEPT;
import static com.mars.admin.modules.system.entity.table.SysRoleDeptTableDef.SYS_ROLE_DEPT;
import static com.mars.admin.modules.system.entity.table.SysUserDeptTableDef.SYS_USER_DEPT;


/**
 * 系统部门Service实现类
 * 继承 CacheableServiceImpl 获得更多便捷方法和缓存功能
 *
 * @author Mars
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "dept")
public class SysDeptServiceImpl extends CacheableServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserDeptMapper sysUserDeptMapper;

    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;

    @Override
    @Cacheable(key = "'code:' + #deptCode")
    public SysDept selectByDeptCode(String deptCode) {
        return sysDeptMapper.selectByDeptCode(deptCode);
    }

    @Override
    @Cacheable(key = "'name:' + #deptName")
    public SysDept selectByDeptName(String deptName) {
        return sysDeptMapper.selectByDeptName(deptName);
    }

    @Override
    public Page<SysDept> selectDeptPage(Page<SysDept> page, SysDept dept) {
        QueryWrapper query = QueryWrapper.create().select().from(SYS_DEPT).where(SYS_DEPT.IS_DELETED.eq(0));

        if (StringUtils.hasText(dept.getDeptName())) {
            query.and(SYS_DEPT.DEPT_NAME.like(dept.getDeptName()));
        }
        if (StringUtils.hasText(dept.getDeptCode())) {
            query.and(SYS_DEPT.DEPT_CODE.like(dept.getDeptCode()));
        }
        if (dept.getStatus() != null) {
            query.and(SYS_DEPT.STATUS.eq(dept.getStatus()));
        }
        if (dept.getParentId() != null) {
            query.and(SYS_DEPT.PARENT_ID.eq(dept.getParentId()));
        }

        query.orderBy(SYS_DEPT.PARENT_ID.asc(), SYS_DEPT.ORDER_NUM.asc());

        return this.page(page, query);
    }

    @Override
    public List<SysDept> selectDeptsByUserId(Long userId) {
        return sysDeptMapper.selectDeptsByUserId(userId);
    }

    @Override
    public List<SysDept> selectDeptsByRoleId(Long roleId) {
        return sysDeptMapper.selectDeptsByRoleId(roleId);
    }

    @Override
    @Cacheable(key = "'normal'")
    public List<SysDept> selectNormalDepts() {
        return sysDeptMapper.selectNormalDepts();
    }

    @Override
    @Cacheable(key = "'children:' + #parentId")
    public List<SysDept> selectChildrenByParentId(Long parentId) {
        return sysDeptMapper.selectChildrenByParentId(parentId);
    }

    @Override
    @Cacheable(key = "'list'")
    public List<SysDept> selectDeptList(SysDept dept) {
        return sysDeptMapper.selectDeptList(dept);
    }

    @Override
    public List<SysDept> selectDeptTree() {
        // 查询所有部门数据
        List<SysDept> allDepts = sysDeptMapper.selectDeptTree();

        // 构建树形结构
        return buildDeptTree(allDepts, 0L);
    }

    /**
     * 构建部门树
     *
     * @param deptList 部门列表
     * @param parentId 父级ID
     * @return 部门树
     */
    private List<SysDept> buildDeptTree(List<SysDept> deptList, Long parentId) {
        return deptList.stream().filter(dept -> dept.getParentId().equals(parentId)).peek(dept -> {
            // 递归查找子部门
            List<SysDept> children = buildDeptTree(deptList, dept.getId());
            dept.setChildren(children);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean insertDept(SysDept dept) {
        // 设置祖级列表
        if (dept.getParentId() != null && dept.getParentId() != 0) {
            SysDept parentDept = this.getById(dept.getParentId());
            if (parentDept != null) {
                String ancestors = parentDept.getAncestors() + "," + parentDept.getId();
                dept.setAncestors(ancestors);
            } else {
                dept.setAncestors("0");
            }
        } else {
            dept.setAncestors("0");
        }

        return this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
        @CacheEvict(key = "'code:' + #dept.deptCode", condition = "#dept.deptCode != null"),
        @CacheEvict(key = "'name:' + #dept.deptName", condition = "#dept.deptName != null"),
        @CacheEvict(key = "'children:' + #dept.parentId", condition = "#dept.parentId != null"),
        @CacheEvict(key = "#dept.id", condition = "#dept.id != null"),
        @CacheEvict(key = "'tree'"),
        @CacheEvict(key = "'list'"),
        @CacheEvict(key = "'normal'")
    })
    public boolean updateDept(SysDept dept) {
        // 获取原部门信息
        SysDept oldDept = this.getById(dept.getId());
        if (oldDept == null) {
            return false;
        }

        // 如果父部门发生变化，需要更新祖级列表
        if (!oldDept.getParentId().equals(dept.getParentId())) {
            // 设置新的祖级列表
            if (dept.getParentId() != null && dept.getParentId() != 0) {
                SysDept parentDept = this.getById(dept.getParentId());
                if (parentDept != null) {
                    String ancestors = parentDept.getAncestors() + "," + parentDept.getId();
                    dept.setAncestors(ancestors);
                } else {
                    dept.setAncestors("0");
                }
            } else {
                dept.setAncestors("0");
            }

            // 更新所有子部门的祖级列表
            updateChildrenAncestors(dept);
        }

        return this.updateById(dept);
    }

    /**
     * 更新子部门的祖级列表
     *
     * @param dept 父部门
     */
    private void updateChildrenAncestors(SysDept dept) {
        String oldAncestors = dept.getAncestors();
        String newAncestors = dept.getAncestors() + "," + dept.getId();

        // 查询所有子部门
        List<SysDept> children = selectChildrenByParentId(dept.getId());
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
            this.updateById(child);
            // 递归更新
            updateChildrenAncestors(child);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deleteDeptById(Long deptId) {
        // 检查是否有子部门
        if (hasChildDept(deptId)) {
            return false;
        }

        // 检查是否有关联用户
        if (hasAssociatedUser(deptId)) {
            return false;
        }

        // 删除用户-部门关联关系
        QueryWrapper queryUserDept = QueryWrapper.create()
                .where(SYS_USER_DEPT.DEPT_ID.eq(deptId));
        sysUserDeptMapper.deleteByQuery(queryUserDept);

        // 删除角色-部门关联关系
        QueryWrapper queryRoleDept = QueryWrapper.create()
                .where(SYS_ROLE_DEPT.DEPT_ID.eq(deptId));
        sysRoleDeptMapper.deleteByQuery(queryRoleDept);

        // 删除部门
        return this.removeById(deptId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deleteDepts(Long[] deptIds) {
        if (deptIds == null || deptIds.length == 0) {
            return false;
        }

        // 依次删除每个部门及其关联数据
        for (Long deptId : deptIds) {
            if (!deleteDeptById(deptId)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean checkDeptCodeUnique(String deptCode, Long deptId) {
        QueryWrapper query = QueryWrapper.create().where(SYS_DEPT.DEPT_CODE.eq(deptCode)).and(SYS_DEPT.IS_DELETED.eq(0));

        if (deptId != null) {
            query.and(SYS_DEPT.ID.ne(deptId));
        }

        return this.count(query) == 0;
    }

    @Override
    public boolean checkDeptNameUnique(String deptName, Long deptId) {
        QueryWrapper query = QueryWrapper.create().where(SYS_DEPT.DEPT_NAME.eq(deptName)).and(SYS_DEPT.IS_DELETED.eq(0));

        if (deptId != null) {
            query.and(SYS_DEPT.ID.ne(deptId));
        }

        return this.count(query) == 0;
    }

    @Override
    public boolean hasChildDept(Long deptId) {
        QueryWrapper query = QueryWrapper.create().where(SYS_DEPT.PARENT_ID.eq(deptId)).and(SYS_DEPT.IS_DELETED.eq(0));

        return this.count(query) > 0;
    }

    @Override
    public boolean hasAssociatedUser(Long deptId) {
        QueryWrapper wrapper = QueryWrapper.create().where(SYS_USER_DEPT.DEPT_ID.eq(deptId));
        return sysUserDeptMapper.selectCountByQuery(wrapper) > 0;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void refreshCache() {
        log.info("清空系统部门缓存");
        // @CacheEvict注解会自动清除sysDept缓存空间的所有数据
    }
}
