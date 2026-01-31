<template>
  <div class="page-container">
    <!-- 欢迎卡片 -->
    <n-card class="welcome-card">
      <div class="welcome-content">
        <div class="welcome-text">
          <h1 class="welcome-title">
            欢迎回来，{{ userStore.nickname }} 👋
          </h1>
          <p class="welcome-desc">
            这是您的管理控制台，您可以在这里管理系统的各项功能
          </p>
        </div>
        <div class="welcome-time">
          <div class="time-display">{{ currentTime }}</div>
          <div class="date-display">{{ currentDate }}</div>
        </div>
      </div>
    </n-card>

    <!-- 统计卡片 -->
    <div class="stat-cards">
      <n-card v-for="stat in stats" :key="stat.title" class="stat-card">
        <div class="stat-content">
          <div class="stat-icon" :style="{ background: stat.bgColor }">
            <n-icon size="24" :color="stat.color">
              <component :is="stat.icon"/>
            </n-icon>
          </div>
          <div class="stat-info">
            <n-skeleton v-if="loading" :width="60" :height="28"/>
            <div v-else class="stat-value">{{ stat.value }}</div>
            <div class="stat-title">{{ stat.title }}</div>
          </div>
        </div>
      </n-card>
    </div>

    <!-- 中间区域：快捷入口 + 更新日志 -->
    <n-grid :x-gap="20" :cols="2" class="middle-section">
      <!-- 快捷入口 -->
      <n-gi>
        <n-card title="快捷入口" class="shortcuts-card">
          <div class="shortcuts-grid">
            <div
                v-for="shortcut in shortcuts"
                :key="shortcut.path"
                class="shortcut-item"
                @click="router.push(shortcut.path)"
            >
              <div class="shortcut-icon" :style="{ background: shortcut.bgColor }">
                <n-icon size="24" :color="shortcut.color">
                  <component :is="shortcut.icon"/>
                </n-icon>
              </div>
              <div class="shortcut-name">{{ shortcut.name }}</div>
            </div>
          </div>
        </n-card>
      </n-gi>

      <!-- 更新日志 -->
      <n-gi>
        <n-card title="更新日志" class="changelog-card">
          <n-timeline>
            <n-timeline-item
                v-for="log in changelog"
                :key="log.version"
                :type="log.type"
                :title="log.version"
                :time="log.date"
            >
              <ul class="changelog-list">
                <li v-for="(item, idx) in log.changes" :key="idx">{{ item }}</li>
              </ul>
            </n-timeline-item>
          </n-timeline>
        </n-card>
      </n-gi>
    </n-grid>

    <!-- 底部区域：系统信息 + 作者介绍 -->
    <n-grid :x-gap="20" :cols="2" class="bottom-section">
      <!-- 系统信息 -->
      <n-gi>
        <n-card title="系统信息" class="system-card">
          <n-descriptions :column="1" label-placement="left">
            <n-descriptions-item label="系统名称">Mars System</n-descriptions-item>
            <n-descriptions-item label="系统版本">v1.0.0</n-descriptions-item>
            <n-descriptions-item label="前端框架">Vue 3.4 + Naive UI</n-descriptions-item>
            <n-descriptions-item label="后端框架">Spring Boot 3.2</n-descriptions-item>
            <n-descriptions-item label="数据库">MySQL 8.0</n-descriptions-item>
            <n-descriptions-item label="缓存">Redis 7.0</n-descriptions-item>
          </n-descriptions>
        </n-card>
      </n-gi>

      <!-- 作者介绍 -->
      <n-gi>
        <n-card title="关于作者" class="author-card">
          <div class="author-content">
            <div class="author-avatar">
              <n-avatar
                  round
                  :size="80"
                  src="https://foruda.gitee.com/avatar/1692522394185109890/4768152_marsfactory_1692522394.png!avatar30"
              >
              </n-avatar>
            </div>
            <div class="author-info">
              <h3 class="author-name">程序员Mars</h3>
              <p class="author-desc">全栈开发工程师，热爱开源，专注于后台管理系统的开发与优化。</p>
              <div class="author-links">
                <n-space>
                  <n-tag type="info" :bordered="false">
                    <template #icon>
                      <n-icon>
                        <LogoGithub/>
                      </n-icon>
                    </template>
                    GitHub
                  </n-tag>
                  <n-tag type="success" :bordered="false">
                    <template #icon>
                      <n-icon>
                        <Globe/>
                      </n-icon>
                    </template>
                    博客
                  </n-tag>
                  <n-tag type="warning" :bordered="false">
                    <template #icon>
                      <n-icon>
                        <Mail/>
                      </n-icon>
                    </template>
                    联系我
                  </n-tag>
                </n-space>
              </div>
            </div>
          </div>
          <n-divider/>
          <div class="project-info">
            <p class="project-desc">
              Mars Admin 是一个基于 Spring Boot 3 + Vue 3 的现代化后台管理系统，
              采用最新的技术栈，提供完整的权限管理、系统监控等功能。
            </p>
            <div class="project-stats">
              <div class="project-stat-item">
                <n-icon size="18" color="#F59E0B">
                  <Star/>
                </n-icon>
                <span>开源免费</span>
              </div>
              <div class="project-stat-item">
                <n-icon size="18" color="#10B981">
                  <Refresh/>
                </n-icon>
                <span>持续更新</span>
              </div>
              <div class="project-stat-item">
                <n-icon size="18" color="#3B82F6">
                  <DocumentText/>
                </n-icon>
                <span>文档完善</span>
              </div>
            </div>
          </div>
        </n-card>
      </n-gi>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted, onUnmounted, markRaw} from 'vue'
