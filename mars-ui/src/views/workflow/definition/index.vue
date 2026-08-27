<template>
  <div class="page-container">
    <n-card class="page-layout">
      <div class="search-form">
        <n-form inline :model="searchForm" label-placement="left">
          <n-form-item label="流程名称">
            <n-input v-model:value="searchForm.flowName" placeholder="请输入流程名称" clearable />
          </n-form-item>
          <n-form-item label="流程编码">
            <n-input v-model:value="searchForm.flowCode" placeholder="请输入流程编码" clearable />
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
        <n-space>
          <n-button type="primary" @click="handleCreate">
            <template #icon><n-icon><AddOutline /></n-icon></template>
            新建流程
          </n-button>
        </n-space>
      </div>

      <n-data-table :columns="columns" :data="tableData" :loading="loading" remote />

      <div class="pagination-container" style="display: flex; justify-content: flex-end; margin-top: 12px">
        <n-pagination
          v-model:page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :item-count="pagination.itemCount"
          :page-sizes="[10, 20, 50]"
          show-size-picker
          show-quick-jumper
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
import { h, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { NButton, NSpace, NTag, useDialog, useMessage, type DataTableColumns } from 'naive-ui'
import { AddOutline, RefreshOutline, SearchOutline } from '@vicons/ionicons5'
import { workflowApi, type FlowDefinition } from '@/api/workflow'

const router = useRouter()
const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const tableData = ref<FlowDefinition[]>([])
const searchForm = reactive({ flowName: '', flowCode: '' })
const pagination = reactive({ page: 1, pageSize: 10, itemCount: 0 })

const publishMap: Record<number, { type: 'default' | 'success' | 'warning'; label: string }> = {
  0: { type: 'default', label: '未发布' },
  1: { type: 'success', label: '已发布' },
  9: { type: 'warning', label: '已失效' }
}

const columns: DataTableColumns<FlowDefinition> = [
  { title: '流程名称', key: 'flowName', minWidth: 160 },
  { title: '流程编码', key: 'flowCode', width: 160 },
  { title: '版本', key: 'version', width: 80 },
  { title: '类别', key: 'category', width: 120 },
  {
    title: '发布状态',
    key: 'isPublish',
    width: 100,
    render: (row) => {
      const item = publishMap[row.isPublish] || publishMap[0]
      return h(NTag, { type: item.type, size: 'small' }, { default: () => item.label })
    }
  },
  {
    title: '激活状态',
    key: 'activityStatus',
    width: 100,
    render: (row) => h(NTag, { type: row.activityStatus === 1 ? 'success' : 'warning', size: 'small' }, {
      default: () => row.activityStatus === 1 ? '激活' : '挂起'
    })
  },
  { title: '创建时间', key: 'createTime', width: 170 },
  {
    title: '操作',
    key: 'actions',
    width: 320,
    fixed: 'right',
    render: (row) => h(NSpace, { size: 'small' }, {
      default: () => [
        h(NButton, { size: 'small', type: 'primary', onClick: () => handleDesign(row) }, { default: () => '设计' }),
        row.isPublish === 1
          ? h(NButton, { size: 'small', onClick: () => handleUnpublish(row) }, { default: () => '取消发布' })
          : h(NButton, { size: 'small', type: 'success', onClick: () => handlePublish(row) }, { default: () => '发布' }),
        h(NButton, { size: 'small', onClick: () => handleCopy(row) }, { default: () => '复制' }),
        h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row) }, { default: () => '删除' })
      ]
    })
  }
]

async function loadData() {
  loading.value = true
  try {
    const res = await workflowApi.definitionPage({
      page: pagination.page,
      pageSize: pagination.pageSize,
      flowName: searchForm.flowName || undefined,
      flowCode: searchForm.flowCode || undefined
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
  searchForm.flowCode = ''
  handleSearch()
}

function handlePageSizeChange() {
  pagination.page = 1
  loadData()
}

function handleCreate() {
  router.push({ name: 'WorkflowDesigner', query: { onlyDesignShow: 'false' } })
}

function handleDesign(row: FlowDefinition) {
  router.push({
    name: 'WorkflowDesigner',
    query: {
      id: String(row.id),
      // 编辑已有流程：直接进入流程图画布，不显示基础信息页
      onlyDesignShow: 'true',
      disabled: row.isPublish === 1 ? 'true' : 'false'
    }
  })
}

async function handlePublish(row: FlowDefinition) {
  await workflowApi.publishDefinition(row.id)
  message.success('发布成功')
  loadData()
}

async function handleUnpublish(row: FlowDefinition) {
  await workflowApi.unpublishDefinition(row.id)
  message.success('已取消发布')
  loadData()
}

async function handleCopy(row: FlowDefinition) {
  await workflowApi.copyDefinition(row.id)
  message.success('复制成功')
  loadData()
}

function handleDelete(row: FlowDefinition) {
  dialog.warning({
    title: '确认删除',
    content: `确定删除流程「${row.flowName}」吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      await workflowApi.removeDefinition([row.id])
      message.success('删除成功')
      loadData()
    }
  })
}

onMounted(() => loadData())
</script>
