<template>
  <div id="app">
    <!-- 这里是应用的根组件 -->
    <u-toast ref="uToast"></u-toast>
    <u-modal ref="uModal"></u-modal>
    <u-no-network></u-no-network>
  </div>
</template>

<script>
export default {
  name: 'App',
  onLaunch: function() {
    console.log('App Launch');
    // 应用启动时的初始化逻辑
    this.initApp();
    // 设置uView主题色
    uni.$u.setConfig({
      // 修改主题色
      theme: {
        primary: '#2979ff',
        success: '#19be6b',
        warning: '#ff9900',
        error: '#fa3534',
        info: '#909399'
      }
    })
  },
  onShow: function() {
    console.log('App Show');
  },
  onHide: function() {
    console.log('App Hide');
  },
  methods: {
    initApp() {
      // 初始化应用
      // 可以在这里进行用户登录状态检查、全局配置等
      console.log('应用初始化完成');
    },
    showNotification(message, type = 'primary') {
      this.$refs.uToast.show({
        type: type,
        message: message,
        duration: 2000,
        position: 'top'
      })
    }
  }
}
</script>

<style>
/* uView基础样式已在uni.scss中全局引入，此处无需重复引入 */

/* 全局样式 */
@import url('./static/css/common.css');

/* 应用全局样式 */
page {
  background-color: #f5f6fa;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
}

/* 全局状态栏适配 */
.page-container {
  min-height: 100vh;
  background-color: #f5f6fa;
}

/* 安全区域适配 */
.safe-area-top {
  padding-top: env(safe-area-inset-top);
}

.safe-area-bottom {
  padding-bottom: env(safe-area-inset-bottom);
}

/* 通用样式 */
.container {
  padding: 30rpx;
}

.card {
  background-color: #ffffff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.text-primary {
  color: #2979ff;
}

.text-success {
  color: #19be6b;
}

.text-warning {
  color: #ff9900;
}

.text-danger {
  color: #fa3534;
}

.text-info {
  color: #909399;
}

.text-secondary {
  color: #606266;
}

.text-muted {
  color: #909399;
}

.text-center {
  text-align: center;
}

.text-right {
  text-align: right;
}

.text-bold {
  font-weight: bold;
}

.btn {
  padding: 20rpx 40rpx;
  border-radius: 8rpx;
  text-align: center;
  font-size: 28rpx;
}

.btn-primary {
  background-color: #2979ff;
  color: #ffffff;
}

.btn-success {
  background-color: #19be6b;
  color: #ffffff;
}

.btn-secondary {
  background-color: #f8f8f8;
  color: #333333;
}

.flex-row {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.flex-column {
  display: flex;
  flex-direction: column;
}

.flex-between {
  justify-content: space-between;
}

.flex-center {
  justify-content: center;
}

.flex-1 {
  flex: 1;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #303133;
  margin: 30rpx 0 20rpx 0;
}

/* 页面过渡动画 */
.fade-enter-active, .fade-leave-active {
  transition: opacity .3s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style> 