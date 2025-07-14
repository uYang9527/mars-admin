# 主题定制

主题定制是Mars Admin的核心特性之一，提供了灵活的主题配置和实时切换功能，支持亮色/暗色模式、自定义颜色和布局配置。

## 功能概述

Mars Admin的主题系统提供了以下功能：

- **主题模式**：亮色、暗色、跟随系统三种模式
- **主题颜色**：自定义主色调和功能色彩
- **布局模式**：垂直、水平、混合等多种布局
- **辅助功能**：灰度模式、色弱模式
- **实时预览**：配置修改实时生效
- **持久化存储**：主题设置本地存储

## 技术架构

### 1. 主题Store

Mars Admin使用Pinia管理主题状态：

```typescript
// src/store/modules/theme/index.ts
import { computed, effectScope, onScopeDispose, ref, toRefs, watch } from 'vue';
import { defineStore } from 'pinia';
import { useEventListener, usePreferredColorScheme } from '@vueuse/core';
import { getPaletteColorByNumber } from '@sa/color';
import { localStg } from '@/utils/storage';

export const useThemeStore = defineStore(SetupStoreId.Theme, () => {
  const scope = effectScope();
  const osTheme = usePreferredColorScheme();

  /** Theme settings */
  const settings: Ref<App.Theme.ThemeSetting> = ref(initThemeSettings());

  /** Theme colors */
  const themeColors = computed(() => {
    const { themeColor, otherColor, isInfoFollowPrimary } = settings.value;
    const colors: App.Theme.ThemeColor = {
      primary: themeColor,
      ...otherColor,
      info: isInfoFollowPrimary ? themeColor : otherColor.info
    };
    return colors;
  });

  /** Dark mode */
  const darkMode = computed(() => {
    if (settings.value.themeScheme === 'auto') {
      return osTheme.value === 'dark';
    }
    return settings.value.themeScheme === 'dark';
  });

  /** Antd theme */
  const antdTheme = computed(() => getAntdTheme(themeColors.value, darkMode.value));

  return {
    ...toRefs(settings.value),
    darkMode,
    themeColors,
    antdTheme,
    setThemeScheme,
    updateThemeColors,
    setThemeLayout
  };
});
```

### 2. 主题配置

```typescript
// src/theme/settings.ts
export const themeSettings: App.Theme.ThemeSetting = {
  themeScheme: 'light',
  grayscale: false,
  colourWeakness: false,
  recommendColor: false,
  themeColor: '#646cff',
  otherColor: {
    info: '#2080f0',
    success: '#52c41a',
    warning: '#faad14',
    error: '#f5222d'
  },
  isInfoFollowPrimary: true,
  layout: {
    mode: 'vertical',
    scrollMode: 'content',
    reverseHorizontalMix: false
  },
  page: {
    animate: true,
    animateMode: 'fade-slide'
  },
  header: {
    height: 56,
    breadcrumb: {
      visible: true,
      showIcon: true
    }
  },
  tab: {
    visible: true,
    cache: true,
    height: 44,
    mode: 'chrome'
  },
  fixedHeaderAndTab: true,
  sider: {
    inverted: false,
    width: 220,
    collapsedWidth: 64,
    mixWidth: 90,
    mixCollapsedWidth: 64,
    mixChildMenuWidth: 200
  },
  footer: {
    visible: true,
    fixed: false,
    height: 48,
    right: true
  }
};
```

## 主题配置面板

### 1. 主题抽屉组件

