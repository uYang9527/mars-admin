# 组件开发

Mars Admin前端基于Vue 3 + TypeScript构建了完整的组件体系，提供了丰富的通用组件和业务组件。

## 组件架构

### 组件分类

```
src/components/
├── advanced/                  # 高级组件
│   ├── data-table.vue        # 数据表格组件
│   ├── search-form.vue       # 搜索表单组件
│   ├── table-header-operation.vue  # 表格头部操作
│   └── table-pagination.vue  # 表格分页组件
├── common/                   # 通用组件
│   ├── better-scroll.vue     # 滚动组件
│   ├── icon-selector.vue     # 图标选择器
│   ├── loading.vue           # 加载组件
│   └── svg-icon.vue          # SVG图标组件
└── custom/                   # 自定义组件
    ├── mars-logo.vue         # Mars Logo组件
    ├── theme-switch.vue      # 主题切换组件
    └── user-avatar.vue       # 用户头像组件
```

### 组件设计原则

1. **单一职责**：每个组件只负责一个功能
2. **可复用性**：组件应该具有良好的复用性
3. **可配置性**：通过props提供灵活的配置选项
4. **类型安全**：使用TypeScript提供类型安全
5. **性能优化**：合理使用响应式和计算属性

## 核心组件详解

### 1. 数据表格组件 (DataTable)

数据表格是管理系统中最重要的组件之一，提供了完整的数据展示和操作功能。

