# Font Awesome 图标使用说明

## ⚠️ 重要：需要下载字体文件

微信小程序**不支持** CDN 字体加载，需要将 Font Awesome 字体文件下载到本地。

## 快速配置（3步）

### 步骤 1: 下载字体文件

从以下链接下载 Font Awesome 字体文件：

1. **fa-solid-900.woff2** (实心图标)  
   https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/webfonts/fa-solid-900.woff2

2. **fa-regular-400.woff2** (线性图标)  
   https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/webfonts/fa-regular-400.woff2

3. **fa-brands-400.woff2** (品牌图标)  
   https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/webfonts/fa-brands-400.woff2

### 步骤 2: 放置字体文件

将下载的3个文件放到 `static/fonts/` 目录：

```
static/
  └── fonts/
      ├── fa-solid-900.woff2
      ├── fa-regular-400.woff2
      ├── fa-brands-400.woff2
      ├── font-awesome.css
      └── README.md
```

### 步骤 3: 修改 font-awesome.css

修改 `font-awesome.css` 中的字体路径：

```css
@font-face {
  font-family: 'FontAwesome';
  src: url('./fa-solid-900.woff2') format('woff2');
  font-weight: 900;
}

@font-face {
  font-family: 'FontAwesome';
  src: url('./fa-regular-400.woff2') format('woff2');
  font-weight: 400;
}

@font-face {
  font-family: 'FontAwesome';
  src: url('./fa-brands-400.woff2') format('woff2');
  font-weight: 400;
}
```

## 当前状态

✅ 已配置所有图标的 Unicode 编码  
⚠️ 需要手动下载字体文件（3个文件）  
⚠️ 需要修改 CSS 中的字体路径为本地路径

## 使用方式

在 Vue 模板中直接使用：

```vue
<template>
  <text class="fas fa-home"></text>
  <text class="far fa-heart"></text>
  <text class="fab fa-weixin"></text>
</template>
```

## 替代方案（推荐）

如果需要更专业的图标效果，建议使用 **iconfont** 方案：

### 方案一：阿里巴巴 iconfont

1. 访问 [iconfont.cn](https://www.iconfont.cn/)
2. 选择 Font Awesome 图标库
3. 下载字体文件（.ttf 格式）
4. 放置到 `static/fonts/` 目录
5. 引入字体文件：

```scss
// App.vue
<style>
@font-face {
  font-family: 'FontAwesome';
  src: url('./static/fonts/fontawesome.ttf') format('truetype');
}

.fa, .fas, .far, .fab {
  font-family: 'FontAwesome' !important;
}
</style>
```

### 方案二：使用 Uni Icons

uni-app 官方图标库，完美支持小程序：

```bash
npm install @dcloudio/uni-ui
```

使用方式：
```vue
<uni-icons type="home" size="24"></uni-icons>
```

## 图标列表

### 已实现的图标映射

| 类名 | 字符 | 说明 |
|------|------|------|
| `fa-home` | 🏠 | 首页 |
| `fa-search` | 🔍 | 搜索 |
| `fa-shopping-cart` | 🛒 | 购物车 |
| `fa-user` | 👤 | 用户 |
| `fa-heart` | ♡/♥ | 收藏 |
| `fa-plus` | + | 加号 |
| `fa-minus` | - | 减号 |
| `fa-check` | ✓ | 勾选 |
| `fa-star` | ⭐ | 星星 |
| `fa-wallet` | 👛 | 钱包 |
| `fa-crown` | 👑 | 皇冠 |
| `fa-leaf` | 🍃 | 叶子 |
| 更多... | ... | 参考 font-awesome.css |

## 推荐做法

### 生产环境建议

1. **下载 Font Awesome 字体文件**
   - 从 [fontawesome.com](https://fontawesome.com/) 下载完整包
   - 提取 `.woff2` 或 `.ttf` 字体文件
   - 放置到 `static/fonts/` 目录

2. **配置字体**
   ```scss
   @font-face {
     font-family: 'FontAwesome';
     src: url('./static/fonts/fa-solid-900.woff2') format('woff2');
     font-weight: 900;
   }
   
   @font-face {
     font-family: 'FontAwesome';
     src: url('./static/fonts/fa-regular-400.woff2') format('woff2');
     font-weight: 400;
   }
   
   @font-face {
     font-family: 'FontAwesome';
     src: url('./static/fonts/fa-brands-400.woff2') format('woff2');
     font-weight: 400;
   }
   ```

3. **使用 Unicode**
   ```vue
   <text class="fas">\uf015</text>  <!-- home icon -->
   ```

## 需要帮助？

- Font Awesome 官网: https://fontawesome.com/
- iconfont 官网: https://www.iconfont.cn/
- uni-app 图标: https://uniapp.dcloud.net.cn/component/uniui/uni-icons.html

---

**当前版本**: v1.0.0 (使用 Emoji/Unicode 字符)  
**推荐升级**: 使用 iconfont 字体文件方案
