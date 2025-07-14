# 状态管理

Mars Admin前端使用Pinia作为状态管理库，提供了响应式的全局状态管理解决方案。

## Pinia简介

Pinia是Vue.js的官方状态管理库，具有以下特点：

- **类型安全**：完整的TypeScript支持
- **开发工具**：优秀的Vue DevTools支持
- **模块化**：支持多个store模块
- **轻量级**：相比Vuex更加轻量
- **组合式API**：原生支持Composition API

## 状态管理架构

### 目录结构

```
src/store/
├── modules/                    # 状态模块
│   ├── app/                   # 应用状态模块
│   │   ├── index.ts           # 应用状态定义
│   │   └── shared.ts          # 应用共享状态
│   ├── auth/                  # 认证状态模块
│   │   ├── index.ts           # 认证状态定义
│   │   └── shared.ts          # 认证共享状态
│   ├── route/                 # 路由状态模块
│   │   ├── index.ts           # 路由状态定义
│   │   └── shared.ts          # 路由共享状态
│   └── theme/                 # 主题状态模块
│       ├── index.ts           # 主题状态定义
│       └── shared.ts          # 主题共享状态
├── plugins/                   # 状态插件
│   └── persist.ts             # 持久化插件
└── index.ts                   # 状态入口文件
```

## 核心状态模块

### 1. 应用状态 (App Store)

应用状态管理全局应用配置和状态。

```typescript
// src/store/modules/app/index.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useLoading } from '@sa/hooks'
import { localStg } from '@/utils/storage'
import { $t } from '@/locales'

export const useAppStore = defineStore('app', () => {
  const scope = 'app-store'

  const { loading: reloadLoading, startLoading, endLoading } = useLoading()

  /** 重新加载页面的loading */
  const reloadFlag = ref(true)

  /** 应用信息 */
  const appInfo = ref({
    name: 'Mars Admin',
    version: '1.0.0',
    description: '基于Vue3、TypeScript、Vite构建的现代化管理系统'
  })

  /** 侧边栏折叠状态 */
  const siderCollapse = ref(false)

  /** 混合布局的侧边栏抽屉可见状态 */
  const mixSiderFixed = ref(false)

  /** 移动端侧边栏可见状态 */
  const mobileSiderVisible = ref(false)

  /** 内容区域全屏状态 */
  const contentFull = ref(false)

  /** 页面是否加载中 */
  const pageLoading = ref(false)

  /** 计算属性：是否为移动端 */
  const isMobile = computed(() => {
    const { width } = useWindowSize()
    return width.value < 768
  })

  /** 重新加载页面 */
  async function reloadPage(duration = 0) {
    startLoading()
    
    reloadFlag.value = false
    
    await nextTick()
    
    if (duration > 0) {
      setTimeout(() => {
        reloadFlag.value = true
        endLoading()
      }, duration)
    } else {
      reloadFlag.value = true
      endLoading()
    }
  }

  /** 切换侧边栏折叠状态 */
  function toggleSiderCollapse() {
    siderCollapse.value = !siderCollapse.value
  }

  /** 设置侧边栏折叠状态 */
  function setSiderCollapse(collapse: boolean) {
    siderCollapse.value = collapse
  }

  /** 切换混合布局侧边栏固定状态 */
  function toggleMixSiderFixed() {
    mixSiderFixed.value = !mixSiderFixed.value
  }

  /** 设置混合布局侧边栏固定状态 */
  function setMixSiderFixed(fixed: boolean) {
    mixSiderFixed.value = fixed
  }

  /** 切换移动端侧边栏可见状态 */
  function toggleMobileSiderVisible() {
    mobileSiderVisible.value = !mobileSiderVisible.value
  }

  /** 设置移动端侧边栏可见状态 */
  function setMobileSiderVisible(visible: boolean) {
    mobileSiderVisible.value = visible
  }

  /** 切换内容区域全屏状态 */
  function toggleContentFull() {
    contentFull.value = !contentFull.value
  }

  /** 设置内容区域全屏状态 */
  function setContentFull(full: boolean) {
    contentFull.value = full
  }

  /** 设置页面加载状态 */
  function setPageLoading(loading: boolean) {
    pageLoading.value = loading
  }

  return {
    // 状态
    reloadFlag,
    reloadLoading,
    appInfo,
    siderCollapse,
    mixSiderFixed,
    mobileSiderVisible,
    contentFull,
    pageLoading,
    
    // 计算属性
    isMobile,
    
    // 方法
    reloadPage,
    toggleSiderCollapse,
    setSiderCollapse,
    toggleMixSiderFixed,
    setMixSiderFixed,
    toggleMobileSiderVisible,
    setMobileSiderVisible,
    toggleContentFull,
    setContentFull,
    setPageLoading
  }
})
```