```vue
<!-- src/components/advanced/data-table.vue -->
<template>
  <div class="data-table">
    <!-- 表格头部操作 -->
    <div v-if="showHeader" class="table-header">
      <div class="header-left">
        <slot name="header-left">
          <a-space>
            <a-button
              v-if="showAdd"
              type="primary"
              @click="handleAdd"
            >
              <template #icon><plus-outlined /></template>
              {{ addText }}
            </a-button>
            <a-button
              v-if="showBatchDelete && selectedRowKeys.length > 0"
              type="danger"
              @click="handleBatchDelete"
            >
              <template #icon><delete-outlined /></template>
              {{ batchDeleteText }}
            </a-button>
          </a-space>
        </slot>
      </div>
      <div class="header-right">
        <slot name="header-right">
          <a-space>
            <!-- 列设置 -->
            <a-tooltip title="列设置">
              <a-button type="text" @click="showColumnSetting = true">
                <template #icon><setting-outlined /></template>
              </a-button>
            </a-tooltip>
            <!-- 刷新 -->
            <a-tooltip title="刷新">
              <a-button type="text" @click="handleRefresh">
                <template #icon><reload-outlined /></template>
              </a-button>
            </a-tooltip>
            <!-- 密度设置 -->
            <a-dropdown>
              <a-button type="text">
                <template #icon><column-height-outlined /></template>
              </a-button>
              <template #overlay>
                <a-menu @click="handleDensityChange">
                  <a-menu-item key="large">宽松</a-menu-item>
                  <a-menu-item key="middle">中等</a-menu-item>
                  <a-menu-item key="small">紧凑</a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </a-space>
        </slot>
      </div>
    </div>

    <!-- 数据表格 -->
    <a-table
      v-bind="tableProps"
      :columns="visibleColumns"
      :data-source="dataSource"
      :loading="loading"
      :pagination="paginationConfig"
      :row-selection="rowSelectionConfig"
      :size="tableSize"
      @change="handleTableChange"
    >
      <!-- 动态插槽 -->
      <template v-for="slot in Object.keys($slots)" #[slot]="scope">
        <slot :name="slot" v-bind="scope" />
      </template>
    </a-table>

    <!-- 列设置弹窗 -->
    <a-modal
      v-model:visible="showColumnSetting"
      title="列设置"
      width="400px"
      @ok="handleColumnSettingOk"
    >
      <a-tree
        v-model:checkedKeys="checkedColumns"
        :tree-data="columnTreeData"
        checkable
        :selectable="false"
      />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { TableProps, TableColumnType } from 'ant-design-vue'
import {
  PlusOutlined,
  DeleteOutlined,
  SettingOutlined,
  ReloadOutlined,
  ColumnHeightOutlined
} from '@ant-design/icons-vue'

// Props定义
interface Props {
  /** 表格列配置 */
  columns: TableColumnType[]
  /** 数据源 */
  dataSource: any[]
  /** 加载状态 */
  loading?: boolean
  /** 是否显示头部 */
  showHeader?: boolean
  /** 是否显示新增按钮 */
  showAdd?: boolean
  /** 新增按钮文本 */
  addText?: string
  /** 是否显示批量删除 */
  showBatchDelete?: boolean
  /** 批量删除按钮文本 */
  batchDeleteText?: string
  /** 是否显示行选择 */
  showRowSelection?: boolean
  /** 分页配置 */
  pagination?: TableProps['pagination']
  /** 其他表格属性 */
  tableProps?: Partial<TableProps>
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  showHeader: true,
  showAdd: true,
  addText: '新增',
  showBatchDelete: true,
  batchDeleteText: '批量删除',
  showRowSelection: true,
  pagination: () => ({
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true,
    showQuickJumper: true,
    showTotal: (total: number) => `共 ${total} 条`
  })
})

// Emits定义
interface Emits {
  (e: 'add'): void
  (e: 'batchDelete', keys: string[]): void
  (e: 'refresh'): void
  (e: 'change', pagination: any, filters: any, sorter: any): void
}

const emit = defineEmits<Emits>()

// 响应式数据
const selectedRowKeys = ref<string[]>([])
const showColumnSetting = ref(false)
const checkedColumns = ref<string[]>([])
const tableSize = ref<'large' | 'middle' | 'small'>('middle')

// 计算属性
const visibleColumns = computed(() => {
  return props.columns.filter(col =>
    checkedColumns.value.includes(col.key as string)
  )
})

const columnTreeData = computed(() => {
  return props.columns.map(col => ({
    key: col.key,
    title: col.title,
    children: []
  }))
})

const paginationConfig = computed(() => {
  if (props.pagination === false) return false
  return {
    ...props.pagination,
    showSizeChanger: true,
    showQuickJumper: true,
    showTotal: (total: number) => `共 ${total} 条`
  }
})

const rowSelectionConfig = computed(() => {
  if (!props.showRowSelection) return undefined
  return {
    selectedRowKeys: selectedRowKeys.value,
    onChange: (keys: string[]) => {
      selectedRowKeys.value = keys
    }
  }
})

// 方法定义
const handleAdd = () => {
  emit('add')
}

const handleBatchDelete = () => {
  emit('batchDelete', selectedRowKeys.value)
}

const handleRefresh = () => {
  emit('refresh')
}

const handleTableChange = (pagination: any, filters: any, sorter: any) => {
  emit('change', pagination, filters, sorter)
}

const handleDensityChange = ({ key }: { key: string }) => {
  tableSize.value = key as 'large' | 'middle' | 'small'
}

const handleColumnSettingOk = () => {
  showColumnSetting.value = false
}

// 初始化
watch(() => props.columns, (columns) => {
  checkedColumns.value = columns.map(col => col.key as string)
}, { immediate: true })
</script>

<style scoped>
.data-table {
  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    padding: 16px;
    background: #fff;
    border-radius: 6px;
  }
}
</style>
```

### 2. 搜索表单组件 (SearchForm)

搜索表单组件提供了灵活的搜索条件配置功能。

