<script setup lang="tsx">
import { computed, nextTick, ref, watch } from 'vue';
import { SimpleScrollbar } from '@sa/materials';
import { useAntdForm, useFormRules } from '@/hooks/common/form';
import { $t } from '@/locales';
import { enableStatusOptions, menuIconTypeOptions, menuTypeOptions } from '@/constants/business';
import SvgIcon from '@/components/custom/svg-icon.vue';
import { getLocalIcons } from '@/utils/icon';
import { fetchAddMenu, fetchGetAllRoles, fetchGetMenuTree, fetchUpdateMenu } from '@/service/api';
import {
  getLayoutAndPage,
  getPathParamFromRoutePath,
  getRoutePathByRouteName,
  getRoutePathWithParam,
  transformLayoutAndPageToComponent
} from './shared';

defineOptions({
  name: 'MenuOperateDrawer'
});

export type OperateType = AntDesign.TableOperateType | 'addChild';

interface Props {
  /** the type of operation */
  operateType: OperateType;
  /** the edit menu data or the parent menu data when adding a child menu */
  rowData?: Api.SystemManage.Menu | null;
  /** all pages */
  allPages?: string[];
}

const props = withDefaults(defineProps<Props>(), {
  allPages: () => []
});

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { formRef, validate, resetFields } = useAntdForm();
const { defaultRequiredRule } = useFormRules();

const title = computed(() => {
  const titles: Record<OperateType, string> = {
    add: $t('page.manage.menu.addMenu'),
    addChild: $t('page.manage.menu.addChildMenu'),
    edit: $t('page.manage.menu.editMenu')
  };
  return titles[props.operateType];
});

type Model = Pick<
  Api.SystemManage.Menu,
  | 'menuType'
  | 'menuName'
  | 'menuCode'
  | 'routeName'
  | 'routePath'
  | 'component'
  | 'redirect'
  | 'i18nKey'
  | 'icon'
  | 'iconType'
  | 'status'
  | 'parentId'
  | 'keepAlive'
  | 'constant'
  | 'href'
  | 'hideInMenu'
  | 'activeMenu'
  | 'multiTab'
  | 'fixedIndexInTab'
  | 'perms'
  | 'visible'
  | 'isFrame'
  | 'isCache'
  | 'menuTypeFlag'
> & {
  query: NonNullable<Api.SystemManage.Menu['query']>;
  buttons: NonNullable<Api.SystemManage.Menu['buttons']>;
  layout: string;
  page: string;
  pathParam: string;
  orderNum: number;
};

const model = ref(createDefaultModel());

function createDefaultModel(): Model {
  return {
    menuType: 'M',
    menuName: '',
    menuCode: '',
    routeName: '',
    routePath: '',
    pathParam: '',
    component: '',
    redirect: '',
    layout: '',
    page: '',
    i18nKey: null,
    icon: '',
    iconType: '1',
    parentId: 0,
    status: '1',
    keepAlive: false,
    constant: false,
    orderNum: 0,
    href: null,
    hideInMenu: false,
    activeMenu: null,
    multiTab: false,
    fixedIndexInTab: null,
    perms: '',
    visible: 1,
    isFrame: 1,
    isCache: 0,
    menuTypeFlag: '',
    query: [],
    buttons: []
  };
}

type RuleKey = Extract<keyof Model, 'menuName' | 'menuCode' | 'status' | 'routeName' | 'routePath'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  menuName: defaultRequiredRule,
  menuCode: defaultRequiredRule,
  status: defaultRequiredRule,
  routeName: defaultRequiredRule,
  routePath: defaultRequiredRule
};

const disabledMenuType = computed(() => props.operateType === 'edit');

const localIcons = getLocalIcons();
const localIconOptions = localIcons.map(item => ({
  label: () => (
    <div class="flex-y-center gap-16px">
      <SvgIcon localIcon={item} class="text-icon" />
      <span>{item}</span>
    </div>
  ),
  value: item
}));

