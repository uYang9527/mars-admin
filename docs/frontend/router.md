# 路由配置

Mars Admin前端使用Vue Router 4作为路由管理库，实现了基于权限的动态路由系统。

## 路由架构

### 目录结构

```
src/router/
├── elegant/                       # 优雅路由系统
│   ├── imports.ts                # 路由导入
│   ├── routes.ts                 # 路由定义
│   └── transform.ts              # 路由转换
├── guard/                        # 路由守卫
│   ├── index.ts                  # 守卫入口
│   ├── permission.ts             # 权限守卫
│   └── dynamic.ts                # 动态路由守卫
├── routes/                       # 路由定义
│   ├── builtin.ts                # 内置路由
│   └── index.ts                  # 路由入口
└── index.ts                      # 路由配置入口
```

## 路由类型定义

### 基础路由类型

```typescript
// typings/router.d.ts
declare namespace App {
  namespace Global {
    /** 菜单项 */
    interface Menu {
      /** 菜单键值 */
      key: string
      /** 菜单标签 */
      label: string
      /** 路由名称 */
      routeName?: string
      /** 路由路径 */
      routePath?: string
      /** 图标 */
      icon?: string
      /** 子菜单 */
      children?: Menu[]
    }

    /** 面包屑项 */
    interface Breadcrumb {
      key: string
      label: string
      routeName?: string
      options?: BreadcrumbOption[]
    }

    interface BreadcrumbOption {
      key: string
      label: string
      routeName: string
    }

    /** 标签页 */
    interface Tab {
      /** 标签页id */
      id: string
      /** 标签页标签 */
      label: string
      /** 路由键值 */
      routeKey: string
      /** 路由路径 */
      routePath: string
      /** 路由完整路径 */
      fullPath: string
      /** 固定标签页 */
      pinned?: boolean
      /** 图标 */
      icon?: string
      /** 本地图标 */
      localIcon?: string
    }
  }

  /** 路由相关类型 */
  namespace Route {
    /** 路由键值 */
    type RouteKey = keyof ElegantConstRoute

    /** 路由映射 */
    type RouteMap = ElegantConstRoute

    /** 路由路径 */
    type RoutePath = RouteMap[RouteKey]

    /** 路由组件 */
    type RouteComponent = Record<string, () => Promise<{ default: any }>>
  }
}
```

### 路由元信息类型

```typescript
// typings/elegant-router.d.ts
declare namespace ElegantRouter {
  interface RouteMeta {
    /** 路由标题 */
    title: string
    /** 路由的动态路径 */
    dynamicPath?: string
    /** 作为单级路由的父级路由布局组件 */
    singleLayout?: Extract<RouteLayoutType, 'base' | 'blank'>
    /** 需要登录权限 */
    requiresAuth?: boolean
    /** 可以访问的角色 */
    roles?: string[]
    /** 可以访问该页面的权限 */
    permissions?: string[]
    /** 缓存页面 */
    keepAlive?: boolean
    /** 是否支持多个tab页签 */
    multiTab?: boolean
    /** 路由顺序，可用于菜单的排序 */
    order?: number
    /** 表示是否在菜单中隐藏 */
    hideInMenu?: boolean
    /** 菜单图标 */
    icon?: string
    /** 本地菜单图标(assets/svg-icon文件夹的图标key) */
    localIcon?: string
    /** 使用外链 */
    href?: string
    /** 是否支持多标签页 */
    multiTab?: boolean
    /** 当前激活的菜单key */
    activeMenu?: import('@elegant-router/types').RouteKey
    /** 固定在tab卡不可关闭 */
    fixedIndexInTab?: number
  }
}
```

## 路由配置

### 1. 基础路由配置