```vue
<!-- src/components/advanced/search-form.vue -->
<template>
  <div class="search-form">
    <a-form
      ref="formRef"
      :model="formData"
      layout="inline"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <!-- 动态渲染表单项 -->
      <template v-for="item in visibleItems" :key="item.field">
        <a-form-item
          :label="item.label"
          :name="item.field"
          :rules="item.rules"
        >
          <!-- 输入框 -->
          <a-input
            v-if="item.type === 'input'"
            v-model:value="formData[item.field]"
            :placeholder="item.placeholder"
            :style="{ width: item.width || '200px' }"
            allow-clear
          />

          <!-- 选择框 -->
          <a-select
            v-else-if="item.type === 'select'"
            v-model:value="formData[item.field]"
            :placeholder="item.placeholder"
            :style="{ width: item.width || '200px' }"
            allow-clear
          >
            <a-select-option
              v-for="option in item.options"
              :key="option.value"
              :value="option.value"
            >
              {{ option.label }}
            </a-select-option>
          </a-select>

          <!-- 日期选择器 -->
          <a-date-picker
            v-else-if="item.type === 'date'"
            v-model:value="formData[item.field]"
            :placeholder="item.placeholder"
            :style="{ width: item.width || '200px' }"
          />

          <!-- 日期范围选择器 -->
          <a-range-picker
            v-else-if="item.type === 'dateRange'"
            v-model:value="formData[item.field]"
            :style="{ width: item.width || '300px' }"
          />

          <!-- 自定义插槽 -->
          <slot
            v-else-if="item.type === 'slot'"
            :name="item.field"
            :item="item"
            :value="formData[item.field]"
            @update:value="(val) => formData[item.field] = val"
          />
        </a-form-item>
      </template>

      <!-- 操作按钮 -->
      <a-form-item>
        <a-space>
          <a-button type="primary" @click="handleSearch">
            <template #icon><search-outlined /></template>
            搜索
          </a-button>
          <a-button @click="handleReset">
            <template #icon><reload-outlined /></template>
            重置
          </a-button>
          <a-button
            v-if="collapsible && items.length > defaultVisibleCount"
            type="link"
            @click="toggleCollapse"
          >
            {{ collapsed ? '展开' : '收起' }}
            <component :is="collapsed ? DownOutlined : UpOutlined" />
          </a-button>
        </a-space>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { SearchOutlined, ReloadOutlined, DownOutlined, UpOutlined } from '@ant-design/icons-vue'

// 表单项类型定义
interface FormItem {
  field: string
  label: string
  type: 'input' | 'select' | 'date' | 'dateRange' | 'slot'
  placeholder?: string
  width?: string
  options?: Array<{ label: string; value: any }>
  rules?: any[]
  defaultValue?: any
}

// Props定义
interface Props {
  /** 表单项配置 */
  items: FormItem[]
  /** 是否可折叠 */
  collapsible?: boolean
  /** 默认显示的表单项数量 */
  defaultVisibleCount?: number
  /** 标签列配置 */
  labelCol?: object
  /** 包装列配置 */
  wrapperCol?: object
}

const props = withDefaults(defineProps<Props>(), {
  collapsible: true,
  defaultVisibleCount: 3,
  labelCol: () => ({ span: 6 }),
  wrapperCol: () => ({ span: 18 })
})

// Emits定义
interface Emits {
  (e: 'search', values: Record<string, any>): void
  (e: 'reset'): void
}

const emit = defineEmits<Emits>()

// 响应式数据
const formRef = ref()
const collapsed = ref(true)
const formData = reactive<Record<string, any>>({})

// 计算属性
const visibleItems = computed(() => {
  if (!props.collapsible || !collapsed.value) {
    return props.items
  }
  return props.items.slice(0, props.defaultVisibleCount)
})

// 方法定义
const handleSearch = async () => {
  try {
    await formRef.value.validate()
    emit('search', { ...formData })
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const handleReset = () => {
  formRef.value.resetFields()
  // 重置为默认值
  props.items.forEach(item => {
    if (item.defaultValue !== undefined) {
      formData[item.field] = item.defaultValue
    } else {
      delete formData[item.field]
    }
  })
  emit('reset')
}

const toggleCollapse = () => {
  collapsed.value = !collapsed.value
}

// 初始化表单数据
const initFormData = () => {
  props.items.forEach(item => {
    if (item.defaultValue !== undefined) {
      formData[item.field] = item.defaultValue
    }
  })
}

// 暴露方法
defineExpose({
  validate: () => formRef.value.validate(),
  resetFields: () => formRef.value.resetFields(),
  getFieldsValue: () => ({ ...formData }),
  setFieldsValue: (values: Record<string, any>) => {
    Object.assign(formData, values)
  }
})

// 初始化
initFormData()
</script>

<style scoped>
.search-form {
  padding: 16px;
  background: #fff;
  border-radius: 6px;
  margin-bottom: 16px;
}
</style>
```

