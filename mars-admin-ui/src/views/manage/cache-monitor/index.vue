<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue';
import { Alert, Button, Card, Progress, Tag } from 'ant-design-vue';
import { fetchCacheMonitor, fetchCacheStats } from '@/service/api';
import SvgIcon from '@/components/custom/svg-icon.vue';

defineOptions({
  name: 'CacheMonitor'
});

const loading = ref(false);
const monitorData = ref<Api.CacheManage.MonitorInfo | null>(null);
const cacheStatsData = ref<Api.CacheManage.CacheStats | null>(null);
const autoRefresh = ref(true);
const refreshInterval = ref(5000); // 5秒刷新一次
let timer: ReturnType<typeof setInterval> | null = null;

// 获取监控数据
async function getMonitorData() {
  loading.value = true;
  try {
    const { data, error } = await fetchCacheMonitor();
    if (!error && data) {
      monitorData.value = data;
    }
  } finally {
    loading.value = false;
  }
}

// 获取缓存统计数据
async function getCacheStats() {
  try {
    const { data, error } = await fetchCacheStats();
    if (!error && data) {
      cacheStatsData.value = data;
    }
  } catch (e) {
    console.error('获取缓存统计失败:', e);
  }
}

// 刷新所有数据
async function refreshData() {
  await Promise.all([getMonitorData(), getCacheStats()]);
}

// 启动自动刷新
function startAutoRefresh() {
  if (timer) {
    clearInterval(timer);
  }
  if (autoRefresh.value) {
    timer = setInterval(refreshData, refreshInterval.value);
  }
}

// 停止自动刷新
function stopAutoRefresh() {
  if (timer) {
    clearInterval(timer);
    timer = null;
  }
}

// 切换自动刷新
function toggleAutoRefresh() {
  autoRefresh.value = !autoRefresh.value;
  if (autoRefresh.value) {
    startAutoRefresh();
  } else {
    stopAutoRefresh();
  }
}

// 手动刷新
async function handleRefresh() {
  await refreshData();
  window.$message?.success('刷新成功');
}

// 格式化运行时间
function formatUptime(seconds: string | number) {
  const num = Number(seconds);
  const days = Math.floor(num / 86400);
  const hours = Math.floor((num % 86400) / 3600);
  const minutes = Math.floor((num % 3600) / 60);

  if (days > 0) return `${days}天${hours}小时${minutes}分钟`;
  if (hours > 0) return `${hours}小时${minutes}分钟`;
  return `${minutes}分钟`;
}

// 获取状态颜色
function getStatusColor(value: string | number, type: 'percentage' | 'number' = 'percentage') {
  const num = Number(value);
  if (type === 'percentage') {
    if (num >= 90) return 'red';
    if (num >= 70) return 'orange';
    return 'green';
  }
  return 'blue';
}

onMounted(() => {
  refreshData();
  startAutoRefresh();
});

onUnmounted(() => {
  stopAutoRefresh();
});
</script>

