<template>
  <div class="page-container">
    <n-card>
      <div class="search-form">
        <n-form inline :model="searchForm" label-placement="left">
          <n-form-item label="部门名称">
            <n-input v-model:value="searchForm.deptName" placeholder="请输入部门名称" clearable />
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
        <n-button v-if="hasPermission('sys:dept:add')" type="primary" @click="handleAdd()">
          <template #icon><n-icon><AddOutline /></n-icon></template>
          新增部门
        </n-button>
      </div>
      
      <n-data-table :columns="columns" :data="tableData" :loading="loading" :row-key="(row: SysDept) => row.id"
        :default-expand-all="true" />
    </n-card>
    
    <n-modal v-model:show="modalVisible" :title="modalTitle" preset="card" style="width: 600px" :mask-closable="false">
      <n-form ref="formRef" :model="formData" :rules="rules" label-placement="left" label-width="80">
        <n-form-item label="上级部门" path="parentId">
          <n-tree-select v-model:value="formData.parentId" :options="deptOptions" key-field="id" label-field="deptName"
            children-field="children" placeholder="请选择上级部门" clearable default-expand-all />
        </n-form-item>
        <n-form-item label="部门名称" path="deptName">
          <n-input v-model:value="formData.deptName" placeholder="请输入部门名称" />
        </n-form-item>
        <n-form-item label="负责人" path="leader">
          <n-input v-model:value="formData.leader" placeholder="请输入负责人" />
        </n-form-item>
        <n-form-item label="联系电话" path="phone">
          <n-input v-model:value="formData.phone" placeholder="请输入联系电话" />
        </n-form-item>
        <n-form-item label="邮箱" path="email">
          <n-input v-model:value="formData.email" placeholder="请输入邮箱" />
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
import { ref, reactive, h, onMounted, computed } from 'vue'
import { NButton, NTag, NSpace, useMessage, useDialog, type DataTableColumns, type FormInst, type FormRules } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline } from '@vicons/ionicons5'
import { deptApi, type SysDept } from '@/api/org'
import { useUserStore } from '@/stores/user'

const message = useMessage()
const dialog = useDialog()
const userStore = useUserStore()
const hasPermission = (permission: string) => userStore.hasPermission(permission)

const searchForm = reactive({ deptName: '', status: null as number | null })
const statusOptions = [{ label: '正常', value: 1 }, { label: '停用', value: 0 }]
const tableData = ref<SysDept[]>([])
const loading = ref(false)

const columns: DataTableColumns<SysDept> = [
  { title: '部门名称', key: 'deptName', width: 200 },
  { title: '负责人', key: 'leader', width: 120 },
  { title: '联系电话', key: 'phone', width: 130 },
  { title: '邮箱', key: 'email', width: 180 },
  { title: '排序', key: 'sort', width: 80 },
  { title: '状态', key: 'status', width: 80, render(row) {
    return h(NTag, { type: row.status === 1 ? 'success' : 'error', size: 'small' }, { default: () => row.status === 1 ? '正常' : '停用' })
  }},
  { title: '创建时间', key: 'createTime', width: 180 },
  { title: '操作', key: 'actions', width: 200, render(row) {
    const buttons = []
    if (hasPermission('sys:dept:add')) buttons.push(h(NButton, { size: 'small', onClick: () => handleAdd(row.id) }, { default: () => '新增' }))
    if (hasPermission('sys:dept:edit')) buttons.push(h(NButton, { size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }))
    if (hasPermission('sys:dept:delete')) buttons.push(h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row) }, { default: () => '删除' }))
    return buttons.length > 0 ? h(NSpace, null, { default: () => buttons }) : '-'
  }}
]

const modalVisible = ref(false)
const modalTitle = ref('新增部门')
const formRef = ref<FormInst | null>(null)
const submitLoading = ref(false)
const formData = reactive<SysDept>({ id: undefined, parentId: 0, deptName: '', sort: 0, leader: '', phone: '', email: '', status: 1 })
const rules: FormRules = {
  deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }]
}

const deptOptions = computed(() => {
  const root = { id: 0, deptName: '主目录', children: tableData.value }
  return [root]
})

async function loadData() {
  loading.value = true
  try { tableData.value = await deptApi.tree(searchForm.deptName, searchForm.status ?? undefined) }
  finally { loading.value = false }
}

function handleSearch() { loadData() }
function handleReset() { searchForm.deptName = ''; searchForm.status = null; loadData() }

function handleAdd(parentId?: number) {
  modalTitle.value = '新增部门'
  Object.assign(formData, { id: undefined, parentId: parentId || 0, deptName: '', sort: 0, leader: '', phone: '', email: '', status: 1 })
  modalVisible.value = true
}

function handleEdit(row: SysDept) {
  modalTitle.value = '编辑部门'
  Object.assign(formData, row)
  modalVisible.value = true
}

async function handleSubmit() {
  try {
    await formRef.value?.validate()
    submitLoading.value = true
    if (formData.id) { await deptApi.update(formData); message.success('更新成功') }
    else { await deptApi.create(formData); message.success('创建成功') }
    modalVisible.value = false
    loadData()
  } finally { submitLoading.value = false }
}

function handleDelete(row: SysDept) {
  dialog.warning({
    title: '提示', content: `确定要删除部门"${row.deptName}"吗？`, positiveText: '确定', negativeText: '取消',
    onPositiveClick: async () => { await deptApi.delete(row.id!); message.success('删除成功'); loadData() }
  })
}

onMounted(() => loadData())
</script>
