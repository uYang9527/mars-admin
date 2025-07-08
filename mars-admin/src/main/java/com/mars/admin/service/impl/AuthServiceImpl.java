package com.mars.admin.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.mars.admin.common.request.LoginRequest;
import com.mars.admin.common.response.LoginResponse;
import com.mars.admin.entity.SysUser;
import com.mars.admin.entity.SysRole;
import com.mars.admin.entity.SysDept;
import com.mars.admin.entity.SysPost;
import com.mars.admin.entity.SysMenu;
import com.mars.admin.framework.exception.BusinessException;
import com.mars.admin.framework.strategy.LoginStrategyFactory;
import com.mars.admin.framework.strategy.LoginStrategy;
import com.mars.admin.framework.strategy.impl.AppLoginStrategy;
import com.mars.admin.mapper.SysUserMapper;
import com.mars.admin.service.IAuthService;
import com.mars.admin.service.ISysLoginInfoService;
import com.mars.admin.service.ISysRoleService;
import com.mars.admin.service.ISysDeptService;
import com.mars.admin.service.ISysPostService;
import com.mars.admin.service.ISysMenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.mybatisflex.core.query.QueryWrapper;

/**
 * 认证服务实现类
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final SysUserMapper sysUserMapper;

    private final ISysLoginInfoService sysLoginInfoService;

    private final ISysRoleService sysRoleService;

    private final ISysDeptService sysDeptService;

    private final ISysPostService sysPostService;

    private final ISysMenuService sysMenuService;

    private final LoginStrategyFactory loginStrategyFactory;

    private final HttpServletRequest request;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 使用策略工厂进行多端登录
        LoginStrategy strategy = loginStrategyFactory.getStrategy(loginRequest.getLoginType());
        return strategy.login(loginRequest);
    }

    /**
     * 原有的PC登录逻辑（保留作为备份）
     */
    @Deprecated
    public LoginResponse oldLogin(LoginRequest loginRequest) {
        // 1. 参数校验
        if (loginRequest == null || !StringUtils.hasText(loginRequest.getUsername()) || !StringUtils.hasText(loginRequest.getPassword())) {
            throw BusinessException.of("用户名或密码不能为空");
        }

        String username = loginRequest.getUsername().trim();
        String password = loginRequest.getPassword();

        // 2. 查询用户
        SysUser user = sysUserMapper.selectByUsername(username);

        // 校验用户信息
        this.checkUserStatus(user, username, password);

        // 6. 执行登录
        StpUtil.login(user.getId());

        // 7. 获取 Token 信息
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 8. 更新用户登录信息
        updateUserLoginInfo(user);

        // 9. 记录登录成功日志
        recordLoginInfo(username, "0", "登录成功");

        // 10. 构建返回结果
        LoginResponse response = new LoginResponse();
        response.setToken(tokenInfo.getTokenValue());
        response.setExpiresIn(tokenInfo.getTokenTimeout());

        // 清除敏感信息
        user.setPassword(null);

        log.info("用户 {} 登录成功，用户ID: {}", username, user.getId());
        return response;
    }

    private void checkUserStatus(SysUser user, String username, String password) {
        if (user == null) {
            // 记录登录失败日志
            recordLoginInfo(username, "0", "用户不存在");
            throw BusinessException.of("用户名或密码错误");
        }

        // 3. 验证密码
        if (!checkPassword(password, user.getPassword())) {
            // 记录登录失败日志
            recordLoginInfo(username, "0", "密码错误");
            throw BusinessException.of("用户名或密码错误");
        }

        // 4. 检查用户状态
        if (user.getStatus() == null || user.getStatus() == 0) {
            recordLoginInfo(username, "0", "用户已被禁用");
            throw BusinessException.of("用户已被禁用，请联系管理员");
        }

        // 5. 检查用户是否被删除
        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            recordLoginInfo(username, "0", "用户已被删除");
            throw BusinessException.of("用户不存在");
        }
    }

    @Override
    public void logout() {
        if (StpUtil.isLogin()) {
            Object loginId = StpUtil.getLoginId();

            // 获取用户信息用于记录日志
            SysUser user = null;
            if (loginId != null) {
                user = sysUserMapper.selectOneById(Long.valueOf(loginId.toString()));
            }

            // 执行登出
            StpUtil.logout();

            // 记录登出日志
            String username = user != null ? user.getUsername() : loginId.toString();
            recordLoginInfo(username, "2", "退出成功");

            log.info("用户 {} 登出成功", username);
        }
    }

    @Override
    public SysUser getCurrentUser() {
        if (!StpUtil.isLogin()) {
            throw BusinessException.of("用户未登录");
        }

        Object loginId = StpUtil.getLoginId();
        SysUser user = sysUserMapper.selectOneById(Long.valueOf(loginId.toString()));
        if (user == null) {
            throw BusinessException.of("用户不存在");
        }

        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() == 0) {
            throw BusinessException.of("用户已被禁用");
        }

        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            throw BusinessException.of("用户已被删除");
        }

        // 查询用户角色信息
        List<SysRole> roles = sysRoleService.selectRolesByUserId(user.getId());
        user.setRoles(roles);
        if (roles != null && !roles.isEmpty()) {
            List<Long> roleIds = roles.stream().map(SysRole::getId).collect(Collectors.toList());
            user.setRoleIds(roleIds);
        }

        // 查询用户部门信息
        List<SysDept> depts = sysDeptService.selectDeptsByUserId(user.getId());
        user.setDepts(depts);
        if (depts != null && !depts.isEmpty()) {
            List<Long> deptIds = depts.stream().map(SysDept::getId).collect(Collectors.toList());
            user.setDeptIds(deptIds);
        }

        // 查询用户岗位信息
        List<SysPost> posts = sysPostService.selectPostsByUserId(user.getId());
        user.setPosts(posts);
        if (posts != null && !posts.isEmpty()) {
            List<Long> postIds = posts.stream().map(SysPost::getId).collect(Collectors.toList());
            user.setPostIds(postIds);
        }

        // 查询用户菜单信息并转换为路由格式
        List<SysMenu> userMenus = sysMenuService.selectMenusByUserId(user.getId());
        log.info("用户 {} 查询到菜单数量: {}", user.getId(), userMenus != null ? userMenus.size() : 0);

        // 如果用户没有菜单，尝试查询所有菜单（用于调试）
        if (userMenus == null || userMenus.isEmpty()) {
            log.warn("用户没有菜单权限，尝试查询所有菜单进行调试");
            try {
                List<SysMenu> allMenus = sysMenuService.selectMenuTree();
                log.info("数据库中总菜单数量: {}", allMenus != null ? allMenus.size() : 0);
                if (allMenus != null && !allMenus.isEmpty()) {
                    // 暂时返回所有菜单用于测试
                    userMenus = allMenus;
                    log.info("使用所有菜单进行测试");
                }
            } catch (Exception e) {
                log.error("查询所有菜单失败", e);
            }
        }

        if (userMenus != null && !userMenus.isEmpty()) {
            for (SysMenu menu : userMenus) {
                log.debug("菜单信息: ID={}, 名称={}, 路由名={}, 路径={}, 类型={}, 父ID={}",
                        menu.getId(), menu.getMenuName(), menu.getRouteName(), menu.getRoutePath(), menu.getMenuType(), menu.getParentId());
            }
        }

        List<Map<String, Object>> routes = convertMenusToElegantRoutes(userMenus);
        log.info("转换后的路由数量: {}", routes.size());
        user.setRoutes(routes);

        // 清除敏感信息
        user.setPassword(null);
        return user;
    }

    /**
     * 将菜单转换为前端ElegantRoute格式
     */
    private List<Map<String, Object>> convertMenusToElegantRoutes(List<SysMenu> menus) {
        if (menus == null || menus.isEmpty()) {
            // 如果没有菜单数据，返回一个简单的测试菜单
            return createTestRoutes();
        }

        // 过滤掉按钮类型的菜单，只保留目录(M)和菜单(C)
        List<SysMenu> validMenus = menus.stream()
                .filter(menu -> "M".equals(menu.getMenuType()) || "C".equals(menu.getMenuType()))
                .collect(Collectors.toList());

        if (validMenus.isEmpty()) {
            log.warn("没有找到有效的菜单数据，返回测试菜单");
            return createTestRoutes();
        }

        // 按层级组织菜单
        Map<Long, List<SysMenu>> menuMap = new HashMap<>();
        List<SysMenu> rootMenus = new ArrayList<>();

        // 分组：根菜单和子菜单
        for (SysMenu menu : validMenus) {
            if (menu.getParentId() == 0) {
                rootMenus.add(menu);
            } else {
                menuMap.computeIfAbsent(menu.getParentId(), k -> new ArrayList<>()).add(menu);
            }
        }

        // 按排序号排序
        rootMenus.sort(Comparator.comparing(SysMenu::getOrderNum));
        menuMap.values().forEach(list -> list.sort(Comparator.comparing(SysMenu::getOrderNum)));

        // 转换根菜单
        List<Map<String, Object>> routes = new ArrayList<>();
        for (SysMenu rootMenu : rootMenus) {
            Map<String, Object> route = convertMenuToRoute(rootMenu, menuMap);
            if (route != null && !route.isEmpty()) {
                routes.add(route);
            }
        }

        log.info("转换完成，共生成 {} 个路由", routes.size());
        return routes;
    }

    /**
     * 创建测试路由数据
     */
    private List<Map<String, Object>> createTestRoutes() {
        List<Map<String, Object>> routes = new ArrayList<>();

        // 创建系统管理菜单
        Map<String, Object> manageRoute = new HashMap<>();
        manageRoute.put("name", "manage");
        manageRoute.put("path", "/manage");
        manageRoute.put("component", "layout.base");

        // 创建meta信息
        Map<String, Object> manageMeta = new HashMap<>();
        manageMeta.put("title", "系统管理");
        manageMeta.put("i18nKey", "route.manage");
        manageMeta.put("icon", "carbon:cloud-service-management");
        manageMeta.put("order", 1);
        manageMeta.put("hideInMenu", false);
        manageMeta.put("keepAlive", false);
        manageMeta.put("roles", Arrays.asList("R_ADMIN"));
        manageRoute.put("meta", manageMeta);

        // 创建子菜单
        List<Map<String, Object>> children = new ArrayList<>();

        // 用户管理
        Map<String, Object> userRoute = new HashMap<>();
        userRoute.put("name", "manage_user");
        userRoute.put("path", "/manage/user");
        userRoute.put("component", "view.manage_user");

        Map<String, Object> userMeta = new HashMap<>();
        userMeta.put("title", "用户管理");
        userMeta.put("i18nKey", "route.manage_user");
        userMeta.put("icon", "ic:round-manage-accounts");
        userMeta.put("order", 1);
        userMeta.put("hideInMenu", false);
        userMeta.put("keepAlive", false);
        userMeta.put("roles", Arrays.asList("R_ADMIN"));
        userRoute.put("meta", userMeta);

        children.add(userRoute);

        // 角色管理
        Map<String, Object> roleRoute = new HashMap<>();
        roleRoute.put("name", "manage_role");
        roleRoute.put("path", "/manage/role");
        roleRoute.put("component", "view.manage_role");

        Map<String, Object> roleMeta = new HashMap<>();
        roleMeta.put("title", "角色管理");
        roleMeta.put("i18nKey", "route.manage_role");
        roleMeta.put("icon", "carbon:user-role");
        roleMeta.put("order", 2);
        roleMeta.put("hideInMenu", false);
        roleMeta.put("keepAlive", false);
        roleMeta.put("roles", Arrays.asList("R_ADMIN"));
        roleRoute.put("meta", roleMeta);

        children.add(roleRoute);

        manageRoute.put("children", children);
        routes.add(manageRoute);

        log.info("创建测试路由数据完成");
        return routes;
    }

    /**
     * 将单个菜单转换为路由格式
     */
    private Map<String, Object> convertMenuToRoute(SysMenu menu, Map<Long, List<SysMenu>> menuMap) {
        if (menu == null) {
            return null;
        }

        // 跳过按钮类型的菜单
        if ("F".equals(menu.getMenuType())) {
            return null;
        }

        Map<String, Object> route = new HashMap<>();

        // 基本路由信息
        route.put("name", menu.getRouteName() != null ? menu.getRouteName() : "unknown");
        route.put("path", menu.getRoutePath() != null ? menu.getRoutePath() : "");

        // 根据菜单类型设置component
        String component = getComponentByMenuType(menu);
        if (component != null) {
            route.put("component", component);
        }

        // 元信息
        Map<String, Object> meta = new HashMap<>();
        meta.put("title", menu.getMenuName());
        meta.put("i18nKey", generateI18nKey(menu.getRouteName()));
        meta.put("icon", menu.getIcon() != null ? menu.getIcon() : "");
        meta.put("order", menu.getOrderNum() != null ? menu.getOrderNum() : 0);
        meta.put("roles", List.of("R_ADMIN")); // 这里可以根据实际权限设置
        meta.put("keepAlive", menu.getIsCache() != null && menu.getIsCache() == 1);
        meta.put("hideInMenu", menu.getVisible() != null && menu.getVisible() == 0);

        route.put("meta", meta);

        // 处理子菜单（只处理菜单类型，不处理按钮类型）
        List<SysMenu> children = menuMap.get(menu.getId());
        if (children != null && !children.isEmpty()) {
            List<Map<String, Object>> childRoutes = new ArrayList<>();
            for (SysMenu child : children) {
                // 只处理菜单类型的子项
                if ("C".equals(child.getMenuType())) {
                    Map<String, Object> childRoute = convertMenuToRoute(child, menuMap);
                    if (childRoute != null) {
                        childRoutes.add(childRoute);
                    }
                }
            }
            if (!childRoutes.isEmpty()) {
                route.put("children", childRoutes);
            }
        }

        return route;
    }

    /**
     * 转换路由名称，确保符合前端命名规范
     */
    private String convertToRouteName(String routeName) {
        if (routeName == null || routeName.isEmpty()) {
            return "unknown";
        }

        // 将路由名称转换为前端期望的格式
        // 例如：User -> manage_user, Role -> manage_role
        switch (routeName.toLowerCase()) {
            case "user":
                return "manage_user";
            case "role":
                return "manage_role";
            case "menu":
                return "manage_menu";
            case "dept":
                return "manage_dept";
            case "post":
                return "manage_post";
            case "config":
                return "manage_config";
            case "dict":
                return "manage_dict";
            case "system":
                return "manage";
            case "monitor":
                return "monitor";
            case "tool":
                return "tool";
            default:
                return routeName.toLowerCase().replace("-", "_");
        }
    }

    /**
     * 根据菜单类型生成component
     */
    private String getComponentByMenuType(SysMenu menu) {
        if (menu.getMenuType() == null) {
            return null;
        }

        // 如果数据库中已经配置了component，直接使用
        if (menu.getComponent() != null && !menu.getComponent().trim().isEmpty()) {
            return menu.getComponent();
        }

        // 否则根据菜单类型生成默认component
        switch (menu.getMenuType()) {
            case "M": // 目录
                return "layout.base";
            case "C": // 菜单
                // 根据路由名称生成view组件路径
                String routeName = menu.getRouteName() != null ? menu.getRouteName() : "unknown";
                return "view." + routeName;
            case "F": // 按钮
                return null; // 按钮不需要component
            default:
                return null;
        }
    }

    /**
     * 生成国际化key
     */
    private String generateI18nKey(String routeName) {
        if (routeName == null || routeName.isEmpty()) {
            return null;
        }

        return "route." + routeName;
    }

    @Override
    public boolean checkPassword(String inputPassword, String storedPassword) {
        if (!StringUtils.hasText(inputPassword) || !StringUtils.hasText(storedPassword)) {
            return false;
        }

        // 使用 MD5 加密验证密码（实际项目中建议使用 BCrypt 等更安全的方式）
        String encryptedPassword = encryptPassword(inputPassword);
        return encryptedPassword.equals(storedPassword);
    }

    /**
     * 更新用户登录信息
     *
     * @param user 用户信息
     */
    private void updateUserLoginInfo(SysUser user) {
        try {
            // 获取客户端IP
            String loginIp = getClientIp();

            // 更新登录次数和最后登录信息
            Integer loginCount = user.getLoginCount() != null ? user.getLoginCount() + 1 : 1;

            SysUser updateUser = new SysUser();
            updateUser.setId(user.getId());
            updateUser.setLoginCount(loginCount);
            updateUser.setLastLoginTime(LocalDateTime.now());
            updateUser.setLastLoginIp(loginIp);

            sysUserMapper.updateByQuery(updateUser, QueryWrapper.create().where("id = ?", user.getId()));

            // 更新原用户对象的信息
            user.setLoginCount(loginCount);
            user.setLastLoginTime(updateUser.getLastLoginTime());
            user.setLastLoginIp(loginIp);

        } catch (Exception e) {
            log.warn("更新用户登录信息失败: {}", e.getMessage());
        }
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   登录状态：0-失败，1-成功，2-退出
     * @param message  登录信息
     */
    private void recordLoginInfo(String username, String status, String message) {
        try {
            if (sysLoginInfoService != null) {
                sysLoginInfoService.recordLoginInfo(username, status, message, getClientIp());
            }
        } catch (Exception e) {
            log.warn("记录登录日志失败: {}", e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
     *
     * @return IP地址
     */
    private String getClientIp() {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 如果是多级代理，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * 密码加密工具方法
     *
     * @param password 明文密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        if (!StringUtils.hasText(password)) {
            return "";
        }
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            // 1. 获取用户信息
            SysUser user = sysUserMapper.selectOneById(userId);
            if (user == null) {
                log.error("修改密码失败：用户不存在，用户ID：{}", userId);
                return false;
            }

            // 2. 验证原密码
            if (!checkPassword(oldPassword, user.getPassword())) {
                log.error("修改密码失败：原密码错误，用户ID：{}", userId);
                return false;
            }

            // 3. 检查新密码格式
            if (!StringUtils.hasText(newPassword) || newPassword.length() < 6 || newPassword.length() > 20) {
                log.error("修改密码失败：新密码格式不正确，用户ID：{}", userId);
                return false;
            }

            // 4. 加密新密码
            String encryptedNewPassword = encryptPassword(newPassword);

            // 5. 更新密码
            SysUser updateUser = new SysUser();
            updateUser.setId(userId);
            updateUser.setPassword(encryptedNewPassword);
            updateUser.setPasswordUpdateTime(LocalDateTime.now());

            int updateCount = sysUserMapper.update(updateUser);

            if (updateCount > 0) {
                log.info("用户密码修改成功，用户ID：{}", userId);
                return true;
            } else {
                log.error("修改密码失败：数据库更新失败，用户ID：{}", userId);
                return false;
            }

        } catch (Exception e) {
            log.error("修改密码失败，用户ID：{}，错误信息：{}", userId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendCaptcha(String mobile) {
        // 委托给APP登录策略处理验证码发送
        try {
            AppLoginStrategy appLoginStrategy = (AppLoginStrategy) loginStrategyFactory.getStrategy("app");
            return appLoginStrategy.sendCaptcha(mobile);
        } catch (Exception e) {
            log.error("发送验证码失败", e);
            return false;
        }
    }
}
