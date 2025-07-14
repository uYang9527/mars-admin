/**
 * 用户相关API - 模拟数据版本
 * 专注于前端展示和交互效果
 */
import { mockRequest, createSuccessResponse, createErrorResponse } from '@/utils/mock'
import { userInfo } from '@/mock/index'

/**
 * 用户登录
 * @param {Object} data 登录数据
 * @param {string} data.username 用户名
 * @param {string} data.password 密码
 * @returns {Promise} 登录结果
 */
export const login = (data) => {
  return mockRequest(() => {
    // 模拟登录验证
    if (data.username === 'admin' && data.password === '123456') {
      return createSuccessResponse({
        token: 'mock_token_' + Date.now(),
        userInfo: userInfo,
        expires: Date.now() + 7 * 24 * 60 * 60 * 1000 // 7天后过期
      }, '登录成功')
    } else {
      return createErrorResponse('用户名或密码错误', 400)
    }
  }, 1000) // 模拟1秒登录时间
}

/**
 * 用户登出
 * @returns {Promise} 登出结果
 */
export const logout = () => {
  return mockRequest(() => {
    return createSuccessResponse(null, '登出成功')
  }, 500)
}

/**
 * 获取用户信息
 * @returns {Promise} 用户信息
 */
export const getUserInfo = () => {
  return mockRequest(() => {
    return createSuccessResponse(userInfo, '获取成功')
  }, 500)
}

/**
 * 更新用户信息
 * @param {Object} data 用户信息
 * @returns {Promise} 更新结果
 */
export const updateUserInfo = (data) => {
  return mockRequest(() => {
    // 合并用户信息
    Object.assign(userInfo, data)
    return createSuccessResponse(userInfo, '更新成功')
  }, 1000)
}

/**
 * 修改密码
 * @param {Object} data 密码数据
 * @param {string} data.oldPassword 旧密码
 * @param {string} data.newPassword 新密码
 * @returns {Promise} 修改结果
 */
export const changePassword = (data) => {
  return mockRequest(() => {
    if (data.oldPassword === '123456') {
      return createSuccessResponse(null, '密码修改成功')
    } else {
      return createErrorResponse('原密码错误', 400)
    }
  }, 1000)
}

/**
 * 上传头像
 * @param {string} filePath 文件路径
 * @returns {Promise} 上传结果
 */
export const uploadAvatar = (filePath) => {
  return mockRequest(() => {
    // 模拟头像上传，实际项目中会使用filePath
    console.log('模拟上传文件:', filePath)
    const avatarUrl = 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80'
    userInfo.avatar = avatarUrl
    return createSuccessResponse({
      url: avatarUrl
    }, '头像上传成功')
  }, 2000) // 模拟上传需要2秒
}
