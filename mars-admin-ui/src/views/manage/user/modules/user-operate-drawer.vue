<script setup lang="ts">
import { computed, reactive, ref, watch } from 'vue';
import { useAntdForm, useFormRules } from '@/hooks/common/form';
import {
  fetchAddUser,
  fetchCheckEmailUnique,
  fetchCheckPhoneUnique,
  fetchCheckUsernameUnique,
  fetchGetAllRoles,
  fetchGetDeptTree,
  fetchGetNormalPosts,
  fetchGetUserById,
  fetchUpdateUser
} from '@/service/api';
import { $t } from '@/locales';

defineOptions({
  name: 'UserOperateDrawer'
});

/**
 * the type of operation
 *
 * - add: add user
 * - edit: edit user
 */
export type OperateType = 'add' | 'edit';

interface Props {
  /** the type of operation */
  operateType: OperateType;
  /** the edit row data */
  rowData?: Api.SystemManage.User | null;
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
    add: $t('page.manage.user.addUser'),
    edit: $t('page.manage.user.editUser')
  };
  return titles[props.operateType];
});

type Model = Pick<
  Api.SystemManage.User,
  'username' | 'nickname' | 'gender' | 'phone' | 'email' | 'status' | 'deptIds' | 'roleIds' | 'postIds' | 'remark'
>;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    username: '',
    nickname: '',
    gender: 0,
    phone: '',
    email: '',
    status: 1,
    deptIds: [],
    roleIds: [],
    postIds: [],
    remark: ''
  };
}

type RuleKey = Extract<keyof Model, 'username' | 'nickname' | 'phone' | 'email'>;

const rules = computed<Record<RuleKey, App.Global.FormRule[]>>(() => {
  const { patternRules } = useFormRules(); // inside computed to make locale reactive

  return {
    username: [
      formRules.userName,
      {
        validator: async (_, value) => {
          if (!value) return Promise.resolve();

          const isUnique = await fetchCheckUsernameUnique(value, props.rowData?.id);
          if (!isUnique.data) {
            return Promise.reject(new Error('用户名已存在'));
          }
          return Promise.resolve();
        },
        trigger: 'blur'
      }
    ],
    nickname: formRules.nickName,
    phone: [
      patternRules.phone,
      {
        validator: async (_, value) => {
          if (!value) return Promise.resolve();

          const isUnique = await fetchCheckPhoneUnique(value, props.rowData?.id);
          if (!isUnique.data) {
            return Promise.reject(new Error('手机号已存在'));
          }
          return Promise.resolve();
        },
        trigger: 'blur'
      }
    ],
    email: [
      patternRules.email,
      {
        validator: async (_, value) => {
          if (!value) return Promise.resolve();

          const isUnique = await fetchCheckEmailUnique(value, props.rowData?.id);
          if (!isUnique.data) {
            return Promise.reject(new Error('邮箱已存在'));
          }
          return Promise.resolve();
        },
        trigger: 'blur'
      }
    ]
  };
});

const genderOptions = [
  { label: '男', value: 0 },
  { label: '女', value: 1 }
];

const statusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
];

// 角色选项
const roleOptions = ref<Api.SystemManage.AllRole[]>([]);
// 岗位选项
const postOptions = ref<Api.SystemManage.Post[]>([]);
// 部门树选项
const deptTreeOptions = ref<Api.SystemManage.DeptTree[]>([]);

// 加载选项数据
async function loadOptions() {
  try {
    const [rolesRes, postsRes, deptTreeRes] = await Promise.all([
      fetchGetAllRoles(),
      fetchGetNormalPosts(),
      fetchGetDeptTree()
    ]);

    if (rolesRes.data) {
      roleOptions.value = rolesRes.data;
    }
    if (postsRes.data) {
      postOptions.value = postsRes.data;
    }
    if (deptTreeRes.data) {
      deptTreeOptions.value = deptTreeRes.data;
    }
  } catch (error) {
    console.error('加载选项数据失败:', error);
  }
}

async function handleInitModel() {
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData?.id) {
    // 获取用户详情
    const { data: userDetail } = await fetchGetUserById(props.rowData.id);
    if (userDetail) {
      console.log('获取到的用户详情数据:', userDetail);
      // 确保将数据正确赋值到模型中
      Object.assign(model, userDetail);
      console.log('合并后的模型数据:', model);
    } else {
      console.error('获取用户详情失败');
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
  const userId = props.rowData?.id;

  if (isAdd) {
    request = fetchAddUser(model);
  } else {
    request = fetchUpdateUser({ ...model, id: userId });
  }

  const { error } = await request;

  if (!error) {
    window.$message?.success(isAdd ? '新增用户成功' : '编辑用户成功');
    closeDrawer();
    emit('submitted');
  }
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    resetFields();
    loadOptions();
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
      <AFormItem label="用户名" name="username">
        <AInput v-model:value="model.username" placeholder="请输入用户名" />
      </AFormItem>
      <AFormItem label="昵称" name="nickname">
        <AInput v-model:value="model.nickname" placeholder="请输入昵称" />
      </AFormItem>
      <AFormItem label="性别" name="gender">
        <ARadioGroup v-model:value="model.gender" :options="genderOptions" />
      </AFormItem>
      <AFormItem label="手机号" name="phone">
        <AInput v-model:value="model.phone" placeholder="请输入手机号" />
      </AFormItem>
      <AFormItem label="邮箱" name="email">
        <AInput v-model:value="model.email" placeholder="请输入邮箱" />
      </AFormItem>
      <AFormItem label="状态" name="status">
        <ARadioGroup v-model:value="model.status" :options="statusOptions" />
      </AFormItem>
      <AFormItem label="部门" name="deptIds">
        <ATreeSelect
          v-model:value="model.deptIds"
          :tree-data="deptTreeOptions"
          :field-names="{ label: 'label', value: 'id', children: 'children' }"
          placeholder="请选择部门"
          tree-default-expand-all
          multiple
        />
      </AFormItem>
      <AFormItem label="角色" name="roleIds">
        <ASelect
          v-model:value="model.roleIds"
          mode="multiple"
          :options="roleOptions"
          :field-names="{ label: 'roleName', value: 'id' }"
          placeholder="请选择角色"
          :max-tag-count="3"
        />
      </AFormItem>
      <AFormItem label="岗位" name="postIds">
        <ASelect
          v-model:value="model.postIds"
          mode="multiple"
          :options="postOptions"
          :field-names="{ label: 'postName', value: 'id' }"
          placeholder="请选择岗位"
          :max-tag-count="3"
        />
      </AFormItem>
      <AFormItem label="备注" name="remark">
        <ATextarea v-model:value="model.remark" placeholder="请输入备注" :rows="3" />
      </AFormItem>
    </AForm>
    <template #footer>
      <div class="flex-y-center justify-end gap-12px">
        <AButton @click="closeDrawer">取消</AButton>
        <AButton type="primary" @click="handleSubmit">确认</AButton>
      </div>
    </template>
  </ADrawer>
</template>

<style scoped></style>
