# 操作日志注解使用指南

Mars Admin 系统采用注解 + AOP 的方式来实现操作日志的自动记录，简化了日志记录的代码，提高了开发效率。

## 📋 系统架构

### 核心组件

1. **@OperationLog 注解** - 标记需要记录日志的方法
2. **OperationLogAspect 切面** - AOP 切面处理器，自动拦截并记录日志
3. **BusinessType 枚举** - 业务操作类型定义
4. **SysOperLog 实体** - 操作日志数据实体

### 工作流程

```
Controller方法 
    ↓ (标注@OperationLog注解)
AOP切面拦截 
    ↓ (解析注解参数)
收集操作信息 
    ↓ (请求参数、返回结果、用户信息等)
保存到数据库
```

## 🔧 注解参数说明

### @OperationLog 注解属性

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| title | String | "" | 模块标题，如"用户管理"、"角色管理" |
| businessType | BusinessType | OTHER | 业务操作类型 |
| operatorType | OperatorType | MANAGE | 操作人类别 |
| isSaveRequestData | boolean | true | 是否保存请求参数 |
| isSaveResponseData | boolean | true | 是否保存响应结果 |
| excludeParamNames | String[] | {} | 排除的请求参数名 |
| description | String | "" | 操作描述，支持SpEL表达式 |

### BusinessType 业务类型

```java
public enum BusinessType {
    OTHER,      // 其它
    INSERT,     // 新增
    UPDATE,     // 修改
    DELETE,     // 删除
    GRANT,      // 授权
    EXPORT,     // 导出
    IMPORT,     // 导入
    FORCE,      // 强退
    GENCODE,    // 生成代码
    CLEAN       // 清空数据
}
```

### OperatorType 操作人类别

```java
public enum OperatorType {
    OTHER,      // 其它
    MANAGE,     // 后台用户
    MOBILE      // 手机端用户
}
```

## 💡 使用示例

### 1. 基础用法

```java
@PostMapping
@OperationLog(title = "用户管理", businessType = BusinessType.INSERT)
public Result<Boolean> save(@RequestBody SysUser user) {
    boolean success = sysUserService.insertUser(user);
    return Result.of(success, "新增成功", "新增失败");
}
```

### 2. 使用SpEL表达式

```java
@PutMapping("/resetPwd")
@OperationLog(
    title = "用户管理", 
    businessType = BusinessType.UPDATE,
    description = "重置用户密码，用户名：#{#user.username}，用户ID：#{#user.id}"
)
public Result<Void> resetPwd(@RequestBody SysUser user) {
    boolean result = sysUserService.resetPassword(user.getId(), user.getPassword());
    return result ? Result.success() : Result.error("重置密码失败");
}
```

### 3. 排除敏感参数

```java
@PostMapping("/login")
@OperationLog(
    title = "用户认证",
    businessType = BusinessType.OTHER,
    excludeParamNames = {"password"}, // 排除密码参数
    description = "用户登录：#{#request.username}"
)
public Result<LoginResponse> login(@RequestBody LoginRequest request) {
    // 登录逻辑
}
```

### 4. 不保存响应数据

```java
@GetMapping("/export")
@OperationLog(
    title = "用户管理",
    businessType = BusinessType.EXPORT,
    isSaveResponseData = false, // 导出数据量大，不保存响应
    description = "导出用户数据"
)
public void exportUsers(HttpServletResponse response) {
    // 导出逻辑
}
```

### 5. 复杂的SpEL表达式

```java
@DeleteMapping("/batch")
@OperationLog(
    title = "用户管理",
    businessType = BusinessType.DELETE,
    description = "批量删除用户，删除数量：#{#ids.size()}，ID列表：#{#ids}"
)
public Result<Boolean> deleteBatch(@RequestBody List<Long> ids) {
    boolean success = sysUserService.deleteUsers(ids.toArray(new Long[0]));
    return Result.of(success, "批量删除成功", "批量删除失败");
}
```

## 🎯 SpEL表达式支持

### 可用变量

| 变量名 | 说明 | 示例 |
|--------|------|------|
| #参数名 | 方法参数 | `#{#user.username}` |
| #result | 方法返回结果 | `#{#result.data.id}` |
| #currentUser | 当前登录用户 | `#{#currentUser.username}` |

### 常用表达式

```java
// 获取参数属性
"用户名：#{#user.username}"

// 获取集合大小
"删除数量：#{#ids.size()}"

// 条件表达式
"状态：#{#user.status == 1 ? '启用' : '禁用'}"

// 获取返回结果
"操作结果：#{#result.success ? '成功' : '失败'}"

// 组合表达式
"用户#{#user.username}#{#user.status == 1 ? '启用' : '禁用'}了账号"
```

## 🔒 自动收集的信息

系统会自动收集以下信息：

- **用户信息**: 操作人用户名、部门名称
- **请求信息**: IP地址、请求URL、请求方法、请求参数
- **方法信息**: 调用的类名和方法名
- **时间信息**: 操作时间、执行耗时
- **状态信息**: 成功/失败状态、错误信息

