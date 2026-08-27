import { request } from '@/utils/request'

export interface FlowDefinition {
  id: string
  flowCode: string
  flowName: string
  category?: string
  version: string
  isPublish: number
  activityStatus: number
  formCustom?: string
  formPath?: string
  createTime?: string
  updateTime?: string
}

export interface WorkflowTask {
  id: number
  definitionId: number
  instanceId: number
  nodeCode: string
  nodeName: string
  flowStatus: string
  flowCode: string
  flowName: string
  businessId: string
  createBy?: string
  approverName?: string
  createTime?: string
  updateTime?: string
}

export interface WorkflowInstance {
  id: number
  definitionId: number
  businessId: string
  nodeCode: string
  nodeName: string
  flowStatus: string
  flowCode: string
  flowName: string
  createBy?: string
  createByName?: string
  createTime?: string
  updateTime?: string
}

export const workflowApi = {
  definitionPage(params: { page?: number; pageSize?: number; flowName?: string; flowCode?: string }) {
    return request({ url: '/workflow/definition/page', method: 'get', params })
  },
  publishedList() {
    return request({ url: '/workflow/definition/published', method: 'get' })
  },
  getDefinition(id: string) {
    return request({ url: `/workflow/definition/${id}`, method: 'get' })
  },
  publishDefinition(id: string) {
    return request({ url: `/workflow/definition/publish/${id}`, method: 'post' })
  },
  unpublishDefinition(id: string) {
    return request({ url: `/workflow/definition/unpublish/${id}`, method: 'post' })
  },
  removeDefinition(ids: string[]) {
    return request({ url: `/workflow/definition/${ids.join(',')}`, method: 'delete' })
  },
  activeDefinition(id: string) {
    return request({ url: `/workflow/definition/active/${id}`, method: 'post' })
  },
  unActiveDefinition(id: string) {
    return request({ url: `/workflow/definition/unactive/${id}`, method: 'post' })
  },
  copyDefinition(id: string) {
    return request({ url: `/workflow/definition/copy/${id}`, method: 'post' })
  },
  todoPage(params: { page?: number; pageSize?: number; flowName?: string }) {
    return request({ url: '/workflow/task/todo/page', method: 'get', params })
  },
  donePage(params: { page?: number; pageSize?: number; flowName?: string }) {
    return request({ url: '/workflow/task/done/page', method: 'get', params })
  },
  approve(data: { taskId: number; message?: string; variable?: Record<string, any> }) {
    return request({ url: '/workflow/task/approve', method: 'post', data })
  },
  reject(data: { taskId: number; message?: string; variable?: Record<string, any> }) {
    return request({ url: '/workflow/task/reject', method: 'post', data })
  },
  instancePage(params: { page?: number; pageSize?: number; flowName?: string; flowStatus?: string }) {
    return request({ url: '/workflow/instance/page', method: 'get', params })
  },
  startInstance(data: { flowCode: string; businessId: string; variable?: Record<string, any> }) {
    return request({ url: '/workflow/instance/start', method: 'post', data })
  },
  terminateInstance(id: number, message?: string) {
    return request({ url: `/workflow/instance/terminate/${id}`, method: 'post', data: { message } })
  }
}
