<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <!-- 搜索区域 -->
    <Card title="文件管理" :bordered="false" class="card-wrapper">
      <Form class="search-form">
        <Row :gutter="[16, 16]">
          <Col :span="8">
            <Form.Item label="文件名">
              <Input v-model:value="searchParams.fileName" placeholder="请输入文件名" />
            </Form.Item>
          </Col>
          <Col :span="8">
            <Form.Item label="存储配置">
              <Input v-model:value="searchParams.configKey" placeholder="请输入存储配置" />
            </Form.Item>
          </Col>
          <Col :span="8">
            <div class="flex justify-end gap-12px">
              <Button @click="() => { searchParams.fileName = ''; searchParams.configKey = ''; getData(); }">
                重置
              </Button>
              <Button type="primary" @click="getData">查询</Button>
            </div>
          </Col>
        </Row>
      </Form>
    </Card>

    <!-- 文件上传和操作区域 -->
    <Card title="文件上传" :bordered="false" class="card-wrapper">
      <div class="flex flex-wrap gap-16px items-center">
        <Upload
          name="file"
          :multiple="true"
          :action="uploadAction"
          :data="uploadData"
          :headers="uploadHeaders"
          :show-upload-list="false"
          @change="handleUploadChange"
        >
          <Button type="primary" size="large">选择文件上传</Button>
        </Upload>

        <Button
          type="primary"
          size="large"
          ghost
          :disabled="checkedRowKeys.length === 0"
          @click="handleBatchDelete"
        >
          批量删除
        </Button>

        <Select v-model:value="selectedConfigKey" placeholder="选择存储配置" style="width: 200px" size="large">
          <Select.Option v-for="config in ossConfigs" :key="config.configKey" :value="config.configKey">
            {{ config.configKey }}
          </Select.Option>
        </Select>
      </div>
    </Card>

    <!-- 文件列表 -->
    <Card title="文件列表" :bordered="false" class="card-wrapper flex-1-hidden">
      <ATable
        :columns="columns"
        :data-source="data"
        :row-key="record => record.id"
        :pagination="mobilePagination"
        :loading="loading"
        :row-selection="rowSelection"
        :scroll="{ x: 1200 }"
        @change="getDataByPage"
      />
    </Card>

    <!-- 文件预览模态框 -->
    <Modal
      v-model:open="previewVisible"
      title="文件预览"
      :width="800"
      :footer="null"
      centered
    >
      <div v-if="previewFile" class="preview-content">
        <div v-if="isImageFile(previewFile.contentType)" class="text-center">
          <img :src="previewFile.url" :alt="previewFile.originalName" class="max-w-full max-h-500px" />
        </div>
        <div v-else class="text-center py-40px">
          <div class="text-lg">{{ previewFile.originalName }}</div>
          <div class="text-gray-500 mt-8px">此文件类型不支持预览</div>
          <Button type="primary" class="mt-16px" @click="handleDownload(previewFile)">
            下载文件
          </Button>
        </div>
      </div>
    </Modal>
  </div>
</template>

<script setup lang="tsx">
import { reactive, ref, onMounted, computed } from 'vue';
import { Button, Popconfirm, Tag, Modal, Upload, Select, message, Card, Form, Input, Row, Col } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import {
  fetchGetFileList,
  fetchDeleteFile,
  fetchDeleteFiles,
  fetchGetEnabledOssConfigs
} from '@/service/api/file-manage';
import { $t } from '@/locales';
import dayjs from 'dayjs';

defineOptions({
  name: 'FileManage'
});

const wrapperEl = ref<HTMLElement>();
const ossConfigs = ref<Api.FileManage.OssConfig[]>([]);
const selectedConfigKey = ref('local');
const previewVisible = ref(false);
const previewFile = ref<Api.FileManage.FileInfo | null>(null);

const { columns, columnChecks, data, getData, getDataByPage, loading, mobilePagination, searchParams } =
  useTable({
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
        width: 48,
        fixed: 'left'
      },
      {
        key: 'index',
        title: $t('common.index'),
        width: 64,
        align: 'center',
        customRender: ({ index }) => index + 1
      },
      {
        key: 'originalName',
        title: '文件名',
        dataIndex: 'originalName',
        align: 'center',
        ellipsis: true
      },
      {
        key: 'size',
        title: '文件大小',
        dataIndex: 'size',
        align: 'center',
        width: 100,
        customRender: ({ record }) => formatFileSize(record.size)
      },
      {
        key: 'contentType',
        title: '文件类型',
        dataIndex: 'contentType',
        align: 'center',
        width: 120,
        ellipsis: true
      },
      {
        key: 'configKey',
        title: '存储配置',
        dataIndex: 'configKey',
        align: 'center',
        width: 100
      },
      {
        key: 'createTime',
        title: '上传时间',
        dataIndex: 'createTime',
        align: 'center',
        width: 150,
        customRender: ({ record }) => {
          return record.createTime ? dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') : '';
        }
      },
      {
        key: 'operate',
        title: $t('common.operate'),
        align: 'center',
        width: 200,
        customRender: ({ record }) => (
          <div class="flex-center gap-8px">
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

const {
  drawerVisible,
  operateType,
  editingData,
  handleAdd,
  handleEdit,
  checkedRowKeys,
  onBatchDeleted,
  onDeleted,
  onSelectChange,
  rowSelection
} = useTableOperate(data, getData);

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
    message.error('获取OSS配置失败');
  }
}

// 文件大小格式化
function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

// 判断是否为图片文件
function isImageFile(contentType: string): boolean {
  return contentType && contentType.startsWith('image/');
}

// 文件预览
function handlePreview(file: Api.FileManage.FileInfo) {
  previewFile.value = file;
  previewVisible.value = true;
}

// 文件下载
function handleDownload(file: Api.FileManage.FileInfo) {
  const link = document.createElement('a');
  link.href = file.url;
  link.download = file.originalName;
  link.target = '_blank';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

// 上传配置
const uploadAction = `/api/file/upload`;

const uploadData = computed(() => ({ configKey: selectedConfigKey.value }));
const uploadHeaders = computed(() => {
  const token = localStorage.getItem('token');
  return token ? { Authorization: `Bearer ${token}` } : {};
});

// 处理文件上传
function handleUploadChange(info: any) {
  const { status } = info.file;
  if (status === 'done') {
    message.success(`${info.file.name} 文件上传成功`);
    getData();
  } else if (status === 'error') {
    message.error(`${info.file.name} 文件上传失败`);
  }
}

// 批量删除
async function handleBatchDelete() {
  if (checkedRowKeys.value.length === 0) {
    message.warning('请选择要删除的文件');
    return;
  }

  Modal.confirm({
    title: '批量删除文件',
    content: `确定要删除选中的 ${checkedRowKeys.value.length} 个文件吗？删除后无法恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      const { error } = await fetchDeleteFiles(checkedRowKeys.value);
      if (!error) {
        message.success('批量删除成功');
        onBatchDeleted();
      }
    }
  });
}

// 删除单个文件
async function handleDelete(id: number) {
  const { error } = await fetchDeleteFile(id);
  if (!error) {
    message.success('删除成功');
    onDeleted();
  }
}

onMounted(() => {
  loadOssConfigs();
});
</script>

<style scoped>
.card-wrapper {
  background: #fff;
}

.search-form {
  margin-bottom: 0;
}

.preview-content img {
  max-width: 100%;
  max-height: 500px;
  object-fit: contain;
}
</style> 