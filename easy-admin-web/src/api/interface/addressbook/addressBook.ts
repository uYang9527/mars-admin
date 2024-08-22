import type { IPageQuery } from '@/api/interface'

export namespace IAddressBook {

  // 查询条件
  export interface Query extends IPageQuery {
    userId?: number
    consignee?: string
    sex?: number
    phone?: string
    provinceCode?: string
    provinceName?: string
    cityCode?: string
    cityName?: string
    districtCode?: string
    districtName?: string
    detail?: string
    label?: string
    isDefault?: number
    createUser?: number
    updateUser?: number
    isDeleted?: number
  }

  // 编辑form表单
  export interface Form {
    id?: number
    userId?: number
    consignee?: string
    sex?: number
    phone?: string
    provinceCode?: string
    provinceName?: string
    cityCode?: string
    cityName?: string
    districtCode?: string
    districtName?: string
    detail?: string
    label?: string
    isDefault?: number
    createUser?: number
    updateUser?: number
    isDeleted?: number
 }

  // list或detail返回结构
  export interface Row {
    id?: number
    userId?: number
    consignee?: string
    sex?: number
    phone?: string
    provinceCode?: string
    provinceName?: string
    cityCode?: string
    cityName?: string
    districtCode?: string
    districtName?: string
    detail?: string
    label?: string
    isDefault?: number
    createUser?: number
    updateUser?: number
    isDeleted?: number
  }

}