# 配置说明

本文档详细介绍Mars Admin的各项配置选项，帮助您根据实际需求进行个性化配置。

## 配置文件结构

### 后端配置文件

```
mars-admin-server/src/main/resources/
├── application.yml              # 主配置文件
├── application-dev.yml          # 开发环境配置
├── application-prod.yml         # 生产环境配置
├── application-test.yml         # 测试环境配置
├── logback-spring.xml          # 日志配置
└── banner.txt                  # 启动横幅
```

### 前端配置文件

```
mars-admin-ui/
├── .env                        # 通用环境变量
├── .env.development           # 开发环境变量
├── .env.production           # 生产环境变量
├── vite.config.ts            # Vite构建配置
├── tsconfig.json             # TypeScript配置
└── package.json              # 项目依赖配置
```

## 后端配置详解

### 1. 服务器配置

```yaml
server:
  port: 9999                    # 服务端口
  servlet:
    context-path: /             # 应用上下文路径
    encoding:
      charset: UTF-8            # 字符编码
      enabled: true
      force: true
  compression:
    enabled: true               # 启用响应压缩
    mime-types: text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json
    min-response-size: 1024     # 最小压缩大小
```

### 2. 数据源配置

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    url: jdbc:mysql://localhost:3306/mars_admin?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
    
    # Druid连接池配置
    druid:
      initial-size: 5                    # 初始化连接数
      min-idle: 5                        # 最小空闲连接数
      max-active: 20                     # 最大活跃连接数
      max-wait: 60000                    # 获取连接等待超时时间(ms)
      time-between-eviction-runs-millis: 60000    # 配置间隔多久进行一次检测
      min-evictable-idle-time-millis: 300000      # 配置连接在池中最小生存时间
      validation-query: SELECT 1         # 验证连接有效性的SQL
      test-while-idle: true              # 空闲时检测连接是否有效
      test-on-borrow: false              # 申请连接时检测连接是否有效
      test-on-return: false              # 归还连接时检测连接是否有效
      pool-prepared-statements: true      # 是否缓存preparedStatement
      max-pool-prepared-statement-per-connection-size: 20  # 每个连接上PSCache的大小
      filters: stat,wall                 # 配置监控统计拦截的filters
      
      # 监控统计拦截的filters
      filter:
        stat:
          enabled: true                  # 启用SQL监控
          db-type: mysql                 # 数据库类型
          log-slow-sql: true             # 记录慢SQL
          slow-sql-millis: 500           # 慢SQL阈值(ms)
        wall:
          enabled: true                  # 启用防火墙
          db-type: mysql                 # 数据库类型
          config:
            multi-statement-allow: true   # 允许多语句执行
      
      # StatViewServlet配置
      stat-view-servlet:
        enabled: true                    # 启用监控页面
        url-pattern: /druid/*           # 监控页面URL模式
        reset-enable: true              # 允许重置统计数据
        login-username: admin           # 监控页面登录用户名
        login-password: marsadmin       # 监控页面登录密码
        allow: ""                       # 允许访问的IP(空表示允许所有)
      
      # WebStatFilter配置
      web-stat-filter:
        enabled: true                   # 启用Web监控
        url-pattern: /*                 # 监控的URL模式
        exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"  # 排除的资源
        session-stat-enable: true       # 启用session统计
      
      # Spring监控配置
      aop-patterns: com.mars.admin.service.*  # AOP监控的包路径
```

### 3. Redis配置

```yaml
spring:
  redis:
    host: localhost                     # Redis服务器地址
    port: 6379                         # Redis服务器端口
    password:                          # Redis密码
    database: 0                        # Redis数据库索引
    timeout: 10s                       # 连接超时时间
    lettuce:
      pool:
        max-active: 8                  # 连接池最大连接数
        max-wait: -1ms                 # 连接池最大阻塞等待时间
        max-idle: 8                    # 连接池最大空闲连接数
        min-idle: 0                    # 连接池最小空闲连接数
      shutdown-timeout: 100ms          # 关闭超时时间
```

### 4. MyBatis-Plus配置

```yaml
mybatis-plus:
  mapper-locations: classpath*:mapper/**/*Mapper.xml
  type-aliases-package: com.mars.admin.domain
  configuration:
    map-underscore-to-camel-case: true  # 开启驼峰命名转换
    cache-enabled: false                # 关闭二级缓存
    call-setters-on-nulls: true        # 空值时调用setter方法
    jdbc-type-for-null: 'null'         # 空值的JDBC类型
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # 日志实现
  global-config:
    db-config:
      id-type: AUTO                     # 主键类型
      logic-delete-field: delFlag       # 逻辑删除字段
      logic-delete-value: 2             # 逻辑删除值
      logic-not-delete-value: 0         # 逻辑未删除值
    banner: false                       # 关闭MyBatis-Plus横幅
