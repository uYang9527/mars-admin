# API文档

Mars Admin提供完整的RESTful API接口，支持前端、移动端等多端调用。

## API概览

### 基础信息

- **Base URL**: `http://localhost:8080`
- **API版本**: v1.0
- **数据格式**: JSON
- **字符编码**: UTF-8
- **认证方式**: Bearer Token (Sa-Token)

### 统一响应格式

所有API接口都遵循统一的响应格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2025-01-14T10:30:00"
}
```

**响应字段说明：**
- `code`: 响应状态码，200表示成功
- `message`: 响应消息
- `data`: 响应数据，可能是对象、数组或基本类型
- `timestamp`: 响应时间戳

### 状态码说明

| 状态码 | 说明 | 描述 |
|--------|------|------|
| 200 | 成功 | 请求成功 |
| 400 | 请求错误 | 请求参数错误 |
| 401 | 未授权 | 未登录或Token过期 |
| 403 | 禁止访问 | 权限不足 |
| 404 | 未找到 | 资源不存在 |
| 500 | 服务器错误 | 内部服务器错误 |

## 认证相关API

### 1. 用户登录

**接口地址：** `POST /auth/login`

**请求参数：**
```json
{
  "username": "admin",
  "password": "123456",
  "loginType": "PC"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 3600
  }
}
```

### 2. 获取用户信息

**接口地址：** `GET /auth/userinfo`

**请求头：**
```
Authorization: Bearer {token}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "email": "admin@example.com",
    "phone": "13800138000",
    "avatar": "/avatar/admin.jpg",
    "roles": [
      {
        "id": 1,
        "roleName": "超级管理员",
        "roleKey": "admin"
      }
    ],
    "permissions": [
      "system:user:list",
      "system:user:add",
      "system:user:edit",
      "system:user:delete"
    ]
  }
}
```

### 3. 退出登录

**接口地址：** `POST /auth/logout`

**请求头：**
```
Authorization: Bearer {token}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "退出成功",
  "data": null
}
```

### 4. 修改密码

**接口地址：** `POST /auth/changePassword`

**请求参数：**
```json
{
  "oldPassword": "123456",
  "newPassword": "newPassword123"
}
```

## 用户管理API

### 1. 获取用户列表

**接口地址：** `GET /system/user/list`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| current | int | 否 | 当前页码，默认1 |
| size | int | 否 | 每页大小，默认10 |
| username | string | 否 | 用户名 |
| nickname | string | 否 | 昵称 |
| phone | string | 否 | 手机号 |
| status | int | 否 | 状态（0停用 1正常） |

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "admin",
        "nickname": "管理员",
        "email": "admin@example.com",
        "phone": "13800138000",
        "gender": 0,
        "status": 1,
        "createTime": "2025-01-01T00:00:00",
        "deptName": "总公司",
        "roles": ["超级管理员"]
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 2. 获取用户详情

**接口地址：** `GET /system/user/{id}`

**路径参数：**
- `id`: 用户ID

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "email": "admin@example.com",
    "phone": "13800138000",
    "gender": 0,
    "avatar": "/avatar/admin.jpg",
    "status": 1,
    "remark": "系统管理员",
    "deptId": 1,
    "postIds": [1],
    "roleIds": [1]
  }
}
```

### 3. 新增用户

**接口地址：** `POST /system/user`

**请求参数：**
```json
{
  "username": "testuser",
  "nickname": "测试用户",
  "password": "123456",
  "email": "test@example.com",
  "phone": "13800138001",
  "gender": 0,
  "status": 1,
  "deptId": 1,
  "postIds": [1],
  "roleIds": [2],
  "remark": "测试用户"
}
```

### 4. 修改用户

**接口地址：** `PUT /system/user`

**请求参数：**
```json
{
  "id": 2,
  "username": "testuser",
  "nickname": "测试用户",
  "email": "test@example.com",
  "phone": "13800138001",
  "gender": 0,
  "status": 1,
  "deptId": 1,
  "postIds": [1],
  "roleIds": [2],
  "remark": "测试用户"
}
```

