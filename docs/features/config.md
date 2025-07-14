# 系统配置

系统配置是Mars Admin的核心管理模块，提供了灵活的系统参数配置功能，支持动态修改系统行为而无需重启应用。

## 功能概述

系统配置模块主要包含以下功能：

- **参数配置管理**：系统运行参数的统一配置
- **配置分类**：按业务模块分类管理配置项
- **动态生效**：配置修改后实时生效
- **配置缓存**：高性能的配置数据缓存
- **配置校验**：配置值的格式和范围校验
- **配置导入导出**：配置数据的批量管理

## 数据模型

### 系统配置实体

```java
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_config", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "参数配置")
public class SysConfig extends BaseEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "参数主键")
    private Long configId;

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String configName;

    @Schema(description = "参数键名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String configKey;

    @Schema(description = "参数键值", requiredMode = Schema.RequiredMode.REQUIRED)
    private String configValue;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;

    @Schema(description = "备注")
    private String remark;
}
```

### 配置分类

系统配置按照业务模块进行分类：

| 分类 | 前缀 | 说明 |
|------|------|------|
| 系统配置 | sys.* | 系统基础配置 |
| 用户配置 | user.* | 用户相关配置 |
| 文件配置 | file.* | 文件上传配置 |
| 邮件配置 | mail.* | 邮件发送配置 |
| 短信配置 | sms.* | 短信发送配置 |
| 支付配置 | pay.* | 支付相关配置 |

## 核心功能实现

### 1. 配置列表查询

**接口地址：** `GET /system/config/list`

**请求参数：**
```json
{
  "current": 1,
  "size": 10,
  "configName": "用户注册",
  "configKey": "sys.user.register",
  "configType": "Y"
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
        "configId": 1,
        "configName": "主框架页-默认皮肤样式名称",
        "configKey": "sys.index.skinName",
        "configValue": "skin-blue",
        "configType": "Y",
        "createTime": "2025-01-01T00:00:00",
        "remark": "蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow"
      },
      {
        "configId": 2,
        "configName": "用户管理-账号初始密码",
        "configKey": "sys.user.initPassword",
        "configValue": "123456",
        "configType": "Y",
        "createTime": "2025-01-01T00:00:00",
        "remark": "初始化密码 123456"
      }
    ],
    "total": 2
  }
}
```

### 2. 根据键名查询配置

**接口地址：** `GET /system/config/configKey/{configKey}`

**功能描述：** 根据配置键名获取配置值，常用于前端获取系统配置。

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": "123456"
}
```

### 3. 新增配置

**接口地址：** `POST /system/config`

**请求参数：**
```json
{
  "configName": "文件上传-单文件大小限制",
  "configKey": "sys.upload.maxFileSize",
  "configValue": "10",
  "configType": "N",
  "remark": "单个文件最大上传大小，单位MB"
}
```

### 4. 修改配置

**接口地址：** `PUT /system/config`

**请求参数：**
```json
{
  "configId": 1,
  "configName": "文件上传-单文件大小限制",
  "configKey": "sys.upload.maxFileSize",
  "configValue": "20",
  "configType": "N",
  "remark": "单个文件最大上传大小，单位MB"
}
```

### 5. 删除配置

**接口地址：** `DELETE /system/config/{configIds}`

**功能描述：** 删除一个或多个配置项，系统内置配置不允许删除。

### 6. 刷新配置缓存

**接口地址：** `DELETE /system/config/refreshCache`

**功能描述：** 清除配置缓存，重新加载配置数据。

## 配置缓存机制

### 1. 配置缓存实现

```java
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String CONFIG_CACHE_KEY = "sys_config:";
    
    /**
     * 根据键名查询参数配置信息
     */
    @Override
    @Cacheable(key = "'configKey:' + #configKey")
    public String selectConfigByKey(String configKey) {
        SysConfig config = this.getOne(
            Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getConfigKey, configKey)
        );
        return config != null ? config.getConfigValue() : StringUtils.EMPTY;
    }
    
    /**
     * 新增参数配置
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean insertConfig(SysConfig config) {
        return this.save(config);
    }
    
    /**
     * 修改参数配置
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean updateConfig(SysConfig config) {
        return this.updateById(config);
    }
    
    /**
     * 删除参数配置
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean deleteConfigByIds(String ids) {
        return this.removeByIds(Arrays.asList(ids.split(",")));
    }
    
    /**
     * 清空参数缓存数据
     */
    @Override
    @CacheEvict(allEntries = true)
    public void clearConfigCache() {
        Set<String> keys = redisTemplate.keys(CONFIG_CACHE_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
    
    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        List<SysConfig> configList = this.list();
        for (SysConfig config : configList) {
            redisTemplate.opsForValue().set(
                CONFIG_CACHE_KEY + config.getConfigKey(),
                config.getConfigValue(),
                Duration.ofHours(24)
            );
        }
    }
}
```

### 2. 配置工具类

```java
@Component
public class ConfigUtils {
    