```typescript
// src/router/routes/builtin.ts
import type { RouteRecordRaw } from 'vue-router'

/** 根路由：重定向到登录页 */
export const ROOT_ROUTE: RouteRecordRaw = {
  path: '/',
  name: 'root',
  redirect: '/home',
  meta: {
    title: 'root'
  }
}

/** 登录路由 */
export const LOGIN_ROUTE: RouteRecordRaw = {
  path: '/login',
  name: 'login',
  component: () => import('@/views/_builtin/login/index.vue'),
  meta: {
    title: '登录',
    requiresAuth: false,
    singleLayout: 'blank'
  }
}

/** 固定路由 */
export const STATIC_ROUTES: RouteRecordRaw[] = [
  ROOT_ROUTE,
  LOGIN_ROUTE,
  {
    path: '/403',
    name: '403',
    component: () => import('@/views/_builtin/403/index.vue'),
    meta: {
      title: '无权限',
      requiresAuth: false,
      singleLayout: 'blank'
    }
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/_builtin/404/index.vue'),
    meta: {
      title: '页面不存在',
      requiresAuth: false,
      singleLayout: 'blank'
    }
  },
  {
    path: '/500',
    name: '500',
    component: () => import('@/views/_builtin/500/index.vue'),
    meta: {
      title: '服务器错误',
      requiresAuth: false,
      singleLayout: 'blank'
    }
  }
]

/** 捕获所有路由 */
export const NOT_FOUND_ROUTE: RouteRecordRaw = {
  path: '/:pathMatch(.*)*',
  name: 'not-found',
  redirect: '/404'
}
```

### 2. 动态路由配置

```typescript
// src/router/elegant/routes.ts
import type { ElegantConstRoute } from '@elegant-router/types'

export const generatedRoutes: ElegantConstRoute[] = [
  {
    name: 'home',
    path: '/home',
    component: 'layout.base$view.home',
    meta: {
      title: '首页',
      requiresAuth: true,
      icon: 'mdi:monitor-dashboard',
      order: 1
    }
  },
  {
    name: 'manage',
    path: '/manage',
    component: 'layout.base',
    meta: {
      title: '系统管理',
      requiresAuth: true,
      icon: 'carbon:cloud-service-management',
      order: 9
    },
    children: [
      {
        name: 'manage_user',
        path: '/manage/user',
        component: 'view.manage_user',
        meta: {
          title: '用户管理',
          requiresAuth: true,
          permissions: ['system:user:list'],
          icon: 'ic:round-manage-accounts'
        }
      },
      {
        name: 'manage_role',
        path: '/manage/role',
        component: 'view.manage_role',
        meta: {
          title: '角色管理',
          requiresAuth: true,
          permissions: ['system:role:list'],
          icon: 'carbon:user-role'
        }
      },
      {
        name: 'manage_menu',
        path: '/manage/menu',
        component: 'view.manage_menu',
        meta: {
          title: '菜单管理',
          requiresAuth: true,
          permissions: ['system:menu:list'],
          icon: 'material-symbols:route'
        }
      }
    ]
  }
]
```

### 3. 路由组件映射

```typescript
// src/router/elegant/imports.ts
import type { RouteComponent } from '@elegant-router/types'

export const layouts: RouteComponent = {
  base: () => import('@/layouts/base-layout/index.vue'),
  blank: () => import('@/layouts/blank-layout/index.vue')
}

export const views: RouteComponent = {
  home: () => import('@/views/home/index.vue'),
  manage_user: () => import('@/views/manage/user/index.vue'),
  manage_role: () => import('@/views/manage/role/index.vue'),
  manage_menu: () => import('@/views/manage/menu/index.vue'),
  manage_dept: () => import('@/views/manage/dept/index.vue'),
  manage_post: () => import('@/views/manage/post/index.vue'),
  manage_dict: () => import('@/views/manage/dict/index.vue'),
  manage_config: () => import('@/views/manage/config/index.vue'),
  manage_file: () => import('@/views/manage/file-manage/index.vue'),
  monitor_operlog: () => import('@/views/manage/operation-log/index.vue'),
  monitor_loginlog: () => import('@/views/manage/login-log/index.vue'),
  monitor_server: () => import('@/views/manage/system-monitor/index.vue'),
  monitor_cache: () => import('@/views/manage/cache-monitor/index.vue')
}
```

## 路由守卫

### 1. 权限守卫