### 5. 删除用户

**接口地址：** `DELETE /system/user/{ids}`

**路径参数：**
- `ids`: 用户ID，多个ID用逗号分隔

## 角色管理API

### 1. 获取角色列表

**接口地址：** `GET /system/role/list`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| current | int | 否 | 当前页码 |
| size | int | 否 | 每页大小 |
| roleName | string | 否 | 角色名称 |
| roleKey | string | 否 | 角色权限字符串 |
| status | int | 否 | 状态 |

### 2. 获取角色详情

**接口地址：** `GET /system/role/{id}`

### 3. 新增角色

**接口地址：** `POST /system/role`

**请求参数：**
```json
{
  "roleName": "测试角色",
  "roleKey": "test",
  "roleSort": 3,
  "status": 1,
  "menuIds": [1, 2, 3],
  "deptIds": [1],
  "remark": "测试角色"
}
```

### 4. 修改角色

**接口地址：** `PUT /system/role`

### 5. 删除角色

**接口地址：** `DELETE /system/role/{ids}`

## 菜单管理API

### 1. 获取菜单列表

**接口地址：** `GET /system/menu/list`

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "menuName": "系统管理",
      "parentId": 0,
      "orderNum": 1,
      "path": "/system",
      "component": "",
      "menuType": "M",
      "visible": "0",
      "status": "0",
      "perms": "",
      "icon": "system",
      "children": [
        {
          "id": 100,
          "menuName": "用户管理",
          "parentId": 1,
          "orderNum": 1,
          "path": "user",
          "component": "system/user/index",
          "menuType": "C",
          "visible": "0",
          "status": "0",
          "perms": "system:user:list",
          "icon": "user"
        }
      ]
    }
  ]
}
```

### 2. 获取路由菜单

**接口地址：** `GET /system/menu/getRouters`

**说明：** 获取当前用户的路由菜单，用于前端动态路由

## 部门管理API

### 1. 获取部门列表

**接口地址：** `GET /system/dept/list`

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "parentId": 0,
      "ancestors": "0",
      "deptName": "总公司",
      "orderNum": 0,
      "leader": "张三",
      "phone": "13800138000",
      "email": "admin@example.com",
      "status": "0",
      "children": [
        {
          "id": 2,
          "parentId": 1,
          "ancestors": "0,1",
          "deptName": "技术部",
          "orderNum": 1,
          "leader": "李四",
          "phone": "13800138001",
          "email": "tech@example.com",
          "status": "0"
        }
      ]
    }
  ]
}
```

### 2. 获取部门树

**接口地址：** `GET /system/dept/treeselect`

**说明：** 获取部门树形结构，用于下拉选择

## 字典管理API

### 1. 获取字典类型列表

**接口地址：** `GET /system/dict/type/list`

### 2. 获取字典数据列表

**接口地址：** `GET /system/dict/data/list`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| dictType | string | 是 | 字典类型 |

### 3. 根据字典类型获取字典数据

**接口地址：** `GET /system/dict/data/type/{dictType}`

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "dictLabel": "男",
      "dictValue": "0",
      "dictType": "sys_user_sex",
      "cssClass": "",
      "listClass": "default",
      "isDefault": "Y",
      "status": "0"
    },
    {
      "dictLabel": "女",
      "dictValue": "1",
      "dictType": "sys_user_sex",
      "cssClass": "",
      "listClass": "default",
      "isDefault": "N",
      "status": "0"
    }
  ]
}
```

## 文件上传API

### 1. 文件上传

**接口地址：** `POST /system/oss/upload`

**请求类型：** `multipart/form-data`

**请求参数：**
- `file`: 文件对象

**响应示例：**
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "id": 1,
    "fileName": "avatar.jpg",
    "originalName": "用户头像.jpg",
    "fileSuffix": "jpg",
    "url": "http://localhost:9000/mars-admin/2025/01/14/avatar_20250114103000.jpg",
    "service": "minio"
  }
}
```

### 2. 文件列表

**接口地址：** `GET /system/oss/list`

