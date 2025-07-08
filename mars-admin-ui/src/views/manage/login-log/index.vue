<script setup lang="tsx">
import { Button as AButton, Card as ACard, Table as ATable, Button, Modal, Popconfirm, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import {
  fetchCleanLoginLog,
  fetchDeleteLoginLog,
  fetchDeleteLoginLogs,
  fetchExportLoginLogExcel,
  fetchGetLoginLogList
} from '@/service/api/log-manage';
import { $t } from '@/locales';
import LoginLogSearch from './modules/loginlog-search.vue';
import LoginLogDetailDrawer from './modules/loginlog-detail-drawer.vue';

defineOptions({
  name: 'LoginLog'
});

const { columns, data, getData, loading, mobilePagination, updatePagination, searchParams } = useTable({
  apiFn: fetchGetLoginLogList,
  apiParams: {
    current: 1,
    size: 10,
    userName: '',
    ipaddr: '',
    status: '',
    loginLocation: ''
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
      key: 'userName',
      dataIndex: 'userName',
      title: '用户账号',
      align: 'center',
      ellipsis: true
    },
    {
      key: 'ipaddr',
      dataIndex: 'ipaddr',
      title: '登录IP',
      align: 'center',
      ellipsis: true
    },
    {
      key: 'loginLocation',
      dataIndex: 'loginLocation',
      title: '登录地点',
      align: 'center',
      ellipsis: true,
      customRender: ({ record }) => record.loginLocation || '-'
    },
    {
      key: 'browser',
      dataIndex: 'browser',
      title: '浏览器',
      align: 'center',
      ellipsis: true,
      customRender: ({ record }) => record.browser || '-'
    },
    {
      key: 'os',
      dataIndex: 'os',
      title: '操作系统',
      align: 'center',
      ellipsis: true,
      customRender: ({ record }) => record.os || '-'
    },
    {
      key: 'status',
      dataIndex: 'status',
      title: '登录状态',
      align: 'center',
      customRender: ({ record }) => (
        <Tag color={record.status === '0' ? 'success' : 'error'}>{record.status === '0' ? '成功' : '失败'}</Tag>
      )
    },
    {
      key: 'msg',
      dataIndex: 'msg',
      title: '提示消息',
      align: 'center',
      ellipsis: true,
      customRender: ({ record }) => record.msg || '-'
    },
    {
      key: 'loginTime',
      dataIndex: 'loginTime',
      title: '登录时间',
      align: 'center',
      ellipsis: true
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
          <Popconfirm title="确定要删除这条登录日志吗？" onConfirm={() => handleDelete(record.id)}>
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

// 批量删除
async function handleBatchDelete() {
  try {
    if (checkedRowKeys.value.length === 0) {
      window.$message?.warning('请选择要删除的登录日志');
      return;
    }

    Modal.confirm({
      title: '批量删除登录日志',
      content: `确定要删除选中的 ${checkedRowKeys.value.length} 条登录日志吗？删除后无法恢复！`,
      okText: '确定',
      cancelText: '取消',
      async onOk() {
        const { error } = await fetchDeleteLoginLogs(checkedRowKeys.value);
        if (!error) {
          window.$message?.success('批量删除成功');
          onBatchDeleted();
        } else {
          window.$message?.error('批量删除失败');
        }
      }
    });
  } catch (e) {
    window.$message?.error('操作失败');
  }
}

// 删除单个日志
async function handleDelete(id: number) {
  try {
    const { error } = await fetchDeleteLoginLog(id);
    if (!error) {
      window.$message?.success('删除成功');
      onDeleted();
    } else {
      window.$message?.error('删除失败');
    }
  } catch (e) {
    window.$message?.error('操作失败');
  }
}

// 清空所有日志
async function handleCleanAll() {
  Modal.confirm({
    title: '清空登录日志',
    content: '确定要清空所有登录日志吗？此操作不可撤销！',
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      const { error } = await fetchCleanLoginLog();
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
      if (searchParams.userName) {
        exportParams.userName = searchParams.userName;
      }
      if (searchParams.ipaddr) {
        exportParams.ipaddr = searchParams.ipaddr;
      }
      if (searchParams.status) {
        exportParams.status = searchParams.status;
      }
      if (searchParams.loginLocation) {
        exportParams.loginLocation = searchParams.loginLocation;
      }
    }

    console.log('导出参数:', exportParams);
    window.$message?.info('开始导出，请稍候...');

    const response = await fetchExportLoginLogExcel(exportParams);
    console.log('API响应:', response);

    if (response.error) {
      console.error('API错误:', response.error);
      window.$message?.error(`导出失败: ${response.error.message || '未知错误'}`);
      return;
    }

    if (response.data) {
      const blob = response.data as Blob;
      console.log('获取到blob:', blob.size, 'bytes');

      // 创建下载链接
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;

      // 设置文件名
      const now = new Date();
      const fileName = `登录日志_${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, '0')}${String(now.getDate()).padStart(2, '0')}_${String(now.getHours()).padStart(2, '0')}${String(now.getMinutes()).padStart(2, '0')}${String(now.getSeconds()).padStart(2, '0')}.xlsx`;
      link.download = fileName;

      // 触发下载
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      // 释放URL对象
      window.URL.revokeObjectURL(url);

      window.$message?.success('导出成功');
    } else {
      console.error('没有获取到数据');
      window.$message?.error('导出失败：没有获取到数据');
    }
  } catch (error) {
    console.error('导出过程中发生错误:', error);
    window.$message?.error(`导出失败: ${error instanceof Error ? error.message : '未知错误'}`);
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
  <div class="min-h-500px flex-col-stretch gap-16px">
    <!-- 搜索区域 -->
    <LoginLogSearch v-model:model="searchParams" @reset="getData" @search="getData" />

    <ACard :bordered="false" :body-style="{ padding: '0' }">
      <!-- 自定义标题区域 -->
      <template #title>
        <div class="flex justify-between items-center w-full">
          <span class="text-base font-medium">登录日志</span>
          <!-- 操作按钮 -->
          <div class="flex flex-wrap justify-end gap-x-12px gap-y-8px lt-sm:(w-200px py-12px)">
            <AButton type="primary" danger ghost :disabled="checkedRowKeys.length === 0" @click="handleBatchDelete">
              批量删除
            </AButton>
            <AButton type="primary" danger ghost @click="handleCleanAll">
              <template #icon>
                <icon-ic-round-delete class="align-sub text-icon" />
              </template>
              <span class="ml-8px">清空日志</span>
            </AButton>
            <AButton type="primary" ghost @click="handleExport">
              <template #icon>
                <icon-ic-round-download class="align-sub text-icon" />
              </template>
              <span class="ml-8px">导出日志</span>
            </AButton>
          </div>
        </div>
      </template>

      <!-- 表格部分 -->
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

    <LoginLogDetailDrawer :visible="drawerVisible" :row-data="editingData" @update:visible="drawerVisible = $event" />
  </div>
</template>

<style scoped></style>