const showLayout = computed(() => model.value.parentId === 0);

const showPage = computed(() => model.value.menuType === '2');

const pageOptions = computed(() => {
  const allPages = [...props.allPages];

  if (model.value.routeName && !allPages.includes(model.value.routeName)) {
    allPages.unshift(model.value.routeName);
  }

  const opts: CommonType.Option[] = allPages.map(page => ({
    label: page,
    value: page
  }));

  return opts;
});

const layoutOptions: CommonType.Option[] = [
  {
    label: 'base',
    value: 'base'
  },
  {
    label: 'blank',
    value: 'blank'
  }
];

/** the enabled role options */
const roleOptions = ref<CommonType.Option<string>[]>([]);

async function getRoleOptions() {
  const { error, data } = await fetchGetAllRoles();

  if (!error) {
    const options = data.map(item => ({
      label: item.roleName,
      value: item.roleCode
    }));

    roleOptions.value = [...options];
  }
}

/** 父级菜单树选项 */
const parentMenuOptions = ref<any[]>([]);

/** 转换菜单数据为树形选择器需要的格式 */
function transformMenuToTreeData(menus: Api.SystemManage.Menu[], excludeId?: number): any[] {
  return menus
    .filter(menu => {
      // 排除当前编辑的菜单，避免循环引用
      if (excludeId && menu.id === excludeId) return false;
      // 过滤掉按钮类型的菜单，按钮不能作为父级菜单
      if (menu.menuType === 'F') return false;
      return true;
    })
    .map(menu => ({
      id: menu.id,
      value: menu.id,
      label: menu.menuName,
      title: menu.menuName,
      children: menu.children ? transformMenuToTreeData(menu.children, excludeId) : undefined
    }));
}

/** 加载菜单树数据 */
async function loadMenuTree() {
  const menuTreeRes = await fetchGetMenuTree();

  if (menuTreeRes.data) {
    // 编辑时排除当前菜单，避免选择自己作为父级
    const excludeId = props.operateType === 'edit' ? props.rowData?.id : undefined;
    const transformedData = transformMenuToTreeData(menuTreeRes.data, excludeId);

    // 添加一个根节点选项
    parentMenuOptions.value = [
      {
        id: 0,
        value: 0,
        label: '根菜单',
        title: '根菜单',
        children: transformedData
      }
    ];
  }
}

/** - add a query input */
function addQuery(index: number) {
  model.value.query.splice(index + 1, 0, {
    key: '',
    value: ''
  });
}

/** - remove a query input */
function removeQuery(index: number) {
  model.value.query.splice(index, 1);
}

/** - add a button input */
function addButton(index: number) {
  model.value.buttons.splice(index + 1, 0, {
    code: '',
    desc: ''
  });
}

/** - remove a button input */
function removeButton(index: number) {
  model.value.buttons.splice(index, 1);
}

async function handleInitModel() {
  // 先重置为默认值
  model.value = createDefaultModel();

  if (!props.rowData) return;

  await nextTick();

  if (props.operateType === 'addChild') {
    const { id } = props.rowData;
    Object.assign(model.value, { parentId: id });
  }

  if (props.operateType === 'edit') {
    const { component, ...rest } = props.rowData;

    const { layout, page } = getLayoutAndPage(component);
    const { path, param } = getPathParamFromRoutePath(rest.routePath || '');

    // 确保parentId正确回显
    const editData = {
      ...rest,
      layout,
      page,
      routePath: path,
      pathParam: param,
      parentId: rest.parentId || 0, // 确保parentId有值，默认为0（根菜单）
      component // 保留原始component值，以备后续使用
    };

    Object.assign(model.value, editData);
  }

  if (!model.value.query) {
    model.value.query = [];
  }
  if (!model.value.buttons) {
    model.value.buttons = [];
  }
}

