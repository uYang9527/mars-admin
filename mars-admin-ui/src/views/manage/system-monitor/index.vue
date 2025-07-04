<template>
  <div class="monitor-dashboard">
    <!-- 顶部状态栏 -->
    <div class="top-status-bar">
      <a-row :gutter="[16, 16]">
        <a-col :xs="24" :sm="12" :lg="6">
          <div class="status-card health-card">
            <div class="card-icon">
              <component :is="getHealthIcon()" />
            </div>
            <div class="card-content">
              <h3>系统健康</h3>
              <div class="health-score">{{ overview.healthScore || 0 }}分</div>
              <div class="health-status" :class="getHealthClass()">
                {{ overview.healthStatus || '正常' }}
              </div>
            </div>
          </div>
        </a-col>
        <a-col :xs="24" :sm="12" :lg="6">
          <div class="status-card alert-card">
            <div class="card-icon">
              <BellOutlined />
            </div>
            <div class="card-content">
              <h3>系统告警</h3>
              <div class="alert-count">{{ alerts.alertCount || 0 }}个</div>
              <div class="alert-status" :class="alerts.hasAlerts ? 'has-alerts' : 'no-alerts'">
                {{ alerts.hasAlerts ? '有告警' : '无告警' }}
              </div>
            </div>
          </div>
        </a-col>
        <a-col :xs="24" :sm="12" :lg="6">
          <div class="status-card uptime-card">
            <div class="card-icon">
              <ClockCircleOutlined />
            </div>
            <div class="card-content">
              <h3>运行时间</h3>
              <div class="uptime">{{ monitorData.server?.uptime || '未知' }}</div>
              <div class="uptime-label">系统运行时间</div>
            </div>
          </div>
        </a-col>
        <a-col :xs="24" :sm="12" :lg="6">
          <div class="status-card load-card">
            <div class="card-icon">
              <FundOutlined />
            </div>
            <div class="card-content">
              <h3>系统负载</h3>
              <div class="load-value">{{ monitorData.systemLoad?.load1 || 0 }}</div>
              <div class="load-status" :class="getLoadClass()">
                {{ monitorData.systemLoad?.loadLevel || '正常' }}负载
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>

    <!-- 告警信息 -->
    <div v-if="alerts.hasAlerts" class="alert-section">
      <a-alert
        :message="`检测到 ${alerts.alertCount} 个系统告警`"
        type="warning"
        show-icon
        closable
      >
        <template #description>
          <div class="alert-list">
            <div v-for="alert in alerts.alerts" :key="alert.timestamp" class="alert-item">
              <a-tag :color="alert.level === '严重' ? 'red' : 'orange'">
                {{ alert.type }}
              </a-tag>
              {{ alert.message }}
            </div>
          </div>
        </template>
      </a-alert>
    </div>

    <!-- 主监控面板 -->
    <div class="main-dashboard">
      <a-row :gutter="[24, 24]">
        <!-- CPU监控 -->
        <a-col :xl="8" :lg="12" :md="24">
          <div class="monitor-card cpu-card">
            <div class="card-header">
              <h3><DatabaseOutlined /> CPU监控</h3>
              <div class="refresh-btn" @click="refreshData">
                <ReloadOutlined :spin="loading" />
              </div>
            </div>
            <div class="card-body">
              <div class="progress-circle">
                <a-progress
                  type="circle"
                  :percent="Math.round(monitorData.cpu?.usagePercent || 0)"
                  :stroke-color="getCpuColor()"
                  :width="120"
                  :stroke-width="8"
                >
                  <template #format="percent">
                    <div class="progress-content">
                      <div class="percentage">{{ percent }}%</div>
                      <div class="label">CPU</div>
                    </div>
                  </template>
                </a-progress>
              </div>
              <div class="cpu-details">
                <div class="detail-row">
                  <span>核心数:</span>
                  <span>{{ monitorData.cpu?.cpuNum || 0 }}个</span>
                </div>
                <div class="detail-row">
                  <span>物理核心:</span>
                  <span>{{ monitorData.cpu?.physicalCores || 0 }}个</span>
                </div>
                <div class="detail-row">
                  <span>频率:</span>
                  <span>{{ monitorData.cpu?.frequency || 0 }}MHz</span>
                </div>
                <div class="detail-row">
                  <span>用户:</span>
                  <span>{{ (monitorData.cpu?.userPercent || 0).toFixed(1) }}%</span>
                </div>
                <div class="detail-row">
                  <span>系统:</span>
                  <span>{{ (monitorData.cpu?.systemPercent || 0).toFixed(1) }}%</span>
                </div>
                <div class="detail-row">
                  <span>IO等待:</span>
                  <span>{{ (monitorData.cpu?.iowaitPercent || 0).toFixed(1) }}%</span>
                </div>
              </div>
            </div>
          </div>
        </a-col>

        <!-- 内存监控 -->
        <a-col :xl="8" :lg="12" :md="24">
          <div class="monitor-card memory-card">
            <div class="card-header">
              <h3><AppstoreOutlined /> 内存监控</h3>
            </div>
            <div class="card-body">
              <div class="progress-circle">
                <a-progress
                  type="circle"
                  :percent="Math.round(monitorData.memory?.usage || 0)"
                  :stroke-color="getMemoryColor()"
                  :width="120"
                  :stroke-width="8"
                >
                  <template #format="percent">
                    <div class="progress-content">
                      <div class="percentage">{{ percent }}%</div>
                      <div class="label">内存</div>
                    </div>
                  </template>
                </a-progress>
              </div>
              <div class="memory-details">
                <div class="detail-row">
                  <span>总内存:</span>
                  <span>{{ monitorData.memory?.totalFormatted || '0B' }}</span>
                </div>
                <div class="detail-row">
                  <span>已使用:</span>
                  <span>{{ monitorData.memory?.usedFormatted || '0B' }}</span>
                </div>
                <div class="detail-row">
                  <span>可用:</span>
                  <span>{{ monitorData.memory?.availableFormatted || '0B' }}</span>
                </div>
                <div v-if="monitorData.memory?.swapTotal" class="detail-row swap-info">
                  <span>交换分区:</span>
                  <span>{{ (monitorData.memory?.swapUsage || 0).toFixed(1) }}%</span>
                </div>
                <div v-if="monitorData.memory?.swapTotal" class="detail-row">
                  <span>交换使用:</span>
                  <span>{{ monitorData.memory?.swapUsedFormatted || '0B' }}</span>
                </div>
              </div>
            </div>
          </div>
        </a-col>

        <!-- JVM监控 -->
        <a-col :xl="8" :lg="12" :md="24">
          <div class="monitor-card jvm-card">
            <div class="card-header">
              <h3><CoffeeOutlined /> JVM监控</h3>
            </div>
            <div class="card-body">
              <div class="progress-circle">
                <a-progress
                  type="circle"
                  :percent="Math.round(monitorData.jvm?.usage || 0)"
                  :stroke-color="getJvmColor()"
                  :width="120"
                  :stroke-width="8"
                >
                  <template #format="percent">
                    <div class="progress-content">
                      <div class="percentage">{{ percent }}%</div>
                      <div class="label">JVM</div>
                    </div>
                  </template>
                </a-progress>
              </div>
              <div class="jvm-details">
                <div class="detail-row">
                  <span>堆内存:</span>
                  <span>{{ monitorData.jvm?.heapFormatted || '0B' }}</span>
                </div>
                <div class="detail-row">
                  <span>非堆内存:</span>
                  <span>{{ monitorData.jvm?.nonHeapFormatted || '0B' }}</span>
                </div>
                <div class="detail-row">
                  <span>Java版本:</span>
                  <span>{{ monitorData.jvm?.version || '未知' }}</span>
                </div>
                <div class="detail-row">
                  <span>运行时间:</span>
                  <span>{{ monitorData.jvm?.uptimeFormatted || '未知' }}</span>
                </div>
                <div class="detail-row">
                  <span>厂商:</span>
                  <span>{{ monitorData.jvm?.vendor || '未知' }}</span>
                </div>
              </div>
            </div>
          </div>
        </a-col>
      </a-row>
    </div>

    <!-- 详细信息面板 -->
    <div class="detail-panels">
      <a-row :gutter="[24, 24]">
        <!-- 磁盘信息 -->
        <a-col :xl="12" :lg="24">
          <div class="monitor-card disk-card">
            <div class="card-header">
              <h3><HddOutlined /> 磁盘监控</h3>
            </div>
            <div class="card-body">
              <a-table
                :columns="diskColumns"
                :data-source="monitorData.disk || []"
                :pagination="false"
                size="small"
                :scroll="{ y: 200 }"
              >
                <template #bodyCell="{ column, record }">
                  <template v-if="column.key === 'usage'">
                    <div class="usage-bar">
                      <a-progress
                        :percent="Math.round(record.usage)"
                        :stroke-color="getDiskColor(record.usage)"
                        size="small"
                        :show-info="false"
                      />
                      <span class="usage-text">{{ record.usage.toFixed(1) }}%</span>
                    </div>
                  </template>
                </template>
              </a-table>
            </div>
          </div>
        </a-col>

        <!-- 网络信息 -->
        <a-col :xl="12" :lg="24">
          <div class="monitor-card network-card">
            <div class="card-header">
              <h3><WifiOutlined /> 网络监控</h3>
            </div>
            <div class="card-body">
              <div class="network-summary">
                <div class="summary-item">
                  <div class="item-label">总发送</div>
                  <div class="item-value">{{ monitorData.network?.totalBytesSentFormatted || '0B' }}</div>
                </div>
                <div class="summary-item">
                  <div class="item-label">总接收</div>
                  <div class="item-value">{{ monitorData.network?.totalBytesReceivedFormatted || '0B' }}</div>
                </div>
                <div class="summary-item">
                  <div class="item-label">网卡数量</div>
                  <div class="item-value">{{ monitorData.network?.interfaceCount || 0 }}个</div>
                </div>
              </div>
              <a-table
                :columns="networkColumns"
                :data-source="monitorData.network?.interfaces?.slice(0, 5) || []"
                :pagination="false"
                size="small"
                :scroll="{ y: 150 }"
              />
            </div>
          </div>
        </a-col>

        <!-- 线程信息 -->
        <a-col :xl="12" :lg="24">
          <div class="monitor-card thread-card">
            <div class="card-header">
              <h3><NodeIndexOutlined /> 线程监控</h3>
            </div>
            <div class="card-body">
              <div class="thread-summary">
                <div class="summary-grid">
                  <div class="grid-item">
                    <div class="item-value">{{ monitorData.thread?.liveThreads || 0 }}</div>
                    <div class="item-label">活动线程</div>
                  </div>
                  <div class="grid-item">
                    <div class="item-value">{{ monitorData.thread?.daemonThreads || 0 }}</div>
                    <div class="item-label">守护线程</div>
                  </div>
                  <div class="grid-item">
                    <div class="item-value">{{ monitorData.thread?.peakThreads || 0 }}</div>
                    <div class="item-label">峰值线程</div>
                  </div>
                  <div class="grid-item" :class="{ 'danger': (monitorData.thread?.deadlockedThreads || 0) > 0 }">
                    <div class="item-value">{{ monitorData.thread?.deadlockedThreads || 0 }}</div>
                    <div class="item-label">死锁线程</div>
                  </div>
                </div>
                <div class="thread-states">
                  <div class="state-item">
                    <span class="state-dot running"></span>
                    <span>运行: {{ monitorData.thread?.runnableThreads || 0 }}</span>
                  </div>
                  <div class="state-item">
                    <span class="state-dot waiting"></span>
                    <span>等待: {{ monitorData.thread?.waitingThreads || 0 }}</span>
                  </div>
                  <div class="state-item">
                    <span class="state-dot blocked"></span>
                    <span>阻塞: {{ monitorData.thread?.blockedThreads || 0 }}</span>
                  </div>
                  <div class="state-item">
                    <span class="state-dot timed-waiting"></span>
                    <span>定时等待: {{ monitorData.thread?.timedWaitingThreads || 0 }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </a-col>

        <!-- 进程信息 -->
        <a-col :xl="12" :lg="24">
          <div class="monitor-card process-card">
            <div class="card-header">
              <h3><AppstoreAddOutlined /> 进程监控</h3>
            </div>
            <div class="card-body">
              <div class="process-summary">
                <div class="summary-item">
                  <div class="item-label">当前进程PID</div>
                  <div class="item-value">{{ processInfo.currentProcess?.pid || 'N/A' }}</div>
                </div>
                <div class="summary-item">
                  <div class="item-label">总进程数</div>
                  <div class="item-value">{{ processInfo.totalProcesses || 0 }}</div>
                </div>
                <div class="summary-item">
                  <div class="item-label">内存使用</div>
                  <div class="item-value">{{ (processInfo.currentProcess?.memoryPercent || 0).toFixed(2) }}%</div>
                </div>
              </div>
              <a-table
                :columns="processColumns"
                :data-source="processInfo.topProcesses?.slice(0, 5) || []"
                :pagination="false"
                size="small"
                :scroll="{ y: 150 }"
              />
            </div>
          </div>
        </a-col>
      </a-row>
    </div>

    <!-- 垃圾回收信息 -->
    <div class="gc-section">
      <div class="monitor-card gc-card">
        <div class="card-header">
          <h3><DeleteOutlined /> 垃圾回收监控</h3>
        </div>
        <div class="card-body">
          <div class="gc-summary">
            <div class="summary-item">
              <div class="item-label">总收集次数</div>
              <div class="item-value">{{ gcInfo.totalCollectionCount || 0 }}</div>
            </div>
            <div class="summary-item">
              <div class="item-label">总收集时间</div>
              <div class="item-value">{{ gcInfo.totalCollectionTime || 0 }}ms</div>
            </div>
            <div class="summary-item">
              <div class="item-label">平均收集时间</div>
              <div class="item-value">{{ (gcInfo.averageCollectionTime || 0).toFixed(2) }}ms</div>
            </div>
          </div>
          <a-row :gutter="[16, 16]">
            <a-col :span="12">
              <h4>垃圾收集器</h4>
              <a-table
                :columns="gcColumns"
                :data-source="gcInfo.collectors || []"
                :pagination="false"
                size="small"
                :scroll="{ y: 200 }"
              />
            </a-col>
            <a-col :span="12">
              <h4>内存池</h4>
              <a-table
                :columns="poolColumns"
                :data-source="gcInfo.memoryPools || []"
                :pagination="false"
                size="small"
                :scroll="{ y: 200 }"
              />
            </a-col>
          </a-row>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import {
  ReloadOutlined,
  DatabaseOutlined,
  AppstoreOutlined,
  CoffeeOutlined,
  HddOutlined,
  WifiOutlined,
  NodeIndexOutlined,
  AppstoreAddOutlined,
  DeleteOutlined,
  BellOutlined,
  ClockCircleOutlined,
  FundOutlined,
  CheckCircleOutlined,
  WarningOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { getMonitorInfo, getSystemAlerts, getPerformanceOverview, getProcessInfo, getGcInfo } from '@/service/api/system'

// 响应式数据
const loading = ref(false)
const monitorData = ref<any>({})
const alerts = ref<any>({})
const overview = ref<any>({})
const processInfo = ref<any>({})
const gcInfo = ref<any>({})

// 自动刷新
let refreshTimer: NodeJS.Timeout | null = null

// 表格列定义
const diskColumns = [
  {
    title: '挂载点',
    dataIndex: 'dirName',
    key: 'dirName',
    width: 120,
  },
  {
    title: '文件系统',
    dataIndex: 'fsType',
    key: 'fsType',
    width: 100,
  },
  {
    title: '总大小',
    dataIndex: 'totalFormatted',
    key: 'totalFormatted',
    width: 80,
  },
  {
    title: '已使用',
    dataIndex: 'usedFormatted',
    key: 'usedFormatted',
    width: 80,
  },
  {
    title: '使用率',
    key: 'usage',
    width: 120,
  },
]

const networkColumns = [
  {
    title: '网卡名称',
    dataIndex: 'name',
    key: 'name',
    width: 120,
  },
  {
    title: '发送',
    dataIndex: 'bytesSentFormatted',
    key: 'bytesSentFormatted',
    width: 100,
  },
  {
    title: '接收',
    dataIndex: 'bytesReceivedFormatted',
    key: 'bytesReceivedFormatted',
    width: 100,
  },
]

const processColumns = [
  {
    title: 'PID',
    dataIndex: 'pid',
    key: 'pid',
    width: 80,
  },
  {
    title: '进程名',
    dataIndex: 'name',
    key: 'name',
    width: 120,
  },
  {
    title: 'CPU%',
    dataIndex: 'cpuPercent',
    key: 'cpuPercent',
    width: 80,
    customRender: ({ text }: any) => `${text.toFixed(2)}%`,
  },
  {
    title: '内存',
    dataIndex: 'residentSetSizeFormatted',
    key: 'residentSetSizeFormatted',
    width: 80,
  },
]

const gcColumns = [
  {
    title: '收集器',
    dataIndex: 'name',
    key: 'name',
    width: 150,
  },
  {
    title: '类型',
    dataIndex: 'type',
    key: 'type',
    width: 80,
  },
  {
    title: '收集次数',
    dataIndex: 'collectionCount',
    key: 'collectionCount',
    width: 100,
  },
  {
    title: '收集时间(ms)',
    dataIndex: 'collectionTime',
    key: 'collectionTime',
    width: 120,
  },
]

const poolColumns = [
  {
    title: '内存池',
    dataIndex: 'name',
    key: 'name',
    width: 150,
  },
  {
    title: '类型',
    dataIndex: 'type',
    key: 'type',
    width: 80,
  },
  {
    title: '使用率',
    dataIndex: 'usage',
    key: 'usage',
    width: 80,
    customRender: ({ text }: any) => `${text.toFixed(1)}%`,
  },
  {
    title: '已使用',
    dataIndex: 'usedFormatted',
    key: 'usedFormatted',
    width: 100,
  },
]

// 颜色函数
const getCpuColor = () => {
  const usage = monitorData.value.cpu?.usagePercent || 0
  if (usage >= 90) return '#ff4d4f'
  if (usage >= 80) return '#ff7a45'
  if (usage >= 70) return '#ffa940'
  if (usage >= 50) return '#52c41a'
  return '#1890ff'
}

const getMemoryColor = () => {
  const usage = monitorData.value.memory?.usage || 0
  if (usage >= 95) return '#ff4d4f'
  if (usage >= 85) return '#ff7a45'
  if (usage >= 75) return '#ffa940'
  if (usage >= 50) return '#52c41a'
  return '#1890ff'
}

const getJvmColor = () => {
  const usage = monitorData.value.jvm?.usage || 0
  if (usage >= 95) return '#ff4d4f'
  if (usage >= 85) return '#ff7a45'
  if (usage >= 75) return '#ffa940'
  if (usage >= 50) return '#52c41a'
  return '#1890ff'
}

const getDiskColor = (usage: number) => {
  if (usage >= 95) return '#ff4d4f'
  if (usage >= 90) return '#ff7a45'
  if (usage >= 80) return '#ffa940'
  if (usage >= 60) return '#52c41a'
  return '#1890ff'
}

const getHealthIcon = () => {
  const score = overview.value.healthScore || 0
  if (score >= 90) return CheckCircleOutlined
  if (score >= 60) return WarningOutlined
  return ExclamationCircleOutlined
}

const getHealthClass = () => {
  const score = overview.value.healthScore || 0
  if (score >= 90) return 'excellent'
  if (score >= 80) return 'good'
  if (score >= 60) return 'normal'
  if (score >= 40) return 'poor'
  return 'danger'
}

const getLoadClass = () => {
  const level = monitorData.value.systemLoad?.loadLevel || '低'
  return level === '高' ? 'high-load' : level === '中' ? 'medium-load' : 'low-load'
}

// 方法
const refreshData = async () => {
  loading.value = true
  try {
    const [monitorRes, alertsRes, overviewRes, processRes, gcRes] = await Promise.all([
      getMonitorInfo(),
      getSystemAlerts(),
      getPerformanceOverview(),
      getProcessInfo(),
      getGcInfo(),
    ])

    monitorData.value = monitorRes.data
    alerts.value = alertsRes.data
    overview.value = overviewRes.data
    processInfo.value = processRes.data
    gcInfo.value = gcRes.data
  } catch (error) {
    console.error('获取监控数据失败:', error)
    message.error('获取监控数据失败')
  } finally {
    loading.value = false
  }
}

const startAutoRefresh = () => {
  if (refreshTimer) clearInterval(refreshTimer)
  refreshTimer = setInterval(refreshData, 5000)
}

const stopAutoRefresh = () => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
    refreshTimer = null
  }
}

