<template>
	<view class="page">
		<view class="form">
			<view class="form-item">
				<text class="label">收货人</text>
				<input v-model="form.name" placeholder="请输入收货人姓名" />
			</view>
			
			<view class="form-item">
				<text class="label">手机号</text>
				<input v-model="form.phone" type="number" placeholder="请输入手机号" maxlength="11" />
			</view>
			
			<view class="form-item" @click="selectRegion">
				<text class="label">所在地区</text>
				<view class="region-value" :class="{ placeholder: !regionText }">
					{{ regionText || '请选择省市区' }}
				</view>
				<text class="iconfont icon-arrow-right"></text>
			</view>
			
			<view class="form-item textarea">
				<text class="label">详细地址</text>
				<textarea v-model="form.detail" placeholder="请输入详细地址，如街道、门牌号等"></textarea>
			</view>
			
			<view class="form-item switch">
				<text class="label">设为默认地址</text>
				<switch :checked="form.isDefault === 1" @change="onDefaultChange" color="#10b981" />
			</view>
		</view>
		
		<!-- 保存按钮 -->
		<view class="save-btn" @click="save">保存</view>
	</view>
</template>

<script>
import { getAddressDetail, createAddress, updateAddress } from '@/utils/api.js'

export default {
	data() {
		return {
			addressId: null,
			form: {
				name: '',
				phone: '',
				province: '',
				city: '',
				district: '',
				detail: '',
				isDefault: 0
			}
		}
	},
	computed: {
		regionText() {
			if (this.form.province) {
				return `${this.form.province} ${this.form.city} ${this.form.district}`
			}
			return ''
		}
	},
	onLoad(options) {
		if (options.id) {
			this.addressId = options.id
			uni.setNavigationBarTitle({ title: '编辑地址' })
			this.loadAddress()
		} else {
			uni.setNavigationBarTitle({ title: '新增地址' })
		}
	},
	methods: {
		// 加载地址详情
		async loadAddress() {
			try {
				uni.showLoading({ title: '加载中...' })
				const res = await getAddressDetail(this.addressId)
				if (res.data) {
					this.form = res.data
				}
			} catch (e) {
				console.error('加载地址失败', e)
			} finally {
				uni.hideLoading()
			}
		},
		
		// 选择地区
		selectRegion() {
			uni.chooseLocation({
				success: (res) => {
					// 这里简化处理，实际应该使用省市区选择器
					console.log('选择位置', res)
				}
			})
			
			// 使用简单的输入方式代替
			uni.showModal({
				title: '输入地区',
				editable: true,
				placeholderText: '格式：省 市 区，如：浙江省 杭州市 西湖区',
				success: (res) => {
					if (res.confirm && res.content) {
						const parts = res.content.trim().split(/\s+/)
						if (parts.length >= 3) {
							this.form.province = parts[0]
							this.form.city = parts[1]
							this.form.district = parts[2]
						} else {
							uni.showToast({ title: '请输入正确格式', icon: 'none' })
						}
					}
				}
			})
		},
		
		// 默认地址切换
		onDefaultChange(e) {
			this.form.isDefault = e.detail.value ? 1 : 0
		},
		
		// 保存
		async save() {
			// 验证
			if (!this.form.name) {
				uni.showToast({ title: '请输入收货人姓名', icon: 'none' })
				return
			}
			if (!this.form.phone || this.form.phone.length !== 11) {
				uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
				return
			}
			if (!this.form.province) {
				uni.showToast({ title: '请选择所在地区', icon: 'none' })
				return
			}
			if (!this.form.detail) {
				uni.showToast({ title: '请输入详细地址', icon: 'none' })
				return
			}
			
			try {
				uni.showLoading({ title: '保存中...' })
				
				if (this.addressId) {
					await updateAddress(this.form)
				} else {
					await createAddress(this.form)
				}
				
				uni.showToast({ title: '保存成功', icon: 'success' })
				setTimeout(() => {
					uni.navigateBack()
				}, 1500)
			} catch (e) {
				console.error('保存地址失败', e)
			} finally {
				uni.hideLoading()
			}
		}
	}
}
</script>

<style lang="scss">
.page {
	min-height: 100vh;
	background: #f8fafc;
	padding-bottom: 160rpx;
}

.form {
	background: #fff;
	margin: 20rpx;
	border-radius: 24rpx;
	overflow: hidden;
	
	.form-item {
		display: flex;
		align-items: center;
		padding: 30rpx;
		border-bottom: 2rpx solid #f8fafc;
		
		&:last-child {
			border-bottom: none;
		}
		
		.label {
			width: 160rpx;
			font-size: 30rpx;
			color: #1e293b;
			flex-shrink: 0;
		}
		
		input {
			flex: 1;
			font-size: 30rpx;
			color: #1e293b;
		}
		
		.region-value {
			flex: 1;
			font-size: 30rpx;
			color: #1e293b;
			
			&.placeholder {
				color: #94a3b8;
			}
		}
		
		.icon-arrow-right {
			font-size: 28rpx;
			color: #cbd5e1;
		}
		
		&.textarea {
			flex-direction: column;
			align-items: flex-start;
			
			.label {
				width: auto;
				margin-bottom: 16rpx;
			}
			
			textarea {
				width: 100%;
				height: 160rpx;
				font-size: 30rpx;
				color: #1e293b;
				background: #f8fafc;
				border-radius: 16rpx;
				padding: 20rpx;
				box-sizing: border-box;
			}
		}
		
		&.switch {
			.label {
				flex: 1;
			}
		}
	}
}

.save-btn {
	position: fixed;
	bottom: 40rpx;
	left: 30rpx;
	right: 30rpx;
	background: #10b981;
	color: #fff;
	height: 100rpx;
	border-radius: 50rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 32rpx;
	font-weight: 500;
	box-shadow: 0 8rpx 24rpx rgba(16, 185, 129, 0.3);
}

/* 图标 */
.icon-arrow-right::before { content: "›"; }
</style>
