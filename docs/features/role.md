# 角色管理

角色管理是Mars Admin权限系统的核心模块，基于RBAC（Role-Based Access Control）模型设计，提供灵活的角色权限分配和管理功能。

## 功能概述

角色管理模块主要包含以下功能：

- **角色信息管理**：角色的基本信息维护
- **权限分配**：为角色分配菜单权限和按钮权限
- **数据权限控制**：设置角色的数据访问范围
- **用户关联**：管理角色与用户的关联关系
- **角色状态管理**：启用/禁用角色

## 数据模型

### 角色实体

```java
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_role", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "系统角色")
public class SysRole extends BaseEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleName;

    @Schema(description = "角色权限字符串", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleKey;

    @Schema(description = "显示顺序")
    private Integer roleSort;

    @Schema(description = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    private Integer dataScope;

    @Schema(description = "菜单树选择项是否关联显示")
    private Boolean menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示")
    private Boolean deptCheckStrictly;

    @Schema(description = "角色状态（0停用 1正常）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    // 关联字段
    @Schema(description = "菜单ID列表")
    private List<Long> menuIds;

    @Schema(description = "部门ID列表")
    private List<Long> deptIds;
}
```

### 数据权限类型

| 数据范围值 | 说明 | 描述 |
|-----------|------|------|
| 1 | 全部数据权限 | 可以查看所有数据 |
| 2 | 自定数据权限 | 只能查看指定部门的数据 |
| 3 | 本部门数据权限 | 只能查看本部门的数据 |
| 4 | 本部门及以下数据权限 | 可以查看本部门及下级部门的数据 |
| 5 | 仅本人数据权限 | 只能查看自己的数据 |

## 核心功能实现

### 1. 角色列表查询

**接口地址：** `GET /system/role/list`

**功能描述：** 分页查询角色列表，支持按角色名称、权限字符串、状态等条件筛选。

**请求参数：**
```json
{
  "current": 1,
  "size": 10,
  "roleName": "管理员",
  "roleKey": "admin",
  "status": 1
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "id": 1,
        "roleName": "超级管理员",
        "roleKey": "admin",
        "roleSort": 1,
        "dataScope": 1,
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "remark": "超级管理员"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 2. 角色详情查询

**接口地址：** `GET /system/role/{id}`

**功能描述：** 根据角色ID查询角色详细信息，包括关联的菜单权限和部门权限。

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "id": 1,
    "roleName": "超级管理员",
    "roleKey": "admin",
    "roleSort": 1,
    "dataScope": 1,
    "menuCheckStrictly": true,
    "deptCheckStrictly": true,
    "status": 1,
    "remark": "超级管理员",
    "menuIds": [1, 2, 3, 100, 101, 102],
    "deptIds": [1, 2, 3]
  }
}
```

### 3. 新增角色

**接口地址：** `POST /system/role`

**功能描述：** 创建新的角色，并分配相应的权限。

**请求参数：**
```json
{
  "roleName": "部门管理员",
  "roleKey": "dept_admin",
  "roleSort": 2,
  "dataScope": 3,
  "menuCheckStrictly": true,
  "deptCheckStrictly": true,
  "status": 1,
  "remark": "部门管理员角色",
  "menuIds": [1, 2, 100, 101],
  "deptIds": [2]
}
```

### 4. 修改角色

**接口地址：** `PUT /system/role`

**功能描述：** 更新角色信息和权限配置。

### 5. 删除角色

**接口地址：** `DELETE /system/role/{ids}`

**功能描述：** 删除一个或多个角色，删除前会检查角色是否被用户使用。

### 6. 角色状态切换

**接口地址：** `PUT /system/role/changeStatus`

**功能描述：** 启用或禁用角色。

**请求参数：**
```json
{
  "id": 2,
  "status": 0
}
```

## 权限分配功能

### 1. 菜单权限分配

角色可以分配以下类型的菜单权限：

- **目录权限**：访问菜单目录的权限
- **菜单权限**：访问具体页面的权限  
- **按钮权限**：页面内按钮操作的权限

**权限树结构示例：**
```json
{
  "id": 1,
  "label": "系统管理",
  "children": [
    {
      "id": 100,
      "label": "用户管理",
      "children": [
        {
          "id": 1001,
          "label": "用户查询"
        },
        {
          "id": 1002,
          "label": "用户新增"
        },
        {
          "id": 1003,
          "label": "用户修改"
        },
        {
          "id": 1004,
          "label": "用户删除"
        }
      ]
    }
  ]
}
```

