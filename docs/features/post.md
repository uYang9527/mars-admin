# 岗位管理

岗位管理是Mars Admin人力资源管理的重要组成部分，提供了完整的岗位信息维护、岗位层级管理和人员分配功能。

## 功能概述

岗位管理模块主要包含以下功能：

- **岗位信息管理**：岗位的基本信息维护
- **岗位层级管理**：岗位的等级和层级关系
- **岗位状态控制**：岗位的启用和停用管理
- **人员分配**：岗位与用户的关联管理
- **岗位权限**：基于岗位的权限配置
- **岗位统计**：岗位人员统计和分析

## 数据模型

### 岗位实体

```java
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_post", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "岗位信息")
public class SysPost extends BaseEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "岗位ID")
    private Long postId;

    @Schema(description = "岗位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String postCode;

    @Schema(description = "岗位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String postName;

    @Schema(description = "显示顺序")
    private Integer postSort;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "备注")
    private String remark;

    // 非数据库字段
    @Schema(description = "岗位人员数量")
    private Integer userCount;

    @Schema(description = "是否已分配给用户")
    private Boolean flag;
}
```

### 用户岗位关联实体

```java
@Data
@Table(value = "sys_user_post")
@Schema(description = "用户和岗位关联")
public class SysUserPost {
    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "岗位ID")
    private Long postId;
}
```

## 核心功能实现

### 1. 岗位列表查询

**接口地址：** `GET /system/post/list`

**功能描述：** 分页查询岗位列表，支持按岗位名称、岗位编码、状态等条件筛选。

**请求参数：**
```json
{
  "current": 1,
  "size": 10,
  "postName": "经理",
  "postCode": "manager",
  "status": "0"
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
        "postId": 1,
        "postCode": "ceo",
        "postName": "董事长",
        "postSort": 1,
        "status": "0",
        "createTime": "2025-01-01T00:00:00",
        "remark": "董事长岗位",
        "userCount": 1
      },
      {
        "postId": 2,
        "postCode": "manager",
        "postName": "项目经理",
        "postSort": 2,
        "status": "0",
        "createTime": "2025-01-01T00:00:00",
        "remark": "项目经理岗位",
        "userCount": 3
      }
    ],
    "total": 2,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 2. 岗位详情查询

**接口地址：** `GET /system/post/{postId}`

**功能描述：** 根据岗位ID查询岗位详细信息。

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "postId": 1,
    "postCode": "ceo",
    "postName": "董事长",
    "postSort": 1,
    "status": "0",
    "createTime": "2025-01-01T00:00:00",
    "remark": "董事长岗位"
  }
}
```

### 3. 新增岗位

**接口地址：** `POST /system/post`

**请求参数：**
```json
{
  "postCode": "developer",
  "postName": "开发工程师",
  "postSort": 5,
  "status": "0",
  "remark": "软件开发工程师岗位"
}
```

### 4. 修改岗位

**接口地址：** `PUT /system/post`

**请求参数：**
```json
{
  "postId": 1,
  "postCode": "senior_developer",
  "postName": "高级开发工程师",
  "postSort": 4,
  "status": "0",
  "remark": "高级软件开发工程师岗位"
}
```

### 5. 删除岗位

**接口地址：** `DELETE /system/post/{postIds}`

**功能描述：** 删除一个或多个岗位，删除前会检查岗位是否被用户使用。

### 6. 获取岗位选项

**接口地址：** `GET /system/post/optionselect`

**功能描述：** 获取所有正常状态的岗位列表，用于下拉选择。

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "postId": 1,
      "postName": "董事长"
    },
    {
      "postId": 2,
      "postName": "项目经理"
    },
    {
      "postId": 3,
      "postName": "开发工程师"
    }
  ]
}
```

## 前端实现

### 1. 岗位管理页面

```vue
<template>
  <div class="post-manage">
    <!-- 搜索表单 -->
    <a-card class="search-card">
      <a-form layout="inline" :model="searchForm">
        <a-form-item label="岗位名称">
          <a-input v-model:value="searchForm.postName" placeholder="请输入岗位名称" />
        </a-form-item>
        <a-form-item label="岗位编码">
          <a-input v-model:value="searchForm.postCode" placeholder="请输入岗位编码" />
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
            新增岗位
          </a-button>
          <a-button type="danger" :disabled="!selectedRowKeys.length" @click="handleBatchDelete">
            <template #icon><delete-outlined /></template>
            批量删除
          </a-button>
          <a-button @click="handleExport">
            <template #icon><export-outlined /></template>
            导出
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
              :checked="record.status === '0'"
              @change="(checked) => handleStatusChange(record, checked)"
            />
          </template>
          <template v-if="column.key === 'userCount'">
            <a-button type="link" @click="handleViewUsers(record)">
              {{ record.userCount || 0 }}人
            </a-button>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-popconfirm title="确定删除此岗位吗？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 岗位编辑弹窗 -->
    <post-edit-modal
      v-model:visible="editModalVisible"
      :post-data="currentPost"
      @success="handleEditSuccess"
    />

    <!-- 岗位人员弹窗 -->
    <post-users-modal
      v-model:visible="usersModalVisible"
      :post-data="currentPost"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined, DeleteOutlined, ExportOutlined } from '@ant-design/icons-vue'
