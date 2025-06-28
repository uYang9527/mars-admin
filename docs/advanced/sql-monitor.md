# SQL监控系统

Mars Admin内置了强大的SQL监控系统，可以实时监控和分析SQL执行情况，帮助开发人员和运维人员优化数据库性能，提高系统响应速度。

## 功能概述

SQL监控系统提供以下核心功能：

- **实时SQL监控**：实时捕获和展示系统中执行的SQL语句
- **慢查询分析**：识别和分析执行时间较长的SQL语句
- **执行计划分析**：查看SQL执行计划，帮助优化查询
- **性能统计**：统计SQL执行次数、平均执行时间等指标
- **趋势图表**：直观展示SQL执行性能趋势
- **告警机制**：对异常SQL进行告警提示

## 技术实现

SQL监控系统基于MyBatis-Flex的拦截器机制实现，通过以下技术手段实现全面监控：

1. **SQL拦截器**：拦截并记录所有通过MyBatis-Flex执行的SQL语句
2. **性能计时**：精确计算SQL执行时间
3. **参数绑定**：记录SQL参数绑定信息
4. **结果统计**：统计SQL执行结果行数
5. **WebSocket**：实时推送SQL执行信息到监控页面
6. **可视化图表**：使用ECharts展示性能指标

## 使用指南

### 访问监控页面

1. 确保已启动Mars Admin系统
2. 在浏览器中访问：`http://your-domain/static/sqlLog.html`
3. 默认情况下，只有管理员角色可以访问此页面

### 监控面板介绍

监控面板主要分为以下几个区域：

#### 1. 实时SQL日志

显示系统中最近执行的SQL语句，包括：
- SQL语句内容
- 执行时间
- 影响行数
- 执行状态
- 来源方法

可以通过顶部的过滤条件筛选特定的SQL语句：
- 关键字过滤
- 时间范围过滤
- 执行时间阈值过滤
- SQL类型过滤（SELECT、INSERT、UPDATE、DELETE等）

#### 2. 慢查询分析

展示执行时间超过阈值的SQL语句，默认阈值为500ms，可以在设置中调整。

慢查询列表包含以下信息：
- SQL语句
- 执行时间
- 执行频率
- 首次出现时间
- 最后出现时间
- 来源类和方法

点击慢查询记录可以查看详细信息，包括：
- 完整SQL语句（格式化展示）
- 参数绑定值
- 堆栈信息
- 优化建议

#### 3. 性能统计

提供SQL执行的统计信息：
- 总执行次数
- 平均执行时间
- 最长执行时间
- 最短执行时间
- 按SQL类型统计（SELECT、INSERT、UPDATE、DELETE）
- 按时间段统计（分钟、小时、天）

#### 4. 趋势图表

使用图表直观展示SQL执行情况：
- SQL执行次数趋势图
- SQL执行时间分布图
- 慢查询占比饼图
- 各类SQL占比图

#### 5. 设置面板

可以调整监控系统的配置：
- 慢查询阈值设置
- 日志保留时间
- 实时刷新间隔
- 监控开关
- 告警阈值设置

### 使用技巧

#### 查找慢查询

1. 在"慢查询分析"标签页中，可以看到所有执行时间超过阈值的SQL
2. 点击"执行时间"列头可以按执行时间排序
3. 点击具体的慢查询记录可以查看详情和优化建议

#### 分析执行计划

1. 在SQL详情页面，点击"查看执行计划"按钮
2. 系统会执行EXPLAIN命令并展示执行计划
3. 根据执行计划中的信息（如是否使用索引、扫描行数等）进行优化

#### 导出分析报告

1. 在监控面板右上角，点击"导出报告"按钮
2. 选择要导出的时间范围和数据类型
3. 点击"确定"按钮生成并下载报告（PDF或Excel格式）

## 配置说明

SQL监控系统的配置位于`application.yml`文件中：

```yaml
# SQL监控配置
sql-monitor:
  # 是否启用SQL监控
  enabled: true
  # 慢查询阈值（毫秒）
  slow-sql-threshold: 500
  # 是否记录参数
  record-params: true
  # 最大保留记录数
  max-record-count: 10000
  # 排除的SQL模式（正则表达式）
  exclude-patterns:
    - "^SELECT \\* FROM sys_dict_data.*"
    - "^SELECT \\* FROM sys_config.*"
  # 是否启用告警
  alarm-enabled: true
  # 告警阈值（毫秒）
  alarm-threshold: 1000
```

## 实现原理

### 核心类

SQL监控系统的核心实现类包括：

#### 1. SqlMonitorInterceptor

MyBatis-Flex拦截器，负责拦截SQL执行并收集执行信息：

