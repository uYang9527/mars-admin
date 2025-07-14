# Druid监控

Druid监控是Mars Admin系统的重要组成部分，提供了完整的数据库连接池监控和SQL执行分析功能，帮助开发者和运维人员实时了解数据库性能状况。

## 功能概述

Druid监控模块主要包含以下功能：

- **连接池监控**：实时监控数据库连接池状态
- **SQL执行监控**：记录和分析SQL执行情况
- **慢SQL检测**：自动识别和标记慢查询
- **性能统计**：提供详细的性能指标统计
- **Web监控界面**：直观的Web监控面板
- **安全防护**：SQL防火墙功能

## 配置说明

### 1. 基础配置

Mars Admin在`application-dev.yml`和`application-prod.yml`中配置了完整的Druid参数：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mars_admin?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
    druid:
      # 连接池配置
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
```

### 2. 监控统计配置

```yaml
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
```

### 3. Web监控配置

```yaml
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

## 访问监控页面

### 1. Druid内置监控页面

Mars Admin提供了Druid内置的监控页面，可以通过以下方式访问：

**访问地址：** `http://localhost:9999/druid/login.html`

**登录信息：**
- 用户名：`admin`
- 密码：`marsadmin`

### 2. 集成SQL监控页面

Mars Admin还提供了自定义的SQL监控页面，集成了更丰富的功能：

**访问地址：** `http://localhost:9999/sqlLog.html`

在该页面中，可以直接点击"📊 Druid监控"链接跳转到Druid监控页面。

## 监控功能详解

### 1. 数据源监控

Druid监控页面提供了以下数据源监控功能：

#### 基本信息
- **数据库URL**：显示数据库连接地址
- **数据库类型**：MySQL
- **驱动类名**：com.mysql.cj.jdbc.Driver
- **初始化时间**：连接池初始化时间

#### 连接池状态
- **活跃连接数**：当前正在使用的连接数
- **池中连接数**：连接池中的总连接数
- **最大连接数**：连接池允许的最大连接数
- **最小空闲连接数**：连接池保持的最小空闲连接数

#### 连接统计
- **连接获取次数**：总的连接获取次数
- **连接创建次数**：新创建连接的次数
- **连接销毁次数**：连接被销毁的次数
- **连接泄漏次数**：检测到的连接泄漏次数

### 2. SQL监控

#### SQL执行统计
- **执行次数**：SQL语句的执行次数
- **执行时间**：SQL语句的执行时间统计
- **最大时间**：单次执行的最大时间
- **平均时间**：平均执行时间
- **最小时间**：单次执行的最小时间

#### 慢SQL检测
Mars Admin配置了500ms的慢SQL阈值，超过该阈值的SQL会被标记为慢SQL：

```yaml
slow-sql-millis: 500  # 慢SQL阈值500ms
```

#### SQL详细信息
- **SQL语句**：完整的SQL语句内容
- **参数信息**：SQL执行时的参数值
- **执行计划**：SQL的执行计划信息
- **错误信息**：SQL执行失败时的错误信息

### 3. Web应用监控

#### URI监控
- **请求URI**：Web请求的URI路径
- **请求次数**：URI的访问次数
- **请求时间**：URI的响应时间统计
- **并发数**：URI的并发访问数

#### Session监控
- **Session创建次数**：Session的创建统计
- **Session销毁次数**：Session的销毁统计
- **Session超时次数**：Session超时的统计

### 4. Spring监控

由于配置了AOP监控模式：

```yaml
aop-patterns: com.mars.admin.service.*
```

可以监控Service层的方法调用：
- **方法执行次数**：Service方法的调用次数
- **方法执行时间**：方法的执行时间统计
- **方法并发数**：方法的并发调用数

## 性能优化建议

### 1. 连接池优化

根据监控数据调整连接池参数：

```yaml
# 根据并发量调整最大连接数
max-active: 20

# 根据业务特点调整最小空闲连接数
min-idle: 5

# 根据网络环境调整超时时间
max-wait: 60000
```

### 2. 慢SQL优化

通过Druid监控识别慢SQL后，可以采取以下优化措施：

#### 索引优化
```sql
-- 为常用查询字段添加索引
CREATE INDEX idx_user_status ON sys_user(status);
CREATE INDEX idx_user_dept_id ON sys_user(dept_id);

-- 为复合查询添加复合索引
CREATE INDEX idx_user_dept_status ON sys_user(dept_id, status);
```

