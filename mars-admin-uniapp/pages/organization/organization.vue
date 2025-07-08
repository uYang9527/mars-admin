<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input 
        class="search-input" 
        placeholder="搜索部门或员工" 
        v-model="searchKeyword"
        @input="handleSearch"
      />
      <view class="search-icon">🔍</view>
    </view>

    <!-- 组织架构树 -->
    <view class="org-tree">
      <view class="tree-node" v-for="(dept, index) in filteredDeptList" :key="dept.id">
        <view class="dept-header" @click="toggleDept(dept)">
          <view class="dept-info">
            <text class="expand-icon">{{ dept.expanded ? '▼' : '▶' }}</text>
            <text class="dept-name">{{ dept.name }}</text>
            <text class="dept-count">({{ dept.memberCount }}人)</text>
          </view>
          <view class="dept-actions">
            <text class="action-btn" @click.stop="viewDeptDetail(dept)">详情</text>
          </view>
        </view>
        
        <!-- 部门成员列表 -->
        <view class="dept-members" v-if="dept.expanded">
          <view class="member-item" v-for="member in dept.members" :key="member.id" @click="viewMemberDetail(member)">
            <view class="member-avatar">
              <image :src="member.avatar || 'https://images.unsplash.com/photo-1511367461989-f85a21fda167?w=80&h=80&fit=crop&crop=face&auto=format&q=80'" class="avatar-image"></image>
            </view>
            <view class="member-info">
              <text class="member-name">{{ member.name }}</text>
              <text class="member-position">{{ member.position }}</text>
              <text class="member-phone">{{ member.phone }}</text>
            </view>
            <view class="member-actions">
              <text class="action-btn" @click.stop="callMember(member)">📞</text>
              <text class="action-btn" @click.stop="chatMember(member)">💬</text>
            </view>
          </view>
        </view>

        <!-- 子部门 -->
        <view class="sub-departments" v-if="dept.expanded && dept.children && dept.children.length > 0">
          <view class="sub-dept-item" v-for="subDept in dept.children" :key="subDept.id">
            <view class="sub-dept-header" @click="toggleDept(subDept)">
              <view class="sub-dept-info">
                <text class="expand-icon">{{ subDept.expanded ? '▼' : '▶' }}</text>
                <text class="sub-dept-name">{{ subDept.name }}</text>
                <text class="sub-dept-count">({{ subDept.memberCount }}人)</text>
              </view>
            </view>
            
            <!-- 子部门成员 -->
            <view class="sub-dept-members" v-if="subDept.expanded">
              <view class="member-item" v-for="member in subDept.members" :key="member.id" @click="viewMemberDetail(member)">
                <view class="member-avatar">
                  <image :src="member.avatar || 'https://images.unsplash.com/photo-1511367461989-f85a21fda167?w=80&h=80&fit=crop&crop=face&auto=format&q=80'" class="avatar-image"></image>
                </view>
                <view class="member-info">
                  <text class="member-name">{{ member.name }}</text>
                  <text class="member-position">{{ member.position }}</text>
                  <text class="member-phone">{{ member.phone }}</text>
                </view>
                <view class="member-actions">
                  <text class="action-btn" @click.stop="callMember(member)">📞</text>
                  <text class="action-btn" @click.stop="chatMember(member)">💬</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 统计信息 -->
    <view class="stats-footer">
      <view class="stats-item">
        <text class="stats-label">总部门数</text>
        <text class="stats-value">{{ totalDepts }}</text>
      </view>
      <view class="stats-item">
        <text class="stats-label">总员工数</text>
        <text class="stats-value">{{ totalMembers }}</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchKeyword: '',
      deptList: [
        {
          id: 1,
          name: '总经理办公室',
          memberCount: 5,
          expanded: false,
          members: [
            {
              id: 1,
              name: '张总',
              position: '总经理',
              phone: '138****8888',
              avatar: 'https://images.unsplash.com/photo-1560250097-0b93528c311a?w=80&h=80&fit=crop&crop=face&auto=format&q=80'
            },
            {
              id: 2,
              name: '李秘书',
              position: '总经理秘书',
              phone: '138****8889',
              avatar: 'https://images.unsplash.com/photo-1494790108755-2616b612b786?w=80&h=80&fit=crop&crop=face&auto=format&q=80'
            }
          ]
        },
        {
          id: 2,
          name: '技术部',
          memberCount: 15,
          expanded: false,
          members: [
            {
              id: 3,
              name: '王工',
              position: '技术总监',
              phone: '138****8890',
              avatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=80&h=80&fit=crop&crop=face&auto=format&q=80'
            },
            {
              id: 4,
              name: '赵工',
              position: '高级工程师',
              phone: '138****8891',
              avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=80&h=80&fit=crop&crop=face&auto=format&q=80'
            }
          ],
          children: [
            {
              id: 21,
              name: '前端开发组',
              memberCount: 8,
              expanded: false,
              members: [
                {
                  id: 5,
                  name: '前端小李',
                  position: '前端工程师',
                  phone: '138****8892',
                  avatar: 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=80&h=80&fit=crop&crop=face&auto=format&q=80'
                }
              ]
            },
            {
              id: 22,
              name: '后端开发组',
              memberCount: 7,
              expanded: false,
              members: [
                {
                  id: 6,
                  name: '后端小王',
                  position: '后端工程师',
                  phone: '138****8893',
                  avatar: 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=80&h=80&fit=crop&crop=face&auto=format&q=80'
                }
              ]
            }
          ]
        },
        {
          id: 3,
          name: '销售部',
          memberCount: 12,
          expanded: false,
          members: [
            {
              id: 7,
              name: '销售经理',
              position: '销售总监',
              phone: '138****8894',
              avatar: 'https://images.unsplash.com/photo-1519345182560-3f2917c472ef?w=80&h=80&fit=crop&crop=face&auto=format&q=80'
            }
          ]
        },
        {
          id: 4,
          name: '人事部',
          memberCount: 6,
          expanded: false,
          members: [
            {
              id: 8,
              name: '人事小张',
              position: '人事专员',
              phone: '138****8895',
              avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=80&h=80&fit=crop&crop=face&auto=format&q=80'
            }
          ]
        },
        {
          id: 5,
          name: '财务部',
          memberCount: 4,
          expanded: false,
          members: [
            {
              id: 9,
              name: '财务小陈',
              position: '会计',
              phone: '138****8896',
              avatar: 'https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=80&h=80&fit=crop&crop=face&auto=format&q=80'
            }
          ]
        }
      ]
    }
  },
  computed: {
    filteredDeptList() {
      if (!this.searchKeyword) {
        return this.deptList;
      }
      return this.deptList.filter(dept => {
        return dept.name.includes(this.searchKeyword) || 
               dept.members.some(member => member.name.includes(this.searchKeyword));
      });
    },
    totalDepts() {
      let count = this.deptList.length;
      this.deptList.forEach(dept => {
        if (dept.children) {
          count += dept.children.length;
        }
      });
      return count;
    },
    totalMembers() {
      let count = 0;
      this.deptList.forEach(dept => {
        count += dept.memberCount;
      });
      return count;
    }
  },
  onLoad() {
    this.loadOrgData();
  },
  methods: {
    loadOrgData() {
      // 模拟加载组织架构数据
      console.log('加载组织架构数据');
    },
    handleSearch() {
      // 搜索逻辑
      console.log('搜索关键词:', this.searchKeyword);
    },
    toggleDept(dept) {
      dept.expanded = !dept.expanded;
      this.$forceUpdate();
    },
    viewDeptDetail(dept) {
      uni.navigateTo({
        url: `/pages/organization/dept-detail?id=${dept.id}`
      });
    },
    viewMemberDetail(member) {
      uni.navigateTo({
        url: `/pages/organization/member-detail?id=${member.id}`
      });
    },
    callMember(member) {
      uni.makePhoneCall({
        phoneNumber: member.phone.replace(/\*/g, '0')
      });
    },
    chatMember(member) {
      uni.showToast({
        title: `发起与${member.name}的聊天`,
        icon: 'none'
      });
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20rpx;
  background: transparent;
  min-height: 100vh;
}

/* 搜索栏样式 - 精致版 */
.search-bar {
  position: relative;
  margin-bottom: 24rpx;
}

.search-input {
  width: 100%;
  height: 88rpx;
  padding: 0 60rpx 0 24rpx;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10rpx);
  border-radius: 44rpx;
  font-size: 28rpx;
  border: 1rpx solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.search-input:focus {
  box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.2);
  border-color: #667eea;
}

