<template>
	<view class="page">
		<view :style="{ height: statusBarHeight + 'px' }"></view>
		
		<view class="content">
			<!-- Logo 居中 -->
			<view class="logo">
				<view class="logo-icon">
					<text class="fas fa-leaf"></text>
				</view>
				<text class="logo-title1">欢迎来到</text>
				<text class="logo-title2">GreenMall</text>
				<text class="logo-desc">新鲜生活，一键触达</text>
			</view>
			
			<!-- 登录按钮 -->
			<view class="login-btns">
				<button class="btn-wechat" @click="showProfilePopup('wechat')" :disabled="logging">
					<text class="fab fa-weixin"></text>
					<text>微信授权登录</text>
				</button>
				
				<!-- 手机号一键登录 -->
				<button class="btn-phone" @click="showProfilePopup('phone')" :disabled="logging">
					<text class="fas fa-mobile-alt"></text>
					<text>手机号一键登录</text>
				</button>
				
				<button class="btn-password" @click="passwordLogin">
					账号密码登录
				</button>
			</view>
			
			<!-- 协议 -->
			<view class="agreement">
				登录即表示同意
				<text class="link">《用户协议》</text>和
				<text class="link">《隐私政策》</text>
			</view>
		</view>
		
		<!-- 底部弹窗：设置头像和昵称 -->
		<view class="popup-mask" :class="{ show: showPopup }" @click="closePopup"></view>
		<view class="popup-container" :class="{ show: showPopup }">
			<view class="popup-header">
				<text class="popup-title">完善个人信息</text>
				<text class="popup-close fas fa-times" @click="closePopup"></text>
			</view>
			<view class="popup-content">
				<view class="avatar-section">
					<button class="avatar-btn" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
						<image class="avatar-img" :src="avatar || '/static/default-avatar.png'" mode="aspectFill"></image>
						<view class="avatar-edit">
							<text class="fas fa-camera"></text>
						</view>
					</button>
					<text class="avatar-tip">点击设置头像</text>
				</view>
				<view class="nickname-section">
					<input class="nickname-input" type="nickname" v-model="nickname" placeholder="请输入昵称" @blur="onNicknameBlur" />
				</view>
			</view>
			<view class="popup-footer">
				<!-- 微信登录确认按钮 -->
				<button v-if="loginType === 'wechat'" class="popup-btn" @click="confirmWechatLogin" :disabled="logging">
					{{ logging ? '登录中...' : '确认登录' }}
				</button>
				<!-- 手机号登录确认按钮 -->
				<button v-else class="popup-btn" open-type="getPhoneNumber" @getphonenumber="confirmPhoneLogin" :disabled="logging">
					{{ logging ? '登录中...' : '授权手机号并登录' }}
				</button>
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
import { login, loginByPhone } from '@/utils/api.js'
import { updateCartBadge } from '@/utils/cart.js'

