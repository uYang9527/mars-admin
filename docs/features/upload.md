# 文件上传

文件上传是Mars Admin系统的重要功能模块，提供了完整的文件管理解决方案，支持多种存储方式和文件类型。

## 功能概述

文件上传模块主要包含以下功能：

- **多存储支持**：本地存储、MinIO、阿里云OSS、腾讯云COS等
- **文件类型控制**：支持图片、文档、视频等多种文件类型
- **文件大小限制**：可配置的文件大小限制
- **文件预览**：图片预览、文档在线预览
- **文件管理**：文件的增删改查、批量操作
- **安全控制**：文件访问权限、病毒扫描

## 数据模型

### 文件存储实体

```java
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_oss", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "OSS对象存储")
public class SysOss extends BaseEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "对象存储主键")
    private Long ossId;

    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;

    @Schema(description = "原名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String originalName;

    @Schema(description = "文件后缀名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileSuffix;

    @Schema(description = "URL地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String url;

    @Schema(description = "服务商")
    private String service;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件类型")
    private String contentType;

    @Schema(description = "存储路径")
    private String filePath;

    @Schema(description = "是否删除（0存在 1删除）")
    private Integer isDelete;
}
```

### OSS配置实体

```java
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "sys_oss_config", onInsert = EntityChangeListener.class, onUpdate = EntityChangeListener.class)
@Schema(description = "对象存储配置")
public class SysOssConfig extends BaseEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "主建")
    private Long ossConfigId;

    @Schema(description = "配置key", requiredMode = Schema.RequiredMode.REQUIRED)
    private String configKey;

    @Schema(description = "accessKey")
    private String accessKey;

    @Schema(description = "秘钥")
    private String secretKey;

    @Schema(description = "桶名称")
    private String bucketName;

    @Schema(description = "前缀")
    private String prefix;

    @Schema(description = "访问站点")
    private String endpoint;

    @Schema(description = "自定义域名")
    private String domain;

    @Schema(description = "是否https（Y=是,N=否）")
    private String isHttps;

    @Schema(description = "域")
    private String region;

    @Schema(description = "桶权限类型(0=private 1=public 2=custom)")
    private String accessPolicy;

    @Schema(description = "是否默认（0=是,1=否）")
    private String status;

    @Schema(description = "扩展字段")
    private String ext1;

    @Schema(description = "备注")
    private String remark;
}
```

## 核心功能实现

### 1. 文件上传接口

**接口地址：** `POST /system/oss/upload`

**请求类型：** `multipart/form-data`

**请求参数：**
- `file`: 文件对象（必填）
- `path`: 存储路径（可选）

**响应示例：**
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "ossId": 1,
    "fileName": "avatar_20250114103000.jpg",
    "originalName": "用户头像.jpg",
    "fileSuffix": "jpg",
    "url": "http://localhost:9000/mars-admin/2025/01/14/avatar_20250114103000.jpg",
    "service": "minio",
    "fileSize": 102400,
    "contentType": "image/jpeg",
    "filePath": "/2025/01/14/avatar_20250114103000.jpg"
  }
}
```

### 2. 文件列表查询

**接口地址：** `GET /system/oss/list`

**请求参数：**
```json
{
  "fileName": "avatar",
  "originalName": "头像",
  "fileSuffix": "jpg",
  "service": "minio",
  "createTime": ["2025-01-01", "2025-01-31"]
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
        "ossId": 1,
        "fileName": "avatar_20250114103000.jpg",
        "originalName": "用户头像.jpg",
        "fileSuffix": "jpg",
        "url": "http://localhost:9000/mars-admin/2025/01/14/avatar_20250114103000.jpg",
        "service": "minio",
        "fileSize": 102400,
        "contentType": "image/jpeg",
        "createTime": "2025-01-14T10:30:00",
        "createBy": "admin"
      }
    ],
    "total": 1
  }
}
```

### 3. 文件删除

**接口地址：** `DELETE /system/oss/{ossIds}`

**功能描述：** 删除一个或多个文件，支持批量删除。

### 4. 文件下载

**接口地址：** `GET /system/oss/download/{ossId}`

**功能描述：** 下载指定文件。

## 存储策略实现

### 1. 存储策略接口

```java
public interface OssStrategy {
    