.search-icon {
  position: absolute;
  right: 24rpx;
  top: 50%;
  transform: translateY(-50%);
  font-size: 32rpx;
  color: #667eea;
}

/* 组织架构树样式 - 精致版 */
.org-tree {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10rpx);
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
  border: 1rpx solid rgba(255, 255, 255, 0.2);
}

.tree-node {
  border-bottom: 1rpx solid rgba(240, 240, 240, 0.5);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.tree-node:last-child {
  border-bottom: none;
}

.tree-node:hover {
  background: rgba(102, 126, 234, 0.05);
}

/* 部门头部样式 - 精致版 */
.dept-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx 24rpx;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.dept-header:hover {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.15) 0%, rgba(118, 75, 162, 0.15) 100%);
}

.dept-info {
  display: flex;
  align-items: center;
}

.expand-icon {
  font-size: 24rpx;
  color: #666666;
  margin-right: 10rpx;
  width: 30rpx;
}

.dept-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333333;
  margin-right: 10rpx;
}

.dept-count {
  font-size: 24rpx;
  color: #999999;
}

.dept-actions {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  font-size: 28rpx;
  color: #007AFF;
  padding: 10rpx 20rpx;
}

/* 部门成员样式 */
.dept-members {
  padding-left: 40rpx;
}

.member-item {
  display: flex;
  align-items: center;
  padding: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.member-item:last-child {
  border-bottom: none;
}

.member-avatar {
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

.member-info {
  flex: 1;
}

.member-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #333333;
  display: block;
  margin-bottom: 8rpx;
}

.member-position {
  font-size: 24rpx;
  color: #666666;
  display: block;
  margin-bottom: 8rpx;
}

.member-phone {
  font-size: 24rpx;
  color: #999999;
}

.member-actions {
  display: flex;
  gap: 20rpx;
}

/* 子部门样式 */
.sub-departments {
  padding-left: 40rpx;
  background-color: #fafafa;
}

.sub-dept-item {
  border-bottom: 1rpx solid #f0f0f0;
}

.sub-dept-header {
  padding: 20rpx;
}

.sub-dept-info {
  display: flex;
  align-items: center;
}

.sub-dept-name {
  font-size: 28rpx;
  color: #333333;
  margin-right: 10rpx;
}

.sub-dept-count {
  font-size: 24rpx;
  color: #999999;
}

.sub-dept-members {
  padding-left: 40rpx;
}

/* 统计信息样式 */
.stats-footer {
  display: flex;
  justify-content: space-around;
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-top: 20rpx;
}

.stats-item {
  text-align: center;
}

.stats-label {
  font-size: 24rpx;
  color: #666666;
  display: block;
  margin-bottom: 10rpx;
}

.stats-value {
  font-size: 36rpx;
  font-weight: bold;
  color: #007AFF;
}
</style> 