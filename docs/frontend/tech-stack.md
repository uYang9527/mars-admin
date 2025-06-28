# 前端技术栈

Mars Admin前端采用现代化的技术栈构建，基于Vue 3生态系统，提供高效、灵活的开发体验和优秀的用户界面。

## 核心技术

### 基础框架

- **Vue 3.5.13**：采用Composition API，提供更好的类型推导和性能
- **TypeScript 5.4.x**：增强代码的可维护性和可靠性
- **Vite 6.1.0**：快速的前端构建工具，提供极速的开发体验

### UI组件库

- **Ant Design Vue 4.2.6**：基于Ant Design设计体系的Vue实现
- **自定义组件**：基于业务需求封装的高级组件

### 状态管理

- **Pinia 3.0.0**：Vue官方推荐的状态管理库，替代Vuex
- **持久化插件**：支持状态持久化到localStorage

### 路由管理

- **Vue Router 4.3.0**：Vue官方路由管理器
- **路由守卫**：实现权限控制和页面访问限制

### HTTP客户端

- **Axios**：基于Promise的HTTP客户端
- **请求拦截器**：统一处理请求头、认证信息
- **响应拦截器**：统一处理响应格式、错误处理

### 工具库

- **dayjs**：轻量级日期处理库
- **lodash-es**：实用工具函数库
- **crypto-js**：加密解密功能
- **colord**：颜色处理工具

### 图表可视化

- **ECharts 5.5.0**：功能丰富的图表库
- **vue-echarts**：ECharts的Vue封装

### CSS预处理器

- **Sass/SCSS**：增强CSS的能力
- **UnoCSS**：原子化CSS引擎

### 国际化

- **vue-i18n**：Vue国际化解决方案
- **dayjs国际化**：日期格式本地化

### 构建工具链

- **ESLint**：代码质量检查
- **Prettier**：代码格式化
- **husky**：Git钩子工具
- **commitlint**：提交信息规范检查

## 项目结构

Mars Admin前端项目采用monorepo结构，使用pnpm workspace管理多个内部包：

```
mars-admin-ui/
├── packages/               # 内部包
│   ├── axios/              # HTTP客户端封装
│   ├── color/              # 颜色工具
│   ├── hooks/              # 可复用的Vue Hooks
│   ├── materials/          # UI组件库
│   ├── ofetch/             # 轻量级请求库
│   ├── scripts/            # 构建脚本
│   ├── uno-preset/         # UnoCSS预设
│   └── utils/              # 工具函数
├── public/                 # 静态资源
├── src/                    # 主应用源码
│   ├── assets/             # 资源文件
│   ├── components/         # 组件
│   ├── constants/          # 常量定义
│   ├── enum/               # 枚举定义
│   ├── hooks/              # 钩子函数
│   ├── layouts/            # 布局组件
│   ├── locales/            # 国际化资源
│   ├── router/             # 路由配置
│   ├── service/            # API服务
│   ├── store/              # Pinia状态
│   ├── styles/             # 全局样式
│   ├── typings/            # 类型定义
│   ├── utils/              # 工具函数
│   ├── views/              # 页面组件
│   ├── App.vue             # 根组件
│   └── main.ts             # 入口文件
└── package.json            # 项目配置
```

## 内部包说明

Mars Admin前端采用monorepo结构，将通用功能抽离为独立的包，提高代码复用性和可维护性。

### @mars/axios

HTTP客户端封装，提供统一的请求处理：

- 请求/响应拦截器
- 统一错误处理
- 请求取消
- 请求重试
- 刷新token机制

```typescript
// packages/axios/src/index.ts
import { createAxios } from './instance';
import type { AxiosTransform, CreateAxiosOptions } from './transform';

// 创建axios实例
function setupAxios(options?: Partial<CreateAxiosOptions>) {
  return createAxios(options);
}

export * from './constant';
export * from './transform';
export * from './options';
export { setupAxios };
export default setupAxios();
```

### @mars/hooks

可复用的Vue Hooks集合：

- useBoolean：布尔值状态管理
- useContext：上下文管理
- useLoading：加载状态管理
- useModal：模态框管理
- useRequest：数据请求管理
- useTable：表格数据管理

