import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

// 静态路由
const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/register/index.vue'),
    meta: { title: '注册', requiresAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'HomeOutline' }
      },
      // 系统管理
      {
        path: 'system/user',
        name: 'SystemUser',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'PersonOutline' }
      },
      {
        path: 'system/role',
        name: 'SystemRole',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理', icon: 'PeopleOutline' }
      },
      {
        path: 'system/menu',
        name: 'SystemMenu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理', icon: 'MenuOutline' }
      },
      {
        path: 'system/dict',
        name: 'SystemDict',
        component: () => import('@/views/system/dict/index.vue'),
        meta: { title: '字典管理', icon: 'BookOutline' }
      },
      {
        path: 'system/config',
        name: 'SystemConfig',
        component: () => import('@/views/system/config/index.vue'),
        meta: { title: '系统配置', icon: 'SettingsSharp' }
      },
      // 文件管理
      {
        path: 'system/file',
        name: 'SystemFile',
        component: () => import('@/views/system/file/index.vue'),
        meta: { title: '文件列表', icon: 'FolderOutline' }
      },
      {
        path: 'system/file-config',
        name: 'SystemFileConfig',
        component: () => import('@/views/system/file-config/index.vue'),
        meta: { title: '文件配置', icon: 'CloudOutline' }
      },
      // 消息中心
      {
        path: 'message/notice',
        name: 'MessageNotice',
        component: () => import('@/views/message/notice/index.vue'),
        meta: { title: '系统通知', icon: 'NotificationsOutline' }
      },
      {
        path: 'message/chat',
        name: 'MessageChat',
        component: () => import('@/views/message/chat/index.vue'),
        meta: { title: '即时聊天', icon: 'ChatbubbleOutline' }
      },
      // 组织管理
      {
        path: 'org/dept',
        name: 'OrgDept',
        component: () => import('@/views/org/dept/index.vue'),
        meta: { title: '部门管理', icon: 'GitNetworkOutline' }
      },
      {
        path: 'org/post',
        name: 'OrgPost',
        component: () => import('@/views/org/post/index.vue'),
        meta: { title: '岗位管理', icon: 'IdCardOutline' }
      },
      // 系统日志
      {
        path: 'log/operlog',
        name: 'LogOper',
        component: () => import('@/views/log/operlog/index.vue'),
        meta: { title: '操作日志', icon: 'ListOutline' }
      },
      {
        path: 'log/loginlog',
        name: 'LogLogin',
        component: () => import('@/views/log/loginlog/index.vue'),
        meta: { title: '登录日志', icon: 'LogInOutline' }
      },
      // 系统监控
      {
        path: 'monitor/online',
        name: 'MonitorOnline',
        component: () => import('@/views/monitor/online/index.vue'),
        meta: { title: '在线用户', icon: 'PeopleCircleOutline' }
      },
      {
        path: 'monitor/job',
        name: 'MonitorJob',
        component: () => import('@/views/monitor/job/index.vue'),
        meta: { title: '定时任务', icon: 'TimerOutline' }
      },
      {
        path: 'monitor/cache',
        name: 'MonitorCache',
        component: () => import('@/views/monitor/cache/index.vue'),
        meta: { title: '缓存监控', icon: 'ServerOutline' }
      },
      {
        path: 'monitor/server',
        name: 'MonitorServer',
        component: () => import('@/views/monitor/server/index.vue'),
        meta: { title: '服务监控', icon: 'DesktopOutline' }
      },
      {
        path: 'monitor/server-manager',
        name: 'ServerManager',
        component: () => import('@/views/monitor/server-manager/index.vue'),
        meta: { title: '服务器管理', icon: 'ServerOutline' }
      },
        {
            path: 'test/test',
            name: 'Test',
            component: () => import('@/views/test/test/index.vue'),
            meta: { title: '测试菜单', icon: 'StarOutline' }
        }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '404', requiresAuth: false }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, _from, next) => {
  const userStore = useUserStore()

  // 设置页面标题
  document.title = `${to.meta.title || ''} - Mars Admin`

  // 不需要认证的页面直接放行
  if (to.meta.requiresAuth === false) {
    next()
    return
  }

  // 检查是否登录
  if (!userStore.token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // 获取用户信息
  if (!userStore.user) {
    try {
      await userStore.getInfo()
    } catch (error) {
      userStore.logout()
      next({ name: 'Login' })
      return
    }
  }

  next()
})

export default router