### 2. 数据权限配置

**全部数据权限：**
```sql
-- 查询所有用户数据
SELECT * FROM sys_user WHERE is_deleted = 0
```

**本部门数据权限：**
```sql
-- 只查询本部门用户数据
SELECT u.* FROM sys_user u
LEFT JOIN sys_user_dept ud ON u.id = ud.user_id
WHERE u.is_deleted = 0 AND ud.dept_id = #{currentUserDeptId}
```

**本部门及以下数据权限：**
```sql
-- 查询本部门及下级部门用户数据
SELECT u.* FROM sys_user u
LEFT JOIN sys_user_dept ud ON u.id = ud.user_id
LEFT JOIN sys_dept d ON ud.dept_id = d.id
WHERE u.is_deleted = 0 
AND (d.id = #{currentUserDeptId} OR d.ancestors LIKE CONCAT('%,', #{currentUserDeptId}, ',%'))
```

**自定数据权限：**
```sql
-- 查询指定部门用户数据
SELECT u.* FROM sys_user u
LEFT JOIN sys_user_dept ud ON u.id = ud.user_id
WHERE u.is_deleted = 0 AND ud.dept_id IN (#{deptIds})
```

## 前端实现

### 1. 角色列表页面

```vue
<template>
  <div class="role-manage">
    <!-- 搜索表单 -->
    <a-card class="search-card">
      <a-form layout="inline" :model="searchForm">
        <a-form-item label="角色名称">
          <a-input v-model:value="searchForm.roleName" placeholder="请输入角色名称" />
        </a-form-item>
        <a-form-item label="权限字符">
          <a-input v-model:value="searchForm.roleKey" placeholder="请输入权限字符" />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="searchForm.status" placeholder="请选择状态">
            <a-select-option :value="1">正常</a-select-option>
            <a-select-option :value="0">停用</a-select-option>
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
            新增角色
          </a-button>
          <a-button type="danger" :disabled="!selectedRowKeys.length" @click="handleBatchDelete">
            <template #icon><delete-outlined /></template>
            批量删除
          </a-button>
        </a-space>
      </template>

      <!-- 数据表格 -->
      <a-table
        :columns="columns"
        :data-source="dataList"
        :pagination="pagination"
        :loading="loading"
        :row-selection="rowSelection"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-switch
              :checked="record.status === 1"
              @change="(checked) => handleStatusChange(record, checked)"
            />
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-button type="link" size="small" @click="handleAssignUser(record)">分配用户</a-button>
              <a-popconfirm title="确定删除此角色吗？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 角色编辑弹窗 -->
    <role-edit-modal
      v-model:visible="editModalVisible"
      :role-data="currentRole"
      @success="handleEditSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import { fetchRoleList, deleteRole, changeRoleStatus } from '@/service/api/role'
import RoleEditModal from './components/role-edit-modal.vue'

// 响应式数据
const loading = ref(false)
const dataList = ref([])
const selectedRowKeys = ref([])
const editModalVisible = ref(false)
const currentRole = ref(null)

// 搜索表单
const searchForm = reactive({
  roleName: '',
  roleKey: '',
  status: undefined
})

// 分页配置
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true
})

// 表格列配置
const columns = [
  {
    title: '角色编号',
    dataIndex: 'id',
    key: 'id',
    width: 100
  },
  {
    title: '角色名称',
    dataIndex: 'roleName',
    key: 'roleName'
  },
  {
    title: '权限字符',
    dataIndex: 'roleKey',
    key: 'roleKey'
  },
  {
    title: '显示顺序',
    dataIndex: 'roleSort',
    key: 'roleSort',
    width: 100
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100
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

// 行选择配置
const rowSelection = {
  selectedRowKeys,
  onChange: (keys: string[]) => {
    selectedRowKeys.value = keys
  }
}

// 方法定义
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.pageSize,
      ...searchForm
    }
    const { data } = await fetchRoleList(params)
    dataList.value = data.records
    pagination.total = data.total
  } catch (error) {
    message.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    roleName: '',
    roleKey: '',
    status: undefined
  })
  handleSearch()
}

const handleAdd = () => {
  currentRole.value = null
  editModalVisible.value = true
}

const handleEdit = (record: any) => {
  currentRole.value = record
  editModalVisible.value = true
}

const handleDelete = async (record: any) => {
  try {
    await deleteRole(record.id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

const handleBatchDelete = async () => {
  try {
    await deleteRole(selectedRowKeys.value.join(','))
    message.success('批量删除成功')
    selectedRowKeys.value = []
    loadData()
  } catch (error) {
    message.error('批量删除失败')
  }
}

const handleStatusChange = async (record: any, checked: boolean) => {
  try {
    await changeRoleStatus({
      id: record.id,
      status: checked ? 1 : 0
    })
    record.status = checked ? 1 : 0
    message.success('状态修改成功')
  } catch (error) {
    message.error('状态修改失败')
  }
}

const handleTableChange = (pag: any) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  loadData()
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

### 2. 角色编辑弹窗

```vue
<template>
  <a-modal
    v-model:visible="visible"
    :title="isEdit ? '编辑角色' : '新增角色'"
    width="800px"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form ref="formRef" :model="formData" :rules="rules" layout="vertical">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="角色名称" name="roleName">
            <a-input v-model:value="formData.roleName" placeholder="请输入角色名称" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="权限字符" name="roleKey">
            <a-input v-model:value="formData.roleKey" placeholder="请输入权限字符" />
          </a-form-item>
        </a-col>
      </a-row>
      
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="显示顺序" name="roleSort">
            <a-input-number v-model:value="formData.roleSort" :min="0" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="状态" name="status">
            <a-radio-group v-model:value="formData.status">
              <a-radio :value="1">正常</a-radio>
              <a-radio :value="0">停用</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item label="数据权限" name="dataScope">
        <a-select v-model:value="formData.dataScope" placeholder="请选择数据权限">
          <a-select-option :value="1">全部数据权限</a-select-option>
          <a-select-option :value="2">自定数据权限</a-select-option>
          <a-select-option :value="3">本部门数据权限</a-select-option>
          <a-select-option :value="4">本部门及以下数据权限</a-select-option>
          <a-select-option :value="5">仅本人数据权限</a-select-option>
        </a-select>
      </a-form-item>

      <!-- 菜单权限树 -->
      <a-form-item label="菜单权限">
        <a-tree
          v-model:checkedKeys="formData.menuIds"
          :tree-data="menuTreeData"
          :checkable="true"
          :check-strictly="!formData.menuCheckStrictly"
          :selectable="false"
        />
        <a-checkbox v-model:checked="formData.menuCheckStrictly" style="margin-top: 8px">
          父子联动
        </a-checkbox>
      </a-form-item>

      <!-- 数据权限部门树（仅自定数据权限时显示） -->
      <a-form-item v-if="formData.dataScope === 2" label="数据权限">
        <a-tree
          v-model:checkedKeys="formData.deptIds"
          :tree-data="deptTreeData"
          :checkable="true"
          :check-strictly="!formData.deptCheckStrictly"
          :selectable="false"
        />
        <a-checkbox v-model:checked="formData.deptCheckStrictly" style="margin-top: 8px">
          父子联动
        </a-checkbox>
      </a-form-item>

      <a-form-item label="备注">
        <a-textarea v-model:value="formData.remark" placeholder="请输入备注" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { message } from 'ant-design-vue'
