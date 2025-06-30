<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { useBoolean } from '@sa/hooks';
import { useAntdForm, useFormRules } from '@/hooks/common/form';
import { $t } from '@/locales';
import { enableStatusOptions } from '@/constants/business';
import { fetchAddRole, fetchUpdateRole } from '@/service/api';
import MenuAuthModal from './menu-auth-modal.vue';
import ButtonAuthModal from './button-auth-modal.vue';
import DataAuthModal from './data-auth-modal.vue';

defineOptions({
  name: 'RoleOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: AntDesign.TableOperateType;
  /** the edit row data */
  rowData?: Api.SystemManage.Role | null;
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
const { defaultRequiredRule } = useFormRules();
const { bool: menuAuthVisible, setTrue: openMenuAuthModal } = useBoolean();
const { bool: buttonAuthVisible, setTrue: openButtonAuthModal } = useBoolean();
const { bool: dataAuthVisible, setTrue: openDataAuthModal } = useBoolean();

const title = computed(() => {
  const titles: Record<AntDesign.TableOperateType, string> = {
    add: $t('page.manage.role.addRole'),
    edit: $t('page.manage.role.editRole')
  };
  return titles[props.operateType];
});

type Model = Pick<Api.SystemManage.Role, 'roleName' | 'roleCode' | 'roleKey' | 'description' | 'status'>;

const model = ref(createDefaultModel());

function createDefaultModel(): Model {
  return {
    roleName: '',
    roleCode: '',
    roleKey: '',
    description: '',
    status: '1'
  };
}

type RuleKey = Exclude<keyof Model, 'description'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  roleName: defaultRequiredRule,
  roleCode: defaultRequiredRule,
  roleKey: defaultRequiredRule,
  status: defaultRequiredRule
};

const roleId = computed(() => props.rowData?.id || -1);

const isEdit = computed(() => props.operateType === 'edit');

function handleInitModel() {
  model.value = createDefaultModel();

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model.value, props.rowData);
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();
  
  let request;
  if (props.operateType === 'add') {
    request = fetchAddRole(model.value);
  } else {
    request = fetchUpdateRole({
      ...model.value,
      id: props.rowData?.id
    });
  }

  const { error } = await request;
  
  if (!error) {
    // 使用直接的中文提示
    window.$message?.success(props.operateType === 'add' ? '新增角色成功' : '编辑角色成功');
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
    <AForm
      ref="formRef"
      layout="vertical"
      :model="model"
      :rules="rules"
      :label-col="{ span: 4 }"
      :wrapper-col="{ span: 20 }"
    >
      <AFormItem :label="$t('page.manage.role.roleName')" name="roleName">
        <AInput v-model:value="model.roleName" :placeholder="$t('page.manage.role.form.roleName')" />
      </AFormItem>
      <AFormItem :label="$t('page.manage.role.roleCode')" name="roleCode">
        <AInput v-model:value="model.roleCode" :placeholder="$t('page.manage.role.form.roleCode')" />
      </AFormItem>
      <AFormItem :label="$t('page.manage.role.roleKey')" name="roleKey">
        <AInput v-model:value="model.roleKey" :placeholder="$t('page.manage.role.form.roleKey')" />
      </AFormItem>
      <AFormItem :label="$t('page.manage.role.roleStatus')" name="status">
        <ARadioGroup v-model:value="model.status">
          <ARadio v-for="item in enableStatusOptions" :key="item.value" :value="item.value">
            {{ $t(item.label) }}
          </ARadio>
        </ARadioGroup>
      </AFormItem>
      <AFormItem :label="$t('page.manage.role.roleDesc')" name="description">
        <AInput v-model:value="model.description" :placeholder="$t('page.manage.role.form.roleDesc')" />
      </AFormItem>
    </AForm>
    <ASpace v-if="isEdit">
      <AButton @click="openMenuAuthModal">{{ $t('page.manage.role.menuAuth') }}</AButton>
      <AButton @click="openDataAuthModal">{{ $t('page.manage.role.dataAuth') }}</AButton>
      <AButton @click="openButtonAuthModal">{{ $t('page.manage.role.buttonAuth') }}</AButton>
      <MenuAuthModal v-model:visible="menuAuthVisible" :role-id="roleId" />
      <DataAuthModal v-model:visible="dataAuthVisible" :role-id="roleId" />
      <ButtonAuthModal v-model:visible="buttonAuthVisible" :role-id="roleId" />
    </ASpace>
    <template #footer>
      <div class="flex-y-center justify-end gap-12px">
        <AButton @click="closeDrawer">{{ $t('common.cancel') }}</AButton>
        <AButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</AButton>
      </div>
    </template>
  </ADrawer>
</template>

<style scoped></style>