### 3. SVG图标组件 (SvgIcon)

SVG图标组件提供了灵活的图标显示功能。

```vue
<!-- src/components/common/svg-icon.vue -->
<template>
  <svg
    :class="['svg-icon', className]"
    :style="{ width: size, height: size, color }"
    aria-hidden="true"
  >
    <use :xlink:href="iconName" />
  </svg>
</template>

<script setup lang="ts">
import { computed } from 'vue'

// Props定义
interface Props {
  /** 图标名称 */
  name: string
  /** 图标大小 */
  size?: string | number
  /** 图标颜色 */
  color?: string
  /** 自定义类名 */
  className?: string
}

const props = withDefaults(defineProps<Props>(), {
  size: '16px',
  color: 'currentColor',
  className: ''
})

// 计算属性
const iconName = computed(() => `#icon-${props.name}`)
</script>

<style scoped>
.svg-icon {
  display: inline-block;
  vertical-align: middle;
  fill: currentColor;
  overflow: hidden;
}
</style>
```

### 4. 用户头像组件 (UserAvatar)

用户头像组件提供了用户头像显示和操作功能。

```vue
<!-- src/components/custom/user-avatar.vue -->
<template>
  <a-dropdown :trigger="['click']" placement="bottomRight">
    <div class="user-avatar" @click.prevent>
      <a-avatar
        :size="size"
        :src="avatar"
        :style="{ backgroundColor: avatarColor }"
      >
        {{ avatarText }}
      </a-avatar>
      <div v-if="showInfo" class="user-info">
        <div class="user-name">{{ nickname }}</div>
        <div class="user-role">{{ roleText }}</div>
      </div>
      <down-outlined v-if="showArrow" class="dropdown-arrow" />
    </div>

    <template #overlay>
      <a-menu @click="handleMenuClick">
        <a-menu-item key="profile">
          <user-outlined />
          个人中心
        </a-menu-item>
        <a-menu-item key="settings">
          <setting-outlined />
          个人设置
        </a-menu-item>
        <a-menu-divider />
        <a-menu-item key="logout">
          <logout-outlined />
          退出登录
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import {
  DownOutlined,
  UserOutlined,
  SettingOutlined,
  LogoutOutlined
} from '@ant-design/icons-vue'
import { useAuthStore } from '@/store/modules/auth'
import { useRouterPush } from '@/hooks/common/use-router'

// Props定义
interface Props {
  /** 头像大小 */
  size?: number | string
  /** 是否显示用户信息 */
  showInfo?: boolean
  /** 是否显示下拉箭头 */
  showArrow?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  size: 32,
  showInfo: true,
  showArrow: true
})

// Emits定义
interface Emits {
  (e: 'profile'): void
  (e: 'settings'): void
  (e: 'logout'): void
}

const emit = defineEmits<Emits>()

// Store和路由
const authStore = useAuthStore()
const { routerPush } = useRouterPush()

// 计算属性
const { userInfo } = storeToRefs(authStore)

const avatar = computed(() => userInfo.value?.avatar)
const nickname = computed(() => userInfo.value?.nickname || '用户')
const roleText = computed(() => {
  const roles = userInfo.value?.roles || []
  return roles.length > 0 ? roles[0] : '普通用户'
})

const avatarText = computed(() => {
  if (avatar.value) return ''
  return nickname.value.charAt(0).toUpperCase()
})

