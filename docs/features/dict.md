# 字典管理

字典管理是Mars Admin系统配置的重要组成部分，提供了系统中各种枚举值、选项配置的统一管理功能。

## 功能概述

字典管理模块主要包含以下功能：

- **字典类型管理**：字典分类的增删改查
- **字典数据管理**：具体字典项的维护
- **字典缓存**：高性能的字典数据缓存
- **前端集成**：前端组件的字典数据绑定
- **数据导出**：字典数据的导入导出
- **多语言支持**：字典的国际化配置

## 数据模型

### 字典类型实体

```java
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_dict_type", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "字典类型")
public class SysDictType extends BaseEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "字典主键")
    private Long dictId;

    @Schema(description = "字典名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dictName;

    @Schema(description = "字典类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dictType;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
```

### 字典数据实体

```java
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_dict_data", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "字典数据")
public class SysDictData extends BaseEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "字典编码")
    private Long dictCode;

    @Schema(description = "字典排序")
    private Integer dictSort;

    @Schema(description = "字典标签", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dictLabel;

    @Schema(description = "字典键值", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dictValue;

    @Schema(description = "字典类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dictType;

    @Schema(description = "样式属性（其他样式扩展）")
    private String cssClass;

    @Schema(description = "表格回显样式")
    private String listClass;

    @Schema(description = "是否默认（Y是 N否）")
    private String isDefault;

    @Schema(description = "状态（0正常 1停用）")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
```

## 核心功能实现

### 1. 字典类型管理

#### 获取字典类型列表

**接口地址：** `GET /system/dict/type/list`

**请求参数：**
```json
{
  "dictName": "用户性别",
  "dictType": "sys_user_sex",
  "status": "0"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "dictId": 1,
        "dictName": "用户性别",
        "dictType": "sys_user_sex",
        "status": "0",
        "createTime": "2025-01-01T00:00:00",
        "remark": "用户性别列表"
      },
      {
        "dictId": 2,
        "dictName": "菜单状态",
        "dictType": "sys_show_hide",
        "status": "0",
        "createTime": "2025-01-01T00:00:00",
        "remark": "菜单状态列表"
      }
    ],
    "total": 2
  }
}
```

#### 新增字典类型

**接口地址：** `POST /system/dict/type`

**请求参数：**
```json
{
  "dictName": "订单状态",
  "dictType": "order_status",
  "status": "0",
  "remark": "订单状态字典"
}
```

### 2. 字典数据管理

#### 获取字典数据列表

**接口地址：** `GET /system/dict/data/list`

**请求参数：**
```json
{
  "dictType": "sys_user_sex",
  "dictLabel": "男",
  "status": "0"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "dictCode": 1,
        "dictSort": 1,
        "dictLabel": "男",
        "dictValue": "0",
        "dictType": "sys_user_sex",
        "cssClass": "",
        "listClass": "default",
        "isDefault": "Y",
        "status": "0",
        "remark": ""
      },
      {
        "dictCode": 2,
        "dictSort": 2,
        "dictLabel": "女",
        "dictValue": "1",
        "dictType": "sys_user_sex",
        "cssClass": "",
        "listClass": "default",
        "isDefault": "N",
        "status": "0",
        "remark": ""
      }
    ],
    "total": 2
  }
}
```

#### 根据字典类型获取字典数据

**接口地址：** `GET /system/dict/data/type/{dictType}`

