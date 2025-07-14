/**
 * 模拟数据工具
 * 用于前端展示和交互效果，不调用真实后端接口
 */

import { API_CONFIG } from '@/api/config'

/**
 * 模拟API请求
 * @param {Function} mockFn 模拟数据函数
 * @param {number} delay 延迟时间(ms)，不传则使用随机延迟
 * @returns {Promise} 模拟的API响应
 */
export const mockRequest = (mockFn, delay) => {
  return new Promise((resolve) => {
    // 计算延迟时间
    let requestDelay = delay
    if (requestDelay === undefined) {
      const { min, max } = API_CONFIG.delay
      requestDelay = Math.floor(Math.random() * (max - min + 1)) + min
    }
    
    setTimeout(() => {
      try {
        const result = mockFn()
        resolve(result)
      } catch (error) {
        resolve({
          code: 500,
          message: '模拟数据生成失败',
          data: null
        })
      }
    }, requestDelay)
  })
}

/**
 * 创建成功响应
 * @param {any} data 响应数据
 * @param {string} message 响应消息
 * @returns {Object} 标准响应格式
 */
export const createSuccessResponse = (data, message = '操作成功') => {
  return {
    code: 200,
    message,
    data,
    timestamp: Date.now()
  }
}

/**
 * 创建错误响应
 * @param {string} message 错误消息
 * @param {number} code 错误码
 * @returns {Object} 标准错误响应格式
 */
export const createErrorResponse = (message = '操作失败', code = 400) => {
  return {
    code,
    message,
    data: null,
    timestamp: Date.now()
  }
}

/**
 * 创建分页响应
 * @param {Array} list 数据列表
 * @param {number} total 总数
 * @param {number} page 当前页
 * @param {number} size 每页大小
 * @returns {Object} 分页响应格式
 */
export const createPageResponse = (list, total, page = 1, size = 10) => {
  return createSuccessResponse({
    list,
    total,
    page,
    size,
    hasMore: page * size < total,
    totalPages: Math.ceil(total / size)
  })
}

/**
 * 模拟分页数据
 * @param {Array} dataList 完整数据列表
 * @param {number} page 页码
 * @param {number} size 每页大小
 * @returns {Object} 分页数据
 */
export const mockPagination = (dataList, page = 1, size = 10) => {
  const start = (page - 1) * size
  const end = start + size
  const list = dataList.slice(start, end)
  
  return {
    list,
    total: dataList.length,
    page,
    size,
    hasMore: end < dataList.length,
    totalPages: Math.ceil(dataList.length / size)
  }
}

/**
 * 模拟搜索功能
 * @param {Array} dataList 数据列表
 * @param {string} keyword 搜索关键词
 * @param {Array} searchFields 搜索字段
 * @returns {Array} 搜索结果
 */
export const mockSearch = (dataList, keyword, searchFields = ['name', 'title']) => {
  if (!keyword || !keyword.trim()) {
    return dataList
  }
  
  const searchTerm = keyword.toLowerCase().trim()
  
  return dataList.filter(item => {
    return searchFields.some(field => {
      const value = item[field]
      return value && value.toString().toLowerCase().includes(searchTerm)
    })
  })
}

/**
 * 模拟数据过滤
 * @param {Array} dataList 数据列表
 * @param {Object} filters 过滤条件
 * @returns {Array} 过滤结果
 */
export const mockFilter = (dataList, filters = {}) => {
  return dataList.filter(item => {
    return Object.keys(filters).every(key => {
      const filterValue = filters[key]
      const itemValue = item[key]
      
      if (filterValue === null || filterValue === undefined || filterValue === '') {
        return true
      }
      
      if (Array.isArray(filterValue)) {
        return filterValue.includes(itemValue)
      }
      
      return itemValue === filterValue
    })
  })
}

/**
 * 模拟数据排序
 * @param {Array} dataList 数据列表
 * @param {string} sortField 排序字段
 * @param {string} sortOrder 排序方向 asc/desc
 * @returns {Array} 排序结果
 */
export const mockSort = (dataList, sortField, sortOrder = 'asc') => {
  if (!sortField) {
    return dataList
  }
  
  return [...dataList].sort((a, b) => {
    const aValue = a[sortField]
    const bValue = b[sortField]
    
    if (aValue < bValue) {
      return sortOrder === 'asc' ? -1 : 1
    }
    if (aValue > bValue) {
      return sortOrder === 'asc' ? 1 : -1
    }
    return 0
  })
}

/**
 * 生成随机ID
 * @param {string} prefix 前缀
 * @returns {string} 随机ID
 */
export const generateId = (prefix = 'id') => {
  return `${prefix}_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
}

/**
 * 模拟文件上传
 * @param {string} filePath 文件路径
 * @param {Object} options 上传选项
 * @returns {Promise} 上传结果
 */
export const mockUpload = (filePath, options = {}) => {
  return mockRequest(() => {
    // 模拟上传成功
    const fileName = filePath.split('/').pop() || 'file'
    const fileUrl = `https://mock-cdn.marsadmin.com/uploads/${generateId('file')}_${fileName}`
    
    return createSuccessResponse({
      url: fileUrl,
      fileName,
      fileSize: Math.floor(Math.random() * 1000000) + 100000, // 随机文件大小
      uploadTime: new Date().toISOString()
    }, '上传成功')
  }, 2000) // 模拟上传需要2秒
}

/**
 * 模拟删除操作
 * @param {string|Array} ids 要删除的ID
 * @returns {Promise} 删除结果
 */
export const mockDelete = (ids) => {
  return mockRequest(() => {
    const deleteCount = Array.isArray(ids) ? ids.length : 1
    return createSuccessResponse(
      { deleteCount },
      `成功删除${deleteCount}条记录`
    )
  }, 500)
}

/**
 * 模拟更新操作
 * @param {Object} data 更新数据
 * @returns {Promise} 更新结果
 */
export const mockUpdate = (data) => {
  return mockRequest(() => {
    return createSuccessResponse(
      { ...data, updateTime: new Date().toISOString() },
      '更新成功'
    )
  }, 800)
}

/**
 * 模拟创建操作
 * @param {Object} data 创建数据
 * @returns {Promise} 创建结果
 */
export const mockCreate = (data) => {
  return mockRequest(() => {
    return createSuccessResponse(
      { 
        ...data, 
        id: generateId(),
        createTime: new Date().toISOString(),
        updateTime: new Date().toISOString()
      },
      '创建成功'
    )
  }, 1000)
}
