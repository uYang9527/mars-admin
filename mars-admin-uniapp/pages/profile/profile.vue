<template>
  <view class="page-container">
    <!-- uView官方导航栏 -->
    <u-navbar
      title="个人中心"
      bgColor="transparent"
      :fixed="true"
      :placeholder="true"
      :safeAreaInsetTop="true"
      :titleStyle="{ color: '#ffffff' }"
    >
      <template #right>
        <view class="navbar-right">
          <u-icon name="setting" size="28" color="#ffffff" @click="handleSetting"></u-icon>
        </view>
      </template>
    </u-navbar>

    <!-- 顶部用户信息卡片 -->
    <view class="profile-header">
      <view class="header-bg"></view>

      <view class="user-card">
        <view class="user-main">
          <view class="avatar-section" @click="changeAvatar">
            <view class="avatar-wrapper">
              <u-avatar :src="userInfo.avatar" size="120"></u-avatar>
              <view class="camera-icon">
                <u-icon name="camera-fill" color="#ffffff" size="20"></u-icon>
              </view>
            </view>
          </view>

          <view class="user-info">
            <text class="user-name">{{ userInfo.name }}</text>
            <text class="user-title">{{ userInfo.position }}</text>
            <view class="user-dept">
              <u-icon name="home" size="16" color="rgba(255,255,255,0.8)"></u-icon>
              <text class="dept-text">{{ userInfo.department }}</text>
            </view>
          </view>
        </view>

        <view class="action-buttons">
          <view class="action-btn" @click="handleContact('phone')">
            <u-icon name="phone-fill" color="#ffffff" size="20"></u-icon>
          </view>
          <view class="action-btn" @click="handleContact('email')">
            <u-icon name="email-fill" color="#ffffff" size="20"></u-icon>
          </view>
        </view>
      </view>
    </view>

    <!-- 统计信息 -->
    <view class="stats-container">
      <view class="stats-grid">
        <view v-for="(item, index) in profileStats" :key="index" class="stats-item">
          <view class="stats-content">
            <text class="stats-number">{{ item.value }}</text>
            <text class="stats-label">{{ item.label }}</text>
          </view>
          <view class="stats-icon" :style="{ backgroundColor: `${item.color}15` }">
            <u-icon :name="item.icon" size="20" :color="item.color"></u-icon>
          </view>
        </view>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-group" v-for="(group, groupIndex) in menuGroups" :key="groupIndex">
        <view class="group-header">
          <text class="group-title">{{ group.title }}</text>
        </view>
        <view class="menu-container">
          <view
            v-for="(item, index) in group.items"
            :key="index"
            class="menu-item"
            @click="handleMenuClick(item)"
          >
            <view class="menu-left">
              <view class="menu-icon" :style="{ backgroundColor: `${item.color}15` }">
                <u-icon :name="item.icon" size="20" :color="item.color"></u-icon>
              </view>
              <view class="menu-content">
                <text class="menu-title">{{ item.name }}</text>
                <text v-if="item.desc" class="menu-desc">{{ item.desc }}</text>
              </view>
            </view>
            <view class="menu-right">
              <u-badge v-if="item.badge" :count="item.badge" :offset="[0, 0]"></u-badge>
              <u-icon name="arrow-right" size="14" color="#ccc"></u-icon>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-section">
      <view class="logout-btn" @click="logout">
        <u-icon name="logout" size="20" color="#ff4757"></u-icon>
        <text class="logout-text">退出登录</text>
      </view>
    </view>

    <!-- 版本信息 -->
    <view class="version-info">
      <text class="version-text">Mars Admin v1.0.0</text>
    </view>

    <u-safe-bottom></u-safe-bottom>
  </view>
</template>

<script>
import api from '@/api'
import pageMixin from '@/mixins/common/page'
import { logout as logoutUser, getCurrentUser } from '@/utils/permission'
import { showLoading, hideLoading, showError, showSuccess } from '@/utils/common'

