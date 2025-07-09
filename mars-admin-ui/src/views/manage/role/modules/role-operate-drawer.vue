<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { useBoolean } from '@sa/hooks';
import { useAntdForm, useFormRules } from '@/hooks/common/form';
import { $t } from '@/locales';
import { enableStatusOptions } from '@/constants/business';
import { fetchAddRole, fetchUpdateRole, fetchGetRoleById } from '@/service/api';
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

// 数据范围选项
const dataScopeOptions = [
  { label: '全部数据权限', value: 1 },
  { label: '自定义数据权限', value: 2 },
  { label: '本部门数据权限', value: 3 },
  { label: '本部门及以下数据权限', value: 4 },
  { label: '仅本人数据权限', value: 5 }
];

// 修改Model类型定义，添加isSystem
type Model = Pick<
  Api.SystemManage.Role,
  | 'roleName'
  | 'roleCode'
  | 'roleKey'
  | 'roleSort'
  | 'dataScope'
  | 'menuCheckStrictly'
  | 'deptCheckStrictly'
  | 'description'
  | 'status'
  | 'remark'
  | 'isSystem'
>;

const model = ref(createDefaultModel());

function createDefaultModel(): Model {
  return {
    roleName: '',
    roleCode: '',
    roleKey: '',
    roleSort: 0,
    dataScope: 1,
    menuCheckStrictly: 1,
    deptCheckStrictly: 1,
    description: '',
    status: 1,
    remark: '',
    isSystem: 0 // 默认为非系统角色
  };
}

// 将布尔值转换为整数
function boolToInt(value: boolean): number {
  return value ? 1 : 0;
}

// 将整数转换为布尔值
function intToBool(value: number): boolean {
  return value === 1;
}

type RuleKey = 'roleName' | 'roleCode' | 'roleKey' | 'roleSort' | 'status';

const rules: Record<RuleKey, App.Global.FormRule> = {
  roleName: defaultRequiredRule,
  roleCode: defaultRequiredRule,
  roleKey: defaultRequiredRule,
  roleSort: defaultRequiredRule,
  status: defaultRequiredRule
};

const roleId = computed(() => props.rowData?.id || -1);

const isEdit = computed(() => props.operateType === 'edit');
const isSystem = computed(() => props.rowData?.isSystem === 1);

