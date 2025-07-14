# 前端项目结构

Mars Admin前端项目基于Vue 3 + TypeScript + Vite构建，采用现代化的前端开发架构。

## 项目目录结构

```
mars-admin-ui/
├── build/                          # 构建配置
│   ├── plugins/                    # Vite插件配置
│   │   ├── router.ts              # 路由插件配置
│   │   ├── unocss.ts              # UnoCSS配置
│   │   └── unplugin.ts            # 自动导入插件配置
│   └── utils/                      # 构建工具函数
├── packages/                       # 内部包
│   ├── axios/                     # HTTP请求封装
│   ├── color/                     # 颜色工具
│   ├── hooks/                     # 通用Hooks
│   ├── materials/                 # 物料组件
│   └── utils/                     # 工具函数
├── public/                         # 静态资源
│   ├── favicon.ico                # 网站图标
│   └── logo.png                   # Logo图片
├── src/                           # 源代码目录
│   ├── assets/                    # 资源文件
│   │   ├── imgs/                  # 图片资源
│   │   └── svg-icon/              # SVG图标
│   ├── components/                # 公共组件
│   │   ├── advanced/              # 高级组件
│   │   ├── common/                # 通用组件
│   │   └── custom/                # 自定义组件
│   ├── constants/                 # 常量定义
│   │   ├── app.ts                 # 应用常量
│   │   ├── business.ts            # 业务常量
│   │   ├── common.ts              # 通用常量
│   │   └── reg.ts                 # 正则表达式
│   ├── enum/                      # 枚举定义
│   │   └── index.ts               # 枚举导出
│   ├── hooks/                     # Vue Hooks
│   │   ├── business/              # 业务Hooks
│   │   └── common/                # 通用Hooks
│   ├── layouts/                   # 布局组件
│   │   ├── base-layout/           # 基础布局
│   │   ├── blank-layout/          # 空白布局
│   │   ├── context/               # 布局上下文
│   │   └── modules/               # 布局模块
│   ├── locales/                   # 国际化
│   │   ├── langs/                 # 语言包
│   │   ├── antd.ts                # Ant Design国际化
│   │   ├── dayjs.ts               # Day.js国际化
│   │   └── index.ts               # 国际化配置
│   ├── plugins/                   # 插件配置
│   │   ├── app.ts                 # 应用插件
│   │   ├── assets.ts              # 资源插件
│   │   ├── dayjs.ts               # 日期插件
│   │   ├── iconify.ts             # 图标插件
│   │   ├── loading.ts             # 加载插件
│   │   └── nprogress.ts           # 进度条插件
│   ├── router/                    # 路由配置
│   │   ├── elegant/               # 优雅路由
│   │   ├── guard/                 # 路由守卫
│   │   ├── routes/                # 路由定义
│   │   └── index.ts               # 路由入口
│   ├── service/                   # API服务
│   │   ├── api/                   # API接口定义
│   │   └── request/               # 请求配置
│   ├── store/                     # 状态管理
│   │   ├── modules/               # 状态模块
│   │   ├── plugins/               # 状态插件
│   │   └── index.ts               # 状态入口
│   ├── styles/                    # 样式文件
│   │   ├── css/                   # CSS样式
│   │   └── scss/                  # SCSS样式
│   ├── theme/                     # 主题配置
│   │   ├── settings.ts            # 主题设置
│   │   └── vars.ts                # 主题变量
│   ├── types/                     # 类型定义
│   │   ├── auto-imports.d.ts      # 自动导入类型
│   │   └── components.d.ts        # 组件类型
│   ├── typings/                   # TypeScript类型
│   │   ├── antd.d.ts              # Ant Design类型
│   │   ├── api.d.ts               # API类型
│   │   ├── app.d.ts               # 应用类型
│   │   ├── common.d.ts            # 通用类型
│   │   ├── components.d.ts        # 组件类型
│   │   ├── elegant-router.d.ts    # 路由类型
│   │   ├── global.d.ts            # 全局类型
│   │   ├── router.d.ts            # 路由类型
│   │   ├── storage.d.ts           # 存储类型
│   │   ├── union-key.d.ts         # 联合类型
│   │   └── vite-env.d.ts          # Vite环境类型
│   ├── utils/                     # 工具函数
│   │   ├── agent.ts               # 用户代理工具
│   │   ├── common.ts              # 通用工具
│   │   ├── error.ts               # 错误处理
│   │   ├── icon.ts                # 图标工具
│   │   ├── service.ts             # 服务工具
│   │   └── storage.ts             # 存储工具
│   ├── views/                     # 页面组件
│   │   ├── _builtin/              # 内置页面
│   │   ├── home/                  # 首页
│   │   ├── manage/                # 管理页面
│   │   └── user-center/           # 用户中心
│   ├── App.vue                    # 根组件
│   └── main.ts                    # 应用入口
├── .env                           # 环境变量
├── .env.test                      # 测试环境变量
├── .env.prod                      # 生产环境变量
├── eslint.config.js               # ESLint配置
├── index.html                     # HTML模板
├── package.json                   # 项目配置
├── pnpm-lock.yaml                 # 依赖锁定文件
├── pnpm-workspace.yaml            # pnpm工作空间配置
├── tsconfig.json                  # TypeScript配置
├── uno.config.ts                  # UnoCSS配置
└── vite.config.ts                 # Vite配置
```

