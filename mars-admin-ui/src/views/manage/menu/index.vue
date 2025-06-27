<script setup lang="tsx">
import { ref, reactive } from 'vue';
import { Button, Popconfirm, Tag, Space } from 'ant-design-vue';
import { SearchOutlined, ReloadOutlined, PlusOutlined } from '@ant-design/icons-vue';
import { 
  fetchGetMenuTree, 
  fetchDeleteMenu,
  fetchAddMenu,
  fetchUpdateMenu
} from '@/service/api';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import TableHeaderOperation from '@/components/advanced/table-header-operation.vue';
import MenuOperateModal from './modules/menu-operate-modal.vue';

const loading = ref(false);
const menuList = ref<Api.SystemManage.Menu[]>([]);

// 弹窗相关
const modalVisible = ref(false);
const operateType = ref<'add' | 'edit'>('add');
const editingData = ref<Api.SystemManage.Menu | null>(null);

// 获取菜单树列表
async function getMenuList() {
  loading.value = true;
  try {
    const { data, error } = await fetchGetMenuTree();
    if (!error && data) {
      originalMenuList.value = data;
      // 应用搜索过滤
      applySearch();
    }
  } finally {
    loading.value = false;
  }
}

// 应用搜索过滤
function applySearch() {
  if (!searchForm.menuName && searchForm.menuCode === '' && searchForm.status === null) {
    menuList.value = originalMenuList.value;
  } else {
    menuList.value = filterMenuTree(originalMenuList.value);
  }
}

// 递归过滤菜单树
function filterMenuTree(data: Api.SystemManage.Menu[]): Api.SystemManage.Menu[] {
  const result: Api.SystemManage.Menu[] = [];
  
  data.forEach(menu => {
    // 检查当前菜单是否匹配
    const nameMatch = !searchForm.menuName || 
      menu.menuName?.toLowerCase().includes(searchForm.menuName.toLowerCase());
    
    const codeMatch = !searchForm.menuCode || 
      menu.menuCode?.toLowerCase().includes(searchForm.menuCode.toLowerCase());
    
    const statusMatch = searchForm.status === null || 
      menu.status?.toString() === searchForm.status;
    
    const currentMatch = nameMatch && codeMatch && statusMatch;
    
    // 递归过滤子菜单
    const filteredChildren = menu.children ? filterMenuTree(menu.children) : [];
    
    // 如果当前菜单匹配或有匹配的子菜单，则包含此菜单
    if (currentMatch || filteredChildren.length > 0) {
      const filteredMenu = { ...menu };
      if (filteredChildren.length > 0) {
        filteredMenu.children = filteredChildren;
      }
      result.push(filteredMenu);
    }
  });
  
  return result;
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

// 渲染图标
function renderIcon(icon: string) {
  if (!icon) return '-';
  
  // 如果包含冒号，说明是 iconify 格式的图标 (如: mdi:home)
  if (icon.includes(':')) {
    return <SvgIcon icon={icon} class="text-16px" />;
  }
  
  // 否则尝试作为 mdi 图标处理
  return <SvgIcon icon={`mdi:${icon}`} class="text-16px" />;
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
    title: '图标',
    dataIndex: 'icon',
    key: 'icon',
    width: 60,
    align: 'center',
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => {
      return renderIcon(record.icon);
    }
  },
  {
    title: '菜单类型',
    dataIndex: 'menuType',
    key: 'menuType',
    width: 100,
    align: 'center',
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
    title: '路由名称',
    dataIndex: 'routeName',
    key: 'routeName',
    width: 120,
    align: 'center'
  },
  {
    title: '路由地址',
    dataIndex: 'routePath',
    key: 'routePath',
    width: 150,
    align: 'center'
  },
  {
    title: '组件路径',
    dataIndex: 'component',
    key: 'component',
    width: 200,
    align: 'center'
  },
  {
    title: '权限标识',
    dataIndex: 'perms',
    key: 'perms',
    width: 150,
    align: 'center'
  },
  {
    title: '隐藏菜单',
    dataIndex: 'visible',
    key: 'visible',
    width: 80,
    align: 'center',
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => {
      return <Tag color={record.visible === 0 ? 'warning' : 'success'}>
        {record.visible === 0 ? '是' : '否'}
      </Tag>;
    }
  },
  {
    title: '排序',
    dataIndex: 'orderNum',
    key: 'orderNum',
    width: 80,
    align: 'center'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 80,
    align: 'center',
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => {
      return <Tag color={record.status === 1 ? 'success' : 'error'}>
        {record.status === 1 ? '启用' : '禁用'}
      </Tag>;
    }
  },
  {
    title: '操作',
    key: 'operate',
    width: 200,
    align: 'center',
    fixed: 'right',
    customRender: ({ record }: { record: Api.SystemManage.Menu }) => (
      <div class="flex-center gap-8px">
        <Button type="primary" ghost size="small" onClick={() => handleEdit(record)}>
          编辑
        </Button>
        <Button size="small" onClick={() => handleAdd(record.id)}>
          新增子菜单
        </Button>
        <Popconfirm title="确定要删除吗？" onConfirm={() => handleDelete(record.id)}>
          <Button danger size="small">
            删除
          </Button>
        </Popconfirm>
      </div>
    )
  }
];

