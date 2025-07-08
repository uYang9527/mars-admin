<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import {
  fetchDeleteDictData,
  fetchDeleteDictType,
  fetchGetDictTree,
  fetchRefreshAllDictCache,
  fetchRefreshDictDataCache,
  fetchRefreshDictTypeCache
} from '@/service/api/system-manage';
import DictTypeOperateDrawer from './modules/dict-type-operate-drawer.vue';
import DictDataOperateDrawer from './modules/dict-data-operate-drawer.vue';

defineOptions({
  name: 'ManageDict'
});

// 基础状态
const loading = ref(false);
const searchValue = ref('');

// 树相关状态
const treeData = ref<any[]>([]);
const expandedKeys = ref<string[]>([]);
const selectedKeys = ref<string[]>([]);
const selectedNode = ref<any>(null);

// 字典类型操作
const dictTypeDrawerVisible = ref(false);
const dictTypeOperateType = ref<'add' | 'edit'>('add');
const dictTypeEditingData = ref<any>(null);

// 字典数据操作
const dictDataDrawerVisible = ref(false);
const dictDataOperateType = ref<'add' | 'edit'>('add');
const dictDataEditingData = ref<any>(null);
const currentDictType = ref('');

// 字典数据表格列定义
const dictDataColumns = [
  {
    title: '字典标签',
    key: 'dictLabel',
    dataIndex: 'dictLabel',
    align: 'center'
  },
  {
    title: '字典键值',
    key: 'dictValue',
    dataIndex: 'dictValue',
    align: 'center'
  },
  {
    title: '状态',
    key: 'status',
    dataIndex: 'status',
    align: 'center'
  },
  {
    title: '操作',
    key: 'operation'
  }
];

// 过滤后的树数据
const filteredTreeData = computed(() => {
  if (!searchValue.value) return treeData.value;

  return treeData.value
    .filter(node => {
      const typeMatch = node.title.toLowerCase().includes(searchValue.value.toLowerCase());
      const dataMatch = node.children?.some((child: any) =>
        child.title.toLowerCase().includes(searchValue.value.toLowerCase())
      );

      if (typeMatch || dataMatch) {
        return {
          ...node,
          children:
            node.children?.filter((child: any) =>
              child.title.toLowerCase().includes(searchValue.value.toLowerCase())
            ) || []
        };
      }
      return false;
    })
    .filter(Boolean);
});

// 根据父节点key获取子节点
function getChildrenByParentKey(parentKey: string) {
  const parentNode = findNodeByKey(treeData.value, parentKey);
  return parentNode?.children || [];
}

// 根据key查找节点
function findNodeByKey(nodes: any[], key: string): any {
  for (const node of nodes) {
    if (node.key === key) {
      return node;
    }
    if (node.children) {
      const found = findNodeByKey(node.children, key);
      if (found) return found;
    }
  }
  return null;
}

// 加载字典树数据
async function loadTreeData() {
  loading.value = true;
  try {
    const {data, error} = await fetchGetDictTree();

    if (error) {
      window.$message?.error('加载数据失败');
      treeData.value = [];
      return;
    }

    treeData.value = data || [];
  } catch (error) {
    console.error('加载字典树数据失败:', error);
    window.$message?.error('加载数据失败');
    treeData.value = [];
  } finally {
    loading.value = false;
  }
}

// 树节点选择
function handleTreeSelect(selectedKeysValue: string[], {node}: any) {
  selectedKeys.value = selectedKeysValue;

  if (selectedKeysValue.length > 0) {
    // 尝试多种方式获取节点数据
    const nodeData = node.dataRef || node.data || node;
    selectedNode.value = {
      key: node.key,
      title: node.title,
      nodeType: nodeData.nodeType,
      data: nodeData.data || nodeData
    };
  } else {
    selectedNode.value = null;
  }
}

// 树节点展开
function handleTreeExpand(expandedKeysValue: string[]) {
  expandedKeys.value = expandedKeysValue;
}

// 字典类型操作
function handleAddDictType() {
  dictTypeOperateType.value = 'add';
  dictTypeEditingData.value = null;
  dictTypeDrawerVisible.value = true;
}

function handleEditDictType(data: any) {
  // 正确获取数据：从data.data中获取
  const actualData = data?.data || data;

  dictTypeOperateType.value = 'edit';
  dictTypeEditingData.value = actualData;
  dictTypeDrawerVisible.value = true;
}

