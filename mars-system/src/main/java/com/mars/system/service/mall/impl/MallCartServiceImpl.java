package com.mars.system.service.mall.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mars.system.entity.mall.MallCart;
import com.mars.system.entity.mall.MallProduct;
import com.mars.system.entity.mall.MallProductSku;
import com.mars.system.mapper.mall.MallCartMapper;
import com.mars.system.mapper.mall.MallProductMapper;
import com.mars.system.mapper.mall.MallProductSkuMapper;
import com.mars.system.service.mall.MallCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 购物车 Service 实现
 *
 * @author Mars
 * @date 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class MallCartServiceImpl implements MallCartService {

    private final MallCartMapper cartMapper;
    private final MallProductMapper productMapper;
    private final MallProductSkuMapper skuMapper;

    @Override
    public List<MallCart> listByMemberId(Long memberId) {
        List<MallCart> cartList = cartMapper.selectList(
            new LambdaQueryWrapper<MallCart>()
                .eq(MallCart::getMemberId, memberId)
                .orderByDesc(MallCart::getCreateTime)
        );
        
        // 填充商品信息
        for (MallCart cart : cartList) {
            fillProductInfo(cart);
        }
        
        return cartList;
    }

    @Override
    public Integer countByMemberId(Long memberId) {
        return Math.toIntExact(cartMapper.selectCount(
            new LambdaQueryWrapper<MallCart>().eq(MallCart::getMemberId, memberId)
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long memberId, Long productId, Long skuId, Integer quantity) {
        // 检查商品是否存在
        MallProduct product = productMapper.selectById(productId);
        if (product == null || product.getStatus() != 1) {
            throw new RuntimeException("商品不存在或已下架");
        }
        
        // 检查库存
        int stock = product.getStock();
        if (skuId != null) {
            MallProductSku sku = skuMapper.selectById(skuId);
            if (sku != null) {
                stock = sku.getStock();
            }
        }
        
        // 检查是否已在购物车
        MallCart existCart = cartMapper.selectOne(
            new LambdaQueryWrapper<MallCart>()
                .eq(MallCart::getMemberId, memberId)
                .eq(MallCart::getProductId, productId)
                .eq(skuId != null, MallCart::getSkuId, skuId)
        );
        
        if (existCart != null) {
            // 更新数量
            int newQuantity = existCart.getQuantity() + quantity;
            if (newQuantity > stock) {
                throw new RuntimeException("库存不足");
            }
            existCart.setQuantity(newQuantity);
            cartMapper.updateById(existCart);
        } else {
            // 新增
            if (quantity > stock) {
                throw new RuntimeException("库存不足");
            }
            MallCart cart = new MallCart();
            cart.setMemberId(memberId);
            cart.setProductId(productId);
            cart.setSkuId(skuId);
            cart.setQuantity(quantity);
            cart.setSelected(1);
            cartMapper.insert(cart);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQuantity(Long memberId, Long cartId, Integer quantity) {
        MallCart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getMemberId().equals(memberId)) {
            throw new RuntimeException("购物车商品不存在");
        }
        
        // 检查库存
        MallProduct product = productMapper.selectById(cart.getProductId());
        int stock = product.getStock();
        if (cart.getSkuId() != null) {
            MallProductSku sku = skuMapper.selectById(cart.getSkuId());
            if (sku != null) {
                stock = sku.getStock();
            }
        }
        
        if (quantity > stock) {
            throw new RuntimeException("库存不足");
        }
        
        cart.setQuantity(quantity);
        cartMapper.updateById(cart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSelected(Long memberId, Long cartId, Integer selected) {
        cartMapper.update(null, new LambdaUpdateWrapper<MallCart>()
            .eq(MallCart::getId, cartId)
            .eq(MallCart::getMemberId, memberId)
            .set(MallCart::getSelected, selected)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectAll(Long memberId, Integer selected) {
        cartMapper.update(null, new LambdaUpdateWrapper<MallCart>()
            .eq(MallCart::getMemberId, memberId)
            .set(MallCart::getSelected, selected)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long memberId, Long[] cartIds) {
        cartMapper.delete(new LambdaQueryWrapper<MallCart>()
            .eq(MallCart::getMemberId, memberId)
            .in(MallCart::getId, Arrays.asList(cartIds))
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clear(Long memberId) {
        cartMapper.delete(new LambdaQueryWrapper<MallCart>()
            .eq(MallCart::getMemberId, memberId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSelected(Long memberId) {
        cartMapper.delete(new LambdaQueryWrapper<MallCart>()
            .eq(MallCart::getMemberId, memberId)
            .eq(MallCart::getSelected, 1)
        );
    }

    @Override
    public List<MallCart> listSelected(Long memberId) {
        List<MallCart> cartList = cartMapper.selectList(
            new LambdaQueryWrapper<MallCart>()
                .eq(MallCart::getMemberId, memberId)
                .eq(MallCart::getSelected, 1)
        );
        
        for (MallCart cart : cartList) {
            fillProductInfo(cart);
        }
        
        return cartList;
    }

    @Override
    public BigDecimal calculateSelectedAmount(Long memberId) {
        List<MallCart> selectedList = listSelected(memberId);
        BigDecimal total = BigDecimal.ZERO;
        
        for (MallCart cart : selectedList) {
            BigDecimal price = cart.getSkuPrice() != null ? cart.getSkuPrice() : cart.getProductPrice();
            if (price != null) {
                total = total.add(price.multiply(new BigDecimal(cart.getQuantity())));
            }
        }
        
        return total;
    }

    /**
     * 填充商品信息
     */
    private void fillProductInfo(MallCart cart) {
        MallProduct product = productMapper.selectById(cart.getProductId());
        if (product != null) {
            cart.setProductName(product.getName());
            cart.setProductImage(product.getMainImage());
            cart.setProductPrice(product.getPrice());
            cart.setStock(product.getStock());
        }
        
        if (cart.getSkuId() != null) {
            MallProductSku sku = skuMapper.selectById(cart.getSkuId());
            if (sku != null) {
                cart.setSkuName(sku.getSkuName());
                cart.setSkuPrice(sku.getPrice());
                cart.setStock(sku.getStock());
                if (sku.getSkuImage() != null) {
                    cart.setProductImage(sku.getSkuImage());
                }
            }
        }
    }
}
