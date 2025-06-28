# 用户管理

用户管理是Mars Admin系统的核心功能之一，负责系统操作者的管理，包括用户的创建、修改、删除、查询、权限分配等操作。

## 功能概述

用户管理模块提供以下核心功能：

- 用户信息的CRUD操作
- 用户角色分配
- 用户状态管理
- 用户密码重置
- 用户导入导出
- 用户数据权限控制

## 页面布局

用户管理页面主要分为以下几个部分：

1. **搜索区域**：提供多条件组合查询功能
2. **操作按钮区**：包含新增、删除、导入、导出等功能按钮
3. **数据表格**：展示用户列表数据
4. **操作列**：提供编辑、删除、分配角色等行操作按钮

## 使用指南

### 用户查询

1. 进入系统管理 -> 用户管理页面
2. 在搜索区域，可以根据以下条件进行查询：
   - 用户名
   - 手机号
   - 状态
   - 部门
   - 创建时间
3. 点击"搜索"按钮执行查询
4. 点击"重置"按钮清除搜索条件
5. 点击"折叠"按钮可以展开/收起搜索区域

### 新增用户

1. 点击"新增"按钮
2. 在弹出的表单中填写用户信息：
   - 用户名（必填）：登录系统的账号
   - 用户昵称（必填）：显示名称
   - 密码（必填）：初始密码
   - 确认密码（必填）：与密码保持一致
   - 手机号（必填）：用于接收通知和找回密码
   - 邮箱：用于接收通知
   - 性别：用户性别
   - 部门（必填）：所属部门
   - 岗位：所属岗位，可多选
   - 角色：用户角色，可多选
   - 状态：启用/禁用
   - 备注：其他说明信息
3. 点击"确定"按钮保存用户信息

### 编辑用户

1. 在用户列表中，点击对应行的"编辑"按钮
2. 在弹出的表单中修改用户信息（密码字段不可修改）
3. 点击"确定"按钮保存修改

### 删除用户

1. 单个删除：在用户列表中，点击对应行的"删除"按钮
2. 批量删除：选中多个用户，点击表格上方的"删除"按钮
3. 在弹出的确认对话框中点击"确定"完成删除操作

### 分配角色

1. 在用户列表中，点击对应行的"分配角色"按钮
2. 在弹出的对话框中，选择要分配给用户的角色（可多选）
3. 点击"确定"按钮保存角色分配

### 重置密码

1. 在用户列表中，点击对应行的"重置密码"按钮
2. 在弹出的对话框中输入新密码和确认密码
3. 点击"确定"按钮完成密码重置

### 用户导入

1. 点击"导入"按钮
2. 在弹出的对话框中，可以下载导入模板
3. 按照模板格式填写用户信息
4. 点击"选择文件"按钮，选择填写好的Excel文件
5. 点击"开始导入"按钮执行导入操作

### 用户导出

1. 点击"导出"按钮
2. 在弹出的对话框中，选择要导出的字段
3. 点击"确定"按钮，系统会生成Excel文件并自动下载

## 数据结构

用户管理模块主要涉及以下数据表：

### sys_user（用户表）

| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| user_id | bigint | 用户ID，主键 |
| username | varchar(30) | 用户名，唯一 |
| nickname | varchar(30) | 用户昵称 |
| password | varchar(100) | 密码（加密存储） |
| dept_id | bigint | 部门ID，外键 |
| email | varchar(50) | 邮箱 |
| mobile | varchar(11) | 手机号 |
| sex | char(1) | 性别（0男 1女 2未知） |
| avatar | varchar(100) | 头像路径 |
| status | char(1) | 状态（0正常 1停用） |
| del_flag | char(1) | 删除标志（0存在 1删除） |
| create_by | varchar(64) | 创建者 |
| create_time | datetime | 创建时间 |
| update_by | varchar(64) | 更新者 |
| update_time | datetime | 更新时间 |
| remark | varchar(500) | 备注 |

### sys_user_role（用户角色关联表）

| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| user_id | bigint | 用户ID，联合主键 |
| role_id | bigint | 角色ID，联合主键 |

### sys_user_post（用户岗位关联表）

| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| user_id | bigint | 用户ID，联合主键 |
| post_id | bigint | 岗位ID，联合主键 |

## API接口

### 用户列表查询

- **请求方式**：GET
- **接口地址**：`/system/user/list`
- **请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| username | String | 否 | 用户名 |
| mobile | String | 否 | 手机号 |
| status | String | 否 | 状态（0正常 1停用） |
| deptId | Long | 否 | 部门ID |
| beginTime | String | 否 | 开始时间 |
| endTime | String | 否 | 结束时间 |
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页记录数 |

