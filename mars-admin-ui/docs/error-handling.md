# 前端错误处理指南

Mars Admin前端提供了完善的错误处理机制，可以统一处理各种类型的错误，包括403演示环境限制、401未授权、HTTP错误、网络错误等。

## 🎯 功能特性

- 🚫 **统一拦截**：在响应拦截器中统一处理各种错误状态码
- 🔍 **错误分类**：将错误分为不同类型，便于针对性处理
- 🛠️ **工具函数**：提供丰富的错误处理工具函数
- 📝 **错误日志**：自动记录错误信息，便于调试
- ⚡ **异常抛出**：将错误信息抛出，让业务代码可以捕获处理

## 📋 错误类型

### 1. 生产环境只读错误 (403)
当在生产环境下尝试进行增删改操作时触发：

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

### 2. 未授权错误 (401)
当用户未登录或token过期时触发：

```json
{
  "code": 401,
  "message": "未授权访问，请重新登录",
  "data": null,
  "success": false
}
```

### 3. HTTP状态码错误
包括400、404、500等各种HTTP状态码错误。

### 4. 网络错误
包括网络连接失败、超时等错误。

## 🔧 使用方法

### 1. 基础用法 - try-catch

```typescript
import { request } from '@/service/request';
import { isProductionReadOnlyError, getErrorMessage } from '@/utils/error';

async function saveUser() {
  try {
    await request.post('/system/user', userData);
    // 成功处理
  } catch (error) {
    if (isProductionReadOnlyError(error)) {
      // 处理生产环境限制错误
      console.log('演示环境限制:', getErrorMessage(error));
    } else {
      // 处理其他错误
      console.error('保存失败:', getErrorMessage(error));
    }
  }
}
```

### 2. 使用错误处理函数

```typescript
import { handleError } from '@/utils/error';

async function deleteUser(id: number) {
  try {
    await request.delete(`/system/user/${id}`);
  } catch (error) {
    handleError(error, {
      onProductionReadOnly: (err) => {
        // 处理生产环境限制
        window.$message?.warning(err.message);
      },
      onUnauthorized: (err) => {
        // 处理未授权错误
        window.$message?.error('请重新登录');
      },
      onDefault: (err) => {
        // 处理其他错误
        window.$message?.error(`删除失败: ${err.message}`);
      }
    });
  }
}
```

### 3. 使用错误包装器

```typescript
import { withErrorHandling } from '@/utils/error';

async function updateUser(userData: any) {
  const result = await withErrorHandling(
    () => request.put('/system/user', userData),
    {
      onProductionReadOnly: (error) => {
        window.$notification?.warning({
          message: '操作限制',
          description: error.message
        });
      }
    }
  );
  
  if (result) {
    // 成功处理
    window.$message?.success('更新成功');
  }
}
```

## 🛠️ 工具函数

### 错误类型判断

```typescript
import {
  isProductionReadOnlyError,
  isUnauthorizedError,
  isBackendBusinessError,
  isHttpError,
  isNetworkError,
  isErrorCode
} from '@/utils/error';

// 判断是否为特定类型的错误
if (isProductionReadOnlyError(error)) {
  // 处理生产环境只读错误
}

if (isUnauthorizedError(error)) {
  // 处理未授权错误
}

if (isErrorCode(error, 403)) {
  // 判断是否为特定错误码
}
```

### 错误信息获取

```typescript
import { getErrorMessage, getErrorCode } from '@/utils/error';

// 获取错误信息
const message = getErrorMessage(error);

// 获取错误码
const code = getErrorCode(error);
```

## 🎨 在Vue组件中使用

### 1. 组合式API

```vue
<template>
  <div>
    <a-button @click="handleSave" :loading="loading">保存</a-button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { request } from '@/service/request';
import { handleError, isProductionReadOnlyError } from '@/utils/error';

const loading = ref(false);

async function handleSave() {
  loading.value = true;
  
  try {
    await request.post('/system/user', {
      username: 'test',
      nickname: '测试用户'
    });
    
    window.$message?.success('保存成功');
  } catch (error) {
    handleError(error, {
      onProductionReadOnly: (err) => {
        // 生产环境限制的特殊处理
        window.$modal?.info({
          title: '演示环境提示',
          content: err.message
        });
      },
      onDefault: (err) => {
        window.$message?.error(`保存失败: ${err.message}`);
      }
    });
  } finally {
    loading.value = false;
  }
}
</script>
```