import {useRouter} from 'vue-router'
import {
  PersonOutline,
  PeopleOutline,
  MenuOutline,
  ShieldCheckmarkOutline,
  LogoGithub,
  Globe,
  Mail,
  Star,
  Refresh,
  DocumentText,
  SettingsOutline,
  TimerOutline,
  ServerOutline
} from '@vicons/ionicons5'
import {useUserStore} from '@/stores/user'
import {dashboardApi} from '@/api/system'

const router = useRouter()
const userStore = useUserStore()

const currentTime = ref('')
const currentDate = ref('')
const loading = ref(true)

// 统计数据
const stats = ref([
  {
    title: '用户总数',
    value: 0,
    icon: markRaw(PersonOutline),
    color: '#111827',
    bgColor: '#F3F4F6'
  },
  {
    title: '角色数量',
    value: 0,
    icon: markRaw(PeopleOutline),
    color: '#059669',
    bgColor: '#D1FAE5'
  },
  {
    title: '菜单数量',
    value: 0,
    icon: markRaw(MenuOutline),
    color: '#2563EB',
    bgColor: '#DBEAFE'
  },
  {
    title: '权限数量',
    value: 0,
    icon: markRaw(ShieldCheckmarkOutline),
    color: '#D97706',
    bgColor: '#FEF3C7'
  }
])

// 快捷入口
const shortcuts = [
  {
    name: '用户管理',
    path: '/system/user',
    icon: markRaw(PersonOutline),
    color: '#111827',
    bgColor: '#F3F4F6'
  },
  {
    name: '角色管理',
    path: '/system/role',
    icon: markRaw(PeopleOutline),
    color: '#059669',
    bgColor: '#D1FAE5'
  },
  {
    name: '菜单管理',
    path: '/system/menu',
    icon: markRaw(MenuOutline),
    color: '#2563EB',
    bgColor: '#DBEAFE'
  },
  {
    name: '系统配置',
    path: '/system/config',
    icon: markRaw(SettingsOutline),
    color: '#7C3AED',
    bgColor: '#EDE9FE'
  },
  {
    name: '定时任务',
    path: '/monitor/job',
    icon: markRaw(TimerOutline),
    color: '#DC2626',
    bgColor: '#FEE2E2'
  },
  {
    name: '服务监控',
    path: '/monitor/server',
    icon: markRaw(ServerOutline),
    color: '#0891B2',
    bgColor: '#CFFAFE'
  }
]

