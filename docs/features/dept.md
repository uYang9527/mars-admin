# 部门管理

部门管理是Mars Admin组织架构管理的核心模块，提供了完整的部门层级管理、数据权限控制和组织架构维护功能。

## 功能概述

部门管理模块主要包含以下功能：

- **部门层级管理**：树形结构的部门组织架构
- **数据权限控制**：基于部门的数据访问权限
- **部门信息维护**：部门基本信息的增删改查
- **负责人管理**：部门负责人的指定和变更
- **部门状态控制**：部门的启用和停用管理
- **组织架构展示**：可视化的组织架构图

## 数据模型

### 部门实体

```java
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_dept", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "部门信息")
public class SysDept extends BaseEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "部门id")
    private Long id;

    @Schema(description = "父部门id")
    private Long parentId;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deptName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门状态（0正常 1停用）")
    private String status;

    @Schema(description = "备注")
    private String remark;

    // 非数据库字段
    @Schema(description = "子部门")
    private List<SysDept> children;

    @Schema(description = "父部门名称")
    private String parentName;
}
```

### 祖级列表说明

祖级列表（ancestors）字段用于快速查询部门的所有上级部门，格式为：`0,1,2,3`

- **根部门**：ancestors = "0"
- **一级部门**：ancestors = "0,1"
- **二级部门**：ancestors = "0,1,2"
- **三级部门**：ancestors = "0,1,2,3"

## 核心功能实现

### 1. 部门列表查询

**接口地址：** `GET /system/dept/list`

**功能描述：** 获取部门树形结构列表，支持按部门名称、状态等条件筛选。

**请求参数：**
```json
{
  "deptName": "技术部",
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
      "parentId": 0,
      "ancestors": "0",
      "deptName": "总公司",
      "orderNum": 0,
      "leader": "张三",
      "phone": "13800138000",
      "email": "admin@example.com",
      "status": "0",
      "createTime": "2025-01-01T00:00:00",
      "children": [
        {
          "id": 2,
          "parentId": 1,
          "ancestors": "0,1",
          "deptName": "技术部",
          "orderNum": 1,
          "leader": "李四",
          "phone": "13800138001",
          "email": "tech@example.com",
          "status": "0",
          "createTime": "2025-01-01T00:00:00",
          "children": [
            {
              "id": 3,
              "parentId": 2,
              "ancestors": "0,1,2",
              "deptName": "前端组",
              "orderNum": 1,
              "leader": "王五",
              "phone": "13800138002",
              "email": "frontend@example.com",
              "status": "0",
              "createTime": "2025-01-01T00:00:00",
              "children": []
            }
          ]
        }
      ]
    }
  ]
}
```

### 2. 部门树选择

**接口地址：** `GET /system/dept/treeselect`

**功能描述：** 获取部门树形结构，用于下拉选择组件。

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "label": "总公司",
      "children": [
        {
          "id": 2,
          "label": "技术部",
          "children": [
            {
              "id": 3,
              "label": "前端组",
              "children": []
            },
            {
              "id": 4,
              "label": "后端组",
              "children": []
            }
          ]
        },
        {
          "id": 5,
          "label": "市场部",
          "children": []
        }
      ]
    }
  ]
}
```

### 3. 新增部门

**接口地址：** `POST /system/dept`

**请求参数：**
```json
{
  "parentId": 2,
  "deptName": "测试组",
  "orderNum": 3,
  "leader": "赵六",
  "phone": "13800138003",
  "email": "test@example.com",
  "status": "0",
  "remark": "负责产品测试工作"
}
```

### 4. 修改部门

**接口地址：** `PUT /system/dept`

### 5. 删除部门

**接口地址：** `DELETE /system/dept/{deptId}`

**功能描述：** 删除部门，删除前会检查是否存在子部门和关联用户。

## 前端实现

### 1. 部门管理页面

```vue
<template>
  <div class="dept-manage">
    <!-- 搜索表单 -->
    <a-card class="search-card">
      <a-form layout="inline" :model="searchForm">
        <a-form-item label="部门名称">
          <a-input v-model:value="searchForm.deptName" placeholder="请输入部门名称" />
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
            新增部门
          </a-button>
          <a-button @click="handleExpandAll">
            <template #icon><menu-unfold-outlined /></template>
            展开/折叠
          </a-button>
        </a-space>
      </template>

      <!-- 部门树表格 -->
      <a-table
        :columns="columns"
        :data-source="deptList"
        :pagination="false"
        :loading="loading"
        row-key="id"
        :default-expand-all-rows="isExpandAll"
        :expand-row-by-click="true"
      >
        <template #bodyCell="{ column, record }">
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
              <a-popconfirm title="确定删除此部门吗？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 部门编辑弹窗 -->
    <dept-edit-modal
      v-model:visible="editModalVisible"
      :dept-data="currentDept"
      :dept-tree="deptTree"
      @success="handleEditSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined, MenuUnfoldOutlined } from '@ant-design/icons-vue'
