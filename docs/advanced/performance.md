# 性能优化

性能优化是Mars Admin系统运行的重要保障，本文档详细介绍了系统各个层面的性能优化策略和最佳实践。

## 优化策略概览

### 优化层次

```
┌─────────────────────────────────────┐
│           前端性能优化               │
├─────────────────────────────────────┤
│           网络传输优化               │
├─────────────────────────────────────┤
│           应用层优化                 │
├─────────────────────────────────────┤
│           数据库优化                 │
├─────────────────────────────────────┤
│           系统层优化                 │
└─────────────────────────────────────┘
```

### 优化目标

| 指标 | 目标值 | 说明 |
|------|--------|------|
| 页面加载时间 | < 2秒 | 首屏加载完成时间 |
| API响应时间 | < 500ms | 95%的API请求响应时间 |
| 数据库查询 | < 100ms | 95%的SQL查询执行时间 |
| 内存使用率 | < 70% | 系统稳定运行时的内存占用 |
| CPU使用率 | < 60% | 正常负载下的CPU占用 |

## 后端性能优化

### 1. 数据库优化

#### 索引优化

```sql
-- 用户表索引优化
CREATE INDEX idx_user_username ON sys_user(user_name);
CREATE INDEX idx_user_email ON sys_user(email);
CREATE INDEX idx_user_status_delFlag ON sys_user(status, del_flag);
CREATE INDEX idx_user_deptId_status ON sys_user(dept_id, status);

-- 角色表索引优化
CREATE INDEX idx_role_status ON sys_role(status);
CREATE INDEX idx_role_roleKey ON sys_role(role_key);

-- 菜单表索引优化
CREATE INDEX idx_menu_parentId ON sys_menu(parent_id);
CREATE INDEX idx_menu_status ON sys_menu(status);

-- 操作日志表索引优化
CREATE INDEX idx_operlog_operTime ON sys_oper_log(oper_time);
CREATE INDEX idx_operlog_operName ON sys_oper_log(oper_name);
CREATE INDEX idx_operlog_businessType ON sys_oper_log(business_type);

-- 登录日志表索引优化
CREATE INDEX idx_logininfor_loginTime ON sys_logininfor(login_time);
CREATE INDEX idx_logininfor_ipaddr ON sys_logininfor(ipaddr);
CREATE INDEX idx_logininfor_userName ON sys_logininfor(user_name);
```

#### 查询优化

```java
/**
 * 优化的用户查询服务
 */
@Service
public class OptimizedUserService {
    
    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private RedisCache redisCache;
    
    /**
     * 分页查询用户列表 - 优化版本
     */
    public TableDataInfo selectUserList(SysUser user, PageDomain pageDomain) {
        // 1. 构建缓存键
        String cacheKey = buildUserListCacheKey(user, pageDomain);
        
        // 2. 尝试从缓存获取
        TableDataInfo cachedResult = redisCache.getCacheObject(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }
        
        // 3. 数据库查询
        startPage(pageDomain);
        List<SysUser> list = userMapper.selectUserListOptimized(user);
        TableDataInfo result = getDataTable(list);
        
        // 4. 缓存结果（5分钟）
        redisCache.setCacheObject(cacheKey, result, 5, TimeUnit.MINUTES);
        
        return result;
    }
    
    /**
     * 批量查询用户信息 - 避免N+1问题
     */
    public List<SysUser> selectUsersByIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        
        // 1. 从缓存中获取已存在的用户
        Map<Long, SysUser> cachedUsers = new HashMap<>();
        List<Long> uncachedIds = new ArrayList<>();
        
        for (Long userId : userIds) {
            String cacheKey = CacheConstants.SYS_USER_KEY + userId;
            SysUser cachedUser = redisCache.getCacheObject(cacheKey);
            if (cachedUser != null) {
                cachedUsers.put(userId, cachedUser);
            } else {
                uncachedIds.add(userId);
            }
        }
        
        // 2. 批量查询未缓存的用户
        List<SysUser> result = new ArrayList<>(cachedUsers.values());
        if (!uncachedIds.isEmpty()) {
            List<SysUser> dbUsers = userMapper.selectUsersByIds(uncachedIds);
            
            // 3. 缓存新查询的用户
            for (SysUser user : dbUsers) {
                String cacheKey = CacheConstants.SYS_USER_KEY + user.getUserId();
                redisCache.setCacheObject(cacheKey, user, 30, TimeUnit.MINUTES);
                result.add(user);
            }
        }
        
        return result;
    }
    
    private String buildUserListCacheKey(SysUser user, PageDomain pageDomain) {
        return String.format("user:list:%s:%d:%d", 
            DigestUtils.md5Hex(JSON.toJSONString(user)), 
            pageDomain.getPageNum(), 
            pageDomain.getPageSize());
    }
}
```

