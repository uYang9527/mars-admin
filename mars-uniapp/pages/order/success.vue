<template>
	<view class="page">
		<view :style="{ height: statusBarHeight + 'px' }"></view>
		
		<view class="content">
			<!-- 成功图标 -->
			<view class="success-icon">
				<text class="fas fa-check"></text>
			</view>
			
			<text class="success-title">支付成功</text>
			<text class="success-desc">感谢您的购买，我们将尽快为您发货</text>
			
			<!-- 订单信息 -->
			<view class="order-info">
				<view class="info-row">
					<text class="info-label">订单金额</text>
					<text class="info-value">¥{{ amount }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">支付方式</text>
					<text class="info-value">微信支付</text>
				</view>
			</view>
			
			<!-- 操作按钮 -->
			<view class="action-btns">
				<view class="btn-secondary" @click="goOrders">查看订单</view>
				<view class="btn-primary" @click="goHome">返回首页</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			statusBarHeight: 0,
			amount: '74.90'
		}
	},
	onLoad() {
		const systemInfo = uni.getSystemInfoSync()
		this.statusBarHeight = systemInfo.statusBarHeight || 0
	},
	methods: {
		goOrders() {
			uni.navigateTo({ url: '/pages/orders/index' })
		},
		goHome() {
			uni.switchTab({ url: '/pages/index/index' })
		}
	}
}
</script>

<style lang="scss" scoped>

.page {
	width: 100%;
	min-height: 100vh;
	background: #fff;
	display: flex;
	flex-direction: column;
}

.content {
	width: 100%;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	min-height: calc(100vh - 88rpx);
	padding: 64rpx 32rpx;
	box-sizing: border-box;
}

.success-icon {
	width: 192rpx;
	height: 192rpx;
	background: rgba(5, 150, 105, 0.1);
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 48rpx;
	animation: bounce 1s infinite;
	
	.fa {
		font-size: 96rpx;
		color: #059669;
	}
}

@keyframes bounce {
	0%, 100% {
		transform: translateY(0);
	}
	50% {
		transform: translateY(-20rpx);
	}
}

.success-title {
	font-size: 48rpx;
	font-weight: 700;
	color: #1e293b;
	margin-bottom: 16rpx;
}

.success-desc {
	font-size: 28rpx;
	color: #64748b;
	margin-bottom: 80rpx;
}

.order-info {
	width: 100%;
	background: #f8fafc;
	border-radius: 24rpx;
	padding: 32rpx;
	margin-bottom: 64rpx;
}

.info-row {
	display: flex;
	justify-content: space-between;
	font-size: 28rpx;
	
	&:not(:last-child) {
		margin-bottom: 16rpx;
	}
}

.info-label {
	color: #64748b;
}

.info-value {
	color: #1e293b;
	font-weight: 600;
}

.action-btns {
	width: 100%;
	display: flex;
	gap: 32rpx;
}

.btn-secondary, .btn-primary {
	flex: 1;
	height: 96rpx;
	line-height: 96rpx;
	border-radius: 100rpx;
	font-size: 28rpx;
	font-weight: 600;
	text-align: center;
	box-sizing: border-box;
}

.btn-secondary {
	background: #fff;
	color: #64748b;
	border: 2rpx solid #e2e8f0;
	
	&:active {
		background: #f8fafc;
	}
}

.btn-primary {
	background: #059669;
	color: #fff;
	box-shadow: 0 16rpx 30rpx -6rpx rgba(5, 150, 105, 0.3);
	
	&:active {
		opacity: 0.9;
	}
}
</style>
