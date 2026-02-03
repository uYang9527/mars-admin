<template>
  <view class="page">
    <!-- 整体滚动区域 -->
    <scroll-view class="scroll-container" scroll-y>
      <!-- 头部渐变区域 -->
      <view class="header">
        <view :style="{ height: statusBarHeight + 'px' }"></view>
        <view class="header-nav">
          <text class="header-title">我的</text>
        </view>

        <!-- 用户卡片 -->
        <view class="user-card" @click="goProfile">
          <view class="user-avatar-wrap">
            <image class="user-avatar" :src="userInfo.avatar" mode="aspectFill"></image>
            <view class="vip-badge">
              <text class="fas fa-crown"></text>
            </view>
          </view>
          <view class="user-info">
            <text class="user-name">{{ userInfo.name }}</text>
            <view class="vip-tag">黄金会员</view>
          </view>
          <text class="fas fa-qrcode"></text>
        </view>
      </view>

      <!-- 内容区域 -->
      <view class="content">
        <!-- 数据统计 -->
    

        <!-- 订单 -->
        <view class="order-card">
          <view class="card-header">
            <text class="card-title">我的订单</text>
            <view class="card-more" @click="goOrders">
              <text>查看全部</text>
              <text class="fas fa-chevron-right"></text>
            </view>
          </view>
          <view class="order-list">
            <view class="order-item" v-for="order in orders" :key="order.id" @click="handleOrder(order)">
              <view class="order-icon" :class="{ active: order.badge }">
                <text :class="order.icon"></text>
                <view class="order-badge" v-if="order.badge">{{ order.badge }}</view>
              </view>
              <text class="order-label">{{ order.label }}</text>
            </view>
          </view>
        </view>

        <!-- 服务菜单 -->
        <view class="menu-card">
          <view class="menu-item" v-for="(menu, index) in menus1" :key="index" @click="handleMenu(menu)">
            <view class="menu-icon" :style="{ background: menu.bg }">
              <text :class="menu.icon" :style="{ color: menu.color }"></text>
            </view>
            <text class="menu-label">{{ menu.label }}</text>
            <text class="fas fa-chevron-right"></text>
          </view>
        </view>

        <view class="menu-card">
          <view class="menu-item" v-for="(menu, index) in menus2" :key="index" @click="handleMenu(menu)">
            <view class="menu-icon" :style="{ background: menu.bg }">
              <text :class="menu.icon" :style="{ color: menu.color }"></text>
            </view>
            <text class="menu-label">{{ menu.label }}</text>
            <text class="fas fa-chevron-right"></text>
          </view>
        </view>

        <view class="safe-bottom"></view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { getMemberInfo, getOrderCount } from '@/utils/api.js'

