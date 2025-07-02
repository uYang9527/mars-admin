<script setup lang="ts">
import { computed, reactive, watch } from 'vue';
import { useAntdForm, useFormRules } from '@/hooks/common/form';
import {
  fetchAddConfig,
  fetchCheckConfigKeyUnique,
  fetchGetConfigById,
  fetchUpdateConfig
} from '@/service/api/system-config';
defineOptions({
  name: 'ConfigOperateDrawer'
});

/**
 * 操作类型
 *
 * - add: 新增配置
 * - edit: 编辑配置
 */
export type OperateType = 'add' | 'edit';

interface Props {
  /** 操作类型 */
  operateType: OperateType;
  /** 编辑行数据 */
  rowData?: Api.SystemManage.Config | null;
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
    add: '新增参数配置',
    edit: '编辑参数配置'
  };
  return titles[props.operateType];
});

type Model = Pick<Api.SystemManage.Config, 'configName' | 'configKey' | 'configValue' | 'configType' | 'remark'>;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    configName: '',
    configKey: '',
    configValue: '',
    configType: 'N',
    remark: ''
  };
}

type RuleKey = Extract<keyof Model, 'configName' | 'configKey' | 'configValue'>;

const rules = computed<Record<RuleKey, App.Global.FormRule[]>>(() => {
  return {
    configName: [
      { required: true, message: '请输入参数名称', trigger: 'blur' },
      { max: 100, message: '参数名称长度不能超过100个字符', trigger: 'blur' }
    ],
    configKey: [
      { required: true, message: '请输入参数键名', trigger: 'blur' },
      { max: 100, message: '参数键名长度不能超过100个字符', trigger: 'blur' },
      {
        validator: async (_, value) => {
          if (!value) return Promise.resolve();

          const isUnique = await fetchCheckConfigKeyUnique(value, props.rowData?.id);
          if (!isUnique.data) {
            return Promise.reject(new Error('参数键名已存在'));
          }
          return Promise.resolve();
        },
        trigger: 'blur'
      }
    ],
    configValue: [{ required: true, message: '请输入参数键值', trigger: 'blur' }]
  };
});

const configTypeOptions = [
  { label: '是', value: 'Y' },
  { label: '否', value: 'N' }
];

function handleInitModel() {
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData) {
    fetchGetConfigById(props.rowData.id)
      .then(({ data }) => {
        if (data) {
          Object.assign(model, data);
        }
      })
      .catch(error => {
        console.error('获取参数配置详情失败', error);
      });
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
    request = fetchAddConfig(model);
  } else {
    request = fetchUpdateConfig({ ...model, id: props.rowData?.id });
  }

  const { error } = await request;

  if (!error) {
    window.$message?.success(isAdd ? '新增参数配置成功' : '编辑参数配置成功');
    closeDrawer();
    emit('submitted');
  }
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    resetFields();
  }
});
</script>

<template>
  <ADrawer
    v-model:open="visible"
    :title="title"
    :width="500"
    :body-style="{ paddingRight: '20px' }"
    :footer-style="{ textAlign: 'right' }"
  >
    <AForm ref="formRef" :model="model" :rules="rules" :label-col="{ span: 4 }" :wrapper-col="{ span: 20 }">
      <AFormItem label="参数名称" name="configName">
        <AInput v-model:value="model.configName" placeholder="请输入参数名称" />
      </AFormItem>
      <AFormItem label="参数键名" name="configKey">
        <AInput v-model:value="model.configKey" placeholder="请输入参数键名" :disabled="props.operateType === 'edit'" />
      </AFormItem>
      <AFormItem label="参数键值" name="configValue">
        <AInput v-model:value="model.configValue" placeholder="请输入参数键值" />
      </AFormItem>
      <AFormItem label="系统内置" name="configType">
        <ARadioGroup v-model:value="model.configType" :options="configTypeOptions" />
      </AFormItem>
      <AFormItem label="备注" name="remark">
        <ATextarea v-model:value="model.remark" placeholder="请输入备注" :rows="4" />
      </AFormItem>
    </AForm>
    <template #footer>
      <ASpace>
        <AButton @click="closeDrawer">取消</AButton>
        <AButton type="primary" @click="handleSubmit">确定</AButton>
      </ASpace>
    </template>
  </ADrawer>
</template>

<style scoped></style>
