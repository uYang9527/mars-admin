package com.mars.admin.controller;

import com.mars.admin.controller.base.BaseController;
import com.mars.admin.entity.User;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * 继承 BaseController 自动获得完整的增删改查功能
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-06-23 17:35:08
 */
@Tag(name = "用户管理", description = "用户相关的增删改查操作")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController extends BaseController<User, Long> {

    private final IUserService userService;

    // 继承 BaseController 后，自动拥有以下接口：
    // GET    /user/list           - 获取所有用户
    // GET    /user/{id}           - 根据ID获取用户
    // GET    /user/page           - 分页查询用户
    // GET    /user/count          - 获取用户总数
    // POST   /user                - 新增用户
    // POST   /user/batch          - 批量新增用户
    // POST   /user/saveOrUpdate   - 保存或更新用户
    // PUT    /user                - 更新用户
    // DELETE /user/{id}           - 删除用户
    // DELETE /user/batch          - 批量删除用户
    // DELETE /user/all            - 删除所有用户

    // 如果需要自定义方法，可以在这里添加
    // 例如：根据用户名查询、修改密码等特殊业务逻辑


    /**
     * 自定义方法示例：根据ID获取用户换成redis
     *
     * @param id 主键ID
     * @return 用户信息
     */
    @Operation(summary = "根据ID获取用户", description = "根据ID获取用户信息")
    @GetMapping("/getById")
    public Result<User> getById(Long id) {
        return Result.success("获取用户成功", userService.getUserByUserId(id));
    }

}
