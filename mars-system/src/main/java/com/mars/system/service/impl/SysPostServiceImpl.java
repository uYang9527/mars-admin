package com.mars.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mars.common.exception.BusinessException;
import com.mars.common.result.PageResult;
import com.mars.system.entity.SysPost;
import com.mars.system.mapper.SysPostMapper;
import com.mars.system.service.SysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 岗位服务实现
 */
@Service
@RequiredArgsConstructor
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    @Override
    public PageResult<SysPost> page(Integer page, Integer pageSize, String postCode, String postName, Integer status) {
        Page<SysPost> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(postCode), SysPost::getPostCode, postCode)
                .like(StringUtils.hasText(postName), SysPost::getPostName, postName)
                .eq(status != null, SysPost::getStatus, status)
                .orderByAsc(SysPost::getSort);
        return PageResult.of(this.page(pageParam, wrapper));
    }

    @Override
    public List<SysPost> listAll() {
        return this.list(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getStatus, 1)
                .orderByAsc(SysPost::getSort));
    }

    @Override
    public void create(SysPost post) {
        // 检查岗位编码是否存在
        if (this.getOne(new LambdaQueryWrapper<SysPost>().eq(SysPost::getPostCode, post.getPostCode())) != null) {
            throw new BusinessException("岗位编码已存在");
        }
        this.save(post);
    }

    @Override
    public void update(SysPost post) {
        SysPost existPost = this.getById(post.getId());
        if (existPost == null) {
            throw new BusinessException("岗位不存在");
        }
        // 检查岗位编码是否存在
        SysPost byCode = this.getOne(new LambdaQueryWrapper<SysPost>().eq(SysPost::getPostCode, post.getPostCode()));
        if (byCode != null && !byCode.getId().equals(post.getId())) {
            throw new BusinessException("岗位编码已存在");
        }
        this.updateById(post);
    }

    @Override
    public void delete(Long id) {
        this.removeById(id);
    }
}
