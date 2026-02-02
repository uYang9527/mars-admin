import { request } from '@/utils/request'

// 学生管理 类型定义
export interface Student {
  id?: number

  name?:  string

  phone?:  string

  idCard?:  string

  address?:  string

  remark?:  string

  deleted?: number

  createTime?:  string

  updateTime?:  string

}

// 学生管理 API
export const studentApi = {
  // 分页查询
  page(params: { page: number; pageSize: number; id?: number; name?:  string }) {
    return request({ url: '/system/student/page', method: 'get', params })
  },

  // 获取详情
  detail(id: number) {
    return request({ url: `/system/student/${id}`, method: 'get' })
  },

  // 新增
  create(data: Student) {
    return request({ url: '/system/student', method: 'post', data })
  },

  // 修改
  update(data: Student) {
    return request({ url: '/system/student', method: 'put', data })
  },

  // 删除
  delete(ids: number[]) {
    return request({ url: `/system/student/${ids.join(',')}`, method: 'delete' })
  }
}
