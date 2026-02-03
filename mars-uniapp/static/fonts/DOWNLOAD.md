# 下载 Font Awesome 字体文件

## 方法一：直接下载（推荐）

复制以下链接到浏览器下载：

### 1. fa-solid-900.woff2 (实心图标，必需)
```
https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/webfonts/fa-solid-900.woff2
```

### 2. fa-regular-400.woff2 (线性图标，必需)
```
https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/webfonts/fa-regular-400.woff2
```

### 3. fa-brands-400.woff2 (品牌图标，必需)
```
https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/webfonts/fa-brands-400.woff2
```

## 方法二：从官网下载

1. 访问：https://fontawesome.com/download
2. 下载免费版本
3. 解压后找到 `webfonts` 目录
4. 复制 `.woff2` 文件到项目的 `static/fonts/` 目录

## 下载后的操作

### 1. 确认文件位置

确保3个字体文件都在 `static/fonts/` 目录：

```
E:\java-code4\mars-admin\mars-uniapp\static\fonts\
├── fa-solid-900.woff2      ✅
├── fa-regular-400.woff2    ✅
├── fa-brands-400.woff2     ✅
├── font-awesome.css
├── README.md
└── DOWNLOAD.md
```

### 2. 修改 font-awesome.css

打开 `font-awesome.css`，修改这3行：

**修改前（CDN路径）：**
```css
src: url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/webfonts/fa-solid-900.woff2') format('woff2');
```

**修改后（本地路径）：**
```css
src: url('./fa-solid-900.woff2') format('woff2');
```

### 3. 完整修改示例

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

## 验证是否成功

1. 重新编译项目
2. 在微信开发者工具中查看页面
3. 图标应该正常显示

## 文件大小参考

- fa-solid-900.woff2: ~76KB
- fa-regular-400.woff2: ~13KB  
- fa-brands-400.woff2: ~74KB
- **总计**: ~163KB

## 常见问题

### Q: 能否只下载其中一个文件？
A: 不建议。项目中使用了3种图标类型（solid/regular/brands），缺少任何一个都会导致部分图标无法显示。

### Q: 是否可以使用其他格式（.ttf）？
A: 可以，但 .woff2 格式更小，加载更快，推荐使用。

### Q: 小程序包太大怎么办？
A: 
1. 使用分包加载
2. 或使用 iconfont 只包含使用的图标
3. 或使用 uni-icons（官方图标库）

## 需要帮助？

如果下载有问题，可以：
1. 使用代理或VPN访问
2. 从GitHub镜像下载
3. 或使用 npm 安装后从 node_modules 复制

```bash
npm install @fortawesome/fontawesome-free
# 然后从 node_modules/@fortawesome/fontawesome-free/webfonts/ 复制文件
```

---

**重要**: 完成下载和配置后，才能在微信小程序中正常显示图标！
