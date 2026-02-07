package com.mars.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.mall.entity.MallFavorite;
import com.mars.mall.entity.MallProduct;
import com.mars.mall.mapper.MallFavoriteMapper;
import com.mars.mall.mapper.MallProductMapper;
import com.mars.mall.service.MallFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品收藏 Service 实现
 *
 * @author Mars
 * @date 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class MallFavoriteServiceImpl implements MallFavoriteService {

    private final MallFavoriteMapper favoriteMapper;
    private final MallProductMapper productMapper;

    @Override
    public Page<MallFavorite> pageByMemberId(Integer page, Integer pageSize, Long memberId) {
        Page<MallFavorite> pageParam = new Page<>(page, pageSize);
        
        Page<MallFavorite> result = favoriteMapper.selectPage(pageParam,
            new LambdaQueryWrapper<MallFavorite>()
                .eq(MallFavorite::getMemberId, memberId)
                .orderByDesc(MallFavorite::getCreateTime)
        );
        
        // 填充商品信息
        for (MallFavorite favorite : result.getRecords()) {
            MallProduct product = productMapper.selectById(favorite.getProductId());
            favorite.setProduct(product);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long memberId, Long productId) {
        // 检查商品是否存在
        MallProduct product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        // 检查是否已收藏
        if (isFavorite(memberId, productId)) {
            return;
        }
        
        MallFavorite favorite = new MallFavorite();
        favorite.setMemberId(memberId);
        favorite.setProductId(productId);
        favoriteMapper.insert(favorite);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long memberId, Long productId) {
        favoriteMapper.delete(
            new LambdaQueryWrapper<MallFavorite>()
                .eq(MallFavorite::getMemberId, memberId)
                .eq(MallFavorite::getProductId, productId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggle(Long memberId, Long productId) {
        if (isFavorite(memberId, productId)) {
            remove(memberId, productId);
            return false;
        } else {
            add(memberId, productId);
            return true;
        }
    }

    @Override
    public boolean isFavorite(Long memberId, Long productId) {
        if (memberId == null || productId == null) {
            return false;
        }
        return favoriteMapper.selectCount(
            new LambdaQueryWrapper<MallFavorite>()
                .eq(MallFavorite::getMemberId, memberId)
                .eq(MallFavorite::getProductId, productId)
        ) > 0;
    }

    @Override
    public Integer countByMemberId(Long memberId) {
        return Math.toIntExact(favoriteMapper.selectCount(
            new LambdaQueryWrapper<MallFavorite>().eq(MallFavorite::getMemberId, memberId)
        ));
    }
}