```

### 5. 文件上传配置

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 100MB              # 单个文件最大大小
      max-request-size: 100MB           # 请求最大大小
      enabled: true                     # 启用文件上传

# 自定义文件配置
file:
  upload:
    path: /opt/mars-admin/upload        # 本地存储路径
    max-size: 100                       # 最大文件大小(MB)
    allowed-types: jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx  # 允许的文件类型

# MinIO配置
minio:
  endpoint: http://localhost:9000       # MinIO服务地址
  access-key: minioadmin               # 访问密钥
  secret-key: minioadmin               # 秘密密钥
  bucket-name: mars-admin              # 存储桶名称
```

### 6. JWT配置

```yaml
jwt:
  secret: mars-admin-secret-key-2024    # JWT密钥
  expire-time: 7200                     # 访问令牌过期时间(秒)
  refresh-time: 604800                  # 刷新令牌过期时间(秒)
  header: Authorization                 # 请求头名称
  prefix: Bearer                        # 令牌前缀
```

### 7. 日志配置

```yaml
logging:
  level:
    com.mars.admin: DEBUG               # 应用日志级别
    org.springframework.security: DEBUG # Spring Security日志级别
    org.mybatis: DEBUG                  # MyBatis日志级别
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{50} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{50} - %msg%n"
  file:
    name: logs/mars-admin.log           # 日志文件路径
    max-size: 100MB                     # 单个日志文件最大大小
    max-history: 30                     # 保留的日志文件数量
```

## 前端配置详解

### 1. 环境变量配置

```bash
# .env.development
# 应用标题
VITE_APP_TITLE=Mars Admin

# 应用描述
VITE_APP_DESC=基于Vue3的现代化后台管理系统

# 应用版本
VITE_APP_VERSION=1.0.0

# API基础路径
VITE_BASE_URL=http://localhost:9999

# API前缀
VITE_API_PREFIX=/api

# 是否开启Mock
VITE_ENABLE_MOCK=false

# 是否开启代理
VITE_ENABLE_PROXY=true

# 代理目标地址
VITE_PROXY_TARGET=http://localhost:9999

# 是否开启PWA
VITE_ENABLE_PWA=false

# 是否开启Gzip压缩
VITE_ENABLE_GZIP=true

# 是否开启包分析
VITE_ENABLE_ANALYZE=false

# 是否开启ESLint
VITE_ENABLE_ESLINT=true

# 是否开启Stylelint
VITE_ENABLE_STYLELINT=true
```

### 2. Vite构建配置

```typescript
// vite.config.ts
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  
  return {
    plugins: [
      vue(),
      // 其他插件配置
    ],
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src'),
        '~': resolve(__dirname, 'src'),
        '#': resolve(__dirname, 'types')
      }
    },
    server: {
      host: '0.0.0.0',
      port: 5173,
      open: true,
      proxy: env.VITE_ENABLE_PROXY === 'true' ? {
        '/api': {
          target: env.VITE_PROXY_TARGET,
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, '')
        }
      } : undefined
    },
    build: {
      target: 'es2015',
      outDir: 'dist',
      assetsDir: 'assets',
      sourcemap: false,
      rollupOptions: {
        output: {
          chunkFileNames: 'assets/js/[name]-[hash].js',
          entryFileNames: 'assets/js/[name]-[hash].js',
          assetFileNames: 'assets/[ext]/[name]-[hash].[ext]'
        }
      }
    }
  }
})
```