## ⚙️ 配置选项

### 在 application.yml 中配置

```yaml
# 操作日志配置
operation-log:
  # 是否启用操作日志
  enabled: true
  # 最大参数长度（字符数）
  max-param-length: 2000
  # 最大返回结果长度（字符数）
  max-result-length: 2000
  # 是否记录异常堆栈
  save-exception-stack: false
```

## 🎨 最佳实践

### 1. 注解使用规范

```java
// ✅ 推荐：明确的标题和业务类型
@OperationLog(title = "用户管理", businessType = BusinessType.INSERT)

// ❌ 不推荐：标题不明确
@OperationLog(title = "操作", businessType = BusinessType.OTHER)
```

### 2. SpEL表达式规范

```java
// ✅ 推荐：描述性强，信息完整
@OperationLog(
    title = "用户管理",
    businessType = BusinessType.UPDATE,
    description = "修改用户#{#user.username}的状态为#{#user.status == 1 ? '启用' : '禁用'}"
)

// ❌ 不推荐：信息不足
@OperationLog(
    title = "用户管理",
    businessType = BusinessType.UPDATE,
    description = "修改用户"
)
```

### 3. 敏感数据处理

```java
// ✅ 推荐：排除敏感参数
@OperationLog(
    title = "用户管理",
    businessType = BusinessType.UPDATE,
    excludeParamNames = {"password", "oldPassword", "newPassword"}
)

// ✅ 推荐：大数据量操作不保存响应
@OperationLog(
    title = "数据导出",
    businessType = BusinessType.EXPORT,
    isSaveResponseData = false
)
```

## 🚀 高级功能

### 1. 自定义切面

如果需要特殊的日志处理逻辑，可以继承 `OperationLogAspect`：

```java
@Aspect
@Component
public class CustomOperationLogAspect extends OperationLogAspect {
    
    @Override
    protected void handleLog(JoinPoint joinPoint, OperationLog operationLog, 
                           HttpServletRequest request, Object result, Throwable e) {
        // 自定义处理逻辑
        super.handleLog(joinPoint, operationLog, request, result, e);
        
        // 额外的处理，如发送通知等
        sendNotification(operationLog.title(), e != null);
    }
}
```

### 2. 条件记录

可以通过自定义注解实现条件记录：

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionalLog {
    String condition() default ""; // SpEL条件表达式
}
```

### 3. 异步记录

对于高并发场景，可以配置异步记录：

```java
@EnableAsync
@Configuration
public class LogConfig {
    
    @Bean
    public TaskExecutor logTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("log-");
        executor.initialize();
        return executor;
    }
}
```

## 📊 日志查询和分析

### 查看操作日志

1. **管理界面**: 系统管理 > 操作日志
2. **支持筛选**: 按操作人、操作时间、业务类型、状态等
3. **详情查看**: 可查看请求参数、返回结果、错误信息等

### 统计分析

```sql
-- 按用户统计操作次数
SELECT oper_name, COUNT(*) as count 
FROM sys_oper_log 
GROUP BY oper_name 
ORDER BY count DESC;

-- 按业务类型统计
SELECT business_type, COUNT(*) as count 
FROM sys_oper_log 
GROUP BY business_type;

-- 错误操作统计
SELECT title, COUNT(*) as error_count 
FROM sys_oper_log 
WHERE status = 1 
GROUP BY title 
ORDER BY error_count DESC;
```

## 🐛 故障排除

### 常见问题

1. **注解不生效**
   - 检查是否添加了 `@EnableAspectJAutoProxy`
   - 确认方法是 public 且不是 final
   - 验证切面类是否被 Spring 管理

2. **SpEL表达式解析失败**
   - 检查参数名是否正确
   - 确认表达式语法正确
   - 查看日志中的警告信息

3. **性能问题**
   - 减少保存的数据量
   - 考虑异步记录
   - 定期清理历史日志

### 调试技巧

```java
// 开启调试日志
logging:
  level:
    com.mars.admin.framework.aspect: DEBUG
```

## 📝 升级指南

### 从手动记录迁移到注解

```java
// 旧方式 - 手动记录
public Result<Boolean> save(@RequestBody SysUser user) {
    try {
        boolean success = sysUserService.insertUser(user);
        
        // 手动记录日志
        operLogUtils.recordLog("用户管理", BusinessType.INSERT, 
                              "新增用户：" + user.getUsername());
        
        return Result.of(success, "新增成功", "新增失败");
    } catch (Exception e) {
        operLogUtils.recordErrorLog("用户管理", BusinessType.INSERT, 
                                   "新增用户失败", e);
        throw e;
    }
}

// 新方式 - 注解记录
@OperationLog(title = "用户管理", businessType = BusinessType.INSERT,
              description = "新增用户：#{#user.username}")
public Result<Boolean> save(@RequestBody SysUser user) {
    boolean success = sysUserService.insertUser(user);
    return Result.of(success, "新增成功", "新增失败");
}
```

通过使用注解方式，代码更加简洁，日志记录更加统一和标准化！ 