### 2. 认证状态 (Auth Store)

认证状态管理用户登录、权限等信息。

```typescript
// src/store/modules/auth/index.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { localStg } from '@/utils/storage'
import { fetchLogin, fetchUserInfo } from '@/service/api/auth'
import { useRouteStore } from '../route'
import { clearAuthStorage, getToken } from './shared'

export const useAuthStore = defineStore('auth', () => {
  const router = useRouter()
  const routeStore = useRouteStore()

  /** 用户token */
  const token = ref(getToken())

  /** 用户信息 */
  const userInfo = ref<Api.Auth.UserInfo | null>(null)

  /** 是否已登录 */
  const isLogin = computed(() => Boolean(token.value))

  /** 用户角色列表 */
  const userRoles = computed(() => userInfo.value?.roles || [])

  /** 用户权限列表 */
  const userPermissions = computed(() => userInfo.value?.permissions || [])

  /** 是否为超级管理员 */
  const isSuper = computed(() => userRoles.value.includes('admin'))

  /** 登录 */
  async function login(username: string, password: string) {
    const { data } = await fetchLogin(username, password)
    
    if (data) {
      await loginByToken(data.token)
      return true
    }
    
    return false
  }

  /** 通过token登录 */
  async function loginByToken(loginToken: string) {
    // 1. 存储token
    localStg.set('token', loginToken)
    token.value = loginToken

    // 2. 获取用户信息
    const { data: info } = await fetchUserInfo()
    
    if (info) {
      // 3. 存储用户信息
      localStg.set('userInfo', info)
      userInfo.value = info

      // 4. 初始化路由
      await routeStore.initAuthRoute()

      // 5. 跳转到首页
      await router.push('/')
      
      return true
    }

    return false
  }

  /** 退出登录 */
  async function logout() {
    // 1. 清除认证信息
    clearAuthStorage()
    token.value = ''
    userInfo.value = null

    // 2. 重置路由
    await routeStore.resetAuthRoute()

    // 3. 跳转到登录页
    await router.push('/login')
  }

  /** 初始化用户信息 */
  async function initUserInfo() {
    const storedUserInfo = localStg.get('userInfo')
    
    if (storedUserInfo) {
      userInfo.value = storedUserInfo
      return true
    }

    if (token.value) {
      try {
        const { data } = await fetchUserInfo()
        if (data) {
          localStg.set('userInfo', data)
          userInfo.value = data
          return true
        }
      } catch (error) {
        // token无效，清除认证信息
        await logout()
      }
    }

    return false
  }

  /** 检查权限 */
  function hasPermission(permission: string) {
    if (isSuper.value) return true
    return userPermissions.value.includes(permission)
  }

  /** 检查角色 */
  function hasRole(role: string) {
    if (isSuper.value) return true
    return userRoles.value.includes(role)
  }

  /** 检查多个权限（或关系） */
  function hasAnyPermission(permissions: string[]) {
    if (isSuper.value) return true
    return permissions.some(permission => userPermissions.value.includes(permission))
  }

  /** 检查多个权限（且关系） */
  function hasAllPermissions(permissions: string[]) {
    if (isSuper.value) return true
    return permissions.every(permission => userPermissions.value.includes(permission))
  }

  return {
    // 状态
    token,
    userInfo,
    
    // 计算属性
    isLogin,
    userRoles,
    userPermissions,
    isSuper,
    
    // 方法
    login,
    loginByToken,
    logout,
    initUserInfo,
    hasPermission,
    hasRole,
    hasAnyPermission,
    hasAllPermissions
  }
})
```

### 3. 路由状态 (Route Store)

路由状态管理动态路由和菜单。

