<template>
	<view class="page">
		<!-- 订单状态 -->
		<view class="status-card" :class="'status-' + order.status">
			<text class="status-text">{{ statusText }}</text>
			<text class="status-desc" v-if="order.status === 0">请尽快完成支付</text>
			<text class="status-desc" v-else-if="order.status === 2">{{ order.deliveryCompany }} {{ order.deliveryNo }}</text>
		</view>
		
		<!-- 收货地址 -->
		<view class="address-card">
			<view class="address-icon">
				<text class="iconfont icon-location"></text>
			</view>
			<view class="address-info">
				<view class="address-top">
					<text class="name">{{ order.receiverName }}</text>
					<text class="phone">{{ order.receiverPhone }}</text>
				</view>
				<text class="address-detail">{{ order.receiverProvince }} {{ order.receiverCity }} {{ order.receiverDistrict }} {{ order.receiverDetail }}</text>
			</view>
		</view>
		
		<!-- 商品列表 -->
		<view class="goods-card">
			<view class="goods-item" v-for="item in order.orderItems" :key="item.id">
				<image class="goods-image" :src="item.productImage" mode="aspectFill"></image>
				<view class="goods-info">
					<text class="goods-name">{{ item.productName }}</text>
					<text class="goods-sku" v-if="item.skuName">{{ item.skuName }}</text>
					<view class="goods-bottom">
						<text class="goods-price">¥{{ item.price }}</text>
						<text class="goods-qty">x{{ item.quantity }}</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 订单信息 -->
		<view class="info-card">
			<view class="info-item">
				<text class="label">订单编号</text>
				<text class="value">{{ order.orderNo }}</text>
			</view>
			<view class="info-item">
				<text class="label">下单时间</text>
				<text class="value">{{ order.createTime }}</text>
			</view>
			<view class="info-item" v-if="order.payTime">
				<text class="label">支付时间</text>
				<text class="value">{{ order.payTime }}</text>
			</view>
			<view class="info-item" v-if="order.deliveryTime">
				<text class="label">发货时间</text>
				<text class="value">{{ order.deliveryTime }}</text>
			</view>
			<view class="info-item">
				<text class="label">商品金额</text>
				<text class="value">¥{{ order.totalAmount }}</text>
			</view>
			<view class="info-item">
				<text class="label">运费</text>
				<text class="value">¥{{ order.freightAmount || '0.00' }}</text>
			</view>
			<view class="info-item total">
				<text class="label">实付金额</text>
				<text class="value price">¥{{ order.payAmount }}</text>
			</view>
		</view>
		
		<!-- 底部操作 -->
		<view class="bottom-bar" v-if="order.status === 0 || order.status === 2">
			<view class="action-btn" v-if="order.status === 0" @click="cancelOrder">取消订单</view>
			<view class="action-btn primary" v-if="order.status === 0" @click="payOrder">立即支付</view>
			<view class="action-btn primary" v-if="order.status === 2" @click="confirmReceive">确认收货</view>
		</view>
	</view>
</template>

<script>
import { getOrderDetail, cancelOrder as cancelOrderApi, payOrder as payOrderApi, receiveOrder } from '@/utils/api.js'
import { getOrderStatusText } from '@/utils/util.js'

export default {
	data() {
		return {
			orderId: null,
			order: {}
		}
	},
	computed: {
		statusText() {
			return getOrderStatusText(this.order.status)
		}
	},
	onLoad(options) {
		this.orderId = options.id
		this.loadOrder()
	},
	methods: {
		// 加载订单
		async loadOrder() {
			try {
				uni.showLoading({ title: '加载中...' })
				const res = await getOrderDetail(this.orderId)
				this.order = res.data || {}
			} catch (e) {
				console.error('加载订单失败', e)
			} finally {
				uni.hideLoading()
			}
		},
		
		// 取消订单
		cancelOrder() {
			uni.showModal({
				title: '提示',
				content: '确定取消订单吗？',
				success: async (res) => {
					if (res.confirm) {
						try {
							await cancelOrderApi(this.order.id)
							this.order.status = 4
							uni.showToast({ title: '订单已取消', icon: 'none' })
						} catch (e) {
							console.error('取消订单失败', e)
						}
					}
				}
			})
		},
		
		// 支付订单
		async payOrder() {
			try {
				uni.showLoading({ title: '支付中...' })
				const res = await payOrderApi(this.order.id, 1)
				
				uni.requestPayment({
					provider: 'wxpay',
					timeStamp: res.data.timeStamp,
					nonceStr: res.data.nonceStr,
					package: res.data.package,
					signType: res.data.signType || 'RSA',
					paySign: res.data.paySign,
					success: () => {
						uni.hideLoading()
						this.order.status = 1
						uni.showToast({ title: '支付成功', icon: 'success' })
					},
					fail: () => {
						uni.hideLoading()
						uni.showToast({ title: '支付取消', icon: 'none' })
					}
				})
			} catch (e) {
				uni.hideLoading()
				console.error('支付失败', e)
			}
		},
		
		// 确认收货
		confirmReceive() {
			uni.showModal({
				title: '提示',
				content: '确认收货？',
				success: async (res) => {
					if (res.confirm) {
						try {
							await receiveOrder(this.order.id)
							this.order.status = 3
							uni.showToast({ title: '已确认收货', icon: 'success' })
						} catch (e) {
							console.error('确认收货失败', e)
						}
					}
				}
			})
		}
	}
}
</script>

