<script setup lang="tsx">
import { computed, reactive, ref } from 'vue';
import { Button, Modal, Popconfirm, Tag } from 'ant-design-vue';
import { fetchDeleteMenu, fetchDeleteMenus, fetchGetMenuTree, fetchRefreshMenuCache } from '@/service/api';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import TableHeaderOperation from '@/components/advanced/table-header-operation.vue';
import MenuOperateDrawer from './modules/menu-operate-drawer.vue';

const loading = ref(false);
const menuList = ref<Api.SystemManage.Menu[]>([]);
const checkedRowKeys = ref<number[]>([]);

// 弹窗相关
const modalVisible = ref(false);
const operateType = ref<'add' | 'edit' | 'addChild'>('add');
const editingData = ref<Api.SystemManage.Menu | null>(null);

// 获取菜单树列表
async function getMenuList() {
  loading.value = true;
  try {
    const { data, error } = await fetchGetMenuTree();
    if (!error && data) {
      originalMenuList.value = data;
      // 应用搜索过滤
      applySearch();
    }
  } finally {
    loading.value = false;
  }
}

// 应用搜索过滤
function applySearch() {
  if (!searchForm.menuName && searchForm.menuCode === '' && searchForm.status === null) {
    menuList.value = originalMenuList.value;
  } else {
    menuList.value = filterMenuTree(originalMenuList.value);
  }
}

// 递归过滤菜单树
function filterMenuTree(data: Api.SystemManage.Menu[]): Api.SystemManage.Menu[] {
  const result: Api.SystemManage.Menu[] = [];

  data.forEach(menu => {
    // 检查当前菜单是否匹配
    const nameMatch = !searchForm.menuName || menu.menuName?.toLowerCase().includes(searchForm.menuName.toLowerCase());

    const codeMatch = !searchForm.menuCode || menu.menuCode?.toLowerCase().includes(searchForm.menuCode.toLowerCase());

    const statusMatch = searchForm.status === null || menu.status?.toString() === searchForm.status;

    const currentMatch = nameMatch && codeMatch && statusMatch;

    // 递归过滤子菜单
    const filteredChildren = menu.children ? filterMenuTree(menu.children) : [];

    // 如果当前菜单匹配或有匹配的子菜单，则包含此菜单
    if (currentMatch || filteredChildren.length > 0) {
      const filteredMenu = { ...menu };
      if (filteredChildren.length > 0) {
        filteredMenu.children = filteredChildren;
      }
      result.push(filteredMenu);
    }
  });

  return result;
}

// 新增菜单
function handleAdd(parentId?: number) {
  if (parentId) {
    // 新增子菜单
    operateType.value = 'addChild';
    editingData.value = { id: parentId } as Api.SystemManage.Menu;
  } else {
    // 新增根菜单
    operateType.value = 'add';
    editingData.value = null;
  }
  modalVisible.value = true;
}

// 编辑菜单
function handleEdit(record: Api.SystemManage.Menu) {
  operateType.value = 'edit';
  editingData.value = record;
  modalVisible.value = true;
}

