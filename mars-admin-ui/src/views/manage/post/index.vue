<template>
  <div ref="wrapperEl" class="flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <PostSearch v-model:model="searchParams" @reset="getData" @search="getData" />
    <ACard
      title="岗位列表"
      :bordered="false"
      :body-style="{ paddingTop: '0px' }"
      class="card-wrapper sm:flex-1-hidden"
    >
      <template #extra>
        <TableHeaderOperation
          v-model:columns="columnChecks"
          :disabled-delete="checkedRowKeys.length === 0"
          :loading="loading"
          @add="handleAdd"
          @delete="handleBatchDelete"
          @refresh="getData"
        />
      </template>
      {{ console.log('表格渲染时的data数据:', data) }}
      <ATable
        :columns="columns"
        :data-source="data"
        size="small"
        :scroll="{ x: 'max-content' }"
        :loading="loading"
        :row-selection="{
          selectedRowKeys: checkedRowKeys,
          onChange: keys => (checkedRowKeys.value = keys)
        }"
        row-key="id"
        :pagination="pagination"
        class="h-full"
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

<script setup lang="tsx">
import { reactive, ref, onMounted, computed } from 'vue';
import type { Ref } from 'vue';
import { Button, Popconfirm, Space, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { 
  fetchGetPostList, 
  fetchDeletePost, 
  fetchDeletePosts,
  fetchChangePostStatus,
} from '@/service/api/system-manage';
import { $t } from '@/locales';
import { useAppStore } from '@/store/modules/app';
import { enableStatusRecord } from '@/constants/business';
import PostOperateDrawer, { type OperateType } from './modules/post-operate-drawer.vue';
import PostSearch from './modules/post-search.vue';

defineOptions({
  name: 'ManagePost'
});

const appStore = useAppStore();

const wrapperEl = ref<HTMLElement>();

const { columns, columnChecks, data, getData, getDataByPage, loading, pagination, reloadColumns, searchParams } =
  useTable({
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
        key: 'index',
        title: $t('common.index'),
        width: 64,
        align: 'center',
        customRender: ({ index }) => index + 1
      },
      {
        key: 'postName',
        title: '岗位名称',
        align: 'center',
        minWidth: 120
      },
      {
        key: 'postCode',
        title: '岗位编码',
        align: 'center',
        minWidth: 120
      },
      {
        key: 'postSort',
        title: '显示顺序',
        align: 'center',
        width: 100
      },
      {
        key: 'status',
        title: '状态',
        align: 'center',
        width: 100,
        customRender: ({ record }) => {
          if (record.status === null || record.status === undefined) {
            return null;
          }
          
          const statusStr = String(record.status);
          console.log(`状态值类型: ${typeof record.status}, 值: ${statusStr}`);
          
          let statusKey = statusStr;
          
          if (statusStr === '1') {
            statusKey = '1';
          } else if (statusStr === '0') {
            statusKey = '2';
          }
          
          console.log(`状态key: ${statusKey}, 对应标签: ${$t(enableStatusRecord[statusKey])}`);
          
          const label = $t(enableStatusRecord[statusKey]);
          
          return <Tag color={statusStr === '1' ? 'success' : 'error'}>{label}</Tag>;
        }
      },
      {
        key: 'createTime',
        title: '创建时间',
        align: 'center',
        width: 160
      },
      {
        key: 'remark',
        title: '备注',
        align: 'center',
        ellipsis: true,
        minWidth: 120
      },
      {
        key: 'operate',
        title: $t('common.operate'),
        align: 'center',
        width: 180,
        fixed: 'right',
        customRender: ({ record }) => (
          <div class="flex-center gap-8px">
            <Button type="primary" ghost size="small" onClick={() => edit(record.id)}>
              {$t('common.edit')}
            </Button>
            <Button
              type={String(record.status) === '1' ? 'default' : 'primary'}
              size="small"
              onClick={() => toggleStatus(record.id, String(record.status))}
            >
              {String(record.status) === '1' ? '禁用' : '启用'}
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
  onDeleted
} = useTableOperate(data, getData);

async function handleBatchDelete() {
  const { error } = await fetchDeletePosts(checkedRowKeys.value);
  if (!error) {
    onBatchDeleted();
  }
}

async function handleDelete(id: number) {
  const { error } = await fetchDeletePost(id);
  if (!error) {
    onDeleted();
  }
}

async function toggleStatus(id: number, status: string) {
  const newStatus = status === '1' ? '0' : '1';
  console.log(`切换状态: ID=${id}, 当前状态=${status}, 新状态=${newStatus}`);
  
  const { error } = await fetchChangePostStatus(id, newStatus);
  if (!error) {
    window.$message?.success('状态修改成功');
    getData();
  }
}

function edit(id: number) {
  handleEdit(id);
}
</script>

<style scoped>
.card-wrapper {
  @apply flex-col-stretch gap-16px overflow-hidden;
}
</style> 