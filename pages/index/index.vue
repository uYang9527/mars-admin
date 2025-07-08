<!-- This file is being rewritten. --> 

<template>
	<view class="container">
		<!-- 1. 顶部轮播图 -->
		<swiper class="banner-swiper" circular indicator-dots autoplay :interval="3000" :duration="500">
			<swiper-item v-for="item in swiperItems" :key="item.id">
				<image :src="item.imageUrl" class="banner-image" mode="aspectFill"></image>
			</swiper-item>
		</swiper>

		<!-- 2. 快捷功能菜单 -->
		<view class="quick-menu card">
			<h3 class="quick-title gradient-text">快捷功能</h3>
			<view class="quick-grid">
				<view class="quick-item" v-for="(item, index) in quickMenuItems" :key="index" @click="handleQuickAction(item.action)">
					<view class="quick-icon">
						<image :src="item.icon" class="icon-image" mode="aspectFit"></image>
					</view>
					<text class="quick-text">{{ item.name }}</text>
				</view>
			</view>
		</view>

		<!-- 3. 公司公告 -->
		<view class="notice-card card">
			<view class="card-header">
				<h3 class="card-title gradient-text">公司公告</h3>
				<view class="more-link" @click="goToMore('notices')">
					<text>更多</text>
					<uni-icons type="right" size="14" color="#667eea"></uni-icons>
				</view>
			</view>
			<view class="notice-list">
				<view class="notice-item" v-for="notice in notices" :key="notice.id">
					<view class="notice-title-wrapper">
						<view class="tag" :class="notice.tag_type">{{notice.tag}}</view>
						<text class="notice-title">{{ notice.title }}</text>
					</view>
					<text class="notice-date">{{ notice.date }}</text>
				</view>
			</view>
		</view>

	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	import {
		onPullDownRefresh
	} from '@dcloudio/uni-app';

	// 轮播图数据
	const swiperItems = ref([{
		id: 1,
		imageUrl: 'https://images.unsplash.com/photo-1519389950473-47ba0277781c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1740&q=80'
	}, {
		id: 2,
		imageUrl: 'https://images.unsplash.com/photo-1522071820081-009f0129c71c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1740&q=80'
	}, {
		id: 3,
		imageUrl: 'https://images.unsplash.com/photo-1552664730-d307ca884978?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1740&q=80'
	}]);

	// 快捷功能菜单数据
	const quickMenuItems = ref([{
		name: '考勤打卡',
		icon: 'https://img.icons8.com/fluency/96/000000/clock.png',
		action: 'attendance'
	}, {
		name: '请假申请',
		icon: 'https://img.icons8.com/fluency/96/000000/calendar.png',
		action: 'leave'
	}, {
		name: '报销申请',
		icon: 'https://img.icons8.com/fluency/96/000000/money-bag.png',
		action: 'expense'
	}, {
		name: '工作汇报',
		icon: 'https://img.icons8.com/fluency/96/000000/report-card.png',
		action: 'report'
	}, {
		name: '会议室预订',
		icon: 'https://img.icons8.com/fluency/96/000000/conference-call.png',
		action: 'meeting'
	}, {
		name: '通讯录',
		icon: 'https://img.icons8.com/fluency/96/000000/contacts.png',
		action: 'contacts'
	}, {
		name: '文件管理',
		icon: 'https://img.icons8.com/fluency/96/000000/folder-invoices.png',
		action: 'files'
	}, {
		name: '更多',
		icon: 'https://img.icons8.com/fluency/96/000000/grid.png',
		action: 'more'
	}]);

	// 公司公告数据
	const notices = ref([{
		id: 1,
		title: '关于2024年春节放假安排的通知',
		date: '2024-01-15',
		tag: '重要',
		tag_type: 'important'
	}, {
		id: 2,
		title: '公司年度表彰大会即将召开',
		date: '2024-01-10',
		tag: '通知',
		tag_type: 'info'
	}, {
		id: 3,
		title: '新版企业协同APP正式上线通知',
		date: '2024-01-08',
		tag: '系统',
		tag_type: 'system'
	}]);

	// 下拉刷新
	onPullDownRefresh(() => {
		console.log('refresh');
		setTimeout(() => {
			uni.stopPullDownRefresh();
			uni.showToast({
				title: '刷新成功',
				icon: 'none'
			});
		}, 1000);
	});

	// 快捷功能点击事件
	const handleQuickAction = (action) => {
		uni.showToast({
			title: `点击了: ${action}`,
			icon: 'none'
		});
	};

	// 查看更多
	const goToMore = (type) => {
		uni.showToast({
			title: `查看更多 ${type}`,
			icon: 'none'
		});
	};
