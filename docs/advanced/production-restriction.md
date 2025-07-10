# 生产环境操作限制

Mars Admin提供了生产环境操作限制功能，可以在生产环境下统一拦截所有增删改操作，防止误操作对生产数据造成影响，非常适合演示环境使用。

## 功能特性

- 🚫 **统一拦截**：自动拦截所有POST、PUT、DELETE、PATCH请求
- 🎯 **环境识别**：只在生产环境（prod）下生效，开发环境不受影响
- ⚙️ **可配置**：支持开启/关闭，可自定义提示信息
- 📋 **白名单**：支持配置白名单路径，允许特定操作
- 🔒 **安全友好**：返回403状态码和友好的错误提示

## 配置说明

### 1. 启用生产环境限制

在生产环境配置文件 `application-prod.yml` 中添加以下配置：

```yaml
# Mars Admin 生产环境配置
mars:
  production:
    read-only:
      enabled: true  # 启用生产环境只读模式
      message: "演示环境，不允许进行增删改操作，如需测试请联系管理员"
```

### 2. 配置项说明

| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `mars.production.read-only.enabled` | boolean | false | 是否启用生产环境只读模式 |
| `mars.production.read-only.message` | string | 演示环境，不允许进行增删改操作 | 拦截时返回的提示信息 |

### 3. 环境配置

确保生产环境的Spring配置文件激活了`prod`环境：

```yaml
spring:
  profiles:
    active: prod
```

## 拦截规则

### 拦截的HTTP方法

- POST（新增操作）
- PUT（更新操作）
- DELETE（删除操作）
- PATCH（部分更新操作）

### 不拦截的HTTP方法

- GET（查询操作）
- HEAD（头部信息）
- OPTIONS（选项查询）

### 白名单路径

以下路径即使在生产环境也会被允许：

- `/auth/login` - 用户登录
- `/auth/logout` - 用户登出
- `/auth/refresh` - 刷新Token
- `/auth/captcha` - 获取验证码
- `/file/download` - 文件下载
- `/export` - 数据导出（包含此关键字的路径）
- `/health` - 健康检查
- `/actuator` - 监控端点

## 返回格式

当操作被拦截时，会返回以下格式的JSON响应：

```json
{
  "code": 403,
  "message": "演示环境，不允许进行增删改操作，如需测试请联系管理员",
  "data": null,
  "success": false,
  "timestamp": "2025-01-08T16:30:00"
}
```

## 使用场景

### 1. 演示环境

在向客户或团队演示系统功能时，启用此功能可以：
- 防止误操作删除重要演示数据
- 保持演示环境数据的稳定性
- 提供友好的操作提示

### 2. 生产环境保护

在生产环境中临时启用此功能可以：
- 在系统维护期间防止数据变更
- 在数据迁移过程中保护数据完整性
- 在问题排查期间避免二次污染

### 3. 只读访问

为外部用户或第三方提供只读访问权限：
- 允许查看数据但不能修改
- 保护核心业务数据安全
- 满足合规性要求

## 开发指南

### 自定义白名单

如果需要添加自定义的白名单路径，可以修改 `ProductionReadOnlyInterceptor` 类：

```java
private static final List<String> WHITELIST_PATHS = Arrays.asList(
    "/auth/login",
    "/auth/logout", 
    "/auth/refresh",
    "/auth/captcha",
    "/file/download",
    "/export",
    "/health",
    "/actuator",
    "/custom/allowed/path"  // 添加自定义路径
);
```

### 自定义拦截逻辑

可以重写 `preHandle` 方法来实现更复杂的拦截逻辑：

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 自定义拦截逻辑
    // 例如：基于用户角色、IP地址、时间等条件进行拦截
    return super.preHandle(request, response, handler);
}
```

## 注意事项

1. **环境识别**：功能只在`spring.profiles.active=prod`时生效
2. **配置优先级**：必须同时设置环境为prod且enabled为true才会拦截
3. **白名单优先**：白名单路径不受拦截影响
4. **日志记录**：所有被拦截的操作都会记录WARN级别日志
5. **性能影响**：拦截器会对每个请求进行检查，但性能影响极小

## 测试验证

### 1. 功能测试

```bash
# 启动应用时指定生产环境
java -jar mars-admin.jar --spring.profiles.active=prod

# 测试查询操作（应该成功）
curl -X GET http://localhost:8080/system/user/list

# 测试新增操作（应该被拦截）
curl -X POST http://localhost:8080/system/user \
  -H "Content-Type: application/json" \
  -d '{"username":"test","nickname":"测试用户"}'
```

### 2. 单元测试

项目包含完整的单元测试，可以运行以下命令验证功能：

```bash
mvn test -Dtest=ProductionReadOnlyInterceptorTest
```

## 常见问题

### Q: 为什么配置了但没有生效？
A: 请检查以下几点：
- 确认`spring.profiles.active=prod`
- 确认`mars.production.read-only.enabled=true`
- 确认请求路径不在白名单中

### Q: 如何临时关闭限制？
A: 有两种方式：
1. 设置`mars.production.read-only.enabled=false`
2. 修改`spring.profiles.active`为非prod环境

### Q: 可以针对特定用户放行吗？
A: 当前版本不支持，如有需要可以自定义拦截器逻辑实现

### Q: 导出功能为什么不受限制？
A: 导出通常是只读操作，不会修改数据，因此被加入了白名单

## 更新日志

- **v1.0.0** (2025-01-08)
  - 初始版本发布
  - 支持基本的增删改操作拦截
  - 支持白名单配置
  - 支持自定义提示信息 