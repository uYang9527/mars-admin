package com.mars.admin.mapper;

import com.mars.admin.entity.SysPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统岗位Mapper接口
 * 继承 BasePlusMapper 获得更多便捷方法
 *
 * @author Mars
 */
@Mapper
public interface SysPostMapper extends BasePlusMapper<SysPost> {

    /**
     * 根据岗位编码查询岗位
     *
     * @param postCode 岗位编码
     * @return 岗位信息
     */
    default SysPost selectByPostCode(String postCode) {
        return selectOneByField("post_code", postCode);
    }

    /**
     * 分页查询岗位列表
     *
     * @param post 查询条件
     * @return 岗位列表
     */
    List<SysPost> selectPostList(SysPost post);

    /**
     * 根据用户ID查询岗位列表
     *
     * @param userId 用户ID
     * @return 岗位列表
     */
    List<SysPost> selectPostsByUserId(@Param("userId") Long userId);

    /**
     * 查询所有正常状态的岗位
     *
     * @return 岗位列表
     */
    @Select("SELECT * FROM sys_post WHERE status = 1 AND is_deleted = 0 ORDER BY post_sort")
    List<SysPost> selectNormalPosts();

    /**
     * 根据岗位名称查询岗位
     *
     * @param postName 岗位名称
     * @return 岗位信息
     */
    default SysPost selectByPostName(String postName) {
        return selectOneByField("post_name", postName);
    }

    /**
     * 根据状态查询岗位列表
     *
     * @param status 状态
     * @return 岗位列表
     */
    default List<SysPost> selectByStatus(Integer status) {
        return selectListByField("status", status);
    }

    /**
     * 检查岗位编码是否存在
     *
     * @param postCode 岗位编码
     * @return 是否存在
     */
    default boolean existsByPostCode(String postCode) {
        return existsByField("post_code", postCode);
    }

    /**
     * 检查岗位名称是否存在
     *
     * @param postName 岗位名称
     * @return 是否存在
     */
    default boolean existsByPostName(String postName) {
        return existsByField("post_name", postName);
    }
} 