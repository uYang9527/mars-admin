import type {
  NavigationGuardNext,
  RouteLocationNormalized,
  Router
} from 'vue-router';
import type { RouteKey } from '@elegant-router/types';
import { useAuthStore } from '@/store/modules/auth';
import { useRouteStore } from '@/store/modules/route';
import { localStg } from '@/utils/storage';

// 全局初始化状态
let isInitializing = false;
let initPromise: Promise<void> | null = null;

/**
 * create route guard
 *
 * @param router router instance
 */
export function createRouteGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    console.log('🛡️ 路由守卫开始:', {
      to: to.name,
      path: to.path,
      from: from.name,
      constant: to.meta?.constant
    });

    const loginRoute: RouteKey = 'login';
    
    const isLogin = Boolean(localStg.get('token'));
    const isConstantRoute = to.meta?.constant === true;
    const isLoginRoute = to.name === loginRoute;
    const is404Route = to.name === 'not-found';

    console.log('🛡️ 状态检查:', { 
      isLogin, 
      isConstantRoute, 
      isLoginRoute,
      is404Route,
      token: localStg.get('token') ? '存在' : '不存在'
    });

    // 特殊处理：如果已登录但访问404页面，检查是否应该初始化路由
    if (is404Route && isLogin) {
      const routeStore = useRouteStore();
      if (!routeStore.isInitAuthRoute) {
        console.log('🛡️ 已登录用户访问404，但认证路由未初始化，尝试初始化后重新路由');
        // 不直接放行404，而是尝试初始化路由，继续执行下面的初始化逻辑
              } else {
          // 认证路由已初始化，但仍然是404
          // 这通常是页面刷新导致的，直接重定向到首页
          console.log('🛡️ 认证路由已初始化但仍是404，重定向到首页');
          next({ path: '/home', replace: true });
          return;
        }
    } else if (isConstantRoute && !is404Route) {
      // 1. 其他常量路由（登录页、根路由等）直接放行
      console.log('🛡️ 常量路由，直接放行');
      next();
      return;
    }

    // 2. 已登录用户访问登录页，重定向到首页
    if (isLoginRoute && isLogin) {
      console.log('🛡️ 已登录访问登录页，重定向到首页');
      next({ path: '/home', replace: true });
      return;
    }

    // 3. 未登录用户访问非常量路由，重定向到登录页
    if (!isLogin) {
      console.log('🛡️ 未登录访问受保护路由，重定向到登录页');
      next({ 
        name: loginRoute, 
        query: { redirect: to.fullPath },
        replace: true 
      });
      return;
    }

    // 4. 已登录用户访问受保护路由
    console.log('🛡️ 已登录用户访问受保护路由');
    const routeStore = useRouteStore();
    const authStore = useAuthStore();
    
    console.log('🛡️ 路由状态:', {
      isInitConstantRoute: routeStore.isInitConstantRoute,
      isInitAuthRoute: routeStore.isInitAuthRoute,
      hasUserId: !!authStore.userInfo.userId,
      userId: authStore.userInfo.userId,
      userName: authStore.userInfo.userName
    });

    // 如果认证路由未初始化，则初始化
    if (!routeStore.isInitAuthRoute) {
      console.log('🛡️ 认证路由未初始化，开始初始化流程...');
      
      // 如果正在初始化，等待初始化完成
      if (isInitializing && initPromise) {
        console.log('🛡️ 正在初始化中，等待完成...');
        try {
          await initPromise;
          console.log('🛡️ 等待初始化完成，重新检查路由');
          next({ path: to.path, replace: true });
          return;
        } catch (error) {
          console.error('🛡️ 等待初始化失败:', error);
          next({ name: loginRoute, replace: true });
          return;
        }
      }
      
      // 开始初始化
      isInitializing = true;
      initPromise = (async () => {
        try {
          // 步骤1: 确保常量路由已初始化
          if (!routeStore.isInitConstantRoute) {
            console.log('🛡️ 步骤1: 初始化常量路由...');
            await routeStore.initConstantRoute();
            console.log('🛡️ 步骤1: 常量路由初始化完成');
          }
          
          // 步骤2: 确保用户信息存在
          if (!authStore.userInfo.userId) {
            console.log('🛡️ 步骤2: 用户信息不存在，重新获取...');
            await authStore.initUserInfo();
            console.log('🛡️ 步骤2: 用户信息获取完成');
            console.log('🛡️ 步骤2: userId:', authStore.userInfo.userId);
            console.log('🛡️ 步骤2: userName:', authStore.userInfo.userName);
            console.log('🛡️ 步骤2: 完整用户信息:', authStore.userInfo);
            
            if (!authStore.userInfo.userId) {
              console.log('🛡️ 步骤2: 获取用户信息失败，userId仍为空');
              throw new Error('获取用户信息失败');
            }
          }
          
          // 步骤3: 初始化认证路由
          console.log('🛡️ 步骤3: 初始化认证路由...');
          await routeStore.initAuthRoute();
          console.log('🛡️ 步骤3: 认证路由初始化完成');
          
          // 步骤4: 等待路由注册完成
          console.log('🛡️ 步骤4: 等待路由注册完成...');
          await new Promise(resolve => setTimeout(resolve, 50));
          
          console.log('🛡️ 所有初始化步骤完成');
        } finally {
          isInitializing = false;
          initPromise = null;
        }
      })();
      
      try {
        await initPromise;
        
        // 检查目标路由是否存在
        const allRoutes = router.getRoutes();
        const targetRoute = allRoutes.find(route => route.path === to.path || route.name === to.name);
        
        console.log('🛡️ 路由检查结果:', {
          targetPath: to.path,
          targetName: to.name,
          routeFound: !!targetRoute,
          totalRoutes: allRoutes.length,
          allRoutePaths: allRoutes.map(r => r.path),
          homeRouteExists: allRoutes.some(r => r.path === '/home' || r.name === 'home')
        });
        
        // 如果目标路由是404，但实际想访问的是有效路径，重新导航
        if (to.name === 'not-found') {
          console.log('🛡️ 目标路由是404，检查是否应该重定向');
          const homeRoute = allRoutes.find(r => r.path === '/home' || r.name === 'home');
          if (homeRoute) {
            console.log('🛡️ 找到home路由，重定向到首页');
            next({ path: '/home', replace: true });
            return;
          }
        }
        
        if (!targetRoute && to.path !== '/home') {
          console.log('🛡️ 目标路由不存在，重定向到首页');
          next({ path: '/home', replace: true });
          return;
        }
        
        console.log('🛡️ 初始化完成，允许访问目标路由');
        next();
        return;
        
      } catch (error) {
        console.error('🛡️ 初始化过程中发生错误:', error);
        if (error instanceof Error) {
          console.error('🛡️ 错误详情:', error.message);
          console.error('🛡️ 错误堆栈:', error.stack);
        }
        
        // 初始化失败，重定向到登录页
        next({ 
          name: loginRoute,
          replace: true 
        });
        return;
      }
    }
    
    console.log('🛡️ 认证路由已初始化，直接放行');
    next();
  });
}



