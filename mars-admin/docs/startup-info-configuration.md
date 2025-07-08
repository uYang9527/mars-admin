# Mars Admin 启动信息配置说明

## 概述

Mars Admin 提供了灵活的启动信息显示配置，可以根据不同环境和需求调整启动时显示的信息内容。

## 配置选项

### 基础配置

在 `application.yml` 或 `application-{profile}.yml` 中添加以下配置：

```yaml
startup:
  info:
    # 启动信息显示模式
    mode: full              # full(完整) | simple(简洁) | none(不显示)
    # 是否显示系统信息
    show-system-info: true  # true | false
    # 是否显示ASCII艺术字
    show-banner: true       # true | false
```

### 配置详解

#### 1. mode (显示模式)

- **full** (默认): 显示完整的启动信息
  - ASCII艺术字横幅
  - 访问地址
  - 接口文档地址
  - 系统监控地址  
  - 多端登录支持说明
  - 快速测试命令

- **simple**: 显示简洁的启动信息
  - 基本访问地址
  - 接口文档链接
  - 监控面板链接

- **none**: 不显示启动信息
  - 只显示Spring Boot默认日志

#### 2. show-system-info (系统信息)

控制是否显示详细的系统运行信息：

- **true** (默认): 显示系统信息
  - 启动时间
  - JVM版本和内存使用情况
  - 操作系统信息
  - 数据库连接信息
  - Redis连接信息
  - 配置环境
  - 重要提示信息

- **false**: 不显示系统信息

#### 3. show-banner (ASCII横幅)

控制是否显示ASCII艺术字横幅：

- **true** (默认): 显示Mars Admin ASCII艺术字
- **false**: 不显示ASCII艺术字

## 使用场景

### 开发环境配置

```yaml
# application-dev.yml
startup:
  info:
    mode: full
    show-system-info: true
    show-banner: true
```

**效果**: 显示完整的启动信息，方便开发调试。

### 测试环境配置

```yaml
# application-test.yml
startup:
  info:
    mode: simple
    show-system-info: false
    show-banner: false
```

**效果**: 显示简洁信息，减少日志输出。

### 生产环境配置

```yaml
# application-prod.yml
startup:
  info:
    mode: none
    show-system-info: false
    show-banner: false
```

**效果**: 不显示启动信息，保持日志清洁。

## 启动信息示例

### Full模式输出

```
==================================================================================
                                                                                  
    ███╗   ███╗ █████╗ ██████╗ ███████╗      █████╗ ██████╗ ███╗   ███╗██╗███╗   ██╗
    ████╗ ████║██╔══██╗██╔══██╗██╔════╝     ██╔══██╗██╔══██╗████╗ ████║██║████╗  ██║
    ██╔████╔██║███████║██████╔╝███████╗     ███████║██║  ██║██╔████╔██║██║██╔██╗ ██║
    ██║╚██╔╝██║██╔══██║██╔══██╗╚════██║     ██╔══██║██║  ██║██║╚██╔╝██║██║██║╚██╗██║
    ██║ ╚═╝ ██║██║  ██║██║  ██║███████║     ██║  ██║██████╔╝██║ ╚═╝ ██║██║██║ ╚████║
    ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝     ╚═╝  ╚═╝╚═════╝ ╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝
                                                                                  
==================================================================================

🚀 Mars-Admin 启动成功！
📋 当前环境: dev
🌐 本地访问地址: http://localhost:8080
🌐 外部访问地址: http://192.168.1.100:8080

📚 接口文档地址:
   ├─ Knife4j文档:  http://localhost:8080/doc.html
   ├─ Swagger文档:  http://localhost:8080/swagger-ui.html
   └─ OpenAPI规范:  http://localhost:8080/v3/api-docs

🔧 系统监控地址:
   ├─ Druid监控:    http://localhost:8080/druid
   ├─ SQL日志:      http://localhost:8080/sqlLog.html
   └─ 健康检查:     http://localhost:8080/actuator/health

🔐 多端登录支持:
   ├─ PC端登录:     POST /auth/login (loginType: pc)
   ├─ 小程序登录:   POST /auth/login (loginType: miniprogram)
   ├─ APP登录:      POST /auth/login (loginType: app)
   └─ 发送验证码:   POST /auth/sendCaptcha?mobile=手机号

💡 快速测试:
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"loginType":"pc","username":"admin","password":"123456"}'

==================================================================================

📊 系统运行信息:
   ├─ 启动时间:     2025-01-16 14:30:25
   ├─ JVM版本:      17.0.1
   ├─ 最大内存:     2048 MB
   ├─ 已用内存:     256 MB
   ├─ 空闲内存:     1792 MB
   ├─ 操作系统:     Windows 10 10.0
   ├─ 数据库:       MySQL 8.0.33
   ├─ 连接地址:     jdbc:mysql://localhost:3306/mars_admin
   ├─ Redis:        localhost:6379 (DB:0)
   └─ 配置环境:     dev

⚠️  重要提示:
   ├─ 默认管理员账号: admin / 123456
   ├─ Druid监控账号:  admin / marsadmin
   ├─ 生产环境请及时修改默认密码
   └─ 详细文档请查看: docs/multi-login-strategy.md

==================================================================================
```

### Simple模式输出

```
🚀 Mars-Admin 启动成功！
📋 当前环境: test
🌐 本地访问地址: http://localhost:8080
🌐 外部访问地址: http://192.168.1.100:8080

📚 接口文档: http://localhost:8080/doc.html
🔧 监控面板: http://localhost:8080/druid
==================================================================================
```

## 实现原理

系统使用Spring Boot的`ApplicationRunner`接口实现启动信息打印：

1. **StartupInfoRunner** (Order=1): 负责显示主要的访问信息
2. **SystemInfoRunner** (Order=2): 负责显示系统运行信息

通过`@Order`注解控制执行顺序，确保信息按逻辑顺序显示。

## 自定义扩展

如果需要添加自定义的启动信息，可以创建新的`ApplicationRunner`：

```java
@Component
@Order(10) // 设置执行顺序
public class CustomStartupRunner implements ApplicationRunner {
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 自定义启动信息逻辑
        System.out.println("🎯 自定义启动信息");
    }
}
```

## 注意事项

1. **性能影响**: 系统信息收集可能会增加启动时间，生产环境建议关闭
2. **日志输出**: 启动信息使用`System.out.println`输出，不会记录到日志文件
3. **环境适配**: 建议根据不同环境配置不同的显示模式
4. **安全考虑**: 生产环境避免显示敏感的连接信息

## 版本信息

- **版本**: v1.0.0
- **更新时间**: 2025-01-16
- **兼容性**: Spring Boot 3.x 