function closeDrawer() {
  visible.value = false;
}

function handleUpdateRoutePathByRouteName() {
  if (model.value.routeName) {
    model.value.routePath = getRoutePathByRouteName(model.value.routeName);
  } else {
    model.value.routePath = '';
  }
}

function handleUpdateI18nKeyByRouteName() {
  if (model.value.routeName) {
    model.value.i18nKey = `route.${model.value.routeName}` as App.I18n.I18nKey;
  } else {
    model.value.i18nKey = null;
  }
}

function getSubmitParams() {
  const { layout, page, pathParam, ...params } = model.value;

  const component = transformLayoutAndPageToComponent(layout, page);
  const routePath = getRoutePathWithParam(model.value.routePath, pathParam);

  params.component = component;
  params.routePath = routePath;

  return params;
}

async function handleSubmit() {
  try {
    await validate();

    const params = { ...model.value };

    // 处理字段映射，确保与后端一致
    params.orderNum = params.orderNum || 0;

    // 菜单类型已经是正确的值（'M', 'C', 'F'），不需要转换

    if (params.menuType === 'M' || params.menuType === 'C') {
      // 目录和菜单需要路由信息
      if (params.pathParam) {
        params.routePath = getRoutePathWithParam(params.routePath, params.pathParam);
      }

      // 处理component字段
      if (params.layout || params.page) {
        // 如果有layout或page信息，则重新组合component
        params.component = transformLayoutAndPageToComponent(params.layout, params.page);
      } else if (props.operateType === 'edit' && props.rowData?.component) {
        // 编辑模式下，如果没有layout/page信息，保留原始component值
        params.component = props.rowData.component;
      }
    } else if (params.menuType === 'F') {
      // 按钮类型不需要路由信息
      params.routeName = '';
      params.routePath = '';
      params.component = '';
      params.layout = '';
      params.page = '';
      params.redirect = '';
      params.isFrame = 1;
      params.isCache = 0;
    }

    // 删除表单中的临时字段，这些字段不需要提交到后端
    delete params.layout;
    delete params.page;
    delete params.pathParam;

    if (props.operateType === 'edit') {
      params.id = props.rowData?.id;
      const { error } = await fetchUpdateMenu(params);
      if (!error) {
        visible.value = false;
        emit('submitted');
      }
    } else {
      if (props.operateType === 'addChild') {
        params.parentId = props.rowData?.id;
      }
      const { error } = await fetchAddMenu(params);
      if (!error) {
        visible.value = false;
        emit('submitted');
      }
    }
  } catch (error) {
    console.error(error);
  }
}

watch(visible, async () => {
  if (visible.value) {
    await loadMenuTree();
    handleInitModel();
    resetFields();
    getRoleOptions();
  }
});

watch(
  () => model.value.routeName,
  () => {
    handleUpdateRoutePathByRouteName();
    handleUpdateI18nKeyByRouteName();
  }
);

// 根据菜单类型判断是否显示路由相关字段
const isDirectory = computed(() => model.value.menuType === 'M');
const isMenu = computed(() => model.value.menuType === 'C');
const isButton = computed(() => model.value.menuType === 'F');
</script>

