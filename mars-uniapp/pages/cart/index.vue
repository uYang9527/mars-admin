<template>
	<view class="page">
		<!-- 头部 -->
		<view class="header">
			<view :style="{ height: statusBarHeight + 'px' }"></view>
			<view class="header-content">
				<text class="header-title">购物车 <text class="header-count">({{ cartList.length }})</text></text>
				<text class="header-btn" @click="toggleManage">{{ isManage ? '完成' : '管理' }}</text>
			</view>
		</view>
		
		<!-- 购物车列表 -->
		<scroll-view class="content" scroll-y>
			<view class="cart-list">
				<view class="cart-item" v-for="(item, index) in cartList" :key="index">
					<view class="item-checkbox" @click="toggleSelect(index)">
						<text :class="item.selected ? 'fas fa-check-circle' : 'far fa-circle'"></text>
					</view>
					<image class="item-image" :src="item.image" mode="aspectFill"></image>
					<view class="item-info">
						<text class="item-name">{{ item.name }}</text>
						<view class="item-spec">{{ item.spec }}</view>
						<view class="item-bottom">
							<text class="item-price">¥{{ item.price }}</text>
							<view class="item-stepper">
								<view class="stepper-btn" @click="changeQuantity(index, -1)">
									<text class="fas fa-minus"></text>
								</view>
								<text class="stepper-value">{{ item.quantity }}</text>
								<view class="stepper-btn" @click="changeQuantity(index, 1)">
									<text class="fas fa-plus"></text>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
			
			<view class="safe-bottom"></view>
		</scroll-view>
		
		<!-- 结算栏 -->
		<view class="checkout-bar">
			<view class="checkout-left">
				<view class="select-all" @click="toggleSelectAll">
					<text :class="isAllSelected ? 'fas fa-check-circle' : 'far fa-circle'"></text>
					<text class="select-text">全选</text>
				</view>
			</view>
			<view class="checkout-right">
				<view class="total-info">
					<view class="total-line">
						<text class="total-label">合计: </text>
						<text class="total-price">¥{{ totalPrice }}</text>
					</view>
					<text class="total-desc">不含运费</text>
				</view>
				<button class="checkout-btn" @click="goCheckout">去结算 ({{ selectedCount }})</button>
			</view>
		</view>
		
		<!-- Toast -->
		<view class="toast" :class="{ show: showToast }">
			<text class="fas fa-check-circle"></text>
			<text>{{ toastText }}</text>
		</view>
	</view>
</template>

<script>
import { getCartList as getLocalCartList, updateCartQuantity as updateLocalCartQuantity, 
         toggleCartSelect as toggleLocalCartSelect, toggleSelectAll as toggleLocalSelectAll, updateCartBadge } from '@/utils/cart.js'
import { getCartList as apiGetCartList, updateCartQuantity as apiUpdateCartQuantity,
         updateCartSelected as apiUpdateCartSelected, selectAllCart as apiSelectAllCart, deleteCart as apiDeleteCart } from '@/utils/api.js'

export default {
	data() {
		return {
			statusBarHeight: 0,
			isManage: false,
			cartList: [],
			showToast: false,
			toastText: '',
			isLoggedIn: false,
			loading: false
		}
	},
	computed: {
		isAllSelected() {
			return this.cartList.length > 0 && this.cartList.every(item => item.selected)
		},
		selectedCount() {
			return this.cartList.filter(item => item.selected).length
		},
		totalPrice() {
			return this.cartList
				.filter(item => item.selected)
				.reduce((total, item) => total + parseFloat(item.price) * item.quantity, 0)
				.toFixed(2)
		}
	},
	onLoad() {
		const systemInfo = uni.getSystemInfoSync()
		this.statusBarHeight = systemInfo.statusBarHeight || 0
	},
	onShow() {
		// 检查登录状态
		const memberInfo = uni.getStorageSync('memberInfo')
		this.isLoggedIn = !!(memberInfo && memberInfo.memberId)
		// 每次显示都刷新数据
		this.loadCartData()
	},
	methods: {
		// 加载购物车数据
		async loadCartData() {
			if (this.isLoggedIn) {
				// 已登录，从后端获取
				this.loading = true
				try {
					const res = await apiGetCartList()
					if (res.code === 200 && res.data) {
						this.cartList = res.data.map(item => ({
							id: item.id,
							productId: item.productId,
							skuId: item.skuId,
							name: item.productName,
							image: item.productImage,
							spec: item.skuName || '',
							price: item.skuPrice || item.productPrice,
							quantity: item.quantity,
							selected: item.selected === 1
						}))
					}
				} catch (e) {
					console.error('加载购物车失败', e)
				} finally {
					this.loading = false
				}
			} else {
				// 未登录，使用本地购物车
				this.cartList = getLocalCartList()
			}
		},
		toggleManage() {
			this.isManage = !this.isManage
			this.showToastMessage(this.isManage ? '进入管理模式' : '退出管理模式')
		},
		async toggleSelect(index) {
			const item = this.cartList[index]
			if (this.isLoggedIn) {
				try {
					await apiUpdateCartSelected(item.id, item.selected ? 0 : 1)
					item.selected = !item.selected
				} catch (e) {
					console.error('更新选中状态失败', e)
				}
			} else {
				toggleLocalCartSelect(item.id)
				this.loadCartData()
			}
		},
		async toggleSelectAll() {
			const allSelected = this.isAllSelected
			if (this.isLoggedIn) {
				try {
					await apiSelectAllCart(allSelected ? 0 : 1)
					this.cartList.forEach(item => item.selected = !allSelected)
				} catch (e) {
					console.error('更新全选状态失败', e)
				}
			} else {
				toggleLocalSelectAll(!allSelected)
				this.loadCartData()
			}
		},
		async changeQuantity(index, delta) {
			const item = this.cartList[index]
			const newQuantity = Math.max(1, item.quantity + delta)
			
			if (this.isLoggedIn) {
				try {
					await apiUpdateCartQuantity(item.id, newQuantity)
					item.quantity = newQuantity
					updateCartBadge()
				} catch (e) {
					console.error('更新数量失败', e)
				}
			} else {
				updateLocalCartQuantity(item.id, newQuantity)
				this.loadCartData()
				updateCartBadge()
			}
		},
		goCheckout() {
			if (this.selectedCount === 0) {
				this.showToastMessage('请选择商品')
				return
			}
			// 检查是否登录
			if (!this.isLoggedIn) {
				uni.navigateTo({ url: '/pages/login/index' })
				return
			}
			uni.navigateTo({ url: '/pages/order/index' })
		},
		showToastMessage(text) {
			this.toastText = text
			this.showToast = true
			setTimeout(() => {
				this.showToast = false
			}, 2000)
		}
	}
}
</script>