    private static ISysConfigService configService;
    
    @Autowired
    public void setConfigService(ISysConfigService configService) {
        ConfigUtils.configService = configService;
    }
    
    /**
     * 根据键名获取参数配置
     */
    public static String getConfigValue(String configKey) {
        return configService.selectConfigByKey(configKey);
    }
    
    /**
     * 根据键名获取参数配置（带默认值）
     */
    public static String getConfigValue(String configKey, String defaultValue) {
        String value = getConfigValue(configKey);
        return StringUtils.isNotEmpty(value) ? value : defaultValue;
    }
    
    /**
     * 获取整型配置
     */
    public static Integer getConfigInt(String configKey) {
        String value = getConfigValue(configKey);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 获取整型配置（带默认值）
     */
    public static Integer getConfigInt(String configKey, Integer defaultValue) {
        Integer value = getConfigInt(configKey);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 获取布尔型配置
     */
    public static Boolean getConfigBoolean(String configKey) {
        String value = getConfigValue(configKey);
        return "true".equalsIgnoreCase(value) || "Y".equalsIgnoreCase(value);
    }
    
    /**
     * 获取布尔型配置（带默认值）
     */
    public static Boolean getConfigBoolean(String configKey, Boolean defaultValue) {
        String value = getConfigValue(configKey);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return "true".equalsIgnoreCase(value) || "Y".equalsIgnoreCase(value);
    }
}
```

## 前端实现

### 1. 系统配置管理页面

```vue
<template>
  <div class="config-manage">
    <!-- 搜索表单 -->
    <a-card class="search-card">
      <a-form layout="inline" :model="searchForm">
        <a-form-item label="参数名称">
          <a-input v-model:value="searchForm.configName" placeholder="请输入参数名称" />
        </a-form-item>
        <a-form-item label="参数键名">
          <a-input v-model:value="searchForm.configKey" placeholder="请输入参数键名" />
        </a-form-item>
        <a-form-item label="系统内置">
          <a-select v-model:value="searchForm.configType" placeholder="请选择类型">
            <a-select-option value="Y">是</a-select-option>
            <a-select-option value="N">否</a-select-option>
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
            新增参数
          </a-button>
          <a-button type="danger" :disabled="!selectedRowKeys.length" @click="handleBatchDelete">
            <template #icon><delete-outlined /></template>
            批量删除
          </a-button>
          <a-button @click="handleRefreshCache">
            <template #icon><reload-outlined /></template>
            刷新缓存
          </a-button>
          <a-button @click="handleExport">
            <template #icon><export-outlined /></template>
            导出
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
          <template v-if="column.key === 'configType'">
            <a-tag :color="record.configType === 'Y' ? 'red' : 'blue'">
              {{ record.configType === 'Y' ? '系统内置' : '用户定义' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'configValue'">
            <a-tooltip :title="record.configValue">
              <span class="config-value">{{ record.configValue }}</span>
            </a-tooltip>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
              <a-popconfirm 
                v-if="record.configType !== 'Y'"
                title="确定删除此参数配置吗？" 
                @confirm="handleDelete(record)"
              >
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 配置编辑弹窗 -->
    <config-edit-modal
      v-model:visible="editModalVisible"
      :config-data="currentConfig"
      @success="handleEditSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { 
  PlusOutlined, 
  DeleteOutlined, 
  ReloadOutlined, 
  ExportOutlined 
} from '@ant-design/icons-vue'
import { 
  fetchConfigList, 
  deleteConfig, 
  refreshConfigCache,
  exportConfig 
} from '@/service/api/config'
import ConfigEditModal from './components/config-edit-modal.vue'

// 响应式数据
const loading = ref(false)
const dataList = ref([])
const selectedRowKeys = ref([])
const editModalVisible = ref(false)
const currentConfig = ref(null)

// 搜索表单
const searchForm = reactive({
  configName: '',
  configKey: '',
  configType: undefined
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
    title: '参数主键',
    dataIndex: 'configId',
    key: 'configId',
    width: 100
  },
  {
    title: '参数名称',
    dataIndex: 'configName',
    key: 'configName'
  },
  {
    title: '参数键名',
    dataIndex: 'configKey',
    key: 'configKey'
  },
  {
    title: '参数键值',
    dataIndex: 'configValue',
    key: 'configValue',
    width: 200
  },
  {
    title: '系统内置',
    dataIndex: 'configType',
    key: 'configType',
    width: 120,
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
    width: 150,
    fixed: 'right'
  }
]

// 行选择配置
const rowSelection = {
  selectedRowKeys,
  onChange: (keys: string[]) => {
    selectedRowKeys.value = keys
  },
  getCheckboxProps: (record: any) => ({
    disabled: record.configType === 'Y' // 系统内置配置不能删除
  })
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
    const { data } = await fetchConfigList(params)
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
    configName: '',
    configKey: '',
    configType: undefined
  })
  handleSearch()
}

const handleAdd = () => {
  currentConfig.value = null
  editModalVisible.value = true
}

const handleEdit = (record) => {
  currentConfig.value = record
  editModalVisible.value = true
}

const handleDelete = async (record) => {
  try {
    await deleteConfig(record.configId)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

const handleBatchDelete = async () => {
  try {
    await deleteConfig(selectedRowKeys.value.join(','))
    message.success('批量删除成功')
    selectedRowKeys.value = []
    loadData()
  } catch (error) {
    message.error('批量删除失败')
  }
}

const handleRefreshCache = async () => {
  try {
    await refreshConfigCache()
    message.success('缓存刷新成功')
  } catch (error) {
    message.error('缓存刷新失败')
  }
}

const handleExport = async () => {
  try {
    const response = await exportConfig(searchForm)
    // 处理文件下载
    const blob = new Blob([response.data])
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '参数配置.xlsx'
    link.click()
    window.URL.revokeObjectURL(url)
    message.success('导出成功')
  } catch (error) {
    message.error('导出失败')
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

// 生命周期
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.config-value {
  display: inline-block;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
```

### 2. 配置编辑弹窗

```vue
<template>
  <a-modal
    v-model:visible="visible"
    :title="isEdit ? '编辑参数' : '新增参数'"
    width="600px"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-form ref="formRef" :model="formData" :rules="rules" layout="vertical">
      <a-form-item label="参数名称" name="configName">
        <a-input v-model:value="formData.configName" placeholder="请输入参数名称" />
      </a-form-item>

      <a-form-item label="参数键名" name="configKey">
        <a-input
          v-model:value="formData.configKey"
          placeholder="请输入参数键名"
          :disabled="isEdit && formData.configType === 'Y'"
        />
      </a-form-item>

      <a-form-item label="参数键值" name="configValue">
        <a-textarea
          v-model:value="formData.configValue"
          placeholder="请输入参数键值"
          :rows="3"
        />
      </a-form-item>

      <a-form-item label="系统内置" v-if="!isEdit">
        <a-radio-group v-model:value="formData.configType">
          <a-radio value="Y">是</a-radio>
          <a-radio value="N">否</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="备注">
        <a-textarea v-model:value="formData.remark" placeholder="请输入备注" :rows="3" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { message } from 'ant-design-vue'
import { createConfig, updateConfig } from '@/service/api/config'

// Props和Emits
interface Props {
  visible: boolean
  configData?: any
}

interface Emits {
  (e: 'update:visible', visible: boolean): void
  (e: 'success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 响应式数据
const formRef = ref()

const formData = reactive({
  configId: undefined,
  configName: '',
  configKey: '',
  configValue: '',
  configType: 'N',
  remark: ''
})

// 表单验证规则
const rules = {
  configName: [
    { required: true, message: '请输入参数名称', trigger: 'blur' }
  ],
  configKey: [
    { required: true, message: '请输入参数键名', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9._-]+$/, message: '参数键名只能包含字母、数字、点、下划线和横线', trigger: 'blur' }
  ],
  configValue: [
    { required: true, message: '请输入参数键值', trigger: 'blur' }
  ]
}

// 计算属性
const isEdit = computed(() => !!props.configData?.configId)

// 监听器
watch(() => props.visible, (visible) => {
  if (visible) {
    if (props.configData) {
      Object.assign(formData, props.configData)
    } else {
      resetForm()
    }
  }
})

// 方法定义
const resetForm = () => {
  Object.assign(formData, {
    configId: undefined,
    configName: '',
    configKey: '',
    configValue: '',
    configType: 'N',
    remark: ''
  })
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()

    if (isEdit.value) {
      await updateConfig(formData)
      message.success('修改成功')
    } else {
      await createConfig(formData)
      message.success('新增成功')
    }

    emit('success')
  } catch (error) {
    if (error.errorFields) {
      message.error('请检查表单输入')
    } else {
      message.error(isEdit.value ? '修改失败' : '新增失败')
    }
  }
}

const handleCancel = () => {
  emit('update:visible', false)
}
</script>
```

## 配置使用示例

### 1. 在业务代码中使用配置

```java
@Service
public class UserServiceImpl implements IUserService {

    @Override
    public void registerUser(SysUser user) {
        // 获取初始密码配置
        String initPassword = ConfigUtils.getConfigValue("sys.user.initPassword", "123456");
        user.setPassword(SecurityUtils.encryptPassword(initPassword));

        // 获取是否允许注册配置
        Boolean allowRegister = ConfigUtils.getConfigBoolean("sys.user.allowRegister", false);
        if (!allowRegister) {
            throw new ServiceException("系统暂不允许注册");
        }

        // 获取用户名最小长度配置
        Integer minUsernameLength = ConfigUtils.getConfigInt("sys.user.minUsernameLength", 4);
        if (user.getUserName().length() < minUsernameLength) {
            throw new ServiceException("用户名长度不能少于" + minUsernameLength + "位");
        }

        this.save(user);
    }
}
```

### 2. 在前端使用配置

```typescript
// src/utils/config.ts
import { fetchConfigByKey } from '@/service/api/config'

// 配置缓存
const configCache = new Map<string, string>()

/**
 * 获取系统配置
 */
export async function getConfig(key: string, defaultValue?: string): Promise<string> {
  // 先从缓存获取
  if (configCache.has(key)) {
    return configCache.get(key)!
  }

  try {
    const { data } = await fetchConfigByKey(key)
    const value = data || defaultValue || ''

    // 缓存配置
    configCache.set(key, value)
    return value
  } catch (error) {
    console.error(`获取配置失败: ${key}`, error)
    return defaultValue || ''
  }
}

/**
 * 获取布尔型配置
 */
export async function getConfigBoolean(key: string, defaultValue = false): Promise<boolean> {
  const value = await getConfig(key)
  return value === 'true' || value === 'Y'
}

/**
 * 获取数字型配置
 */
export async function getConfigNumber(key: string, defaultValue = 0): Promise<number> {
  const value = await getConfig(key)
  const num = parseInt(value)
  return isNaN(num) ? defaultValue : num
}

/**
 * 清除配置缓存
 */
export function clearConfigCache() {
  configCache.clear()
}
```

```vue
<!-- 使用示例 -->
<template>
  <div>
    <a-upload
      :max-count="maxFileCount"
      :before-upload="beforeUpload"
    >
      <a-button>上传文件</a-button>
    </a-upload>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getConfigNumber } from '@/utils/config'

const maxFileCount = ref(1)
const maxFileSize = ref(10)

const beforeUpload = (file: File) => {
  const isLtMaxSize = file.size / 1024 / 1024 < maxFileSize.value
  if (!isLtMaxSize) {
    message.error(`文件大小不能超过 ${maxFileSize.value}MB`)
    return false
  }
  return true
}

onMounted(async () => {
  // 从配置中获取文件上传限制
  maxFileCount.value = await getConfigNumber('sys.upload.maxFileCount', 1)
  maxFileSize.value = await getConfigNumber('sys.upload.maxFileSize', 10)
})
</script>
```

## 常用系统配置

### 1. 基础系统配置

```sql
-- 系统基础配置
INSERT INTO sys_config VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO sys_config VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', '初始化密码 123456');
INSERT INTO sys_config VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', '深色主题theme-dark，浅色主题theme-light');
INSERT INTO sys_config VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO sys_config VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', '是否开启注册用户功能（true开启，false关闭）');
```

### 2. 文件上传配置

```sql
-- 文件上传配置
INSERT INTO sys_config VALUES (10, '文件上传-单文件大小限制', 'sys.upload.maxFileSize', '10', 'N', '单个文件最大上传大小，单位MB');
INSERT INTO sys_config VALUES (11, '文件上传-允许的文件类型', 'sys.upload.allowedTypes', 'jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx', 'N', '允许上传的文件类型，用逗号分隔');
INSERT INTO sys_config VALUES (12, '文件上传-存储方式', 'sys.upload.storageType', 'local', 'N', '文件存储方式：local本地存储，minio对象存储，aliyun阿里云OSS');
```

### 3. 邮件配置

```sql
-- 邮件配置
INSERT INTO sys_config VALUES (20, '邮件发送-SMTP服务器', 'sys.mail.host', 'smtp.qq.com', 'N', 'SMTP服务器地址');
INSERT INTO sys_config VALUES (21, '邮件发送-SMTP端口', 'sys.mail.port', '587', 'N', 'SMTP服务器端口');
INSERT INTO sys_config VALUES (22, '邮件发送-发送者邮箱', 'sys.mail.username', '', 'N', '发送者邮箱地址');
INSERT INTO sys_config VALUES (23, '邮件发送-邮箱密码', 'sys.mail.password', '', 'N', '邮箱密码或授权码');
INSERT INTO sys_config VALUES (24, '邮件发送-是否启用SSL', 'sys.mail.ssl', 'true', 'N', '是否启用SSL加密');
```

## 最佳实践

### 1. 配置设计原则
- **键名规范**：使用点分隔的层级命名，如 `sys.user.initPassword`
- **分类清晰**：按业务模块分类，便于管理和查找
- **类型明确**：在备注中说明配置值的类型和格式
- **默认值**：为配置项提供合理的默认值

### 2. 性能优化
- **缓存策略**：使用Redis缓存热点配置数据
- **批量加载**：系统启动时批量加载配置到缓存
- **变更通知**：配置修改后及时清除缓存
- **懒加载**：按需加载不常用的配置

### 3. 安全考虑
- **敏感信息**：密码等敏感配置要加密存储
- **权限控制**：系统内置配置只允许管理员修改
- **输入验证**：对配置值进行格式和范围验证
- **审计日志**：记录配置的修改历史

## 常见问题

### Q: 如何实现配置的热更新？

**解决方案**：
```java
@Component
public class ConfigChangeListener {

    @EventListener
    public void handleConfigChange(ConfigChangeEvent event) {
        String configKey = event.getConfigKey();
        String newValue = event.getNewValue();

        // 通知相关组件配置已变更
        applicationEventPublisher.publishEvent(
            new ConfigUpdatedEvent(configKey, newValue)
        );

        // 清除相关缓存
        cacheManager.evict("config", configKey);
    }
}
```

### Q: 如何实现配置的环境隔离？

**解决方案**：
```java
// 在配置键名中加入环境标识
public String getConfigKey(String key) {
    String env = System.getProperty("spring.profiles.active", "dev");
    return env + "." + key;
}

// 或者使用不同的配置表
@Value("${spring.profiles.active:dev}")
private String activeProfile;

public String selectConfigByKey(String configKey) {
    String tableName = "sys_config_" + activeProfile;
    // 从对应环境的配置表查询
}
```

### Q: 如何实现配置的版本管理？

**解决方案**：
```sql
-- 创建配置历史表
CREATE TABLE sys_config_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_id BIGINT NOT NULL,
    config_key VARCHAR(100) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    change_type VARCHAR(20) NOT NULL,
    change_time DATETIME NOT NULL,
    change_by VARCHAR(50) NOT NULL
);

-- 记录配置变更历史
INSERT INTO sys_config_history (config_id, config_key, old_value, new_value, change_type, change_time, change_by)
VALUES (1, 'sys.user.initPassword', '123456', '888888', 'UPDATE', NOW(), 'admin');
```
```
