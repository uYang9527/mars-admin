import http from '@/api'
import { ADMIN_MODULE } from '@/api/helper/prefix'
import type { IPage } from '@/api/interface'
import type { IAddressBook } from '@/api/interface/addressbook/addressBook'
import type { UploadRawFile } from "element-plus/es/components/upload/src/upload";

/**
* 查询列表
* @param params
* @returns {*}
*/
export const getAddressBookListApi = (params: IAddressBook.Query) => {
  return http.get<IPage<IAddressBook.Row>>(ADMIN_MODULE + `/address-book`, params)
}

/**
* 添加
* @param params
* @returns {*}
*/
export const createAddressBookApi = (params: IAddressBook.Form) => {
  return http.post(ADMIN_MODULE + `/address-book`, params)
}

/**
* 修改
* @param params
* @returns {*}
*/
export const updateAddressBookApi = (params: IAddressBook.Form) => {
  return http.put(ADMIN_MODULE + `/address-book`, params)
}

/**
* 删除
* @param params
* @returns {*}
*/
export const removeAddressBookApi = (params: { ids: number[] }) => {
 return http.delete(ADMIN_MODULE + `/address-book`, params)
}

/**
* 获取详情
* @param params
* @returns {*}
*/
export const getAddressBookDetailApi = (params: { id: number }) => {
  const { id } = params
  return http.get<IAddressBook.Row>(ADMIN_MODULE + `/address-book/${id}`)
}

/**
* 导入excel
* @param params
*/
export const importAddressBookExcelApi = (params : UploadRawFile) => {
  return http.upload(ADMIN_MODULE + `/address-book/import`, params)
}

/**
* 导出excel
* @param params
* @returns {*}
*/
export const exportAddressBookExcelApi  = (params: IAddressBook.Query) => {
  return http.download(ADMIN_MODULE + `/address-book/export`, params)
}