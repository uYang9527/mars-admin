<script setup lang="ts">
import { computed, nextTick, reactive, ref, watch } from 'vue';
import { useAntdForm, useFormRules } from '@/hooks/common/form';
import {
  fetchAddDept,
  fetchCheckDeptCodeUnique,
  fetchCheckDeptNameUnique,
  fetchGetDeptTree,
  fetchUpdateDept,
  fetchGetDeptById
} from '@/service/api';
defineOptions({
  name: 'DeptOperateDrawer'
});

/**
 * the type of operation
 *
 * - add: add dept
 * - edit: edit dept
 */
export type OperateType = 'add' | 'edit';

interface Props {
  /** the type of operation */
  operateType: OperateType;
  /** the edit row data */
  rowData?: Api.SystemManage.Dept | null;
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
    add: '新增部门',
    edit: '编辑部门'
  };
  return titles[props.operateType];
});

interface Model {
  parentId: number;
  deptName: string;
  deptCode: string;
  orderNum: number;
  leader: string;
  phone: string;
  email: string;
  status: string;
  remark: string;
}

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    parentId: 0,
    deptName: '',
    deptCode: '',
    orderNum: 0,
    leader: '',
    phone: '',
    email: '',
    status: '1',
    remark: ''
  };
}

type RuleKey = Extract<keyof Model, 'deptName' | 'deptCode' | 'phone' | 'email'>;

const rules = computed<Record<RuleKey, App.Global.FormRule[]>>(() => {
  const { patternRules } = useFormRules(); // inside computed to make locale reactive

  return {
    deptName: [
      { required: true, message: '请输入部门名称' },
      {
        validator: async (_, value) => {
          if (!value) return Promise.resolve();

          const isUnique = await fetchCheckDeptNameUnique(value, props.rowData?.id);
          if (!isUnique.data) {
            return Promise.reject(new Error('部门名称已存在'));
          }
          return Promise.resolve();
        },
        trigger: 'blur'
      }
    ],
    deptCode: [
      { required: true, message: '请输入部门编码' },
      {
        validator: async (_, value) => {
          if (!value) return Promise.resolve();

          const isUnique = await fetchCheckDeptCodeUnique(value, props.rowData?.id);
          if (!isUnique.data) {
            return Promise.reject(new Error('部门编码已存在'));
          }
          return Promise.resolve();
        },
        trigger: 'blur'
      }
    ],
    phone: [patternRules.phone],
    email: [patternRules.email]
  };
});

const statusOptions = [
  { label: '启用', value: '1' },
  { label: '禁用', value: '0' }
];

// 父部门树选项
const parentDeptOptions = ref<any[]>([]);

// 转换部门数据为树形选择器需要的格式
function transformDeptToTreeData(depts: Api.SystemManage.Dept[]): any[] {
  return depts.map(dept => ({
    id: dept.id,
    value: dept.id,
    label: dept.deptName,
    title: dept.deptName,
    children: dept.children ? transformDeptToTreeData(dept.children) : undefined
  }));
}

// 加载部门树数据
async function loadDeptTree() {
  const deptTreeRes = await fetchGetDeptTree();

  if (deptTreeRes.data) {
    const transformedData = transformDeptToTreeData(deptTreeRes.data);
    // 添加一个根节点选项
    parentDeptOptions.value = [
      {
        id: 0,
        value: 0,
        label: '无上级部门',
        title: '无上级部门',
        children: transformedData
      }
    ];
  }
}

async function handleInitModel() {
  // 先重置为默认值
  const defaultModel = createDefaultModel();
  model.parentId = defaultModel.parentId;
  model.deptName = defaultModel.deptName;
  model.deptCode = defaultModel.deptCode;
  model.orderNum = defaultModel.orderNum;
  model.leader = defaultModel.leader;
  model.phone = defaultModel.phone;
  model.email = defaultModel.email;
  model.status = defaultModel.status;
  model.remark = defaultModel.remark;

  if (props.operateType === 'edit' && props.rowData?.id) {
    try {
      // 通过API重新获取完整的部门详情数据，确保数据完整性
      const { data: deptDetail } = await fetchGetDeptById(props.rowData.id);
      
      if (deptDetail) {
        // 直接赋值给reactive对象的属性
        model.parentId = deptDetail.parentId || 0;
        model.deptName = deptDetail.deptName || '';
        model.deptCode = deptDetail.deptCode || '';
        model.orderNum = deptDetail.orderNum || 0;
        model.leader = deptDetail.leader || '';
        model.phone = deptDetail.phone || '';
        model.email = deptDetail.email || '';
    // 确保status为字符串类型
        model.status = typeof deptDetail.status === 'number' ? String(deptDetail.status) : (deptDetail.status || '1');
        model.remark = deptDetail.remark || '';

    // 强制触发表单更新
    nextTick(() => {
      if (formRef.value) {
        formRef.value.clearValidate();
      }
    });
      }
    } catch (error) {
      console.error('获取部门详情失败:', error);
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
    request = fetchAddDept(model);
  } else {
    request = fetchUpdateDept({ ...model, id: props.rowData?.id });
  }

  const { error } = await request;

  if (!error) {
    window.$message?.success(isAdd ? '新增部门成功' : '编辑部门成功');
    closeDrawer();
    emit('submitted');
  }
}

watch(visible, async () => {
  if (visible.value) {
    await loadDeptTree();
    await nextTick();
    // 先设置数据
    await handleInitModel();
    await nextTick();
    // 最后重置验证状态
    if (formRef.value) {
      formRef.value.clearValidate();
    }
  }
});

// 监听rowData变化，确保数据更新时能正确回显
watch(() => props.rowData, async () => {
  if (visible.value && props.rowData) {
    await handleInitModel();
  }
}, { deep: true });
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
      <AFormItem label="上级部门" name="parentId">
        <ATreeSelect
          v-model:value="model.parentId"
          :tree-data="parentDeptOptions"
          :field-names="{ label: 'label', value: 'value', children: 'children' }"
          placeholder="请选择上级部门"
          tree-default-expand-all
        />
      </AFormItem>
      <AFormItem label="部门名称" name="deptName">
        <AInput v-model:value="model.deptName" placeholder="请输入部门名称" />
      </AFormItem>
      <AFormItem label="部门编码" name="deptCode">
        <AInput v-model:value="model.deptCode" placeholder="请输入部门编码" />
      </AFormItem>
      <AFormItem label="显示顺序" name="orderNum">
        <AInputNumber v-model:value="model.orderNum" :min="0" placeholder="请输入显示顺序" class="w-full" />
      </AFormItem>
      <AFormItem label="负责人" name="leader">
        <AInput v-model:value="model.leader" placeholder="请输入负责人" />
      </AFormItem>
      <AFormItem label="联系电话" name="phone">
        <AInput v-model:value="model.phone" placeholder="请输入联系电话" />
      </AFormItem>
      <AFormItem label="邮箱" name="email">
        <AInput v-model:value="model.email" placeholder="请输入邮箱" />
      </AFormItem>
      <AFormItem label="状态" name="status">
        <ARadioGroup v-model:value="model.status" :options="statusOptions" />
      </AFormItem>
      <AFormItem label="备注" name="remark">
        <ATextarea v-model:value="model.remark" placeholder="请输入备注" :rows="4" />
      </AFormItem>
    </AForm>

    <template #footer>
      <div class="flex-y-center justify-end gap-12px">
        <AButton @click="closeDrawer">取消</AButton>
        <AButton type="primary" @click="handleSubmit">确定</AButton>
      </div>
    </template>
  </ADrawer>
</template>

<style scoped></style>
