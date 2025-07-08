<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { $t } from '@/locales';
import { useAuthStore } from '@/store/modules/auth';
import HeaderBanner from './modules/header-banner.vue';
import LineChart from './modules/line-chart.vue';
import PieChart from './modules/pie-chart.vue';
import ProjectNews from './modules/project-news.vue';
import CreativityBanner from './modules/creativity-banner.vue';

defineOptions({
  name: 'HomePage'
});

const authStore = useAuthStore();

// 系统信息
const systemInfo = ref({
  version: '1.0.0',
  buildTime: '2024-01-01',
  javaVersion: 'Java 17',
  springBootVersion: '3.0.5',
  database: 'MySQL 8.0'
});



// 今日数据
const todayData = ref({
  visitors: 2847,
  orders: 156,
  sales: 45680,
  conversion: 4.8
});

// 获取当前时间问候语
const greeting = computed(() => {
  const hour = new Date().getHours();
  if (hour < 9) return '早上好';
  if (hour < 12) return '上午好';
  if (hour < 18) return '下午好';
  return '晚上好';
});

// 打开码云项目地址
const openGiteeProject = () => {
  window.open('https://gitee.com/Marsfactory/mars-admin', '_blank');
};

onMounted(() => {
  // 这里可以加载实际的系统数据
});
</script>

