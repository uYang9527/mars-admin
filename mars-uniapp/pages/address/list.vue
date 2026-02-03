<template>
	<view class="page">
		<!-- 地址列表 -->
		<view class="address-list" v-if="addressList.length > 0">
			<view class="address-item" v-for="item in addressList" :key="item.id" @click="selectAddress(item)">
				<view class="address-info">
					<view class="address-top">
						<text class="name">{{ item.name }}</text>
						<text class="phone">{{ item.phone }}</text>
						<text class="default-tag" v-if="item.isDefault">默认</text>
					</view>
					<text class="address-detail">{{ item.province }} {{ item.city }} {{ item.district }} {{ item.detail }}</text>
				</view>
				<view class="address-actions">
					<view class="action-item" @click.stop="setDefault(item)" v-if="!item.isDefault">
						<text class="iconfont icon-check"></text>
						<text>设为默认</text>
					</view>
					<view class="action-item" @click.stop="editAddress(item)">
						<text class="iconfont icon-edit"></text>
						<text>编辑</text>
					</view>
					<view class="action-item" @click.stop="deleteAddress(item)">
						<text class="iconfont icon-delete"></text>
						<text>删除</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 空状态 -->
		<view class="empty" v-else>
			<text class="empty-icon">📍</text>
			<text class="empty-text">暂无收货地址</text>
		</view>
		
		<!-- 新增按钮 -->
		<view class="add-btn" @click="addAddress">
			<text class="iconfont icon-plus"></text>
			<text>新增收货地址</text>
		</view>
	</view>
</template>

<script>
import { getAddressList, setDefaultAddress, deleteAddress as deleteAddressApi } from '@/utils/api.js'

export default {
	data() {
		return {
			addressList: [],
			isSelect: false // 是否选择模式
		}
	},
	onLoad(options) {
		this.isSelect = options.select === '1'
	},
	onShow() {
		this.loadAddressList()
	},
	methods: {
		// 加载地址列表
		async loadAddressList() {
			try {
				uni.showLoading({ title: '加载中...' })
				const res = await getAddressList()
				this.addressList = res.data || []
			} catch (e) {
				console.error('加载地址失败', e)
			} finally {
				uni.hideLoading()
			}
		},
		
		// 选择地址
		selectAddress(item) {
			if (this.isSelect) {
				// 返回选择的地址
				const eventChannel = this.getOpenerEventChannel()
				eventChannel.emit('selectAddress', item)
				uni.navigateBack()
			}
		},
		
		// 设为默认
		async setDefault(item) {
			try {
				await setDefaultAddress(item.id)
				// 更新列表
				this.addressList.forEach(addr => {
					addr.isDefault = addr.id === item.id ? 1 : 0
				})
				uni.showToast({ title: '设置成功', icon: 'none' })
			} catch (e) {
				console.error('设置默认地址失败', e)
			}
		},
		
		// 新增地址
		addAddress() {
			uni.navigateTo({ url: '/pages/address/edit' })
		},
		
		// 编辑地址
		editAddress(item) {
			uni.navigateTo({ url: `/pages/address/edit?id=${item.id}` })
		},
		
		// 删除地址
		deleteAddress(item) {
			uni.showModal({
				title: '提示',
				content: '确定删除该地址吗？',
				success: async (res) => {
					if (res.confirm) {
						try {
							await deleteAddressApi(item.id)
							const index = this.addressList.findIndex(a => a.id === item.id)
							if (index > -1) {
								this.addressList.splice(index, 1)
							}
							uni.showToast({ title: '删除成功', icon: 'none' })
						} catch (e) {
							console.error('删除地址失败', e)
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
	padding-bottom: 160rpx;
}

.address-list {
	padding: 20rpx;
}

.address-item {
	background: #fff;
	border-radius: 24rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.04);
	
	.address-info {
		padding-bottom: 24rpx;
		border-bottom: 2rpx solid #f8fafc;
		
		.address-top {
			display: flex;
			align-items: center;
			gap: 16rpx;
			margin-bottom: 12rpx;
			
			.name {
				font-size: 32rpx;
				font-weight: 600;
				color: #1e293b;
			}
			
			.phone {
				font-size: 28rpx;
				color: #64748b;
			}
			
			.default-tag {
				background: #10b981;
				color: #fff;
				font-size: 22rpx;
				padding: 4rpx 12rpx;
				border-radius: 8rpx;
			}
		}
		
		.address-detail {
			font-size: 28rpx;
			color: #64748b;
			line-height: 1.5;
		}
	}
	
	.address-actions {
		display: flex;
		padding-top: 20rpx;
		gap: 40rpx;
		
		.action-item {
			display: flex;
			align-items: center;
			gap: 8rpx;
			font-size: 26rpx;
			color: #64748b;
			
			.iconfont {
				font-size: 28rpx;
			}
		}
	}
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

.add-btn {
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
	gap: 12rpx;
	font-size: 32rpx;
	font-weight: 500;
	box-shadow: 0 8rpx 24rpx rgba(16, 185, 129, 0.3);
	
	.iconfont {
		font-size: 36rpx;
	}
}

/* 图标 */
.icon-check::before { content: "✓"; }
.icon-edit::before { content: "✏"; }
.icon-delete::before { content: "🗑"; }
.icon-plus::before { content: "+"; }
</style>
