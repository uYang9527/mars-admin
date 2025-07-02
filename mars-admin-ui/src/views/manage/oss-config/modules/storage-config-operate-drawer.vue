<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue';
import { Button, Drawer, Form, FormItem, Input, Radio, RadioGroup, Spin } from 'ant-design-vue';
import { useAntdForm, useFormRules } from '@/hooks/common/form';
import {
  fetchAddOssConfig,
  fetchGetOssConfigById,
  fetchTestOssConnection,
  fetchUpdateOssConfig
} from '@/service/api/file-manage';

defineOptions({
  name: 'StorageConfigOperateDrawer'
});

/**
 * the type of operation
 *
 * - add: add storage config
 * - edit: edit storage config
 */
export type OperateType = 'add' | 'edit';

interface Props {
  /** the type of operation */
  operateType: OperateType;
  /** the edit row data */
  rowData?: Api.FileManage.OssConfig | null;
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { formRef, validate, resetFields } = useAntdForm();
const { formRules } = useFormRules();

const title = computed(() => {
  const titles: Record<OperateType, string> = {
    add: '新增存储配置',
    edit: '编辑存储配置'
  };
  return titles[props.operateType];
});

type Model = Pick<
  Api.FileManage.OssConfig,
  | 'configKey'
  | 'accessKey'
  | 'secretKey'
  | 'bucketName'
  | 'prefix'
  | 'endpoint'
  | 'domain'
  | 'isHttps'
  | 'region'
  | 'remark'
  | 'status'
>;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    configKey: '',
    accessKey: '',
    secretKey: '',
    bucketName: '',
    prefix: '',
    endpoint: '',
    domain: '',
    isHttps: 1,
    region: '',
    remark: '',
    status: 1
  };
}

type RuleKey = Extract<keyof Model, 'configKey' | 'bucketName' | 'endpoint'>;

const rules = computed<Record<RuleKey, App.Global.FormRule[]>>(() => {
  return {
    configKey: [{ required: true, message: '请输入配置标识', trigger: 'blur' }],
    bucketName: [{ required: true, message: '请输入存储桶名称', trigger: 'blur' }],
    endpoint: [{ required: true, message: '请输入端点地址', trigger: 'blur' }]
  };
});

// 状态选项
const statusOptions = [
  { label: '启用', value: 1 },
  { label: '停用', value: 0 }
];

// 测试连接
const testLoading = ref(false);
// 数据加载状态
const dataLoading = ref(false);

async function testConnection() {
  await validate();

  testLoading.value = true;
  try {
    const { error } = await fetchTestOssConnection(model);
    if (!error) {
      window.$message?.success('连接测试成功');
    }
  } finally {
    testLoading.value = false;
  }
}

async function handleInitModel() {
  // 重置为默认模型（这会清空所有字段）
  const defaultModel = createDefaultModel();
  Object.keys(defaultModel).forEach(key => {
    model[key] = defaultModel[key];
  });

  if (props.operateType === 'edit' && props.rowData?.id) {
    // 编辑模式：调用API获取最新数据
    dataLoading.value = true;
    try {
      const { data, error } = await fetchGetOssConfigById(props.rowData.id);
      if (!error && data) {
        Object.assign(model, data);
      }
    } catch (err) {
      console.error('获取配置详情失败:', err);
      window.$message?.error('获取配置详情失败');
    } finally {
      dataLoading.value = false;
    }
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  let request;
  const isAdd = props.operateType === 'add';

  if (isAdd) {
    request = fetchAddOssConfig(model);
  } else {
    request = fetchUpdateOssConfig({ ...model, id: props.rowData?.id });
  }

  const { error } = await request;

  if (!error) {
    window.$message?.success(isAdd ? '新增存储配置成功' : '编辑存储配置成功');
    closeDrawer();
    emit('submitted');
  }
}

watch(visible, () => {
  if (visible.value) {
    // 先重置表单状态
    resetFields();
    // 再处理数据初始化
    handleInitModel();
  }
});
</script>

<template>
  <Drawer
    v-model:open="visible"
    :title="title"
    :width="500"
    :body-style="{ paddingRight: '20px' }"
    :footer-style="{ textAlign: 'right' }"
  >
    <!-- 数据加载中 -->
    <div v-if="dataLoading" class="h-200px flex items-center justify-center">
      <Spin size="large" />
      <span class="ml-12px">正在加载配置详情...</span>
    </div>

    <!-- 表单内容 -->
    <Form v-else ref="formRef" :model="model" :rules="rules" :label-col="{ span: 5 }" :wrapper-col="{ span: 19 }">
      <FormItem label="配置标识" name="configKey">
        <Input
          v-model:value="model.configKey"
          placeholder="请输入配置标识（如：local、minio、aliyun等）"
          :disabled="operateType === 'edit'"
        />
      </FormItem>

      <FormItem label="Access Key">
        <Input v-model:value="model.accessKey" placeholder="请输入Access Key" />
      </FormItem>
      <FormItem label="Secret Key">
        <Input.Password v-model:value="model.secretKey" placeholder="请输入Secret Key" />
      </FormItem>
      <FormItem label="存储桶名称" name="bucketName">
        <Input v-model:value="model.bucketName" placeholder="请输入存储桶名称" />
      </FormItem>
      <FormItem label="前缀">
        <Input v-model:value="model.prefix" placeholder="请输入文件路径前缀" />
      </FormItem>
      <FormItem label="端点地址" name="endpoint">
        <Input v-model:value="model.endpoint" placeholder="请输入端点地址" />
      </FormItem>
      <FormItem label="自定义域名">
        <Input v-model:value="model.domain" placeholder="请输入自定义域名" />
      </FormItem>
      <FormItem label="HTTPS">
        <RadioGroup v-model:value="model.isHttps">
          <Radio :value="1">启用</Radio>
          <Radio :value="0">禁用</Radio>
        </RadioGroup>
      </FormItem>

      <FormItem v-if="model.configKey === 'aliyun'" label="地域">
        <Input v-model:value="model.region" placeholder="请输入地域" />
      </FormItem>

      <FormItem label="状态">
        <RadioGroup v-model:value="model.status" :options="statusOptions" />
      </FormItem>
      <FormItem label="备注">
        <Input.TextArea v-model:value="model.remark" placeholder="请输入备注" :rows="3" />
      </FormItem>
    </Form>

    <template #footer>
      <div class="flex-y-center justify-between">
        <Button
          v-if="model.configKey && model.configKey !== 'local'"
          :loading="testLoading"
          :disabled="dataLoading"
          @click="testConnection"
        >
          测试连接
        </Button>
        <div class="flex gap-12px">
          <Button @click="closeDrawer">取消</Button>
          <Button type="primary" :disabled="dataLoading" @click="handleSubmit">确认</Button>
        </div>
      </div>
    </template>
  </Drawer>
</template>

<style scoped></style>