```typescript
// src/store/modules/route/index.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import { router } from '@/router'
import { fetchUserRoutes } from '@/service/api/route'
import { transformAuthRouteToVueRoutes, transformRouteToMenu } from '@/utils/router'

export const useRouteStore = defineStore('route', () => {
  /** 是否已初始化权限路由 */
  const isInitAuthRoute = ref(false)

  /** 菜单数据 */
  const menus = ref<App.Global.Menu[]>([])

  /** 搜索的菜单 */
  const searchMenus = computed(() => {
    return getSearchMenus(menus.value)
  })

  /** 缓存的路由名称 */
  const cacheRoutes = ref<string[]>([])

  /** 初始化权限路由 */
  async function initAuthRoute() {
    if (isInitAuthRoute.value) return

    try {
      // 1. 获取用户路由数据
      const { data } = await fetchUserRoutes()
      
      if (data) {
        // 2. 转换为Vue路由
        const vueRoutes = transformAuthRouteToVueRoutes(data)
        
        // 3. 添加路由到router
        vueRoutes.forEach(route => {
          router.addRoute(route)
        })

        // 4. 生成菜单数据
        menus.value = transformRouteToMenu(data)

        // 5. 更新缓存路由
        updateCacheRoutes(vueRoutes)

        isInitAuthRoute.value = true
      }
    } catch (error) {
      console.error('初始化权限路由失败:', error)
    }
  }

  /** 重置权限路由 */
  async function resetAuthRoute() {
    // 1. 清除动态路由
    const routes = router.getRoutes()
    routes.forEach(route => {
      if (route.meta?.dynamic) {
        router.removeRoute(route.name!)
      }
    })

    // 2. 重置状态
    isInitAuthRoute.value = false
    menus.value = []
    cacheRoutes.value = []
  }

  /** 更新缓存路由 */
  function updateCacheRoutes(routes: RouteRecordRaw[]) {
    const cacheNames: string[] = []
    
    function getCacheNames(routeList: RouteRecordRaw[]) {
      routeList.forEach(route => {
        if (route.meta?.keepAlive && route.name) {
          cacheNames.push(route.name as string)
        }
        if (route.children) {
          getCacheNames(route.children)
        }
      })
    }
    
    getCacheNames(routes)
    cacheRoutes.value = cacheNames
  }

  /** 添加缓存路由 */
  function addCacheRoute(routeName: string) {
    if (!cacheRoutes.value.includes(routeName)) {
      cacheRoutes.value.push(routeName)
    }
  }

  /** 移除缓存路由 */
  function removeCacheRoute(routeName: string) {
    const index = cacheRoutes.value.indexOf(routeName)
    if (index > -1) {
      cacheRoutes.value.splice(index, 1)
    }
  }

  /** 获取搜索菜单 */
  function getSearchMenus(menuList: App.Global.Menu[]): App.Global.Menu[] {
    const result: App.Global.Menu[] = []
    
    function traverse(menus: App.Global.Menu[]) {
      menus.forEach(menu => {
        if (menu.children) {
          traverse(menu.children)
        } else if (menu.routeName) {
          result.push(menu)
        }
      })
    }
    
    traverse(menuList)
    return result
  }

  return {
    // 状态
    isInitAuthRoute,
    menus,
    cacheRoutes,
    
    // 计算属性
    searchMenus,
    
    // 方法
    initAuthRoute,
    resetAuthRoute,
    updateCacheRoutes,
    addCacheRoute,
    removeCacheRoute
  }
})
```

### 4. 主题状态 (Theme Store)

主题状态管理应用主题配置。

