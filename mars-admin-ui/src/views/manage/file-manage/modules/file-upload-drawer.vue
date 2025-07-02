<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { InboxOutlined } from '@ant-design/icons-vue';
import { fetchGetEnabledOssConfigs } from '@/service/api/file-manage';

defineOptions({
  name: 'FileUploadDrawer'
});

interface Emits {
  (e: 'uploaded'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

// OSS配置和上传相关
const ossConfigs = ref<any[]>([]);
const selectedConfigKey = ref('local');

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
    window.$message?.error('获取存储配置失败');
  }
}

// 获取配置颜色
function getConfigColor(configKey: string): string {
  const colors = {
    local: 'blue',
    minio: 'green',
    aliyun: 'orange',
    qiniu: 'purple'
  };
  return colors[configKey] || 'default';
}

// 获取配置名称
function getConfigName(configKey: string): string {
  const names = {
    local: '本地存储',
    minio: 'MinIO',
    aliyun: '阿里云OSS',
    qiniu: '七牛云'
  };
  return names[configKey] || configKey;
}

// 处理文件上传
function handleUploadChange(info: any) {
  const { status } = info.file;
  if (status === 'done') {
    window.$message?.success(`${info.file.name} 文件上传成功`);
    emit('uploaded');
    visible.value = false;
  } else if (status === 'error') {
    window.$message?.error(`${info.file.name} 文件上传失败`);
  }
}

function closeDrawer() {
  visible.value = false;
}

// 组件挂载时加载配置
onMounted(() => {
  loadOssConfigs();
});
</script>

<template>
  <ADrawer :open="visible" title="文件上传" :width="500" :footer="null" placement="right" @close="closeDrawer">
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
        @change="handleUploadChange"
      >
        <p class="ant-upload-drag-icon">
          <InboxOutlined style="font-size: 48px; color: #bfbfbf" />
        </p>
        <p class="ant-upload-text">点击或拖拽文件到此区域上传</p>
        <p class="ant-upload-hint">支持单个或批量上传</p>
      </AUploadDragger>
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