#### SQL优化

```xml
<!-- 优化的用户查询Mapper -->
<mapper namespace="com.mars.admin.mapper.SysUserMapper">
    
    <!-- 优化的用户列表查询 -->
    <select id="selectUserListOptimized" parameterType="SysUser" resultMap="SysUserResult">
        SELECT 
            u.user_id, u.user_name, u.nick_name, u.email, u.avatar, 
            u.phonenumber, u.sex, u.status, u.create_time,
            d.dept_name, d.leader
        FROM sys_user u
        LEFT JOIN sys_dept d ON u.dept_id = d.dept_id
        WHERE u.del_flag = '0'
        <if test="userId != null and userId != 0">
            AND u.user_id = #{userId}
        </if>
        <if test="userName != null and userName != ''">
            AND u.user_name LIKE CONCAT('%', #{userName}, '%')
        </if>
        <if test="status != null and status != ''">
            AND u.status = #{status}
        </if>
        <if test="phonenumber != null and phonenumber != ''">
            AND u.phonenumber LIKE CONCAT('%', #{phonenumber}, '%')
        </if>
        <if test="params.beginTime != null and params.beginTime != ''">
            AND DATE_FORMAT(u.create_time,'%Y-%m-%d') &gt;= DATE_FORMAT(#{params.beginTime},'%Y-%m-%d')
        </if>
        <if test="params.endTime != null and params.endTime != ''">
            AND DATE_FORMAT(u.create_time,'%Y-%m-%d') &lt;= DATE_FORMAT(#{params.endTime},'%Y-%m-%d')
        </if>
        <if test="deptId != null and deptId != 0">
            AND (u.dept_id = #{deptId} OR u.dept_id IN (
                SELECT t.dept_id FROM sys_dept t WHERE FIND_IN_SET(#{deptId}, ancestors)
            ))
        </if>
        <!-- 数据范围过滤 -->
        ${params.dataScope}
        ORDER BY u.user_id DESC
    </select>
    
    <!-- 批量查询用户 -->
    <select id="selectUsersByIds" resultMap="SysUserResult">
        SELECT 
            u.user_id, u.user_name, u.nick_name, u.email, u.avatar, 
            u.phonenumber, u.sex, u.status, u.create_time,
            d.dept_name, d.leader
        FROM sys_user u
        LEFT JOIN sys_dept d ON u.dept_id = d.dept_id
        WHERE u.del_flag = '0' 
        AND u.user_id IN
        <foreach item="userId" collection="array" open="(" separator="," close=")">
            #{userId}
        </foreach>
    </select>
    
    <!-- 统计查询优化 -->
    <select id="countUserByCondition" parameterType="SysUser" resultType="int">
        SELECT COUNT(1) 
        FROM sys_user u
        <if test="deptId != null and deptId != 0">
            LEFT JOIN sys_dept d ON u.dept_id = d.dept_id
        </if>
        WHERE u.del_flag = '0'
        <if test="userName != null and userName != ''">
            AND u.user_name LIKE CONCAT('%', #{userName}, '%')
        </if>
        <if test="status != null and status != ''">
            AND u.status = #{status}
        </if>
        <if test="deptId != null and deptId != 0">
            AND (u.dept_id = #{deptId} OR u.dept_id IN (
                SELECT t.dept_id FROM sys_dept t WHERE FIND_IN_SET(#{deptId}, ancestors)
            ))
        </if>
    </select>
</mapper>
```

### 2. 缓存优化

#### 多级缓存策略

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    /**
     * 本地缓存配置
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats());
        return cacheManager;
    }
    
    /**
     * Redis缓存配置
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(30))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
            
        return RedisCacheManager.builder(factory)
            .cacheDefaults(config)
            .build();
    }
}

/**
 * 多级缓存服务
 */
@Service
public class MultiLevelCacheService {
    
    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager localCacheManager;
    