```java
@Component
public class SqlMonitorInterceptor implements InnerInterceptor {
    
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, 
                           RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        
        // 保存上下文信息
        SqlMonitorContext.set("startTime", startTime);
        SqlMonitorContext.set("sql", boundSql.getSql());
        SqlMonitorContext.set("params", parameter);
        SqlMonitorContext.set("mappedStatement", ms.getId());
    }
    
    @Override
    public void afterQuery(Executor executor, MappedStatement ms, Object parameter,
                          RowBounds rowBounds, ResultHandler resultHandler, 
                          BoundSql boundSql, Object resultObject) {
        // 计算执行时间
        long startTime = (Long) SqlMonitorContext.get("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        
        // 创建SQL执行记录
        SqlExecuteRecord record = new SqlExecuteRecord();
        record.setSql((String) SqlMonitorContext.get("sql"));
        record.setParams(JSON.toJSONString(SqlMonitorContext.get("params")));
        record.setExecuteTime(executeTime);
        record.setMappedStatement((String) SqlMonitorContext.get("mappedStatement"));
        record.setExecuteType("SELECT");
        record.setExecuteTimestamp(new Date());
        
        // 获取结果集大小
        if (resultObject instanceof Collection) {
            record.setResultSize(((Collection<?>) resultObject).size());
        }
        
        // 保存记录
        SqlMonitorService.addRecord(record);
        
        // 检查是否为慢查询
        if (executeTime > sqlMonitorProperties.getSlowSqlThreshold()) {
            SqlMonitorService.addSlowSql(record);
            
            // 检查是否需要告警
            if (sqlMonitorProperties.isAlarmEnabled() && 
                executeTime > sqlMonitorProperties.getAlarmThreshold()) {
                SqlMonitorService.sendAlarm(record);
            }
        }
        
        // 清理上下文
        SqlMonitorContext.clear();
    }
    
    // 其他方法实现（update、insert、delete等）
}
```

#### 2. SqlMonitorService

负责处理和管理SQL监控数据：

```java
@Service
public class SqlMonitorService {
    
    // 使用环形缓冲区存储最近的SQL执行记录
    private static final CircularFifoQueue<SqlExecuteRecord> recentRecords = 
        new CircularFifoQueue<>(10000);
    
    // 存储慢查询记录
    private static final Map<String, SlowSqlInfo> slowSqlMap = 
        new ConcurrentHashMap<>();
    
    // 存储统计信息
    private static final SqlStatistics statistics = new SqlStatistics();
    
    // WebSocket会话管理
    private static final Set<Session> sessions = 
        Collections.synchronizedSet(new HashSet<>());
    
    /**
     * 添加SQL执行记录
     */
    public static void addRecord(SqlExecuteRecord record) {
        recentRecords.add(record);
        statistics.update(record);
        
        // 实时推送到WebSocket客户端
        broadcastRecord(record);
    }
    
    /**
     * 添加慢查询记录
     */
    public static void addSlowSql(SqlExecuteRecord record) {
        // 使用SQL指纹（去除具体参数值的SQL）作为key
        String sqlFingerprint = SqlUtils.getFingerprint(record.getSql());
        
        SlowSqlInfo slowSqlInfo = slowSqlMap.computeIfAbsent(
            sqlFingerprint, k -> new SlowSqlInfo(record));
        
        // 更新慢查询信息
        slowSqlInfo.incrementCount();
        slowSqlInfo.setLastOccurTime(record.getExecuteTimestamp());
        
        if (record.getExecuteTime() > slowSqlInfo.getMaxExecuteTime()) {
            slowSqlInfo.setMaxExecuteTime(record.getExecuteTime());
            slowSqlInfo.setWorstRecord(record);
        }
    }
    
    /**
     * 发送告警
     */
    public static void sendAlarm(SqlExecuteRecord record) {
        // 实现告警逻辑（邮件、短信、钉钉等）
    }
    
    // 其他方法
}
```

#### 3. SqlMonitorController

提供REST API接口，供监控页面调用：

