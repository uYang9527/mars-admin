<template>
  <div class="page-container">
    <n-card class="page-layout">
      <div class="search-form">
        <n-form inline :model="searchForm" label-placement="left">
          <n-form-item label="流程名称">
            <n-input v-model:value="searchForm.flowName" placeholder="请输入流程名称" clearable />
          </n-form-item>
          <n-form-item label="流程状态">
            <n-select
              v-model:value="searchForm.flowStatus"
              placeholder="全部状态"
              clearable
              :options="statusOptions"
              style="width: 160px"
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
              <n-button type="success" @click="showStartModal = true">
                <template #icon><n-icon><PlayOutline /></n-icon></template>
                发起流程
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

    <n-modal v-model:show="showStartModal" preset="card" title="发起流程" style="width: 520px">
      <n-form label-placement="top">
        <n-form-item label="选择流程" required>
          <n-select
            v-model:value="startForm.flowCode"
            placeholder="请选择已发布的流程"
            :options="flowOptions"
            filterable
          />
        </n-form-item>
        <n-form-item label="业务ID" required>
          <n-input v-model:value="startForm.businessId" placeholder="请输入业务唯一标识" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showStartModal = false">取消</n-button>
          <n-button type="primary" :loading="submitting" @click="handleStart">发起</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, reactive, ref } from 'vue'
import { NButton, NSpace, useDialog, useMessage, type DataTableColumns, type SelectOption } from 'naive-ui'
import { PlayOutline, RefreshOutline, SearchOutline } from '@vicons/ionicons5'
import { workflowApi, type FlowDefinition, type WorkflowInstance } from '@/api/workflow'

const message = useMessage()
const dialog = useDialog()
const loading = ref(false)
const submitting = ref(false)
const showStartModal = ref(false)
const tableData = ref<WorkflowInstance[]>([])
const flowOptions = ref<SelectOption[]>([])
const searchForm = reactive({ flowName: '', flowStatus: null as string | null })
const startForm = reactive({ flowCode: null as string | null, businessId: '' })
const pagination = reactive({ page: 1, pageSize: 10, itemCount: 0 })

const statusOptions = [
  { label: '待提交', value: '0' },
  { label: '审批中', value: '1' },
  { label: '审批通过', value: '2' },
  { label: '终止', value: '4' },
  { label: '已完成', value: '8' },
  { label: '已退回', value: '9' }
]

const statusMap: Record<string, string> = {
  '0': '待提交', '1': '审批中', '2': '审批通过', '4': '终止',
  '5': '作废', '6': '撤销', '8': '已完成', '9': '已退回', '10': '失效', '11': '拿回'
}

const columns: DataTableColumns<WorkflowInstance> = [
  { title: '流程名称', key: 'flowName', minWidth: 140 },
  { title: '流程编码', key: 'flowCode', width: 140 },
  { title: '业务ID', key: 'businessId', width: 140 },
  { title: '当前节点', key: 'nodeName', width: 140 },
  {
    title: '流程状态',
    key: 'flowStatus',
    width: 120,
    render: (row) => statusMap[row.flowStatus] || row.flowStatus
  },
  { title: '发起人', key: 'createByName', width: 120 },
  { title: '创建时间', key: 'createTime', width: 170 },
  {
    title: '操作',
    key: 'actions',
    width: 100,
    fixed: 'right',
    render: (row) => {
      if (row.flowStatus === '1') {
        return h(NButton, { size: 'small', type: 'error', onClick: () => handleTerminate(row) }, { default: () => '终止' })
      }
      return null
    }
  }
]

async function loadData() {
  loading.value = true
  try {
    const res = await workflowApi.instancePage({
      page: pagination.page,
      pageSize: pagination.pageSize,
      flowName: searchForm.flowName || undefined,
      flowStatus: searchForm.flowStatus || undefined
    })
    tableData.value = res.list || []
    pagination.itemCount = res.total || 0
  } finally {
    loading.value = false
  }
}

async function loadPublishedFlows() {
  const list: FlowDefinition[] = await workflowApi.publishedList()
  flowOptions.value = list.map(item => ({
    label: `${item.flowName} (${item.flowCode})`,
    value: item.flowCode
  }))
}

function handleSearch() {
  pagination.page = 1
  loadData()
}

function handleReset() {
  searchForm.flowName = ''
  searchForm.flowStatus = null
  handleSearch()
}

function handlePageSizeChange() {
  pagination.page = 1
  loadData()
}

async function handleStart() {
  if (!startForm.flowCode || !startForm.businessId) {
    message.warning('请选择流程并填写业务ID')
    return
  }
  submitting.value = true
  try {
    await workflowApi.startInstance({
      flowCode: startForm.flowCode,
      businessId: startForm.businessId
    })
    message.success('流程发起成功')
    showStartModal.value = false
    startForm.flowCode = null
    startForm.businessId = ''
    loadData()
  } finally {
    submitting.value = false
  }
}

function handleTerminate(row: WorkflowInstance) {
  dialog.warning({
    title: '终止流程',
    content: `确定终止流程实例「${row.flowName}」吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      await workflowApi.terminateInstance(row.id, '管理员终止')
      message.success('流程已终止')
      loadData()
    }
  })
}

onMounted(() => {
  loadData()
  loadPublishedFlows()
})
</script>
