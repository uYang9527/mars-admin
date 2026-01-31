package com.mars.system.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mars.common.exception.BusinessException;
import com.mars.common.result.PageResult;
import com.mars.system.entity.SysUser;
import com.mars.system.entity.SysUserRole;
import com.mars.system.mapper.SysUserMapper;
import com.mars.system.mapper.SysUserRoleMapper;
import com.mars.system.service.SysUserService;
import com.mars.system.service.SystemConfigHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserRoleMapper userRoleMapper;
    private final SystemConfigHelper configHelper;

    private static final String DEFAULT_PASSWORD = "123456";

    @Override
    public PageResult<SysUser> page(Integer page, Integer pageSize, String username, Integer status) {
        Page<SysUser> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(username), SysUser::getUsername, username)
                .eq(status != null, SysUser::getStatus, status)
                .orderByDesc(SysUser::getCreateTime);
        Page<SysUser> result = this.page(pageParam, wrapper);
        // 清空密码
        result.getRecords().forEach(user -> user.setPassword(null));
        return PageResult.of(result);
    }

    @Override
    public SysUser getDetail(Long id) {
        SysUser user = this.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SysUser user, List<Long> roleIds) {
        // 检查用户名是否存在
        if (this.getByUsername(user.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        // 加密密码
        String password = StringUtils.hasText(user.getPassword()) ? user.getPassword() : DEFAULT_PASSWORD;
        user.setPassword(BCrypt.hashpw(password));
        this.save(user);
        // 保存用户角色关联
        saveUserRoles(user.getId(), roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUser user, List<Long> roleIds) {
        SysUser existUser = this.getById(user.getId());
        if (existUser == null) {
            throw new BusinessException("用户不存在");
        }
        // 检查用户名是否存在
        SysUser byUsername = this.getByUsername(user.getUsername());
        if (byUsername != null && !byUsername.getId().equals(user.getId())) {
            throw new BusinessException("用户名已存在");
        }
        // 不更新密码
        user.setPassword(null);
        this.updateById(user);
        // 更新用户角色关联
        userRoleMapper.deleteByUserId(user.getId());
        saveUserRoles(user.getId(), roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
        userRoleMapper.deleteByUserId(id);
    }

    @Override
    public SysUser getByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    public List<String> getRoleCodes(Long userId) {
        return baseMapper.selectRoleCodesByUserId(userId);
    }

    @Override
    public List<String> getPermissions(Long userId) {
        return baseMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        // 验证新密码规则
        configHelper.validatePassword(newPassword);
        
        user.setPassword(BCrypt.hashpw(newPassword));
        this.updateById(user);
    }

    @Override
    public void resetPassword(Long userId) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(BCrypt.hashpw(DEFAULT_PASSWORD));
        this.updateById(user);
    }

    @Override
    public void updateProfile(Long userId, SysUser profile) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 只允许更新昵称、邮箱、手机号、头像
        user.setNickname(profile.getNickname());
        user.setEmail(profile.getEmail());
        user.setPhone(profile.getPhone());
        user.setAvatar(profile.getAvatar());
        this.updateById(user);
    }

    @Override
    public List<SysUser> listAll() {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getStatus, 1);
        wrapper.orderByAsc(SysUser::getId);
        List<SysUser> users = this.list(wrapper);
        users.forEach(u -> u.setPassword(null));
        return users;
    }

    private void saveUserRoles(Long userId, List<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
    }
}
