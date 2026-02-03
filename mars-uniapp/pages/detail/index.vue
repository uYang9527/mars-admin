<template>
	<view class="page">
		<!-- 顶部导航 -->
		<view class="nav-bar" :style="{ paddingTop: statusBarHeight + 'px' }">
			<view class="nav-btn" @click="goBack">
				<text class="fas fa-arrow-left"></text>
			</view>
			<view class="nav-btn">
				<text class="fas fa-share-alt"></text>
			</view>
		</view>

		<!-- 内容区域 -->
		<scroll-view class="content" scroll-y>
			<!-- 商品图片 -->
			<view class="product-image-wrap">
				<image class="product-image" :src="product.image" mode="aspectFill"></image>
			</view>

			<!-- 商品信息 -->
			<view class="product-info">
				<view class="info-header">
					<text class="product-title">{{ product.name }}</text>
					<view class="favorite-wrap" @click="toggleFavorite">
						<text :class="isFavorite ? 'fas fa-heart' : 'far fa-heart'"></text>
						<text class="favorite-text">收藏</text>
					</view>
				</view>

				<text class="product-desc">{{ product.desc }}</text>

				<view class="price-wrap">
					<text class="price">¥{{ product.price }}</text>
					<text class="origin-price">¥{{ product.originPrice }}</text>
					<view class="tag">限时特惠</view>
				</view>

				<!-- 规格选择 -->
				<view class="specs-section">
					<text class="section-title">规格</text>
					<view class="specs-list">
						<view
							class="spec-item"
							:class="{ active: activeSpec === spec.id }"
							v-for="spec in specs"
							:key="spec.id"
							@click="selectSpec(spec)"
						>
							<text>{{ spec.name }}</text>
						</view>
					</view>
				</view>

				<!-- 配送信息 -->
				<view class="delivery-info">
					<text class="fas fa-truck-fast"></text>
					<view class="delivery-text">
						<text class="delivery-title">最快明日送达</text>
						<text class="delivery-desc">现在下单，预计明日 09:00-15:00 送达</text>
					</view>
				</view>

				<!-- 商品详情 -->
				<view class="detail-section">
					<text class="section-title">商品详情</text>
					<view class="detail-placeholder">
						<text>[ 图文详情加载中... ]</text>
					</view>
				</view>
			</view>
		</scroll-view>

		<!-- 底部操作栏 -->
		<view class="action-bar">
			<view class="action-btns">
				<view class="action-btn" @click="goHome">
					<text class="fas fa-store"></text>
					<text class="btn-text">店铺</text>
				</view>
				<view class="action-btn" @click="goCart">
					<text class="fas fa-shopping-cart"></text>
					<view class="cart-badge" v-if="cartCount"></view>
					<text class="btn-text">购物车</text>
				</view>
			</view>
			<view class="action-buttons">
				<button class="btn-add" @click="addCart">加入购物车</button>
				<button class="btn-buy" @click="buyNow">立即购买</button>
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
import { addToCart as addLocalCart, getCartCount, updateCartBadge } from '@/utils/cart.js'
import { getProductDetail, addToCart as apiAddToCart, toggleFavorite as apiToggleFavorite } from '@/utils/api.js'

