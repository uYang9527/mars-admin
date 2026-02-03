<template>
	<view class="page">
		<!-- 状态标签 -->
		<view class="tabs">
			<view class="tab-item" :class="{ active: currentStatus === null }" @click="changeStatus(null)">全部</view>
			<view class="tab-item" :class="{ active: currentStatus === 0 }" @click="changeStatus(0)">待付款</view>
			<view class="tab-item" :class="{ active: currentStatus === 1 }" @click="changeStatus(1)">待发货</view>
			<view class="tab-item" :class="{ active: currentStatus === 2 }" @click="changeStatus(2)">待收货</view>
			<view class="tab-item" :class="{ active: currentStatus === 3 }" @click="changeStatus(3)">已完成</view>
		</view>
		
		<!-- 订单列表 -->
		<scroll-view class="order-list" scroll-y @scrolltolower="loadMore">
			<view class="order-item" v-for="order in orderList" :key="order.id" @click="goDetail(order.id)">
				<view class="order-header">
					<text class="order-no">订单号: {{ order.orderNo }}</text>
					<text class="order-status" :class="'status-' + order.status">{{ getStatusText(order.status) }}</text>
				</view>
				
				<!-- 商品列表 -->
				<view class="goods-list">
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
				
				<view class="order-footer">
					<text class="total-text">共{{ getTotalQuantity(order) }}件商品 实付: <text class="total-price">¥{{ order.payAmount }}</text></text>
					<view class="order-actions">
						<view class="action-btn" v-if="order.status === 0" @click.stop="cancelOrder(order)">取消订单</view>
						<view class="action-btn primary" v-if="order.status === 0" @click.stop="payOrder(order)">立即支付</view>
						<view class="action-btn primary" v-if="order.status === 2" @click.stop="confirmReceive(order)">确认收货</view>
						<view class="action-btn" v-if="order.status === 3 || order.status === 4" @click.stop="deleteOrder(order)">删除订单</view>
					</view>
				</view>
			</view>
			
			<!-- 加载状态 -->
			<view class="load-more" v-if="loading">
				<text>加载中...</text>
			</view>
			<view class="no-more" v-else-if="!hasMore && orderList.length > 0">
				<text>没有更多订单了</text>
			</view>
			
			<!-- 空状态 -->
			<view class="empty" v-if="!loading && orderList.length === 0">
				<text class="empty-icon">📋</text>
				<text class="empty-text">暂无订单</text>
			</view>
		</scroll-view>
	</view>
</template>

<script>
import { getOrderList, cancelOrder as cancelOrderApi, payOrder as payOrderApi, receiveOrder, deleteOrder as deleteOrderApi } from '@/utils/api.js'
import { getOrderStatusText } from '@/utils/util.js'