import { fetchDeptList, deleteDept, changeDeptStatus } from '@/service/api/dept'
import DeptEditModal from './components/dept-edit-modal.vue'

// 响应式数据
const loading = ref(false)
const deptList = ref([])
const deptTree = ref([])
const isExpandAll = ref(false)
const editModalVisible = ref(false)
const currentDept = ref(null)

// 搜索表单
const searchForm = reactive({
  deptName: '',
  status: undefined
})

// 表格列配置
const columns = [
  {
    title: '部门名称',
    dataIndex: 'deptName',
    key: 'deptName',
    width: 200
  },
  {
    title: '排序',
    dataIndex: 'orderNum',
    key: 'orderNum',
    width: 80,
    align: 'center'
  },
  {
    title: '负责人',
    dataIndex: 'leader',
    key: 'leader',
    width: 120
  },
  {
    title: '联系电话',
    dataIndex: 'phone',
    key: 'phone',
    width: 150
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
    width: 200
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
    const { data } = await fetchDeptList(searchForm)
    deptList.value = data
    deptTree.value = buildDeptTree(data)
  } catch (error) {
    message.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const buildDeptTree = (depts) => {
  const tree = []
  const map = {}
  
  // 创建映射
  depts.forEach(dept => {
    map[dept.id] = { ...dept, children: [] }
  })
  
  // 构建树形结构
  depts.forEach(dept => {
    if (dept.parentId === 0) {
      tree.push(map[dept.id])
    } else {
      if (map[dept.parentId]) {
        map[dept.parentId].children.push(map[dept.id])
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
    deptName: '',
    status: undefined
  })
  loadData()
}

const handleAdd = () => {
  currentDept.value = null
  editModalVisible.value = true
}

const handleAddChild = (record) => {
  currentDept.value = { parentId: record.id }
  editModalVisible.value = true
}

const handleEdit = (record) => {
  currentDept.value = record
  editModalVisible.value = true
}

const handleDelete = async (record) => {
  try {
    await deleteDept(record.id)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

const handleStatusChange = async (record, checked) => {
  try {
    await changeDeptStatus({
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

### 2. 部门编辑弹窗

```vue
<template>
  <a-modal
    v-model:visible="visible"
    :title="isEdit ? '编辑部门' : '新增部门'"
    width="600px"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form ref="formRef" :model="formData" :rules="rules" layout="vertical">
      <a-form-item label="上级部门" name="parentId">
        <a-tree-select
          v-model:value="formData.parentId"
          :tree-data="parentDeptOptions"
          :field-names="{ children: 'children', label: 'deptName', value: 'id' }"
          placeholder="选择上级部门"
          tree-default-expand-all
          allow-clear
        />
      </a-form-item>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="部门名称" name="deptName">
            <a-input v-model:value="formData.deptName" placeholder="请输入部门名称" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="显示排序" name="orderNum">
            <a-input-number v-model:value="formData.orderNum" :min="0" style="width: 100%" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="负责人" name="leader">
            <a-input v-model:value="formData.leader" placeholder="请输入负责人" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="联系电话" name="phone">
            <a-input v-model:value="formData.phone" placeholder="请输入联系电话" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-form-item label="邮箱" name="email">
        <a-input v-model:value="formData.email" placeholder="请输入邮箱" />
      </a-form-item>

      <a-form-item label="部门状态">
        <a-radio-group v-model:value="formData.status">
          <a-radio value="0">正常</a-radio>
          <a-radio value="1">停用</a-radio>
        </a-radio-group>
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
import { createDept, updateDept } from '@/service/api/dept'

// Props和Emits
interface Props {
  visible: boolean
  deptData?: any
  deptTree: any[]
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
  deptName: '',
  orderNum: 0,
  leader: '',
  phone: '',
  email: '',
  status: '0',
  remark: ''
})

// 表单验证规则
const rules = {
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ],
  orderNum: [
    { required: true, message: '请输入显示排序', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 计算属性
const isEdit = computed(() => !!props.deptData?.id)

const parentDeptOptions = computed(() => {
  const options = [{ id: 0, deptName: '主类目', children: [] }]
  return options.concat(props.deptTree)
})

// 监听器
watch(() => props.visible, (visible) => {
  if (visible) {
    if (props.deptData) {
      Object.assign(formData, props.deptData)
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
    deptName: '',
    orderNum: 0,
    leader: '',
    phone: '',
    email: '',
    status: '0',
    remark: ''
  })
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()

    if (isEdit.value) {
      await updateDept(formData)
      message.success('修改成功')
    } else {
      await createDept(formData)
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

## 数据权限控制

### 1. 部门数据权限实现

```java
@Component
public class DataScopeAspect {

    /**
     * 数据权限过滤
     */
    @Before("@annotation(dataScopeAnnotation)")
    public void doBefore(JoinPoint point, DataScope dataScopeAnnotation) {
        clearDataScope(point);
        handleDataScope(point, dataScopeAnnotation);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope dataScope) {
        // 获取当前用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            SysUser currentUser = loginUser.getUser();

            // 如果是超级管理员，则不过滤数据
            if (!currentUser.isAdmin()) {
                String permission = StringUtils.defaultIfEmpty(dataScope.permission(),
                    PermissionConstants.SYSTEM_USER_LIST);

                dataScopeFilter(joinPoint, currentUser, dataScope.deptAlias(),
                    dataScope.userAlias(), permission);
            }
        }
    }

    /**
     * 数据范围过滤
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user,
            String deptAlias, String userAlias, String permission) {
        StringBuilder sqlString = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        for (SysRole role : user.getRoles()) {
            int dataScope = role.getDataScope();
            if (CONDITIONS_FULL_DATA_SCOPE.equals(dataScope)) {
                // 全部数据权限
                sqlString = new StringBuilder();
                conditions.clear();
                break;
            } else if (CONDITIONS_CUSTOM_DATA_SCOPE.equals(dataScope)) {
                // 自定数据权限
                conditions.add(MessageFormat.format(
                    " OR {0}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {1} ) ",
                    deptAlias, role.getRoleId()));
            } else if (CONDITIONS_DEPT_DATA_SCOPE.equals(dataScope)) {
                // 部门数据权限
                conditions.add(MessageFormat.format(" OR {0}.dept_id = {1} ",
                    deptAlias, user.getDeptId()));
            } else if (CONDITIONS_DEPT_AND_CHILD_DATA_SCOPE.equals(dataScope)) {
                // 部门及以下数据权限
                conditions.add(MessageFormat.format(
                    " OR {0}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {1} OR ancestors LIKE '%,{1},%' )",
                    deptAlias, user.getDeptId()));
            } else if (CONDITIONS_SELF_DATA_SCOPE.equals(dataScope)) {
                // 仅本人数据权限
                if (StringUtils.isNotBlank(userAlias)) {
                    conditions.add(MessageFormat.format(" OR {0}.user_id = {1} ",
                        userAlias, user.getUserId()));
                } else {
                    conditions.add(MessageFormat.format(" OR {0}.dept_id = 0 ", deptAlias));
                }
            }
        }

        if (!conditions.isEmpty()) {
            sqlString.append(" AND (").append(StringUtils.join(conditions, " ")).append(")");
        }

        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.getParams().put(DATA_SCOPE, sqlString.toString());
            }
        }
    }
}
```

### 2. 使用数据权限注解

```java
@Service
public class SysUserServiceImpl implements ISysUserService {

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }
}
```

### 3. MyBatis SQL映射

```xml
<select id="selectUserList" parameterType="SysUser" resultMap="SysUserResult">
    SELECT u.user_id, u.dept_id, u.user_name, u.nick_name, u.email, u.avatar, u.phonenumber, u.password, u.sex, u.status, u.del_flag, u.login_ip, u.login_date, u.create_by, u.create_time, u.remark,
           d.dept_id, d.parent_id, d.ancestors, d.dept_name, d.order_num, d.leader, d.status as dept_status,
           r.role_id, r.role_name, r.role_key, r.role_sort, r.data_scope, r.menu_check_strictly, r.dept_check_strictly, r.status as role_status
    FROM sys_user u
        LEFT JOIN sys_dept d ON u.dept_id = d.dept_id
        LEFT JOIN sys_user_role ur ON u.user_id = ur.user_id
        LEFT JOIN sys_role r ON r.role_id = ur.role_id
    WHERE u.del_flag = '0'
        <if test="userId != null and userId != 0">
            AND u.user_id = #{userId}
        </if>
        <if test="userName != null and userName != ''">
            AND u.user_name like concat('%', #{userName}, '%')
        </if>
        <if test="status != null and status != ''">
            AND u.status = #{status}
        </if>
        <if test="phonenumber != null and phonenumber != ''">
            AND u.phonenumber like concat('%', #{phonenumber}, '%')
        </if>
        <if test="params.beginTime != null and params.beginTime != ''"><!-- 开始时间检索 -->
            AND date_format(u.create_time,'%y%m%d') &gt;= date_format(#{params.beginTime},'%y%m%d')
        </if>
        <if test="params.endTime != null and params.endTime != ''"><!-- 结束时间检索 -->
            AND date_format(u.create_time,'%y%m%d') &lt;= date_format(#{params.endTime},'%y%m%d')
        </if>
        <if test="deptId != null and deptId != 0">
            AND (u.dept_id = #{deptId} OR u.dept_id IN ( SELECT t.dept_id FROM sys_dept t WHERE find_in_set(#{deptId}, ancestors) ))
        </if>
        <!-- 数据范围过滤 -->
        ${params.dataScope}
    ORDER BY u.user_id
</select>
```

## 组织架构可视化

### 1. 组织架构图组件

```vue
<template>
  <div class="org-chart">
    <div class="org-chart-container" ref="chartContainer">
      <div
        v-for="dept in flatDeptList"
        :key="dept.id"
        :class="['dept-node', { 'root-node': dept.parentId === 0 }]"
        :style="getDeptNodeStyle(dept)"
        @click="handleDeptClick(dept)"
      >
        <div class="dept-info">
          <div class="dept-name">{{ dept.deptName }}</div>
          <div class="dept-leader" v-if="dept.leader">{{ dept.leader }}</div>
          <div class="dept-count">{{ dept.userCount || 0 }}人</div>
        </div>

        <!-- 连接线 -->
        <div v-if="dept.parentId !== 0" class="connection-line"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

interface Props {
  deptTree: any[]
}

const props = defineProps<Props>()

const chartContainer = ref()

// 扁平化部门列表
const flatDeptList = computed(() => {
  const result = []

  function flatten(depts, level = 0, parentX = 0, parentY = 0) {
    depts.forEach((dept, index) => {
      const x = parentX + index * 200
      const y = level * 150

      result.push({
        ...dept,
        level,
        x,
        y,
        parentX,
        parentY
      })

      if (dept.children && dept.children.length > 0) {
        flatten(dept.children, level + 1, x, y)
      }
    })
  }

  flatten(props.deptTree)
  return result
})

// 获取部门节点样式
const getDeptNodeStyle = (dept) => {
  return {
    position: 'absolute',
    left: dept.x + 'px',
    top: dept.y + 'px',
    transform: 'translateX(-50%)'
  }
}

// 部门点击事件
const handleDeptClick = (dept) => {
  console.log('点击部门:', dept)
}

onMounted(() => {
  // 初始化图表
})
</script>

<style scoped>
.org-chart {
  width: 100%;
  height: 600px;
  overflow: auto;
  background: #f5f5f5;
  border-radius: 6px;
}

.org-chart-container {
  position: relative;
  min-width: 100%;
  min-height: 100%;
  padding: 50px;
}

.dept-node {
  width: 160px;
  background: #fff;
  border: 2px solid #e6f7ff;
  border-radius: 8px;
  padding: 12px;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  &:hover {
    border-color: #1890ff;
    box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
  }

  &.root-node {
    border-color: #52c41a;
    background: linear-gradient(135deg, #f6ffed 0%, #e6f7ff 100%);
  }
}

.dept-info {
  text-align: center;

  .dept-name {
    font-size: 14px;
    font-weight: 600;
    color: #262626;
    margin-bottom: 4px;
  }

  .dept-leader {
    font-size: 12px;
    color: #666;
    margin-bottom: 4px;
  }

  .dept-count {
    font-size: 12px;
    color: #1890ff;
    background: #e6f7ff;
    padding: 2px 8px;
    border-radius: 10px;
    display: inline-block;
  }
}

.connection-line {
  position: absolute;
  top: -75px;
  left: 50%;
  width: 2px;
  height: 75px;
  background: #d9d9d9;
  transform: translateX(-50%);

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -50px;
    width: 100px;
    height: 2px;
    background: #d9d9d9;
  }
}
</style>
```

## 最佳实践

### 1. 部门设计原则
- **层级合理**：部门层级不宜过深，建议不超过5级
- **职责清晰**：每个部门的职责要明确，避免重叠
- **负责人明确**：每个部门都应该有明确的负责人
- **联系方式完整**：保持部门联系方式的准确性

### 2. 数据权限设计
- **权限最小化**：遵循最小权限原则
- **层级继承**：下级部门可以继承上级部门的权限
- **灵活配置**：支持自定义数据权限范围
- **性能考虑**：优化数据权限查询的性能

### 3. 组织架构管理
- **变更记录**：记录组织架构的变更历史
- **影响分析**：变更前分析对用户和权限的影响
- **批量操作**：支持批量调整部门结构
- **数据一致性**：确保部门变更后数据的一致性

## 常见问题

### Q: 如何处理部门合并？

**解决方案**：
```java
@Transactional
public void mergeDepts(Long sourceDeptId, Long targetDeptId) {
    // 1. 更新用户部门
    userMapper.updateUserDept(sourceDeptId, targetDeptId);

    // 2. 更新子部门的父级
    deptMapper.updateParentDept(sourceDeptId, targetDeptId);

    // 3. 删除源部门
    deptMapper.deleteDept(sourceDeptId);

    // 4. 更新祖级列表
    updateAncestors();
}
```

### Q: 如何实现部门负责人的权限继承？

**解决方案**：
```java
// 在数据权限过滤中添加负责人逻辑
if (user.getUserId().equals(dept.getLeaderId())) {
    // 部门负责人可以查看本部门及下级部门数据
    conditions.add(MessageFormat.format(
        " OR {0}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {1} OR ancestors LIKE '%,{1},%' )",
        deptAlias, dept.getDeptId()));
}
```

### Q: 如何优化大型组织架构的查询性能？

**解决方案**：
```sql
-- 1. 为ancestors字段添加索引
CREATE INDEX idx_sys_dept_ancestors ON sys_dept(ancestors);

-- 2. 使用CTE递归查询（MySQL 8.0+）
WITH RECURSIVE dept_tree AS (
    SELECT dept_id, parent_id, dept_name, 0 as level
    FROM sys_dept
    WHERE parent_id = 0

    UNION ALL

    SELECT d.dept_id, d.parent_id, d.dept_name, dt.level + 1
    FROM sys_dept d
    INNER JOIN dept_tree dt ON d.parent_id = dt.dept_id
)
SELECT * FROM dept_tree;
```
```