export default {
  data() {
    return {
      statusBarHeight: 0,
      isLoggedIn: false,
      userInfo: {
        name: '请登录',
        avatar: '/static/default-avatar.png'
      },
      stats: [
        {
          icon: 'fas fa-ticket-alt',
          label: '优惠券',
          value: '0',
          color: '#059669',
          bg: 'linear-gradient(135deg, rgba(5, 150, 105, 0.1), rgba(5, 150, 105, 0.05))'
        },
        {
          icon: 'fas fa-star',
          label: '积分',
          value: '0',
          color: '#f59e0b',
          bg: 'linear-gradient(135deg, rgba(245, 158, 11, 0.1), rgba(245, 158, 11, 0.05))'
        },
        {
          icon: 'fas fa-coins',
          label: '余额',
          value: '¥0.00',
          color: '#ef4444',
          bg: 'linear-gradient(135deg, rgba(239, 68, 68, 0.1), rgba(239, 68, 68, 0.05))'
        },
        {
          icon: 'fas fa-wallet',
          label: '钱包',
          value: '',
          color: '#6366f1',
          bg: 'linear-gradient(135deg, rgba(99, 102, 241, 0.1), rgba(99, 102, 241, 0.05))'
        }
      ],
      orders: [
        {id: 0, icon: 'far fa-credit-card', label: '待付款', badge: 0},
        {id: 1, icon: 'fas fa-box', label: '待发货', badge: 0},
        {id: 2, icon: 'fas fa-shipping-fast', label: '待收货', badge: 0},
        {id: 3, icon: 'far fa-comment-dots', label: '已完成', badge: 0}
      ],
      menus1: [
        {
          id: 1,
          icon: 'fas fa-map-marker-alt',
          label: '收货地址',
          color: '#059669',
          bg: 'linear-gradient(135deg, rgba(5, 150, 105, 0.1), rgba(5, 150, 105, 0.05))',
          url: '/pages/address/list'
        },
        {
          id: 2,
          icon: 'fas fa-heart',
          label: '我的收藏',
          color: '#ef4444',
          bg: 'linear-gradient(135deg, rgba(239, 68, 68, 0.1), rgba(239, 68, 68, 0.05))',
          url: '/pages/favorite/index'
        }
      ],
      menus2: [
        {
          id: 3,
          icon: 'fas fa-headset',
          label: '客服中心',
          color: '#3b82f6',
          bg: 'linear-gradient(135deg, rgba(59, 130, 246, 0.1), rgba(59, 130, 246, 0.05))'
        },
        {
          id: 4,
          icon: 'fas fa-cog',
          label: '设置',
          color: '#6b7280',
          bg: 'linear-gradient(135deg, rgba(107, 114, 128, 0.1), rgba(107, 114, 128, 0.05))',
          url: '/pages/settings/index'
        }
      ]
    }
  },
  onLoad() {
    const systemInfo = uni.getSystemInfoSync()
    this.statusBarHeight = systemInfo.statusBarHeight || 0
  },
  onShow() {
    this.checkLoginAndLoad()
  },
  methods: {
    // 检查登录状态并加载数据
    async checkLoginAndLoad() {
      const memberInfo = uni.getStorageSync('memberInfo')
      this.isLoggedIn = !!(memberInfo && memberInfo.memberId)
      
      if (this.isLoggedIn) {
        this.userInfo = {
          name: memberInfo.nickname || '用户',
          avatar: memberInfo.avatar || '/static/default-avatar.png'
        }
        // 更新积分
        this.stats[1].value = memberInfo.points || '0'
        
        // 加载最新会员信息
        this.loadMemberInfo()
        // 加载订单数量
        this.loadOrderCount()
      } else {
        this.userInfo = {
          name: '请登录',
          avatar: '/static/default-avatar.png'
        }
      }
    },
    // 加载会员信息
    async loadMemberInfo() {
      try {
        const res = await getMemberInfo()
        if (res.code === 200 && res.data) {
          this.userInfo.name = res.data.nickname || '用户'
          this.userInfo.avatar = res.data.avatar || '/static/default-avatar.png'
          this.stats[1].value = res.data.points || '0'
          this.stats[2].value = `¥${res.data.balance || '0.00'}`
          
          // 更新本地存储
          const stored = uni.getStorageSync('memberInfo')
          uni.setStorageSync('memberInfo', { ...stored, ...res.data })
        }
      } catch (e) {
        console.error('加载会员信息失败', e)
      }
    },
    // 加载订单数量
    async loadOrderCount() {
      try {
        const res = await getOrderCount()
        if (res.code === 200 && res.data) {
          // 待付款
          this.orders[0].badge = res.data.pendingPay || 0
          // 待发货
          this.orders[1].badge = res.data.pendingShip || 0
          // 待收货
          this.orders[2].badge = res.data.pendingReceive || 0
          // 已完成
          this.orders[3].badge = res.data.completed || 0
        }
      } catch (e) {
        console.error('加载订单数量失败', e)
      }
    },
    goSettings() {
      uni.navigateTo({url: '/pages/settings/index'})
    },
    goProfile() {
      if (!this.isLoggedIn) {
        uni.navigateTo({url: '/pages/login/index'})
        return
      }
      uni.showToast({title: '编辑资料', icon: 'none'})
    },
    goOrders() {
      if (!this.isLoggedIn) {
        uni.navigateTo({url: '/pages/login/index'})
        return
      }
      uni.navigateTo({url: '/pages/orders/index'})
    },
    handleStat(stat) {
      if (!this.isLoggedIn) {
        uni.navigateTo({url: '/pages/login/index'})
        return
      }
      uni.showToast({title: stat.label, icon: 'none'})
    },
    handleOrder(order) {
      if (!this.isLoggedIn) {
        uni.navigateTo({url: '/pages/login/index'})
        return
      }
      uni.navigateTo({url: `/pages/orders/index?status=${order.id}`})
    },
    handleMenu(menu) {
      if (menu.url) {
        if (!this.isLoggedIn && menu.id !== 4 && menu.id !== 3) {
          uni.navigateTo({url: '/pages/login/index'})
          return
        }
        uni.navigateTo({url: menu.url})
      } else {
        uni.showToast({title: menu.label, icon: 'none'})
      }
    }
  }
}
</script>

