/**
 * API统一导出文件
 */

// 导入所有API模块
import * as userApi from './modules/user'
import * as homeApi from './modules/home'
import * as messageApi from './modules/message'
import * as organizationApi from './modules/organization'

// 统一导出
export default {
  user: userApi,
  home: homeApi,
  message: messageApi,
  organization: organizationApi
}

// 也可以按需导出
export {
  userApi,
  homeApi,
  messageApi,
  organizationApi
}

// 导出配置
export { API_CONFIG, API_ENDPOINTS, STATUS_CODE, ERROR_MESSAGE, CACHE_CONFIG } from './config'