```vue
<!-- src/layouts/modules/theme-drawer/index.vue -->
<script setup lang="ts">
import { SimpleScrollbar } from '@sa/materials';
import { useAppStore } from '@/store/modules/app';
import { $t } from '@/locales';
import DarkMode from './modules/dark-mode.vue';
import LayoutMode from './modules/layout-mode.vue';
import ThemeColor from './modules/theme-color.vue';
import PageFun from './modules/page-fun.vue';
import ConfigOperation from './modules/config-operation.vue';

const appStore = useAppStore();
</script>

<template>
  <ADrawer
    :open="appStore.themeDrawerVisible"
    :title="$t('theme.themeDrawerTitle')"
    :closable="false"
    :body-style="{ padding: '0px' }"
    @close="appStore.closeThemeDrawer"
  >
    <template #extra>
      <ButtonIcon icon="ant-design:close-outlined" @click="appStore.closeThemeDrawer" />
    </template>
    <SimpleScrollbar>
      <div class="px-24px pb-24px pt-8px">
        <DarkMode />
        <LayoutMode />
        <ThemeColor />
        <PageFun />
      </div>
    </SimpleScrollbar>
    <template #footer>
      <ConfigOperation />
    </template>
  </ADrawer>
</template>
```

### 2. 暗色模式切换

```vue
<!-- src/layouts/modules/theme-drawer/modules/dark-mode.vue -->
<script setup lang="ts">
import { computed } from 'vue';
import { useThemeStore } from '@/store/modules/theme';
import { $t } from '@/locales';
import SettingItem from '../components/setting-item.vue';

const themeStore = useThemeStore();

const options = [
  {
    value: 'light',
    icon: 'material-symbols:sunny',
    label: $t('theme.themeSchema.light')
  },
  {
    value: 'dark', 
    icon: 'material-symbols:nightlight',
    label: $t('theme.themeSchema.dark')
  },
  {
    value: 'auto',
    icon: 'material-symbols:hdr-auto',
    label: $t('theme.themeSchema.auto')
  }
];

const showSiderInverted = computed(() => {
  const { layout } = themeStore;
  const { mode } = layout;
  
  return mode === 'vertical' || mode === 'vertical-mix';
});

function handleSegmentChange(value: UnionKey.ThemeScheme) {
  themeStore.setThemeScheme(value);
}

function handleGrayscaleChange(checked: boolean) {
  themeStore.setGrayscale(checked);
}
</script>

<template>
  <ADivider>{{ $t('theme.themeSchema.title') }}</ADivider>
  <div class="flex-col-stretch gap-16px">
    <div class="i-flex-center">
      <ASegmented 
        :value="themeStore.themeScheme" 
        :options="options" 
        class="bg-layout" 
        @change="handleSegmentChange"
      >
        <template #label="{ payload }">
          <div class="w-[70px] flex justify-center">
            <SvgIcon :icon="payload.icon" class="h-28px text-icon-small" />
          </div>
        </template>
      </ASegmented>
    </div>
    
    <Transition name="sider-inverted">
      <SettingItem v-if="showSiderInverted" :label="$t('theme.sider.inverted')">
        <ASwitch v-model:checked="themeStore.sider.inverted" />
      </SettingItem>
    </Transition>
    
    <SettingItem :label="$t('theme.grayscale')">
      <ASwitch :checked="themeStore.grayscale" @update:checked="handleGrayscaleChange" />
    </SettingItem>
    
    <SettingItem :label="$t('theme.colourWeakness')">
      <ASwitch :checked="themeStore.colourWeakness" @update:checked="themeStore.setColourWeakness" />
    </SettingItem>
  </div>
</template>
```

### 3. 主题颜色配置

