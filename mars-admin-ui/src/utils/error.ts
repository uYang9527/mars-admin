/** 错误处理工具函数 */

/** 错误类型枚举 */
export enum ErrorType {
  /** 生产环境只读错误 */
  PRODUCTION_READONLY = 'ProductionReadOnlyError',
  /** 未授权错误 */
  UNAUTHORIZED = 'UnauthorizedError',
  /** 后端业务错误 */
  BACKEND_BUSINESS = 'BackendBusinessError',
  /** HTTP错误 */
  HTTP_ERROR = 'HttpError',
  /** 网络错误 */
  NETWORK_ERROR = 'NetworkError'
}

/** 扩展的错误接口 */
export interface ExtendedError extends Error {
  name: string;
  code?: number;
  response?: any;
  originalError?: Error;
}

/** 判断是否为生产环境只读错误 */
export function isProductionReadOnlyError(error: any): error is ExtendedError {
  return error?.name === ErrorType.PRODUCTION_READONLY;
}

/** 判断是否为未授权错误 */
export function isUnauthorizedError(error: any): error is ExtendedError {
  return error?.name === ErrorType.UNAUTHORIZED;
}

/** 判断是否为后端业务错误 */
export function isBackendBusinessError(error: any): error is ExtendedError {
  return error?.name === ErrorType.BACKEND_BUSINESS;
}

/** 判断是否为HTTP错误 */
export function isHttpError(error: any): error is ExtendedError {
  return error?.name === ErrorType.HTTP_ERROR;
}

/** 判断是否为网络错误 */
export function isNetworkError(error: any): error is ExtendedError {
  return error?.name === ErrorType.NETWORK_ERROR;
}

/** 判断是否为特定错误码 */
export function isErrorCode(error: any, code: number): boolean {
  return error?.code === code;
}

/** 获取错误信息 */
export function getErrorMessage(error: any): string {
  if (error?.message) {
    return error.message;
  }
  if (error?.response?.data?.message) {
    return error.response.data.message;
  }
  if (error?.response?.data?.msg) {
    return error.response.data.msg;
  }
  return '未知错误';
}

/** 获取错误码 */
export function getErrorCode(error: any): number | undefined {
  return error?.code || error?.response?.status || error?.response?.data?.code;
}

/** 统一错误处理函数 */
export function handleError(
  error: any,
  customHandlers?: {
    onProductionReadOnly?: (error: ExtendedError) => void;
    onUnauthorized?: (error: ExtendedError) => void;
    onBackendBusiness?: (error: ExtendedError) => void;
    onHttpError?: (error: ExtendedError) => void;
    onNetworkError?: (error: ExtendedError) => void;
    onDefault?: (error: any) => void;
  }
) {
  console.error('🔥 错误处理:', error);

  if (isProductionReadOnlyError(error)) {
    console.warn('🚫 生产环境只读错误:', error.message);
    customHandlers?.onProductionReadOnly?.(error);
    return;
  }

  if (isUnauthorizedError(error)) {
    console.warn('🔐 未授权错误:', error.message);
    customHandlers?.onUnauthorized?.(error);
    return;
  }

  if (isBackendBusinessError(error)) {
    console.warn('❌ 后端业务错误:', error.message);
    customHandlers?.onBackendBusiness?.(error);
    return;
  }

  if (isHttpError(error)) {
    console.warn('🌐 HTTP错误:', error.message);
    customHandlers?.onHttpError?.(error);
    return;
  }

  if (isNetworkError(error)) {
    console.warn('📡 网络错误:', error.message);
    customHandlers?.onNetworkError?.(error);
    return;
  }

  // 默认处理
  console.error('❓ 未知错误:', error);
  customHandlers?.onDefault?.(error);
}

/** 创建错误处理装饰器（用于Vue组件方法） */
export function withErrorHandler(customHandlers?: Parameters<typeof handleError>[1]) {
  return function (target: any, propertyKey: string, descriptor: PropertyDescriptor) {
    const originalMethod = descriptor.value;

    descriptor.value = async function (...args: any[]) {
      try {
        return await originalMethod.apply(this, args);
      } catch (error) {
        handleError(error, customHandlers);
        throw error; // 重新抛出错误，让调用方决定是否需要进一步处理
      }
    };

    return descriptor;
  };
}

/** 异步函数错误包装器 */
export async function withErrorHandling<T>(
  asyncFn: () => Promise<T>,
  customHandlers?: Parameters<typeof handleError>[1]
): Promise<T | null> {
  try {
    return await asyncFn();
  } catch (error) {
    handleError(error, customHandlers);
    return null;
  }
}

/** 错误边界组件的错误处理 */
export function handleComponentError(error: any, instance: any, info: string) {
  console.error('🔥 Vue组件错误:', {
    error,
    instance,
    info
  });

  // 发送错误到监控系统（如果有的话）
  // sendErrorToMonitoring(error, instance, info);
}

/** 全局错误处理 */
export function setupGlobalErrorHandler() {
  // 处理未捕获的Promise拒绝
  window.addEventListener('unhandledrejection', event => {
    console.error('🔥 未捕获的Promise拒绝:', event.reason);
    handleError(event.reason);
    event.preventDefault(); // 阻止默认行为
  });

  // 处理全局JavaScript错误
  window.addEventListener('error', event => {
    console.error('🔥 全局JavaScript错误:', {
      message: event.message,
      filename: event.filename,
      lineno: event.lineno,
      colno: event.colno,
      error: event.error
    });
    handleError(event.error);
  });
}
