<script setup lang="ts">
import { computed, shallowRef, watch } from 'vue';
import type { DataNode } from 'ant-design-vue/es/tree';
import { $t } from '@/locales';
import { fetchAssignRoleDepts, fetchGetDeptTree, fetchGetDeptsByRoleId } from '@/service/api';

defineOptions({
  name: 'DataAuthModal'
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

const title = computed(() => $t('common.edit') + $t('page.manage.role.dataAuth'));

const tree = shallowRef<DataNode[]>([]);

// 获取部门树
async function getTree() {
  const { error, data } = await fetchGetDeptTree();

  if (!error) {
    tree.value = recursiveTransform(data);
  }
}

// 递归转换树结构
function recursiveTransform(data: Api.SystemManage.DeptTree[]): DataNode[] {
  return data.map(item => {
    const { id: key, label } = item;

    if (item.children) {
      return {
        key,
        title: label,
        children: recursiveTransform(item.children)
      };
    }

    return {
      key,
      title: label
    };
  });
}

const checks = shallowRef<number[]>([]);

// 获取角色已分配的部门
async function getChecks() {
  if (!props.roleId) return;

  const { error, data } = await fetchGetDeptsByRoleId(props.roleId);

  if (!error && data) {
    // 提取部门ID作为选中项
    checks.value = data.map(item => item.id);
  }
}

// 提交数据权限
async function handleSubmit() {
  if (!props.roleId) return;

  // 提交授权请求
  const { error } = await fetchAssignRoleDepts(props.roleId, checks.value);

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
      <span class="text-secondary">{{ $t('page.manage.role.selectDataTip') }}</span>
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
