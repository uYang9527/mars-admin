<script setup lang="ts">
import type { MenuInfo } from 'ant-design-vue/es/menu/src/interface';
import type { RouteKey } from '@elegant-router/types';
import { computed } from 'vue';
import { GLOBAL_HEADER_MENU_ID } from '@/constants/app';
import { useThemeStore } from '@/store/modules/theme';
import { useRouteStore } from '@/store/modules/route';
import { useRouterPush } from '@/hooks/common/router';
import { useMenu } from '../../../context';
import { convertMenusToAntdItems } from '@/store/modules/route/shared';

defineOptions({
  name: 'HorizontalMenu'
});

const themeStore = useThemeStore();
const routeStore = useRouteStore();
const { routerPushByKeyWithMetaQuery } = useRouterPush();
const { selectedKey } = useMenu();

// 转换菜单格式
const menuItems = computed(() => convertMenusToAntdItems(routeStore.menus));

function handleClickMenu(menuInfo: MenuInfo) {
  const key = menuInfo.key as RouteKey;

  routerPushByKeyWithMetaQuery(key);
}
</script>

<template>
  <Teleport :to="`#${GLOBAL_HEADER_MENU_ID}`">
    <AMenu
      mode="horizontal"
      :selected-keys="[selectedKey]"
      :items="menuItems"
      class="horizontal-menu size-full transition-300 border-0!"
      :class="{ 'bg-container!': themeStore.darkMode }"
      :style="{ lineHeight: themeStore.header.height + 'px' }"
      @click="handleClickMenu"
    />
  </Teleport>
</template>

<style lang="scss" scoped></style>
