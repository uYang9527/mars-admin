<template>
  <div class="page-container">
    <n-grid :cols="2" :x-gap="16" :y-gap="16">
      <n-gi>
        <n-card title="基本信息">
          <n-descriptions :column="1" label-placement="left">
            <n-descriptions-item label="Redis版本">{{ cacheInfo.stats?.redis_version }}</n-descriptions-item>
            <n-descriptions-item label="运行天数">{{ cacheInfo.stats?.uptime_in_days }} 天</n-descriptions-item>
            <n-descriptions-item label="连接客户端数">{{ cacheInfo.stats?.connected_clients }}</n-descriptions-item>
            <n-descriptions-item label="已处理命令数">{{ cacheInfo.stats?.total_commands_processed }}</n-descriptions-item>
            <n-descriptions-item label="每秒处理命令">{{ cacheInfo.stats?.instantaneous_ops_per_sec }}</n-descriptions-item>
          </n-descriptions>
        </n-card>
      </n-gi>
      <n-gi>
        <n-card title="内存信息">
          <n-descriptions :column="1" label-placement="left">
            <n-descriptions-item label="已用内存">{{ cacheInfo.memory?.used_memory_human }}</n-descriptions-item>
            <n-descriptions-item label="内存峰值">{{ cacheInfo.memory?.used_memory_peak_human }}</n-descriptions-item>
            <n-descriptions-item label="Key数量">{{ cacheInfo.dbSize }}</n-descriptions-item>
          </n-descriptions>
        </n-card>
      </n-gi>
    </n-grid>

    <n-card title="缓存键列表" style="margin-top: 16px">
      <div class="search-form">
        <n-form inline label-placement="left">
          <n-form-item label="键名模式">
            <n-input v-model:value="searchPattern" placeholder="请输入键名模式" clearable style="width: 300px" />
          </n-form-item>
          <n-form-item>
            <n-button type="primary" @click="loadKeys">
              <template #icon><n-icon><SearchOutline /></n-icon></template>
              搜索
            </n-button>
          </n-form-item>
        </n-form>
      </div>
      
      <n-data-table :columns="columns" :data="keys" :loading="keysLoading" :row-key="(row: string) => row" />
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, h, onMounted } from 'vue'
import { NButton, useMessage, useDialog, type DataTableColumns } from 'naive-ui'
import { SearchOutline } from '@vicons/ionicons5'
import { cacheApi } from '@/api/monitor'

const message = useMessage()
const dialog = useDialog()

const cacheInfo = ref<any>({})
const keys = ref<string[]>([])
const keysLoading = ref(false)
const searchPattern = ref('*')

const columns: DataTableColumns<string> = [
  { title: '键名', key: 'key', render(row) { return h('span', {}, row) }},
  { title: '操作', key: 'actions', width: 100, render(row) {
    return h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row) }, { default: () => '删除' })
  }}
]

async function loadCacheInfo() {
  try { cacheInfo.value = await cacheApi.info() }
  catch { /* ignore */ }
}

async function loadKeys() {
  keysLoading.value = true
  try { keys.value = await cacheApi.keys(searchPattern.value) || [] }
  finally { keysLoading.value = false }
}

function handleDelete(key: string) {
  dialog.warning({
    title: '提示', content: `确定要删除缓存"${key}"吗？`, positiveText: '确定', negativeText: '取消',
    onPositiveClick: async () => { await cacheApi.delete(key); message.success('删除成功'); loadKeys() }
  })
}

onMounted(() => { loadCacheInfo(); loadKeys() })
</script>
