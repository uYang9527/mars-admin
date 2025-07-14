<template>
  <view class="page-container">
    <!-- uView官方导航栏 -->
    <u-navbar
      title="组织架构"
      :bgColor="navbarConfig.background"
      :fixed="true"
      :placeholder="true"
      :safeAreaInsetTop="true"
      :titleStyle="{ color: '#ffffff' }"
    >
      <template #right>
        <view class="navbar-right">
          <u-icon name="grid" size="28" color="#ffffff" @click="switchViewMode"></u-icon>
        </view>
      </template>
    </u-navbar>

    <!-- 页面内容 -->
    <view class="page-content">
      <!-- 搜索和筛选 -->
      <view class="search-section">
        <view class="search-container">
          <u-search
            v-model="searchKeyword"
            placeholder="搜索部门或员工"
            :showAction="false"
            height="44"
            animation
            shape="round"
            bgColor="#f8f9fa"
            borderColor="transparent"
            clearabled
            @change="handleSearch"
          >
            <template #prefix>
              <u-icon name="search" size="18" color="#999"></u-icon>
            </template>
          </u-search>
        </view>

        <view class="filter-tabs">
          <view
            v-for="(tab, index) in viewTabs"
            :key="index"
            class="filter-tab"
            :class="{ active: viewMode === tab.value }"
            @click="switchViewMode(tab.value)"
          >
            <u-icon :name="tab.icon" size="16" :color="viewMode === tab.value ? '#ffffff' : '#666'"></u-icon>
            <text class="tab-text">{{ tab.label }}</text>
          </view>
        </view>
      </view>

      <!-- 内容主体 -->
      <view class="content-main">
        <!-- 组织树视图 -->
        <view class="org-tree" v-if="viewMode === 'tree'">
          <u-empty
            v-if="filteredDepts.length === 0"
            icon="search"
            text="未找到相关部门"
            marginTop="100"
            marginBottom="100"
          ></u-empty>
          
          <view v-else class="dept-list">
            <view 
              v-for="dept in filteredDepts" 
              :key="dept.id"
              class="dept-item"
            >
              <!-- 部门标题 -->
              <view class="dept-header" @click="toggleDept(dept)">
                <view class="dept-title-box">
                  <u-icon name="home" size="32" color="#2979ff"></u-icon>
                  <text class="dept-title">{{ dept.name }}</text>
                  <text class="dept-count">({{ dept.memberCount }}人)</text>
                </view>
                <view class="dept-actions">
                  <u-button text="详情" type="primary" size="mini" plain shape="circle" @click.stop="viewDeptDetail(dept)"></u-button>
                  <u-icon 
                    :name="dept.expanded ? 'arrow-up' : 'arrow-down'" 
                    size="24" 
                    color="#909399"
                    style="margin-left: 20rpx;"
                  ></u-icon>
                </view>
              </view>
              
              <!-- 部门成员列表 -->
              <view class="dept-members" v-if="dept.expanded">
                <!-- 直属成员 -->
                <view 
                  v-for="member in dept.members" 
                  :key="member.id"
                  class="member-item"
                  @click="viewMemberDetail(member)"
                >
                  <view class="member-info">
                    <u-avatar :src="member.avatar || defaultAvatar" size="80"></u-avatar>
                    <view class="member-details">
                      <view class="member-name">{{ member.name }}</view>
                      <view class="member-position">{{ member.position }}</view>
                    </view>
                  </view>
                  <view class="member-actions">
                    <u-icon name="phone" size="30" color="#19be6b" @click.stop="callMember(member)"></u-icon>
                    <u-icon name="chat" size="30" color="#2979ff" @click.stop="chatMember(member)" style="margin-left: 20rpx;"></u-icon>
                  </view>
                </view>
                
                <!-- 子部门 -->
                <view v-if="dept.children && dept.children.length > 0" class="sub-depts">
                  <view 
                    v-for="subDept in dept.children" 
                    :key="subDept.id"
                    class="sub-dept-item"
                  >
                    <!-- 子部门标题 -->
                    <view class="sub-dept-header" @click="toggleDept(subDept)">
                      <view class="dept-title-box">
                        <u-icon name="folder" size="28" color="#ff9900"></u-icon>
                        <text class="dept-title">{{ subDept.name }}</text>
                        <text class="dept-count">({{ subDept.memberCount }}人)</text>
                      </view>
                      <u-icon 
                        :name="subDept.expanded ? 'arrow-up' : 'arrow-down'" 
                        size="22" 
                        color="#909399"
                      ></u-icon>
                    </view>
                    
                    <!-- 子部门成员 -->
                    <view class="sub-dept-members" v-if="subDept.expanded">
                      <view 
                        v-for="member in subDept.members" 
                        :key="member.id"
                        class="member-item sub-member"
                        @click="viewMemberDetail(member)"
                      >
                        <view class="member-info">
                          <u-avatar :src="member.avatar || defaultAvatar" size="70"></u-avatar>
                          <view class="member-details">
                            <view class="member-name">{{ member.name }}</view>
                            <view class="member-position">{{ member.position }}</view>
                          </view>
                        </view>
                        <view class="member-actions">
                          <u-icon name="phone" size="26" color="#19be6b" @click.stop="callMember(member)"></u-icon>
                          <u-icon name="chat" size="26" color="#2979ff" @click.stop="chatMember(member)" style="margin-left: 16rpx;"></u-icon>
                        </view>
                      </view>
                    </view>
                  </view>
                </view>
              </view>
            </view>
          </view>
        </view>
        
        <!-- 卡片视图 -->
        <view class="org-cards" v-if="viewMode === 'card'">
          <u-empty
            v-if="filteredDepts.length === 0"
            icon="search"
            text="未找到相关部门"
            marginTop="100"
            marginBottom="100"
          ></u-empty>
          
          <template v-else>
            <!-- 部门列表 -->
            <view class="section-title">部门列表</view>
            <view class="dept-cards">
              <view 
                v-for="dept in filteredDepts" 
                :key="dept.id"
                class="dept-card-wrapper"
              >
                <u-cell-group>
                  <u-cell
                    :title="dept.name"
                    :label="`${dept.memberCount}人`"
                    :isLink="true"
                    @click="toggleDept(dept)"
                  >
                    <template #icon>
                      <image :src="deptIcon" style="width: 80rpx; height: 80rpx;" />
                    </template>
                  </u-cell>
                </u-cell-group>

                <!-- 成员列表 -->
                <view v-if="dept.expanded">
                  <u-cell-group v-for="member in dept.members" :key="member.id">
                    <u-cell @click="viewMemberDetail(member)">
                      <template #icon>
                        <view class="member-card-head">
                          <u-avatar :src="member.avatar || defaultAvatar" size="100"></u-avatar>
                          <view class="member-card-info">
                            <view class="member-name">{{ member.name }}</view>
                            <view class="member-position">{{ member.position }}</view>
                          </view>
                        </view>
                      </template>
                      <template #label>
                        <view class="card-body">
                          <view class="card-item">
                            <u-icon name="phone" size="24" color="#909399"></u-icon>
                            <text class="card-value">{{ member.phone }}</text>
                          </view>
                          <view class="card-item" v-if="member.email">
                            <u-icon name="email" size="24" color="#909399"></u-icon>
                            <text class="card-value">{{ member.email }}</text>
                          </view>
                        </view>
                      </template>
                    </u-cell>
                  </u-cell-group>
                </view>
              </view>
            </view>
            
            <!-- 员工列表 -->
            <template v-if="expandedDept">
              <view class="section-title">{{ expandedDept.name }}的成员</view>
              <view class="member-cards">
                <view 
                  v-for="member in expandedDept.members" 
                  :key="member.id"
                  class="member-card-wrapper"
                >
                  <u-cell-group>
                    <u-cell @click="viewMemberDetail(member)">
                      <template #icon>
                        <view class="member-card-head">
                          <u-avatar :src="member.avatar || defaultAvatar" size="100"></u-avatar>
                          <view class="member-card-info">
                            <view class="member-name">{{ member.name }}</view>
                            <view class="member-position">{{ member.position }}</view>
                          </view>
                        </view>
                      </template>
                      <template #label>
                        <view class="card-body">
                          <view class="card-item">
                            <u-icon name="phone" size="24" color="#909399"></u-icon>
                            <text class="card-value">{{ member.phone }}</text>
                          </view>
                          <view class="card-item" v-if="member.email">
                            <u-icon name="email" size="24" color="#909399"></u-icon>
                            <text class="card-value">{{ member.email }}</text>
                          </view>
                        </view>
                      </template>
                      <template #right>
                        <view class="card-actions">
                          <u-button text="打电话" type="success" size="mini" icon="phone" @click.stop="callMember(member)"></u-button>
                          <u-button text="发消息" type="primary" size="mini" icon="chat" @click.stop="chatMember(member)" style="margin-left: 10rpx;"></u-button>
                        </view>
                      </template>
                    </u-cell>
                  </u-cell-group>
                </view>
              </view>
            </template>
          </template>
        </view>
      </view>

      <!-- 统计信息 -->
      <view class="stats-footer card">
        <u-grid :border="false" :col="2">
          <u-grid-item>
            <view class="stats-box">
              <text class="stats-label">总部门数</text>
              <text class="stats-value">{{ totalDepts }}</text>
            </view>
          </u-grid-item>
          <u-grid-item>
            <view class="stats-box">
              <text class="stats-label">总员工数</text>
              <text class="stats-value">{{ totalMembers }}</text>
            </view>
          </u-grid-item>
        </u-grid>
      </view>
    </view>
    
    <!-- 员工详情弹窗 -->
    <u-popup
      :show="showMemberPopup"
      mode="center"
      round="12"
      @close="closeMemberPopup"
      closeOnClickOverlay
      width="80%"
    >
      <view class="member-popup">
        <view class="member-popup-header">
          <u-avatar :src="currentMember.avatar || defaultAvatar" size="150"></u-avatar>
          <view class="member-popup-info">
            <view class="member-popup-name">{{ currentMember.name }}</view>
            <view class="member-popup-position">{{ currentMember.position }}</view>
          </view>
        </view>
        
        <u-gap height="20"></u-gap>
        
        <view class="member-popup-details">
          <u-cell-group>
            <u-cell icon="home" title="所属部门" :value="currentMember.department || '未指定'"></u-cell>
            <u-cell icon="phone" title="联系电话" :value="currentMember.phone || '未设置'" @click="callMember(currentMember)" isLink></u-cell>
            <u-cell icon="email" title="电子邮箱" :value="currentMember.email || '未设置'" isLink></u-cell>
            <u-cell icon="calendar" title="入职时间" :value="currentMember.joinDate || '未记录'"></u-cell>
          </u-cell-group>
        </view>
        
        <u-gap height="30"></u-gap>
        
        <view class="member-popup-actions">
          <u-button type="primary" text="发送消息" icon="chat" @click="chatMember(currentMember)"></u-button>
          <u-button type="success" text="拨打电话" icon="phone" @click="callMember(currentMember)" style="margin-left: 20rpx;"></u-button>
        </view>
      </view>
    </u-popup>

    <!-- 部门详情弹窗 -->
    <u-popup
      :show="showDeptPopup"
      mode="center"
      round="12"
      @close="closeDeptPopup"
      closeOnClickOverlay
      width="80%"
    >
      <view class="dept-popup">
        <view class="dept-popup-title">{{ currentDept.name }}</view>
        
        <u-gap height="20"></u-gap>
        
        <view class="dept-popup-details">
          <u-cell-group>
            <u-cell icon="account" title="部门主管" :value="getDeptManager(currentDept) || '未指定'"></u-cell>
            <u-cell icon="man-add" title="部门人数" :value="`${currentDept.memberCount || 0}人`"></u-cell>
            <u-cell icon="map" title="办公地点" :value="currentDept.location || '未设置'"></u-cell>
            <u-cell icon="phone" title="部门电话" :value="currentDept.phone || '未设置'"></u-cell>
          </u-cell-group>
          
          <view class="dept-desc">
            <view class="desc-title">部门简介：</view>
            <view class="desc-content">{{ currentDept.description || '暂无部门介绍' }}</view>
          </view>
        </view>
        
        <u-gap height="30"></u-gap>
        
        <view class="dept-popup-actions">
          <u-button type="primary" text="查看成员" icon="account" @click="showDeptMembers"></u-button>
          <u-button type="info" text="组织关系" icon="list" style="margin-left: 20rpx;"></u-button>
        </view>
      </view>
    </u-popup>

    <u-safe-bottom></u-safe-bottom>
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
        background: 'linear-gradient(45deg, #2979ff, #007AFF)'
      },
      searchKeyword: '',
      viewMode: 'tree', // tree or card
      viewTabs: [
        { label: '树形', value: 'tree', icon: 'list' },
        { label: '卡片', value: 'card', icon: 'grid' }
      ],
      defaultAvatar: 'https://cdn.uviewui.com/uview/avatar/1.jpg',
      deptIcon: 'https://img.icons8.com/color/96/000000/department.png',
      expandedDept: null,
      showMemberPopup: false,
      currentMember: {},
      showDeptPopup: false,
      currentDept: {},
      departments: []
    }
  },
  computed: {
    filteredDepts() {
      if (!this.searchKeyword) {
        return this.departments;
      }
      
      const keyword = this.searchKeyword.toLowerCase();
      const results = [];
      
      // 搜索部门
      for (const dept of this.departments) {
        // 检查部门名称
        const matchesDept = dept.name.toLowerCase().includes(keyword);
        
        // 检查部门成员
        const matchesMembers = dept.members && dept.members.some(member => 
          member.name.toLowerCase().includes(keyword) ||
          member.position.toLowerCase().includes(keyword)
        );
        
        // 检查子部门
        let matchesSubDepts = false;
        let matchingChildren = [];
        
        if (dept.children && dept.children.length > 0) {
          matchingChildren = dept.children.filter(subDept => 
            subDept.name.toLowerCase().includes(keyword) ||
            (subDept.members && subDept.members.some(member => 
              member.name.toLowerCase().includes(keyword) ||
              member.position.toLowerCase().includes(keyword)
            ))
          );
          
          matchesSubDepts = matchingChildren.length > 0;
        }
        
        // 如果匹配，添加到结果中
        if (matchesDept || matchesMembers || matchesSubDepts) {
          // 创建一个副本以避免修改原始数据
          const deptCopy = JSON.parse(JSON.stringify(dept));
          
          // 如果是在子部门或子部门的成员中匹配的，只保留匹配的子部门
          if (matchesSubDepts && !matchesDept && !matchesMembers) {
            deptCopy.children = matchingChildren;
          }
          
          results.push(deptCopy);
        }
      }
      
      return results;
    },
    totalDepts() {
      let count = this.departments.length;
      
      for (const dept of this.departments) {
        if (dept.children) {
          count += dept.children.length;
        }
      }
      
      return count;
    },
    totalMembers() {
      let count = 0;
      
      for (const dept of this.departments) {
        count += dept.members ? dept.members.length : 0;
        
        if (dept.children) {
          for (const subDept of dept.children) {
            count += subDept.members ? subDept.members.length : 0;
          }
        }
      }
      
      return count;
    }
  },
  methods: {
    // 重写页面混入的loadPageData方法
    async loadPageData() {
      try {
        this.showLoading('加载组织架构...');

        // 这里可以调用API获取组织架构数据
        // const res = await api.organization.getDepartmentList();

        // 暂时使用mock数据
        const { departmentList } = await import('@/mock/index.js');
        this.departments = JSON.parse(JSON.stringify(departmentList));

      } catch (error) {
        console.error('加载组织架构失败:', error);
        this.showError('加载组织架构失败');
      } finally {
        this.hideLoading();
      }
    },
    handleSearch(value) {
      // 搜索功能已通过计算属性实现
      console.log('搜索关键字：', value);
    },
    switchViewMode(mode) {
      if (mode) {
        this.viewMode = mode;
      } else {
        this.viewMode = this.viewMode === 'tree' ? 'card' : 'tree';
      }

      const modeText = this.viewMode === 'tree' ? '树状视图' : '卡片视图';
      uni.showToast({
        title: `已切换到${modeText}`,
        icon: 'none'
      });
    },
    toggleDept(dept) {
      // 切换部门展开/折叠状态
      dept.expanded = !dept.expanded;
      
      // 在卡片视图下，设置当前展开的部门
      if (this.viewMode === 'card') {
        this.expandedDept = dept.expanded ? dept : null;
      }
    },
    getDeptManager(dept) {
      if (!dept.members || dept.members.length === 0) {
        return '未指定';
      }
      
      // 查找职位中包含 '主管'、'经理'、'总监' 等关键词的成员
      const manager = dept.members.find(member => 
        member.position.includes('主管') || 
        member.position.includes('经理') || 
        member.position.includes('总监') ||
        member.position.includes('总裁') ||
        member.position.includes('负责人')
      );
      
      return manager ? manager.name : dept.members[0].name;
    },
    viewMemberDetail(member) {
      this.currentMember = member;
      this.showMemberPopup = true;
    },
    closeMemberPopup() {
      this.showMemberPopup = false;
    },
    viewDeptDetail(dept) {
      this.currentDept = dept;
      this.showDeptPopup = true;
    },
    closeDeptPopup() {
      this.showDeptPopup = false;
    },
    showDeptMembers() {
      this.closeDeptPopup();
      
      // 在树状视图中展开部门
      if (this.viewMode === 'tree') {
        const dept = this.departments.find(d => d.id === this.currentDept.id);
        if (dept) {
          dept.expanded = true;
        }
      } 
      // 在卡片视图中显示成员
      else {
        this.expandedDept = this.currentDept;
      }
    },
    callMember(member) {
      uni.makePhoneCall({
        phoneNumber: member.phone,
        fail: () => {
          uni.showToast({
            title: '拨打电话失败',
            icon: 'none'
          });
        }
      });
    },
    chatMember(member) {
      uni.showToast({
        title: `与${member.name}发起对话`,
        icon: 'none'
      });
      
      // 实际项目中可以跳转到聊天页面
      // uni.navigateTo({
      //   url: `/pages/chat/chat?userId=${member.id}&userName=${member.name}`
      // });
    }
  }
}
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background-color: #f5f6fa;
}

