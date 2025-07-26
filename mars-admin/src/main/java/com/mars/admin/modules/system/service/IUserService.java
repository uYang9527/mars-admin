package com.mars.admin.modules.system.service;

import com.mars.admin.modules.system.entity.User;
import com.mars.admin.modules.base.service.BaseService;

/**
 * 用户服务接口
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-06-23 17:35:08
 */
public interface IUserService extends BaseService<User> {


    /**
     * 根据用户名查询用户
     *
     * @param id 用户id
     * @return 用户
     */
    User getUserByUserId(Long id);

}