## 核心目录详解

### 1. src/components/ - 组件目录

#### advanced/ - 高级组件
```
advanced/
├── table-header-operation.vue     # 表格头部操作组件
├── table-pagination.vue           # 表格分页组件
├── search-form.vue                # 搜索表单组件
└── data-table.vue                 # 数据表格组件
```

#### common/ - 通用组件
```
common/
├── better-scroll.vue              # 滚动组件
├── icon-selector.vue              # 图标选择器
├── loading.vue                    # 加载组件
└── svg-icon.vue                   # SVG图标组件
```

#### custom/ - 自定义组件
```
custom/
├── mars-logo.vue                  # Mars Logo组件
├── theme-switch.vue               # 主题切换组件
└── user-avatar.vue                # 用户头像组件
```

### 2. src/views/ - 页面目录

#### _builtin/ - 内置页面
```
_builtin/
├── 403/                           # 403权限不足页面
├── 404/                           # 404页面未找到
├── 500/                           # 500服务器错误
├── iframe-page/                   # iframe页面
└── login/                         # 登录页面
    ├── index.vue                  # 登录主页面
    └── modules/                   # 登录模块
        ├── pwd-login.vue          # 密码登录
        ├── code-login.vue         # 验证码登录
        ├── register.vue           # 用户注册
        ├── reset-pwd.vue          # 重置密码
        └── bind-wechat.vue        # 微信绑定
```

#### manage/ - 管理页面
```
manage/
├── user/                          # 用户管理
│   ├── index.vue                  # 用户列表页面
│   └── modules/                   # 用户模块
│       ├── user-operate-drawer.vue # 用户操作抽屉
│       └── user-search.vue        # 用户搜索组件
├── role/                          # 角色管理
├── menu/                          # 菜单管理
├── dept/                          # 部门管理
├── post/                          # 岗位管理
├── dict/                          # 字典管理
├── config/                        # 参数配置
├── file-manage/                   # 文件管理
├── operation-log/                 # 操作日志
├── login-log/                     # 登录日志
├── system-monitor/                # 系统监控
└── cache-monitor/                 # 缓存监控
```

### 3. src/service/ - API服务目录

```
service/
├── api/                           # API接口定义
│   ├── auth.ts                    # 认证相关API
│   ├── user.ts                    # 用户管理API
│   ├── role.ts                    # 角色管理API
│   ├── menu.ts                    # 菜单管理API
│   ├── dept.ts                    # 部门管理API
│   ├── dict.ts                    # 字典管理API
│   ├── config.ts                  # 配置管理API
│   ├── upload.ts                  # 文件上传API
│   └── monitor.ts                 # 监控相关API
└── request/                       # 请求配置
    ├── index.ts                   # 请求入口
    ├── instance.ts                # 请求实例
    └── shared.ts                  # 共享配置
```

### 4. src/store/ - 状态管理目录