export default {
	data() {
		return {
			statusBarHeight: 0,
			productId: null,
			loading: true,
			product: {
				id: null,
				name: '',
				desc: '',
				price: '0',
				originPrice: '0',
				image: '',
				detail: ''
			},
			specs: [],
			activeSpec: null,
			selectedSku: null,
			isFavorite: false,
			cartCount: 0,
			showToast: false,
			toastText: ''
		}
	},
	onLoad(options) {
		const systemInfo = uni.getSystemInfoSync()
		this.statusBarHeight = systemInfo.statusBarHeight || 0

		if (options.id) {
			this.productId = options.id
			this.loadProductDetail()
		}

		// 获取购物车数量
		this.cartCount = getCartCount()
	},
	onShow() {
		// 更新购物车数量
		this.cartCount = getCartCount()
	},
	methods: {
		// 加载商品详情
		async loadProductDetail() {
			this.loading = true
			try {
				const res = await getProductDetail(this.productId)
				if (res.code === 200 && res.data) {
					const data = res.data
					this.product = {
						id: data.id,
						name: data.name,
						desc: data.subtitle,
						price: data.price,
						originPrice: data.originalPrice || data.price,
						image: data.mainImage,
						images: data.images ? JSON.parse(data.images) : [],
						detail: data.detail || ''
					}
					
					// 处理SKU列表
					if (data.skuList && data.skuList.length > 0) {
						this.specs = data.skuList.map(sku => ({
							id: sku.id,
							name: sku.skuName,
							price: sku.price,
							stock: sku.stock
						}))
						// 默认选中第一个
						this.activeSpec = this.specs[0].id
						this.selectedSku = this.specs[0]
					}
					
					// 收藏状态
					this.isFavorite = data.isFavorite || false
				}
			} catch (e) {
				console.error('加载商品详情失败', e)
				this.showToastMessage('加载失败，请重试')
			} finally {
				this.loading = false
			}
		},
		goBack() {
			uni.navigateBack()
		},
		goHome() {
			uni.switchTab({ url: '/pages/index/index' })
		},
		goCart() {
			uni.switchTab({ url: '/pages/cart/index' })
		},
		async toggleFavorite() {
			const memberInfo = uni.getStorageSync('memberInfo')
			if (!memberInfo || !memberInfo.memberId) {
				uni.navigateTo({ url: '/pages/login/index' })
				return
			}
			
			try {
				const res = await apiToggleFavorite(this.productId)
				if (res.code === 200) {
					this.isFavorite = res.data
					this.showToastMessage(this.isFavorite ? '已收藏' : '已取消收藏')
				}
			} catch (e) {
				console.error('收藏操作失败', e)
			}
		},
		selectSpec(spec) {
			this.activeSpec = spec.id
			this.selectedSku = spec
			// 更新价格
			if (spec.price) {
				this.product.price = spec.price
			}
		},
		async addCart() {
			const memberInfo = uni.getStorageSync('memberInfo')
			if (memberInfo && memberInfo.memberId) {
				// 已登录，调用后端API
				try {
					await apiAddToCart(this.productId, this.activeSpec, 1)
					updateCartBadge()
					this.cartCount = getCartCount()
					this.showToastMessage('已加入购物车')
				} catch (e) {
					console.error('添加购物车失败', e)
				}
			} else {
				// 未登录，使用本地购物车
				addLocalCart({
					id: this.product.id,
					name: this.product.name,
					image: this.product.image,
					price: this.product.price,
					spec: this.selectedSku ? this.selectedSku.name : ''
				})
				this.cartCount = getCartCount()
				this.showToastMessage('已加入购物车')
			}
		},
		async buyNow() {
			const memberInfo = uni.getStorageSync('memberInfo')
			if (!memberInfo || !memberInfo.memberId) {
				// 未登录，先跳转登录
				uni.navigateTo({ url: '/pages/login/index' })
				return
			}
			
			// 已登录，添加到购物车后跳转确认订单
			try {
				await apiAddToCart(this.productId, this.activeSpec, 1)
				updateCartBadge()
				// 跳转到确认订单页面（直接购买）
				uni.navigateTo({ 
					url: `/pages/order/confirm?productId=${this.productId}&skuId=${this.activeSpec || ''}&quantity=1&direct=1` 
				})
			} catch (e) {
				console.error('立即购买失败', e)
			}
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
	position: relative;
	display: flex;
	flex-direction: column;
}

.nav-bar {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	display: flex;
	justify-content: space-between;
	padding: 12rpx 32rpx;
	z-index: 100;
	box-sizing: border-box;
}

.nav-btn {
	width: 72rpx;
	height: 72rpx;
	background: rgba(255, 255, 255, 0.8);
	backdrop-filter: blur(10px);
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);

	.fa {
		font-size: 32rpx;
		color: #1e293b;
	}
}

.content {
	width: 100%;
	padding-bottom: 140rpx;
	box-sizing: border-box;
}

.product-image-wrap {
	width: 100%;
	height: 800rpx;
	background: #fff;
}

.product-image {
	width: 100%;
	height: 100%;
}

.product-info {
	background: #fff;
	border-radius: 48rpx 48rpx 0 0;
	margin-top: -48rpx;
	position: relative;
	z-index: 10;
	padding: 48rpx;
	padding-bottom: 24rpx;
	box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.05);
}

.info-header {
	display: flex;
	justify-content: space-between;
	align-items: flex-start;
	margin-bottom: 16rpx;
}

.product-title {
	flex: 1;
	font-size: 40rpx;
	font-weight: 700;
	color: #1e293b;
	line-height: 1.4;
	padding-right: 24rpx;
}

