<script setup lang="ts">
import { computed, ref } from 'vue';
import { $t } from '@/locales';

defineOptions({
  name: 'CreativityBanner'
});

// 技术栈信息
const techStack = ref([
  { name: 'Vue 3', icon: 'mdi:vuejs', color: '#4FC08D' },
  { name: 'TypeScript', icon: 'mdi:language-typescript', color: '#3178C6' },
  { name: 'Vite', icon: 'mdi:lightning-bolt', color: '#646CFF' },
  { name: 'Ant Design', icon: 'mdi:ant-design', color: '#1890FF' },
  { name: 'Spring Boot', icon: 'mdi:leaf', color: '#6DB33F' },
  { name: 'MySQL', icon: 'mdi:database', color: '#4479A1' }
]);

// 特性列表
const features = ref([
  { title: '现代化技术栈', desc: '基于 Vue 3 + Spring Boot 3 构建', icon: 'mdi:rocket-launch' },
  { title: '完整权限系统', desc: 'RBAC 权限控制，安全可靠', icon: 'mdi:shield-check' },
  { title: '响应式设计', desc: '完美适配各种设备屏幕', icon: 'mdi:responsive' },
  { title: '开箱即用', desc: '丰富的功能模块，快速开发', icon: 'mdi:package-variant' }
]);
</script>

<template>
  <ACard :bordered="false" class="card-wrapper overflow-hidden">
    <div class="relative">
      <!-- 背景装饰 -->
      <div class="absolute inset-0 bg-gradient-to-br from-purple-500/5 via-blue-500/5 to-cyan-500/5"></div>
      <div class="absolute top-0 right-0 w-64 h-64 bg-gradient-to-bl from-purple-400/10 to-transparent rounded-full blur-3xl"></div>
      <div class="absolute bottom-0 left-0 w-48 h-48 bg-gradient-to-tr from-blue-400/10 to-transparent rounded-full blur-2xl"></div>
      
      <div class="relative z-10 p-24px">
        <!-- 标题区域 -->
        <div class="text-center mb-32px">
          <div class="flex-center gap-12px mb-16px">
            <div class="relative">
              <SvgIcon icon="mdi:rocket-launch" class="text-48px text-purple-500" />
              <div class="absolute -top-2 -right-2 w-4 h-4 bg-green-500 rounded-full animate-pulse"></div>
            </div>
          </div>
          <h2 class="text-28px font-bold text-gray-800 dark:text-gray-100 mb-8px">
            MarsAdmin 管理系统
          </h2>
          <p class="text-16px text-gray-600 dark:text-gray-400 max-w-md mx-auto">
            基于现代化技术栈构建的企业级后台管理系统，助力您的项目快速开发
          </p>
        </div>

        <!-- 技术栈展示 -->
        <div class="mb-32px">
          <h3 class="text-center text-18px font-semibold text-gray-800 dark:text-gray-200 mb-20px">
            技术栈
          </h3>
          <div class="flex-center gap-16px flex-wrap">
            <div
              v-for="tech in techStack"
              :key="tech.name"
              class="group flex-center gap-8px px-16px py-8px rounded-full border border-gray-200 dark:border-gray-700 hover:border-opacity-50 transition-all duration-300 hover:scale-105"
              :style="{ borderColor: tech.color + '40' }"
            >
              <SvgIcon 
                :icon="tech.icon" 
                class="text-20px transition-transform duration-300 group-hover:scale-110"
                :style="{ color: tech.color }"
              />
              <span class="text-14px font-medium text-gray-700 dark:text-gray-300">{{ tech.name }}</span>
            </div>
          </div>
        </div>

        <!-- 特性展示 -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-20px">
          <div
            v-for="feature in features"
            :key="feature.title"
            class="group text-center p-20px rounded-16px border border-gray-100 dark:border-gray-700 hover:border-blue-200 dark:hover:border-blue-600 transition-all duration-300 hover:shadow-lg hover:-translate-y-1"
          >
            <div class="w-48px h-48px rounded-12px bg-blue-50 dark:bg-blue-900/30 flex-center mx-auto mb-12px group-hover:scale-110 transition-transform duration-300">
              <SvgIcon :icon="feature.icon" class="text-24px text-blue-500" />
            </div>
            <h4 class="text-14px font-semibold text-gray-800 dark:text-gray-200 mb-6px">
              {{ feature.title }}
            </h4>
            <p class="text-12px text-gray-600 dark:text-gray-400 leading-relaxed">
              {{ feature.desc }}
            </p>
          </div>
        </div>

        <!-- 底部操作 -->
        <div class="flex-center gap-16px mt-32px">
          <AButton type="primary" size="large" class="px-24px">
            <template #icon>
              <SvgIcon icon="mdi:github" />
            </template>
            查看源码
          </AButton>
          <AButton size="large" class="px-24px">
            <template #icon>
              <SvgIcon icon="mdi:file-document" />
            </template>
            使用文档
          </AButton>
        </div>

        <!-- 版本信息 -->
        <div class="text-center mt-24px">
          <div class="inline-flex items-center gap-8px px-12px py-4px rounded-full bg-gray-100 dark:bg-gray-800 text-12px text-gray-600 dark:text-gray-400">
            <SvgIcon icon="mdi:tag" class="text-green-500" />
            <span>Version 1.0.0</span>
            <span class="mx-4px">•</span>
            <span>MIT License</span>
          </div>
        </div>
      </div>
    </div>
  </ACard>
</template>

<style scoped>
/* 动画效果 */
@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.group:hover .animate-float {
  animation: float 2s ease-in-out infinite;
}
</style>
