package com.mars.system.service.mall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.mall.MallCategory;

import java.util.List;

/**
 * 商品分类 Service
 *
 * @author Mars
 * @date 2026-02-03
 */
public interface MallCategoryService {

    /**
     * 分页查询
     */
    Page<MallCategory> page(Integer page, Integer pageSize, String name);

    /**
     * 查询所有分类(树形结构)
     */
    List<MallCategory> listTree();

    /**
     * 查询所有启用的分类(树形结构)
     */
    List<MallCategory> listTreeEnabled();

    /**
     * 查询所有一级分类
     */
    List<MallCategory> listParentCategories();

    /**
     * 根据ID查询
     */
    MallCategory getById(Long id);

    /**
     * 新增
     */
    void create(MallCategory category);

    /**
     * 修改
     */
    void update(MallCategory category);

    /**
     * 删除
     */
    void delete(Long[] ids);
}
