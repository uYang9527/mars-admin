<template>
	<view class="page">
		<!-- 头部 -->
		<view class="header">
			<view :style="{ height: statusBarHeight + 'px' }"></view>
			<view class="header-content">
				<view class="search-bar" @click="goSearch">
					<text class="fas fa-search"></text>
					<text class="search-text">搜索商品</text>
				</view>
				<text class="far fa-comment-dots"></text>
			</view>
		</view>
		
		<!-- 内容区域 -->
		<view class="content">
			<!-- 左侧分类 -->
			<scroll-view class="category-sidebar" scroll-y>
				<view 
					class="category-item"
					:class="{ active: activeCategory === cat.id }"
					v-for="cat in categories" 
					:key="cat.id"
					@click="selectCategory(cat)"
				>
					<text>{{ cat.name }}</text>
				</view>
			</scroll-view>
			
			<!-- 右侧商品 -->
			<scroll-view class="category-content" scroll-y>
				<!-- Banner -->
				<view class="category-banner">
					<view class="banner-content">
						<text class="banner-title">秋季水果节</text>
						<text class="banner-subtitle">满199减50</text>
					</view>
					<text class="fas fa-leaf banner-icon"></text>
				</view>
				
				<!-- 商品标题 -->
				<text class="content-title">热销水果</text>
				
				<!-- 商品网格 -->
				<view class="product-grid">
					<view 
						class="product-item" 
						v-for="product in products" 
						:key="product.id"
						@click="goDetail(product.id)"
					>
						<view class="product-image-wrap">
							<image class="product-image" :src="product.image" mode="aspectFit"></image>
						</view>
						<text class="product-name">{{ product.name }}</text>
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
import { getCategoryList, getProductList } from '@/utils/api.js'

export default {
	data() {
		return {
			statusBarHeight: 0,
			activeCategory: null,
			categories: [],
			products: [],
			loading: false
		}
	},
	onLoad() {
		const systemInfo = uni.getSystemInfoSync()
		this.statusBarHeight = systemInfo.statusBarHeight || 0
		this.loadCategories()
	},
	methods: {
		// 加载分类列表
		async loadCategories() {
			try {
				const res = await getCategoryList()
				if (res.code === 200 && res.data) {
					// 添加"推荐分类"
					this.categories = [
						{ id: 0, name: '推荐分类' },
						...res.data.map(cat => ({
							id: cat.id,
							name: cat.name,
							icon: cat.icon
						}))
					]
					// 默认选中第一个
					if (this.categories.length > 0) {
						this.activeCategory = this.categories[0].id
						this.loadProducts()
					}
				}
			} catch (e) {
				console.error('加载分类失败', e)
				// 使用默认数据
				this.useDefaultCategories()
			}
		},
		// 加载商品列表
		async loadProducts() {
			this.loading = true
			try {
				const params = { pageSize: 20 }
				if (this.activeCategory && this.activeCategory !== 0) {
					params.categoryId = this.activeCategory
				} else {
					// 推荐分类显示推荐商品
					params.isRecommend = 1
				}
				const res = await getProductList(params)
				if (res.code === 200 && res.data) {
					this.products = (res.data.list || []).map(item => ({
						id: item.id,
						name: item.name,
						image: item.mainImage
					}))
				}
			} catch (e) {
				console.error('加载商品失败', e)
			} finally {
				this.loading = false
			}
		},
		// 使用默认分类数据
		useDefaultCategories() {
			this.categories = [
				{ id: 0, name: '推荐分类' },
				{ id: 1, name: '新鲜水果' },
				{ id: 2, name: '时令蔬菜' },
				{ id: 3, name: '肉禽蛋品' },
				{ id: 4, name: '海鲜水产' },
				{ id: 5, name: '乳品烘焙' }
			]
			this.activeCategory = 0
			this.products = [
				{ id: 1, name: '香蕉', image: 'https://images.unsplash.com/photo-1528825871115-3581a5387919?auto=format&fit=crop&w=200' },
				{ id: 2, name: '葡萄', image: 'https://images.unsplash.com/photo-1573501740349-335c03c80a6d?auto=format&fit=crop&w=200' },
				{ id: 3, name: '凤梨', image: 'https://images.unsplash.com/photo-1550258987-190a2d41a8ba?auto=format&fit=crop&w=200' },
				{ id: 4, name: '柠檬', image: 'https://images.unsplash.com/photo-1582281298055-e25b84a30b0b?auto=format&fit=crop&w=200' }
			]
		},
		goSearch() {
			uni.navigateTo({ url: '/pages/search/index' })
		},
		selectCategory(cat) {
			this.activeCategory = cat.id
			this.loadProducts()
		},
		goDetail(id) {
			uni.navigateTo({ url: `/pages/detail/index?id=${id}` })
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
	width: 100%;
	background: #fff;
	border-bottom: 2rpx solid #f1f5f9;
	box-sizing: border-box;
}

.header-content {
	display: flex;
	align-items: center;
	gap: 24rpx;
	padding: 24rpx 32rpx 32rpx;
}

.search-bar {
	flex: 1;
	height: 72rpx;
	background: #f1f5f9;
	border-radius: 100rpx;
	display: flex;
	align-items: center;
	padding: 0 32rpx;
	gap: 16rpx;
	
	.fa {
		font-size: 28rpx;
		color: #94a3b8;
	}
	
	.search-text {
		color: #94a3b8;
		font-size: 28rpx;
	}
}

.fa-comment-dots {
	font-size: 40rpx;
	color: #1e293b;
}

.content {
	flex: 1;
	display: flex;
	overflow: hidden;
}

.category-sidebar {
	width: 192rpx;
	background: #f8fafc;
	height: calc(100vh - 180rpx);
}

.category-item {
	padding: 32rpx 0;
	text-align: center;
	font-size: 24rpx;
	font-weight: 500;
	color: #64748b;
	position: relative;
	transition: all 0.3s;
	
	&.active {
		background: #fff;
		color: #059669;
		font-weight: 700;
		
		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 8rpx;
			height: 40rpx;
			background: #059669;
			border-radius: 0 4rpx 4rpx 0;
		}
	}
	
	&:active {
		background: #f1f5f9;
	}
}

.category-content {
	flex: 1;
	height: calc(100vh - 180rpx);
	padding: 32rpx;
}

.category-banner {
	height: 192rpx;
	background: linear-gradient(135deg, #d1fae5, #a7f3d0);
	border-radius: 16rpx;
	padding: 32rpx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 48rpx;
	overflow: hidden;
	position: relative;
}

.banner-content {
	z-index: 1;
}

.banner-title {
	display: block;
	font-size: 28rpx;
	font-weight: 700;
	color: #064e3b;
	margin-bottom: 8rpx;
}

.banner-subtitle {
	font-size: 24rpx;
	color: #059669;
}

.banner-icon {
	position: absolute;
	right: -32rpx;
	bottom: -32rpx;
	font-size: 192rpx;
	color: #059669;
	opacity: 0.15;
}

.content-title {
	font-size: 28rpx;
	font-weight: 700;
	color: #1e293b;
	margin-bottom: 24rpx;
	display: block;
}

.product-grid {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 24rpx;
}

.product-item {
	text-align: center;
}

.product-image-wrap {
	background: #f8fafc;
	border-radius: 16rpx;
	padding: 16rpx;
	margin-bottom: 16rpx;
	transition: all 0.3s;
}

.product-item:active .product-image-wrap {
	box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.1);
}

.product-image {
	width: 100%;
	height: 160rpx;
}

.product-name {
	font-size: 24rpx;
	color: #1e293b;
}
</style>
