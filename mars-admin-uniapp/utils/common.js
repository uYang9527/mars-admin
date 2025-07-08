/**
 * 通用工具函数
 */

/**
 * 格式化日期时间
 * @param {Date|string|number} date 日期
 * @param {string} format 格式化字符串
 * @returns {string} 格式化后的日期字符串
 */
export const formatDate = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) return ''
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

/**
 * 格式化相对时间
 * @param {Date|string|number} date 日期
 * @returns {string} 相对时间字符串
 */
export const formatRelativeTime = (date) => {
  if (!date) return ''
  
  const now = new Date()
  const target = new Date(date)
  const diff = now.getTime() - target.getTime()
  
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  const week = 7 * day
  const month = 30 * day
  const year = 365 * day
  
  if (diff < minute) {
    return '刚刚'
  } else if (diff < hour) {
    return `${Math.floor(diff / minute)}分钟前`
  } else if (diff < day) {
    return `${Math.floor(diff / hour)}小时前`
  } else if (diff < week) {
    return `${Math.floor(diff / day)}天前`
  } else if (diff < month) {
    return `${Math.floor(diff / week)}周前`
  } else if (diff < year) {
    return `${Math.floor(diff / month)}个月前`
  } else {
    return `${Math.floor(diff / year)}年前`
  }
}

/**
 * 防抖函数
 * @param {Function} func 要防抖的函数
 * @param {number} delay 延迟时间
 * @returns {Function} 防抖后的函数
 */
export const debounce = (func, delay = 300) => {
  let timer = null
  return function(...args) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      func.apply(this, args)
    }, delay)
  }
}

/**
 * 节流函数
 * @param {Function} func 要节流的函数
 * @param {number} delay 延迟时间
 * @returns {Function} 节流后的函数
 */
export const throttle = (func, delay = 300) => {
  let timer = null
  return function(...args) {
    if (!timer) {
      timer = setTimeout(() => {
        func.apply(this, args)
        timer = null
      }, delay)
    }
  }
}

/**
 * 深拷贝
 * @param {any} obj 要拷贝的对象
 * @returns {any} 拷贝后的对象
 */
export const deepClone = (obj) => {
  if (obj === null || typeof obj !== 'object') return obj
  if (obj instanceof Date) return new Date(obj)
  if (obj instanceof Array) return obj.map(item => deepClone(item))
  if (typeof obj === 'object') {
    const clonedObj = {}
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        clonedObj[key] = deepClone(obj[key])
      }
    }
    return clonedObj
  }
}

/**
 * 生成UUID
 * @returns {string} UUID字符串
 */
export const generateUUID = () => {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = Math.random() * 16 | 0
    const v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}

/**
 * 获取文件扩展名
 * @param {string} filename 文件名
 * @returns {string} 扩展名
 */
export const getFileExtension = (filename) => {
  if (!filename) return ''
  const lastDotIndex = filename.lastIndexOf('.')
  return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1).toLowerCase() : ''
}

/**
 * 格式化文件大小
 * @param {number} bytes 字节数
 * @returns {string} 格式化后的文件大小
 */
export const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

/**
 * 验证手机号
 * @param {string} phone 手机号
 * @returns {boolean} 是否有效
 */
export const validatePhone = (phone) => {
  const phoneRegex = /^1[3-9]\d{9}$/
  return phoneRegex.test(phone)
}

/**
 * 验证邮箱
 * @param {string} email 邮箱
 * @returns {boolean} 是否有效
 */
export const validateEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

/**
 * 验证身份证号
 * @param {string} idCard 身份证号
 * @returns {boolean} 是否有效
 */
export const validateIdCard = (idCard) => {
  const idCardRegex = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  return idCardRegex.test(idCard)
}

/**
 * 隐藏手机号中间四位
 * @param {string} phone 手机号
 * @returns {string} 隐藏后的手机号
 */
