/**
 * 网络请求封装
 */

// 获取基础URL
const getBaseUrl = () => {
	return getApp().globalData.baseUrl || 'http://localhost:8080'
}

// 获取会员ID
const getMemberId = () => {
	const memberInfo = uni.getStorageSync('memberInfo')
	return memberInfo ? memberInfo.memberId : null
}

// 获取Token
const getToken = () => {
	return uni.getStorageSync('token') || ''
}

/**
 * 发起请求
 */
const request = (options) => {
	return new Promise((resolve, reject) => {
		const baseUrl = getBaseUrl()
		const url = options.url.startsWith('http') ? options.url : baseUrl + options.url
		
		// 添加memberId参数
		let data = options.data || {}
		const memberId = getMemberId()
		if (memberId && !data.memberId) {
			if (options.method === 'GET') {
				data.memberId = memberId
			}
		}
		
		// 构建请求头，添加 Token
		const token = getToken()
		const header = {
			'Content-Type': options.contentType || 'application/json',
			...options.header
		}
		// 后端 Sa-Token 配置使用 'Authorization' 作为 token-name
		if (token) {
			header['Authorization'] = token
		}
		
		uni.request({
			url: url,
			method: options.method || 'GET',
			data: data,
			header: header,
			success: (res) => {
				if (res.statusCode === 200) {
					if (res.data.code === 200) {
						resolve(res.data)
					} else if (res.data.code === 401 || res.data.code === 11011 || res.data.code === 11012) {
						// 未登录或Token失效，清除本地信息并跳转登录页
						uni.removeStorageSync('token')
						uni.removeStorageSync('memberInfo')
						uni.showToast({
							title: '请先登录',
							icon: 'none'
						})
						setTimeout(() => {
							uni.navigateTo({ url: '/pages/login/index' })
						}, 500)
						reject(res.data)
					} else {
						// 业务错误
						uni.showToast({
							title: res.data.message || '请求失败',
							icon: 'none'
						})
						reject(res.data)
					}
				} else if (res.statusCode === 401) {
					// HTTP 401 未授权
					uni.removeStorageSync('token')
					uni.removeStorageSync('memberInfo')
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					})
					setTimeout(() => {
						uni.navigateTo({ url: '/pages/login/index' })
					}, 500)
					reject(res)
				} else {
					uni.showToast({
						title: '网络请求失败',
						icon: 'none'
					})
					reject(res)
				}
			},
			fail: (err) => {
				uni.showToast({
					title: '网络连接失败',
					icon: 'none'
				})
				reject(err)
			}
		})
	})
}

/**
 * GET请求
 */
export const get = (url, data = {}) => {
	return request({ url, method: 'GET', data })
}

/**
 * POST请求
 */
export const post = (url, data = {}) => {
	return request({ url, method: 'POST', data })
}

/**
 * PUT请求
 */
export const put = (url, data = {}) => {
	return request({ url, method: 'PUT', data })
}

/**
 * DELETE请求
 */
export const del = (url, data = {}) => {
	return request({ url, method: 'DELETE', data })
}

export default {
	get,
	post,
	put,
	del,
	request
}
