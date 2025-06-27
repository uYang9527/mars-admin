<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <!-- 搜索区域 -->
    <ACard :title="$t('common.search')" :bordered="false" class="card-wrapper">
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
              <AInput v-model:value="searchParams.deptName" placeholder="请输入部门名称" />
            </AFormItem>
          </ACol>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem label="状态" name="status" class="m-0">
              <ASelect
                v-model:value="searchParams.status"
                placeholder="请选择状态"
                :options="[
                  { label: '启用', value: '1' },
                  { label: '禁用', value: '0' }
                ]"
                clearable
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
        />
      </template>
      <ATable
        v-model:selectedRowKeys="checkedRowKeys"
        :columns="columns"
        :data-source="data"
        size="small"
        :scroll="{ x: 962 }"
        :loading="loading"
        :row-key="row => row.id"
        :pagination="mobilePagination"
        class="sm:h-full"
      />
    </ACard>
  </div>
</template>

<script setup lang="tsx">
import { ref } from 'vue';
import { Button, Popconfirm, Tag } from 'ant-design-vue';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { enableStatusRecord } from '@/constants/business';
import TableHeaderOperation from '@/components/advanced/table-header-operation.vue';

const appStore = useAppStore();

defineOptions({
  name: 'ManageDept'
});

const wrapperEl = ref<HTMLElement>();

// 模拟数据和接口
const mockData = ref([
  {
    id: 1,
    deptName: '总公司',
    deptCode: 'HEAD_OFFICE',
    orderNum: 0,
    leader: 'admin',
    phone: '15888888888',
    email: 'admin@example.com',
    status: '1',
    createTime: '2024-01-01 00:00:00'
  }
]);

const { columns, columnChecks, data, loading, getData, mobilePagination, searchParams } = useTable({
  apiFn: async () => ({ data: mockData.value, error: null }),
  apiParams: {
    current: 1,
    size: 10,
    deptName: null,
    status: null
  },
  columns: () => [
    {
      type: 'selection',
      align: 'center',
      width: 48
    },
    {
      key: 'index',
      title: $t('common.index'),
      align: 'center',
      width: 64
    },
    {
      dataIndex: 'deptName',
      title: '部门名称',
      align: 'center',
      minWidth: 100
    },
    {
      dataIndex: 'deptCode',
      title: '部门编码',
      align: 'center',
      minWidth: 100
    },
    {
      dataIndex: 'orderNum',
      title: '排序',
      align: 'center',
      width: 80
    },
    {
      dataIndex: 'leader',
      title: '负责人',
      align: 'center',
      width: 100
    },
    {
      dataIndex: 'phone',
      title: '联系电话',
      align: 'center',
      width: 120
    },
    {
      dataIndex: 'status',
      title: $t('common.status'),
      align: 'center',
      width: 100,
      customRender: ({ record }) => {
        if (record.status === null) {
          return null;
        }

        const status = record.status as Api.Common.EnableStatus;
        const label = $t(enableStatusRecord[status]);
        const color = status === '1' ? 'success' : 'warning';

        return <Tag color={color}>{label}</Tag>;
      }
    },
    {
      dataIndex: 'createTime',
      title: $t('common.createTime'),
      align: 'center',
      width: 180
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 130,
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
  handleEdit,
  checkedRowKeys,
  onBatchDeleted,
  onDeleted
} = useTableOperate(data, getData);

async function handleBatchDelete() {
  // TODO: 实现批量删除
  console.log('批量删除:', checkedRowKeys.value);
  onBatchDeleted();
}

async function handleDelete(id: number) {
  // TODO: 实现删除
  console.log('删除:', id);
  onDeleted();
}

function edit(id: number) {
  handleEdit(id);
}

function handleSearch() {
  getData();
}

function handleReset() {
  Object.assign(searchParams, {
    current: 1,
    size: 10,
    deptName: null,
    status: null
  });
  getData();
}
</script>

<style scoped>
.card-wrapper {
  @apply flex-col-stretch gap-16px overflow-hidden;
}
</style> 