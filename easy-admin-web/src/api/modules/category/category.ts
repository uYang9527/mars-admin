import http from '@/api'
import { ADMIN_MODULE } from '@/api/helper/prefix'
import type { IPage } from '@/api/interface'
import type { ICategory } from '@/api/interface/category/category'
import type { UploadRawFile } from "element-plus/es/components/upload/src/upload";

/**
* 查询列表
* @param params
* @returns {*}
*/
export const getCategoryListApi = (params: ICategory.Query) => {
  return http.get<IPage<ICategory.Row>>(ADMIN_MODULE + `/category`, params)
}

/**
* 添加
* @param params
* @returns {*}
*/
export const createCategoryApi = (params: ICategory.Form) => {
  return http.post(ADMIN_MODULE + `/category`, params)
}

/**
* 修改
* @param params
* @returns {*}
*/
export const updateCategoryApi = (params: ICategory.Form) => {
  return http.put(ADMIN_MODULE + `/category`, params)
}

/**
* 删除
* @param params
* @returns {*}
*/
export const removeCategoryApi = (params: { ids: number[] }) => {
 return http.delete(ADMIN_MODULE + `/category`, params)
}

/**
* 获取详情
* @param params
* @returns {*}
*/
export const getCategoryDetailApi = (params: { id: number }) => {
  const { id } = params
  return http.get<ICategory.Row>(ADMIN_MODULE + `/category/${id}`)
}

/**
* 导入excel
* @param params
*/
export const importCategoryExcelApi = (params : UploadRawFile) => {
  return http.upload(ADMIN_MODULE + `/category/import`, params)
}

/**
* 导出excel
* @param params
* @returns {*}
*/
export const exportCategoryExcelApi  = (params: ICategory.Query) => {
  return http.download(ADMIN_MODULE + `/category/export`, params)
}