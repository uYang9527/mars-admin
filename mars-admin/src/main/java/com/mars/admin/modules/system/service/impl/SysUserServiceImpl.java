package com.mars.admin.modules.system.service.impl;

import com.mars.admin.modules.system.entity.SysUser;
import com.mars.admin.modules.system.entity.SysUserDept;
import com.mars.admin.modules.system.entity.SysUserPost;
import com.mars.admin.modules.system.entity.SysUserRole;
import com.mars.admin.framework.exception.BusinessException;
import com.mars.admin.modules.system.mapper.SysUserDeptMapper;
import com.mars.admin.modules.system.mapper.SysUserMapper;
import com.mars.admin.modules.system.mapper.SysUserPostMapper;
import com.mars.admin.modules.system.mapper.SysUserRoleMapper;
import com.mars.admin.modules.system.service.ISysUserService;
import com.mars.admin.modules.base.service.impl.BaseServiceImpl;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mars.admin.modules.system.entity.table.SysUserDeptTableDef.SYS_USER_DEPT;
import static com.mars.admin.modules.system.entity.table.SysUserPostTableDef.SYS_USER_POST;
import static com.mars.admin.modules.system.entity.table.SysUserRoleTableDef.SYS_USER_ROLE;
import static com.mars.admin.modules.system.entity.table.SysUserTableDef.SYS_USER;


/**
 * 系统用户Service实现类
 * 继承 BaseServiceImpl 获得更多便捷方法
 *
 * @author Mars
 */
