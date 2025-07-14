# 菜单管理

菜单管理是Mars Admin权限系统的重要组成部分，负责管理系统的菜单结构、权限控制和动态路由生成。

## 功能概述

菜单管理模块主要包含以下功能：

- **菜单结构管理**：树形菜单的增删改查
- **权限控制**：菜单级别的权限配置
- **动态路由**：基于菜单生成前端路由
- **图标管理**：菜单图标的配置和显示
- **多级菜单**：支持无限级菜单嵌套
- **菜单排序**：自定义菜单显示顺序

## 数据模型

### 菜单实体

```java
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_menu", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "系统菜单")
public class SysMenu extends BaseEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "菜单ID")
    private Long id;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuName;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由参数")
    private String query;

    @Schema(description = "是否为外链（0是 1否）")
    private Integer isFrame;

    @Schema(description = "是否缓存（0缓存 1不缓存）")
    private Integer isCache;

    @Schema(description = "菜单类型（M目录 C菜单 F按钮）")
    private String menuType;

    @Schema(description = "菜单状态（0显示 1隐藏）")
    private String visible;

    @Schema(description = "菜单状态（0正常 1停用）")
    private String status;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "备注")
    private String remark;

    // 非数据库字段
    @Schema(description = "子菜单")
    private List<SysMenu> children;
}
```

### 菜单类型说明

| 菜单类型 | 说明 | 特点 |
|---------|------|------|
| M (目录) | 菜单目录 | 只作为菜单分组，不对应具体页面 |
| C (菜单) | 菜单项 | 对应具体的页面组件 |
| F (按钮) | 功能按钮 | 页面内的操作按钮权限 |

## 核心功能实现

### 1. 菜单列表查询

**接口地址：** `GET /system/menu/list`

**功能描述：** 获取菜单树形结构列表，支持按菜单名称、状态等条件筛选。

**请求参数：**
```json
{
  "menuName": "用户管理",
  "status": "0"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "menuName": "系统管理",
      "parentId": 0,
      "orderNum": 1,
      "path": "/system",
      "component": "",
      "menuType": "M",
      "visible": "0",
      "status": "0",
      "perms": "",
      "icon": "system",
      "children": [
        {
          "id": 100,
          "menuName": "用户管理",
          "parentId": 1,
          "orderNum": 1,
          "path": "user",
          "component": "system/user/index",
          "menuType": "C",
          "visible": "0",
          "status": "0",
          "perms": "system:user:list",
          "icon": "user",
          "children": [
            {
              "id": 1001,
              "menuName": "用户查询",
              "parentId": 100,
              "orderNum": 1,
              "path": "",
              "component": "",
              "menuType": "F",
              "visible": "0",
              "status": "0",
              "perms": "system:user:query",
              "icon": ""
            }
          ]
        }
      ]
    }
  ]
}
```

### 2. 获取路由菜单

**接口地址：** `GET /system/menu/getRouters`

**功能描述：** 获取当前用户的路由菜单，用于前端动态路由生成。

**响应示例：**
```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "name": "System",
      "path": "/system",
      "hidden": false,
      "redirect": "noRedirect",
      "component": "Layout",
      "alwaysShow": true,
      "meta": {
        "title": "系统管理",
        "icon": "system",
        "noCache": false,
        "link": null
      },
      "children": [
        {
          "name": "User",
          "path": "user",
          "hidden": false,
          "component": "system/user/index",
          "meta": {
            "title": "用户管理",
            "icon": "user",
            "noCache": false,
            "link": null
          }
        }
      ]
    }
  ]
}
```

### 3. 新增菜单

**接口地址：** `POST /system/menu`

**请求参数：**
```json
{
  "menuName": "测试菜单",
  "parentId": 1,
  "orderNum": 5,
  "path": "test",
  "component": "system/test/index",
  "menuType": "C",
  "visible": "0",
  "status": "0",
  "perms": "system:test:list",
  "icon": "test",
  "remark": "测试菜单"
}
```