### 3. TypeScript配置

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "useDefineForClassFields": true,
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "skipLibCheck": true,
    "moduleResolution": "bundler",
    "allowImportingTsExtensions": true,
    "resolveJsonModule": true,
    "isolatedModules": true,
    "noEmit": true,
    "jsx": "preserve",
    "strict": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noFallthroughCasesInSwitch": true,
    "baseUrl": ".",
    "paths": {
      "@/*": ["src/*"],
      "~/*": ["src/*"],
      "#/*": ["types/*"]
    }
  },
  "include": ["src/**/*.ts", "src/**/*.d.ts", "src/**/*.tsx", "src/**/*.vue"],
  "references": [{ "path": "./tsconfig.node.json" }]
}
```

## 环境配置

### 1. 开发环境

开发环境配置注重开发体验和调试便利：

```yaml
# application-dev.yml
spring:
  profiles:
    active: dev
  
logging:
  level:
    root: INFO
    com.mars.admin: DEBUG
    
debug: true

# 开发环境特殊配置
management:
  endpoints:
    web:
      exposure:
        include: "*"        # 暴露所有端点
```

### 2. 生产环境

生产环境配置注重性能和安全：

```yaml
# application-prod.yml
spring:
  profiles:
    active: prod

logging:
  level:
    root: WARN
    com.mars.admin: INFO

debug: false

# 生产环境安全配置
management:
  endpoints:
    web:
      exposure:
        include: health,info  # 只暴露必要端点
      base-path: /actuator
  endpoint:
    health:
      show-details: never
```

### 3. 测试环境

测试环境配置便于自动化测试：

```yaml
# application-test.yml
spring:
  profiles:
    active: test
  
  datasource:
    url: jdbc:h2:mem:testdb  # 使用内存数据库
    driver-class-name: org.h2.Driver
    
logging:
  level:
    root: WARN
    com.mars.admin: DEBUG
```

## 配置最佳实践

### 1. 敏感信息保护

```yaml
# 使用环境变量
spring:
  datasource:
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
  
  redis:
    password: ${REDIS_PASSWORD:}

jwt:
  secret: ${JWT_SECRET:default-secret}
```

### 2. 配置文件分离

```yaml
# application.yml - 通用配置
spring:
  application:
    name: mars-admin
  profiles:
    active: @spring.profiles.active@

# application-dev.yml - 开发环境特定配置
# application-prod.yml - 生产环境特定配置
```

### 3. 配置验证

```java
@ConfigurationProperties(prefix = "jwt")
@Validated
public class JwtProperties {
    
    @NotBlank
    private String secret;
    
    @Min(60)
    @Max(86400)
    private Integer expireTime;
    
    // getter/setter
}
```

## 常见配置问题

### Q: 数据库连接池配置优化

**解决方案**：
```yaml
spring:
  datasource:
    druid:
      initial-size: 10      # 根据并发量调整
      min-idle: 10          # 保持足够的空闲连接
      max-active: 50        # 根据数据库连接数限制调整
      max-wait: 30000       # 避免过长等待
```

### Q: Redis连接超时问题

**解决方案**：
```yaml
spring:
  redis:
    timeout: 30s          # 增加超时时间
    lettuce:
      pool:
        max-active: 16    # 增加连接池大小
        max-wait: 10s     # 设置合理的等待时间
```

### Q: 文件上传大小限制

**解决方案**：
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 500MB    # 根据需求调整
      max-request-size: 500MB
      
server:
  tomcat:
    max-swallow-size: 500MB   # Tomcat配置
```

## 下一步

配置完成后，您可以：

1. [开始本地开发](development.md) - 学习如何进行本地开发
2. [查看API文档](../backend/api.md) - 了解后端API接口
3. [了解权限系统](../backend/permission.md) - 学习权限配置
4. [部署到生产环境](deploy.md) - 学习生产环境部署
