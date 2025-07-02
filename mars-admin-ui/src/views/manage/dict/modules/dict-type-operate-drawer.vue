<script setup lang="ts">
import { reactive, ref, watch } from 'vue';
import { Form } from 'ant-design-vue';
import {
  fetchAddDictType,
  fetchCheckDictNameUnique,
  fetchCheckDictTypeUnique,
  fetchGetDictTypeById,
  fetchUpdateDictType
} from '@/service/api/system-manage';
import { $t } from '@/locales';

export type OperateType = 'add' | 'edit';

const useForm = Form.useForm;

const props = defineProps<{
  visible: boolean;
  operateType: OperateType;
  rowData: any;
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

const formModel = reactive({
  id: null,
  dictName: '',
  dictType: '',
  status: 1,
  remark: ''
});

const formRules = {
  dictName: [{ required: true, message: '请输入字典名称' }],
  dictType: [{ required: true, message: '请输入字典类型' }]
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
          const { data } = await fetchGetDictTypeById(props.rowData.id);
          if (data) {
            Object.assign(formModel, data);
          }
        } catch (error) {
          console.error('获取字典类型详情失败', error);
        } finally {
          loading.value = false;
        }
      }
    }
  }
);

function resetForm() {
  Object.assign(formModel, {
    id: null,
    dictName: '',
    dictType: '',
    status: 1,
    remark: ''
  });
}

async function checkDictTypeUnique() {
  if (!formModel.dictType) {
    return true;
  }

  const { data } = await fetchCheckDictTypeUnique(formModel.dictType, formModel.id);
  return data;
}

async function checkDictNameUnique() {
  if (!formModel.dictName) {
    return true;
  }

  const { data } = await fetchCheckDictNameUnique(formModel.dictName, formModel.id);
  return data;
}

async function handleSubmit() {
  try {
    await validate();

    // 校验字典类型是否唯一
    const isTypeUnique = await checkDictTypeUnique();
    if (!isTypeUnique) {
      window.$message?.error('字典类型已存在');
      return;
    }

    // 校验字典名称是否唯一
    const isNameUnique = await checkDictNameUnique();
    if (!isNameUnique) {
      window.$message?.error('字典名称已存在');
      return;
    }

    loading.value = true;

    let result;
    const isAdd = props.operateType === 'add';
    if (isAdd) {
      result = await fetchAddDictType(formModel);
    } else {
      result = await fetchUpdateDictType(formModel);
    }

    if (!result.error) {
      window.$message?.success(isAdd ? '新增字典类型成功' : '编辑字典类型成功');
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
    :title="operateType === 'add' ? '新增字典类型' : '编辑字典类型'"
    :width="500"
    :visible="visible"
    :confirm-loading="loading"
    :footer-style="{ textAlign: 'right' }"
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
        <AFormItem label="字典名称" v-bind="validateInfos.dictName">
          <AInput v-model:value="formModel.dictName" placeholder="请输入字典名称" allow-clear />
        </AFormItem>
        <AFormItem label="字典类型" v-bind="validateInfos.dictType">
          <AInput v-model:value="formModel.dictType" placeholder="请输入字典类型" allow-clear />
        </AFormItem>
        <AFormItem label="状态">
          <ASelect v-model:value="formModel.status">
            <ASelectOption v-for="item in statusOptions" :key="item.value" :value="item.value">
              {{ item.label }}
            </ASelectOption>
          </ASelect>
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