    /**
     * 上传文件
     */
    UploadResult upload(MultipartFile file, String path);
    
    /**
     * 删除文件
     */
    void delete(String fileName);
    
    /**
     * 获取文件URL
     */
    String getUrl(String fileName);
    
    /**
     * 检查文件是否存在
     */
    boolean exists(String fileName);
}
```

### 2. MinIO存储实现

```java
@Component("minioStrategy")
public class MinioOssStrategy implements OssStrategy {
    
    @Autowired
    private MinioClient minioClient;
    
    @Autowired
    private OssProperties ossProperties;
    
    @Override
    public UploadResult upload(MultipartFile file, String path) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String objectName = StringUtils.isNotBlank(path) ? path + "/" + fileName : fileName;
            
            // 上传文件
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(ossProperties.getBucketName())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            
            // 构建返回结果
            String url = getUrl(objectName);
            return UploadResult.builder()
                .fileName(fileName)
                .originalName(file.getOriginalFilename())
                .fileSuffix(getFileSuffix(file.getOriginalFilename()))
                .url(url)
                .service("minio")
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .filePath(objectName)
                .build();
                
        } catch (Exception e) {
            throw new ServiceException("文件上传失败: " + e.getMessage());
        }
    }
    
    @Override
    public void delete(String fileName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(ossProperties.getBucketName())
                    .object(fileName)
                    .build()
            );
        } catch (Exception e) {
            throw new ServiceException("文件删除失败: " + e.getMessage());
        }
    }
    
    @Override
    public String getUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(ossProperties.getBucketName())
                    .object(fileName)
                    .expiry(7, TimeUnit.DAYS)
                    .build()
            );
        } catch (Exception e) {
            throw new ServiceException("获取文件URL失败: " + e.getMessage());
        }
    }
    
    private String generateFileName(String originalName) {
        String suffix = getFileSuffix(originalName);
        String datePath = DateUtil.format(new Date(), "yyyy/MM/dd");
        String uuid = IdUtil.simpleUUID();
        return datePath + "/" + uuid + "." + suffix;
    }
    
    private String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
```

### 3. 阿里云OSS存储实现

```java
@Component("aliyunStrategy")
public class AliyunOssStrategy implements OssStrategy {
    
    @Autowired
    private OSS ossClient;
    
    @Autowired
    private OssProperties ossProperties;
    
    @Override
    public UploadResult upload(MultipartFile file, String path) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String objectName = StringUtils.isNotBlank(path) ? path + "/" + fileName : fileName;
            
            // 创建上传请求
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            
            // 上传文件
            PutObjectRequest putRequest = new PutObjectRequest(
                ossProperties.getBucketName(), 
                objectName, 
                file.getInputStream(), 
                metadata
            );
            
            ossClient.putObject(putRequest);
            
            // 构建返回结果
            String url = getUrl(objectName);
            return UploadResult.builder()
                .fileName(fileName)
                .originalName(file.getOriginalFilename())
                .fileSuffix(getFileSuffix(file.getOriginalFilename()))
                .url(url)
                .service("aliyun")
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .filePath(objectName)
                .build();
                
        } catch (Exception e) {
            throw new ServiceException("文件上传失败: " + e.getMessage());
        }
    }
    
    @Override
    public void delete(String fileName) {
        try {
            ossClient.deleteObject(ossProperties.getBucketName(), fileName);
        } catch (Exception e) {
            throw new ServiceException("文件删除失败: " + e.getMessage());
        }
    }
    
    @Override
    public String getUrl(String fileName) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000 * 24 * 7); // 7天
            return ossClient.generatePresignedUrl(
                ossProperties.getBucketName(), 
                fileName, 
                expiration
            ).toString();
        } catch (Exception e) {
            throw new ServiceException("获取文件URL失败: " + e.getMessage());
        }
    }
}
```

## 前端实现

### 1. 文件上传组件

```vue
<template>
  <div class="upload-component">
    <a-upload
      v-model:file-list="fileList"
      :action="uploadUrl"
      :headers="uploadHeaders"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :on-remove="handleRemove"
      :multiple="multiple"
      :accept="accept"
      :show-upload-list="showUploadList"
      :list-type="listType"
    >
      <slot>
        <a-button v-if="listType === 'text'">
          <template #icon><upload-outlined /></template>
          {{ uploadText }}
        </a-button>
        <div v-else-if="listType === 'picture-card'" class="upload-card">
          <plus-outlined />
          <div class="upload-text">{{ uploadText }}</div>
        </div>
      </slot>
    </a-upload>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { message } from 'ant-design-vue'
import { UploadOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { getToken } from '@/utils/auth'

// Props定义
interface Props {
  /** 文件列表 */
  modelValue?: any[]
  /** 是否支持多选 */
  multiple?: boolean
  /** 接受的文件类型 */
  accept?: string
  /** 文件大小限制(MB) */
  maxSize?: number
  /** 最大文件数量 */
  maxCount?: number
  /** 上传按钮文本 */
  uploadText?: string
  /** 列表类型 */
  listType?: 'text' | 'picture' | 'picture-card'
  /** 是否显示上传列表 */
  showUploadList?: boolean
  /** 存储路径 */
  path?: string
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: () => [],
  multiple: false,
  accept: '*',
  maxSize: 10,
  maxCount: 1,
  uploadText: '上传文件',
  listType: 'text',
  showUploadList: true,
  path: ''
})

// Emits定义
interface Emits {
  (e: 'update:modelValue', files: any[]): void
  (e: 'success', file: any, response: any): void
  (e: 'error', file: any, error: any): void
}

const emit = defineEmits<Emits>()

// 响应式数据
const fileList = ref(props.modelValue || [])

// 计算属性
const uploadUrl = computed(() => '/api/system/oss/upload')

const uploadHeaders = computed(() => ({
  'Authorization': `Bearer ${getToken()}`
}))

// 方法定义
const beforeUpload = (file: File) => {
  // 检查文件类型
  if (props.accept !== '*') {
    const acceptTypes = props.accept.split(',').map(type => type.trim())
    const fileType = file.type
    const fileName = file.name
    const fileSuffix = fileName.substring(fileName.lastIndexOf('.'))
    
    const isAccepted = acceptTypes.some(type => {
      if (type.startsWith('.')) {
        return fileSuffix.toLowerCase() === type.toLowerCase()
      }
      return fileType.includes(type)
    })
    
    if (!isAccepted) {
      message.error(`只能上传 ${props.accept} 格式的文件`)
      return false
    }
  }
  
  // 检查文件大小
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize
  if (!isLtMaxSize) {
    message.error(`文件大小不能超过 ${props.maxSize}MB`)
    return false
  }
  
  // 检查文件数量
  if (!props.multiple && fileList.value.length >= props.maxCount) {
    message.error(`最多只能上传 ${props.maxCount} 个文件`)
    return false
  }
  
  return true
}

const handleSuccess = (response: any, file: any) => {
  if (response.code === 200) {
    const uploadedFile = {
      uid: file.uid,
      name: response.data.originalName,
      status: 'done',
      url: response.data.url,
      response: response.data
    }
    
    if (props.multiple) {
      fileList.value.push(uploadedFile)
    } else {
      fileList.value = [uploadedFile]
    }
    
    emit('update:modelValue', fileList.value)
    emit('success', file, response)
    message.success('上传成功')
  } else {
    message.error(response.message || '上传失败')
  }
}

const handleError = (error: any, file: any) => {
  message.error('上传失败')
  emit('error', file, error)
}

const handleRemove = (file: any) => {
  const index = fileList.value.findIndex(item => item.uid === file.uid)
  if (index > -1) {
    fileList.value.splice(index, 1)
    emit('update:modelValue', fileList.value)
  }
}