```java
@RestController
@RequestMapping("/monitor/sql")
public class SqlMonitorController {
    
    @Autowired
    private SqlMonitorService sqlMonitorService;
    
    /**
     * 获取最近的SQL执行记录
     */
    @GetMapping("/recent")
    public R<List<SqlExecuteRecord>> getRecentRecords(
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long minTime,
            @RequestParam(required = false) Long maxTime) {
        
        return R.ok(sqlMonitorService.getRecentRecords(limit, keyword, type, minTime, maxTime));
    }
    
    /**
     * 获取慢查询列表
     */
    @GetMapping("/slow")
    public R<List<SlowSqlInfo>> getSlowSqlList() {
        return R.ok(sqlMonitorService.getSlowSqlList());
    }
    
    /**
     * 获取SQL统计信息
     */
    @GetMapping("/statistics")
    public R<SqlStatistics> getStatistics() {
        return R.ok(sqlMonitorService.getStatistics());
    }
    
    /**
     * 获取SQL执行计划
     */
    @PostMapping("/explain")
    public R<List<Map<String, Object>>> explainSql(@RequestBody ExplainSqlDTO dto) {
        return R.ok(sqlMonitorService.explainSql(dto.getSql(), dto.getParams()));
    }
    
    /**
     * 清空监控数据
     */
    @DeleteMapping("/clear")
    public R<Void> clearMonitorData() {
        sqlMonitorService.clearMonitorData();
        return R.ok();
    }
    
    /**
     * 更新监控设置
     */
    @PutMapping("/settings")
    public R<Void> updateSettings(@RequestBody SqlMonitorSettings settings) {
        sqlMonitorService.updateSettings(settings);
        return R.ok();
    }
}
```

#### 4. SqlMonitorWebSocket

处理WebSocket连接，实现实时数据推送：

```java
@ServerEndpoint("/ws/sql-monitor")
@Component
public class SqlMonitorWebSocket {
    
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        SqlMonitorService.addSession(session);
    }
    
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SqlMonitorService.removeSession(session);
    }
    
    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // 处理客户端消息
    }
    
    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
```

### 前端实现

SQL监控页面使用Vue.js和ECharts实现，主要包含以下组件：

- **实时日志表格**：展示最近执行的SQL
- **慢查询列表**：展示慢查询信息
- **统计图表**：使用ECharts展示性能指标
- **SQL详情弹窗**：展示SQL详细信息和执行计划
- **设置面板**：配置监控参数

## 性能影响

SQL监控系统对应用性能的影响主要体现在以下几个方面：

1. **内存占用**：
   - 默认最多保存10000条SQL记录，约占用10-20MB内存
   - 慢查询记录会长期保存，但通常数量有限

2. **CPU开销**：
   - SQL拦截和分析会增加少量CPU开销
   - 实测影响通常在1-3%之间

3. **网络开销**：
   - WebSocket实时推送会增加少量网络流量
   - 可以通过调整推送频率来减少影响

为了减少对生产环境的影响，可以采取以下措施：

- 在生产环境中调高慢查询阈值
- 减少保留的记录数量
- 关闭参数记录功能
- 设置排除模式，不监控某些频繁执行的SQL

## 最佳实践

### 优化慢查询

1. **添加合适的索引**
   - 分析WHERE条件和JOIN条件
   - 为常用查询条件创建索引
   - 考虑复合索引的列顺序

2. **优化SQL语句**
   - 避免SELECT *，只查询需要的列
   - 减少JOIN表的数量
   - 使用LIMIT限制结果集大小
   - 避免在WHERE子句中使用函数

3. **调整数据库参数**
   - 增加缓冲池大小
   - 优化查询缓存
   - 调整连接池参数

### 定期分析

建议定期（如每周）分析SQL监控数据，关注以下指标：

- 新出现的慢查询
- 执行频率高的SQL
- 执行时间变长的SQL
- 特定业务场景下的SQL性能

### 与开发流程结合

将SQL监控集成到开发流程中：

- 在测试环境中启用更严格的监控（较低的慢查询阈值）
- 在代码审查中检查潜在的SQL性能问题
- 将SQL优化作为发布前检查项

## 常见问题

### Q: 监控页面无法访问或没有数据？

**解决方案**：
1. 确认SQL监控功能已在配置中启用（`sql-monitor.enabled=true`）
2. 检查当前用户是否有权限访问监控页面
3. 检查浏览器控制台是否有JavaScript错误
4. 验证WebSocket连接是否正常建立

### Q: 如何减少误报的慢查询？

**解决方案**：
1. 适当调高慢查询阈值（如从500ms调整到1000ms）
2. 使用排除模式，忽略特定的SQL语句
3. 对于首次执行较慢但后续执行较快的SQL（如预热查询），可以在设置中启用"忽略首次执行"选项

### Q: 监控数据会占用大量内存吗？

**解决方案**：
1. 默认情况下，系统使用环形缓冲区存储最近的10000条记录
2. 可以通过配置`sql-monitor.max-record-count`减少保留记录数
3. 定期使用"清空数据"功能释放内存
4. 监控数据不会持久化到数据库，重启应用后数据会重置

### Q: 生产环境是否应该启用SQL监控？

**解决方案**：
1. 生产环境可以启用SQL监控，但建议调整以下配置：
   - 提高慢查询阈值（如1000ms或更高）
   - 减少记录保留数量
   - 配置排除模式，忽略频繁执行的SQL
   - 关闭实时推送或降低推送频率
2. 对于高并发系统，可以考虑只在特定时间段启用监控
3. 或者使用采样模式，只监控部分SQL执行 