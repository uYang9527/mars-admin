<template>
  <view class="container">
    <!-- 消息分类标签 -->
    <view class="message-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeTab === tab.key }"
        v-for="tab in tabs" 
        :key="tab.key"
        @click="switchTab(tab.key)"
      >
        <text class="tab-text">{{ tab.name }}</text>
        <!-- <view class="tab-badge" v-if="tab.count > 0">{{ tab.count }}</view> -->
      </view>
    </view>

    <!-- 消息列表 -->
    <view class="message-list">
      <!-- 系统消息 -->
      <view v-if="activeTab === 'system'">
        <view class="message-item" v-for="msg in systemMessages" :key="msg.id" @click="viewMessage(msg)">
          <view class="message-icon system-icon">📢</view>
          <view class="message-content">
            <view class="message-header">
              <text class="message-title">{{ msg.title }}</text>
              <text class="message-time">{{ msg.time }}</text>
            </view>
            <text class="message-preview">{{ msg.content }}</text>
          </view>
          <view class="message-status">
            <view class="unread-dot" v-if="!msg.read"></view>
          </view>
        </view>
      </view>

      <!-- 工作消息 -->
      <view v-if="activeTab === 'work'">
        <view class="message-item" v-for="msg in workMessages" :key="msg.id" @click="viewMessage(msg)">
          <view class="message-icon work-icon">💼</view>
          <view class="message-content">
            <view class="message-header">
              <text class="message-title">{{ msg.title }}</text>
              <text class="message-time">{{ msg.time }}</text>
            </view>
            <text class="message-preview">{{ msg.content }}</text>
            <view class="message-tags">
              <text class="message-tag" :class="msg.priority">{{ msg.priorityText }}</text>
            </view>
          </view>
          <view class="message-status">
            <view class="unread-dot" v-if="!msg.read"></view>
          </view>
        </view>
      </view>

      <!-- 审批消息 -->
      <view v-if="activeTab === 'approval'">
        <view class="message-item" v-for="msg in approvalMessages" :key="msg.id" @click="viewMessage(msg)">
          <view class="message-icon approval-icon">✅</view>
          <view class="message-content">
            <view class="message-header">
              <text class="message-title">{{ msg.title }}</text>
              <text class="message-time">{{ msg.time }}</text>
            </view>
            <text class="message-preview">{{ msg.content }}</text>
            <view class="message-tags">
              <text class="message-tag" :class="msg.status">{{ msg.statusText }}</text>
            </view>
          </view>
          <view class="message-status">
            <view class="unread-dot" v-if="!msg.read"></view>
          </view>
        </view>
      </view>

      <!-- 通知消息 -->
      <view v-if="activeTab === 'notice'">
        <view class="message-item" v-for="msg in noticeMessages" :key="msg.id" @click="viewMessage(msg)">
          <view class="message-icon notice-icon">🔔</view>
          <view class="message-content">
            <view class="message-header">
              <text class="message-title">{{ msg.title }}</text>
              <text class="message-time">{{ msg.time }}</text>
            </view>
            <text class="message-preview">{{ msg.content }}</text>
          </view>
          <view class="message-status">
            <view class="unread-dot" v-if="!msg.read"></view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="getCurrentMessages().length === 0">
      <text class="empty-text">暂无消息</text>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons">
      <view class="action-btn" @click="markAllRead">
        <text class="btn-text">全部已读</text>
      </view>
      <view class="action-btn" @click="clearMessages">
        <text class="btn-text">清空消息</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'system',
      tabs: [
        { key: 'system', name: '系统消息', count: 3 },
        { key: 'work', name: '工作消息', count: 5 },
        { key: 'approval', name: '审批消息', count: 2 },
        { key: 'notice', name: '通知消息', count: 1 }
      ],
      systemMessages: [
        {
          id: 1,
          title: '系统维护通知',
          content: '系统将于今晚22:00-24:00进行维护升级，期间可能影响正常使用，请提前做好准备。',
          time: '2024-01-15 14:30',
          read: false
        },
        {
          id: 2,
          title: '密码安全提醒',
          content: '为了您的账户安全，建议定期更换密码，密码应包含字母、数字和特殊字符。',
          time: '2024-01-14 09:15',
          read: true
        },
        {
          id: 3,
          title: '功能更新说明',
          content: '新版本已发布，新增了考勤统计、报表导出等功能，快来体验吧！',
          time: '2024-01-13 16:45',
          read: false
        }
      ],
      workMessages: [
        {
          id: 4,
          title: '项目进度汇报',
          content: '请于本周五前提交项目进度汇报，包括完成情况、遇到问题及下周计划。',
          time: '2024-01-15 10:20',
          read: false,
          priority: 'high',
          priorityText: '高优先级'
        },
        {
          id: 5,
          title: '会议室预订确认',
          content: '您预订的会议室A-101已确认，会议时间：明天下午2:00-4:00。',
          time: '2024-01-14 17:30',
          read: true,
          priority: 'normal',
          priorityText: '普通'
        },
        {
          id: 6,
          title: '培训课程提醒',
          content: '新员工培训课程将于下周一开始，请准时参加。',
          time: '2024-01-13 11:00',
          read: false,
          priority: 'medium',
          priorityText: '中等优先级'
        }
      ],
      approvalMessages: [
        {
          id: 7,
          title: '请假申请审批',
          content: '您的请假申请已提交，等待主管审批。',
          time: '2024-01-15 08:45',
          read: false,
          status: 'pending',
          statusText: '待审批'
        },
        {
          id: 8,
          title: '报销申请通过',
          content: '您的报销申请已通过审批，款项将在3个工作日内到账。',
          time: '2024-01-14 15:20',
          read: true,
          status: 'approved',
          statusText: '已通过'
        }
      ],
      noticeMessages: [
        {
          id: 9,
          title: '春节放假通知',
          content: '根据国家法定假期安排，春节放假时间为2月10日-2月17日。',
          time: '2024-01-12 18:00',
          read: false
        }
      ]
    }
  },
  onLoad() {
    this.loadMessages();
  },
  onPullDownRefresh() {
    this.loadMessages();
    setTimeout(() => {
      uni.stopPullDownRefresh();
    }, 1000);
  },
  methods: {
    loadMessages() {
      // 模拟加载消息数据
      console.log('加载消息数据');
      this.updateTabCounts();
    },
    switchTab(tabKey) {
      this.activeTab = tabKey;
    },
    getCurrentMessages() {
      switch (this.activeTab) {
        case 'system':
          return this.systemMessages;
        case 'work':
          return this.workMessages;
        case 'approval':
          return this.approvalMessages;
        case 'notice':
          return this.noticeMessages;
        default:
          return [];
      }
    },
    updateTabCounts() {
      this.tabs.forEach(tab => {
        const messages = this.getCurrentMessages();
        tab.count = messages.filter(msg => !msg.read).length;
      });
    },
    viewMessage(message) {
      // 标记为已读
      message.read = true;
      this.updateTabCounts();
      
      // 跳转到消息详情页
      uni.navigateTo({
        url: `/pages/message/message-detail?id=${message.id}&type=${this.activeTab}`
      });
    },
    markAllRead() {
      uni.showModal({
        title: '确认操作',
        content: '确定要将所有消息标记为已读吗？',
        success: (res) => {
          if (res.confirm) {
            const messages = this.getCurrentMessages();
            messages.forEach(msg => {
              msg.read = true;
            });
            this.updateTabCounts();
            uni.showToast({
              title: '已全部标记为已读',
              icon: 'success'
            });
          }
        }
      });
    },
    clearMessages() {
      uni.showModal({
        title: '确认操作',
        content: '确定要清空当前分类的消息吗？此操作不可恢复。',
        success: (res) => {
          if (res.confirm) {
            switch (this.activeTab) {
              case 'system':
                this.systemMessages = [];
                break;
              case 'work':
                this.workMessages = [];
                break;
              case 'approval':
                this.approvalMessages = [];
                break;
              case 'notice':
                this.noticeMessages = [];
                break;
            }
            this.updateTabCounts();
            uni.showToast({
              title: '消息已清空',
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

/* 消息分类标签样式 - 精致版 */
.message-tabs {
  display: flex;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10rpx);
  padding: 24rpx 0;
  border-bottom: 1rpx solid rgba(224, 224, 224, 0.3);
  box-shadow: 0 2rpx 16rpx rgba(0, 0, 0, 0.08);
  margin-bottom: 16rpx;
}

.tab-item {
  flex: 1;
  position: relative;
  text-align: center;
  padding: 24rpx 12rpx;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 12rpx;
  margin: 0 8rpx;
}

.tab-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
  transform: translateY(-2rpx);
  box-shadow: 0 4rpx 16rpx rgba(102, 126, 234, 0.3);
}

.tab-text {
  font-size: 28rpx;
  color: #2c3e50;
  font-weight: 500;
  transition: all 0.3s ease;
}

.tab-item.active .tab-text {
  color: #ffffff;
  font-weight: 600;
}

.tab-badge {
  position: absolute;
  top: 10rpx;
  right: 50%;
  transform: translateX(50%);
  background-color: #ff4757;
  color: #ffffff;
  font-size: 20rpx;
  padding: 4rpx 8rpx;
  border-radius: 20rpx;
  min-width: 32rpx;
  text-align: center;
}

/* 消息列表样式 - 精致版 */
.message-list {
  padding: 0 20rpx 20rpx;
}

.message-item {
  display: flex;
  align-items: flex-start;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10rpx);
  border-radius: 20rpx;
  padding: 32rpx;
  margin-bottom: 16rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
  border: 1rpx solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.message-item:hover {
  transform: translateY(-2rpx);
  box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
}

.message-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.system-icon {
  background-color: #e3f2fd;
}

.work-icon {
  background-color: #f3e5f5;
}

.approval-icon {
  background-color: #e8f5e8;
}

.notice-icon {
  background-color: #fff3e0;
}

.message-content {
  flex: 1;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
}

.message-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333333;
}

.message-time {
  font-size: 24rpx;
  color: #999999;
}

.message-preview {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.message-tags {
  margin-top: 10rpx;
}

.message-tag {
  font-size: 20rpx;
  padding: 4rpx 8rpx;
  border-radius: 8rpx;
  margin-right: 10rpx;
}

.message-tag.high {
  background-color: #ffebee;
  color: #c62828;
}

.message-tag.medium {
  background-color: #fff3e0;
  color: #ef6c00;
}

.message-tag.normal {
  background-color: #e8f5e8;
  color: #2e7d32;
}

.message-tag.pending {
  background-color: #fff3e0;
  color: #ef6c00;
}

.message-tag.approved {
  background-color: #e8f5e8;
  color: #2e7d32;
}

.message-status {
  margin-left: 20rpx;
  display: flex;
  align-items: center;
}

.unread-dot {
  width: 16rpx;
  height: 16rpx;
  background-color: #ff4757;
  border-radius: 50%;
}

/* 空状态样式 */
.empty-state {
  text-align: center;
  padding: 100rpx 0;
}

.empty-text {
  font-size: 28rpx;
  color: #999999;
}

/* 操作按钮样式 */
.action-buttons {
  display: flex;
  justify-content: space-around;
  padding: 30rpx;
  background-color: #ffffff;
  border-top: 1rpx solid #e0e0e0;
}

.action-btn {
  flex: 1;
  text-align: center;
  padding: 20rpx;
  margin: 0 10rpx;
  background-color: #f8f8f8;
  border-radius: 8rpx;
}

.btn-text {
  font-size: 28rpx;
  color: #333333;
}
</style> 