export default {
	data() {
		return {
			currentStatus: null,
			orderList: [],
			page: 1,
			pageSize: 10,
			hasMore: true,
			loading: false
		}
	},
	onLoad(options) {
		if (options.status !== undefined) {
			this.currentStatus = parseInt(options.status)
		}
		this.loadOrders()
	},
	methods: {
		// 获取状态文本
		getStatusText(status) {
			return getOrderStatusText(status)
		},
		
		// 计算订单商品总数
		getTotalQuantity(order) {
			return (order.orderItems || []).reduce((sum, item) => sum + item.quantity, 0)
		},
		
		// 切换状态
		changeStatus(status) {
			this.currentStatus = status
			this.page = 1
			this.orderList = []
			this.hasMore = true
			this.loadOrders()
		},
		
		// 加载订单
		async loadOrders() {
			if (this.loading || !this.hasMore) return
			
			this.loading = true
			try {
				const res = await getOrderList({
					page: this.page,
					pageSize: this.pageSize,
					status: this.currentStatus
				})
				
				const list = res.data?.list || []
				if (this.page === 1) {
					this.orderList = list
				} else {
					this.orderList = [...this.orderList, ...list]
				}
				
				this.hasMore = list.length >= this.pageSize
			} catch (e) {
				console.error('加载订单失败', e)
			} finally {
				this.loading = false
			}
		},
		
		// 加载更多
		loadMore() {
			this.page++
			this.loadOrders()
		},
		
		// 订单详情
		goDetail(id) {
			uni.navigateTo({ url: `/pages/order/detail?id=${id}` })
		},
		
		// 取消订单
		cancelOrder(order) {
			uni.showModal({
				title: '提示',
				content: '确定取消订单吗？',
				success: async (res) => {
					if (res.confirm) {
						try {
							await cancelOrderApi(order.id)
							order.status = 4
							uni.showToast({ title: '订单已取消', icon: 'none' })
						} catch (e) {
							console.error('取消订单失败', e)
						}
					}
				}
			})
		},
		
		// 支付订单
		async payOrder(order) {
			try {
				uni.showLoading({ title: '支付中...' })
				const res = await payOrderApi(order.id, 1)
				
				uni.requestPayment({
					provider: 'wxpay',
					timeStamp: res.data.timeStamp,
					nonceStr: res.data.nonceStr,
					package: res.data.package,
					signType: res.data.signType || 'RSA',
					paySign: res.data.paySign,
					success: () => {
						uni.hideLoading()
						order.status = 1
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
		confirmReceive(order) {
			uni.showModal({
				title: '提示',
				content: '确认收货？',
				success: async (res) => {
					if (res.confirm) {
						try {
							await receiveOrder(order.id)
							order.status = 3
							uni.showToast({ title: '已确认收货', icon: 'success' })
						} catch (e) {
							console.error('确认收货失败', e)
						}
					}
				}
			})
		},
		
		// 删除订单
		deleteOrder(order) {
			uni.showModal({
				title: '提示',
				content: '确定删除订单吗？',
				success: async (res) => {
					if (res.confirm) {
						try {
							await deleteOrderApi(order.id)
							const index = this.orderList.findIndex(o => o.id === order.id)
							if (index > -1) {
								this.orderList.splice(index, 1)
							}
							uni.showToast({ title: '订单已删除', icon: 'none' })
						} catch (e) {
							console.error('删除订单失败', e)
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
	display: flex;
	flex-direction: column;
}

.tabs {
	display: flex;
	background: #fff;
	padding: 20rpx 0;
	border-bottom: 2rpx solid #f1f5f9;
	
	.tab-item {
		flex: 1;
		text-align: center;
		font-size: 28rpx;
		color: #64748b;
		padding: 16rpx 0;
		position: relative;
		
		&.active {
			color: #10b981;
			font-weight: 600;
			
			&::after {
				content: '';
				position: absolute;
				bottom: 0;
				left: 50%;
				transform: translateX(-50%);
				width: 40rpx;
				height: 4rpx;
				background: #10b981;
				border-radius: 4rpx;
			}
		}
	}
}

.order-list {
	flex: 1;
	padding: 20rpx;
}

.order-item {
	background: #fff;
	border-radius: 24rpx;
	margin-bottom: 20rpx;
	overflow: hidden;
	box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.04);
	
	.order-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 24rpx;
		border-bottom: 2rpx solid #f8fafc;
		
		.order-no {
			font-size: 26rpx;
			color: #64748b;
		}
		
		.order-status {
			font-size: 26rpx;
			font-weight: 500;
			
			&.status-0 { color: #ef4444; }
			&.status-1 { color: #f59e0b; }
			&.status-2 { color: #3b82f6; }
			&.status-3 { color: #10b981; }
			&.status-4 { color: #94a3b8; }
		}
	}
	
	.goods-list {
		padding: 20rpx 24rpx;
	}
	
	.goods-item {
		display: flex;
		gap: 20rpx;
		padding: 12rpx 0;
		
		.goods-image {
			width: 140rpx;
			height: 140rpx;
			border-radius: 12rpx;
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
				display: -webkit-box;
				-webkit-line-clamp: 1;
				-webkit-box-orient: vertical;
				overflow: hidden;
			}
			
			.goods-sku {
				font-size: 24rpx;
				color: #94a3b8;
				margin-bottom: 8rpx;
			}
			
			.goods-bottom {
				display: flex;
				justify-content: space-between;
				align-items: center;
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
	
	.order-footer {
		padding: 20rpx 24rpx;
		border-top: 2rpx solid #f8fafc;
		display: flex;
		justify-content: space-between;
		align-items: center;
		
		.total-text {
			font-size: 26rpx;
			color: #64748b;
			
			.total-price {
				color: #ef4444;
				font-weight: 600;
			}
		}
		
		.order-actions {
			display: flex;
			gap: 16rpx;
			
			.action-btn {
				padding: 12rpx 24rpx;
				border: 2rpx solid #e2e8f0;
				border-radius: 32rpx;
				font-size: 26rpx;
				color: #64748b;
				
				&.primary {
					border-color: #10b981;
					background: #10b981;
					color: #fff;
				}
			}
		}
	}
}

.load-more, .no-more {
	text-align: center;
	padding: 30rpx;
	font-size: 26rpx;
	color: #94a3b8;
}

.empty {
	padding: 120rpx 0;
	display: flex;
	flex-direction: column;
	align-items: center;
	
	.empty-icon {
		font-size: 100rpx;
		margin-bottom: 20rpx;
	}
	
	.empty-text {
		font-size: 28rpx;
		color: #94a3b8;
	}
}
</style>