async function handleDeleteDictType(data: any) {
  // 正确获取ID：从data.data中获取
  const actualData = data?.data;
  const idValue = actualData?.id;

  if (!actualData?.id) {
    window.$message?.error('数据错误，无法删除');
    return;
  }

  try {
    const {error} = await fetchDeleteDictType(actualData.id);
    if (!error) {
      window.$message?.success('删除成功');
      loadTreeData();
      // 清空选中状态
      if (selectedNode.value?.data?.id === actualData.id) {
        selectedKeys.value = [];
        selectedNode.value = null;
      }
    }
  } catch (error) {
    console.error('删除字典类型失败:', error);
    window.$message?.error('删除失败');
  }
}

// 字典数据操作
function handleAddDictData(typeData: any) {
  // 正确获取数据：优先从typeData.data中获取，如果没有则从typeData中获取
  const actualData = typeData?.data || typeData;

  dictDataOperateType.value = 'add';
  dictDataEditingData.value = null;
  currentDictType.value = actualData?.dictType || '';
  dictDataDrawerVisible.value = true;
}

function handleEditDictData(data: any) {
  // 正确获取数据：优先从data.data中获取，如果没有则从data中获取（兼容右侧详情页面的调用）
  const actualData = data?.data || data;

  dictDataOperateType.value = 'edit';
  dictDataEditingData.value = actualData;
  currentDictType.value = actualData?.dictType || '';
  dictDataDrawerVisible.value = true;
}

async function handleDeleteDictData(data: any) {
  // 正确获取ID：优先从data.data中获取，如果没有则从data中获取（兼容右侧详情页面的调用）
  const actualData = data?.data || data;
  const idValue = actualData?.id;

  if (!actualData?.id) {
    window.$message?.error('数据错误，无法删除');
    return;
  }

  try {
    const {error} = await fetchDeleteDictData(actualData.id);
    if (!error) {
      window.$message?.success('删除成功');
      loadTreeData();
      // 清空选中状态
      if (selectedNode.value?.data?.id === actualData.id) {
        selectedKeys.value = [];
        selectedNode.value = null;
      }
    }
  } catch (error) {
    console.error('删除字典数据失败:', error);
    window.$message?.error('删除失败');
  }
}

// 刷新缓存
async function handleRefreshAllCache() {
  try {
    const {error} = await fetchRefreshAllDictCache();
    if (!error) {
      window.$message?.success('刷新所有字典缓存成功');
    }
  } catch (err) {
    console.error('刷新所有缓存失败:', err);
    window.$message?.error('刷新缓存失败，请重试');
  }
}

async function handleRefreshTypeCache() {
  try {
    const {error} = await fetchRefreshDictTypeCache();
    if (!error) {
      window.$message?.success('刷新字典类型缓存成功');
    }
  } catch (err) {
    console.error('刷新类型缓存失败:', err);
    window.$message?.error('刷新缓存失败，请重试');
  }
}

async function handleRefreshDataCache() {
  try {
    const {error} = await fetchRefreshDictDataCache();
    if (!error) {
      window.$message?.success('刷新字典数据缓存成功');
    }
  } catch (err) {
    console.error('刷新数据缓存失败:', err);
    window.$message?.error('刷新缓存失败，请重试');
  }
}

onMounted(() => {
  loadTreeData();
});
</script>