import { createRole, updateRole, getRoleDetail } from '@/service/api/role'
import { getMenuTree } from '@/service/api/menu'
import { getDeptTree } from '@/service/api/dept'

// Props和Emits
interface Props {
  visible: boolean
  roleData?: any
}

interface Emits {
  (e: 'update:visible', visible: boolean): void
  (e: 'success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 响应式数据
const formRef = ref()
const menuTreeData = ref([])
const deptTreeData = ref([])

const formData = reactive({
  id: undefined,
  roleName: '',
  roleKey: '',
  roleSort: 0,
  dataScope: 1,
  menuCheckStrictly: true,
  deptCheckStrictly: true,
  status: 1,
  remark: '',
  menuIds: [],
  deptIds: []
})

// 表单验证规则
const rules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleKey: [
    { required: true, message: '请输入权限字符', trigger: 'blur' }
  ],
  roleSort: [
    { required: true, message: '请输入显示顺序', trigger: 'blur' }
  ]
}

// 计算属性
const isEdit = computed(() => !!props.roleData?.id)

// 监听器
watch(() => props.visible, (visible) => {
  if (visible) {
    loadMenuTree()
    loadDeptTree()
    if (props.roleData) {
      loadRoleDetail(props.roleData.id)
    } else {
      resetForm()
    }
  }
})

// 方法定义
const loadMenuTree = async () => {
  try {
    const { data } = await getMenuTree()
    menuTreeData.value = data
  } catch (error) {
    message.error('加载菜单树失败')
  }
}

const loadDeptTree = async () => {
  try {
    const { data } = await getDeptTree()
    deptTreeData.value = data
  } catch (error) {
    message.error('加载部门树失败')
  }
}

const loadRoleDetail = async (id: number) => {
  try {
    const { data } = await getRoleDetail(id)
    Object.assign(formData, data)
  } catch (error) {
    message.error('加载角色详情失败')
  }
}

const resetForm = () => {
  Object.assign(formData, {
    id: undefined,
    roleName: '',
    roleKey: '',
    roleSort: 0,
    dataScope: 1,
    menuCheckStrictly: true,
    deptCheckStrictly: true,
    status: 1,
    remark: '',
    menuIds: [],
    deptIds: []
  })
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    if (isEdit.value) {
      await updateRole(formData)
      message.success('修改成功')
    } else {
      await createRole(formData)
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

## 权限验证

### 1. 后端权限验证

```java
@RestController
@RequestMapping("/system/user")
public class SysUserController {
    
    @SaCheckPermission("system:user:list")
    @GetMapping("/list")
    public Result<Page<SysUser>> list(@RequestParam Map<String, Object> params) {
        // 用户列表查询逻辑
    }
    
    @SaCheckPermission("system:user:add")
    @PostMapping
    public Result<Void> add(@RequestBody SysUser user) {
        // 用户新增逻辑
    }
    
    @SaCheckPermission("system:user:edit")
    @PutMapping
    public Result<Void> edit(@RequestBody SysUser user) {
        // 用户修改逻辑
    }
    
    @SaCheckPermission("system:user:remove")
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@PathVariable String ids) {
        // 用户删除逻辑
    }
}
```

### 2. 前端权限验证

```vue
<template>
  <div>
    <!-- 使用权限指令控制按钮显示 -->
    <a-button v-permission="'system:user:add'" type="primary">
      新增用户
    </a-button>
    
    <a-button v-permission="'system:user:edit'" type="default">
      编辑用户
    </a-button>
    
    <a-button v-permission="'system:user:remove'" type="danger">
      删除用户
    </a-button>
  </div>
</template>

<script setup lang="ts">
import { usePermission } from '@/hooks/business/use-permission'

const { hasPermission } = usePermission()

// 在代码中检查权限
const handleEdit = () => {
  if (!hasPermission('system:user:edit')) {
    message.warning('您没有编辑权限')
    return
  }
  // 执行编辑逻辑
}
</script>
```

## 最佳实践

### 1. 角色设计原则
- **最小权限原则**：角色只分配必要的权限
- **职责分离**：不同职责的用户分配不同角色
- **权限继承**：合理设计角色层级关系
- **定期审查**：定期检查和调整角色权限

### 2. 数据权限设计
- **明确数据边界**：清楚定义数据访问范围
- **灵活配置**：支持多种数据权限类型
- **性能考虑**：避免复杂的数据权限查询影响性能
- **安全防护**：防止数据权限绕过和越权访问

### 3. 权限缓存优化
- **缓存用户权限**：将用户权限信息缓存到Redis
- **权限变更通知**：权限变更时及时清除相关缓存
- **批量权限检查**：减少权限检查的数据库查询次数

## 常见问题

### Q: 如何实现角色继承？

**解决方案**：
1. 在角色表中添加父角色字段
2. 权限检查时递归查询父角色权限
3. 使用缓存优化权限查询性能
4. 避免循环继承关系

### Q: 如何处理角色权限冲突？

**解决方案**：
1. 制定明确的权限优先级规则
2. 使用权限合并策略（取并集或交集）
3. 提供权限冲突检测和提示
4. 记录权限变更日志便于追踪

### Q: 如何实现动态权限控制？

**解决方案**：
1. 使用动态路由根据权限生成菜单
2. 实现权限指令控制页面元素显示
3. 在API层面进行权限验证
4. 提供权限实时刷新机制
