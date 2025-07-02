<script setup lang="ts">
import { reactive, watch } from 'vue';
import { $t } from '@/locales';

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
  <ACard :title="$t('common.search')" :bordered="false" class="card-wrapper">
    <AForm
      :model="searchModel"
      :label-col="{
        span: 5,
        md: 7
      }"
    >
      <ARow :gutter="[16, 16]" wrap>
        <ACol :span="24" :md="12" :lg="8">
          <AFormItem label="字典标签" name="dictLabel" class="m-0">
            <AInput v-model:value="searchModel.dictLabel" placeholder="请输入字典标签" @input="handleSearch" />
          </AFormItem>
        </ACol>
        <ACol :span="24" :md="12" :lg="8">
          <AFormItem label="字典键值" name="dictValue" class="m-0">
            <AInput v-model:value="searchModel.dictValue" placeholder="请输入字典键值" @press-enter="handleSearch" />
          </AFormItem>
        </ACol>
        <ACol :span="24" :md="12" :lg="8">
          <AFormItem label="状态" name="status" class="m-0">
            <ASelect
              v-model:value="searchModel.status"
              placeholder="请选择状态"
              :options="statusOptions"
              clearable
              @change="handleSearch"
            />
          </AFormItem>
        </ACol>
        <div class="flex-1">
          <AFormItem class="m-0">
            <div class="w-full flex-y-center justify-end gap-12px">
              <AButton @click="handleReset">
                <template #icon>
                  <icon-ic-round-refresh class="align-sub text-icon" />
                </template>
                <span class="ml-8px">{{ $t('common.reset') }}</span>
              </AButton>
              <AButton type="primary" ghost @click="handleSearch">
                <template #icon>
                  <icon-ic-round-search class="align-sub text-icon" />
                </template>
                <span class="ml-8px">{{ $t('common.search') }}</span>
              </AButton>
            </div>
          </AFormItem>
        </div>
      </ARow>
    </AForm>
  </ACard>
</template>

<style scoped></style>