```vue
<!-- src/layouts/modules/theme-drawer/modules/theme-color.vue -->
<script setup lang="ts">
import { ColorPicker } from '@sa/materials';
import { useThemeStore } from '@/store/modules/theme';
import { $t } from '@/locales';
import SettingItem from '../components/setting-item.vue';

const themeStore = useThemeStore();

function handleUpdateColor(color: string, key: App.Theme.ThemeColorKey) {
  themeStore.updateThemeColors(key, color);
}
</script>

<template>
  <ADivider>{{ $t('theme.themeColor.title') }}</ADivider>
  <div class="flex-col-stretch gap-12px">
    <ATooltip placement="topLeft">
      <SettingItem key="recommend-color" :label="$t('theme.recommendColor')">
        <ASwitch v-model:checked="themeStore.recommendColor" />
      </SettingItem>
      <template #title>
        <p>
          <span class="pr-12px">{{ $t('theme.recommendColorDesc') }}</span>
          <br />
          <AButton
            type="link"
            href="https://uicolors.app/create"
            target="_blank"
            rel="noopener noreferrer"
            class="text-gray"
          >
            https://uicolors.app/create
          </AButton>
        </p>
      </template>
    </ATooltip>
    
    <SettingItem 
      v-for="(_, key) in themeStore.themeColors" 
      :key="key" 
      :label="$t(`theme.themeColor.${key}`)"
    >
      <template v-if="key === 'info'" #suffix>
        <ACheckbox v-model:checked="themeStore.isInfoFollowPrimary">
          {{ $t('theme.themeColor.followPrimary') }}
        </ACheckbox>
      </template>
      <ColorPicker
        :color="themeStore.themeColors[key]"
        :disabled="key === 'info' && themeStore.isInfoFollowPrimary"
        @update:color="handleUpdateColor($event, key)"
      />
    </SettingItem>
  </div>
</template>
```

### 4. 布局模式配置

```vue
<!-- src/layouts/modules/theme-drawer/modules/layout-mode.vue -->
<script setup lang="ts">
import { computed } from 'vue';
import { useThemeStore } from '@/store/modules/theme';
import { $t } from '@/locales';
import SettingItem from '../components/setting-item.vue';

const themeStore = useThemeStore();

const layoutModes: { key: UnionKey.ThemeLayoutMode; label: string }[] = [
  {
    key: 'vertical',
    label: $t('theme.layoutMode.vertical')
  },
  {
    key: 'horizontal',
    label: $t('theme.layoutMode.horizontal')
  },
  {
    key: 'vertical-mix',
    label: $t('theme.layoutMode.verticalMix')
  },
  {
    key: 'horizontal-mix',
    label: $t('theme.layoutMode.horizontalMix')
  }
];

const showReverseHorizontalMix = computed(() => {
  return themeStore.layout.mode === 'horizontal-mix';
});

function handleLayoutModeChange(mode: UnionKey.ThemeLayoutMode) {
  themeStore.setThemeLayout(mode);
}
</script>

<template>
  <ADivider>{{ $t('theme.layoutMode.title') }}</ADivider>
  <div class="flex-col-stretch gap-16px">
    <div class="grid grid-cols-2 gap-16px">
      <div
        v-for="item in layoutModes"
        :key="item.key"
        :class="[
          'border-2px cursor-pointer rounded-6px hover:border-primary',
          themeStore.layout.mode === item.key ? 'border-primary' : 'border-transparent'
        ]"
        @click="handleLayoutModeChange(item.key)"
      >
        <div class="layout-card">
          <LayoutModeCard :mode="item.key" />
        </div>
        <p class="text-center text-sm mt-8px">{{ item.label }}</p>
      </div>
    </div>
    
    <Transition name="layout-reverse">
      <SettingItem v-if="showReverseHorizontalMix" :label="$t('theme.layoutMode.reverseHorizontalMix')">
        <ASwitch 
          :checked="themeStore.layout.reverseHorizontalMix" 
          @update:checked="themeStore.setLayoutReverseHorizontalMix" 
        />
      </SettingItem>
    </Transition>
  </div>
</template>
```

## 主题按钮

### 1. 主题配置按钮

```vue
<!-- src/layouts/modules/global-header/components/theme-button.vue -->
<script setup lang="ts">
import { useAppStore } from '@/store/modules/app';
import { $t } from '@/locales';

const appStore = useAppStore();
</script>

<template>
  <ButtonIcon
    icon="majesticons:color-swatch-line"
    :tooltip-content="$t('icon.themeConfig')"
    trigger-parent
    @click="appStore.openThemeDrawer"
  />
</template>
```

### 2. 在布局中使用