const avatarColor = computed(() => {
  if (avatar.value) return 'transparent'
  // 根据用户名生成颜色
  const colors = ['#f56a00', '#7265e6', '#ffbf00', '#00a2ae']
  const index = nickname.value.charCodeAt(0) % colors.length
  return colors[index]
})

// 方法定义
const handleMenuClick = ({ key }: { key: string }) => {
  switch (key) {
    case 'profile':
      emit('profile')
      routerPush({ name: 'user-center' })
      break
    case 'settings':
      emit('settings')
      routerPush({ name: 'user-settings' })
      break
    case 'logout':
      emit('logout')
      authStore.logout()
      break
  }
}
</script>

<style scoped>
.user-avatar {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: rgba(0, 0, 0, 0.04);
  }

  .user-info {
    display: flex;
    flex-direction: column;

    .user-name {
      font-size: 14px;
      font-weight: 500;
      line-height: 1.2;
    }

    .user-role {
      font-size: 12px;
      color: #666;
      line-height: 1.2;
    }
  }

  .dropdown-arrow {
    font-size: 12px;
    color: #666;
  }
}
</style>
```

## 组件通信模式

### 1. Props和Emits

```vue
<!-- 父组件 -->
<template>
  <user-form
    v-model:visible="formVisible"
    :user-data="currentUser"
    @submit="handleSubmit"
    @cancel="handleCancel"
  />
</template>

<!-- 子组件 -->
<script setup lang="ts">
// Props定义
interface Props {
  visible: boolean
  userData?: UserData
}

// Emits定义
interface Emits {
  (e: 'update:visible', visible: boolean): void
  (e: 'submit', data: UserData): void
  (e: 'cancel'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 双向绑定
const visible = useVModel(props, 'visible', emit)
</script>
```

### 2. Provide/Inject

```vue
<!-- 祖先组件 -->
<script setup lang="ts">
import { provide } from 'vue'

const tableConfig = reactive({
  size: 'middle',
  bordered: false
})

provide('tableConfig', tableConfig)
</script>

<!-- 后代组件 -->
<script setup lang="ts">
import { inject } from 'vue'

const tableConfig = inject('tableConfig', {
  size: 'middle',
  bordered: false
})
</script>
```

### 3. 事件总线

```typescript
// src/utils/event-bus.ts
import mitt from 'mitt'

type Events = {
  'user:updated': { id: number; data: any }
  'theme:changed': { theme: string }
  'notification:show': { message: string; type: string }
}

export const eventBus = mitt<Events>()

// 使用示例
// 发送事件
eventBus.emit('user:updated', { id: 1, data: userData })

// 监听事件
eventBus.on('user:updated', (data) => {
  console.log('用户更新:', data)
})
```

## 组件样式规范

### 1. CSS Modules

```vue
<template>
  <div :class="$style.container">
    <h1 :class="$style.title">标题</h1>
    <p :class="$style.content">内容</p>
  </div>
</template>

<style module>
.container {
  padding: 16px;
  background: #fff;
  border-radius: 6px;
}

.title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
}

.content {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
}
</style>
```

### 2. Scoped样式

```vue
<style scoped>
.component {
  /* 组件根样式 */

  :deep(.ant-btn) {
    /* 深度选择器，修改子组件样式 */
    border-radius: 4px;
  }

  :global(.global-class) {
    /* 全局样式 */
    color: red;
  }
}
</style>
```

### 3. CSS变量

```vue
<template>
  <div class="themed-component" :style="cssVars">
    <h1>主题化组件</h1>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useThemeStore } from '@/store/modules/theme'

const themeStore = useThemeStore()

const cssVars = computed(() => ({
  '--primary-color': themeStore.themeColor,
  '--border-radius': '6px',
  '--spacing': '16px'
}))
</script>

