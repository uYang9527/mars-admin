/**
 * 消息相关API
 */
import request from '@/utils/request'
import { API_ENDPOINTS, API_CONFIG } from '@/api/config'
import { messageList } from '@/mock/index'

/**
 * 获取消息列表
 * @param {Object} params 查询参数
 * @param {string} params.type 消息类型：system, work, approval, notice
 * @param {number} params.page 页码
 * @param {number} params.size 每页数量
 * @param {string} params.status 消息状态：all, read, unread
 * @returns {Promise} 消息列表
 */
export const getMessageList = (params = {}) => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        const { type = 'all', page = 1, size = 10, status = 'all' } = params
        let messages = []
        
        // 根据类型获取消息
        if (type === 'all') {
          messages = [
            ...messageList.system,
            ...messageList.work,
            ...messageList.approval,
            ...messageList.notice
          ]
        } else {
          messages = messageList[type] || []
        }
        
        // 根据状态过滤
        if (status === 'read') {
          messages = messages.filter(msg => msg.read)
        } else if (status === 'unread') {
          messages = messages.filter(msg => !msg.read)
        }
        
        // 分页处理
        const start = (page - 1) * size
        const end = start + size
        const list = messages.slice(start, end)
        
        resolve({
          code: 200,
          message: '获取成功',
          data: {
            list,
            total: messages.length,
            page,
            size,
            hasMore: end < messages.length
          }
        })
      }, 500)
    })
  }
  
  return request.get(API_ENDPOINTS.MESSAGE.LIST, params)
}

/**
 * 获取消息详情
 * @param {string} id 消息ID
 * @returns {Promise} 消息详情
 */
export const getMessageDetail = (id) => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        // 在所有消息中查找
        const allMessages = [
          ...messageList.system,
          ...messageList.work,
          ...messageList.approval,
          ...messageList.notice
        ]
        
        const message = allMessages.find(msg => msg.id === id)
        
        if (message) {
          resolve({
            code: 200,
            message: '获取成功',
            data: message
          })
        } else {
          resolve({
            code: 404,
            message: '消息不存在',
            data: null
          })
        }
      }, 300)
    })
  }
  
  return request.get(API_ENDPOINTS.MESSAGE.DETAIL, { id })
}

/**
 * 标记消息为已读
 * @param {string|Array} ids 消息ID或ID数组
 * @returns {Promise} 操作结果
 */
export const markMessageRead = (ids) => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        const idArray = Array.isArray(ids) ? ids : [ids]
        
        // 在所有消息中标记为已读
        Object.keys(messageList).forEach(type => {
          messageList[type].forEach(msg => {
            if (idArray.includes(msg.id)) {
              msg.read = true
            }
          })
        })
        
        resolve({
          code: 200,
          message: '标记成功',
          data: null
        })
      }, 300)
    })
  }
  
  return request.put(API_ENDPOINTS.MESSAGE.READ, { ids })
}

/**
 * 删除消息
 * @param {string|Array} ids 消息ID或ID数组
 * @returns {Promise} 操作结果
 */
export const deleteMessage = (ids) => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        const idArray = Array.isArray(ids) ? ids : [ids]
        
        // 在所有消息中删除
        Object.keys(messageList).forEach(type => {
          messageList[type] = messageList[type].filter(msg => !idArray.includes(msg.id))
        })
        
        resolve({
          code: 200,
          message: '删除成功',
          data: null
        })
      }, 300)
    })
  }
  
  return request.delete(API_ENDPOINTS.MESSAGE.DELETE, { ids })
}

/**
 * 获取未读消息数量
 * @returns {Promise} 未读消息数量
 */
export const getUnreadCount = () => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        let unreadCount = 0
        
        Object.keys(messageList).forEach(type => {
          unreadCount += messageList[type].filter(msg => !msg.read).length
        })
        
        resolve({
          code: 200,
          message: '获取成功',
          data: {
            total: unreadCount,
            system: messageList.system.filter(msg => !msg.read).length,
            work: messageList.work.filter(msg => !msg.read).length,
            approval: messageList.approval.filter(msg => !msg.read).length,
            notice: messageList.notice.filter(msg => !msg.read).length
          }
        })
      }, 200)
    })
  }
  
  return request.get(API_ENDPOINTS.MESSAGE.UNREAD_COUNT)
}

/**
 * 搜索消息
 * @param {Object} params 搜索参数
 * @param {string} params.keyword 搜索关键词
 * @param {string} params.type 消息类型
 * @param {number} params.page 页码
 * @param {number} params.size 每页数量
 * @returns {Promise} 搜索结果
 */
export const searchMessages = (params = {}) => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        const { keyword, type = 'all', page = 1, size = 10 } = params
        
        if (!keyword) {
          resolve({
            code: 400,
            message: '请输入搜索关键词',
            data: null
          })
          return
        }
        
        let messages = []
        
        // 根据类型获取消息
        if (type === 'all') {
          messages = [
            ...messageList.system,
            ...messageList.work,
            ...messageList.approval,
            ...messageList.notice
          ]
        } else {
          messages = messageList[type] || []
        }
        
        // 搜索过滤
        const searchResults = messages.filter(msg => 
          msg.title.toLowerCase().includes(keyword.toLowerCase()) ||
          msg.content.toLowerCase().includes(keyword.toLowerCase())
        )
        
        // 分页处理
        const start = (page - 1) * size
        const end = start + size
        const list = searchResults.slice(start, end)
        
        resolve({
          code: 200,
          message: '搜索成功',
          data: {
            list,
            total: searchResults.length,
            page,
            size,
            hasMore: end < searchResults.length,
            keyword
          }
        })
      }, 800)
    })
  }
  
  return request.get(API_ENDPOINTS.MESSAGE.LIST, { ...params, search: params.keyword })
}

/**
 * 清空指定类型的消息
 * @param {string} type 消息类型：system, work, approval, notice, all
 * @returns {Promise} 操作结果
 */
export const clearMessages = (type = 'all') => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        if (type === 'all') {
          Object.keys(messageList).forEach(key => {
            messageList[key] = []
          })
        } else if (messageList[type]) {
          messageList[type] = []
        }
        
        resolve({
          code: 200,
          message: '清空成功',
          data: null
        })
      }, 500)
    })
  }
  
  return request.delete(API_ENDPOINTS.MESSAGE.DELETE, { type })
}