```vue
<!-- src/layouts/modules/global-header/index.vue -->
<template>
  <div class="h-full flex-y-center justify-end">
    <FullScreen v-if="!appStore.isMobile" :full="isFullscreen" @click="toggle" />
    <LangSwitch 
      :lang="appStore.locale" 
      :lang-options="appStore.localeOptions" 
      @change-lang="appStore.changeLocale" 
    />
    <ThemeButton />
    <UserAvatar />
  </div>
</template>
```

## 颜色系统

### 1. 颜色选择器

```vue
<!-- src/packages/materials/src/libs/color-picker/index.vue -->
<script setup lang="ts">
import { ref, watch, nextTick } from 'vue';
import ColorPicker from '@simonwep/pickr';

interface Props {
  color: string;
  disabled?: boolean;
  palettes?: string[];
}

interface Emits {
  (e: 'update:color', color: string): void;
}

const props = withDefaults(defineProps<Props>(), {
  disabled: false,
  palettes: () => [
    '#646cff',
    '#1890ff',
    '#52c41a',
    '#faad14',
    '#f5222d',
    '#722ed1',
    '#fa8c16',
    '#eb2f96',
    '#13c2c2',
    '#a0d911'
  ]
});

const emit = defineEmits<Emits>();

const domRef = ref<HTMLElement | null>(null);
const instance = ref<ColorPicker | null>(null);

function handleColorChange(hsva: ColorPicker.HSVaColor) {
  const color = hsva.toHEXA().toString();
  emit('update:color', color);
}

function initColorPicker() {
  if (!domRef.value) return;

  instance.value = ColorPicker.create({
    el: domRef.value,
    theme: 'nano',
    swatches: props.palettes,
    lockOpacity: true,
    default: props.color,
    disabled: props.disabled,
    components: {
      preview: true,
      opacity: false,
      hue: true,
      interaction: {
        hex: true,
        rgba: true,
        input: true
      }
    }
  });

  instance.value.on('change', handleColorChange);
}

function updateColor(color: string) {
  if (!instance.value) return;
  instance.value.setColor(color);
}

watch(() => props.color, updateColor);
watch(() => props.disabled, (disabled) => {
  if (instance.value) {
    if (disabled) {
      instance.value.disable();
    } else {
      instance.value.enable();
    }
  }
});

onMounted(() => {
  nextTick(initColorPicker);
});

onBeforeUnmount(() => {
  if (instance.value) {
    instance.value.destroyAndRemove();
  }
});
</script>

<template>
  <div ref="domRef" class="color-picker"></div>
</template>

<style scoped>
.color-picker {
  width: 32px;
  height: 32px;
}
</style>
```

### 2. 主题颜色生成

```typescript
// src/store/modules/theme/shared.ts
import { getColorPalette } from '@sa/color';

export function createThemePaletteColors(colors: App.Theme.ThemeColor, recommended = false) {
  const colorKeys = Object.keys(colors) as App.Theme.ThemeColorKey[];
  const colorPaletteVar = {} as App.Theme.ThemePaletteColor;

  colorKeys.forEach(key => {
    const colorMap = getColorPalette(colors[key], recommended);

    colorPaletteVar[key] = colorMap.get(500)!;

    colorMap.forEach((hex, number) => {
      colorPaletteVar[`${key}-${number}`] = hex;
    });
  });

  return colorPaletteVar;
}

export function createThemeToken(
  colors: App.Theme.ThemeColor,
  tokens?: App.Theme.ThemeSetting['tokens'],
  recommended = false
) {
  const paletteColors = createThemePaletteColors(colors, recommended);

  const { light, dark } = tokens || themeSettings.tokens;

  const themeTokens: App.Theme.ThemeTokenCSSVars = {
    colors: {
      ...paletteColors,
      nprogress: paletteColors.primary,
      ...light.colors
    },
    boxShadow: {
      ...light.boxShadow
    }
  };

  const darkThemeTokens: App.Theme.ThemeTokenCSSVars = {
    colors: {
      ...themeTokens.colors,
      ...dark.colors
    },
    boxShadow: {
      ...themeTokens.boxShadow,
      ...dark.boxShadow
    }
  };

  return {
    themeTokens,
    darkThemeTokens
  };
}
```