### 4. 修改菜单

**接口地址：** `PUT /system/menu`

### 5. 删除菜单

**接口地址：** `DELETE /system/menu/{menuId}`

**功能描述：** 删除菜单，删除前会检查是否存在子菜单。

## 前端实现

### 1. 菜单管理页面

```vue
<template>
  <div class="menu-manage">
    <!-- 搜索表单 -->
    <a-card class="search-card">
      <a-form layout="inline" :model="searchForm">
        <a-form-item label="菜单名称">
          <a-input v-model:value="searchForm.menuName" placeholder="请输入菜单名称" />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="searchForm.status" placeholder="请选择状态">
            <a-select-option value="0">正常</a-select-option>
            <a-select-option value="1">停用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleSearch">搜索</a-button>
          <a-button @click="handleReset">重置</a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 操作按钮 -->
    <a-card class="table-card">
      <template #title>
        <a-space>
          <a-button type="primary" @click="handleAdd">
            <template #icon><plus-outlined /></template>
            新增菜单
          </a-button>
          <a-button @click="handleExpandAll">
            <template #icon><menu-unfold-outlined /></template>
            展开/折叠
          </a-button>
        </a-space>
      </template>

      <!-- 菜单树表格 -->
      <a-table
        :columns="columns"
        :data-source="menuList"
        :pagination="false"
        :loading="loading"
        row-key="id"
        :default-expand-all-rows="isExpandAll"
        :expand-row-by-click="true"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'icon'">
            <svg-icon v-if="record.icon" :name="record.icon" />
          </template>
          <template v-if="column.key === 'menuType'">
            <a-tag v-if="record.menuType === 'M'" color="blue">目录</a-tag>
            <a-tag v-else-if="record.menuType === 'C'" color="green">菜单</a-tag>
            <a-tag v-else-if="record.menuType === 'F'" color="orange">按钮</a-tag>
          </template>
          <template v-if="column.key === 'status'">
            <a-switch
              :checked="record.status === '0'"
              @change="(checked) => handleStatusChange(record, checked)"
            />
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-button type="link" size="small" @click="handleAddChild(record)">新增</a-button>
              <a-popconfirm title="确定删除此菜单吗？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 菜单编辑弹窗 -->
    <menu-edit-modal
      v-model:visible="editModalVisible"
      :menu-data="currentMenu"
      :menu-tree="menuTree"
      @success="handleEditSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined, MenuUnfoldOutlined } from '@ant-design/icons-vue'
import { fetchMenuList, deleteMenu, changeMenuStatus } from '@/service/api/menu'
import MenuEditModal from './components/menu-edit-modal.vue'
import SvgIcon from '@/components/common/svg-icon.vue'

// 响应式数据
const loading = ref(false)
const menuList = ref([])
const menuTree = ref([])
const isExpandAll = ref(false)
const editModalVisible = ref(false)
const currentMenu = ref(null)

// 搜索表单
const searchForm = reactive({
  menuName: '',
  status: undefined
})

// 表格列配置
const columns = [
  {
    title: '菜单名称',
    dataIndex: 'menuName',
    key: 'menuName',
    width: 200
  },
  {
    title: '图标',
    dataIndex: 'icon',
    key: 'icon',
    width: 80,
    align: 'center'
  },
  {
    title: '排序',
    dataIndex: 'orderNum',
    key: 'orderNum',
    width: 80,
    align: 'center'
  },
  {
    title: '权限标识',
    dataIndex: 'perms',
    key: 'perms',
    width: 150
  },
  {
    title: '组件路径',
    dataIndex: 'component',
    key: 'component',
    width: 200
  },
  {
    title: '类型',
    dataIndex: 'menuType',
    key: 'menuType',
    width: 80,
    align: 'center'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 80,
    align: 'center'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 180
  },
  {
    title: '操作',
    key: 'action',
    width: 200,
    fixed: 'right'
  }
]

// 方法定义
const loadData = async () => {
  loading.value = true
  try {
    const { data } = await fetchMenuList(searchForm)
    menuList.value = data
    menuTree.value = buildMenuTree(data)
  } catch (error) {
    message.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const buildMenuTree = (menus) => {
  const tree = []
  const map = {}
  
  // 创建映射
  menus.forEach(menu => {
    map[menu.id] = { ...menu, children: [] }
  })
  
  // 构建树形结构
  menus.forEach(menu => {
    if (menu.parentId === 0) {
      tree.push(map[menu.id])
    } else {
      if (map[menu.parentId]) {
        map[menu.parentId].children.push(map[menu.id])
      }
    }
  })
  
  return tree
}

const handleSearch = () => {
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    menuName: '',
    status: undefined
  })
  loadData()
}

const handleAdd = () => {
  currentMenu.value = null
  editModalVisible.value = true
}

const handleAddChild = (record) => {
  currentMenu.value = { parentId: record.id }
  editModalVisible.value = true
}

const handleEdit = (record) => {
  currentMenu.value = record
  editModalVisible.value = true
}

const handleDelete = async (record) => {
  try {
    await deleteMenu(record.id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

const handleStatusChange = async (record, checked) => {
  try {
    await changeMenuStatus({
      id: record.id,
      status: checked ? '0' : '1'
    })
    record.status = checked ? '0' : '1'
    message.success('状态修改成功')
  } catch (error) {
    message.error('状态修改失败')
  }
}

const handleExpandAll = () => {
  isExpandAll.value = !isExpandAll.value
}

const handleEditSuccess = () => {
  editModalVisible.value = false
  loadData()
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>
```

