<script setup lang="tsx">
import { onMounted, reactive, ref } from 'vue';
import { Button, Modal, Popconfirm, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import {
  fetchDeleteDictData,
  fetchDeleteDictDatas,
  fetchDeleteDictType,
  fetchDeleteDictTypes,
  fetchGetDictDataList,
  fetchGetDictTypeList,
  fetchRefreshDictCache
} from '@/service/api/system-manage';
import { $t } from '@/locales';
import { useAppStore } from '@/store/modules/app';
import TableHeaderOperation from '@/components/advanced/table-header-operation.vue';
import DictTypeOperateDrawer from './modules/dict-type-operate-drawer.vue';
import DictDataOperateDrawer from './modules/dict-data-operate-drawer.vue';

defineOptions({
  name: 'ManageDict'
});

const appStore = useAppStore();
const wrapperEl = ref<HTMLElement>();

// 字典类型表格配置
const {
  columns: dictTypeColumns,
  columnChecks: dictTypeColumnChecks,
  data: dictTypeData,
  getData: getDictTypeData,
  loading: dictTypeLoading,
  pagination: dictTypePagination,
  searchParams: dictTypeSearchParams
} = useTable({
  apiFn: fetchGetDictTypeList,
  apiParams: {
    current: 1,
    size: 10,
    dictName: '',
    dictType: '',
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
      key: 'dictName',
      title: '字典名称',
      align: 'center',
      minWidth: 120,
      dataIndex: 'dictName'
    },
    {
      key: 'dictType',
      title: '字典类型',
      align: 'center',
      minWidth: 150,
      dataIndex: 'dictType',
      customRender: ({ record }) => (
        <Button type="link" class="h-auto p-0 text-blue-600 hover:text-blue-800" onClick={() => viewDictData(record)}>
          {record.dictType}
        </Button>
      )
    },
    {
      key: 'status',
      title: '状态',
      align: 'center',
      width: 100,
      dataIndex: 'status',
      customRender: ({ record }) => {
        if (record.status === null) {
          return null;
        }

        const status = record.status;
        const label = status === 1 ? '正常' : '停用';
        const color = status === 1 ? 'success' : 'error';

        return <Tag color={color}>{label}</Tag>;
      }
    },
    {
      key: 'createTime',
      title: '创建时间',
      align: 'center',
      width: 180,
      dataIndex: 'createTime'
    },
    {
      key: 'remark',
      title: '备注',
      align: 'center',
      ellipsis: true,
      minWidth: 120,
      dataIndex: 'remark'
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 240,
      fixed: 'right',
      customRender: ({ record }) => (
        <div class="flex-center gap-8px">
          <Button type="primary" ghost size="small" onClick={() => editDictType(record.id)}>
            {$t('common.edit')}
          </Button>
          <Button size="small" onClick={() => refreshDictCache()}>
            刷新缓存
          </Button>
          <Popconfirm title={$t('common.confirmDelete')} onConfirm={() => handleDeleteDictType(record.id)}>
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
  drawerVisible: dictTypeDrawerVisible,
  operateType: dictTypeOperateType,
  editingData: dictTypeEditingData,
  handleAdd: handleAddDictType,
  handleEdit: handleEditDictType,
  checkedRowKeys: dictTypeCheckedRowKeys,
  onBatchDeleted: onDictTypeBatchDeleted,
  onDeleted: onDictTypeDeleted
} = useTableOperate(dictTypeData, getDictTypeData);

// 字典数据相关
const dictDataModalVisible = ref(false);
const selectedDictType = ref('');
const selectedDictTypeName = ref('');
const dictDataData = ref([]);
const dictDataLoading = ref(false);
const dictDataOperateDrawerVisible = ref(false);
const dictDataOperateType = ref('add');
const dictDataEditingData = ref(null);
const dictDataCheckedRowKeys = ref([]);

// 字典数据分页参数
const dictDataPagination = reactive({
  current: 1,
  pageSize: 20,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total, range) => `第 ${range[0]}-${range[1]} 条/总共 ${total} 条`,
  onChange: (page, pageSize) => {
    dictDataPagination.current = page;
    dictDataPagination.pageSize = pageSize;
    loadDictData();
  },
  onShowSizeChange: (current, size) => {
    dictDataPagination.current = 1;
    dictDataPagination.pageSize = size;
    loadDictData();
  }
});

// 字典数据搜索参数
const dictDataSearchParams = reactive({
  dictLabel: '',
  dictValue: '',
  status: null
});

// 字典数据表格列配置
const dictDataColumns = [
  {
    key: 'index',
    title: $t('common.index'),
    width: 64,
    align: 'center',
    customRender: ({ index }) => index + 1
  },
  {
    key: 'dictLabel',
    title: '字典标签',
    align: 'center',
    minWidth: 120,
    dataIndex: 'dictLabel'
  },
  {
    key: 'dictValue',
    title: '字典键值',
    align: 'center',
    minWidth: 120,
    dataIndex: 'dictValue'
  },
  {
    key: 'dictSort',
    title: '字典排序',
    align: 'center',
    width: 100,
    dataIndex: 'dictSort'
  },
  {
    key: 'status',
    title: '状态',
    align: 'center',
    width: 100,
    dataIndex: 'status',
    customRender: ({ record }) => {
      if (record.status === null) {
        return null;
      }

      const status = record.status;
      const label = status === 1 ? '正常' : '停用';
      const color = status === 1 ? 'success' : 'error';

      return <Tag color={color}>{label}</Tag>;
    }
  },
  {
    key: 'isDefault',
    title: '是否默认',
    align: 'center',
    width: 100,
    dataIndex: 'isDefault',
    customRender: ({ record }) => {
      return record.isDefault === 'Y' ? '是' : '否';
    }
  },
  {
    key: 'createTime',
    title: '创建时间',
    align: 'center',
    width: 160,
    dataIndex: 'createTime'
  },
  {
    key: 'operate',
    title: $t('common.operate'),
    align: 'center',
    width: 180,
    fixed: 'right',
    customRender: ({ record }) => (
      <div class="flex-center gap-8px">
        <Button type="primary" ghost size="small" onClick={() => editDictData(record.id)}>
          {$t('common.edit')}
        </Button>
        <Popconfirm title={$t('common.confirmDelete')} onConfirm={() => handleDeleteDictData(record.id)}>
          <Button danger size="small">
            {$t('common.delete')}
          </Button>
        </Popconfirm>
      </div>
    )
  }
];

// 搜索表单
const searchForm = reactive({
  dictName: '',
  dictType: '',
  status: null
});

// 折叠状态
const collapsed = ref(false);

// 查看字典数据
async function viewDictData(dictType) {
  selectedDictType.value = dictType.dictType;
  selectedDictTypeName.value = dictType.dictName;

  // 重置分页和搜索参数
  dictDataPagination.current = 1;
  dictDataPagination.pageSize = 20;
  dictDataPagination.total = 0;
  Object.assign(dictDataSearchParams, {
    dictLabel: '',
    dictValue: '',
    status: null
  });

  dictDataModalVisible.value = true;
  await loadDictData();
}

// 加载字典数据
async function loadDictData() {
  if (!selectedDictType.value) return;

  dictDataLoading.value = true;
  try {
    const { data, error } = await fetchGetDictDataList({
      current: dictDataPagination.current,
      size: dictDataPagination.pageSize,
      dictType: selectedDictType.value,
      dictLabel: dictDataSearchParams.dictLabel || undefined,
      dictValue: dictDataSearchParams.dictValue || undefined,
      status: dictDataSearchParams.status
    });

    if (!error && data) {
      dictDataData.value = data.records || [];
      dictDataPagination.total = data.total;
    }
  } catch (error) {
    console.error('加载字典数据失败:', error);
    dictDataData.value = [];
  } finally {
    dictDataLoading.value = false;
  }
}

// 字典类型操作
async function handleBatchDeleteDictType() {
  const { error } = await fetchDeleteDictTypes(dictTypeCheckedRowKeys.value);
  if (!error) {
    onDictTypeBatchDeleted();
  }
}

async function handleDeleteDictType(id) {
  const { error } = await fetchDeleteDictType(id);
  if (!error) {
    onDictTypeDeleted();
  }
}

function editDictType(id) {
  handleEditDictType(id);
}

// 字典数据操作
function handleAddDictData() {
  if (!selectedDictType.value) {
    window.$message?.warning('请先选择字典类型');
    return;
  }
  dictDataOperateType.value = 'add';
  dictDataEditingData.value = null;
  dictDataOperateDrawerVisible.value = true;
}

function editDictData(id) {
  const record = dictDataData.value.find(item => item.id === id);
  dictDataOperateType.value = 'edit';
  dictDataEditingData.value = record;
  dictDataOperateDrawerVisible.value = true;
}

async function handleDeleteDictData(id) {
  const { error } = await fetchDeleteDictData(id);
  if (!error) {
    window.$message?.success('删除成功');
    loadDictData();
  }
}

async function handleBatchDeleteDictData() {
  if (dictDataCheckedRowKeys.value.length === 0) {
    window.$message?.warning('请选择要删除的数据');
    return;
  }

  const { error } = await fetchDeleteDictDatas(dictDataCheckedRowKeys.value);
  if (!error) {
    window.$message?.success('批量删除成功');
    dictDataCheckedRowKeys.value = [];
    loadDictData();
  }
}

// 刷新字典缓存
async function refreshDictCache() {
  const { error } = await fetchRefreshDictCache();
  if (!error) {
    window.$message?.success('刷新字典缓存成功');
  }
}

// 搜索功能
function handleSearch() {
  Object.assign(dictTypeSearchParams, searchForm);
  dictTypeSearchParams.current = 1;
  getDictTypeData();
}

function handleReset() {
  Object.assign(searchForm, {
    dictName: '',
    dictType: '',
    status: null
  });
  Object.assign(dictTypeSearchParams, {
    current: 1,
    size: 10,
    dictName: '',
    dictType: '',
    status: null
  });
  getDictTypeData();
}

// 字典类型提交回调
function handleDictTypeSubmitted() {
  getDictTypeData();
}

// 字典数据搜索功能
function handleDictDataSearch() {
  dictDataPagination.current = 1;
  loadDictData();
}

function handleDictDataReset() {
  Object.assign(dictDataSearchParams, {
    dictLabel: '',
    dictValue: '',
    status: null
  });
  dictDataPagination.current = 1;
  loadDictData();
}

// 字典数据提交回调
function handleDictDataSubmitted() {
  loadDictData();
}

function toggleCollapsed() {
  collapsed.value = !collapsed.value;
}

onMounted(() => {
  getDictTypeData();
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
              <AFormItem label="字典名称" name="dictName" class="m-0">
                <AInput v-model:value="searchForm.dictName" placeholder="请输入字典名称" />
              </AFormItem>
            </ACol>
            <ACol :span="24" :md="12" :lg="8">
              <AFormItem label="字典类型" name="dictType" class="m-0">
                <AInput v-model:value="searchForm.dictType" placeholder="请输入字典类型" />
              </AFormItem>
            </ACol>
            <ACol :span="24" :md="12" :lg="8">
              <AFormItem label="状态" name="status" class="m-0">
                <ASelect
                  v-model:value="searchForm.status"
                  placeholder="请选择状态"
                  :options="[
                    { label: '正常', value: 1 },
                    { label: '停用', value: 0 }
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
      </div>
    </ACard>

    <!-- 字典类型表格区域 -->
    <ACard
      title="字典类型列表"
      :bordered="false"
      :body-style="{ paddingTop: '0px' }"
      class="sm:flex-1-hidden card-wrapper"
    >
      <template #extra>
        <TableHeaderOperation
          v-model:columns="dictTypeColumnChecks"
          :disabled-delete="dictTypeCheckedRowKeys.length === 0"
          :loading="dictTypeLoading"
          @add="handleAddDictType"
          @delete="handleBatchDeleteDictType"
          @refresh="getDictTypeData"
        />
      </template>
      <ATable
        :columns="dictTypeColumns"
        :data-source="dictTypeData"
        size="small"
        :scroll="{ x: 'max-content' }"
        :loading="dictTypeLoading"
        :row-selection="{
          selectedRowKeys: dictTypeCheckedRowKeys,
          onChange: keys => (dictTypeCheckedRowKeys.value = keys)
        }"
        row-key="id"
        :pagination="dictTypePagination"
        class="h-full"
      />
    </ACard>

    <!-- 字典数据弹窗 -->
    <AModal
      v-model:open="dictDataModalVisible"
      :title="`字典数据管理 - ${selectedDictTypeName}`"
      width="90%"
      :footer="null"
      :body-style="{ padding: '16px', height: '75vh', overflow: 'hidden' }"
    >
      <div class="h-full flex-col-stretch gap-16px">
        <!-- 字典数据搜索区域 -->
        <ACard :bordered="false" class="!mb-0">
          <AForm :model="dictDataSearchParams" layout="inline" class="mb-16px">
            <AFormItem label="字典标签">
              <AInput
                v-model:value="dictDataSearchParams.dictLabel"
                placeholder="请输入字典标签"
                style="width: 150px"
                @press-enter="handleDictDataSearch"
              />
            </AFormItem>
            <AFormItem label="字典键值">
              <AInput
                v-model:value="dictDataSearchParams.dictValue"
                placeholder="请输入字典键值"
                style="width: 150px"
                @press-enter="handleDictDataSearch"
              />
            </AFormItem>
            <AFormItem label="状态">
              <ASelect
                v-model:value="dictDataSearchParams.status"
                placeholder="请选择状态"
                style="width: 120px"
                :options="[
                  { label: '正常', value: 1 },
                  { label: '停用', value: 0 }
                ]"
                clearable
              />
            </AFormItem>
            <AFormItem>
              <ASpace>
                <AButton type="primary" @click="handleDictDataSearch">
                  <template #icon>
                    <icon-ic-round-search class="align-sub text-icon" />
                  </template>
                  搜索
                </AButton>
                <AButton @click="handleDictDataReset">
                  <template #icon>
                    <icon-ic-round-refresh class="align-sub text-icon" />
                  </template>
                  重置
                </AButton>
              </ASpace>
            </AFormItem>
          </AForm>

          <div class="flex-y-center justify-between">
            <div class="flex-y-center gap-12px">
              <AButton type="primary" @click="handleAddDictData">
                <template #icon>
                  <icon-ic-round-plus class="align-sub text-icon" />
                </template>
                <span class="ml-8px">新增字典数据</span>
              </AButton>
              <AButton danger :disabled="dictDataCheckedRowKeys.length === 0" @click="handleBatchDeleteDictData">
                <template #icon>
                  <icon-ic-round-delete class="align-sub text-icon" />
                </template>
                <span class="ml-8px">批量删除</span>
              </AButton>
              <AButton @click="loadDictData">
                <template #icon>
                  <icon-mdi-refresh class="align-sub text-icon" />
                </template>
                <span class="ml-8px">刷新</span>
              </AButton>
            </div>
            <div class="text-gray-500">
              字典类型：
              <span class="text-blue-600 font-medium">{{ selectedDictType }}</span>
            </div>
          </div>
        </ACard>

        <!-- 字典数据表格 -->
        <div class="flex-1 overflow-hidden">
          <ATable
            :columns="dictDataColumns"
            :data-source="dictDataData"
            size="small"
            :scroll="{ x: 'max-content', y: 'calc(75vh - 200px)' }"
            :loading="dictDataLoading"
            :row-selection="{
              selectedRowKeys: dictDataCheckedRowKeys,
              onChange: keys => (dictDataCheckedRowKeys.value = keys)
            }"
            row-key="id"
            :pagination="dictDataPagination"
            class="h-full"
          />
        </div>
      </div>
    </AModal>

    <!-- 字典类型操作抽屉 -->
    <DictTypeOperateDrawer
      v-model:visible="dictTypeDrawerVisible"
      :operate-type="dictTypeOperateType"
      :row-data="dictTypeEditingData"
      @submitted="handleDictTypeSubmitted"
    />

    <!-- 字典数据操作抽屉 -->
    <DictDataOperateDrawer
      v-model:visible="dictDataOperateDrawerVisible"
      :operate-type="dictDataOperateType"
      :row-data="dictDataEditingData"
      :dict-type="selectedDictType"
      @submitted="handleDictDataSubmitted"
    />
  </div>
</template>

<style scoped>
.text-icon {
  font-size: 16px;
}

/* 折叠状态下的卡片样式优化 */
.card-wrapper {
  transition: all 0.3s ease;
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

/* 字典数据弹窗样式 */
:deep(.ant-modal-body) {
  padding: 16px !important;
}

:deep(.ant-table-wrapper) {
  height: 100%;
}

:deep(.ant-table-container) {
  height: 100%;
}

/* 超链接样式 */
:deep(.ant-btn-link) {
  color: #1890ff;
  border: none;
  box-shadow: none;
  background: transparent;
}

:deep(.ant-btn-link:hover) {
  color: #40a9ff;
}

:deep(.ant-btn-link:focus) {
  color: #1890ff;
}
</style>
