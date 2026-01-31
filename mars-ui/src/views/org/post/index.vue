<template>
  <div class="page-container">
    <n-card>
      <div class="search-form">
        <n-form inline :model="searchForm" label-placement="left">
          <n-form-item label="岗位编码">
            <n-input v-model:value="searchForm.postCode" placeholder="请输入岗位编码" clearable />
          </n-form-item>
          <n-form-item label="岗位名称">
            <n-input v-model:value="searchForm.postName" placeholder="请输入岗位名称" clearable />
          </n-form-item>
          <n-form-item label="状态">
            <n-select v-model:value="searchForm.status" placeholder="请选择状态" :options="statusOptions" clearable style="width: 120px" />
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
      
      <div class="table-toolbar">
        <n-button v-if="hasPermission('sys:post:add')" type="primary" @click="handleAdd">
          <template #icon><n-icon><AddOutline /></n-icon></template>
          新增岗位
        </n-button>
      </div>
      
      <n-data-table :columns="columns" :data="tableData" :loading="loading" :pagination="pagination"
        :row-key="(row: SysPost) => row.id" @update:page="handlePageChange" @update:page-size="handlePageSizeChange" />
    </n-card>
    
    <n-modal v-model:show="modalVisible" :title="modalTitle" preset="card" style="width: 500px" :mask-closable="false">
      <n-form ref="formRef" :model="formData" :rules="rules" label-placement="left" label-width="80">
        <n-form-item label="岗位编码" path="postCode">
          <n-input v-model:value="formData.postCode" placeholder="请输入岗位编码" />
        </n-form-item>
        <n-form-item label="岗位名称" path="postName">
          <n-input v-model:value="formData.postName" placeholder="请输入岗位名称" />
        </n-form-item>
        <n-form-item label="显示排序" path="sort">
          <n-input-number v-model:value="formData.sort" :min="0" placeholder="请输入排序" style="width: 100%" />
        </n-form-item>
        <n-form-item label="状态" path="status">
          <n-switch v-model:value="formData.status" :checked-value="1" :unchecked-value="0">
            <template #checked>正常</template>
            <template #unchecked>停用</template>
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
import { postApi, type SysPost } from '@/api/org'
import { useUserStore } from '@/stores/user'

const message = useMessage()
const dialog = useDialog()
const userStore = useUserStore()
const hasPermission = (permission: string) => userStore.hasPermission(permission)

const searchForm = reactive({ postCode: '', postName: '', status: null as number | null })
const statusOptions = [{ label: '正常', value: 1 }, { label: '停用', value: 0 }]
const tableData = ref<SysPost[]>([])
const loading = ref(false)
const pagination = reactive({ page: 1, pageSize: 10, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50] })

const columns: DataTableColumns<SysPost> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '岗位编码', key: 'postCode', width: 120 },
  { title: '岗位名称', key: 'postName', width: 150 },
  { title: '排序', key: 'sort', width: 80 },
  { title: '状态', key: 'status', width: 80, render(row) {
    return h(NTag, { type: row.status === 1 ? 'success' : 'error', size: 'small' }, { default: () => row.status === 1 ? '正常' : '停用' })
  }},
  { title: '备注', key: 'remark', ellipsis: { tooltip: true } },
  { title: '创建时间', key: 'createTime', width: 180 },
  { title: '操作', key: 'actions', width: 150, fixed: 'right', render(row) {
    const buttons = []
    if (hasPermission('sys:post:edit')) buttons.push(h(NButton, { size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }))
    if (hasPermission('sys:post:delete')) buttons.push(h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row) }, { default: () => '删除' }))
    return buttons.length > 0 ? h(NSpace, null, { default: () => buttons }) : '-'
  }}
]

const modalVisible = ref(false)
const modalTitle = ref('新增岗位')
const formRef = ref<FormInst | null>(null)
const submitLoading = ref(false)
const formData = reactive<SysPost>({ id: undefined, postCode: '', postName: '', sort: 0, status: 1, remark: '' })
const rules: FormRules = {
  postCode: [{ required: true, message: '请输入岗位编码', trigger: 'blur' }],
  postName: [{ required: true, message: '请输入岗位名称', trigger: 'blur' }]
}

async function loadData() {
  loading.value = true
  try {
    const res = await postApi.page({ page: pagination.page, pageSize: pagination.pageSize, ...searchForm })
    tableData.value = res.list
    pagination.itemCount = res.total
  } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadData() }
function handleReset() { searchForm.postCode = ''; searchForm.postName = ''; searchForm.status = null; handleSearch() }
function handlePageChange(page: number) { pagination.page = page; loadData() }
function handlePageSizeChange(pageSize: number) { pagination.pageSize = pageSize; pagination.page = 1; loadData() }

function handleAdd() {
  modalTitle.value = '新增岗位'
  Object.assign(formData, { id: undefined, postCode: '', postName: '', sort: 0, status: 1, remark: '' })
  modalVisible.value = true
}

function handleEdit(row: SysPost) {
  modalTitle.value = '编辑岗位'
  Object.assign(formData, row)
  modalVisible.value = true
}

async function handleSubmit() {
  try {
    await formRef.value?.validate()
    submitLoading.value = true
    if (formData.id) { await postApi.update(formData); message.success('更新成功') }
    else { await postApi.create(formData); message.success('创建成功') }
    modalVisible.value = false
    loadData()
  } finally { submitLoading.value = false }
}

function handleDelete(row: SysPost) {
  dialog.warning({
    title: '提示', content: `确定要删除岗位"${row.postName}"吗？`, positiveText: '确定', negativeText: '取消',
    onPositiveClick: async () => { await postApi.delete(row.id!); message.success('删除成功'); loadData() }
  })
}

onMounted(() => loadData())
</script>
