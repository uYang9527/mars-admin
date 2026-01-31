package com.mars.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mars.common.exception.BusinessException;
import com.mars.system.entity.SysDept;
import com.mars.system.mapper.SysDeptMapper;
import com.mars.system.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门服务实现
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public List<SysDept> tree(String deptName, Integer status) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(deptName), SysDept::getDeptName, deptName)
                .eq(status != null, SysDept::getStatus, status)
                .orderByAsc(SysDept::getSort);
        List<SysDept> depts = this.list(wrapper);
        return buildTree(depts);
    }

    @Override
    public List<SysDept> listAll() {
        return this.list(new LambdaQueryWrapper<SysDept>().orderByAsc(SysDept::getSort));
    }

    @Override
    public void create(SysDept dept) {
        if (dept.getParentId() == null) {
            dept.setParentId(0L);
            dept.setAncestors("0");
        } else if (dept.getParentId() > 0) {
            SysDept parent = this.getById(dept.getParentId());
            if (parent == null) {
                throw new BusinessException("父部门不存在");
            }
            dept.setAncestors(parent.getAncestors() + "," + dept.getParentId());
        }
        this.save(dept);
    }

    @Override
    public void update(SysDept dept) {
        SysDept existDept = this.getById(dept.getId());
        if (existDept == null) {
            throw new BusinessException("部门不存在");
        }
        if (dept.getParentId() != null && dept.getParentId().equals(dept.getId())) {
            throw new BusinessException("上级部门不能选择自己");
        }
        // 更新祖级列表
        if (dept.getParentId() != null && !dept.getParentId().equals(existDept.getParentId())) {
            if (dept.getParentId() == 0) {
                dept.setAncestors("0");
            } else {
                SysDept parent = this.getById(dept.getParentId());
                if (parent == null) {
                    throw new BusinessException("父部门不存在");
                }
                dept.setAncestors(parent.getAncestors() + "," + dept.getParentId());
            }
        }
        this.updateById(dept);
    }

    @Override
    public void delete(Long id) {
        // 检查是否有子部门
        long count = this.count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id));
        if (count > 0) {
            throw new BusinessException("存在子部门，无法删除");
        }
        this.removeById(id);
    }

    /**
     * 构建部门树
     */
    private List<SysDept> buildTree(List<SysDept> depts) {
        List<SysDept> tree = new ArrayList<>();
        for (SysDept dept : depts) {
            if (dept.getParentId() == null || dept.getParentId() == 0) {
                dept.setChildren(getChildren(dept.getId(), depts));
                tree.add(dept);
            }
        }
        return tree;
    }

    /**
     * 递归获取子部门
     */
    private List<SysDept> getChildren(Long parentId, List<SysDept> depts) {
        List<SysDept> children = new ArrayList<>();
        for (SysDept dept : depts) {
            if (parentId.equals(dept.getParentId())) {
                dept.setChildren(getChildren(dept.getId(), depts));
                children.add(dept);
            }
        }
        return children.isEmpty() ? null : children;
    }
}
