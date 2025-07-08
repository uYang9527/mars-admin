<template>
  <view class="container">
    <!-- 用户信息头部 -->
    <view class="user-header">
      <view class="user-avatar" @click="changeAvatar">
        <image :src="userInfo.avatar" class="avatar-image"></image>
        <view class="avatar-edit">📷</view>
      </view>
      <view class="user-info">
        <text class="user-name">{{ userInfo.name }}</text>
        <text class="user-position">{{ userInfo.position }}</text>
        <text class="user-dept">{{ userInfo.department }}</text>
      </view>
      <view class="user-qr" @click="showQRCode">
        <text class="qr-icon">📱</text>
      </view>
    </view>

    <!-- 统计信息 -->
    <view class="stats-section">
      <view class="stats-item" v-for="(item, index) in statsData" :key="index">
        <text class="stats-number">{{ item.value }}</text>
        <text class="stats-label">{{ item.label }}</text>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-group" v-for="(group, groupIndex) in menuGroups" :key="groupIndex">
        <view class="menu-item" v-for="(item, index) in group" :key="index" @click="handleMenuClick(item)">
          <view class="menu-icon">
            <text class="icon-text">{{ item.icon }}</text>
          </view>
          <text class="menu-text">{{ item.name }}</text>
          <view class="menu-badge" v-if="item.badge">{{ item.badge }}</view>
          <view class="menu-arrow">></view>
        </view>
      </view>
    </view>

    <!-- 设置选项 -->
    <view class="settings-section">
      <view class="settings-item" v-for="(item, index) in settingsMenu" :key="index" @click="handleSettingsClick(item)">
        <view class="settings-icon">
          <text class="icon-text">{{ item.icon }}</text>
        </view>
        <text class="settings-text">{{ item.name }}</text>
        <view class="settings-arrow">></view>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-section">
      <view class="logout-btn" @click="logout">
        <text class="logout-text">退出登录</text>
      </view>
    </view>

    <!-- 版本信息 -->
    <view class="version-info">
      <text class="version-text">版本号：{{ version }}</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      version: '1.0.0',
      userInfo: {
        name: '张三',
        position: '高级工程师',
        department: '技术部',
        avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&h=100&fit=crop&crop=face&auto=format&q=80',
        phone: '138****8888',
        email: 'zhangsan@company.com'
      },
      statsData: [
        {
          label: '考勤天数',
          value: '22'
        },
        {
          label: '请假次数',
          value: '2'
        },
        {
          label: '加班时长',
          value: '48h'
        },
        {
          label: '项目参与',
          value: '5'
        }
      ],
      menuGroups: [
        [
          {
            name: '个人信息',
            icon: '👤',
            action: 'profile'
          },
          {
            name: '我的考勤',
            icon: '📅',
            action: 'attendance'
          },
          {
            name: '我的申请',
            icon: '📝',
            action: 'applications',
            badge: '3'
          }
        ],
        [
          {
            name: '我的任务',
            icon: '📋',
            action: 'tasks',
            badge: '5'
          },
          {
            name: '我的项目',
            icon: '💼',
            action: 'projects'
          },
          {
            name: '工作汇报',
            icon: '📊',
            action: 'reports'
          }
        ],
        [
          {
            name: '收藏夹',
            icon: '⭐',
            action: 'favorites'
          },
          {
            name: '下载中心',
            icon: '📥',
            action: 'downloads'
          },
          {
            name: '帮助中心',
            icon: '❓',
            action: 'help'
          }
        ]
      ],
      settingsMenu: [
        {
          name: '账户设置',
          icon: '⚙️',
          action: 'account-settings'
        },
        {
          name: '消息通知',
          icon: '🔔',
          action: 'notifications'
        },
        {
          name: '隐私设置',
          icon: '🔒',
          action: 'privacy'
        },
        {
          name: '关于我们',
          icon: 'ℹ️',
          action: 'about'
        }
      ]
    }
  },
  onLoad() {
    this.loadUserInfo();
  },
  onShow() {
    this.refreshUserData();
  },
  methods: {
    loadUserInfo() {
      // 模拟加载用户信息
      console.log('加载用户信息');
    },
    refreshUserData() {
      // 刷新用户数据
      console.log('刷新用户数据');
    },
    changeAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0];
          // 这里可以上传头像到服务器
          this.userInfo.avatar = tempFilePath;
          uni.showToast({
            title: '头像更新成功',
            icon: 'success'
          });
        },
        fail: (err) => {
          console.log('选择图片失败:', err);
        }
      });
    },
    showQRCode() {
      uni.showModal({
        title: '个人二维码',
        content: '这里可以显示个人二维码，方便他人添加联系方式',
        showCancel: false
      });
    },
    handleMenuClick(item) {
      console.log('点击菜单项:', item.name);
      
      switch (item.action) {
        case 'profile':
          uni.navigateTo({
            url: '/pages/profile/user-info'
          });
          break;
        case 'attendance':
          uni.navigateTo({
            url: '/pages/profile/attendance'
          });
          break;
        case 'applications':
          uni.navigateTo({
            url: '/pages/profile/applications'
          });
          break;
        case 'tasks':
          uni.navigateTo({
            url: '/pages/profile/tasks'
          });
          break;
        case 'projects':
          uni.navigateTo({
            url: '/pages/profile/projects'
          });
          break;
        case 'reports':
          uni.navigateTo({
            url: '/pages/profile/reports'
          });
          break;
        case 'favorites':
          uni.navigateTo({
            url: '/pages/profile/favorites'
          });
          break;
        case 'downloads':
          uni.navigateTo({
            url: '/pages/profile/downloads'
          });
          break;
        case 'help':
          uni.navigateTo({
            url: '/pages/profile/help'
          });
          break;
        default:
          uni.showToast({
            title: `点击了${item.name}`,
            icon: 'none'
          });
      }
    },
    handleSettingsClick(item) {
      console.log('点击设置项:', item.name);
      
      switch (item.action) {
        case 'account-settings':
          uni.navigateTo({
            url: '/pages/settings/account'
          });
          break;
        case 'notifications':
          uni.navigateTo({
            url: '/pages/settings/notifications'
          });
          break;
        case 'privacy':
          uni.navigateTo({
            url: '/pages/settings/privacy'
          });
          break;
        case 'about':
          uni.navigateTo({
            url: '/pages/settings/about'
          });
          break;
        default:
          uni.showToast({
            title: `点击了${item.name}`,
            icon: 'none'
          });
      }
    },
    logout() {
      uni.showModal({
        title: '确认退出',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            // 清除本地存储的用户信息
            uni.removeStorageSync('userInfo');
            uni.removeStorageSync('token');
            
            // 跳转到登录页面
            uni.reLaunch({
              url: '/pages/login/login'
            });
            
            uni.showToast({
              title: '已退出登录',
              icon: 'success'
            });
          }
        }
      });
    }
  }
}
</script>