<template>
  <ADrawer
    v-model:open="visible"
    :title="title"
    :width="800"
    :body-style="{ paddingRight: '20px' }"
    :footer-style="{ textAlign: 'right' }"
  >
    <div class="h-full overflow-auto">
      <SimpleScrollbar>
        <AForm ref="formRef" :model="model" :rules="rules" :label-col="{ lg: 8, xs: 4 }" label-wrap class="pr-20px">
          <ARow>
            <ACol :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.menuType')" name="menuType">
                <ARadioGroup v-model:value="model.menuType" :disabled="disabledMenuType">
                  <ARadio v-for="item in menuTypeOptions" :key="item.value" :value="item.value">
                    {{ $t(item.label) }}
                  </ARadio>
                </ARadioGroup>
              </AFormItem>
            </ACol>
            <ACol :lg="12" :xs="24">
              <AFormItem label="上级菜单" name="parentId">
                <ATreeSelect
                  v-model:value="model.parentId"
                  :tree-data="parentMenuOptions"
                  :field-names="{ label: 'label', value: 'value', children: 'children' }"
                  placeholder="请选择上级菜单"
                  tree-default-expand-all
                />
              </AFormItem>
            </ACol>
            <ACol :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.menuName')" name="menuName">
                <AInput v-model:value="model.menuName" :placeholder="$t('page.manage.menu.form.menuName')" />
              </AFormItem>
            </ACol>
            <ACol :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.menuCode')" name="menuCode">
                <AInput
                  v-model:value="model.menuCode"
                  :placeholder="
                    isDirectory
                      ? $t('page.manage.menu.form.menuCodeDirectory')
                      : isMenu
                        ? $t('page.manage.menu.form.menuCodeMenu')
                        : $t('page.manage.menu.form.menuCodeButton')
                  "
                />
              </AFormItem>
            </ACol>
            <ACol v-if="!isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.routeName')" name="routeName">
                <AInput v-model:value="model.routeName" :placeholder="$t('page.manage.menu.form.routeName')" />
              </AFormItem>
            </ACol>
            <ACol v-if="!isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.routePath')" name="routePath">
                <AInput v-model:value="model.routePath" disabled :placeholder="$t('page.manage.menu.form.routePath')" />
              </AFormItem>
            </ACol>
            <ACol v-if="isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.perms')" name="perms">
                <AInput v-model:value="model.perms" :placeholder="$t('page.manage.menu.form.perms')" />
              </AFormItem>
            </ACol>
            <ACol v-if="isMenu" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.pathParam')" name="pathParam">
                <AInput v-model:value="model.pathParam" :placeholder="$t('page.manage.menu.form.pathParam')" />
              </AFormItem>
            </ACol>
            <ACol v-if="isDirectory && showLayout" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.layout')" name="layout">
                <ASelect
                  v-model:value="model.layout"
                  :options="layoutOptions"
                  :placeholder="$t('page.manage.menu.form.layout')"
                />
              </AFormItem>
            </ACol>
            <ACol v-if="isMenu && showPage" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.page')" name="page">
                <ASelect
                  v-model:value="model.page"
                  :options="pageOptions"
                  :placeholder="$t('page.manage.menu.form.page')"
                />
              </AFormItem>
            </ACol>
            <ACol v-if="!isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.i18nKey')" name="i18nKey">
                <AInput v-model:value="model.i18nKey as string" :placeholder="$t('page.manage.menu.form.i18nKey')" />
              </AFormItem>
            </ACol>
            <ACol :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.order')" name="orderNum">
                <AInputNumber
                  v-model:value="model.orderNum as number"
                  class="w-full"
                  :placeholder="$t('page.manage.menu.form.order')"
                />
              </AFormItem>
            </ACol>
            <ACol v-if="!isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.iconTypeTitle')" name="iconType">
                <ARadioGroup v-model:value="model.iconType">
                  <ARadio v-for="item in menuIconTypeOptions" :key="item.value" :value="item.value">
                    {{ $t(item.label) }}
                  </ARadio>
                </ARadioGroup>
              </AFormItem>
            </ACol>

            <ACol v-if="!isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.icon')" name="icon">
                <template v-if="model.iconType === '1'">
                  <AInput v-model:value="model.icon" :placeholder="$t('page.manage.menu.form.icon')" class="flex-1">
                    <template #suffix>
                      <SvgIcon v-if="model.icon" :icon="model.icon" class="text-icon" />
                    </template>
                  </AInput>
                </template>
                <template v-if="model.iconType === '2'">
                  <ASelect
                    v-model:value="model.icon"
                    :placeholder="$t('page.manage.menu.form.localIcon')"
                    :options="localIconOptions"
                  />
                </template>
              </AFormItem>
            </ACol>
            <ACol :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.menuStatus')" name="status">
                <ARadioGroup v-model:value="model.status">
                  <ARadio v-for="item in enableStatusOptions" :key="item.value" :value="item.value">
                    {{ $t(item.label) }}
                  </ARadio>
                </ARadioGroup>
              </AFormItem>
            </ACol>
            <ACol v-if="isMenu" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.keepAlive')" name="keepAlive">
                <ARadioGroup v-model:value="model.keepAlive">
                  <ARadio :value="true">{{ $t('common.yesOrNo.yes') }}</ARadio>
                  <ARadio :value="false">{{ $t('common.yesOrNo.no') }}</ARadio>
                </ARadioGroup>
              </AFormItem>
            </ACol>
            <ACol v-if="!isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.constant')" name="constant">
                <ARadioGroup v-model:value="model.constant">
                  <ARadio value>
                    {{ $t('common.yesOrNo.yes') }}
                  </ARadio>
                  <ARadio :value="false">
                    {{ $t('common.yesOrNo.no') }}
                  </ARadio>
                </ARadioGroup>
              </AFormItem>
            </ACol>
            <ACol v-if="isMenu" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.href')" name="href">
                <AInput v-model:value="model.href as string" :placeholder="$t('page.manage.menu.form.href')" />
              </AFormItem>
            </ACol>
            <ACol v-if="!isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.hideInMenu')" name="hideInMenu">
                <ARadioGroup v-model:value="model.hideInMenu">
                  <ARadio :value="true">{{ $t('common.yesOrNo.yes') }}</ARadio>
                  <ARadio :value="false">{{ $t('common.yesOrNo.no') }}</ARadio>
                </ARadioGroup>
              </AFormItem>
            </ACol>
            <ACol v-if="model.hideInMenu && !isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.activeMenu')" name="activeMenu">
                <ASelect
                  v-model:value="model.activeMenu as string"
                  :options="pageOptions"
                  clearable
                  :placeholder="$t('page.manage.menu.form.activeMenu')"
                />
              </AFormItem>
            </ACol>
            <ACol v-if="isMenu" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.multiTab')" name="multiTab">
                <ARadioGroup v-model:value="model.multiTab">
                  <ARadio value :label="$t('common.yesOrNo.yes')" />
                  <ARadio :value="false" :label="$t('common.yesOrNo.no')" />
                </ARadioGroup>
              </AFormItem>
            </ACol>
            <ACol v-if="isMenu && model.multiTab" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.fixedIndexInTab')" name="fixedIndexInTab">
                <AInputNumber
                  v-model:value="model.fixedIndexInTab as number"
                  class="w-full"
                  clearable
                  :placeholder="$t('page.manage.menu.form.fixedIndexInTab')"
                />
              </AFormItem>
            </ACol>
            <ACol v-if="isMenu" :span="24">
              <AFormItem :label-col="{ span: 4 }" :label="$t('page.manage.menu.query')" name="query">
                <AButton v-if="model.query.length === 0" type="dashed" block @click="addQuery(-1)">
                  <template #icon>
                    <icon-carbon-add class="align-sub text-icon" />
                  </template>
                  <span class="ml-8px">{{ $t('common.add') }}</span>
                </AButton>
                <template v-else>
                  <div v-for="(item, index) in model.query" :key="index" class="flex gap-3">
                    <ACol :span="9">
                      <AFormItem :name="['query', index, 'key']">
                        <AInput
                          v-model:value="item.key"
                          :placeholder="$t('page.manage.menu.form.queryKey')"
                          class="flex-1"
                        />
                      </AFormItem>
                    </ACol>
                    <ACol :span="9">
                      <AFormItem :name="['query', index, 'value']">
                        <AInput
                          v-model:value="item.value"
                          :placeholder="$t('page.manage.menu.form.queryValue')"
                          class="flex-1"
                        />
                      </AFormItem>
                    </ACol>
                    <ACol :span="5">
                      <ASpace class="ml-12px">
                        <AButton size="middle" @click="addQuery(index)">
                          <template #icon>
                            <icon-ic:round-plus class="align-sub text-icon" />
                          </template>
                        </AButton>
                        <AButton size="middle" @click="removeQuery(index)">
                          <template #icon>
                            <icon-ic-round-remove class="align-sub text-icon" />
                          </template>
                        </AButton>
                      </ASpace>
                    </ACol>
                  </div>
                </template>
              </AFormItem>
            </ACol>
            <ACol :span="24">
              <AFormItem :label-col="{ span: 4 }" :label="$t('page.manage.menu.button')" name="buttons">
                <AButton v-if="model.buttons.length === 0" type="dashed" block @click="addButton(-1)">
                  <template #icon>
                    <icon-carbon-add class="align-sub text-icon" />
                  </template>
                  <span class="ml-8px">{{ $t('common.add') }}</span>
                </AButton>
                <template v-else>
                  <div v-for="(item, index) in model.buttons" :key="index" class="flex gap-3">
                    <ACol :span="9">
                      <AFormItem :name="['buttons', index, 'code']">
                        <AInput
                          v-model:value="item.code"
                          :placeholder="$t('page.manage.menu.form.buttonCode')"
                          class="flex-1"
                        ></AInput>
                      </AFormItem>
                    </ACol>
                    <ACol :span="9">
                      <AFormItem :name="['buttons', index, 'desc']">
                        <AInput
                          v-model:value="item.desc"
                          :placeholder="$t('page.manage.menu.form.buttonDesc')"
                          class="flex-1"
                        ></AInput>
                      </AFormItem>
                    </ACol>
                    <ACol :span="5">
                      <ASpace class="ml-12px">
                        <AButton size="middle" @click="addButton(index)">
                          <template #icon>
                            <icon-ic:round-plus class="align-sub text-icon" />
                          </template>
                        </AButton>
                        <AButton size="middle" @click="removeButton(index)">
                          <template #icon>
                            <icon-ic-round-remove class="align-sub text-icon" />
                          </template>
                        </AButton>
                      </ASpace>
                    </ACol>
                  </div>
                </template>
              </AFormItem>
            </ACol>
            <ACol v-if="!isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.redirect')" name="redirect">
                <AInput v-model:value="model.redirect" :placeholder="$t('page.manage.menu.form.redirect')" />
              </AFormItem>
            </ACol>
            <ACol v-if="!isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.visible')" name="visible">
                <ARadioGroup v-model:value="model.visible">
                  <ARadio :value="1">{{ $t('common.yesOrNo.yes') }}</ARadio>
                  <ARadio :value="0">{{ $t('common.yesOrNo.no') }}</ARadio>
                </ARadioGroup>
              </AFormItem>
            </ACol>
            <ACol v-if="!isButton" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.isFrame')" name="isFrame">
                <ARadioGroup v-model:value="model.isFrame">
                  <ARadio :value="0">{{ $t('common.yesOrNo.yes') }}</ARadio>
                  <ARadio :value="1">{{ $t('common.yesOrNo.no') }}</ARadio>
                </ARadioGroup>
              </AFormItem>
            </ACol>
            <ACol v-if="isMenu" :lg="12" :xs="24">
              <AFormItem :label="$t('page.manage.menu.isCache')" name="isCache">
                <ARadioGroup v-model:value="model.isCache">
                  <ARadio :value="0">{{ $t('common.yesOrNo.yes') }}</ARadio>
                  <ARadio :value="1">{{ $t('common.yesOrNo.no') }}</ARadio>
                </ARadioGroup>
              </AFormItem>
            </ACol>
          </ARow>
        </AForm>
      </SimpleScrollbar>
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
