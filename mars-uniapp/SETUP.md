# GreenMall 小程序 - 快速配置指南

## ⚠️ 必读：Font Awesome 字体文件配置

**项目无法显示图标？** 请先完成以下步骤：

### 🔥 第一步：下载字体文件（必需！）

详细步骤请查看：**`FONT-SETUP.md`**

快速步骤：
1. 下载3个字体文件（.woff2格式）
2. 放到 `static/fonts/` 目录
3. 重新编译项目

**下载链接**：见 `static/fonts/DOWNLOAD.md`

---

## 一、安装依赖

本项目基于 **uni-app** 框架开发，使用 **Font Awesome** 图标库（Unicode编码）。

### 1. 开发工具
- HBuilderX (推荐最新版)
- 微信开发者工具
- 或其他小程序开发工具

## 二、项目配置

### 1. Font Awesome 图标配置

项目已在每个页面通过 CDN 引入 Font Awesome：

```scss
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css');
```

**注意**: 如果小程序平台不支持CDN，请下载字体文件到本地：

1. 下载 Font Awesome 字体文件
2. 放置到 `static/fonts/` 目录
3. 修改引入路径为本地路径

### 2. TabBar 图标准备

方案一：准备图片图标（推荐）
- 在 `static/tabbar/` 目录准备8个图标文件
- 参考 `static/tabbar/README.md`

方案二：使用 iconfont 字体图标
- 修改 `pages.json` 中 tabBar 配置
- 使用 `iconfontSrc` 指定字体文件

### 3. manifest.json 配置

修改以下配置：
```json
{
  "mp-weixin": {
    "appid": "你的小程序AppID"
  }
}
```

## 三、运行项目

### 微信小程序

1. 在 HBuilderX 中打开项目
2. 点击菜单: 运行 > 运行到小程序模拟器 > 微信开发者工具
3. 首次运行会自动打开微信开发者工具
4. 在微信开发者工具中预览

### 其他平台

- 支付宝小程序: 运行 > 支付宝小程序
- 百度小程序: 运行 > 百度小程序
- 字节跳动小程序: 运行 > 抖音小程序

## 四、页面说明

### 主页面（已配置TabBar）

| 页面 | 路径 | 说明 |
|------|------|------|
| 首页 | /pages/index/index | 商品展示、分类入口 |
| 分类 | /pages/category/index | 分类浏览 |
| 购物车 | /pages/cart/index | 购物车管理 |
| 我的 | /pages/profile/index | 个人中心 |

### 功能页面

| 页面 | 路径 | 说明 |
|------|------|------|
| 搜索 | /pages/search/index | 搜索商品 |
| 详情 | /pages/detail/index | 商品详情 |
| 登录 | /pages/login/index | 登录入口 |
| 密码登录 | /pages/login/password | 账号密码登录 |
| 订单列表 | /pages/orders/index | 我的订单 |
| 确认订单 | /pages/order/index | 确认订单信息 |
| 支付成功 | /pages/order/success | 支付结果 |
| 设置 | /pages/settings/index | 设置页面 |

## 五、主题色配置

项目使用统一主题色 `#059669`，如需修改：

1. 全局搜索替换 `#059669` 为新颜色
2. 同时修改相关 rgba 值：
   - `rgba(5, 150, 105, 0.1)` - 10%透明度
   - `rgba(5, 150, 105, 0.2)` - 20%透明度（阴影）

## 六、注意事项

### 1. 自定义导航栏

所有页面使用自定义导航栏，需要处理：
- 状态栏高度适配
- 返回按钮功能
- 标题居中

### 2. Font Awesome 图标

使用方式：
```vue
<text class="fas fa-home"></text>      <!-- 实心 -->
<text class="far fa-heart"></text>     <!-- 线性 -->
<text class="fab fa-weixin"></text>    <!-- 品牌 -->
```

### 3. 页面跳转

- TabBar页面: `uni.switchTab()`
- 普通页面: `uni.navigateTo()`
- 返回: `uni.navigateBack()`
- 重定向: `uni.reLaunch()`

### 4. 数据接口

当前使用模拟数据，需要对接后端API：
- 修改各页面的 data 数据源
- 添加API请求方法
- 处理加载状态

## 七、开发建议

### 1. 状态管理

建议使用 Vuex 或 Pinia 管理：
- 用户信息
- 购物车数据
- 登录状态

### 2. 工具函数

创建 `utils/` 目录：
- `api.js` - 接口请求
- `util.js` - 工具函数
- `storage.js` - 本地存储
- `auth.js` - 登录鉴权

### 3. 组件封装

建议封装的公共组件：
- 商品卡片组件
- Toast提示组件
- Loading组件
- 空状态组件

## 八、发布上线

### 1. 版本号管理

修改 `manifest.json`:
```json
{
  "versionName": "1.0.0",
  "versionCode": "100"
}
```

### 2. 小程序信息

在微信公众平台配置：
- 小程序名称
- 小程序简介
- 服务类目
- 接口权限

### 3. 代码审核

- 完善隐私政策
- 添加用户协议
- 配置合法域名

## 需要帮助？

查看详细文档：
- [uni-app 官方文档](https://uniapp.dcloud.net.cn/)
- [Font Awesome 图标库](https://fontawesome.com/icons)
- [微信小程序文档](https://developers.weixin.qq.com/miniprogram/dev/framework/)

---

**当前版本**: v1.0.0  
**最后更新**: 2026-02-03
