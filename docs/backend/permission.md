# 权限系统

Mars Admin采用基于RBAC（Role-Based Access Control，基于角色的访问控制）的权限管理系统，提供灵活、精细的权限控制。

## RBAC模型

### 核心概念

Mars Admin的RBAC模型包含以下核心元素：

- **用户（User）**：系统的操作者，可以是管理员、普通用户等
- **角色（Role）**：权限的集合，用户通过被分配角色间接获得权限
- **菜单（Menu）**：系统功能的导航结构，包含页面和按钮
- **权限（Permission）**：对特定资源的操作权限，如查看、新增、修改、删除等
- **部门（Department）**：组织结构单元，用于数据权限控制

### 关系图

```
+-------+     +-------+     +--------+     +------------+
| 用户  | --> | 角色  | --> | 菜单   | --> | 权限       |
+-------+     +-------+     +--------+     +------------+
                 |
                 v
            +----------+
            | 数据权限 |
            +----------+
```

## 权限分类

Mars Admin的权限系统分为两大类：

### 1. 功能权限

控制用户可以访问哪些功能模块、页面和按钮。

- **菜单权限**：控制用户可见的菜单项
- **按钮权限**：控制用户可操作的按钮（如新增、修改、删除等）
- **API权限**：控制用户可调用的后端接口

### 2. 数据权限

控制用户可以访问哪些数据范围。

- **全部数据**：可以查看所有数据
- **本部门数据**：只能查看本部门的数据
- **本部门及下级部门数据**：可查看本部门及其下级部门的数据
- **自定义数据**：根据特定规则过滤数据
- **仅本人数据**：只能查看与自己相关的数据

## 数据库设计

### 核心表结构

Mars Admin的权限系统涉及以下核心表：

1. **sys_user**：用户表
   - user_id：用户ID
   - username：用户名
   - password：密码
   - dept_id：部门ID
   - status：状态

2. **sys_role**：角色表
   - role_id：角色ID
   - role_name：角色名称
   - role_key：角色标识
   - data_scope：数据范围

3. **sys_menu**：菜单表
   - menu_id：菜单ID
   - menu_name：菜单名称
   - parent_id：父菜单ID
   - path：路由地址
   - component：组件路径
   - perms：权限标识
   - menu_type：菜单类型（目录、菜单、按钮）

4. **sys_dept**：部门表
   - dept_id：部门ID
   - parent_id：父部门ID
   - dept_name：部门名称
   - ancestors：祖级列表
   - order_num：显示顺序

5. **sys_user_role**：用户角色关联表
   - user_id：用户ID
   - role_id：角色ID

6. **sys_role_menu**：角色菜单关联表
   - role_id：角色ID
   - menu_id：菜单ID

7. **sys_role_dept**：角色部门关联表（数据权限）
   - role_id：角色ID
   - dept_id：部门ID

### ER图

```
+-----------+      +------------+      +-----------+
| sys_user  |------| sys_user_  |------| sys_role  |
+-----------+      | role       |      +-----------+
                   +------------+           |
                                            |
                                     +------------+
                                     | sys_role_  |
                                     | menu       |
                                     +------------+
                                            |
+-----------+      +------------+      +-----------+
| sys_dept  |------| sys_role_  |------| sys_menu  |
+-----------+      | dept       |      +-----------+
                   +------------+
```

## 权限实现

### 后端实现

Mars Admin使用Sa-Token框架实现权限控制：

#### 1. 认证流程

```java
// 登录认证
@PostMapping("/login")
public R<LoginVO> login(@RequestBody @Valid LoginDTO loginDTO) {
    // 验证用户名密码
    String token = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
    
    // 返回token和用户信息
    return R.ok(new LoginVO(token, userInfo));
}
```

#### 2. 权限注解

