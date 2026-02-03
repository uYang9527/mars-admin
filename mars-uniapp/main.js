import App from './App'

// 全局配置
const globalData = {
  // 后端API地址 - 开发环境使用本地地址，生产环境需要改成实际域名
  baseUrl: 'http://localhost:8080',
  // 用户信息
  userInfo: null,
  // 会员信息
  memberInfo: null
}

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
Vue.config.productionTip = false
App.mpType = 'app'
// 注入全局数据
App.globalData = globalData
const app = new Vue({
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  // 注入全局数据
  app.config.globalProperties.$globalData = globalData
  // 挂载到 getApp() 可访问
  if (!getApp()) {
    // Vue3 环境需要特殊处理
    App.globalData = globalData
  }
  return {
    app
  }
}
// #endif
