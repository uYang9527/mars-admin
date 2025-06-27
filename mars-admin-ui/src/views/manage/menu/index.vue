<script setup lang="tsx">
import { ref, reactive } from 'vue';
import { Button, Popconfirm, Tag, Space } from 'ant-design-vue';
import { 
  fetchGetMenuList, 
  fetchDeleteMenu,
  fetchAddMenu,
  fetchUpdateMenu
} from '@/service/api';
import { $t } from '@/locales';
import MenuOperateModal from './modules/menu-operate-modal.vue';

const loading = ref(false);
const menuList = ref<Api.SystemManage.Menu[]>([]);

// 弹窗相关
const modalVisible = ref(false);
const operateType = ref<'add' | 'edit'>('add');
const editingData = ref<Api.SystemManage.Menu | null>(null);

// 获取菜单列表
async function getMenuList() {
  loading.value = true;
  try {
    const { data, error } = await fetchGetMenuList();
    if (!error && data) {
      menuList.value = data;
    }
  } finally {
    loading.value = false;
  }
}

// 新增菜单
function handleAdd(parentId?: number) {
  operateType.value = 'add';
  editingData.value = parentId ? { parentId } as Api.SystemManage.Menu : null;
  modalVisible.value = true;
}

// 编辑菜单
function handleEdit(record: Api.SystemManage.Menu) {
  operateType.value = 'edit';
  editingData.value = record;
  modalVisible.value = true;
}

// 删除菜单
async function handleDelete(id: number) {
  const { error } = await fetchDeleteMenu(id);
  if (!error) {
    window.$message?.success('删除成功');
    getMenuList();
  }
}

// 表格列配置
const columns = [
  {
    title: '菜单名称',
    dataIndex: 'menuName',
    key: 'menuName',
    width: 200
  },
  {
    title: '菜单编码',
    dataIndex: 'menuCode',
    key: 'menuCode',
    width: 150
  },
  {
    title: '菜单类型',
    dataIndex: 'menuType',
    key: 'menuType',
    width: 100,
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => {
      const typeMap = {
        'M': { label: '目录', color: 'blue' },
        'C': { label: '菜单', color: 'green' },
        'F': { label: '按钮', color: 'orange' }
      };
      const type = typeMap[record.menuType];
      return <Tag color={type.color}>{type.label}</Tag>;
    }
  },
  {
    title: '路由地址',
    dataIndex: 'routePath',
    key: 'routePath',
    width: 150
  },
  {
    title: '组件路径',
    dataIndex: 'component',
    key: 'component',
    width: 200
  },
  {
    title: '权限标识',
    dataIndex: 'perms',
    key: 'perms',
    width: 150
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 80,
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => {
      return <Tag color={record.status === '0' ? 'success' : 'error'}>
        {record.status === '0' ? '启用' : '禁用'}
      </Tag>;
    }
  },
  {
    title: '操作',
    key: 'operate',
    width: 200,
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => (
      <Space size="small">
        <Button type="primary" ghost size="small" onClick={() => handleEdit(record)}>
          编辑
        </Button>
        <Button size="small" onClick={() => handleAdd(record.id)}>
          新增
        </Button>
        <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.id)}>
          <Button danger size="small">
            删除
          </Button>
        </Popconfirm>
      </Space>
    )
  }
];

// 搜索表单
const searchForm = reactive({
  menuName: '',
  status: null
});

function handleSearch() {
  // 这里可以添加搜索逻辑
  getMenuList();
}

function handleReset() {
  Object.assign(searchForm, {
    menuName: '',
    status: null
  });
  getMenuList();
}

// 提交后刷新列表
function handleSubmitted() {
  modalVisible.value = false;
  getMenuList();
}

// 初始化
getMenuList();
</script>

<template>
  <div class="flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <!-- 搜索区域 -->
    <ACard title="查询条件" :bordered="false">
      <AForm :model="searchForm" layout="inline">
        <AFormItem label="菜单名称">
          <AInput v-model:value="searchForm.menuName" placeholder="请输入菜单名称" allow-clear />
        </AFormItem>
        <AFormItem label="状态">
          <ASelect v-model:value="searchForm.status" placeholder="请选择状态" allow-clear style="width: 120px">
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
      title="菜单列表" 
      :bordered="false"
      :body-style="{ paddingTop: '0px' }"
      class="card-wrapper sm:flex-1-hidden"
    >
      <template #extra>
        <ASpace>
          <AButton type="primary" @click="handleAdd()">
            <template #icon><PlusOutlined /></template>
            新增菜单
          </AButton>
          <AButton @click="getMenuList">
            <template #icon><ReloadOutlined /></template>
            刷新
          </AButton>
        </ASpace>
      </template>
      
      <ATable
        :columns="columns"
        :data-source="menuList"
        :loading="loading"
        row-key="id"
        :pagination="false"
        size="small"
        :scroll="{ x: 'max-content' }"
        class="h-full"
      />
    </ACard>

    <!-- 菜单操作弹窗 -->
    <MenuOperateModal
      v-model:visible="modalVisible"
      :operate-type="operateType"
      :row-data="editingData"
      @submitted="handleSubmitted"
    />
  </div>
</template>

<style scoped>
:deep(.ant-table-tbody > tr > td) {
  padding: 8px;
}
</style>
