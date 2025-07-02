<script setup lang="ts">
import { computed, shallowRef, watch } from 'vue';
import type { DataNode } from 'ant-design-vue/es/tree';
import { $t } from '@/locales';
import { fetchAssignRoleMenus, fetchGetMenusByRoleId, fetchGetMenusList } from '@/service/api';

defineOptions({
  name: 'ButtonAuthModal'
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

const title = computed(() => $t('common.edit') + $t('page.manage.role.buttonAuth'));

const tree = shallowRef<DataNode[]>([]);

// 获取所有按钮权限
async function getAllButtons() {
  const { error, data } = await fetchGetMenusList();

  if (!error && data) {
    // 过滤出按钮类型的菜单
    const buttons = data.filter(item => item.menuType === 'F');

    // 转换为树形结构
    tree.value = buttons.map(button => ({
      key: button.id,
      title: button.menuName || `按钮-${button.id}`,
      code: button.perms || button.buttonCode
    }));
  }
}

const checks = shallowRef<number[]>([]);

// 获取角色已有按钮权限
async function getChecks() {
  if (!props.roleId) return;

  const { error, data } = await fetchGetMenusByRoleId(props.roleId);

  if (!error && data) {
    // 过滤出按钮类型的菜单
    const buttons = data.filter(item => item.menuType === 'F');
    // 提取按钮ID
    checks.value = buttons.map(item => item.id);
  }
}

// 提交按钮权限
async function handleSubmit() {
  if (!props.roleId) return;

  // 获取当前角色的所有菜单权限
  const { error: fetchError, data: roleMenus } = await fetchGetMenusByRoleId(props.roleId);

  if (fetchError || !roleMenus) {
    window.$message?.error('获取角色菜单权限失败');
    return;
  }

  // 过滤出非按钮类型的菜单ID
  const menuIds = roleMenus.filter(item => item.menuType !== 'F').map(item => item.id);

  // 合并菜单ID和选中的按钮ID
  const allIds = [...menuIds, ...checks.value];

  // 提交授权请求
  const { error } = await fetchAssignRoleMenus(props.roleId, allIds);

  if (!error) {
    window.$message?.success($t('common.modifySuccess'));
    closeModal();
  }
}

function init() {
  getAllButtons();
  getChecks();
}

// 监听弹窗显示状态
watch(visible, val => {
  if (val) {
    init();
  }
});
</script>

<template>
  <AModal v-model:open="visible" :title="title" class="w-480px">
    <div class="pb-12px">
      <span class="text-secondary">{{ $t('page.manage.role.selectButtonTip') }}</span>
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
