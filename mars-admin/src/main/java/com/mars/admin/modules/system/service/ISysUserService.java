package com.mars.admin.modules.system.service;

import com.mars.admin.modules.system.entity.SysUser;
import com.mars.admin.modules.base.service.BaseService;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

/**
 * 系统用户Service接口
 * 继承 BaseService 获得更多便捷方法
 *
 * @author Mars
 */
public interface ISysUserService extends BaseService<SysUser> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser selectByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    SysUser selectByEmail(String email);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    SysUser selectByPhone(String phone);

    /**
     * 查询用户详情（包含角色、部门、岗位信息）
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    SysUser selectUserDetailById(Long userId);

    /**
     * 分页查询用户列表
     *
     * @param page 分页参数
     * @param user 查询条件
     * @return 用户分页列表
     */
    Page<SysUser> selectUserPage(Page<SysUser> page, SysUser user);

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 新增结果
     */
    boolean insertUser(SysUser user);

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return 修改结果
     */
    boolean updateUser(SysUser user);

    /**
     * 删除用户
     *
     * @param userIds 用户ID数组
     * @return 删除结果
     */
    boolean deleteUsers(Long[] userIds);

    /**
     * 重置用户密码
     *
     * @param userId 用户ID
     * @param password 新密码
     * @return 重置结果
     */
    boolean resetPassword(Long userId, String password);

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 修改结果
     */
    boolean changeStatus(Long userId, Integer status);

    /**
     * 分配用户角色
     *
     * @param userId 用户ID
     * @param roleIds 角色ID数组
     * @return 分配结果
     */
    boolean assignRoles(Long userId, Long[] roleIds);

    /**
     * 分配用户部门
     *
     * @param userId 用户ID
     * @param deptIds 部门ID数组
     * @param mainDeptId 主部门ID
     * @return 分配结果
     */
    boolean assignDepts(Long userId, Long[] deptIds, Long mainDeptId);

    /**
     * 分配用户岗位
     *
     * @param userId 用户ID
     * @param postIds 岗位ID数组
     * @return 分配结果
     */
    boolean assignPosts(Long userId, Long[] postIds);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @return 更新结果
     */
    boolean updateLoginInfo(Long userId, String loginIp);

    /**
     * 校验用户名是否唯一
     *
     * @param username 用户名
     * @param userId 用户ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkUsernameUnique(String username, Long userId);

    /**
     * 校验邮箱是否唯一
     *
     * @param email 邮箱
     * @param userId 用户ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkEmailUnique(String email, Long userId);

    /**
     * 校验手机号是否唯一
     *
     * @param phone 手机号
     * @param userId 用户ID（修改时传入）
     * @return 是否唯一
     */
    boolean checkPhoneUnique(String phone, Long userId);

    /**
     * 导入用户数据
     *
     * @param userList 用户列表
     * @param isUpdateSupport 是否支持更新
     * @return 导入结果
     */
    String importUser(List<SysUser> userList, boolean isUpdateSupport);

    /**
     * 导出用户数据
     *
     * @param user 查询条件
     * @return 用户列表
     */
    List<SysUser> exportUser(SysUser user);

    /**
     * 更新个人信息
     *
     * @param user 用户信息
     * @return 更新结果
     */
    boolean updatePersonalInfo(SysUser user);
}