```
store/
├── modules/                       # 状态模块
│   ├── app/                       # 应用状态
│   │   ├── index.ts               # 应用状态入口
│   │   └── shared.ts              # 应用共享状态
│   ├── auth/                      # 认证状态
│   │   ├── index.ts               # 认证状态入口
│   │   └── shared.ts              # 认证共享状态
│   ├── route/                     # 路由状态
│   │   ├── index.ts               # 路由状态入口
│   │   └── shared.ts              # 路由共享状态
│   └── theme/                     # 主题状态
│       ├── index.ts               # 主题状态入口
│       └── shared.ts              # 主题共享状态
├── plugins/                       # 状态插件
│   └── persist.ts                 # 持久化插件
└── index.ts                       # 状态入口
```

### 5. src/router/ - 路由目录

```
router/
├── elegant/                       # 优雅路由
│   ├── imports.ts                 # 路由导入
│   ├── routes.ts                  # 路由定义
│   └── transform.ts               # 路由转换
├── guard/                         # 路由守卫
│   ├── index.ts                   # 守卫入口
│   ├── permission.ts              # 权限守卫
│   └── dynamic.ts                 # 动态路由守卫
├── routes/                        # 路由定义
│   ├── builtin.ts                 # 内置路由
│   └── index.ts                   # 路由入口
└── index.ts                       # 路由配置入口
```

## 文件命名规范

### 1. 组件命名
- **Vue组件**: 使用PascalCase，如 `UserManage.vue`
- **组件文件夹**: 使用kebab-case，如 `user-manage/`
- **组件内部文件**: 使用kebab-case，如 `user-operate-drawer.vue`

### 2. 文件命名
- **TypeScript文件**: 使用kebab-case，如 `user-service.ts`
- **样式文件**: 使用kebab-case，如 `user-manage.scss`
- **类型定义文件**: 使用kebab-case，如 `user-types.d.ts`

### 3. 目录命名
- **功能目录**: 使用kebab-case，如 `user-center/`
- **模块目录**: 使用kebab-case，如 `auth-module/`

## 代码组织原则

### 1. 单一职责原则
每个文件、组件、函数都应该有明确的单一职责：
```typescript
// ✅ 好的做法 - 单一职责
// user-api.ts - 只处理用户相关API
export const fetchUserList = () => { /* ... */ }
export const createUser = () => { /* ... */ }
export const updateUser = () => { /* ... */ }

// ❌ 不好的做法 - 职责混乱
// api.ts - 混合了多种API
export const fetchUserList = () => { /* ... */ }
export const fetchRoleList = () => { /* ... */ }
export const uploadFile = () => { /* ... */ }
```

### 2. 模块化组织
相关功能应该组织在同一个模块中：
```
user-manage/
├── index.vue              # 主组件
├── components/            # 子组件
│   ├── user-form.vue
│   └── user-table.vue
├── hooks/                 # 业务Hooks
│   └── use-user.ts
├── types/                 # 类型定义
│   └── user.ts
└── constants/             # 常量定义
    └── user.ts
```

### 3. 依赖关系清晰
- 避免循环依赖
- 明确依赖层级
- 使用绝对路径导入

```typescript
// ✅ 好的做法 - 使用绝对路径
import { fetchUserList } from '@/service/api/user'
import { UserType } from '@/typings/api'

// ❌ 不好的做法 - 使用相对路径
import { fetchUserList } from '../../../service/api/user'
```

## 组件设计规范

### 1. 组件结构
```vue
<template>
  <!-- 模板内容 -->
</template>

<script setup lang="ts">
// 1. 导入依赖
import { ref, computed, onMounted } from 'vue'
import type { ComponentProps } from './types'

// 2. 定义Props和Emits
interface Props {
  title: string
  visible: boolean
}

interface Emits {
  (e: 'update:visible', visible: boolean): void
  (e: 'confirm', data: any): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 3. 响应式数据
const loading = ref(false)
const formData = ref({})

// 4. 计算属性
const isValid = computed(() => {
  return formData.value.name && formData.value.email
})

// 5. 方法定义
const handleSubmit = () => {
  // 处理提交逻辑
}

// 6. 生命周期
onMounted(() => {
  // 组件挂载后的逻辑
})
</script>

<style scoped>
/* 组件样式 */
</style>
```

