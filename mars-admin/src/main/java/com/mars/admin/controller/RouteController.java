package com.mars.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mars.admin.entity.SysMenu;
import com.mars.admin.framework.common.Result;
import com.mars.admin.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由控制器
 * 处理前端的路由请求，包括获取用户路由、常量路由等
 *
 * @author Mars
 */
@Slf4j
@RestController
@RequestMapping("/route")
@Tag(name = "路由管理", description = "前端路由相关接口")
public class RouteController {

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 获取用户路由
     */
    @GetMapping("/getUserRoutes")
    @Operation(summary = "获取用户路由", description = "获取当前登录用户的路由信息")
    public Result<Map<String, Object>> getUserRoutes() {
        try {
            // 获取当前登录用户ID
            Object loginId = StpUtil.getLoginId();
            Long userId = Long.valueOf(loginId.toString());
            log.info("获取用户路由，用户ID: {}", userId);

            // 查询用户菜单
            List<SysMenu> userMenus = sysMenuService.selectMenusByUserId(userId);
            log.info("查询到用户菜单数量: {}", userMenus != null ? userMenus.size() : 0);

            // 如果没有查询到菜单，返回空路由
            if (userMenus == null || userMenus.isEmpty()) {
                log.warn("用户没有菜单权限，返回空路由");
                Map<String, Object> emptyResult = new HashMap<>();
                emptyResult.put("routes", new ArrayList<>());
                emptyResult.put("home", "home");
                return Result.success(emptyResult);
            }

            // 转换为前端需要的路由格式
            List<Map<String, Object>> routes = convertMenusToElegantRoutes(userMenus);

            Map<String, Object> result = new HashMap<>();
            result.put("routes", routes);
            result.put("home", "home"); // 默认首页

            log.info("返回路由数据: {}", result);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取用户路由失败", e);
            // 返回空路由，确保前端能正常显示
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("routes", new ArrayList<>());
            errorResult.put("home", "home");
            return Result.success(errorResult);
        }
    }

    /**
     * 获取常量路由
     */
    @GetMapping("/getConstantRoutes")
    @Operation(summary = "获取常量路由", description = "获取系统常量路由")
    public Result<List<Map<String, Object>>> getConstantRoutes() {
        return Result.success(List.of());
    }

    /**
     * 检查路由是否存在
     */
    @GetMapping("/isRouteExist")
    @Operation(summary = "检查路由是否存在", description = "检查指定路由是否存在")
    public Result<Boolean> isRouteExist(@RequestParam String routeName) {
        // 简单实现：检查路由名称是否在菜单中存在
        Object loginId = StpUtil.getLoginId();
        Long userId = Long.valueOf(loginId.toString());

        List<SysMenu> userMenus = sysMenuService.selectMenusByUserId(userId);
        boolean exists = userMenus.stream()
                .anyMatch(menu -> routeName.equals(menu.getRouteName()));

        return Result.success(exists);
    }

    /**
     * 将菜单转换为前端ElegantRoute格式
     */
    private List<Map<String, Object>> convertMenusToElegantRoutes(List<SysMenu> menus) {
        List<Map<String, Object>> routes = new ArrayList<>();

        // 按照菜单层级结构处理
        // 首先找出所有顶级菜单（parent_id = 0）
        List<SysMenu> topMenus = menus.stream()
                .filter(menu -> menu.getParentId() == 0)
                .sorted((a, b) -> Integer.compare(a.getOrderNum(), b.getOrderNum()))
                .toList();

        for (SysMenu topMenu : topMenus) {
            // 检查目录类型菜单是否有子菜单
            if ("M".equals(topMenu.getMenuType())) {
                List<SysMenu> childMenus = menus.stream()
                        .filter(child -> topMenu.getId().equals(child.getParentId()))
                        .filter(child -> "M".equals(child.getMenuType()) || "C".equals(child.getMenuType()))
                        .toList();

                // 如果目录菜单没有有效的子菜单，跳过它
                if (childMenus.isEmpty()) {
                    log.info("跳过空目录菜单: {}", topMenu.getMenuName());
                    continue;
                }
            }

            Map<String, Object> route = convertMenuToRoute(topMenu, menus);
            if (route != null) {
                routes.add(route);
            }
        }

        log.info("转换完成，共生成 {} 个顶级路由", routes.size());
        return routes;
    }