<style lang="scss">
.page {
	min-height: 100vh;
	background: #f8fafc;
	padding-bottom: 200rpx;
}

.status-card {
	padding: 40rpx 30rpx;
	color: #fff;
	
	&.status-0 { background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%); }
	&.status-1 { background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%); }
	&.status-2 { background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%); }
	&.status-3 { background: linear-gradient(135deg, #10b981 0%, #059669 100%); }
	&.status-4, &.status-5 { background: linear-gradient(135deg, #64748b 0%, #475569 100%); }
	
	.status-text {
		display: block;
		font-size: 36rpx;
		font-weight: 600;
		margin-bottom: 8rpx;
	}
	
	.status-desc {
		font-size: 26rpx;
		opacity: 0.9;
	}
}

.address-card {
	background: #fff;
	margin: 20rpx;
	border-radius: 24rpx;
	padding: 30rpx;
	display: flex;
	gap: 20rpx;
	box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.04);
	
	.address-icon {
		width: 64rpx;
		height: 64rpx;
		background: #d1fae5;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		
		.iconfont {
			font-size: 32rpx;
			color: #10b981;
		}
	}
	
	.address-info {
		flex: 1;
		
		.address-top {
			display: flex;
			gap: 20rpx;
			margin-bottom: 8rpx;
			
			.name {
				font-size: 30rpx;
				font-weight: 600;
				color: #1e293b;
			}
			
			.phone {
				font-size: 28rpx;
				color: #64748b;
			}
		}
		
		.address-detail {
			font-size: 26rpx;
			color: #64748b;
			line-height: 1.5;
		}
	}
}

.goods-card {
	background: #fff;
	margin: 20rpx;
	border-radius: 24rpx;
	padding: 20rpx;
	box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.04);
	
	.goods-item {
		display: flex;
		gap: 20rpx;
		padding: 16rpx;
		
		.goods-image {
			width: 160rpx;
			height: 160rpx;
			border-radius: 16rpx;
			background: #f8fafc;
		}
		
		.goods-info {
			flex: 1;
			display: flex;
			flex-direction: column;
			
			.goods-name {
				font-size: 28rpx;
				color: #1e293b;
				font-weight: 500;
				margin-bottom: 8rpx;
			}
			
			.goods-sku {
				font-size: 24rpx;
				color: #94a3b8;
			}
			
			.goods-bottom {
				display: flex;
				justify-content: space-between;
				margin-top: auto;
				
				.goods-price {
					font-size: 28rpx;
					color: #1e293b;
					font-weight: 500;
				}
				
				.goods-qty {
					font-size: 26rpx;
					color: #94a3b8;
				}
			}
		}
	}
}

.info-card {
	background: #fff;
	margin: 20rpx;
	border-radius: 24rpx;
	padding: 20rpx 30rpx;
	box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.04);
	
	.info-item {
		display: flex;
		justify-content: space-between;
		padding: 16rpx 0;
		border-bottom: 2rpx solid #f8fafc;
		
		&:last-child {
			border-bottom: none;
		}
		
		&.total {
			padding-top: 24rpx;
		}
		
		.label {
			font-size: 28rpx;
			color: #64748b;
		}
		
		.value {
			font-size: 28rpx;
			color: #1e293b;
			
			&.price {
				font-size: 32rpx;
				font-weight: 600;
				color: #ef4444;
			}
		}
	}
}

.bottom-bar {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: #fff;
	padding: 20rpx 30rpx;
	padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
	padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
	display: flex;
	justify-content: flex-end;
	gap: 20rpx;
	border-top: 2rpx solid #f1f5f9;
	
	.action-btn {
		padding: 20rpx 40rpx;
		border: 2rpx solid #e2e8f0;
		border-radius: 44rpx;
		font-size: 28rpx;
		color: #64748b;
		
		&.primary {
			border-color: #10b981;
			background: #10b981;
			color: #fff;
		}
	}
}

/* 图标 */
.icon-location::before { content: "📍"; }
</style>