```typescript
// src/store/modules/theme/index.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { localStg } from '@/utils/storage'
import { themeSettings } from '@/theme/settings'

export const useThemeStore = defineStore('theme', () => {
  /** 主题配置 */
  const settings = ref({ ...themeSettings })

  /** 暗黑模式 */
  const darkMode = computed(() => settings.value.darkMode)

  /** 主题颜色 */
  const themeColor = computed(() => settings.value.themeColor)

  /** 布局模式 */
  const layout = computed(() => settings.value.layout.mode)

  /** 是否显示面包屑 */
  const showBreadcrumb = computed(() => settings.value.breadcrumb.visible)

  /** 是否显示标签页 */
  const showTab = computed(() => settings.value.tab.visible)

  /** 是否显示页脚 */
  const showFooter = computed(() => settings.value.footer.visible)

  /** 切换暗黑模式 */
  function toggleDarkMode() {
    settings.value.darkMode = !settings.value.darkMode
    saveSettings()
  }

  /** 设置主题颜色 */
  function setThemeColor(color: string) {
    settings.value.themeColor = color
    saveSettings()
  }

  /** 设置布局模式 */
  function setLayoutMode(mode: App.Theme.LayoutMode) {
    settings.value.layout.mode = mode
    saveSettings()
  }

  /** 更新主题配置 */
  function updateSettings(newSettings: Partial<App.Theme.ThemeSetting>) {
    Object.assign(settings.value, newSettings)
    saveSettings()
  }

  /** 重置主题配置 */
  function resetSettings() {
    settings.value = { ...themeSettings }
    saveSettings()
  }

  /** 保存主题配置到本地存储 */
  function saveSettings() {
    localStg.set('themeSettings', settings.value)
  }

  /** 从本地存储加载主题配置 */
  function loadSettings() {
    const storedSettings = localStg.get('themeSettings')
    if (storedSettings) {
      settings.value = { ...themeSettings, ...storedSettings }
    }
  }

  return {
    // 状态
    settings,
    
    // 计算属性
    darkMode,
    themeColor,
    layout,
    showBreadcrumb,
    showTab,
    showFooter,
    
    // 方法
    toggleDarkMode,
    setThemeColor,
    setLayoutMode,
    updateSettings,
    resetSettings,
    saveSettings,
    loadSettings
  }
})
```

## 状态持久化

### 持久化插件配置

```typescript
// src/store/plugins/persist.ts
import type { PiniaPluginContext } from 'pinia'
import { localStg } from '@/utils/storage'

/** 需要持久化的store列表 */
const persistStores = ['auth', 'theme']

/** Pinia持久化插件 */
export function piniaPluginPersist({ store }: PiniaPluginContext) {
  if (persistStores.includes(store.$id)) {
    // 从本地存储恢复状态
    const storedState = localStg.get(`store-${store.$id}`)
    if (storedState) {
      store.$patch(storedState)
    }

    // 监听状态变化并保存到本地存储
    store.$subscribe((mutation, state) => {
      localStg.set(`store-${store.$id}`, state)
    })
  }
}
```

### 存储工具封装

```typescript
// src/utils/storage.ts
interface StorageData {
  token: string
  userInfo: Api.Auth.UserInfo
  themeSettings: App.Theme.ThemeSetting
  [key: string]: any
}

class LocalStorage {
  private prefix = 'mars-admin-'

  set<T extends keyof StorageData>(key: T, value: StorageData[T]) {
    const data = JSON.stringify(value)
    localStorage.setItem(this.prefix + key, data)
  }

  get<T extends keyof StorageData>(key: T): StorageData[T] | null {
    const data = localStorage.getItem(this.prefix + key)
    if (data) {
      try {
        return JSON.parse(data)
      } catch {
        return null
      }
    }
    return null
  }

  remove(key: keyof StorageData) {
    localStorage.removeItem(this.prefix + key)
  }

  clear() {
    const keys = Object.keys(localStorage)
    keys.forEach(key => {
      if (key.startsWith(this.prefix)) {
        localStorage.removeItem(key)
      }
    })
  }
}

export const localStg = new LocalStorage()
```

## 在组件中使用状态

### 1. 基础用法

```vue
<template>
  <div class="user-info">
    <a-avatar :src="userInfo?.avatar" />
    <span>{{ userInfo?.nickname }}</span>
    <a-button @click="handleLogout">退出登录</a-button>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'

const authStore = useAuthStore()

// 解构响应式状态
const { userInfo, isLogin } = storeToRefs(authStore)

// 调用store方法
const handleLogout = () => {
  authStore.logout()
}
</script>
```

### 2. 权限控制

```vue
<template>
  <div>
    <!-- 使用权限判断 -->
    <a-button v-if="hasPermission('system:user:add')" type="primary">
      新增用户
    </a-button>
    
    <!-- 使用角色判断 -->
    <a-button v-if="hasRole('admin')" type="danger">
      删除用户
    </a-button>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/store/modules/auth'

const authStore = useAuthStore()
const { hasPermission, hasRole } = authStore
</script>
```

### 3. 主题切换

```vue
<template>
  <div class="theme-switch">
    <a-switch 
      v-model:checked="isDark" 
      @change="toggleDarkMode"
    >
      <template #checkedChildren>🌙</template>
      <template #unCheckedChildren>☀️</template>
    </a-switch>
    
    <a-color-picker 
      v-model:value="currentThemeColor"
      @change="handleColorChange"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useThemeStore } from '@/store/modules/theme'

const themeStore = useThemeStore()

const isDark = computed(() => themeStore.darkMode)
const currentThemeColor = computed(() => themeStore.themeColor)

const toggleDarkMode = () => {
  themeStore.toggleDarkMode()
}

const handleColorChange = (color: string) => {
  themeStore.setThemeColor(color)
}
</script>
```

