<script setup lang="ts">
import { computed } from 'vue';
import type { MenuInfo } from 'ant-design-vue/es/menu/src/interface';
import type { RouteKey } from '@elegant-router/types';
import { SimpleScrollbar } from '@sa/materials';
import { GLOBAL_HEADER_MENU_ID, GLOBAL_SIDER_MENU_ID } from '@/constants/app';
import { useAppStore } from '@/store/modules/app';
import { useThemeStore } from '@/store/modules/theme';
import { useRouteStore } from '@/store/modules/route';
import { useRouterPush } from '@/hooks/common/router';
import { useMenu, useMixMenuContext } from '../../../context';
import { convertMenusToAntdItems } from '@/store/modules/route/shared';
import { useRouter } from 'vue-router';

defineOptions({
  name: 'ReversedHorizontalMixMenu'
});

const appStore = useAppStore();
const themeStore = useThemeStore();
const routeStore = useRouteStore();
const { routerPushByKeyWithMetaQuery } = useRouterPush();
const {
  firstLevelMenus,
  childLevelMenus,
  activeFirstLevelMenuKey,
  setActiveFirstLevelMenuKey,
  isActiveFirstLevelMenuHasChildren
} = useMixMenuContext();
const { selectedKey } = useMenu();

// 转换菜单格式
const firstLevelMenuItems = computed(() => convertMenusToAntdItems(firstLevelMenus.value));
const childLevelMenuItems = computed(() => convertMenusToAntdItems(childLevelMenus.value));

function handleSelectMixMenu(menuInfo: MenuInfo) {
  const key = menuInfo.key as RouteKey;

  setActiveFirstLevelMenuKey(key);

  if (!isActiveFirstLevelMenuHasChildren.value) {
    // 从menuInfo对象中直接获取isFrame和originalUrl属性
    const isFrame = (menuInfo as any).isFrame === true;
    const originalUrl = (menuInfo as any).originalUrl;
    
    // 如果是外部链接菜单(isFrame=true 且有 originalUrl)
    if (isFrame && originalUrl) {
      console.log('🔗 点击外部链接菜单:', originalUrl);
      // 直接在新窗口中打开外部链接
      window.open(originalUrl, '_blank');
    } else {
      // 普通路由导航
      routerPushByKeyWithMetaQuery(key);
    }
  }
}

const openKeys = computed(() => {
  if (appStore.siderCollapse || !selectedKey.value) return [];

  if (!selectedKey.value) return [];

  return routeStore.getSelectedMenuKeyPath(selectedKey.value);
});

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
      :selected-keys="[activeFirstLevelMenuKey]"
      :items="firstLevelMenuItems"
      class="horizontal-menu size-full transition-300 border-0!"
      :class="{ 'bg-container!': themeStore.darkMode }"
      :style="{ lineHeight: themeStore.header.height + 'px' }"
      @click="handleSelectMixMenu"
    />
  </Teleport>
  <Teleport :to="`#${GLOBAL_SIDER_MENU_ID}`">
    <SimpleScrollbar>
      <AMenu
        mode="inline"
        :items="childLevelMenuItems"
        :selected-keys="[selectedKey]"
        :open-keys="openKeys"
        :inline-collapsed="appStore.siderCollapse"
        :inline-indent="18"
        class="size-full transition-300 border-0!"
        :class="{ 'bg-container!': themeStore.darkMode }"
        @click="handleClickMenu"
      />
    </SimpleScrollbar>
  </Teleport>
</template>

<style scoped></style>
