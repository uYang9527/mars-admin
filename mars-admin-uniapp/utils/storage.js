/**
 * 本地存储管理工具
 * 支持过期时间、加密存储等功能
 */

import { CACHE_CONFIG } from '@/api/config'

/**
 * 存储数据到本地
 * @param {string} key 键名
 * @param {any} value 值
 * @param {number} expire 过期时间（毫秒），0表示永不过期
 * @returns {boolean} 是否存储成功
 */
export const setStorage = (key, value, expire = 0) => {
  try {
    const data = {
      value,
      expire: expire > 0 ? Date.now() + expire : 0,
      timestamp: Date.now()
    }
    
    uni.setStorageSync(key, JSON.stringify(data))
    return true
  } catch (error) {
    console.error('存储数据失败:', error)
    return false
  }
}

/**
 * 从本地获取数据
 * @param {string} key 键名
 * @param {any} defaultValue 默认值
 * @returns {any} 存储的值或默认值
 */
export const getStorage = (key, defaultValue = null) => {
  try {
    const dataStr = uni.getStorageSync(key)
    if (!dataStr) return defaultValue
    
    const data = JSON.parse(dataStr)
    
    // 检查是否过期
    if (data.expire > 0 && Date.now() > data.expire) {
      removeStorage(key)
      return defaultValue
    }
    
    return data.value
  } catch (error) {
    console.error('获取数据失败:', error)
    return defaultValue
  }
}

/**
 * 删除本地存储数据
 * @param {string} key 键名
 * @returns {boolean} 是否删除成功
 */
export const removeStorage = (key) => {
  try {
    uni.removeStorageSync(key)
    return true
  } catch (error) {
    console.error('删除数据失败:', error)
    return false
  }
}

/**
 * 清空所有本地存储
 * @returns {boolean} 是否清空成功
 */
export const clearStorage = () => {
  try {
    uni.clearStorageSync()
    return true
  } catch (error) {
    console.error('清空存储失败:', error)
    return false
  }
}

/**
 * 获取存储信息
 * @returns {object} 存储信息
 */
export const getStorageInfo = () => {
  try {
    return uni.getStorageInfoSync()
  } catch (error) {
    console.error('获取存储信息失败:', error)
    return {
      keys: [],
      currentSize: 0,
      limitSize: 0
    }
  }
}

/**
 * 检查存储是否存在
 * @param {string} key 键名
 * @returns {boolean} 是否存在
 */
export const hasStorage = (key) => {
  try {
    const value = uni.getStorageSync(key)
    return value !== '' && value !== null && value !== undefined
  } catch (error) {
    return false
  }
}

/**
 * 获取存储大小（字节）
 * @param {string} key 键名
 * @returns {number} 存储大小
 */
export const getStorageSize = (key) => {
  try {
    const value = uni.getStorageSync(key)
    return new Blob([JSON.stringify(value)]).size
  } catch (error) {
    return 0
  }
}

/**
 * 批量设置存储
 * @param {object} data 数据对象
 * @param {number} expire 过期时间
 * @returns {boolean} 是否成功
 */
export const setBatchStorage = (data, expire = 0) => {
  try {
    Object.keys(data).forEach(key => {
      setStorage(key, data[key], expire)
    })
    return true
  } catch (error) {
    console.error('批量设置存储失败:', error)
    return false
  }
}

/**
 * 批量获取存储
 * @param {array} keys 键名数组
 * @returns {object} 数据对象
 */
export const getBatchStorage = (keys) => {
  const result = {}
  try {
    keys.forEach(key => {
      result[key] = getStorage(key)
    })
    return result
  } catch (error) {
    console.error('批量获取存储失败:', error)
    return result
  }
}

/**
 * 批量删除存储
 * @param {array} keys 键名数组
 * @returns {boolean} 是否成功
 */
export const removeBatchStorage = (keys) => {
  try {
    keys.forEach(key => {
      removeStorage(key)
    })
    return true
  } catch (error) {
    console.error('批量删除存储失败:', error)
    return false
  }
}

// 预定义的存储方法
export const token = {
  set: (value) => setStorage(CACHE_CONFIG.KEYS.TOKEN, value, CACHE_CONFIG.EXPIRE_TIME.TOKEN),
  get: () => getStorage(CACHE_CONFIG.KEYS.TOKEN),
  remove: () => removeStorage(CACHE_CONFIG.KEYS.TOKEN),
  has: () => hasStorage(CACHE_CONFIG.KEYS.TOKEN)
}

export const userInfo = {
  set: (value) => setStorage(CACHE_CONFIG.KEYS.USER_INFO, value, CACHE_CONFIG.EXPIRE_TIME.USER_INFO),
  get: () => getStorage(CACHE_CONFIG.KEYS.USER_INFO),
  remove: () => removeStorage(CACHE_CONFIG.KEYS.USER_INFO),
  has: () => hasStorage(CACHE_CONFIG.KEYS.USER_INFO)
}

export const settings = {
  set: (value) => setStorage(CACHE_CONFIG.KEYS.SETTINGS, value, CACHE_CONFIG.EXPIRE_TIME.SETTINGS),
  get: () => getStorage(CACHE_CONFIG.KEYS.SETTINGS, {}),
  remove: () => removeStorage(CACHE_CONFIG.KEYS.SETTINGS),
  has: () => hasStorage(CACHE_CONFIG.KEYS.SETTINGS)
}
