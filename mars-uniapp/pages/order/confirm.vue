<template>
	<view class="page">
		<!-- 收货地址 -->
		<view class="address-card" @click="selectAddress">
			<view class="address-icon">
				<text class="iconfont icon-location"></text>
			</view>
			<view class="address-info" v-if="address">
				<view class="address-top">
					<text class="name">{{ address.name }}</text>
					<text class="phone">{{ formatPhone(address.phone) }}</text>
				</view>
				<text class="address-detail">{{ address.province }} {{ address.city }} {{ address.district }} {{ address.detail }}</text>
			</view>
			<view class="address-empty" v-else>
				<text>请选择收货地址</text>
			</view>
			<text class="iconfont icon-arrow-right"></text>
		</view>
		
		<!-- 商品列表 -->
		<view class="goods-card">
			<view class="goods-images">
				<image v-for="(item, index) in orderItems" :key="index" 
					   :src="item.productImage" mode="aspectFill"></image>
			</view>
			<view class="goods-summary">
				<text>共 {{ orderItems.length }} 件商品</text>
				<text>小计: ¥{{ totalAmount }}</text>
			</view>
		</view>
		
		<!-- 支付方式 -->
		<view class="pay-card">
			<text class="card-title">支付方式</text>
			<view class="pay-item" @click="selectPayType(1)">
				<text class="iconfont icon-wechat"></text>
				<text class="pay-name">微信支付</text>
				<text class="iconfont" :class="payType === 1 ? 'icon-checked' : 'icon-unchecked'"></text>
			</view>
			<view class="pay-item" @click="selectPayType(2)">
				<text class="iconfont icon-alipay"></text>
				<text class="pay-name">支付宝</text>
				<text class="iconfont" :class="payType === 2 ? 'icon-checked' : 'icon-unchecked'"></text>
			</view>
		</view>
		
		<!-- 订单备注 -->
		<view class="remark-card">
			<text class="card-title">订单备注</text>
			<input class="remark-input" v-model="remark" placeholder="选填，请输入备注信息" />
		</view>
		
		<!-- 底部结算 -->
		<view class="bottom-bar">
			<view class="total-info">
				<text class="total-label">共{{ orderItems.length }}件</text>
				<text class="total-price">¥{{ totalAmount }}</text>
			</view>
			<view class="submit-btn" @click="submitOrder">立即支付</view>
		</view>
	</view>
</template>

<script>
import { getCartList, getDefaultAddress, createOrderFromCart, createOrderDirect, payOrder, getProductDetail } from '@/utils/api.js'
import { formatPhone } from '@/utils/util.js'

