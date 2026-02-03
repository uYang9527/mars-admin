<template>
  <view class="page">
    <!-- 头部绿色区域 -->
    <view class="header">
      <!-- 状态栏占位 -->
      <view :style="{ height: statusBarHeight + 'px' }"></view>

      <!-- 位置和通知 -->
      <view class="header-top">
        <view class="location">
          <text class="location-label">当前位置</text>
          <view class="location-value">
            <text class="fa fa-map-marker-alt"></text>
            <text>杭州市, 西湖区</text>
          </view>
        </view>
        <view class="notification" @click="goNotification">
          <text class="far fa-bell"></text>
          <view class="badge"></view>
        </view>
      </view>

      <!-- 搜索框 -->
      <view class="search-bar" @click="goSearch">
        <text class="fas fa-search"></text>
        <text class="search-placeholder">搜索新鲜水果、蔬菜...</text>
        <text class="search-btn">搜索</text>
      </view>
    </view>

    <!-- 内容区域 -->
    <scroll-view class="content" scroll-y>
      <!-- 轮播图 -->
      <swiper class="banner" indicator-dots indicator-color="rgba(255,255,255,0.5)"
              indicator-active-color="#059669" autoplay circular>
        <swiper-item v-for="(banner, index) in banners" :key="index">
          <image class="banner-image" :src="banner.image" mode="aspectFill"></image>
          <view class="banner-overlay"></view>
          <view class="banner-info">
            <text class="banner-tag">新品上市</text>
            <text class="banner-title">{{ banner.title }}</text>
          </view>
        </swiper-item>
      </swiper>


      <!-- 推荐商品 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">每日优选</text>
          <view class="section-more" @click="goProductList">
            <text>查看全部</text>
            <text class="fas fa-chevron-right"></text>
          </view>
        </view>

        <view class="product-grid">
          <view class="product-card" v-for="product in products" :key="product.id"
                @click="goDetail(product.id)">
            <view class="product-image-wrap">
              <image class="product-image" :src="product.image" mode="aspectFill"></image>
         
            </view>
            <view class="product-info">
              <text class="product-name">{{ product.name }}</text>
              <text class="product-desc">{{ product.desc }}</text>
              <view class="product-bottom">
                <text class="product-price">¥{{ product.price }}</text>
                <view class="product-add" @click.stop="addCart(product)">
                  <text class="fas fa-plus"></text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 底部安全区域 -->
      <view class="safe-bottom"></view>
    </scroll-view>

    <!-- Toast提示 -->
    <view class="toast" :class="{ show: showToast }">
      <text class="fas fa-check-circle"></text>
      <text>{{ toastText }}</text>
    </view>
  </view>
</template>

<script>
import { addToCart, updateCartBadge } from '@/utils/cart.js'
import { getHomeData, addToCart as apiAddToCart } from '@/utils/api.js'

