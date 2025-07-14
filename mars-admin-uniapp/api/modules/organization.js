/**
 * 组织架构相关API
 */
import request from '@/utils/request'
import { API_ENDPOINTS, API_CONFIG } from '@/api/config'
import { departmentList } from '@/mock/index'

/**
 * 获取部门列表
 * @param {Object} params 查询参数
 * @param {string} params.keyword 搜索关键词
 * @param {string} params.parentId 父部门ID
 * @returns {Promise} 部门列表
 */
export const getDepartmentList = (params = {}) => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        let departments = JSON.parse(JSON.stringify(departmentList))
        
        // 搜索过滤
        if (params.keyword) {
          const keyword = params.keyword.toLowerCase()
          departments = departments.filter(dept => {
            // 检查部门名称
            const matchesDept = dept.name.toLowerCase().includes(keyword)
            
            // 检查部门成员
            const matchesMembers = dept.members && dept.members.some(member => 
              member.name.toLowerCase().includes(keyword) ||
              member.position.toLowerCase().includes(keyword)
            )
            
            // 检查子部门
            let matchesSubDepts = false
            if (dept.children && dept.children.length > 0) {
              matchesSubDepts = dept.children.some(subDept => 
                subDept.name.toLowerCase().includes(keyword) ||
                (subDept.members && subDept.members.some(member => 
                  member.name.toLowerCase().includes(keyword) ||
                  member.position.toLowerCase().includes(keyword)
                ))
              )
            }
            
            return matchesDept || matchesMembers || matchesSubDepts
          })
        }
        
        resolve({
          code: 200,
          message: '获取成功',
          data: departments
        })
      }, 500)
    })
  }
  
  return request.get(API_ENDPOINTS.ORGANIZATION.DEPARTMENT_LIST, params)
}

/**
 * 获取部门详情
 * @param {string} id 部门ID
 * @returns {Promise} 部门详情
 */
export const getDepartmentDetail = (id) => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        // 在所有部门中查找
        let department = null
        
        for (const dept of departmentList) {
          if (dept.id === id) {
            department = dept
            break
          }
          
          // 在子部门中查找
          if (dept.children) {
            const subDept = dept.children.find(child => child.id === id)
            if (subDept) {
              department = subDept
              break
            }
          }
        }
        
        if (department) {
          resolve({
            code: 200,
            message: '获取成功',
            data: department
          })
        } else {
          resolve({
            code: 404,
            message: '部门不存在',
            data: null
          })
        }
      }, 300)
    })
  }
  
  return request.get(API_ENDPOINTS.ORGANIZATION.DEPARTMENT_DETAIL, { id })
}

/**
 * 获取部门成员列表
 * @param {Object} params 查询参数
 * @param {string} params.departmentId 部门ID
 * @param {number} params.page 页码
 * @param {number} params.size 每页数量
 * @param {string} params.keyword 搜索关键词
 * @returns {Promise} 成员列表
 */
export const getMemberList = (params = {}) => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        const { departmentId, page = 1, size = 10, keyword } = params
        let members = []
        
        // 获取指定部门的成员
        if (departmentId) {
          for (const dept of departmentList) {
            if (dept.id === departmentId) {
              members = dept.members || []
              break
            }
            
            // 在子部门中查找
            if (dept.children) {
              const subDept = dept.children.find(child => child.id === departmentId)
              if (subDept) {
                members = subDept.members || []
                break
              }
            }
          }
        } else {
          // 获取所有成员
          departmentList.forEach(dept => {
            if (dept.members) {
              members = members.concat(dept.members)
            }
            if (dept.children) {
              dept.children.forEach(subDept => {
                if (subDept.members) {
                  members = members.concat(subDept.members)
                }
              })
            }
          })
        }
        
        // 搜索过滤
        if (keyword) {
          const searchKeyword = keyword.toLowerCase()
          members = members.filter(member => 
            member.name.toLowerCase().includes(searchKeyword) ||
            member.position.toLowerCase().includes(searchKeyword) ||
            (member.phone && member.phone.includes(searchKeyword)) ||
            (member.email && member.email.toLowerCase().includes(searchKeyword))
          )
        }
        
        // 分页处理
        const start = (page - 1) * size
        const end = start + size
        const list = members.slice(start, end)
        
        resolve({
          code: 200,
          message: '获取成功',
          data: {
            list,
            total: members.length,
            page,
            size,
            hasMore: end < members.length
          }
        })
      }, 500)
    })
  }
  
  return request.get(API_ENDPOINTS.ORGANIZATION.MEMBER_LIST, params)
}

/**
 * 获取成员详情
 * @param {string} id 成员ID
 * @returns {Promise} 成员详情
 */
export const getMemberDetail = (id) => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        let member = null
        
        // 在所有部门的成员中查找
        for (const dept of departmentList) {
          if (dept.members) {
            member = dept.members.find(m => m.id === id)
            if (member) {
              member.department = dept.name
              break
            }
          }
          
          // 在子部门中查找
          if (dept.children) {
            for (const subDept of dept.children) {
              if (subDept.members) {
                member = subDept.members.find(m => m.id === id)
                if (member) {
                  member.department = `${dept.name} - ${subDept.name}`
                  break
                }
              }
            }
            if (member) break
          }
        }
        
        if (member) {
          resolve({
            code: 200,
            message: '获取成功',
            data: member
          })
        } else {
          resolve({
            code: 404,
            message: '成员不存在',
            data: null
          })
        }
      }, 300)
    })
  }
  
  return request.get(API_ENDPOINTS.ORGANIZATION.MEMBER_DETAIL, { id })
}

/**
 * 获取组织架构统计信息
 * @returns {Promise} 统计信息
 */
export const getOrganizationStats = () => {
  if (API_CONFIG.mock) {
    return new Promise((resolve) => {
      setTimeout(() => {
        let totalDepts = departmentList.length
        let totalMembers = 0
        
        // 计算总部门数和总人数
        departmentList.forEach(dept => {
          if (dept.members) {
            totalMembers += dept.members.length
          }
          if (dept.children) {
            totalDepts += dept.children.length
            dept.children.forEach(subDept => {
              if (subDept.members) {
                totalMembers += subDept.members.length
              }
            })
          }
        })
        
        resolve({
          code: 200,
          message: '获取成功',
          data: {
            totalDepts,
            totalMembers,
            avgMembersPerDept: Math.round(totalMembers / totalDepts),
            activeDepts: totalDepts // 假设所有部门都是活跃的
          }
        })
      }, 300)
    })
  }
  
  return request.get(API_ENDPOINTS.ORGANIZATION.STATS)
}
