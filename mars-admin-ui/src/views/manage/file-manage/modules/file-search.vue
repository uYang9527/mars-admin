<script setup lang="ts">
import { ref } from 'vue';
import { fetchGetEnabledOssConfigs } from '@/service/api/file-manage';

defineOptions({
  name: 'FileSearch'
});

interface Emits {
  (e: 'reset'): void;
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const model = defineModel<Api.FileManage.FileSearchParams>('model', { required: true });

// 折叠状态
const collapsed = ref(false);

// OSS配置选项
const ossConfigs = ref<any[]>([]);

// 获取OSS配置
async function loadOssConfigs() {
  try {
    const { data: configs, error } = await fetchGetEnabledOssConfigs();
    if (!error && configs) {
      ossConfigs.value = configs;
    }
  } catch (error) {
    console.error('获取存储配置失败:', error);
  }
}

async function reset() {
  model.value.fileName = '';
  model.value.configKey = '';
  emit('reset');
}

async function search() {
  emit('search');
}

function toggleCollapsed() {
  collapsed.value = !collapsed.value;
}

// 加载配置
loadOssConfigs();
</script>

<template>
  <ACard
    title="搜索"
    :bordered="false"
    class="card-wrapper"
    :body-style="collapsed ? { padding: '0', display: 'none' } : {}"
    :data-collapsed="collapsed"
  >
    <template #extra>
      <AButton type="text" @click="toggleCollapsed">
        <template #icon>
          <icon-mdi:chevron-down v-if="collapsed" class="text-icon" />
          <icon-mdi:chevron-up v-else class="text-icon" />
        </template>
        {{ collapsed ? '展开' : '收起' }}
      </AButton>
    </template>

    <div v-show="!collapsed">
      <AForm
        :model="model"
        :label-col="{
          span: 5,
          md: 7
        }"
      >
        <ARow :gutter="[16, 16]" wrap>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem label="文件名" name="fileName" class="m-0">
              <AInput v-model:value="model.fileName" placeholder="请输入文件名" @press-enter="search" />
            </AFormItem>
          </ACol>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem label="存储配置" name="configKey" class="m-0">
              <ASelect v-model:value="model.configKey" placeholder="请选择存储配置" clearable @change="search">
                <ASelectOption v-for="config in ossConfigs" :key="config.configKey" :value="config.configKey">
                  {{ config.configKey }}
                </ASelectOption>
              </ASelect>
            </AFormItem>
          </ACol>
          <div class="flex-1">
            <AFormItem class="m-0">
              <div class="w-full flex-y-center justify-end gap-12px">
                <AButton @click="reset">
                  <template #icon>
                    <icon-ic-round-refresh class="align-sub text-icon" />
                  </template>
                  <span class="ml-8px">重置</span>
                </AButton>
                <AButton type="primary" ghost @click="search">
                  <template #icon>
                    <icon-ic-round-search class="align-sub text-icon" />
                  </template>
                  <span class="ml-8px">查询</span>
                </AButton>
              </div>
            </AFormItem>
          </div>
        </ARow>
      </AForm>
    </div>
  </ACard>
</template>

<style scoped>
.text-icon {
  font-size: 16px;
}

/* 折叠状态下的卡片样式优化 */
.card-wrapper {
  transition: all 0.3s ease;
}

/* 折叠时隐藏卡片主体，只保留标题栏 */
.card-wrapper :deep(.ant-card-body) {
  transition: all 0.3s ease;
}

/* 当折叠时，强制隐藏卡片主体 */
.card-wrapper[data-collapsed='true'] :deep(.ant-card-body) {
  display: none !important;
  height: 0 !important;
  padding: 0 !important;
  margin: 0 !important;
}

/* 优化折叠后的卡片头部样式 */
.card-wrapper[data-collapsed='true'] :deep(.ant-card-head) {
  margin-bottom: 0 !important;
  border-bottom: none !important;
}
</style>