    @Autowired
    @Qualifier("redisCacheManager")
    private CacheManager redisCacheManager;
    
    /**
     * 多级缓存获取
     */
    public <T> T get(String cacheName, String key, Class<T> type, Supplier<T> supplier) {
        // 1. 先从本地缓存获取
        Cache localCache = localCacheManager.getCache(cacheName);
        if (localCache != null) {
            T value = localCache.get(key, type);
            if (value != null) {
                return value;
            }
        }
        
        // 2. 从Redis缓存获取
        Cache redisCache = redisCacheManager.getCache(cacheName);
        if (redisCache != null) {
            T value = redisCache.get(key, type);
            if (value != null) {
                // 回写到本地缓存
                if (localCache != null) {
                    localCache.put(key, value);
                }
                return value;
            }
        }
        
        // 3. 从数据源获取
        T value = supplier.get();
        if (value != null) {
            // 写入两级缓存
            if (localCache != null) {
                localCache.put(key, value);
            }
            if (redisCache != null) {
                redisCache.put(key, value);
            }
        }
        
        return value;
    }
    
    /**
     * 清除多级缓存
     */
    public void evict(String cacheName, String key) {
        Cache localCache = localCacheManager.getCache(cacheName);
        if (localCache != null) {
            localCache.evict(key);
        }
        
        Cache redisCache = redisCacheManager.getCache(cacheName);
        if (redisCache != null) {
            redisCache.evict(key);
        }
    }
}
```

#### 缓存预热

```java
@Component
public class CacheWarmupService {
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysDictDataService dictDataService;
    
    @Autowired
    private ISysConfigService configService;
    
    /**
     * 系统启动时预热缓存
     */
    @EventListener(ApplicationReadyEvent.class)
    public void warmupCache() {
        log.info("开始预热系统缓存...");
        
        // 预热字典数据
        warmupDictData();
        
        // 预热系统配置
        warmupSystemConfig();
        
        // 预热热点用户数据
        warmupHotUsers();
        
        log.info("系统缓存预热完成");
    }
    
    private void warmupDictData() {
        try {
            List<SysDictData> dictDataList = dictDataService.selectDictDataList(new SysDictData());
            log.info("预热字典数据: {} 条", dictDataList.size());
        } catch (Exception e) {
            log.error("预热字典数据失败", e);
        }
    }
    
    private void warmupSystemConfig() {
        try {
            List<SysConfig> configList = configService.selectConfigList(new SysConfig());
            log.info("预热系统配置: {} 条", configList.size());
        } catch (Exception e) {
            log.error("预热系统配置失败", e);
        }
    }
    
    private void warmupHotUsers() {
        try {
            // 预热最近活跃的用户数据
            SysUser queryUser = new SysUser();
            queryUser.setStatus("0");
            PageDomain pageDomain = new PageDomain();
            pageDomain.setPageNum(1);
            pageDomain.setPageSize(100);
            
            TableDataInfo result = userService.selectUserList(queryUser, pageDomain);
            log.info("预热用户数据: {} 条", result.getRows().size());
        } catch (Exception e) {
            log.error("预热用户数据失败", e);
        }
    }
}
```

### 3. 连接池优化

#### HikariCP配置优化

```yaml
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      # 连接池名称
      pool-name: HikariPool-Mars
      # 最小空闲连接数
      minimum-idle: 10
      # 最大连接池大小
      maximum-pool-size: 50
      # 连接超时时间(毫秒)
      connection-timeout: 30000
      # 空闲连接超时时间(毫秒)
      idle-timeout: 600000
      # 连接最大生命周期(毫秒)
      max-lifetime: 1800000
      # 连接测试查询
      connection-test-query: SELECT 1
      # 是否自动提交
      auto-commit: true
      # 连接泄漏检测阈值(毫秒)
      leak-detection-threshold: 60000
      # 验证连接的超时时间(毫秒)
      validation-timeout: 5000
```

#### Redis连接池优化

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    timeout: 10s
    lettuce:
      pool:
        # 连接池最大连接数
        max-active: 16
        # 连接池最大阻塞等待时间
        max-wait: 10s
        # 连接池中的最大空闲连接
        max-idle: 8
        # 连接池中的最小空闲连接
        min-idle: 2
      # 关闭超时时间
      shutdown-timeout: 100ms
```

### 4. 异步处理优化

#### 异步任务配置

