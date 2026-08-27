<template>
  <div class="page-container">
    <n-card class="page-layout">
      <div class="search-form">
        <n-form inline :model="searchForm" label-placement="left">
          <n-form-item label="流程名称">
            <n-input v-model:value="searchForm.flowName" placeholder="请输入流程名称" clearable />
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

      <n-data-table :columns="columns" :data="tableData" :loading="loading" remote />

      <div class="pagination-container" style="display: flex; justify-content: flex-end; margin-top: 12px">
        <n-pagination
          v-model:page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :item-count="pagination.itemCount"
          :page-sizes="[10, 20, 50]"
          show-size-picker
          @update:page="loadData"
          @update:page-size="handlePageSizeChange"
        >
          <template #prefix>共 {{ pagination.itemCount }} 条</template>
        </n-pagination>
      </div>
    </n-card>

    <n-modal v-model:show="showApproveModal" preset="card" title="任务审批" style="width: 520px">
      <n-form label-placement="top">
        <n-form-item label="审批意见">
          <n-input v-model:value="approveForm.message" type="textarea" placeholder="请输入审批意见" :rows="4" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showApproveModal = false">取消</n-button>
          <n-button type="error" :loading="submitting" @click="handleReject">驳回</n-button>
          <n-button type="primary" :loading="submitting" @click="handleApprove">通过</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, reactive, ref } from 'vue'
import { NButton, NSpace, useMessage, type DataTableColumns } from 'naive-ui'
import { RefreshOutline, SearchOutline } from '@vicons/ionicons5'
import { workflowApi, type WorkflowTask } from '@/api/workflow'

const message = useMessage()
const loading = ref(false)
const submitting = ref(false)
const showApproveModal = ref(false)
const currentTask = ref<WorkflowTask | null>(null)
const tableData = ref<WorkflowTask[]>([])
const searchForm = reactive({ flowName: '' })
const approveForm = reactive({ message: '' })
const pagination = reactive({ page: 1, pageSize: 10, itemCount: 0 })

const columns: DataTableColumns<WorkflowTask> = [
  { title: '流程名称', key: 'flowName', minWidth: 140 },
  { title: '流程编码', key: 'flowCode', width: 140 },
  { title: '当前节点', key: 'nodeName', width: 140 },
  { title: '业务ID', key: 'businessId', width: 140 },
  { title: '发起人', key: 'approverName', width: 120 },
  { title: '创建时间', key: 'createTime', width: 170 },
  {
    title: '操作',
    key: 'actions',
    width: 120,
    fixed: 'right',
    render: (row) => h(NButton, { size: 'small', type: 'primary', onClick: () => openApprove(row) }, { default: () => '办理' })
  }
]

async function loadData() {
  loading.value = true
  try {
    const res = await workflowApi.todoPage({
      page: pagination.page,
      pageSize: pagination.pageSize,
      flowName: searchForm.flowName || undefined
    })
    tableData.value = res.list || []
    pagination.itemCount = res.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.page = 1
  loadData()
}

function handleReset() {
  searchForm.flowName = ''
  handleSearch()
}

function handlePageSizeChange() {
  pagination.page = 1
  loadData()
}

function openApprove(row: WorkflowTask) {
  currentTask.value = row
  approveForm.message = ''
  showApproveModal.value = true
}

async function handleApprove() {
  if (!currentTask.value) return
  submitting.value = true
  try {
    await workflowApi.approve({ taskId: currentTask.value.id, message: approveForm.message })
    message.success('审批通过')
    showApproveModal.value = false
    loadData()
  } finally {
    submitting.value = false
  }
}

async function handleReject() {
  if (!currentTask.value) return
  submitting.value = true
  try {
    await workflowApi.reject({ taskId: currentTask.value.id, message: approveForm.message })
    message.success('已驳回')
    showApproveModal.value = false
    loadData()
  } finally {
    submitting.value = false
  }
}

onMounted(() => loadData())
</script>
