import { request } from '../request';

// ==================== 系统配置管理 ====================

/** 分页查询配置列表 */
export function fetchGetConfigList(params?: Api.SystemManage.ConfigSearchParams) {
  return request<Api.SystemManage.ConfigList>({
    url: '/system/config/page',
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
    data: { ids }
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

// ==================== 字典类型管理 ====================

/** 分页查询字典类型列表 */
export function fetchGetDictTypeList(params?: Api.SystemManage.DictTypeSearchParams) {
  return request<Api.SystemManage.DictTypeList>({
    url: '/system/dict/type/page',
    method: 'get',
    params
  });
}

/** 查询所有正常状态的字典类型 */
export function fetchGetNormalDictTypes() {
  return request<Api.SystemManage.DictType[]>({
    url: '/system/dict/type/optionselect',
    method: 'get'
  });
}

/** 根据字典类型查询字典类型信息 */
export function fetchGetDictTypeByType(dictType: string) {
  return request<Api.SystemManage.DictType>({
    url: `/system/dict/type/type/${dictType}`,
    method: 'get'
  });
}

/** 查询字典类型详情（包含字典数据） */
export function fetchGetDictTypeDetail(dictTypeId: number) {
  return request<Api.SystemManage.DictType>({
    url: `/system/dict/type/detail/${dictTypeId}`,
    method: 'get'
  });
}

/** 根据ID获取字典类型详情 */
export function fetchGetDictTypeById(id: number) {
  return request<Api.SystemManage.DictType>({
    url: `/system/dict/type/${id}`,
    method: 'get'
  });
}

/** 新增字典类型 */
export function fetchAddDictType(data: Partial<Api.SystemManage.DictType>) {
  return request({
    url: '/system/dict/type',
    method: 'post',
    data
  });
}

/** 更新字典类型 */
export function fetchUpdateDictType(data: Partial<Api.SystemManage.DictType>) {
  return request({
    url: '/system/dict/type',
    method: 'put',
    data
  });
}

/** 删除字典类型 */
export function fetchDeleteDictType(id: number) {
  return request({
    url: `/system/dict/type/${id}`,
    method: 'delete'
  });
}

/** 批量删除字典类型 */
export function fetchDeleteDictTypes(ids: number[]) {
  return request({
    url: '/system/dict/type/batch',
    method: 'delete',
    data: { ids }
  });
}

/** 校验字典类型是否唯一 */
export function fetchCheckDictTypeUnique(dictType: string, dictTypeId?: number) {
  return request<boolean>({
    url: '/system/dict/type/checkDictTypeUnique',
    method: 'get',
    params: { dictType, dictTypeId }
  });
}

/** 校验字典名称是否唯一 */
export function fetchCheckDictNameUnique(dictName: string, dictTypeId?: number) {
  return request<boolean>({
    url: '/system/dict/type/checkDictNameUnique',
    method: 'get',
    params: { dictName, dictTypeId }
  });
}

/** 刷新字典缓存 */
export function fetchRefreshDictCache() {
  return request({
    url: '/system/dict/type/refreshCache',
    method: 'delete'
  });
}

// ==================== 字典数据管理 ====================

/** 分页查询字典数据列表 */
export function fetchGetDictDataList(params?: Api.SystemManage.DictDataSearchParams) {
  return request<Api.SystemManage.DictDataList>({
    url: '/system/dict/data/pageList',
    method: 'get',
    params
  });
}

/** 根据字典类型查询字典数据 */
export function fetchGetDictDataByType(dictType: string) {
  return request<Api.SystemManage.DictData[]>({
    url: `/system/dict/data/type/${dictType}`,
    method: 'get'
  });
}

/** 根据字典类型查询正常状态的字典数据 */
export function fetchGetNormalDictDataByType(dictType: string) {
  return request<Api.SystemManage.DictData[]>({
    url: `/system/dict/data/type/${dictType}/normal`,
    method: 'get'
  });
}

/** 根据字典类型和字典值查询字典数据 */
export function fetchGetDictDataByTypeAndValue(dictType: string, dictValue: string) {
  return request<Api.SystemManage.DictData>({
    url: `/system/dict/data/type/${dictType}/value/${dictValue}`,
    method: 'get'
  });
}

/** 根据ID获取字典数据详情 */
export function fetchGetDictDataById(id: number) {
  return request<Api.SystemManage.DictData>({
    url: `/system/dict/data/${id}`,
    method: 'get'
  });
}

/** 新增字典数据 */
export function fetchAddDictData(data: Partial<Api.SystemManage.DictData>) {
  return request({
    url: '/system/dict/data',
    method: 'post',
    data
  });
}

/** 更新字典数据 */
export function fetchUpdateDictData(data: Partial<Api.SystemManage.DictData>) {
  return request({
    url: '/system/dict/data',
    method: 'put',
    data
  });
}

/** 删除字典数据 */
export function fetchDeleteDictData(id: number) {
  return request({
    url: `/system/dict/data/${id}`,
    method: 'delete'
  });
}

/** 批量删除字典数据 */
export function fetchDeleteDictDatas(ids: number[]) {
  return request({
    url: '/system/dict/data/batch',
    method: 'delete',
    data: { ids }
  });
}

/** 校验字典值是否唯一 */
export function fetchCheckDictValueUnique(dictType: string, dictValue: string, dictDataId?: number) {
  return request<boolean>({
    url: '/system/dict/data/checkDictValueUnique',
    method: 'get',
    params: { dictType, dictValue, dictDataId }
  });
}