<template>
  <div class="h-full flex-col-stretch gap-16px overflow-hidden">
    <!-- 主体内容区域 - 左右分栏 -->
    <div class="h-full flex gap-16px overflow-hidden">
      <!-- 左侧：字典树 -->
      <div class="w-400px flex-col-stretch">
        <ACard title="字典管理" :bordered="false" :body-style="{ padding: '16px' }" class="h-full">
          <template #extra>
            <ADropdown placement="bottomRight" >
              <AButton type="default">
                <template #icon>
                  <icon-ic-round-refresh class="align-sub text-icon"/>
                </template>
                刷新缓存
                <icon-ic-round-keyboard-arrow-down class="ml-1 align-sub text-icon"/>
              </AButton>
              <template #overlay>
                <AMenu>
                  <AMenuItem @click="handleRefreshAllCache">
                    <template #icon>
                      <icon-ic-round-refresh class="text-blue-500"/>
                    </template>
                    刷新所有缓存
                  </AMenuItem>
                  <AMenuItem @click="handleRefreshTypeCache">
                    <template #icon>
                      <icon-ic-round-category class="text-green-500"/>
                    </template>
                    刷新类型缓存
                  </AMenuItem>
                  <AMenuItem @click="handleRefreshDataCache">
                    <template #icon>
                      <icon-ic-round-label class="text-orange-500"/>
                    </template>
                    刷新数据缓存
                  </AMenuItem>
                </AMenu>
              </template>
            </ADropdown>
            <AButton type="primary" size="small" @click="handleAddDictType" style="margin-left: 10px">
              <template #icon>
                <icon-ic-round-plus class="align-sub text-icon"/>
              </template>
              新增类型
            </AButton>
          </template>

          <div class="h-full flex-col-stretch gap-12px">
            <!-- 搜索框 -->
            <AInput v-model:value="searchValue" placeholder="搜索字典..." allow-clear>
              <template #prefix>
                <icon-ic-round-search class="text-gray-400"/>
              </template>
            </AInput>

            <!-- 树容器 -->
            <div class="flex-1 overflow-auto">
              <ASpin :spinning="loading">
                <ATree
                  v-if="filteredTreeData.length > 0"
                  :tree-data="filteredTreeData"
                  :expanded-keys="expandedKeys"
                  :selected-keys="selectedKeys"
                  show-line
                  @select="handleTreeSelect"
                  @expand="handleTreeExpand"
                >
                  <template #icon="{ nodeType }">
                    <icon-ic-round-category v-if="nodeType === 'dictType'" class="text-base text-blue-500"/>
                    <icon-ic-round-label v-else class="text-base text-green-500"/>
                  </template>

                  <template #title="nodeData">
                    <div class="group min-h-[24px] w-full flex items-center justify-between pr-2">
                      <div class="min-w-0 flex flex-1 items-center gap-2">
                        <span class="truncate text-sm leading-6">{{ nodeData.title }}</span>
                        <!--                        <ATag v-if="nodeData.nodeType === 'dictType'" size="small" color="blue">-->
                        <!--                          {{ nodeData.data?.dictType }}-->
                        <!--                        </ATag>-->
                        <!--                        <ATag-->
                        <!--                          v-else-if="nodeData.data?.status !== undefined"-->
                        <!--                          size="small"-->
                        <!--                          :color="nodeData.data.status === 1 ? 'success' : 'error'"-->
                        <!--                        >-->
                        <!--                          {{ nodeData.data.status === 1 ? '正常' : '停用' }}-->
                        <!--                        </ATag>-->
                      </div>

                      <!-- 操作按钮 -->
                      <div class="flex items-center gap-1 opacity-0 transition-opacity group-hover:opacity-100">
                        <!-- 字典类型操作 -->
                        <template v-if="nodeData.nodeType === 'dictType'">
                          <AButton
                            type="text"
                            size="small"
                            title="新增字典数据"
                            @click.stop="handleAddDictData(nodeData.data)"
                          >
                            <icon-ic-round-plus class="text-xs text-green-600"/>
                          </AButton>

                          <AButton
                            type="text"
                            size="small"
                            title="编辑字典类型"
                            @click.stop="handleEditDictType(nodeData.data)"
                          >
                            <icon-ic-round-edit class="text-xs text-blue-600"/>
                          </AButton>
                          <APopconfirm title="确定删除此字典类型吗？" @confirm="handleDeleteDictType(nodeData.data)">
                            <AButton type="text" size="small" title="删除字典类型" @click.stop>
                              <icon-ic-round-delete class="text-xs text-red-600"/>
                            </AButton>
                          </APopconfirm>
                        </template>

                        <!-- 字典数据操作 -->
                        <template v-else>
                          <AButton
                            type="text"
                            size="small"
                            title="编辑字典数据"
                            @click.stop="handleEditDictData(nodeData.data)"
                          >
                            <icon-ic-round-edit class="text-xs text-blue-600"/>
                          </AButton>
                          <APopconfirm title="确定删除此字典数据吗？" @confirm="handleDeleteDictData(nodeData.data)">
                            <AButton type="text" size="small" title="删除字典数据" @click.stop>
                              <icon-ic-round-delete class="text-xs text-red-600"/>
                            </AButton>
                          </APopconfirm>
                        </template>
                      </div>
                    </div>
                  </template>
                </ATree>
                <AEmpty v-else description="暂无字典数据"/>
              </ASpin>
            </div>
          </div>
        </ACard>
      </div>

      <!-- 右侧：详情信息 -->
      <div class="flex-col-stretch flex-1">
        <ACard :bordered="false" class="h-full">
          <div v-if="selectedNode" class="h-full overflow-auto p-4">
            <!-- 字典类型详情 -->
            <div v-if="selectedNode.nodeType === 'dictType'" class="space-y-4">
              <div class="flex items-center gap-3">
                <h3 class="m-0 text-lg font-medium">字典类型详情</h3>
                <!--                <ATag color="blue">{{ selectedNode.data.dictType }}</ATag>-->
              </div>

              <div class="rounded-lg p-4">
                <div class="grid grid-cols-2 gap-4">
                  <div>
                    <label class="text-sm text-gray-600">字典名称：</label>
                    <div class="font-medium">{{ selectedNode.data.dictName }}</div>
                  </div>
                  <div>
                    <label class="text-sm text-gray-600">字典类型：</label>
                    <div class="font-medium">{{ selectedNode.data.dictType }}</div>
                  </div>
                  <div>
                    <label class="text-sm text-gray-600">状态：</label>
                    <ATag :color="selectedNode.data.status === 1 ? 'success' : 'error'">
                      {{ selectedNode.data.status === 1 ? '正常' : '停用' }}
                    </ATag>
                  </div>
                  <div>
                    <label class="text-sm text-gray-600">创建时间：</label>
                    <div class="text-sm">{{ selectedNode.data.createTime }}</div>
                  </div>
                  <div v-if="selectedNode.data.remark" class="col-span-2">
                    <label class="text-sm text-gray-600">备注：</label>
                    <div class="mt-1 text-sm">{{ selectedNode.data.remark }}</div>
                  </div>
                </div>
              </div>

              <div class="flex items-center justify-between pt-4">
                <h4 class="text-base font-medium">字典数据列表</h4>
                <AButton type="primary" size="small" @click="handleAddDictData(selectedNode.data)">
                  <template #icon>
                    <icon-ic-round-plus class="align-sub text-icon"/>
                  </template>
                  新增数据
                </AButton>
              </div>

              <div class="max-h-400px overflow-auto">
                <ATable
                  v-if="getChildrenByParentKey(selectedNode.key).length"
                  :columns="dictDataColumns"
                  :data-source="getChildrenByParentKey(selectedNode.key)"
                  :pagination="false"
                  size="small"
                  row-key="key"
                >
                  <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'dictLabel'">
                      <span class="font-medium">{{ record.data.dictLabel }}</span>
                    </template>
                    <template v-else-if="column.key === 'dictValue'">
                      <span class="text-gray-600">{{ record.data.dictValue }}</span>
                    </template>
                    <template v-else-if="column.key === 'status'">
                      <ATag :color="record.data.status === 1 ? 'success' : 'error'" size="small">
                        {{ record.data.status === 1 ? '正常' : '停用' }}
                      </ATag>
                    </template>
                    <template v-else-if="column.key === 'operation'">
                      <div class="flex items-center gap-1">
                        <AButton type="text" size="small" @click="handleEditDictData(record.data)">
                          <icon-ic-round-edit class="text-blue-600"/>
                        </AButton>
                        <APopconfirm title="确定删除吗？" @confirm="handleDeleteDictData(record.data)">
                          <AButton type="text" size="small">
                            <icon-ic-round-delete class="text-red-600"/>
                          </AButton>
                        </APopconfirm>
                      </div>
                    </template>
                  </template>
                </ATable>
                <AEmpty v-else description="暂无字典数据"/>
              </div>
            </div>

            <!-- 字典数据详情 -->
            <div v-else class="space-y-4">
              <div class="flex items-center gap-3">
                <h3 class="m-0 text-lg font-medium">字典数据详情</h3>
                <ATag :color="selectedNode.data.status === 1 ? 'success' : 'error'">
                  {{ selectedNode.data.status === 1 ? '正常' : '停用' }}
                </ATag>
              </div>

              <div class="rounded-lg p-4">
                <div class="grid grid-cols-2 gap-4">
                  <div>
                    <label class="text-sm text-gray-600">字典标签：</label>
                    <div class="font-medium">{{ selectedNode.data.dictLabel }}</div>
                  </div>
                  <div>
                    <label class="text-sm text-gray-600">字典键值：</label>
                    <div class="font-medium">{{ selectedNode.data.dictValue }}</div>
                  </div>
                  <div>
                    <label class="text-sm text-gray-600">字典类型：</label>
                    <div class="font-medium">{{ selectedNode.data.dictType }}</div>
                  </div>
                  <div>
                    <label class="text-sm text-gray-600">字典排序：</label>
                    <div class="font-medium">{{ selectedNode.data.dictSort }}</div>
                  </div>
                  <div>
                    <label class="text-sm text-gray-600">是否默认：</label>
                    <ATag :color="selectedNode.data.isDefault === 'Y' ? 'success' : 'default'">
                      {{ selectedNode.data.isDefault === 'Y' ? '是' : '否' }}
                    </ATag>
                  </div>
                  <div>
                    <label class="text-sm text-gray-600">创建时间：</label>
                    <div class="text-sm">{{ selectedNode.data.createTime }}</div>
                  </div>
                  <div v-if="selectedNode.data.remark" class="col-span-2">
                    <label class="text-sm text-gray-600">备注：</label>
                    <div class="mt-1 text-sm">{{ selectedNode.data.remark }}</div>
                  </div>
                  <div v-if="selectedNode.data.cssClass" class="col-span-2">
                    <label class="text-sm text-gray-600">CSS类名：</label>
                    <div class="mt-1 text-sm font-mono">{{ selectedNode.data.cssClass }}</div>
                  </div>
                  <div v-if="selectedNode.data.listClass" class="col-span-2">
                    <label class="text-sm text-gray-600">列表类名：</label>
                    <div class="mt-1 text-sm font-mono">{{ selectedNode.data.listClass }}</div>
                  </div>
                </div>
              </div>

              <div class="flex gap-2 pt-4">
                <AButton type="primary" @click="handleEditDictData(selectedNode.data)">
                  <template #icon>
                    <icon-ic-round-edit class="align-sub text-icon"/>
                  </template>
                  编辑数据
                </AButton>
                <APopconfirm title="确定删除此字典数据吗？" @confirm="handleDeleteDictData(selectedNode.data)">
                  <AButton danger>
                    <template #icon>
                      <icon-ic-round-delete class="align-sub text-icon"/>
                    </template>
                    删除数据
                  </AButton>
                </APopconfirm>
              </div>
            </div>
          </div>

          <div v-else class="h-full flex-center">
            <AEmpty description="请在左侧选择字典类型或字典数据以查看详细信息">
              <template #image>
                <icon-mdi:folder-search-outline class="text-6xl text-gray-300"/>
              </template>
            </AEmpty>
          </div>
        </ACard>
      </div>
    </div>

    <!-- 字典类型操作抽屉 -->
    <DictTypeOperateDrawer
      v-model:visible="dictTypeDrawerVisible"
      :operate-type="dictTypeOperateType"
      :row-data="dictTypeEditingData"
      @submitted="loadTreeData"
    />

    <!-- 字典数据操作抽屉 -->
    <DictDataOperateDrawer
      v-model:visible="dictDataDrawerVisible"
      :operate-type="dictDataOperateType"
      :row-data="dictDataEditingData"
      :dict-type="currentDictType"
      @submitted="loadTreeData"
    />
  </div>
</template>

<style scoped>
/* 悬停按钮样式 */
.group:hover .group-hover\:opacity-100 {
  opacity: 1;
}

.transition-opacity {
  transition: opacity 0.15s ease-in-out;
}

/* Tree组件图标和文字对齐样式 */
:deep(.ant-tree .ant-tree-node-content-wrapper) {
  display: flex;
  align-items: center;
  line-height: 24px;
  min-height: 24px;
}

:deep(.ant-tree .ant-tree-iconEle) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  margin-right: 4px;
  line-height: 1;
}

:deep(.ant-tree .ant-tree-title) {
  display: flex;
  align-items: center;
  flex: 1;
  min-height: 24px;
  line-height: 24px;
}

/* 确保树节点的垂直对齐 */
:deep(.ant-tree .ant-tree-treenode) {
  display: flex;
  align-items: center;
}

:deep(.ant-tree .ant-tree-switcher) {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
