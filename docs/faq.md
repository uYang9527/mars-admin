# 常见问题

本页面收集了使用Mars Admin过程中的常见问题和解决方案。

## 安装部署问题

### Q: 如何解决MySQL连接错误？

**问题描述**：启动项目时出现MySQL连接错误，如`Communications link failure`或`Access denied for user`。

**解决方案**：
1. 检查MySQL服务是否正常运行
2. 验证数据库连接信息是否正确（用户名、密码、数据库名）
3. 确认MySQL用户是否有正确的访问权限
4. 检查防火墙设置是否阻止了MySQL连接（默认端口3306）

```yaml
# 正确的数据库配置示例
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mars_admin?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
    username: root
    password: yourpassword
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### Q: 项目启动时提示端口被占用怎么办？

**问题描述**：启动项目时提示`Web server failed to start. Port 8080 was already in use.`

**解决方案**：
1. 找出占用端口的进程：
   ```bash
   # Windows
   netstat -ano | findstr 8080
   
   # Linux/Mac
   lsof -i:8080
   ```

2. 关闭占用端口的进程或修改项目端口：
   ```yaml
   # application.yml
   server:
     port: 8081  # 修改为其他未被占用的端口
   ```

### Q: 如何解决前端依赖安装失败的问题？

**问题描述**：运行`pnpm install`时出现依赖安装错误。

**解决方案**：
1. 清除缓存后重试：
   ```bash
   pnpm store prune
   pnpm install
   ```

2. 检查Node.js版本是否符合要求（18+）：
   ```bash
   node -v
   ```

3. 尝试使用国内镜像源：
   ```bash
   pnpm config set registry https://registry.npmmirror.com
   ```

4. 使用兼容模式安装：
   ```bash
   pnpm install --no-strict-peer-dependencies
   ```

## 登录认证问题

### Q: 登录时提示"用户名或密码错误"怎么办？

**问题描述**：明明输入了正确的用户名和密码，却提示"用户名或密码错误"。

**解决方案**：
1. 默认管理员账号为：用户名`admin`，密码`123456`
2. 检查数据库中的用户表`sys_user`，确认用户状态是否为正常
3. 密码是加密存储的，不能直接在数据库中修改，请使用"重置密码"功能
4. 检查是否开启了验证码功能，验证码输入是否正确

### Q: 登录后立即被踢出或提示未授权怎么办？

**问题描述**：成功登录后，页面立即跳转回登录页或提示"未授权"。

**解决方案**：
1. 检查Redis连接是否正常，Sa-Token默认使用Redis存储会话
2. 检查用户是否被分配了角色和权限
3. 检查浏览器控制台是否有API请求错误
4. 清除浏览器缓存和Cookie后重试

### Q: 如何实现单点登录？

**问题描述**：如何配置系统实现单点登录，一处登录处处可用？

**解决方案**：
Mars Admin基于Sa-Token实现单点登录，按照以下步骤配置：

1. 修改配置文件：
```yaml
sa-token:
  # 打开SSO功能
  sso:
    # SSO-Server端配置
    type: server
    # 配置SSO-Server的地址
    auth-url: http://sa-sso-server.com:9000/sso/auth
    # 是否打开单点注销功能
    is-slo: true
```

2. 参考Sa-Token官方文档完成详细配置：[Sa-Token SSO文档](https://sa-token.dev33.cn/doc/index.html#/sso/sso-server)

## 权限管理问题

### Q: 新增的菜单为什么看不到？

**问题描述**：在菜单管理中添加了新菜单，但在左侧导航栏中看不到。

**解决方案**：
1. 检查菜单是否正确配置了路由地址和组件路径
2. 确认当前用户角色是否被授予了该菜单的权限
3. 检查菜单的父级菜单是否正确设置
4. 尝试刷新页面或重新登录以更新权限缓存

### Q: 如何给用户分配多个角色？

**问题描述**：系统需要给某些用户分配多个角色，如何操作？

**解决方案**：
1. 进入"用户管理"页面
2. 点击目标用户行的"分配角色"按钮
3. 在弹出的对话框中，可以选择多个角色（按住Ctrl键进行多选）
4. 点击"确定"保存设置

### Q: 数据权限如何配置？

**问题描述**：如何限制用户只能查看特定部门的数据？

**解决方案**：
1. 进入"角色管理"页面
2. 点击目标角色行的"数据权限"按钮
3. 在弹出的对话框中，选择适当的数据权限范围：
   - 全部数据权限
   - 本部门数据权限
   - 本部门及以下数据权限
   - 仅本人数据权限
   - 自定义数据权限（可选择特定部门）
4. 点击"确定"保存设置

## 功能使用问题

### Q: 如何配置文件上传到OSS？

**问题描述**：如何将文件上传功能从本地存储切换到阿里云OSS？

**解决方案**：
1. 修改配置文件`application.yml`：
```yaml
# 文件上传配置
file:
  upload:
    # 存储类型：local-本地存储，oss-对象存储
    type: oss
    # OSS配置
    oss:
      endpoint: oss-cn-beijing.aliyuncs.com
      access-key-id: your-access-key-id
      access-key-secret: your-access-key-secret
      bucket-name: your-bucket-name
```

2. 重启应用使配置生效

### Q: 如何使用SQL监控功能？

**问题描述**：如何查看和分析系统的SQL执行情况？

**解决方案**：
1. 访问SQL监控页面：`http://your-domain/sqlLog.html`
2. 在监控页面可以查看：
   - 实时SQL执行日志
   - SQL执行统计信息
   - 慢查询分析
   - 性能趋势图表