**功能描述：** 根据字典类型获取对应的字典数据，常用于前端下拉选择等组件。

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "dictLabel": "男",
      "dictValue": "0",
      "dictType": "sys_user_sex",
      "cssClass": "",
      "listClass": "default",
      "isDefault": "Y",
      "status": "0"
    },
    {
      "dictLabel": "女",
      "dictValue": "1",
      "dictType": "sys_user_sex",
      "cssClass": "",
      "listClass": "default",
      "isDefault": "N",
      "status": "0"
    }
  ]
}
```

## 前端实现

### 1. 字典类型管理页面

```vue
<template>
  <div class="dict-type-manage">
    <!-- 搜索表单 -->
    <a-card class="search-card">
      <a-form layout="inline" :model="searchForm">
        <a-form-item label="字典名称">
          <a-input v-model:value="searchForm.dictName" placeholder="请输入字典名称" />
        </a-form-item>
        <a-form-item label="字典类型">
          <a-input v-model:value="searchForm.dictType" placeholder="请输入字典类型" />
        </a-form-item>
        <a-form-item label="状态">
          <a-select v-model:value="searchForm.status" placeholder="请选择状态">
            <a-select-option value="0">正常</a-select-option>
            <a-select-option value="1">停用</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleSearch">搜索</a-button>
          <a-button @click="handleReset">重置</a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 操作按钮 -->
    <a-card class="table-card">
      <template #title>
        <a-space>
          <a-button type="primary" @click="handleAdd">
            <template #icon><plus-outlined /></template>
            新增字典
          </a-button>
          <a-button type="danger" :disabled="!selectedRowKeys.length" @click="handleBatchDelete">
            <template #icon><delete-outlined /></template>
            批量删除
          </a-button>
          <a-button @click="handleRefreshCache">
            <template #icon><reload-outlined /></template>
            刷新缓存
          </a-button>
        </a-space>
      </template>

      <!-- 数据表格 -->
      <a-table
        :columns="columns"
        :data-source="dataList"
        :pagination="pagination"
        :loading="loading"
        :row-selection="rowSelection"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'dictType'">
            <a-button type="link" @click="handleViewData(record)">
              {{ record.dictType }}
            </a-button>
          </template>
          <template v-if="column.key === 'status'">
            <a-switch
              :checked="record.status === '0'"
              @change="(checked) => handleStatusChange(record, checked)"
            />
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-button type="link" size="small" @click="handleViewData(record)">字典配置</a-button>
              <a-popconfirm title="确定删除此字典类型吗？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 字典类型编辑弹窗 -->
    <dict-type-edit-modal
      v-model:visible="editModalVisible"
      :dict-type-data="currentDictType"
      @success="handleEditSuccess"
    />

    <!-- 字典数据管理弹窗 -->
    <dict-data-modal
      v-model:visible="dataModalVisible"
      :dict-type="currentDictType"
      @success="handleDataSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined, DeleteOutlined, ReloadOutlined } from '@ant-design/icons-vue'
import { 
  fetchDictTypeList, 
  deleteDictType, 
  changeDictTypeStatus,
  refreshDictCache 
} from '@/service/api/dict'
import DictTypeEditModal from './components/dict-type-edit-modal.vue'
import DictDataModal from './components/dict-data-modal.vue'

// 响应式数据
const loading = ref(false)
const dataList = ref([])
const selectedRowKeys = ref([])
const editModalVisible = ref(false)
const dataModalVisible = ref(false)
const currentDictType = ref(null)

// 搜索表单
const searchForm = reactive({
  dictName: '',
  dictType: '',
  status: undefined
})

// 分页配置
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true
})

// 表格列配置
const columns = [
  {
    title: '字典编号',
    dataIndex: 'dictId',
    key: 'dictId',
    width: 100
  },
  {
    title: '字典名称',
    dataIndex: 'dictName',
    key: 'dictName'
  },
  {
    title: '字典类型',
    dataIndex: 'dictType',
    key: 'dictType'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    align: 'center'
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 180
  },
  {
    title: '操作',
    key: 'action',
    width: 250,
    fixed: 'right'
  }
]

// 行选择配置
const rowSelection = {
  selectedRowKeys,
  onChange: (keys: string[]) => {
    selectedRowKeys.value = keys
  }
}

// 方法定义
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.pageSize,
      ...searchForm
    }
    const { data } = await fetchDictTypeList(params)
    dataList.value = data.records
    pagination.total = data.total
  } catch (error) {
    message.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    dictName: '',
    dictType: '',
    status: undefined
  })
  handleSearch()
}

const handleAdd = () => {
  currentDictType.value = null
  editModalVisible.value = true
}

const handleEdit = (record) => {
  currentDictType.value = record
  editModalVisible.value = true
}

const handleViewData = (record) => {
  currentDictType.value = record
  dataModalVisible.value = true
}

