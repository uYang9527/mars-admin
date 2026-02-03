package com.mars.system.service.mall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.mall.MallBanner;

import java.util.List;

/**
 * 轮播图 Service
 *
 * @author Mars
 * @date 2026-02-03
 */
public interface MallBannerService {

    /**
     * 分页查询
     */
    Page<MallBanner> page(Integer page, Integer pageSize, String position);

    /**
     * 根据位置查询有效轮播图
     */
    List<MallBanner> listByPosition(String position);

    /**
     * 根据ID查询
     */
    MallBanner getById(Long id);

    /**
     * 新增
     */
    void create(MallBanner banner);

    /**
     * 修改
     */
    void update(MallBanner banner);

    /**
     * 删除
     */
    void delete(Long[] ids);

    /**
     * 更新状态
     */
    void updateStatus(Long id, Integer status);
}