<template>
  <div class="flex-col-stretch gap-20px overflow-auto p-10px">
    <!-- 欢迎横幅 -->
    <div class="relative overflow-hidden">
      <!-- 背景装饰 -->
      <div class="absolute inset-0 bg-gradient-to-br from-blue-500/10 via-purple-500/10 to-pink-500/10"></div>
      <div class="absolute top-0 right-0 w-96 h-96 bg-gradient-to-bl from-blue-400/20 to-transparent rounded-full blur-3xl"></div>
      <div class="absolute bottom-0 left-0 w-80 h-80 bg-gradient-to-tr from-purple-400/20 to-transparent rounded-full blur-3xl"></div>

      <ACard :bordered="false" class="card-wrapper relative z-10">
        <div class="flex-y-center justify-between">
          <div class="flex-y-center gap-16px">
            <div class="relative">
              <div class="size-80px overflow-hidden rounded-full ring-4 ring-blue-100 dark:ring-blue-900/30">
                <img src="@/assets/imgs/soybean.jpg" class="size-full object-cover" />
              </div>
              <div class="absolute -bottom-1 -right-1 size-6 bg-green-500 rounded-full border-2 border-white dark:border-gray-800"></div>
            </div>
            <div>
              <h1 class="text-24px font-bold text-gray-800 dark:text-gray-100 mb-2">
                {{ greeting }}，{{ authStore.userInfo.userName }}！
              </h1>
              <p class="text-gray-600 dark:text-gray-400 text-16px">
                欢迎回到 MarsAdmin 管理系统，今天也要加油哦 🎉
              </p>
            </div>
          </div>

          <!-- 今日概览 -->
          <div class="hidden lg:flex gap-32px">
            <div class="text-center">
              <div class="text-20px font-bold text-blue-600">{{ todayData.visitors }}</div>
              <div class="text-12px text-gray-500">今日访问</div>
            </div>
            <div class="text-center">
              <div class="text-20px font-bold text-green-600">{{ todayData.orders }}</div>
              <div class="text-12px text-gray-500">新增订单</div>
            </div>
            <div class="text-center">
              <div class="text-20px font-bold text-purple-600">¥{{ todayData.sales.toLocaleString() }}</div>
              <div class="text-12px text-gray-500">今日销售</div>
            </div>
            <div class="text-center">
              <div class="text-20px font-bold text-orange-600">{{ todayData.conversion }}%</div>
              <div class="text-12px text-gray-500">转化率</div>
            </div>
          </div>
        </div>
      </ACard>
    </div>

    <!-- 数据概览仪表板 -->
    <ACard :bordered="false" class="card-wrapper">
      <template #title>
        <div class="flex-y-center gap-8px">
          <SvgIcon icon="mdi:chart-box" class="text-blue-500" />
          <span>数据概览</span>
        </div>
      </template>
      <template #extra>
        <div class="flex-y-center gap-8px text-12px text-gray-500">
          <SvgIcon icon="mdi:clock-outline" />
          <span>实时更新</span>
        </div>
      </template>

      <div class="grid grid-cols-2 md:grid-cols-4 gap-20px">
        <!-- 在线用户 -->
        <div class="group relative p-20px rounded-16px bg-gradient-to-br from-blue-50 to-blue-100 dark:from-blue-900/30 dark:to-blue-800/30 border border-blue-200/50 dark:border-blue-600/30 transition-all duration-300 hover:shadow-lg hover:-translate-y-1">
          <div class="flex-y-center justify-between mb-12px">
            <div class="w-40px h-40px rounded-12px bg-blue-500/20 flex-center">
              <SvgIcon icon="mdi:account-group" class="text-20px text-blue-600" />
            </div>
            <div class="px-6px py-2px rounded-full bg-green-100 dark:bg-green-900/30 text-10px text-green-600 font-medium">
              +12%
            </div>
          </div>
          <div class="text-24px font-bold text-gray-800 dark:text-gray-200 mb-2">
            {{ todayData.visitors.toLocaleString() }}
          </div>
          <div class="text-12px text-gray-600 dark:text-gray-400">在线用户</div>
        </div>

        <!-- 今日订单 -->
        <div class="group relative p-20px rounded-16px bg-gradient-to-br from-green-50 to-green-100 dark:from-green-900/30 dark:to-green-800/30 border border-green-200/50 dark:border-green-600/30 transition-all duration-300 hover:shadow-lg hover:-translate-y-1">
          <div class="flex-y-center justify-between mb-12px">
            <div class="w-40px h-40px rounded-12px bg-green-500/20 flex-center">
              <SvgIcon icon="mdi:shopping" class="text-20px text-green-600" />
            </div>
            <div class="px-6px py-2px rounded-full bg-green-100 dark:bg-green-900/30 text-10px text-green-600 font-medium">
              +8%
            </div>
          </div>
          <div class="text-24px font-bold text-gray-800 dark:text-gray-200 mb-2">
            {{ todayData.orders }}
          </div>
          <div class="text-12px text-gray-600 dark:text-gray-400">今日订单</div>
        </div>

        <!-- 销售额 -->
        <div class="group relative p-20px rounded-16px bg-gradient-to-br from-purple-50 to-purple-100 dark:from-purple-900/30 dark:to-purple-800/30 border border-purple-200/50 dark:border-purple-600/30 transition-all duration-300 hover:shadow-lg hover:-translate-y-1">
          <div class="flex-y-center justify-between mb-12px">
            <div class="w-40px h-40px rounded-12px bg-purple-500/20 flex-center">
              <SvgIcon icon="mdi:currency-usd" class="text-20px text-purple-600" />
            </div>
            <div class="px-6px py-2px rounded-full bg-red-100 dark:bg-red-900/30 text-10px text-red-600 font-medium">
              -2%
            </div>
          </div>
          <div class="text-24px font-bold text-gray-800 dark:text-gray-200 mb-2">
            ¥{{ todayData.sales.toLocaleString() }}
          </div>
          <div class="text-12px text-gray-600 dark:text-gray-400">今日销售</div>
        </div>

        <!-- 转化率 -->
        <div class="group relative p-20px rounded-16px bg-gradient-to-br from-orange-50 to-orange-100 dark:from-orange-900/30 dark:to-orange-800/30 border border-orange-200/50 dark:border-orange-600/30 transition-all duration-300 hover:shadow-lg hover:-translate-y-1">
          <div class="flex-y-center justify-between mb-12px">
            <div class="w-40px h-40px rounded-12px bg-orange-500/20 flex-center">
              <SvgIcon icon="mdi:trending-up" class="text-20px text-orange-600" />
            </div>
            <div class="px-6px py-2px rounded-full bg-green-100 dark:bg-green-900/30 text-10px text-green-600 font-medium">
              +5%
            </div>
          </div>
          <div class="text-24px font-bold text-gray-800 dark:text-gray-200 mb-2">
            {{ todayData.conversion }}%
          </div>
          <div class="text-12px text-gray-600 dark:text-gray-400">转化率</div>
        </div>
      </div>
    </ACard>



    <!-- 图表区域 -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-20px">
      <!-- 趋势图表 -->
      <div class="lg:col-span-2">
        <ACard :bordered="false" class="card-wrapper h-full">
          <template #title>
            <div class="flex-y-center gap-8px">
              <SvgIcon icon="mdi:chart-line" class="text-blue-500" />
              <span>数据趋势</span>
            </div>
          </template>
          <template #extra>
            <div class="flex-y-center gap-8px">
              <ASelect size="small" :value="'week'" style="width: 80px">
                <ASelectOption value="week">本周</ASelectOption>
                <ASelectOption value="month">本月</ASelectOption>
                <ASelectOption value="year">本年</ASelectOption>
              </ASelect>
            </div>
          </template>
          <div class="h-360px chart-container">
            <LineChart />
          </div>
        </ACard>
      </div>

      <!-- 数据分布 -->
      <div class="lg:col-span-1">
        <ACard :bordered="false" class="card-wrapper h-full">
          <template #title>
            <div class="flex-y-center gap-8px">
              <SvgIcon icon="mdi:chart-donut" class="text-purple-500" />
              <span>数据分布</span>
            </div>
          </template>
          <template #extra>
            <div class="text-12px text-gray-500">今日</div>
          </template>
          <div class="h-360px flex-center chart-container">
            <PieChart />
          </div>
        </ACard>
      </div>
    </div>

    <!-- 底部信息区域 -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-20px">
      <!-- 项目动态 -->
      <div>
        <ProjectNews />
      </div>

      <!-- 系统信息 -->
      <div>
        <ACard :bordered="false" class="card-wrapper h-full">
          <template #title>
            <div class="flex-y-center gap-8px">
              <SvgIcon icon="mdi:information" class="text-blue-500" />
              <span>系统信息</span>
            </div>
          </template>
          <template #extra>
            <div class="flex-y-center gap-4px text-12px text-gray-500">
              <div class="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
              <span>运行中</span>
            </div>
          </template>

          <div class="space-y-16px">
            <div class="flex justify-between items-center py-12px px-16px rounded-8px bg-gray-50 dark:bg-gray-800/50 hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors">
              <div class="flex-y-center gap-8px">
                <SvgIcon icon="mdi:tag" class="text-blue-500 text-14px" />
                <span class="text-gray-600 dark:text-gray-400">系统版本</span>
              </div>
              <span class="font-medium text-blue-600">v{{ systemInfo.version }}</span>
            </div>

            <div class="flex justify-between items-center py-12px px-16px rounded-8px bg-gray-50 dark:bg-gray-800/50 hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors">
              <div class="flex-y-center gap-8px">
                <SvgIcon icon="mdi:calendar" class="text-green-500 text-14px" />
                <span class="text-gray-600 dark:text-gray-400">构建时间</span>
              </div>
              <span class="font-medium">{{ systemInfo.buildTime }}</span>
            </div>

            <div class="flex justify-between items-center py-12px px-16px rounded-8px bg-gray-50 dark:bg-gray-800/50 hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors">
              <div class="flex-y-center gap-8px">
                <SvgIcon icon="mdi:language-java" class="text-orange-500 text-14px" />
                <span class="text-gray-600 dark:text-gray-400">Java版本</span>
              </div>
              <span class="font-medium">{{ systemInfo.javaVersion }}</span>
            </div>

            <div class="flex justify-between items-center py-12px px-16px rounded-8px bg-gray-50 dark:bg-gray-800/50 hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors">
              <div class="flex-y-center gap-8px">
                <SvgIcon icon="mdi:leaf" class="text-green-600 text-14px" />
                <span class="text-gray-600 dark:text-gray-400">Spring Boot</span>
              </div>
              <span class="font-medium">{{ systemInfo.springBootVersion }}</span>
            </div>

            <div class="flex justify-between items-center py-12px px-16px rounded-8px bg-gray-50 dark:bg-gray-800/50 hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors">
              <div class="flex-y-center gap-8px">
                <SvgIcon icon="mdi:database" class="text-blue-600 text-14px" />
                <span class="text-gray-600 dark:text-gray-400">数据库</span>
              </div>
              <span class="font-medium">{{ systemInfo.database }}</span>
            </div>
          </div>
        </ACard>
      </div>
    </div>

    <!-- 作者介绍 -->
    <ACard :bordered="false" class="card-wrapper">
      <template #title>
        <div class="flex-y-center gap-8px">
          <SvgIcon icon="mdi:account-heart" class="text-purple-500" />
          <span>作者介绍</span>
        </div>
      </template>

      <div class="relative overflow-hidden rounded-12px">
        <!-- 背景装饰 -->
        <div class="absolute inset-0 bg-gradient-to-br from-purple-50/50 via-blue-50/50 to-cyan-50/50 dark:from-purple-900/20 dark:via-blue-900/20 dark:to-cyan-900/20"></div>
        <div class="absolute top-0 right-0 w-32 h-32 bg-gradient-to-bl from-purple-200/30 to-transparent rounded-full blur-2xl"></div>
        <div class="absolute bottom-0 left-0 w-24 h-24 bg-gradient-to-tr from-blue-200/30 to-transparent rounded-full blur-xl"></div>

        <div class="relative z-10 p-24px">
          <!-- 主要内容区域 -->
          <div class="flex flex-col xl:flex-row xl:items-center gap-24px">
            <!-- 左侧：头像和基本信息 -->
            <div class="flex-1">
              <div class="flex flex-col sm:flex-row sm:items-start gap-20px">
                <!-- 头像区域 -->
                <div class="relative flex-shrink-0 mx-auto sm:mx-0">
                  <div class="w-80px h-80px rounded-full bg-gradient-to-br from-purple-500 to-blue-500 flex-center text-white text-24px font-bold shadow-lg ring-4 ring-purple-100 dark:ring-purple-900/30">
                    M
                  </div>
                  <div class="absolute -bottom-2 -right-2 w-6 h-6 bg-green-500 rounded-full border-2 border-white dark:border-gray-800 flex-center">
                    <SvgIcon icon="mdi:check" class="text-white text-12px" />
                  </div>
                </div>

                <!-- 基本信息 -->
                <div class="flex-1 text-center sm:text-left">
                  <div class="flex flex-col sm:flex-row sm:items-center gap-8px mb-12px">
                    <h3 class="text-22px font-bold text-gray-800 dark:text-gray-200">程序员Mars</h3>

                  </div>

                  <p class="text-15px text-gray-600 dark:text-gray-400 leading-relaxed mb-20px max-w-md">
                    专注于前后端技术栈开发，致力于打造高质量的企业级管理系统
                  </p>

                  <!-- 联系方式 -->
                  <div class="flex flex-col sm:flex-row gap-16px">
                    <div class="flex items-center gap-12px justify-center sm:justify-start">
                      <div class="w-40px h-40px rounded-12px bg-green-100 dark:bg-green-900/30 flex-center">
                        <SvgIcon icon="mdi:wechat" class="text-18px text-green-600" />
                      </div>
                      <div class="text-left">
                        <div class="text-12px text-gray-500 dark:text-gray-400">微信联系</div>
                        <div class="text-15px font-semibold text-gray-800 dark:text-gray-200">Mars8377</div>
                      </div>
                    </div>

                    <div class="flex items-center gap-12px justify-center sm:justify-start">
                      <div class="w-40px h-40px rounded-12px bg-blue-100 dark:bg-blue-900/30 flex-center">
                        <SvgIcon icon="mdi:handshake" class="text-18px text-blue-600" />
                      </div>
                      <div class="text-left">
                        <div class="text-12px text-gray-500 dark:text-gray-400">商务合作</div>
                        <div class="text-15px font-semibold text-gray-800 dark:text-gray-200">添加微信详谈</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 右侧：操作按钮 -->
            <div class="flex xl:flex-col gap-12px xl:min-w-140px justify-center">
              <AButton type="primary" size="middle" class="px-24px h-44px font-medium flex-center">
                <template #icon>
                  <SvgIcon icon="mdi:music-note" class="text-16px" />
                </template>
                <span class="ml-6px">关注抖音</span>
              </AButton>
              <AButton size="middle" class="px-24px h-44px font-medium flex-center" @click="openGiteeProject">
                <template #icon>
                  <SvgIcon icon="mdi:source-repository" class="text-16px" />
                </template>
                <span class="ml-6px">码云项目</span>
              </AButton>
            </div>
          </div>

          <!-- 技能标签 -->
          <div class="mt-24px pt-20px border-t border-gray-200/60 dark:border-gray-700/60">
            <div class="flex flex-col sm:flex-row sm:items-center gap-12px">
              <span class="text-13px text-gray-500 dark:text-gray-400 font-medium">技术栈：</span>
              <div class="flex items-center gap-8px flex-wrap">
                <span v-for="skill in ['Java', 'Spring Boot', 'Python', '微服务', '爬虫逆向', '数据分析', '机器学习', '深度学习', '前端']"
                      :key="skill"
                      class="px-10px py-4px rounded-full bg-gradient-to-r from-gray-100 to-gray-50 dark:from-gray-800 dark:to-gray-700 text-12px text-gray-700 dark:text-gray-300 font-medium border border-gray-200/50 dark:border-gray-600/50 hover:shadow-sm transition-all duration-200">
                  {{ skill }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </ACard>

    <!-- 创意横幅 -->
    <CreativityBanner />
  </div>
</template>

<style scoped>
.card-wrapper {
  @apply shadow-sm hover:shadow-md transition-shadow duration-300;
}

/* 自定义滚动条样式 */
:deep(.ant-card-body) {
  scrollbar-width: thin;
  scrollbar-color: rgba(156, 163, 175, 0.3) transparent;
}

:deep(.ant-card-body)::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

:deep(.ant-card-body)::-webkit-scrollbar-track {
  background: transparent;
}

:deep(.ant-card-body)::-webkit-scrollbar-thumb {
  background-color: rgba(156, 163, 175, 0.3);
  border-radius: 3px;
  transition: background-color 0.3s ease;
}

:deep(.ant-card-body)::-webkit-scrollbar-thumb:hover {
  background-color: rgba(156, 163, 175, 0.5);
}

/* 页面滚动条样式 */
.flex-col-stretch {
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE and Edge */
}

.flex-col-stretch::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

/* 响应式调整 */
@media (max-width: 640px) {
  .grid-cols-2 {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .lg\\:col-span-2,
  .lg\\:col-span-1 {
    grid-column: span 1;
  }
}

/* 图表容器样式 */
.chart-container {
  @apply relative overflow-hidden;
}

.chart-container::before {
  content: '';
  @apply absolute inset-0 bg-gradient-to-br from-blue-50/30 to-purple-50/30 dark:from-blue-900/10 dark:to-purple-900/10 pointer-events-none;
}
</style>
