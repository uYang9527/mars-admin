<script setup lang="tsx">
import { ref } from 'vue';
import { Button, Modal, Popconfirm, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { enableStatusRecord } from '@/constants/business';
import TableHeaderOperation from '@/components/advanced/table-header-operation.vue';
import {
  fetchDeleteDept,
  fetchDeleteDepts,
  fetchGetDeptById,
  fetchGetDeptTree,
  fetchRefreshDeptCache
} from '@/service/api';
import DeptOperateDrawer from './modules/dept-operate-drawer.vue';

defineOptions({
  name: 'ManageDept'
});

// 折叠状态
const collapsed = ref(false);

// 使用树形数据展示部门，支持搜索
const { columns, columnChecks, data, loading, getData, searchParams } = useTable({
  apiFn: params => {
    // 使用新的tree接口，支持搜索参数
    return fetchGetDeptTree(params);
  },
  apiParams: {
    deptName: null,
    status: null
  },
  columns: () => [
    {
      key: 'selection',
      title: '',
      type: 'selection',
      align: 'center',
      width: 48,
      fixed: 'left'
    },
    {
      key: 'index',
      dataIndex: 'index',
      title: $t('common.index'),
      align: 'center'
    },
    {
      key: 'deptName',
      dataIndex: 'label',
      title: '部门名称',
      align: 'left',
      customRender: ({ record }) => {
        return record.label;
      }
    },
    {
      key: 'deptCode',
      dataIndex: 'deptCode',
      title: '部门编码',
      align: 'center'
    },
    {
      key: 'orderNum',
      dataIndex: 'orderNum',
      title: '排序',
      align: 'center'
    },
    {
      key: 'leader',
      dataIndex: 'leader',
      title: '负责人',
      align: 'center'
    },
    {
      key: 'phone',
      dataIndex: 'phone',
      title: '联系电话',
      align: 'center'
    },
    {
      key: 'email',
      dataIndex: 'email',
      title: '邮箱',
      align: 'center'
    },
    {
      dataIndex: 'status',
      title: $t('common.status'),
      align: 'center',
      customRender: ({ record }) => {
        if (record.status === null) {
          return null;
        }

        const status = record.status as Api.Common.EnableStatus;
        const label = $t(enableStatusRecord[status]);
        const color = status === 1 ? 'success' : 'warning';

        return <Tag color={color}>{label}</Tag>;
      }
    },
    {
      dataIndex: 'createTime',
      title: $t('common.createTime'),
      align: 'center'
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      customRender: ({ record }) => (
        <div class="flex-center gap-8px">
          <Button type="primary" ghost size="small" onClick={() => edit(record.id)}>
            {$t('common.edit')}
          </Button>
          <Popconfirm title={$t('common.confirmDelete')} onConfirm={() => handleDelete(record.id)}>
            <Button danger size="small">
              {$t('common.delete')}
            </Button>
          </Popconfirm>
        </div>
      )
    }
  ]
});

const {
  drawerVisible,
  operateType,
  editingData,
  handleAdd,
  checkedRowKeys,
  onBatchDeleted,
  onDeleted,
  handleEdit,
  rowSelection
} = useTableOperate(data, getData);

async function handleBatchDelete() {
  if (checkedRowKeys.value.length === 0) {
    window.$message?.warning('请选择要删除的数据');
    return;
  }

  // 使用ant-design-vue的Modal.confirm替代
  Modal.confirm({
    title: '批量删除部门',
    content: `确定要删除选中的 ${checkedRowKeys.value.length} 个部门吗？删除后无法恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        const { error, message } = await fetchDeleteDepts(checkedRowKeys.value);
        if (!error) {
          window.$message?.success('批量删除成功');
          onBatchDeleted();
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

async function handleDelete(id: number) {
  try {
    const { error, message } = await fetchDeleteDept(id);
    if (!error) {
      onDeleted();
    } else {
      // 显示后端返回的错误信息
      window.$message?.error(message || '删除失败');
    }
  } catch (error) {
    window.$message?.error('删除失败');
  }
}

// 递归查找树形数据中的记录
function findRecordInTree(treeData: any[], id: number): any {
  for (const item of treeData) {
    if (item.id === id) {
      return item;
    }
    if (item.children && item.children.length > 0) {
      const found = findRecordInTree(item.children, id);
      if (found) {
        return found;
      }
    }
  }
  return null;
}

async function edit(id: number) {
  try {
    // 在树形数据中查找记录
    const record = findRecordInTree(data.value, id);
    
    if (record) {
      // 直接设置editingData和operateType，然后打开抽屉
      editingData.value = record;
      operateType.value = 'edit';
      drawerVisible.value = true;
    } else {
      window.$message?.error('未找到对应的部门记录');
    }
  } catch (error) {
    console.error('获取部门信息错误:', error);
    window.$message?.error('获取部门信息失败');
  }
}

function handleSearch() {
  getData();
}

// 刷新部门缓存
async function refreshDeptCache() {
  try {
    const { error } = await fetchRefreshDeptCache();
    if (!error) {
      window.$message?.success('刷新部门缓存成功');
    }
  } catch (err) {
    console.error('刷新部门缓存失败:', err);
    window.$message?.error('刷新缓存失败，请重试');
  }
}

function handleReset() {
  Object.assign(searchParams, {
    deptName: null,
    status: null
  });
  getData();
}

function toggleCollapsed() {
  collapsed.value = !collapsed.value;
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
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
          :model="searchParams"
          :label-col="{
            span: 5,
            md: 7
          }"
        >
          <ARow :gutter="[16, 16]" wrap>
            <ACol :span="24" :md="12" :lg="8">
              <AFormItem label="部门名称" name="deptName" class="m-0">
                <AInput v-model:value="searchParams.deptName" placeholder="请输入部门名称" @input="handleSearch" />
              </AFormItem>
            </ACol>
            <ACol :span="24" :md="12" :lg="8">
              <AFormItem label="状态" name="status" class="m-0">
                <ASelect
                  v-model:value="searchParams.status"
                  placeholder="请选择状态"
                  :options="[
                    { label: '启用', value: 1 },
                    { label: '禁用', value: 0 }
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

    <ACard title="部门管理" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
      <template #extra>
        <TableHeaderOperation
          v-model:columns="columnChecks"
          :disabled-delete="checkedRowKeys.length === 0"
          :loading="loading"
          @add="handleAdd"
          @delete="handleBatchDelete"
          @refresh="getData"
        >
          <template #suffix>
            <AButton size="small" type="primary" ghost @click="refreshDeptCache">
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
        :data-source="data"
        size="small"
        :scroll="{ x: 962 }"
        :loading="loading"
        :row-key="row => row.id"
        :row-selection="rowSelection"
        :pagination="false"
        children-column-name="children"
        :default-expand-all-rows="true"
        class="sm:h-full"
      />
    </ACard>

    <!-- 部门操作抽屉 -->
    <DeptOperateDrawer
      v-model:visible="drawerVisible"
      :operate-type="operateType"
      :row-data="editingData"
      @submitted="getData"
    />
  </div>
</template>

<style scoped>
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