## CSS变量系统

### 1. CSS变量定义

```css
/* src/styles/css-vars/index.css */
:root {
  /* 主题颜色 */
  --primary-color: #646cff;
  --primary-color-50: #f0f0ff;
  --primary-color-100: #e0e0ff;
  --primary-color-200: #c7c7ff;
  --primary-color-300: #a5a5ff;
  --primary-color-400: #8080ff;
  --primary-color-500: #646cff;
  --primary-color-600: #5a5aff;
  --primary-color-700: #4d4dff;
  --primary-color-800: #4040ff;
  --primary-color-900: #3333ff;

  /* 功能颜色 */
  --info-color: #2080f0;
  --success-color: #52c41a;
  --warning-color: #faad14;
  --error-color: #f5222d;

  /* 布局颜色 */
  --layout-bg-color: #f6f9f8;
  --layout-header-bg-color: #ffffff;
  --layout-sider-bg-color: #001529;
  --layout-content-bg-color: #ffffff;

  /* 文本颜色 */
  --text-color-1: rgba(0, 0, 0, 0.88);
  --text-color-2: rgba(0, 0, 0, 0.65);
  --text-color-3: rgba(0, 0, 0, 0.45);
  --text-color-4: rgba(0, 0, 0, 0.25);

  /* 边框颜色 */
  --border-color: #d9d9d9;
  --border-color-2: #f0f0f0;

  /* 阴影 */
  --box-shadow-1: 0 1px 2px 0 rgba(0, 0, 0, 0.03), 0 1px 6px -1px rgba(0, 0, 0, 0.02), 0 2px 4px 0 rgba(0, 0, 0, 0.02);
  --box-shadow-2: 0 3px 6px -4px rgba(0, 0, 0, 0.12), 0 6px 16px 0 rgba(0, 0, 0, 0.08), 0 9px 28px 8px rgba(0, 0, 0, 0.05);
}

/* 暗色主题 */
[data-theme='dark'] {
  /* 布局颜色 */
  --layout-bg-color: #000000;
  --layout-header-bg-color: #141414;
  --layout-sider-bg-color: #001529;
  --layout-content-bg-color: #141414;

  /* 文本颜色 */
  --text-color-1: rgba(255, 255, 255, 0.88);
  --text-color-2: rgba(255, 255, 255, 0.65);
  --text-color-3: rgba(255, 255, 255, 0.45);
  --text-color-4: rgba(255, 255, 255, 0.25);

  /* 边框颜色 */
  --border-color: #424242;
  --border-color-2: #303030;
}
```

### 2. CSS变量应用

```typescript
// src/store/modules/theme/shared.ts
export function addThemeVarsToHtml(themeVars: App.Theme.ThemeTokenCSSVars) {
  const keys = Object.keys(themeVars) as (keyof App.Theme.ThemeTokenCSSVars)[];
  const style: string[] = [];

  keys.forEach(key => {
    const colors = themeVars[key];
    Object.entries(colors).forEach(([colorKey, color]) => {
      style.push(`--${key}-${colorKey}: ${color}`);
    });
  });

  const styleStr = style.join(';');

  document.documentElement.style.cssText += styleStr;
}

export function toggleHtmlClass(
  className: string | string[],
  add?: boolean
) {
  const classNames = Array.isArray(className) ? className : [className];

  classNames.forEach(name => {
    if (add === undefined) {
      document.documentElement.classList.toggle(name);
    } else if (add) {
      document.documentElement.classList.add(name);
    } else {
      document.documentElement.classList.remove(name);
    }
  });
}
```

## Ant Design主题

### 1. Ant Design主题配置