// 修改handleInitModel函数，确保处理isSystem字段
async function handleInitModel() {
  Object.assign(model.value, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData?.id) {
    // 获取角色详情
    const { data: roleDetail } = await fetchGetRoleById(props.rowData.id);
    if (roleDetail) {
      // 确保数据类型正确
      Object.assign(model.value, {
        roleName: roleDetail.roleName || '',
        roleCode: roleDetail.roleCode || '',
        roleKey: roleDetail.roleKey || '',
        roleSort: Number(roleDetail.roleSort || 0),
        dataScope: Number(roleDetail.dataScope || 1),
        menuCheckStrictly: Number(roleDetail.menuCheckStrictly || 1),
        deptCheckStrictly: Number(roleDetail.deptCheckStrictly || 1),
        description: roleDetail.description || '',
        status: Number(roleDetail.status ?? 1),
        remark: roleDetail.remark || '',
        isSystem: Number(roleDetail.isSystem ?? 0) // 添加isSystem字段处理
      });
    }
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();

  // 确保提交的数据类型正确
  const submitData = {
    ...model.value,
    menuCheckStrictly:
      typeof model.value.menuCheckStrictly === 'boolean'
        ? boolToInt(model.value.menuCheckStrictly)
        : model.value.menuCheckStrictly,
    deptCheckStrictly:
      typeof model.value.deptCheckStrictly === 'boolean'
        ? boolToInt(model.value.deptCheckStrictly)
        : model.value.deptCheckStrictly
  };

  let request;
  if (props.operateType === 'add') {
    request = fetchAddRole(submitData);
  } else {
    request = fetchUpdateRole({
      ...submitData,
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
    :width="600"
    :body-style="{ paddingRight: '20px' }"
    :footer-style="{ textAlign: 'right' }"
  >
    <AForm
      ref="formRef"
      layout="horizontal"
      :model="model"
      :rules="rules"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 19 }"
      class="role-form"
    >
      <AFormItem :label="$t('page.manage.role.roleName')" name="roleName">
        <AInput v-model:value="model.roleName" :placeholder="$t('page.manage.role.form.roleName')" />
      </AFormItem>

      <AFormItem :label="$t('page.manage.role.roleCode')" name="roleCode">
        <AInput
          v-model:value="model.roleCode"
          :placeholder="$t('page.manage.role.form.roleCode')"
          :disabled="isEdit && isSystem"
        />
      </AFormItem>

      <ARow :gutter="16">
        <ACol :span="12">
          <AFormItem 
            :label="$t('page.manage.role.roleKey')" 
            name="roleKey"
            :label-col="{ span: 10 }"
            :wrapper-col="{ span: 14 }"
          >
            <AInput
              v-model:value="model.roleKey"
              :placeholder="$t('page.manage.role.form.roleKey')"
              :disabled="isEdit && isSystem"
            />
          </AFormItem>
        </ACol>
        <ACol :span="12">
          <AFormItem 
            label="显示顺序" 
            name="roleSort"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 16 }"
          >
            <AInputNumber
              v-model:value="model.roleSort"
              :min="0"
              :max="9999"
              style="width: 100%"
              placeholder="请输入显示顺序"
            />
          </AFormItem>
        </ACol>
      </ARow>

      <AFormItem :label="$t('page.manage.role.roleStatus')" name="status">
        <ARadioGroup v-model:value="model.status">
          <ARadio v-for="item in enableStatusOptions" :key="item.value" :value="Number(item.value)">
            {{ $t(item.label) }}
          </ARadio>
        </ARadioGroup>
      </AFormItem>

      <AFormItem label="数据范围" name="dataScope">
        <ASelect v-model:value="model.dataScope" style="width: 100%">
          <ASelectOption v-for="item in dataScopeOptions" :key="item.value" :value="item.value">
            {{ item.label }}
          </ASelectOption>
        </ASelect>
      </AFormItem>

      <ARow :gutter="16">
        <ACol :span="12">
          <AFormItem 
            label="菜单树关联显示" 
            name="menuCheckStrictly"
            :label-col="{ span: 12 }"
            :wrapper-col="{ span: 12 }"
          >
            <ASwitch
              :checked="model.menuCheckStrictly === 1"
              checked-children="是"
              un-checked-children="否"
              @change="value => (model.menuCheckStrictly = value ? 1 : 0)"
            />
          </AFormItem>
        </ACol>
        <ACol :span="12">
          <AFormItem 
            label="部门树关联显示" 
            name="deptCheckStrictly"
            :label-col="{ span: 12 }"
            :wrapper-col="{ span: 12 }"
          >
            <ASwitch
              :checked="model.deptCheckStrictly === 1"
              checked-children="是"
              un-checked-children="否"
              @change="value => (model.deptCheckStrictly = value ? 1 : 0)"
            />
          </AFormItem>
        </ACol>
      </ARow>

      <AFormItem :label="$t('page.manage.role.roleDesc')" name="description">
        <ATextarea v-model:value="model.description" :placeholder="$t('page.manage.role.form.roleDesc')" :rows="2" />
      </AFormItem>

      <AFormItem label="备注" name="remark">
        <ATextarea v-model:value="model.remark" placeholder="请输入备注" :rows="2" />
      </AFormItem>
    </AForm>

    <div v-if="isEdit" class="mt-4">
      <ADivider orientation="left">权限分配</ADivider>
      <ASpace>
        <AButton type="primary" @click="openMenuAuthModal">{{ $t('page.manage.role.menuAuth') }}</AButton>
        <AButton type="primary" @click="openDataAuthModal">{{ $t('page.manage.role.dataAuth') }}</AButton>
        <AButton type="primary" @click="openButtonAuthModal">{{ $t('page.manage.role.buttonAuth') }}</AButton>
      </ASpace>
      <MenuAuthModal v-model:visible="menuAuthVisible" :role-id="roleId" />
      <DataAuthModal v-model:visible="dataAuthVisible" :role-id="roleId" />
      <ButtonAuthModal v-model:visible="buttonAuthVisible" :role-id="roleId" />
    </div>

    <template #footer>
      <div class="flex-y-center justify-end gap-12px">
        <AButton @click="closeDrawer">{{ $t('common.cancel') }}</AButton>
        <AButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</AButton>
      </div>
    </template>
  </ADrawer>
</template>

<style scoped></style>
