<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import { $t } from '@/locales';

defineOptions({
  name: 'DictTypeSearch'
});

interface Model {
  dictName: string;
  dictType: string;
  status: number | null;
}

const props = defineProps<{
  model: Model;
}>();

const emit = defineEmits<{
  (e: 'update:model', model: Model): void;
  (e: 'search'): void;
  (e: 'reset'): void;
}>();

// 折叠状态
const collapsed = ref(false);

const statusOptions = [
  { label: '正常', value: 1 },
  { label: '停用', value: 0 }
];

const searchModel = reactive<Model>({
  dictName: '',
  dictType: '',
  status: null
});

watch(
  () => props.model,
  newModel => {
    Object.assign(searchModel, newModel);
  },
  { immediate: true, deep: true }
);

function handleSearch() {
  emit('update:model', { ...searchModel });
  emit('search');
}

function handleReset() {
  Object.assign(searchModel, {
    dictName: '',
    dictType: '',
    status: null
  });
  emit('update:model', { ...searchModel });
  emit('reset');
}

function toggleCollapsed() {
  collapsed.value = !collapsed.value;
}
</script>

<template>
  <ACard
    :title="$t('common.search')"
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
      <AForm :model="searchModel">
        <AFormItem label="字典名称" class="mb-12px">
          <AInput v-model:value="searchModel.dictName" placeholder="请输入字典名称" size="small" />
        </AFormItem>
        <AFormItem label="字典类型" class="mb-12px">
          <AInput v-model:value="searchModel.dictType" placeholder="请输入字典类型" size="small" />
        </AFormItem>
        <AFormItem label="状态" class="mb-12px">
          <ASelect
            v-model:value="searchModel.status"
            placeholder="请选择状态"
            :options="statusOptions"
            clearable
            size="small"
            class="w-full"
          />
        </AFormItem>
        <AFormItem class="mb-0">
          <ASpace class="w-full" direction="vertical" size="small">
            <AButton type="primary" ghost size="small" block @click="handleSearch">
              <template #icon>
                <icon-ic-round-search class="align-sub text-icon" />
              </template>
              <span class="ml-8px">{{ $t('common.search') }}</span>
            </AButton>
            <AButton size="small" block @click="handleReset">
              <template #icon>
                <icon-ic-round-refresh class="align-sub text-icon" />
              </template>
              <span class="ml-8px">{{ $t('common.reset') }}</span>
            </AButton>
          </ASpace>
        </AFormItem>
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