```typescript
// src/store/modules/theme/shared.ts
import type { ThemeConfig } from 'ant-design-vue';

export function getAntdTheme(colors: App.Theme.ThemeColor, darkMode: boolean): ThemeConfig {
  const { primary, info, success, warning, error } = colors;

  const theme: ThemeConfig = {
    token: {
      colorPrimary: primary,
      colorInfo: info,
      colorSuccess: success,
      colorWarning: warning,
      colorError: error,
      borderRadius: 6,
      wireframe: false
    },
    algorithm: darkMode ? [theme.darkAlgorithm] : undefined,
    components: {
      Menu: {
        colorBgContainer: 'transparent',
        colorBgElevated: 'transparent',
        colorBorder: 'transparent'
      },
      Button: {
        controlHeight: 32,
        paddingInline: 16
      },
      Input: {
        controlHeight: 32,
        paddingInline: 12
      },
      Select: {
        controlHeight: 32
      },
      DatePicker: {
        controlHeight: 32
      },
      Switch: {
        controlHeight: 22
      }
    }
  };

  return theme;
}
```

### 2. 在应用中使用

```vue
<!-- src/App.vue -->
<script setup lang="ts">
import { computed } from 'vue';
import { ConfigProvider } from 'ant-design-vue';
import { useThemeStore } from '@/store/modules/theme';
import { useAppStore } from '@/store/modules/app';
import { antdLocales } from '@/locales/antd';

const themeStore = useThemeStore();
const appStore = useAppStore();

const antdLocale = computed(() => {
  return antdLocales[appStore.locale];
});
</script>

<template>
  <ConfigProvider
    :locale="antdLocale"
    :theme="themeStore.antdTheme"
    :direction="appStore.direction"
  >
    <router-view />
  </ConfigProvider>
</template>
```

## 主题切换动画

### 1. 主题切换过渡

```css
/* src/styles/css-vars/transition.css */
.theme-transition {
  transition:
    background-color 0.3s ease,
    border-color 0.3s ease,
    color 0.3s ease,
    box-shadow 0.3s ease;
}

/* 布局过渡 */
.layout-transition {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 侧边栏过渡 */
.sider-transition {
  transition:
    width 0.3s cubic-bezier(0.4, 0, 0.2, 1),
    min-width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 主题抽屉过渡 */
.sider-inverted-enter-active,
.sider-inverted-leave-active {
  transition: all 0.3s ease;
}

.sider-inverted-enter-from,
.sider-inverted-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.layout-reverse-enter-active,
.layout-reverse-leave-active {
  transition: all 0.3s ease;
}

.layout-reverse-enter-from,
.layout-reverse-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
```

### 2. 页面切换动画

```typescript
// src/store/modules/theme/index.ts
export const pageAnimationModes: App.Theme.PageAnimateMode[] = [
  'fade',
  'fade-slide',
  'fade-bottom',
  'fade-scale',
  'zoom-fade',
  'zoom-out'
];

export function setPageAnimateMode(mode: App.Theme.PageAnimateMode) {
  settings.value.page.animateMode = mode;
}
```

```css
/* src/styles/css-vars/animation.css */
/* 页面切换动画 */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-slide-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.fade-slide-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.zoom-fade-enter-active,
.zoom-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.zoom-fade-enter-from {
  opacity: 0;
  transform: scale(0.95);
}

.zoom-fade-leave-to {
  opacity: 0;
  transform: scale(1.05);
}
```

## 响应式设计

### 1. 断点配置

```typescript
// src/utils/common.ts
export const breakpoints = {
  xs: 480,
  sm: 576,
  md: 768,
  lg: 992,
  xl: 1200,
  xxl: 1600
} as const;

export function getDeviceType(width: number): App.Device.Type {
  if (width < breakpoints.sm) return 'mobile';
  if (width < breakpoints.lg) return 'tablet';
  return 'desktop';
}
```

### 2. 响应式布局

