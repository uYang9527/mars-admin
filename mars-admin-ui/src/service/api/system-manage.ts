import { request } from '../request';

/** get role list */
export function fetchGetRoleList(params?: Api.SystemManage.RoleSearchParams) {
  return request<Api.SystemManage.RoleList>({
    url: '/system/role/page',
    method: 'get',
    params
  });
}

/**
 * get all roles
 *
 * these roles are all enabled
 */
export function fetchGetAllRoles() {
  return request<Api.SystemManage.AllRole[]>({
    url: '/system/role/list',
    method: 'get'
  });
}

/** get user list */
export function fetchGetUserList(params?: Api.SystemManage.UserSearchParams) {
  return request<Api.SystemManage.UserList>({
    url: '/system/user/page',
    method: 'get',
    params
  });
}

/** get menu list */
export function fetchGetMenuList() {
  return request<Api.SystemManage.Menu[]>({
    url: '/system/menu/list',
    method: 'get'
  });
}

/** get all pages */
export function fetchGetAllPages() {
  return request<string[]>({
    url: '/systemManage/getAllPages',
    method: 'get'
  });
}

/** get menu tree */
export function fetchGetMenuTree() {
  return request<Api.SystemManage.MenuTree[]>({
    url: '/system/menu/tree',
    method: 'get'
  });
}

/**
 * 获取所有用户
 */
export function fetchGetAllUsers() {
  return request<Api.SystemManage.User[]>({
    url: '/system/user/list',
    method: 'get'
  });
}

/**
 * 根据ID获取用户详情
 */
export function fetchGetUserById(id: number) {
  return request<Api.SystemManage.User>({
    url: `/system/user/${id}`,
    method: 'get'
  });
}

/**
 * 新增用户
 */
export function fetchAddUser(data: Partial<Api.SystemManage.User>) {
  return request({
    url: '/system/user',
    method: 'post',
    data
  });
}

/**
 * 更新用户
 */
export function fetchUpdateUser(data: Partial<Api.SystemManage.User>) {
  return request({
    url: '/system/user',
    method: 'put',
    data
  });
}

/**
 * 删除用户
 */
export function fetchDeleteUser(id: number) {
  return request({
    url: `/system/user/${id}`,
    method: 'delete'
  });
}

/**
 * 批量删除用户
 */
export function fetchDeleteUsers(ids: number[]) {
  return request({
    url: '/system/user/batch',
    method: 'delete',
    data: { ids }
  });
}

/**
 * 重置用户密码
 */
export function fetchResetUserPassword(userId: number, password: string) {
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: { id: userId, password }
  });
}

/**
 * 修改用户状态
 */
export function fetchChangeUserStatus(userId: number, status: string) {
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: { id: userId, status }
  });
}

/**
 * 分配用户角色
 */
export function fetchAssignUserRoles(userId: number, roleIds: number[]) {
  return request({
    url: '/system/user/authRole',
    method: 'put',
    data: { id: userId, roleIds }
  });
}

/**
 * 校验用户名是否唯一
 */
export function fetchCheckUsernameUnique(username: string, userId?: number) {
  return request<boolean>({
    url: '/system/user/checkUsernameUnique',
    method: 'get',
    params: { username, userId }
  });
}

/**
 * 校验邮箱是否唯一
 */
export function fetchCheckEmailUnique(email: string, userId?: number) {
  return request<boolean>({
    url: '/system/user/checkEmailUnique',
    method: 'get',
    params: { email, userId }
  });
}

/**
 * 校验手机号是否唯一
 */
export function fetchCheckPhoneUnique(phone: string, userId?: number) {
  return request<boolean>({
    url: '/system/user/checkPhoneUnique',
    method: 'get',
    params: { phone, userId }
  });
}

/**
 * 查询所有正常状态的角色
 */
export function fetchGetNormalRoles() {
  return request<Api.SystemManage.AllRole[]>({
    url: '/system/role/optionselect',
    method: 'get'
  });
}

/**
 * 根据ID获取角色详情
 */
export function fetchGetRoleById(id: number) {
  return request<Api.SystemManage.Role>({
    url: `/system/role/${id}`,
    method: 'get'
  });
}

/**
 * 新增角色
 */
export function fetchAddRole(data: Partial<Api.SystemManage.Role>) {
  return request({
    url: '/system/role',
    method: 'post',
    data
  });
}

