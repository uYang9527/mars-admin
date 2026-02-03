<template>
	<view class="page">
		<!-- 头部 -->
		<view class="header">
			<view :style="{ height: statusBarHeight + 'px' }"></view>
			<view class="header-content">
				<text class="fas fa-arrow-left" @click="goBack"></text>
				<text class="header-title">确认订单</text>
			</view>
		</view>
		
		<!-- 内容 -->
		<scroll-view class="content" scroll-y>
			<!-- 地址 -->
			<view class="address-card" @click="selectAddress">
				<view class="address-top-bar"></view>
				<view class="address-content">
					<view class="address-icon">
						<text class="fas fa-map-marker-alt"></text>
					</view>
					<view class="address-info">
						<text class="address-user">{{ address.name }} <text class="address-phone">{{ address.phone }}</text></text>
						<text class="address-detail">{{ address.detail }}</text>
					</view>
					<text class="fas fa-chevron-right"></text>
				</view>
			</view>
			
			<!-- 商品预览 -->
			<view class="goods-card">
				<view class="goods-images">
					<image 
						v-for="(item, index) in orderItems" 
						:key="index"
						:src="item.image" 
						mode="aspectFill"
					></image>
				</view>
				<view class="goods-summary">
					<text>共 {{ orderItems.length }} 件商品</text>
					<text>小计: ¥{{ subtotal }}</text>
				</view>
			</view>
			
			<!-- 支付方式 -->
			<view class="payment-card">
				<text class="card-title">支付方式</text>
				<view 
					class="payment-item"
					v-for="(pay, index) in paymentMethods" 
					:key="index"
					@click="selectPayment(pay)"
				>
					<view class="payment-left">
						<text :class="pay.icon" :style="{ color: pay.color }"></text>
						<text class="payment-name">{{ pay.name }}</text>
					</view>
					<text :class="payment === pay.id ? 'fas fa-check-circle checked' : 'far fa-circle'"></text>
				</view>
			</view>
		</scroll-view>
		
		<!-- 底部结算栏 -->
		<view class="footer-bar">
			<view class="total-info">
				<text class="total-label">共{{ orderItems.length }}件</text>
				<text class="total-price">¥{{ subtotal }}</text>
			</view>
			<button class="pay-btn" @click="doPay">立即支付</button>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			statusBarHeight: 0,
			address: {
				name: 'Lisa Wong',
				phone: '138****8888',
				detail: '浙江省 杭州市 西湖区 某某街道 101号'
			},
			orderItems: [
				{ image: 'https://images.unsplash.com/photo-1550258987-190a2d41a8ba?auto=format&fit=crop&w=100', price: 29.9 },
				{ image: 'https://images.unsplash.com/photo-1601004890684-d8cbf643f5f2?auto=format&fit=crop&w=100', price: 45.0 }
			],
			paymentMethods: [
				{ id: 1, name: '微信支付', icon: 'fab fa-weixin', color: '#07c160' },
				{ id: 2, name: '支付宝', icon: 'fab fa-alipay', color: '#1677ff' }
			],
			payment: 2
		}
	},
	computed: {
		subtotal() {
			return this.orderItems.reduce((sum, item) => sum + item.price, 0).toFixed(1)
		}
	},
	onLoad() {
		const systemInfo = uni.getSystemInfoSync()
		this.statusBarHeight = systemInfo.statusBarHeight || 0
	},
	methods: {
		goBack() {
			uni.navigateBack()
		},
		selectAddress() {
			uni.showToast({ title: '选择收货地址', icon: 'none' })
		},
		selectPayment(pay) {
			this.payment = pay.id
			uni.showToast({ title: `已选择${pay.name}`, icon: 'none' })
		},
		doPay() {
			uni.navigateTo({ url: '/pages/order/success' })
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
	box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.03);
}

.header-content {
	display: flex;
	align-items: center;
	gap: 24rpx;
	padding: 28rpx 32rpx 32rpx;
	
	.fa {
		font-size: 36rpx;
		color: #1e293b;
	}
	
	.header-title {
		font-size: 36rpx;
		font-weight: 700;
		color: #1e293b;
	}
}

