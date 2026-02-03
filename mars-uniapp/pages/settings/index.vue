<template>
	<view class="page">
		<!-- 头部 -->
		<view class="header">
			<view :style="{ height: statusBarHeight + 'px' }"></view>
			<view class="header-content">
				<text class="fas fa-arrow-left" @click="goBack"></text>
				<text class="header-title">设置</text>
			</view>
		</view>
		
		<!-- 内容 -->
		<scroll-view class="content" scroll-y>
			<!-- 账号信息 -->
			<view class="menu-card">
				<view class="menu-item" v-for="(menu, index) in accountMenus" :key="index" @click="handleMenu(menu)">
					<text :class="menu.icon"></text>
					<text class="menu-label">{{ menu.label }}</text>
					<text class="fas fa-chevron-right"></text>
				</view>
			</view>
			
			<!-- 通用设置 -->
			<view class="menu-card">
				<view class="menu-item">
					<text class="fas fa-bell"></text>
					<text class="menu-label">消息通知</text>
					<switch :checked="notification" @change="toggleNotification" color="#059669" />
				</view>
				<view class="menu-item" @click="changeLanguage">
					<text class="fas fa-language"></text>
					<text class="menu-label">语言设置</text>
					<view class="menu-right">
						<text class="menu-value">简体中文</text>
						<text class="fas fa-chevron-right"></text>
					</view>
				</view>
				<view class="menu-item" @click="clearCache">
					<text class="fas fa-trash-alt"></text>
					<text class="menu-label">清除缓存</text>
					<view class="menu-right">
						<text class="menu-value">128MB</text>
						<text class="fas fa-chevron-right"></text>
					</view>
				</view>
			</view>
			
			<!-- 关于 -->
			<view class="menu-card">
				<view class="menu-item" v-for="(menu, index) in aboutMenus" :key="index" @click="handleMenu(menu)">
					<text :class="menu.icon"></text>
					<text class="menu-label">{{ menu.label }}</text>
					<text class="fas fa-chevron-right"></text>
				</view>
			</view>
			
			<!-- 退出登录 -->
			<button class="logout-btn" @click="logout">退出登录</button>
			
			<view class="version">版本号：v1.0.0</view>
		</scroll-view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			statusBarHeight: 0,
			notification: true,
			accountMenus: [
				{ id: 1, icon: 'fas fa-user-circle', label: '个人资料' },
				{ id: 2, icon: 'fas fa-shield-alt', label: '账号安全' },
				{ id: 3, icon: 'fas fa-key', label: '修改密码' }
			],
			aboutMenus: [
				{ id: 4, icon: 'fas fa-info-circle', label: '关于我们' },
				{ id: 5, icon: 'fas fa-file-contract', label: '用户协议' },
				{ id: 6, icon: 'fas fa-user-shield', label: '隐私政策' }
			]
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
		handleMenu(menu) {
			uni.showToast({ title: menu.label, icon: 'none' })
		},
		toggleNotification(e) {
			this.notification = e.detail.value
			uni.showToast({ title: this.notification ? '已开启通知' : '已关闭通知', icon: 'none' })
		},
		changeLanguage() {
			uni.showToast({ title: '语言设置', icon: 'none' })
		},
		clearCache() {
			uni.showModal({
				title: '提示',
				content: '确定要清除缓存吗？',
				success: (res) => {
					if (res.confirm) {
						uni.showToast({ title: '清除成功', icon: 'success' })
					}
				}
			})
		},
		logout() {
			uni.showModal({
				title: '提示',
				content: '确定要退出登录吗？',
				success: (res) => {
					if (res.confirm) {
						uni.showToast({ title: '已退出登录', icon: 'none' })
						setTimeout(() => {
							uni.reLaunch({ url: '/pages/login/index' })
						}, 1000)
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

.content {
	width: 100%;
	height: calc(100vh - 120rpx);
	padding: 32rpx;
	box-sizing: border-box;
}

.menu-card {
	background: #fff;
	border-radius: 32rpx;
	overflow: hidden;
	margin-bottom: 32rpx;
	box-shadow: 0 2rpx 16rpx rgba(0,0,0,0.03);
}

.menu-item {
	display: flex;
	align-items: center;
	padding: 32rpx;
	border-bottom: 2rpx solid #f8fafc;
	transition: background 0.3s;
	
	&:last-child {
		border-bottom: none;
	}
	
	&:active {
		background: #f8fafc;
	}
	
	> .fa:first-child {
		font-size: 36rpx;
		color: #059669;
		width: 40rpx;
		margin-right: 24rpx;
	}
}

.menu-label {
	flex: 1;
	font-size: 28rpx;
	font-weight: 500;
	color: #1e293b;
}

.menu-right {
	display: flex;
	align-items: center;
	gap: 16rpx;
}

.menu-value {
	font-size: 28rpx;
	color: #94a3b8;
}

.fa-chevron-right {
	font-size: 24rpx;
	color: #cbd5e1;
}

.logout-btn {
	width: 100%;
	background: #fff;
	color: #ef4444;
	padding: 32rpx;
	border-radius: 32rpx;
	font-size: 28rpx;
	font-weight: 700;
	border: none;
	box-shadow: 0 2rpx 16rpx rgba(0,0,0,0.03);
	
	&::after {
		border: none;
	}
	
	&:active {
		background: #fef2f2;
	}
}

.version {
	text-align: center;
	color: #94a3b8;
	font-size: 24rpx;
	padding: 48rpx 0;
}
</style>
