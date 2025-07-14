/**
 * 交互效果工具
 * 专注于前端展示和用户交互体验
 */

/**
 * 模拟加载效果
 * @param {string} title 加载提示文字
 * @param {number} duration 加载时长(ms)
 * @returns {Promise} 加载完成
 */
export const simulateLoading = (title = '加载中...', duration = 1000) => {
  uni.showLoading({ title })
  
  return new Promise((resolve) => {
    setTimeout(() => {
      uni.hideLoading()
      resolve()
    }, duration)
  })
}

/**
 * 模拟操作成功
 * @param {string} message 成功消息
 * @param {Function} callback 回调函数
 */
export const simulateSuccess = (message = '操作成功', callback) => {
  uni.showToast({
    title: message,
    icon: 'success',
    duration: 1500
  })
  
  if (callback) {
    setTimeout(callback, 1500)
  }
}

/**
 * 模拟操作失败
 * @param {string} message 失败消息
 * @param {Function} callback 回调函数
 */
export const simulateError = (message = '操作失败', callback) => {
  uni.showToast({
    title: message,
    icon: 'error',
    duration: 2000
  })
  
  if (callback) {
    setTimeout(callback, 2000)
  }
}

/**
 * 模拟确认对话框
 * @param {string} content 对话框内容
 * @param {string} title 对话框标题
 * @returns {Promise<boolean>} 用户选择结果
 */
export const simulateConfirm = (content, title = '提示') => {
  return new Promise((resolve) => {
    uni.showModal({
      title,
      content,
      confirmText: '确定',
      cancelText: '取消',
      success: (res) => {
        resolve(res.confirm)
      }
    })
  })
}

/**
 * 模拟下拉刷新
 * @param {Function} refreshCallback 刷新回调
 * @param {number} duration 刷新时长
 */
export const simulateRefresh = async (refreshCallback, duration = 1000) => {
  try {
    if (refreshCallback) {
      await refreshCallback()
    }
    
    // 模拟刷新延迟
    await new Promise(resolve => setTimeout(resolve, duration))
    
    uni.showToast({
      title: '刷新成功',
      icon: 'success',
      duration: 1000
    })
  } catch (error) {
    uni.showToast({
      title: '刷新失败',
      icon: 'error',
      duration: 1500
    })
  } finally {
    uni.stopPullDownRefresh()
  }
}

/**
 * 模拟上拉加载更多
 * @param {Function} loadMoreCallback 加载更多回调
 * @param {boolean} hasMore 是否还有更多数据
 */
export const simulateLoadMore = async (loadMoreCallback, hasMore = true) => {
  if (!hasMore) {
    uni.showToast({
      title: '没有更多数据了',
      icon: 'none',
      duration: 1000
    })
    return
  }
  
  try {
    uni.showLoading({ title: '加载中...' })
    
    if (loadMoreCallback) {
      await loadMoreCallback()
    }
    
    // 模拟加载延迟
    await new Promise(resolve => setTimeout(resolve, 800))
    
    uni.hideLoading()
  } catch (error) {
    uni.hideLoading()
    uni.showToast({
      title: '加载失败',
      icon: 'error',
      duration: 1500
    })
  }
}

/**
 * 模拟搜索
 * @param {string} keyword 搜索关键词
 * @param {Function} searchCallback 搜索回调
 */
export const simulateSearch = async (keyword, searchCallback) => {
  if (!keyword || !keyword.trim()) {
    uni.showToast({
      title: '请输入搜索关键词',
      icon: 'none',
      duration: 1500
    })
    return
  }
  
  try {
    uni.showLoading({ title: '搜索中...' })
    
    // 模拟搜索延迟
    await new Promise(resolve => setTimeout(resolve, 600))
    
    if (searchCallback) {
      await searchCallback(keyword.trim())
    }
    
    uni.hideLoading()
  } catch (error) {
    uni.hideLoading()
    uni.showToast({
      title: '搜索失败',
      icon: 'error',
      duration: 1500
    })
  }
}

/**
 * 模拟文件上传
 * @param {string} filePath 文件路径
 * @param {Function} uploadCallback 上传回调
 */
export const simulateUpload = async (filePath, uploadCallback) => {
  try {
    uni.showLoading({ title: '上传中...' })
    
    // 模拟上传进度
    for (let i = 0; i <= 100; i += 20) {
      await new Promise(resolve => setTimeout(resolve, 200))
      uni.showLoading({ title: `上传中...${i}%` })
    }
    
    if (uploadCallback) {
      await uploadCallback(filePath)
    }
    
    uni.hideLoading()
    uni.showToast({
      title: '上传成功',
      icon: 'success',
      duration: 1500
    })
  } catch (error) {
    uni.hideLoading()
    uni.showToast({
      title: '上传失败',
      icon: 'error',
      duration: 1500
    })
  }
}

/**
 * 模拟删除操作
 * @param {string} itemName 删除项名称
 * @param {Function} deleteCallback 删除回调
 */
export const simulateDelete = async (itemName, deleteCallback) => {
  const confirmed = await simulateConfirm(`确定要删除"${itemName}"吗？`, '删除确认')
  
  if (confirmed) {
    try {
      uni.showLoading({ title: '删除中...' })
      
      // 模拟删除延迟
      await new Promise(resolve => setTimeout(resolve, 500))
      
      if (deleteCallback) {
        await deleteCallback()
      }
      
      uni.hideLoading()
      uni.showToast({
        title: '删除成功',
        icon: 'success',
        duration: 1500
      })
    } catch (error) {
      uni.hideLoading()
      uni.showToast({
        title: '删除失败',
        icon: 'error',
        duration: 1500
      })
    }
  }
}

/**
 * 模拟批量操作
 * @param {Array} items 操作项列表
 * @param {string} action 操作名称
 * @param {Function} batchCallback 批量操作回调
 */
export const simulateBatchOperation = async (items, action, batchCallback) => {
  if (!items || items.length === 0) {
    uni.showToast({
      title: '请选择要操作的项目',
      icon: 'none',
      duration: 1500
    })
    return
  }
  
  const confirmed = await simulateConfirm(`确定要${action}选中的${items.length}个项目吗？`, '批量操作确认')
  
  if (confirmed) {
    try {
      uni.showLoading({ title: `${action}中...` })
      
      // 模拟批量操作延迟
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      if (batchCallback) {
        await batchCallback(items)
      }
      
      uni.hideLoading()
      uni.showToast({
        title: `${action}成功`,
        icon: 'success',
        duration: 1500
      })
    } catch (error) {
      uni.hideLoading()
      uni.showToast({
        title: `${action}失败`,
        icon: 'error',
        duration: 1500
      })
    }
  }
}
