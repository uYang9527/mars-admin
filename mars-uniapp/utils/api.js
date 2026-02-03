/**
 * API接口定义
 */
import { get, post, put, del } from './request.js'

// 获取会员ID
const getMemberId = () => {
	const memberInfo = uni.getStorageSync('memberInfo')
	return memberInfo ? memberInfo.memberId : null
}

// ==================== 登录相关 ====================

/**
 * 微信授权登录
 * @param code 微信登录code
 * @param nickname 用户昵称
 * @param avatar 用户头像
 */
export const login = (code, nickname, avatar) => {
	return post('/api/mall/login', { code, nickname, avatar })
}

/**
 * 手机号一键登录
 * @param loginCode 微信登录code
 * @param phoneCode 手机号授权code
 * @param nickname 用户昵称
 * @param avatar 用户头像
 */
export const loginByPhone = (loginCode, phoneCode, nickname, avatar) => {
	return post('/api/mall/loginByPhone', { loginCode, phoneCode, nickname, avatar })
}

/**
 * 绑定手机号
 */
export const getPhone = (code) => {
	return post('/api/mall/phone', { code, memberId: getMemberId() })
}

/**
 * 获取会员信息
 */
export const getMemberInfo = () => {
	return get('/api/mall/member/info', { memberId: getMemberId() })
}

/**
 * 更新会员信息
 */
export const updateMemberInfo = (data) => {
	return put('/api/mall/member/info', { ...data, id: getMemberId() })
}

// ==================== 首页数据 ====================

/**
 * 获取首页数据
 */
export const getHomeData = () => {
	return get('/api/mall/home')
}

// ==================== 分类相关 ====================

/**
 * 获取分类树
 */
export const getCategoryTree = () => {
	return get('/api/mall/category/tree')
}

/**
 * 获取分类列表
 */
export const getCategoryList = () => {
	return get('/api/mall/category/list')
}

// ==================== 商品相关 ====================

/**
 * 获取商品列表
 */
export const getProductList = (params) => {
	return get('/api/mall/product/list', params)
}

/**
 * 获取商品详情
 */
export const getProductDetail = (id) => {
	return get(`/api/mall/product/${id}`, { memberId: getMemberId() })
}

// ==================== 购物车相关 ====================

/**
 * 获取购物车列表
 */
export const getCartList = () => {
	return get('/api/mall/cart/list', { memberId: getMemberId() })
}

/**
 * 获取购物车数量
 */
export const getCartCount = () => {
	return get('/api/mall/cart/count', { memberId: getMemberId() })
}

/**
 * 添加到购物车
 */
export const addToCart = (productId, skuId, quantity = 1) => {
	return post('/api/mall/cart/add', {
		memberId: getMemberId(),
		productId,
		skuId,
		quantity
	})
}

/**
 * 更新购物车数量
 */
export const updateCartQuantity = (cartId, quantity) => {
	return put('/api/mall/cart/quantity', {
		memberId: getMemberId(),
		cartId,
		quantity
	})
}

/**
 * 更新购物车选中状态
 */
export const updateCartSelected = (cartId, selected) => {
	return put('/api/mall/cart/selected', {
		memberId: getMemberId(),
		cartId,
		selected
	})
}

/**
 * 全选/取消全选
 */
export const selectAllCart = (selected) => {
	return put('/api/mall/cart/selectAll', {
		memberId: getMemberId(),
		selected
	})
}

/**
 * 删除购物车商品
 */
export const deleteCart = (cartIds) => {
	return del('/api/mall/cart', {
		memberId: getMemberId(),
		cartIds: cartIds.join(',')
	})
}

/**
 * 获取选中商品金额
 */
export const getCartSelectedAmount = () => {
	return get('/api/mall/cart/amount', { memberId: getMemberId() })
}

// ==================== 地址相关 ====================

/**
 * 获取地址列表
 */
export const getAddressList = () => {
	return get('/api/mall/address/list', { memberId: getMemberId() })
}

/**
 * 获取地址详情
 */
export const getAddressDetail = (id) => {
	return get(`/api/mall/address/${id}`)
}

/**
 * 获取默认地址
 */
