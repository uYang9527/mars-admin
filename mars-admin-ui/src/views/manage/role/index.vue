<script setup lang="tsx">
import { ref } from 'vue';
import { Button, Modal, Popconfirm, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import {
  fetchChangeRoleStatus,
  fetchDeleteRole,
  fetchDeleteRoles,
  fetchGetRoleList,
  fetchRefreshRoleCache
} from '@/service/api';
import { $t } from '@/locales';
import { enableStatusRecord } from '@/constants/business';
import RoleOperateDrawer from './modules/role-operate-drawer.vue';
import MenuAuthModal from './modules/menu-auth-modal.vue';
import ButtonAuthModal from './modules/button-auth-modal.vue';
import DataAuthModal from './modules/data-auth-modal.vue';
import RoleSearch from './modules/role-search.vue';

const wrapperEl = ref<HTMLElement>();

const { columns, columnChecks, data, getData, loading, mobilePagination, updatePagination, searchParams } = useTable({
  apiFn: fetchGetRoleList,
  apiParams: {
    current: 1,
    size: 10,
    status: null,
    roleName: null,
    roleCode: null,
    roleKey: null
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
      title: $t('common.index'),
      width: 64,
      align: 'center',
      customRender: ({ index }) => index + 1
    },
    {
      key: 'roleName',
      title: $t('page.manage.role.roleName'),
      align: 'center',
      dataIndex: 'roleName'
    },
    {
      key: 'roleCode',
      title: $t('page.manage.role.roleCode'),
      align: 'center',
      dataIndex: 'roleCode'
    },
    {
      key: 'roleKey',
      title: $t('page.manage.role.roleKey'),
      align: 'center',
      dataIndex: 'roleKey'
    },
    {
      key: 'description',
      title: $t('page.manage.role.roleDesc'),
      align: 'center',
      dataIndex: 'description'
    },
    {
      key: 'roleSort',
      title: '显示顺序',
      align: 'center',
      dataIndex: 'roleSort'
    },
    {
      key: 'status',
      title: $t('page.manage.role.roleStatus'),
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
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      customRender: ({ record }) => (
        <div class="flex-center gap-8px">
          <Button type="primary" ghost size="small" onClick={() => edit(record.id)}>
            {$t('common.edit')}
          </Button>
          <Button size="small" onClick={() => handleMenuAuth(record)}>
            菜单权限
          </Button>
          <Button size="small" onClick={() => handleDataAuth(record)}>
            数据权限
          </Button>
          <Button size="small" onClick={() => handleButtonAuth(record)}>
            按钮权限
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

// 菜单权限弹窗
const menuAuthVisible = ref(false);
const menuAuthRoleId = ref<number | null>(null);

// 按钮权限弹窗
const buttonAuthVisible = ref(false);
const buttonAuthRoleId = ref<number | null>(null);

// 数据权限弹窗
const dataAuthVisible = ref(false);
const dataAuthRoleId = ref<number | null>(null);

async function handleBatchDelete() {
  try {
    // 先检查是否有选择记录
    if (checkedRowKeys.value.length === 0) {
      window.$message?.warning('请选择要删除的角色');
      return;
    }

    // 使用ant-design-vue的Modal.confirm替代
    Modal.confirm({
      title: '批量删除角色',
      content: `确定要删除选中的 ${checkedRowKeys.value.length} 个角色吗？删除后无法恢复！`,
      okText: '确定',
      cancelText: '取消',
      async onOk() {
        // 发送请求
        const { error } = await fetchDeleteRoles(checkedRowKeys.value);
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
    // 检查是否是系统内置角色
    const role = data.value.find(item => item.id === id);
    if (role && role.isSystem === 1) {
      window.$message?.error('系统内置角色不能删除');
      return;
    }

    const { error } = await fetchDeleteRole(id);
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
  const { error } = await fetchChangeRoleStatus(id, newStatus);
  if (!error) {
    window.$message?.success('状态修改成功');
    getData();
  }
}

function handleMenuAuth(record: Api.SystemManage.Role) {
  menuAuthRoleId.value = record.id;
  menuAuthVisible.value = true;
}

function handleButtonAuth(record: Api.SystemManage.Role) {
  buttonAuthRoleId.value = record.id;
  buttonAuthVisible.value = true;
}

function handleDataAuth(record: Api.SystemManage.Role) {
  dataAuthRoleId.value = record.id;
  dataAuthVisible.value = true;
}

function edit(id: number) {
  handleEdit(id);
}

// 刷新角色缓存
async function refreshRoleCache() {
  try {
    const { error } = await fetchRefreshRoleCache();
    if (!error) {
      window.$message?.success('刷新角色缓存成功');
    }
  } catch (err) {
    console.error('刷新角色缓存失败:', err);
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
    <RoleSearch v-model:model="searchParams" @reset="getData" @search="getData" />

    <!-- 表格区域 -->
    <ACard title="角色列表" :bordered="false" :body-style="{ paddingTop: '0px' }" class="sm:flex-1-hidden card-wrapper">
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
            <AButton size="small" type="primary" ghost @click="refreshRoleCache">
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
        row-key="id"
        :row-selection="rowSelection"
        :pagination="mobilePagination"
        class="h-full"
        @change="handleTableChange"
      />
    </ACard>

    <!-- 角色操作抽屉 -->
    <RoleOperateDrawer
      v-model:visible="drawerVisible"
      :operate-type="operateType"
      :row-data="editingData"
      @submitted="getData"
    />

    <!-- 菜单权限弹窗 -->
    <MenuAuthModal v-model:visible="menuAuthVisible" :role-id="menuAuthRoleId" />

    <!-- 按钮权限弹窗 -->
    <ButtonAuthModal v-model:visible="buttonAuthVisible" :role-id="buttonAuthRoleId" />

    <!-- 数据权限弹窗 -->
    <DataAuthModal v-model:visible="dataAuthVisible" :role-id="dataAuthRoleId" />
  </div>
</template>

<style scoped></style>