export default {
	data() {
		return {
			type: 'cart', // cart-从购物车, direct-直接购买
			address: null,
			orderItems: [],
			payType: 1,
			remark: '',
			// 直接购买参数
			productId: null,
			skuId: null,
			quantity: 1
		}
	},
	computed: {
		totalAmount() {
			let total = 0
			this.orderItems.forEach(item => {
				const price = item.skuPrice || item.productPrice || item.price || 0
				total += price * (item.quantity || 1)
			})
			return total.toFixed(2)
		}
	},
	onLoad(options) {
		this.type = options.type || 'cart'
		
		if (this.type === 'direct') {
			this.productId = options.productId
			this.skuId = options.skuId || null
			this.quantity = parseInt(options.quantity) || 1
			this.loadDirectProduct()
		} else {
			this.loadCartItems()
		}
		
		this.loadDefaultAddress()
	},
	methods: {
		formatPhone,
		
		// 加载直接购买商品
		async loadDirectProduct() {
			try {
				uni.showLoading({ title: '加载中...' })
				const res = await getProductDetail(this.productId)
				const product = res.data
				
				let item = {
					productId: product.id,
					productName: product.name,
					productImage: product.mainImage,
					productPrice: product.price,
					quantity: this.quantity
				}
				
				// 如果选择了SKU
				if (this.skuId && product.skuList) {
					const sku = product.skuList.find(s => s.id == this.skuId)
					if (sku) {
						item.skuId = sku.id
						item.skuName = sku.skuName
						item.skuPrice = sku.price
					}
				}
				
				this.orderItems = [item]
			} catch (e) {
				console.error('加载商品失败', e)
			} finally {
				uni.hideLoading()
			}
		},
		
		// 加载购物车商品
		async loadCartItems() {
			try {
				uni.showLoading({ title: '加载中...' })
				const res = await getCartList()
				// 只显示选中的商品
				this.orderItems = (res.data || []).filter(item => item.selected)
			} catch (e) {
				console.error('加载购物车失败', e)
			} finally {
				uni.hideLoading()
			}
		},
		
		// 加载默认地址
		async loadDefaultAddress() {
			try {
				const res = await getDefaultAddress()
				this.address = res.data
			} catch (e) {
				console.error('加载默认地址失败', e)
			}
		},
		
		// 选择地址
		selectAddress() {
			uni.navigateTo({
				url: '/pages/address/list?select=1',
				events: {
					selectAddress: (address) => {
						this.address = address
					}
				}
			})
		},
		
		// 选择支付方式
		selectPayType(type) {
			this.payType = type
		},
		
		// 提交订单并支付
		async submitOrder() {
			if (!this.address) {
				uni.showToast({ title: '请选择收货地址', icon: 'none' })
				return
			}
			
			if (this.orderItems.length === 0) {
				uni.showToast({ title: '请选择商品', icon: 'none' })
				return
			}
			
			// 微信支付
			if (this.payType === 1) {
				await this.wechatPay()
			} else {
				uni.showToast({ title: '暂不支持该支付方式', icon: 'none' })
			}
		},
		
		// 微信支付
		async wechatPay() {
			try {
				uni.showLoading({ title: '正在创建订单...' })
				
				// 1. 创建订单
				let orderRes
				if (this.type === 'direct') {
					orderRes = await createOrderDirect(
						this.productId,
						this.skuId,
						this.quantity,
						this.address.id,
						this.remark,
						null
					)
				} else {
					orderRes = await createOrderFromCart(
						this.address.id,
						this.remark,
						null
					)
				}
				
				const order = orderRes.data
				console.log('订单创建成功:', order)
				
				// 2. 获取微信支付参数
				uni.showLoading({ title: '正在拉起支付...' })
				const payRes = await payOrder(order.id, 1)
				console.log('支付参数:', payRes.data)
				
				uni.hideLoading()
				
				// 3. 拉起微信支付
				uni.requestPayment({
					provider: 'wxpay',
					timeStamp: payRes.data.timeStamp,
					nonceStr: payRes.data.nonceStr,
					package: payRes.data.package,
					signType: payRes.data.signType || 'RSA',
					paySign: payRes.data.paySign,
					success: (res) => {
						console.log('支付成功', res)
						uni.redirectTo({
							url: `/pages/order/success?orderNo=${order.orderNo}&amount=${order.payAmount}`
						})
					},
					fail: (err) => {
						console.error('支付失败或取消', err)
						if (err.errMsg.indexOf('cancel') > -1) {
							uni.showToast({ title: '支付已取消', icon: 'none' })
						} else {
							uni.showToast({ title: '支付失败', icon: 'none' })
						}
						// 跳转到订单详情
						setTimeout(() => {
							uni.redirectTo({
								url: `/pages/order/detail?id=${order.id}`
							})
						}, 1500)
					}
				})
				
			} catch (e) {
				uni.hideLoading()
				console.error('支付流程错误', e)
				uni.showToast({ title: e.message || '支付失败，请重试', icon: 'none' })
			}
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

.address-card {
	background: #fff;
	border-radius: 24rpx;
	margin: 20rpx;
	padding: 30rpx;
	display: flex;
	align-items: center;
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
			align-items: center;
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
			display: -webkit-box;
			-webkit-line-clamp: 1;
			-webkit-box-orient: vertical;
			overflow: hidden;
		}
	}
	
	.address-empty {
		flex: 1;
		
		text {
			color: #94a3b8;
			font-size: 28rpx;
		}
	}
	
	.icon-arrow-right {
		font-size: 28rpx;
		color: #cbd5e1;
	}
}

.goods-card {
	background: #fff;
	border-radius: 24rpx;
	margin: 20rpx;
	padding: 30rpx;
	box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.04);
	
	.goods-images {
		display: flex;
		gap: 16rpx;
		margin-bottom: 20rpx;
		overflow-x: auto;
		
		image {
			width: 128rpx;
			height: 128rpx;
			border-radius: 12rpx;
			border: 2rpx solid #f1f5f9;
			flex-shrink: 0;
		}
	}
	
	.goods-summary {
		display: flex;
		justify-content: space-between;
		font-size: 28rpx;
		color: #475569;
		
		text:last-child {
			font-weight: 500;
			color: #1e293b;
		}
	}
}

.pay-card, .remark-card {
	background: #fff;
	border-radius: 24rpx;
	margin: 20rpx;
	padding: 30rpx;
	box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.04);
	
	.card-title {
		display: block;
		font-size: 30rpx;
		font-weight: 600;
		color: #1e293b;
		margin-bottom: 24rpx;
	}
}

.pay-item {
	display: flex;
	align-items: center;
	padding: 20rpx 0;
	
	.icon-wechat {
		color: #07c160;
		font-size: 44rpx;
		margin-right: 20rpx;
	}
	
	.icon-alipay {
		color: #1677ff;
		font-size: 44rpx;
		margin-right: 20rpx;
	}
	
	.pay-name {
		flex: 1;
		font-size: 28rpx;
		color: #475569;
	}
	
	.icon-checked {
		color: #10b981;
		font-size: 40rpx;
	}
	
	.icon-unchecked {
		color: #cbd5e1;
		font-size: 40rpx;
	}
}

.remark-input {
	background: #f8fafc;
	border-radius: 12rpx;
	padding: 20rpx;
	font-size: 28rpx;
}

.bottom-bar {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: #fff;
	border-top: 2rpx solid #f1f5f9;
	padding: 20rpx 30rpx;
	padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
	padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
	display: flex;
	justify-content: flex-end;
	align-items: center;
	gap: 30rpx;
	
	.total-info {
		text-align: right;
		
		.total-label {
			display: block;
			font-size: 22rpx;
			color: #94a3b8;
		}
		
		.total-price {
			font-size: 40rpx;
			font-weight: 700;
			color: #ef4444;
		}
	}
	
	.submit-btn {
		background: #10b981;
		color: #fff;
		padding: 24rpx 60rpx;
		border-radius: 44rpx;
		font-size: 30rpx;
		font-weight: 600;
		box-shadow: 0 8rpx 24rpx rgba(16, 185, 129, 0.3);
	}
}

/* 图标 */
.icon-location::before { content: "📍"; }
.icon-arrow-right::before { content: "›"; }
.icon-wechat::before { content: "💚"; }
.icon-alipay::before { content: "💙"; }
.icon-checked::before { content: "☑"; }
.icon-unchecked::before { content: "○"; }
</style>
