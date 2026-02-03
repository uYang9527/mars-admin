package com.mars.system.service.mall.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.mall.MallCategory;
import com.mars.system.mapper.mall.MallCategoryMapper;
import com.mars.system.service.mall.MallCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品分类 Service 实现
 *
 * @author Mars
 * @date 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class MallCategoryServiceImpl implements MallCategoryService {

    private final MallCategoryMapper categoryMapper;

    @Override
    public Page<MallCategory> page(Integer page, Integer pageSize, String name) {
        Page<MallCategory> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<MallCategory> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(MallCategory::getName, name);
        }
        wrapper.orderByAsc(MallCategory::getSort);
        return categoryMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public List<MallCategory> listTree() {
        List<MallCategory> allCategories = categoryMapper.selectList(
            new LambdaQueryWrapper<MallCategory>().orderByAsc(MallCategory::getSort)
        );
        return buildTree(allCategories);
    }

    @Override
    public List<MallCategory> listTreeEnabled() {
        List<MallCategory> allCategories = categoryMapper.selectList(
            new LambdaQueryWrapper<MallCategory>()
                .eq(MallCategory::getStatus, 1)
                .orderByAsc(MallCategory::getSort)
        );
        return buildTree(allCategories);
    }

    @Override
    public List<MallCategory> listParentCategories() {
        return categoryMapper.selectList(
            new LambdaQueryWrapper<MallCategory>()
                .eq(MallCategory::getParentId, 0)
                .eq(MallCategory::getStatus, 1)
                .orderByAsc(MallCategory::getSort)
        );
    }

    @Override
    public MallCategory getById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(MallCategory category) {
        categoryMapper.insert(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MallCategory category) {
        categoryMapper.updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        // 检查是否有子分类
        Long count = categoryMapper.selectCount(
            new LambdaQueryWrapper<MallCategory>().in(MallCategory::getParentId, Arrays.asList(ids))
        );
        if (count > 0) {
            throw new RuntimeException("存在子分类，无法删除");
        }
        categoryMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 构建树形结构
     */
    private List<MallCategory> buildTree(List<MallCategory> categories) {
        // 获取所有根节点
        List<MallCategory> rootList = categories.stream()
            .filter(c -> c.getParentId() == 0)
            .collect(Collectors.toList());
        
        // 为每个根节点设置子节点
        for (MallCategory root : rootList) {
            root.setChildren(getChildren(root.getId(), categories));
        }
        
        return rootList;
    }

    /**
     * 递归获取子节点
     */
    private List<MallCategory> getChildren(Long parentId, List<MallCategory> allCategories) {
        List<MallCategory> children = allCategories.stream()
            .filter(c -> c.getParentId().equals(parentId))
            .collect(Collectors.toList());
        
        for (MallCategory child : children) {
            child.setChildren(getChildren(child.getId(), allCategories));
        }
        
        return children.isEmpty() ? null : children;
    }
}
