<template>
  <div id="app">
    <!-- 这里是应用的根组件 -->
  </div>
</template>

<script>
export default {
  name: 'App',
  onLaunch: function() {
    console.log('App Launch');
    // 应用启动时的初始化逻辑
    this.initApp();
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
    }
  }
}
</script>

<style>
/* 全局样式 */
@import url('./static/css/common.css');

/* 应用全局样式 */
page {
  background-color: #f8f8f8;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
}

/* 通用样式 */
.container {
  padding: 20rpx;
}

.card {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.text-primary {
  color: #007AFF;
}

.text-secondary {
  color: #666666;
}

.text-muted {
  color: #999999;
}

.btn {
  padding: 20rpx 40rpx;
  border-radius: 8rpx;
  text-align: center;
  font-size: 28rpx;
}

.btn-primary {
  background-color: #007AFF;
  color: #ffffff;
}

.btn-secondary {
  background-color: #f8f8f8;
  color: #333333;
}
</style> 