export default {
  mixins: [pageMixin],
  data() {
    return {
      navbarConfig: {
        bgColor: 'transparent'
      },
      userInfo: {},
      profileStats: [],
      menuGroups: []
    }
  },
  methods: {
    // 重写页面混入的loadPageData方法
    async loadPageData() {
      try {
        this.showLoading('加载用户信息...');

        // 获取当前用户信息
        const currentUser = getCurrentUser();
        if (currentUser) {
          this.userInfo = currentUser;
        }

        // 加载mock数据
        const { profileStats, profileMenus } = await import('@/mock/index.js');
        this.profileStats = profileStats;
        this.menuGroups = [
          {
            title: '个人中心',
            items: profileMenus.personal
          },
          {
            title: '工作管理',
            items: profileMenus.work
          },
          {
            title: '资源中心',
            items: profileMenus.resource
          }
        ];

      } catch (error) {
        console.error('加载用户信息失败:', error);
        this.showError('加载用户信息失败');
      } finally {
        this.hideLoading();
      }
    },
    handleSetting() {
      uni.showToast({
        title: '进入设置页面',
        icon: 'none'
      });
    },
    changeAvatar() {
      uni.showActionSheet({
        itemList: ['拍照', '从相册选择'],
        success: (res) => {
          if (res.tapIndex === 0) {
            // 拍照
            this.chooseImage('camera');
          } else if (res.tapIndex === 1) {
            // 从相册选择
            this.chooseImage('album');
          }
        }
      });
    },
    chooseImage(sourceType) {
      uni.chooseImage({
        count: 1,
        sourceType: [sourceType],
        success: (res) => {
          const tempFilePaths = res.tempFilePaths;
          // 在实际项目中，这里应该调用上传接口
          this.userInfo.avatar = tempFilePaths[0];
          uni.showToast({
            title: '头像已更新',
            icon: 'success'
          });
        }
      });
    },
    handleContact(type) {
      if (type === 'phone') {
        uni.makePhoneCall({
          phoneNumber: this.userInfo.phone,
          fail: () => {
            uni.showToast({
              title: '拨打电话失败',
              icon: 'none'
            });
          }
        });
      } else if (type === 'email') {
        // 实际项目中可以跳转到邮件发送页面
        uni.showToast({
          title: '发送邮件：' + this.userInfo.email,
          icon: 'none'
        });
      }
    },
    handleMenuClick(item) {
      uni.showToast({
        title: `点击了${item.name}`,
        icon: 'none'
      });
      
      // 根据action跳转到不同页面
      switch (item.action) {
        case 'profile':
          // 个人信息
          break;
        case 'attendance':
          // 考勤
          break;
        // 其他功能...
      }
    },
    // 退出登录
    async logout() {
      const confirmed = await this.confirm('确定要退出登录吗？', '提示');
      if (confirmed) {
        try {
          this.showLoading('退出中...');

          // 调用退出登录API
          // await api.user.logout();

          // 清除登录状态
          logoutUser();

          this.showSuccess('退出成功');
        } catch (error) {
          console.error('退出登录失败:', error);
          this.showError('退出失败');
        } finally {
          this.hideLoading();
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
}

.profile-header {
  position: relative;
  height: 400rpx;
  overflow: hidden;
}

.header-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  z-index: 0;

  &::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 40rpx;
    background: #f8f9fa;
    border-radius: 40rpx 40rpx 0 0;
  }
}

.navbar-right {
  display: flex;
  align-items: center;
  padding: 10rpx;
  position: relative;
  z-index: 2;
}

.user-card {
  position: relative;
  z-index: 1;
  margin: 40rpx 30rpx 0;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20rpx);
  border-radius: 24rpx;
  padding: 40rpx 30rpx;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.user-main {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}

.avatar-section {
  margin-right: 30rpx;
}

.avatar-wrapper {
  position: relative;

  .camera-icon {
    position: absolute;
    bottom: 0;
    right: 0;
    width: 40rpx;
    height: 40rpx;
    background: rgba(0, 0, 0, 0.6);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 2px solid #ffffff;
  }
}

.user-info {
  flex: 1;
  color: #ffffff;
}

.user-name {
  font-size: 40rpx;
  font-weight: 700;
  margin-bottom: 8rpx;
  display: block;
}

.user-title {
  font-size: 28rpx;
  opacity: 0.9;
  margin-bottom: 12rpx;
  display: block;
}

.user-dept {
  display: flex;
  align-items: center;

  .dept-text {
    font-size: 24rpx;
    opacity: 0.8;
    margin-left: 8rpx;
  }
}

.action-buttons {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  width: 64rpx;
  height: 64rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.9);
    background: rgba(255, 255, 255, 0.3);
  }
}

// 统计信息样式
.stats-container {
  margin: 30rpx;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.stats-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx 24rpx;
  background: #ffffff;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 20rpx rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.98);
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
  }
}

.stats-content {
  flex: 1;
}

.stats-number {
  font-size: 40rpx;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 8rpx;
  display: block;
}

.stats-label {
  font-size: 26rpx;
  color: #666;
  font-weight: 500;
}

.stats-icon {
  width: 60rpx;
  height: 60rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 20rpx;
}

// 菜单样式
.menu-section {
  padding: 0 30rpx;
}

.menu-group {
  margin-bottom: 30rpx;
}

.group-header {
  margin-bottom: 16rpx;
}

.group-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #1a1a1a;
}

.menu-container {
  background: #ffffff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 20rpx rgba(0, 0, 0, 0.06);
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 30rpx;
  border-bottom: 1px solid #f5f5f5;
  transition: all 0.3s ease;

  &:last-child {
    border-bottom: none;
  }

  &:active {
    background: #f8f9fa;
    transform: scale(0.98);
  }
}

.menu-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.menu-icon {
  width: 56rpx;
  height: 56rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.menu-content {
  flex: 1;
}

.menu-title {
  font-size: 32rpx;
  font-weight: 500;
  color: #1a1a1a;
  margin-bottom: 4rpx;
  display: block;
}

.menu-desc {
  font-size: 24rpx;
  color: #999;
  display: block;
}

.menu-right {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

// 退出登录样式
.logout-section {
  margin: 40rpx 30rpx;
}

.logout-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24rpx;
  background: #ffffff;
  border-radius: 16rpx;
  border: 2px solid #ff4757;
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.98);
    background: #ff4757;

    .logout-text {
      color: #ffffff;
    }
  }
}

.logout-text {
  font-size: 32rpx;
  font-weight: 600;
  color: #ff4757;
  margin-left: 12rpx;
  transition: all 0.3s ease;
}

.stats-section {
  margin: 30rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.stats-box {
  padding: 20rpx 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stats-number {
  font-size: 36rpx;
  font-weight: bold;
  color: #2979ff;
  margin-bottom: 10rpx;
}

.stats-label {
  font-size: 24rpx;
  color: #909399;
}

.menu-section {
  padding: 0 30rpx;
}

.group-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #303133;
  margin: 30rpx 0 20rpx 20rpx;
}

.menu-list {
  margin-bottom: 30rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.logout-section {
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.version-info {
  text-align: center;
  padding-bottom: 30rpx;
}

.version-text {
  font-size: 24rpx;
  color: #909399;
}
</style> 