import { fetchPostList, deletePost, changePostStatus, exportPost } from '@/service/api/post'
import PostEditModal from './components/post-edit-modal.vue'
import PostUsersModal from './components/post-users-modal.vue'

// 响应式数据
const loading = ref(false)
const dataList = ref([])
const selectedRowKeys = ref([])
const editModalVisible = ref(false)
const usersModalVisible = ref(false)
const currentPost = ref(null)

// 搜索表单
const searchForm = reactive({
  postName: '',
  postCode: '',
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
    title: '岗位编号',
    dataIndex: 'postId',
    key: 'postId',
    width: 100
  },
  {
    title: '岗位编码',
    dataIndex: 'postCode',
    key: 'postCode'
  },
  {
    title: '岗位名称',
    dataIndex: 'postName',
    key: 'postName'
  },
  {
    title: '岗位排序',
    dataIndex: 'postSort',
    key: 'postSort',
    width: 100,
    align: 'center'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    align: 'center'
  },
  {
    title: '人员数量',
    dataIndex: 'userCount',
    key: 'userCount',
    width: 120,
    align: 'center'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 180
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark'
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
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
    const { data } = await fetchPostList(params)
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
    postName: '',
    postCode: '',
    status: undefined
  })
  handleSearch()
}

const handleAdd = () => {
  currentPost.value = null
  editModalVisible.value = true
}

const handleEdit = (record) => {
  currentPost.value = record
  editModalVisible.value = true
}

