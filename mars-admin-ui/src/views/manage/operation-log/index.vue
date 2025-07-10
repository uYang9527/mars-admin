<script setup lang="tsx">
import { Card as ACard, Table as ATable, Button, Modal, Popconfirm, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import {
  fetchCleanOperLog,
  fetchDeleteOperLog,
  fetchDeleteOperLogs,
  fetchExportOperLogExcel,
  fetchGetOperLogList
} from '@/service/api/log-manage';
import { $t } from '@/locales';
import OperLogSearch from './modules/operlog-search.vue';
import OperLogDetailDrawer from './modules/operlog-detail-drawer.vue';

defineOptions({
  name: 'OperationLog'
});

const { columns, data, getData, loading, mobilePagination, updatePagination, searchParams, updateSearchParams } =
  useTable({
    apiFn: fetchGetOperLogList,
    apiParams: {
      current: 1,
      size: 10,
      title: '',
      operName: '',
      businessType: null,
      status: null,
      operIp: ''
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
        customRender: ({ index }) => index + 1
      },
      {
        key: 'title',
        dataIndex: 'title',
        title: '模块标题',
        align: 'center',
        ellipsis: true
      },
      {
        key: 'businessType',
        dataIndex: 'businessType',
        title: '业务类型',
        align: 'center',
        customRender: ({ record }) => (
          <Tag color={getBusinessTypeColor(record.businessType)}>{getBusinessTypeText(record.businessType)}</Tag>
        )
      },
      {
        key: 'operName',
        dataIndex: 'operName',
        title: '操作人员',
        align: 'center',
        ellipsis: true
      },
      {
        key: 'operIp',
        dataIndex: 'operIp',
        title: '主机地址',
        align: 'center',
        ellipsis: true
      },
      {
        key: 'operLocation',
        dataIndex: 'operLocation',
        title: '操作地点',
        align: 'center',
        ellipsis: true,
        customRender: ({ record }) => record.operLocation || '-'
      },
      {
        key: 'status',
        dataIndex: 'status',
        title: '操作状态',
        align: 'center',
        customRender: ({ record }) => (
          <Tag color={record.status === 0 ? 'success' : 'error'}>{record.status === 0 ? '正常' : '异常'}</Tag>
        )
      },
      {
        key: 'operTime',
        dataIndex: 'operTime',
        title: '操作时间',
        align: 'center',
        ellipsis: true
      },
      {
        key: 'costTime',
        dataIndex: 'costTime',
        title: '消耗时间(ms)',
        align: 'center',
        customRender: ({ record }) => record.costTime || 0
      },
      {
        key: 'operate',
        title: $t('common.operate'),
        align: 'center',
        customRender: ({ record }) => (
          <div class="flex-center gap-8px">
            <Button type="primary" ghost size="small" onClick={() => handleDetail(record.id)}>
              详情
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

const { drawerVisible, editingData, checkedRowKeys, onBatchDeleted, onDeleted, rowSelection } = useTableOperate(
  data,
  getData
);

// 查看详情
function handleDetail(id: number) {
  const record = data.value.find(item => item.id === id);
  if (record) {
    editingData.value = record;
    drawerVisible.value = true;
  }
}

// 获取业务类型颜色
function getBusinessTypeColor(type: number): string {
  const colors = {
    0: 'default', // 其它
    1: 'green', // 新增
    2: 'blue', // 修改
    3: 'red' // 删除
  };
  return colors[type] || 'default';
}

// 获取业务类型文本
function getBusinessTypeText(type: number): string {
  const texts = {
    0: '其它',
    1: '新增',
    2: '修改',
    3: '删除'
  };
  return texts[type] || '未知';
}

// 批量删除
async function handleBatchDelete() {
  if (checkedRowKeys.value.length === 0) {
    window.$message?.warning('请选择要删除的操作日志');
    return;
  }

  Modal.confirm({
    title: '批量删除操作日志',
    content: `确定要删除选中的 ${checkedRowKeys.value.length} 条操作日志吗？删除后无法恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      const { error } = await fetchDeleteOperLogs(checkedRowKeys.value);
      if (!error) {
        window.$message?.success('批量删除成功');
        onBatchDeleted();
      }
    }
  });
}

// 删除单个日志
async function handleDelete(id: number) {
  const { error } = await fetchDeleteOperLog(id);
  if (!error) {
    window.$message?.success('删除成功');
    onDeleted();
  }
}

// 清空所有日志
async function handleCleanAll() {
  Modal.confirm({
    title: '清空操作日志',
    content: '确定要清空所有操作日志吗？此操作不可撤销！',
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      const { error } = await fetchCleanOperLog();
      if (!error) {
        window.$message?.success('清空成功');
        getData();
      }
    }
  });
}

// 导出日志
async function handleExport() {
  try {
    // 构建导出参数，只包含搜索条件，不包含分页参数
    const exportParams: any = {};

    // 安全检查 searchParams.value
    if (searchParams) {
      if (searchParams.title) {
        exportParams.title = searchParams.title;
      }
      if (searchParams.operName) {
        exportParams.operName = searchParams.operName;
      }
      if (searchParams.businessType !== null && searchParams.businessType !== undefined) {
        exportParams.businessType = searchParams.businessType;
      }
      if (searchParams.status !== null && searchParams.status !== undefined) {
        exportParams.status = searchParams.status;
      }
      if (searchParams.operIp) {
        exportParams.operIp = searchParams.operIp;
      }
    }
    window.$message?.info('开始导出，请稍候...');

    const response = await fetchExportOperLogExcel(exportParams);

    if (response.error) {
      window.$message?.error(`导出失败: ${response.error.message || '未知错误'}`);
      return;
    }

    if (response.data) {
      const blob = response.data as Blob;

      // 创建下载链接
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;

      // 设置文件名
      const now = new Date();
      const fileName = `操作日志_${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, '0')}${String(now.getDate()).padStart(2, '0')}_${String(now.getHours()).padStart(2, '0')}${String(now.getMinutes()).padStart(2, '0')}${String(now.getSeconds()).padStart(2, '0')}.xlsx`;
      link.download = fileName;

      // 触发下载
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      // 释放URL对象
      window.URL.revokeObjectURL(url);

      window.$message?.success('导出成功');
    } else {
      window.$message?.error('导出失败：没有获取到数据');
    }
  } catch (error) {
    window.$message?.error(`导出失败: ${error instanceof Error ? error.message : '未知错误'}`);
  }
}

// 处理表格变化事件（分页、排序、筛选）
function handleTableChange(pagination: any) {
  // 处理分页变化（包括页码和分页大小）
  if (pagination) {
    const { current, pageSize } = pagination;

    // 不要重设搜索条件中的除分页之外的参数
    const currentParams = { ...searchParams };

    // 更新分页设置
    updatePagination({
      current: current || 1,
      pageSize: pageSize || 10,
      total: mobilePagination.value.total // 保持原来的总记录数
    });

    // 更新搜索参数中的分页信息，但保留其他搜索条件
    currentParams.current = current || 1;
    currentParams.size = pageSize || 10;

    // 使用updateSearchParams方法而不是直接修改searchParams
    updateSearchParams(currentParams);

    // 重新加载数据
    getData();
  }
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px">
    <!-- 搜索区域 -->
    <OperLogSearch v-model:model="searchParams" @reset="getData" @search="getData" />

    <!-- 操作日志列表 -->
    <ACard :bordered="false" :body-style="{ padding: '0' }">
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="w-full flex items-center justify-between">
          <span class="text-base font-medium">操作日志</span>
          <div class="flex flex-wrap gap-12px">
            <AButton type="primary" danger ghost :disabled="checkedRowKeys.length === 0" @click="handleBatchDelete">
              批量删除
            </AButton>
            <AButton type="primary" danger ghost @click="handleCleanAll">
              <template #icon>
                <icon-ic-round-delete class="align-sub text-icon" />
              </template>
              清空日志
            </AButton>
            <AButton type="primary" ghost @click="handleExport">
              <template #icon>
                <icon-ic-round-download class="align-sub text-icon" />
              </template>
              导出日志
            </AButton>
          </div>
        </div>
      </template>

      <!-- 表格 -->
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

    <!-- 操作日志详情抽屉 -->
    <OperLogDetailDrawer v-model:visible="drawerVisible" :row-data="editingData" />
  </div>
</template>

<style scoped></style>
