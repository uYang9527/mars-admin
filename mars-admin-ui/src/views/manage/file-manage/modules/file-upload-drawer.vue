<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { InboxOutlined } from '@ant-design/icons-vue';
import { fetchGetEnabledOssConfigs } from '@/service/api/file-manage';

defineOptions({
  name: 'FileUploadDrawer'
});

interface Emits {
  (e: 'uploaded'): void;
  (e: 'update:visible', value: boolean): void;
}

// 合并为单个defineEmits调用
const emit = defineEmits<Emits>();
const props = defineProps<{ visible: boolean }>();

// OSS配置和上传相关
const ossConfigs = ref<any[]>([]);
const selectedConfigKey = ref('local');
const uploading = ref(false);
const fileList = ref<any[]>([]);

// 计算属性：是否显示抽屉
const isVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
});

// 上传配置
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('token') || ''}`
}));

// 获取OSS配置
async function loadOssConfigs() {
  try {
    const { data: configs, error } = await fetchGetEnabledOssConfigs();
    if (!error && configs) {
      ossConfigs.value = configs;
      if (configs.length > 0) {
        selectedConfigKey.value = configs[0].configKey;
      }
    }
  } catch (error) {
    console.error('获取存储配置失败', error);
    window.$message?.error?.('获取存储配置失败');
  }
}

// 获取配置颜色
function getConfigColor(configKey: string): string {
  const colors: Record<string, string> = {
    local: 'blue',
    minio: 'green',
    aliyun: 'orange',
    qiniu: 'purple'
  };
  return colors[configKey] || 'default';
}

// 获取配置名称
function getConfigName(configKey: string): string {
  const names: Record<string, string> = {
    local: '本地存储',
    minio: 'MinIO',
    aliyun: '阿里云OSS',
    qiniu: '七牛云'
  };
  return names[configKey] || configKey;
}

// 处理文件上传前的检查
function handleBeforeUpload(file: any) {
  // 检查文件大小 - 默认限制为10MB
  const isLt10M = file.size / 1024 / 1024 < 10;
  if (!isLt10M) {
    window.$message?.error?.('文件大小不能超过10MB!');
    return false;
  }
  
  // 检查文件类型 (可根据需要调整)
  const isValidType = /\.(jpg|jpeg|png|gif|bmp|doc|docx|xls|xlsx|ppt|pptx|pdf|txt|zip|rar)$/i.test(file.name);
  if (!isValidType) {
    window.$message?.error?.('不支持的文件类型!');
    return false;
  }
  
  uploading.value = true;
  return true;
}

// 处理文件上传
function handleUploadChange(info: any) {
  const { status, name, response } = info.file;
  
  // 处理预览URL
  const fileListWithPreview = info.fileList.map((file: any) => {
    if (file.status === 'done' && file.response && file.response.code === 200 && file.response.data) {
      // 为上传成功的文件添加预览URL
      file.url = file.response.data.url;
    }
    return file;
  });
  
  // 更新文件列表
  fileList.value = fileListWithPreview;
  
  if (status === 'uploading') {
    uploading.value = true;
    return;
  }
  
  if (status === 'done') {
    uploading.value = false;
    if (response && response.code === 200) {
      window.$message?.success?.(`${name} 文件上传成功`);
      emit('uploaded');
      isVisible.value = false;
    } else {
      // 服务器返回了错误信息
      const errorMsg = response?.message || '文件上传失败';
      window.$message?.error?.(`${name} 上传失败: ${errorMsg}`);
    }
  } else if (status === 'error') {
    uploading.value = false;
    // 根据错误状态码提供更具体的错误信息
    let errorMessage = '文件上传失败';
    if (info.file.error && info.file.error.status) {
      switch (info.file.error.status) {
        case 413:
          errorMessage = '文件太大，超出服务器限制';
          break;
        case 415:
          errorMessage = '不支持的文件类型';
          break;
        case 401:
          errorMessage = '身份验证失败，请重新登录';
          break;
        case 403:
          errorMessage = '您没有权限上传文件';
          break;
        case 500:
          errorMessage = '服务器内部错误，请稍后重试';
          break;
        case 503:
          errorMessage = '服务暂时不可用，请稍后重试';
          break;
        default:
          if (info.file.error.response) {
            try {
              const responseData = JSON.parse(info.file.error.response);
              errorMessage = responseData.msg || errorMessage;
            } catch (e) {
              // 解析JSON失败，使用默认错误消息
            }
          }
      }
    }
    window.$message?.error?.(`${name} ${errorMessage}`);
  }
}

function closeDrawer() {
  fileList.value = [];
  uploading.value = false;
  isVisible.value = false;
}

// 组件挂载时加载配置
onMounted(() => {
  loadOssConfigs();
});
</script>

<template>
  <ADrawer :open="isVisible" title="文件上传" :width="500" :footer="null" placement="right" @close="closeDrawer">
    <div class="upload-content">
      <!-- 存储方式选择 -->
      <div class="mb-4">
        <div class="mb-2 text-sm">存储方式</div>
        <ASelect v-model:value="selectedConfigKey" placeholder="请选择存储配置" class="w-full">
          <ASelectOption v-for="config in ossConfigs" :key="config.configKey" :value="config.configKey">
            <div class="flex items-center gap-2">
              <span>{{ config.configKey }}</span>
              <ATag :color="getConfigColor(config.configKey)" size="small">
                {{ getConfigName(config.configKey) }}
              </ATag>
            </div>
          </ASelectOption>
        </ASelect>
      </div>

      <!-- 文件上传区域 -->
      <AUploadDragger
        name="file"
        :multiple="true"
        action="/proxy-default/file/upload"
        :data="{ configKey: selectedConfigKey }"
        :headers="uploadHeaders"
        class="upload-dragger"
        :file-list="fileList"
        :before-upload="handleBeforeUpload"
        @change="handleUploadChange"
        :showUploadList="{ showPreviewIcon: true }"
      >
        <p class="ant-upload-drag-icon">
          <InboxOutlined style="font-size: 48px; color: #bfbfbf" />
        </p>
        <p class="ant-upload-text">点击或拖拽文件到此区域上传</p>
        <p class="ant-upload-hint">
          支持单个或批量上传<br />
          文件大小不超过10MB<br />
          支持常见文档、图片和压缩文件
        </p>
      </AUploadDragger>
      
      <!-- 上传状态提示 -->
      <div v-if="uploading" class="mt-4 text-center text-gray-500">
        <ASpin /> 文件上传中，请稍候...
      </div>
    </div>
  </ADrawer>
</template>

<style scoped>
.upload-content {
  padding: 16px;
}

.upload-dragger {
  margin-top: 16px;
}
</style>