export const hidePhone = (phone) => {
  if (!phone || phone.length < 11) return phone
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

/**
 * 隐藏身份证号中间部分
 * @param {string} idCard 身份证号
 * @returns {string} 隐藏后的身份证号
 */
export const hideIdCard = (idCard) => {
  if (!idCard || idCard.length < 15) return idCard
  return idCard.replace(/(\d{6})\d+(\d{4})/, '$1******$2')
}

/**
 * 获取URL参数
 * @param {string} name 参数名
 * @param {string} url URL字符串
 * @returns {string|null} 参数值
 */
export const getUrlParam = (name, url = window.location.href) => {
  const regex = new RegExp('[?&]' + name + '=([^&#]*)', 'i')
  const match = regex.exec(url)
  return match ? decodeURIComponent(match[1]) : null
}

/**
 * 设置页面标题
 * @param {string} title 标题
 */
export const setPageTitle = (title) => {
  uni.setNavigationBarTitle({
    title: title
  })
}

/**
 * 显示成功提示
 * @param {string} message 提示信息
 * @param {number} duration 显示时长
 */
export const showSuccess = (message, duration = 2000) => {
  uni.showToast({
    title: message,
    icon: 'success',
    duration
  })
}

/**
 * 显示错误提示
 * @param {string} message 提示信息
 * @param {number} duration 显示时长
 */
export const showError = (message, duration = 2000) => {
  uni.showToast({
    title: message,
    icon: 'none',
    duration
  })
}

/**
 * 显示加载提示
 * @param {string} message 提示信息
 */
export const showLoading = (message = '加载中...') => {
  uni.showLoading({
    title: message,
    mask: true
  })
}

/**
 * 隐藏加载提示
 */
export const hideLoading = () => {
  uni.hideLoading()
}

/**
 * 确认对话框
 * @param {string} content 内容
 * @param {string} title 标题
 * @returns {Promise<boolean>} 用户选择结果
 */
export const confirm = (content, title = '提示') => {
  return new Promise((resolve) => {
    uni.showModal({
      title,
      content,
      success: (res) => {
        resolve(res.confirm)
      }
    })
  })
}

/**
 * 页面跳转
 * @param {string} url 页面路径
 * @param {object} params 参数
 */
export const navigateTo = (url, params = {}) => {
  const query = Object.keys(params).map(key => `${key}=${encodeURIComponent(params[key])}`).join('&')
  const fullUrl = query ? `${url}?${query}` : url
  
  uni.navigateTo({
    url: fullUrl,
    fail: (err) => {
      console.error('页面跳转失败:', err)
    }
  })
}

/**
 * 页面重定向
 * @param {string} url 页面路径
 * @param {object} params 参数
 */
export const redirectTo = (url, params = {}) => {
  const query = Object.keys(params).map(key => `${key}=${encodeURIComponent(params[key])}`).join('&')
  const fullUrl = query ? `${url}?${query}` : url
  
  uni.redirectTo({
    url: fullUrl,
    fail: (err) => {
      console.error('页面重定向失败:', err)
    }
  })
}

/**
 * 返回上一页
 * @param {number} delta 返回层数
 */
export const navigateBack = (delta = 1) => {
  uni.navigateBack({
    delta,
    fail: (err) => {
      console.error('页面返回失败:', err)
    }
  })
}

/**
 * 存储数据到本地
 * @param {string} key 键名
 * @param {any} value 值
 */
export const setStorage = (key, value) => {
  try {
    uni.setStorageSync(key, value)
  } catch (error) {
    console.error('存储数据失败:', error)
  }
}

/**
 * 从本地获取数据
 * @param {string} key 键名
 * @param {any} defaultValue 默认值
 * @returns {any} 存储的值
 */
export const getStorage = (key, defaultValue = null) => {
  try {
    return uni.getStorageSync(key) || defaultValue
  } catch (error) {
    console.error('获取数据失败:', error)
    return defaultValue
  }
}

/**
 * 删除本地存储数据
 * @param {string} key 键名
 */
export const removeStorage = (key) => {
  try {
    uni.removeStorageSync(key)
  } catch (error) {
    console.error('删除数据失败:', error)
  }
}

/**
 * 清空本地存储
 */
export const clearStorage = () => {
  try {
    uni.clearStorageSync()
  } catch (error) {
    console.error('清空存储失败:', error)
  }
} 