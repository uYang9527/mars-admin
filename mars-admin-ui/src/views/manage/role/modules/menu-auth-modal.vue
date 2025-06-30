<script setup lang="ts">
import { computed, shallowRef, watch } from 'vue';
import type { DataNode } from 'ant-design-vue/es/tree';
import { $t } from '@/locales';
import { fetchGetMenuTree, fetchGetMenusByRoleId, fetchAssignRoleMenus } from '@/service/api';

defineOptions({
  name: 'MenuAuthModal'
});

interface Props {
  /** the roleId */
  roleId: number;
}

const props = defineProps<Props>();

const visible = defineModel<boolean>('visible', {
  default: false
});

function closeModal() {
  visible.value = false;
}

const title = computed(() => $t('common.edit') + $t('page.manage.role.menuAuth'));

const tree = shallowRef<DataNode[]>([]);

async function getTree() {
  const { error, data } = await fetchGetMenuTree();

  if (!error && data) {
    tree.value = recursiveTransform(data);
  }
}

function recursiveTransform(data: Api.SystemManage.MenuTree[]): DataNode[] {
  return data.map(item => {
    // 使用菜单名称作为标题显示
    const { id: key, label, menuName } = item;
    const title = menuName || label;

    if (item.children && item.children.length > 0) {
      return {
        key,
        title,
        children: recursiveTransform(item.children)
      };
    }

    return {
      key,
      title
    };
  });
}

const checks = shallowRef<number[]>([]);

async function getChecks() {
  if (!props.roleId) return;
  
  const { error, data } = await fetchGetMenusByRoleId(props.roleId);
  
  if (!error && data) {
    // 提取菜单ID作为选中项
    checks.value = data.map(item => item.id);
  }
}

async function handleSubmit() {
  if (!props.roleId) return;
  
  // 提交授权请求
  const { error } = await fetchAssignRoleMenus(props.roleId, checks.value);

  if (!error) {
    window.$message?.success($t('common.modifySuccess'));
    closeModal();
  }
}

async function init() {
  await getTree();
  await getChecks();
}

watch(visible, val => {
  if (val) {
    init();
  }
});
</script>

<template>
  <AModal v-model:open="visible" :title="title" class="w-480px">
    <div class="pb-12px">
      <span class="text-secondary">{{ $t('page.manage.role.selectMenuTip') }}</span>
    </div>
    <ATree v-model:checked-keys="checks" :tree-data="tree" checkable :height="280" class="h-280px" />
    <template #footer>
      <AButton size="small" class="mt-16px" @click="closeModal">
        {{ $t('common.cancel') }}
      </AButton>
      <AButton type="primary" size="small" class="mt-16px" @click="handleSubmit">
        {{ $t('common.confirm') }}
      </AButton>
    </template>
  </AModal>
</template>

<style scoped></style>
