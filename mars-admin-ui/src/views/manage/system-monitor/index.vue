<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref, h } from 'vue';
import { Card, Table, Progress, Tag, Descriptions, Statistic, Row, Col, Spin, Alert, Space, Button } from 'ant-design-vue';
import type { DescriptionsItem } from 'ant-design-vue';
import { useEcharts } from '@/hooks/common/echarts';
import { fetchSystemMonitorInfo } from '@/service/api/system';
import SvgIcon from '@/components/custom/svg-icon.vue';

defineOptions({
  name: 'SystemMonitor'
});

const loading = ref(false);
const monitorData = ref<Api.SystemManage.MonitorInfo | null>(null);
const autoRefresh = ref(true);
const refreshInterval = ref(5000);
let timer: ReturnType<typeof setInterval> | null = null;

const { domRef: cpuChartRef, setOptions: setCpuChartOptions } = useEcharts();
const { domRef: memoryChartRef, setOptions: setMemoryChartOptions } = useEcharts();
const { domRef: jvmChartRef, setOptions: setJvmChartOptions } = useEcharts();
const { domRef: networkChartRef, setOptions: setNetworkChartOptions } = useEcharts();
const { domRef: threadChartRef, setOptions: setThreadChartOptions } = useEcharts();
const { domRef: systemLoadChartRef, setOptions: setSystemLoadChartOptions } = useEcharts();

// 服务器信息
const serverInfoItems = computed<DescriptionsItem[]>(() => {
  const server = monitorData.value?.server;
  if (!server) return [];
  return [
    { key: 'computerName', label: '服务器名称', children: server.computerName, span: 1 },
    { key: 'computerIp', label: '服务器IP', children: server.computerIp, span: 1 },
    { key: 'osName', label: '操作系统', children: server.osName, span: 2 },
    { key: 'osVersion', label: '系统版本', children: server.osVersion, span: 1 },
    { key: 'osArch', label: '系统架构', children: server.osArch, span: 1 },
    { key: 'javaVersion', label: 'Java版本', children: server.javaVersion, span: 2 },
    { key: 'processId', label: '进程ID', children: server.processId, span: 1 },
    { key: 'availableProcessors', label: '可用处理器', children: `${server.availableProcessors} 个`, span: 1 },
    { key: 'uptime', label: '运行时长', children: server.uptime, span: 1 },
    { key: 'timezone', label: '时区', children: server.timezone, span: 1 }
  ];
});

// JVM信息
const jvmInfoItems = computed<DescriptionsItem[]>(() => {
  const jvm = monitorData.value?.jvm;
  if (!jvm) return [];
  return [
    { key: 'name', label: 'JVM名称', children: jvm.name, span: 3 },
    { key: 'version', label: 'JVM版本', children: jvm.version, span: 1 },
    { key: 'home', label: 'Java路径', children: jvm.home, span: 2 },
    { key: 'startTime', label: '启动时间', children: jvm.startTime, span: 1 },
    { key: 'runTime', label: '运行时长', children: jvm.runTime, span: 1 },
    { key: 'heapFormatted', label: '堆内存', children: jvm.heapFormatted, span: 1 },
    { key: 'nonHeapFormatted', label: '非堆内存', children: jvm.nonHeapFormatted, span: 1 },
    { 
      key: 'classLoader', 
      label: '类加载信息', 
      children: jvm.classLoader ? `已加载: ${jvm.classLoader.loadedClassCount}, 总计: ${jvm.classLoader.totalLoadedClassCount}` : '未知',
      span: 2
    }
  ];
});

// 磁盘列表配置
const diskColumns = [
  { title: '盘符路径', dataIndex: 'dirName', key: 'dirName', width: 120 },
  { title: '文件系统', dataIndex: 'sysTypeName', key: 'sysTypeName', width: 100 },
  { title: '总大小', dataIndex: 'total', key: 'total', width: 100 },
  { title: '已用', dataIndex: 'used', key: 'used', width: 100 },
  { title: '可用', dataIndex: 'free', key: 'free', width: 100 },
  {
    title: '使用率',
    dataIndex: 'usage',
    key: 'usage',
    width: 150,
    customRender: ({ text }: { text: number }) => {
      return h(Progress, {
        percent: Number(text.toFixed(1)),
        strokeColor: getProgressColor(text),
        size: 'small',
        showInfo: true,
        format: (percent: number) => `${percent}%`
      });
    }
  }
];

