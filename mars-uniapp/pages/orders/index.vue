<template>
	<view class="page">
		<!-- 头部 -->
		<view class="header">
			<view :style="{ height: statusBarHeight + 'px' }"></view>
			<view class="header-content">
				<text class="fas fa-arrow-left" @click="goBack"></text>
				<text class="header-title">我的订单</text>
			</view>
		</view>
		
		<!-- 标签页 -->
		<view class="tabs">
			<view 
				class="tab-item" 
				:class="{ active: activeTab === tab.id }"
				v-for="tab in tabs" 
				:key="tab.id"
				@click="switchTab(tab.id)"
			>
				<text>{{ tab.name }}</text>
			</view>
		</view>
		
		<!-- 订单列表 -->
		<scroll-view class="content" scroll-y>
			<view class="order-list">
				<view class="order-item" v-for="order in orders" :key="order.id">
					<view class="order-header">
						<text class="order-time">{{ order.time }}</text>
						<text class="order-status" :style="{ color: order.statusColor }">{{ order.status }}</text>
					</view>
					<view class="order-body">
						<view class="order-product">
							<image class="product-image" :src="order.image" mode="aspectFill"></image>
							<view class="product-info">
								<text class="product-name">{{ order.name }}</text>
								<text class="product-spec">{{ order.spec }}</text>
								<text class="product-price">¥{{ order.price }} x {{ order.quantity }}</text>
							</view>
						</view>
					</view>
					<view class="order-footer">
						<text class="order-total">共{{order.quantity}}件商品 实付: <text class="total-price">¥{{ order.total }}</text></text>
						<button class="order-btn" @click="handleOrder(order)">{{ order.btnText }}</button>
					</view>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
import { getOrderList, cancelOrder, payOrder, receiveOrder } from '@/utils/api.js'

