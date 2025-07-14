<template>
  <view class="page-container">
    <!-- uView官方导航栏 -->
    <u-navbar
      title="Mars Admin"
      :bgColor="navbarConfig.background"
      :fixed="true"
      :placeholder="true"
      :safeAreaInsetTop="true"
      :titleStyle="{ color: '#ffffff' }"
    >
      <template #right>
        <view class="navbar-right" @click="goToMessage">
          <u-icon name="bell" size="28" color="#ffffff"></u-icon>
          <u-badge :count="unreadCount" :isDot="true" :offset="[0, -4]"></u-badge>
        </view>
      </template>
    </u-navbar>

    <!-- 页面内容 -->
    <view class="page-content">
      <!-- 搜索框 -->
      <view class="search-section">
        <view class="search-container">
          <u-search
            v-model="searchKeyword"
            placeholder="搜索应用、文件、同事..."
            :showAction="false"
            height="44"
            animation
            shape="round"
            bgColor="#f8f9fa"
            borderColor="transparent"
            clearabled
            @click="handleSearchClick"
            @change="handleSearchChange"
          >
            <template #prefix>
              <u-icon name="search" size="18" color="#999"></u-icon>
            </template>
          </u-search>
        </view>
      </view>

      <!-- 头部轮播图 -->
      <view class="banner-section">
        <u-swiper
          v-if="bannerList.length"
          :list="bannerList"
          keyName="image"
          indicator
          indicatorMode="dot"
          circular
          height="180"
          radius="16"
          :interval="4000"
          @click="handleBannerClick"
        ></u-swiper>
        <u-empty v-else icon="photo" text="暂无轮播图" marginTop="50" marginBottom="50"></u-empty>
      </view>

      <!-- 快捷功能 -->
      <view class="quick-section">
        <view class="section-title">
          <text class="title-text">快捷功能</text>
          <text class="subtitle-text">常用应用入口</text>
        </view>
        <view v-if="quickMenuList.length" class="quick-grid">
          <view
            v-for="(item, index) in quickMenuList"
            :key="index"
            class="quick-item"
            @click="handleQuickMenu(item)"
          >
            <view class="quick-icon-wrapper" :style="{ backgroundColor: `${item.color}15` }">
              <u-icon :name="item.icon" size="28" :color="item.color"></u-icon>
              <u-badge v-if="item.badge" :count="item.badge" :offset="[6, -6]"></u-badge>
            </view>
            <text class="quick-text">{{ item.name }}</text>
          </view>
        </view>
        <u-empty v-else icon="list" text="暂无快捷功能" marginTop="30" marginBottom="30"></u-empty>
      </view>

      <!-- 工作动态 -->
      <view class="content-section">
        <view class="section-header">
          <view class="header-left">
            <text class="section-title">工作动态</text>
            <text class="section-subtitle">最新工作进展</text>
          </view>
          <view class="header-right" @click="goToWorkList">
            <text class="more-text">查看更多</text>
            <u-icon name="arrow-right" size="14" color="#999"></u-icon>
          </view>
        </view>

        <view v-if="workList.length" class="work-list">
          <work-item
            v-for="(item, index) in workList"
            :key="index"
            :item="item"
            @click="goToWorkDetail"
          ></work-item>
        </view>
        <u-empty v-else icon="file-text" text="暂无工作动态" marginTop="30" marginBottom="30"></u-empty>
      </view>

      <!-- 公司公告 -->
      <view class="content-section">
        <view class="section-header">
          <view class="header-left">
            <text class="section-title">公司公告</text>
            <text class="section-subtitle">重要通知信息</text>
          </view>
          <view class="header-right" @click="goToNoticeList">
            <text class="more-text">查看更多</text>
            <u-icon name="arrow-right" size="14" color="#999"></u-icon>
          </view>
        </view>

        <view v-if="noticeList.length" class="notice-list">
          <view v-for="(item, index) in noticeList" :key="index" class="notice-item" @click="goToNoticeDetail(item)">
            <view class="notice-content">
              <view class="notice-header">
                <text class="notice-title">{{ item.title }}</text>
                <u-tag :text="item.type" type="primary" size="mini" plain shape="circle"></u-tag>
              </view>
              <text class="notice-desc">{{ item.content }}</text>
              <view class="notice-meta">
                <text class="notice-time">{{ item.time }}</text>
                <u-icon name="arrow-right" size="12" color="#ccc"></u-icon>
              </view>
            </view>
          </view>
        </view>
        <u-empty v-else icon="bell" text="暂无公告" marginTop="30" marginBottom="30"></u-empty>
      </view>

      <!-- 统计数据 -->
      <view class="stats-section">
        <view class="section-title">
          <text class="title-text">数据统计</text>
          <text class="subtitle-text">实时数据展示</text>
        </view>
        <view v-if="statsData.length" class="stats-grid">
          <view v-for="(item, index) in statsData" :key="index" class="stats-item">
            <view class="stats-content">
              <text class="stats-number">{{ item.value }}</text>
              <text class="stats-label">{{ item.label }}</text>
            </view>
            <view class="stats-icon" :style="{ backgroundColor: `${item.color}15` }">
              <u-icon :name="item.icon" size="20" :color="item.color"></u-icon>
            </view>
          </view>
        </view>
        <u-empty v-else icon="data" text="暂无统计数据" marginTop="30" marginBottom="30"></u-empty>
      </view>

      <!-- 底部安全区域 -->
      <u-gap height="50" bgColor="transparent"></u-gap>
      <u-safe-bottom></u-safe-bottom>
    </view>
  </view>