// 暴露方法
defineExpose({
  clearFiles: () => {
    fileList.value = []
    emit('update:modelValue', [])
  }
})
</script>

<style scoped>
.upload-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  
  .upload-text {
    margin-top: 8px;
    font-size: 14px;
    color: #666;
  }
}
</style>
```

### 2. 图片上传组件

```vue
<template>
  <div class="image-upload">
    <a-upload
      v-model:file-list="fileList"
      :action="uploadUrl"
      :headers="uploadHeaders"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-preview="handlePreview"
      list-type="picture-card"
      :multiple="multiple"
      :max-count="maxCount"
    >
      <div v-if="fileList.length < maxCount">
        <plus-outlined />
        <div class="upload-text">上传图片</div>
      </div>
    </a-upload>

    <!-- 图片预览 -->
    <a-modal v-model:visible="previewVisible" :footer="null" centered>
      <img :src="previewImage" style="width: 100%" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'

// Props定义
interface Props {
  modelValue?: string | string[]
  multiple?: boolean
  maxCount?: number
  maxSize?: number
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  multiple: false,
  maxCount: 1,
  maxSize: 5
})

// Emits定义
interface Emits {
  (e: 'update:modelValue', value: string | string[]): void
}

const emit = defineEmits<Emits>()

// 响应式数据
const fileList = ref([])
const previewVisible = ref(false)
const previewImage = ref('')

// 方法定义
const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    message.error('只能上传图片文件')
    return false
  }

  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize
  if (!isLtMaxSize) {
    message.error(`图片大小不能超过 ${props.maxSize}MB`)
    return false
  }

  return true
}

const handleSuccess = (response: any, file: any) => {
  if (response.code === 200) {
    const url = response.data.url

    if (props.multiple) {
      const urls = fileList.value.map(f => f.response?.url || f.url).filter(Boolean)
      urls.push(url)
      emit('update:modelValue', urls)
    } else {
      emit('update:modelValue', url)
    }
  }
}

const handlePreview = (file: any) => {
  previewImage.value = file.url || file.response?.url
  previewVisible.value = true
}
</script>
```

### 3. 文件管理页面

```vue
<template>
  <div class="file-manage">
    <!-- 搜索表单 -->
    <a-card class="search-card">
      <a-form layout="inline" :model="searchForm">
        <a-form-item label="文件名">
          <a-input v-model:value="searchForm.fileName" placeholder="请输入文件名" />
        </a-form-item>
        <a-form-item label="文件类型">
          <a-select v-model:value="searchForm.fileSuffix" placeholder="请选择文件类型">
            <a-select-option value="jpg">JPG</a-select-option>
            <a-select-option value="png">PNG</a-select-option>
            <a-select-option value="pdf">PDF</a-select-option>
            <a-select-option value="doc">DOC</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="存储服务">
          <a-select v-model:value="searchForm.service" placeholder="请选择存储服务">
            <a-select-option value="local">本地存储</a-select-option>
            <a-select-option value="minio">MinIO</a-select-option>
            <a-select-option value="aliyun">阿里云OSS</a-select-option>
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
          <upload-component
            :multiple="true"
            :max-count="10"
            upload-text="上传文件"
            @success="handleUploadSuccess"
          />
          <a-button type="danger" :disabled="!selectedRowKeys.length" @click="handleBatchDelete">
            <template #icon><delete-outlined /></template>
            批量删除
          </a-button>
        </a-space>
      </template>

      <!-- 文件列表 -->
      <a-table
        :columns="columns"
        :data-source="dataList"
        :pagination="pagination"
        :loading="loading"
        :row-selection="rowSelection"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'fileName'">
            <a-space>
              <file-icon :file-type="record.fileSuffix" />
              <span>{{ record.fileName }}</span>
            </a-space>
          </template>
          <template v-if="column.key === 'fileSize'">
            {{ formatFileSize(record.fileSize) }}
          </template>
          <template v-if="column.key === 'url'">
            <a-button type="link" @click="handlePreview(record)">预览</a-button>
          </template>
          <template v-if="column.key === 'action'">
            <a-space>
              <a-button type="link" size="small" @click="handleDownload(record)">下载</a-button>
              <a-button type="link" size="small" @click="handleCopyUrl(record)">复制链接</a-button>
              <a-popconfirm title="确定删除此文件吗？" @confirm="handleDelete(record)">
                <a-button type="link" size="small" danger>删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 文件预览弹窗 -->
    <file-preview-modal
      v-model:visible="previewModalVisible"
      :file-data="currentFile"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { DeleteOutlined } from '@ant-design/icons-vue'