<style lang="scss" scoped>

.page {
  width: 100%;
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
}

.scroll-container {
  width: 100%;
  height: 100vh;
}

.header {
  width: 100%;
  background: linear-gradient(135deg, #059669 0%, #047857 100%);
  padding: 0 32rpx 64rpx;
  position: relative;
  box-sizing: border-box;
}

.header-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 0 48rpx;

  .header-title {
    font-size: 40rpx;
    font-weight: 700;
    color: #fff;
  }

  .fa-cog {
    font-size: 40rpx;
    color: #fff;
    opacity: 0.9;
  }
}

.user-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 32rpx;
  padding: 32rpx;
  display: flex;
  align-items: center;
  gap: 32rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.user-avatar-wrap {
  position: relative;
}

.user-avatar {
  width: 128rpx;
  height: 128rpx;
  border-radius: 32rpx;
  border: 8rpx solid rgba(255, 255, 255, 0.5);
}

.vip-badge {
  position: absolute;
  bottom: -8rpx;
  right: -8rpx;
  width: 48rpx;
  height: 48rpx;
  background: linear-gradient(135deg, #fbbf24, #f59e0b);
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.2);

  .fa {
    font-size: 20rpx;
    color: #fff;
  }
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 36rpx;
  font-weight: 700;
  color: #1e293b;
  display: block;
  margin-bottom: 12rpx;
}

.vip-tag {
  display: inline-block;
  background: #059669;
  color: #fff;
  font-size: 20rpx;
  font-weight: 600;
  padding: 6rpx 20rpx;
  border-radius: 8rpx;
}

.fa-qrcode {
  font-size: 48rpx;
  color: #94a3b8;
}

.content {
  width: 100%;
  padding: 32rpx;
  margin-top: -32rpx;
  box-sizing: border-box;
  background: #f5f7fa;
}

.stats-card {
  background: #fff;
  border-radius: 32rpx;
  padding: 48rpx 32rpx;
  margin-bottom: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.03);
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32rpx;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.stat-icon {
  width: 96rpx;
  height: 96rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
  transition: transform 0.3s;

  .fa {
    font-size: 40rpx;
  }
}

.stat-item:active .stat-icon {
  transform: scale(1.1);
}

.stat-value {
  font-size: 32rpx;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 22rpx;
  color: #64748b;
}

.order-card, .menu-card {
  background: #fff;
  border-radius: 32rpx;
  padding: 48rpx 40rpx;
  margin-bottom: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.03);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}

.card-title {
  font-size: 32rpx;
  font-weight: 700;
  color: #1e293b;
}

.card-more {
  display: flex;
  align-items: center;
  gap: 8rpx;
  color: #059669;
  font-size: 24rpx;

  .fa {
    font-size: 20rpx;
  }
}

.order-list {
  display: flex;
  justify-content: space-between;
}

.order-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16rpx;
}

.order-icon {
  position: relative;
  width: 88rpx;
  height: 88rpx;
  border-radius: 24rpx;
  background: rgba(0, 0, 0, 0.03);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;

  .fa {
    font-size: 40rpx;
    color: #64748b;
  }

  &.active {
    background: linear-gradient(135deg, rgba(5, 150, 105, 0.1), rgba(5, 150, 105, 0.05));

    .fa {
      color: #059669;
    }
  }
}

.order-item:active .order-icon {
  transform: scale(1.1);
}

.order-badge {
  position: absolute;
  top: -8rpx;
  right: -8rpx;
  min-width: 32rpx;
  height: 32rpx;
  background: #ef4444;
  color: #fff;
  font-size: 18rpx;
  font-weight: 700;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
  box-shadow: 0 2rpx 8rpx rgba(239, 68, 68, 0.3);
}

.order-label {
  font-size: 22rpx;
  color: #64748b;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 32rpx 0;
  border-bottom: 2rpx solid #f1f5f9;
  transition: background 0.3s;

  &:last-child {
    border-bottom: none;
  }

  &:active {
    background: #f8fafc;
  }
}

.menu-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 32rpx;

  .fa {
    font-size: 36rpx;
  }
}

.menu-label {
  flex: 1;
  font-size: 28rpx;
  font-weight: 500;
  color: #1e293b;
}

.fa-chevron-right {
  font-size: 24rpx;
  color: #cbd5e1;
}

.safe-bottom {
  height: 240rpx;
}
</style>