```java
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    
    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数
        executor.setCorePoolSize(10);
        // 最大线程数
        executor.setMaxPoolSize(50);
        // 队列容量
        executor.setQueueCapacity(200);
        // 线程名前缀
        executor.setThreadNamePrefix("Mars-Async-");
        // 线程空闲时间
        executor.setKeepAliveSeconds(60);
        
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        
        executor.initialize();
        return executor;
    }
    
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}

/**
 * 异步服务示例
 */
@Service
public class AsyncService {
    
    /**
     * 异步记录操作日志
     */
    @Async("taskExecutor")
    public void recordOperationLog(SysOperLog operLog) {
        try {
            operLogService.insertOperlog(operLog);
        } catch (Exception e) {
            log.error("记录操作日志失败", e);
        }
    }
    
    /**
     * 异步发送邮件
     */
    @Async("taskExecutor")
    public CompletableFuture<Boolean> sendEmailAsync(String to, String subject, String content) {
        try {
            emailService.sendEmail(to, subject, content);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            return CompletableFuture.completedFuture(false);
        }
    }
    
    /**
     * 异步处理文件上传
     */
    @Async("taskExecutor")
    public void processFileUpload(MultipartFile file, String uploadPath) {
        try {
            // 文件处理逻辑
            FileUploadUtils.upload(uploadPath, file);
            
            // 生成缩略图
            if (FileUploadUtils.isImage(file)) {
                ImageUtils.generateThumbnail(uploadPath, file.getOriginalFilename());
            }
        } catch (Exception e) {
            log.error("处理文件上传失败", e);
        }
    }
}
```

### 5. JVM优化

#### JVM参数配置

```bash
# 生产环境JVM参数配置
JAVA_OPTS="
# 堆内存设置
-Xms2g
-Xmx4g
-XX:NewRatio=3
-XX:SurvivorRatio=8

# 垃圾收集器设置
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=16m
-XX:G1ReservePercent=25
-XX:InitiatingHeapOccupancyPercent=30

# 内存溢出处理
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/opt/mars-admin/logs/heapdump.hprof

# GC日志
-XX:+PrintGC
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
-XX:+PrintGCApplicationStoppedTime
-Xloggc:/opt/mars-admin/logs/gc.log
-XX:+UseGCLogFileRotation
-XX:NumberOfGCLogFiles=5
-XX:GCLogFileSize=10M

# JIT编译优化
-XX:+TieredCompilation
-XX:TieredStopAtLevel=4

# 其他优化参数
-XX:+UseFastAccessorMethods
-XX:+OptimizeStringConcat
-XX:+UseStringDeduplication
-server
"
```

#### 内存监控

```java
@Component
public class MemoryMonitor {
    
    private final MeterRegistry meterRegistry;
    
    public MemoryMonitor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        initMemoryMetrics();
    }
    
    private void initMemoryMetrics() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        
        // 堆内存监控
        Gauge.builder("jvm.memory.heap.used")
            .register(meterRegistry, memoryBean, bean -> bean.getHeapMemoryUsage().getUsed());
        Gauge.builder("jvm.memory.heap.max")
            .register(meterRegistry, memoryBean, bean -> bean.getHeapMemoryUsage().getMax());
        Gauge.builder("jvm.memory.heap.committed")
            .register(meterRegistry, memoryBean, bean -> bean.getHeapMemoryUsage().getCommitted());
            
        // 非堆内存监控
        Gauge.builder("jvm.memory.nonheap.used")
            .register(meterRegistry, memoryBean, bean -> bean.getNonHeapMemoryUsage().getUsed());
        Gauge.builder("jvm.memory.nonheap.max")
            .register(meterRegistry, memoryBean, bean -> bean.getNonHeapMemoryUsage().getMax());
            
        // GC监控
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            Gauge.builder("jvm.gc.collection.count")
                .tag("gc", gcBean.getName())
                .register(meterRegistry, gcBean, GarbageCollectorMXBean::getCollectionCount);
            Gauge.builder("jvm.gc.collection.time")
                .tag("gc", gcBean.getName())
                .register(meterRegistry, gcBean, GarbageCollectorMXBean::getCollectionTime);
        }
    }
    
    @Scheduled(fixedRate = 30000)
    public void checkMemoryUsage() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        
        double heapUsedPercent = (double) heapUsage.getUsed() / heapUsage.getMax() * 100;
        
        if (heapUsedPercent > 80) {
            log.warn("堆内存使用率过高: {:.2f}%", heapUsedPercent);
            
            if (heapUsedPercent > 90) {
                log.error("堆内存使用率严重过高: {:.2f}%, 建议立即检查", heapUsedPercent);
                // 可以触发告警或自动处理
            }
        }
    }
}
```

