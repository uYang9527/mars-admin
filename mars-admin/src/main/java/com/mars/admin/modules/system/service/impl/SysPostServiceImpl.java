package com.mars.admin.modules.system.service.impl;

import com.mars.admin.modules.system.entity.SysPost;
import com.mars.admin.modules.system.mapper.SysPostMapper;
import com.mars.admin.modules.system.mapper.SysUserPostMapper;
import com.mars.admin.modules.system.service.ISysPostService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.CacheableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.mars.admin.modules.system.entity.table.SysPostTableDef.SYS_POST;
import static com.mars.admin.modules.system.entity.table.SysUserPostTableDef.SYS_USER_POST;


/**
 * 系统岗位Service实现类
 * 继承 CacheableServiceImpl 获得更多便捷方法和缓存功能
 *
 * @author Mars
 */
@Service
@CacheConfig(cacheNames = "post")
public class SysPostServiceImpl extends CacheableServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    @Autowired
    private SysPostMapper sysPostMapper;

    @Autowired
    private SysUserPostMapper sysUserPostMapper;

    @Override
    @Cacheable(key = "'code:' + #postCode")
    public SysPost selectByPostCode(String postCode) {
        return sysPostMapper.selectByPostCode(postCode);
    }

    @Override
    @Cacheable(key = "'name:' + #postName")
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
    @Cacheable(key = "'user:' + #userId")
    public List<SysPost> selectPostsByUserId(Long userId) {
        return sysPostMapper.selectPostsByUserId(userId);
    }

    @Override
    @Cacheable(key = "'normal'")
    public List<SysPost> selectNormalPosts() {
        return sysPostMapper.selectNormalPosts();
    }

    @Override
    @Cacheable(key = "#id")
    public SysPost getPostById(Long id) {
        return super.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean insertPost(SysPost post) {
        return this.save(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
        @CacheEvict(key = "'code:' + #post.postCode", condition = "#post.postCode != null"),
        @CacheEvict(key = "'name:' + #post.postName", condition = "#post.postName != null"),
        @CacheEvict(key = "#post.id", condition = "#post.id != null")
    })
    public boolean updatePost(SysPost post) {
        return this.updateById(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deletePostById(Long postId) {
        // 先删除岗位与用户的关联关系
        QueryWrapper queryUserPost = QueryWrapper.create()
                .where(SYS_USER_POST.POST_ID.eq(postId));
        sysUserPostMapper.deleteByQuery(queryUserPost);

        // 再删除岗位
        return this.removeById(postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public boolean deletePosts(Long[] postIds) {
        if (postIds == null || postIds.length == 0) {
            return false;
        }

        // 依次删除每个岗位及其关联数据
        for (Long postId : postIds) {
            deletePostById(postId);
        }

        return true;
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