<style lang="scss" scoped>

.page {
	width: 100%;
	min-height: 100vh;
	background: #f8fafc;
	display: flex;
	flex-direction: column;
}

.header {
	background: #fff;
	position: sticky;
	top: 0;
	z-index: 10;
}

.header-content {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 28rpx 32rpx 32rpx;
}

.header-title {
	font-size: 40rpx;
	font-weight: 700;
	color: #1e293b;
}

.header-count {
	font-size: 28rpx;
	font-weight: 400;
	color: #64748b;
}

.header-btn {
	font-size: 28rpx;
	color: #64748b;
}

.content {
	width: 100%;
	height: calc(100vh - 280rpx);
	padding: 32rpx;
	padding-bottom: 160rpx;
	box-sizing: border-box;
}

.cart-list {
	display: flex;
	flex-direction: column;
	gap: 32rpx;
}

.cart-item {
	background: #fff;
	border-radius: 32rpx;
	padding: 32rpx;
	display: flex;
	gap: 24rpx;
	box-shadow: 0 2rpx 16rpx rgba(0,0,0,0.03);
}

.item-checkbox {
	width: 48rpx;
	height: 48rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	
	.fa {
		font-size: 36rpx;
		color: #cbd5e1;
		transition: color 0.3s;
		
		&.fa-check-circle {
			color: #059669;
		}
	}
}

.item-image {
	width: 160rpx;
	height: 160rpx;
	border-radius: 16rpx;
	background: #f8fafc;
	flex-shrink: 0;
}

.item-info {
	flex: 1;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
}

.item-name {
	font-size: 28rpx;
	font-weight: 600;
	color: #1e293b;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	margin-bottom: 12rpx;
}

.item-spec {
	display: inline-block;
	background: #f1f5f9;
	color: #64748b;
	font-size: 24rpx;
	padding: 4rpx 16rpx;
	border-radius: 8rpx;
	align-self: flex-start;
	margin-bottom: 16rpx;
}

.item-bottom {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.item-price {
	font-size: 32rpx;
	font-weight: 700;
	color: #059669;
}

.item-stepper {
	display: flex;
	align-items: center;
	gap: 24rpx;
}

.stepper-btn {
	width: 48rpx;
	height: 48rpx;
	border: 2rpx solid #e2e8f0;
	border-radius: 8rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.3s;
	
	.fa {
		font-size: 20rpx;
		color: #64748b;
	}
	
	&:active {
		background: #f8fafc;
	}
}

.stepper-value {
	font-size: 28rpx;
	font-weight: 600;
	color: #1e293b;
	min-width: 48rpx;
	text-align: center;
}

.safe-bottom {
	height: 32rpx;
}

.checkout-bar {
	position: fixed;
	bottom: 50rpx;
	left: 0;
	right: 0;
	background: #fff;
	border-top: 2rpx solid #f1f5f9;
	padding: 24rpx 32rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	box-shadow: 0 -2rpx 16rpx rgba(0,0,0,0.05);
	z-index: 100;
	box-sizing: border-box;
}

.select-all {
	display: flex;
	align-items: center;
	gap: 16rpx;
	
	.fa {
		font-size: 36rpx;
		color: #cbd5e1;
		
		&.fa-check-circle {
			color: #059669;
		}
	}
	
	.select-text {
		font-size: 28rpx;
		color: #64748b;
	}
}

.checkout-right {
	display: flex;
	align-items: center;
	gap: 32rpx;
}

.total-info {
	text-align: right;
}

.total-line {
	margin-bottom: 4rpx;
}

.total-label {
	font-size: 28rpx;
	color: #1e293b;
}

.total-price {
	font-size: 36rpx;
	font-weight: 700;
	color: #059669;
}

.total-desc {
	font-size: 20rpx;
	color: #94a3b8;
}

.checkout-btn {
	background: #059669;
	color: #fff;
	padding: 20rpx 64rpx;
	border-radius: 100rpx;
	font-size: 28rpx;
	font-weight: 700;
	border: none;
	box-shadow: 0 8rpx 24rpx rgba(5, 150, 105, 0.2);
	
	&::after {
		border: none;
	}
}

.toast {
	position: fixed;
	top: 80rpx;
	left: 50%;
	transform: translateX(-50%);
	background: rgba(0,0,0,0.8);
	color: #fff;
	padding: 24rpx 48rpx;
	border-radius: 100rpx;
	display: flex;
	align-items: center;
	gap: 16rpx;
	opacity: 0;
	transition: opacity 0.3s;
	z-index: 9999;
	
	&.show {
		opacity: 1;
	}
	
	.fa {
		color: #059669;
		font-size: 32rpx;
	}
	
	text:last-child {
		font-size: 28rpx;
	}
}
</style>
