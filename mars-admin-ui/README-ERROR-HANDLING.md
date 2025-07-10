# 前端错误处理功能 - 快速说明

## 🎯 功能概述

为Mars Admin前端项目添加了完善的错误处理机制，可以统一拦截和处理各种类型的错误，特别是针对后端返回的403、401等状态码错误。

## ✅ 已完成的功能

### 1. **响应拦截器增强** (`src/service/request/index.ts`)
- ✅ 增强了403错误处理（生产环境操作限制）
- ✅ 增强了401错误处理（未授权访问）
- ✅ 添加了HTTP状态码错误处理
- ✅ 统一异常抛出机制，让业务代码可以捕获处理

### 2. **错误处理工具库** (`src/utils/error.ts`)
- ✅ 错误类型枚举和判断函数
- ✅ 错误信息获取工具函数
- ✅ 统一错误处理函数
- ✅ 异步函数错误包装器
- ✅ 全局错误处理设置

### 3. **全局错误处理** (`src/main.ts`)
- ✅ 设置了全局未捕获异常处理
- ✅ 设置了Vue组件错误处理

### 4. **演示组件** (`src/components/common/error-handler-demo.vue`)
- ✅ 创建了完整的错误处理演示组件
- ✅ 展示了各种错误处理方式的使用方法

### 5. **文档** (`docs/error-handling.md`)
- ✅ 详细的使用指南和最佳实践
- ✅ 完整的API文档和示例代码

## 🚀 核心特性

### 1. **错误类型分类**
```typescript
enum ErrorType {
  PRODUCTION_READONLY = 'ProductionReadOnlyError',  // 生产环境只读
  UNAUTHORIZED = 'UnauthorizedError',               // 未授权
  BACKEND_BUSINESS = 'BackendBusinessError',        // 后端业务错误
  HTTP_ERROR = 'HttpError',                         // HTTP错误
  NETWORK_ERROR = 'NetworkError'                    // 网络错误
}
```

### 2. **异常自动抛出**
响应拦截器现在会将错误信息抛出，业务代码可以通过try-catch捕获：

```typescript
try {
  await request.post('/system/user', userData);
} catch (error) {
  if (isProductionReadOnlyError(error)) {
    // 处理生产环境限制错误
    console.log('演示环境限制:', error.message);
  }
}
```

### 3. **错误信息处理**
对于你提到的403错误响应：
```json
{
  "code": 403,
  "message": "演示环境，不允许进行增删改操作，如需测试请联系管理员",
  "data": null,
  "timestamp": 1752048917903,
  "error": true,
  "success": false
}
```

现在会：
1. 在控制台记录详细错误信息
2. 显示用户友好的错误提示
3. 抛出类型化的异常供业务代码处理

## 📋 使用示例

### 基础用法
```typescript
import { request } from '@/service/request';
import { isProductionReadOnlyError, getErrorMessage } from '@/utils/error';

async function saveUser() {
  try {
    await request.post('/system/user', userData);
    window.$message?.success('保存成功');
  } catch (error) {
    if (isProductionReadOnlyError(error)) {
      // 特殊处理生产环境限制
      window.$modal?.info({
        title: '演示环境提示',
        content: error.message
      });
    } else {
      // 处理其他错误
      window.$message?.error(`保存失败: ${getErrorMessage(error)}`);
    }
  }
}
```

### 使用错误处理函数
```typescript
import { handleError } from '@/utils/error';

try {
  await request.delete('/system/user/1');
} catch (error) {
  handleError(error, {
    onProductionReadOnly: (err) => {
      window.$notification?.warning({
        message: '演示环境限制',
        description: err.message
      });
    },
    onDefault: (err) => {
      window.$message?.error(`操作失败: ${err.message}`);
    }
  });
}
```

## 🔧 配置说明

### 环境变量配置
错误处理会根据以下环境变量进行行为调整：
- `VITE_SERVICE_SUCCESS_CODE`: 成功状态码（默认200）
- `VITE_SERVICE_LOGOUT_CODES`: 需要退出登录的状态码
- `VITE_SERVICE_MODAL_LOGOUT_CODES`: 需要弹窗提示的状态码
- `VITE_SERVICE_EXPIRED_TOKEN_CODES`: Token过期状态码

### 开发环境调试
在开发环境下，所有错误都会在控制台详细记录：
```
🚫 操作被拦截: 演示环境，不允许进行增删改操作，如需测试请联系管理员
🔐 认证失败: 未授权访问，请重新登录
❌ 后端业务错误 [500]: 服务器内部错误
🌐 HTTP错误 [404]: 请求的资源不存在
```

## 📁 文件结构
```
mars-admin-ui/src/
├── service/request/
│   ├── index.ts                 # 增强的响应拦截器
│   └── shared.ts               # 共享工具函数
├── utils/
│   └── error.ts                # 错误处理工具库
├── components/common/
│   └── error-handler-demo.vue  # 错误处理演示组件
├── main.ts                     # 全局错误处理设置
└── docs/
    └── error-handling.md       # 详细使用文档
```

## 🎉 总结

现在前端项目已经具备了完善的错误处理能力：

1. **自动拦截**：响应拦截器自动处理各种错误状态码
2. **异常抛出**：将错误信息抛出供业务代码捕获处理
3. **类型安全**：提供TypeScript类型支持和类型守卫函数
4. **工具丰富**：提供多种错误处理工具函数
5. **文档完善**：详细的使用指南和最佳实践

特别是对于你提到的403错误，现在会：
- 在控制台显示 `🚫 操作被拦截: 演示环境，不允许进行增删改操作`
- 显示用户友好的错误提示
- 抛出 `ProductionReadOnlyError` 异常供业务代码处理

这样前端就能很好地配合后端的生产环境操作限制功能了！ 