    /**
     * 将单个菜单转换为路由格式
     */
    private Map<String, Object> convertMenuToRoute(SysMenu menu, List<SysMenu> allMenus) {
        // 只处理菜单类型为 M（目录）或 C（菜单）的项
        if (!"M".equals(menu.getMenuType()) && !"C".equals(menu.getMenuType())) {
            return null;
        }

        // 检查必要字段
        if (menu.getRouteName() == null || menu.getRoutePath() == null) {
            log.warn("菜单缺少必要字段: {}", menu);
            return null;
        }

        Map<String, Object> route = new HashMap<>();
        route.put("name", menu.getRouteName());
        route.put("path", menu.getRoutePath());
        // isFrame=0表示是外链，此时设置isFrame=true
        boolean isExternalLink = menu.getIsFrame() == 0;
        route.put("isFrame", isExternalLink);

        // 如果是外链且路径以http开头，将原始路径保存在originalUrl中
        if (isExternalLink && (menu.getRoutePath().startsWith("http://") || menu.getRoutePath().startsWith("https://"))) {
            route.put("originalUrl", menu.getRoutePath());
        }

        // 设置组件
        String component;
        if ("M".equals(menu.getMenuType())) {
            // 目录类型使用布局组件
            component = "layout.base";
        } else {
            // 菜单类型使用具体的视图组件
            if (menu.getComponent() != null && !menu.getComponent().trim().isEmpty()) {
                component = menu.getComponent();
            } else {
                // 如果没有指定组件，根据路由名称生成
                // 对于单页面路由（如home），使用 layout.base$view.xxx 格式
                component = "layout.base$view." + menu.getRouteName();
            }
        }
        route.put("component", component);

        // 设置meta信息
        Map<String, Object> meta = new HashMap<>();
        meta.put("title", menu.getMenuName());
        meta.put("i18nKey", "route." + menu.getRouteName());
        meta.put("icon", convertIcon(menu.getIcon()));
        meta.put("order", menu.getOrderNum());

        // 根据菜单权限设置角色要求
        if (menu.getPerms() != null && !menu.getPerms().trim().isEmpty()) {
            // 如果菜单有权限要求，设置需要超级管理员角色
            // 注意：这里使用的角色代码需要与数据库中的角色代码一致
            meta.put("roles", List.of("SUPER_ADMIN"));  // 使用与用户角色匹配的代码
        } else {
            // 如果没有特殊权限要求，所有登录用户都可以访问
            meta.put("roles", List.of());
        }

        meta.put("hideInMenu", menu.getVisible() == 0);
        meta.put("keepAlive", menu.getIsCache() == 1);
        route.put("meta", meta);

        // 处理子菜单
        List<SysMenu> childMenus = allMenus.stream()
                .filter(child -> menu.getId().equals(child.getParentId()))
                .sorted((a, b) -> Integer.compare(a.getOrderNum(), b.getOrderNum()))
                .toList();

        if (!childMenus.isEmpty()) {
            List<Map<String, Object>> children = new ArrayList<>();
            for (SysMenu childMenu : childMenus) {
                Map<String, Object> childRoute = convertMenuToRoute(childMenu, allMenus);
                if (childRoute != null) {
                    children.add(childRoute);
                }
            }
            if (!children.isEmpty()) {
                route.put("children", children);
            }
        }

        log.debug("转换菜单路由: {} -> {}", menu.getMenuName(), route);
        return route;
    }

    /**
     * 转换图标名称
     */
    private String convertIcon(String icon) {
        if (icon == null || icon.trim().isEmpty()) {
            return "mdi:menu";
        }

        // 如果图标名称不包含前缀，添加默认前缀
        if (!icon.contains(":")) {
            // 根据图标名称映射到合适的图标库
            switch (icon.toLowerCase()) {
                case "home":
                    return "mdi:home";
                case "system":
                    return "carbon:cloud-service-management";
                case "user":
                    return "ic:round-manage-accounts";
                case "peoples":
                    return "carbon:user-role";
                case "tree-table":
                    return "material-symbols:route";
                case "tree":
                    return "carbon:tree-view";
                case "post":
                    return "carbon:user-identification";
                case "dict":
                    return "carbon:data-base";
                case "edit":
                    return "carbon:settings";
                case "monitor":
                    return "carbon:dashboard";
                case "tool":
                    return "carbon:tool-box";
                default:
                    return "mdi:" + icon;
            }
        }

        return icon;
    }

    /**
     * 获取子菜单
     */
    private List<SysMenu> getChildMenus(Long parentId) {
        // 获取当前登录用户ID
        Object loginId = StpUtil.getLoginId();
        Long userId = Long.valueOf(loginId.toString());

        // 查询用户所有菜单，然后筛选子菜单
        List<SysMenu> allMenus = sysMenuService.selectMenusByUserId(userId);
        return allMenus.stream()
                .filter(menu -> parentId.equals(menu.getParentId()))
                .toList();
    }
} 