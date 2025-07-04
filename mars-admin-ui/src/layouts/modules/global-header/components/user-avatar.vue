<script setup lang="ts">
import { ref } from 'vue';
import { Modal } from 'ant-design-vue';
import { useAuthStore } from '@/store/modules/auth';
import { useRouterPush } from '@/hooks/common/router';
import { $t } from '@/locales';
import UserProfileDrawer from '@/views/user-center/modules/user-profile-drawer.vue';

defineOptions({
  name: 'UserAvatar'
});

const authStore = useAuthStore();
const { toLogin } = useRouterPush();

// 个人中心抽屉状态
const profileDrawerVisible = ref(false);

function loginOrRegister() {
  toLogin();
}

function showUserProfile() {
  profileDrawerVisible.value = true;
}

function logout() {
  Modal.confirm({
    title: $t('common.tip'),
    content: $t('common.logoutConfirm'),
    okText: $t('common.confirm'),
    cancelText: $t('common.cancel'),
    onOk: () => {
      authStore.resetStore();
    }
  });
}

function onProfileRefresh() {
  // 个人信息更新后的回调，可以在这里刷新用户信息
  console.log('个人信息已更新');
}
</script>

<template>
  <div>
  <AButton v-if="!authStore.isLogin" @click="loginOrRegister">{{ $t('page.login.common.loginOrRegister') }}</AButton>
  <ADropdown v-else placement="bottomRight" trigger="click">
    <ButtonIcon>
      <template v-if="authStore.userInfo.avatar">
        <AAvatar :src="authStore.userInfo.avatar" :size="32" />
      </template>
      <template v-else>
        <SvgIcon icon="ph:user-circle" class="text-icon-large" />
      </template>
      <span class="text-16px font-medium">{{ authStore.userInfo.username }}</span>
    </ButtonIcon>
    <template #overlay>
      <AMenu>
          <AMenuItem @click="showUserProfile">
          <div class="flex-center gap-8px">
            <SvgIcon icon="ph:user-circle" class="text-icon" />
            {{ $t('common.userCenter') }}
          </div>
        </AMenuItem>
        <AMenuDivider />
        <AMenuItem @click="logout">
          <div class="flex-center gap-8px">
            <SvgIcon icon="ph:sign-out" class="text-icon" />
            {{ $t('common.logout') }}
          </div>
        </AMenuItem>
      </AMenu>
    </template>
  </ADropdown>

    <!-- 个人中心抽屉 -->
    <UserProfileDrawer v-model:visible="profileDrawerVisible" @refresh="onProfileRefresh" />
  </div>
</template>

<style scoped></style>