## 前端性能优化

### 1. 构建优化

#### Vite配置优化

```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import { visualizer } from 'rollup-plugin-visualizer'
import viteCompression from 'vite-plugin-compression'

export default defineConfig({
  plugins: [
    vue(),
    // Gzip压缩
    viteCompression({
      verbose: true,
      disable: false,
      threshold: 10240,
      algorithm: 'gzip',
      ext: '.gz'
    }),
    // 包分析
    visualizer({
      filename: 'dist/stats.html',
      open: true,
      gzipSize: true,
      brotliSize: true
    })
  ],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
      '~': resolve(__dirname, 'src'),
      '#': resolve(__dirname, 'types')
    }
  },
  build: {
    target: 'es2015',
    outDir: 'dist',
    assetsDir: 'assets',
    sourcemap: false,
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true
      }
    },
    rollupOptions: {
      output: {
        chunkFileNames: 'assets/js/[name]-[hash].js',
        entryFileNames: 'assets/js/[name]-[hash].js',
        assetFileNames: 'assets/[ext]/[name]-[hash].[ext]',
        manualChunks: {
          // 第三方库分包
          vendor: ['vue', 'vue-router', 'pinia'],
          antd: ['ant-design-vue'],
          utils: ['lodash-es', 'dayjs'],
          charts: ['echarts']
        }
      }
    },
    // 启用代码分割
    chunkSizeWarningLimit: 1500
  },
  server: {
    host: '0.0.0.0',
    port: 5173,
    open: true,
    cors: true,
    proxy: {
      '/api': {
        target: 'http://localhost:9999',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
```

### 2. 组件优化

#### 虚拟滚动

```vue
<!-- src/components/VirtualList/index.vue -->
<template>
  <div class="virtual-list" :style="{ height: containerHeight + 'px' }" @scroll="handleScroll">
    <div class="virtual-list-phantom" :style="{ height: totalHeight + 'px' }"></div>
    <div class="virtual-list-content" :style="{ transform: `translateY(${startOffset}px)` }">
      <div
        v-for="item in visibleData"
        :key="item.id"
        class="virtual-list-item"
        :style="{ height: itemHeight + 'px' }"
      >
        <slot :item="item" :index="item.index"></slot>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface Props {
  items: any[]
  itemHeight: number
  containerHeight: number
  bufferSize?: number
}

const props = withDefaults(defineProps<Props>(), {
  bufferSize: 5
})

const scrollTop = ref(0)

// 计算总高度
const totalHeight = computed(() => props.items.length * props.itemHeight)

// 计算可见区域的起始索引
const startIndex = computed(() => {
  return Math.max(0, Math.floor(scrollTop.value / props.itemHeight) - props.bufferSize)
})

// 计算可见区域的结束索引
const endIndex = computed(() => {
  const visibleCount = Math.ceil(props.containerHeight / props.itemHeight)
  return Math.min(props.items.length - 1, startIndex.value + visibleCount + props.bufferSize)
})

// 计算可见数据
const visibleData = computed(() => {
  return props.items.slice(startIndex.value, endIndex.value + 1).map((item, index) => ({
    ...item,
    index: startIndex.value + index
  }))
})

// 计算偏移量
const startOffset = computed(() => startIndex.value * props.itemHeight)

// 处理滚动事件
const handleScroll = (event: Event) => {
  scrollTop.value = (event.target as HTMLElement).scrollTop
}
</script>

<style scoped>
.virtual-list {
  position: relative;
  overflow-y: auto;
}

.virtual-list-phantom {
  position: absolute;
  left: 0;
  top: 0;
  right: 0;
  z-index: -1;
}

.virtual-list-content {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
}

.virtual-list-item {
  box-sizing: border-box;
}
</style>
```

### 3. 网络优化

#### 请求缓存