export default {
	data() {
		return {
			statusBarHeight: 0,
			showToast: false,
			toastText: '',
			logging: false,
			// 弹窗控制
			showPopup: false,
			loginType: 'wechat', // 'wechat' 或 'phone'
			// 用户头像和昵称
			avatar: '',
			nickname: ''
		}
	},
	onLoad() {
		const systemInfo = uni.getSystemInfoSync()
		this.statusBarHeight = systemInfo.statusBarHeight || 0
	},
	methods: {
		// 显示设置头像昵称弹窗
		showProfilePopup(type) {
			this.loginType = type
			this.showPopup = true
		},
		
		// 关闭弹窗
		closePopup() {
			if (this.logging) return
			this.showPopup = false
		},
		
		// 选择头像回调
		onChooseAvatar(e) {
			console.log('选择头像:', e.detail.avatarUrl)
			this.avatar = e.detail.avatarUrl
		},
		
		// 昵称输入完成
		onNicknameBlur(e) {
			this.nickname = e.detail.value
		},
		
		// ========== 微信授权登录确认 ==========
		confirmWechatLogin() {
			// 检查是否设置了头像和昵称
			if (!this.avatar) {
				this.showToastMessage('请先设置头像')
				return
			}
			if (!this.nickname || !this.nickname.trim()) {
				this.showToastMessage('请先输入昵称')
				return
			}
			
			if (this.logging) return
			this.logging = true
			this.showToastMessage('微信授权登录中...')
			
			// 调用微信登录获取code
			uni.login({
				provider: 'weixin',
				success: async (loginRes) => {
					console.log('wx.login code:', loginRes.code)
					try {
						const res = await login(loginRes.code, this.nickname, this.avatar)
						if (res.code === 200 && res.data) {
							this.handleLoginSuccess(res.data)
						} else {
							this.showToastMessage(res.message || '登录失败')
						}
					} catch (e) {
						console.error('登录失败', e)
						this.showToastMessage(e.message || '登录失败，请重试')
					} finally {
						this.logging = false
					}
				},
				fail: (err) => {
					console.error('wx.login失败', err)
					this.showToastMessage('获取登录凭证失败')
					this.logging = false
				}
			})
		},
		
		// ========== 手机号一键登录确认 ==========
		confirmPhoneLogin(e) {
			// 检查是否设置了头像和昵称
			if (!this.avatar) {
				this.showToastMessage('请先设置头像')
				return
			}
			if (!this.nickname || !this.nickname.trim()) {
				this.showToastMessage('请先输入昵称')
				return
			}
			
			// 检查用户是否授权手机号
			if (e.detail.errMsg !== 'getPhoneNumber:ok') {
				if (e.detail.errMsg.indexOf('deny') > -1 || e.detail.errMsg.indexOf('cancel') > -1) {
					this.showToastMessage('您取消了手机号授权')
				} else {
					this.showToastMessage('获取手机号失败')
				}
				return
			}
			
			if (this.logging) return
			this.logging = true
			this.showToastMessage('手机号登录中...')
			
			const phoneCode = e.detail.code
			
			// 先获取微信登录code，再一起发送给后端
			uni.login({
				provider: 'weixin',
				success: async (loginRes) => {
					console.log('wx.login code:', loginRes.code)
					console.log('phone code:', phoneCode)
					try {
						const res = await loginByPhone(loginRes.code, phoneCode, this.nickname, this.avatar)
						if (res.code === 200 && res.data) {
							this.handleLoginSuccess(res.data)
						} else {
							this.showToastMessage(res.message || '登录失败')
						}
					} catch (e) {
						console.error('手机号登录失败', e)
						this.showToastMessage(e.message || '登录失败，请重试')
					} finally {
						this.logging = false
					}
				},
				fail: (err) => {
					console.error('wx.login失败', err)
					this.showToastMessage('获取登录凭证失败')
					this.logging = false
				}
			})
		},
		
		// 登录成功统一处理
		handleLoginSuccess(data) {
			// 保存会员信息
			uni.setStorageSync('memberInfo', data)
			uni.setStorageSync('token', data.token)
			
			this.showToastMessage('登录成功')
			this.showPopup = false
			// 更新购物车角标
			updateCartBadge()
			
			setTimeout(() => {
				uni.switchTab({ url: '/pages/index/index' })
			}, 500)
		},
		
		// 账号密码登录（跳转到密码登录页）
		passwordLogin() {
			uni.navigateTo({ url: '/pages/login/password' })
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

.content {
	width: 100%;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	min-height: calc(100vh - 88rpx);
	padding: 80rpx 48rpx 96rpx;
	box-sizing: border-box;
}

.logo {
	display: flex;
	flex-direction: column;
	align-items: center;
	text-align: center;
	margin-bottom: 120rpx;
	
	.logo-icon {
		width: 160rpx;
		height: 160rpx;
		background: #059669;
		border-radius: 56rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 48rpx;
		box-shadow: 0 16rpx 48rpx rgba(5, 150, 105, 0.25);
		
		.fa {
			font-size: 80rpx;
			color: #fff;
		}
	}
	
	.logo-title1 {
		display: block;
		font-size: 52rpx;
		font-weight: 700;
		color: #1e293b;
		margin-bottom: 12rpx;
	}
	
	.logo-title2 {
		display: block;
		font-size: 52rpx;
		font-weight: 700;
		color: #059669;
		margin-bottom: 24rpx;
	}
	
	.logo-desc {
		font-size: 28rpx;
		color: #64748b;
	}
}

.login-btns {
	width: 100%;
	display: flex;
	flex-direction: column;
	gap: 32rpx;
}

.btn-wechat, .btn-phone, .btn-password {
	width: 100%;
	height: 112rpx;
	border-radius: 32rpx;
	font-size: 32rpx;
	font-weight: 700;
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 24rpx;
	border: none;
	
	&::after {
		border: none;
	}
	
	&[disabled] {
		opacity: 0.6;
	}
	
	.fa {
		font-size: 40rpx;
	}
}

.btn-wechat {
	background: #059669;
	color: #fff;
	box-shadow: 0 8rpx 24rpx rgba(5, 150, 105, 0.2);
	
	&:active {
		opacity: 0.9;
	}
}

.btn-phone {
	background: #fff;
	color: #059669;
	border: 4rpx solid #059669;
	
	&:active {
		background: rgba(5, 150, 105, 0.05);
	}
}

.btn-password {
	background: #f1f5f9;
	color: #1e293b;
	font-weight: 500;
	font-size: 28rpx;
	
	&:active {
		background: #e2e8f0;
	}
}

.agreement {
	width: 100%;
	text-align: center;
	font-size: 24rpx;
	color: #94a3b8;
	line-height: 1.6;
	margin-top: 48rpx;
	
	.link {
		color: #059669;
	}
}

/* ========== 底部弹窗样式 ========== */
.popup-mask {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.5);
	z-index: 1000;
	opacity: 0;
	visibility: hidden;
	transition: all 0.3s;
	
	&.show {
		opacity: 1;
		visibility: visible;
	}
}

.popup-container {
	position: fixed;
	left: 0;
	right: 0;
	bottom: 0;
	background: #fff;
	border-radius: 48rpx 48rpx 0 0;
	z-index: 1001;
	transform: translateY(100%);
	transition: transform 0.3s ease-out;
	padding-bottom: env(safe-area-inset-bottom);
	
	&.show {
		transform: translateY(0);
	}
}

.popup-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 40rpx 48rpx 24rpx;
	border-bottom: 2rpx solid #f1f5f9;
}

.popup-title {
	font-size: 36rpx;
	font-weight: 700;
	color: #1e293b;
}

.popup-close {
	font-size: 40rpx;
	color: #94a3b8;
	padding: 16rpx;
}

.popup-content {
	padding: 48rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.avatar-section {
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-bottom: 40rpx;
}

.avatar-btn {
	width: 180rpx;
	height: 180rpx;
	padding: 0;
	margin: 0;
	background: transparent;
	border: none;
	position: relative;
	
	&::after {
		border: none;
	}
}

.avatar-img {
	width: 180rpx;
	height: 180rpx;
	border-radius: 50%;
	border: 4rpx solid #e2e8f0;
	background: #f8fafc;
}

.avatar-edit {
	position: absolute;
	right: 0;
	bottom: 0;
	width: 56rpx;
	height: 56rpx;
	background: #059669;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	border: 4rpx solid #fff;
	box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.15);
	
	.fa {
		font-size: 28rpx;
		color: #fff;
	}
}

.avatar-tip {
	font-size: 26rpx;
	color: #94a3b8;
	margin-top: 20rpx;
}

.nickname-section {
	width: 100%;
}

.nickname-input {
	width: 100%;
	height: 96rpx;
	background: #f8fafc;
	border: 2rpx solid #e2e8f0;
	border-radius: 20rpx;
	padding: 0 32rpx;
	font-size: 32rpx;
	color: #1e293b;
	text-align: center;
	box-sizing: border-box;
	
	&::placeholder {
		color: #94a3b8;
	}
}

.popup-footer {
	padding: 24rpx 48rpx 48rpx;
}

.popup-btn {
	width: 100%;
	height: 112rpx;
	background: #059669;
	color: #fff;
	border-radius: 32rpx;
	font-size: 32rpx;
	font-weight: 700;
	display: flex;
	align-items: center;
	justify-content: center;
	border: none;
	box-shadow: 0 8rpx 24rpx rgba(5, 150, 105, 0.2);
	
	&::after {
		border: none;
	}
	
	&[disabled] {
		opacity: 0.6;
	}
}

/* ========== Toast样式 ========== */
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
