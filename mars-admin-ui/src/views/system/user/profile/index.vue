<template>
  <div class="profile-container">
    <!-- 上部个人信息 -->
    <el-card class="profile-card info-card" shadow="hover">
      <div class="profile-header">
        <h3 class="profile-title">个人信息</h3>
      </div>
      <div class="profile-content">
        <div class="profile-main">
          <div class="avatar-section">
            <userAvatar />
            <h2 class="user-name">{{ state.user.userName }}</h2>
          </div>
          <div class="info-grid">
            <div class="info-item">
              <div class="info-label">
                <svg-icon icon-class="phone" />
                <span>手机号码</span>
              </div>
              <div class="info-value">{{ state.user.phonenumber || '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <svg-icon icon-class="email" />
                <span>用户邮箱</span>
              </div>
              <div class="info-value">{{ state.user.email || '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <svg-icon icon-class="tree" />
                <span>所属部门</span>
              </div>
              <div class="info-value" v-if="state.user.deptName">{{ state.user.deptName }} / {{ state.postGroup }}</div>
              <div class="info-value" v-else>未设置</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <svg-icon icon-class="peoples" />
                <span>所属角色</span>
              </div>
              <div class="info-value">{{ state.roleGroup || '未设置' }}</div>
            </div>
            <div class="info-item">
              <div class="info-label">
                <svg-icon icon-class="date" />
                <span>创建日期</span>
              </div>
              <div class="info-value">{{ state.user.createTime || '未知' }}</div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 下部账户管理 -->
    <el-card class="profile-card tab-card" shadow="hover">
      <div class="tab-header">
        <h3 class="tab-title">账号管理</h3>
      </div>
      <el-tabs v-model="activeTab" class="profile-tabs">
        <el-tab-pane label="基本资料" name="userinfo">
          <userInfo :user="userForm" />
        </el-tab-pane>
        <el-tab-pane label="修改密码" name="resetPwd">
          <resetPwd />
        </el-tab-pane>

        <el-tab-pane label="在线设备" name="onlineDevice">
          <onlineDevice :devices="state.devices" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup name="Profile" lang="ts">
import UserAvatar from './userAvatar.vue';
import UserInfo from './userInfo.vue';
import ResetPwd from './resetPwd.vue';
import ThirdParty from './thirdParty.vue';
import OnlineDevice from './onlineDevice.vue';
import { getAuthList } from '@/api/system/social/auth';
import { getUserProfile } from '@/api/system/user';
import { getOnline } from '@/api/monitor/online';
import { UserVO } from '@/api/system/user/types';

const activeTab = ref('userinfo');
interface State {
  user: Partial<UserVO>;
  roleGroup: string;
  postGroup: string;
  auths: any;
  devices: any;
}
const state = ref<State>({
  user: {},
  roleGroup: '',
  postGroup: '',
  auths: [],
  devices: []
});

const userForm = ref({});

const getUser = async () => {
  const res = await getUserProfile();
  state.value.user = res.data.user;
  userForm.value = { ...res.data.user };
  state.value.roleGroup = res.data.roleGroup;
  state.value.postGroup = res.data.postGroup;
};

const getAuths = async () => {
  const res = await getAuthList();
  state.value.auths = res.data;
};
const getOnlines = async () => {
  const res = await getOnline();
  state.value.devices = res.rows;
};

onMounted(() => {
  getUser();
  getAuths();
  getOnlines();
});
</script>

<style scoped>
.profile-container {
  padding: 20px;
  background-color: var(--el-bg-color-page, #f5f7fa);
  min-height: calc(100vh - 120px);
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.profile-card {
  border-radius: 8px;
  transition: all 0.3s;
  margin-bottom: 0;
}

.info-card {
  margin-bottom: 0;
}

.profile-header, .tab-header {
  margin-bottom: 16px;
  border-bottom: 1px solid var(--el-border-color-lighter);
  padding-bottom: 12px;
}

.profile-title, .tab-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.profile-content {
  padding: 0 8px;
}

.profile-main {
  display: flex;
  align-items: flex-start;
  gap: 40px;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 150px;
}

.user-name {
  margin: 12px 0 0;
  font-size: 18px;
  font-weight: 600;
  text-align: center;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px 40px;
  flex: 1;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-label {
  display: flex;
  align-items: center;
  color: var(--el-text-color-secondary);
}

.info-label svg-icon {
  margin-right: 8px;
  font-size: 16px;
}

.info-label span {
  margin-left: 8px;
}

.info-value {
  font-weight: 500;
  color: var(--el-text-color-primary);
  font-size: 15px;
}

.profile-tabs {
  margin-top: 8px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .profile-container {
    padding: 12px;
    gap: 15px;
  }

  .profile-main {
    flex-direction: column;
    align-items: center;
    gap: 20px;
  }

  .info-grid {
    grid-template-columns: 1fr;
    width: 100%;
  }
}
</style>
