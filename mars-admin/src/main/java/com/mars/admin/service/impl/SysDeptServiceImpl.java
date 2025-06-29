package com.mars.admin.service.impl;

import com.mars.admin.entity.SysDept;
import com.mars.admin.mapper.SysDeptMapper;
import com.mars.admin.mapper.SysUserDeptMapper;
import com.mars.admin.service.ISysDeptService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mars.admin.entity.table.SysDeptTableDef.SYS_DEPT;
import static com.mars.admin.entity.table.SysUserDeptTableDef.SYS_USER_DEPT;

/**
 * 系统部门Service实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars
 */
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<SysDept> implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserDeptMapper sysUserDeptMapper;


    @Override
    public SysDept selectByDeptCode(String deptCode) {
        return sysDeptMapper.selectByDeptCode(deptCode);
    }

    @Override
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
    public List<SysDept> selectNormalDepts() {
        return sysDeptMapper.selectNormalDepts();
    }

    @Override
    public List<SysDept> selectChildrenByParentId(Long parentId) {
        return sysDeptMapper.selectChildrenByParentId(parentId);
    }

    @Override
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
    public boolean deleteDepts(Long[] deptIds) {
        return this.removeByIds(Arrays.asList(deptIds));
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
}
