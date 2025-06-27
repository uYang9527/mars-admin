<script setup lang="tsx">
import { reactive, ref } from 'vue';
import { Button, Popconfirm, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import {
  fetchGetRoleList,
  fetchDeleteRole,
  fetchDeleteRoles,
  fetchChangeRoleStatus
} from '@/service/api';
import { $t } from '@/locales';
import { enableStatusRecord } from '@/constants/business';
import RoleOperateDrawer, { type OperateType } from './modules/role-operate-drawer.vue';
import MenuAuthModal from './modules/menu-auth-modal.vue';
import ButtonAuthModal from './modules/button-auth-modal.vue';

const wrapperEl = ref<HTMLElement>();

const { columns, columnChecks, data, getData, getDataByPage, loading, mobilePagination, searchParams } =
  useTable({
    apiFn: fetchGetRoleList,
    apiParams: {
      current: 1,
      size: 10,
      status: null,
      roleName: null,
      roleCode: null
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
        key: 'roleName',
        title: $t('page.manage.role.roleName'),
        align: 'center',
        minWidth: 120
      },
      {
        key: 'roleCode',
        title: $t('page.manage.role.roleCode'),
        align: 'center',
        minWidth: 120
      },
      {
        key: 'roleDesc',
        title: $t('page.manage.role.roleDesc'),
        align: 'center',
        minWidth: 120
      },
      {
        key: 'status',
        title: $t('page.manage.role.roleStatus'),
        align: 'center',
        width: 100,
        customRender: ({ record }) => {
          if (record.status === null) {
            return null;
          }

          const label = $t(enableStatusRecord[record.status]);

          return <Tag color={record.status === '0' ? 'success' : 'error'}>{label}</Tag>;
        }
      },
      {
        key: 'operate',
        title: $t('common.operate'),
        align: 'center',
        width: 280,
        customRender: ({ record }) => (
          <div class="flex-center gap-8px">
            <Button type="primary" ghost size="small" onClick={() => edit(record.id)}>
              {$t('common.edit')}
            </Button>
            <Button size="small" onClick={() => handleMenuAuth(record)}>
              菜单权限
            </Button>
            <Button size="small" onClick={() => handleButtonAuth(record)}>
              按钮权限
            </Button>
            <Button
              type={record.status === '0' ? 'default' : 'primary'}
              size="small"
              onClick={() => toggleStatus(record.id, record.status)}
            >
              {record.status === '0' ? '禁用' : '启用'}
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

// 菜单权限弹窗
const menuAuthVisible = ref(false);
const menuAuthRoleData = ref<Api.SystemManage.Role | null>(null);

// 按钮权限弹窗
const buttonAuthVisible = ref(false);
const buttonAuthRoleData = ref<Api.SystemManage.Role | null>(null);

async function handleBatchDelete() {
  const { error } = await fetchDeleteRoles(checkedRowKeys.value);
  if (!error) {
    onBatchDeleted();
  }
}

async function handleDelete(id: number) {
  const { error } = await fetchDeleteRole(id);
  if (!error) {
    onDeleted();
  }
}

async function toggleStatus(id: number, status: string) {
  const newStatus = status === '0' ? '1' : '0';
  const { error } = await fetchChangeRoleStatus(id, newStatus);
  if (!error) {
    window.$message?.success('状态修改成功');
    getData();
  }
}

function handleMenuAuth(record: Api.SystemManage.Role) {
  menuAuthRoleData.value = record;
  menuAuthVisible.value = true;
}

function handleButtonAuth(record: Api.SystemManage.Role) {
  buttonAuthRoleData.value = record;
  buttonAuthVisible.value = true;
}

function edit(id: number) {
  handleEdit(id);
}

// 搜索参数
const searchModel = reactive({
  roleName: '',
  roleCode: '',
  status: null
});

function handleSearch() {
  Object.assign(searchParams, searchModel);
  getData();
}

function handleReset() {
  Object.assign(searchModel, {
    roleName: '',
    roleCode: '',
    status: null
  });
  Object.assign(searchParams, searchModel);
  getData();
}
</script>

<template>
  <div ref="wrapperEl" class="flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <!-- 搜索区域 -->
    <ACard title="查询条件" :bordered="false">
      <AForm :model="searchModel" layout="inline">
        <AFormItem label="角色名称">
          <AInput v-model:value="searchModel.roleName" placeholder="请输入角色名称" allow-clear />
        </AFormItem>
        <AFormItem label="角色编码">
          <AInput v-model:value="searchModel.roleCode" placeholder="请输入角色编码" allow-clear />
        </AFormItem>
        <AFormItem label="状态">
          <ASelect v-model:value="searchModel.status" placeholder="请选择状态" allow-clear style="width: 120px">
            <ASelectOption value="0">启用</ASelectOption>
            <ASelectOption value="1">禁用</ASelectOption>
          </ASelect>
        </AFormItem>
        <AFormItem>
          <ASpace>
            <AButton type="primary" @click="handleSearch">
              <template #icon><SearchOutlined /></template>
              查询
            </AButton>
            <AButton @click="handleReset">
              <template #icon><ReloadOutlined /></template>
              重置
            </AButton>
          </ASpace>
        </AFormItem>
      </AForm>
    </ACard>

    <!-- 表格区域 -->
    <ACard
      title="角色列表"
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
        :scroll="{ x: 'max-content' }"
        :loading="loading"
        row-key="id"
        :pagination="mobilePagination"
        class="h-full"
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
    <MenuAuthModal
      v-model:visible="menuAuthVisible"
      :role-data="menuAuthRoleData"
    />

    <!-- 按钮权限弹窗 -->
    <ButtonAuthModal
      v-model:visible="buttonAuthVisible"
      :role-data="buttonAuthRoleData"
    />
  </div>
</template>

<style scoped></style>
