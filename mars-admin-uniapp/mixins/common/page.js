/**
 * 页面通用混入
 * 提供页面级别的通用功能
 */

import { showLoading, hideLoading, showError, showSuccess } from '@/utils/common'
import { isLoggedIn, canAccessPage } from '@/utils/permission'

export default {
  data() {
    return {
      // 页面加载状态
      loading: false,
      // 页面数据
      pageData: {},
      // 分页信息
      pagination: {
        page: 1,
        size: 10,
        total: 0,
        hasMore: true
      },
      // 刷新状态
      refreshing: false
    }
  },
  
  onLoad(options) {
    // 页面参数
    this.pageOptions = options || {}
    
    // 权限检查
    this.checkPagePermission()
    
    // 初始化页面
    this.initPage()
  },
  
  onShow() {
    // 页面显示时的处理
    this.onPageShow()
  },
  
  onHide() {
    // 页面隐藏时的处理
    this.onPageHide()
  },
  
  onUnload() {
    // 页面卸载时的处理
    this.onPageUnload()
  },
  
  onPullDownRefresh() {
    // 下拉刷新
    this.handleRefresh()
  },
  
  onReachBottom() {
    // 上拉加载更多
    this.handleLoadMore()
  },
  
  methods: {
    /**
     * 检查页面权限
     */
    checkPagePermission() {
      const currentPages = getCurrentPages()
      const currentPage = currentPages[currentPages.length - 1]
      const route = currentPage.route
      
      if (!canAccessPage('/' + route)) {
        uni.showToast({
          title: '无访问权限',
          icon: 'none'
        })
        
        setTimeout(() => {
          if (!isLoggedIn()) {
            uni.reLaunch({
              url: '/pages/login/login'
            })
          } else {
            uni.navigateBack()
          }
        }, 1500)
      }
    },
    
    /**
     * 初始化页面
     */
    initPage() {
      // 子类可重写此方法
      this.loadPageData()
    },
    
    /**
     * 页面显示时的处理
     */
    onPageShow() {
      // 子类可重写此方法
    },
    
    /**
     * 页面隐藏时的处理
     */
    onPageHide() {
      // 子类可重写此方法
    },
    
    /**
     * 页面卸载时的处理
     */
    onPageUnload() {
      // 清理定时器、取消请求等
      this.cleanup()
    },
    
    /**
     * 加载页面数据
     */
    async loadPageData() {
      // 子类可重写此方法
      console.log('loadPageData - 子类应重写此方法')
    },
    
    /**
     * 处理下拉刷新
     */
    async handleRefresh() {
      try {
        this.refreshing = true
        this.resetPagination()
        await this.loadPageData()
      } catch (error) {
        console.error('刷新失败:', error)
        showError('刷新失败')
      } finally {
        this.refreshing = false
        uni.stopPullDownRefresh()
      }
    },
    
    /**
     * 处理上拉加载更多
     */
    async handleLoadMore() {
      if (!this.pagination.hasMore || this.loading) {
        return
      }
      
      try {
        this.pagination.page++
        await this.loadPageData()
      } catch (error) {
        console.error('加载更多失败:', error)
        this.pagination.page--
        showError('加载失败')
      }
    },
    
    /**
     * 重置分页信息
     */
    resetPagination() {
      this.pagination = {
        page: 1,
        size: 10,
        total: 0,
        hasMore: true
      }
    },
    
    /**
     * 更新分页信息
     * @param {object} data 分页数据
     */
    updatePagination(data) {
      const { total, page, size, hasMore } = data
      this.pagination = {
        ...this.pagination,
        total: total || 0,
        page: page || this.pagination.page,
        size: size || this.pagination.size,
        hasMore: hasMore !== undefined ? hasMore : (this.pagination.page * this.pagination.size < total)
      }
    },
    
    /**
     * 显示加载状态
     * @param {string} title 加载提示文字
     */
    showLoading(title = '加载中...') {
      this.loading = true
      showLoading(title)
    },
    
    /**
     * 隐藏加载状态
     */
    hideLoading() {
      this.loading = false
      hideLoading()
    },
    
    /**
     * 显示成功提示
     * @param {string} message 提示信息
     */
    showSuccess(message) {
      showSuccess(message)
    },
    
    /**
     * 显示错误提示
     * @param {string} message 提示信息
     */
    showError(message) {
      showError(message)
    },
    
    /**
     * 确认对话框
     * @param {string} content 内容
     * @param {string} title 标题
     * @returns {Promise<boolean>} 用户选择结果
     */
    confirm(content, title = '提示') {
      return new Promise((resolve) => {
        uni.showModal({
          title,
          content,
          success: (res) => {
            resolve(res.confirm)
          }
        })
      })
    },
    
    /**
     * 页面跳转
     * @param {string} url 页面路径
     * @param {object} params 参数
     */
    navigateTo(url, params = {}) {
      const query = Object.keys(params).map(key => `${key}=${encodeURIComponent(params[key])}`).join('&')
      const fullUrl = query ? `${url}?${query}` : url
      
      uni.navigateTo({
        url: fullUrl,
        fail: (err) => {
          console.error('页面跳转失败:', err)
        }
      })
    },
    
    /**
     * 页面重定向
     * @param {string} url 页面路径
     * @param {object} params 参数
     */
    redirectTo(url, params = {}) {
      const query = Object.keys(params).map(key => `${key}=${encodeURIComponent(params[key])}`).join('&')
      const fullUrl = query ? `${url}?${query}` : url
      
      uni.redirectTo({
        url: fullUrl,
        fail: (err) => {
          console.error('页面重定向失败:', err)
        }
      })
    },
    
    /**
     * 返回上一页
     * @param {number} delta 返回层数
     */
    navigateBack(delta = 1) {
      uni.navigateBack({
        delta,
        fail: (err) => {
          console.error('页面返回失败:', err)
        }
      })
    },
    
    /**
     * 清理资源
     */
    cleanup() {
      // 子类可重写此方法进行资源清理
    }
  }
}
