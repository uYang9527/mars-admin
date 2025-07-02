import { request } from '../request';

// ==================== 操作日志管理 ====================

/** 分页查询操作日志列表 */
export function fetchGetOperLogList(params?: Api.LogManage.OperLogSearchParams) {
  return request<Api.LogManage.OperLogList>({
    url: '/system/operlog/pageList',
    method: 'get',
    params
  });
}

/** 根据ID获取操作日志 */
export function fetchGetOperLogById(id: number) {
  return request<Api.LogManage.OperLog>({
    url: `/system/operlog/${id}`,
    method: 'get'
  });
}

/** 删除操作日志 */
export function fetchDeleteOperLog(id: number) {
  return request({
    url: `/system/operlog/${id}`,
    method: 'delete'
  });
}

/** 批量删除操作日志 */
export function fetchDeleteOperLogs(ids: number[]) {
  return request({
    url: '/system/operlog/batch',
    method: 'delete',
    data: ids
  });
}

/** 清空操作日志 */
export function fetchCleanOperLog() {
  return request({
    url: '/system/operlog/clean',
    method: 'delete'
  });
}

/** 导出操作日志 */
export function fetchExportOperLog(data: Api.LogManage.OperLog) {
  return request<Api.LogManage.OperLog[]>({
    url: '/system/operlog/export',
    method: 'post',
    data
  });
}

/** 导出操作日志为Excel文件 */
export function fetchExportOperLogExcel(data: Api.LogManage.OperLog) {
  return request({
    url: '/system/operlog/exportExcel',
    method: 'post',
    data,
    responseType: 'blob'
  });
}

/** 根据用户名查询操作日志 */
export function fetchGetOperLogByUser(userName: string) {
  return request<Api.LogManage.OperLog[]>({
    url: `/system/operlog/user/${userName}`,
    method: 'get'
  });
}

/** 根据操作类型查询操作日志 */
export function fetchGetOperLogByType(operType: number) {
  return request<Api.LogManage.OperLog[]>({
    url: `/system/operlog/type/${operType}`,
    method: 'get'
  });
}

/** 根据状态查询操作日志 */
export function fetchGetOperLogByStatus(status: number) {
  return request<Api.LogManage.OperLog[]>({
    url: `/system/operlog/status/${status}`,
    method: 'get'
  });
}

// ==================== 登录日志管理 ====================

/** 分页查询登录日志列表 */
export function fetchGetLoginLogList(params?: Api.LogManage.LoginLogSearchParams) {
  return request<Api.LogManage.LoginLogList>({
    url: '/system/logininfor/pageList',
    method: 'get',
    params
  });
}

/** 根据ID获取登录日志 */
export function fetchGetLoginLogById(id: number) {
  return request<Api.LogManage.LoginLog>({
    url: `/system/logininfor/${id}`,
    method: 'get'
  });
}

/** 删除登录日志 */
export function fetchDeleteLoginLog(id: number) {
  return request({
    url: `/system/logininfor/${id}`,
    method: 'delete'
  });
}

/** 批量删除登录日志 */
export function fetchDeleteLoginLogs(ids: number[]) {
  return request({
    url: '/system/logininfor/batch',
    method: 'delete',
    data: ids
  });
}

/** 清空登录日志 */
export function fetchCleanLoginLog() {
  return request({
    url: '/system/logininfor/clean',
    method: 'delete'
  });
}

/** 导出登录日志（支持GET请求参数） */
export function fetchExportLoginLog(data: Api.LogManage.LoginLog) {
  return request<Api.LogManage.LoginLog[]>({
    url: '/system/logininfor/export',
    method: 'post',
    data
  });
}

/** 导出登录日志为Excel文件 */
export function fetchExportLoginLogExcel(data: Api.LogManage.LoginLog) {
  return request({
    url: '/system/logininfor/exportExcel',
    method: 'post',
    data,
    responseType: 'blob'
  });
}

/** 根据用户名查询登录日志 */
export function fetchGetLoginLogByUser(userName: string) {
  return request<Api.LogManage.LoginLog[]>({
    url: `/system/logininfor/user/${userName}`,
    method: 'get'
  });
}

/** 根据状态查询登录日志 */
export function fetchGetLoginLogByStatus(status: string) {
  return request<Api.LogManage.LoginLog[]>({
    url: `/system/logininfor/status/${status}`,
    method: 'get'
  });
}

/** 根据IP地址查询登录日志 */
export function fetchGetLoginLogByIp(ipaddr: string) {
  return request<Api.LogManage.LoginLog[]>({
    url: `/system/logininfor/ip/${ipaddr}`,
    method: 'get'
  });
}