<style scoped>
.themed-component {
  padding: var(--spacing);
  border: 1px solid var(--primary-color);
  border-radius: var(--border-radius);
}
</style>
```

## 组件测试

### 1. 单元测试

```typescript
// tests/components/user-avatar.test.ts
import { mount } from '@vue/test-utils'
import { describe, it, expect, vi } from 'vitest'
import UserAvatar from '@/components/custom/user-avatar.vue'

describe('UserAvatar', () => {
  it('renders correctly with user info', () => {
    const wrapper = mount(UserAvatar, {
      props: {
        size: 40,
        showInfo: true
      },
      global: {
        stubs: ['a-avatar', 'a-dropdown', 'a-menu']
      }
    })

    expect(wrapper.find('.user-avatar').exists()).toBe(true)
    expect(wrapper.find('.user-info').exists()).toBe(true)
  })

  it('emits logout event when logout menu item is clicked', async () => {
    const wrapper = mount(UserAvatar)

    await wrapper.find('[data-testid="logout-menu-item"]').trigger('click')

    expect(wrapper.emitted('logout')).toBeTruthy()
  })

  it('generates correct avatar color based on username', () => {
    const wrapper = mount(UserAvatar, {
      props: {
        username: 'John'
      }
    })

    const avatarColor = wrapper.vm.avatarColor
    expect(avatarColor).toMatch(/^#[0-9a-f]{6}$/i)
  })
})
```

### 2. 组件快照测试

```typescript
// tests/components/__snapshots__/user-avatar.test.ts.snap
import { mount } from '@vue/test-utils'
import UserAvatar from '@/components/custom/user-avatar.vue'

describe('UserAvatar Snapshots', () => {
  it('matches snapshot', () => {
    const wrapper = mount(UserAvatar, {
      props: {
        size: 32,
        showInfo: true,
        showArrow: true
      }
    })

    expect(wrapper.html()).toMatchSnapshot()
  })
})
```

## 组件文档

### 1. 组件API文档

```typescript
/**
 * 用户头像组件
 *
 * @example
 * ```vue
 * <user-avatar
 *   :size="40"
 *   :show-info="true"
 *   @logout="handleLogout"
 * />
 * ```
 */
export interface UserAvatarProps {
  /** 头像大小 */
  size?: number | string
  /** 是否显示用户信息 */
  showInfo?: boolean
  /** 是否显示下拉箭头 */
  showArrow?: boolean
}

export interface UserAvatarEmits {
  /** 个人中心事件 */
  (e: 'profile'): void
  /** 个人设置事件 */
  (e: 'settings'): void
  /** 退出登录事件 */
  (e: 'logout'): void
}
```

### 2. Storybook文档

```typescript
// stories/UserAvatar.stories.ts
import type { Meta, StoryObj } from '@storybook/vue3'
import UserAvatar from '@/components/custom/user-avatar.vue'

const meta: Meta<typeof UserAvatar> = {
  title: 'Components/UserAvatar',
  component: UserAvatar,
  parameters: {
    docs: {
      description: {
        component: '用户头像组件，提供用户信息显示和操作菜单'
      }
    }
  },
  argTypes: {
    size: {
      control: { type: 'number' },
      description: '头像大小'
    },
    showInfo: {
      control: { type: 'boolean' },
      description: '是否显示用户信息'
    }
  }
}

export default meta
type Story = StoryObj<typeof UserAvatar>

export const Default: Story = {
  args: {
    size: 32,
    showInfo: true,
    showArrow: true
  }
}

export const Large: Story = {
  args: {
    size: 48,
    showInfo: true,
    showArrow: true
  }
}

export const WithoutInfo: Story = {
  args: {
    size: 32,
    showInfo: false,
    showArrow: false
  }
}
```

## 组件性能优化

### 1. 懒加载组件

```typescript
// 异步组件
const AsyncComponent = defineAsyncComponent({
  loader: () => import('./heavy-component.vue'),
  loadingComponent: LoadingComponent,
  errorComponent: ErrorComponent,
  delay: 200,
  timeout: 3000
})

// 条件渲染
const HeavyComponent = defineAsyncComponent(() =>
  import('./heavy-component.vue')
)
```

### 2. 组件缓存

```vue
<template>
  <keep-alive :include="cacheComponents">
    <component :is="currentComponent" />
  </keep-alive>
</template>

<script setup lang="ts">
const cacheComponents = ['UserList', 'UserDetail']
</script>
```

### 3. 虚拟滚动

```vue
<!-- src/components/advanced/virtual-list.vue -->
<template>
  <div class="virtual-list" :style="{ height: containerHeight + 'px' }">
    <div
      class="virtual-list-phantom"
      :style="{ height: totalHeight + 'px' }"
    />
    <div
      class="virtual-list-content"
      :style="{ transform: `translateY(${offsetY}px)` }"
    >
      <div
        v-for="item in visibleItems"
        :key="item.id"
        class="virtual-list-item"
        :style="{ height: itemHeight + 'px' }"
      >
        <slot :item="item" :index="item.index" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

interface Props {
  items: any[]
  itemHeight: number
  containerHeight: number
}

const props = defineProps<Props>()

const scrollTop = ref(0)

const totalHeight = computed(() => props.items.length * props.itemHeight)
const visibleCount = computed(() => Math.ceil(props.containerHeight / props.itemHeight))
const startIndex = computed(() => Math.floor(scrollTop.value / props.itemHeight))
const endIndex = computed(() => Math.min(startIndex.value + visibleCount.value, props.items.length))
const offsetY = computed(() => startIndex.value * props.itemHeight)

const visibleItems = computed(() => {
  return props.items.slice(startIndex.value, endIndex.value).map((item, index) => ({
    ...item,
    index: startIndex.value + index
  }))
})

const handleScroll = (e: Event) => {
  scrollTop.value = (e.target as HTMLElement).scrollTop
}

onMounted(() => {
  const container = document.querySelector('.virtual-list')
  container?.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  const container = document.querySelector('.virtual-list')
  container?.removeEventListener('scroll', handleScroll)
})
</script>
```

## 最佳实践

### 1. 组件设计原则
- **单一职责**：每个组件只负责一个功能
- **可复用性**：设计通用的、可复用的组件
- **可配置性**：通过props提供灵活的配置
- **可扩展性**：使用插槽和事件支持扩展

### 2. 性能优化
- **按需加载**：使用动态导入懒加载组件
- **组件缓存**：合理使用keep-alive缓存组件
- **虚拟滚动**：处理大量数据时使用虚拟滚动
- **防抖节流**：对频繁触发的事件进行防抖节流

### 3. 可维护性
- **类型安全**：使用TypeScript定义组件接口
- **文档完善**：编写详细的组件文档和示例
- **测试覆盖**：编写单元测试和集成测试
- **代码规范**：遵循统一的代码风格和命名规范

## 常见问题

### Q: 如何在组件间共享状态？

**解决方案**：
```typescript
// 1. 使用Pinia状态管理
const store = useStore()

// 2. 使用Provide/Inject
provide('sharedState', reactive({ count: 0 }))
const sharedState = inject('sharedState')

// 3. 使用事件总线
eventBus.emit('state:update', newState)
```

### Q: 如何优化大列表渲染性能？

**解决方案**：
```vue
<!-- 1. 使用虚拟滚动 -->
<virtual-list :items="items" :item-height="50" />

<!-- 2. 使用分页 -->
<a-table :pagination="{ pageSize: 50 }" />

<!-- 3. 使用懒加载 -->
<infinite-scroll @load="loadMore" />
```

### Q: 如何处理组件的异步数据？

**解决方案**：
```vue
<script setup lang="ts">
const { data, loading, error } = await useFetch('/api/data')

// 或者使用Suspense
</script>

<template>
  <Suspense>
    <AsyncComponent />
    <template #fallback>
      <loading-component />
    </template>
  </Suspense>
</template>
```