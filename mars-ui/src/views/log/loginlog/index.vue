<template>
  <div class="page-container">
    <n-card>
      <div class="search-form">
        <n-form inline :model="searchForm" label-placement="left">
          <n-form-item label="用户名">
            <n-input v-model:value="searchForm.username" placeholder="请输入用户名" clearable />
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
        <n-button type="error" @click="handleClean">
          <template #icon><n-icon><TrashOutline /></n-icon></template>
          清空日志
        </n-button>
      </div>
      
      <n-data-table :columns="columns" :data="tableData" :loading="loading" :pagination="pagination"
        :row-key="(row: SysLoginLog) => row.id" @update:page="handlePageChange" @update:page-size="handlePageSizeChange" />
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted } from 'vue'
import { NButton, NTag, NSpace, useMessage, useDialog, type DataTableColumns } from 'naive-ui'
import { SearchOutline, RefreshOutline, TrashOutline } from '@vicons/ionicons5'
import { loginLogApi, type SysLoginLog } from '@/api/monitor'

const message = useMessage()
const dialog = useDialog()

const searchForm = reactive({ username: '', status: null as number | null })
const statusOptions = [{ label: '成功', value: 0 }, { label: '失败', value: 1 }]
const tableData = ref<SysLoginLog[]>([])
const loading = ref(false)
const pagination = reactive({ page: 1, pageSize: 10, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50] })

const columns: DataTableColumns<SysLoginLog> = [
  { title: 'ID', key: 'id', width: 80 },
  { title: '用户名', key: 'username', width: 120 },
  { title: 'IP地址', key: 'ipaddr', width: 140 },
  { title: '登录地点', key: 'loginLocation', width: 150 },
  { title: '浏览器', key: 'browser', width: 120 },
  { title: '操作系统', key: 'os', width: 150 },
  { title: '状态', key: 'status', width: 80, render(row) {
    return h(NTag, { type: row.status === 0 ? 'success' : 'error', size: 'small' }, { default: () => row.status === 0 ? '成功' : '失败' })
  }},
  { title: '提示信息', key: 'msg', ellipsis: { tooltip: true } },
  { title: '登录时间', key: 'loginTime', width: 180 },
  { title: '操作', key: 'actions', width: 80, fixed: 'right', render(row) {
    return h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row) }, { default: () => '删除' })
  }}
]

async function loadData() {
  loading.value = true
  try {
    const res = await loginLogApi.page({ page: pagination.page, pageSize: pagination.pageSize, ...searchForm })
    tableData.value = res.list
    pagination.itemCount = res.total
  } finally { loading.value = false }
}

function handleSearch() { pagination.page = 1; loadData() }
function handleReset() { searchForm.username = ''; searchForm.status = null; handleSearch() }
function handlePageChange(page: number) { pagination.page = page; loadData() }
function handlePageSizeChange(pageSize: number) { pagination.pageSize = pageSize; pagination.page = 1; loadData() }

function handleDelete(row: SysLoginLog) {
  dialog.warning({
    title: '提示', content: '确定要删除该日志吗？', positiveText: '确定', negativeText: '取消',
    onPositiveClick: async () => { await loginLogApi.delete(row.id!); message.success('删除成功'); loadData() }
  })
}

function handleClean() {
  dialog.warning({
    title: '提示', content: '确定要清空所有登录日志吗？', positiveText: '确定', negativeText: '取消',
    onPositiveClick: async () => { await loginLogApi.clean(); message.success('清空成功'); loadData() }
  })
}

onMounted(() => loadData())
</script>