// 更新日志
const changelog = [
  {
    version: 'v1.0.0',
    date: '2026-01-29',
    type: 'success' as const,
    changes: [
      '新增文件存储策略工厂（本地/MinIO/OSS/COS）',
      '新增推送服务策略工厂（极光/友盟/个推）',
      '新增短信/支付服务策略工厂',
      '优化登录页面（三种样式+滑块验证码）',
      '完善系统配置分组管理'
    ]
  },
  {
    version: 'v0.9.0',
    date: '2026-01-25',
    type: 'info' as const,
    changes: [
      '新增即时通讯功能（WebSocket私聊/群聊）',
      '完成字典管理和系统配置功能',
      '实现部门和岗位管理',
      '完成定时任务管理功能'
    ]
  },
  {
    version: 'v0.8.0',
    date: '2026-01-20',
    type: 'default' as const,
    changes: [
      '搭建项目基础框架',
      '集成 Sa-Token 实现认证授权',
      '完成基础权限管理（用户、角色、菜单）',
      '实现登录日志和操作日志记录',
      '添加系统监控功能'
    ]
  }
]

// 加载统计数据
async function loadStats() {
  try {
    loading.value = true
    const data = await dashboardApi.getStats()
    stats.value[0].value = data.userCount
    stats.value[1].value = data.roleCount
    stats.value[2].value = data.menuCount
    stats.value[3].value = data.permissionCount
  } catch (error) {
    console.error('加载统计数据失败', error)
  } finally {
    loading.value = false
  }
}

// 更新时间
function updateTime() {
  const now = new Date()
  currentTime.value = now.toLocaleTimeString('zh-CN', {hour12: false})
  currentDate.value = now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
}

let timer: number
onMounted(() => {
  updateTime()
  timer = window.setInterval(updateTime, 1000)
  loadStats()
})

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<style lang="scss" scoped>
.welcome-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #313a3d 0%, #55595d 100%);

  :deep(.n-card__content) {
    padding: 32px;
  }
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-title {
  font-size: 24px;
  font-weight: 700;
  color: #FFFFFF;
  margin-bottom: 8px;
}

.welcome-desc {
  font-size: 15px;
  color: #9CA3AF;
}

.welcome-time {
  text-align: right;
}

.time-display {
  font-size: 32px;
  font-weight: 700;
  color: #FFFFFF;
  font-variant-numeric: tabular-nums;
}

.date-display {
  font-size: 14px;
  color: #9CA3AF;
  margin-top: 4px;
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  :deep(.n-card__content) {
    padding: 20px;
  }
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  line-height: 1;
}

.stat-title {
  font-size: 14px;
  color: #6B7280;
  margin-top: 4px;
}

.middle-section {
  margin-bottom: 20px;
}

.shortcuts-card {
  height: 100%;
}

.shortcuts-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.shortcut-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    background: #F3F4F6;
  }
}

.shortcut-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
}

.shortcut-name {
  font-size: 13px;
  color: #374151;
  font-weight: 500;
}

.changelog-card {
  height: 100%;

  :deep(.n-card__content) {
    max-height: 280px;
    overflow-y: auto;
  }
}

.changelog-list {
  margin: 0;
  padding-left: 16px;
  font-size: 13px;
  color: #6B7280;

  li {
    margin-bottom: 4px;

    &:last-child {
      margin-bottom: 0;
    }
  }
}

.bottom-section {
  margin-bottom: 20px;
}

.system-card {
  height: 100%;

  :deep(.n-descriptions) {
    --n-th-padding: 10px 12px;
    --n-td-padding: 10px 12px;
  }
}

.author-card {
  height: 100%;
}

.author-content {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.author-avatar {
  flex-shrink: 0;
}

.author-info {
  flex: 1;
}

.author-name {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
  margin: 0 0 8px 0;
}

.author-desc {
  font-size: 14px;
  color: #6B7280;
  margin: 0 0 12px 0;
  line-height: 1.5;
}

.author-links {
  margin-top: 8px;
}

.project-info {
  margin-top: 4px;
}

.project-desc {
  font-size: 13px;
  color: #6B7280;
  margin: 0 0 12px 0;
  line-height: 1.6;
}

.project-stats {
  display: flex;
  gap: 24px;
}

.project-stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #374151;
}

@media (max-width: 1200px) {
  .stat-cards {
    grid-template-columns: repeat(2, 1fr);
  }

  .middle-section,
  .bottom-section {
    :deep(.n-grid) {
      display: block;
    }

    :deep(.n-gi) {
      margin-bottom: 20px;
    }
  }
}

@media (max-width: 768px) {
  .welcome-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }

  .welcome-time {
    text-align: left;
  }

  .stat-cards {
    grid-template-columns: 1fr;
  }

  .shortcuts-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .author-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .project-stats {
    justify-content: center;
    flex-wrap: wrap;
  }
}
</style>