</script>

<style lang="scss" scoped>
	/* 全局容器和背景 */
	.container {
		background: linear-gradient(180deg, #f3f5fc 0%, #ffffff 100%);
		min-height: 100vh;
		padding-bottom: 50rpx;
		box-sizing: border-box;
	}

	/* 卡片统一样式 */
	.card {
		background: rgba(255, 255, 255, 0.8);
		backdrop-filter: blur(20px);
		-webkit-backdrop-filter: blur(20px);
		border: 1px solid rgba(255, 255, 255, 0.25);
		border-radius: 32rpx;
		margin: 32rpx;
		padding: 32rpx;
		box-shadow: 0 16rpx 64rpx 0 rgba(31, 38, 135, 0.1);
		animation: fadeInUp 0.7s ease-out forwards;
		opacity: 0;
	}

	.card-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 32rpx;
	}

	.card-title {
		font-size: 36rpx;
		font-weight: 600;
	}

	.gradient-text {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		-webkit-background-clip: text;
		background-clip: text;
		color: transparent;
	}

	.more-link {
		display: flex;
		align-items: center;
		font-size: 26rpx;
		color: #667eea;
	}

	/* 1. 轮播图 */
	.banner-swiper {
		width: 100%;
		height: 400rpx;
	}

	.banner-image {
		width: 100%;
		height: 100%;
	}

	/* 2. 快捷功能 */
	.quick-menu {
		margin-top: -64rpx; // 使其与轮播图有部分重叠
		position: relative;
		z-index: 10;
		animation-delay: 0.1s;
	}

	.quick-title {
		font-size: 36rpx;
		font-weight: 600;
		margin-bottom: 32rpx;
	}

	.quick-grid {
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 32rpx;
	}

	.quick-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		transition: transform 0.2s ease-in-out;
	}
	
	.quick-item:active {
		transform: scale(0.92);
	}

	.quick-icon {
		width: 100rpx;
		height: 100rpx;
		margin-bottom: 16rpx;
		background-color: #f0f2f7;
		border-radius: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);
	}

	.icon-image {
		width: 60rpx;
		height: 60rpx;
	}

	.quick-text {
		font-size: 26rpx;
		color: #333;
	}

	/* 3. 公司公告 */
	.notice-card {
		animation-delay: 0.2s;
	}

	.notice-list {
		display: flex;
		flex-direction: column;
		gap: 32rpx;
	}

	.notice-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	
	.notice-title-wrapper {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.tag {
		padding: 4rpx 12rpx;
		border-radius: 8rpx;
		font-size: 22rpx;
		font-weight: 500;
		color: #fff;
		
		&.important {
			background: linear-gradient(135deg, #ff7e5f, #feb47b);
		}
		&.info {
			background: linear-gradient(135deg, #667eea, #764ba2);
		}
		&.system {
			background: linear-gradient(135deg, #30cfd0, #330867);
		}
	}

	.notice-title {
		font-size: 28rpx;
		color: #333;
		// 超出省略
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		max-width: 400rpx;
	}

	.notice-date {
		font-size: 24rpx;
		color: #999;
	}

	/* 动画 */
	@keyframes fadeInUp {
		from {
			opacity: 0;
			transform: translateY(40rpx);
		}
		to {
			opacity: 1;
			transform: translateY(0);
		}
	}
</style> 