/**
 * 首页相关API - 模拟数据版本
 * 专注于前端展示和交互效果
 */
import { mockRequest, createSuccessResponse, mockPagination } from '@/utils/mock'
import { bannerList, quickMenuList, noticeList, workList, statsData } from '@/mock/index'

/**
 * 获取轮播图数据
 * @returns {Promise} 轮播图列表
 */
export const getBannerList = () => {
  return mockRequest(() => {
    return createSuccessResponse(bannerList, '获取成功')
  }, 500)
}

/**
 * 获取快捷菜单数据
 * @returns {Promise} 快捷菜单列表
 */
export const getQuickMenuList = () => {
  return mockRequest(() => {
    return createSuccessResponse(quickMenuList, '获取成功')
  }, 500)
}

/**
 * 获取公告列表
 * @param {Object} params 查询参数
 * @param {number} params.page 页码
 * @param {number} params.size 每页数量
 * @returns {Promise} 公告列表
 */
export const getNoticeList = (params = {}) => {
  return mockRequest(() => {
    const { page = 1, size = 10 } = params
    const start = (page - 1) * size
    const end = start + size
    const list = noticeList.slice(start, end)

    return createSuccessResponse({
      list,
      total: noticeList.length,
      page,
      size,
      hasMore: end < noticeList.length
    }, '获取成功')
  }, 500)
}

/**
 * 获取工作动态列表
 * @param {Object} params 查询参数
 * @param {number} params.page 页码
 * @param {number} params.size 每页数量
 * @returns {Promise} 工作动态列表
 */
export const getWorkList = (params = {}) => {
  return mockRequest(() => {
    const { page = 1, size = 10 } = params
    const start = (page - 1) * size
    const end = start + size
    const list = workList.slice(start, end)

    return createSuccessResponse({
      list,
      total: workList.length,
      page,
      size,
      hasMore: end < workList.length
    }, '获取成功')
  }, 500)
}

/**
 * 获取统计数据
 * @returns {Promise} 统计数据
 */
export const getStatsData = () => {
  return mockRequest(() => {
    return createSuccessResponse(statsData, '获取成功')
  }, 500)
}

/**
 * 获取首页所有数据
 * @returns {Promise} 首页数据
 */
export const getHomeData = () => {
  return mockRequest(() => {
    return createSuccessResponse({
      bannerList,
      quickMenuList,
      noticeList: noticeList.slice(0, 3), // 只返回前3条
      workList: workList.slice(0, 3), // 只返回前3条
      statsData
    }, '获取成功')
  }, 800) // 模拟稍长的加载时间
}
