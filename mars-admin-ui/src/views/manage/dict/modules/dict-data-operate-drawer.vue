<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import { Form } from 'ant-design-vue';
import {
  fetchAddDictData,
  fetchCheckDictValueUnique,
  fetchGetDictDataById,
  fetchUpdateDictData
} from '@/service/api/system-manage';
import { $t } from '@/locales';

export type OperateType = 'add' | 'edit';

const useForm = Form.useForm;

const props = defineProps<{
  visible: boolean;
  operateType: OperateType;
  rowData: any;
  dictType: string;
}>();

const emit = defineEmits<{
  (e: 'update:visible', visible: boolean): void;
  (e: 'submitted'): void;
}>();

const formRef = ref();
const loading = ref(false);

const statusOptions = [
  { label: '正常', value: 1 },
  { label: '停用', value: 0 }
];

const defaultOptions = [
  { label: '是', value: 'Y' },
  { label: '否', value: 'N' }
];

const formModel = reactive({
  id: null,
  dictLabel: '',
  dictValue: '',
  dictType: '',
  dictSort: 0,
  cssClass: '',
  listClass: '',
  isDefault: 'N',
  status: 1,
  remark: ''
});

const formRules = {
  dictLabel: [{ required: true, message: '请输入字典标签' }],
  dictValue: [{ required: true, message: '请输入字典键值' }],
  dictType: [{ required: true, message: '请选择字典类型' }],
  dictSort: [{ required: true, message: '请输入字典排序' }]
};

const { validateInfos, validate } = useForm(formModel, formRules);

watch(
  () => props.visible,
  async newVisible => {
    if (newVisible) {
      resetForm();

      if (props.operateType === 'edit' && props.rowData) {
        loading.value = true;
        try {
          const { data } = await fetchGetDictDataById(props.rowData.id);
          if (data) {
            Object.assign(formModel, data);
          }
        } catch (error) {
          console.error('获取字典数据详情失败', error);
        } finally {
          loading.value = false;
        }
      } else {
        // 新增时设置字典类型
        formModel.dictType = props.dictType;
      }
    }
  }
);

function resetForm() {
  Object.assign(formModel, {
    id: null,
    dictLabel: '',
    dictValue: '',
    dictType: '',
    dictSort: 0,
    cssClass: '',
    listClass: '',
    isDefault: 'N',
    status: 1,
    remark: ''
  });
}

async function checkDictValueUnique() {
  if (!formModel.dictType || !formModel.dictValue) {
    return true;
  }

  const { data } = await fetchCheckDictValueUnique(formModel.dictType, formModel.dictValue, formModel.id);
  return data;
}

async function handleSubmit() {
  try {
    await validate();

    // 校验字典键值是否唯一
    const isUnique = await checkDictValueUnique();
    if (!isUnique) {
      window.$message?.error('字典键值已存在');
      return;
    }

    loading.value = true;

    let result;
    const isAdd = props.operateType === 'add';
    if (isAdd) {
      result = await fetchAddDictData(formModel);
    } else {
      result = await fetchUpdateDictData(formModel);
    }

    if (!result.error) {
      window.$message?.success(isAdd ? '新增字典数据成功' : '编辑字典数据成功');
      emit('update:visible', false);
      emit('submitted');
    }
  } catch (error) {
    console.error('表单验证失败', error);
  } finally {
    loading.value = false;
  }
}

function handleCancel() {
  emit('update:visible', false);
}

defineExpose({
  formModel
});
</script>

<template>
  <ADrawer
    :title="operateType === 'add' ? '新增字典数据' : '编辑字典数据'"
    :width="500"
    :visible="visible"
    :confirm-loading="loading"
    @update:visible="$emit('update:visible', $event)"
    @close="handleCancel"
  >
    <ASpin :spinning="loading">
      <AForm
        ref="formRef"
        :model="formModel"
        label-placement="left"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 20 }"
      >
        <AFormItem label="字典类型">
          <AInput v-model:value="formModel.dictType" disabled />
        </AFormItem>
        <AFormItem label="字典标签" v-bind="validateInfos.dictLabel">
          <AInput v-model:value="formModel.dictLabel" placeholder="请输入字典标签" allow-clear />
        </AFormItem>
        <AFormItem label="字典键值" v-bind="validateInfos.dictValue">
          <AInput v-model:value="formModel.dictValue" placeholder="请输入字典键值" allow-clear />
        </AFormItem>
        <AFormItem label="字典排序" v-bind="validateInfos.dictSort">
          <AInputNumber v-model:value="formModel.dictSort" :min="0" style="width: 100%" />
        </AFormItem>
        <AFormItem label="状态">
          <ASelect v-model:value="formModel.status">
            <ASelectOption v-for="item in statusOptions" :key="item.value" :value="item.value">
              {{ item.label }}
            </ASelectOption>
          </ASelect>
        </AFormItem>
        <AFormItem label="是否默认">
          <ASelect v-model:value="formModel.isDefault">
            <ASelectOption v-for="item in defaultOptions" :key="item.value" :value="item.value">
              {{ item.label }}
            </ASelectOption>
          </ASelect>
        </AFormItem>
        <AFormItem label="CSS类名">
          <AInput v-model:value="formModel.cssClass" placeholder="请输入CSS类名" allow-clear />
        </AFormItem>
        <AFormItem label="表格回显样式">
          <AInput v-model:value="formModel.listClass" placeholder="请输入表格回显样式" allow-clear />
        </AFormItem>
        <AFormItem label="备注">
          <ATextarea v-model:value="formModel.remark" placeholder="请输入备注" :rows="4" allow-clear />
        </AFormItem>
      </AForm>
    </ASpin>

    <template #footer>
      <ASpace>
        <AButton @click="handleCancel">{{ $t('common.cancel') }}</AButton>
        <AButton type="primary" :loading="loading" @click="handleSubmit">{{ $t('common.confirm') }}</AButton>
      </ASpace>
    </template>
  </ADrawer>
</template>

<style scoped></style>
