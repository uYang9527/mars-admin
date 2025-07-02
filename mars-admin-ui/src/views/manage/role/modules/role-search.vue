<script setup lang="ts">
import { ref } from 'vue';
import { $t } from '@/locales';
import { enableStatusOptions } from '@/constants/business';

defineOptions({
  name: 'RoleSearch'
});

interface Emits {
  (e: 'reset'): void;
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const model = defineModel<Api.SystemManage.RoleSearchParams>('model', { required: true });

// 折叠状态
const collapsed = ref(false);

function reset() {
  emit('reset');
}

function search() {
  emit('search');
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
      <AForm
        :model="model"
        :label-col="{
          span: 5,
          md: 7
        }"
      >
        <ARow :gutter="[16, 16]" wrap>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem :label="$t('page.manage.role.roleName')" name="roleName" class="m-0">
              <AInput
                v-model:value="model.roleName"
                :placeholder="$t('page.manage.role.form.roleName')"
                @input="search"
              />
            </AFormItem>
          </ACol>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem :label="$t('page.manage.role.roleCode')" name="roleCode" class="m-0">
              <AInput
                v-model:value="model.roleCode"
                :placeholder="$t('page.manage.role.form.roleCode')"
                @press-enter="search"
              />
            </AFormItem>
          </ACol>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem :label="$t('page.manage.role.roleKey')" name="roleKey" class="m-0">
              <AInput
                v-model:value="model.roleKey"
                :placeholder="$t('page.manage.role.form.roleKey')"
                @press-enter="search"
              />
            </AFormItem>
          </ACol>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem :label="$t('page.manage.role.roleStatus')" name="status" class="m-0">
              <ASelect
                v-model:value="model.status"
                :placeholder="$t('page.manage.role.form.roleStatus')"
                clearable
                @change="search"
              >
                <ASelectOption v-for="option in enableStatusOptions" :key="option.value" :value="option.value">
                  {{ $t(option.label) }}
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
