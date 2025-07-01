import { request } from '../request';

/**
 * 获取缓存监控信息
 */
export function fetchCacheMonitor() {
  return request<Api.CacheManage.MonitorInfo>({
    url: '/system/cache/monitor',
    method: 'get'
  });
}

/**
 * 获取缓存列表
 */
export function fetchCacheList(params: {
  current: number;
  size: number;
  pattern?: string;
  cacheSpace?: string;
}) {
  return request<Api.CacheManage.CacheListResponse>({
    url: '/system/cache/list',
    method: 'get',
    params
  });
}

/**
 * 获取缓存详情
 */
export function fetchCacheDetail(key: string) {
  return request<Api.CacheManage.CacheDetail>({
    url: `/system/cache/detail/${encodeURIComponent(key)}`,
    method: 'get'
  });
}

/**
 * 删除缓存
 */
export function fetchDeleteCache(key: string) {
  return request<string>({
    url: `/system/cache/delete/${encodeURIComponent(key)}`,
    method: 'delete'
  });
}

/**
 * 批量删除缓存
 */
export function fetchDeleteCacheBatch(keys: string[]) {
  return request<string>({
    url: '/system/cache/batch',
    method: 'delete',
    data: keys
  });
}

/**
 * 清空缓存空间
 */
export function fetchClearCacheSpace(cacheSpace: string) {
  return request<string>({
    url: `/system/cache/clear/${cacheSpace}`,
    method: 'delete'
  });
}

/**
 * 获取缓存统计信息
 */
export function fetchCacheStats() {
  return request<Api.CacheManage.CacheStats>({
    url: '/system/cache/stats',
    method: 'get'
  });
}

/**
 * 清除所有缓存（修复序列化问题）
 */
export function fetchClearAllCache() {
  return request<string>({
    url: '/system/cache/clear-all',
    method: 'delete'
  });
} 