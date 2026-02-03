package com.mars.system.service.mall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.mall.MallProduct;

import java.util.List;

/**
 * 商品 Service
 *
 * @author Mars
 * @date 2026-02-03
 */
public interface MallProductService {

    /**
     * 后台分页查询
     */
    Page<MallProduct> page(Integer page, Integer pageSize, String name, Long categoryId, Integer status);

    /**
     * 小程序分页查询
     */
    Page<MallProduct> pageForMini(Integer page, Integer pageSize, String keyword, Long categoryId, 
                                   Integer isHot, Integer isNew, Integer isRecommend, String orderBy);

    /**
     * 根据ID查询(包含SKU)
     */
    MallProduct getById(Long id);

    /**
     * 小程序查询商品详情(包含SKU、是否收藏)
     */
    MallProduct getDetailForMini(Long id, Long memberId);

    /**
     * 查询推荐商品
     */
    List<MallProduct> listRecommend(Integer limit);

    /**
     * 查询热门商品
     */
    List<MallProduct> listHot(Integer limit);

    /**
     * 查询新品
     */
    List<MallProduct> listNew(Integer limit);

    /**
     * 新增
     */
    void create(MallProduct product);

    /**
     * 修改
     */
    void update(MallProduct product);

    /**
     * 删除
     */
    void delete(Long[] ids);

    /**
     * 上下架
     */
    void updateStatus(Long id, Integer status);

    /**
     * 扣减库存
     */
    void deductStock(Long productId, Long skuId, Integer quantity);

    /**
     * 恢复库存
     */
    void restoreStock(Long productId, Long skuId, Integer quantity);

    /**
     * 增加销量
     */
    void addSales(Long productId, Long skuId, Integer quantity);
}
