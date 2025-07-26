package com.mars.admin.modules.system.service.impl;

import com.mars.admin.modules.system.entity.User;
import com.mars.admin.modules.system.mapper.UserMapper;
import com.mars.admin.modules.system.service.IUserService;
import com.mybatisflex.spring.service.impl.CacheableServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * 继承 CrudServiceImpl 自动获得完整的 CRUD 功能
 * 无需手动编写任何代码，只需要实现接口即可！
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-06-23 17:35:08
 */
@Service
@CacheConfig(cacheNames = "user")
public class UserServiceImpl extends CacheableServiceImpl<UserMapper, User> implements IUserService {


    @Override
    @Cacheable(key = "#id")
    public User getUserByUserId(Long id) {
        return super.getById(id);
    }

    // 无需编写任何代码！
    // 继承 CrudServiceImpl 后，自动拥有所有 CRUD 功能
    // 自动注入 UserMapper，通过反射获取泛型类型
    // 如果需要自定义方法，可以在这里添加

}
