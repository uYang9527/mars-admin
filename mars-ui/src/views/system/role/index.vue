<template>
  <div class="page-container">
    <n-card>
      <!-- 搜索表单 -->
      <div class="search-form">
        <n-form inline :model="searchForm" label-placement="left">
          <n-form-item label="角色名称">
            <n-input v-model:value="searchForm.name" placeholder="请输入角色名称" clearable />
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
          新增角色
        </n-button>
      </div>
      
      <!-- 表格 -->
      <n-data-table
        :columns="columns"
        :data="tableData"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row: SysRole) => row.id"
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
      />
    </n-card>
    
    <!-- 新增/编辑弹窗 -->
    <n-modal
      v-model:show="modalVisible"
      :title="modalTitle"
      preset="card"
      style="width: 700px"
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
        <n-form-item label="角色名称" path="name">
          <n-input v-model:value="formData.name" placeholder="请输入角色名称" />
        </n-form-item>
        <n-form-item label="角色编码" path="code">
          <n-input v-model:value="formData.code" placeholder="请输入角色编码" />
        </n-form-item>
        <n-form-item label="排序" path="sort">
          <n-input-number v-model:value="formData.sort" :min="0" style="width: 100%" />
        </n-form-item>
        <n-form-item label="状态" path="status">
          <n-switch v-model:value="formData.status" :checked-value="1" :unchecked-value="0">
            <template #checked>启用</template>
            <template #unchecked>禁用</template>
          </n-switch>
        </n-form-item>
        <n-form-item label="菜单权限" path="menuIds">
          <div class="menu-tree-wrapper">
            <n-tree
              :data="menuTreeData"
              :checked-keys="menuIds"
              checkable
              cascade
              check-strategy="all"
              selectable
              block-line
              @update:checked-keys="handleMenuCheck"
            />
          </div>
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
import { NButton, NTag, NSpace, useMessage, useDialog, type DataTableColumns, type FormInst, type FormRules, type TreeOption } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline } from '@vicons/ionicons5'
import { roleApi, menuApi, type SysRole, type SysMenu } from '@/api/system'

const message = useMessage()
const dialog = useDialog()

// 搜索表单
const searchForm = reactive({
  name: '',
  status: null as number | null
})

// 状态选项
const statusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
]

// 表格数据
const tableData = ref<SysRole[]>([])
const loading = ref(false)
const pagination = reactive({
  page: 1,
  pageSize: 10,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50]
})

// 菜单树
const menuTreeData = ref<TreeOption[]>([])
const menuIds = ref<number[]>([])

// 表格列
const columns: DataTableColumns<SysRole> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '角色名称', key: 'name', width: 150 },
  { title: '角色编码', key: 'code', width: 150 },
  { title: '排序', key: 'sort', width: 80 },
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
  { title: '备注', key: 'remark', ellipsis: { tooltip: true } },
  { title: '创建时间', key: 'createTime', width: 180 },
  {
    title: '操作',
    key: 'actions',
    width: 150,
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
const modalTitle = ref('新增角色')
const formRef = ref<FormInst | null>(null)
const submitLoading = ref(false)

const formData = reactive<SysRole>({
  id: undefined,
  name: '',
  code: '',
  sort: 0,
  status: 1,
  remark: ''
})

const rules: FormRules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

// 转换菜单为树结构
function convertMenuToTree(menus: SysMenu[]): TreeOption[] {
  return menus.map(menu => ({
    key: menu.id,
    label: menu.name,
    children: menu.children ? convertMenuToTree(menu.children) : undefined
  }))
}

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await roleApi.page({
      page: pagination.page,
      pageSize: pagination.pageSize,
      name: searchForm.name || undefined,
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

// 加载菜单树
async function loadMenuTree() {
  try {
    const menus = await menuApi.tree()
    menuTreeData.value = convertMenuToTree(menus)
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
  searchForm.name = ''
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

// 菜单选择
function handleMenuCheck(keys: number[]) {
  menuIds.value = keys
}

// 新增
function handleAdd() {
  modalTitle.value = '新增角色'
  Object.assign(formData, {
    id: undefined,
    name: '',
    code: '',
    sort: 0,
    status: 1,
    remark: ''
  })
  menuIds.value = []
  modalVisible.value = true
}

// 编辑
async function handleEdit(row: SysRole) {
  modalTitle.value = '编辑角色'
  try {
    const res = await roleApi.detail(row.id!)
    Object.assign(formData, res.role)
    menuIds.value = res.menuIds
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
      role: { ...formData },
      menuIds: menuIds.value
    }
    
    if (formData.id) {
      await roleApi.update(data)
      message.success('更新成功')
    } else {
      await roleApi.create(data)
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
function handleDelete(row: SysRole) {
  dialog.warning({
    title: '提示',
    content: `确定要删除角色"${row.name}"吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await roleApi.delete(row.id!)
        message.success('删除成功')
        loadData()
      } catch (error) {
        // 错误已在拦截器处理
      }
    }
  })
}

onMounted(() => {
  loadData()
  loadMenuTree()
})
</script>

<style lang="scss" scoped>
.menu-tree-wrapper {
  width: 100%;
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #E5E7EB;
  border-radius: 8px;
  padding: 12px;
}
</style>