### 2. 菜单编辑弹窗

```vue
<template>
  <a-modal
    v-model:visible="visible"
    :title="isEdit ? '编辑菜单' : '新增菜单'"
    width="800px"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form ref="formRef" :model="formData" :rules="rules" layout="vertical">
      <a-row :gutter="16">
        <a-col :span="24">
          <a-form-item label="上级菜单" name="parentId">
            <a-tree-select
              v-model:value="formData.parentId"
              :tree-data="parentMenuOptions"
              :field-names="{ children: 'children', label: 'menuName', value: 'id' }"
              placeholder="选择上级菜单"
              tree-default-expand-all
              allow-clear
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="菜单类型" name="menuType">
            <a-radio-group v-model:value="formData.menuType" @change="handleMenuTypeChange">
              <a-radio value="M">目录</a-radio>
              <a-radio value="C">菜单</a-radio>
              <a-radio value="F">按钮</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="菜单图标" name="icon">
            <icon-selector v-model:value="formData.icon" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="菜单名称" name="menuName">
            <a-input v-model:value="formData.menuName" placeholder="请输入菜单名称" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="显示排序" name="orderNum">
            <a-input-number v-model:value="formData.orderNum" :min="0" style="width: 100%" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16" v-if="formData.menuType !== 'F'">
        <a-col :span="12">
          <a-form-item label="路由地址" name="path">
            <a-input v-model:value="formData.path" placeholder="请输入路由地址" />
          </a-form-item>
        </a-col>
        <a-col :span="12" v-if="formData.menuType === 'C'">
          <a-form-item label="组件路径" name="component">
            <a-input v-model:value="formData.component" placeholder="请输入组件路径" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16" v-if="formData.menuType !== 'M'">
        <a-col :span="24">
          <a-form-item label="权限标识" name="perms">
            <a-input v-model:value="formData.perms" placeholder="请输入权限标识" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16" v-if="formData.menuType !== 'F'">
        <a-col :span="12">
          <a-form-item label="路由参数" name="query">
            <a-input v-model:value="formData.query" placeholder="请输入路由参数" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="是否缓存">
            <a-radio-group v-model:value="formData.isCache">
              <a-radio :value="0">缓存</a-radio>
              <a-radio :value="1">不缓存</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="显示状态">
            <a-radio-group v-model:value="formData.visible">
              <a-radio value="0">显示</a-radio>
              <a-radio value="1">隐藏</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="菜单状态">
            <a-radio-group v-model:value="formData.status">
              <a-radio value="0">正常</a-radio>
              <a-radio value="1">停用</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item label="备注">
        <a-textarea v-model:value="formData.remark" placeholder="请输入备注" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { message } from 'ant-design-vue'
import { createMenu, updateMenu } from '@/service/api/menu'
import IconSelector from '@/components/common/icon-selector.vue'

// Props和Emits
interface Props {
  visible: boolean
  menuData?: any
  menuTree: any[]
}

interface Emits {
  (e: 'update:visible', visible: boolean): void
  (e: 'success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 响应式数据
const formRef = ref()

const formData = reactive({
  id: undefined,
  parentId: 0,
  menuName: '',
  orderNum: 0,
  path: '',
  component: '',
  query: '',
  isFrame: 1,
  isCache: 0,
  menuType: 'M',
  visible: '0',
  status: '0',
  perms: '',
  icon: '',
  remark: ''
})

// 表单验证规则
const rules = {
  menuName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '请输入显示排序', trigger: 'blur' }
  ],
  path: [
    { required: true, message: '请输入路由地址', trigger: 'blur' }
  ]
}

// 计算属性
const isEdit = computed(() => !!props.menuData?.id)

const parentMenuOptions = computed(() => {
  const options = [{ id: 0, menuName: '主类目', children: [] }]
  return options.concat(props.menuTree)
})

// 监听器
watch(() => props.visible, (visible) => {
  if (visible) {
    if (props.menuData) {
      Object.assign(formData, props.menuData)
    } else {
      resetForm()
    }
  }
})

// 方法定义
const resetForm = () => {
  Object.assign(formData, {
    id: undefined,
    parentId: 0,
    menuName: '',
    orderNum: 0,
    path: '',
    component: '',
    query: '',
    isFrame: 1,
    isCache: 0,
    menuType: 'M',
    visible: '0',
    status: '0',
    perms: '',
    icon: '',
    remark: ''
  })
  formRef.value?.resetFields()
}

const handleMenuTypeChange = () => {
  if (formData.menuType === 'F') {
    formData.path = ''
    formData.component = ''
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()

    if (isEdit.value) {
      await updateMenu(formData)
      message.success('修改成功')
    } else {
      await createMenu(formData)
      message.success('新增成功')
    }

    emit('success')
  } catch (error) {
    if (error.errorFields) {
      message.error('请检查表单输入')
    } else {
      message.error(isEdit.value ? '修改失败' : '新增失败')
    }
  }
}

const handleCancel = () => {
  emit('update:visible', false)
}
</script>
```

