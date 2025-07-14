<template>
  <view class="page-container">
    <!-- uView官方导航栏 -->
    <u-navbar
      title="消息中心"
      :bgColor="navbarConfig.background"
      :fixed="true"
      :placeholder="true"
      :safeAreaInsetTop="true"
      :titleStyle="{ color: '#ffffff' }"
    >
      <template #right>
        <view class="navbar-right">
          <u-icon name="search" size="28" color="#ffffff" @click="showSearchView"></u-icon>
          <u-icon name="more-dot-fill" size="28" color="#ffffff" style="margin-left: 30rpx;" @click="showMoreOptions"></u-icon>
        </view>
      </template>
    </u-navbar>

    <!-- 页面内容 -->
    <view class="page-content">
      <!-- 消息分类标签 -->
      <view class="tabs-container">
        <view class="tabs-wrapper">
          <view
            v-for="(tab, index) in tabs"
            :key="index"
            class="tab-item"
            :class="{ active: tabCurrent === index }"
            @click="tabChange(index)"
          >
            <text class="tab-text">{{ tab.name }}</text>
            <view v-if="tab.badge > 0" class="tab-badge">{{ tab.badge > 99 ? '99+' : tab.badge }}</view>
          </view>
        </view>
        <view class="tab-indicator" :style="{ transform: `translateX(${tabCurrent * 100}%)` }"></view>
      </view>

      <!-- 消息列表 -->
      <view class="message-list">
        <!-- 空状态展示 -->
        <u-empty
          v-if="currentMessages.length === 0"
          icon="email"
          text="暂无消息"
          marginTop="100"
          marginBottom="100"
        ></u-empty>

        <!-- 消息列表 -->
        <template v-else>
          <view class="message-container">
            <u-swipe-action>
              <u-swipe-action-item
                v-for="(msg, index) in currentMessages"
                :key="msg.id || index"
                :options="swipeOptions"
                @click="handleSwipeAction"
              >
                <view
                  class="message-item"
                  :class="{ unread: !msg.read }"
                  @click="viewMessage(msg)"
                >
                  <view class="message-avatar">
                    <view class="avatar-wrapper" :class="[getIconClass(tabCurrent)]">
                      <u-icon :name="getIconName(tabCurrent)" size="24" color="#ffffff"></u-icon>
                    </view>
                    <view v-if="!msg.read" class="unread-dot"></view>
                  </view>

                  <view class="message-content">
                    <view class="message-header">
                      <text class="message-title">{{ msg.title }}</text>
                      <text class="message-time">{{ msg.time }}</text>
                    </view>
                    <text class="message-desc">{{ msg.content }}</text>
                    <view class="message-tags" v-if="hasTags(msg)">
                      <u-tag v-if="msg.priority" :text="msg.priorityText" :type="getPriorityType(msg.priority)" size="mini" plain shape="circle"></u-tag>
                      <u-tag v-if="msg.status" :text="msg.statusText" :type="getStatusType(msg.status)" size="mini" plain shape="circle"></u-tag>
                    </view>
                  </view>

                  <view class="message-arrow">
                    <u-icon name="arrow-right" size="14" color="#ccc"></u-icon>
                  </view>
                </view>
              </u-swipe-action-item>
          </u-swipe-action>
          </view>
        </template>
      </view>

      <!-- 底部工具栏 -->
      <view class="action-bar">
        <u-button text="全部已读" type="info" size="normal" icon="checkmark-circle" @click="markAllRead"></u-button>
        <u-button text="清空消息" type="warning" size="normal" icon="trash" @click="clearMessages"></u-button>
      </view>
      
      <!-- 操作菜单弹出层 -->
      <u-popup :show="showActionPopup" mode="bottom" @close="closePopup" round="16" safeAreaInsetBottom>
        <view class="popup-content">
          <view class="popup-title">消息操作</view>
          <u-grid :border="false" col="3">
            <u-grid-item v-for="(item, index) in popupActions" :key="index" @click="handlePopupAction(item.action)">
              <view class="popup-grid-item">
                <u-icon :name="item.icon" size="40" :color="item.color"></u-icon>
                <text class="popup-grid-text">{{ item.name }}</text>
              </view>
            </u-grid-item>
          </u-grid>
          <view class="popup-cancel">
            <u-button text="取消" type="info" shape="circle" @click="closePopup"></u-button>
          </view>
        </view>
      </u-popup>
      
      <!-- 搜索弹出层 -->
      <u-popup :show="showSearchPopup" mode="center" @close="closeSearchPopup" round="12">
        <view class="search-popup">
          <u-search
            v-model="searchKeyword"
            placeholder="搜索消息内容"
            showAction
            actionText="搜索"
            @search="searchMessages"
            @custom="searchMessages"
            bgColor="#f5f6fa"
          ></u-search>
        </view>
      </u-popup>

      <u-safe-bottom></u-safe-bottom>
    </view>
  </view>
