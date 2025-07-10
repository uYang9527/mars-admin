import type { AxiosResponse } from 'axios';
import { BACKEND_ERROR_CODE, createFlatRequest, createRequest } from '@sa/axios';
import { useAuthStore } from '@/store/modules/auth';
import { localStg } from '@/utils/storage';
import { getServiceBaseURL } from '@/utils/service';
import { $t } from '@/locales';
import { getAuthorization, handleExpiredRequest, showErrorMsg } from './shared';
import type { RequestInstanceState } from './type';

const isHttpProxy = import.meta.env.DEV && import.meta.env.VITE_HTTP_PROXY === 'Y';
const { baseURL, otherBaseURL } = getServiceBaseURL(import.meta.env, isHttpProxy);

// 调试信息
console.log('🔧 代理配置调试信息:');
console.log('- DEV模式:', import.meta.env.DEV);
console.log('- VITE_HTTP_PROXY:', import.meta.env.VITE_HTTP_PROXY);
console.log('- VITE_SERVICE_BASE_URL:', import.meta.env.VITE_SERVICE_BASE_URL);
console.log('- isHttpProxy:', isHttpProxy);
console.log('- 最终baseURL:', baseURL);
console.log('- 环境变量全部:', import.meta.env);

export const request = createFlatRequest<App.Service.Response, RequestInstanceState>(
  {
    baseURL,
    headers: {
      apifoxToken: 'XL299LiMEDZ0H5h3A29PxwQXdMJqWyY2'
    }
  },
  {
    async onRequest(config) {
      const Authorization = getAuthorization();
      Object.assign(config.headers, { Authorization });

      return config;
    },
    isBackendSuccess(response) {
      // when the backend response code is "0000"(default), it means the request is success
      // to change this logic by yourself, you can modify the `VITE_SERVICE_SUCCESS_CODE` in `.env` file
      return String(response.data.code) === import.meta.env.VITE_SERVICE_SUCCESS_CODE;
    },
    async onBackendFail(response, instance) {
      const authStore = useAuthStore();
      const responseCode = String(response.data.code);

      function handleLogout() {
        authStore.resetStore();
      }

      function logoutAndCleanup() {
        handleLogout();
        window.removeEventListener('beforeunload', handleLogout);

        request.state.errMsgStack = request.state.errMsgStack.filter(msg => msg !== response.data.msg);
      }

      // 处理403错误 - 演示环境限制
      if (responseCode === '403') {
        const errorMessage = response.data.message || '演示环境，不允许进行增删改操作';
        console.warn('🚫 操作被拦截:', errorMessage);
        
        // 显示错误提示
        showErrorMsg(request.state, errorMessage);
        
        // 抛出异常，让调用方可以捕获处理
        const error = new Error(errorMessage);
        error.name = 'ProductionReadOnlyError';
        (error as any).code = 403;
        (error as any).response = response;
        throw error;
      }

      // 处理401错误 - 未授权
      if (responseCode === '401') {
        const errorMessage = response.data.message || '未授权访问，请重新登录';
        console.warn('🔐 认证失败:', errorMessage);
        
        // 显示错误提示
        showErrorMsg(request.state, errorMessage);
        
        // 抛出异常
        const error = new Error(errorMessage);
        error.name = 'UnauthorizedError';
        (error as any).code = 401;
        (error as any).response = response;
        throw error;
      }

      // when the backend response code is in `logoutCodes`, it means the user will be logged out and redirected to login page
      const logoutCodes = import.meta.env.VITE_SERVICE_LOGOUT_CODES?.split(',') || [];
      if (logoutCodes.includes(responseCode)) {
        handleLogout();
        return null;
      }

      // when the backend response code is in `modalLogoutCodes`, it means the user will be logged out by displaying a modal
      const modalLogoutCodes = import.meta.env.VITE_SERVICE_MODAL_LOGOUT_CODES?.split(',') || [];
      if (modalLogoutCodes.includes(responseCode) && !request.state.errMsgStack?.includes(responseCode)) {
        request.state.errMsgStack = [...(request.state.errMsgStack || []), response.data.msg];

        // prevent the user from refreshing the page
        window.addEventListener('beforeunload', handleLogout);

        window.$modal?.error({
          title: $t('common.error'),
          content: response.data.msg,
          okText: $t('common.confirm'),
          maskClosable: false,
          onOk() {
            logoutAndCleanup();
          },
          onCancel() {
            logoutAndCleanup();
          }
        });

        return null;
      }

      // when the backend response code is in `expiredTokenCodes`, it means the token is expired, and refresh token
      // the api `refreshToken` can not return error code in `expiredTokenCodes`, otherwise it will be a dead loop, should return `logoutCodes` or `modalLogoutCodes`
      const expiredTokenCodes = import.meta.env.VITE_SERVICE_EXPIRED_TOKEN_CODES?.split(',') || [];
      if (expiredTokenCodes.includes(responseCode)) {
        const success = await handleExpiredRequest(request.state);
        if (success) {
          const Authorization = getAuthorization();
          Object.assign(response.config.headers, { Authorization });

          return instance.request(response.config) as Promise<AxiosResponse>;
        }
      }

      // 处理其他错误状态码，统一抛出异常
      const errorMessage = response.data.message || response.data.msg || `请求失败，错误码：${responseCode}`;
      console.warn(`❌ 后端业务错误 [${responseCode}]:`, errorMessage);
      
      // 显示错误提示
      showErrorMsg(request.state, errorMessage);
      
      // 抛出异常
      const error = new Error(errorMessage);
      error.name = 'BackendBusinessError';
      (error as any).code = parseInt(responseCode) || 500;
      (error as any).response = response;
      throw error;
    },
    transformBackendResponse(response) {
      return response.data.data;
    },
    onError(error) {
      // when the request is fail, you can show error message

      let message = error.message;
      let backendErrorCode = '';

      // get backend error message and code
      if (error.code === BACKEND_ERROR_CODE) {
        message = error.response?.data?.message || error.response?.data?.msg || message;
        backendErrorCode = String(error.response?.data?.code) || '';
      }

      // 处理HTTP状态码错误
      if (error.response?.status) {
        const httpStatus = error.response.status;
        console.warn(`🌐 HTTP错误 [${httpStatus}]:`, message);
        
        // 根据HTTP状态码设置不同的错误信息
        switch (httpStatus) {
          case 400:
            message = '请求参数错误';
            break;
          case 401:
            message = '未授权访问，请重新登录';
            break;
          case 403:
            message = error.response?.data?.message || '演示环境，不允许进行增删改操作';
            break;
          case 404:
            message = '请求的资源不存在';
            break;
          case 405:
            message = '请求方法不被允许';
            break;
          case 408:
            message = '请求超时';
            break;
          case 500:
            message = '服务器内部错误';
            break;
          case 502:
            message = '网关错误';
            break;
          case 503:
            message = '服务不可用';
            break;
          case 504:
            message = '网关超时';
            break;
          default:
            message = `网络错误 (${httpStatus})`;
        }
        
        // 抛出HTTP错误异常
        const httpError = new Error(message);
        httpError.name = 'HttpError';
        (httpError as any).code = httpStatus;
        (httpError as any).response = error.response;
        
        // 显示错误提示
        showErrorMsg(request.state, message);
        
        // 重新抛出异常
        throw httpError;
      }

      // the error message is displayed in the modal
      const modalLogoutCodes = import.meta.env.VITE_SERVICE_MODAL_LOGOUT_CODES?.split(',') || [];
      if (modalLogoutCodes.includes(backendErrorCode)) {
        return;
      }

      // when the token is expired, refresh token and retry request, so no need to show error message
      const expiredTokenCodes = import.meta.env.VITE_SERVICE_EXPIRED_TOKEN_CODES?.split(',') || [];
      if (expiredTokenCodes.includes(backendErrorCode)) {
        return;
      }

      // 显示通用错误信息
      showErrorMsg(request.state, message);
      
      // 抛出通用网络错误
      const networkError = new Error(message);
      networkError.name = 'NetworkError';
      (networkError as any).originalError = error;
      throw networkError;
    }
  }
);

export const demoRequest = createRequest<App.Service.DemoResponse>(
  {
    baseURL: otherBaseURL.demo
  },
  {
    async onRequest(config) {
      const { headers } = config;

      // set token
      const token = localStg.get('token');
      const Authorization = token ? `${token}` : null;
      Object.assign(headers, { Authorization });

      return config;
    },
    isBackendSuccess(response) {
      // when the backend response code is "200", it means the request is success
      // you can change this logic by yourself
      return response.data.status === '200';
    },
    async onBackendFail(_response) {
      // when the backend response code is not "200", it means the request is fail
      // for example: the token is expired, refresh token and retry request
    },
    transformBackendResponse(response) {
      return response.data.result;
    },
    onError(error) {
      // when the request is fail, you can show error message

      let message = error.message;

      // show backend error message
      if (error.code === BACKEND_ERROR_CODE) {
        message = error.response?.data?.message || message;
      }

      window.$message?.error(message);
    }
  }
);