3. 可以通过设置过滤条件，只查看特定类型的SQL语句

### Q: 如何修改系统主题？

**问题描述**：如何更改系统的颜色主题和布局方式？

**解决方案**：
1. 点击右上角的用户头像，选择"主题设置"
2. 在主题设置面板中，可以调整：
   - 主题色：选择系统主色调
   - 布局模式：侧边栏模式、顶部导航模式、混合模式等
   - 深色模式：开启或关闭深色模式
   - 内容区域：宽度、圆角大小等
3. 设置完成后会实时预览效果，满意后可以点击"保存"应用设置

## 开发问题

### Q: 如何新增一个业务模块？

**问题描述**：如何在系统中添加一个新的业务功能模块？

**解决方案**：
1. **后端开发**：
   - 创建实体类：`com.mars.admin.modules.system.entity.YourEntity`
   - 创建Mapper接口：`com.mars.admin.mapper.YourMapper`
   - 创建Service接口和实现：`com.mars.admin.service.YourService`
   - 创建Controller：`com.mars.admin.controller.YourController`

2. **前端开发**：
   - 在`src/views`下创建新的视图组件
   - 在`src/router/routes`中添加路由配置
   - 在`src/service/api`中添加API调用方法

3. **权限配置**：
   - 在菜单管理中添加新模块的菜单项
   - 配置相应的权限标识
   - 为需要访问的角色授予菜单权限

### Q: 如何自定义异常处理？

**问题描述**：如何处理业务中的自定义异常并返回友好提示？

**解决方案**：
1. 定义业务异常类：
```java
public class BusinessException extends RuntimeException {
    private String code;
    
    public BusinessException(String message) {
        super(message);
        this.code = "500";
    }
    
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    // getter and setter
}
```

2. 在全局异常处理器中捕获：
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        return R.fail(e.getCode(), e.getMessage());
    }
    
    // 其他异常处理方法
}
```

3. 在业务代码中使用：
```java
if (invalidCondition) {
    throw new BusinessException("400", "输入参数不符合要求");
}
```

### Q: 如何添加自定义验证器？

**问题描述**：如何添加自定义的数据验证规则？

**解决方案**：
1. 定义自定义注解：
```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileValidator.class)
public @interface Mobile {
    String message() default "手机号格式不正确";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
```

2. 实现验证器：
```java
public class MobileValidator implements ConstraintValidator<Mobile, String> {
    
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true; // 为空时不校验，交给@NotEmpty处理
        }
        return MOBILE_PATTERN.matcher(value).matches();
    }
}
```

3. 在DTO中使用：
```java
public class UserDTO {
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @Mobile
    private String mobile;
    
    // 其他字段和getter/setter
}
```

## 系统维护问题

### Q: 如何备份和恢复数据库？

**问题描述**：如何定期备份系统数据库并在需要时恢复？

**解决方案**：
1. **备份数据库**：
```bash
# MySQL备份命令
mysqldump -u username -p mars_admin > backup_filename.sql
```

2. **恢复数据库**：
```bash
# 先创建空数据库
mysql -u username -p -e "CREATE DATABASE mars_admin CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

# 恢复数据
mysql -u username -p mars_admin < backup_filename.sql
```

3. **自动备份脚本**：
创建定时备份脚本并通过crontab定期执行。详细步骤请参考[部署指南](guide/deploy.md)中的备份策略部分。

### Q: 如何清理日志和临时文件？

**问题描述**：系统运行一段时间后，日志和临时文件占用大量磁盘空间，如何清理？

**解决方案**：
1. **日志文件清理**：
   - 配置logback的日志滚动策略，自动清理老旧日志
   - 使用系统工具如logrotate进行日志管理

2. **临时文件清理**：
   - 定期清理上传临时目录：`/tmp/mars-admin/upload-tmp/`
   - 清理缓存目录：`/tmp/mars-admin/cache/`

3. **自动清理脚本**：
```bash
#!/bin/bash
# 清理30天前的日志文件
find /var/log/mars-admin -name "*.log.*" -mtime +30 -delete

# 清理7天前的临时上传文件
find /tmp/mars-admin/upload-tmp -type f -mtime +7 -delete
```

### Q: 系统性能变慢如何排查？

**问题描述**：系统运行一段时间后变得缓慢，如何定位和解决性能问题？

**解决方案**：
1. **检查系统资源**：
   - CPU使用率：`top`命令
   - 内存使用：`free -m`命令
   - 磁盘使用：`df -h`命令

2. **检查数据库性能**：
   - 使用内置SQL监控查看慢查询
   - 检查数据库索引是否合理
   - 分析数据库连接池使用情况

3. **检查JVM性能**：
   - 使用JVisualVM或JProfiler分析内存使用
   - 检查是否存在内存泄漏
   - 调整JVM参数，如堆大小

4. **检查缓存效率**：
   - 分析Redis缓存命中率
   - 检查缓存策略是否合理

## 其他问题

### Q: 如何联系技术支持？

如果您遇到未在本文档中列出的问题，可以通过以下方式获取支持：

1. GitHub Issues：https://github.com/Marsfactory/mars-admin/issues
2. Gitee Issues：https://gitee.com/Marsfactory/mars-admin/issues
3. 官方社区：https://bbs.mars-admin.com
4. 电子邮件：support@mars-admin.com

### Q: 如何参与项目贡献？

如果您希望为Mars Admin项目贡献代码或文档，请参考[贡献指南](contributing.md)。 