```typescript
// src/utils/request-cache.ts
interface CacheItem {
  data: any
  timestamp: number
  ttl: number
}

class RequestCache {
  private cache = new Map<string, CacheItem>()

  // 生成缓存键
  private generateKey(url: string, params?: any): string {
    const paramStr = params ? JSON.stringify(params) : ''
    return `${url}:${paramStr}`
  }

  // 设置缓存
  set(url: string, params: any, data: any, ttl: number = 5 * 60 * 1000): void {
    const key = this.generateKey(url, params)
    this.cache.set(key, {
      data,
      timestamp: Date.now(),
      ttl
    })
  }

  // 获取缓存
  get(url: string, params?: any): any | null {
    const key = this.generateKey(url, params)
    const item = this.cache.get(key)

    if (!item) return null

    // 检查是否过期
    if (Date.now() - item.timestamp > item.ttl) {
      this.cache.delete(key)
      return null
    }

    return item.data
  }

  // 清除缓存
  clear(url?: string, params?: any): void {
    if (url) {
      const key = this.generateKey(url, params)
      this.cache.delete(key)
    } else {
      this.cache.clear()
    }
  }
}

export const requestCache = new RequestCache()
```

## 性能监控

### 1. 前端性能监控

```typescript
// src/utils/performance-monitor.ts
class PerformanceMonitor {
  private metrics: Map<string, number> = new Map()

  // 监控页面加载性能
  monitorPageLoad(): void {
    window.addEventListener('load', () => {
      const navigation = performance.getEntriesByType('navigation')[0] as PerformanceNavigationTiming

      const metrics = {
        // DNS查询时间
        dnsTime: navigation.domainLookupEnd - navigation.domainLookupStart,
        // TCP连接时间
        tcpTime: navigation.connectEnd - navigation.connectStart,
        // 请求响应时间
        requestTime: navigation.responseEnd - navigation.requestStart,
        // DOM解析时间
        domParseTime: navigation.domContentLoadedEventEnd - navigation.domContentLoadedEventStart,
        // 页面完全加载时间
        loadTime: navigation.loadEventEnd - navigation.loadEventStart,
        // 首屏时间
        firstPaintTime: this.getFirstPaintTime(),
        // 首次内容绘制时间
        firstContentfulPaintTime: this.getFirstContentfulPaintTime()
      }

      // 发送性能数据
      this.sendMetrics('page-load', metrics)
    })
  }

  // 监控API请求性能
  monitorApiRequest(url: string, startTime: number, endTime: number): void {
    const duration = endTime - startTime
    this.metrics.set(`api-${url}`, duration)

    // 如果请求时间过长，记录慢请求
    if (duration > 1000) {
      console.warn(`慢请求检测: ${url} 耗时 ${duration}ms`)
      this.sendMetrics('slow-api', { url, duration })
    }
  }

  // 获取首屏时间
  private getFirstPaintTime(): number {
    const paintEntries = performance.getEntriesByType('paint')
    const firstPaint = paintEntries.find(entry => entry.name === 'first-paint')
    return firstPaint ? firstPaint.startTime : 0
  }

  // 获取首次内容绘制时间
  private getFirstContentfulPaintTime(): number {
    const paintEntries = performance.getEntriesByType('paint')
    const firstContentfulPaint = paintEntries.find(entry => entry.name === 'first-contentful-paint')
    return firstContentfulPaint ? firstContentfulPaint.startTime : 0
  }

  // 发送性能指标
  private sendMetrics(type: string, data: any): void {
    // 这里可以发送到监控服务
    fetch('/api/monitor/performance', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        type,
        data,
        timestamp: Date.now(),
        userAgent: navigator.userAgent,
        url: location.href
      })
    }).catch(error => {
      console.error('发送性能数据失败:', error)
    })
  }
}

export const performanceMonitor = new PerformanceMonitor()

// 自动开始监控
performanceMonitor.monitorPageLoad()
```

## 常见问题

### Q: 如何优化大数据量的表格渲染？

**解决方案**：
1. 使用虚拟滚动技术
2. 实现分页加载
3. 使用表格懒加载
4. 优化表格列的渲染逻辑

### Q: 如何减少首屏加载时间？

**解决方案**：
1. 代码分割和懒加载
2. 关键资源预加载
3. 使用CDN加速
4. 启用Gzip压缩
5. 优化图片资源

### Q: 如何处理内存泄漏问题？

**解决方案**：
1. 及时清理事件监听器
2. 正确使用组件缓存
3. 避免闭包引用
4. 定期清理无用的缓存数据