@Slf4j
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserDeptMapper sysUserDeptMapper;

    @Autowired
    private SysUserPostMapper sysUserPostMapper;

    @Override
    public SysUser selectByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public SysUser selectByEmail(String email) {
        return sysUserMapper.selectByEmail(email);
    }

    @Override
    public SysUser selectByPhone(String phone) {
        return sysUserMapper.selectByPhone(phone);
    }

    @Override
    public SysUser selectUserDetailById(Long userId) {
        return sysUserMapper.selectUserDetailById(userId);
    }

    @Override
    public Page<SysUser> selectUserPage(Page<SysUser> page, SysUser user) {
        QueryWrapper query = QueryWrapper.create()
                .select(SYS_USER.ALL_COLUMNS)
                .from(SYS_USER)
                .where(SYS_USER.IS_DELETED.eq(0));

        if (StringUtils.hasText(user.getUsername())) {
            query.and(SYS_USER.USERNAME.like(user.getUsername()));
        }
        if (StringUtils.hasText(user.getNickname())) {
            query.and(SYS_USER.NICKNAME.like(user.getNickname()));
        }
        if (StringUtils.hasText(user.getPhone())) {
            query.and(SYS_USER.PHONE.like(user.getPhone()));
        }
        if (StringUtils.hasText(user.getEmail())) {
            query.and(SYS_USER.EMAIL.like(user.getEmail()));
        }
        if (user.getGender() != null) {
            query.and(SYS_USER.GENDER.eq(user.getGender()));
        }
        if (user.getStatus() != null) {
            query.and(SYS_USER.STATUS.eq(user.getStatus()));
        }
        if (user.getUserType() != null) {
            query.and(SYS_USER.USER_TYPE.eq(user.getUserType()));
        }

        query.orderBy(SYS_USER.CREATE_TIME.desc());

        return this.page(page, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(SysUser user) {
        // 设置默认值
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        if (user.getUserType() == null) {
            user.setUserType(1);
        }
        if (user.getLoginCount() == null) {
            user.setLoginCount(0);
        }

        // 密码加密处理
        if (user.getPassword() != null) {
            user.setPassword(AuthServiceImpl.encryptPassword(user.getPassword()));
        }

        // 插入用户
        boolean result = this.save(user);

        // 分配角色
        if (result && user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
            assignRoles(user.getId(), user.getRoleIds().toArray(new Long[0]));
        }

        // 分配部门
        if (result && user.getDeptIds() != null && !user.getDeptIds().isEmpty()) {
            // 默认第一个为主部门
            Long mainDeptId = user.getDeptIds().get(0);
            assignDepts(user.getId(), user.getDeptIds().toArray(new Long[0]), mainDeptId);
        }

        // 分配岗位
        if (result && user.getPostIds() != null && !user.getPostIds().isEmpty()) {
            assignPosts(user.getId(), user.getPostIds().toArray(new Long[0]));
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        // 更新用户
        boolean result = this.updateById(user);

        if (result) {
            // 重新分配角色
            if (user.getRoleIds() != null) {
                assignRoles(user.getId(), user.getRoleIds().toArray(new Long[0]));
            }

            // 重新分配部门
            if (user.getDeptIds() != null) {
                // 默认第一个为主部门
                Long mainDeptId = user.getDeptIds().isEmpty() ? null : user.getDeptIds().get(0);
                assignDepts(user.getId(), user.getDeptIds().toArray(new Long[0]), mainDeptId);
            }

            // 重新分配岗位
            if (user.getPostIds() != null) {
                assignPosts(user.getId(), user.getPostIds().toArray(new Long[0]));
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUsers(Long[] userIds) {
        if (userIds == null || userIds.length == 0) {
            return false;
        }

        try {
            for (Long userId : userIds) {
                // 先检查是否管理员账号，避免删除管理员
                SysUser user = this.getById(userId);
                if (user != null && "admin".equals(user.getUsername())) {
                    throw new BusinessException("管理员账号不能删除");
                }

                // 删除用户角色关联
                QueryWrapper userRoleQuery = QueryWrapper.create()
                        .where(SYS_USER_ROLE.USER_ID.eq(userId));
                sysUserRoleMapper.deleteByQuery(userRoleQuery);

                // 删除用户部门关联
                QueryWrapper userDeptQuery = QueryWrapper.create()
                        .where(SYS_USER_DEPT.USER_ID.eq(userId));
                sysUserDeptMapper.deleteByQuery(userDeptQuery);

                // 删除用户岗位关联
                QueryWrapper userPostQuery = QueryWrapper.create()
                        .where(SYS_USER_POST.USER_ID.eq(userId));
                sysUserPostMapper.deleteByQuery(userPostQuery);

                // 设置删除时间
                SysUser deleteUser = new SysUser();
                deleteUser.setId(userId);
                deleteUser.setDeleteTime(LocalDateTime.now());
                this.updateById(deleteUser);
            }

            // 批量逻辑删除用户
            return this.removeByIds(Arrays.asList(userIds));
        } catch (BusinessException e) {
            log.error("删除用户失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage(), e);
            throw new BusinessException("删除用户失败: " + e.getMessage());
        }
    }

    @Override
    public boolean resetPassword(Long userId, String password) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(password);
        user.setPasswordUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    public boolean changeStatus(Long userId, Integer status) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setStatus(status);
        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(Long userId, Long[] roleIds) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        try {
            // 删除用户原有角色
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(SYS_USER_ROLE.USER_ID.eq(userId));
            sysUserRoleMapper.deleteByQuery(queryWrapper);

            // 如果没有分配角色，直接返回
            if (roleIds == null || roleIds.length == 0) {
                return true;
            }

            // 批量插入新的用户角色关联
            List<SysUserRole> userRoles = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateBy(userId); // 当前登录用户ID，可从上下文获取
                userRoles.add(userRole);
            }

            return sysUserRoleMapper.insertBatch(userRoles) > 0;
        } catch (Exception e) {
            log.error("分配角色失败: {}", e.getMessage(), e);
            throw new BusinessException("分配角色失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignDepts(Long userId, Long[] deptIds, Long mainDeptId) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        try {
            // 删除用户原有部门
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(SYS_USER_DEPT.USER_ID.eq(userId));
            sysUserDeptMapper.deleteByQuery(queryWrapper);

            // 如果没有分配部门，直接返回
            if (deptIds == null || deptIds.length == 0) {
                return true;
            }

            // 批量插入新的用户部门关联
            List<SysUserDept> userDepts = new ArrayList<>();
            for (Long deptId : deptIds) {
                SysUserDept userDept = new SysUserDept();
                userDept.setUserId(userId);
                userDept.setDeptId(deptId);

                // 设置是否主部门
                userDept.setIsMain(deptId.equals(mainDeptId) ? 1 : 0);

                userDept.setCreateBy(userId); // 当前登录用户ID，可从上下文获取
                userDepts.add(userDept);
            }

            return sysUserDeptMapper.insertBatch(userDepts) > 0;
        } catch (Exception e) {
            log.error("分配部门失败: {}", e.getMessage(), e);
            throw new BusinessException("分配部门失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPosts(Long userId, Long[] postIds) {
        if (userId == null) {
            throw new BusinessException("用户ID不能为空");
        }

        try {
            // 删除用户原有岗位
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(SYS_USER_POST.USER_ID.eq(userId));
            sysUserPostMapper.deleteByQuery(queryWrapper);

            // 如果没有分配岗位，直接返回
            if (postIds == null || postIds.length == 0) {
                return true;
            }

            // 批量插入新的用户岗位关联
            List<SysUserPost> userPosts = new ArrayList<>();
            for (Long postId : postIds) {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(userId);
                userPost.setPostId(postId);
                userPost.setCreateBy(userId); // 当前登录用户ID，可从上下文获取
                userPosts.add(userPost);
            }

            return sysUserPostMapper.insertBatch(userPosts) > 0;
        } catch (Exception e) {
            log.error("分配岗位失败: {}", e.getMessage(), e);
            throw new BusinessException("分配岗位失败: " + e.getMessage());
        }
    }

    @Override
    public boolean updateLoginInfo(Long userId, String loginIp) {
        return sysUserMapper.updateUserLoginInfo(userId, loginIp) > 0;
    }

    @Override
    public boolean checkUsernameUnique(String username, Long userId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_USER.USERNAME.eq(username))
                .and(SYS_USER.IS_DELETED.eq(0));

        if (userId != null) {
            query.and(SYS_USER.ID.ne(userId));
        }

        return this.count(query) == 0;
    }

    @Override
    public boolean checkEmailUnique(String email, Long userId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_USER.EMAIL.eq(email))
                .and(SYS_USER.IS_DELETED.eq(0));

        if (userId != null) {
            query.and(SYS_USER.ID.ne(userId));
        }

        return this.count(query) == 0;
    }

    @Override
    public boolean checkPhoneUnique(String phone, Long userId) {
        QueryWrapper query = QueryWrapper.create()
                .where(SYS_USER.PHONE.eq(phone))
                .and(SYS_USER.IS_DELETED.eq(0));

        if (userId != null) {
            query.and(SYS_USER.ID.ne(userId));
        }

        return this.count(query) == 0;
    }

    @Override
    public String importUser(List<SysUser> userList, boolean updateSupport) {
        if (userList == null || userList.isEmpty()) {
            throw BusinessException.of("导入用户列表不能为空");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SysUser user : userList) {
            try {
                                 // 检查用户名是否已存在
                 SysUser existUser = sysUserMapper.selectByUsername(user.getUsername());
                if (existUser == null) {
                    // 新增用户
                    if (user.getPassword() != null) {
                        user.setPassword(AuthServiceImpl.encryptPassword(user.getPassword()));
                    }
                    this.save(user);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、用户 ").append(user.getUsername()).append(" 导入成功");
                } else if (updateSupport) {
                    // 更新用户
                    user.setId(existUser.getId());
                    if (user.getPassword() != null) {
                        user.setPassword(AuthServiceImpl.encryptPassword(user.getPassword()));
                    }
                    this.updateById(user);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、用户 ").append(user.getUsername()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、用户 ").append(user.getUsername()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、用户 " + user.getUsername() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw BusinessException.of(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public List<SysUser> exportUser(SysUser user) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(SYS_USER)
                .where(SYS_USER.IS_DELETED.eq(0));

        if (StringUtils.hasText(user.getUsername())) {
            query.and(SYS_USER.USERNAME.like(user.getUsername()));
        }
        if (StringUtils.hasText(user.getNickname())) {
            query.and(SYS_USER.NICKNAME.like(user.getNickname()));
        }
        if (user.getStatus() != null) {
            query.and(SYS_USER.STATUS.eq(user.getStatus()));
        }

        return this.list(query);
    }

    /**
     * 更新个人用户信息
     * @param user 用户信息
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePersonalInfo(SysUser user) {
        if (user == null || user.getId() == null) {
            throw new BusinessException("用户ID不能为空");
        }

        try {
            // 1. 检查用户是否存在
            SysUser existUser = this.getById(user.getId());
            if (existUser == null) {
                throw new BusinessException("用户不存在");
            }

            // 2. 检查用户状态
            if (existUser.getStatus() == null || existUser.getStatus() == 0) {
                throw new BusinessException("用户已被禁用，无法修改个人信息");
            }

            // 3. 验证邮箱唯一性
            if (StringUtils.hasText(user.getEmail()) && !user.getEmail().equals(existUser.getEmail())) {
                if (!checkEmailUnique(user.getEmail(), user.getId())) {
                    throw new BusinessException("邮箱已存在，请使用其他邮箱");
                }
            }

            // 4. 验证手机号唯一性
            if (StringUtils.hasText(user.getPhone()) && !user.getPhone().equals(existUser.getPhone())) {
                if (!checkPhoneUnique(user.getPhone(), user.getId())) {
                    throw new BusinessException("手机号已存在，请使用其他手机号");
                }
            }

            // 5. 构建更新用户对象，只更新个人信息相关字段
            SysUser updateUser = new SysUser();
            updateUser.setId(user.getId());

            // 只更新允许修改的个人信息字段
            if (StringUtils.hasText(user.getNickname())) {
                updateUser.setNickname(user.getNickname().trim());
            }
            if (StringUtils.hasText(user.getRealName())) {
                updateUser.setRealName(user.getRealName().trim());
            }
            if (StringUtils.hasText(user.getEmail())) {
                updateUser.setEmail(user.getEmail().trim());
            }
            if (StringUtils.hasText(user.getPhone())) {
                updateUser.setPhone(user.getPhone().trim());
            }
            if (user.getGender() != null) {
                updateUser.setGender(user.getGender());
            }
            if (StringUtils.hasText(user.getAvatar())) {
                updateUser.setAvatar(user.getAvatar());
            }
            if (user.getBirthday() != null) {
                updateUser.setBirthday(user.getBirthday());
            }
            if (user.getRemark() != null) {
                updateUser.setRemark(user.getRemark());
            }

            // 设置更新时间
            updateUser.setUpdateTime(LocalDateTime.now());

            // 6. 执行更新
            boolean result = this.updateById(updateUser);

            if (result) {
                log.info("用户个人信息更新成功，用户ID：{}", user.getId());
            } else {
                log.error("用户个人信息更新失败，用户ID：{}", user.getId());
            }

            return result;

        } catch (BusinessException e) {
            log.error("更新个人信息失败：{}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("更新个人信息失败，用户ID：{}，错误信息：{}", user.getId(), e.getMessage(), e);
            throw new BusinessException("更新个人信息失败：" + e.getMessage());
        }
    }
}