.page-content {
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
  min-height: calc(100vh - 88rpx);
}

.navbar-right {
  display: flex;
  align-items: center;
  padding: 10rpx;
}

// 搜索和筛选区域
.search-section {
  padding: 20rpx 30rpx 30rpx;
}

.search-container {
  background: #ffffff;
  border-radius: 24rpx;
  padding: 8rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
  margin-bottom: 20rpx;
}

.filter-tabs {
  display: flex;
  background: #ffffff;
  border-radius: 16rpx;
  padding: 8rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.filter-tab {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16rpx 20rpx;
  border-radius: 12rpx;
  transition: all 0.3s ease;

  &.active {
    background: #2979ff;

    .tab-text {
      color: #ffffff;
      font-weight: 600;
    }
  }
}

.tab-text {
  font-size: 26rpx;
  color: #666;
  margin-left: 8rpx;
  transition: all 0.3s ease;
}

.content-main {
  padding: 0 30rpx;
  margin-bottom: 120rpx;
}

/* 树状视图样式 */
.dept-item {
  background: #ffffff;
  border-radius: 20rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 20rpx rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.98);
  }
}

.dept-header {
  padding: 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
  border-bottom: 1px solid #f0f0f0;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    left: 30rpx;
    top: 0;
    bottom: 0;
    width: 6rpx;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 3rpx;
  }
}

