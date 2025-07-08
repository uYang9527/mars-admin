<script setup lang="tsx">
import { ref } from 'vue';
import { Button, Modal, Popconfirm, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import {
  fetchChangePostStatus,
  fetchDeletePost,
  fetchDeletePosts,
  fetchGetPostList,
  fetchRefreshPostCache
} from '@/service/api/system-manage';
import { $t } from '@/locales';
import { enableStatusRecord } from '@/constants/business';
import PostOperateDrawer from './modules/post-operate-drawer.vue';
import PostSearch from './modules/post-search.vue';

defineOptions({
  name: 'ManagePost'
});

const wrapperEl = ref<HTMLElement>();

const { columns, columnChecks, data, getData, loading, mobilePagination, updatePagination, searchParams } = useTable({
  apiFn: fetchGetPostList,
  apiParams: {
    current: 1,
    size: 10,
    postName: null,
    postCode: null,
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
      title: $t('common.index'),
      align: 'center',
      customRender: ({ index }) => index + 1
    },
    {
      key: 'postName',
      title: '岗位名称',
      align: 'center',
      dataIndex: 'postName'
    },
    {
      key: 'postCode',
      title: '岗位编码',
      align: 'center',
      dataIndex: 'postCode'
    },
    {
      key: 'postSort',
      title: '显示顺序',
      align: 'center',
      dataIndex: 'postSort'
    },
    {
      key: 'status',
      title: '状态',
      align: 'center',
      dataIndex: 'status',
      customRender: ({ record }) => {
        if (record.status === null) {
          return null;
        }

        const label = $t(enableStatusRecord[record.status]);

        return <Tag color={record.status === 1 ? 'success' : 'error'}>{label}</Tag>;
      }
    },
    {
      key: 'createTime',
      title: '创建时间',
      align: 'center',
      dataIndex: 'createTime'
    },
    {
      key: 'remark',
      title: '备注',
      align: 'center',
      ellipsis: true,
      dataIndex: 'remark'
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      fixed: 'right',
      customRender: ({ record }) => (
        <div class="flex-center gap-8px">
          <Button type="primary" ghost size="small" onClick={() => edit(record.id)}>
            {$t('common.edit')}
          </Button>
          <Button
            type={record.status === 1 ? 'default' : 'primary'}
            size="small"
            onClick={() => toggleStatus(record.id, record.status)}
          >
            {record.status === 1 ? '禁用' : '启用'}
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
  handleEdit,
  checkedRowKeys,
  onBatchDeleted,
  onDeleted,
  rowSelection
} = useTableOperate(data, getData);

async function handleBatchDelete() {
  if (checkedRowKeys.value.length === 0) {
    window.$message?.warning('请选择要删除的岗位');
    return;
  }

  Modal.confirm({
    title: '批量删除岗位',
    content: `确定要删除选中的 ${checkedRowKeys.value.length} 个岗位吗？删除后无法恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        const { error } = await fetchDeletePosts(checkedRowKeys.value);
        if (!error) {
          onBatchDeleted();
        } else {
          window.$message?.error(`批量删除失败: ${error}`);
        }
      } catch (e) {
        window.$message?.error(`操作失败: ${e.message}`);
      }
    }
  });
}

async function handleDelete(id: number) {
  const { error } = await fetchDeletePost(id);
  if (!error) {
    onDeleted();
  }
}

async function toggleStatus(id: number, status: number) {
  const newStatus = status === 1 ? 0 : 1;
  const { error } = await fetchChangePostStatus(id, newStatus);
  if (!error) {
    window.$message?.success('状态修改成功');
    getData();
  }
}

function edit(id: number) {
  handleEdit(id);
}

// 刷新岗位缓存
async function refreshPostCache() {
  try {
    const { error } = await fetchRefreshPostCache();
    if (!error) {
      window.$message?.success('刷新岗位缓存成功');
    }
  } catch (err) {
    console.error('刷新岗位缓存失败:', err);
    window.$message?.error('刷新缓存失败，请重试');
  }
}

// 处理表格变化事件（分页、排序、筛选）
function handleTableChange(paginationInfo: any) {
  // 处理分页变化（包括页码和分页大小）
  if (paginationInfo) {
    const { current, pageSize } = paginationInfo;

    // 处理分页大小变化
    updatePagination({
      current: current || 1,
      pageSize: pageSize || mobilePagination.value.pageSize
    });
    // 更新分页参数
    searchParams.current = current || 1;
    searchParams.size = pageSize || mobilePagination.value.pageSize;

    // 刷新数据
    getData();
  }
}
</script>

<template>
  <div ref="wrapperEl" class="flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <PostSearch v-model:model="searchParams" @reset="getData" @search="getData" />
    <ACard title="岗位列表" :bordered="false" :body-style="{ paddingTop: '0px' }" class="sm:flex-1-hidden card-wrapper">
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
            <AButton size="small" type="primary" ghost @click="refreshPostCache">
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
        :scroll="{ x: 'max-content' }"
        :loading="loading"
        :row-selection="rowSelection"
        row-key="id"
        :pagination="mobilePagination"
        class="h-full"
        @change="handleTableChange"
      />
    </ACard>
    <PostOperateDrawer
      v-model:visible="drawerVisible"
      :operate-type="operateType"
      :row-data="editingData"
      @submitted="getData"
    />
  </div>
</template>

<style scoped>
.card-wrapper {
  @apply flex-col-stretch gap-16px overflow-hidden;
}
</style>
