<script setup lang="tsx">
import {ref} from 'vue';
import {Card as ACard, Table as ATable, Button, Modal, Popconfirm, Tag} from 'ant-design-vue';
import dayjs from 'dayjs';
import {useTable, useTableOperate} from '@/hooks/common/table';
import {fetchDeleteFile, fetchDeleteFiles, fetchGetFileList, fetchGetFilePreviewInfo, getFileDownloadUrl, getFilePreviewUrl} from '@/service/api/file-manage';
import {$t} from '@/locales';
import FileSearch from './modules/file-search.vue';
import FileOperateDrawer from './modules/file-operate-drawer.vue';
import FileUploadDrawer from './modules/file-upload-drawer.vue';

defineOptions({
  name: 'FileManage'
});

// 上传抽屉相关
const uploadVisible = ref(false);

const {columns, data, getData, loading, mobilePagination, updatePagination, searchParams} = useTable({
  apiFn: fetchGetFileList,
  apiParams: {
    current: 1,
    size: 10,
    fileName: '',
    configKey: ''
  },
  columns: () => [
    {
      key: 'selection',
      title: '',
      align: 'center',
      type: 'selection',
      width: 10,
      fixed: 'left'
    },
    {
      key: 'index',
      dataIndex: 'index',
      title: $t('common.index'),
      align: 'center',
      width: 80,
      customRender: ({index}) => index + 1
    },
    {
      key: 'thumbnail',
      title: '缩略图',
      align: 'center',
      width: 80,
      customRender: ({record}) => {
        if (isImageFile(record.contentType)) {
          return (
            <img
              src={record.url}
              alt={record.originalName}
              class="h-12 w-12 cursor-pointer rounded object-cover"
              onClick={() => handlePreview(record)}
            />
          );
        }
        return (
          <div class="h-12 w-12 flex items-center justify-center rounded bg-gray-100 text-xs text-gray-500">
            {getFileIcon(record.contentType)}
          </div>
        );
      }
    },
    {
      key: 'originalName',
      title: '文件名',
      dataIndex: 'originalName',
      align: 'center',
      ellipsis: true,
      customRender: ({record}) => (
        <div class="cursor-pointer text-blue-600" onClick={() => handleShowDetail(record)}>
          {record.originalName}
        </div>
      )
    },
    {
      key: 'size',
      dataIndex: 'size',
      title: '文件大小',
      align: 'center',
      customRender: ({record}) => {
        return formatFileSize(record.size);
      }
    },
    {
      key: 'contentType',
      dataIndex: 'contentType',
      title: '文件类型',
      align: 'center'
    },
    {
      key: 'configKey',
      dataIndex: 'configKey',
      title: '存储配置',
      align: 'center',
      customRender: ({record}) => <Tag color={getConfigColor(record.configKey)}>{record.configKey}</Tag>
    },
    {
      key: 'createTime',
      dataIndex: 'createTime',
      title: '上传时间',
      align: 'center',
      width: 300,
      customRender: ({record}) => {
        return record.createTime ? dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') : '';
      }
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      customRender: ({record}) => (
        <div class="flex-center gap-8px">
          <Button type="primary" ghost size="small" onClick={() => handleShowDetail(record)}>
            详情
          </Button>
          <Button size="small" onClick={() => handlePreview(record)}>
            预览
          </Button>
          <Button size="small" onClick={() => handleDownload(record)}>
            下载
          </Button>
          <Popconfirm title={$t('common.confirmDelete')} onConfirm={() => handleDelete(record.id)}>
            <Button danger size="small">
              {$t('common.delete')}
            </Button>
          </Popconfirm>
        </div>
      )
    }
  ]
});

const {checkedRowKeys, onBatchDeleted, onDeleted, rowSelection} = useTableOperate(data, getData);

// 预览相关
const previewVisible = ref(false);
const previewFile = ref<any>(null);

// 详情相关
const detailVisible = ref(false);
const detailFile = ref<any>(null);