<template>
  <div class="flex-col-stretch gap-16px overflow-auto p-10px">
    <!-- 页面标题 -->
    <div class="flex-y-center justify-between">
      <div class="flex-y-center gap-8px">
        <SvgIcon icon="mdi:monitor-dashboard" class="text-20px text-blue-500" />
        <h2 class="text-18px font-bold">缓存监控</h2>
      </div>
      <div class="flex-y-center gap-12px">
        <div class="flex-y-center gap-8px text-12px text-gray-500">
          <SvgIcon :icon="autoRefresh ? 'mdi:play' : 'mdi:pause'" class="text-14px" />
          <span>{{ autoRefresh ? '自动刷新中' : '已暂停刷新' }}</span>
        </div>
        <Button size="small" class="flex-y-center gap-4px" @click="toggleAutoRefresh">
          <SvgIcon :icon="autoRefresh ? 'mdi:pause' : 'mdi:play'" class="text-12px" />
          {{ autoRefresh ? '暂停刷新' : '开始刷新' }}
        </Button>
        <Button type="primary" size="small" :loading="loading" class="flex-y-center gap-4px" @click="handleRefresh">
          <SvgIcon icon="mdi:refresh" class="text-12px" />
          刷新
        </Button>
      </div>
    </div>

    <!-- 状态概览 -->
    <div class="grid grid-cols-1 gap-16px lg:grid-cols-4 md:grid-cols-2">
      <!-- Redis 状态 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:database" class="text-red-500" />
            <span>Redis 状态</span>
          </div>
        </template>
        <div v-if="monitorData" class="space-y-12px">
          <div class="flex-y-center justify-between">
            <span class="text-gray-600">版本</span>
            <Tag color="blue">{{ monitorData.redis.redis_version }}</Tag>
          </div>
          <div class="flex-y-center justify-between">
            <span class="text-gray-600">运行模式</span>
            <Tag :color="monitorData.redis.redis_mode === 'standalone' ? 'green' : 'orange'">
              {{ monitorData.redis.redis_mode === 'standalone' ? '单机' : '集群' }}
            </Tag>
          </div>
          <div class="flex-y-center justify-between">
            <span class="text-gray-600">端口</span>
            <span class="font-mono">{{ monitorData.redis.tcp_port }}</span>
          </div>
          <div class="flex-y-center justify-between">
            <span class="text-gray-600">运行时间</span>
            <span class="text-12px">{{ formatUptime(monitorData.redis.uptime_in_seconds) }}</span>
          </div>
        </div>
        <div v-else class="h-100px flex-center">
          <ASpin />
        </div>
      </Card>

      <!-- 内存使用 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:memory" class="text-purple-500" />
            <span>内存使用</span>
          </div>
        </template>
        <div v-if="monitorData" class="space-y-12px">
          <div>
            <div class="mb-4px flex-y-center justify-between">
              <span class="text-gray-600">Redis 内存</span>
              <span class="text-12px font-mono">{{ monitorData.redis.used_memory_human }}</span>
            </div>
            <div class="mb-8px text-10px text-gray-400">峰值: {{ monitorData.redis.used_memory_peak_human }}</div>
          </div>
          <div>
            <div class="mb-4px flex-y-center justify-between">
              <span class="text-gray-600">JVM 堆内存</span>
              <span class="text-12px font-mono">{{ monitorData.jvm.heap_used }}</span>
            </div>
            <Progress
              :percent="parseFloat(monitorData.jvm.heap_usage)"
              :stroke-color="getStatusColor(monitorData.jvm.heap_usage)"
              size="small"
              :show-info="false"
            />
            <div class="mt-2px text-10px text-gray-400">最大: {{ monitorData.jvm.heap_max }}</div>
          </div>
        </div>
        <div v-else class="h-100px flex-center">
          <ASpin />
        </div>
      </Card>

      <!-- 连接与性能 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:connection" class="text-green-500" />
            <span>连接与性能</span>
          </div>
        </template>
        <div v-if="monitorData" class="space-y-12px">
          <div class="flex-y-center justify-between">
            <span class="text-gray-600">连接数</span>
            <span class="text-blue-600 font-mono">{{ monitorData.redis.connected_clients }}</span>
          </div>
          <div class="flex-y-center justify-between">
            <span class="text-gray-600">命中率</span>
            <Tag :color="getStatusColor(monitorData.redis.hit_rate)">
              {{ monitorData.redis.hit_rate }}
            </Tag>
          </div>
          <div class="flex-y-center justify-between">
            <span class="text-gray-600">QPS</span>
            <span class="text-orange-600 font-mono">{{ monitorData.redis.instantaneous_ops_per_sec }}</span>
          </div>
          <div class="flex-y-center justify-between">
            <span class="text-gray-600">总命令数</span>
            <span class="text-12px font-mono">
              {{ Number(monitorData.redis.total_commands_processed).toLocaleString() }}
            </span>
          </div>
        </div>
        <div v-else class="h-100px flex-center">
          <ASpin />
        </div>
      </Card>

      <!-- 缓存统计 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:chart-pie" class="text-orange-500" />
            <span>缓存统计</span>
          </div>
        </template>
        <div v-if="monitorData && cacheStatsData" class="space-y-12px">
          <div class="flex-y-center justify-between">
            <span class="text-gray-600">总键数</span>
            <span class="text-blue-600 font-mono">{{ cacheStatsData.total.toLocaleString() }}</span>
          </div>
          <div class="space-y-6px">
            <div
              v-for="space in cacheStatsData.spaces.slice(0, 3)"
              :key="space.space"
              class="flex-y-center justify-between text-12px"
            >
              <div class="flex-y-center gap-4px">
                <div class="h-8px w-8px rounded-full" :style="{ backgroundColor: space.color }"></div>
                <span>{{ space.name }}</span>
              </div>
              <span class="font-mono">{{ space.count }}</span>
            </div>
          </div>
          <div class="border-t border-gray-100 pt-4px text-center text-10px text-gray-400">查看完整统计 →</div>
        </div>
        <div v-else class="h-100px flex-center">
          <ASpin />
        </div>
      </Card>
    </div>

    <!-- 详细信息 -->
    <div class="grid grid-cols-1 gap-16px lg:grid-cols-2">
      <!-- Redis 详细信息 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:information" class="text-blue-500" />
            <span>Redis 详细信息</span>
          </div>
        </template>
        <div v-if="monitorData" class="space-y-12px">
          <div class="grid grid-cols-2 gap-12px text-12px">
            <div class="space-y-8px">
              <div class="flex justify-between">
                <span class="text-gray-600">内存碎片率:</span>
                <span class="font-mono">{{ monitorData.redis.mem_fragmentation_ratio }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">最大内存:</span>
                <span class="font-mono">{{ monitorData.redis.maxmemory_human || '未设置' }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">缓存命中:</span>
                <span class="text-green-600 font-mono">
                  {{ Number(monitorData.redis.keyspace_hits).toLocaleString() }}
                </span>
              </div>
            </div>
            <div class="space-y-8px">
              <div class="flex justify-between">
                <span class="text-gray-600">运行天数:</span>
                <span class="font-mono">{{ monitorData.redis.uptime_in_days }} 天</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">峰值内存:</span>
                <span class="font-mono">{{ monitorData.redis.used_memory_peak_human }}</span>
              </div>
              <div class="flex justify-between">
                <span class="text-gray-600">缓存未命中:</span>
                <span class="text-red-600 font-mono">
                  {{ Number(monitorData.redis.keyspace_misses).toLocaleString() }}
                </span>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="h-120px flex-center">
          <ASpin />
        </div>
      </Card>

      <!-- 系统信息 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:server" class="text-green-500" />
            <span>系统信息</span>
          </div>
        </template>
        <div v-if="monitorData" class="space-y-12px">
          <div class="text-12px space-y-8px">
            <div class="flex justify-between">
              <span class="text-gray-600">服务器时间:</span>
              <span class="font-mono">{{ monitorData.server.server_time }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">Java 版本:</span>
              <span class="font-mono">{{ monitorData.server.java_version }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">操作系统:</span>
              <span class="font-mono">{{ monitorData.server.os_name }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">系统架构:</span>
              <span class="font-mono">{{ monitorData.server.os_arch }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">非堆内存:</span>
              <span class="font-mono">{{ monitorData.jvm.non_heap_used }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600">已提交内存:</span>
              <span class="font-mono">{{ monitorData.jvm.heap_committed }}</span>
            </div>
          </div>
        </div>
        <div v-else class="h-120px flex-center">
          <ASpin />
        </div>
      </Card>
    </div>

    <!-- 缓存空间分布 -->
    <Card v-if="cacheStatsData" class="card-wrapper" :bordered="false">
      <template #title>
        <div class="flex-y-center gap-8px">
          <SvgIcon icon="mdi:chart-donut" class="text-purple-500" />
          <span>缓存空间分布</span>
        </div>
      </template>
      <div class="grid grid-cols-2 gap-16px lg:grid-cols-8 md:grid-cols-4">
        <div
          v-for="space in cacheStatsData.spaces"
          :key="space.space"
          class="rounded-12px bg-gray-50 p-16px text-center transition-all duration-300 dark:bg-gray-800/50 hover:shadow-md"
        >
          <div
            class="mx-auto mb-8px h-40px w-40px flex-center rounded-full text-white font-bold"
            :style="{ backgroundColor: space.color }"
          >
            {{ space.count }}
          </div>
          <div class="mb-2px text-12px text-gray-600">{{ space.name }}</div>
          <div class="text-10px text-gray-400">{{ space.space }}</div>
        </div>
      </div>
    </Card>

    <!-- 提示信息 -->
    <Alert
      message="监控说明"
      description="页面会每5秒自动刷新监控数据，您也可以手动刷新。建议定期关注Redis内存使用率和命中率指标。"
      type="info"
      show-icon
      closable
    />
  </div>
</template>

<style scoped>
.card-wrapper {
  @apply shadow-sm border border-gray-100 dark:border-gray-700;
}

.card-wrapper :deep(.ant-card-head) {
  @apply border-gray-100 dark:border-gray-700;
}

.card-wrapper :deep(.ant-card-head-title) {
  @apply text-14px font-medium;
}
</style>
