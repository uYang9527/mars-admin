# Mars Admin 模拟数据使用说明

## 概述

Mars Admin 项目当前完全使用模拟数据，专注于前端展示和交互效果。所有API调用都会返回预设的模拟数据，不会发起真实的网络请求。

## 配置说明

### API配置 (api/config.js)

```javascript
export const API_CONFIG = {
  mock: true,           // 强制使用mock数据
  timeout: 10000,       // 模拟请求超时时间
  delay: {              // 模拟网络延迟
    min: 300,           // 最小延迟(ms)
    max: 1000           // 最大延迟(ms)
  }
}
```

## 模拟数据特性

### 1. 真实的网络延迟
- 所有API调用都有随机延迟（300-1000ms）
- 模拟真实的网络请求体验
- 可以测试加载状态和用户体验

### 2. 完整的响应格式
```javascript
{
  code: 200,           // 状态码
  message: '操作成功', // 响应消息
  data: {...},         // 响应数据
  timestamp: 1234567890 // 时间戳
}
```

### 3. 支持的功能
- ✅ 分页查询
- ✅ 搜索过滤
- ✅ 数据排序
- ✅ 增删改查
- ✅ 文件上传
- ✅ 批量操作

## 使用示例

### 基础API调用
```javascript
import api from '@/api'

// 用户登录
const loginRes = await api.user.login({
  username: 'admin',
  password: '123456'
})

// 获取首页数据
const homeData = await api.home.getHomeData()

// 获取消息列表
const messageList = await api.message.getMessageList({
  page: 1,
  size: 10,
  type: 'system'
})
```

### 交互效果
```javascript
import { simulateLoading, simulateSuccess, simulateConfirm } from '@/utils/interaction'

// 模拟加载
await simulateLoading('处理中...', 2000)

// 模拟成功提示
simulateSuccess('操作成功', () => {
  // 成功后的回调
})

// 模拟确认对话框
const confirmed = await simulateConfirm('确定要删除吗？')
if (confirmed) {
  // 用户确认后的操作
}
```

## 模拟数据管理

### 数据文件位置
```
mock/
├── index.js          # 统一导出
├── user.js           # 用户数据
├── home.js           # 首页数据
├── message.js        # 消息数据
└── organization.js   # 组织架构数据
```

### 修改模拟数据
1. 直接编辑 `mock/` 目录下的数据文件
2. 数据会立即生效，无需重启
3. 支持动态数据（如时间戳、随机数等）

### 添加新的模拟接口
```javascript
// 在对应的API模块中添加
export const newApi = (params) => {
  return mockRequest(() => {
    // 处理参数
    const { page, size } = params
    
    // 返回模拟数据
    return createSuccessResponse({
      list: mockData,
      total: mockData.length
    }, '获取成功')
  }, 500) // 延迟500ms
}
```

## 页面交互效果

### 1. 加载状态
- 所有页面都有统一的加载状态
- 支持下拉刷新和上拉加载更多
- 加载失败时有错误提示

### 2. 空状态展示
- 使用 `ma-empty` 组件展示空状态
- 支持自定义图标和描述
- 可添加操作按钮

### 3. 用户反馈
- 操作成功/失败提示
- 确认对话框
- 加载进度提示

## 测试场景

### 登录测试
```
用户名: admin
密码: 123456
```

### 数据展示测试
- 首页：轮播图、快捷菜单、公告、工作动态
- 消息：系统消息、工作消息、审批消息、通知公告
- 组织架构：部门列表、成员信息
- 个人中心：用户信息、统计数据、功能菜单

### 交互测试
- 搜索功能（支持关键词搜索）
- 分页加载（支持上拉加载更多）
- 下拉刷新（模拟数据更新）
- 删除操作（带确认对话框）
- 文件上传（模拟上传进度）

## 切换到真实API

当需要对接真实后端时，只需要：

1. 修改 `api/config.js` 中的配置：
```javascript
export const API_CONFIG = {
  mock: false,          // 关闭mock模式
  baseURL: 'https://your-api.com',
  timeout: 15000
}
```

2. 在API模块中添加真实的请求逻辑：
```javascript
export const login = (data) => {
  // mock模式下的代码保持不变
  if (API_CONFIG.mock) {
    return mockRequest(...)
  }
  
  // 添加真实API调用
  return request.post('/api/user/login', data)
}
```

## 注意事项

1. **数据持久化**: 模拟数据在页面刷新后会重置
2. **网络状态**: 不会受到真实网络状况影响
3. **错误模拟**: 可以通过修改mock函数来模拟各种错误情况
4. **性能测试**: 模拟数据不能反映真实的网络性能

## 开发建议

1. **充分测试交互**: 利用模拟数据测试各种用户交互场景
2. **优化用户体验**: 关注加载状态、错误处理、空状态展示
3. **准备真实对接**: 保持API接口设计的一致性，便于后续对接
4. **文档维护**: 及时更新模拟数据和接口文档

通过这套模拟数据系统，可以在不依赖后端的情况下，完整地开发和测试前端功能，确保用户体验的完整性和一致性。