// 批量删除菜单
async function handleBatchDelete() {
  if (checkedRowKeys.value.length === 0) {
    window.$message?.warning('请选择要删除的菜单');
    return;
  }

  // 使用ant-design-vue的Modal.confirm替代
  Modal.confirm({
    title: '批量删除菜单',
    content: `确定要删除选中的 ${checkedRowKeys.value.length} 个菜单吗？删除后无法恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        const { error, message } = await fetchDeleteMenus(checkedRowKeys.value);
        if (!error) {
          window.$message?.success('批量删除成功');
          checkedRowKeys.value = [];
          getMenuList();
        } else {
          // 显示后端返回的错误信息
          window.$message?.error(message || '批量删除失败');
        }
      } catch (error) {
        window.$message?.error('批量删除失败');
      }
    }
  });
}

// 删除菜单
async function handleDelete(id: number) {
  const { error, message } = await fetchDeleteMenu(id);
  if (!error) {
    window.$message?.success('删除成功');
    getMenuList();
  } else {
    // 显示后端返回的错误信息
    window.$message?.error(message || '删除失败');
  }
}

// 刷新菜单缓存
async function refreshMenuCache() {
  try {
    const { error } = await fetchRefreshMenuCache();
    if (!error) {
      window.$message?.success('刷新菜单缓存成功');
    }
  } catch (err) {
    console.error('刷新菜单缓存失败:', err);
    window.$message?.error('刷新缓存失败，请重试');
  }
}

// 渲染图标
function renderIcon(icon: string) {
  if (!icon) return '-';

  // 如果包含冒号，说明是 iconify 格式的图标 (如: mdi:home)
  if (icon.includes(':')) {
    return <SvgIcon icon={icon} class="text-16px" />;
  }

  // 否则尝试作为 mdi 图标处理
  return <SvgIcon icon={`mdi:${icon}`} class="text-16px" />;
}

// 表格列配置
const columns = [
  {
    title: '菜单名称',
    dataIndex: 'menuName',
    key: 'menuName',
    width: 200
  },
  {
    title: '图标',
    dataIndex: 'icon',
    key: 'icon',
    width: 60,
    align: 'center',
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => {
      return renderIcon(record.icon);
    }
  },
  {
    title: '菜单类型',
    dataIndex: 'menuType',
    key: 'menuType',
    width: 100,
    align: 'center',
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => {
      const typeMap = {
        M: { label: '目录', color: 'blue' },
        C: { label: '菜单', color: 'green' },
        F: { label: '按钮', color: 'orange' }
      };
      const type = typeMap[record.menuType];
      return <Tag color={type.color}>{type.label}</Tag>;
    }
  },
  {
    title: '路由名称',
    dataIndex: 'routeName',
    key: 'routeName',
    width: 120,
    align: 'center'
  },
  {
    title: '路由地址',
    dataIndex: 'routePath',
    key: 'routePath',
    width: 150,
    align: 'center'
  },
  {
    title: '组件路径',
    dataIndex: 'component',
    key: 'component',
    width: 200,
    align: 'center'
  },
  {
    title: '权限标识',
    dataIndex: 'perms',
    key: 'perms',
    width: 150,
    align: 'center'
  },
  {
    title: '隐藏菜单',
    dataIndex: 'visible',
    key: 'visible',
    width: 80,
    align: 'center',
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => {
      return <Tag color={record.visible === 0 ? 'warning' : 'success'}>{record.visible === 0 ? '是' : '否'}</Tag>;
    }
  },
  {
    title: '排序',
    dataIndex: 'orderNum',
    key: 'orderNum',
    width: 80,
    align: 'center'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 80,
    align: 'center',
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => {
      return <Tag color={record.status === 1 ? 'success' : 'error'}>{record.status === 1 ? '启用' : '禁用'}</Tag>;
    }
  },
  {
    title: '操作',
    key: 'operate',
    width: 200,
    align: 'center',
    fixed: 'right',
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => (
      <div class="flex-center gap-8px">
        <Button type="primary" ghost size="small" onClick={() => handleEdit(record)}>
          编辑
        </Button>
        <Button size="small" onClick={() => handleAdd(record.id)}>
          新增子菜单
        </Button>
        <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.id)}>
          <Button danger size="small">
            删除
          </Button>
        </Popconfirm>
      </div>
    )
  }
];

// 表格行选择配置
const rowSelection = computed(() => {
  return {
    selectedRowKeys: checkedRowKeys.value,
    onChange: (selectedRowKeys: number[]) => {
      checkedRowKeys.value = selectedRowKeys;
    },
    columnWidth: 48
  };
});

// 搜索表单
const searchForm = reactive({
  menuName: '',
  menuCode: '',
  status: null
});

// 折叠状态
const collapsed = ref(false);

// 原始菜单数据
const originalMenuList = ref<Api.SystemManage.Menu[]>([]);

// 统计菜单数量（递归统计）
function countMenus(menus: Api.SystemManage.Menu[]): number {
  let count = 0;
  menus.forEach(menu => {
    count++;
    if (menu.children && menu.children.length > 0) {
      count += countMenus(menu.children);
    }
  });
  return count;
}

function handleSearch() {
  // 应用搜索过滤
  applySearch();
}

function handleReset() {
  Object.assign(searchForm, {
    menuName: '',
    menuCode: '',
    status: null
  });
  getMenuList();
}

function toggleCollapsed() {
  collapsed.value = !collapsed.value;
}

// 提交后刷新列表
function handleSubmitted() {
  modalVisible.value = false;
  getMenuList();
}

// 初始化
getMenuList();
</script>

<template>
  <div class="h-full flex-col-stretch gap-16px overflow-hidden">
    <!-- 搜索区域 -->
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
          :model="searchForm"
          :label-col="{
            span: 5,
            md: 7
          }"
        >
          <ARow :gutter="[16, 16]" wrap>
            <ACol :span="24" :md="12" :lg="8">
              <AFormItem label="菜单名称" name="menuName" class="m-0">
                <AInput v-model:value="searchForm.menuName" placeholder="请输入菜单名称" @input="handleSearch" />
              </AFormItem>
            </ACol>
            <ACol :span="24" :md="12" :lg="8">
              <AFormItem label="菜单编码" name="menuCode" class="m-0">
                <AInput v-model:value="searchForm.menuCode" placeholder="请输入菜单编码" @input="handleSearch" />
              </AFormItem>
            </ACol>
            <ACol :span="24" :md="12" :lg="8">
              <AFormItem label="状态" name="status" class="m-0">
                <ASelect
                  v-model:value="searchForm.status"
                  placeholder="请选择状态"
                  :options="[
                    { label: '启用', value: '1' },
                    { label: '禁用', value: '0' }
                  ]"
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
      </div>
    </ACard>

    <!-- 表格区域 -->
    <ACard
      title="菜单列表"
      :bordered="false"
      :body-style="{ paddingTop: '0px', height: 'auto', maxHeight: 'calc(100vh - 280px)', overflow: 'hidden' }"
      class="flex-1-hidden card-wrapper"
    >
      <template #extra>
        <TableHeaderOperation
          :loading="loading"
          :disabled-delete="checkedRowKeys.length === 0"
          @add="handleAdd"
          @delete="handleBatchDelete"
          @refresh="getMenuList"
        >
          <template #prefix>
            <span
              v-if="searchForm.menuName || searchForm.menuCode || searchForm.status !== null"
              class="mr-12px text-gray-500"
            >
              找到 {{ countMenus(menuList) }} 条记录
            </span>
          </template>
          <template #suffix>
            <AButton size="small" type="primary" ghost @click="refreshMenuCache">
              <template #icon>
                <icon-mdi-cached class="align-sub text-icon" />
              </template>
              <span class="ml-8px">刷新缓存</span>
            </AButton>
          </template>
        </TableHeaderOperation>
      </template>

      <ATable
        :columns="columns"
        :data-source="menuList"
        :loading="loading"
        row-key="id"
        :pagination="false"
        size="small"
        :scroll="{ x: 'max-content', y: 'calc(100vh - 380px)' }"
        :default-expand-all-rows="true"
        :row-selection="rowSelection"
        class="menu-table"
      />
    </ACard>

    <!-- 菜单操作弹窗 -->
    <MenuOperateDrawer
      v-model:visible="modalVisible"
      :operate-type="operateType"
      :row-data="editingData"
      @submitted="handleSubmitted"
    />
  </div>
</template>

<style scoped>
:deep(.ant-table-tbody > tr > td) {
  padding: 8px;
}

.flex-1-hidden {
  flex: 1;
  overflow: hidden;
}

:deep(.ant-card-body) {
  height: 100%;
  overflow: hidden;
}

:deep(.ant-table-wrapper) {
  height: 100%;
}

:deep(.ant-spin-nested-loading) {
  height: 100%;
}

:deep(.ant-spin-container) {
  height: 100%;
  display: flex;
  flex-direction: column;
}

:deep(.ant-table) {
  flex: 1;
}

/* 图标居中对齐 */
:deep(.ant-table-tbody .ant-table-cell) {
  vertical-align: middle;
}

/* 菜单表格样式优化 */
.menu-table {
  height: 100%;
}

.menu-table :deep(.ant-table-container) {
  height: 100%;
}

.menu-table :deep(.ant-table-body) {
  scrollbar-width: thin;
  scrollbar-color: rgba(59, 130, 246, 0.3) transparent;
  max-height: calc(100vh - 380px);
  overflow-y: auto;
}

.menu-table :deep(.ant-table-body::-webkit-scrollbar) {
  width: 8px;
  height: 8px;
}

.menu-table :deep(.ant-table-body::-webkit-scrollbar-thumb) {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.3), rgba(99, 102, 241, 0.3));
  border-radius: 4px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.menu-table :deep(.ant-table-body::-webkit-scrollbar-thumb:hover) {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.6), rgba(99, 102, 241, 0.6));
  transform: scale(1.1);
}

.menu-table :deep(.ant-table-body::-webkit-scrollbar-track) {
  background: rgba(248, 250, 252, 0.8);
  border-radius: 4px;
  border: 1px solid rgba(226, 232, 240, 0.3);
}

/* 确保表格可以正确滚动 */
.menu-table :deep(.ant-spin-container) {
  height: 100%;
}

.menu-table :deep(.ant-table-wrapper) {
  height: 100%;
}

/* 响应式滚动高度 */
@media (max-height: 800px) {
  .menu-table :deep(.ant-table-body) {
    max-height: calc(100vh - 320px);
  }
}

@media (max-height: 600px) {
  .menu-table :deep(.ant-table-body) {
    max-height: calc(100vh - 280px);
  }
}

/* 小屏幕设备优化 */
@media (max-width: 768px) {
  .menu-table :deep(.ant-table-body) {
    max-height: calc(100vh - 250px);
  }
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

.text-icon {
  font-size: 16px;
}
</style>
