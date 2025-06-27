import type { App } from 'vue';
import {
  type RouterHistory,
  createMemoryHistory,
  createRouter,
  createWebHashHistory,
  createWebHistory
} from 'vue-router';
import { createBuiltinVueRoutes } from './routes/builtin';
import { createStaticRoutes, getAuthVueRoutes } from './routes';
import { createRouterGuard } from './guard';

const { VITE_ROUTER_HISTORY_MODE = 'history', VITE_BASE_URL } = import.meta.env;

const historyCreatorMap: Record<Env.RouterHistoryMode, (base?: string) => RouterHistory> = {
  hash: createWebHashHistory,
  history: createWebHistory,
  memory: createMemoryHistory
};

// 创建基础路由：内置路由 + 常量路由
function createInitialRoutes() {
  const builtinRoutes = createBuiltinVueRoutes();
  const { constantRoutes } = createStaticRoutes();
  const constantVueRoutes = getAuthVueRoutes(constantRoutes);
  
  console.log('🚀 初始化路由:');
  console.log('- 内置路由数量:', builtinRoutes.length);
  console.log('- 常量路由数量:', constantVueRoutes.length);
  
  return [...builtinRoutes, ...constantVueRoutes];
}

export const router = createRouter({
  history: historyCreatorMap[VITE_ROUTER_HISTORY_MODE](VITE_BASE_URL),
  routes: createInitialRoutes()
});

/** Setup Vue Router */
export async function setupRouter(app: App) {
  app.use(router);
  createRouterGuard(router);
  await router.isReady();
}
