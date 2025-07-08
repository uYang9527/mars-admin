<script setup lang="tsx">
import { onMounted, reactive, ref } from 'vue';
import { Button, Drawer, Input, Modal, Popconfirm, Select, Space, Tag } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import {
  fetchCacheDetail,
  fetchCacheList,
  fetchClearAllCache,
  fetchClearCacheSpace,
  fetchDeleteCache,
  fetchDeleteCacheBatch
} from '@/service/api';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';

defineOptions({
  name: 'CacheList'
});

const wrapperEl = ref<HTMLElement>();

// 缓存详情抽屉
const detailVisible = ref(false);
const currentCacheDetail = ref<Api.CacheManage.CacheDetail | null>(null);
const detailLoading = ref(false);

// 缓存空间选项
const cacheSpaceOptions = [
  { label: '全部', value: '' },
  { label: '字典类型', value: 'dictType' },
  { label: '字典数据', value: 'dictData' },
  { label: '岗位管理', value: 'post' },
  { label: '用户管理', value: 'user' },
  { label: '系统配置', value: 'config' },
  { label: '菜单管理', value: 'menu' },
  { label: '角色管理', value: 'role' },
  { label: '部门管理', value: 'dept' }
];

// 表格配置
const { columns, data, getData, loading, mobilePagination, updatePagination, searchParams } = useTable({
  apiFn: fetchCacheList,
  apiParams: {
    current: 1,
    size: 20,
    pattern: '',
    cacheSpace: ''
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
      width: 64,
      customRender: ({ index }) => index + 1
    },
    {
      key: 'cache_space',
      title: '缓存空间',
      align: 'center',
      width: 120,
      dataIndex: 'cache_space_name',
      customRender: ({ record }) => {
        const colors = ['blue', 'green', 'orange', 'purple', 'cyan', 'magenta', 'volcano', 'gold'];
        const colorIndex = Math.abs(record.cache_space.charCodeAt(0)) % colors.length;
        return <Tag color={colors[colorIndex]}>{record.cache_space_name}</Tag>;
      }
    },
    {
      key: 'key',
      title: '缓存键',
      align: 'left',
      dataIndex: 'key',
      ellipsis: true,
      width: 300,
      customRender: ({ record }) => (
        <div class="text-12px font-mono">
          <div class="truncate text-gray-800 dark:text-gray-200">{record.key}</div>
        </div>
      )
    },
    {
      key: 'type',
      title: '数据类型',
      align: 'center',
      width: 100,
      dataIndex: 'type',
      customRender: ({ record }) => {
        const typeColors = {
          string: 'green',
          hash: 'blue',
          list: 'orange',
          set: 'purple',
          zset: 'cyan'
        };
        return <Tag color={typeColors[record.type] || 'default'}>{record.type.toUpperCase()}</Tag>;
      }
    },
    {
      key: 'size',
      title: '大小',
      align: 'center',
      width: 100,
      dataIndex: 'size',
      customRender: ({ record }) => <span class="text-12px font-mono">{record.size}</span>
    },
    {
      key: 'ttl_text',
      title: '过期时间',
      align: 'center',
      width: 120,
      dataIndex: 'ttl_text',
      customRender: ({ record }) => {
        let color = 'default';
        if (record.ttl === -1) color = 'green';
        else if (record.ttl < 300) color = 'red';
        else if (record.ttl < 3600) color = 'orange';
        else color = 'blue';

        return <Tag color={color}>{record.ttl_text}</Tag>;
      }
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 200,
      fixed: 'right',
      customRender: ({ record }) => (
        <div class="flex-center gap-8px">
          <Button type="link" size="small" onClick={() => handleViewDetail(record.key)}>
            查看详情
          </Button>
          <Popconfirm title="确定要删除此缓存吗？" onConfirm={() => handleDelete(record.key)}>
            <Button danger size="small">
              删除
            </Button>
          </Popconfirm>
        </div>
      )
    }
  ]
});

const { checkedRowKeys, onBatchDeleted, onDeleted, rowSelection } = useTableOperate(data, getData);

// 搜索表单
const searchForm = reactive({
  pattern: '',
  cacheSpace: ''
});

// 查看缓存详情
async function handleViewDetail(key: string) {
  detailLoading.value = true;
  detailVisible.value = true;
  currentCacheDetail.value = null;

  try {
    const { data: detail, error } = await fetchCacheDetail(key);
    if (!error && detail) {
      currentCacheDetail.value = detail;
    } else {
      window.$message?.error('获取缓存详情失败');
      detailVisible.value = false;
    }
  } catch (e) {
    window.$message?.error('获取缓存详情失败');
    detailVisible.value = false;
  } finally {
    detailLoading.value = false;
  }
}

// 删除单个缓存 - 移除重复的成功提示
async function handleDelete(key: string) {
  try {
    const { error } = await fetchDeleteCache(key);
    if (!error) {
      onDeleted(); // 只调用onDeleted，它会统一显示成功消息
    } else {
      window.$message?.error('删除失败');
    }
  } catch (e) {
    window.$message?.error('删除失败');
  }
}

