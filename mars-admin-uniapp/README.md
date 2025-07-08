# 企业小程序 - Mars Admin UniApp

## 📱 项目简介

这是一个基于 UniApp 框架开发的企业级小程序，包含首页、组织、消息、我的四个核心模块，为企业员工提供便捷的移动办公体验。

## ✨ 功能特色

### 🏠 首页模块
- 📊 企业数据统计展示
- 🎠 轮播图展示企业文化
- ⚡ 快捷功能入口（考勤、请假、报销等）
- 📢 公司公告和通知
- 💼 工作动态实时更新

### 👥 组织模块
- 🏢 树形组织架构展示
- 👤 员工信息查看
- 🔍 部门和员工搜索
- 📞 一键拨打电话
- 💬 快速发起聊天

### 📬 消息模块
- 📋 多类型消息分类（系统、工作、审批、通知）
- 🔴 未读消息提醒
- 🏷️ 消息优先级标签
- ✅ 批量操作（全部已读、清空消息）
- 📄 消息详情查看

### 👤 我的模块
- 🎨 个性化用户信息展示
- 📈 个人数据统计
- 📱 功能菜单分组
- ⚙️ 设置选项
- 🚪 安全退出登录

## 🛠️ 技术栈

- **框架**: UniApp
- **语言**: JavaScript/Vue.js
- **样式**: CSS3 + 响应式设计
- **图标**: Unicode Emoji
- **状态管理**: 本地存储
- **网络请求**: 封装的 HTTP 工具

## 📦 项目结构

```
mars-admin-uniapp/
├── pages/                    # 页面文件
│   ├── index/               # 首页
│   ├── organization/        # 组织架构
│   ├── message/            # 消息中心
│   └── profile/            # 个人中心
├── static/                  # 静态资源
│   ├── css/                # 样式文件
│   └── images/             # 图片资源
├── utils/                   # 工具函数
│   ├── request.js          # 网络请求
│   └── common.js           # 通用工具
├── App.vue                  # 应用入口
├── pages.json              # 页面配置
├── manifest.json           # 应用配置
└── README.md               # 项目说明
```

## 🚀 快速开始

### 环境要求

- **HBuilderX**: 3.0+
- **Node.js**: 14.0+
- **微信开发者工具**: 最新版本

### 安装步骤

1. **克隆项目**
```bash
git clone [项目地址]
cd mars-admin-uniapp
```

2. **使用 HBuilderX 打开项目**
   - 打开 HBuilderX
   - 文件 -> 打开目录 -> 选择项目文件夹

3. **配置后端接口**
```javascript
// utils/request.js
const config = {
  baseURL: 'http://your-api-domain.com', // 修改为你的后端API地址
  // ...
}
```

4. **运行项目**
   - 点击 HBuilderX 顶部菜单 "运行" -> "运行到小程序模拟器" -> "微信开发者工具"

## 📱 支持平台

- ✅ 微信小程序
- ✅ 支付宝小程序
- ✅ 百度小程序
- ✅ 字节跳动小程序
- ✅ H5
- ✅ App (Android/iOS)

## 🎨 界面设计

### 设计原则
- **简洁明了**: 界面简洁，操作直观
- **统一规范**: 遵循统一的设计规范
- **响应式**: 适配不同尺寸屏幕
- **用户友好**: 注重用户体验

### 色彩方案
- **主色调**: #007AFF (蓝色)
- **成功色**: #28a745 (绿色)
- **警告色**: #ffc107 (黄色)
- **危险色**: #dc3545 (红色)
- **文本色**: #333333 (深灰)
- **辅助色**: #666666 (中灰)
- **提示色**: #999999 (浅灰)

## 🔧 功能配置

### 底部导航配置
```json
// pages.json
"tabBar": {
  "color": "#999999",
  "selectedColor": "#007AFF",
  "backgroundColor": "#ffffff",
  "list": [
    // 导航项配置
  ]
}
```

### 网络请求配置
```javascript
// utils/request.js
const config = {
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  header: {
    'Content-Type': 'application/json'
  }
}
```

## 📊 数据模拟

项目中使用静态数据模拟，便于开发和演示：

- **用户信息**: 模拟员工基本信息
- **组织架构**: 模拟公司部门结构
- **消息数据**: 模拟各类型消息
- **统计数据**: 模拟业务统计信息

## 🔐 安全特性

- **Token 认证**: 支持 JWT Token 认证
- **请求拦截**: 自动添加认证头
- **错误处理**: 统一的错误处理机制
- **权限控制**: 基于角色的权限管理

## 📈 性能优化

- **图片懒加载**: 优化图片加载性能
- **请求缓存**: 减少重复网络请求
- **代码分割**: 按需加载页面组件
- **资源压缩**: 压缩图片和代码

## 🐛 常见问题

### Q: 如何修改底部导航图标？
A: 将图标文件放置在 `static/images/` 目录下，然后在 `pages.json` 中修改对应的 `iconPath` 和 `selectedIconPath`。

### Q: 如何连接真实的后端接口？
A: 修改 `utils/request.js` 中的 `baseURL` 为你的后端API地址，并确保接口返回格式符合约定。

### Q: 如何添加新的页面？
A: 1. 在 `pages/` 目录下创建新的页面文件夹和 `.vue` 文件
   2. 在 `pages.json` 中添加页面配置
   3. 如需要底部导航，在 `tabBar.list` 中添加配置

### Q: 如何自定义主题色？
A: 修改 `static/css/common.css` 中的颜色变量，或者直接修改各个组件的样式。

## 🤝 贡献指南

1. Fork 本项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系我们

- **邮箱**: support@mars-admin.com
- **官网**: https://mars-admin.com
- **文档**: https://docs.mars-admin.com

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者！

---

**Mars Admin UniApp** - 让企业移动办公更简单！ 🚀 