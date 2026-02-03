package com.mars.system.service.mall.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.mall.MallCategory;
import com.mars.system.entity.mall.MallProduct;
import com.mars.system.entity.mall.MallProductSku;
import com.mars.system.mapper.mall.MallCategoryMapper;
import com.mars.system.mapper.mall.MallFavoriteMapper;
import com.mars.system.mapper.mall.MallProductMapper;
import com.mars.system.mapper.mall.MallProductSkuMapper;
import com.mars.system.service.mall.MallFavoriteService;
import com.mars.system.service.mall.MallProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 商品 Service 实现
 *
 * @author Mars
 * @date 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class MallProductServiceImpl implements MallProductService {

    private final MallProductMapper productMapper;
    private final MallProductSkuMapper skuMapper;
    private final MallCategoryMapper categoryMapper;
    private final MallFavoriteService favoriteService;

    @Override
    public Page<MallProduct> page(Integer page, Integer pageSize, String name, Long categoryId, Integer status) {
        Page<MallProduct> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<MallProduct> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(MallProduct::getName, name);
        }
        if (categoryId != null) {
            wrapper.eq(MallProduct::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(MallProduct::getStatus, status);
        }
        wrapper.orderByDesc(MallProduct::getId);
        
        Page<MallProduct> result = productMapper.selectPage(pageParam, wrapper);
        // 填充分类名称
        for (MallProduct product : result.getRecords()) {
            MallCategory category = categoryMapper.selectById(product.getCategoryId());
            if (category != null) {
                product.setCategoryName(category.getName());
            }
        }
        return result;
    }

    @Override
    public Page<MallProduct> pageForMini(Integer page, Integer pageSize, String keyword, Long categoryId,
                                          Integer isHot, Integer isNew, Integer isRecommend, String orderBy) {
        Page<MallProduct> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<MallProduct> wrapper = new LambdaQueryWrapper<>();
        
        // 只查询上架商品
        wrapper.eq(MallProduct::getStatus, 1);
        
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(MallProduct::getName, keyword)
                            .or().like(MallProduct::getSubtitle, keyword));
        }
        if (categoryId != null) {
            wrapper.eq(MallProduct::getCategoryId, categoryId);
        }
        if (isHot != null && isHot == 1) {
            wrapper.eq(MallProduct::getIsHot, 1);
        }
        if (isNew != null && isNew == 1) {
            wrapper.eq(MallProduct::getIsNew, 1);
        }
        if (isRecommend != null && isRecommend == 1) {
            wrapper.eq(MallProduct::getIsRecommend, 1);
        }
        
        // 排序
        if ("sales".equals(orderBy)) {
            wrapper.orderByDesc(MallProduct::getSales);
        } else if ("price_asc".equals(orderBy)) {
            wrapper.orderByAsc(MallProduct::getPrice);
        } else if ("price_desc".equals(orderBy)) {
            wrapper.orderByDesc(MallProduct::getPrice);
        } else if ("new".equals(orderBy)) {
            wrapper.orderByDesc(MallProduct::getCreateTime);
        } else {
            wrapper.orderByAsc(MallProduct::getSort).orderByDesc(MallProduct::getId);
        }
        
        return productMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public MallProduct getById(Long id) {
        MallProduct product = productMapper.selectById(id);
        if (product != null) {
            // 查询SKU列表
            List<MallProductSku> skuList = skuMapper.selectList(
                new LambdaQueryWrapper<MallProductSku>()
                    .eq(MallProductSku::getProductId, id)
                    .eq(MallProductSku::getStatus, 1)
            );
            product.setSkuList(skuList);
            
            // 查询分类名称
            MallCategory category = categoryMapper.selectById(product.getCategoryId());
            if (category != null) {
                product.setCategoryName(category.getName());
            }
        }
        return product;
    }

    @Override
    public MallProduct getDetailForMini(Long id, Long memberId) {
        MallProduct product = getById(id);
        if (product != null && memberId != null) {
            // 查询是否收藏
            product.setIsFavorite(favoriteService.isFavorite(memberId, id));
        }
        return product;
    }

    @Override
    public List<MallProduct> listRecommend(Integer limit) {
        return productMapper.selectList(
            new LambdaQueryWrapper<MallProduct>()
                .eq(MallProduct::getStatus, 1)
                .eq(MallProduct::getIsRecommend, 1)
                .orderByAsc(MallProduct::getSort)
                .last("LIMIT " + limit)
        );
    }

    @Override
    public List<MallProduct> listHot(Integer limit) {
        return productMapper.selectList(
            new LambdaQueryWrapper<MallProduct>()
                .eq(MallProduct::getStatus, 1)
                .eq(MallProduct::getIsHot, 1)
                .orderByDesc(MallProduct::getSales)
                .last("LIMIT " + limit)
        );
    }

    @Override
    public List<MallProduct> listNew(Integer limit) {
        return productMapper.selectList(
            new LambdaQueryWrapper<MallProduct>()
                .eq(MallProduct::getStatus, 1)
                .eq(MallProduct::getIsNew, 1)
                .orderByDesc(MallProduct::getCreateTime)
                .last("LIMIT " + limit)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(MallProduct product) {
        productMapper.insert(product);
        
        // 保存SKU
        if (product.getSkuList() != null && !product.getSkuList().isEmpty()) {
            for (MallProductSku sku : product.getSkuList()) {
                sku.setProductId(product.getId());
                skuMapper.insert(sku);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MallProduct product) {
        productMapper.updateById(product);
        
        // 更新SKU(先删后加)
        if (product.getSkuList() != null) {
            skuMapper.delete(
                new LambdaQueryWrapper<MallProductSku>()
                    .eq(MallProductSku::getProductId, product.getId())
            );
            for (MallProductSku sku : product.getSkuList()) {
                sku.setId(null);
                sku.setProductId(product.getId());
                skuMapper.insert(sku);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        productMapper.deleteBatchIds(Arrays.asList(ids));
        // 删除SKU
        skuMapper.delete(
            new LambdaQueryWrapper<MallProductSku>().in(MallProductSku::getProductId, Arrays.asList(ids))
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        MallProduct product = new MallProduct();
        product.setId(id);
        product.setStatus(status);
        productMapper.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductStock(Long productId, Long skuId, Integer quantity) {
        // 扣减商品库存
        productMapper.update(null, new LambdaUpdateWrapper<MallProduct>()
            .eq(MallProduct::getId, productId)
            .ge(MallProduct::getStock, quantity)
            .setSql("stock = stock - " + quantity)
        );
        
        // 扣减SKU库存
        if (skuId != null) {
            skuMapper.update(null, new LambdaUpdateWrapper<MallProductSku>()
                .eq(MallProductSku::getId, skuId)
                .ge(MallProductSku::getStock, quantity)
                .setSql("stock = stock - " + quantity)
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreStock(Long productId, Long skuId, Integer quantity) {
        // 恢复商品库存
        productMapper.update(null, new LambdaUpdateWrapper<MallProduct>()
            .eq(MallProduct::getId, productId)
            .setSql("stock = stock + " + quantity)
        );
        
        // 恢复SKU库存
        if (skuId != null) {
            skuMapper.update(null, new LambdaUpdateWrapper<MallProductSku>()
                .eq(MallProductSku::getId, skuId)
                .setSql("stock = stock + " + quantity)
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSales(Long productId, Long skuId, Integer quantity) {
        // 增加商品销量
        productMapper.update(null, new LambdaUpdateWrapper<MallProduct>()
            .eq(MallProduct::getId, productId)
            .setSql("sales = sales + " + quantity)
        );
        
        // 增加SKU销量
        if (skuId != null) {
            skuMapper.update(null, new LambdaUpdateWrapper<MallProductSku>()
                .eq(MallProductSku::getId, skuId)
                .setSql("sales = sales + " + quantity)
            );
        }
    }
}
