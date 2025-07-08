<script setup lang="ts">
import dayjs from 'dayjs';

defineOptions({
  name: 'FileOperateDrawer'
});

interface Props {
  /** 文件数据 */
  fileData?: Api.FileManage.FileInfo | null;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'download', file: Api.FileManage.FileInfo): void;
  (e: 'preview', file: Api.FileManage.FileInfo): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

// 判断是否为图片文件
function isImageFile(contentType: string): boolean {
  return contentType && contentType.startsWith('image/');
}

// 获取文件图标
function getFileIcon(contentType: string): string {
  if (contentType.includes('pdf')) return '📄';
  if (contentType.includes('word') || contentType.includes('document')) return '📝';
  if (contentType.includes('excel') || contentType.includes('sheet')) return '📊';
  if (contentType.includes('powerpoint') || contentType.includes('presentation')) return '📋';
  if (contentType.includes('zip') || contentType.includes('rar')) return '📦';
  if (contentType.includes('video')) return '🎬';
  if (contentType.includes('audio')) return '🎵';
  return '📄';
}

// 文件大小格式化
function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return `${Number.parseFloat((bytes / k ** i).toFixed(2))} ${sizes[i]}`;
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

function closeDrawer() {
  visible.value = false;
}

function handleDownload() {
  if (props.fileData) {
    emit('download', props.fileData);
  }
}

function handlePreview() {
  if (props.fileData) {
    emit('preview', props.fileData);
  }
}

function copyUrl() {
  if (props.fileData?.url) {
    navigator.clipboard.writeText(props.fileData.url);
    window.$message?.success('链接已复制 📋');
  }
}
</script>

<template>
  <ADrawer
    v-model:open="visible"
    title="文件详情"
    :width="500"
    placement="right"
    :body-style="{ paddingRight: '20px' }"
  >
    <div v-if="fileData" class="detail-content">
      <!-- 文件预览区域 -->
      <div v-if="isImageFile(fileData.contentType)" class="mb-6 text-center">
        <div class="border rounded-lg p-4">
          <img
            :src="fileData.url"
            :alt="fileData.originalName"
            class="max-h-60 max-w-full cursor-pointer object-contain transition-opacity hover:opacity-80"
            @click="handlePreview"
          />
        </div>
      </div>

      <!-- 文件类型图标展示（非图片文件） -->
      <div v-else class="mb-6 text-center">
        <div class="mx-auto mb-3 h-20 w-20 flex items-center justify-center rounded-lg bg-gray-100 text-3xl">
          {{ getFileIcon(fileData.contentType) }}
        </div>
        <div class="text-sm text-gray-600 font-medium">{{ fileData.originalName }}</div>
      </div>

      <!-- 文件信息 -->
      <div class="space-y-4">
        <div class="detail-item">
          <div class="detail-label">📄 文件名</div>
          <div class="detail-value">{{ fileData.originalName }}</div>
        </div>

        <div class="detail-item">
          <div class="detail-label">📏 文件大小</div>
          <div class="detail-value">{{ formatFileSize(fileData.size) }}</div>
        </div>

        <div class="detail-item">
          <div class="detail-label">🏷️ 文件类型</div>
          <div class="detail-value">
            <ATag color="blue">{{ fileData.contentType }}</ATag>
          </div>
        </div>

        <div class="detail-item">
          <div class="detail-label">🗂️ 存储配置</div>
          <div class="detail-value">
            <ATag :color="getConfigColor(fileData.configKey)">{{ fileData.configKey }}</ATag>
          </div>
        </div>

        <div class="detail-item">
          <div class="detail-label">📂 文件路径</div>
          <div class="detail-value break-all text-xs text-gray-500">
            {{ fileData.filePath || '-' }}
          </div>
        </div>

        <div class="detail-item">
          <div class="detail-label">🔗 访问URL</div>
          <div class="detail-value">
            <AInput :value="fileData.url" readonly size="small" class="text-xs">
              <template #suffix>
                <AButton size="small" type="text" @click="copyUrl">复制</AButton>
              </template>
            </AInput>
          </div>
        </div>

        <div class="detail-item">
          <div class="detail-label">⏰ 上传时间</div>
          <div class="detail-value">
            {{ fileData.createTime ? dayjs(fileData.createTime).format('YYYY-MM-DD HH:mm:ss') : '-' }}
          </div>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="grid grid-cols-2 mt-8 gap-3 border-t pt-4">
        <AButton type="primary" @click="handleDownload">📥 下载文件</AButton>
        <AButton @click="handlePreview">👁️ 预览文件</AButton>
      </div>
    </div>
  </ADrawer>
</template>

<style scoped>
.detail-content .detail-item {
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.detail-content .detail-item:last-child {
  border-bottom: none;
}

.detail-content .detail-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
  font-weight: 500;
}

.detail-content .detail-value {
  font-size: 14px;
  color: #333;
  word-break: break-all;
}

.detail-content .detail-value .ant-input {
  border: 1px solid #e8e8e8;
  background-color: #fafafa;
}

.detail-content .detail-value .ant-input:focus {
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}
</style>
