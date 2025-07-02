<script setup lang="ts">
import { computed, reactive, watch } from 'vue';
import { useAntdForm, useFormRules } from '@/hooks/common/form';
import {
  fetchAddPost,
  fetchCheckPostCodeUnique,
  fetchCheckPostNameUnique,
  fetchGetPostById,
  fetchUpdatePost
} from '@/service/api/system-manage';

defineOptions({
  name: 'PostOperateDrawer'
});

/**
 * the type of operation
 *
 * - add: add post
 * - edit: edit post
 */
export type OperateType = 'add' | 'edit';

interface Props {
  /** the type of operation */
  operateType: OperateType;
  /** the edit row data */
  rowData?: Api.SystemManage.Post | null;
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
    add: '新增岗位',
    edit: '编辑岗位'
  };
  return titles[props.operateType];
});

type Model = Pick<Api.SystemManage.Post, 'postName' | 'postCode' | 'postSort' | 'status' | 'remark'>;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    postName: '',
    postCode: '',
    postSort: 0,
    status: '1',
    remark: ''
  };
}

type RuleKey = Extract<keyof Model, 'postName' | 'postCode'>;

const rules = computed<Record<RuleKey, App.Global.FormRule[]>>(() => {
  return {
    postName: [
      { required: true, message: '请输入岗位名称', trigger: 'blur' },
      { max: 50, message: '岗位名称长度不能超过50个字符', trigger: 'blur' },
      {
        validator: async (_, value) => {
          if (!value) return Promise.resolve();

          const isUnique = await fetchCheckPostNameUnique(value, props.rowData?.id);
          if (!isUnique.data) {
            return Promise.reject(new Error('岗位名称已存在'));
          }
          return Promise.resolve();
        },
        trigger: 'blur'
      }
    ],
    postCode: [
      { required: true, message: '请输入岗位编码', trigger: 'blur' },
      { max: 50, message: '岗位编码长度不能超过50个字符', trigger: 'blur' },
      {
        validator: async (_, value) => {
          if (!value) return Promise.resolve();

          const isUnique = await fetchCheckPostCodeUnique(value, props.rowData?.id);
          if (!isUnique.data) {
            return Promise.reject(new Error('岗位编码已存在'));
          }
          return Promise.resolve();
        },
        trigger: 'blur'
      }
    ]
  };
});

const statusOptions = [
  { label: '启用', value: '1' },
  { label: '禁用', value: '0' }
];

function handleInitModel() {
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData) {
    fetchGetPostById(props.rowData.id)
      .then(({ data }) => {
        if (data) {
          const rowDataWithStringStatus = {
            ...data,
            status: data.status !== null ? String(data.status) : '1'
          };
          Object.assign(model, rowDataWithStringStatus);
        }
      })
      .catch(error => {
        console.error('获取岗位详情失败', error);
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
    request = fetchAddPost(model);
  } else {
    request = fetchUpdatePost({ ...model, id: props.rowData?.id });
  }

  const { error } = await request;

  if (!error) {
    window.$message?.success(isAdd ? '新增岗位成功' : '编辑岗位成功');
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
      <AFormItem label="岗位名称" name="postName">
        <AInput v-model:value="model.postName" placeholder="请输入岗位名称" />
      </AFormItem>
      <AFormItem label="岗位编码" name="postCode">
        <AInput v-model:value="model.postCode" placeholder="请输入岗位编码" />
      </AFormItem>
      <AFormItem label="岗位顺序" name="postSort">
        <AInputNumber v-model:value="model.postSort" :min="0" class="w-full" />
      </AFormItem>
      <AFormItem label="状态" name="status">
        <ARadioGroup v-model:value="model.status" :options="statusOptions" />
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