<style scoped>
.container {
  padding: 0;
  background: transparent;
  min-height: 100vh;
}

/* 用户信息头部样式 - 精致版 */
.user-header {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 48rpx 32rpx;
  color: #ffffff;
  margin-bottom: 24rpx;
  position: relative;
  overflow: hidden;
}

.user-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 20% 50%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
              radial-gradient(circle at 80% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  opacity: 0.8;
}

.user-avatar {
  position: relative;
  width: 140rpx;
  height: 140rpx;
  border-radius: 50%;
  margin-right: 32rpx;
  overflow: hidden;
  border: 4rpx solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.2);
  z-index: 1;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.user-avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 12rpx 32rpx rgba(0, 0, 0, 0.3);
}

.avatar-image {
  width: 100%;
  height: 100%;
}

.avatar-edit {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 40rpx;
  height: 40rpx;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20rpx;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 36rpx;
  font-weight: bold;
  display: block;
  margin-bottom: 10rpx;
}

.user-position {
  font-size: 28rpx;
  display: block;
  margin-bottom: 8rpx;
  opacity: 0.9;
}

.user-dept {
  font-size: 24rpx;
  opacity: 0.8;
}

.user-qr {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
}

/* 统计信息样式 - 精致版 */
.stats-section {
  display: flex;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10rpx);
  margin: 0 20rpx 24rpx;
  border-radius: 20rpx;
  padding: 32rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
  border: 1rpx solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.stats-section:hover {
  transform: translateY(-2rpx);
  box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
}

.stats-item {
  flex: 1;
  text-align: center;
  padding: 16rpx;
  border-radius: 16rpx;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.stats-item:hover {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  transform: translateY(-2rpx);
}

.stats-number {
  font-size: 40rpx;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  display: block;
  margin-bottom: 12rpx;
}

.stats-label {
  font-size: 24rpx;
  color: #2c3e50;
  font-weight: 500;
}

/* 功能菜单样式 - 精致版 */
.menu-section {
  margin: 0 20rpx;
}

.menu-group {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10rpx);
  border-radius: 20rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
  border: 1rpx solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.menu-group:hover {
  transform: translateY(-2rpx);
  box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 32rpx;
  border-bottom: 1rpx solid rgba(240, 240, 240, 0.5);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:hover {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
  transform: translateX(4rpx);
}

.menu-icon {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.icon-text {
  font-size: 32rpx;
}

.menu-text {
  flex: 1;
  font-size: 28rpx;
  color: #333333;
}

.menu-badge {
  background-color: #ff4757;
  color: #ffffff;
  font-size: 20rpx;
  padding: 4rpx 8rpx;
  border-radius: 20rpx;
  margin-right: 20rpx;
  min-width: 32rpx;
  text-align: center;
}

.menu-arrow {
  font-size: 28rpx;
  color: #cccccc;
}

/* 设置选项样式 */
.settings-section {
  background-color: #ffffff;
  border-radius: 16rpx;
  margin: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.settings-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.settings-item:last-child {
  border-bottom: none;
}

.settings-icon {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.settings-text {
  flex: 1;
  font-size: 28rpx;
  color: #333333;
}

.settings-arrow {
  font-size: 28rpx;
  color: #cccccc;
}

/* 退出登录样式 */
.logout-section {
  margin: 20rpx;
  padding: 20rpx 0;
}

.logout-btn {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 30rpx;
  text-align: center;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.logout-text {
  font-size: 28rpx;
  color: #ff4757;
  font-weight: bold;
}

/* 版本信息样式 */
.version-info {
  text-align: center;
  padding: 20rpx;
  margin-bottom: 40rpx;
}

.version-text {
  font-size: 24rpx;
  color: #999999;
}
</style> 