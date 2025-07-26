# Mars Admin Mapper XML 文件说明

本目录包含了Mars Admin系统中所有Mapper接口对应的XML映射文件，用于定义复杂的SQL查询语句。

## 文件列表

### 1. SysUserMapper.xml
**对应接口**: `com.mars.admin.modules.system.mapper.SysUserMapper`
**主要功能**:
- 用户详情查询（包含角色、部门、岗位信息）
- 用户列表分页查询
- 根据角色ID查询用户列表
- 根据部门ID查询用户列表
- 更新用户登录信息

**核心SQL**:
- `selectUserDetailById`: 查询用户详情
- `selectUserList`: 分页查询用户列表
- `selectUsersByRoleId`: 根据角色ID查询用户
- `selectUsersByDeptId`: 根据部门ID查询用户
- `updateUserLoginInfo`: 更新用户登录信息

### 2. SysRoleMapper.xml
**对应接口**: `com.mars.admin.modules.system.mapper.SysRoleMapper`
**主要功能**:
- 角色列表查询
- 根据用户ID查询角色列表
- 角色详情查询
- 根据菜单ID查询角色列表

**核心SQL**:
- `selectRoleList`: 分页查询角色列表
- `selectRolesByUserId`: 根据用户ID查询角色（用于getCurrentUser）
- `selectRoleDetailById`: 查询角色详情
- `selectRolesByMenuId`: 根据菜单ID查询角色

### 3. SysDeptMapper.xml
**对应接口**: `com.mars.admin.modules.system.mapper.SysDeptMapper`
**主要功能**:
- 部门列表查询
- 根据用户ID查询部门列表
- 根据角色ID查询部门列表
- 部门树查询

**核心SQL**:
- `selectDeptList`: 分页查询部门列表
- `selectDeptsByUserId`: 根据用户ID查询部门（用于getCurrentUser）
- `selectDeptsByRoleId`: 根据角色ID查询部门
- `selectDeptTree`: 查询部门树结构

### 4. SysPostMapper.xml
**对应接口**: `com.mars.admin.modules.system.mapper.SysPostMapper`
**主要功能**:
- 岗位列表查询
- 根据用户ID查询岗位列表

**核心SQL**:
- `selectPostList`: 分页查询岗位列表
- `selectPostsByUserId`: 根据用户ID查询岗位（用于getCurrentUser）

### 5. SysMenuMapper.xml
**对应接口**: `com.mars.admin.modules.system.mapper.SysMenuMapper`
**主要功能**:
- 菜单列表查询
- 根据用户ID查询菜单列表
- 根据角色ID查询菜单列表
- 根据父菜单ID查询子菜单

**核心SQL**:
- `selectMenuList`: 分页查询菜单列表
- `selectMenusByUserId`: 根据用户ID查询菜单（权限控制）
- `selectMenusByRoleId`: 根据角色ID查询菜单
- `selectChildrenByParentId`: 查询子菜单

### 6. SysConfigMapper.xml
**对应接口**: `com.mars.admin.modules.system.mapper.SysConfigMapper`
**主要功能**:
- 系统配置列表查询

**核心SQL**:
- `selectConfigList`: 分页查询配置列表

### 7. SysDictTypeMapper.xml
**对应接口**: `com.mars.admin.modules.system.mapper.SysDictTypeMapper`
**主要功能**:
- 字典类型列表查询
- 字典类型详情查询

**核心SQL**:
- `selectDictTypeList`: 分页查询字典类型列表
- `selectDictTypeDetailById`: 查询字典类型详情

### 8. SysDictDataMapper.xml
**对应接口**: `com.mars.admin.modules.system.mapper.SysDictDataMapper`
**主要功能**:
- 字典数据列表查询
- 根据字典类型查询字典数据

**核心SQL**:
- `selectDictDataList`: 分页查询字典数据列表
- `selectDictDataByType`: 根据字典类型查询字典数据

### 9. SysOperLogMapper.xml
**对应接口**: `com.mars.admin.modules.system.mapper.SysOperLogMapper`
**主要功能**:
- 操作日志列表查询

**核心SQL**:
- `selectOperLogList`: 分页查询操作日志列表

### 10. SysLogininforMapper.xml
**对应接口**: `com.mars.admin.mapper.SysLogininforMapper`
**主要功能**:
- 登录日志列表查询
- 清空登录日志

**核心SQL**:
- `selectLogininforList`: 分页查询登录日志列表
- `cleanLoginInfo`: 清空登录日志

## 关键特性

### 1. ResultMap映射
每个XML文件都定义了对应的ResultMap，用于将数据库字段映射到Java实体类属性，支持驼峰命名转换。

### 2. 动态SQL
使用MyBatis的动态SQL特性，支持条件查询、模糊查询等功能。

### 3. 关联查询
- `selectRolesByUserId`: 通过用户角色关联表查询用户的角色列表
- `selectDeptsByUserId`: 通过用户部门关联表查询用户的部门列表
- `selectPostsByUserId`: 通过用户岗位关联表查询用户的岗位列表
- `selectMenusByUserId`: 通过角色菜单关联表查询用户的菜单权限

### 4. 权限控制
- 所有查询都包含`is_deleted = 0`条件，实现逻辑删除
- 权限相关查询包含`status = 1`条件，确保只查询启用状态的数据

## 使用说明

1. 这些XML文件与对应的Mapper接口配合使用
2. 复杂查询通过XML实现，简单查询可直接使用MyBatis-Flex的便捷方法
3. 所有SQL都经过优化，支持分页查询和排序
4. 遵循统一的命名规范和代码风格

## 注意事项

1. 修改XML文件后需要重启应用
2. 新增SQL语句时请遵循现有的命名规范
3. 确保SQL语句的安全性，防止SQL注入
4. 复杂关联查询建议添加适当的索引优化性能 
