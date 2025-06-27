<script setup lang="ts">
import { reactive, watch } from 'vue';
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons-vue';

defineOptions({
  name: 'DictDataSearch'
});

interface Model {
  dictLabel: string;
  dictValue: string;
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
  dictLabel: '',
  dictValue: '',
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
  const dictType = searchModel.dictType; // 保留字典类型
  Object.assign(searchModel, {
    dictLabel: '',
    dictValue: '',
    dictType, // 重置时保留字典类型
    status: null
  });
  emit('update:model', { ...searchModel });
  emit('reset');
}
</script>

<template>
  <ACard title="查询条件" :bordered="false">
    <AForm :model="searchModel" layout="inline">
      <AFormItem label="字典标签">
        <AInput v-model:value="searchModel.dictLabel" placeholder="请输入字典标签" allow-clear />
      </AFormItem>
      <AFormItem label="字典键值">
        <AInput v-model:value="searchModel.dictValue" placeholder="请输入字典键值" allow-clear />
      </AFormItem>
      <AFormItem label="状态">
        <ASelect v-model:value="searchModel.status" placeholder="请选择状态" allow-clear style="width: 120px">
          <ASelectOption v-for="item in statusOptions" :key="item.value" :value="item.value">
            {{ item.label }}
          </ASelectOption>
        </ASelect>
      </AFormItem>
      <AFormItem>
        <ASpace>
          <AButton type="primary" @click="handleSearch">
            <template #icon><SearchOutlined /></template>
            查询
          </AButton>
          <AButton @click="handleReset">
            <template #icon><ReloadOutlined /></template>
            重置
          </AButton>
        </ASpace>
      </AFormItem>
    </AForm>
  </ACard>
</template>

<style scoped></style> 