#### SQL语句优化
```sql
-- 避免SELECT *，只查询需要的字段
SELECT user_id, user_name, email FROM sys_user WHERE status = '0';

-- 使用LIMIT限制结果集
SELECT * FROM sys_user WHERE dept_id = 1 LIMIT 10;

-- 避免在WHERE子句中使用函数
-- 不推荐
SELECT * FROM sys_user WHERE DATE(create_time) = '2025-01-01';
-- 推荐
SELECT * FROM sys_user WHERE create_time >= '2025-01-01 00:00:00'
  AND create_time < '2025-01-02 00:00:00';
```

### 3. 连接泄漏检测

如果监控显示连接泄漏，检查以下几点：

1. **确保连接正确关闭**
```java
// 使用try-with-resources自动关闭连接
try (Connection conn = dataSource.getConnection();
     PreparedStatement ps = conn.prepareStatement(sql)) {
    // 执行SQL
}
```

2. **检查事务管理**
```java
@Transactional
public void updateUser(SysUser user) {
    // 确保事务正确提交或回滚
    userMapper.updateById(user);
}
```

## 监控告警

### 1. 连接池告警

可以通过监控数据设置告警规则：

```java
@Component
public class DruidMonitorAlarm {

    @Scheduled(fixedRate = 60000) // 每分钟检查一次
    public void checkConnectionPool() {
        DruidDataSource dataSource = (DruidDataSource) this.dataSource;

        // 检查活跃连接数
        int activeCount = dataSource.getActiveCount();
        int maxActive = dataSource.getMaxActive();

        if (activeCount > maxActive * 0.8) {
            // 发送告警：连接池使用率超过80%
            sendAlarm("连接池使用率过高: " + activeCount + "/" + maxActive);
        }

        // 检查连接泄漏
        long leakCount = dataSource.getRemoveAbandonedCount();
        if (leakCount > 0) {
            sendAlarm("检测到连接泄漏: " + leakCount + "个");
        }
    }
}
```

### 2. 慢SQL告警

```java
@Component
public class SlowSqlAlarm {

    public void checkSlowSql() {
        // 获取慢SQL统计
        List<JdbcSqlStatValue> sqlStats = DruidStatManagerFacade.getInstance()
            .getDataSourceStatDataList();

        for (JdbcSqlStatValue stat : sqlStats) {
            if (stat.getMaxTimespan() > 1000) { // 超过1秒
                sendAlarm("发现慢SQL: " + stat.getSql() +
                         ", 最大执行时间: " + stat.getMaxTimespan() + "ms");
            }
        }
    }
}
```

## 最佳实践

### 1. 生产环境配置

生产环境建议的Druid配置：

```yaml
spring:
  datasource:
    druid:
      # 生产环境连接池配置
      initial-size: 10
      min-idle: 10
      max-active: 50
      max-wait: 30000

      # 生产环境监控配置
      filter:
        stat:
          slow-sql-millis: 1000  # 生产环境慢SQL阈值调高
        wall:
          config:
            select-allow: true
            delete-allow: false    # 生产环境禁止DELETE
            update-allow: true
            insert-allow: true

      # 生产环境访问控制
      stat-view-servlet:
        allow: "192.168.1.0/24"  # 限制访问IP段
        deny: ""                 # 禁止访问的IP
```

### 2. 安全配置

```yaml
# 生产环境安全配置
stat-view-servlet:
  login-username: ${DRUID_USERNAME:admin}     # 使用环境变量
  login-password: ${DRUID_PASSWORD:password}  # 使用环境变量
  reset-enable: false                         # 生产环境禁止重置
```

### 3. 监控数据导出

可以通过Druid提供的API导出监控数据：

```java
@RestController
@RequestMapping("/monitor/druid")
public class DruidMonitorController {

    @GetMapping("/datasource")
    public List<Map<String, Object>> getDataSourceStat() {
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }

    @GetMapping("/sql")
    public List<Map<String, Object>> getSqlStat() {
        return DruidStatManagerFacade.getInstance().getSqlStatDataList(null);
    }

    @GetMapping("/wall")
    public WallProviderStatValue getWallStat() {
        return DruidStatManagerFacade.getInstance().getWallStatMap();
    }
}
```

## 常见问题

### Q: 如何解决连接池耗尽问题？

**解决方案：**
1. 增加最大连接数
2. 检查连接泄漏
3. 优化慢SQL
4. 调整连接超时时间

### Q: 如何处理大量慢SQL？

**解决方案：**
1. 添加合适的索引
2. 优化SQL语句
3. 分页查询大结果集
4. 使用缓存减少数据库访问

### Q: 监控页面无法访问怎么办？

**解决方案：**
1. 检查`stat-view-servlet.enabled`是否为true
2. 确认访问URL正确：`/druid/`
3. 检查IP访问限制配置
4. 验证用户名密码是否正确