const handleDelete = async (record) => {
  try {
    await deletePost(record.postId)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

const handleBatchDelete = async () => {
  try {
    await deletePost(selectedRowKeys.value.join(','))
    message.success('批量删除成功')
    selectedRowKeys.value = []
    loadData()
  } catch (error) {
    message.error('批量删除失败')
  }
}

const handleStatusChange = async (record, checked) => {
  try {
    await changePostStatus({
      postId: record.postId,
      status: checked ? '0' : '1'
    })
    record.status = checked ? '0' : '1'
    message.success('状态修改成功')
  } catch (error) {
    message.error('状态修改失败')
  }
}

const handleViewUsers = (record) => {
  currentPost.value = record
  usersModalVisible.value = true
}

const handleExport = async () => {
  try {
    const response = await exportPost(searchForm)
    // 处理文件下载
    const blob = new Blob([response.data])
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '岗位数据.xlsx'
    link.click()
    window.URL.revokeObjectURL(url)
    message.success('导出成功')
  } catch (error) {
    message.error('导出失败')
  }
}

const handleTableChange = (pag) => {
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

### 2. 岗位编辑弹窗

```vue
<template>
  <a-modal
    v-model:visible="visible"
    :title="isEdit ? '编辑岗位' : '新增岗位'"
    width="600px"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form ref="formRef" :model="formData" :rules="rules" layout="vertical">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="岗位名称" name="postName">
            <a-input v-model:value="formData.postName" placeholder="请输入岗位名称" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="岗位编码" name="postCode">
            <a-input v-model:value="formData.postCode" placeholder="请输入岗位编码" />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row :gutter="16">
        <a-col :span="12">
          <a-form-item label="岗位排序" name="postSort">
            <a-input-number v-model:value="formData.postSort" :min="0" style="width: 100%" />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="岗位状态">
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
import { createPost, updatePost } from '@/service/api/post'

// Props和Emits
interface Props {
  visible: boolean
  postData?: any
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
  postId: undefined,
  postName: '',
  postCode: '',
  postSort: 0,
  status: '0',
  remark: ''
})

// 表单验证规则
const rules = {
  postName: [
    { required: true, message: '请输入岗位名称', trigger: 'blur' }
  ],
  postCode: [
    { required: true, message: '请输入岗位编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '岗位编码只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  postSort: [
    { required: true, message: '请输入岗位排序', trigger: 'blur' }
  ]
}

// 计算属性
const isEdit = computed(() => !!props.postData?.postId)

// 监听器
watch(() => props.visible, (visible) => {
  if (visible) {
    if (props.postData) {
      Object.assign(formData, props.postData)
    } else {
      resetForm()
    }
  }
})

// 方法定义
const resetForm = () => {
  Object.assign(formData, {
    postId: undefined,
    postName: '',
    postCode: '',
    postSort: 0,
    status: '0',
    remark: ''
  })
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()

    if (isEdit.value) {
      await updatePost(formData)
      message.success('修改成功')
    } else {
      await createPost(formData)
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

## 岗位与用户关联

### 1. 用户岗位分配

```java
@Service
public class SysUserPostServiceImpl implements ISysUserPostService {

    /**
     * 为用户分配岗位
     */
    @Override
    @Transactional
    public void insertUserPost(Long userId, List<Long> postIds) {
        if (CollectionUtils.isNotEmpty(postIds)) {
            // 先删除用户原有岗位
            this.remove(Wrappers.<SysUserPost>lambdaQuery()
                .eq(SysUserPost::getUserId, userId));

            // 批量插入新岗位
            List<SysUserPost> userPosts = postIds.stream()
                .map(postId -> {
                    SysUserPost userPost = new SysUserPost();
                    userPost.setUserId(userId);
                    userPost.setPostId(postId);
                    return userPost;
                })
                .collect(Collectors.toList());

            this.saveBatch(userPosts);
        }
    }

    /**
     * 根据用户ID查询岗位
     */
    @Override
    public List<Long> selectPostIdsByUserId(Long userId) {
        return this.list(Wrappers.<SysUserPost>lambdaQuery()
            .eq(SysUserPost::getUserId, userId))
            .stream()
            .map(SysUserPost::getPostId)
            .collect(Collectors.toList());
    }

    /**
     * 根据岗位ID查询用户
     */
    @Override
    public List<Long> selectUserIdsByPostId(Long postId) {
        return this.list(Wrappers.<SysUserPost>lambdaQuery()
            .eq(SysUserPost::getPostId, postId))
            .stream()
            .map(SysUserPost::getUserId)
            .collect(Collectors.toList());
    }
}
```

### 2. 岗位人员统计

```java
@Service
public class SysPostServiceImpl implements ISysPostService {

    /**
     * 查询岗位列表（包含人员统计）
     */
    @Override
    public List<SysPost> selectPostListWithUserCount(SysPost post) {
        List<SysPost> postList = this.selectPostList(post);

        // 统计每个岗位的人员数量
        for (SysPost sysPost : postList) {
            Long userCount = sysUserPostService.count(
                Wrappers.<SysUserPost>lambdaQuery()
                    .eq(SysUserPost::getPostId, sysPost.getPostId())
            );
            sysPost.setUserCount(userCount.intValue());
        }

        return postList;
    }

    /**
     * 检查岗位是否被用户使用
     */
    @Override
    public boolean checkPostExistUser(Long postId) {
        return sysUserPostService.count(
            Wrappers.<SysUserPost>lambdaQuery()
                .eq(SysUserPost::getPostId, postId)
        ) > 0;
    }
}
```

## 岗位权限配置

### 1. 基于岗位的权限控制

```java
@Component
public class PostPermissionEvaluator {

    /**
     * 检查用户是否具有指定岗位
     */
    public boolean hasPost(String postCode) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return false;
        }

        List<SysPost> userPosts = loginUser.getUser().getPosts();
        return userPosts.stream()
            .anyMatch(post -> postCode.equals(post.getPostCode()));
    }

    /**
     * 检查用户是否具有任一指定岗位
     */
    public boolean hasAnyPost(String... postCodes) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return false;
        }

        List<SysPost> userPosts = loginUser.getUser().getPosts();
        Set<String> userPostCodes = userPosts.stream()
            .map(SysPost::getPostCode)
            .collect(Collectors.toSet());

        return Arrays.stream(postCodes)
            .anyMatch(userPostCodes::contains);
    }
}
```

### 2. 岗位权限注解

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePost {

    /**
     * 需要的岗位编码
     */
    String[] value() default {};

    /**
     * 验证模式：AND-需要所有岗位，OR-需要任一岗位
     */
    Logical logical() default Logical.OR;
}

// 使用示例
@RequirePost({"manager", "director"})
@GetMapping("/sensitive-data")
public Result getSensitiveData() {
    // 只有经理或总监岗位才能访问
    return Result.success();
}
```

