import { request } from '@/service/request';

/** 获取系统监控信息 */
export function fetchSystemMonitorInfo() {
  return request.get<Api.SystemManage.MonitorInfo>('/system/monitor/info');
}

/** 系统健康检查 */
export function fetchSystemHealthCheck() {
  return request.get<Api.SystemManage.HealthCheck>('/system/monitor/health');
}

/** 获取系统性能摘要 */
export function fetchPerformanceSummary() {
  return request.get<Record<string, any>>('/system/monitor/summary');
}

/** 获取系统告警信息 */
export function fetchSystemAlerts(params?: {
  cpuThreshold?: number;
  memoryThreshold?: number;
  diskThreshold?: number;
  jvmThreshold?: number;
}) {
  return request.get<Record<string, any>>('/system/monitor/alerts', { params });
}

/** 获取系统运行时间统计 */
export function fetchSystemUptime() {
  return request.get<Record<string, any>>('/system/monitor/uptime');
} 