```vue
<!-- src/layouts/base-layout/index.vue -->
<script setup lang="ts">
import { computed } from 'vue';
import { useThemeStore } from '@/store/modules/theme';
import { useAppStore } from '@/store/modules/app';

const themeStore = useThemeStore();
const appStore = useAppStore();

const layoutClass = computed(() => {
  const { mode } = themeStore.layout;
  const { isMobile } = appStore;

  return [
    'layout',
    `layout-${mode}`,
    {
      'layout-mobile': isMobile,
      'layout-fixed-header': themeStore.fixedHeaderAndTab,
      'layout-sider-inverted': themeStore.sider.inverted
    }
  ];
});

const layoutStyle = computed(() => {
  const { header, sider } = themeStore;

  return {
    '--header-height': `${header.height}px`,
    '--sider-width': `${sider.width}px`,
    '--sider-collapsed-width': `${sider.collapsedWidth}px`
  };
});
</script>

<template>
  <div :class="layoutClass" :style="layoutStyle">
    <LayoutHeader />
    <div class="layout-main">
      <LayoutSider />
      <LayoutContent />
    </div>
    <LayoutFooter />
  </div>
</template>
```

## 最佳实践

### 1. 主题设计原则

- **一致性**：保持整体视觉风格的一致性
- **可访问性**：确保颜色对比度符合无障碍标准
- **响应式**：适配不同设备和屏幕尺寸
- **性能**：优化主题切换的性能

### 2. 颜色选择建议

- **主色调**：选择符合品牌形象的主色调
- **功能色**：使用标准的功能色（成功、警告、错误）
- **中性色**：合理使用灰度色彩
- **对比度**：确保文本和背景有足够的对比度

### 3. 自定义主题

```typescript
// 自定义主题配置
export const customTheme: App.Theme.ThemeSetting = {
  themeScheme: 'light',
  themeColor: '#1890ff',
  otherColor: {
    info: '#1890ff',
    success: '#52c41a',
    warning: '#faad14',
    error: '#ff4d4f'
  },
  layout: {
    mode: 'vertical',
    scrollMode: 'content'
  },
  header: {
    height: 64,
    breadcrumb: {
      visible: true,
      showIcon: true
    }
  },
  sider: {
    inverted: true,
    width: 240,
    collapsedWidth: 80
  }
};
```

## 常见问题

### Q: 如何添加新的主题颜色？

**解决方案：**
```typescript
// 1. 扩展主题颜色类型
interface ThemeColor {
  primary: string;
  info: string;
  success: string;
  warning: string;
  error: string;
  purple: string; // 新增颜色
}

// 2. 更新默认配置
const defaultColors = {
  // ... 其他颜色
  purple: '#722ed1'
};

// 3. 在主题配置中添加
<SettingItem :label="$t('theme.themeColor.purple')">
  <ColorPicker
    :color="themeStore.themeColors.purple"
    @update:color="handleUpdateColor($event, 'purple')"
  />
</SettingItem>
```

### Q: 如何实现自定义布局模式？

**解决方案：**
```typescript
// 1. 定义新的布局模式
type ThemeLayoutMode = 'vertical' | 'horizontal' | 'vertical-mix' | 'horizontal-mix' | 'custom';

// 2. 创建布局组件
const CustomLayout = defineComponent({
  setup() {
    return () => (
      <div class="custom-layout">
        {/* 自定义布局结构 */}
      </div>
    );
  }
});

// 3. 在布局选择中添加
const layoutModes = [
  // ... 其他模式
  {
    key: 'custom',
    label: '自定义布局',
    component: CustomLayout
  }
];
```

### Q: 如何实现主题的导入导出？

**解决方案：**
```typescript
// 导出主题配置
export function exportThemeConfig() {
  const config = themeStore.settings;
  const blob = new Blob([JSON.stringify(config, null, 2)], {
    type: 'application/json'
  });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = 'theme-config.json';
  link.click();
  URL.revokeObjectURL(url);
}

// 导入主题配置
export function importThemeConfig(file: File) {
  const reader = new FileReader();
  reader.onload = (e) => {
    try {
      const config = JSON.parse(e.target?.result as string);
      themeStore.updateSettings(config);
      message.success('主题配置导入成功');
    } catch (error) {
      message.error('主题配置格式错误');
    }
  };
  reader.readAsText(file);
}
```
```