export default {
	data() {
		return {
			statusBarHeight: 0,
			activeTab: -1, // -1 表示全部
			tabs: [
				{ id: -1, name: '全部', status: null },
				{ id: 0, name: '待付款', status: 0 },
				{ id: 1, name: '待发货', status: 1 },
				{ id: 2, name: '待收货', status: 2 },
				{ id: 3, name: '已完成', status: 3 }
			],
			orders: [],
			loading: false,
			page: 1,
			hasMore: true
		}
	},
	onLoad(options) {
		const systemInfo = uni.getSystemInfoSync()
		this.statusBarHeight = systemInfo.statusBarHeight || 0
		
		// 从参数获取状态
		if (options.status !== undefined) {
			this.activeTab = parseInt(options.status)
		}
		
		this.loadOrders()
	},
	methods: {
		goBack() {
			uni.navigateBack()
		},
		switchTab(id) {
			this.activeTab = id
			this.page = 1
			this.orders = []
			this.hasMore = true
			this.loadOrders()
		},
		// 加载订单列表
		async loadOrders() {
			if (this.loading || !this.hasMore) return
			
			this.loading = true
			try {
				const params = {
					page: this.page,
					pageSize: 10
				}
				// 如果不是全部，添加状态筛选
				if (this.activeTab !== -1) {
					params.status = this.activeTab
				}
				
				const res = await getOrderList(params)
				if (res.code === 200 && res.data) {
					const list = (res.data.list || []).map(order => this.formatOrder(order))
					if (this.page === 1) {
						this.orders = list
					} else {
						this.orders = [...this.orders, ...list]
					}
					this.hasMore = list.length >= 10
					this.page++
				}
			} catch (e) {
				console.error('加载订单失败', e)
			} finally {
				this.loading = false
			}
		},
		// 格式化订单数据
		formatOrder(order) {
			const statusMap = {
				0: { text: '待付款', color: '#f59e0b', btn: '去支付' },
				1: { text: '待发货', color: '#3b82f6', btn: '提醒发货' },
				2: { text: '待收货', color: '#8b5cf6', btn: '确认收货' },
				3: { text: '已完成', color: '#059669', btn: '再次购买' },
				4: { text: '已取消', color: '#94a3b8', btn: '删除订单' },
				5: { text: '已退款', color: '#ef4444', btn: '查看详情' }
			}
			const statusInfo = statusMap[order.status] || { text: '未知', color: '#94a3b8', btn: '查看详情' }
			
			// 取第一个商品信息
			const firstItem = order.items && order.items.length > 0 ? order.items[0] : {}
			
			return {
				id: order.id,
				orderNo: order.orderNo,
				time: order.createTime,
				status: statusInfo.text,
				statusCode: order.status,
				statusColor: statusInfo.color,
				name: firstItem.productName || '商品',
				spec: firstItem.skuName || '',
				price: firstItem.price || order.payAmount,
				quantity: order.items ? order.items.reduce((sum, item) => sum + item.quantity, 0) : 1,
				total: order.payAmount,
				image: firstItem.productImage || '',
				btnText: statusInfo.btn
			}
		},
		// 处理订单操作
		async handleOrder(order) {
			switch (order.statusCode) {
				case 0: // 待付款 - 去支付
					this.goPay(order)
					break
				case 2: // 待收货 - 确认收货
					await this.confirmReceive(order)
					break
				case 3: // 已完成 - 再次购买
					// TODO: 再次购买
					uni.showToast({ title: '功能开发中', icon: 'none' })
					break
				default:
					uni.navigateTo({ url: `/pages/order/detail?id=${order.id}` })
			}
		},
		// 去支付
		goPay(order) {
			uni.navigateTo({ url: `/pages/order/detail?id=${order.id}` })
		},
		// 确认收货
		async confirmReceive(order) {
			uni.showModal({
				title: '确认收货',
				content: '确认已收到商品？',
				success: async (res) => {
					if (res.confirm) {
						try {
							await receiveOrder(order.id)
							uni.showToast({ title: '确认收货成功', icon: 'success' })
							// 刷新列表
							this.page = 1
							this.orders = []
							this.hasMore = true
							this.loadOrders()
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

.tabs {
	background: #fff;
	display: flex;
	padding: 0 32rpx 24rpx;
	border-bottom: 2rpx solid #f1f5f9;
	position: sticky;
	top: 140rpx;
	z-index: 10;
	box-sizing: border-box;
}

.tab-item {
	flex: 1;
	text-align: center;
	padding: 16rpx 0;
	font-size: 28rpx;
	color: #64748b;
	position: relative;
	transition: all 0.3s;
	
	&.active {
		color: #059669;
		font-weight: 700;
		
		&::after {
			content: '';
			position: absolute;
			bottom: -2rpx;
			left: 50%;
			transform: translateX(-50%);
			width: 60rpx;
			height: 4rpx;
			background: #059669;
			border-radius: 2rpx;
		}
	}
}

.content {
	width: 100%;
	height: calc(100vh - 320rpx);
	padding: 32rpx;
	box-sizing: border-box;
}

.order-list {
	display: flex;
	flex-direction: column;
	gap: 32rpx;
}

.order-item {
	background: #fff;
	border-radius: 32rpx;
	overflow: hidden;
	box-shadow: 0 2rpx 16rpx rgba(0,0,0,0.03);
}

.order-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 24rpx 32rpx;
	background: #f8fafc;
	border-bottom: 2rpx solid #f1f5f9;
}

.order-time {
	font-size: 24rpx;
	color: #64748b;
}

.order-status {
	font-size: 24rpx;
	font-weight: 600;
}

.order-body {
	padding: 32rpx;
}

.order-product {
	display: flex;
	gap: 24rpx;
}

.product-image {
	width: 128rpx;
	height: 128rpx;
	border-radius: 16rpx;
	background: #f8fafc;
	flex-shrink: 0;
}

.product-info {
	flex: 1;
	display: flex;
	flex-direction: column;
	gap: 8rpx;
}

.product-name {
	font-size: 28rpx;
	font-weight: 600;
	color: #1e293b;
}

.product-spec {
	font-size: 24rpx;
	color: #94a3b8;
}

.product-price {
	font-size: 28rpx;
	font-weight: 700;
	color: #059669;
	margin-top: 8rpx;
}

.order-footer {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 24rpx 32rpx;
	border-top: 2rpx solid #f1f5f9;
}

.order-total {
	font-size: 28rpx;
	color: #64748b;
}

.total-price {
	color: #059669;
	font-weight: 700;
}

.order-btn {
	padding: 12rpx 40rpx;
	background: #059669;
	color: #fff;
	border-radius: 100rpx;
	font-size: 24rpx;
	font-weight: 600;
	border: none;
	
	&::after {
		border: none;
	}
}
</style>