export default {
  data() {
    return {
      statusBarHeight: 0,
      loading: true,
      banners: [],
      categories: [],
      products: [],
      showToast: false,
      toastText: ''
    }
  },
  onLoad() {
    const systemInfo = uni.getSystemInfoSync()
    this.statusBarHeight = systemInfo.statusBarHeight || 0
    this.loadHomeData()
  },
  onShow() {
    // 更新购物车角标
    updateCartBadge()
  },
  onPullDownRefresh() {
    this.loadHomeData().finally(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    // 加载首页数据
    async loadHomeData() {
      this.loading = true
      try {
        const res = await getHomeData()
        if (res.code === 200 && res.data) {
          // 轮播图
          this.banners = (res.data.banners || []).map(item => ({
            id: item.id,
            image: item.image,
            title: item.title,
            subtitle: item.subtitle,
            linkType: item.linkType,
            linkValue: item.linkValue
          }))
          
          // 分类
          this.categories = (res.data.categories || []).map(item => ({
            id: item.id,
            name: item.name,
            icon: this.getCategoryIcon(item.icon)
          }))
          
          // 推荐商品
          this.products = (res.data.recommendProducts || []).map(item => ({
            id: item.id,
            name: item.name,
            desc: item.subtitle,
            price: item.price,
            image: item.mainImage,
            isFavorite: false
          }))
        }
      } catch (e) {
        console.error('加载首页数据失败', e)
        // 如果加载失败，使用默认数据
        this.useDefaultData()
      } finally {
        this.loading = false
      }
    },
    // 获取分类图标
    getCategoryIcon(icon) {
      if (!icon) return 'fas fa-tag'
      // 如果已经是完整的类名，直接返回
      if (icon.startsWith('fa')) return icon
      // 否则添加 fas 前缀
      return `fas ${icon}`
    },
    // 使用默认数据（接口失败时）
    useDefaultData() {
      this.banners = [{
        id: 1,
        image: 'https://images.unsplash.com/photo-1542838132-92c53300491e?q=80&w=1000&auto=format&fit=crop',
        title: '有机牛油果 5折起'
      }]
      this.categories = [
        {id: 1, name: '水果', icon: 'fas fa-apple-alt'},
        {id: 2, name: '蔬菜', icon: 'fas fa-carrot'},
        {id: 3, name: '海鲜', icon: 'fas fa-fish'},
        {id: 4, name: '肉类', icon: 'fas fa-drumstick-bite'}
      ]
      this.products = [
        {id: 1, name: '智利进口菠萝', desc: '单果重约1.5kg', price: '29.9',
         image: 'https://images.unsplash.com/photo-1550258987-190a2d41a8ba?auto=format&fit=crop&w=400', isFavorite: false},
        {id: 2, name: '有机阳光草莓', desc: '甜度15+ 500g/盒', price: '45.0',
         image: 'https://images.unsplash.com/photo-1601004890684-d8cbf643f5f2?auto=format&fit=crop&w=400', isFavorite: false}
      ]
    },
    goSearch() {
      uni.navigateTo({url: '/pages/search/index'})
    },
    goNotification() {
      uni.showToast({title: '暂无通知', icon: 'none'})
    },
    goCategory(id) {
      uni.switchTab({url: '/pages/category/index'})
    },
    goProductList() {
      uni.navigateTo({url: '/pages/search/index'})
    },
    goDetail(id) {
      uni.navigateTo({url: `/pages/detail/index?id=${id}`})
    },
    async addCart(product) {
      // 检查是否登录
      const memberInfo = uni.getStorageSync('memberInfo')
      if (memberInfo && memberInfo.memberId) {
        // 已登录，调用后端API
        try {
          await apiAddToCart(product.id, null, 1)
          updateCartBadge()
          this.showToastMessage(`${product.name} 已加入购物车`)
        } catch (e) {
          console.error('添加购物车失败', e)
        }
      } else {
        // 未登录，使用本地购物车
        addToCart(product)
        this.showToastMessage(`${product.name} 已加入购物车`)
      }
    },
    toggleFavorite(product) {
      product.isFavorite = !product.isFavorite
      this.showToastMessage(product.isFavorite ? '已收藏' : '已取消收藏')
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
  background-color: #F8FAFC;
  display: flex;
  flex-direction: column;
}

.header {
  width: 100%;
  background: linear-gradient(135deg, #059669 0%, #047857 100%);
  padding: 0 24rpx 32rpx;
  border-radius: 0 0 80rpx 80rpx;
  box-sizing: border-box;
}

.header-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 0;
  color: #fff;
}

.location {
  .location-label {
    font-size: 24rpx;
    opacity: 0.8;
    display: block;
  }

  .location-value {
    display: flex;
    align-items: center;
    gap: 8rpx;
    font-size: 36rpx;
    font-weight: 600;
    margin-top: 8rpx;

    .fa {
      font-size: 28rpx;
    }
  }
}

.notification {
  width: 80rpx;
  height: 80rpx;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;

  .fa {
    font-size: 36rpx;
  }

  .badge {
    position: absolute;
    top: 0;
    right: 0;
    width: 24rpx;
    height: 24rpx;
    background: #ef4444;
    border-radius: 50%;
    border: 4rpx solid #059669;
  }
}
.fa-plus{
	color: #ffffff;
	font-size: 32rpx;
}

.search-bar {
  background: #fff;
  border-radius: 48rpx;
  height: 96rpx;
  display: flex;
  align-items: center;
  padding: 0 32rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);

  .fa {
    color: #94a3b8;
    font-size: 32rpx;
    margin-right: 24rpx;
  }

  .search-placeholder {
    flex: 1;
    color: #94a3b8;
    font-size: 28rpx;
  }

  .search-btn {
    color: #059669;
    font-size: 28rpx;
    font-weight: 500;
    padding-left: 24rpx;
    border-left: 2rpx solid #e2e8f0;
  }
}

.content {
  width: 100%;
  height: calc(100vh - 360rpx);
  padding: 0 24rpx;
  margin-top: -40rpx;
  box-sizing: border-box;
}

.banner {
  margin-top: 20rpx;
  height: 320rpx;
  border-radius: 32rpx;
  overflow: hidden;
  margin-bottom: 48rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);

  swiper-item {
    position: relative;
  }

  .banner-image {
    width: 100%;
    height: 100%;
  }

  .banner-overlay {
    position: absolute;
    inset: 0;
    background: rgba(0, 0, 0, 0.1);
  }

  .banner-info {
    position: absolute;
    left: 32rpx;
    bottom: 32rpx;
    color: #fff;

    .banner-tag {
      display: inline-block;
      background: #059669;
      padding: 8rpx 20rpx;
      border-radius: 12rpx;
      font-size: 24rpx;
      font-weight: 600;
      margin-bottom: 12rpx;
    }

    .banner-title {
      display: block;
      font-size: 40rpx;
      font-weight: 700;
    }
  }
}

.categories {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32rpx;
  margin-bottom: 64rpx;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16rpx;

  .category-icon {
    width: 112rpx;
    height: 112rpx;
    border-radius: 50%;
    background: rgba(5, 150, 105, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    transition: transform 0.3s;

    .fa {
      font-size: 48rpx;
      color: #059669;
    }
  }

  &:active .category-icon {
    transform: scale(0.95);
  }

  .category-name {
    font-size: 24rpx;
    color: #475569;
    font-weight: 500;
  }
}

.section {
  margin-bottom: 48rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 32rpx;

  .section-title {
    font-size: 36rpx;
    font-weight: 700;
    color: #1e293b;
  }

  .section-more {
    display: flex;
    align-items: center;
    gap: 8rpx;
    color: #059669;
    font-size: 24rpx;
    font-weight: 500;

    .fa {
      font-size: 20rpx;
    }
  }
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 32rpx;
}

.product-card {
  background: #fff;
  border-radius: 32rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.03);

  .product-image-wrap {
    position: relative;
    height: 256rpx;
    border-radius: 24rpx;
    overflow: hidden;
    margin-bottom: 24rpx;

    .product-image {
      width: 100%;
      height: 100%;
      transition: transform 0.5s;
    }

    .product-favorite {
      position: absolute;
      top: 16rpx;
      right: 16rpx;
      width: 56rpx;
      height: 56rpx;
      background: rgba(255, 255, 255, 0.9);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);

      .fa {
        font-size: 24rpx;
        color: #94a3b8;

        &.fa-heart.fas {
          color: #ef4444;
        }
      }
    }
  }

  &:active .product-image {
    transform: scale(1.1);
  }

  .product-info {
    .product-name {
      font-size: 28rpx;
      font-weight: 600;
      color: #1e293b;
      margin-bottom: 8rpx;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .product-desc {
      font-size: 24rpx;
      color: #94a3b8;
      margin-bottom: 16rpx;
    }

    .product-bottom {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .product-price {
        font-size: 36rpx;
        font-weight: 700;
        color: #059669;
      }

      .product-add {
        width: 56rpx;
        height: 56rpx;
        background: #059669;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: opacity 0.3s;

        .fa {
          font-size: 24rpx;
          color: #fff;
        }

        &:active {
          opacity: 0.8;
        }
      }
    }
  }
}

.safe-bottom {
  height: 200rpx;
}

.toast {
  position: fixed;
  top: 80rpx;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: #fff;
  padding: 24rpx 48rpx;
  border-radius: 100rpx;
  display: flex;
  align-items: center;
  gap: 16rpx;
  opacity: 0;
  transition: opacity 0.3s;
  z-index: 9999;
  box-shadow: 0 10px 40rpx rgba(0, 0, 0, 0.3);

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