/**
 * 更新角色
 */
export function fetchUpdateRole(data: Partial<Api.SystemManage.Role>) {
  return request({
    url: '/system/role',
    method: 'put',
    data
  });
}

/**
 * 删除角色
 */
export function fetchDeleteRole(id: number) {
  return request({
    url: `/system/role/${id}`,
    method: 'delete'
  });
}

/**
 * 批量删除角色
 */
export function fetchDeleteRoles(ids: number[]) {
  return request({
    url: '/system/role/batch',
    method: 'delete',
    data: { ids }
  });
}

/**
 * 修改角色状态
 */
export function fetchChangeRoleStatus(roleId: number, status: string) {
  return request({
    url: '/system/role/changeStatus',
    method: 'put',
    data: { id: roleId, status }
  });
}

/**
 * 校验角色编码是否唯一
 */
export function fetchCheckRoleCodeUnique(roleCode: string, roleId?: number) {
  return request<boolean>({
    url: '/system/role/checkRoleCodeUnique',
    method: 'get',
    params: { roleCode, roleId }
  });
}

/**
 * 校验角色权限字符是否唯一
 */
export function fetchCheckRoleKeyUnique(roleKey: string, roleId?: number) {
  return request<boolean>({
    url: '/system/role/checkRoleKeyUnique',
    method: 'get',
    params: { roleKey, roleId }
  });
}

/**
 * 根据用户ID查询菜单
 */
export function fetchGetMenusByUserId(userId: number) {
  return request<Api.SystemManage.Menu[]>({
    url: `/system/menu/user/${userId}`,
    method: 'get'
  });
}

/**
 * 根据角色ID查询菜单
 */
export function fetchGetMenusByRoleId(roleId: number) {
  return request<Api.SystemManage.Menu[]>({
    url: `/system/menu/role/${roleId}`,
    method: 'get'
  });
}

/**
 * 根据用户ID查询权限
 */
export function fetchGetPermsByUserId(userId: number) {
  return request<string[]>({
    url: `/system/menu/permissions/${userId}`,
    method: 'get'
  });
}

/**
 * 根据ID获取菜单详情
 */
export function fetchGetMenuById(id: number) {
  return request<Api.SystemManage.Menu>({
    url: `/system/menu/${id}`,
    method: 'get'
  });
}

/**
 * 新增菜单
 */
export function fetchAddMenu(data: Partial<Api.SystemManage.Menu>) {
  return request({
    url: '/system/menu',
    method: 'post',
    data
  });
}

/**
 * 更新菜单
 */
export function fetchUpdateMenu(data: Partial<Api.SystemManage.Menu>) {
  return request({
    url: '/system/menu',
    method: 'put',
    data
  });
}

/**
 * 删除菜单
 */
export function fetchDeleteMenu(id: number) {
  return request({
    url: `/system/menu/${id}`,
    method: 'delete'
  });
}

/**
 * 校验菜单编码是否唯一
 */
export function fetchCheckMenuCodeUnique(menuCode: string, menuId?: number) {
  return request<boolean>({
    url: '/system/menu/checkMenuCodeUnique',
    method: 'get',
    params: { menuCode, menuId }
  });
}

/**
 * 校验权限标识是否唯一
 */
export function fetchCheckPermsUnique(perms: string, menuId?: number) {
  return request<boolean>({
    url: '/system/menu/checkPermsUnique',
    method: 'get',
    params: { perms, menuId }
  });
}

/**
 * 获取部门列表
 */
export function fetchGetDeptList() {
  return request<Api.SystemManage.Dept[]>({
    url: '/system/dept/list',
    method: 'get'
  });
}

/**
 * 查询部门树结构
 */
export function fetchGetDeptTree() {
  return request<Api.SystemManage.DeptTree[]>({
    url: '/system/dept/tree',
    method: 'get'
  });
}

/**
 * 根据用户ID查询部门
 */
export function fetchGetDeptsByUserId(userId: number) {
  return request<Api.SystemManage.Dept[]>({
    url: `/system/dept/user/${userId}`,
    method: 'get'
  });
}

/**
 * 根据角色ID查询部门
 */
