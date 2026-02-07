package com.mars.mall.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.mall.entity.*;
import com.mars.mall.mapper.*;
import com.mars.mall.service.MallCartService;
import com.mars.mall.service.MallOrderService;
import com.mars.mall.service.MallProductService;
import com.mars.pay.WechatPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 订单 Service 实现
 *
 * @author Mars
 * @date 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class MallOrderServiceImpl implements MallOrderService {

    private final MallOrderMapper orderMapper;
    private final MallOrderItemMapper orderItemMapper;
    private final MallAddressMapper addressMapper;
    private final MallCartMapper cartMapper;
    private final MallProductMapper productMapper;
    private final MallProductSkuMapper skuMapper;
    private final MallMemberMapper memberMapper;
    private final MallCartService cartService;
    private final MallProductService productService;
    private final WechatPayService wechatPayService;

    @Override
    public Page<MallOrder> page(Integer page, Integer pageSize, String orderNo, Integer status, Long memberId) {
        Page<MallOrder> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<MallOrder> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(orderNo)) {
            wrapper.like(MallOrder::getOrderNo, orderNo);
        }
        if (status != null) {
            wrapper.eq(MallOrder::getStatus, status);
        }
        if (memberId != null) {
            wrapper.eq(MallOrder::getMemberId, memberId);
        }
        wrapper.orderByDesc(MallOrder::getCreateTime);
        
        Page<MallOrder> result = orderMapper.selectPage(pageParam, wrapper);
        
        // 填充订单商品
        for (MallOrder order : result.getRecords()) {
            fillOrderItems(order);
            fillMemberInfo(order);
        }
        
        return result;
    }

    @Override
    public Page<MallOrder> pageForMini(Integer page, Integer pageSize, Long memberId, Integer status) {
        Page<MallOrder> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<MallOrder> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(MallOrder::getMemberId, memberId);
        if (status != null) {
            wrapper.eq(MallOrder::getStatus, status);
        }
        wrapper.orderByDesc(MallOrder::getCreateTime);
        
        Page<MallOrder> result = orderMapper.selectPage(pageParam, wrapper);
        
        // 填充订单商品
        for (MallOrder order : result.getRecords()) {
            fillOrderItems(order);
        }
        
        return result;
    }

    @Override
    public MallOrder getById(Long id) {
        MallOrder order = orderMapper.selectById(id);
        if (order != null) {
            fillOrderItems(order);
        }
        return order;
    }

    @Override
    public MallOrder getByOrderNo(String orderNo) {
        MallOrder order = orderMapper.selectOne(
            new LambdaQueryWrapper<MallOrder>().eq(MallOrder::getOrderNo, orderNo)
        );
        if (order != null) {
            fillOrderItems(order);
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MallOrder createFromCart(Long memberId, Long addressId, String remark, Long couponId) {
        // 获取选中的购物车商品
        List<MallCart> cartList = cartService.listSelected(memberId);
        if (cartList.isEmpty()) {
            throw new RuntimeException("请选择商品");
        }
        
        // 获取收货地址
        MallAddress address = addressMapper.selectById(addressId);
        if (address == null || !address.getMemberId().equals(memberId)) {
            throw new RuntimeException("收货地址不存在");
        }
        
        // 计算订单金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<MallOrderItem> orderItems = new ArrayList<>();
        
        for (MallCart cart : cartList) {
            BigDecimal price = cart.getSkuPrice() != null ? cart.getSkuPrice() : cart.getProductPrice();
            BigDecimal itemTotal = price.multiply(new BigDecimal(cart.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            
            // 创建订单商品
            MallOrderItem item = new MallOrderItem();
            item.setProductId(cart.getProductId());
            item.setSkuId(cart.getSkuId());
            item.setProductName(cart.getProductName());
            item.setProductImage(cart.getProductImage());
            item.setSkuName(cart.getSkuName());
            item.setPrice(price);
            item.setQuantity(cart.getQuantity());
            item.setTotalAmount(itemTotal);
            orderItems.add(item);
            
            // 扣减库存
            productService.deductStock(cart.getProductId(), cart.getSkuId(), cart.getQuantity());
        }
        
        // 创建订单
        MallOrder order = new MallOrder();
        order.setOrderNo(generateOrderNo());
        order.setMemberId(memberId);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount); // 暂不考虑优惠
        order.setFreightAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setStatus(MallOrder.STATUS_PENDING_PAY);
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhone());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverDistrict(address.getDistrict());
        order.setReceiverDetail(address.getDetail());
        order.setRemark(remark);
        
        orderMapper.insert(order);
        
        // 保存订单商品
        for (MallOrderItem item : orderItems) {
            item.setOrderId(order.getId());
            item.setOrderNo(order.getOrderNo());
            orderItemMapper.insert(item);
        }
        
        // 清空购物车
        cartService.deleteSelected(memberId);
        
        order.setOrderItems(orderItems);
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MallOrder createDirect(Long memberId, Long productId, Long skuId, Integer quantity,
                                   Long addressId, String remark, Long couponId) {
        // 检查商品
        MallProduct product = productMapper.selectById(productId);
        if (product == null || product.getStatus() != 1) {
            throw new RuntimeException("商品不存在或已下架");
        }
        
        // 获取价格和库存
        BigDecimal price = product.getPrice();
        int stock = product.getStock();
        String skuName = null;
        
        if (skuId != null) {
            MallProductSku sku = skuMapper.selectById(skuId);
            if (sku != null) {
                price = sku.getPrice();
                stock = sku.getStock();
                skuName = sku.getSkuName();
            }
        }
        
        if (quantity > stock) {
            throw new RuntimeException("库存不足");
        }
        
        // 获取收货地址
        MallAddress address = addressMapper.selectById(addressId);
        if (address == null || !address.getMemberId().equals(memberId)) {
            throw new RuntimeException("收货地址不存在");
        }
        
        // 计算金额
        BigDecimal totalAmount = price.multiply(new BigDecimal(quantity));
        
        // 创建订单
        MallOrder order = new MallOrder();
        order.setOrderNo(generateOrderNo());
        order.setMemberId(memberId);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setFreightAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setStatus(MallOrder.STATUS_PENDING_PAY);
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhone());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverDistrict(address.getDistrict());
        order.setReceiverDetail(address.getDetail());
        order.setRemark(remark);
        
        orderMapper.insert(order);
        
        // 创建订单商品
        MallOrderItem item = new MallOrderItem();
        item.setOrderId(order.getId());
        item.setOrderNo(order.getOrderNo());
        item.setProductId(productId);
        item.setSkuId(skuId);
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setSkuName(skuName);
        item.setPrice(price);
        item.setQuantity(quantity);
        item.setTotalAmount(totalAmount);
        orderItemMapper.insert(item);
        
        // 扣减库存
        productService.deductStock(productId, skuId, quantity);
        
        order.setOrderItems(Collections.singletonList(item));
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long memberId, Long orderId) {
        MallOrder order = orderMapper.selectById(orderId);
        if (order == null || !order.getMemberId().equals(memberId)) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != MallOrder.STATUS_PENDING_PAY) {
            throw new RuntimeException("订单状态不允许取消");
        }
        
        // 更新状态
        order.setStatus(MallOrder.STATUS_CANCELLED);
        orderMapper.updateById(order);
        
        // 恢复库存
        List<MallOrderItem> items = orderItemMapper.selectList(
            new LambdaQueryWrapper<MallOrderItem>().eq(MallOrderItem::getOrderId, orderId)
        );
        for (MallOrderItem item : items) {
            productService.restoreStock(item.getProductId(), item.getSkuId(), item.getQuantity());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> pay(Long memberId, Long orderId, Integer payType) {
        MallOrder order = orderMapper.selectById(orderId);
        if (order == null || !order.getMemberId().equals(memberId)) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != MallOrder.STATUS_PENDING_PAY) {
            throw new RuntimeException("订单状态不允许支付");
        }
        
        // 获取会员openId
        MallMember member = memberMapper.selectById(memberId);
        if (member == null) {
            throw new RuntimeException("会员不存在");
        }
        
        // 调用微信支付
        if (payType == MallOrder.PAY_TYPE_WECHAT) {
            return wechatPayService.createMiniProgramOrder(
                order.getOrderNo(),
                order.getPayAmount(),
                "商城订单支付",
                member.getOpenId()
            );
        }
        
        throw new RuntimeException("不支持的支付方式");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(String orderNo, String payNo, Integer payType) {
        MallOrder order = getByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != MallOrder.STATUS_PENDING_PAY) {
            return; // 已处理
        }
        
        // 更新订单状态
        order.setStatus(MallOrder.STATUS_PENDING_SHIP);
        order.setPayType(payType);
        order.setPayTime(LocalDateTime.now());
        order.setPayNo(payNo);
        orderMapper.updateById(order);
        
        // 增加销量
        for (MallOrderItem item : order.getOrderItems()) {
            productService.addSales(item.getProductId(), item.getSkuId(), item.getQuantity());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ship(Long orderId, String deliveryCompany, String deliveryNo) {
        MallOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != MallOrder.STATUS_PENDING_SHIP) {
            throw new RuntimeException("订单状态不允许发货");
        }
        
        order.setStatus(MallOrder.STATUS_PENDING_RECEIVE);
        order.setDeliveryCompany(deliveryCompany);
        order.setDeliveryNo(deliveryNo);
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receive(Long memberId, Long orderId) {
        MallOrder order = orderMapper.selectById(orderId);
        if (order == null || !order.getMemberId().equals(memberId)) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != MallOrder.STATUS_PENDING_RECEIVE) {
            throw new RuntimeException("订单状态不允许确认收货");
        }
        
        order.setStatus(MallOrder.STATUS_COMPLETED);
        order.setReceiveTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long memberId, Long orderId) {
        MallOrder order = orderMapper.selectById(orderId);
        if (order == null || !order.getMemberId().equals(memberId)) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getStatus() != MallOrder.STATUS_COMPLETED && 
            order.getStatus() != MallOrder.STATUS_CANCELLED) {
            throw new RuntimeException("订单状态不允许删除");
        }
        
        orderMapper.deleteById(orderId);
    }

    @Override
    public Map<String, Integer> countByStatus(Long memberId) {
        Map<String, Integer> result = new HashMap<>();
        
        LambdaQueryWrapper<MallOrder> baseWrapper = new LambdaQueryWrapper<MallOrder>()
            .eq(MallOrder::getMemberId, memberId);
        
        result.put("pendingPay", Math.toIntExact(orderMapper.selectCount(
            baseWrapper.clone().eq(MallOrder::getStatus, MallOrder.STATUS_PENDING_PAY))));
        result.put("pendingShip", Math.toIntExact(orderMapper.selectCount(
            baseWrapper.clone().eq(MallOrder::getStatus, MallOrder.STATUS_PENDING_SHIP))));
        result.put("pendingReceive", Math.toIntExact(orderMapper.selectCount(
            baseWrapper.clone().eq(MallOrder::getStatus, MallOrder.STATUS_PENDING_RECEIVE))));
        result.put("completed", Math.toIntExact(orderMapper.selectCount(
            baseWrapper.clone().eq(MallOrder::getStatus, MallOrder.STATUS_COMPLETED))));
        
        return result;
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "M" + IdUtil.getSnowflakeNextIdStr();
    }

    /**
     * 填充订单商品
     */
    private void fillOrderItems(MallOrder order) {
        List<MallOrderItem> items = orderItemMapper.selectList(
            new LambdaQueryWrapper<MallOrderItem>().eq(MallOrderItem::getOrderId, order.getId())
        );
        order.setOrderItems(items);
    }

    /**
     * 填充会员信息
     */
    private void fillMemberInfo(MallOrder order) {
        MallMember member = memberMapper.selectById(order.getMemberId());
        if (member != null) {
            order.setMemberNickname(member.getNickname());
            order.setMemberPhone(member.getPhone());
        }
    }
}