// 网络接口列表配置
const networkColumns = [
  { title: '接口名称', dataIndex: 'name', key: 'name', width: 120 },
  { title: '显示名称', dataIndex: 'displayName', key: 'displayName', width: 150 },
  { title: 'IP地址', dataIndex: 'ipAddresses', key: 'ipAddresses', width: 120,
    customRender: ({ text }: { text: string[] }) => text?.join(', ') || 'N/A'
  },
  { title: '状态', dataIndex: 'enabled', key: 'enabled', width: 80,
    customRender: ({ text }: { text: boolean }) => h(Tag, { color: text ? 'green' : 'red' }, () => text ? '启用' : '禁用')
  },
  { title: '发送(MB)', dataIndex: 'bytesSent', key: 'bytesSent', width: 100,
    customRender: ({ text }: { text: number }) => (text / (1024 * 1024)).toFixed(2)
  },
  { title: '接收(MB)', dataIndex: 'bytesReceived', key: 'bytesReceived', width: 100,
    customRender: ({ text }: { text: number }) => (text / (1024 * 1024)).toFixed(2)
  }
];

// 垃圾回收信息列表配置
const gcColumns = [
  { title: '收集器名称', dataIndex: 'name', key: 'name', width: 150 },
  { title: '类型', dataIndex: 'type', key: 'type', width: 80,
    customRender: ({ text }: { text: string }) => h(Tag, { color: text === '新生代' ? 'blue' : text === '老年代' ? 'orange' : 'gray' }, () => text)
  },
  { title: '收集次数', dataIndex: 'collectionCount', key: 'collectionCount', width: 100 },
  { title: '总耗时(ms)', dataIndex: 'collectionTime', key: 'collectionTime', width: 100 },
  { title: '平均耗时(ms)', dataIndex: 'averageCollectionTime', key: 'averageCollectionTime', width: 120,
    customRender: ({ text }: { text: number }) => text.toFixed(2)
  }
];

// 创建高级仪表盘配置
function createAdvancedGaugeOption(title: string, value: number, max: number = 100) {
  const color = getGaugeColor(value);
  return {
    series: [
      {
        type: 'gauge',
        startAngle: 90,
        endAngle: -270,
        min: 0,
        max: max,
        splitNumber: 5,
        radius: '85%',
        center: ['50%', '55%'],
        pointer: {
          show: true,
          length: '70%',
          width: 4,
          itemStyle: {
            color: color.primary,
            shadowBlur: 5,
            shadowColor: 'rgba(0,0,0,0.3)'
          }
        },
        progress: {
          show: true,
          overlap: false,
          roundCap: true,
          clip: false,
          itemStyle: {
            color: {
              type: 'linear',
              x: 0, y: 0, x2: 1, y2: 0,
              colorStops: [
                { offset: 0, color: color.start },
                { offset: 1, color: color.end }
              ]
            },
            shadowBlur: 10,
            shadowColor: 'rgba(0,0,0,0.2)'
          }
        },
        axisLine: {
          lineStyle: {
            width: 15,
            color: [[1, 'rgba(0,0,0,0.1)']]
          }
        },
        splitLine: {
          show: true,
          length: 8,
          lineStyle: {
            width: 2,
            color: 'rgba(0,0,0,0.3)'
          }
        },
        axisTick: {
          show: true,
          length: 5,
          lineStyle: {
            width: 1,
            color: 'rgba(0,0,0,0.2)'
          }
        },
        axisLabel: {
          show: true,
          distance: 25,
          fontSize: 11,
          color: '#666',
          formatter: '{value}%'
        },
        data: [
          {
            value: value,
            name: title,
            title: {
              offsetCenter: [0, '95%'],
              fontSize: 14,
              fontWeight: 'bold',
              color: '#333'
            },
            detail: {
              valueAnimation: true,
              fontSize: 28,
              fontWeight: 'bold',
              color: color.primary,
              offsetCenter: [0, '40%'],
              formatter: '{value}%'
            }
          }
        ]
      }
    ]
  };
}

