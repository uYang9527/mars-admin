<template>
  <div class="page-container">
    <n-card>
      <!-- 搜索表单 -->
      <div class="search-form">
        <n-form inline :model="searchForm" label-placement="left">
          <n-form-item label="用户名">
            <n-input v-model:value="searchForm.username" placeholder="请输入用户名" clearable />
          </n-form-item>
          <n-form-item label="状态">
            <n-select
              v-model:value="searchForm.status"
              placeholder="请选择状态"
              :options="statusOptions"
              clearable
              style="width: 120px"
            />
          </n-form-item>
          <n-form-item>
            <n-space>
              <n-button type="primary" @click="handleSearch">
                <template #icon><n-icon><SearchOutline /></n-icon></template>
                搜索
              </n-button>
              <n-button @click="handleReset">
                <template #icon><n-icon><RefreshOutline /></n-icon></template>
                重置
              </n-button>
            </n-space>
          </n-form-item>
        </n-form>
      </div>
      
      <!-- 工具栏 -->
      <div class="table-toolbar">
        <n-button type="primary" @click="handleAdd">
          <template #icon><n-icon><AddOutline /></n-icon></template>
          新增用户
        </n-button>
      </div>
      
      <!-- 表格 -->
      <n-data-table
        :columns="columns"
        :data="tableData"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row: SysUser) => row.id"
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
      />
    </n-card>
    
    <!-- 新增/编辑弹窗 -->
    <n-modal
      v-model:show="modalVisible"
      :title="modalTitle"
      preset="card"
      style="width: 600px"
      :mask-closable="false"
    >
      <n-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-placement="left"
        label-width="80"
        class="modal-form"
      >
        <n-form-item label="用户名" path="username">
          <n-input v-model:value="formData.username" placeholder="请输入用户名" :disabled="!!formData.id" />
        </n-form-item>
        <n-form-item v-if="!formData.id" label="密码" path="password">
          <n-input v-model:value="formData.password" type="password" placeholder="请输入密码，留空默认123456" show-password-on="click" />
        </n-form-item>
        <n-form-item label="昵称" path="nickname">
          <n-input v-model:value="formData.nickname" placeholder="请输入昵称" />
        </n-form-item>
        <n-form-item label="邮箱" path="email">
          <n-input v-model:value="formData.email" placeholder="请输入邮箱" />
        </n-form-item>
        <n-form-item label="手机号" path="phone">
          <n-input v-model:value="formData.phone" placeholder="请输入手机号" />
        </n-form-item>
        <n-form-item label="性别" path="gender">
          <n-radio-group v-model:value="formData.gender">
            <n-radio :value="1">男</n-radio>
            <n-radio :value="2">女</n-radio>
            <n-radio :value="0">未知</n-radio>
          </n-radio-group>
        </n-form-item>
        <n-form-item label="角色" path="roleIds">
          <n-select
            v-model:value="roleIds"
            multiple
            :options="roleOptions"
            placeholder="请选择角色"
          />
        </n-form-item>
        <n-form-item label="状态" path="status">
          <n-switch v-model:value="formData.status" :checked-value="1" :unchecked-value="0">
            <template #checked>启用</template>
            <template #unchecked>禁用</template>
          </n-switch>
        </n-form-item>
        <n-form-item label="备注" path="remark">
          <n-input v-model:value="formData.remark" type="textarea" placeholder="请输入备注" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="modalVisible = false">取消</n-button>
          <n-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted } from 'vue'
import { NButton, NTag, NSpace, useMessage, useDialog, type DataTableColumns, type FormInst, type FormRules } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline } from '@vicons/ionicons5'
import { userApi, roleApi, type SysUser, type SysRole } from '@/api/system'

const message = useMessage()
const dialog = useDialog()

// 搜索表单
const searchForm = reactive({
  username: '',
  status: null as number | null
})

// 状态选项
const statusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
]

// 表格数据
const tableData = ref<SysUser[]>([])
const loading = ref(false)
const pagination = reactive({
  page: 1,
  pageSize: 10,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50]
})

// 角色选项
const roleOptions = ref<Array<{ label: string; value: number }>>([])