```java
// 需要指定权限才能访问
@SaCheckPermission("system:user:list")
@GetMapping("/list")
public R<PageResult<SysUserVO>> list(SysUserQuery query) {
    return R.ok(userService.page(query));
}

// 需要指定角色才能访问
@SaCheckRole("admin")
@PostMapping
public R<Void> add(@RequestBody @Valid SysUserDTO userDTO) {
    userService.add(userDTO);
    return R.ok();
}
```

#### 3. 全局拦截器

```java
@Configuration
public class SaTokenConfig {
    // 注册Sa-Token全局过滤器
    @Bean
    public SaServletFilter saServletFilter() {
        return new SaServletFilter()
            .addInclude("/**")
            .addExclude("/auth/login", "/captcha", "/doc.html", "/webjars/**", "/swagger-resources")
            .setAuth(obj -> {
                // 检查是否登录
                StpUtil.checkLogin();
                
                // 获取当前请求路径
                String path = SaHolder.getRequest().getRequestPath();
                
                // 检查路径权限
                SaRouter.match("/**", () -> {
                    // 权限校验
                    StpUtil.checkPermissionOr(permissionList);
                });
            });
    }
}
```

#### 4. 数据权限实现

```java
@Component
public class DataScopeInterceptor implements InnerInterceptor {
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        // 获取当前用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        
        // 如果是超级管理员，不进行数据过滤
        if (loginUser.isAdmin()) {
            return;
        }
        
        // 获取角色的数据权限范围
        String dataScope = loginUser.getRole().getDataScope();
        
        // 构建数据权限SQL
        String dataScopeSql = buildDataScopeSql(dataScope, loginUser);
        
        // 修改原SQL，添加数据权限条件
        String originalSql = boundSql.getSql();
        String newSql = originalSql + " AND (" + dataScopeSql + ")";
        
        // 替换原SQL
        PluginUtils.mpBoundSql(boundSql).sql(newSql);
    }
    
    private String buildDataScopeSql(String dataScope, LoginUser loginUser) {
        // 根据不同的数据范围构建SQL条件
        switch (dataScope) {
            case "1": // 全部数据
                return "1=1";
            case "2": // 本部门数据
                return "dept_id = " + loginUser.getDeptId();
            case "3": // 本部门及下级部门
                return "dept_id IN (SELECT dept_id FROM sys_dept WHERE ancestors LIKE '%" + loginUser.getDeptId() + "%')";
            case "4": // 仅本人数据
                return "create_by = '" + loginUser.getUsername() + "'";
            case "5": // 自定义数据
                return buildCustomDataScope(loginUser.getRoleId());
            default:
                return "1=0"; // 默认无权限
        }
    }
}
```

### 前端实现

前端权限控制主要通过以下几个方面实现：

#### 1. 路由权限控制

```typescript
// src/router/guard/permission.ts
import { useAuthStore } from '@/store/modules/auth';

export function createPermissionGuard(router) {
  router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore();
    
    // 白名单路由直接放行
    if (authStore.isWhiteList(to)) {
      next();
      return;
    }
    
    // 未登录跳转到登录页
    if (!authStore.isLogin) {
      next({ name: 'login', query: { redirect: to.fullPath } });
      return;
    }
    
    // 已经加载过权限路由
    if (authStore.isInitAuthRoute) {
      next();
      return;
    }
    
    // 获取权限路由
    await authStore.initAuthRoute();
    
    // 动态添加路由后，需要重新导航
    if (to.name === 'not-found') {
      const ROOT_ROUTE = '/';
      const path = to.redirectedFrom?.fullPath || ROOT_ROUTE;
      next({ path, replace: true });
      return;
    }
    
    next();
  });
}
```

#### 2. 菜单权限控制

