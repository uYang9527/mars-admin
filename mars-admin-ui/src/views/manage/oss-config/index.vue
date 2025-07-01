<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <!-- 搜索区域 -->
    <ACard title="OSS配置管理" :bordered="false" class="card-wrapper">
      <AForm class="search-form">
        <ARow :gutter="[16, 16]">
          <ACol :span="8">
            <AFormItem label="配置key">
              <AInput v-model:value="searchParams.configKey" placeholder="请输入配置key" />
            </AFormItem>
          </ACol>
          <ACol :span="8">
            <AFormItem label="状态">
              <ASelect v-model:value="searchParams.status" placeholder="请选择状态" allow-clear>
                <ASelectOption :value="1">启用</ASelectOption>
                <ASelectOption :value="0">停用</ASelectOption>
              </ASelect>
            </AFormItem>
          </ACol>
          <ACol :span="8">
            <div class="flex justify-end gap-12px">
              <AButton @click="() => { searchParams.configKey = ''; searchParams.status = null; getData(); }">
                重置
              </AButton>
              <AButton type="primary" @click="getData">
                查询
              </AButton>
            </div>
          </ACol>
        </ARow>
      </AForm>
    </ACard>

    <!-- 操作按钮 -->
    <ACard :bordered="false" class="card-wrapper">
      <div class="flex gap-12px">
        <AButton type="primary" @click="add">
          新增配置
        </AButton>
        <AButton
          type="primary"
          ghost
          :disabled="checkedRowKeys.length === 0"
          @click="handleBatchDelete"
        >
          批量删除
        </AButton>
      </div>
    </ACard>

    <!-- OSS配置列表 -->
    <ACard title="配置列表" :bordered="false" class="card-wrapper flex-1-hidden">
      <ATable
        :columns="columns"
        :data-source="data"
        :row-key="record => record.id"
        :pagination="mobilePagination"
        :loading="loading"
        :row-selection="rowSelection"
        :scroll="{ x: 1400 }"
        @change="getDataByPage"
      />
    </ACard>

    <!-- 新增/编辑模态框 -->
    <AModal
      v-model:open="formVisible"
      :title="isEdit ? '编辑OSS配置' : '新增OSS配置'"
      :width="800"
      @ok="handleSubmit"
      @cancel="() => formVisible = false"
    >
      <AForm
        ref="formRef"
        :model="formData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
      >
        <AFormItem label="配置key" name="configKey">
          <AInput v-model:value="formData.configKey" placeholder="请输入配置key" :disabled="isEdit" />
        </AFormItem>
        
        <AFormItem label="存储类型" name="storageType">
          <ARadioGroup v-model:value="formData.storageType" @change="handleStorageTypeChange">
            <ARadio value="local">本地存储</ARadio>
            <ARadio value="minio">MinIO</ARadio>
            <ARadio value="aliyun">阿里云OSS</ARadio>
          </ARadioGroup>
        </AFormItem>

        <template v-if="formData.storageType !== 'local'">
          <AFormItem label="Access Key" name="accessKey">
            <AInput v-model:value="formData.accessKey" placeholder="请输入Access Key" />
          </AFormItem>
          
          <AFormItem label="Secret Key" name="secretKey">
            <AInputPassword v-model:value="formData.secretKey" placeholder="请输入Secret Key" />
          </AFormItem>
          
          <AFormItem label="桶名称" name="bucketName">
            <AInput v-model:value="formData.bucketName" placeholder="请输入桶名称" />
          </AFormItem>
          
          <AFormItem label="访问端点" name="endpoint">
            <AInput v-model:value="formData.endpoint" placeholder="请输入访问端点" />
          </AFormItem>
          
          <AFormItem label="自定义域名" name="domain">
            <AInput v-model:value="formData.domain" placeholder="请输入自定义域名（可选）" />
          </AFormItem>
          
          <AFormItem label="区域" name="region">
            <AInput v-model:value="formData.region" placeholder="请输入区域（可选）" />
          </AFormItem>
        </template>
        
        <AFormItem label="前缀" name="prefix">
          <AInput v-model:value="formData.prefix" placeholder="请输入文件前缀（可选）" />
        </AFormItem>
        
        <AFormItem label="HTTPS" name="isHttps">
          <ASwitch v-model:checked="formData.isHttps" />
        </AFormItem>
        
        <AFormItem label="状态" name="status">
          <ARadioGroup v-model:value="formData.status">
            <ARadio :value="1">启用</ARadio>
            <ARadio :value="0">停用</ARadio>
          </ARadioGroup>
        </AFormItem>
        
        <AFormItem label="备注" name="remark">
          <ATextarea v-model:value="formData.remark" placeholder="请输入备注（可选）" :rows="3" />
        </AFormItem>
      </AForm>
    </AModal>
  </div>
</template>

<script setup lang="tsx">
import { reactive, ref } from 'vue';
import { Button, Popconfirm, Tag, Modal, Form, Input, Select, Switch, Radio } from 'ant-design-vue';
import { useTable, useTableOperate } from '@/hooks/common/table';
import {
  fetchGetOssConfigList,
  fetchAddOssConfig,
  fetchUpdateOssConfig,
  fetchDeleteOssConfig,
  fetchDeleteOssConfigs,
  fetchUpdateOssConfigStatus,
  fetchTestOssConnection
} from '@/service/api/file-manage';
import { $t } from '@/locales';
import { enableStatusRecord } from '@/constants/business';