### 2. 选项式API

```vue
<script>
import { request } from '@/service/request';
import { handleError } from '@/utils/error';

export default {
  methods: {
    async saveData() {
      try {
        await request.post('/api/save', this.formData);
        this.$message.success('保存成功');
      } catch (error) {
        handleError(error, {
          onProductionReadOnly: (err) => {
            this.$notification.warning({
              message: '演示环境',
              description: err.message
            });
          }
        });
      }
    }
  }
}
</script>
```

## 🔧 自定义错误处理

### 1. 扩展错误类型

```typescript
// 在 utils/error.ts 中添加新的错误类型
export enum ErrorType {
  // 现有类型...
  CUSTOM_BUSINESS = 'CustomBusinessError'
}

export function isCustomBusinessError(error: any): error is ExtendedError {
  return error?.name === ErrorType.CUSTOM_BUSINESS;
}
```

### 2. 自定义拦截器处理

```typescript
// 在 service/request/index.ts 中添加自定义处理逻辑
async onBackendFail(response, instance) {
  const responseCode = String(response.data.code);
  
  // 自定义业务错误处理
  if (responseCode === '1001') {
    const errorMessage = response.data.message || '自定义业务错误';
    const error = new Error(errorMessage);
    error.name = 'CustomBusinessError';
    (error as any).code = 1001;
    throw error;
  }
  
  // 其他处理...
}
```

## 📝 最佳实践

### 1. 统一错误提示

```typescript
// 创建统一的错误提示函数
function showErrorNotification(error: any) {
  if (isProductionReadOnlyError(error)) {
    window.$notification?.warning({
      message: '演示环境限制',
      description: error.message,
      duration: 5
    });
  } else if (isUnauthorizedError(error)) {
    window.$notification?.error({
      message: '认证失败',
      description: '请重新登录',
      duration: 3
    });
  } else {
    window.$message?.error(getErrorMessage(error));
  }
}

// 在业务代码中使用
try {
  await someApiCall();
} catch (error) {
  showErrorNotification(error);
}
```

### 2. 错误日志记录

```typescript
// 记录错误到日志系统
function logError(error: any, context?: string) {
  console.error(`[${context || 'Unknown'}] 错误:`, {
    message: getErrorMessage(error),
    code: getErrorCode(error),
    stack: error.stack,
    timestamp: new Date().toISOString()
  });
  
  // 可以发送到远程日志系统
  // sendToLogSystem(error, context);
}
```

### 3. 全局错误边界

```vue
<!-- ErrorBoundary.vue -->
<template>
  <div v-if="hasError" class="error-boundary">
    <h3>出现了错误</h3>
    <p>{{ errorMessage }}</p>
    <a-button @click="retry">重试</a-button>
  </div>
  <slot v-else />
</template>

<script setup lang="ts">
import { ref, onErrorCaptured } from 'vue';
import { getErrorMessage } from '@/utils/error';

const hasError = ref(false);
const errorMessage = ref('');

onErrorCaptured((error) => {
  hasError.value = true;
  errorMessage.value = getErrorMessage(error);
  return false; // 阻止错误继续传播
});

function retry() {
  hasError.value = false;
  errorMessage.value = '';
}
</script>
```

## 🐛 调试技巧

### 1. 启用详细日志

在开发环境中，所有错误都会在控制台中详细记录：

```typescript
// 开发环境下查看完整错误信息
if (import.meta.env.DEV) {
  console.group('🔥 错误详情');
  console.error('错误对象:', error);
  console.log('错误类型:', error?.name);
  console.log('错误码:', getErrorCode(error));
  console.log('错误信息:', getErrorMessage(error));
  console.log('响应数据:', error?.response?.data);
  console.groupEnd();
}
```

### 2. 网络请求监控

在浏览器开发者工具的Network选项卡中可以查看：
- 请求URL和方法
- 请求头和响应头
- 响应状态码和数据
- 请求耗时

### 3. 错误重现

使用演示组件测试各种错误场景：

```vue
<!-- 在开发环境中使用 -->
<ErrorHandlerDemo v-if="isDev" />
```

## 🔄 更新日志

- **v1.0.0** (2025-01-08)
  - 初始版本发布
  - 支持403生产环境限制错误处理
  - 支持401未授权错误处理
  - 支持HTTP状态码错误处理
  - 支持网络错误处理
  - 提供完整的工具函数库 