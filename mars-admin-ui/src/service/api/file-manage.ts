import { request } from '../request';

// ==================== 文件管理 ====================

/** 上传文件 */
export function fetchUploadFile(formData: FormData) {
  return request<Api.FileManage.FileUploadResult>({
    url: '/file/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

/** 分页查询文件列表 */
export function fetchGetFileList(params?: Api.FileManage.FileSearchParams) {
  return request<Api.FileManage.FileList>({
    url: '/file/page',
    method: 'get',
    params
  });
}

/** 根据ID获取文件信息 */
export function fetchGetFileById(id: number) {
  return request<Api.FileManage.FileInfo>({
    url: `/file/${id}`,
    method: 'get'
  });
}

/** 删除文件 */
export function fetchDeleteFile(id: number) {
  return request<boolean>({
    url: `/file/${id}`,
    method: 'delete'
  });
}

/** 批量删除文件 */
export function fetchDeleteFiles(ids: number[]) {
  return request<boolean>({
    url: '/file/batch',
    method: 'delete',
    data: ids
  });
}

/** 获取文件预签名URL */
export function fetchGetPresignedUrl(id: number, expireTime?: number) {
  return request<string>({
    url: `/file/${id}/presigned-url`,
    method: 'get',
    params: { expireTime }
  });
}

/** 获取可用的上传策略类型 */
export function fetchGetAvailableTypes() {
  return request<string[]>({
    url: '/file/types',
    method: 'get'
  });
}

/** 获取文件预览信息 */
export function fetchGetFilePreviewInfo(fileId: number) {
  return request<Api.FileManage.FilePreviewInfo>({
    url: `/file/preview/info/${fileId}`,
    method: 'get'
  });
}

/** 获取文件下载地址 */
export function getFileDownloadUrl(fileId: number): string {
  return `/proxy-default/file/download/${fileId}`;
}

/** 获取文件预览地址 */
export function getFilePreviewUrl(fileId: number): string {
  return `/proxy-default/file/preview/${fileId}`;
}

// ==================== OSS配置管理 ====================

/** 分页查询OSS配置列表 */
export function fetchGetOssConfigList(params?: Api.FileManage.OssConfigSearchParams) {
  return request<Api.FileManage.OssConfigList>({
    url: '/system/oss-config/page',
    method: 'get',
    params
  });
}

/** 根据ID获取OSS配置 */
export function fetchGetOssConfigById(id: number) {
  return request<Api.FileManage.OssConfig>({
    url: `/system/oss-config/${id}`,
    method: 'get'
  });
}

/** 根据配置key获取OSS配置 */
export function fetchGetOssConfigByKey(configKey: string) {
  return request<Api.FileManage.OssConfig>({
    url: `/system/oss-config/by-key/${configKey}`,
    method: 'get'
  });
}

/** 获取启用的OSS配置列表 */
export function fetchGetEnabledOssConfigs() {
  return request<Api.FileManage.OssConfig[]>({
    url: '/system/oss-config/enabled',
    method: 'get'
  });
}

/** 新增OSS配置 */
export function fetchAddOssConfig(data: Partial<Api.FileManage.OssConfig>) {
  return request({
    url: '/system/oss-config',
    method: 'post',
    data
  });
}

/** 更新OSS配置 */
export function fetchUpdateOssConfig(data: Partial<Api.FileManage.OssConfig>) {
  return request({
    url: '/system/oss-config',
    method: 'put',
    data
  });
}

/** 删除OSS配置 */
export function fetchDeleteOssConfig(id: number) {
  return request({
    url: `/system/oss-config/${id}`,
    method: 'delete'
  });
}

/** 批量删除OSS配置 */
export function fetchDeleteOssConfigs(ids: number[]) {
  return request({
    url: '/system/oss-config/batch',
    method: 'delete',
    data: ids
  });
}

/** 更新OSS配置状态 */
export function fetchUpdateOssConfigStatus(id: number, status: number) {
  return request({
    url: `/system/oss-config/${id}/status/${status}`,
    method: 'put'
  });
}

/** 切换OSS配置状态 */
export function fetchChangeOssConfigStatus(id: number, status: number) {
  return request({
    url: `/system/oss-config/${id}/status`,
    method: 'put',
    data: { status }
  });
}

/** 测试OSS配置连接 */
export function fetchTestOssConnection(data: Partial<Api.FileManage.OssConfig>) {
  return request({
    url: '/system/oss-config/test',
    method: 'post',
    data
  });
}

/**
 * 刷新OSS配置缓存
 */
export function fetchRefreshOssConfigCache() {
  return request({
    url: '/system/oss-config/refreshCache',
    method: 'delete'
  });
}
