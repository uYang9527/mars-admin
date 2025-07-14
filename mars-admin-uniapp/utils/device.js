/**
 * 设备信息工具
 * 获取设备信息、平台判断、屏幕适配等
 */

let systemInfo = null

/**
 * 获取系统信息
 * @param {boolean} force 是否强制重新获取
 * @returns {object} 系统信息
 */
export const getSystemInfo = (force = false) => {
  if (!systemInfo || force) {
    try {
      systemInfo = uni.getSystemInfoSync()
    } catch (error) {
      console.error('获取系统信息失败:', error)
      systemInfo = {}
    }
  }
  return systemInfo
}

/**
 * 获取设备类型
 * @returns {string} 设备类型：mobile, tablet, desktop
 */
export const getDeviceType = () => {
  const info = getSystemInfo()
  const { screenWidth, screenHeight } = info
  
  if (screenWidth < 768) {
    return 'mobile'
  } else if (screenWidth < 1024) {
    return 'tablet'
  } else {
    return 'desktop'
  }
}

/**
 * 判断是否为移动设备
 * @returns {boolean} 是否为移动设备
 */
export const isMobile = () => {
  return getDeviceType() === 'mobile'
}

/**
 * 判断是否为平板设备
 * @returns {boolean} 是否为平板设备
 */
export const isTablet = () => {
  return getDeviceType() === 'tablet'
}

/**
 * 判断是否为桌面设备
 * @returns {boolean} 是否为桌面设备
 */
export const isDesktop = () => {
  return getDeviceType() === 'desktop'
}

/**
 * 获取平台信息
 * @returns {string} 平台：ios, android, h5, mp-weixin, mp-alipay, etc.
 */
export const getPlatform = () => {
  const info = getSystemInfo()
  return info.platform || 'unknown'
}

/**
 * 判断是否为iOS
 * @returns {boolean} 是否为iOS
 */
export const isIOS = () => {
  return getPlatform() === 'ios'
}

/**
 * 判断是否为Android
 * @returns {boolean} 是否为Android
 */
export const isAndroid = () => {
  return getPlatform() === 'android'
}

/**
 * 判断是否为H5
 * @returns {boolean} 是否为H5
 */
export const isH5 = () => {
  // #ifdef H5
  return true
  // #endif
  // #ifndef H5
  return false
  // #endif
}

/**
 * 判断是否为小程序
 * @returns {boolean} 是否为小程序
 */
export const isMiniProgram = () => {
  // #ifdef MP
  return true
  // #endif
  // #ifndef MP
  return false
  // #endif
}

/**
 * 判断是否为微信小程序
 * @returns {boolean} 是否为微信小程序
 */
export const isWechatMiniProgram = () => {
  // #ifdef MP-WEIXIN
  return true
  // #endif
  // #ifndef MP-WEIXIN
  return false
  // #endif
}

/**
 * 判断是否为支付宝小程序
 * @returns {boolean} 是否为支付宝小程序
 */
export const isAlipayMiniProgram = () => {
  // #ifdef MP-ALIPAY
  return true
  // #endif
  // #ifndef MP-ALIPAY
  return false
  // #endif
}

/**
 * 判断是否为App
 * @returns {boolean} 是否为App
 */
export const isApp = () => {
  // #ifdef APP-PLUS
  return true
  // #endif
  // #ifndef APP-PLUS
  return false
  // #endif
}

/**
 * 获取状态栏高度
 * @returns {number} 状态栏高度（px）
 */
export const getStatusBarHeight = () => {
  const info = getSystemInfo()
  return info.statusBarHeight || 0
}

/**
 * 获取导航栏高度
 * @returns {number} 导航栏高度（px）
 */
export const getNavBarHeight = () => {
  const info = getSystemInfo()
  let navBarHeight = 44 // 默认导航栏高度
  
  // #ifdef MP-WEIXIN
  const menuButton = uni.getMenuButtonBoundingClientRect()
  navBarHeight = menuButton.height + (menuButton.top - info.statusBarHeight) * 2
  // #endif
  
  return navBarHeight
}

/**
 * 获取安全区域
 * @returns {object} 安全区域信息
 */
export const getSafeArea = () => {
  const info = getSystemInfo()
  return info.safeArea || {
    left: 0,
    right: info.screenWidth || 0,
    top: 0,
    bottom: info.screenHeight || 0,
    width: info.screenWidth || 0,
    height: info.screenHeight || 0
  }
}

/**
 * 获取安全区域底部高度
 * @returns {number} 安全区域底部高度（px）
 */
export const getSafeAreaBottom = () => {
  const info = getSystemInfo()
  const safeArea = getSafeArea()
  return info.screenHeight - safeArea.bottom
}

/**
 * 获取屏幕宽度
 * @returns {number} 屏幕宽度（px）
 */
export const getScreenWidth = () => {
  const info = getSystemInfo()
  return info.screenWidth || 0
}

/**
 * 获取屏幕高度
 * @returns {number} 屏幕高度（px）
 */
export const getScreenHeight = () => {
  const info = getSystemInfo()
  return info.screenHeight || 0
}

/**
 * 获取窗口宽度
 * @returns {number} 窗口宽度（px）
 */
export const getWindowWidth = () => {
  const info = getSystemInfo()
  return info.windowWidth || 0
}

/**
 * 获取窗口高度
 * @returns {number} 窗口高度（px）
 */
export const getWindowHeight = () => {
  const info = getSystemInfo()
  return info.windowHeight || 0
}

/**
 * 获取像素比
 * @returns {number} 像素比
 */
export const getPixelRatio = () => {
  const info = getSystemInfo()
  return info.pixelRatio || 1
}

/**
 * rpx转px
 * @param {number} rpx rpx值
 * @returns {number} px值
 */
export const rpxToPx = (rpx) => {
  const screenWidth = getScreenWidth()
  return (rpx * screenWidth) / 750
}

/**
 * px转rpx
 * @param {number} px px值
 * @returns {number} rpx值
 */
export const pxToRpx = (px) => {
  const screenWidth = getScreenWidth()
  return (px * 750) / screenWidth
}

/**
 * 获取设备信息摘要
 * @returns {object} 设备信息摘要
 */
export const getDeviceInfo = () => {
  const info = getSystemInfo()
  return {
    platform: getPlatform(),
    deviceType: getDeviceType(),
    brand: info.brand || 'unknown',
    model: info.model || 'unknown',
    system: info.system || 'unknown',
    version: info.version || 'unknown',
    screenWidth: getScreenWidth(),
    screenHeight: getScreenHeight(),
    statusBarHeight: getStatusBarHeight(),
    safeAreaBottom: getSafeAreaBottom(),
    pixelRatio: getPixelRatio()
  }
}
