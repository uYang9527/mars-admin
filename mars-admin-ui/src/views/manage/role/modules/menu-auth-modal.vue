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
// 存储菜单ID到父ID的映射关系
const menuParentMap = shallowRef<Record<string | number, string | number>>({});

async function getTree() {
  const { error, data } = await fetchGetMenuTree();

  if (!error && data) {
    // 构建菜单ID到父ID的映射关系
    buildParentMap(data);
    tree.value = recursiveTransform(data);
  }
}

// 构建菜单ID到父ID的映射关系
function buildParentMap(data: any[], parentId?: string | number) {
  data.forEach(item => {
    if (parentId !== undefined) {
      menuParentMap.value[item.id] = parentId;
    }
    
    if (item.children && item.children.length > 0) {
      buildParentMap(item.children, item.id);
    }
  });
}

function recursiveTransform(data: any[]): DataNode[] {
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

const checks = shallowRef<(string | number)[]>([]);
const expandedKeys = shallowRef<(string | number)[]>([]);
const autoExpandParent = shallowRef(true);

async function getChecks() {
  if (!props.roleId) return;
  
  const { error, data } = await fetchGetMenusByRoleId(props.roleId);
  
  if (!error && data) {
    // 提取菜单ID作为选中项
    checks.value = data.map(item => item.id);
    // 设置展开的节点
    expandedKeys.value = [...checks.value];
  }
}

// 处理展开/折叠事件
function handleExpand(keys: (string | number)[]) {
  expandedKeys.value = keys;
  autoExpandParent.value = false;
}

// 获取所有父节点ID
function getAllParentIds(ids: (string | number)[]): (string | number)[] {
  const parentIds: (string | number)[] = [];
  
  function collectParents(id: string | number) {
    const parentId = menuParentMap.value[id];
    if (parentId !== undefined && !parentIds.includes(parentId) && !ids.includes(parentId)) {
      parentIds.push(parentId);
      collectParents(parentId);
    }
  }
  
  ids.forEach(id => collectParents(id));
  return parentIds;
}

async function handleSubmit() {
  if (!props.roleId) return;
  
  // 获取选中节点的所有父节点ID
  const parentIds = getAllParentIds(checks.value);
  // 合并选中节点ID和父节点ID
  const allMenuIds = [...checks.value, ...parentIds];
  // 去重并转换为数字类型
  const uniqueMenuIds = [...new Set(allMenuIds)].map(id => Number(id));
  
  // 提交授权请求
  const { error } = await fetchAssignRoleMenus(props.roleId, uniqueMenuIds);

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
  <AModal :open="visible" :title="title" class="w-480px" @update:open="val => visible = val">
    <div class="pb-12px">
      <span class="text-secondary">{{ $t('page.manage.role.selectMenuTip') }}</span>
    </div>
    <ATree 
      v-model:checked-keys="checks" 
      v-model:expanded-keys="expandedKeys"
      :auto-expand-parent="autoExpandParent"
      :tree-data="tree" 
      checkable 
      :height="280" 
      class="h-280px"
      @expand="handleExpand"
    />
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
