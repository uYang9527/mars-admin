// 购物车管理工具
const CART_KEY = 'cart_list'

// 获取购物车列表
export function getCartList() {
	try {
		const cartData = uni.getStorageSync(CART_KEY)
		return cartData ? JSON.parse(cartData) : []
	} catch (e) {
		return []
	}
}

// 保存购物车列表
export function saveCartList(list) {
	try {
		uni.setStorageSync(CART_KEY, JSON.stringify(list))
		return true
	} catch (e) {
		return false
	}
}

// 添加商品到购物车
export function addToCart(product) {
	const cartList = getCartList()
	
	// 查找是否已存在
	const existIndex = cartList.findIndex(item => item.id === product.id)
	
	if (existIndex > -1) {
		// 已存在，数量+1
		cartList[existIndex].quantity += 1
	} else {
		// 不存在，新增
		cartList.push({
			id: product.id,
			name: product.name,
			desc: product.desc,
			price: product.price,
			image: product.image,
			spec: product.desc || '默认规格',
			quantity: 1,
			selected: true
		})
	}
	
	saveCartList(cartList)
	
	// 更新 TabBar 角标
	updateCartBadge(cartList)
	
	return cartList.length
}

// 更新购物车数量
export function updateCartQuantity(id, quantity) {
	const cartList = getCartList()
	const item = cartList.find(item => item.id === id)
	
	if (item) {
		item.quantity = Math.max(1, quantity)
		saveCartList(cartList)
		updateCartBadge(cartList)
	}
}

// 切换选中状态
export function toggleCartSelect(id) {
	const cartList = getCartList()
	const item = cartList.find(item => item.id === id)
	
	if (item) {
		item.selected = !item.selected
		saveCartList(cartList)
	}
	
	return item ? item.selected : false
}

// 全选/取消全选
export function toggleSelectAll(selectAll) {
	const cartList = getCartList()
	cartList.forEach(item => {
		item.selected = selectAll
	})
	saveCartList(cartList)
}

// 删除购物车商品
export function removeCartItem(id) {
	let cartList = getCartList()
	cartList = cartList.filter(item => item.id !== id)
	saveCartList(cartList)
	updateCartBadge(cartList)
}

// 清空购物车
export function clearCart() {
	saveCartList([])
	updateCartBadge([])
}

// 获取购物车总数
export function getCartCount() {
	const cartList = getCartList()
	return cartList.reduce((total, item) => total + item.quantity, 0)
}

// 获取选中商品总价
export function getSelectedTotal() {
	const cartList = getCartList()
	return cartList
		.filter(item => item.selected)
		.reduce((total, item) => total + parseFloat(item.price) * item.quantity, 0)
		.toFixed(1)
}

// 获取选中商品数量
export function getSelectedCount() {
	const cartList = getCartList()
	return cartList.filter(item => item.selected).length
}

// 更新 TabBar 角标
export function updateCartBadge(cartList) {
	const count = cartList ? cartList.reduce((total, item) => total + item.quantity, 0) : getCartCount()
	
	if (count > 0) {
		uni.setTabBarBadge({
			index: 2, // 购物车是第3个tab（index从0开始）
			text: count > 99 ? '99+' : count.toString()
		})
	} else {
		uni.removeTabBarBadge({
			index: 2
		})
	}
}