export function fetchGetDeptsByRoleId(roleId: number) {
  return request<Api.SystemManage.Dept[]>({
    url: `/system/dept/role/${roleId}`,
    method: 'get'
  });
}

/**
 * 根据ID获取部门详情
 */
export function fetchGetDeptById(id: number) {
  return request<Api.SystemManage.Dept>({
    url: `/system/dept/${id}`,
    method: 'get'
  });
}

/**
 * 新增部门
 */
export function fetchAddDept(data: Partial<Api.SystemManage.Dept>) {
  return request({
    url: '/system/dept',
    method: 'post',
    data
  });
}

/**
 * 更新部门
 */
export function fetchUpdateDept(data: Partial<Api.SystemManage.Dept>) {
  return request({
    url: '/system/dept',
    method: 'put',
    data
  });
}

/**
 * 删除部门
 */
export function fetchDeleteDept(id: number) {
  return request({
    url: `/system/dept/${id}`,
    method: 'delete'
  });
}

/**
 * 校验部门编码是否唯一
 */
export function fetchCheckDeptCodeUnique(deptCode: string, deptId?: number) {
  return request<boolean>({
    url: '/system/dept/checkDeptCodeUnique',
    method: 'get',
    params: { deptCode, deptId }
  });
}

/**
 * 校验部门名称是否唯一
 */
export function fetchCheckDeptNameUnique(deptName: string, deptId?: number) {
  return request<boolean>({
    url: '/system/dept/checkDeptNameUnique',
    method: 'get',
    params: { deptName, deptId }
  });
}

/**
 * 分页查询岗位列表
 */
export function fetchGetPostList(params?: Api.SystemManage.PostSearchParams) {
  // 转换参数，确保与后端期望的参数名一致
  const apiParams: Record<string, any> = { ...params };
  if (params?.current !== undefined) {
    apiParams.pageNumber = params.current;
    delete apiParams.current;
  }
  if (params?.size !== undefined) {
    apiParams.pageSize = params.size;
    delete apiParams.size;
  }
  
  return request<Api.Common.PaginatingQueryRecord<Api.SystemManage.Post>>({
    url: '/system/post/page',
    method: 'get',
    params: apiParams
  });
}

/**
 * 查询所有正常状态的岗位
 */
export function fetchGetNormalPosts() {
  return request<Api.SystemManage.Post[]>({
    url: '/system/post/optionselect',
    method: 'get'
  });
}

/**
 * 根据用户ID查询岗位
 */
export function fetchGetPostsByUserId(userId: number) {
  return request<Api.SystemManage.Post[]>({
    url: `/system/post/user/${userId}`,
    method: 'get'
  });
}

/**
 * 根据ID获取岗位详情
 */
export function fetchGetPostById(id: number) {
  return request<Api.SystemManage.Post>({
    url: `/system/post/${id}`,
    method: 'get'
  });
}

/**
 * 新增岗位
 */
export function fetchAddPost(data: Partial<Api.SystemManage.Post>) {
  return request({
    url: '/system/post',
    method: 'post',
    data
  });
}

/**
 * 更新岗位
 */
export function fetchUpdatePost(data: Partial<Api.SystemManage.Post>) {
  return request({
    url: '/system/post',
    method: 'put',
    data
  });
}

/**
 * 删除岗位
 */
export function fetchDeletePost(id: number) {
  return request({
    url: `/system/post/${id}`,
    method: 'delete'
  });
}

/**
 * 批量删除岗位
 */
export function fetchDeletePosts(ids: number[]) {
  return request({
    url: '/system/post/batch',
    method: 'delete',
    data: { ids }
  });
}

/**
 * 校验岗位编码是否唯一
 */
export function fetchCheckPostCodeUnique(postCode: string, postId?: number) {
  return request<boolean>({
    url: '/system/post/checkPostCodeUnique',
    method: 'get',
    params: { postCode, postId }
  });
}

/**
 * 校验岗位名称是否唯一
 */
export function fetchCheckPostNameUnique(postName: string, postId?: number) {
  return request<boolean>({
    url: '/system/post/checkPostNameUnique',
    method: 'get',
    params: { postName, postId }
  });
}

/**
 * 修改岗位状态
 */
export function fetchChangePostStatus(postId: number, status: string) {
  return request({
    url: '/system/post/changeStatus',
    method: 'put',
    data: { id: postId, status }
  });
}