// 删除详情页面中的缓存
async function handleDeleteInDetail(key: string) {
  try {
    const { error } = await fetchDeleteCache(key);
    if (!error) {
      window.$message?.success('删除成功');
      detailVisible.value = false;
      getData(); // 刷新列表
    } else {
      window.$message?.error('删除失败');
    }
  } catch (e) {
    window.$message?.error('删除失败');
  }
}

// 批量删除缓存
async function handleBatchDelete() {
  if (checkedRowKeys.value.length === 0) {
    window.$message?.warning('请选择要删除的缓存');
    return;
  }

  Modal.confirm({
    title: '批量删除缓存',
    content: `确定要删除选中的 ${checkedRowKeys.value.length} 个缓存吗？删除后无法恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        // 获取选中行的keys
        const selectedKeys = data.value
          .filter((_, index) => checkedRowKeys.value.includes(index))
          .map(item => item.key);

        const { error } = await fetchDeleteCacheBatch(selectedKeys);
        if (!error) {
          onBatchDeleted(); // 使用统一的批量删除成功处理
        } else {
          window.$message?.error('批量删除失败');
        }
      } catch (e) {
        window.$message?.error('批量删除失败');
      }
    }
  });
}

// 清空缓存空间
async function handleClearSpace(space: string) {
  if (!space) {
    window.$message?.warning('请先选择缓存空间');
    return;
  }

  const spaceName = cacheSpaceOptions.find(opt => opt.value === space)?.label || space;

  Modal.confirm({
    title: '清空缓存空间',
    content: `确定要清空"${spaceName}"缓存空间的所有数据吗？此操作不可恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      try {
        const { error } = await fetchClearCacheSpace(space);
        if (!error) {
          window.$message?.success(`已清空"${spaceName}"缓存空间`);
          getData();
        } else {
          window.$message?.error('清空缓存空间失败');
        }
      } catch (e) {
        window.$message?.error('清空缓存空间失败');
      }
    }
  });
}

// 修复缓存序列化问题
async function handleFixCache() {
  Modal.confirm({
    title: '修复缓存序列化问题',
    content: `检测到缓存数据序列化格式不一致，这会导致缓存详情无法正常显示。

是否要清除所有缓存数据并重新生成？
• 清除后需要重新访问各功能模块以生成新的缓存
• 这将修复"¬"字符导致的详情查看问题
• 此操作不可恢复，请确认执行`,
    okText: '立即修复',
    cancelText: '取消',
    async onOk() {
      try {
        const { error, data } = await fetchClearAllCache();
        if (!error) {
          window.$message?.success(data || '缓存修复成功！请重新访问功能模块生成新缓存');
          getData();
        } else {
          window.$message?.error('缓存修复失败');
        }
      } catch (e) {
        window.$message?.error('缓存修复失败');
      }
    }
  });
}

// 搜索功能
function handleSearch() {
  Object.assign(searchParams, searchForm);
  searchParams.current = 1;
  getData();
}

function handleReset() {
  Object.assign(searchForm, {
    pattern: '',
    cacheSpace: ''
  });
  Object.assign(searchParams, {
    current: 1,
    size: 20,
    pattern: '',
    cacheSpace: ''
  });
  getData();
}