// 表格列
const columns: DataTableColumns<SysUser> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '用户名', key: 'username', width: 120 },
  { title: '昵称', key: 'nickname', width: 120 },
  { title: '邮箱', key: 'email', width: 180 },
  { title: '手机号', key: 'phone', width: 130 },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render(row) {
      return h(
        NTag,
        { type: row.status === 1 ? 'success' : 'error', size: 'small' },
        { default: () => (row.status === 1 ? '启用' : '禁用') }
      )
    }
  },
  { title: '创建时间', key: 'createTime', width: 180 },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    fixed: 'right',
    render(row) {
      return h(NSpace, null, {
        default: () => [
          h(
            NButton,
            { size: 'small', onClick: () => handleEdit(row) },
            { default: () => '编辑' }
          ),
          h(
            NButton,
            { size: 'small', onClick: () => handleResetPassword(row) },
            { default: () => '重置密码' }
          ),
          h(
            NButton,
            { size: 'small', type: 'error', onClick: () => handleDelete(row) },
            { default: () => '删除' }
          )
        ]
      })
    }
  }
]

// 弹窗
const modalVisible = ref(false)
const modalTitle = ref('新增用户')
const formRef = ref<FormInst | null>(null)
const submitLoading = ref(false)
const roleIds = ref<number[]>([])

const formData = reactive<SysUser>({
  id: undefined,
  username: '',
  password: '',
  nickname: '',
  email: '',
  phone: '',
  gender: 0,
  status: 1,
  remark: ''
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }]
}

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await userApi.page({
      page: pagination.page,
      pageSize: pagination.pageSize,
      username: searchForm.username || undefined,
      status: searchForm.status ?? undefined
    })
    tableData.value = res.list
    pagination.itemCount = res.total
  } catch (error) {
    // 错误已在拦截器处理
  } finally {
    loading.value = false
  }
}

// 加载角色列表
async function loadRoles() {
  try {
    const roles = await roleApi.list()
    roleOptions.value = roles.map((role: SysRole) => ({
      label: role.name,
      value: role.id!
    }))
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 搜索
function handleSearch() {
  pagination.page = 1
  loadData()
}

// 重置
function handleReset() {
  searchForm.username = ''
  searchForm.status = null
  handleSearch()
}

// 分页
function handlePageChange(page: number) {
  pagination.page = page
  loadData()
}

function handlePageSizeChange(pageSize: number) {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadData()
}

// 新增
function handleAdd() {
  modalTitle.value = '新增用户'
  Object.assign(formData, {
    id: undefined,
    username: '',
    password: '',
    nickname: '',
    email: '',
    phone: '',
    gender: 0,
    status: 1,
    remark: ''
  })
  roleIds.value = []
  modalVisible.value = true
}

// 编辑
async function handleEdit(row: SysUser) {
  modalTitle.value = '编辑用户'
  try {
    const res = await userApi.detail(row.id!)
    Object.assign(formData, res.user)
    roleIds.value = res.roleIds
    modalVisible.value = true
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 提交
async function handleSubmit() {
  try {
    await formRef.value?.validate()
    submitLoading.value = true
    
    const data = {
      user: { ...formData },
      roleIds: roleIds.value
    }
    
    if (formData.id) {
      await userApi.update(data)
      message.success('更新成功')
    } else {
      await userApi.create(data)
      message.success('创建成功')
    }
    
    modalVisible.value = false
    loadData()
  } catch (error) {
    // 错误已在拦截器处理
  } finally {
    submitLoading.value = false
  }
}

// 删除
function handleDelete(row: SysUser) {
  dialog.warning({
    title: '提示',
    content: `确定要删除用户"${row.username}"吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await userApi.delete(row.id!)
        message.success('删除成功')
        loadData()
      } catch (error) {
        // 错误已在拦截器处理
      }
    }
  })
}

// 重置密码
function handleResetPassword(row: SysUser) {
  dialog.warning({
    title: '提示',
    content: `确定要重置用户"${row.username}"的密码吗？重置后密码为123456`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await userApi.resetPassword(row.id!)
        message.success('密码重置成功')
      } catch (error) {
        // 错误已在拦截器处理
      }
    }
  })
}

onMounted(() => {
  loadData()
  loadRoles()
})
</script>
