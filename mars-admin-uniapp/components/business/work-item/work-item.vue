<template>
  <view class="work-item" @click="handleClick">
    <!-- 用户头像 -->
    <view class="work-avatar">
      <u-avatar :src="item.avatar" :size="avatarSize"></u-avatar>
    </view>
    
    <!-- 工作信息 -->
    <view class="work-info">
      <view class="work-header">
        <text class="work-name">{{ item.name }}</text>
        <text class="work-time">{{ item.time }}</text>
      </view>
      <view class="work-content">
        <text class="work-desc">{{ item.description }}</text>
      </view>
      <view v-if="item.tags && item.tags.length" class="work-tags">
        <u-tag 
          v-for="(tag, index) in item.tags" 
          :key="index"
          :text="tag.name" 
          :type="tag.type || 'info'"
          size="mini" 
          plain 
          shape="circle"
          :style="{ marginRight: '10rpx' }"
        ></u-tag>
      </view>
    </view>
    
    <!-- 操作按钮 -->
    <view v-if="showAction" class="work-action">
      <u-icon name="arrow-right" size="24" color="#c0c4cc"></u-icon>
    </view>
  </view>
</template>

<script>
export default {
  name: 'WorkItem',
  props: {
    // 工作动态数据
    item: {
      type: Object,
      required: true,
      default: () => ({})
    },
    // 头像大小
    avatarSize: {
      type: [String, Number],
      default: 80
    },
    // 是否显示操作按钮
    showAction: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    handleClick() {
      this.$emit('click', this.item)
    }
  }
}
</script>

<style lang="scss" scoped>
.work-item {
  display: flex;
  align-items: flex-start;
  padding: 30rpx 0;
  border-bottom: 1px solid #f4f4f5;
  transition: background-color 0.3s ease;
  
  &:last-child {
    border-bottom: none;
  }
  
  &:active {
    background-color: #f8f9fa;
  }
  
  .work-avatar {
    margin-right: 20rpx;
  }
  
  .work-info {
    flex: 1;
    min-width: 0;
    
    .work-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 10rpx;
      
      .work-name {
        font-size: 28rpx;
        font-weight: 500;
        color: #303133;
      }
      
      .work-time {
        font-size: 24rpx;
        color: #909399;
        flex-shrink: 0;
        margin-left: 20rpx;
      }
    }
    
    .work-content {
      margin-bottom: 15rpx;
      
      .work-desc {
        font-size: 26rpx;
        color: #606266;
        line-height: 1.5;
        word-break: break-all;
      }
    }
    
    .work-tags {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
    }
  }
  
  .work-action {
    display: flex;
    align-items: center;
    margin-left: 20rpx;
    padding: 10rpx;
  }
}
</style>
