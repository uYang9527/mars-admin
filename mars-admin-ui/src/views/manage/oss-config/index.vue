<script setup lang="tsx">
import {Card as ACard, Table as ATable, Button, Modal, Popconfirm, Tag} from 'ant-design-vue';
import {useTable, useTableOperate} from '@/hooks/common/table';
import {
  fetchChangeOssConfigStatus,
  fetchDeleteOssConfig,
  fetchDeleteOssConfigs,
  fetchGetOssConfigList,
  fetchRefreshOssConfigCache
} from '@/service/api/file-manage';
import {$t} from '@/locales';
import StorageConfigSearch from './modules/storage-config-search.vue';
import StorageConfigOperateDrawer from './modules/storage-config-operate-drawer.vue';

defineOptions({
  name: 'OssConfig'
});


const { columns, data, getData, loading, mobilePagination, updatePagination, searchParams } = useTable({
  apiFn: fetchGetOssConfigList,
  apiParams: {
    current: 1,
    size: 10,
    configKey: '',
    status: null
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
      align: 'center',
      customRender: ({index}) => index + 1
    },
    {
      key: 'configKey',
      dataIndex: 'configKey',
      title: '配置标识',
      align: 'center',
      customRender: ({record}) => (
        <Tag color={getConfigColor(record.configKey)}>{getConfigName(record.configKey)}</Tag>
      )
    },
    {
      key: 'bucketName',
      dataIndex: 'bucketName',
      title: '存储桶',
      align: 'center',
      customRender: ({record}) => record.bucketName || '-'
    },
    {
      key: 'endpoint',
      dataIndex: 'endpoint',
      title: '端点地址',
      align: 'center',
      ellipsis: true,
      customRender: ({record}) => record.endpoint || '-'
    },
    {
      key: 'domain',
      dataIndex: 'domain',
      title: '自定义域名',
      align: 'center',
      ellipsis: true,
      customRender: ({record}) => record.domain || '-'
    },
    {
      key: 'status',
      dataIndex: 'status',
      title: '状态',
      align: 'center',
      customRender: ({record}) => (
        <Tag color={record.status === 1 ? 'success' : 'error'}>{record.status === 1 ? '启用' : '停用'}</Tag>
      )
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
          <Button
            type={record.status === 1 ? 'default' : 'primary'}
            size="small"
            onClick={() => toggleStatus(record.id, record.status)}
          >
            {record.status === 1 ? '停用' : '启用'}
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

// 编辑配置
function edit(id: number) {
  handleEdit(id);
}

// 获取配置颜色
function getConfigColor(configKey: string): string {
  const colors = {
    local: 'blue',
    minio: 'green',
    aliyun: 'orange'
  };
  return colors[configKey] || 'default';
}

// 获取配置名称
function getConfigName(configKey: string): string {
  const names = {
    local: '本地存储',
    minio: 'MinIO',
    aliyun: '阿里云OSS'
  };
  return names[configKey] || configKey;
}

// 切换状态
async function toggleStatus(id: number, currentStatus: number) {
  const newStatus = currentStatus === 1 ? 0 : 1;
  const {error} = await fetchChangeOssConfigStatus(id, newStatus);
  if (!error) {
    window.$message?.success(newStatus === 1 ? '启用成功' : '停用成功');
    getData();
  }
}

// 批量删除
async function handleBatchDelete() {
  if (checkedRowKeys.value.length === 0) {
    window.$message?.warning('请选择要删除的存储配置');
    return;
  }

  Modal.confirm({
    title: '批量删除存储配置',
    content: `确定要删除选中的 ${checkedRowKeys.value.length} 个存储配置吗？删除后无法恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      const {error} = await fetchDeleteOssConfigs(checkedRowKeys.value);
      if (!error) {
        window.$message?.success('批量删除成功');
        onBatchDeleted();
      }
    }
  });
}

// 删除单个配置
async function handleDelete(id: number) {
  const {error} = await fetchDeleteOssConfig(id);
  if (!error) {
    window.$message?.success('删除成功');
    onDeleted();
  }
}

// 刷新OSS配置缓存
async function refreshOssConfigCache() {
  try {
    const {error} = await fetchRefreshOssConfigCache();
    if (!error) {
      window.$message?.success('刷新OSS配置缓存成功');
    }
  } catch (err) {
    console.error('刷新OSS配置缓存失败:', err);
    window.$message?.error('刷新缓存失败，请重试');
  }
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
  <div class="min-h-500px flex-col-stretch gap-16px">
    <!-- 搜索区域 -->
    <StorageConfigSearch v-model:model="searchParams" @reset="getData" @search="getData"/>



    <!-- 存储配置列表 -->
    <ACard :bordered="false" :body-style="{ padding: '0' }">
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="flex justify-between items-center w-full">
          <span class="text-base font-medium">存储配置列表</span>
          <div class="flex gap-12px flex-wrap">
            <AButton type="primary" ghost @click="handleAdd">
              <template #icon>
                <icon-ic-round-add class="align-sub text-icon" />
              </template>
              新增存储配置</AButton>
            <AButton type="primary" ghost :disabled="checkedRowKeys.length === 0" @click="handleBatchDelete">
              批量删除
            </AButton>
            <AButton type="primary" ghost @click="refreshOssConfigCache">
              <template #icon>
                <icon-mdi-cached class="align-sub text-icon" />
              </template>
              <span class="ml-8px">刷新缓存</span>
            </AButton>
          </div>
        </div>
      </template>

      <ATable
        :columns="columns"
        :data-source="data"
        :row-key="record => record.id"
        :pagination="mobilePagination"
        :loading="loading"
        :row-selection="rowSelection"
        :scroll="{ x: 1400 }"
        size="small"
        @change="handleTableChange"
        class="h-full"
      />
    </ACard>


    <!-- 配置操作抽屉 -->
    <StorageConfigOperateDrawer
      v-model:visible="drawerVisible"
      :operate-type="operateType"
      :row-data="editingData"
      @submitted="getData"
    />
  </div>
</template>

<style scoped>


.detail-content .ant-descriptions-item-label {
  width: 120px;
}
</style>