.dept-title-box {
  display: flex;
  align-items: center;
  margin-left: 20rpx;
}

.dept-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #1a1a1a;
  margin-left: 16rpx;
}

.dept-count {
  font-size: 26rpx;
  color: #909399;
  margin-left: 10rpx;
}

.dept-actions {
  display: flex;
  align-items: center;
}

.dept-members {
  padding: 0 20rpx;
}

.member-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  border-bottom: 1px solid #f4f4f5;
}

.member-item:last-child {
  border-bottom: none;
}

.member-info {
  display: flex;
  align-items: center;
}

.member-details {
  margin-left: 20rpx;
}

.member-name {
  font-size: 30rpx;
  font-weight: 500;
  color: #303133;
}

.member-position {
  font-size: 26rpx;
  color: #606266;
  margin-top: 8rpx;
}

.member-actions {
  display: flex;
}

.sub-depts {
  padding: 10rpx 0;
  background-color: #f8f9fc;
}

.sub-dept-item {
  margin: 10rpx 20rpx;
  background-color: #ffffff;
  border-radius: 8rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.03);
}

.sub-dept-header {
  padding: 20rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ebeef5;
}

.sub-dept-members {
  padding: 0 10rpx;
}

.sub-member {
  padding: 16rpx;
}

/* 卡片视图样式 */
.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #303133;
  margin: 30rpx 10rpx 20rpx 10rpx;
}