.content {
	width: 100%;
	height: calc(100vh - 260rpx);
	padding: 32rpx;
	box-sizing: border-box;
}

.address-card, .goods-card, .payment-card {
	background: #fff;
	border-radius: 24rpx;
	padding: 32rpx;
	margin-bottom: 32rpx;
	box-shadow: 0 2rpx 16rpx rgba(0,0,0,0.03);
}

.address-card {
	position: relative;
	overflow: hidden;
}

.address-top-bar {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	height: 8rpx;
	background: repeating-linear-gradient(
		45deg,
		#059669 0,
		#059669 20rpx,
		#fff 20rpx,
		#fff 40rpx,
		#3b82f6 40rpx,
		#3b82f6 60rpx,
		#fff 60rpx,
		#fff 80rpx
	);
}

.address-content {
	display: flex;
	align-items: center;
	gap: 24rpx;
	margin-top: 16rpx;
}

.address-icon {
	width: 64rpx;
	height: 64rpx;
	border-radius: 50%;
	background: rgba(5, 150, 105, 0.1);
	display: flex;
	align-items: center;
	justify-content: center;
	
	.fa {
		font-size: 28rpx;
		color: #059669;
	}
}

.address-info {
	flex: 1;
	display: flex;
	flex-direction: column;
	gap: 8rpx;
}

.address-user {
	font-size: 28rpx;
	font-weight: 700;
	color: #1e293b;
}

.address-phone {
	font-weight: 400;
	color: #64748b;
	margin-left: 16rpx;
}

.address-detail {
	font-size: 24rpx;
	color: #64748b;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.fa-chevron-right {
	font-size: 24rpx;
	color: #cbd5e1;
}

.goods-images {
	display: flex;
	gap: 24rpx;
	margin-bottom: 24rpx;
	overflow-x: auto;
	
	image {
		width: 128rpx;
		height: 128rpx;
		border-radius: 16rpx;
		border: 2rpx solid #f1f5f9;
		flex-shrink: 0;
	}
}

.goods-summary {
	display: flex;
	justify-content: space-between;
	font-size: 28rpx;
	
	text:first-child {
		color: #64748b;
	}
	
	text:last-child {
		color: #1e293b;
		font-weight: 600;
	}
}

.card-title {
	display: block;
	font-size: 28rpx;
	font-weight: 700;
	color: #1e293b;
	margin-bottom: 24rpx;
}

.payment-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 24rpx 16rpx;
	border-radius: 16rpx;
	transition: background 0.3s;
	
	&:active {
		background: #f8fafc;
	}
	
	&:not(:last-child) {
		margin-bottom: 24rpx;
	}
}

.payment-left {
	display: flex;
	align-items: center;
	gap: 16rpx;
	
	.fa:first-child {
		font-size: 40rpx;
	}
	
	.payment-name {
		font-size: 28rpx;
		color: #1e293b;
	}
}

.fa-circle, .fa-check-circle {
	font-size: 32rpx;
	color: #cbd5e1;
	
	&.checked {
		color: #059669;
	}
}

.footer-bar {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: #fff;
	border-top: 2rpx solid #f1f5f9;
	padding: 24rpx 32rpx;
	padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
	display: flex;
	justify-content: flex-end;
	align-items: center;
	gap: 32rpx;
	z-index: 100;
	box-sizing: border-box;
}

.total-info {
	text-align: right;
	display: flex;
	flex-direction: column;
	gap: 4rpx;
}

.total-label {
	font-size: 20rpx;
	color: #94a3b8;
}

.total-price {
	font-size: 36rpx;
	font-weight: 700;
	color: #ef4444;
}

.pay-btn {
	background: #059669;
	color: #fff;
	padding: 24rpx 64rpx;
	border-radius: 100rpx;
	font-size: 28rpx;
	font-weight: 700;
	border: none;
	box-shadow: 0 8rpx 24rpx rgba(5, 150, 105, 0.2);
	
	&::after {
		border: none;
	}
}
</style>