```typescript
// src/router/guard/permission.ts
import type { Router } from 'vue-router'
import { useAuthStore } from '@/store/modules/auth'
import { useRouteStore } from '@/store/modules/route'

/** 权限守卫 */
export function createPermissionGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore()
    const routeStore = useRouteStore()

    // 1. 检查是否需要登录
    if (to.meta.requiresAuth) {
      if (!authStore.isLogin) {
        // 未登录，跳转到登录页
        const redirect = to.fullPath
        next({ path: '/login', query: { redirect } })
        return
      }

      // 2. 初始化用户信息
      const hasUserInfo = await authStore.initUserInfo()
      if (!hasUserInfo) {
        next({ path: '/login' })
        return
      }

      // 3. 初始化权限路由
      if (!routeStore.isInitAuthRoute) {
        await routeStore.initAuthRoute()
        // 重新导航到目标路由
        next({ ...to, replace: true })
        return
      }

      // 4. 检查路由权限
      if (to.meta.permissions) {
        const hasPermission = authStore.hasAnyPermission(to.meta.permissions)
        if (!hasPermission) {
          next({ path: '/403' })
          return
        }
      }

      // 5. 检查角色权限
      if (to.meta.roles) {
        const hasRole = to.meta.roles.some(role => authStore.hasRole(role))
        if (!hasRole) {
          next({ path: '/403' })
          return
        }
      }
    }

    next()
  })
}
```

### 2. 动态路由守卫

```typescript
// src/router/guard/dynamic.ts
import type { Router } from 'vue-router'
import { useRouteStore } from '@/store/modules/route'

/** 动态路由守卫 */
export function createDynamicRouteGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    const routeStore = useRouteStore()

    // 如果路由不存在，尝试从动态路由中查找
    if (to.matched.length === 0) {
      const dynamicRoute = routeStore.getDynamicRoute(to.path)
      if (dynamicRoute) {
        // 添加动态路由
        router.addRoute(dynamicRoute)
        next({ ...to, replace: true })
        return
      }
    }

    next()
  })
}
```

### 3. 标签页守卫

```typescript
// src/router/guard/tab.ts
import type { Router } from 'vue-router'
import { useTabStore } from '@/store/modules/tab'

/** 标签页守卫 */
export function createTabGuard(router: Router) {
  router.afterEach((to) => {
    const tabStore = useTabStore()

    // 添加标签页
    if (to.meta.title && !to.meta.hideInMenu) {
      tabStore.addTab({
        id: to.name as string,
        label: to.meta.title,
        routeKey: to.name as string,
        routePath: to.path,
        fullPath: to.fullPath,
        icon: to.meta.icon,
        localIcon: to.meta.localIcon
      })
    }

    // 设置当前激活标签页
    tabStore.setActiveTab(to.name as string)
  })
}
```

## 路由工具函数

### 1. 路由转换工具

```typescript
// src/utils/router.ts
import type { RouteRecordRaw } from 'vue-router'
import { layouts, views } from '@/router/elegant/imports'

/** 转换路由组件 */
export function transformRouteComponent(component: string) {
  const [layout, view] = component.split('$')
  
  if (view) {
    // 布局组件 + 视图组件
    return {
      layout: layouts[layout.replace('layout.', '')],
      view: views[view.replace('view.', '')]
    }
  } else {
    // 只有布局组件
    return {
      layout: layouts[layout.replace('layout.', '')]
    }
  }
}

/** 转换权限路由为Vue路由 */
export function transformAuthRouteToVueRoutes(routes: App.Global.Menu[]): RouteRecordRaw[] {
  return routes.map(route => transformSingleRoute(route))
}

/** 转换单个路由 */
function transformSingleRoute(route: App.Global.Menu): RouteRecordRaw {
  const { component } = transformRouteComponent(route.component)
  
  const vueRoute: RouteRecordRaw = {
    path: route.path,
    name: route.name,
    component,
    meta: {
      title: route.meta.title,
      requiresAuth: route.meta.requiresAuth,
      permissions: route.meta.permissions,
      roles: route.meta.roles,
      keepAlive: route.meta.keepAlive,
      icon: route.meta.icon,
      localIcon: route.meta.localIcon,
      hideInMenu: route.meta.hideInMenu,
      order: route.meta.order,
      dynamic: true // 标记为动态路由
    }
  }

  // 处理子路由
  if (route.children && route.children.length > 0) {
    vueRoute.children = route.children.map(child => transformSingleRoute(child))
  }

  return vueRoute
}

/** 转换路由为菜单 */
export function transformRouteToMenu(routes: App.Global.Menu[]): App.Global.Menu[] {
  return routes
    .filter(route => !route.meta.hideInMenu)
    .map(route => ({
      key: route.name,
      label: route.meta.title,
      routeName: route.name,
      routePath: route.path,
      icon: route.meta.icon,
      localIcon: route.meta.localIcon,
      children: route.children ? transformRouteToMenu(route.children) : undefined
    }))
    .sort((a, b) => (a.order || 0) - (b.order || 0))
}
```

