<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <n-loading-bar-provider>
      <n-message-provider>
        <n-dialog-provider>
          <GlobalApiProvider />
          <router-view />
        </n-dialog-provider>
      </n-message-provider>
    </n-loading-bar-provider>
  </n-config-provider>
</template>

<script setup lang="ts">
import type { GlobalThemeOverrides } from 'naive-ui'
import { useMessage, useDialog, useLoadingBar } from 'naive-ui'
import { defineComponent, h } from 'vue'

// 注入全局API
const GlobalApiProvider = defineComponent({
  setup() {
    window.$message = useMessage()
    window.$dialog = useDialog()
    window.$loadingBar = useLoadingBar()
    return () => null
  }
})

const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#111827',
    primaryColorHover: '#000000',
    primaryColorPressed: '#1F2937',
    primaryColorSuppl: '#374151',
    // 统一 warning 颜色为主题色
    warningColor: '#111827',
    warningColorHover: '#000000',
    warningColorPressed: '#1F2937',
    warningColorSuppl: '#374151',
    // 统一 info 颜色为主题色
    infoColor: '#111827',
    infoColorHover: '#000000',
    infoColorPressed: '#1F2937',
    infoColorSuppl: '#374151',
    // 统一 success 颜色为主题色
    successColor: '#111827',
    successColorHover: '#000000',
    successColorPressed: '#1F2937',
    successColorSuppl: '#374151',
    textColorBase: '#1F2937',
    textColor1: '#1F2937',
    textColor2: '#6B7280',
    textColor3: '#9CA3AF',
    borderColor: '#E5E7EB',
    dividerColor: '#E5E7EB',
    inputColor: '#F9FAFB',
    tableColor: '#FFFFFF',
    cardColor: '#FFFFFF',
    modalColor: '#FFFFFF',
    bodyColor: '#F3F4F6',
    hoverColor: '#F3F4F6',
    borderRadius: '8px',
    borderRadiusSmall: '6px'
  },
  Button: {
    borderRadiusMedium: '8px',
    borderRadiusSmall: '6px',
    heightMedium: '36px'
  },
  Card: {
    borderRadius: '12px',
    paddingMedium: '20px',
    titleFontSizeMedium: '16px'
  },
  DataTable: {
    borderRadius: '12px',
    thColor: '#F9FAFB',
    thTextColor: '#6B7280',
    thFontWeight: '600',
    tdColor: '#FFFFFF'
  },
  Input: {
    borderRadius: '8px',
    heightMedium: '36px'
  },
  Form: {
    labelFontSizeTopMedium: '14px',
    labelTextColor: '#374151'
  },
  Menu: {
    itemHeight: '44px',
    borderRadius: '8px',
    itemColorActive: '#F3F4F6',
    itemColorActiveHover: '#E5E7EB',
    itemTextColorActive: '#111827',
    itemTextColorActiveHover: '#111827',
    itemIconColorActive: '#111827',
    itemIconColorActiveHover: '#111827'
  },
  Tag: {
    borderRadius: '6px'
  },
  Dialog: {
    borderRadius: '12px'
  }
}
</script>
