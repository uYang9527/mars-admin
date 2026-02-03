<template>
	<view class="page">
		<scroll-view class="favorite-list" scroll-y @scrolltolower="loadMore" v-if="favoriteList.length > 0">
			<view class="product-grid">
				<view class="product-card" v-for="item in favoriteList" :key="item.id">
					<view class="product-image-wrap" @click="goDetail(item.product?.id)">
						<image class="product-image" :src="item.product?.mainImage" mode="aspectFill"></image>
						<view class="remove-btn" @click.stop="removeFavorite(item)">
							<text class="iconfont icon-close"></text>
						</view>
					</view>
					<view class="product-info" @click="goDetail(item.product?.id)">
						<text class="product-name">{{ item.product?.name }}</text>
						<view class="product-bottom">
							<text class="product-price">¥{{ item.product?.price }}</text>
							<view class="add-cart" @click.stop="addToCart(item.product)">
								<text class="iconfont icon-cart"></text>
							</view>
						</view>
					</view>
				</view>
			</view>
			
			<view class="load-more" v-if="loading">
				<text>加载中...</text>
			</view>
			<view class="no-more" v-else-if="!hasMore">
				<text>没有更多了</text>
			</view>
		</scroll-view>
		
		<!-- 空状态 -->
		<view class="empty" v-else>
			<text class="empty-icon">💖</text>
			<text class="empty-text">暂无收藏</text>
			<view class="empty-btn" @click="goHome">去逛逛</view>
		</view>
	</view>
</template>

<script>
import { getFavoriteList, removeFavorite as removeFavoriteApi, addToCart as addToCartApi } from '@/utils/api.js'

export default {
	data() {
		return {
			favoriteList: [],
			page: 1,
			pageSize: 10,
			hasMore: true,
			loading: false
		}
	},
	onShow() {
		this.page = 1
		this.favoriteList = []
		this.hasMore = true
		this.loadFavorites()
	},
	methods: {
		// 加载收藏列表
		async loadFavorites() {
			if (this.loading || !this.hasMore) return
			
			this.loading = true
			try {
				const res = await getFavoriteList({
					page: this.page,
					pageSize: this.pageSize
				})
				
				const list = res.data?.list || []
				if (this.page === 1) {
					this.favoriteList = list
				} else {
					this.favoriteList = [...this.favoriteList, ...list]
				}
				
				this.hasMore = list.length >= this.pageSize
			} catch (e) {
				console.error('加载收藏失败', e)
			} finally {
				this.loading = false
			}
		},
		
		// 加载更多
		loadMore() {
			this.page++
			this.loadFavorites()
		},
		
		// 取消收藏
		async removeFavorite(item) {
			try {
				await removeFavoriteApi(item.productId)
				const index = this.favoriteList.findIndex(f => f.id === item.id)
				if (index > -1) {
					this.favoriteList.splice(index, 1)
				}
				uni.showToast({ title: '已取消收藏', icon: 'none' })
			} catch (e) {
				console.error('取消收藏失败', e)
			}
		},
		
		// 加入购物车
		async addToCart(product) {
			if (!product) return
			try {
				await addToCartApi(product.id, null, 1)
				uni.showToast({ title: '已加入购物车', icon: 'none' })
			} catch (e) {
				console.error('加入购物车失败', e)
			}
		},
		
		// 商品详情
		goDetail(id) {
			if (!id) return
			uni.navigateTo({ url: `/pages/detail/index?id=${id}` })
		},
		
		// 去首页
		goHome() {
			uni.switchTab({ url: '/pages/index/index' })
		}
	}
}
</script>

<style lang="scss">
.page {
	min-height: 100vh;
	background: #f8fafc;
}

.favorite-list {
	height: 100vh;
	padding: 20rpx;
}

.product-grid {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 20rpx;
}

.product-card {
	background: #fff;
	border-radius: 24rpx;
	overflow: hidden;
	box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.04);
	
	.product-image-wrap {
		position: relative;
		height: 280rpx;
		
		.product-image {
			width: 100%;
			height: 100%;
		}
		
		.remove-btn {
			position: absolute;
			top: 12rpx;
			right: 12rpx;
			width: 48rpx;
			height: 48rpx;
			background: rgba(0,0,0,0.5);
			border-radius: 50%;
			display: flex;
			align-items: center;
			justify-content: center;
			
			.iconfont {
				font-size: 24rpx;
				color: #fff;
			}
		}
	}
	
	.product-info {
		padding: 20rpx;
		
		.product-name {
			display: block;
			font-size: 28rpx;
			color: #1e293b;
			margin-bottom: 16rpx;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			overflow: hidden;
			min-height: 76rpx;
		}
		
		.product-bottom {
			display: flex;
			justify-content: space-between;
			align-items: center;
			
			.product-price {
				font-size: 32rpx;
				font-weight: 600;
				color: #10b981;
			}
			
			.add-cart {
				width: 56rpx;
				height: 56rpx;
				background: #10b981;
				border-radius: 50%;
				display: flex;
				align-items: center;
				justify-content: center;
				
				.iconfont {
					font-size: 28rpx;
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
	padding: 200rpx 0;
	display: flex;
	flex-direction: column;
	align-items: center;
	
	.empty-icon {
		font-size: 120rpx;
		margin-bottom: 30rpx;
	}
	
	.empty-text {
		font-size: 32rpx;
		color: #94a3b8;
		margin-bottom: 40rpx;
	}
	
	.empty-btn {
		padding: 24rpx 60rpx;
		background: #10b981;
		color: #fff;
		border-radius: 44rpx;
		font-size: 28rpx;
		font-weight: 500;
	}
}

/* 图标 */
.icon-close::before { content: "✕"; }
.icon-cart::before { content: "🛒"; }
</style>