// 搜索表单
const searchForm = reactive({
  menuName: '',
  menuCode: '',
  status: null
});

// 原始菜单数据
const originalMenuList = ref<Api.SystemManage.Menu[]>([]);

// 统计菜单数量（递归统计）
function countMenus(menus: Api.SystemManage.Menu[]): number {
  let count = 0;
  menus.forEach(menu => {
    count++;
    if (menu.children && menu.children.length > 0) {
      count += countMenus(menu.children);
    }
  });
  return count;
}

function handleSearch() {
  // 应用搜索过滤
  applySearch();
}

function handleReset() {
  Object.assign(searchForm, {
    menuName: '',
    menuCode: '',
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
    <ACard :title="$t('common.search')" :bordered="false" class="card-wrapper">
      <AForm
        :model="searchForm"
        :label-col="{
          span: 5,
          md: 7
        }"
      >
        <ARow :gutter="[16, 16]" wrap>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem label="菜单名称" name="menuName" class="m-0">
              <AInput 
                v-model:value="searchForm.menuName" 
                placeholder="请输入菜单名称" 
                @input="handleSearch"
              />
            </AFormItem>
          </ACol>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem label="菜单编码" name="menuCode" class="m-0">
              <AInput 
                v-model:value="searchForm.menuCode" 
                placeholder="请输入菜单编码" 
                @input="handleSearch"
              />
            </AFormItem>
          </ACol>
          <ACol :span="24" :md="12" :lg="8">
            <AFormItem label="状态" name="status" class="m-0">
              <ASelect
                v-model:value="searchForm.status"
                placeholder="请选择状态"
                :options="[
                  { label: '启用', value: '1' },
                  { label: '禁用', value: '0' }
                ]"
                clearable
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
    </ACard>

    <!-- 表格区域 -->
    <ACard 
      title="菜单列表" 
      :bordered="false"
      :body-style="{ paddingTop: '0px' }"
      class="card-wrapper sm:flex-1-hidden"
    >
      <template #extra>
        <TableHeaderOperation
          :loading="loading"
          @add="handleAdd"
          @refresh="getMenuList"
        >
          <template #prefix>
            <span v-if="searchForm.menuName || searchForm.menuCode || searchForm.status !== null" class="text-gray-500 mr-12px">
              找到 {{ countMenus(menuList) }} 条记录
            </span>
          </template>
        </TableHeaderOperation>
      </template>
      
      <ATable
        :columns="columns"
        :data-source="menuList"
        :loading="loading"
        row-key="id"
        :pagination="false"
        size="small"
        :scroll="{ x: 'max-content' }"
        :default-expand-all-rows="true"
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

.flex-1-hidden {
  flex: 1;
  overflow: hidden;
}

:deep(.ant-card-body) {
  height: 100%;
  overflow: hidden;
}

:deep(.ant-table-wrapper) {
  height: 100%;
}

:deep(.ant-spin-nested-loading) {
  height: 100%;
}

:deep(.ant-spin-container) {
  height: 100%;
  display: flex;
  flex-direction: column;
}

:deep(.ant-table) {
  flex: 1;
}

/* 图标居中对齐 */
:deep(.ant-table-tbody .ant-table-cell) {
  vertical-align: middle;
}
</style>