### 2. 组件通信
- **父子通信**: 使用Props和Emits
- **跨组件通信**: 使用Pinia状态管理
- **事件通信**: 使用事件总线或Provide/Inject

### 3. 组件复用
- 提取公共逻辑到Hooks
- 使用插槽提高组件灵活性
- 合理使用组件继承和混入

## 样式组织

### 1. 样式文件结构
```
styles/
├── css/
│   ├── reset.css              # 重置样式
│   ├── common.css             # 通用样式
│   └── variables.css          # CSS变量
└── scss/
    ├── mixins.scss            # SCSS混入
    ├── functions.scss         # SCSS函数
    └── variables.scss         # SCSS变量
```

### 2. 样式命名规范
- 使用BEM命名规范
- 组件样式使用scoped
- 全局样式谨慎使用

```scss
// BEM命名示例
.user-manage {
  &__header {
    display: flex;
    justify-content: space-between;
    
    &--sticky {
      position: sticky;
      top: 0;
    }
  }
  
  &__content {
    padding: 16px;
  }
  
  &__footer {
    text-align: center;
  }
}
```

## 类型定义规范

### 1. API类型定义
```typescript
// typings/api.d.ts
declare namespace Api {
  namespace Auth {
    interface LoginToken {
      token: string
      expiresIn: number
    }
    
    interface UserInfo {
      id: number
      username: string
      nickname: string
      roles: string[]
      permissions: string[]
    }
  }
  
  namespace User {
    interface User {
      id: number
      username: string
      nickname: string
      email: string
      phone: string
      status: number
    }
    
    interface UserSearchParams {
      username?: string
      nickname?: string
      status?: number
    }
  }
}
```

### 2. 组件类型定义
```typescript
// components/user-form/types.ts
export interface UserFormData {
  username: string
  nickname: string
  email: string
  phone: string
}

export interface UserFormProps {
  visible: boolean
  data?: UserFormData
  mode: 'create' | 'edit'
}

export interface UserFormEmits {
  (e: 'update:visible', visible: boolean): void
  (e: 'submit', data: UserFormData): void
}
```

## 工具函数组织

### 1. 通用工具函数
```typescript
// utils/common.ts
export const formatDate = (date: Date, format: string) => {
  // 日期格式化逻辑
}

export const debounce = (fn: Function, delay: number) => {
  // 防抖函数逻辑
}

export const throttle = (fn: Function, delay: number) => {
  // 节流函数逻辑
}
```

### 2. 业务工具函数
```typescript
// utils/business.ts
export const formatUserStatus = (status: number) => {
  const statusMap = {
    0: '禁用',
    1: '正常'
  }
  return statusMap[status] || '未知'
}

export const validatePhone = (phone: string) => {
  const phoneReg = /^1[3-9]\d{9}$/
  return phoneReg.test(phone)
}
```

## 最佳实践

### 1. 性能优化
- 使用`v-memo`缓存复杂计算
- 合理使用`keep-alive`缓存组件
- 使用虚拟滚动处理大列表
- 图片懒加载和预加载

### 2. 代码质量
- 使用ESLint和Prettier保证代码风格
- 编写单元测试覆盖核心逻辑
- 使用TypeScript提供类型安全
- 定期进行代码审查

### 3. 开发效率
- 使用代码片段提高开发效率
- 配置开发工具提升体验
- 使用热重载快速调试
- 合理使用第三方库

### 4. 维护性
- 保持代码简洁易读
- 添加必要的注释说明
- 遵循统一的编码规范
- 及时重构优化代码

## 常见问题

### Q: 如何添加新的页面？

**解决方案**：
1. 在`src/views`目录下创建页面组件
2. 在路由配置中添加路由定义
3. 在菜单管理中添加对应菜单
4. 配置相应的权限

### Q: 如何封装可复用组件？

**解决方案**：
1. 分析组件的通用性和可配置性
2. 设计合理的Props和Emits接口
3. 使用插槽提供内容定制能力
4. 编写完整的类型定义和文档

### Q: 如何优化打包体积？

**解决方案**：
1. 使用动态导入进行代码分割
2. 配置Tree Shaking移除无用代码
3. 使用CDN加载第三方库
4. 压缩图片和其他静态资源
