<script setup lang="tsx">
import { reactive, ref, onMounted } from 'vue';
import { Button, Popconfirm, Space, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { 
  fetchGetDictTypeList, 
  fetchDeleteDictType, 
  fetchDeleteDictTypes,
  fetchRefreshDictCache,
  fetchGetNormalDictTypes,
  fetchGetDictDataList,
  fetchDeleteDictData,
  fetchDeleteDictDatas
} from '@/service/api/system-manage';
import { $t } from '@/locales';
import { useAppStore } from '@/store/modules/app';
import DictTypeOperateDrawer from './modules/dict-type-operate-drawer.vue';
import DictDataOperateDrawer from './modules/dict-data-operate-drawer.vue';
import DictTypeSearch from './modules/dict-type-search.vue';
import DictDataSearch from './modules/dict-data-search.vue';
import { SearchOutlined, PlusOutlined, ReloadOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons-vue';

defineOptions({
  name: 'ManageDict'
});

const appStore = useAppStore();
const wrapperEl = ref<HTMLElement>();

// 字典类型相关
const dictTypeList = ref([]);
const dictTypeLoading = ref(false);
const selectedDictType = ref('');
const selectedDictTypeName = ref('');

// 字典类型查询参数
const dictTypeSearchParams = reactive({
  current: 1,
  size: 10,
  dictName: '',
  dictType: '',
  status: null
});

// 静态测试数据
const staticDictTypes = [
  { id: 1, dictName: '用户性别', dictType: 'sys_user_sex', status: 1, remark: '用户性别列表' },
  { id: 2, dictName: '显示状态', dictType: 'sys_show_hide', status: 1, remark: '显示状态列表' },
  { id: 3, dictName: '系统状态', dictType: 'sys_normal_disable', status: 1, remark: '系统状态列表' },
  { id: 4, dictName: '是否', dictType: 'sys_yes_no', status: 1, remark: '系统是否列表' }
];

// 字典类型操作相关
const dictTypeDrawerVisible = ref(false);
const dictTypeOperateType = ref('add');
const dictTypeEditingData = ref(null);

// 字典数据相关
const {
  columns: dictDataColumns,
  columnChecks: dictDataColumnChecks,
  data: dictDataData,
  getData: getDictDataData,
  loading: dictDataLoading,
  pagination: dictDataPagination,
  searchParams: dictDataSearchParams
} = useTable({
  apiFn: fetchGetDictDataList,
  apiParams: {
    current: 1,
    size: 10,
    dictLabel: null,
    dictValue: null,
    dictType: null,
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
  ]
});

const {
  drawerVisible: dictDataDrawerVisible,
  operateType: dictDataOperateType,
  editingData: dictDataEditingData,
  handleAdd: handleAddDictData,
  handleEdit: handleEditDictData,
  checkedRowKeys: dictDataCheckedRowKeys,
  onBatchDeleted: onDictDataBatchDeleted,
  onDeleted: onDictDataDeleted
} = useTableOperate(dictDataData, getDictDataData);

// 加载字典类型列表
async function loadDictTypes() {
  dictTypeLoading.value = true;
  try {
    // 使用fetchGetDictTypeList替代fetchGetNormalDictTypes，以支持查询功能
    const { data } = await fetchGetDictTypeList(dictTypeSearchParams);
    if (data && data.records) {
      dictTypeList.value = data.records;
      console.log('字典类型列表数据:', dictTypeList.value);
    } else {
      console.warn('获取字典类型数据为空');
      dictTypeList.value = staticDictTypes;
    }
  } catch (error) {
    console.error('加载字典类型列表失败', error);
    dictTypeList.value = staticDictTypes;
  } finally {
    dictTypeLoading.value = false;
  }
}

// 字典类型搜索
function handleDictTypeSearch(model) {
  Object.assign(dictTypeSearchParams, model);
  dictTypeSearchParams.current = 1;
  loadDictTypes();
}

// 字典类型选择
function selectDictType(dictType) {
  selectedDictType.value = dictType.dictType;
  selectedDictTypeName.value = dictType.dictName;
  
  // 更新字典数据搜索参数
  dictDataSearchParams.dictType = selectedDictType.value;
  getDictDataData();
}

// 字典类型操作
function handleAddDictType() {
  dictTypeOperateType.value = 'add';
  dictTypeEditingData.value = null;
  dictTypeDrawerVisible.value = true;
}

function handleEditDictType(dictType) {
  dictTypeOperateType.value = 'edit';
  dictTypeEditingData.value = dictType;
  dictTypeDrawerVisible.value = true;
}

async function handleDeleteDictType(dictType) {
  const { error } = await fetchDeleteDictType(dictType.id);
  if (!error) {
    window.$message?.success('删除成功');
    loadDictTypes();
    
    // 如果删除的是当前选中的字典类型，清空选择
    if (selectedDictType.value === dictType.dictType) {
      selectedDictType.value = '';
      selectedDictTypeName.value = '';
      dictDataSearchParams.dictType = '';
      getDictDataData();
    }
  }
}

// 字典数据操作
async function handleBatchDeleteDictData() {
  const { error } = await fetchDeleteDictDatas(dictDataCheckedRowKeys.value);
  if (!error) {
    onDictDataBatchDeleted();
  }
}

async function handleDeleteDictData(id) {
  const { error } = await fetchDeleteDictData(id);
  if (!error) {
    onDictDataDeleted();
  }
}

function editDictData(id) {
  handleEditDictData(id);
}

function handleAddDictDataWithType() {
  if (!selectedDictType.value) {
    window.$message?.warning('请先选择字典类型');
    return;
  }
  handleAddDictData();
}

// 刷新字典缓存
async function refreshDictCache() {
  const { error } = await fetchRefreshDictCache();
  if (!error) {
    window.$message?.success('刷新字典缓存成功');
    loadDictTypes();
  }
}

// 字典类型提交回调
function handleDictTypeSubmitted() {
  loadDictTypes();
}

onMounted(() => {
  loadDictTypes();
});
</script>

<template>
  <div ref="wrapperEl" class="dict-container flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <div class="dict-header flex-y-center justify-between mb-16px">
      <h2 class="text-lg font-medium">字典管理</h2>
      <ASpace>
        <AButton type="primary" @click="handleAddDictType">
          <template #icon><PlusOutlined /></template>
          新增字典类型
        </AButton>
        <AButton type="primary" @click="refreshDictCache">
          <template #icon><ReloadOutlined /></template>
          刷新缓存
        </AButton>
      </ASpace>
    </div>
    
    <div class="dict-content flex-1 flex h-full">
      <!-- 左侧字典类型列表 -->
      <div class="dict-type-container mr-16px">
        <!-- 字典类型搜索 -->
        <DictTypeSearch v-model:model="dictTypeSearchParams" @search="handleDictTypeSearch" @reset="loadDictTypes" />
        
        <!-- 字典类型列表 -->
        <ACard title="字典类型" class="dict-type-list mt-16px" :bordered="false" :body-style="{ height: 'calc(100% - 58px)', overflow: 'auto', padding: '12px' }">
          <ASpin :spinning="dictTypeLoading">
            <AList item-layout="horizontal" :data-source="dictTypeList">
              <template #renderItem="{ item }">
                <AListItem 
                  :class="{ 'selected-dict-type': selectedDictType === item.dictType }"
                  @click="selectDictType(item)"
                >
                  <div class="flex-y-center justify-between w-full px-2">
                    <div>
                      <div class="font-medium">{{ item.dictName }}</div>
                      <div class="text-xs text-gray-500">{{ item.dictType }}</div>
                    </div>
                    <ASpace>
                      <AButton type="link" size="small" @click.stop="handleEditDictType(item)">
                        <EditOutlined />
                      </AButton>
                      <Popconfirm title="确定要删除该字典类型吗？" @confirm.stop="handleDeleteDictType(item)">
                        <AButton type="link" size="small" danger @click.stop>
                          <DeleteOutlined />
                        </AButton>
                      </Popconfirm>
                    </ASpace>
                  </div>
                </AListItem>
              </template>
              <template #empty>
                <AEmpty description="暂无字典类型" />
              </template>
            </AList>
          </ASpin>
        </ACard>
      </div>
      
      <!-- 右侧字典数据表格 -->
      <div class="dict-data-content flex-1">
        <!-- 字典数据搜索区域 -->
        <DictDataSearch v-model:model="dictDataSearchParams" @search="getDictDataData" @reset="getDictDataData" />
        
        <!-- 字典数据表格区域 -->
        <ACard
          :title="selectedDictTypeName ? `字典数据列表 - ${selectedDictTypeName}` : '字典数据列表'"
          :bordered="false"
          :body-style="{ paddingTop: '0px' }"
          class="card-wrapper mt-16px"
        >
          <template #extra>
            <ASpace>
              <AButton type="primary" :disabled="!selectedDictType" @click="handleAddDictDataWithType">
                <template #icon><PlusOutlined /></template>
                新增字典数据
              </AButton>
              <AButton danger :disabled="dictDataCheckedRowKeys.length === 0" @click="handleBatchDeleteDictData">
                <template #icon><DeleteOutlined /></template>
                批量删除
              </AButton>
              <AButton @click="getDictDataData">
                <template #icon><ReloadOutlined /></template>
                刷新
              </AButton>
            </ASpace>
          </template>
          <ATable
            :columns="dictDataColumns"
            :data-source="dictDataData"
            size="small"
            :scroll="{ x: 'max-content' }"
            :loading="dictDataLoading"
            :row-selection="{
              selectedRowKeys: dictDataCheckedRowKeys,
              onChange: keys => (dictDataCheckedRowKeys.value = keys)
            }"
            row-key="id"
            :pagination="dictDataPagination"
            class="h-full"
          />
        </ACard>
      </div>
    </div>
    
    <!-- 字典类型操作抽屉 -->
    <DictTypeOperateDrawer
      v-model:visible="dictTypeDrawerVisible"
      :operate-type="dictTypeOperateType"
      :row-data="dictTypeEditingData"
      @submitted="handleDictTypeSubmitted"
    />
    
    <!-- 字典数据操作抽屉 -->
    <DictDataOperateDrawer
      v-model:visible="dictDataDrawerVisible"
      :operate-type="dictDataOperateType"
      :row-data="dictDataEditingData"
      :dict-type="selectedDictType"
      @submitted="getDictDataData"
    />
  </div>
</template>

<style scoped>
.dict-container {
  height: calc(100vh - 180px);
}

.dict-content {
  height: calc(100% - 50px);
}

.dict-type-container {
  width: 280px;
  display: flex;
  flex-direction: column;
}

.dict-type-list {
  flex: 1;
  height: calc(100% - 150px);
  overflow: auto;
}

.dict-data-content {
  height: 100%;
  overflow: auto;
}

.card-wrapper {
  @apply flex-col-stretch gap-16px overflow-hidden;
}

.selected-dict-type {
  background-color: #f0f7ff;
  border-left: 3px solid #1890ff;
}

.ant-list-item {
  cursor: pointer;
  padding: 8px 0;
  border-radius: 4px;
  margin-bottom: 4px;
}

.ant-list-item:hover {
  background-color: #f5f5f5;
}
</style>