import { fetchFileList, deleteFile } from '@/service/api/upload'
import UploadComponent from '@/components/business/upload-component.vue'
import FileIcon from '@/components/business/file-icon.vue'
import FilePreviewModal from './components/file-preview-modal.vue'

// 响应式数据
const loading = ref(false)
const dataList = ref([])
const selectedRowKeys = ref([])
const previewModalVisible = ref(false)
const currentFile = ref(null)

// 搜索表单
const searchForm = reactive({
  fileName: '',
  fileSuffix: undefined,
  service: undefined
})

// 分页配置
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

// 表格列配置
const columns = [
  { title: '文件名', dataIndex: 'fileName', key: 'fileName' },
  { title: '原始名称', dataIndex: 'originalName', key: 'originalName' },
  { title: '文件大小', dataIndex: 'fileSize', key: 'fileSize', width: 120 },
  { title: '文件类型', dataIndex: 'fileSuffix', key: 'fileSuffix', width: 100 },
  { title: '存储服务', dataIndex: 'service', key: 'service', width: 120 },
  { title: '上传时间', dataIndex: 'createTime', key: 'createTime', width: 180 },
  { title: '预览', dataIndex: 'url', key: 'url', width: 80 },
  { title: '操作', key: 'action', width: 200, fixed: 'right' }
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
    const { data } = await fetchFileList(params)
    dataList.value = data.records
    pagination.total = data.total
  } catch (error) {
    message.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const formatFileSize = (size: number) => {
  if (size < 1024) return size + ' B'
  if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB'
  if (size < 1024 * 1024 * 1024) return (size / 1024 / 1024).toFixed(2) + ' MB'
  return (size / 1024 / 1024 / 1024).toFixed(2) + ' GB'
}

const handleSearch = () => {
  pagination.current = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    fileName: '',
    fileSuffix: undefined,
    service: undefined
  })
  handleSearch()
}

const handleUploadSuccess = () => {
  loadData()
}

const handlePreview = (record) => {
  currentFile.value = record
  previewModalVisible.value = true
}

const handleDownload = (record) => {
  window.open(record.url, '_blank')
}

const handleCopyUrl = async (record) => {
  try {
    await navigator.clipboard.writeText(record.url)
    message.success('链接已复制到剪贴板')
  } catch (error) {
    message.error('复制失败')
  }
}

const handleDelete = async (record) => {
  try {
    await deleteFile(record.ossId)
    message.success('删除成功')
    loadData()
  } catch (error) {
    message.error('删除失败')
  }
}

const handleBatchDelete = async () => {
  try {
    await deleteFile(selectedRowKeys.value.join(','))
    message.success('批量删除成功')
    selectedRowKeys.value = []
    loadData()
  } catch (error) {
    message.error('批量删除失败')
  }
}