</template>

<script>
import api from '@/api'
import { showLoading, hideLoading, showError } from '@/utils/common'

export default {
  data() {
    return {
      // 导航栏配置
      navbarConfig: {
        background: 'linear-gradient(45deg, #2979ff, #007AFF)',
      },
      searchKeyword: '',
      unreadCount: 8,
      loading: false,
      // 页面数据
      bannerList: [],
      quickMenuList: [],
      noticeList: [],
      workList: [],
      statsData: []
    }
  },
  onLoad() {
    this.loadHomeData();
  },
  onPullDownRefresh() {
    // 下拉刷新
    this.loadHomeData(true);
  },
  onShow() {
    // 页面显示时刷新未读消息数
    this.getUnreadCount();
  },
  methods: {
    // 加载首页数据
    async loadHomeData(isRefresh = false) {
      try {
        if (!isRefresh) {
          this.loading = true;
          showLoading('加载中...');
        }

        const res = await api.home.getHomeData();
        if (res.code === 200) {
          const { bannerList, quickMenuList, noticeList, workList, statsData } = res.data;
          this.bannerList = bannerList || [];
          this.quickMenuList = quickMenuList || [];
          this.noticeList = noticeList || [];
          this.workList = workList || [];
          this.statsData = statsData || [];
        } else {
          showError(res.message || '加载失败');
        }
      } catch (error) {
        console.error('加载首页数据失败:', error);
        showError('加载失败，请稍后重试');
      } finally {
        this.loading = false;
        hideLoading();
        if (isRefresh) {
          uni.stopPullDownRefresh();
        }
      }
    },

    // 获取未读消息数
    async getUnreadCount() {
      // 这里可以调用API获取未读消息数
      // const res = await api.message.getUnreadCount();
      // this.unreadCount = res.data || 0;
    },
    // 搜索框点击事件
    handleSearchClick() {
      uni.showToast({
        title: '进入搜索页面',
        icon: 'none'
      });
      // TODO: 跳转到搜索页面
      // uni.navigateTo({
      //   url: '/pages/search/search'
      // });
    },

    // 搜索内容变化
    handleSearchChange(value) {
      console.log('搜索内容:', value);
      // TODO: 实现搜索功能
    },

    // 轮播图点击事件
    handleBannerClick(index) {
      const banner = this.bannerList[index];
      if (banner) {
        uni.showToast({
          title: `点击了${banner.title}`,
          icon: 'none'
        });
        // TODO: 根据banner类型跳转到不同页面
      }
    },

    // 快捷菜单点击事件
    handleQuickMenu(item) {
      uni.showToast({
        title: `点击了${item.name}`,
        icon: 'none'
      });

      // 根据action跳转到不同页面
      switch (item.action) {
        case 'attendance':
          // TODO: 跳转到考勤打卡页面
          break;
        case 'leave':
          // TODO: 跳转到请假申请页面
          break;
        case 'expense':
          // TODO: 跳转到报销申请页面
          break;
        case 'report':
          // TODO: 跳转到工作汇报页面
          break;
        case 'meeting':
          // TODO: 跳转到会议室预订页面
          break;
        case 'contacts':
          // TODO: 跳转到通讯录页面
          break;
        case 'files':
          // TODO: 跳转到文件管理页面
          break;
        case 'more':
          // TODO: 跳转到更多应用页面
          break;
        default:
          console.log('未知的快捷菜单:', item.action);
      }
    },
    // 跳转到消息页面
    goToMessage() {
      uni.switchTab({
        url: '/pages/message/message'
      });
    },

    // 跳转到工作动态列表页
    goToWorkList() {
      uni.showToast({
        title: '跳转到工作动态列表',
        icon: 'none'
      });
      // TODO: 跳转到工作动态列表页面
      // uni.navigateTo({
      //   url: '/pages/work/list'
      // });
    },

    // 跳转到工作动态详情页
    goToWorkDetail(item) {
      uni.showToast({
        title: `查看${item.name}的工作动态`,
        icon: 'none'
      });
      // TODO: 跳转到工作动态详情页面
      // uni.navigateTo({
      //   url: `/pages/work/detail?id=${item.id}`
      // });
    },

    // 跳转到公告列表
    goToNoticeList() {
      uni.showToast({
        title: '跳转到公告列表',
        icon: 'none'
      });
      // TODO: 跳转到公告列表页面
      // uni.navigateTo({
      //   url: '/pages/notice/list'
      // });
    },

    // 跳转到公告详情
    goToNoticeDetail(item) {
      uni.showToast({
        title: `查看公告:${item.title}`,
        icon: 'none'
      });
      // TODO: 跳转到公告详情页面
      // uni.navigateTo({
      //   url: `/pages/notice/detail?id=${item.id}`
      // });
    }
  }
}
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
}

