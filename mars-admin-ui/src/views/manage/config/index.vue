<script setup lang="tsx">
import { onMounted, reactive, ref } from 'vue';
import { Button, Modal, Popconfirm, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import {
  fetchDeleteConfig,
  fetchDeleteConfigs,
  fetchGetConfigList,
  fetchRefreshConfigCache
} from '@/service/api/system-config';
import { $t } from '@/locales';
import TableHeaderOperation from '@/components/advanced/table-header-operation.vue';
import ConfigOperateDrawer from './modules/config-operate-drawer.vue';

defineOptions({
  name: 'ManageConfig'
});

const wrapperEl = ref<HTMLElement>();

// 参数配置表格配置
const { columns, columnChecks, data, getData, loading, mobilePagination, updatePagination, searchParams } = useTable({
  apiFn: fetchGetConfigList,
  apiParams: {
    current: 1,
    size: 10,
    configName: '',
    configKey: '',
    configType: null
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
      key: 'configName',
      title: '参数名称',
      align: 'center',
      dataIndex: 'configName'
    },
    {
      key: 'configKey',
      title: '参数键名',
      align: 'center',
      dataIndex: 'configKey'
    },
    {
      key: 'configValue',
      title: '参数键值',
      align: 'center',
      dataIndex: 'configValue'
    },
    {
      key: 'configType',
      title: '系统内置',
      align: 'center',
      dataIndex: 'configType',
      customRender: ({ record }) => {
        if (!record.configType) {
          return null;
        }

        const configType = record.configType;
        const label = configType === 'Y' ? '是' : '否';
        const color = configType === 'Y' ? 'success' : 'default';

        return <Tag color={color}>{label}</Tag>;
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

// 搜索表单
const searchForm = reactive({
  configName: '',
  configKey: '',
  configType: null
});

// 折叠状态
const collapsed = ref(false);

// 参数配置操作
async function handleBatchDelete() {
  if (checkedRowKeys.value.length === 0) {
    window.$message?.warning('请选择要删除的配置');
    return;
  }

  Modal.confirm({
    title: '批量删除配置',
    content: `确定要删除选中的 ${checkedRowKeys.value.length} 个配置吗？删除后无法恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        const { error } = await fetchDeleteConfigs(checkedRowKeys.value);
        if (!error) {
          window.$message?.success('批量删除成功');
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
  const { error } = await fetchDeleteConfig(id);
  if (!error) {
    onDeleted();
  }
}

function edit(id: number) {
  handleEdit(id);
}

async function refreshConfigCache() {
  const { error } = await fetchRefreshConfigCache();
  if (!error) {
    window.$message?.success('刷新参数缓存成功');
  }
}

// 搜索功能
function handleSearch() {
  Object.assign(searchParams, searchForm);
  searchParams.current = 1;
  getData();
}

function handleReset() {
  Object.assign(searchForm, {
    configName: '',
    configKey: '',
    configType: null
  });
  Object.assign(searchParams, {
    current: 1,
    size: 10,
    configName: '',
    configKey: '',
    configType: null
  });
  getData();
}

function toggleCollapsed() {
  collapsed.value = !collapsed.value;
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

onMounted(() => {
  getData();
});
</script>

<template>
  <div ref="wrapperEl" class="flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <!-- 搜索区域 -->
    <ACard
      :title="$t('common.search')"
      :bordered="false"
      class="card-wrapper"
      :body-style="collapsed ? { padding: '0', display: 'none' } : {}"
      :data-collapsed="collapsed"
    >
      <template #extra>
        <AButton type="text" @click="toggleCollapsed">
          <template #icon>
            <icon-mdi:chevron-down v-if="collapsed" class="text-icon" />
            <icon-mdi:chevron-up v-else class="text-icon" />
          </template>
          {{ collapsed ? '展开' : '收起' }}
        </AButton>
      </template>

      <div v-show="!collapsed">
        <AForm
          :model="searchForm"
          :label-col="{
            span: 5,
            md: 7
          }"
        >
          <ARow :gutter="[16, 16]" wrap>
            <ACol :span="24" :md="12" :lg="8">
              <AFormItem label="参数名称" name="configName" class="m-0">
                <AInput v-model:value="searchForm.configName" placeholder="请输入参数名称" @input="handleSearch" />
              </AFormItem>
            </ACol>
            <ACol :span="24" :md="12" :lg="8">
              <AFormItem label="参数键名" name="configKey" class="m-0">
                <AInput v-model:value="searchForm.configKey" placeholder="请输入参数键名" @press-enter="handleSearch" />
              </AFormItem>
            </ACol>
            <ACol :span="24" :md="12" :lg="8">
              <AFormItem label="系统内置" name="configType" class="m-0">
                <ASelect
                  v-model:value="searchForm.configType"
                  placeholder="请选择系统内置"
                  :options="[
                    { label: '是', value: 'Y' },
                    { label: '否', value: 'N' }
                  ]"
                  allow-clear
                  @change="handleSearch"
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
      </div>
    </ACard>

    <!-- 参数配置表格区域 -->
    <ACard
      title="参数配置列表"
      :bordered="false"
      :body-style="{ paddingTop: '0px' }"
      class="sm:flex-1-hidden card-wrapper"
    >
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
            <AButton size="small" type="primary" ghost @click="refreshConfigCache">
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

    <!-- 参数配置操作抽屉 -->
    <ConfigOperateDrawer
      v-model:visible="drawerVisible"
      :operate-type="operateType"
      :row-data="editingData"
      @submitted="getData"
    />
  </div>
</template>

<style scoped>
.text-icon {
  font-size: 16px;
}

/* 折叠状态下的卡片样式优化 */
.card-wrapper {
  @apply flex-col-stretch gap-16px overflow-hidden;
}

/* 折叠时隐藏卡片主体，只保留标题栏 */
.card-wrapper :deep(.ant-card-body) {
  transition: all 0.3s ease;
}

/* 当折叠时，强制隐藏卡片主体 */
.card-wrapper[data-collapsed='true'] :deep(.ant-card-body) {
  display: none !important;
  height: 0 !important;
  padding: 0 !important;
  margin: 0 !important;
}

/* 优化折叠后的卡片头部样式 */
.card-wrapper[data-collapsed='true'] :deep(.ant-card-head) {
  margin-bottom: 0 !important;
  border-bottom: none !important;
}
</style>
