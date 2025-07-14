/**
 * API配置文件
 * 当前项目使用模拟数据，不调用真实后端接口
 */

// API配置 - 目前只使用模拟数据
export const API_CONFIG = {
  mock: true,           // 强制使用mock数据
  timeout: 10000,       // 模拟请求超时时间
  delay: {              // 模拟网络延迟
    min: 300,           // 最小延迟(ms)
    max: 1000           // 最大延迟(ms)
  }
}

// API接口地址
export const API_ENDPOINTS = {
  // 用户相关
  USER: {
    LOGIN: '/api/user/login',
    LOGOUT: '/api/user/logout',
    INFO: '/api/user/info',
    UPDATE: '/api/user/update',
    CHANGE_PASSWORD: '/api/user/change-password'
  },
  
  // 首页相关
  HOME: {
    BANNER: '/api/home/banner',
    QUICK_MENU: '/api/home/quick-menu',
    NOTICE: '/api/home/notice',
    WORK_LIST: '/api/home/work-list',
    STATS: '/api/home/stats'
  },
  
  // 消息相关
  MESSAGE: {
    LIST: '/api/message/list',
    DETAIL: '/api/message/detail',
    READ: '/api/message/read',
    DELETE: '/api/message/delete',
    UNREAD_COUNT: '/api/message/unread-count'
  },
  
  // 组织架构相关
  ORGANIZATION: {
    DEPARTMENT_LIST: '/api/organization/department/list',
    DEPARTMENT_DETAIL: '/api/organization/department/detail',
    MEMBER_LIST: '/api/organization/member/list',
    MEMBER_DETAIL: '/api/organization/member/detail'
  },
  
  // 文件相关
  FILE: {
    UPLOAD: '/api/file/upload',
    DOWNLOAD: '/api/file/download',
    DELETE: '/api/file/delete'
  },
  
  // 考勤相关
  ATTENDANCE: {
    CLOCK_IN: '/api/attendance/clock-in',
    CLOCK_OUT: '/api/attendance/clock-out',
    RECORD: '/api/attendance/record',
    STATS: '/api/attendance/stats'
  },
  
  // 申请相关
  APPLICATION: {
    LEAVE: '/api/application/leave',
    EXPENSE: '/api/application/expense',
    OVERTIME: '/api/application/overtime',
    LIST: '/api/application/list',
    DETAIL: '/api/application/detail',
    APPROVE: '/api/application/approve'
  }
}

// 请求状态码
export const STATUS_CODE = {
  SUCCESS: 200,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  SERVER_ERROR: 500
}

// 错误信息
export const ERROR_MESSAGE = {
  NETWORK_ERROR: '网络连接失败，请检查网络设置',
  TIMEOUT_ERROR: '请求超时，请稍后重试',
  SERVER_ERROR: '服务器错误，请稍后重试',
  UNAUTHORIZED: '登录已过期，请重新登录',
  FORBIDDEN: '权限不足，无法访问',
  NOT_FOUND: '请求的资源不存在',
  UNKNOWN_ERROR: '未知错误，请稍后重试'
}

// 缓存配置
export const CACHE_CONFIG = {
  // 缓存键名
  KEYS: {
    TOKEN: 'mars_admin_token',
    USER_INFO: 'mars_admin_user_info',
    SETTINGS: 'mars_admin_settings'
  },
  // 缓存过期时间（毫秒）
  EXPIRE_TIME: {
    TOKEN: 7 * 24 * 60 * 60 * 1000, // 7天
    USER_INFO: 24 * 60 * 60 * 1000, // 1天
    SETTINGS: 30 * 24 * 60 * 60 * 1000 // 30天
  }
}
