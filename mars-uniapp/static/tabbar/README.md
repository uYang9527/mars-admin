# TabBar 图标说明

需要准备以下图标文件（建议尺寸 81x81 px）：

## 图标列表

1. **home.png** - 首页图标（灰色）
2. **home-active.png** - 首页激活图标（#059669）

3. **category.png** - 分类图标（灰色）
4. **category-active.png** - 分类激活图标（#059669）

5. **cart.png** - 购物车图标（灰色）
6. **cart-active.png** - 购物车激活图标（#059669）

7. **profile.png** - 我的图标（灰色）
8. **profile-active.png** - 我的激活图标（#059669）

## 图标设计规范

- 尺寸: 81x81 px (导出 @2x 和 @3x)
- 格式: PNG (透明背景)
- 线条粗细: 2-3px
- 颜色:
  - 未激活: #94a3b8
  - 激活: #059669

## 可使用 iconfont 替代

如果不想准备图片，可以修改 pages.json 使用 iconfont：

```json
"tabBar": {
  "iconfontSrc": "static/font/iconfont.ttf",
  "list": [
    {
      "pagePath": "pages/index/index",
      "iconPath": "\ue001",
      "selectedIconPath": "\ue001",
      "text": "首页"
    }
  ]
}
```
