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
import { useRouter } from 'vue-router';

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
  const key = menuInfo.key as string;
  
  // 检查菜单键是否是外部链接格式（以ext:开头）
  if (key.startsWith('ext:')) {
    // 提取URL部分（移除"ext:"前缀）
    const externalUrl = key.substring(4);
    console.log('🔗 点击外部链接菜单:', externalUrl);
    // 直接在新窗口中打开外部链接
    window.open(externalUrl, '_blank');
  } else {
    // 普通路由导航
    routerPushByKeyWithMetaQuery(key as RouteKey);
  }
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
