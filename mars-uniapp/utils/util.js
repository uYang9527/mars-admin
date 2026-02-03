/**
 * 工具函数
 */

/**
 * 检查是否登录
 */
export const checkLogin = () => {
	const memberInfo = uni.getStorageSync('memberInfo')
	return !!memberInfo
}

/**
 * 获取会员信息
 */
export const getMemberInfo = () => {
	return uni.getStorageSync('memberInfo')
}

/**
 * 保存会员信息
 */
export const saveMemberInfo = (info) => {
	uni.setStorageSync('memberInfo', info)
	getApp().globalData.memberInfo = info
	getApp().globalData.isLogin = true
}

/**
 * 清除会员信息
 */
export const clearMemberInfo = () => {
	uni.removeStorageSync('memberInfo')
	getApp().globalData.memberInfo = null
	getApp().globalData.isLogin = false
}

/**
 * 跳转到登录页
 */
export const goLogin = () => {
	uni.navigateTo({
		url: '/pages/login/index'
	})
}

/**
 * 需要登录才能执行的操作
 */
export const requireLogin = (callback) => {
	if (checkLogin()) {
		callback && callback()
	} else {
		uni.showModal({
			title: '提示',
			content: '请先登录',
			confirmText: '去登录',
			success: (res) => {
				if (res.confirm) {
					goLogin()
				}
			}
		})
	}
}

/**
 * 格式化价格
 */
export const formatPrice = (price) => {
	if (price === undefined || price === null) return '0.00'
	return parseFloat(price).toFixed(2)
}

/**
 * 格式化时间
 */
export const formatTime = (dateStr) => {
	if (!dateStr) return ''
	const date = new Date(dateStr)
	const year = date.getFullYear()
	const month = String(date.getMonth() + 1).padStart(2, '0')
	const day = String(date.getDate()).padStart(2, '0')
	const hour = String(date.getHours()).padStart(2, '0')
	const minute = String(date.getMinutes()).padStart(2, '0')
	return `${year}-${month}-${day} ${hour}:${minute}`
}

/**
 * 格式化手机号(隐藏中间4位)
 */
export const formatPhone = (phone) => {
	if (!phone) return ''
	return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

/**
 * 订单状态文本
 */
export const getOrderStatusText = (status) => {
	const statusMap = {
		0: '待付款',
		1: '待发货',
		2: '待收货',
		3: '已完成',
		4: '已取消',
		5: '已退款'
	}
	return statusMap[status] || '未知'
}

/**
 * 支付方式文本
 */
export const getPayTypeText = (payType) => {
	const typeMap = {
		1: '微信支付',
		2: '支付宝'
	}
	return typeMap[payType] || '未知'
}

/**
 * 防抖函数
 */
export const debounce = (fn, delay = 300) => {
	let timer = null
	return function(...args) {
		if (timer) clearTimeout(timer)
		timer = setTimeout(() => {
			fn.apply(this, args)
		}, delay)
	}
}

/**
 * 节流函数
 */
export const throttle = (fn, delay = 300) => {
	let last = 0
	return function(...args) {
		const now = Date.now()
		if (now - last >= delay) {
			last = now
			fn.apply(this, args)
		}
	}
}

export default {
	checkLogin,
	getMemberInfo,
	saveMemberInfo,
	clearMemberInfo,
	goLogin,
	requireLogin,
	formatPrice,
	formatTime,
	formatPhone,
	getOrderStatusText,
	getPayTypeText,
	debounce,
	throttle
}
