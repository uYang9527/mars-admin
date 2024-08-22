import type { IPageQuery } from '@/api/interface'

export namespace ICategory {

  // 查询条件
  export interface Query extends IPageQuery {
    type?: number
    name?: string
    sort?: number
    createUser?: number
    updateUser?: number
  }

  // 编辑form表单
  export interface Form {
    id?: number
    type?: number
    name?: string
    sort?: number
    createUser?: number
    updateUser?: number
 }

  // list或detail返回结构
  export interface Row {
    id?: number
    type?: number
    name?: string
    sort?: number
    createUser?: number
    updateUser?: number
  }

}