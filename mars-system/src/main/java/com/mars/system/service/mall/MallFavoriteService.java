package com.mars.system.service.mall;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.mall.MallFavorite;

/**
 * 商品收藏 Service
 *
 * @author Mars
 * @date 2026-02-03
 */
public interface MallFavoriteService {

    /**
     * 分页查询收藏列表
     */
    Page<MallFavorite> pageByMemberId(Integer page, Integer pageSize, Long memberId);

    /**
     * 添加收藏
     */
    void add(Long memberId, Long productId);

    /**
     * 取消收藏
     */
    void remove(Long memberId, Long productId);

    /**
     * 切换收藏状态
     */
    boolean toggle(Long memberId, Long productId);

    /**
     * 检查是否已收藏
     */
    boolean isFavorite(Long memberId, Long productId);

    /**
     * 获取收藏数量
     */
    Integer countByMemberId(Long memberId);
}
