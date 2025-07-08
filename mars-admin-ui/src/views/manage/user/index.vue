<script setup lang="tsx">
import {ref} from 'vue';
import {Card as ACard, Table as ATable, Button, Modal, Popconfirm, Tag} from 'ant-design-vue';
import {useTable, useTableOperate} from '@/hooks/common/table';
import {
  fetchChangeUserStatus,
  fetchDeleteUser,
  fetchDeleteUsers,
  fetchGetUserList,
  fetchResetUserPassword
} from '@/service/api';
import {$t} from '@/locales';
import {enableStatusRecord, userGenderRecord } from '@/constants/business';
import TableHeaderOperation from '@/components/advanced/table-header-operation.vue';
import UserOperateDrawer from './modules/user-operate-drawer.vue';
import UserSearch from './modules/user-search.vue';

const wrapperEl = ref<HTMLElement>();

const { columns, columnChecks, data, getData, loading, mobilePagination, updatePagination, searchParams } = useTable({
  apiFn: fetchGetUserList,
  apiParams: {
    current: 1,
    size: 10,
    // if you want to use the searchParams in Form, you can define the initial values
    status: null,
    username: null,
    gender: null,
    nickname: null,
    phone: null,
    email: null
  },
  columns: () => [
    {
      key: 'selection',
      title: '',
      align: 'center',
      type: 'selection',
      width: 48,
      fixed: 'left'
    },
    {
      key: 'index',
      dataIndex: 'index',
      title: $t('common.index'),
      align: 'center',
      customRender: ({ index }) => index + 1
    },
    {
      key: 'username',
      dataIndex: 'username',
      title: $t('page.manage.user.userName'),
      align: 'center'
    },
    {
      key: 'nickname',
      dataIndex: 'nickname',
      title: $t('page.manage.user.nickName'),
      align: 'center'
    },
    {
      key: 'gender',
      dataIndex: 'gender',
      title: $t('page.manage.user.userGender'),
      align: 'center',
      customRender: ({ record }) => {
        if (record.gender === null) {
          return null;
        }

        const label = $t(userGenderRecord[record.gender]);

        return <Tag color={record.gender === 0 ? 'blue' : 'pink'}>{label}</Tag>;
      }
    },
    {
      key: 'phone',
      dataIndex: 'phone',
      title: $t('page.manage.user.userPhone'),
      align: 'center'
    },
    {
      key: 'email',
      dataIndex: 'email',
      title: $t('page.manage.user.userEmail'),
      align: 'center'
    },
    {
      key: 'status',
      dataIndex: 'status',
      title: $t('page.manage.user.userStatus'),
      align: 'center',
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
  onDeleted,
  rowSelection
} = useTableOperate(data, getData);

async function handleBatchDelete() {
  try {
    // 先检查是否有选择记录
    if (checkedRowKeys.value.length === 0) {
      window.$message?.warning('请选择要删除的用户');
      return;
    }

    // 使用ant-design-vue的Modal.confirm替代
    Modal.confirm({
      title: '批量删除用户',
      content: `确定要删除选中的 ${checkedRowKeys.value.length} 个用户吗？删除后无法恢复！`,
      okText: '确定',
      cancelText: '取消',
      async onOk() {
        // 发送请求
        const {error} = await fetchDeleteUsers(checkedRowKeys.value);
        if (!error) {
          window.$message?.success('批量删除成功');
          onBatchDeleted();
        } else {
          window.$message?.error(`批量删除失败: ${error}`);
        }
      }
    });
  } catch (e) {
    window.$message?.error(`操作失败: ${e.message}`);
  }
}

async function handleDelete(id: number) {
  try {
    // 检查是否是管理员用户
    const user = data.value.find(item => item.id === id);
    if (user && user.username === 'admin') {
      window.$message?.error('管理员用户不能删除');
      return;
    }

    // 发送请求
    const {error} = await fetchDeleteUser(id);
    if (!error) {
      window.$message?.success('删除成功');
      onDeleted();
    } else {
      window.$message?.error(`删除失败: ${error}`);
    }
  } catch (e) {
    window.$message?.error(`操作失败: ${e.message}`);
  }
}

async function toggleStatus(id: number, status: number) {
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

// 处理表格变化事件（分页、排序、筛选）
function handleTableChange(paginationInfo: any) {
  // 处理分页变化（包括页码和分页大小）
  if (paginationInfo) {
    const {current, pageSize} = paginationInfo;

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
    <UserSearch v-model:model="searchParams" @reset="getData" @search="getData"/>
    <ACard title="用户列表" :bordered="false" :body-style="{ paddingTop: '0px' }" class="sm:flex-1-hidden card-wrapper">
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
        :row-selection="rowSelection"
        :pagination="mobilePagination"
        class="h-full"
        @change="handleTableChange"
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
