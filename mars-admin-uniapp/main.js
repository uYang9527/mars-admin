import App from './App'
import Vue from 'vue'
import uView from 'uview-ui'

Vue.config.productionTip = false

App.mpType = 'app'

// 使用uView UI
Vue.use(uView)

// 引入uView提供的对uni api的封装
uni.$u.setConfig({
  // 修改默认单位为rpx
  unit: 'rpx'
})

const app = new Vue({
  ...App
})
app.$mount()