.page-content {
  padding-bottom: 30rpx;
}

.navbar-right {
  display: flex;
  align-items: center;
  position: relative;
  padding: 10rpx;
}

// 搜索区域
.search-section {
  padding: 20rpx 30rpx 30rpx;

  .search-container {
    background: #ffffff;
    border-radius: 24rpx;
    padding: 8rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
  }
}

// 轮播图区域
.banner-section {
  padding: 0 30rpx 30rpx;
}

// 通用区域样式
.quick-section,
.content-section,
.stats-section {
  margin: 0 30rpx 30rpx;
  background: #ffffff;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 20rpx rgba(0, 0, 0, 0.06);
}

// 标题样式
.section-title {
  margin-bottom: 30rpx;

  .title-text {
    font-size: 36rpx;
    font-weight: 600;
    color: #1a1a1a;
    display: block;
    margin-bottom: 8rpx;
  }

  .subtitle-text {
    font-size: 26rpx;
    color: #999;
    display: block;
  }
}

// 区域头部
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 30rpx;

  .header-left {
    flex: 1;
  }

  .section-title {
    font-size: 36rpx;
    font-weight: 600;
    color: #1a1a1a;
    margin-bottom: 8rpx;
  }

  .section-subtitle {
    font-size: 26rpx;
    color: #999;
  }

  .header-right {
    display: flex;
    align-items: center;
    padding: 8rpx 16rpx;
    border-radius: 20rpx;
    background: #f8f9fa;

    .more-text {
      font-size: 26rpx;
      color: #666;
      margin-right: 8rpx;
    }
  }
}

// 快捷菜单样式
.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 10rpx;
  border-radius: 16rpx;
  transition: all 0.3s ease;
  position: relative;

  &:active {
    transform: scale(0.95);
    background: #f8f9fa;
  }
}

.quick-icon-wrapper {
  position: relative;
  width: 88rpx;
  height: 88rpx;
  border-radius: 22rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 16rpx;
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.9);
  }
}

.quick-text {
  font-size: 24rpx;
  color: #333;
  text-align: center;
  font-weight: 500;
}

// 公告列表样式
.notice-list {
  .notice-item {
    padding: 24rpx;
    margin-bottom: 20rpx;
    background: #f8f9fa;
    border-radius: 16rpx;
    transition: all 0.3s ease;
    position: relative;

    &:last-child {
      margin-bottom: 0;
    }

    &:active {
      transform: scale(0.98);
      background: #f0f0f0;
    }
  }

  .notice-content {
    flex: 1;
  }

  .notice-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 12rpx;
  }

  .notice-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #1a1a1a;
    line-height: 1.4;
    flex: 1;
    margin-right: 20rpx;
  }

  .notice-desc {
    font-size: 26rpx;
    color: #666;
    line-height: 1.5;
    margin-bottom: 16rpx;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .notice-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .notice-time {
    font-size: 24rpx;
    color: #999;
  }
}

// 统计数据样式
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
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
  border-radius: 16rpx;
  border: 1px solid #f0f0f0;
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
</style> 