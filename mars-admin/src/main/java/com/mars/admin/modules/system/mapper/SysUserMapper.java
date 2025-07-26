package com.mars.admin.modules.system.mapper;

import com.mars.admin.modules.base.mapper.BasePlusMapper;
import com.mars.admin.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统用户Mapper接口
 *
 * @author Mars
 */
@Mapper
public interface SysUserMapper extends BasePlusMapper<SysUser> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND is_deleted = 0")
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_user WHERE email = #{email} AND is_deleted = 0")
    SysUser selectByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_user WHERE phone = #{phone} AND is_deleted = 0")
    SysUser selectByPhone(@Param("phone") String phone);

    /**
     * 根据微信OpenId查询用户
     *
     * @param openId 微信OpenId
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_user WHERE open_id = #{openId} AND is_deleted = 0")
    SysUser selectByOpenId(@Param("openId") String openId);

    /**
     * 查询用户详情（包含角色、部门、岗位信息）
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    SysUser selectUserDetailById(@Param("userId") Long userId);

    /**
     * 分页查询用户列表
     *
     * @param user 查询条件
     * @return 用户列表
     */
    List<SysUser> selectUserList(SysUser user);

    /**
     * 根据角色ID查询用户列表
     *
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<SysUser> selectUsersByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据部门ID查询用户列表
     *
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<SysUser> selectUsersByDeptId(@Param("deptId") Long deptId);

    /**
     * 更新用户登录信息
     *
     * @param userId 用户ID
     * @param loginIp 登录IP
     * @return 更新结果
     */
    int updateUserLoginInfo(@Param("userId") Long userId, @Param("loginIp") String loginIp);
}
