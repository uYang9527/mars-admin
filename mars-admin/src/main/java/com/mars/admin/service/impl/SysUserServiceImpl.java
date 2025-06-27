package com.mars.admin.service.impl;

import com.mars.admin.entity.SysUser;
import com.mars.admin.entity.SysUserRole;
import com.mars.admin.framework.exception.BusinessException;
import com.mars.admin.mapper.SysUserMapper;
import com.mars.admin.service.ISysUserService;
import com.mars.admin.service.impl.BaseServiceImpl;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.mars.admin.entity.table.SysUserTableDef.SYS_USER;
import static com.mars.admin.entity.table.SysUserRoleTableDef.SYS_USER_ROLE;

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

        // 插入用户
        boolean result = this.save(user);

        // 分配角色
        if (result && user.getRoleIds() != null && user.getRoleIds().size() > 0) {
            assignRoles(user.getId(), user.getRoleIds().toArray(new Long[0]));
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        // 更新用户
        boolean result = this.updateById(user);

        // 重新分配角色
        if (result && user.getRoleIds() != null) {
            // 重新分配角色
            if (user.getRoleIds().size() > 0) {
                assignRoles(user.getId(), user.getRoleIds().toArray(new Long[0]));
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUsers(Long[] userIds) {
        return this.removeByIds(Arrays.asList(userIds));
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
        // TODO: 实现角色分配逻辑，需要创建SysUserRole的mapper和service
        return true;
    }

    @Override
    public boolean assignDepts(Long userId, Long[] deptIds, Long mainDeptId) {
        // TODO: 实现部门分配逻辑
        return true;
    }

    @Override
    public boolean assignPosts(Long userId, Long[] postIds) {
        // TODO: 实现岗位分配逻辑
        return true;
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
     * 初始化管理员用户
     * 如果系统中没有管理员用户，则创建默认管理员
     */
    @PostConstruct
    public void initAdminUser() {
        try {
                         // 检查是否已存在管理员用户
             SysUser adminUser = sysUserMapper.selectByUsername("admin");
            if (adminUser == null) {
                // 创建默认管理员用户
                adminUser = new SysUser();
                adminUser.setUsername("admin");
                adminUser.setPassword(AuthServiceImpl.encryptPassword("123456")); // 默认密码
                adminUser.setNickname("系统管理员");
                adminUser.setRealName("管理员");
                adminUser.setEmail("admin@mars.com");
                adminUser.setPhone("13800138000");
                adminUser.setStatus(1); // 启用状态
                adminUser.setUserType(1); // 系统用户
                adminUser.setGender(0); // 未知
                adminUser.setRemark("系统默认管理员用户");

                // 保存用户
                this.save(adminUser);
                
                log.info("初始化管理员用户成功：username=admin, password=123456");
            }
        } catch (Exception e) {
            log.warn("初始化管理员用户失败：{}", e.getMessage());
        }
    }
} 