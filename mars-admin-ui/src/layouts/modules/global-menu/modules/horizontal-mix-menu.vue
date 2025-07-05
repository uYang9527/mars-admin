<script setup lang="ts">
import { computed } from 'vue';
import type { MenuInfo } from 'ant-design-vue/es/menu/src/interface';
import type { RouteKey } from '@elegant-router/types';
import { GLOBAL_HEADER_MENU_ID, GLOBAL_SIDER_MENU_ID } from '@/constants/app';
import { useAppStore } from '@/store/modules/app';
import { useThemeStore } from '@/store/modules/theme';
import { useRouterPush } from '@/hooks/common/router';
import FirstLevelMenu from '../components/first-level-menu.vue';
import { useMenu, useMixMenuContext } from '../../../context';
import { convertMenusToAntdItems } from '@/store/modules/route/shared';
import { useRouter } from 'vue-router';
import { useRouteStore } from '@/store/modules/route';

defineOptions({
  name: 'HorizontalMixMenu'
});

const appStore = useAppStore();
const themeStore = useThemeStore();
const { routerPushByKeyWithMetaQuery } = useRouterPush();
const { allMenus, childLevelMenus, activeFirstLevelMenuKey, setActiveFirstLevelMenuKey } = useMixMenuContext();
const { selectedKey } = useMenu();
const routeStore = useRouteStore();

const inverted = computed(() => !themeStore.darkMode && themeStore.sider.inverted);

// 转换子菜单格式
const childMenuItems = computed(() => convertMenusToAntdItems(childLevelMenus.value));

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

function handleSelectMixMenu(menu: App.Global.Menu) {
  // 保存当前选中的一级菜单键
  setActiveFirstLevelMenuKey(menu.key);

  // 如果没有子菜单，则需要导航
  if (!menu.children?.length) {
    // 检查菜单键是否是外部链接格式（以ext:开头）
    if (menu.key.startsWith('ext:')) {
      // 提取URL部分（移除"ext:"前缀）
      const externalUrl = menu.key.substring(4);
      console.log('🔗 点击外部链接菜单:', externalUrl);
      // 直接在新窗口中打开外部链接
      window.open(externalUrl, '_blank');
    } else if (menu.isFrame && menu.originalUrl) {
      // 兼容原有的属性方式
      console.log('🔗 点击外部链接菜单 (原属性方式):', menu.originalUrl);
      window.open(menu.originalUrl, '_blank');
    } else {
      // 普通路由导航
      routerPushByKeyWithMetaQuery(menu.routeKey);
    }
  }
}
</script>

<template>
  <Teleport :to="`#${GLOBAL_HEADER_MENU_ID}`">
    <AMenu
      mode="horizontal"
      :selected-keys="[selectedKey]"
      :items="childMenuItems"
      class="horizontal-menu size-full transition-300 border-0!"
      :class="{ 'bg-container!': themeStore.darkMode }"
      :style="{ lineHeight: themeStore.header.height + 'px' }"
      @click="handleClickMenu"
    />
  </Teleport>
  <Teleport :to="`#${GLOBAL_SIDER_MENU_ID}`">
    <FirstLevelMenu
      :menus="allMenus"
      :active-menu-key="activeFirstLevelMenuKey"
      :inverted="inverted"
      :sider-collapse="appStore.siderCollapse"
      :dark-mode="themeStore.darkMode"
      :theme-color="themeStore.themeColor"
      @select="handleSelectMixMenu"
      @toggle-sider-collapse="appStore.toggleSiderCollapse"
    />
  </Teleport>
</template>

<style lang="scss" scoped></style>