defineOptions({
  name: 'OssConfig'
});

const wrapperEl = ref<HTMLElement>();

const { columns, columnChecks, data, getData, getDataByPage, loading, mobilePagination, searchParams } =
  useTable({
    apiFn: fetchGetOssConfigList,
    apiParams: {
      current: 1,
      size: 10,
      configKey: '',
      status: null
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
        key: 'configKey',
        title: '配置Key',
        dataIndex: 'configKey',
        align: 'center'
      },
      {
        key: 'storageType',
        title: '存储类型',
        dataIndex: 'storageType',
        align: 'center',
        customRender: ({ record }) => {
          const typeMap = {
            local: '本地存储',
            minio: 'MinIO',
            aliyun: '阿里云OSS'
          };
          return typeMap[record.storageType] || record.storageType;
        }
      },
      {
        key: 'bucketName',
        title: '桶名称',
        dataIndex: 'bucketName',
        align: 'center'
      },
      {
        key: 'endpoint',
        title: '访问端点',
        dataIndex: 'endpoint',
        align: 'center',
        ellipsis: true
      },
      {
        key: 'status',
        title: '状态',
        dataIndex: 'status',
        align: 'center',
        customRender: ({ record }) => {
          if (record.status === null) {
            return null;
          }
          const label = record.status === 1 ? '启用' : '停用';
          return <Tag color={record.status === 1 ? 'success' : 'error'}>{label}</Tag>;
        }
      },
      {
        key: 'createTime',
        title: '创建时间',
        dataIndex: 'createTime',
        align: 'center'
      },
      {
        key: 'operate',
        title: $t('common.operate'),
        align: 'center',
        customRender: ({ record }) => (
          <div class="flex-center gap-8px">
            <Button type="primary" ghost size="small" onClick={() => edit(record.id)}>
              {$t('common.edit')}
            </Button>
            <Button size="small" onClick={() => testConnection(record.id)}>
              测试连接
            </Button>
            <Button
              type={record.status === 1 ? 'default' : 'primary'}
              size="small"
              onClick={() => toggleStatus(record.id, record.status)}
            >
              {record.status === 1 ? '停用' : '启用'}
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

// 表单相关
const formVisible = ref(false);
const formRef = ref();
const isEdit = ref(false);
const formData = reactive({
  id: null,
  configKey: '',
  storageType: 'local',
  accessKey: '',
  secretKey: '',
  bucketName: '',
  endpoint: '',
  domain: '',
  region: '',
  prefix: '',
  isHttps: false,
  status: 1,
  remark: ''
});

function add() {
  formVisible.value = true;
  isEdit.value = false;
  resetForm();
}

function edit(id: number) {
  const record = data.value.find(item => item.id === id);
  if (record) {
    formVisible.value = true;
    isEdit.value = true;
    Object.assign(formData, record);
  }
}

function resetForm() {
  Object.assign(formData, {
    id: null,
    configKey: '',
    storageType: 'local',
    accessKey: '',
    secretKey: '',
    bucketName: '',
    endpoint: '',
    domain: '',
    region: '',
    prefix: '',
    isHttps: false,
    status: 1,
    remark: ''
  });
}

async function handleSubmit() {
  try {
    const api = isEdit.value ? fetchUpdateOssConfig : fetchAddOssConfig;
    const { error } = await api(formData);
    if (!error) {
      window.$message?.success(isEdit.value ? '更新成功' : '添加成功');
      formVisible.value = false;
      getData();
    }
  } catch (error) {
    window.$message?.error('操作失败');
  }
}

async function handleBatchDelete() {
  if (checkedRowKeys.value.length === 0) {
    window.$message?.warning('请选择要删除的配置');
    return;
  }

  Modal.confirm({
    title: '批量删除配置',
    content: `确定要删除选中的 ${checkedRowKeys.value.length} 个配置吗？删除后无法恢复！`,
    okText: '确定',
    cancelText: '取消',
    async onOk() {
      const { error } = await fetchDeleteOssConfigs(checkedRowKeys.value);
      if (!error) {
        window.$message?.success('批量删除成功');
        onBatchDeleted();
      }
    }
  });
}

async function handleDelete(id: number) {
  const { error } = await fetchDeleteOssConfig(id);
  if (!error) {
    window.$message?.success('删除成功');
    onDeleted();
  }
}

async function toggleStatus(id: number, status: number) {
  const newStatus = status === 1 ? 0 : 1;
  const { error } = await fetchUpdateOssConfigStatus(id, newStatus);
  if (!error) {
    window.$message?.success('状态修改成功');
    getData();
  }
}

async function testConnection(id: number) {
  try {
    const { error } = await fetchTestOssConnection(id);
    if (!error) {
      window.$message?.success('连接测试成功');
    } else {
      window.$message?.error('连接测试失败');
    }
  } catch (error) {
    window.$message?.error('连接测试失败');
  }
}

function handleStorageTypeChange() {
  // 根据存储类型重置相关字段
  if (formData.storageType === 'local') {
    formData.accessKey = '';
    formData.secretKey = '';
    formData.bucketName = '';
    formData.endpoint = '';
    formData.domain = '';
    formData.region = '';
  }
}
</script>

<style scoped>
.card-wrapper {
  background: #fff;
}

.search-form {
  margin-bottom: 0;
}
</style> 