// 文件大小格式化
function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return `${Number.parseFloat((bytes / k ** i).toFixed(2))} ${sizes[i]}`;
}

// 判断是否为图片文件
function isImageFile(contentType: string): boolean {
  return contentType && contentType.startsWith('image/');
}

// 获取文件图标
function getFileIcon(contentType: string): string {
  if (contentType.includes('pdf')) return 'PDF';
  if (contentType.includes('word') || contentType.includes('document')) return 'DOC';
  if (contentType.includes('excel') || contentType.includes('sheet')) return 'XLS';
  if (contentType.includes('powerpoint') || contentType.includes('presentation')) return 'PPT';
  if (contentType.includes('zip') || contentType.includes('rar')) return 'ZIP';
  if (contentType.includes('video')) return 'MP4';
  if (contentType.includes('audio')) return 'MP3';
  return 'FILE';
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

// 文件预览
async function handlePreview(file: any) {
  try {
    // 获取预览信息
    const { data: previewInfo, error } = await fetchGetFilePreviewInfo(file.id);
    if (error || !previewInfo) {
      window.$message?.error('获取预览信息失败');
      return;
    }
    
    // 根据预览类型处理
    switch (previewInfo.previewType) {
      case 1: // 图片直接预览
        previewFile.value = {...file, previewUrl: previewInfo.previewUrl};
        previewVisible.value = true;
        break;
      case 3: // PDF预览
        previewFile.value = {...file, previewUrl: previewInfo.previewUrl, previewType: previewInfo.previewType};
        previewVisible.value = true;
        break;
      case 2: // Office文档预览
        if (previewInfo.needConvert) {
          // 如果需要转换，需要调用Office预览服务
          // 这里可以集成第三方预览服务，比如永中Office
          window.$message?.info('Office文档预览需要集成第三方预览服务');
        } else {
          previewFile.value = {...file, previewUrl: previewInfo.previewUrl, previewType: previewInfo.previewType};
  previewVisible.value = true;
        }
        break;
      case 4: // 视频预览
      case 5: // 音频预览
      case 6: // 文本预览
        previewFile.value = {...file, previewUrl: previewInfo.previewUrl, previewType: previewInfo.previewType};
        previewVisible.value = true;
        break;
      default:
        // 不支持预览，提示下载
        Modal.confirm({
          title: '文件预览',
          content: '该文件类型不支持在线预览，是否下载查看？',
          okText: '下载',
          cancelText: '取消',
          onOk: () => handleDownload(file)
        });
    }
  } catch (error) {
    console.error('预览文件失败:', error);
    window.$message?.error('预览文件失败');
  }
}

// 文件详情
function handleShowDetail(file: any) {
  detailFile.value = file;
  detailVisible.value = true;
}

// 文件下载
function handleDownload(file: any) {
  const downloadUrl = getFileDownloadUrl(file.id);
  const link = document.createElement('a');
  link.href = downloadUrl;
  link.download = file.originalName;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

// 在新标签页打开URL
function openInNewTab(url: string) {
  window.open(url, '_blank');
}

// 显示文件上传抽屉
function showUploadDrawer() {
  uploadVisible.value = true;
}

// 批量删除
async function handleBatchDelete() {
  if (checkedRowKeys.value.length === 0) {
    window.$message?.warning('请选择要删除的文件');
    return;
  }

  Modal.confirm({
    title: '批量删除文件',
    content: `确定要删除选中的 ${checkedRowKeys.value.length} 个文件吗？删除后无法恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      const {error} = await fetchDeleteFiles(checkedRowKeys.value);
      if (!error) {
        window.$message?.success('批量删除成功');
        onBatchDeleted();
      }
    }
  });
}

// 删除单个文件
async function handleDelete(id: number) {
  const {error} = await fetchDeleteFile(id);
  if (!error) {
    window.$message?.success('删除成功');
    onDeleted();
  }
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
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px">
    <!-- 搜索区域 -->
    <FileSearch v-model:model="searchParams" @reset="getData" @search="getData"/>


    <ACard :bordered="false" :body-style="{ padding: '0' }">
      <!-- 自定义标题插槽 -->
      <template #title>
        <div class="flex justify-between items-center w-full">
          <span class="text-base font-medium">文件列表</span>
          <div class="flex gap-12px flex-wrap">
            <Button type="primary" ghost @click="showUploadDrawer">
              <template #icon>
                <icon-ic-round-upload class="align-sub text-icon" />
              </template>
              上传文件</Button>
            <Button type="primary" ghost :disabled="checkedRowKeys.length === 0" @click="handleBatchDelete">
              批量删除
            </Button>
          </div>
        </div>
      </template>

      <ATable
        :columns="columns"
        :data-source="data"
        :row-key="record => record.id"
        :pagination="mobilePagination"
        :loading="loading"
        :row-selection="rowSelection"
        :scroll="{ x: 1200 }"
        size="small"
        class="h-full"
        @change="handleTableChange"
      />
    </ACard>


    <!-- 文件上传抽屉 -->
    <FileUploadDrawer v-model:visible="uploadVisible" @uploaded="getData"/>

    <!-- 文件预览模态框 -->
    <Modal v-model:open="previewVisible" title="文件预览" :footer="null" centered :width="800">
      <div v-if="previewFile" class="preview-content">
        <!-- 图片预览 -->
        <div v-if="isImageFile(previewFile.contentType)" class="text-center">
          <img :src="previewFile.previewUrl || previewFile.url" :alt="previewFile.originalName"/>
        </div>
        
        <!-- PDF预览 -->
        <div v-else-if="previewFile.previewType === 3" class="text-center" style="height: 600px;">
          <iframe :src="previewFile.previewUrl" class="w-full h-full border-0" title="PDF文件预览"></iframe>
        </div>
        
        <!-- Office文档预览 -->
        <div v-else-if="previewFile.previewType === 2" class="text-center" style="height: 600px;">
          <iframe :src="previewFile.previewUrl" class="w-full h-full border-0" title="Office文件预览"></iframe>
        </div>
        
        <!-- 视频预览 -->
        <div v-else-if="previewFile.previewType === 4" class="text-center">
          <video 
            controls 
            class="max-w-full" 
            style="max-height: 500px; margin: 0 auto;"
          >
            <source :src="previewFile.previewUrl" :type="previewFile.contentType">
            您的浏览器不支持视频预览
          </video>
        </div>
        
        <!-- 音频预览 -->
        <div v-else-if="previewFile.previewType === 5" class="text-center py-10">
          <div class="mb-5 text-xl">🎵 音频文件播放</div>
          <audio controls class="w-full">
            <source :src="previewFile.previewUrl" :type="previewFile.contentType">
            您的浏览器不支持音频预览
          </audio>
          <div class="mt-4 text-gray-500">{{ previewFile.originalName }}</div>
        </div>
        
        <!-- 文本预览 -->
        <div v-else-if="previewFile.previewType === 6" class="text-center" style="height: 600px;">
          <iframe :src="previewFile.previewUrl" class="w-full h-full border-0" title="文本文件预览"></iframe>
        </div>
        
        <!-- 其他类型文件 -->
        <div v-else class="py-40px text-center">
          <div class="mb-4 text-6xl">📄</div>
          <div class="text-lg">{{ previewFile.originalName }}</div>
          <div class="mt-8px text-gray-500">此文件类型不支持直接预览</div>
          <Button type="primary" class="mt-16px" @click="handleDownload(previewFile)">下载文件</Button>
        </div>
      </div>
    </Modal>

    <!-- 文件详情抽屉 -->
    <FileOperateDrawer
      v-model:visible="detailVisible"
      :file-data="detailFile"
      @download="handleDownload"
      @preview="handlePreview"
    />
  </div>
</template>

<style scoped>

.preview-content img {
  max-width: 100%;
  max-height: 500px;
  object-fit: contain;
}

.upload-content {
  padding: 16px 0;
}
</style>
