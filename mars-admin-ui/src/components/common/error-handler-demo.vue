<template>
  <div class="error-handler-demo">
    <div class="demo-section">
      <h3>错误处理演示</h3>
      <div class="button-group">
        <a-button type="primary" @click="handleSaveUser">
          新增用户（会触发403错误）
        </a-button>
        <a-button type="default" @click="handleDeleteUser">
          删除用户（会触发403错误）
        </a-button>
        <a-button type="default" @click="handleUnauthorizedRequest">
          未授权请求（会触发401错误）
        </a-button>
        <a-button type="default" @click="handleNetworkError">
          网络错误演示
        </a-button>
      </div>
    </div>

    <div class="demo-section">
      <h4>错误处理结果：</h4>
      <div class="error-log">
        <div v-for="(log, index) in errorLogs" :key="index" class="log-item">
          <span class="log-time">{{ log.time }}</span>
          <span class="log-type" :class="log.type">{{ log.type }}</span>
          <span class="log-message">{{ log.message }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { request } from '@/service/request';
import { 
  handleError, 
  isProductionReadOnlyError, 
  isUnauthorizedError,
  isHttpError,
  isNetworkError,
  getErrorMessage,
  getErrorCode,
  withErrorHandling
} from '@/utils/error';

interface ErrorLog {
  time: string;
  type: string;
  message: string;
}

const errorLogs = ref<ErrorLog[]>([]);

function addErrorLog(type: string, message: string) {
  errorLogs.value.unshift({
    time: new Date().toLocaleTimeString(),
    type,
    message
  });
  
  // 只保留最近10条记录
  if (errorLogs.value.length > 10) {
    errorLogs.value = errorLogs.value.slice(0, 10);
  }
}

// 方式1：使用try-catch手动处理
async function handleSaveUser() {
  try {
    await request.post('/system/user', {
      username: 'testuser',
      nickname: '测试用户'
    });
    addErrorLog('SUCCESS', '用户创建成功');
  } catch (error) {
    console.log('捕获到错误:', error);
    
    if (isProductionReadOnlyError(error)) {
      addErrorLog('PRODUCTION_READONLY', `生产环境限制: ${getErrorMessage(error)}`);
    } else {
      addErrorLog('ERROR', `创建用户失败: ${getErrorMessage(error)}`);
    }
  }
}

// 方式2：使用错误处理函数
async function handleDeleteUser() {
  try {
    await request.delete('/system/user/1');
    addErrorLog('SUCCESS', '用户删除成功');
  } catch (error) {
    handleError(error, {
      onProductionReadOnly: (err) => {
        addErrorLog('PRODUCTION_READONLY', `演示环境限制: ${err.message}`);
      },
      onDefault: (err) => {
        addErrorLog('ERROR', `删除用户失败: ${getErrorMessage(err)}`);
      }
    });
  }
}

// 方式3：使用withErrorHandling包装器
async function handleUnauthorizedRequest() {
  const result = await withErrorHandling(
    () => request.get('/system/user/unauthorized'),
    {
      onUnauthorized: (error) => {
        addErrorLog('UNAUTHORIZED', `认证失败: ${error.message}`);
      },
      onDefault: (error) => {
        addErrorLog('ERROR', `请求失败: ${getErrorMessage(error)}`);
      }
    }
  );
  
  if (result) {
    addErrorLog('SUCCESS', '请求成功');
  }
}

// 方式4：处理网络错误
async function handleNetworkError() {
  try {
    // 请求一个不存在的接口
    await request.get('/nonexistent/api');
    addErrorLog('SUCCESS', '请求成功');
  } catch (error) {
    console.log('网络错误:', error);
    
    if (isNetworkError(error)) {
      addErrorLog('NETWORK_ERROR', `网络错误: ${getErrorMessage(error)}`);
    } else if (isHttpError(error)) {
      addErrorLog('HTTP_ERROR', `HTTP错误 [${getErrorCode(error)}]: ${getErrorMessage(error)}`);
    } else {
      addErrorLog('ERROR', `请求失败: ${getErrorMessage(error)}`);
    }
  }
}
</script>

<style scoped>
.error-handler-demo {
  padding: 20px;
  max-width: 800px;
}

.demo-section {
  margin-bottom: 30px;
}

.button-group {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.error-log {
  background: #f5f5f5;
  border-radius: 6px;
  padding: 15px;
  max-height: 300px;
  overflow-y: auto;
}

.log-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #e8e8e8;
  font-family: 'Courier New', monospace;
  font-size: 12px;
}

.log-item:last-child {
  border-bottom: none;
}

.log-time {
  color: #666;
  margin-right: 10px;
  min-width: 80px;
}

.log-type {
  padding: 2px 6px;
  border-radius: 3px;
  color: white;
  font-weight: bold;
  margin-right: 10px;
  min-width: 120px;
  text-align: center;
}

.log-type.SUCCESS {
  background: #52c41a;
}

.log-type.PRODUCTION_READONLY {
  background: #fa8c16;
}

.log-type.UNAUTHORIZED {
  background: #f5222d;
}

.log-type.HTTP_ERROR {
  background: #722ed1;
}

.log-type.NETWORK_ERROR {
  background: #eb2f96;
}

.log-type.ERROR {
  background: #ff4d4f;
}

.log-message {
  flex: 1;
  color: #333;
}
</style> 