.dept-cards {
  display: flex;
  flex-wrap: wrap;
}

.dept-card-wrapper {
  width: 100%;
  padding: 10rpx;
  box-sizing: border-box;
  margin-bottom: 10rpx;
}

.member-cards {
  display: flex;
  flex-wrap: wrap;
}

.member-card-wrapper {
  width: 100%;
  padding: 10rpx;
  box-sizing: border-box;
  margin-bottom: 10rpx;
}

.card-body {
  padding: 10rpx 0;
}

.card-item {
  display: flex;
  align-items: center;
  margin: 10rpx 0;
}

.card-label {
  font-size: 28rpx;
  color: #606266;
}

.card-value {
  font-size: 28rpx;
  color: #303133;
  margin-left: 10rpx;
}

.card-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16rpx;
}

.member-card-head {
  display: flex;
  align-items: center;
  padding: 10rpx;
}

.member-card-info {
  margin-left: 20rpx;
}

/* 统计信息 */
.stats-footer {
  margin: 20rpx 30rpx;
  border-radius: 12rpx;
}

.stats-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.stats-value {
  font-size: 36rpx;
  font-weight: 600;
  color: #2979ff;
  margin-top: 10rpx;
}

.stats-label {
  font-size: 26rpx;
  color: #909399;
}

