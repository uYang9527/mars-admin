import { request } from '../request';

// ==================== 系统配置管理 ====================

/** 分页查询配置列表 */
export function fetchGetConfigList(params?: Api.SystemManage.ConfigSearchParams) {
  return request<Api.SystemManage.ConfigList>({
    url: '/system/config/pageList',
    method: 'get',
    params
  });
}

/** 根据配置键查询配置值 */
export function fetchGetConfigByKey(configKey: string) {
  return request<string>({
    url: `/system/config/configKey/${configKey}`,
    method: 'get'
  });
}

/** 根据ID获取配置详情 */
export function fetchGetConfigById(id: number) {
  return request<Api.SystemManage.Config>({
    url: `/system/config/${id}`,
    method: 'get'
  });
}

/** 新增配置 */
export function fetchAddConfig(data: Partial<Api.SystemManage.Config>) {
  return request({
    url: '/system/config',
    method: 'post',
    data
  });
}

/** 更新配置 */
export function fetchUpdateConfig(data: Partial<Api.SystemManage.Config>) {
  return request({
    url: '/system/config',
    method: 'put',
    data
  });
}

/** 删除配置 */
export function fetchDeleteConfig(id: number) {
  return request({
    url: `/system/config/${id}`,
    method: 'delete'
  });
}

/** 批量删除配置 */
export function fetchDeleteConfigs(ids: number[]) {
  return request({
    url: '/system/config/batch',
    method: 'delete',
    data: ids
  });
}

/** 校验配置键是否唯一 */
export function fetchCheckConfigKeyUnique(configKey: string, configId?: number) {
  return request<boolean>({
    url: '/system/config/checkConfigKeyUnique',
    method: 'get',
    params: { configKey, configId }
  });
}

/** 刷新配置缓存 */
export function fetchRefreshConfigCache() {
  return request({
    url: '/system/config/refreshCache',
    method: 'delete'
  });
}


