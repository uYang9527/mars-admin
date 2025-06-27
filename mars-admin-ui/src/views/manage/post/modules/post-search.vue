<script setup lang="ts">
import { computed } from 'vue';
import { $t } from '@/locales';
import { useAntdForm, useFormRules } from '@/hooks/common/form';
import { enableStatusOptions } from '@/constants/business';
import { translateOptions } from '@/utils/common';

defineOptions({
  name: 'PostSearch'
});

interface Emits {
  (e: 'reset'): void;
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const { formRef, validate, resetFields } = useAntdForm();

const model = defineModel<Api.SystemManage.PostSearchParams>('model', { required: true });

async function reset() {
  await resetFields();
  emit('reset');
}

async function search() {
  await validate();
  emit('search');
}
</script>

<template>
  <ACard :title="$t('common.search')" :bordered="false" class="card-wrapper">
    <AForm
      ref="formRef"
      :model="model"
      :label-col="{
        span: 5,
        md: 7
      }"
    >
      <ARow :gutter="[16, 16]" wrap>
        <ACol :span="24" :md="12" :lg="8">
          <AFormItem label="岗位名称" name="postName" class="m-0">
            <AInput v-model:value="model.postName" placeholder="请输入岗位名称" />
          </AFormItem>
        </ACol>
        <ACol :span="24" :md="12" :lg="8">
          <AFormItem label="岗位编码" name="postCode" class="m-0">
            <AInput v-model:value="model.postCode" placeholder="请输入岗位编码" />
          </AFormItem>
        </ACol>
        <ACol :span="24" :md="12" :lg="8">
          <AFormItem label="状态" name="status" class="m-0">
            <ASelect
              v-model:value="model.status"
              placeholder="请选择状态"
              :options="translateOptions(enableStatusOptions)"
              clearable
            />
          </AFormItem>
        </ACol>
        <div class="flex-1">
          <AFormItem class="m-0">
            <div class="w-full flex-y-center justify-end gap-12px">
              <AButton @click="reset">
                <template #icon>
                  <icon-ic-round-refresh class="align-sub text-icon" />
                </template>
                <span class="ml-8px">{{ $t('common.reset') }}</span>
              </AButton>
              <AButton type="primary" ghost @click="search">
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