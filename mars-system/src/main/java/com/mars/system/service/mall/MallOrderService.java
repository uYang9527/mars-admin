package com.mars.system.service.mall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.mall.MallOrder;

import java.util.Map;

/**
 * 订单 Service
 *
 * @author Mars
 * @date 2026-02-03
 */
public interface MallOrderService {

    /**
     * 后台分页查询
     */
    Page<MallOrder> page(Integer page, Integer pageSize, String orderNo, Integer status, Long memberId);

    /**
     * 小程序分页查询
     */
    Page<MallOrder> pageForMini(Integer page, Integer pageSize, Long memberId, Integer status);

    /**
     * 根据ID查询
     */
    MallOrder getById(Long id);

    /**
     * 根据订单号查询
     */
    MallOrder getByOrderNo(String orderNo);

    /**
     * 创建订单(从购物车)
     */
    MallOrder createFromCart(Long memberId, Long addressId, String remark, Long couponId);

    /**
     * 创建订单(直接购买)
     */
    MallOrder createDirect(Long memberId, Long productId, Long skuId, Integer quantity, 
                            Long addressId, String remark, Long couponId);

    /**
     * 取消订单
     */
    void cancel(Long memberId, Long orderId);

    /**
     * 支付订单
     */
    Map<String, String> pay(Long memberId, Long orderId, Integer payType);

    /**
     * 支付成功回调
     */
    void paySuccess(String orderNo, String payNo, Integer payType);

    /**
     * 发货
     */
    void ship(Long orderId, String deliveryCompany, String deliveryNo);

    /**
     * 确认收货
     */
    void receive(Long memberId, Long orderId);

    /**
     * 删除订单
     */
    void delete(Long memberId, Long orderId);

    /**
     * 获取各状态订单数量
     */
    Map<String, Integer> countByStatus(Long memberId);
}
