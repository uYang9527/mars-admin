<template>
  <view class="container">
    <!-- 头部轮播图 -->
    <view class="banner-section">
      <swiper class="banner-swiper" indicator-dots="true" autoplay="true" interval="3000" duration="500">
        <swiper-item v-for="(item, index) in bannerList" :key="index">
          <image :src="item.image" class="banner-image" mode="aspectFill"></image>
        </swiper-item>
      </swiper>
    </view>

    <!-- 快捷功能 -->
    <view class="quick-menu">
      <view class="quick-title">快捷功能</view>
      <view class="quick-grid">
        <view class="quick-item" v-for="(item, index) in quickMenuList" :key="index" @click="handleQuickMenu(item)">
          <view class="quick-icon">
            <image :src="item.icon" class="icon-image"></image>
          </view>
          <text class="quick-text">{{ item.name }}</text>
        </view>
      </view>
    </view>

    <!-- 公司公告 -->
    <view class="notice-section">
      <view class="section-header">
        <text class="section-title">公司公告</text>
        <text class="section-more" @click="goToNoticeList">更多</text>
      </view>
      <view class="notice-list">
        <view class="notice-item" v-for="(item, index) in noticeList" :key="index" @click="goToNoticeDetail(item)">
          <view class="notice-content">
            <text class="notice-title">{{ item.title }}</text>
            <text class="notice-time">{{ item.time }}</text>
          </view>
          <view class="notice-arrow">
            <text class="arrow-icon">></text>
          </view>
        </view>
      </view>
    </view>

    <!-- 工作动态 -->
    <view class="work-section">
      <view class="section-header">
        <text class="section-title">工作动态</text>
        <text class="section-more" @click="goToWorkList">更多</text>
      </view>
      <view class="work-list">
        <view class="work-item" v-for="(item, index) in workList" :key="index" @click="goToWorkDetail(item)">
          <view class="work-avatar">
            <image :src="item.avatar" class="avatar-image"></image>
          </view>
          <view class="work-content">
            <text class="work-name">{{ item.name }}</text>
            <text class="work-desc">{{ item.description }}</text>
            <text class="work-time">{{ item.time }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 统计数据 -->
    <view class="stats-section">
      <view class="section-title">数据统计</view>
      <view class="stats-grid">
        <view class="stats-item" v-for="(item, index) in statsList" :key="index">
          <text class="stats-number">{{ item.number }}</text>
          <text class="stats-label">{{ item.label }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      bannerList: [
        {
          image: 'https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&h=400&fit=crop&crop=center',
          title: '企业文化'
        },
        {
          image: 'https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=800&h=400&fit=crop&crop=center',
          title: '团队建设'
        },
        {
          image: 'https://images.unsplash.com/photo-1560472354-b33ff0c44a43?w=800&h=400&fit=crop&crop=center',
          title: '业务发展'
        }
      ],
      quickMenuList: [
        {
          name: '考勤打卡',
          icon: 'https://img.icons8.com/fluency/96/000000/clock.png',
          action: 'attendance'
        },
        {
          name: '请假申请',
          icon: 'https://img.icons8.com/fluency/96/000000/calendar.png',
          action: 'leave'
        },
        {
          name: '报销申请',
          icon: 'https://img.icons8.com/fluency/96/000000/money-bag.png',
          action: 'expense'
        },
        {
          name: '工作汇报',
          icon: 'https://img.icons8.com/fluency/96/000000/report-card.png',
          action: 'report'
        },
        {
          name: '会议室预订',
          icon: 'https://img.icons8.com/fluency/96/000000/conference-call.png',
          action: 'meeting'
        },
        {
          name: '通讯录',
          icon: 'https://img.icons8.com/fluency/96/000000/contacts.png',
          action: 'contacts'
        },
        {
          name: '文件管理',
          icon: 'https://img.icons8.com/fluency/96/000000/menu.png',
          action: 'files'
        },
        {
          name: '更多',
          icon: 'https://img.icons8.com/fluency/96/000000/menu.png',
          action: 'more'
        }
      ],
      noticeList: [
        {
          id: 1,
          title: '关于2024年春节放假安排的通知',
          time: '2024-01-15'
        },
        {
          id: 2,
          title: '公司年度表彰大会即将召开',
          time: '2024-01-10'
        },
        {
          id: 3,
          title: '新员工入职培训安排',
          time: '2024-01-08'
        }
      ],
      workList: [
        {
          id: 1,
          name: '张三',
          avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=80&h=80&fit=crop&crop=face&auto=format&q=80',
          description: '完成了产品需求分析报告',
          time: '2小时前'
        },
        {
          id: 2,
          name: '李四',
          avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=80&h=80&fit=crop&crop=face&auto=format&q=80',
          description: '提交了项目进度汇报',
          time: '4小时前'
        },
        {
          id: 3,
          name: '王五',
          avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=80&h=80&fit=crop&crop=face&auto=format&q=80',
          description: '完成了客户需求调研',
          time: '6小时前'
        }
      ],
      statsList: [
        {
          number: '1,234',
          label: '员工总数'
        },
        {
          number: '56',
          label: '部门数量'
        },
        {
          number: '89%',
          label: '出勤率'
        },
        {
          number: '23',
          label: '项目数量'
        }
      ]
    }
  },
  onLoad() {
    this.loadData();
  },
  onPullDownRefresh() {
    this.loadData();
    setTimeout(() => {
      uni.stopPullDownRefresh();
    }, 1000);
  },
  methods: {
    loadData() {
      // 模拟加载数据
      console.log('加载首页数据');
    },
    handleQuickMenu(item) {
      console.log('点击快捷功能:', item.name);
      uni.showToast({
        title: `点击了${item.name}`,
        icon: 'none'
      });
    },
    goToNoticeList() {
      uni.navigateTo({
        url: '/pages/notice/notice-list'
      });
    },
    goToNoticeDetail(item) {
      uni.navigateTo({
        url: `/pages/notice/notice-detail?id=${item.id}`
      });
    },
    goToWorkList() {
      uni.navigateTo({
        url: '/pages/work/work-list'
      });
    },
    goToWorkDetail(item) {
      uni.navigateTo({
        url: `/pages/work/work-detail?id=${item.id}`
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

/* 轮播图样式 - 精致版 */
.banner-section {
  margin-bottom: 24rpx;
  overflow: hidden;
}

.banner-swiper {
  height: 320rpx;
  border-radius: 0 0 24rpx 24rpx;
  overflow: hidden;
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.banner-image:hover {
  transform: scale(1.05);
}

/* 快捷功能样式 - 精致版 */
.quick-menu {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10rpx);
  padding: 32rpx;
  margin: 0 16rpx 24rpx;
  border-radius: 20rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
  border: 1rpx solid rgba(255, 255, 255, 0.2);
}

.quick-title {
  font-size: 34rpx;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 32rpx;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24rpx;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx 16rpx;
  border-radius: 16rpx;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.quick-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.quick-item:hover::before {
  opacity: 1;
}

.quick-item:hover {
  transform: translateY(-4rpx);
}

.quick-icon {
  width: 88rpx;
  height: 88rpx;
  margin-bottom: 12rpx;
  position: relative;
  z-index: 1;
}

.icon-image {
  width: 100%;
  height: 100%;
  filter: drop-shadow(0 4rpx 8rpx rgba(0, 0, 0, 0.1));
}

.quick-text {
  font-size: 24rpx;
  color: #2c3e50;
  text-align: center;
  font-weight: 500;
  position: relative;
  z-index: 1;
}

/* 公告样式 - 精致版 */
.notice-section, .work-section {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10rpx);
  padding: 32rpx;
  margin: 0 16rpx 24rpx;
  border-radius: 20rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
  border: 1rpx solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.notice-section:hover, .work-section:hover {
  transform: translateY(-2rpx);
  box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}

.section-title {
  font-size: 34rpx;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.section-more {
  font-size: 28rpx;
  color: #667eea;
  font-weight: 500;
  transition: color 0.3s ease;
}

.section-more:hover {
  color: #764ba2;
}

.notice-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.notice-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.notice-content {
  flex: 1;
}

.notice-title {
  font-size: 28rpx;
  color: #333333;
  display: block;
  margin-bottom: 10rpx;
}

.notice-time {
  font-size: 24rpx;
  color: #999999;
}

.notice-arrow {
  margin-left: 20rpx;
}

.arrow-icon {
  font-size: 28rpx;
  color: #cccccc;
}

/* 工作动态样式 */
.work-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.work-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.work-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 20rpx;
  overflow: hidden;
}

.avatar-image {
  width: 100%;
  height: 100%;
}

.work-content {
  flex: 1;
}

.work-name {
  font-size: 28rpx;
  color: #333333;
  font-weight: bold;
  display: block;
  margin-bottom: 10rpx;
}

.work-desc {
  font-size: 26rpx;
  color: #666666;
  display: block;
  margin-bottom: 10rpx;
}

.work-time {
  font-size: 24rpx;
  color: #999999;
}

/* 统计数据样式 - 精致版 */
.stats-section {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10rpx);
  padding: 32rpx;
  margin: 0 16rpx 24rpx;
  border-radius: 20rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
  border: 1rpx solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.stats-section:hover {
  transform: translateY(-2rpx);
  box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24rpx;
  margin-top: 32rpx;
}

.stats-item {
  text-align: center;
  padding: 24rpx;
  border-radius: 16rpx;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.stats-item:hover {
  transform: translateY(-4rpx);
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.2) 0%, rgba(118, 75, 162, 0.2) 100%);
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
  color: #666666;
}
</style> 