export const getDefaultAddress = () => {
	return get('/api/mall/address/default', { memberId: getMemberId() })
}

/**
 * 新增地址
 */
export const createAddress = (data) => {
	return post('/api/mall/address', { ...data, memberId: getMemberId() })
}

/**
 * 修改地址
 */
export const updateAddress = (data) => {
	return put('/api/mall/address', data)
}

/**
 * 删除地址
 */
export const deleteAddress = (id) => {
	return del(`/api/mall/address/${id}`, { memberId: getMemberId() })
}

/**
 * 设为默认地址
 */
export const setDefaultAddress = (id) => {
	return put(`/api/mall/address/${id}/default`, { memberId: getMemberId() })
}

// ==================== 订单相关 ====================

/**
 * 获取订单列表
 */
export const getOrderList = (params) => {
	return get('/api/mall/order/list', { ...params, memberId: getMemberId() })
}

/**
 * 获取订单详情
 */
export const getOrderDetail = (id) => {
	return get(`/api/mall/order/${id}`)
}

/**
 * 获取各状态订单数量
 */
export const getOrderCount = () => {
	return get('/api/mall/order/count', { memberId: getMemberId() })
}

/**
 * 从购物车创建订单
 */
export const createOrderFromCart = (addressId, remark, couponId) => {
	return post('/api/mall/order/fromCart', {
		memberId: getMemberId(),
		addressId,
		remark,
		couponId
	})
}

/**
 * 直接购买创建订单
 */
export const createOrderDirect = (productId, skuId, quantity, addressId, remark, couponId) => {
	return post('/api/mall/order/direct', {
		memberId: getMemberId(),
		productId,
		skuId,
		quantity,
		addressId,
		remark,
		couponId
	})
}

/**
 * 取消订单
 */
export const cancelOrder = (id) => {
	return put(`/api/mall/order/${id}/cancel`, { memberId: getMemberId() })
}

/**
 * 支付订单（拉起微信支付）
 */
export const payOrder = (id, payType = 1) => {
	return post(`/api/mall/order/${id}/pay`, { payType })
}

/**
 * 确认收货
 */
export const receiveOrder = (id) => {
	return put(`/api/mall/order/${id}/receive`, { memberId: getMemberId() })
}

/**
 * 删除订单
 */
export const deleteOrder = (id) => {
	return del(`/api/mall/order/${id}`, { memberId: getMemberId() })
}

// ==================== 收藏相关 ====================

/**
 * 获取收藏列表
 */
export const getFavoriteList = (params) => {
	return get('/api/mall/favorite/list', { ...params, memberId: getMemberId() })
}

/**
 * 添加收藏
 */
export const addFavorite = (productId) => {
	return post('/api/mall/favorite', { memberId: getMemberId(), productId })
}

/**
 * 取消收藏
 */
export const removeFavorite = (productId) => {
	return del('/api/mall/favorite', { memberId: getMemberId(), productId })
}

/**
 * 切换收藏状态
 */
export const toggleFavorite = (productId) => {
	return post('/api/mall/favorite/toggle', { memberId: getMemberId(), productId })
}

/**
 * 获取收藏数量
 */
export const getFavoriteCount = () => {
	return get('/api/mall/favorite/count', { memberId: getMemberId() })
}

export default {
	login,
	loginByPhone,
	getPhone,
	getMemberInfo,
	updateMemberInfo,
	getHomeData,
	getCategoryTree,
	getCategoryList,
	getProductList,
	getProductDetail,
	getCartList,
	getCartCount,
	addToCart,
	updateCartQuantity,
	updateCartSelected,
	selectAllCart,
	deleteCart,
	getCartSelectedAmount,
	getAddressList,
	getAddressDetail,
	getDefaultAddress,
	createAddress,
	updateAddress,
	deleteAddress,
	setDefaultAddress,
	getOrderList,
	getOrderDetail,
	getOrderCount,
	createOrderFromCart,
	createOrderDirect,
	cancelOrder,
	payOrder,
	receiveOrder,
	deleteOrder,
	getFavoriteList,
	addFavorite,
	removeFavorite,
	toggleFavorite,
	getFavoriteCount
}
