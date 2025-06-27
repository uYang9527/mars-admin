package com.mars.admin.service.impl;

import com.mars.admin.entity.SysDept;
import com.mars.admin.mapper.SysDeptMapper;
import com.mars.admin.service.ISysDeptService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.mars.admin.entity.table.SysDeptTableDef.SYS_DEPT;

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
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_DEPT)
                .where(SYS_DEPT.IS_DELETED.eq(0));

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
    public List<SysDept> selectDeptTree() {
        return sysDeptMapper.selectDeptTree();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDept(SysDept dept) {
        return this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(SysDept dept) {
        return this.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDepts(Long[] deptIds) {
        return this.removeByIds(Arrays.asList(deptIds));
    }

    @Override
    public boolean checkDeptCodeUnique(String deptCode, Long deptId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_DEPT.DEPT_CODE.eq(deptCode))
                .and(SYS_DEPT.IS_DELETED.eq(0));

        if (deptId != null) {
            query.and(SYS_DEPT.ID.ne(deptId));
        }

        return this.count(query) == 0;
    }

    @Override
    public boolean checkDeptNameUnique(String deptName, Long deptId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_DEPT.DEPT_NAME.eq(deptName))
                .and(SYS_DEPT.IS_DELETED.eq(0));

        if (deptId != null) {
            query.and(SYS_DEPT.ID.ne(deptId));
        }

        return this.count(query) == 0;
    }
} 