### 2. 路由导航工具

```typescript
// src/hooks/common/use-router.ts
import { useRouter as useVueRouter, useRoute } from 'vue-router'
import type { RouteLocationRaw } from 'vue-router'

export function useRouterPush() {
  const router = useVueRouter()
  const route = useRoute()

  /** 路由跳转 */
  const routerPush = (to: RouteLocationRaw) => {
    return router.push(to)
  }

  /** 路由替换 */
  const routerReplace = (to: RouteLocationRaw) => {
    return router.replace(to)
  }

  /** 路由返回 */
  const routerBack = () => {
    router.back()
  }

  /** 路由前进 */
  const routerForward = () => {
    router.forward()
  }

  /** 跳转到首页 */
  const toHome = () => {
    routerPush({ name: 'home' })
  }

  /** 跳转到登录页 */
  const toLogin = (redirect?: string) => {
    const query = redirect ? { redirect } : undefined
    routerPush({ name: 'login', query })
  }

  /** 跳转到上一页或指定页面 */
  const toLastPage = (fallback: RouteLocationRaw = { name: 'home' }) => {
    if (history.length > 1) {
      routerBack()
    } else {
      routerPush(fallback)
    }
  }

  return {
    route,
    router,
    routerPush,
    routerReplace,
    routerBack,
    routerForward,
    toHome,
    toLogin,
    toLastPage
  }
}
```

## 路由懒加载

### 1. 组件懒加载

```typescript
// 基础懒加载
const Home = () => import('@/views/home/index.vue')

// 带加载状态的懒加载
const AsyncComponent = defineAsyncComponent({
  loader: () => import('@/views/async-component/index.vue'),
  loadingComponent: () => import('@/components/common/loading.vue'),
  errorComponent: () => import('@/components/common/error.vue'),
  delay: 200,
  timeout: 3000
})

// 分组懒加载
const UserManage = () => import(
  /* webpackChunkName: "manage" */ 
  '@/views/manage/user/index.vue'
)
```

### 2. 路由级别的代码分割

```typescript
// vite.config.ts
export default defineConfig({
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          // 将管理相关页面打包到一个chunk
          'manage': [
            'src/views/manage/user/index.vue',
            'src/views/manage/role/index.vue',
            'src/views/manage/menu/index.vue'
          ],
          // 将监控相关页面打包到一个chunk
          'monitor': [
            'src/views/manage/system-monitor/index.vue',
            'src/views/manage/cache-monitor/index.vue'
          ]
        }
      }
    }
  }
})
```

## 路由缓存

### 1. 页面缓存配置

```vue
<!-- src/layouts/base-layout/index.vue -->
<template>
  <div class="layout">
    <router-view v-slot="{ Component, route }">
      <transition name="fade" mode="out-in">
        <keep-alive :include="cacheRoutes">
          <component :is="Component" :key="route.fullPath" />
        </keep-alive>
      </transition>
    </router-view>
  </div>
</template>

<script setup lang="ts">
import { useRouteStore } from '@/store/modules/route'

const routeStore = useRouteStore()
const { cacheRoutes } = storeToRefs(routeStore)
</script>
```

### 2. 动态缓存控制

```typescript
// src/hooks/business/use-route-cache.ts
import { useRouteStore } from '@/store/modules/route'

export function useRouteCache() {
  const routeStore = useRouteStore()

  /** 添加缓存路由 */
  const addCacheRoute = (routeName: string) => {
    routeStore.addCacheRoute(routeName)
  }

  /** 移除缓存路由 */
  const removeCacheRoute = (routeName: string) => {
    routeStore.removeCacheRoute(routeName)
  }

  /** 清除所有缓存 */
  const clearAllCache = () => {
    routeStore.clearCacheRoutes()
  }

  /** 刷新当前页面缓存 */
  const refreshCurrentCache = () => {
    const route = useRoute()
    if (route.name) {
      removeCacheRoute(route.name as string)
      nextTick(() => {
        addCacheRoute(route.name as string)
      })
    }
  }

  return {
    addCacheRoute,
    removeCacheRoute,
    clearAllCache,
    refreshCurrentCache
  }
}
```