```vue
<!-- src/layouts/modules/global-menu/index.vue -->
<template>
  <div class="global-menu">
    <template v-for="menu in menus" :key="menu.path">
      <!-- 有子菜单且有权限显示 -->
      <a-sub-menu v-if="menu.children && menu.children.length > 0 && hasPermission(menu)" :key="menu.path">
        <template #title>
          <icon-font :type="menu.icon" />
          <span>{{ menu.title }}</span>
        </template>
        <template v-for="child in menu.children" :key="child.path">
          <menu-item v-if="hasPermission(child)" :menu="child" />
        </template>
      </a-sub-menu>
      <!-- 无子菜单且有权限显示 -->
      <menu-item v-else-if="hasPermission(menu)" :menu="menu" />
    </template>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth';

const authStore = useAuthStore();

// 判断是否有菜单权限
function hasPermission(menu) {
  // 没有设置权限，则所有人都可访问
  if (!menu.meta?.permissions || menu.meta.permissions.length === 0) {
    return true;
  }
  
  // 检查当前用户是否拥有菜单所需权限
  return authStore.hasPermission(menu.meta.permissions);
}
</script>
```

#### 3. 按钮权限控制

```vue
<!-- 自定义权限指令 -->
<template>
  <div>
    <a-button v-permission="'system:user:add'" type="primary">新增用户</a-button>
    <a-button v-permission="'system:user:edit'" type="primary">编辑用户</a-button>
    <a-button v-permission="'system:user:delete'" type="primary" danger>删除用户</a-button>
  </div>
</template>

<script setup>
import { usePermission } from '@/hooks/business/permission';

// 注册权限指令
const { register } = usePermission();
register();
</script>
```

```typescript
// src/hooks/business/permission.ts
import { useAuthStore } from '@/store/modules/auth';
import type { Directive, DirectiveBinding } from 'vue';

export function usePermission() {
  const authStore = useAuthStore();
  
  // 判断是否有权限
  function hasPermission(permission: string | string[]): boolean {
    return authStore.hasPermission(permission);
  }
  
  // 注册权限指令
  function register() {
    const permissionDirective: Directive = {
      mounted(el: HTMLElement, binding: DirectiveBinding) {
        const { value } = binding;
        const hasAuth = hasPermission(value);
        
        if (!hasAuth) {
          el.parentNode?.removeChild(el);
        }
      }
    };
    
    // 注册全局指令
    app.directive('permission', permissionDirective);
  }
  
  return {
    hasPermission,
    register
  };
}
```

## 权限管理功能

Mars Admin提供完整的权限管理功能，包括：

### 1. 用户管理

- 用户列表查询
- 新增/编辑/删除用户
- 分配用户角色
- 重置密码
- 启用/禁用用户

### 2. 角色管理

- 角色列表查询
- 新增/编辑/删除角色
- 分配菜单权限
- 分配数据权限
- 分配用户

### 3. 菜单管理

- 菜单树形展示
- 新增/编辑/删除菜单
- 配置菜单属性（图标、排序、权限标识等）

### 4. 部门管理

- 部门树形展示
- 新增/编辑/删除部门
- 设置部门排序

## 最佳实践

### 权限设计原则

1. **最小权限原则**：用户只应拥有完成其工作所需的最小权限集合
2. **职责分离**：关键操作应由不同角色共同完成，避免权力过度集中
3. **易于理解**：权限设计应简单明了，便于管理和审计

### 权限命名规范

权限标识采用以下格式：`模块:对象:操作`

例如：
- `system:user:list` - 系统模块下用户对象的列表查询操作
- `system:role:add` - 系统模块下角色对象的新增操作
- `system:menu:delete` - 系统模块下菜单对象的删除操作

### 常见问题

1. **权限不生效**
   - 检查角色是否正确分配了菜单权限
   - 检查菜单是否正确配置了权限标识
   - 检查代码中的权限注解是否与数据库中的权限标识一致

2. **数据权限异常**
   - 检查角色的数据权限范围设置
   - 检查SQL拦截器是否正常工作
   - 检查部门层级关系是否正确

3. **权限缓存问题**
   - 修改权限后需要清除相关缓存
   - 用户重新登录后才能获取最新权限 