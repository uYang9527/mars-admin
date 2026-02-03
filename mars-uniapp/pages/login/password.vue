<template>
	<view class="page">
		<!-- 头部 -->
		<view class="header">
			<view :style="{ height: statusBarHeight + 'px' }"></view>
			<view class="header-content">
				<text class="fas fa-arrow-left" @click="goBack"></text>
				<text class="header-title">账号密码登录</text>
			</view>
		</view>
		
		<!-- 内容 -->
		<view class="content">
			<!-- 表单 -->
			<view class="form">
				<view class="form-item">
					<text class="form-label">手机号码</text>
					<view class="form-input" :class="{ focus: phoneFocus }">
						<text class="fas fa-mobile-alt"></text>
						<input 
							type="number" 
							placeholder="请输入手机号"
							v-model="phone"
							@focus="phoneFocus = true"
							@blur="phoneFocus = false"
						/>
					</view>
				</view>
				
				<view class="form-item">
					<text class="form-label">密码</text>
					<view class="form-input" :class="{ focus: passwordFocus }">
						<text class="fas fa-lock"></text>
						<input 
							:type="showPassword ? 'text' : 'password'"
							placeholder="请输入密码"
							v-model="password"
							@focus="passwordFocus = true"
							@blur="passwordFocus = false"
						/>
						<text 
							:class="showPassword ? 'fas fa-eye' : 'far fa-eye-slash'"
							@click="showPassword = !showPassword"
						></text>
					</view>
				</view>
			</view>
			
			<!-- 选项 -->
			<view class="options">
				<label class="remember">
					<checkbox :checked="remember" @change="remember = !remember" color="#059669" />
					<text>记住密码</text>
				</label>
				<text class="forgot" @click="forgotPassword">忘记密码?</text>
			</view>
			
			<!-- 登录按钮 -->
			<button class="login-btn" @click="doLogin">登 录</button>
			
			<!-- 注册 -->
			<view class="register">
				还没有账号? <text class="link" @click="goRegister">立即注册</text>
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
export default {
	data() {
		return {
			statusBarHeight: 0,
			phone: '',
			password: '',
			remember: false,
			showPassword: false,
			phoneFocus: false,
			passwordFocus: false,
			showToast: false,
			toastText: ''
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
		doLogin() {
			if (!this.phone) {
				this.showToastMessage('请输入手机号')
				return
			}
			if (!this.password) {
				this.showToastMessage('请输入密码')
				return
			}
			this.showToastMessage('登录成功')
			setTimeout(() => {
				uni.switchTab({ url: '/pages/index/index' })
			}, 1000)
		},
		forgotPassword() {
			uni.showToast({ title: '忘记密码', icon: 'none' })
		},
		goRegister() {
			uni.showToast({ title: '立即注册', icon: 'none' })
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
	background: #fff;
	display: flex;
	flex-direction: column;
}

.header {
	background: #fff;
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
	padding: 48rpx 32rpx;
	box-sizing: border-box;
}

.form {
	margin-bottom: 64rpx;
}

.form-item {
	margin-bottom: 32rpx;
}

.form-label {
	display: block;
	font-size: 24rpx;
	color: #64748b;
	margin-bottom: 16rpx;
	margin-left: 8rpx;
}

.form-input {
	background: #f8fafc;
	border-radius: 24rpx;
	padding: 28rpx 32rpx;
	display: flex;
	align-items: center;
	gap: 24rpx;
	border: 4rpx solid transparent;
	transition: all 0.3s;
	
	&.focus {
		border-color: #059669;
		background: #fff;
	}
	
	.fa:first-child {
		font-size: 28rpx;
		color: #94a3b8;
	}
	
	input {
		flex: 1;
		font-size: 28rpx;
		color: #1e293b;
	}
	
	.fa:last-child {
		font-size: 28rpx;
		color: #94a3b8;
	}
}

.options {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 64rpx;
}

.remember {
	display: flex;
	align-items: center;
	gap: 16rpx;
	
	checkbox {
		transform: scale(0.8);
	}
	
	text {
		font-size: 24rpx;
		color: #64748b;
	}
}

.forgot {
	font-size: 24rpx;
	color: #059669;
	font-weight: 600;
}

.login-btn {
	width: 100%;
	background: #059669;
	color: #fff;
	height: 112rpx;
	border-radius: 32rpx;
	font-size: 32rpx;
	font-weight: 700;
	border: none;
	box-shadow: 0 8rpx 24rpx rgba(5, 150, 105, 0.2);
	margin-bottom: 48rpx;
	
	&::after {
		border: none;
	}
	
	&:active {
		opacity: 0.9;
	}
}

.register {
	text-align: center;
	font-size: 28rpx;
	color: #64748b;
	
	.link {
		color: #059669;
		font-weight: 700;
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