```typescript
// packages/hooks/src/use-table.ts
import { ref, reactive, computed } from 'vue';
import type { Ref } from 'vue';

export interface TableOptions<T = any, P = any> {
  fetchData: (params: P) => Promise<{
    list: T[];
    total: number;
  }>;
  defaultParams?: Partial<P>;
  immediate?: boolean;
}

export function useTable<T = any, P extends Record<string, any> = any>(
  options: TableOptions<T, P>
) {
  const { fetchData, defaultParams = {}, immediate = true } = options;
  
  const loading = ref(false);
  const dataList = ref<T[]>([]) as Ref<T[]>;
  const total = ref(0);
  
  const params = reactive({
    page: 1,
    pageSize: 10,
    ...defaultParams,
  } as P);
  
  const pagination = computed(() => ({
    current: params.page,
    pageSize: params.pageSize,
    total: total.value,
    showSizeChanger: true,
    showQuickJumper: true,
  }));
  
  async function loadData() {
    loading.value = true;
    try {
      const { list, total: totalCount } = await fetchData(params);
      dataList.value = list;
      total.value = totalCount;
    } finally {
      loading.value = false;
    }
  }
  
  function onPageChange(page: number, pageSize: number) {
    params.page = page;
    params.pageSize = pageSize;
    loadData();
  }
  
  function reload() {
    params.page = 1;
    loadData();
  }
  
  if (immediate) {
    loadData();
  }
  
  return {
    loading,
    dataList,
    total,
    params,
    pagination,
    loadData,
    onPageChange,
    reload,
  };
}
```

### @mars/materials

UI组件库，包含定制化的高级组件：

- AdminLayout：管理后台布局
- PageTab：页面标签组件
- ColorPicker：颜色选择器
- SimpleScrollbar：自定义滚动条

```typescript
// packages/materials/src/libs/admin-layout/index.ts
import { defineComponent } from 'vue';
import styles from './index.module.css';

export default defineComponent({
  name: 'AdminLayout',
  props: {
    // 属性定义
  },
  setup(props, { slots }) {
    // 组件逻辑
    
    return () => (
      <div class={styles.adminLayout}>
        {/* 组件模板 */}
      </div>
    );
  },
});
```

## 主要功能模块

### 认证授权

基于Token的认证系统：

- 登录认证
- 权限控制
- 路由守卫
- 记住我功能

```typescript
// src/store/modules/auth/index.ts
import { defineStore } from 'pinia';
import { router } from '@/router';
import { getToken, setToken, removeToken } from '@/utils/auth';
import { login, getUserInfo, logout } from '@/service/api/auth';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: getToken() || '',
    userInfo: null,
    permissions: [],
    roles: [],
  }),
  
  getters: {
    isLogin: state => !!state.token,
    hasPermission: state => (permission) => {
      return state.permissions.includes(permission);
    },
  },
  
  actions: {
    async login(username, password, rememberMe = false) {
      try {
        const { token } = await login(username, password);
        setToken(token, rememberMe);
        this.token = token;
        return true;
      } catch (error) {
        return false;
      }
    },
    
    async getUserInfo() {
      if (!this.token) return null;
      
      try {
        const data = await getUserInfo();
        this.userInfo = data;
        this.permissions = data.permissions || [];
        this.roles = data.roles || [];
        return data;
      } catch (error) {
        this.resetAuth();
        return null;
      }
    },
    
    async logout() {
      if (this.token) {
        try {
          await logout();
        } finally {
          this.resetAuth();
          router.push('/login');
        }
      }
    },
    
    resetAuth() {
      removeToken();
      this.$reset();
    },
  },
});
```

### 路由管理

动态路由系统：

- 基于权限的路由生成
- 路由守卫
- 页面缓存
- 页面标签管理

```typescript
// src/router/guard/permission.ts
import { useAuthStore } from '@/store/modules/auth';
import { WHITE_LIST } from '@/constants/route';

export function createPermissionGuard(router) {
  router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore();
    
    // 白名单路由直接放行
    if (WHITE_LIST.includes(to.name)) {
      next();
      return;
    }
    
    // 未登录跳转到登录页
    if (!authStore.isLogin) {
      next({ name: 'login', query: { redirect: to.fullPath } });
      return;
    }
    
    // 获取用户信息和权限
    if (!authStore.userInfo) {
      try {
        await authStore.getUserInfo();
      } catch (error) {
        authStore.resetAuth();
        next({ name: 'login', query: { redirect: to.fullPath } });
        return;
      }
    }
    
    next();
  });
}
```