### 3. 文件删除

**接口地址：** `DELETE /system/oss/{ids}`

## 系统监控API

### 1. 获取服务器信息

**接口地址：** `GET /monitor/server`

**响应示例：**
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "cpu": {
      "cpuNum": 8,
      "used": 15.6,
      "sys": 8.2,
      "free": 84.4
    },
    "mem": {
      "total": 16.0,
      "used": 8.5,
      "free": 7.5,
      "usage": 53.1
    },
    "jvm": {
      "total": 512,
      "max": 512,
      "used": 256,
      "free": 256,
      "usage": 50.0,
      "version": "17.0.1"
    },
    "sys": {
      "computerName": "MARS-SERVER",
      "computerIp": "192.168.1.100",
      "osName": "Windows 10",
      "osArch": "amd64"
    }
  }
}
```

### 2. 获取缓存信息

**接口地址：** `GET /monitor/cache`

### 3. SQL监控

**接口地址：** `GET /monitor/sql/recent`

**请求参数：**
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limit | int | 否 | 限制数量，默认100 |
| keyword | string | 否 | 关键字搜索 |
| minTime | long | 否 | 最小执行时间(ms) |

## 日志管理API

### 1. 操作日志列表

**接口地址：** `GET /monitor/operlog/list`

### 2. 登录日志列表

**接口地址：** `GET /monitor/logininfor/list`

## 错误处理

### 常见错误码

| 错误码 | 错误信息 | 解决方案 |
|--------|----------|----------|
| 401 | Token已过期 | 重新登录获取新Token |
| 403 | 权限不足 | 联系管理员分配权限 |
| 400 | 参数校验失败 | 检查请求参数格式 |
| 500 | 系统内部错误 | 联系技术支持 |

### 错误响应示例

```json
{
  "code": 400,
  "message": "用户名不能为空",
  "data": null,
  "timestamp": "2025-01-14T10:30:00"
}
```

## API调用示例

### JavaScript (Axios)

```javascript
// 设置请求拦截器
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 登录
const login = async (username, password) => {
  const response = await axios.post('/auth/login', {
    username,
    password,
    loginType: 'PC'
  });
  return response.data;
};

// 获取用户列表
const getUserList = async (params) => {
  const response = await axios.get('/system/user/list', { params });
  return response.data;
};
```

### Java (OkHttp)

```java
// 登录
public String login(String username, String password) {
    OkHttpClient client = new OkHttpClient();
    
    String json = String.format(
        "{\"username\":\"%s\",\"password\":\"%s\",\"loginType\":\"PC\"}", 
        username, password
    );
    
    RequestBody body = RequestBody.create(
        MediaType.parse("application/json"), json
    );
    
    Request request = new Request.Builder()
        .url("http://localhost:8080/auth/login")
        .post(body)
        .build();
    
    try (Response response = client.newCall(request).execute()) {
        return response.body().string();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```

## 接口测试

### 使用Postman测试

1. **导入API文档**
   - 访问 `http://localhost:8080/doc.html`
   - 导出OpenAPI 3.0格式文档
   - 在Postman中导入文档

2. **设置环境变量**
   ```
   baseUrl: http://localhost:8080
   token: {{登录后获取的token}}
   ```

3. **测试流程**
   - 先调用登录接口获取token
   - 设置Authorization头部
   - 测试其他接口

### 使用curl测试

```bash
# 登录
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456","loginType":"PC"}'

# 获取用户信息
curl -X GET http://localhost:8080/auth/userinfo \
  -H "Authorization: Bearer {token}"
```

## 版本更新

### v1.0.0 (2025-01-14)
- 初始版本发布
- 完整的用户权限管理API
- 系统监控API
- 文件上传API

### 后续版本计划
- 增加批量操作API
- 优化响应性能
- 增加API限流
- 支持GraphQL

## 技术支持

如有API使用问题，请：
1. 查看在线API文档：`http://localhost:8080/doc.html`
2. 提交Issue到GitHub仓库
3. 联系技术支持：support@marsadmin.cn