const handleTableChange = (pag) => {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  loadData()
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>
```

## 安全控制

### 1. 文件类型验证

```java
@Component
public class FileValidator {

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
        "application/pdf", "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    /**
     * 验证文件类型
     */
    public boolean validateFileType(MultipartFile file, String allowedTypes) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        // 检查MIME类型
        if (!isAllowedContentType(contentType, allowedTypes)) {
            return false;
        }

        // 检查文件扩展名
        if (!isAllowedExtension(fileName, allowedTypes)) {
            return false;
        }

        // 检查文件头
        return validateFileHeader(file);
    }

    /**
     * 验证文件头
     */
    private boolean validateFileHeader(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] header = new byte[10];
            inputStream.read(header);

            String contentType = file.getContentType();
            if (contentType.startsWith("image/")) {
                return validateImageHeader(header, contentType);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateImageHeader(byte[] header, String contentType) {
        // JPEG文件头：FF D8 FF
        if ("image/jpeg".equals(contentType)) {
            return header[0] == (byte) 0xFF && header[1] == (byte) 0xD8 && header[2] == (byte) 0xFF;
        }

        // PNG文件头：89 50 4E 47 0D 0A 1A 0A
        if ("image/png".equals(contentType)) {
            return header[0] == (byte) 0x89 && header[1] == 0x50 &&
                   header[2] == 0x4E && header[3] == 0x47;
        }

        return true;
    }
}
```

### 2. 文件大小限制

```java
@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件最大50MB
        factory.setMaxFileSize(DataSize.ofMegabytes(50));
        // 总上传数据最大100MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(100));
        return factory.createMultipartConfig();
    }
}
```

### 3. 病毒扫描

```java
@Component
public class VirusScanner {

    /**
     * 扫描文件病毒
     */
    public boolean scanFile(MultipartFile file) {
        try {
            // 使用ClamAV进行病毒扫描
            ClamAVClient clamAVClient = new ClamAVClient("localhost", 3310);

            byte[] fileBytes = file.getBytes();
            ScanResult result = clamAVClient.scan(fileBytes);

            return result.getStatus() == ScanResult.Status.OK;
        } catch (Exception e) {
            log.error("病毒扫描失败", e);
            return false;
        }
    }
}
```

## 最佳实践

### 1. 文件命名策略
- **时间戳命名**：使用时间戳避免文件名冲突
- **UUID命名**：使用UUID确保文件名唯一性
- **路径分层**：按日期分层存储，便于管理
- **原名保留**：保留原始文件名便于用户识别

### 2. 存储优化
- **CDN加速**：使用CDN加速文件访问
- **压缩存储**：对图片进行压缩存储
- **分片上传**：大文件使用分片上传
- **断点续传**：支持上传中断后继续上传

### 3. 安全防护
- **类型限制**：严格限制上传文件类型
- **大小限制**：设置合理的文件大小限制
- **病毒扫描**：对上传文件进行病毒扫描
- **访问控制**：设置文件访问权限

## 常见问题

### Q: 如何实现大文件分片上传？

**解决方案**：
```javascript
// 前端分片上传
const uploadLargeFile = async (file) => {
  const chunkSize = 2 * 1024 * 1024 // 2MB
  const chunks = Math.ceil(file.size / chunkSize)

  for (let i = 0; i < chunks; i++) {
    const start = i * chunkSize
    const end = Math.min(start + chunkSize, file.size)
    const chunk = file.slice(start, end)

    await uploadChunk(chunk, i, chunks, file.name)
  }

  // 合并分片
  await mergeChunks(file.name, chunks)
}
```

### Q: 如何实现文件的在线预览？

**解决方案**：
```java
// 后端文件预览
@GetMapping("/preview/{ossId}")
public void previewFile(@PathVariable Long ossId, HttpServletResponse response) {
    SysOss oss = sysOssService.getById(ossId);

    if (oss.getFileSuffix().equals("pdf")) {
        // PDF直接返回
        response.setContentType("application/pdf");
    } else if (isImageFile(oss.getFileSuffix())) {
        // 图片直接返回
        response.setContentType(oss.getContentType());
    } else {
        // 其他文件转换为PDF预览
        convertToPdf(oss, response);
    }
}
```

### Q: 如何处理文件存储的容灾备份？

**解决方案**：
```java
// 多存储策略
@Component
public class MultiStorageStrategy {

    @Autowired
    private List<OssStrategy> strategies;

    public void uploadWithBackup(MultipartFile file) {
        // 主存储
        UploadResult primary = strategies.get(0).upload(file, "");

        // 备份存储
        CompletableFuture.runAsync(() -> {
            strategies.get(1).upload(file, "backup/");
        });
    }
}
```
```
