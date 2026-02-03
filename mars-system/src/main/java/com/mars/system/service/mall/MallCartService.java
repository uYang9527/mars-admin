package com.mars.system.service.mall;

import com.mars.system.entity.mall.MallCart;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车 Service
 *
 * @author Mars
 * @date 2026-02-03
 */
public interface MallCartService {

    /**
     * 查询会员购物车列表
     */
    List<MallCart> listByMemberId(Long memberId);

    /**
     * 查询购物车商品数量
     */
    Integer countByMemberId(Long memberId);

    /**
     * 添加商品到购物车
     */
    void add(Long memberId, Long productId, Long skuId, Integer quantity);

    /**
     * 更新购物车商品数量
     */
    void updateQuantity(Long memberId, Long cartId, Integer quantity);

    /**
     * 更新购物车商品选中状态
     */
    void updateSelected(Long memberId, Long cartId, Integer selected);

    /**
     * 全选/全不选
     */
    void selectAll(Long memberId, Integer selected);

    /**
     * 删除购物车商品
     */
    void delete(Long memberId, Long[] cartIds);

    /**
     * 清空购物车
     */
    void clear(Long memberId);

    /**
     * 删除已选中的购物车商品(下单后)
     */
    void deleteSelected(Long memberId);

    /**
     * 获取选中商品列表
     */
    List<MallCart> listSelected(Long memberId);

    /**
     * 计算选中商品总金额
     */
    BigDecimal calculateSelectedAmount(Long memberId);
}