## 动态路由生成

### 1. 后端路由数据转换

```java
@Service
public class SysMenuServiceImpl implements ISysMenuService {

    /**
     * 构建前端路由
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new ArrayList<>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(),
                StringUtils.equals("1", menu.getIsCache()), menu.getPath()));

            List<SysMenu> cMenus = menu.getChildren();
            if (CollectionUtils.isNotEmpty(cMenus) && "M".equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(),
                    StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/");
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent("InnerLink");
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }
}
```

### 2. 前端路由注册

```typescript
// src/utils/router.ts
export function transformMenuToRoutes(menus: any[]): RouteRecordRaw[] {
  const routes: RouteRecordRaw[] = []

  menus.forEach(menu => {
    const route: RouteRecordRaw = {
      path: menu.path,
      name: menu.name,
      component: resolveComponent(menu.component),
      meta: {
        title: menu.meta.title,
        icon: menu.meta.icon,
        noCache: menu.meta.noCache,
        link: menu.meta.link,
        hidden: menu.hidden,
        roles: menu.roles,
        permissions: menu.permissions
      }
    }

    if (menu.children && menu.children.length > 0) {
      route.children = transformMenuToRoutes(menu.children)
    }

    routes.push(route)
  })

  return routes
}

function resolveComponent(component: string) {
  if (component === 'Layout') {
    return () => import('@/layouts/base-layout/index.vue')
  }

  return () => import(`@/views/${component}.vue`)
}
```