/* 弹窗样式 */
.member-popup {
  padding: 40rpx;
}

.member-popup-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 30rpx;
}

.member-popup-info {
  margin-top: 20rpx;
  text-align: center;
}

.member-popup-name {
  font-size: 36rpx;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10rpx;
}

.member-popup-position {
  font-size: 30rpx;
  color: #606266;
}

.member-popup-details {
  margin-bottom: 20rpx;
}

.member-popup-actions {
  display: flex;
  justify-content: center;
}

.dept-popup {
  padding: 40rpx;
}

.dept-popup-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #303133;
  text-align: center;
  margin-bottom: 30rpx;
  padding-bottom: 20rpx;
  border-bottom: 1px solid #ebeef5;
}

.dept-desc {
  margin-top: 20rpx;
  padding: 16rpx;
  background-color: #f8f9fc;
  border-radius: 8rpx;
}

.desc-title {
  font-size: 30rpx;
  font-weight: 500;
  color: #303133;
  margin-bottom: 10rpx;
}

.desc-content {
  font-size: 28rpx;
  color: #606266;
  line-height: 1.6;
}

.dept-popup-actions {
  display: flex;
  justify-content: center;
}

/* 媒体查询，平板或更大屏幕上显示多列 */
@media screen and (min-width: 768px) {
  .dept-card-wrapper {
    width: 50%;
  }
  
  .member-card-wrapper {
    width: 50%;
  }
}
</style> 