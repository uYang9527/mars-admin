package com.mars.admin.mapper;

import com.mars.admin.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问层
 * 继承 BasePlusMapper 获得更多便捷方法
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-06-23 17:35:08
 */
@Mapper
public interface UserMapper extends BasePlusMapper<User> {
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    default User selectByUsername(String username) {
        return selectOneByField("username", username);
    }
} 