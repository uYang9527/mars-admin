<script setup lang="tsx">
import {reactive, ref, watch, computed} from 'vue';
import type {Ref} from 'vue';
import {Button, Popconfirm, Space, Tag, Card as ACard, Table as ATable} from 'ant-design-vue';
import {useTable, useTableOperate} from '@/hooks/common/table';
import {
  fetchGetUserList,
  fetchDeleteUser,
  fetchDeleteUsers,
  fetchChangeUserStatus,
  fetchResetUserPassword
} from '@/service/api';
import {$t} from '@/locales';
import {useAppStore} from '@/store/modules/app';
import {enableStatusRecord, userGenderRecord} from '@/constants/business';
import UserOperateDrawer, {type OperateType} from './modules/user-operate-drawer.vue';
import UserSearch from './modules/user-search.vue';
import TableHeaderOperation from '@/components/advanced/table-header-operation.vue';

const appStore = useAppStore();

const wrapperEl = ref<HTMLElement>();

const {columns, columnChecks, data, getData, getDataByPage, loading, mobilePagination, reloadColumns, searchParams} =
  useTable({
    apiFn: fetchGetUserList,
    apiParams: {
      current: 1,
      size: 10,
      // if you want to use the searchParams in Form, you can define the initial values
      status: null,
      userName: null,
      userGender: null,
      nickName: null,
      phone: null,
      email: null
    },
    columns: () => [
      {
        key: 'index',
        dataIndex: 'index',
        title: $t('common.index'),
        width: 64,
        align: 'center',
        customRender: ({index}) => index + 1
      },
      {
        key: 'userName',
        dataIndex: 'userName',
        title: $t('page.manage.user.userName'),
        align: 'center',
        minWidth: 100
      },
      {
        key: 'nickName',
        dataIndex: 'nickName',
        title: $t('page.manage.user.nickName'),
        align: 'center',
        minWidth: 100
      },
      {
        key: 'userGender',
        dataIndex: 'userGender',
        title: $t('page.manage.user.userGender'),
        align: 'center',
        minWidth: 100,
        customRender: ({record}) => {
          if (record.userGender === null) {
            return null;
          }

          const label = $t(userGenderRecord[record.userGender]);

          return <Tag color={record.userGender === 0 ? 'blue' : 'pink'}>{label}</Tag>;
        }
      },
      {
        key: 'phone',
        dataIndex: 'phone',
        title: $t('page.manage.user.userPhone'),
        align: 'center',
        minWidth: 120
      },
      {
        key: 'email',
        dataIndex: 'email',
        title: $t('page.manage.user.userEmail'),
        align: 'center',
        minWidth: 100
      },
      {
        key: 'status',
        dataIndex: 'status',
        title: $t('page.manage.user.userStatus'),
        align: 'center',
        width: 100,
        customRender: ({record}) => {
          if (record.status === null) {
            return null;
          }

          const label = $t(enableStatusRecord[record.status]);

          return <Tag color={record.status === 1 ? 'success' : 'error'}>{label}</Tag>;
        }
      },
      {
        key: 'operate',
        title: $t('common.operate'),
        align: 'center',
        width: 260,
        customRender: ({record}) => (
          <div class="flex-center gap-8px">
            <Button type="primary" ghost size="small" onClick={() => edit(record.id)}>
              {$t('common.edit')}
            </Button>
            <Popconfirm title="确定要重置密码吗？" onConfirm={() => resetPassword(record.id)}>
              <Button size="small">重置密码</Button>
            </Popconfirm>
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
  onDeleted
  // you can use other methods of useTableOperate
} = useTableOperate(data, getData);

async function handleBatchDelete() {
  const {error} = await fetchDeleteUsers(checkedRowKeys.value);
  if (!error) {
    onBatchDeleted();
  }
}

async function handleDelete(id: number) {
  const {error} = await fetchDeleteUser(id);
  if (!error) {
    onDeleted();
  }
}

async function toggleStatus(id: number, status: string) {
  const newStatus = status === 1 ? 0 : 1;
  const {error} = await fetchChangeUserStatus(id, newStatus);
  if (!error) {
    window.$message?.success('状态修改成功');
    getData();
  }
}

async function resetPassword(id: number) {
  const {error} = await fetchResetUserPassword(id, '123456');
  if (!error) {
    window.$message?.success('密码重置成功，新密码为：123456');
  }
}

function edit(id: number) {
  handleEdit(id);
}
</script>

<template>
  <div ref="wrapperEl" class="flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <UserSearch v-model:model="searchParams" @reset="getData" @search="getData"/>
    <ACard
      title="用户列表"
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
      <ATable
        :columns="columns"
        :data-source="data"
        size="small"
        :scroll="{ x: 'max-content', y: 'calc(100vh - 320px)' }"
        :loading="loading"
        row-key="id"
        :pagination="mobilePagination"
        class="h-full"
      />
    </ACard>
    <UserOperateDrawer
      v-model:visible="drawerVisible"
      :operate-type="operateType"
      :row-data="editingData"
      @submitted="getData"
    />
  </div>
</template>

<style scoped></style>
