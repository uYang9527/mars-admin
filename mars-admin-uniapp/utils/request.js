/**
 * 网络请求工具
 */

// 基础配置
const config = {
  baseURL: 'http://localhost:8080', // 后端API地址
  timeout: 10000, // 请求超时时间
  header: {
    'Content-Type': 'application/json'
  }
}

/**
 * 请求拦截器
 */
const requestInterceptor = (options) => {
  // 显示加载提示
  uni.showLoading({
    title: '加载中...',
    mask: true
  })
  
  // 添加token
  const token = uni.getStorageSync('token')
  if (token) {
    options.header = {
      ...options.header,
      'Authorization': `Bearer ${token}`
    }
  }
  
  // 处理URL
  if (!options.url.startsWith('http')) {
    options.url = config.baseURL + options.url
  }
  
  return options
}

/**
 * 响应拦截器
 */
const responseInterceptor = (response) => {
  // 隐藏加载提示
  uni.hideLoading()
  
  const { statusCode, data } = response
  
  // HTTP状态码检查
  if (statusCode !== 200) {
    uni.showToast({
      title: `请求失败: ${statusCode}`,
      icon: 'none'
    })
    return Promise.reject(response)
  }
  
  // 业务状态码检查
  if (data.code !== 200) {
    // 处理特殊错误码
    switch (data.code) {
      case 401:
        // 未授权，清除token并跳转到登录页
        uni.removeStorageSync('token')
        uni.removeStorageSync('userInfo')
        uni.reLaunch({
          url: '/pages/login/login'
        })
        break
      case 403:
        uni.showToast({
          title: '权限不足',
          icon: 'none'
        })
        break
      default:
        uni.showToast({
          title: data.message || '请求失败',
          icon: 'none'
        })
    }
    return Promise.reject(data)
  }
  
  return data
}

/**
 * 通用请求方法
 */
const request = (options) => {
  return new Promise((resolve, reject) => {
    // 请求拦截
    const requestOptions = requestInterceptor({
      ...config,
      ...options
    })
    
    uni.request({
      ...requestOptions,
      success: (response) => {
        try {
          const result = responseInterceptor(response)
          resolve(result)
        } catch (error) {
          reject(error)
        }
      },
      fail: (error) => {
        uni.hideLoading()
        uni.showToast({
          title: '网络请求失败',
          icon: 'none'
        })
        reject(error)
      }
    })
  })
}

/**
 * GET请求
 */
const get = (url, params = {}) => {
  return request({
    url,
    method: 'GET',
    data: params
  })
}

/**
 * POST请求
 */
const post = (url, data = {}) => {
  return request({
    url,
    method: 'POST',
    data
  })
}

/**
 * PUT请求
 */
const put = (url, data = {}) => {
  return request({
    url,
    method: 'PUT',
    data
  })
}

/**
 * DELETE请求
 */
const del = (url, params = {}) => {
  return request({
    url,
    method: 'DELETE',
    data: params
  })
}

/**
 * 文件上传
 */
const upload = (url, filePath, formData = {}) => {
  return new Promise((resolve, reject) => {
    uni.showLoading({
      title: '上传中...',
      mask: true
    })
    
    const token = uni.getStorageSync('token')
    const header = {}
    if (token) {
      header['Authorization'] = `Bearer ${token}`
    }
    
    uni.uploadFile({
      url: config.baseURL + url,
      filePath,
      name: 'file',
      formData,
      header,
      success: (response) => {
        uni.hideLoading()
        try {
          const data = JSON.parse(response.data)
          if (data.code === 200) {
            resolve(data)
          } else {
            uni.showToast({
              title: data.message || '上传失败',
              icon: 'none'
            })
            reject(data)
          }
        } catch (error) {
          uni.showToast({
            title: '上传失败',
            icon: 'none'
          })
          reject(error)
        }
      },
      fail: (error) => {
        uni.hideLoading()
        uni.showToast({
          title: '上传失败',
          icon: 'none'
        })
        reject(error)
      }
    })
  })
}

/**
 * 文件下载
 */
const download = (url, filename) => {
  return new Promise((resolve, reject) => {
    uni.showLoading({
      title: '下载中...',
      mask: true
    })
    
    const token = uni.getStorageSync('token')
    const header = {}
    if (token) {
      header['Authorization'] = `Bearer ${token}`
    }
    
    uni.downloadFile({
      url: config.baseURL + url,
      header,
      success: (response) => {
        uni.hideLoading()
        if (response.statusCode === 200) {
          // 保存文件
          uni.saveFile({
            tempFilePath: response.tempFilePath,
            success: (saveResult) => {
              uni.showToast({
                title: '下载成功',
                icon: 'success'
              })
              resolve(saveResult)
            },
            fail: (saveError) => {
              uni.showToast({
                title: '保存失败',
                icon: 'none'
              })
              reject(saveError)
            }
          })
        } else {
          uni.showToast({
            title: '下载失败',
            icon: 'none'
          })
          reject(response)
        }
      },
      fail: (error) => {
        uni.hideLoading()
        uni.showToast({
          title: '下载失败',
          icon: 'none'
        })
        reject(error)
      }
    })
  })
}

export default {
  get,
  post,
  put,
  delete: del,
  upload,
  download,
  request
} 