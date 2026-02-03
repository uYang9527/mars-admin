<script>
export default {
	onLaunch: function() {
		console.log('App Launch')
		
		// 获取系统信息
		const systemInfo = uni.getSystemInfoSync()
		// 设置全局状态栏高度
		uni.setStorageSync('statusBarHeight', systemInfo.statusBarHeight)
		
		// 检查登录状态
		const token = uni.getStorageSync('token')
		if (!token) {
			// 未登录，可以跳转到登录页
			// uni.reLaunch({ url: '/pages/login/index' })
		}
	},
	onShow: function() {
		console.log('App Show')
	},
	onHide: function() {
		console.log('App Hide')
	}
}
</script>

<style lang="scss">
/* Font Awesome 图标 - 使用 SCSS 文件 */
@import "./static/fonts/fontawesome.scss";

/* 全局样式 */
page {
	width: 100%;
	height: 100%;
	background-color: #F8FAFC;
	font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Microsoft YaHei', sans-serif;
	box-sizing: border-box;
}

/* 重置默认样式 */
view, text, image, button, scroll-view, swiper, input, checkbox, switch, label {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

view, scroll-view, swiper {
	display: block;
}

/* 确保页面容器全宽 */
.page {
	width: 100%;
	min-height: 100vh;
}

/* 按钮默认样式重置 */
button {
	padding: 0;
	margin: 0;
	line-height: normal;
}

button::after {
	border: none;
}

/* Font Awesome 字体 */
.fa, .fas, .far, .fab {
	font-family: "Font Awesome 6 Free", "Font Awesome 6 Brands";
	font-weight: 900;
	font-style: normal;
}

.far {
	font-weight: 400;
}

.fab {
	font-family: "Font Awesome 6 Brands";
}

/* 主题色变量 */
:root {
	--primary-color: #059669;
	--secondary-color: #047857;
	--accent-color: #d1fae5;
	--text-color: #1e293b;
	--text-gray: #64748b;
	--text-light: #94a3b8;
	--border-color: #e2e8f0;
	--bg-color: #F8FAFC;
}

/* 通用类 */
.primary-color {
	color: #059669 !important;
}

.text-center {
	text-align: center;
}

.flex {
	display: flex;
}

.flex-column {
	flex-direction: column;
}

.align-center {
	align-items: center;
}

.justify-center {
	justify-content: center;
}

.justify-between {
	justify-content: space-between;
}

/* 文本省略 */
.ellipsis {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.ellipsis-2 {
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
}

/* 动画 */
@keyframes fadeIn {
	from {
		opacity: 0;
	}
	to {
		opacity: 1;
	}
}

@keyframes slideUp {
	from {
		transform: translateY(100rpx);
		opacity: 0;
	}
	to {
		transform: translateY(0);
		opacity: 1;
	}
}

.fade-in {
	animation: fadeIn 0.3s ease;
}

.slide-up {
	animation: slideUp 0.3s ease;
}
</style>