const handleDelete = async (record) => {
  try {
    await deleteDictType(record.dictId)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

const handleBatchDelete = async () => {
  try {
    await deleteDictType(selectedRowKeys.value.join(','))
    message.success('批量删除成功')
    selectedRowKeys.value = []
    loadData()
  } catch (error) {
    message.error('批量删除失败')
  }
}

const handleStatusChange = async (record, checked) => {
  try {
    await changeDictTypeStatus({
      dictId: record.dictId,
      status: checked ? '0' : '1'
    })
    record.status = checked ? '0' : '1'
    message.success('状态修改成功')
  } catch (error) {
    message.error('状态修改失败')
  }
}

const handleRefreshCache = async () => {
  try {
    await refreshDictCache()
    message.success('缓存刷新成功')
  } catch (error) {
    message.error('缓存刷新失败')
  }
}

const handleTableChange = (pag) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  loadData()
}

const handleEditSuccess = () => {
  editModalVisible.value = false
  loadData()
}

const handleDataSuccess = () => {
  dataModalVisible.value = false
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>
```

### 2. 字典数据组件

```vue
<template>
  <a-modal
    v-model:visible="visible"
    :title="`字典数据 - ${dictType?.dictName}`"
    width="1200px"
    :footer="null"
    @cancel="handleCancel"
  >
    <!-- 操作按钮 -->
    <div class="dict-data-header">
      <a-space>
        <a-button type="primary" @click="handleAddData">
          <template #icon><plus-outlined /></template>
          新增数据
        </a-button>
        <a-button @click="handleRefresh">
          <template #icon><reload-outlined /></template>
          刷新
        </a-button>
      </a-space>
    </div>

    <!-- 字典数据表格 -->
    <a-table
      :columns="dataColumns"
      :data-source="dictDataList"
      :pagination="false"
      :loading="dataLoading"
      row-key="dictCode"
      size="small"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'dictLabel'">
          <a-tag :color="getTagColor(record.listClass)">
            {{ record.dictLabel }}
          </a-tag>
        </template>
        <template v-if="column.key === 'isDefault'">
          <a-tag :color="record.isDefault === 'Y' ? 'green' : 'default'">
            {{ record.isDefault === 'Y' ? '是' : '否' }}
          </a-tag>
        </template>
        <template v-if="column.key === 'status'">
          <a-switch
            :checked="record.status === '0'"
            size="small"
            @change="(checked) => handleDataStatusChange(record, checked)"
          />
        </template>
        <template v-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="handleEditData(record)">编辑</a-button>
            <a-popconfirm title="确定删除此字典数据吗？" @confirm="handleDeleteData(record)">
              <a-button type="link" size="small" danger>删除</a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 字典数据编辑弹窗 -->
    <dict-data-edit-modal
      v-model:visible="dataEditModalVisible"
      :dict-data="currentDictData"
      :dict-type="dictType"
      @success="handleDataEditSuccess"
    />
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined, ReloadOutlined } from '@ant-design/icons-vue'
import {
  fetchDictDataList,
  deleteDictData,
  changeDictDataStatus
} from '@/service/api/dict'
import DictDataEditModal from './dict-data-edit-modal.vue'

// Props和Emits
interface Props {
  visible: boolean
  dictType?: any
}

interface Emits {
  (e: 'update:visible', visible: boolean): void
  (e: 'success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 响应式数据
const dataLoading = ref(false)
const dictDataList = ref([])
const dataEditModalVisible = ref(false)
const currentDictData = ref(null)

// 字典数据表格列配置
const dataColumns = [
  {
    title: '字典编码',
    dataIndex: 'dictCode',
    key: 'dictCode',
    width: 100
  },
  {
    title: '字典标签',
    dataIndex: 'dictLabel',
    key: 'dictLabel',
    width: 120
  },
  {
    title: '字典键值',
    dataIndex: 'dictValue',
    key: 'dictValue',
    width: 120
  },
  {
    title: '字典排序',
    dataIndex: 'dictSort',
    key: 'dictSort',
    width: 100,
    align: 'center'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 80,
    align: 'center'
  },
  {
    title: '是否默认',
    dataIndex: 'isDefault',
    key: 'isDefault',
    width: 100,
    align: 'center'
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark'
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
    fixed: 'right'
  }
]

// 监听器
watch(() => props.visible, (visible) => {
  if (visible && props.dictType) {
    loadDictData()
  }
})

// 方法定义
const loadDictData = async () => {
  if (!props.dictType) return

  dataLoading.value = true
  try {
    const { data } = await fetchDictDataList({
      dictType: props.dictType.dictType
    })
    dictDataList.value = data.records || []
  } catch (error) {
    message.error('加载字典数据失败')
  } finally {
    dataLoading.value = false
  }
}

const getTagColor = (listClass) => {
  const colorMap = {
    'default': 'default',
    'primary': 'blue',
    'success': 'green',
    'info': 'cyan',
    'warning': 'orange',
    'danger': 'red'
  }
  return colorMap[listClass] || 'default'
}

const handleAddData = () => {
  currentDictData.value = { dictType: props.dictType.dictType }
  dataEditModalVisible.value = true
}

const handleEditData = (record) => {
  currentDictData.value = record
  dataEditModalVisible.value = true
}

const handleDeleteData = async (record) => {
  try {
    await deleteDictData(record.dictCode)
    message.success('删除成功')
    loadDictData()
  } catch (error) {
    message.error('删除失败')
  }
}

const handleDataStatusChange = async (record, checked) => {
  try {
    await changeDictDataStatus({
      dictCode: record.dictCode,
      status: checked ? '0' : '1'
    })
    record.status = checked ? '0' : '1'
    message.success('状态修改成功')
  } catch (error) {
    message.error('状态修改失败')
  }
}

const handleRefresh = () => {
  loadDictData()
}

const handleDataEditSuccess = () => {
  dataEditModalVisible.value = false
  loadDictData()
}

const handleCancel = () => {
  emit('update:visible', false)
}
</script>

<style scoped>
.dict-data-header {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}
</style>
```

## 字典缓存机制

### 1. 后端缓存实现

```java
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String DICT_CACHE_KEY = "sys_dict:";

    /**
     * 根据字典类型查询字典数据
     */
    @Override
    @Cacheable(key = "'normalType:' + #dictType")
    public List<SysDictData> selectNormalDictDataByType(String dictType) {
        return sysDictDataMapper.selectNormalDictDataByType(dictType);
    }

    /**
     * 新增字典数据
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean insertDictData(SysDictData dictData) {
        return this.save(dictData);
    }

    /**
     * 修改字典数据
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean updateDictData(SysDictData dictData) {
        return this.updateById(dictData);
    }

    /**
     * 删除字典数据
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean deleteDictDataByIds(String ids) {
        return this.removeByIds(Arrays.asList(ids.split(",")));
    }

    /**
     * 刷新字典缓存
     */
    @Override
    public void refreshCache() {
        // 清除所有字典缓存
        Set<String> keys = redisTemplate.keys(DICT_CACHE_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        // 重新加载字典数据到缓存
        List<SysDictType> dictTypes = sysDictTypeMapper.selectList(null);
        for (SysDictType dictType : dictTypes) {
            if ("0".equals(dictType.getStatus())) {
                List<SysDictData> dictDataList = selectNormalDictDataByType(dictType.getDictType());
                redisTemplate.opsForValue().set(
                    DICT_CACHE_KEY + dictType.getDictType(),
                    dictDataList,
                    Duration.ofHours(24)
                );
            }
        }
    }
}
```

### 2. 前端字典Hook

```typescript
// src/hooks/business/use-dict.ts
import { ref, computed } from 'vue'
import { fetchDictDataByType } from '@/service/api/dict'

interface DictData {
  dictLabel: string
  dictValue: string
  dictType: string
  cssClass?: string
  listClass?: string
  isDefault?: string
  status: string
}

// 字典缓存
const dictCache = new Map<string, DictData[]>()

export function useDict(dictType: string) {
  const dictData = ref<DictData[]>([])
  const loading = ref(false)

  // 获取字典数据
  const loadDict = async () => {
    // 先从缓存获取
    if (dictCache.has(dictType)) {
      dictData.value = dictCache.get(dictType)!
      return
    }

    loading.value = true
    try {
      const { data } = await fetchDictDataByType(dictType)
      dictData.value = data
      // 缓存数据
      dictCache.set(dictType, data)
    } catch (error) {
      console.error(`加载字典数据失败: ${dictType}`, error)
      dictData.value = []
    } finally {
      loading.value = false
    }
  }

  // 获取字典选项（用于下拉选择）
  const dictOptions = computed(() => {
    return dictData.value.map(item => ({
      label: item.dictLabel,
      value: item.dictValue,
      disabled: item.status === '1'
    }))
  })

  // 根据值获取标签
  const getDictLabel = (value: string) => {
    const item = dictData.value.find(d => d.dictValue === value)
    return item?.dictLabel || value
  }

  // 根据值获取样式类
  const getDictClass = (value: string) => {
    const item = dictData.value.find(d => d.dictValue === value)
    return item?.listClass || 'default'
  }

  // 获取默认值
  const getDefaultValue = () => {
    const defaultItem = dictData.value.find(d => d.isDefault === 'Y')
    return defaultItem?.dictValue || ''
  }

  // 初始化加载
  loadDict()

  return {
    dictData,
    dictOptions,
    loading,
    loadDict,
    getDictLabel,
    getDictClass,
    getDefaultValue
  }
}

// 字典标签组件
export function useDictTag(dictType: string, value: string) {
  const { getDictLabel, getDictClass } = useDict(dictType)

  const label = computed(() => getDictLabel(value))
  const tagClass = computed(() => getDictClass(value))

  return {
    label,
    tagClass
  }
}
```

### 3. 字典组件封装

```vue
<!-- src/components/business/dict-select.vue -->
<template>
  <a-select
    v-bind="$attrs"
    :loading="loading"
    :options="dictOptions"
    :placeholder="placeholder || `请选择${dictName}`"
  />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useDict } from '@/hooks/business/use-dict'

interface Props {
  dictType: string
  dictName?: string
  placeholder?: string
}

const props = defineProps<Props>()

const { dictOptions, loading } = useDict(props.dictType)
</script>
```

```vue
<!-- src/components/business/dict-tag.vue -->
<template>
  <a-tag :color="tagColor">
    {{ label }}
  </a-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useDictTag } from '@/hooks/business/use-dict'

interface Props {
  dictType: string
  value: string
}

const props = defineProps<Props>()

const { label, tagClass } = useDictTag(props.dictType, props.value)

const tagColor = computed(() => {
  const colorMap = {
    'default': 'default',
    'primary': 'blue',
    'success': 'green',
    'info': 'cyan',
    'warning': 'orange',
    'danger': 'red'
  }
  return colorMap[tagClass.value] || 'default'
})
</script>
```

## 使用示例

### 1. 在表单中使用字典

```vue
<template>
  <a-form>
    <!-- 使用字典选择组件 -->
    <a-form-item label="用户性别" name="sex">
      <dict-select
        v-model:value="formData.sex"
        dict-type="sys_user_sex"
        dict-name="性别"
      />
    </a-form-item>

    <!-- 使用字典Hook -->
    <a-form-item label="用户状态" name="status">
      <a-select v-model:value="formData.status" :options="userStatusOptions" />
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useDict } from '@/hooks/business/use-dict'
import DictSelect from '@/components/business/dict-select.vue'

