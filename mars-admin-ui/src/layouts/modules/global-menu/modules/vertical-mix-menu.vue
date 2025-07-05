<script setup lang="ts">
import { computed } from 'vue';
import type { MenuInfo } from 'ant-design-vue/es/menu/src/interface';
import type { RouteKey } from '@elegant-router/types';
import { SimpleScrollbar } from '@sa/materials';
import { useBoolean } from '@sa/hooks';
import { useAppStore } from '@/store/modules/app';
import { useThemeStore } from '@/store/modules/theme';
import { useRouteStore } from '@/store/modules/route';
import { useRouterPush } from '@/hooks/common/router';
import { $t } from '@/locales';
import { GLOBAL_SIDER_MENU_ID } from '@/constants/app';
import { useMenu, useMixMenuContext } from '../../../context';
import FirstLevelMenu from '../components/first-level-menu.vue';
import GlobalLogo from '../../global-logo/index.vue';
import { convertMenusToAntdItems } from '@/store/modules/route/shared';
import { useRouter } from 'vue-router';

defineOptions({
  name: 'VerticalMixMenu'
});

const appStore = useAppStore();
const themeStore = useThemeStore();
const routeStore = useRouteStore();
const { routerPushByKeyWithMetaQuery } = useRouterPush();
const { bool: drawerVisible, setBool: setDrawerVisible } = useBoolean();
const {
  allMenus,
  childLevelMenus,
  activeFirstLevelMenuKey,
  setActiveFirstLevelMenuKey,
  getActiveFirstLevelMenuKey
  //
} = useMixMenuContext();
const { selectedKey } = useMenu();

const inverted = computed(() => !themeStore.darkMode && themeStore.sider.inverted);

const menuTheme = computed(() => (inverted.value ? 'dark' : 'light'));

const hasChildMenus = computed(() => childLevelMenus.value.length > 0);

const showDrawer = computed(() => hasChildMenus.value && (drawerVisible.value || appStore.mixSiderFixed));

function handleSelectMixMenu(menu: App.Global.Menu) {
  setActiveFirstLevelMenuKey(menu.key);

  if (menu.children?.length) {
    setDrawerVisible(true);
  } else {
    // 如果是外部链接菜单(isFrame=true 且有 originalUrl)
    if (menu.isFrame && menu.originalUrl) {
      console.log('🔗 点击外部链接菜单:', menu.originalUrl);
      // 直接在新窗口中打开外部链接
      window.open(menu.originalUrl, '_blank');
    } else {
      // 普通路由导航
      routerPushByKeyWithMetaQuery(menu.routeKey);
    }
  }
}

function handleResetActiveMenu() {
  setDrawerVisible(false);

  if (!appStore.mixSiderFixed) {
    getActiveFirstLevelMenuKey();
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
  <Teleport :to="`#${GLOBAL_SIDER_MENU_ID}`">
    <div class="h-full flex" @mouseleave="handleResetActiveMenu">
      <FirstLevelMenu
        :menus="allMenus"
        :active-menu-key="activeFirstLevelMenuKey"
        :inverted="inverted"
        :sider-collapse="appStore.siderCollapse"
        :dark-mode="themeStore.darkMode"
        :theme-color="themeStore.themeColor"
        @select="handleSelectMixMenu"
        @toggle-sider-collapse="appStore.toggleSiderCollapse"
      >
        <GlobalLogo :show-title="false" :style="{ height: themeStore.header.height + 'px' }" />
      </FirstLevelMenu>
      <div
        class="relative h-full transition-width-300"
        :style="{ width: appStore.mixSiderFixed && hasChildMenus ? themeStore.sider.mixChildMenuWidth + 'px' : '0px' }"
      >
        <DarkModeContainer
          class="absolute-lt h-full flex-col-stretch nowrap-hidden shadow-sm transition-all-300"
          :inverted="inverted"
          :style="{ width: showDrawer ? themeStore.sider.mixChildMenuWidth + 'px' : '0px' }"
        >
          <header class="flex-y-center justify-between px-12px" :style="{ height: themeStore.header.height + 'px' }">
            <h2 class="text-16px text-primary font-bold">{{ $t('system.title') }}</h2>
            <PinToggler
              :pin="appStore.mixSiderFixed"
              :class="{ 'text-white:88 !hover:text-white': inverted }"
              @click="appStore.toggleMixSiderFixed"
            />
          </header>
          <SimpleScrollbar class="menu-wrapper" :class="{ 'select-menu': !inverted }">
            <AMenu
              mode="inline"
              :theme="menuTheme"
              :items="convertMenusToAntdItems(childLevelMenus)"
              :selected-keys="[selectedKey]"
              :open-keys="openKeys"
              class="size-full transition-300 border-0!"
              :class="{ 'bg-container!': !inverted }"
              @click="handleClickMenu"
            />
          </SimpleScrollbar>
        </DarkModeContainer>
      </div>
    </div>
  </Teleport>
</template>

<style lang="scss" scoped></style>
