package com.mars.admin.service.impl;

import com.mars.admin.entity.SysPost;
import com.mars.admin.mapper.SysPostMapper;
import com.mars.admin.service.ISysPostService;
import com.mars.admin.service.impl.BaseServiceImpl;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static com.mars.admin.entity.table.SysPostTableDef.SYS_POST;

/**
 * 系统岗位Service实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars
 */
@Service
public class SysPostServiceImpl extends BaseServiceImpl<SysPost> implements ISysPostService {

    @Autowired
    private SysPostMapper sysPostMapper;

    @Override
    public SysPost selectByPostCode(String postCode) {
        return sysPostMapper.selectByPostCode(postCode);
    }

    @Override
    public SysPost selectByPostName(String postName) {
        return sysPostMapper.selectByPostName(postName);
    }

    @Override
    public Page<SysPost> selectPostPage(Page<SysPost> page, SysPost post) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_POST)
                .where(SYS_POST.IS_DELETED.eq(0));

        if (StringUtils.hasText(post.getPostName())) {
            query.and(SYS_POST.POST_NAME.like(post.getPostName()));
        }
        if (StringUtils.hasText(post.getPostCode())) {
            query.and(SYS_POST.POST_CODE.like(post.getPostCode()));
        }
        if (post.getStatus() != null) {
            query.and(SYS_POST.STATUS.eq(post.getStatus()));
        }

        query.orderBy(SYS_POST.POST_SORT.asc(), SYS_POST.CREATE_TIME.desc());

        return this.page(page, query);
    }

    @Override
    public List<SysPost> selectPostsByUserId(Long userId) {
        return sysPostMapper.selectPostsByUserId(userId);
    }

    @Override
    public List<SysPost> selectNormalPosts() {
        return sysPostMapper.selectNormalPosts();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertPost(SysPost post) {
        return this.save(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePost(SysPost post) {
        return this.updateById(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePosts(Long[] postIds) {
        return this.removeByIds(Arrays.asList(postIds));
    }

    @Override
    public boolean checkPostCodeUnique(String postCode, Long postId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_POST.POST_CODE.eq(postCode))
                .and(SYS_POST.IS_DELETED.eq(0));

        if (postId != null) {
            query.and(SYS_POST.ID.ne(postId));
        }

        return this.count(query) == 0;
    }

    @Override
    public boolean checkPostNameUnique(String postName, Long postId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_POST.POST_NAME.eq(postName))
                .and(SYS_POST.IS_DELETED.eq(0));

        if (postId != null) {
            query.and(SYS_POST.ID.ne(postId));
        }

        return this.count(query) == 0;
    }

    @Override
    public List<SysPost> exportPost(SysPost post) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_POST)
                .where(SYS_POST.IS_DELETED.eq(0));

        if (StringUtils.hasText(post.getPostName())) {
            query.and(SYS_POST.POST_NAME.like(post.getPostName()));
        }
        if (post.getStatus() != null) {
            query.and(SYS_POST.STATUS.eq(post.getStatus()));
        }

        return this.list(query);
    }
} 