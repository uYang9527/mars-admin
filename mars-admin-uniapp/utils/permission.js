/**
 * 权限管理工具
 * 处理用户权限验证、页面访问控制等
 */

import { token, userInfo } from './storage'

/**
 * 检查用户是否已登录
 * @returns {boolean} 是否已登录
 */
export const isLoggedIn = () => {
  return token.has() && token.get()
}

/**
 * 检查用户是否有指定权限
 * @param {string|array} permission 权限码或权限码数组
 * @returns {boolean} 是否有权限
 */
export const hasPermission = (permission) => {
  if (!isLoggedIn()) return false
  
  const user = userInfo.get()
  if (!user || !user.permissions) return false
  
  const userPermissions = user.permissions || []
  
  if (typeof permission === 'string') {
    return userPermissions.includes(permission)
  }
  
  if (Array.isArray(permission)) {
    return permission.some(p => userPermissions.includes(p))
  }
  
  return false
}

/**
 * 检查用户是否有指定角色
 * @param {string|array} role 角色或角色数组
 * @returns {boolean} 是否有角色
 */
export const hasRole = (role) => {
  if (!isLoggedIn()) return false
  
  const user = userInfo.get()
  if (!user || !user.roles) return false
  
  const userRoles = user.roles || []
  
  if (typeof role === 'string') {
    return userRoles.includes(role)
  }
  
  if (Array.isArray(role)) {
    return role.some(r => userRoles.includes(r))
  }
  
  return false
}

/**
 * 检查是否为管理员
 * @returns {boolean} 是否为管理员
 */
export const isAdmin = () => {
  return hasRole(['admin', 'super_admin'])
}

/**
 * 检查页面访问权限
 * @param {string} path 页面路径
 * @returns {boolean} 是否可访问
 */
export const canAccessPage = (path) => {
  // 公开页面，无需权限
  const publicPages = [
    '/pages/login/login',
    '/pages/register/register',
    '/pages/forgot-password/forgot-password'
  ]
  
  if (publicPages.includes(path)) {
    return true
  }
  
  // 需要登录的页面
  if (!isLoggedIn()) {
    return false
  }
  
  // 这里可以根据具体业务需求添加页面权限控制
  // 例如：某些页面只有管理员可以访问
  const adminOnlyPages = [
    '/pages/admin/users',
    '/pages/admin/settings'
  ]
  
  if (adminOnlyPages.includes(path)) {
    return isAdmin()
  }
  
  return true
}

/**
 * 页面权限拦截器
 * @param {string} url 目标页面URL
 * @param {function} success 成功回调
 * @param {function} fail 失败回调
 */
export const pageGuard = (url, success, fail) => {
  const path = url.split('?')[0] // 去除查询参数
  
  if (canAccessPage(path)) {
    success && success()
  } else {
    fail && fail()
    
    // 未登录跳转到登录页
    if (!isLoggedIn()) {
      uni.reLaunch({
        url: '/pages/login/login'
      })
    } else {
      // 无权限提示
      uni.showToast({
        title: '无访问权限',
        icon: 'none'
      })
    }
  }
}

/**
 * 登录
 * @param {object} userData 用户数据
 * @param {string} tokenValue token值
 */
export const login = (userData, tokenValue) => {
  token.set(tokenValue)
  userInfo.set(userData)
}

/**
 * 登出
 */
export const logout = () => {
  token.remove()
  userInfo.remove()
  
  // 跳转到登录页
  uni.reLaunch({
    url: '/pages/login/login'
  })
}

/**
 * 获取当前用户信息
 * @returns {object|null} 用户信息
 */
export const getCurrentUser = () => {
  return userInfo.get()
}

/**
 * 更新用户信息
 * @param {object} data 用户数据
 */
export const updateUserInfo = (data) => {
  const currentUser = getCurrentUser()
  if (currentUser) {
    const updatedUser = { ...currentUser, ...data }
    userInfo.set(updatedUser)
  }
}

/**
 * 权限指令（用于模板中的权限控制）
 * @param {string} permission 权限码
 * @returns {boolean} 是否显示
 */
export const vPermission = (permission) => {
  return hasPermission(permission)
}

/**
 * 角色指令（用于模板中的角色控制）
 * @param {string} role 角色
 * @returns {boolean} 是否显示
 */
export const vRole = (role) => {
  return hasRole(role)
}

// 权限常量
export const PERMISSIONS = {
  // 用户管理
  USER_VIEW: 'user:view',
  USER_CREATE: 'user:create',
  USER_UPDATE: 'user:update',
  USER_DELETE: 'user:delete',
  
  // 部门管理
  DEPT_VIEW: 'dept:view',
  DEPT_CREATE: 'dept:create',
  DEPT_UPDATE: 'dept:update',
  DEPT_DELETE: 'dept:delete',
  
  // 角色管理
  ROLE_VIEW: 'role:view',
  ROLE_CREATE: 'role:create',
  ROLE_UPDATE: 'role:update',
  ROLE_DELETE: 'role:delete',
  
  // 系统设置
  SYSTEM_VIEW: 'system:view',
  SYSTEM_UPDATE: 'system:update'
}

// 角色常量
export const ROLES = {
  SUPER_ADMIN: 'super_admin',
  ADMIN: 'admin',
  MANAGER: 'manager',
  USER: 'user'
}
