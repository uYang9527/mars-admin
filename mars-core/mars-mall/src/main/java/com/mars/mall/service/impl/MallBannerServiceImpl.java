package com.mars.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.mall.entity.MallBanner;
import com.mars.mall.mapper.MallBannerMapper;
import com.mars.mall.service.MallBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 轮播图 Service 实现
 *
 * @author Mars
 * @date 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class MallBannerServiceImpl implements MallBannerService {

    private final MallBannerMapper bannerMapper;

    @Override
    public Page<MallBanner> page(Integer page, Integer pageSize, String position) {
        Page<MallBanner> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<MallBanner> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(position)) {
            wrapper.eq(MallBanner::getPosition, position);
        }
        wrapper.orderByAsc(MallBanner::getSort).orderByDesc(MallBanner::getId);
        
        return bannerMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public List<MallBanner> listByPosition(String position) {
        LocalDateTime now = LocalDateTime.now();
        
        return bannerMapper.selectList(
            new LambdaQueryWrapper<MallBanner>()
                .eq(MallBanner::getPosition, position)
                .eq(MallBanner::getStatus, 1)
                .and(w -> w
                    .isNull(MallBanner::getStartTime)
                    .or()
                    .le(MallBanner::getStartTime, now)
                )
                .and(w -> w
                    .isNull(MallBanner::getEndTime)
                    .or()
                    .ge(MallBanner::getEndTime, now)
                )
                .orderByAsc(MallBanner::getSort)
        );
    }

    @Override
    public MallBanner getById(Long id) {
        return bannerMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(MallBanner banner) {
        bannerMapper.insert(banner);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MallBanner banner) {
        bannerMapper.updateById(banner);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        bannerMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        MallBanner banner = new MallBanner();
        banner.setId(id);
        banner.setStatus(status);
        bannerMapper.updateById(banner);
    }
}
