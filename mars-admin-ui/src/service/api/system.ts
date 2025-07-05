import { request } from '@/service/request';

/**
 * 获取系统监控信息
 */
export const getMonitorInfo = () => {
  return request<any>({
    url: '/system/monitor/info',
    method: 'GET'
  });
};

// 向后兼容的别名
export const fetchSystemMonitorInfo = getMonitorInfo;

/**
 * 获取系统资源使用情况
 */
export const getResourceUsage = () => {
  return request<any>({
    url: '/system/monitor/resources',
    method: 'GET'
  });
};

/**
 * 获取系统告警信息
 */
export const getSystemAlerts = (params?: {
  cpuThreshold?: number;
  memoryThreshold?: number;
  diskThreshold?: number;
  jvmThreshold?: number;
}) => {
  return request<any>({
    url: '/system/monitor/alerts',
    method: 'GET',
    params
  });
};

/**
 * 获取系统性能总览
 */
export const getPerformanceOverview = () => {
  return request<any>({
    url: '/system/monitor/overview',
    method: 'GET'
  });
};

/**
 * 获取进程信息
 */
export const getProcessInfo = () => {
  return request<any>({
    url: '/system/monitor/process',
    method: 'GET'
  });
};

/**
 * 获取垃圾回收信息
 */
export const getGcInfo = () => {
  return request<any>({
    url: '/system/monitor/gc',
    method: 'GET'
  });
}; 