// 生命周期
onMounted(() => {
  refreshData()
  startAutoRefresh()
})

onUnmounted(() => {
  stopAutoRefresh()
})
</script>

<style lang="scss" scoped>
.monitor-dashboard {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;

  // 顶部状态栏
  .top-status-bar {
    margin-bottom: 24px;

    .status-card {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 12px;
      padding: 20px;
      color: white;
      display: flex;
      align-items: center;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-2px);
      }

      .card-icon {
        font-size: 36px;
        margin-right: 16px;
        opacity: 0.8;
      }

      .card-content {
        flex: 1;

        h3 {
          margin: 0 0 8px 0;
          font-size: 14px;
          opacity: 0.9;
          font-weight: 500;
        }

        .health-score, .alert-count, .uptime, .load-value {
          font-size: 24px;
          font-weight: bold;
          margin-bottom: 4px;
        }

        .health-status, .alert-status, .uptime-label, .load-status {
          font-size: 12px;
          opacity: 0.8;

          &.excellent { color: #52c41a; }
          &.good { color: #1890ff; }
          &.normal { color: #faad14; }
          &.poor { color: #ff7a45; }
          &.danger { color: #ff4d4f; }

          &.has-alerts { color: #ff7875; }
          &.no-alerts { color: #95de64; }

          &.high-load { color: #ff7875; }
          &.medium-load { color: #ffc069; }
          &.low-load { color: #95de64; }
        }
      }

      &.health-card {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.alert-card {
        background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
      }

      &.uptime-card {
        background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
      }

      &.load-card {
        background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
      }
    }
  }

  // 告警区域
  .alert-section {
    margin-bottom: 24px;

    .alert-list {
      .alert-item {
        margin-bottom: 8px;
        display: flex;
        align-items: center;
        gap: 8px;
      }
    }
  }

  // 监控卡片
  .monitor-card {
    background: white;
    border-radius: 12px;
    box-shadow: 0 2px 16px rgba(0, 0, 0, 0.06);
    overflow: hidden;
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 4px 24px rgba(0, 0, 0, 0.12);
      transform: translateY(-2px);
    }

    .card-header {
      padding: 16px 20px;
      background: linear-gradient(90deg, #f8f9fa, #ffffff);
      border-bottom: 1px solid #f0f0f0;
      display: flex;
      justify-content: space-between;
      align-items: center;

      h3 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
        color: #333;
        display: flex;
        align-items: center;
        gap: 8px;

        .anticon {
          color: #1890ff;
        }
      }

      .refresh-btn {
        cursor: pointer;
        color: #666;
        transition: color 0.3s;

        &:hover {
          color: #1890ff;
        }
      }
    }

    .card-body {
      padding: 20px;
    }
  }

  // 圆形进度条
  .progress-circle {
    text-align: center;
    margin-bottom: 20px;

    .progress-content {
      .percentage {
        font-size: 20px;
        font-weight: bold;
        color: #333;
      }

      .label {
        font-size: 12px;
        color: #666;
        margin-top: 4px;
      }
    }
  }

  // 详情列表
  .cpu-details, .memory-details, .jvm-details {
    .detail-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 0;
      border-bottom: 1px solid #f5f5f5;

      &:last-child {
        border-bottom: none;
      }

      span:first-child {
        color: #666;
        font-size: 13px;
      }

      span:last-child {
        font-weight: 500;
        color: #333;
        font-size: 13px;
      }

      &.swap-info {
        margin-top: 8px;
        padding-top: 12px;
        border-top: 1px solid #e8e8e8;
      }
    }
  }

  // 使用率条
  .usage-bar {
    display: flex;
    align-items: center;
    gap: 8px;

    .ant-progress {
      flex: 1;
    }

    .usage-text {
      font-size: 12px;
      font-weight: 500;
      min-width: 45px;
      text-align: right;
    }
  }

  // 网络摘要
  .network-summary {
    display: flex;
    justify-content: space-around;
    margin-bottom: 16px;
    padding: 16px 0;
    background: #fafafa;
    border-radius: 8px;

    .summary-item {
      text-align: center;

      .item-label {
        font-size: 12px;
        color: #666;
        margin-bottom: 4px;
      }

      .item-value {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }
  }

  // 线程摘要
  .thread-summary {
    .summary-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px;
      margin-bottom: 20px;

      .grid-item {
        text-align: center;
        padding: 12px;
        background: #fafafa;
        border-radius: 8px;

        &.danger {
          background: #fff2f0;
          color: #ff4d4f;
        }

        .item-value {
          font-size: 20px;
          font-weight: bold;
          margin-bottom: 4px;
        }

        .item-label {
          font-size: 12px;
          color: #666;
        }
      }
    }

    .thread-states {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;

      .state-item {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 13px;

        .state-dot {
          width: 8px;
          height: 8px;
          border-radius: 50%;

          &.running { background: #52c41a; }
          &.waiting { background: #1890ff; }
          &.blocked { background: #ff4d4f; }
          &.timed-waiting { background: #faad14; }
        }
      }
    }
  }

  // 进程摘要
  .process-summary {
    display: flex;
    justify-content: space-around;
    margin-bottom: 16px;
    padding: 16px 0;
    background: #fafafa;
    border-radius: 8px;

    .summary-item {
      text-align: center;

      .item-label {
        font-size: 12px;
        color: #666;
        margin-bottom: 4px;
      }

      .item-value {
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }
  }

  // GC区域
  .gc-section {
    margin-top: 24px;

    .gc-summary {
      display: flex;
      justify-content: space-around;
      margin-bottom: 20px;
      padding: 20px 0;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
      border-radius: 8px;

      .summary-item {
        text-align: center;

        .item-label {
          font-size: 13px;
          color: #666;
          margin-bottom: 6px;
        }

        .item-value {
          font-size: 18px;
          font-weight: bold;
          color: #333;
        }
      }
    }

    h4 {
      margin-bottom: 12px;
      color: #333;
      font-weight: 600;
    }
  }

  // 表格样式优化
  :deep(.ant-table) {
    .ant-table-thead > tr > th {
      background: #fafafa;
      font-weight: 600;
      font-size: 12px;
    }

    .ant-table-tbody > tr > td {
      font-size: 12px;
    }

    .ant-table-tbody > tr:hover > td {
      background: #f5f7fa;
    }
  }

  // 响应式设计
  @media (max-width: 768px) {
    padding: 16px;

    .top-status-bar {
      .status-card {
        padding: 16px;

        .card-icon {
          font-size: 28px;
        }

        .card-content {
          h3 {
            font-size: 13px;
          }

          .health-score, .alert-count, .uptime, .load-value {
            font-size: 20px;
          }
        }
      }
    }

    .monitor-card {
      .card-body {
        padding: 16px;
      }
    }

    .thread-summary .summary-grid {
      grid-template-columns: repeat(2, 1fr);
    }
  }
}
</style> 