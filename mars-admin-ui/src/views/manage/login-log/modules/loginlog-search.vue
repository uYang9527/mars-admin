<script setup lang="ts">
import { ref } from 'vue';
import {
  Button as AButton,
  Card as ACard,
  Col as ACol,
  Form as AForm,
  FormItem as AFormItem,
  Input as AInput,
  Row as ARow,
  Select as ASelect
} from 'ant-design-vue';

defineOptions({
  name: 'LoginLogSearch'
});

interface SearchModel {
  current?: number;
  size?: number;
  userName?: string;
  ipaddr?: string;
  status?: string;
  loginLocation?: string;
}

interface Props {
  modelValue?: SearchModel;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'update:modelValue', value: SearchModel): void;
  (e: 'reset'): void;
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

// 折叠状态
const collapsed = ref(false);
const formRef = ref();

// 状态选项
const statusOptions = [
  { label: '成功', value: '0' },
  { label: '失败', value: '1' }
];

function toggleCollapsed() {
  collapsed.value = !collapsed.value;
}

function updateField(field: keyof SearchModel, value: any) {
  const newModel = { ...props.modelValue, [field]: value };
  emit('update:modelValue', newModel);
}

function handleSearch() {
  emit('search');
}

function handleReset() {
  emit('reset');
}
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
        ref="formRef"
        :model="modelValue"
        :label-col="{
          span: 5,
          md: 7
        }"
      >
        <ARow :gutter="[16, 16]" wrap>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem label="用户账号" name="userName" class="m-0">
              <AInput
                :value="modelValue?.userName || ''"
                placeholder="请输入用户账号"
                allow-clear
                @update:value="updateField('userName', $event)"
                @press-enter="handleSearch"
              />
            </AFormItem>
          </ACol>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem label="登录IP" name="ipaddr" class="m-0">
              <AInput
                :value="modelValue?.ipaddr || ''"
                placeholder="请输入登录IP"
                allow-clear
                @update:value="updateField('ipaddr', $event)"
                @press-enter="handleSearch"
              />
            </AFormItem>
          </ACol>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem label="登录状态" name="status" class="m-0">
              <ASelect
                :value="modelValue?.status || ''"
                placeholder="请选择登录状态"
                :options="statusOptions"
                clearable
                @update:value="updateField('status', $event)"
              />
            </AFormItem>
          </ACol>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem label="登录地点" name="loginLocation" class="m-0">
              <AInput
                :value="modelValue?.loginLocation || ''"
                placeholder="请输入登录地点"
                allow-clear
                @update:value="updateField('loginLocation', $event)"
                @press-enter="handleSearch"
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
                  <span class="ml-8px">重置</span>
                </AButton>
                <AButton type="primary" ghost @click="handleSearch">
                  <template #icon>
                    <icon-ic-round-search class="align-sub text-icon" />
                  </template>
                  <span class="ml-8px">搜索</span>
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
