package com.mars.admin.service.impl;

import com.mars.admin.entity.User;
import com.mars.admin.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 * 继承 CrudServiceImpl 自动获得完整的 CRUD 功能
 * 无需手动编写任何代码，只需要实现接口即可！
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-06-23 17:35:08
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {
    
    // 无需编写任何代码！
    // 继承 CrudServiceImpl 后，自动拥有所有 CRUD 功能
    // 自动注入 UserMapper，通过反射获取泛型类型
    // 如果需要自定义方法，可以在这里添加
    
} 