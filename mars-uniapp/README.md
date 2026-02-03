# GreenMall 小程序 - 绿色商城

基于商城原型HTML一比一复刻的uni-app小程序项目

## 项目特点

- ✅ **主题色**: #059669（翡翠绿）
- ✅ **图标库**: Font Awesome 6.4.0
- ✅ **UI风格**: 现代简约，高质感设计
- ✅ **页面数量**: 12个完整页面
- ✅ **响应式**: 适配所有小程序平台

## 技术栈

- uni-app (Vue 3)
- SCSS
- Font Awesome Icons
- 原生小程序API

## 页面结构

### 主要页面（TabBar）

1. **首页** (`pages/index/index.vue`)
   - 渐变头部设计
   - 位置显示 + 通知中心
   - 搜索框
   - 轮播图
   - 分类入口（4宫格）
   - 推荐商品列表

2. **分类页** (`pages/category/index.vue`)
   - 左右分栏布局
   - 左侧分类导航
   - 右侧商品展示
   - 分类Banner

3. **购物车** (`pages/cart/index.vue`)
   - 商品列表
   - 商品选择
   - 数量增减
   - 全选功能
   - 合计金额
   - 去结算

4. **个人中心** (`pages/profile/index.vue`)
   - 渐变头部 + 毛玻璃卡片
   - 用户信息展示
   - 数据统计（优惠券、积分、余额、钱包）
   - 订单状态（5个入口）
   - 服务菜单（带彩色图标）

### 功能页面

5. **商品详情** (`pages/detail/index.vue`)
   - 商品大图
   - 商品信息
   - 规格选择
   - 配送信息
   - 加入购物车 / 立即购买

6. **搜索页** (`pages/search/index.vue`)
   - 搜索框
   - 热门搜索标签
   - 搜索历史
   - 历史清除

7. **订单列表** (`pages/orders/index.vue`)
   - 订单状态筛选
   - 订单详情卡片
   - 操作按钮

8. **确认订单** (`pages/order/index.vue`)
   - 收货地址
   - 商品预览
   - 支付方式选择
   - 立即支付

9. **支付成功** (`pages/order/success.vue`)
   - 成功动画
   - 订单信息
   - 返回首页/查看订单

10. **登录页** (`pages/login/index.vue`)
    - Logo展示
    - 微信授权登录
    - 手机号一键登录
    - 账号密码登录入口

11. **账号密码登录** (`pages/login/password.vue`)
    - 手机号输入
    - 密码输入
    - 记住密码
    - 忘记密码

12. **设置页** (`pages/settings/index.vue`)
    - 账号信息
    - 通用设置（开关）
    - 关于信息
    - 退出登录

## 设计规范

### 颜色规范
```scss
主色调: #059669 (翡翠绿)
辅助色: #047857 (深绿)
背景色: #f8fafc (浅灰)
文字色: #1e293b (深灰)
次要文字: #64748b (中灰)
占位文字: #94a3b8 (浅灰)
```

### 尺寸规范
```scss
圆角大: 32rpx
圆角中: 24rpx
圆角小: 16rpx
按钮圆角: 100rpx (全圆)
图标大: 40rpx
图标中: 32rpx
图标小: 24rpx
间距大: 48rpx
间距中: 32rpx
间距小: 24rpx
```

### 字体规范
```scss
特大标题: 48rpx (bold)
大标题: 40rpx (bold)
标题: 36rpx (bold)
小标题: 32rpx (600)
正文: 28rpx (normal)
辅助文字: 24rpx
说明文字: 20rpx
```

## Font Awesome 使用

由于微信小程序不支持外部CDN，项目使用了 **Unicode字符 + Emoji** 方案替代。

### 当前方案（已实现）

已在 `static/fonts/font-awesome.css` 中映射了所有使用的图标：

```vue
<text class="fas fa-home"></text>      <!-- 🏠 首页 -->
<text class="fas fa-search"></text>    <!-- 🔍 搜索 -->
<text class="fas fa-heart"></text>     <!-- ♥ 收藏 -->
```

### 图标样式
- `fas` - Solid (实心)
- `far` - Regular (线性)  
- `fab` - Brands (品牌)

### 推荐方案（生产环境）

**使用 iconfont 字体文件**：
1. 下载 Font Awesome 字体文件（.ttf）
2. 放置到 `static/fonts/` 目录
3. 配置 `@font-face` 引入

详细说明请查看：`static/fonts/README.md`

## 运行项目

### ⚠️ 运行前必须完成（重要！）

**下载 Font Awesome 字体文件**：
1. 打开 `static/fonts/DOWNLOAD.md`
2. 下载3个字体文件（.woff2格式）
3. 放置到 `static/fonts/` 目录
4. 完成后项目才能正常显示图标

### 运行步骤

1. 使用 HBuilderX 导入项目
2. **完成字体文件下载**（参考上方）
3. 选择运行到微信开发者工具
4. 确保已安装微信开发者工具

## 注意事项

1. **Font Awesome 在小程序中的使用**：
   - 已通过 `@import` 在每个页面引入
   - 支持所有平台（微信、支付宝、百度等）
   - 使用 `<text>` 标签 + class 方式

2. **自定义导航栏**：
   - 所有页面使用 `navigationStyle: custom`
   - 需要手动处理状态栏高度
   - 使用 `uni.getSystemInfoSync()` 获取

3. **主题色统一**：
   - 所有主题相关颜色统一使用 #059669
   - 阴影使用 rgba(5, 150, 105, 0.2)

4. **TabBar 图标**：
   - 需要准备 `static/tabbar/` 目录下的图标
   - 或者改用 `iconfontPath` 使用图标字体

## 目录结构

```
├── pages/              # 页面目录
│   ├── index/         # 首页
│   ├── category/      # 分类页
│   ├── cart/          # 购物车
│   ├── profile/       # 个人中心
│   ├── detail/        # 商品详情
│   ├── search/        # 搜索页
│   ├── login/         # 登录相关
│   ├── orders/        # 订单列表
│   ├── order/         # 订单确认/成功
│   └── settings/      # 设置页
├── static/            # 静态资源
│   └── tabbar/        # TabBar图标
├── App.vue            # 应用入口
├── pages.json         # 页面配置
├── manifest.json      # 应用配置
└── README.md          # 说明文档
```

## 待完善功能

- [ ] API接口对接
- [ ] 用户登录鉴权
- [ ] 购物车数据持久化
- [ ] 订单支付流程
- [ ] 图片上传功能
- [ ] TabBar图标资源

## 版本

v1.0.0 - 2026-02-03

基于HTML原型完美复刻！✨
