package com.mars.admin.service;

import com.mars.admin.entity.SysPost;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * 系统岗位Service接口
 * 继承 BaseService 获得更多便捷方法
 *
 * @author Mars
 */
public interface ISysPostService extends BaseService<SysPost> {

    /**
     * 根据岗位编码查询岗位
     *
     * @param postCode 岗位编码
     * @return 岗位信息
     */
    SysPost selectByPostCode(String postCode);

    /**
     * 根据岗位名称查询岗位
     *
     * @param postName 岗位名称
     * @return 岗位信息
     */
    SysPost selectByPostName(String postName);

    /**
     * 分页查询岗位列表
     *
     * @param page 分页参数
     * @param post 查询条件
     * @return 岗位分页列表
     */
    Page<SysPost> selectPostPage(Page<SysPost> page, SysPost post);

    /**
     * 根据用户ID查询岗位列表
     *
     * @param userId 用户ID
     * @return 岗位列表
     */
    List<SysPost> selectPostsByUserId(Long userId);

    /**
     * 查询所有正常状态的岗位
     *
     * @return 岗位列表
     */
    List<SysPost> selectNormalPosts();

    /**
     * 根据ID查询岗位（使用缓存）
     *
     * @param id 岗位ID
     * @return 岗位信息
     */
    SysPost getPostById(Long id);

    /**
     * 新增岗位
     *
     * @param post 岗位信息
     * @return 新增结果
     */
    boolean insertPost(SysPost post);

    /**
     * 修改岗位
     *
     * @param post 岗位信息
     * @return 修改结果
     */
    boolean updatePost(SysPost post);

    /**
     * 删除岗位
     *
     * @param postIds 岗位ID数组
     * @return 删除结果
     */
    boolean deletePosts(Long[] postIds);

    /**
     * 校验岗位编码是否唯一
     *
     * @param postCode 岗位编码
     * @param postId 岗位ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkPostCodeUnique(String postCode, Long postId);

    /**
     * 校验岗位名称是否唯一
     *
     * @param postName 岗位名称
     * @param postId 岗位ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkPostNameUnique(String postName, Long postId);

    /**
     * 导出岗位
     *
     * @param post 查询条件
     * @return 岗位列表
     */
    List<SysPost> exportPost(SysPost post);
} 