const formData = reactive({
  sex: '',
  status: ''
})

// 使用字典Hook
const { dictOptions: userStatusOptions } = useDict('sys_normal_disable')
</script>
```

### 2. 在表格中显示字典

```vue
<template>
  <a-table :columns="columns" :data-source="dataList">
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'sex'">
        <dict-tag dict-type="sys_user_sex" :value="record.sex" />
      </template>
      <template v-if="column.key === 'status'">
        <dict-tag dict-type="sys_normal_disable" :value="record.status" />
      </template>
    </template>
  </a-table>
</template>

<script setup lang="ts">
import DictTag from '@/components/business/dict-tag.vue'

const columns = [
  { title: '用户名', dataIndex: 'username', key: 'username' },
  { title: '性别', dataIndex: 'sex', key: 'sex' },
  { title: '状态', dataIndex: 'status', key: 'status' }
]
</script>
```

## 最佳实践

### 1. 字典设计原则
- **命名规范**：字典类型使用下划线命名，如 `sys_user_sex`
- **值的设计**：字典值尽量使用数字或简短字符串
- **排序合理**：通过 `dictSort` 字段控制显示顺序
- **状态管理**：合理使用状态字段控制字典的启用/停用

### 2. 性能优化
- **缓存策略**：使用Redis缓存热点字典数据
- **懒加载**：按需加载字典数据
- **批量获取**：一次性获取多个字典类型的数据
- **前端缓存**：在前端缓存字典数据减少请求

### 3. 维护建议
- **版本控制**：重要字典变更要有版本记录
- **影响分析**：修改字典前分析对业务的影响
- **数据备份**：定期备份字典数据
- **文档维护**：维护字典的业务含义文档

## 常见问题

### Q: 如何实现字典的国际化？

**解决方案**：
```java
// 在字典数据表中添加语言字段
ALTER TABLE sys_dict_data ADD COLUMN lang VARCHAR(10) DEFAULT 'zh-CN';

// 查询时根据语言过滤
@Override
public List<SysDictData> selectDictDataByType(String dictType, String lang) {
    return sysDictDataMapper.selectDictDataByTypeAndLang(dictType, lang);
}
```

### Q: 如何处理字典数据的级联关系？

**解决方案**：
```java
// 添加父级字典字段
ALTER TABLE sys_dict_data ADD COLUMN parent_code BIGINT;

// 查询子级字典
public List<SysDictData> selectChildDictData(String dictType, String parentValue) {
    return sysDictDataMapper.selectChildDictData(dictType, parentValue);
}
```

### Q: 如何实现字典数据的批量导入？

**解决方案**：
```java
@Override
@Transactional
public void importDictData(MultipartFile file) throws Exception {
    List<SysDictData> dictDataList = ExcelUtil.importExcel(file, SysDictData.class);

    for (SysDictData dictData : dictDataList) {
        // 验证数据
        validateDictData(dictData);
        // 保存数据
        this.save(dictData);
    }

    // 刷新缓存
    refreshCache();
}
```
```
