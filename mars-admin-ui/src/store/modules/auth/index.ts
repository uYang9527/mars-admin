import { computed, nextTick, reactive, ref } from 'vue';
import { useRoute } from 'vue-router';
import { defineStore } from 'pinia';
import { useLoading } from '@sa/hooks';
import { SetupStoreId } from '@/enum';
import { useRouterPush } from '@/hooks/common/router';
import { fetchGetUserInfo, fetchLogin } from '@/service/api';
import { localStg } from '@/utils/storage';
import { $t } from '@/locales';
import { useRouteStore } from '../route';
import { useTabStore } from '../tab';
import { clearAuthStorage, getToken } from './shared';

export const useAuthStore = defineStore(SetupStoreId.Auth, () => {
  const route = useRoute();
  const routeStore = useRouteStore();
  const tabStore = useTabStore();
  const { toLogin, redirectFromLogin } = useRouterPush(false);
  const { loading: loginLoading, startLoading, endLoading } = useLoading();

  const token = ref(getToken());

  const userInfo: Api.Auth.UserInfo = reactive({
    id: '',
    userId: '',
    userName: '',
    nickName: '',
    avatar: '',
    roles: [],
    permissions: [],
    routes: []
  });

  /** is super role in static route */
  const isStaticSuper = computed(() => {
    const { VITE_AUTH_ROUTE_MODE, VITE_STATIC_SUPER_ROLE } = import.meta.env;

    return VITE_AUTH_ROUTE_MODE === 'static' && userInfo.roles.includes(VITE_STATIC_SUPER_ROLE);
  });

  /** Is login */
  const isLogin = computed(() => Boolean(token.value));

  /** Reset auth store */
  async function resetStore() {
    const authStore = useAuthStore();

    console.log('🔄 开始重置认证store...');
    
    clearAuthStorage();
    authStore.$reset();
    tabStore.cacheTabs();

    // 重置路由store，但不等待其完成
    routeStore.resetStore();

    if (!route.meta.constant) {
      console.log('🔄 需要跳转到登录页面');
      // 简单直接地跳转到登录页面，不做复杂的路由检查
      // 让路由守卫来处理路由初始化
      window.location.href = '/login';
    }
    
    console.log('✅ 认证store重置完成');
  }

  /**
   * Login
   *
   * @param username User name
   * @param password Password
   * @param [redirect=true] Whether to redirect after login. Default is `true`
   */
  async function login(username: string, password: string, redirect = true) {
    startLoading();

    console.log('🔐 开始登录...');
    const { data: loginToken, error } = await fetchLogin(username, password);

    if (!error) {
      console.log('🔐 登录接口成功，开始处理token...');
      const pass = await loginByToken(loginToken);

      if (pass) {
        console.log('🔐 token处理成功，开始初始化认证路由...');
        
        // 登录成功后，初始化认证路由
        try {
          await routeStore.initAuthRoute();
          console.log('🔐 认证路由初始化成功');
        } catch (error) {
          console.error('🔐 认证路由初始化失败:', error);
        }

        console.log('🔐 开始重定向...');
        await redirectFromLogin(redirect);

        console.log('🔐 准备显示登录成功提示，用户名:', userInfo.userName || userInfo.nickName);
        
        window.$notification?.success({
          message: $t('page.login.common.loginSuccess'),
          description: $t('page.login.common.welcomeBack', { 
            userName: userInfo.userName || userInfo.nickName || '用户' 
          })
        });
        
        console.log('🔐 登录流程完成');
      }
    } else {
      console.error('🔐 登录接口失败:', error);
      resetStore();
    }

    endLoading();
  }

  async function loginByToken(loginToken: Api.Auth.LoginToken) {
    // 1. stored in the localStorage, the later requests need it in headers
    localStg.set('token', loginToken.token);
    localStg.set('refreshToken', loginToken.refreshToken);

    // 2. get user info
    const pass = await getUserInfo();

    if (pass) {
      token.value = loginToken.token;

      return true;
    }

    return false;
  }

  async function getUserInfo() {
    const { data: info, error } = await fetchGetUserInfo();

    if (!error && info) {
      console.log('🔐 后端返回的用户信息:', info);
      
      // 处理字段映射：后端返回id，前端需要userId
      const backendInfo = info as any; // 使用any类型处理后端数据
      const mappedInfo = {
        ...info,
        userId: backendInfo.id || backendInfo.userId, // 兼容后端返回id字段
        userName: backendInfo.userName || backendInfo.username || backendInfo.name, // 兼容多种用户名字段
        nickName: backendInfo.nickName || backendInfo.nickname || backendInfo.nick_name || backendInfo.userName || backendInfo.username || backendInfo.name
      };
      
      console.log('🔐 字段映射后的用户信息:', mappedInfo);
      
      // 处理角色信息，确保角色代码正确映射
      if (mappedInfo.roles && Array.isArray(mappedInfo.roles)) {
        // 提取角色代码，用于权限验证
        const roleCodes = mappedInfo.roles.map((role: any) => {
          if (typeof role === 'string') {
            return role;
          }
          // 如果是对象，提取roleCode字段
          return role.roleCode || role.role_code || role.code || role.key;
        }).filter(Boolean);
        
        console.log('🔐 用户角色信息:', mappedInfo.roles);
        console.log('🔐 提取的角色代码:', roleCodes);
        
        // 更新用户信息，使用角色代码数组
        Object.assign(userInfo, {
          ...mappedInfo,
          roles: roleCodes
        });
      } else {
        // update store
        Object.assign(userInfo, mappedInfo);
      }
      
      console.log('🔐 最终用户信息:', userInfo);
      console.log('🔐 用户ID:', userInfo.userId);
      console.log('🔐 用户名:', userInfo.userName);
      console.log('🔐 昵称:', userInfo.nickName);
      console.log('🔐 用户角色数组:', userInfo.roles);
      return true;
    }

    console.error('🔐 获取用户信息失败:', error);
    return false;
  }

  async function initUserInfo() {
    const hasToken = getToken();

    if (hasToken) {
      const pass = await getUserInfo();

      if (!pass) {
        resetStore();
      }
    }
  }

  return {
    token,
    userInfo,
    isStaticSuper,
    isLogin,
    loginLoading,
    resetStore,
    login,
    initUserInfo
  };
});
