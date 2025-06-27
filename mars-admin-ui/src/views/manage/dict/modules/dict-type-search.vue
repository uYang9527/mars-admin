<script setup lang="ts">
import { reactive, watch } from 'vue';
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
</script>

<template>
  <ACard :title="$t('common.search')" :bordered="false" class="card-wrapper">
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
          <AButton type="primary" ghost @click="handleSearch" size="small" block>
            <template #icon>
              <icon-ic-round-search class="align-sub text-icon" />
            </template>
            <span class="ml-8px">{{ $t('common.search') }}</span>
          </AButton>
          <AButton @click="handleReset" size="small" block>
            <template #icon>
              <icon-ic-round-refresh class="align-sub text-icon" />
            </template>
            <span class="ml-8px">{{ $t('common.reset') }}</span>
          </AButton>
        </ASpace>
      </AFormItem>
    </AForm>
  </ACard>
</template>

<style scoped></style> 