<template>
  <div class="page-container">
    <n-card>
      <div class="table-toolbar">
        <n-button @click="loadData">
          <template #icon><n-icon><RefreshOutline /></n-icon></template>
          刷新
        </n-button>
      </div>
      
      <n-data-table :columns="columns" :data="tableData" :loading="loading" :row-key="(row: OnlineUser) => row.tokenId" />
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, h, onMounted } from 'vue'
import { NButton, NSpace, useMessage, useDialog, type DataTableColumns } from 'naive-ui'
import { RefreshOutline } from '@vicons/ionicons5'
import { onlineApi, type OnlineUser } from '@/api/monitor'

const message = useMessage()
const dialog = useDialog()

const tableData = ref<OnlineUser[]>([])
const loading = ref(false)

const columns: DataTableColumns<OnlineUser> = [
  { title: '会话ID', key: 'tokenId', ellipsis: { tooltip: true } },
  { title: '用户ID', key: 'userId', width: 100 },
  { title: '登录时间', key: 'loginTime', width: 180 },
  { title: '最后访问时间', key: 'lastAccessTime', width: 180 },
  { title: '操作', key: 'actions', width: 100, fixed: 'right', render(row) {
    return h(NSpace, null, { default: () => [
      h(NButton, { size: 'small', type: 'error', onClick: () => handleForceLogout(row) }, { default: () => '强退' })
    ]})
  }}
]

async function loadData() {
  loading.value = true
  try { tableData.value = await onlineApi.list() }
  finally { loading.value = false }
}

function handleForceLogout(row: OnlineUser) {
  dialog.warning({
    title: '提示', content: '确定要强制下线该用户吗？', positiveText: '确定', negativeText: '取消',
    onPositiveClick: async () => { await onlineApi.forceLogout(row.tokenId); message.success('操作成功'); loadData() }
  })
}

onMounted(() => loadData())
</script>
