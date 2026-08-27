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
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { RefreshOutline, SearchOutline } from '@vicons/ionicons5'
import { workflowApi, type WorkflowTask } from '@/api/workflow'
import type { DataTableColumns } from 'naive-ui'

const loading = ref(false)
const tableData = ref<WorkflowTask[]>([])
const searchForm = reactive({ flowName: '' })
const pagination = reactive({ page: 1, pageSize: 10, itemCount: 0 })

const statusMap: Record<string, string> = {
  '0': '待提交', '1': '审批中', '2': '审批通过', '4': '终止',
  '5': '作废', '6': '撤销', '8': '已完成', '9': '已退回', '10': '失效', '11': '拿回'
}

const columns: DataTableColumns<WorkflowTask> = [
  { title: '流程名称', key: 'flowName', minWidth: 140 },
  { title: '流程编码', key: 'flowCode', width: 140 },
  { title: '节点名称', key: 'nodeName', width: 140 },
  { title: '业务ID', key: 'businessId', width: 140 },
  {
    title: '流程状态',
    key: 'flowStatus',
    width: 120,
    render: (row) => statusMap[row.flowStatus] || row.flowStatus
  },
  { title: '办理时间', key: 'createTime', width: 170 }
]

async function loadData() {
  loading.value = true
  try {
    const res = await workflowApi.donePage({
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

onMounted(() => loadData())
</script>