## 路由动画

### 1. 页面切换动画

```vue
<!-- src/layouts/base-layout/index.vue -->
<template>
  <router-view v-slot="{ Component, route }">
    <transition 
      :name="getTransitionName(route)"
      mode="out-in"
      @before-enter="onBeforeEnter"
      @after-enter="onAfterEnter"
    >
      <component :is="Component" :key="route.fullPath" />
    </transition>
  </router-view>
</template>

<script setup lang="ts">
import type { RouteLocationNormalized } from 'vue-router'

const getTransitionName = (route: RouteLocationNormalized) => {
  // 根据路由层级决定动画类型
  const depth = route.path.split('/').length
  return depth > 2 ? 'slide-left' : 'fade'
}

const onBeforeEnter = () => {
  // 页面进入前的处理
  document.body.style.overflow = 'hidden'
}

const onAfterEnter = () => {
  // 页面进入后的处理
  document.body.style.overflow = 'auto'
}
</script>

<style scoped>
/* 淡入淡出动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 滑动动画 */
.slide-left-enter-active,
.slide-left-leave-active {
  transition: transform 0.3s ease;
}

.slide-left-enter-from {
  transform: translateX(100%);
}

.slide-left-leave-to {
  transform: translateX(-100%);
}
</style>
```

## 最佳实践

### 1. 路由设计原则
- **RESTful风格**：遵循RESTful API设计原则
- **语义化路径**：路由路径要有明确的语义
- **层级清晰**：保持合理的路由层级结构
- **参数规范**：统一路由参数的命名和格式

### 2. 性能优化
- **懒加载**：使用动态导入实现路由懒加载
- **代码分割**：合理分割路由组件到不同chunk
- **缓存策略**：合理使用keep-alive缓存页面
- **预加载**：预加载可能访问的路由组件

### 3. 安全考虑
- **权限验证**：在路由守卫中进行权限验证
- **参数验证**：验证路由参数的合法性
- **防止越权**：确保用户只能访问有权限的路由
- **敏感信息**：避免在路由中暴露敏感信息

## 常见问题

### Q: 如何实现路由级别的权限控制？

**解决方案**：
```typescript
// 在路由meta中配置权限
{
  path: '/user',
  meta: {
    permissions: ['system:user:list'],
    roles: ['admin', 'user']
  }
}

// 在路由守卫中验证权限
router.beforeEach((to, from, next) => {
  if (to.meta.permissions) {
    const hasPermission = checkPermissions(to.meta.permissions)
    if (!hasPermission) {
      next('/403')
      return
    }
  }
  next()
})
```

### Q: 如何处理动态路由参数？

**解决方案**：
```typescript
// 定义动态路由
{
  path: '/user/:id',
  name: 'user-detail',
  component: UserDetail,
  props: true // 将路由参数作为props传递给组件
}

// 在组件中接收参数
const props = defineProps<{
  id: string
}>()

// 或者使用useRoute获取参数
const route = useRoute()
const userId = computed(() => route.params.id as string)
```

### Q: 如何实现路由的前进后退动画？

**解决方案**：
```typescript
// 记录路由历史
const routeHistory = ref<string[]>([])

router.beforeEach((to, from) => {
  const toIndex = routeHistory.value.indexOf(to.path)
  const fromIndex = routeHistory.value.indexOf(from.path)
  
  if (toIndex > fromIndex) {
    // 前进
    to.meta.transition = 'slide-left'
  } else {
    // 后退
    to.meta.transition = 'slide-right'
  }
})
```

### Q: 如何处理路由懒加载失败？

**解决方案**：
```typescript
const AsyncComponent = defineAsyncComponent({
  loader: () => import('@/views/component.vue'),
  errorComponent: () => import('@/components/error.vue'),
  timeout: 3000,
  onError(error, retry, fail, attempts) {
    if (attempts <= 3) {
      // 重试3次
      retry()
    } else {
      // 失败处理
      fail()
    }
  }
})
```
