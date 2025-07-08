<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue';
import { Alert, Button, Card, Progress, Tag } from 'ant-design-vue';
import { getMonitorInfo } from '@/service/api/system';
import SvgIcon from '@/components/custom/svg-icon.vue';

defineOptions({
  name: 'SystemMonitor'
});

const loading = ref(false);
const monitorData = ref<any>({});
const processInfo = ref<any>({});
const autoRefresh = ref(true);
const refreshInterval = ref(5000); // 5秒刷新一次
let timer: ReturnType<typeof setInterval> | null = null;

// 获取监控数据
async function getMonitorData() {
  loading.value = true;
  try {
    const { data, error } = await getMonitorInfo();
    if (!error && data) {
      monitorData.value = data;
    }
  } catch (error) {
    console.error('获取监控数据失败:', error);
  } finally {
    loading.value = false;
  }
}

// 刷新数据
async function refreshData() {
  await getMonitorData();
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

// 获取状态颜色
function getCpuColor() {
  const usage = monitorData.value.cpu?.usagePercent || 0;
  if (usage >= 90) return '#ff4d4f';
  if (usage >= 80) return '#ff7a45';
  if (usage >= 70) return '#ffa940';
  if (usage >= 50) return '#52c41a';
  return '#1890ff';
}

function getMemoryColor() {
  const usage = monitorData.value.memory?.usage || 0;
  if (usage >= 95) return '#ff4d4f';
  if (usage >= 85) return '#ff7a45';
  if (usage >= 75) return '#ffa940';
  if (usage >= 50) return '#52c41a';
  return '#1890ff';
}

function getJvmColor() {
  const usage = monitorData.value.jvm?.usage || 0;
  if (usage >= 95) return '#ff4d4f';
  if (usage >= 85) return '#ff7a45';
  if (usage >= 75) return '#ffa940';
  if (usage >= 50) return '#52c41a';
  return '#1890ff';
}

function getDiskColor(usage: number) {
  if (usage >= 95) return '#ff4d4f';
  if (usage >= 90) return '#ff7a45';
  if (usage >= 80) return '#ffa940';
  if (usage >= 60) return '#52c41a';
  return '#1890ff';
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
        <h2 class="text-18px font-bold">系统监控</h2>
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

    <!-- 核心监控指标 -->
    <div class="grid grid-cols-1 gap-16px lg:grid-cols-3 md:grid-cols-2">
      <!-- CPU监控 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:cpu-64-bit" class="text-blue-500" />
            <span>CPU监控</span>
          </div>
        </template>
        <div v-if="monitorData.cpu" class="space-y-16px">
          <div class="flex-center">
            <Progress
              type="circle"
              :percent="Math.round(monitorData.cpu.usagePercent || 0)"
              :stroke-color="getCpuColor()"
              :width="120"
              :stroke-width="8"
            >
              <template #format="percent">
                <div class="text-center">
                  <div class="text-24px font-bold">{{ percent }}%</div>
                  <div class="text-12px text-gray-500">CPU</div>
                </div>
              </template>
            </Progress>
          </div>
          <div class="space-y-8px">
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">核心数</span>
              <Tag color="blue">{{ monitorData.cpu.cpuNum || 0 }}个</Tag>
            </div>
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">物理核心</span>
              <Tag color="green">{{ monitorData.cpu.physicalCores || 0 }}个</Tag>
            </div>
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">频率</span>
              <span class="font-mono">{{ monitorData.cpu.frequency || 0 }}MHz</span>
            </div>
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">用户占用</span>
              <span class="font-mono">{{ (monitorData.cpu.userPercent || 0).toFixed(1) }}%</span>
            </div>
          </div>
        </div>
        <div v-else class="h-220px flex-center">
          <ASpin />
        </div>
      </Card>

      <!-- 内存监控 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:memory" class="text-purple-500" />
            <span>内存监控</span>
          </div>
        </template>
        <div v-if="monitorData.memory" class="space-y-16px">
          <div class="flex-center">
            <Progress
              type="circle"
              :percent="Math.round(monitorData.memory.usage || 0)"
              :stroke-color="getMemoryColor()"
              :width="120"
              :stroke-width="8"
            >
              <template #format="percent">
                <div class="text-center">
                  <div class="text-24px font-bold">{{ percent }}%</div>
                  <div class="text-12px text-gray-500">内存</div>
                </div>
              </template>
            </Progress>
          </div>
          <div class="space-y-8px">
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">总内存</span>
              <Tag color="blue">{{ monitorData.memory.totalFormatted || '0B' }}</Tag>
            </div>
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">已使用</span>
              <span class="font-mono">{{ monitorData.memory.usedFormatted || '0B' }}</span>
            </div>
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">可用</span>
              <span class="font-mono">{{ monitorData.memory.availableFormatted || '0B' }}</span>
            </div>
            <div v-if="monitorData.memory.swapTotal" class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">交换分区</span>
              <span class="font-mono">{{ (monitorData.memory.swapUsage || 0).toFixed(1) }}%</span>
            </div>
          </div>
        </div>
        <div v-else class="h-220px flex-center">
          <ASpin />
        </div>
      </Card>

      <!-- JVM监控 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:coffee" class="text-orange-500" />
            <span>JVM监控</span>
          </div>
        </template>
        <div v-if="monitorData.jvm" class="space-y-16px">
          <div class="flex-center">
            <Progress
              type="circle"
              :percent="Math.round(monitorData.jvm.usage || 0)"
              :stroke-color="getJvmColor()"
              :width="120"
              :stroke-width="8"
            >
              <template #format="percent">
                <div class="text-center">
                  <div class="text-24px font-bold">{{ percent }}%</div>
                  <div class="text-12px text-gray-500">JVM</div>
                </div>
              </template>
            </Progress>
          </div>
          <div class="space-y-8px">
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">堆内存</span>
              <Tag color="orange">{{ monitorData.jvm.heapFormatted || '0B' }}</Tag>
            </div>
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">非堆内存</span>
              <span class="font-mono">{{ monitorData.jvm.nonHeapFormatted || '0B' }}</span>
            </div>
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">Java版本</span>
              <span class="font-mono">{{ monitorData.jvm.version || '未知' }}</span>
            </div>
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">厂商</span>
              <span class="font-mono">{{ monitorData.jvm.vendor || '未知' }}</span>
            </div>
          </div>
        </div>
        <div v-else class="h-220px flex-center">
          <ASpin />
        </div>
      </Card>
    </div>

    <!-- 详细信息面板 -->
    <div class="grid grid-cols-1 gap-16px lg:grid-cols-2">
      <!-- 磁盘监控 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:harddisk" class="text-green-500" />
            <span>磁盘监控</span>
          </div>
        </template>
        <div v-if="monitorData.disk && monitorData.disk.length" class="space-y-12px">
          <div
            v-for="disk in monitorData.disk"
            :key="disk.driveName"
            class="rounded-8px bg-gray-50 p-12px dark:bg-gray-800/50"
          >
            <div class="mb-8px flex-y-center justify-between">
              <div class="flex-y-center gap-8px">
                <div class="h-8px w-8px rounded-full bg-green-500"></div>
                <span class="text-14px font-medium">{{ disk.name }}</span>
                <Tag size="small" color="blue">{{ disk.fsType }}</Tag>
              </div>
              <span class="text-12px font-mono">{{ disk.usage?.toFixed(1) }}%</span>
            </div>
            <div class="mb-8px">
              <Progress
                :percent="Math.round(disk.usage || 0)"
                :stroke-color="getDiskColor(disk.usage)"
                size="small"
                :show-info="false"
              />
            </div>
            <div class="grid grid-cols-2 gap-8px text-12px text-gray-600">
              <div>总空间: {{ disk.totalFormatted }}</div>
              <div>已使用: {{ disk.usedFormatted }}</div>
            </div>
          </div>
        </div>
        <div v-else class="h-200px flex-center">
          <ASpin />
        </div>
      </Card>

      <!-- 线程监控 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:graph-outline" class="text-indigo-500" />
            <span>线程监控</span>
          </div>
        </template>
        <div v-if="monitorData.thread" class="space-y-16px">
          <!-- 核心指标 -->
          <div class="grid grid-cols-2 gap-16px">
            <div class="text-center">
              <div class="text-24px text-blue-600 font-bold">{{ monitorData.thread.liveThreads || 0 }}</div>
              <div class="text-12px text-gray-500">活动线程</div>
            </div>
            <div class="text-center">
              <div class="text-24px text-green-600 font-bold">{{ monitorData.thread.peakThreads || 0 }}</div>
              <div class="text-12px text-gray-500">峰值线程</div>
            </div>
          </div>

          <!-- 线程状态分布 -->
          <div class="space-y-12px">
            <div class="text-12px text-gray-600 font-medium">线程状态分布</div>
            <div class="space-y-8px">
              <!-- 运行线程 -->
              <div class="flex-y-center justify-between">
                <div class="flex-y-center gap-8px">
                  <div class="h-8px w-8px rounded-full bg-green-500"></div>
                  <span class="text-12px text-gray-600">运行线程</span>
                </div>
                <div class="flex-y-center gap-8px">
                  <div class="w-60px flex-1">
                    <Progress
                      :percent="
                        Math.round(
                          ((monitorData.thread.runnableThreads || 0) / (monitorData.thread.liveThreads || 1)) * 100
                        )
                      "
                      stroke-color="#52c41a"
                      size="small"
                      :show-info="false"
                    />
                  </div>
                  <span class="min-w-24px text-12px font-mono">{{ monitorData.thread.runnableThreads || 0 }}</span>
                </div>
              </div>

              <!-- 等待线程 -->
              <div class="flex-y-center justify-between">
                <div class="flex-y-center gap-8px">
                  <div class="h-8px w-8px rounded-full bg-orange-500"></div>
                  <span class="text-12px text-gray-600">等待线程</span>
                </div>
                <div class="flex-y-center gap-8px">
                  <div class="w-60px flex-1">
                    <Progress
                      :percent="
                        Math.round(
                          ((monitorData.thread.waitingThreads || 0) / (monitorData.thread.liveThreads || 1)) * 100
                        )
                      "
                      stroke-color="#fa8c16"
                      size="small"
                      :show-info="false"
                    />
                  </div>
                  <span class="min-w-24px text-12px font-mono">{{ monitorData.thread.waitingThreads || 0 }}</span>
                </div>
              </div>

              <!-- 阻塞线程 -->
              <div class="flex-y-center justify-between">
                <div class="flex-y-center gap-8px">
                  <div class="h-8px w-8px rounded-full bg-red-500"></div>
                  <span class="text-12px text-gray-600">阻塞线程</span>
                </div>
                <div class="flex-y-center gap-8px">
                  <div class="w-60px flex-1">
                    <Progress
                      :percent="
                        Math.round(
                          ((monitorData.thread.blockedThreads || 0) / (monitorData.thread.liveThreads || 1)) * 100
                        )
                      "
                      stroke-color="#ff4d4f"
                      size="small"
                      :show-info="false"
                    />
                  </div>
                  <span class="min-w-24px text-12px font-mono">{{ monitorData.thread.blockedThreads || 0 }}</span>
                </div>
              </div>

              <!-- 定时等待线程 -->
              <div class="flex-y-center justify-between">
                <div class="flex-y-center gap-8px">
                  <div class="h-8px w-8px rounded-full bg-blue-500"></div>
                  <span class="text-12px text-gray-600">定时等待</span>
                </div>
                <div class="flex-y-center gap-8px">
                  <div class="w-60px flex-1">
                    <Progress
                      :percent="
                        Math.round(
                          ((monitorData.thread.timedWaitingThreads || 0) / (monitorData.thread.liveThreads || 1)) * 100
                        )
                      "
                      stroke-color="#1890ff"
                      size="small"
                      :show-info="false"
                    />
                  </div>
                  <span class="min-w-24px text-12px font-mono">{{ monitorData.thread.timedWaitingThreads || 0 }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 其他线程信息 -->
          <div class="border-t border-gray-100 pt-12px space-y-8px">
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">守护线程</span>
              <Tag color="blue">{{ monitorData.thread.daemonThreads || 0 }}</Tag>
            </div>
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">死锁线程</span>
              <Tag :color="(monitorData.thread.deadlockedThreads || 0) > 0 ? 'red' : 'green'">
                {{ monitorData.thread.deadlockedThreads || 0 }}
              </Tag>
            </div>
          </div>
        </div>
        <div v-else class="h-280px flex-center">
          <ASpin />
        </div>
      </Card>
    </div>

    <!-- 网络和进程监控 -->
    <div class="grid grid-cols-1 gap-16px lg:grid-cols-2">
      <!-- 网络监控 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:network" class="text-cyan-500" />
            <span>网络监控</span>
          </div>
        </template>
        <div v-if="monitorData.network" class="space-y-16px">
          <div class="grid grid-cols-2 gap-16px">
            <div class="text-center">
              <div class="text-16px text-blue-600 font-bold">
                {{ monitorData.network.totalBytesSentFormatted || '0B' }}
              </div>
              <div class="text-12px text-gray-500">总发送</div>
            </div>
            <div class="text-center">
              <div class="text-16px text-green-600 font-bold">
                {{ monitorData.network.totalBytesReceivedFormatted || '0B' }}
              </div>
              <div class="text-12px text-gray-500">总接收</div>
            </div>
          </div>
          <div class="space-y-8px">
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">网卡数量</span>
              <Tag color="blue">{{ monitorData.network.interfaceCount || 0 }}个</Tag>
            </div>
            <div
              v-for="(interface_, index) in monitorData.network.interfaces?.slice(0, 3) || []"
              :key="index"
              class="flex-y-center justify-between text-12px"
            >
              <span class="text-gray-600">{{ interface_.name }}</span>
              <div class="flex-y-center gap-4px">
                <Tag size="small" color="blue">发送: {{ interface_.bytesSentFormatted }}</Tag>
                <Tag size="small" color="green">接收: {{ interface_.bytesReceivedFormatted }}</Tag>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="h-200px flex-center">
          <ASpin />
        </div>
      </Card>

      <!-- 进程监控 -->
      <Card class="card-wrapper" :bordered="false">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:application-cog" class="text-pink-500" />
            <span>进程监控</span>
          </div>
        </template>
        <div v-if="processInfo" class="space-y-16px">
          <div class="grid grid-cols-2 gap-16px">
            <div class="text-center">
              <div class="text-16px text-blue-600 font-bold">{{ processInfo.currentProcess?.pid || 'N/A' }}</div>
              <div class="text-12px text-gray-500">当前进程PID</div>
            </div>
            <div class="text-center">
              <div class="text-16px text-green-600 font-bold">{{ processInfo.totalProcesses || 0 }}</div>
              <div class="text-12px text-gray-500">总进程数</div>
            </div>
          </div>
          <div class="space-y-8px">
            <div class="flex-y-center justify-between text-12px">
              <span class="text-gray-600">内存使用</span>
              <Tag color="orange">{{ (processInfo.currentProcess?.memoryPercent || 0).toFixed(2) }}%</Tag>
            </div>
            <div
              v-for="(process, index) in processInfo.topProcesses?.slice(0, 3) || []"
              :key="index"
              class="flex-y-center justify-between text-12px"
            >
              <span class="text-gray-600">{{ process.name }}</span>
              <div class="flex-y-center gap-4px">
                <Tag size="small" color="blue">CPU: {{ process.cpuPercent?.toFixed(1) }}%</Tag>
                <Tag size="small" color="green">内存: {{ process.memoryPercent?.toFixed(1) }}%</Tag>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="h-200px flex-center">
          <ASpin />
        </div>
      </Card>
    </div>

    <!-- 垃圾回收监控 -->
    <Card v-if="monitorData.gc" class="card-wrapper" :bordered="false">
      <template #title>
        <div class="flex-y-center gap-8px">
          <SvgIcon icon="mdi:delete-sweep" class="text-red-500" />
          <span>垃圾回收监控</span>
        </div>
      </template>
      <div class="grid grid-cols-1 gap-16px lg:grid-cols-4 md:grid-cols-2">
        <div
          v-for="gc in monitorData.gc"
          :key="gc.name"
          class="rounded-12px bg-gray-50 p-16px text-center transition-all duration-300 dark:bg-gray-800/50 hover:shadow-md"
        >
          <div class="mb-8px text-16px text-blue-600 font-bold">{{ gc.collectionCount || 0 }}</div>
          <div class="mb-4px text-12px text-gray-600">{{ gc.name }}</div>
          <div class="text-10px text-gray-400">{{ gc.collectionTime }}ms</div>
        </div>
      </div>
    </Card>

    <!-- 提示信息 -->
    <Alert
      message="监控说明"
      description="系统监控数据每5秒自动刷新，请关注CPU、内存使用率，及时发现系统异常。建议CPU使用率低于80%，内存使用率低于85%。"
      type="info"
      show-icon
      closable
    />
  </div>
</template>

<style lang="scss" scoped>
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
