package com.mars.workflow.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.system.entity.SysDept;
import com.mars.system.entity.SysPost;
import com.mars.system.entity.SysRole;
import com.mars.system.entity.SysUser;
import com.mars.system.service.SysDeptService;
import com.mars.system.service.SysPostService;
import com.mars.system.service.SysRoleService;
import com.mars.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.dromara.warm.flow.ui.dto.HandlerFunDto;
import org.dromara.warm.flow.ui.dto.HandlerQuery;
import org.dromara.warm.flow.ui.service.HandlerSelectService;
import org.dromara.warm.flow.ui.vo.HandlerFeedBackVo;
import org.dromara.warm.flow.ui.vo.HandlerSelectVo;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * 流程设计器办理人选择服务
 */
@Component
@RequiredArgsConstructor
public class MarsHandlerSelectService implements HandlerSelectService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final SysUserService userService;
    private final SysRoleService roleService;
    private final SysDeptService deptService;
    private final SysPostService postService;

    @Override
    public List<String> getHandlerType() {
        return Arrays.asList("用户", "角色", "部门", "岗位");
    }

    @Override
    public HandlerSelectVo getHandlerSelect(HandlerQuery query) {
        String handlerType = query.getHandlerType();
        if ("用户".equals(handlerType)) {
            return selectUsers(query);
        }
        if ("角色".equals(handlerType)) {
            return selectRoles(query);
        }
        if ("部门".equals(handlerType)) {
            return selectDepts(query);
        }
        if ("岗位".equals(handlerType)) {
            return selectPosts(query);
        }
        return new HandlerSelectVo();
    }

  @Override
  public List<HandlerFeedBackVo> handlerFeedback(List<String> storageIds) {
    if (storageIds == null || storageIds.isEmpty()) {
      return List.of();
    }
    List<HandlerFeedBackVo> result = new java.util.ArrayList<>();
    for (String storageId : storageIds) {
      HandlerFeedBackVo vo = new HandlerFeedBackVo();
      vo.setStorageId(storageId);
      if (storageId.startsWith("role:")) {
        SysRole role = roleService.getById(Long.parseLong(storageId.substring(5)));
        if (role != null) {
          vo.setHandlerName(role.getName());
        }
      } else if (storageId.startsWith("dept:")) {
        SysDept dept = deptService.getById(Long.parseLong(storageId.substring(5)));
        if (dept != null) {
          vo.setHandlerName(dept.getDeptName());
        }
      } else if (storageId.startsWith("post:")) {
        SysPost post = postService.getById(Long.parseLong(storageId.substring(5)));
        if (post != null) {
          vo.setHandlerName(post.getPostName());
        }
      } else {
        SysUser user = userService.getById(Long.parseLong(storageId));
        if (user != null) {
          vo.setHandlerName(user.getNickname() != null ? user.getNickname() : user.getUsername());
        }
      }
      result.add(vo);
    }
    return result;
  }

    private HandlerSelectVo selectUsers(HandlerQuery query) {
        int pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getStatus, 1)
                .orderByDesc(SysUser::getCreateTime);
        if (query.getHandlerName() != null && !query.getHandlerName().isEmpty()) {
            wrapper.and(w -> w.like(SysUser::getUsername, query.getHandlerName())
                    .or().like(SysUser::getNickname, query.getHandlerName()));
        }

        Page<SysUser> page = userService.page(new Page<>(pageNum, pageSize), wrapper);
        HandlerFunDto<SysUser> funDto = new HandlerFunDto<>(page.getRecords(), page.getTotal())
                .setStorageId(u -> String.valueOf(u.getId()))
                .setHandlerCode(u -> u.getUsername())
                .setHandlerName(u -> u.getNickname() != null ? u.getNickname() : u.getUsername())
                .setCreateTime(u -> u.getCreateTime() != null ? u.getCreateTime().format(DATE_TIME_FORMATTER) : "");
        return getHandlerSelectVo(funDto);
    }

    private HandlerSelectVo selectRoles(HandlerQuery query) {
        List<SysRole> roles = roleService.listEnabled();
        if (query.getHandlerName() != null && !query.getHandlerName().isEmpty()) {
            roles = roles.stream()
                    .filter(r -> r.getName().contains(query.getHandlerName()))
                    .toList();
        }
        HandlerFunDto<SysRole> funDto = new HandlerFunDto<>(roles, roles.size())
                .setStorageId(r -> "role:" + r.getId())
                .setHandlerCode(r -> r.getCode())
                .setHandlerName(SysRole::getName)
                .setCreateTime(r -> r.getCreateTime() != null ? r.getCreateTime().format(DATE_TIME_FORMATTER) : "");
        return getHandlerSelectVo(funDto);
    }

    private HandlerSelectVo selectDepts(HandlerQuery query) {
        List<SysDept> depts = deptService.listAll();
        if (query.getHandlerName() != null && !query.getHandlerName().isEmpty()) {
            depts = depts.stream()
                    .filter(d -> d.getDeptName().contains(query.getHandlerName()))
                    .toList();
        }
        HandlerFunDto<SysDept> funDto = new HandlerFunDto<>(depts, depts.size())
                .setStorageId(d -> "dept:" + d.getId())
                .setHandlerCode(d -> String.valueOf(d.getId()))
                .setHandlerName(SysDept::getDeptName)
                .setCreateTime(d -> d.getCreateTime() != null ? d.getCreateTime().format(DATE_TIME_FORMATTER) : "");
        return getHandlerSelectVo(funDto);
    }

    private HandlerSelectVo selectPosts(HandlerQuery query) {
        List<SysPost> posts = postService.listAll();
        if (query.getHandlerName() != null && !query.getHandlerName().isEmpty()) {
            posts = posts.stream()
                    .filter(p -> p.getPostName().contains(query.getHandlerName()))
                    .toList();
        }
        HandlerFunDto<SysPost> funDto = new HandlerFunDto<>(posts, posts.size())
                .setStorageId(p -> "post:" + p.getId())
                .setHandlerCode(SysPost::getPostCode)
                .setHandlerName(SysPost::getPostName)
                .setCreateTime(p -> p.getCreateTime() != null ? p.getCreateTime().format(DATE_TIME_FORMATTER) : "");
        return getHandlerSelectVo(funDto);
    }
}