// 创建网络流量图表
function createNetworkChart(network: any) {
  return {
    title: {
      text: '网络流量',
      left: 'center',
      textStyle: {
        fontSize: 14,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['发送', '接收'],
      bottom: 10
    },
    xAxis: {
      type: 'category',
      data: ['实时']
    },
    yAxis: {
      type: 'value',
      name: 'MB/s',
      nameTextStyle: {
        fontSize: 12
      }
    },
    series: [
      {
        name: '发送',
        type: 'bar',
        data: [network.sendRate || 0],
        itemStyle: {
          color: '#3498db'
        }
      },
      {
        name: '接收',
        type: 'bar',
        data: [network.receiveRate || 0],
        itemStyle: {
          color: '#2ecc71'
        }
      }
    ]
  };
}

// 创建线程状态饼图
function createThreadPieChart(thread: any) {
  return {
    title: {
      text: '线程状态分布',
      left: 'center',
      textStyle: {
        fontSize: 14,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '线程状态',
        type: 'pie',
        radius: '50%',
        data: [
          { value: thread.runnableCount, name: '运行中' },
          { value: thread.blockedCount, name: '阻塞' },
          { value: thread.waitingCount, name: '等待' },
          { value: thread.timedWaitingCount, name: '定时等待' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };
}

// 创建系统负载图表
function createSystemLoadChart(systemLoad: any) {
  return {
    title: {
      text: '系统负载',
      left: 'center',
      textStyle: {
        fontSize: 14,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['1分钟', '5分钟', '15分钟'],
      bottom: 10
    },
    xAxis: {
      type: 'category',
      data: ['当前']
    },
    yAxis: {
      type: 'value',
      name: '负载值'
    },
    series: [
      {
        name: '1分钟',
        type: 'line',
        data: [systemLoad.load1 || 0],
        smooth: true
      },
      {
        name: '5分钟',
        type: 'line',
        data: [systemLoad.load5 || 0],
        smooth: true
      },
      {
        name: '15分钟',
        type: 'line',
        data: [systemLoad.load15 || 0],
        smooth: true
      }
    ]
  };
}

// 工具函数
function getProgressColor(value: number): string {
  if (value < 60) return '#52c41a';
  if (value < 80) return '#faad14';
  return '#ff4d4f';
}

function getGaugeColor(value: number) {
  if (value < 60) {
    return {
      primary: '#52c41a',
      start: '#95de64',
      end: '#52c41a'
    };
  }
  if (value < 80) {
    return {
      primary: '#faad14',
      start: '#ffd666',
      end: '#faad14'
    };
  }
  return {
    primary: '#ff4d4f',
    start: '#ff7875',
    end: '#ff4d4f'
  };
}

// 获取监控数据
async function getMonitorData() {
  try {
    loading.value = true;
    const { data, error } = await fetchSystemMonitorInfo();
    if (!error && data) {
      monitorData.value = data;
      updateCharts(data);
    } else {
      window.$message?.error('获取监控数据失败');
    }
  } catch (error) {
    console.error('获取监控数据失败:', error);
    window.$message?.error('获取监控数据失败');
  } finally {
    loading.value = false;
  }
}

// 更新图表
function updateCharts(data: Api.SystemManage.MonitorInfo) {
  // CPU使用率
  setCpuChartOptions(createAdvancedGaugeOption('CPU', data.cpu.usagePercent));
  
  // 内存使用率
  setMemoryChartOptions(createAdvancedGaugeOption('内存', data.memory.usage));
  
  // JVM堆内存使用率
  setJvmChartOptions(createAdvancedGaugeOption('JVM堆', data.jvm.usage));
  
  // 网络流量图表
  if (data.network) {
    setNetworkChartOptions(createNetworkChart(data.network));
  }
  
  // 线程状态图表
  if (data.thread) {
    setThreadChartOptions(createThreadPieChart(data.thread));
  }
  
  // 系统负载图表
  if (data.systemLoad) {
    setSystemLoadChartOptions(createSystemLoadChart(data.systemLoad));
  }
}

// 自动刷新控制
function startAutoRefresh() {
  if (timer) clearInterval(timer);
  if (autoRefresh.value) {
    timer = setInterval(getMonitorData, refreshInterval.value);
  }
}

function stopAutoRefresh() {
  if (timer) {
    clearInterval(timer);
    timer = null;
  }
}

function toggleAutoRefresh() {
  autoRefresh.value = !autoRefresh.value;
  if (autoRefresh.value) {
    startAutoRefresh();
  } else {
    stopAutoRefresh();
  }
}

async function handleRefresh() {
  await getMonitorData();
  window.$message?.success('刷新成功');
}

function handleIntervalChange(interval: number) {
  refreshInterval.value = interval;
  if (autoRefresh.value) {
    startAutoRefresh();
  }
}

onMounted(() => {
  getMonitorData();
  startAutoRefresh();
});

onUnmounted(() => {
  stopAutoRefresh();
});
</script>

<template>
  <div class="system-monitor-container">
    <!-- 页面头部 -->
    <Card :bordered="false" class="header-card">
      <div class="header-content">
        <div class="title-section">
          <div class="icon-wrapper">
            <SvgIcon icon="mdi:monitor-dashboard" class="header-icon" />
          </div>
          <div class="title-info">
            <h1 class="page-title">系统监控中心</h1>
            <p class="page-subtitle">实时监控系统运行状态和性能指标</p>
          </div>
        </div>
        
        <div class="control-section">
          <Space size="middle">
            <div class="status-info">
              <SvgIcon :icon="autoRefresh ? 'mdi:play-circle' : 'mdi:pause-circle'" 
                      :class="['status-icon', autoRefresh ? 'active' : 'paused']" />
              <span class="status-text">{{ autoRefresh ? '自动刷新中' : '已暂停刷新' }}</span>
            </div>
            
            <Button size="small" @click="() => handleIntervalChange(3000)" 
                    :type="refreshInterval === 3000 ? 'primary' : 'default'">3s</Button>
            <Button size="small" @click="() => handleIntervalChange(5000)" 
                    :type="refreshInterval === 5000 ? 'primary' : 'default'">5s</Button>
            <Button size="small" @click="() => handleIntervalChange(10000)" 
                    :type="refreshInterval === 10000 ? 'primary' : 'default'">10s</Button>
            
            <Button @click="toggleAutoRefresh" :type="autoRefresh ? 'default' : 'primary'">
              {{ autoRefresh ? '暂停' : '开始' }}
            </Button>
            <Button type="primary" :loading="loading" @click="handleRefresh">
              <template #icon>
                <SvgIcon icon="mdi:refresh" />
              </template>
              刷新
            </Button>
          </Space>
        </div>
      </div>
      
      <!-- 最后更新时间 -->
      <div v-if="monitorData?.timestamp" class="update-time">
        最后更新: {{ new Date(monitorData.timestamp).toLocaleString() }}
      </div>
    </Card>

    <Spin :spinning="loading" size="large">
      <!-- 核心指标卡片 -->
      <Row :gutter="[24, 24]" class="metrics-row">
        <Col :xs="24" :sm="12" :lg="8">
          <Card class="metric-card cpu-card" :bordered="false">
            <div class="metric-header">
              <SvgIcon icon="mdi:cpu-64-bit" class="metric-icon cpu-icon" />
              <div class="metric-info">
                <h3>CPU 使用率</h3>
                <p>{{ monitorData?.cpu.cpuModel }}</p>
              </div>
            </div>
            <div class="chart-container" ref="cpuChartRef"></div>
            <div class="metric-details">
              <Row :gutter="16">
                <Col :span="12">
                  <Statistic title="核心数" :value="monitorData?.cpu.cpuNum" suffix="个" />
                </Col>
                <Col :span="12">
                  <Statistic title="频率" :value="monitorData?.cpu.frequency" suffix="MHz" />
                </Col>
              </Row>
            </div>
          </Card>
        </Col>
        
        <Col :xs="24" :sm="12" :lg="8">
          <Card class="metric-card memory-card" :bordered="false">
            <div class="metric-header">
              <SvgIcon icon="mdi:memory" class="metric-icon memory-icon" />
              <div class="metric-info">
                <h3>内存使用率</h3>
                <p>{{ monitorData?.memory.usedFormatted }} / {{ monitorData?.memory.totalFormatted }}</p>
              </div>
            </div>
            <div class="chart-container" ref="memoryChartRef"></div>
            <div class="metric-details">
              <Row :gutter="16">
                <Col :span="12">
                  <Statistic title="可用内存" :value="monitorData?.memory.availableFormatted" />
                </Col>
                <Col :span="12">
                  <Statistic title="交换分区" 
                            :value="monitorData?.memory.swapUsage ? `${monitorData.memory.swapUsage.toFixed(1)}%` : 'N/A'" />
                </Col>
              </Row>
            </div>
          </Card>
        </Col>
        
        <Col :xs="24" :sm="12" :lg="8">
          <Card class="metric-card jvm-card" :bordered="false">
            <div class="metric-header">
              <SvgIcon icon="mdi:language-java" class="metric-icon jvm-icon" />
              <div class="metric-info">
                <h3>JVM 堆内存</h3>
                <p>{{ monitorData?.jvm.heapFormatted }}</p>
              </div>
            </div>
            <div class="chart-container" ref="jvmChartRef"></div>
            <div class="metric-details">
              <Row :gutter="16">
                <Col :span="12">
                  <Statistic title="非堆内存" :value="monitorData?.jvm.nonHeapFormatted" />
                </Col>
                <Col :span="12">
                  <Statistic title="运行时长" :value="monitorData?.jvm.runTime" />
                </Col>
              </Row>
            </div>
          </Card>
        </Col>
      </Row>

      <!-- 扩展监控指标 -->
      <Row :gutter="[24, 24]" class="extended-metrics-row">
        <Col :xs="24" :lg="8">
          <Card class="chart-card" title="线程状态分布" :bordered="false">
            <template #extra>
              <Tag v-if="monitorData?.thread.deadlockedThreads" color="red">
                死锁: {{ monitorData.thread.deadlockedThreads }}
              </Tag>
            </template>
            <div class="chart-container small" ref="threadChartRef"></div>
            <div class="thread-stats">
              <Row :gutter="16">
                <Col :span="8">
                  <Statistic title="活动线程" :value="monitorData?.thread.liveThreads" />
                </Col>
                <Col :span="8">
                  <Statistic title="守护线程" :value="monitorData?.thread.daemonThreads" />
                </Col>
                <Col :span="8">
                  <Statistic title="峰值线程" :value="monitorData?.thread.peakThreads" />
                </Col>
              </Row>
            </div>
          </Card>
        </Col>
        
        <Col :xs="24" :lg="8">
          <Card class="chart-card" title="系统负载" :bordered="false">
            <template #extra>
              <Tag :color="getStatusTagColor(monitorData?.systemLoad.loadLevel || '')">
                {{ monitorData?.systemLoad.loadLevel }}负载
              </Tag>
            </template>
            <div class="chart-container small" ref="systemLoadChartRef"></div>
            <div class="load-stats">
              <Row :gutter="16">
                <Col :span="12">
                  <Statistic title="进程数" :value="monitorData?.systemLoad.processCount" />
                </Col>
                <Col :span="12">
                  <Statistic title="负载率" :value="monitorData?.systemLoad.loadPercent" suffix="%" />
                </Col>
              </Row>
            </div>
          </Card>
        </Col>
        
        <Col :xs="24" :lg="8">
          <Card class="chart-card" title="网络流量统计" :bordered="false">
            <template #extra>
              <Space>
                <span class="network-rate">
                  ↑ {{ formatBytes(monitorData?.network.sendRate || 0) }}/s
                </span>
                <span class="network-rate">
                  ↓ {{ formatBytes(monitorData?.network.receiveRate || 0) }}/s
                </span>
              </Space>
            </template>
            <div class="chart-container small" ref="networkChartRef"></div>
            <div class="network-stats">
              <Row :gutter="16">
                <Col :span="12">
                  <Statistic title="发送包数" :value="monitorData?.network.totalPacketsSent" />
                </Col>
                <Col :span="12">
                  <Statistic title="接收包数" :value="monitorData?.network.totalPacketsReceived" />
                </Col>
              </Row>
            </div>
          </Card>
        </Col>
      </Row>

      <!-- 详细信息表格 -->
      <Row :gutter="[24, 24]" class="details-row">
        <Col :span="24">
          <Card title="服务器详细信息" :bordered="false" class="info-card">
            <Descriptions :items="serverInfoItems" :column="4" bordered size="small" />
          </Card>
        </Col>
        
        <Col :span="24">
          <Card title="Java虚拟机信息" :bordered="false" class="info-card">
            <Descriptions :items="jvmInfoItems" :column="3" bordered size="small" />
          </Card>
        </Col>
        
        <Col :xs="24" :lg="12">
          <Card title="磁盘状态" :bordered="false" class="table-card">
            <Table
              :columns="diskColumns"
              :data-source="monitorData?.disk"
              :pagination="false"
              row-key="dirName"
              size="small"
              :scroll="{ x: 600 }"
            />
          </Card>
        </Col>
        
        <Col :xs="24" :lg="12">
          <Card title="网络接口" :bordered="false" class="table-card">
            <Table
              :columns="networkColumns"
              :data-source="monitorData?.network?.interfaces"
              :pagination="false"
              row-key="name"
              size="small"
              :scroll="{ x: 600 }"
            />
          </Card>
        </Col>
        
        <Col :span="24" v-if="monitorData?.jvm?.gcInfo?.length">
          <Card title="垃圾回收信息" :bordered="false" class="table-card">
            <Table
              :columns="gcColumns"
              :data-source="monitorData.jvm.gcInfo"
              :pagination="false"
              row-key="name"
              size="small"
            />
          </Card>
        </Col>
      </Row>
    </Spin>
  </div>
</template>

<style scoped>
.system-monitor-container {
  padding: 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
}

.header-card {
  margin-bottom: 24px;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.title-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  border-radius: 20px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
}

.header-icon {
  font-size: 32px;
  color: white;
}

.title-info h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: #1a1a1a;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.title-info p {
  margin: 4px 0 0 0;
  color: #666;
  font-size: 14px;
}

.control-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.status-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 8px;
  background: rgba(0, 0, 0, 0.05);
}

.status-icon {
  font-size: 16px;
  transition: all 0.3s ease;
}

.status-icon.active {
  color: #52c41a;
}

.status-icon.paused {
  color: #faad14;
}

.status-text {
  font-size: 12px;
  color: #666;
}

.update-time {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  font-size: 12px;
  color: #999;
  text-align: right;
}

.metrics-row {
  margin-bottom: 24px;
}

.metric-card {
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.metric-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.cpu-card {
  border-left: 4px solid #667eea;
}

.memory-card {
  border-left: 4px solid #52c41a;
}

.jvm-card {
  border-left: 4px solid #fa8c16;
}

.metric-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.metric-icon {
  font-size: 32px;
  padding: 12px;
  border-radius: 12px;
  background: rgba(0, 0, 0, 0.05);
}

.cpu-icon {
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
}

.memory-icon {
  color: #52c41a;
  background: rgba(82, 196, 26, 0.1);
}

.jvm-icon {
  color: #fa8c16;
  background: rgba(250, 140, 22, 0.1);
}

.metric-info h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
}

.metric-info p {
  margin: 4px 0 0 0;
  font-size: 12px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 200px;
}

.chart-container {
  height: 280px;
  width: 100%;
}

.chart-container.small {
  height: 220px;
}

.metric-details {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.extended-metrics-row {
  margin-bottom: 24px;
}

.chart-card {
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.chart-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.thread-stats,
.load-stats,
.network-stats {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.network-rate {
  font-size: 12px;
  color: #666;
  background: rgba(0, 0, 0, 0.05);
  padding: 4px 8px;
  border-radius: 4px;
}

.details-row {
  margin-bottom: 24px;
}

.info-card,
.table-card {
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .system-monitor-container {
    padding: 16px;
  }
  
  .header-content {
    flex-direction: column;
    align-items: stretch;
  }
  
  .title-section {
    justify-content: center;
  }
  
  .control-section {
    justify-content: center;
    flex-wrap: wrap;
  }
  
  .chart-container {
    height: 240px;
  }
  
  .chart-container.small {
    height: 200px;
  }
}

/* 深色模式适配 */
@media (prefers-color-scheme: dark) {
  .system-monitor-container {
    background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
  }
  
  .metric-card,
  .chart-card,
  .info-card,
  .table-card,
  .header-card {
    background: rgba(255, 255, 255, 0.1);
    color: #fff;
  }
  
  .title-info h1 {
    color: #fff;
  }
  
  .metric-info h3 {
    color: #fff;
  }
}
</style> 