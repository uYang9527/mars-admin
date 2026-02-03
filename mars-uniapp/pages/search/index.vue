<template>
	<view class="page">
		<!-- 搜索栏 -->
		<view class="search-bar">
			<view :style="{ height: statusBarHeight + 'px' }"></view>
			<view class="search-content">
				<text class="fas fa-arrow-left" @click="goBack"></text>
				<view class="search-input">
					<text class="fas fa-search"></text>
					<input 
						type="text" 
						placeholder="搜索新鲜水果、蔬菜..." 
						v-model="keyword"
						@confirm="doSearch"
						focus
					/>
				</view>
				<text class="search-btn" @click="doSearch">搜索</text>
			</view>
		</view>
		
		<!-- 内容 -->
		<scroll-view class="content" scroll-y>
			<!-- 热门搜索 -->
			<view class="section">
				<text class="section-title">热门搜索</text>
				<view class="tags">
					<view 
						class="tag" 
						v-for="(tag, index) in hotSearches" 
						:key="index"
						@click="searchTag(tag)"
					>
						{{ tag }}
					</view>
				</view>
			</view>
			
			<!-- 搜索历史 -->
			<view class="section">
				<view class="section-header">
					<text class="section-title">搜索历史</text>
					<text class="fas fa-trash-alt" @click="clearHistory"></text>
				</view>
				<view class="history-list">
					<view 
						class="history-item" 
						v-for="(item, index) in history" 
						:key="index"
						@click="searchTag(item)"
					>
						<text class="fas fa-history"></text>
						<text class="history-text">{{ item }}</text>
						<text class="fas fa-arrow-right"></text>
					</view>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			statusBarHeight: 0,
			keyword: '',
			hotSearches: ['草莓', '牛油果', '有机蔬菜', '三文鱼', '新鲜水果'],
			history: ['凤梨', '有机草莓']
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
		doSearch() {
			if (!this.keyword.trim()) return
			uni.showToast({ title: `搜索: ${this.keyword}`, icon: 'none' })
		},
		searchTag(tag) {
			this.keyword = tag
			this.doSearch()
		},
		clearHistory() {
			this.history = []
			uni.showToast({ title: '搜索历史已清除', icon: 'none' })
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

.search-bar {
	background: #fff;
	position: sticky;
	top: 0;
	z-index: 10;
}

.search-content {
	display: flex;
	align-items: center;
	gap: 24rpx;
	padding: 28rpx 32rpx 32rpx;
	
	.fa-arrow-left {
		font-size: 36rpx;
		color: #1e293b;
	}
}

.search-input {
	flex: 1;
	height: 80rpx;
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
	
	input {
		flex: 1;
		font-size: 28rpx;
		color: #1e293b;
	}
}

.search-btn {
	color: #059669;
	font-size: 28rpx;
	font-weight: 500;
}

.content {
	width: 100%;
	height: calc(100vh - 160rpx);
	padding: 40rpx 32rpx;
	box-sizing: border-box;
}

.section {
	margin-bottom: 48rpx;
}

.section-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 24rpx;
}

.section-title {
	font-size: 28rpx;
	font-weight: 700;
	color: #1e293b;
}

.fa-trash-alt {
	font-size: 24rpx;
	color: #94a3b8;
}

.tags {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
}

.tag {
	padding: 16rpx 32rpx;
	background: #fff;
	border-radius: 100rpx;
	font-size: 24rpx;
	color: #64748b;
	transition: all 0.3s;
	
	&:active {
		background: rgba(5, 150, 105, 0.05);
		color: #059669;
	}
}

.history-list {
	display: flex;
	flex-direction: column;
	gap: 24rpx;
}

.history-item {
	background: #fff;
	padding: 24rpx;
	border-radius: 24rpx;
	display: flex;
	align-items: center;
	gap: 24rpx;
	
	&:active {
		background: #f8fafc;
	}
	
	.fa-history {
		font-size: 28rpx;
		color: #94a3b8;
	}
	
	.history-text {
		flex: 1;
		font-size: 28rpx;
		color: #1e293b;
	}
	
	.fa-arrow-right {
		font-size: 24rpx;
		color: #cbd5e1;
	}
}
</style>