- **响应示例**：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "total": 100,
    "list": [
      {
        "userId": 1,
        "username": "admin",
        "nickname": "管理员",
        "deptName": "总公司",
        "email": "admin@example.com",
        "mobile": "13800138000",
        "sex": "0",
        "status": "0",
        "createTime": "2023-01-01 00:00:00",
        "roles": [
          {
            "roleId": 1,
            "roleName": "超级管理员"
          }
        ]
      }
    ]
  }
}
```

### 新增用户

- **请求方式**：POST
- **接口地址**：`/system/user`
- **请求参数**：

```json
{
  "username": "testuser",
  "nickname": "测试用户",
  "password": "123456",
  "deptId": 100,
  "email": "test@example.com",
  "mobile": "13800138001",
  "sex": "0",
  "status": "0",
  "postIds": [1, 2],
  "roleIds": [2]
}
```

- **响应示例**：

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 修改用户

- **请求方式**：PUT
- **接口地址**：`/system/user`
- **请求参数**：

```json
{
  "userId": 2,
  "username": "testuser",
  "nickname": "测试用户修改",
  "deptId": 100,
  "email": "test@example.com",
  "mobile": "13800138001",
  "sex": "0",
  "status": "0",
  "postIds": [1, 2],
  "roleIds": [2]
}
```

- **响应示例**：

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 删除用户

- **请求方式**：DELETE
- **接口地址**：`/system/user/{userIds}`
- **请求参数**：
  - userIds: 用户ID，多个用户ID用逗号分隔

- **响应示例**：

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 重置密码

- **请求方式**：PUT
- **接口地址**：`/system/user/resetPwd`
- **请求参数**：

```json
{
  "userId": 2,
  "password": "123456"
}
```

- **响应示例**：

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 分配角色

- **请求方式**：PUT
- **接口地址**：`/system/user/authRole`
- **请求参数**：

```json
{
  "userId": 2,
  "roleIds": [2, 3]
}
```

- **响应示例**：

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

## 前端实现

用户管理前端页面位于`src/views/manage/user/index.vue`，主要包含以下组件：

- **用户列表**：使用`a-table`组件展示用户数据
- **搜索表单**：使用`a-form`组件实现条件搜索
- **用户表单**：使用`a-drawer`组件实现用户新增/编辑表单
- **角色分配**：使用`a-modal`和`a-checkbox-group`实现角色分配

### 代码示例

```vue
<template>
  <div class="user-manage">
    <!-- 搜索区域 -->
    <a-card
      :bordered="false"
      :body-style="collapsed ? { padding: '0', display: 'none' } : {}"
      :data-collapsed="collapsed"
    >
      <template #title>
        <a-space>
          <search-outlined />
          <span>筛选搜索</span>
        </a-space>
      </template>
      <template #extra>
        <a-space>
          <a-button @click="handleSearch">
            <template #icon>
              <search-outlined />
            </template>
            搜索
          </a-button>
          <a-button @click="resetSearch">
            <template #icon>
              <reload-outlined />
            </template>
            重置
          </a-button>
          <a-button @click="toggleCollapse">
            <template #icon>
              <component :is="collapsed ? 'down-outlined' : 'up-outlined'" />
            </template>
            {{ collapsed ? '展开' : '折叠' }}
          </a-button>
        </a-space>
      </template>
      
      <!-- 搜索表单 -->
      <a-form
        ref="searchFormRef"
        :model="searchForm"
        layout="horizontal"
      >
        <!-- 表单项省略 -->
      </a-form>
    </a-card>
    
    <!-- 操作按钮和表格 -->
    <a-card :bordered="false" class="mt-4">
      <template #title>
        <a-space>
          <user-outlined />
          <span>用户列表</span>
        </a-space>
      </template>
      <template #extra>
        <a-space>
          <a-button type="primary" @click="handleAdd" v-permission="'system:user:add'">
            <template #icon>
              <plus-outlined />
            </template>
            新增
          </a-button>
          <!-- 其他按钮省略 -->
        </a-space>
      </template>
      
      <!-- 用户表格 -->
      <a-table
        :columns="columns"
        :data-source="userList"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
        row-key="userId"
      >
        <!-- 表格列配置省略 -->
      </a-table>
    </a-card>
    
    <!-- 用户表单抽屉 -->
    <user-operate-drawer
      v-model:visible="drawerVisible"
      :title="formTitle"
      :form-data="formData"
      @success="handleSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { getUserList, deleteUser } from '@/service/api/user';
import UserOperateDrawer from './modules/user-operate-drawer.vue';
import { usePermission } from '@/hooks/business/permission';

// 组件逻辑省略
</script>

<style lang="scss" scoped>
.user-manage {
  [data-collapsed="true"] {
    :deep(.ant-card-head) {
      border-bottom: 0;
    }
  }
}
</style>
```

## 最佳实践

### 用户管理注意事项

1. **密码安全**
   - 密码必须加密存储，推荐使用BCrypt算法
   - 密码长度至少8位，包含字母、数字和特殊字符
   - 定期提醒用户修改密码

2. **数据权限控制**
   - 普通用户只能查看自己的信息
   - 部门管理员只能管理本部门及下级部门的用户
   - 超级管理员可以管理所有用户

3. **敏感操作保护**
   - 删除用户前需二次确认
   - 批量操作需要提示操作影响范围
   - 重要操作需记录操作日志

4. **性能优化**
   - 用户列表分页查询
   - 合理使用缓存减少数据库压力
   - 大数据量导入导出使用异步处理

## 常见问题

### Q: 如何批量导入用户？

**解决方案**：
1. 下载系统提供的Excel导入模板
2. 按照模板格式填写用户信息（必填字段不能为空）
3. 使用系统的导入功能上传Excel文件
4. 系统会自动处理数据并反馈导入结果

### Q: 为什么无法删除某些用户？

**解决方案**：
1. 检查该用户是否为系统内置用户（如admin）
2. 确认当前登录用户是否有删除该用户的权限
3. 检查该用户是否与系统中的重要数据关联

### Q: 如何修改用户密码？

**解决方案**：
1. 管理员可以通过用户列表中的"重置密码"功能为用户重置密码
2. 用户可以在个人中心页面修改自己的密码
3. 忘记密码可以通过手机号验证码或邮箱找回密码 