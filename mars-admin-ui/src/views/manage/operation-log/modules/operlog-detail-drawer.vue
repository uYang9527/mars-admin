<script setup lang="ts">
import { computed } from 'vue';
import { Card as ACard, Drawer as ADrawer, Tag as ATag } from 'ant-design-vue';

defineOptions({
  name: 'OperLogDetailDrawer'
});

interface Props {
  /** the edit row data or null */
  rowData?: Api.LogManage.OperLog | null;
  /** whether the drawer is visible */
  visible: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  rowData: null
});

interface Emits {
  (e: 'update:visible', visible: boolean): void;
}

const emit = defineEmits<Emits>();

const visible = computed({
  get() {
    return props.visible;
  },
  set(value: boolean) {
    emit('update:visible', value);
  }
});

// 获取业务类型颜色
function getBusinessTypeColor(type?: number): string {
  const colors = {
    0: 'default', // 其它
    1: 'green', // 新增
    2: 'blue', // 修改
    3: 'red' // 删除
  };
  return colors[type || 0] || 'default';
}

// 获取业务类型文本
function getBusinessTypeText(type?: number): string {
  const texts = {
    0: '其它',
    1: '新增',
    2: '修改',
    3: '删除'
  };
  return texts[type || 0] || '未知';
}
</script>

<template>
  <ADrawer v-model:open="visible" title="操作日志详情" width="500" :closable="true" :destroy-on-close="true">
    <div v-if="rowData" class="space-y-16px">
      <!-- 基本信息 -->
      <ACard title="基本信息" size="small">
        <div class="space-y-12px">
          <div class="flex items-center">
            <span class="label">模块标题：</span>
            <span class="value">{{ rowData.title || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">业务类型：</span>
            <ATag :color="getBusinessTypeColor(rowData.businessType)">
              {{ getBusinessTypeText(rowData.businessType) }}
            </ATag>
          </div>
          <div class="flex items-center">
            <span class="label">操作状态：</span>
            <ATag :color="rowData.status === 0 ? 'success' : 'error'">
              {{ rowData.status === 0 ? '正常' : '异常' }}
            </ATag>
          </div>
          <div class="flex items-center">
            <span class="label">操作人员：</span>
            <span class="value">{{ rowData.operName || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">部门名称：</span>
            <span class="value">{{ rowData.deptName || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">操作时间：</span>
            <span class="value">{{ rowData.operTime || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">消耗时间：</span>
            <span class="value">{{ rowData.costTime || 0 }}ms</span>
          </div>
        </div>
      </ACard>

      <!-- 请求信息 -->
      <ACard title="请求信息" size="small">
        <div class="space-y-12px">
          <div class="flex items-center">
            <span class="label">请求方式：</span>
            <ATag color="blue">{{ rowData.requestMethod || '-' }}</ATag>
          </div>
          <div class="flex items-center">
            <span class="label">请求URL：</span>
            <span class="value text-break">{{ rowData.operUrl || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">主机地址：</span>
            <span class="value">{{ rowData.operIp || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">操作地点：</span>
            <span class="value">{{ rowData.operLocation || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">方法名称：</span>
            <span class="value text-break text-xs">{{ rowData.method || '-' }}</span>
          </div>
        </div>
      </ACard>

      <!-- 请求参数 -->
      <ACard title="请求参数" size="small">
        <div class="max-h-200px overflow-auto">
          <pre class="whitespace-pre-wrap break-words text-xs">{{ rowData.operParam || '无' }}</pre>
        </div>
      </ACard>

      <!-- 返回参数 -->
      <ACard title="返回参数" size="small">
        <div class="max-h-200px overflow-auto">
          <pre class="whitespace-pre-wrap break-words text-xs">{{ rowData.jsonResult || '无' }}</pre>
        </div>
      </ACard>

      <!-- 错误信息 -->
      <ACard v-if="rowData.status === 1" title="错误信息" size="small">
        <div class="max-h-200px overflow-auto">
          <pre class="whitespace-pre-wrap break-words text-xs text-red-500">{{ rowData.errorMsg || '无' }}</pre>
        </div>
      </ACard>
    </div>
  </ADrawer>
</template>

<style scoped>
.label {
  @apply text-sm font-medium w-100px flex-shrink-0;
  @apply text-gray-600 dark:text-gray-300;
}

.value {
  @apply text-sm flex-1;
  @apply text-gray-900 dark:text-gray-100;
}
</style>
