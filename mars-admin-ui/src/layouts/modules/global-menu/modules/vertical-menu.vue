<script setup lang="ts">
import { computed } from 'vue';
import type { MenuInfo } from 'ant-design-vue/es/menu/src/interface';
import type { RouteKey } from '@elegant-router/types';
import { SimpleScrollbar } from '@sa/materials';
import { useAppStore } from '@/store/modules/app';
import { useThemeStore } from '@/store/modules/theme';
import { useRouteStore } from '@/store/modules/route';
import { useRouterPush } from '@/hooks/common/router';
import { GLOBAL_SIDER_MENU_ID } from '@/constants/app';
import { useMenu } from '../../../context';
import { convertMenusToAntdItems } from '@/store/modules/route/shared';

defineOptions({
  name: 'VerticalMenu'
});

const appStore = useAppStore();
const themeStore = useThemeStore();
const routeStore = useRouteStore();
const { routerPushByKeyWithMetaQuery } = useRouterPush();
const { selectedKey } = useMenu();

const darkTheme = computed(() => !themeStore.darkMode && themeStore.sider.inverted);

const menuTheme = computed(() => (darkTheme.value ? 'dark' : 'light'));

// 转换菜单格式
const menuItems = computed(() => {
  console.log('原始菜单数据:', routeStore.menus);
  const items = convertMenusToAntdItems(routeStore.menus);
  console.log('转换后的菜单数据:', items);
  return items;
});

const openKeys = computed(() => {
  if (appStore.siderCollapse || !selectedKey.value) return [];

  if (!selectedKey.value) return [];

  return routeStore.getSelectedMenuKeyPath(selectedKey.value);
});

function handleClickMenu(menuInfo: MenuInfo) {
  const key = menuInfo.key as RouteKey;

  routerPushByKeyWithMetaQuery(key);
}
</script>

<template>
  <Teleport :to="`#${GLOBAL_SIDER_MENU_ID}`">
    <SimpleScrollbar class="menu-wrapper" :class="{ 'select-menu': !darkTheme }">
      <AMenu
        mode="inline"
        :theme="menuTheme"
        :items="menuItems"
        :selected-keys="[selectedKey]"
        :open-keys="openKeys"
        :inline-collapsed="appStore.siderCollapse"
        :inline-indent="18"
        class="size-full transition-300 border-0!"
        :class="{ 'bg-container!': !darkTheme }"
        @click="handleClickMenu"
      />
    </SimpleScrollbar>
  </Teleport>
</template>

<style lang="scss" scoped></style>