// 格式化JSON显示
function formatJsonValue(value: any): string {
  if (typeof value === 'string') {
    try {
      const parsed = JSON.parse(value);
      return JSON.stringify(parsed, null, 2);
    } catch {
      return value;
    }
  }
  return JSON.stringify(value, null, 2);
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
  <div class="flex-col-stretch gap-16px overflow-hidden">
    <div ref="wrapperEl" class="flex-col-stretch gap-16px overflow-hidden">
      <!-- 搜索区域 - 优化布局 -->
      <ACard title="缓存列表管理" :bordered="false" size="small" class="card-wrapper">
        <template #extra>
          <div class="flex-y-center gap-12px">
            <div class="flex-y-center gap-8px text-12px text-gray-500">
              <SvgIcon icon="mdi:database-search" />
              <span>共 {{ mobilePagination.total }} 个缓存键</span>
            </div>
          </div>
        </template>

        <!-- 简化搜索区域布局 -->
        <div class="flex-col gap-16px">
          <div class="flex flex-wrap items-center gap-12px">
            <div class="flex-y-center gap-8px">
              <span class="whitespace-nowrap text-gray-700 dark:text-gray-300">缓存键:</span>
              <AInput v-model:value="searchForm.pattern" placeholder="请输入缓存键关键词" class="w-200px" allow-clear />
            </div>
            <div class="flex-y-center gap-8px">
              <span class="whitespace-nowrap text-gray-700 dark:text-gray-300">缓存空间:</span>
              <ASelect
                v-model:value="searchForm.cacheSpace"
                placeholder="请选择缓存空间"
                class="w-150px"
                :options="cacheSpaceOptions"
                allow-clear
              />
            </div>
            <div class="flex-y-center gap-12px">
              <AButton type="primary" class="flex-y-center gap-4px" @click="handleSearch">
                <SvgIcon icon="mdi:magnify" class="text-14px" />
                搜索
              </AButton>
              <AButton class="flex-y-center gap-4px" @click="handleReset">
                <SvgIcon icon="mdi:refresh" class="text-14px" />
                重置
              </AButton>
              <AButton
                v-if="searchForm.cacheSpace"
                danger
                class="flex-y-center gap-4px"
                @click="handleClearSpace(searchForm.cacheSpace)"
              >
                <SvgIcon icon="mdi:delete-sweep" class="text-14px" />
                清空空间
              </AButton>
              <AButton type="primary" ghost class="flex-y-center gap-4px" @click="handleFixCache">
                <SvgIcon icon="mdi:auto-fix" class="text-14px" />
                修复缓存
              </AButton>
            </div>
          </div>
        </div>
      </ACard>

      <!-- 表格区域 -->
      <ACard :bordered="false" size="small" class="flex-1-hidden card-wrapper">
        <template #title>
          <div class="flex-y-center gap-8px">
            <SvgIcon icon="mdi:table" class="text-blue-500" />
            <span>缓存数据</span>
          </div>
        </template>

        <template #extra>
          <!-- 移除新增功能，只保留批量删除和刷新 -->
          <div class="flex-y-center gap-8px">
            <Button
              v-if="checkedRowKeys.length > 0"
              danger
              size="small"
              class="flex-y-center gap-4px"
              @click="handleBatchDelete"
            >
              <SvgIcon icon="mdi:delete" class="text-12px" />
              批量删除
            </Button>
            <Button size="small" :loading="loading" class="flex-y-center gap-4px" @click="getData">
              <SvgIcon icon="mdi:refresh" class="text-12px" />
              刷新
            </Button>
          </div>
        </template>

        <ATable
          :columns="columns"
          :data-source="data"
          :loading="loading"
          :row-selection="rowSelection"
          :pagination="mobilePagination"
          :scroll="{ x: 1200, y: 'calc(100vh - 420px)' }"
          row-key="key"
          size="small"
          @change="handleTableChange"
        />
      </ACard>
    </div>

    <!-- 缓存详情抽屉 - 优化显示 -->
    <Drawer v-model:open="detailVisible" title="缓存详情" placement="right" :width="600">
      <div v-if="currentCacheDetail" class="space-y-16px">
        <!-- 基本信息 -->
        <div class="rounded-8px bg-gray-50 p-16px dark:bg-gray-800">
          <h3 class="mb-12px flex-y-center gap-8px text-14px font-medium">
            <SvgIcon icon="mdi:information" class="text-blue-500" />
            基本信息
          </h3>
          <div class="text-12px space-y-8px">
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">缓存键:</span>
              <span class="max-w-400px break-all text-right font-mono">{{ currentCacheDetail.key }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">缓存空间:</span>
              <Tag :color="currentCacheDetail.cache_space === 'dictType' ? 'blue' : 'green'">
                {{ currentCacheDetail.cache_space_name }}
              </Tag>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">数据类型:</span>
              <Tag color="purple">{{ currentCacheDetail.type.toUpperCase() }}</Tag>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">数据大小:</span>
              <span class="font-mono">{{ currentCacheDetail.size }}</span>
            </div>
            <div class="flex justify-between">
              <span class="text-gray-600 dark:text-gray-400">过期时间:</span>
              <Tag :color="currentCacheDetail.ttl === -1 ? 'green' : 'orange'">
                {{ currentCacheDetail.ttl_text }}
              </Tag>
            </div>
          </div>
        </div>

        <!-- 缓存值 -->
        <div class="rounded-8px bg-gray-50 p-16px dark:bg-gray-800">
          <h3 class="mb-12px flex-y-center gap-8px text-14px font-medium">
            <SvgIcon icon="mdi:code-json" class="text-green-500" />
            缓存值
          </h3>
          <div class="border rounded-4px bg-white p-12px dark:bg-gray-900">
            <pre class="max-h-400px overflow-auto whitespace-pre-wrap break-words text-12px">{{
              formatJsonValue(currentCacheDetail.value)
            }}</pre>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="flex gap-12px border-t pt-16px">
          <Button type="primary" @click="detailVisible = false">关闭</Button>
          <Button danger @click="handleDeleteInDetail(currentCacheDetail.key)">删除此缓存</Button>
        </div>
      </div>

      <div v-else-if="detailLoading" class="h-200px flex-center">
        <ASpin size="large" />
      </div>

      <div v-else class="h-200px flex-center text-gray-500">暂无数据</div>
    </Drawer>
  </div>
</template>

<style scoped>
.card-wrapper {
  @apply shadow-sm border border-gray-100 dark:border-gray-700;
}

.card-wrapper :deep(.ant-card-head) {
  @apply border-gray-100 dark:border-gray-700;
}

.card-wrapper :deep(.ant-card-head-title) {
  @apply text-14px font-medium;
}
</style>
