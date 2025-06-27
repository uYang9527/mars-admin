<script setup lang="ts">
import { computed } from 'vue';
import { $t } from '@/locales';

defineOptions({
  name: 'ProjectNews'
});

interface NewsItem {
  id: number;
  title: string;
  content: string;
  time: string;
  type: 'update' | 'feature' | 'fix' | 'security';
  avatar?: string;
}

const newses = computed<NewsItem[]>(() => [
  { 
    id: 1, 
    title: '系统安全更新',
    content: '修复了用户权限验证的安全漏洞，提升系统安全性', 
    time: '2024-01-15 14:30:00',
    type: 'security'
  },
  { 
    id: 2, 
    title: '新功能发布',
    content: '新增字典管理功能，支持系统配置项的动态管理', 
    time: '2024-01-12 09:15:30',
    type: 'feature'
  },
  { 
    id: 3, 
    title: '性能优化',
    content: '优化数据库查询性能，页面加载速度提升30%', 
    time: '2024-01-10 16:45:22',
    type: 'update'
  },
  { 
    id: 4, 
    title: 'Bug修复',
    content: '修复菜单管理页面搜索功能异常的问题', 
    time: '2024-01-08 11:20:15',
    type: 'fix'
  },
  { 
    id: 5, 
    title: 'UI界面优化',
    content: '优化登录页面设计，提升用户体验', 
    time: '2024-01-05 13:30:45',
    type: 'update'
  }
]);

const getTypeConfig = (type: NewsItem['type']) => {
  const configs = {
    update: { icon: 'mdi:update', color: '#1890ff', bgColor: '#e6f7ff' },
    feature: { icon: 'mdi:star', color: '#52c41a', bgColor: '#f6ffed' },
    fix: { icon: 'mdi:wrench', color: '#fa8c16', bgColor: '#fff7e6' },
    security: { icon: 'mdi:shield-check', color: '#f5222d', bgColor: '#fff2f0' }
  };
  return configs[type];
};

const formatTime = (time: string) => {
  const date = new Date(time);
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  const days = Math.floor(diff / (1000 * 60 * 60 * 24));
  
  if (days === 0) return '今天';
  if (days === 1) return '昨天';
  if (days < 7) return `${days}天前`;
  return date.toLocaleDateString();
};
</script>

<template>
  <ACard :bordered="false" class="card-wrapper">
    <template #title>
      <div class="flex-y-center gap-8px">
        <SvgIcon icon="mdi:newspaper" class="text-blue-500" />
        <span>项目动态</span>
      </div>
    </template>
    <template #extra>
      <AButton type="link" size="small">
        查看更多
        <template #icon>
          <SvgIcon icon="mdi:arrow-right" class="ml-4px" />
        </template>
      </AButton>
    </template>

    <div class="space-y-16px">
      <div
        v-for="item in newses"
        :key="item.id"
        class="group relative p-16px rounded-12px border border-gray-100 dark:border-gray-700 hover:border-blue-200 dark:hover:border-blue-600 transition-all duration-300 hover:shadow-md cursor-pointer"
      >
        <!-- 类型标识 -->
        <div class="absolute top-12px right-12px">
          <div 
            class="px-8px py-2px rounded-full text-12px font-medium"
            :style="{ 
              color: getTypeConfig(item.type).color, 
              backgroundColor: getTypeConfig(item.type).bgColor 
            }"
          >
            {{ item.type === 'update' ? '更新' : item.type === 'feature' ? '新功能' : item.type === 'fix' ? '修复' : '安全' }}
          </div>
        </div>

        <div class="pr-80px">
          <!-- 内容 -->
          <div class="w-full">
            <h4 class="text-14px font-medium text-gray-800 dark:text-gray-200 mb-4px group-hover:text-blue-600 transition-colors">
              {{ item.title }}
            </h4>
            <p class="text-13px text-gray-600 dark:text-gray-400 line-clamp-2 mb-8px">
              {{ item.content }}
            </p>
            <div class="flex-y-center gap-8px text-12px text-gray-500">
              <SvgIcon icon="mdi:clock-outline" />
              <span>{{ formatTime(item.time) }}</span>
            </div>
          </div>
        </div>

        <!-- 悬停效果 -->
        <div class="absolute inset-0 rounded-12px bg-gradient-to-r from-blue-50/0 to-blue-50/50 dark:from-blue-900/0 dark:to-blue-900/20 opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none"></div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="newses.length === 0" class="text-center py-40px">
      <SvgIcon icon="mdi:newspaper-variant-outline" class="text-48px text-gray-300 dark:text-gray-600 mb-12px" />
      <p class="text-gray-500 dark:text-gray-400">暂无项目动态</p>
    </div>
  </ACard>
</template>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