.favorite-wrap {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 8rpx;

	.fa {
		font-size: 40rpx;
		color: #94a3b8;
		transition: color 0.3s;

		&.fas {
			color: #ef4444;
		}
	}

	.favorite-text {
		font-size: 20rpx;
		color: #94a3b8;
	}
}

.product-desc {
	font-size: 28rpx;
	color: #64748b;
	margin-bottom: 32rpx;
	display: block;
}

.price-wrap {
	display: flex;
	align-items: flex-end;
	gap: 16rpx;
	padding-bottom: 32rpx;
	border-bottom: 2rpx solid #f1f5f9;
}

.price {
	font-size: 48rpx;
	font-weight: 700;
	color: #059669;
}

.origin-price {
	font-size: 28rpx;
	color: #94a3b8;
	text-decoration: line-through;
	margin-bottom: 6rpx;
}

.tag {
	background: rgba(5, 150, 105, 0.1);
	color: #059669;
	font-size: 20rpx;
	font-weight: 600;
	padding: 4rpx 16rpx;
	border-radius: 8rpx;
	margin-bottom: 8rpx;
}

.specs-section {
	margin-top: 32rpx;
}

.delivery-info {
	margin-top: 24rpx;
}

.detail-section {
	margin-top: 24rpx;
	margin-bottom: 0;
}

.section-title {
	font-size: 28rpx;
	font-weight: 700;
	color: #1e293b;
	display: block;
	margin-bottom: 24rpx;
}

.specs-list {
	display: flex;
	gap: 24rpx;
}

.spec-item {
	padding: 16rpx 32rpx;
	background: #f8fafc;
	border: 2rpx solid transparent;
	border-radius: 16rpx;
	font-size: 28rpx;
	color: #64748b;
	transition: all 0.3s;

	&.active {
		background: rgba(5, 150, 105, 0.05);
		border-color: #059669;
		color: #059669;
		font-weight: 600;
	}
}

.delivery-info {
	display: flex;
	align-items: flex-start;
	gap: 24rpx;
	background: #f8fafc;
	padding: 24rpx;
	border-radius: 24rpx;

	.fa {
		font-size: 32rpx;
		color: #059669;
		margin-top: 4rpx;
	}
}

.delivery-text {
	flex: 1;
}

.delivery-title {
	font-size: 28rpx;
	font-weight: 700;
	color: #1e293b;
	display: block;
	margin-bottom: 8rpx;
}

.delivery-desc {
	font-size: 24rpx;
	color: #94a3b8;
}

.detail-placeholder {
	width: 100%;
	height: 160rpx;
	background: #f8fafc;
	border-radius: 16rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	color: #94a3b8;
	font-size: 28rpx;
}

.action-bar {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: #fff;
	border-top: 2rpx solid #f1f5f9;
	padding: 20rpx 32rpx;
	padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
	display: flex;
	align-items: center;
	gap: 24rpx;
	z-index: 100;
	box-sizing: border-box;
}

.action-btns {
	display: flex;
	gap: 32rpx;
	padding: 0 16rpx;
}

.action-btn {
	display: flex;
	flex-direction: column;
	align-items: center;
	position: relative;

	.fa {
		font-size: 40rpx;
		color: #64748b;
		margin-bottom: 8rpx;
	}

	.cart-badge {
		position: absolute;
		top: -8rpx;
		right: -8rpx;
		width: 28rpx;
		height: 28rpx;
		background: #ef4444;
		border-radius: 50%;
		border: 2rpx solid #fff;
	}

	.btn-text {
		font-size: 22rpx;
		color: #64748b;
	}
}

.action-buttons {
	flex: 1;
	display: flex;
	gap: 0;
}

.btn-add, .btn-buy {
	flex: 1;
	height: 88rpx;
	font-size: 30rpx;
	font-weight: 700;
	border: none;
	display: flex;
	align-items: center;
	justify-content: center;

	&::after {
		border: none;
	}
}

.btn-add {
	background: rgba(5, 150, 105, 0.1);
	color: #059669;
	border-radius: 88rpx 0 0 88rpx;
}

.btn-buy {
	background: #059669;
	color: #fff;
	border-radius: 0 88rpx 88rpx 0;
	box-shadow: 0 8rpx 24rpx rgba(5, 150, 105, 0.2);
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
