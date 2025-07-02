<template>
  <ADrawer
    :open="visible"
    title="登录日志详情"
    width="600"
    :closable="true"
    :destroy-on-close="true"
    @close="handleClose"
  >
    <div v-if="rowData" class="space-y-16px">
      <!-- 基本信息 -->
      <ACard title="基本信息" size="small">
        <div class="space-y-12px">
          <div class="flex items-center">
            <span class="label">用户账号：</span>
            <span class="value">{{ rowData.userName || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">登录状态：</span>
            <ATag :color="rowData.status === '0' ? 'success' : 'error'">
              {{ rowData.status === '0' ? '成功' : '失败' }}
            </ATag>
          </div>
          <div class="flex items-center">
            <span class="label">提示消息：</span>
            <span class="value">{{ rowData.msg || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">登录时间：</span>
            <span class="value">{{ rowData.loginTime || '-' }}</span>
          </div>
        </div>
      </ACard>

      <!-- 网络信息 -->
      <ACard title="网络信息" size="small">
        <div class="space-y-12px">
          <div class="flex items-center">
            <span class="label">登录IP：</span>
            <span class="value">{{ rowData.ipaddr || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">登录地点：</span>
            <span class="value">{{ rowData.loginLocation || '-' }}</span>
          </div>
        </div>
      </ACard>

      <!-- 设备信息 -->
      <ACard title="设备信息" size="small">
        <div class="space-y-12px">
          <div class="flex items-center">
            <span class="label">浏览器：</span>
            <span class="value">{{ rowData.browser || '-' }}</span>
          </div>
          <div class="flex items-center">
            <span class="label">操作系统：</span>
            <span class="value">{{ rowData.os || '-' }}</span>
          </div>
        </div>
      </ACard>
    </div>
  </ADrawer>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { 
  Drawer as ADrawer,
  Card as ACard,
  Tag as ATag
} from 'ant-design-vue';

defineOptions({
  name: 'LoginLogDetailDrawer'
});

interface Props {
  /** the edit row data or null */
  rowData?: {
    id?: number;
    userName?: string;
    ipaddr?: string;
    loginLocation?: string;
    browser?: string;
    os?: string;
    status?: string;
    msg?: string;
    loginTime?: string;
  } | null;
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

function handleClose() {
  emit('update:visible', false);
}
</script>

<style scoped>
.label {
  @apply text-sm font-medium text-gray-600 w-100px flex-shrink-0;
}

.value {
  @apply text-sm text-gray-900 flex-1;
}
</style> 