## 自定义Hooks

### 1. 权限Hook

```typescript
// src/hooks/business/use-permission.ts
import { useAuthStore } from '@/store/modules/auth'

export function usePermission() {
  const authStore = useAuthStore()

  /** 检查权限 */
  const hasPermission = (permission: string | string[]) => {
    if (Array.isArray(permission)) {
      return authStore.hasAnyPermission(permission)
    }
    return authStore.hasPermission(permission)
  }

  /** 检查角色 */
  const hasRole = (role: string | string[]) => {
    if (Array.isArray(role)) {
      return role.some(r => authStore.hasRole(r))
    }
    return authStore.hasRole(role)
  }

  /** 检查是否为超级管理员 */
  const isSuper = computed(() => authStore.isSuper)

  return {
    hasPermission,
    hasRole,
    isSuper
  }
}
```

### 2. 主题Hook

```typescript
// src/hooks/business/use-theme.ts
import { useThemeStore } from '@/store/modules/theme'

export function useTheme() {
  const themeStore = useThemeStore()

  /** 切换暗黑模式 */
  const toggleDark = () => {
    themeStore.toggleDarkMode()
  }

  /** 设置主题颜色 */
  const setThemeColor = (color: string) => {
    themeStore.setThemeColor(color)
  }

  /** 获取CSS变量 */
  const getCSSVar = (name: string) => {
    return getComputedStyle(document.documentElement).getPropertyValue(name)
  }

  /** 设置CSS变量 */
  const setCSSVar = (name: string, value: string) => {
    document.documentElement.style.setProperty(name, value)
  }

  return {
    toggleDark,
    setThemeColor,
    getCSSVar,
    setCSSVar,
    ...storeToRefs(themeStore)
  }
}
```

## 状态管理最佳实践

### 1. 状态设计原则
- **单一数据源**：每个状态只有一个来源
- **状态最小化**：只存储必要的状态
- **状态扁平化**：避免深层嵌套的状态结构
- **状态不可变**：通过方法修改状态，不直接修改

### 2. 性能优化
- **按需导入**：只导入需要的状态和方法
- **计算属性缓存**：使用computed缓存派生状态
- **状态分割**：将大的store拆分为小的模块
- **懒加载**：按需加载状态模块

### 3. 调试技巧
- **Vue DevTools**：使用开发工具查看状态变化
- **状态日志**：在开发环境记录状态变化
- **时间旅行**：利用DevTools的时间旅行功能
- **状态快照**：保存和恢复状态快照

### 4. 测试策略
- **单元测试**：测试store的方法和计算属性
- **集成测试**：测试组件与store的交互
- **端到端测试**：测试完整的用户流程
- **状态模拟**：在测试中模拟不同的状态

## 常见问题

### Q: 如何在setup外使用store？

**解决方案**：
```typescript
// 在setup外使用需要先创建pinia实例
import { createPinia } from 'pinia'
import { useAuthStore } from '@/store/modules/auth'

const pinia = createPinia()
const authStore = useAuthStore(pinia)
```

### Q: 如何重置store状态？

**解决方案**：
```typescript
// 方法1：使用$reset方法
authStore.$reset()

// 方法2：手动重置
authStore.$patch({
  token: '',
  userInfo: null
})

// 方法3：在store中定义reset方法
function resetStore() {
  token.value = ''
  userInfo.value = null
}
```

### Q: 如何处理异步状态？

**解决方案**：
```typescript
const loading = ref(false)
const error = ref<string | null>(null)

async function fetchData() {
  loading.value = true
  error.value = null
  
  try {
    const data = await api.getData()
    // 处理数据
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}
```

### Q: 如何实现状态的类型安全？

**解决方案**：
```typescript
// 定义状态类型
interface UserState {
  id: number
  name: string
  email: string
}

// 使用泛型约束
const userInfo = ref<UserState | null>(null)

// 类型守卫
function isValidUser(user: any): user is UserState {
  return user && typeof user.id === 'number' && typeof user.name === 'string'
}
```