### 国际化

多语言支持：

- 中英文切换
- 组件国际化
- 日期国际化
- 动态加载语言包

```typescript
// src/locales/index.ts
import { createI18n } from 'vue-i18n';
import { localStorage } from '@/utils/storage';
import { LOCALE_KEY } from '@/constants/app';
import zhCN from './langs/zh-cn';
import enUS from './langs/en-us';

const defaultLocale = localStorage.get(LOCALE_KEY) || 'zh-CN';

const i18n = createI18n({
  legacy: false,
  locale: defaultLocale,
  fallbackLocale: 'zh-CN',
  messages: {
    'zh-CN': zhCN,
    'en-US': enUS,
  },
});

export function setupI18n(app) {
  app.use(i18n);
}

export function setLocale(locale) {
  i18n.global.locale.value = locale;
  localStorage.set(LOCALE_KEY, locale);
}

export { i18n };
```

### 主题系统

可定制的主题系统：

- 多主题切换
- 深色模式
- 主题色定制
- 布局模式切换

```typescript
// src/store/modules/theme/index.ts
import { defineStore } from 'pinia';
import { localStorage } from '@/utils/storage';
import { generateTheme } from '@/utils/theme';
import { THEME_KEY } from '@/constants/app';
import { defaultThemeConfig } from '@/theme/settings';

export const useThemeStore = defineStore('theme', {
  state: () => ({
    ...defaultThemeConfig,
    ...JSON.parse(localStorage.get(THEME_KEY) || '{}'),
  }),
  
  actions: {
    setTheme(theme) {
      Object.assign(this, theme);
      localStorage.set(THEME_KEY, JSON.stringify(this.$state));
      this.applyTheme();
    },
    
    toggleDark(isDark) {
      this.darkMode = isDark;
      document.documentElement.classList.toggle('dark', isDark);
      localStorage.set(THEME_KEY, JSON.stringify(this.$state));
    },
    
    setLayoutMode(mode) {
      this.layout.mode = mode;
      localStorage.set(THEME_KEY, JSON.stringify(this.$state));
    },
    
    setPrimaryColor(color) {
      this.themeColor = color;
      generateTheme(color);
      localStorage.set(THEME_KEY, JSON.stringify(this.$state));
    },
    
    applyTheme() {
      // 应用主题设置
      document.documentElement.classList.toggle('dark', this.darkMode);
      generateTheme(this.themeColor);
      // 应用其他主题设置
    },
    
    resetTheme() {
      this.$reset();
      localStorage.remove(THEME_KEY);
      this.applyTheme();
    },
  },
});
```

### 表格组件

封装的高级表格组件：

- 数据加载
- 分页处理
- 排序过滤
- 列设置
- 导出功能

```vue
<!-- src/components/advanced/table-header-operation.vue -->
<template>
  <div class="table-header-operation">
    <a-space>
      <a-tooltip title="刷新">
        <a-button type="text" :loading="loading" @click="handleRefresh">
          <template #icon>
            <reload-outlined />
          </template>
        </a-button>
      </a-tooltip>
      
      <a-tooltip title="列设置">
        <a-button type="text" @click="showColumnSetting = true">
          <template #icon>
            <setting-outlined />
          </template>
        </a-button>
      </a-tooltip>
      
      <a-tooltip title="全屏">
        <a-button type="text" @click="toggleFullscreen">
          <template #icon>
            <component :is="isFullscreen ? 'fullscreen-exit-outlined' : 'fullscreen-outlined'" />
          </template>
        </a-button>
      </a-tooltip>
    </a-space>
    
    <table-column-setting
      v-model:visible="showColumnSetting"
      :columns="columns"
      @change="handleColumnChange"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useFullscreen } from '@/hooks/common/useFullscreen';
import TableColumnSetting from './table-column-setting.vue';

const props = defineProps({
  loading: Boolean,
  columns: Array,
  tableRef: Object,
});

const emit = defineEmits(['refresh', 'column-change']);

const showColumnSetting = ref(false);
const { isFullscreen, toggle: toggleFullscreen } = useFullscreen(props.tableRef);

function handleRefresh() {
  emit('refresh');
}

function handleColumnChange(newColumns) {
  emit('column-change', newColumns);
}
</script>
```