## 权限控制

### 1. 菜单权限验证

```java
@Component
public class MenuPermissionEvaluator {

    /**
     * 检查菜单权限
     */
    public boolean hasMenuPermission(String permission) {
        // 获取当前用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return false;
        }

        // 超级管理员拥有所有权限
        if (loginUser.getUser().isAdmin()) {
            return true;
        }

        // 检查用户权限
        Set<String> permissions = loginUser.getPermissions();
        return permissions.contains(permission);
    }

    /**
     * 过滤用户菜单
     */
    public List<SysMenu> filterUserMenus(List<SysMenu> menus, Set<String> permissions) {
        List<SysMenu> userMenus = new ArrayList<>();

        for (SysMenu menu : menus) {
            SysMenu menuCopy = new SysMenu();
            BeanUtils.copyProperties(menu, menuCopy);

            // 检查菜单权限
            if (hasPermission(menu.getPerms(), permissions)) {
                // 递归过滤子菜单
                if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                    List<SysMenu> childMenus = filterUserMenus(menu.getChildren(), permissions);
                    menuCopy.setChildren(childMenus);
                }
                userMenus.add(menuCopy);
            }
        }

        return userMenus;
    }

    private boolean hasPermission(String permission, Set<String> userPermissions) {
        if (StringUtils.isEmpty(permission)) {
            return true;
        }
        return userPermissions.contains(permission);
    }
}
```

### 2. 前端权限指令

```typescript
// src/directives/permission.ts
import type { Directive } from 'vue'
import { useAuthStore } from '@/store/modules/auth'

export const permission: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const authStore = useAuthStore()

    if (value && value instanceof Array && value.length > 0) {
      const hasPermission = authStore.hasAnyPermission(value)
      if (!hasPermission) {
        el.parentNode?.removeChild(el)
      }
    } else {
      throw new Error('权限指令需要传入权限数组')
    }
  }
}

// 使用示例
// <a-button v-permission="['system:menu:add']">新增菜单</a-button>
```

## 最佳实践

### 1. 菜单设计原则
- **层级清晰**：菜单层级不宜过深，建议不超过3级
- **命名规范**：菜单名称要简洁明了，权限标识要规范
- **图标统一**：使用统一的图标库，保持视觉一致性
- **权限细化**：合理设计权限粒度，既要安全又要易用

### 2. 性能优化
- **懒加载**：大型应用使用路由懒加载
- **缓存策略**：合理使用页面缓存提升用户体验
- **权限缓存**：缓存用户权限信息减少查询
- **菜单预加载**：预加载常用菜单组件

### 3. 安全考虑
- **前后端双重验证**：前端控制显示，后端控制访问
- **权限最小化**：遵循最小权限原则
- **敏感操作确认**：重要操作需要二次确认
- **审计日志**：记录菜单权限变更日志

## 常见问题

### Q: 如何实现菜单的国际化？

**解决方案**：
```typescript
// 在菜单数据中添加国际化key
const menu = {
  menuName: 'system.user.title',
  // ...
}

// 在前端使用国际化函数
const title = computed(() => t(menu.menuName))
```

### Q: 如何处理外链菜单？

**解决方案**：
```java
// 后端判断外链
if (isExternalLink(menu.getPath())) {
    router.setPath(menu.getPath());
    router.setComponent("InnerLink");
}

// 前端处理外链
if (route.meta.link) {
    window.open(route.meta.link, '_blank')
}
```

### Q: 如何实现菜单的动态显示/隐藏？

**解决方案**：
```typescript
// 基于条件动态控制菜单显示
const shouldShowMenu = computed(() => {
  return hasPermission(menu.perms) && menu.visible === '0'
})
```
```