## 岗位数据导入导出

### 1. 岗位数据导出

```java
@RestController
@RequestMapping("/system/post")
public class SysPostController {

    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPost post) {
        List<SysPost> list = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<>(SysPost.class);
        util.exportExcel(response, list, "岗位数据");
    }
}
```

### 2. 岗位数据导入

```java
@PostMapping("/importData")
public Result importData(MultipartFile file) throws Exception {
    ExcelUtil<SysPost> util = new ExcelUtil<>(SysPost.class);
    List<SysPost> postList = util.importExcel(file.getInputStream());

    String message = postService.importPost(postList);
    return Result.success(message);
}

@Service
public class SysPostServiceImpl implements ISysPostService {

    @Override
    @Transactional
    public String importPost(List<SysPost> postList) {
        if (CollectionUtils.isEmpty(postList)) {
            throw new ServiceException("导入岗位数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SysPost post : postList) {
            try {
                // 验证岗位编码是否存在
                SysPost existPost = this.selectPostByCode(post.getPostCode());
                if (existPost == null) {
                    this.insertPost(post);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、岗位 ")
                        .append(post.getPostName()).append(" 导入成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、岗位 ")
                        .append(post.getPostName()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、岗位 " + post.getPostName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }

        return successMsg.toString();
    }
}
```

## 最佳实践

### 1. 岗位设计原则
- **编码规范**：岗位编码使用英文，便于系统识别
- **层级清晰**：岗位层级要清晰，便于权限管理
- **职责明确**：每个岗位的职责要明确定义
- **灵活配置**：支持岗位的动态调整和配置

### 2. 权限管理
- **岗位权限**：基于岗位分配权限，简化权限管理
- **权限继承**：高级岗位可以继承低级岗位的权限
- **权限隔离**：不同岗位之间的权限要有明确边界
- **审计跟踪**：记录岗位权限的变更历史

### 3. 数据维护
- **定期清理**：定期清理无用的岗位数据
- **数据校验**：确保岗位数据的完整性和一致性
- **备份恢复**：定期备份岗位配置数据
- **变更管理**：建立岗位变更的审批流程

## 常见问题

### Q: 如何实现岗位的层级管理？

**解决方案**：
```java
// 在岗位表中添加层级字段
ALTER TABLE sys_post ADD COLUMN post_level INT DEFAULT 1;
ALTER TABLE sys_post ADD COLUMN parent_post_id BIGINT;

// 查询岗位层级
public List<SysPost> selectPostTree() {
    List<SysPost> posts = this.selectPostList(new SysPost());
    return buildPostTree(posts);
}

private List<SysPost> buildPostTree(List<SysPost> posts) {
    // 构建树形结构的逻辑
}
```

### Q: 如何处理用户的多岗位权限？

**解决方案**：
```java
// 获取用户所有岗位的权限并集
public Set<String> getUserPermissionsByPosts(Long userId) {
    List<Long> postIds = sysUserPostService.selectPostIdsByUserId(userId);
    Set<String> permissions = new HashSet<>();

    for (Long postId : postIds) {
        Set<String> postPermissions = getPostPermissions(postId);
        permissions.addAll(postPermissions);
    }

    return permissions;
}
```

### Q: 如何实现岗位的动态权限配置？

**解决方案**：
```java
// 创建岗位权限关联表
CREATE TABLE sys_post_menu (
    post_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, menu_id)
);

// 为岗位分配权限
@Override
public void assignPermissionsToPost(Long postId, List<Long> menuIds) {
    // 删除原有权限
    sysPostMenuService.remove(
        Wrappers.<SysPostMenu>lambdaQuery()
            .eq(SysPostMenu::getPostId, postId)
    );

    // 分配新权限
    if (CollectionUtils.isNotEmpty(menuIds)) {
        List<SysPostMenu> postMenus = menuIds.stream()
            .map(menuId -> new SysPostMenu(postId, menuId))
            .collect(Collectors.toList());
        sysPostMenuService.saveBatch(postMenus);
    }
}
```
```