## 开发规范

### 目录结构规范

- 按功能模块组织代码
- 公共组件放在components目录
- 页面组件放在views目录
- 业务逻辑放在service目录
- 工具函数放在utils目录

### 命名规范

- 文件名：kebab-case（如user-info.vue）
- 组件名：PascalCase（如UserInfo）
- 变量名：camelCase（如userName）
- 常量名：UPPER_SNAKE_CASE（如MAX_COUNT）
- CSS类名：kebab-case（如user-avatar）

### 代码风格

- 使用ESLint和Prettier保持一致的代码风格
- 使用TypeScript类型定义
- 使用Composition API编写组件
- 使用SFC（Single File Component）组件格式

### 提交规范

使用约定式提交规范（Conventional Commits）：

- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码风格调整
- refactor: 代码重构
- perf: 性能优化
- test: 测试相关
- chore: 构建过程或辅助工具变动

## 构建与部署

### 开发环境

```bash
# 安装依赖
pnpm install

# 启动开发服务器
pnpm dev
```

### 生产构建

```bash
# 构建生产版本
pnpm build

# 预览生产构建
pnpm preview
```

### 环境配置

使用.env文件管理不同环境的配置：

- .env：所有环境共享的配置
- .env.development：开发环境配置
- .env.production：生产环境配置

```
# .env.development
VITE_API_BASE_URL=/api
VITE_MOCK=true
VITE_DROP_CONSOLE=false

# .env.production
VITE_API_BASE_URL=https://api.example.com
VITE_MOCK=false
VITE_DROP_CONSOLE=true
```

## 性能优化

### 代码分割

使用动态导入实现路由级别的代码分割：

```typescript
// src/router/routes/index.ts
const routes = [
  {
    path: '/dashboard',
    component: () => import('@/views/dashboard/index.vue'),
    meta: { title: '仪表盘' },
  },
  {
    path: '/user',
    component: () => import('@/views/user/index.vue'),
    meta: { title: '用户管理' },
  },
];
```

### 组件懒加载

对于大型组件或不常用组件，使用懒加载：

```typescript
import { defineAsyncComponent } from 'vue';

const HeavyComponent = defineAsyncComponent(() => 
  import('@/components/heavy-component.vue')
);
```

### 虚拟滚动

对于长列表，使用虚拟滚动提高性能：

```vue
<template>
  <virtual-list
    :data-key="'id'"
    :data-sources="list"
    :data-component="ItemComponent"
    :estimate-size="60"
  />
</template>
```

### 图片优化

- 使用WebP格式
- 图片懒加载
- 适当的图片压缩

### 缓存策略

- 使用keep-alive缓存组件
- localStorage存储用户设置
- 合理使用API数据缓存

## 常见问题

### Q: 如何添加新页面？

**解决方案**：
1. 在`src/views`目录下创建新的Vue组件
2. 在`src/router/routes`中添加路由配置
3. 在菜单管理中添加对应的菜单项
4. 分配相应的权限

### Q: 如何使用自定义图标？

**解决方案**：
1. 将SVG图标文件放在`src/assets/svg-icon`目录下
2. 使用`SvgIcon`组件引用图标：
   ```vue
   <svg-icon name="your-icon-name" />
   ```

### Q: 如何添加新的API接口？

**解决方案**：
1. 在`src/service/api`目录下创建或修改API定义文件
2. 使用封装的请求函数发起请求：
   ```typescript
   import { http } from '@/service/request';
   
   export function getUserList(params) {
     return http.get('/system/user/list', { params });
   }
   ```

### Q: 如何实现权限控制？

**解决方案**：
1. 使用权限指令控制按钮/元素的显示：
   ```vue
   <a-button v-permission="'system:user:add'">新增用户</a-button>
   ```
2. 使用路由守卫控制页面访问：
   ```typescript
   meta: {
     permissions: ['system:user:list']
   }
   ```

## 参考资源

- [Vue 3官方文档](https://v3.vuejs.org/)
- [TypeScript官方文档](https://www.typescriptlang.org/docs/)
- [Vite官方文档](https://vitejs.dev/guide/)
- [Ant Design Vue文档](https://antdv.com/components/overview)
- [Pinia官方文档](https://pinia.vuejs.org/) 