</template>

<script>
import api from '@/api'
import pageMixin from '@/mixins/common/page'
import { showLoading, hideLoading, showError, showSuccess } from '@/utils/common'

export default {
  mixins: [pageMixin],
  data() {
    return {
      navbarConfig: {
        background: 'linear-gradient(45deg, #2979ff, #007AFF)',
      },
      tabCurrent: 0,
      tabs: [
        { name: '系统消息' },
        { name: '工作消息' },
        { name: '审批消息' },
        { name: '通知消息' }
      ],
      showActionPopup: false,
      showSearchPopup: false,
      searchKeyword: '',
      // 侧滑选项
      swipeOptions: [
        {
          text: '已读',
          style: {
            backgroundColor: '#19be6b'
          }
        },
        {
          text: '删除',
          style: {
            backgroundColor: '#fa3534'
          }
        }
      ],
      // 弹出菜单选项
      popupActions: [
        {
          name: '只看未读',
          icon: 'eye',
          color: '#2979ff',
          action: 'filterUnread'
        },
        {
          name: '只看已读',
          icon: 'eye-fill',
          color: '#2979ff',
          action: 'filterRead'
        },
        {
          name: '全部消息',
          icon: 'list',
          color: '#2979ff',
          action: 'showAll'
        },
        {
          name: '全部已读',
          icon: 'checkmark-circle',
          color: '#19be6b',
          action: 'markAllRead'
        },
        {
          name: '清空消息',
          icon: 'trash',
          color: '#fa3534',
          action: 'clearMessages'
        }
      ],
      // 消息数据
      allMessages: {
        system: [],
        work: [],
        approval: [],
        notice: []
      },
      // 过滤设置
      filter: 'all' // 'all', 'read', 'unread'
    }
  },
  computed: {
    currentMessages() {
      let messages = [];
      
      switch (this.tabCurrent) {
        case 0:
          messages = this.allMessages.system;
          break;
        case 1:
          messages = this.allMessages.work;
          break;
        case 2:
          messages = this.allMessages.approval;
          break;
        case 3:
          messages = this.allMessages.notice;
          break;
        default:
          messages = [];
      }
      
      if (this.filter === 'read') {
        return messages.filter(msg => msg.read);
      } else if (this.filter === 'unread') {
        return messages.filter(msg => !msg.read);
      }
      
      return messages;
    }
  },
  methods: {
    // 重写页面混入的loadPageData方法
    async loadPageData() {
      try {
        this.showLoading('加载消息...');

        // 这里可以调用API获取消息数据
        // const res = await api.message.getMessageList();

        // 暂时使用mock数据
        const { messageList } = await import('@/mock/index.js');

        this.allMessages = {
          system: JSON.parse(JSON.stringify(messageList.system)),
          work: JSON.parse(JSON.stringify(messageList.work)),
          approval: JSON.parse(JSON.stringify(messageList.approval)),
          notice: JSON.parse(JSON.stringify(messageList.notice))
        };

      } catch (error) {
        console.error('加载消息失败:', error);
        this.showError('加载消息失败');
      } finally {
        this.hideLoading();
      }
    },
    tabChange(index) {
      this.tabCurrent = index;
    },
    getIconName(tabIndex) {
      const icons = ['volume', 'file-text', 'checkmark', 'bell'];
      return icons[tabIndex] || 'chat';
    },
    getIconClass(tabIndex) {
      const classes = ['system-icon', 'work-icon', 'approval-icon', 'notice-icon'];
      return classes[tabIndex] || '';
    },
    hasTags(msg) {
      return msg.priority || msg.status;
    },
    getPriorityType(priority) {
      switch (priority) {
        case 'high':
          return 'error';
        case 'medium':
          return 'warning';
        default:
          return 'info';
      }
    },
    getStatusType(status) {
      switch (status) {
        case 'approved':
          return 'success';
        case 'pending':
          return 'warning';
        case 'rejected':
          return 'error';
        default:
          return 'info';
      }
    },
    handleSwipeAction(action) {
      // 根据action处理不同操作
      const optionIndex = action.index;
      const messageIndex = action.index;

      if (optionIndex === 0) {
        // 标记为已读
        this.markAsRead(messageIndex);
      } else if (optionIndex === 1) {
        // 删除消息
        this.deleteMessage(messageIndex);
      }
    },
    markAsRead(index) {
      const messages = this.currentMessages;
      if (messages[index]) {
        messages[index].read = true;
        uni.showToast({
          title: '已标记为已读',
          icon: 'success'
        });
      }
    },
    deleteMessage(index) {
      const messages = this.currentMessages;
      if (messages[index]) {
        messages.splice(index, 1);
        uni.showToast({
          title: '已删除消息',
          icon: 'success'
        });
      }
    },
    showMoreOptions() {
      this.showActionPopup = true;
    },
    closePopup() {
      this.showActionPopup = false;
    },
    showSearchView() {
      this.showSearchPopup = true;
    },
    closeSearchPopup() {
      this.showSearchPopup = false;
    },
    searchMessages() {
      if (!this.searchKeyword.trim()) {
        uni.showToast({
          title: '请输入搜索内容',
          icon: 'none'
        });
        return;
      }
      
      const keyword = this.searchKeyword.toLowerCase();
      
      // 在所有消息中搜索
      const searchResults = {
        system: this.allMessages.system.filter(msg => 
          msg.title.toLowerCase().includes(keyword) || msg.content.toLowerCase().includes(keyword)
        ),
        work: this.allMessages.work.filter(msg => 
          msg.title.toLowerCase().includes(keyword) || msg.content.toLowerCase().includes(keyword)
        ),
        approval: this.allMessages.approval.filter(msg => 
          msg.title.toLowerCase().includes(keyword) || msg.content.toLowerCase().includes(keyword)
        ),
        notice: this.allMessages.notice.filter(msg => 
          msg.title.toLowerCase().includes(keyword) || msg.content.toLowerCase().includes(keyword)
        )
      };
      
      // 统计搜索结果数量
      const totalResults = searchResults.system.length + 
                          searchResults.work.length + 
                          searchResults.approval.length + 
                          searchResults.notice.length;
      
      if (totalResults === 0) {
        uni.showToast({
          title: '未找到匹配内容',
          icon: 'none'
        });
      } else {
        // 使用搜索结果替换当前显示的消息
        this.allMessages = searchResults;
        this.closeSearchPopup();
        uni.showToast({
          title: `找到${totalResults}条相关消息`,
          icon: 'none'
        });
      }
    },
    handlePopupAction(action) {
      this.closePopup();
      
      switch (action) {
        case 'filterUnread':
          this.filterUnread();
          break;
        case 'filterRead':
          this.filterRead();
          break;
        case 'showAll':
          this.showAllMessages();
          break;
        case 'markAllRead':
          this.markAllRead();
          break;
        case 'clearMessages':
          this.clearMessages();
          break;
      }
    },
    filterUnread() {
      this.filter = 'unread';
      uni.showToast({
        title: '只显示未读消息',
        icon: 'none'
      });
    },
    filterRead() {
      this.filter = 'read';
      uni.showToast({
        title: '只显示已读消息',
        icon: 'none'
      });
    },
    showAllMessages() {
      this.filter = 'all';
      uni.showToast({
        title: '显示全部消息',
        icon: 'none'
      });
    },
    markAllRead() {
      // 根据当前选项卡将对应类型的消息全部标记为已读
      let messages = [];
      
      switch (this.tabCurrent) {
        case 0:
          messages = this.allMessages.system;
          break;
        case 1:
          messages = this.allMessages.work;
          break;
        case 2:
          messages = this.allMessages.approval;
          break;
        case 3:
          messages = this.allMessages.notice;
          break;
      }
      
      messages.forEach(msg => {
        msg.read = true;
      });
      
      uni.showToast({
        title: '已全部标为已读',
        icon: 'success'
      });
    },
    clearMessages() {
      uni.showModal({
        title: '确认清空',
        content: '确定要清空当前分类下的所有消息吗？',
        success: res => {
          if (res.confirm) {
            // 清空当前分类的消息
            switch (this.tabCurrent) {
              case 0:
                this.allMessages.system = [];
                break;
              case 1:
                this.allMessages.work = [];
                break;
              case 2:
                this.allMessages.approval = [];
                break;
              case 3:
                this.allMessages.notice = [];
                break;
            }
            
            uni.showToast({
              title: '已清空消息',
              icon: 'success'
            });
          }
        }
      });
    },
    viewMessage(msg) {
      // 查看消息详情，标记为已读
      msg.read = true;
      
      uni.showToast({
        title: `查看消息：${msg.title}`,
        icon: 'none'
      });
      
      // 实际项目中可跳转到详情页
      // uni.navigateTo({
      //   url: `/pages/message-detail/message-detail?id=${msg.id}&type=${this.tabCurrent}`
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

.navbar-right {
  display: flex;
  align-items: center;
  padding: 10rpx;
}

// 标签栏样式
.tabs-container {
  background: #ffffff;
  margin: 20rpx 30rpx 20rpx;
  border-radius: 16rpx;
  padding: 8rpx;
  box-shadow: 0 2rpx 20rpx rgba(0, 0, 0, 0.06);
  position: relative;
  overflow: hidden;
}

.tabs-wrapper {
  display: flex;
  position: relative;
}

.tab-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20rpx 16rpx;
  border-radius: 12rpx;
  transition: all 0.3s ease;
  position: relative;
  z-index: 2;

  &.active {
    background: #2979ff;

    .tab-text {
      color: #ffffff;
      font-weight: 600;
    }

    .tab-badge {
      background: rgba(255, 255, 255, 0.2);
      color: #ffffff;
    }
  }
}

.tab-text {
  font-size: 28rpx;
  color: #666;
  transition: all 0.3s ease;
}

.tab-badge {
  background: #ff4757;
  color: #ffffff;
  font-size: 20rpx;
  padding: 4rpx 8rpx;
  border-radius: 10rpx;
  margin-left: 8rpx;
  min-width: 32rpx;
  text-align: center;
  line-height: 1;
}

// 消息列表样式
.message-list {
  padding: 0 30rpx;
  margin-bottom: 120rpx;
}

.message-container {
  background: #ffffff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 20rpx rgba(0, 0, 0, 0.06);
}

.message-item {
  display: flex;
  align-items: center;
  padding: 24rpx 30rpx;
  border-bottom: 1px solid #f5f5f5;
  transition: all 0.3s ease;
  position: relative;

  &:last-child {
    border-bottom: none;
  }

  &.unread {
    background: linear-gradient(90deg, rgba(41, 121, 255, 0.02) 0%, rgba(41, 121, 255, 0.05) 100%);

    .message-title {
      font-weight: 600;
    }
  }

  &:active {
    background: #f8f9fa;
    transform: scale(0.98);
  }
}

.message-avatar {
  position: relative;
  margin-right: 24rpx;
}

.avatar-wrapper {
  width: 64rpx;
  height: 64rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  &.system-icon {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  }

  &.work-icon {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  }

  &.approval-icon {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  }

  &.notice-icon {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  }
}

.unread-dot {
  position: absolute;
  top: -4rpx;
  right: -4rpx;
  width: 16rpx;
  height: 16rpx;
  background: #ff4757;
  border-radius: 50%;
  border: 2px solid #ffffff;
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12rpx;
}

.message-title {
  font-size: 32rpx;
  font-weight: 500;
  color: #1a1a1a;
  line-height: 1.4;
  flex: 1;
  margin-right: 20rpx;
}

.message-time {
  font-size: 24rpx;
  color: #999;
  white-space: nowrap;
}

.message-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.5;
  margin-bottom: 16rpx;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.message-tags {
  display: flex;
  gap: 12rpx;
  margin-bottom: 8rpx;
}

.message-arrow {
  display: flex;
  align-items: center;
  margin-left: 16rpx;
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  display: flex;
  justify-content: space-around;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
  z-index: 9;
}

.popup-content {
  padding: 40rpx 20rpx;
}

.popup-title {
  text-align: center;
  font-size: 32rpx;
  font-weight: 600;
  margin-bottom: 40rpx;
  color: #303133;
}

.popup-grid-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.popup-grid-text {
  font-size: 26rpx;
  color: #606266;
  margin-top: 10rpx;
}

.popup-cancel {
  margin-top: 40rpx;
  padding: 0 30rpx;
}

.search-popup {
  width: 600rpx;
  padding: 30rpx;
}
</style> 