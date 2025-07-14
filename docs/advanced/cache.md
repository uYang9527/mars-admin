# 缓存管理

缓存管理是Mars Admin系统性能优化的重要组成部分，提供了多层次的缓存策略和统一的缓存管理功能。

## 功能概述

缓存管理模块主要包含以下功能：

- **多级缓存**：本地缓存、分布式缓存的统一管理
- **缓存监控**：缓存使用情况的实时监控
- **缓存清理**：支持按键、按模式批量清理缓存
- **缓存预热**：系统启动时预加载热点数据
- **缓存统计**：缓存命中率、性能指标统计
- **缓存配置**：灵活的缓存策略配置

## 缓存架构

### 缓存层级

```
┌─────────────────┐
│   应用层缓存     │  (业务数据缓存)
├─────────────────┤
│   框架层缓存     │  (Session、权限等)
├─────────────────┤
│   数据层缓存     │  (MyBatis二级缓存)
├─────────────────┤
│   分布式缓存     │  (Redis集群)
└─────────────────┘
```

### 缓存分类

| 缓存类型 | 存储介质 | 适用场景 | 过期策略 |
|---------|---------|---------|---------|
| 本地缓存 | JVM内存 | 热点数据、配置信息 | TTL、LRU |
| 分布式缓存 | Redis | 会话数据、共享数据 | TTL、手动清理 |
| 数据库缓存 | MyBatis | 查询结果集 | 数据变更时清理 |
| HTTP缓存 | 浏览器/CDN | 静态资源 | ETag、Last-Modified |

## 核心实现

### 1. 缓存配置

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1)) // 默认1小时过期
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .disableCachingNullValues(); // 不缓存null值
        
        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            .withCacheConfiguration("user", config.entryTtl(Duration.ofMinutes(30)))
            .withCacheConfiguration("dict", config.entryTtl(Duration.ofHours(24)))
            .withCacheConfiguration("config", config.entryTtl(Duration.ofHours(12)))
            .build();
    }
    
    @Bean
    public CacheManager localCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .recordStats());
        return cacheManager;
    }
}
```

### 2. 缓存注解使用

```java
@Service
public class SysUserServiceImpl implements ISysUserService {
    
    /**
     * 查询用户信息（带缓存）
     */
    @Override
    @Cacheable(value = "user", key = "#userId", unless = "#result == null")
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }
    
    /**
     * 更新用户信息（清除缓存）
     */
    @Override
    @CacheEvict(value = "user", key = "#user.userId")
    public boolean updateUser(SysUser user) {
        return this.updateById(user);
    }
    
    /**
     * 删除用户（清除缓存）
     */
    @Override
    @CacheEvict(value = "user", key = "#userId")
    public boolean deleteUserById(Long userId) {
        return this.removeById(userId);
    }
    
    /**
     * 查询用户列表（条件缓存）
     */
    @Override
    @Cacheable(value = "userList", key = "#user.deptId + '_' + #user.status", 
               condition = "#user.deptId != null")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }
    
    /**
     * 清除所有用户缓存
     */
    @Override
    @CacheEvict(value = {"user", "userList"}, allEntries = true)
    public void clearUserCache() {
        // 方法体可以为空，注解会自动清除缓存
    }
}
```

### 3. 自定义缓存管理器

```java
@Component
public class CustomCacheManager {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String CACHE_PREFIX = "mars:cache:";
    
    /**
     * 设置缓存
     */
    public void set(String key, Object value, Duration timeout) {
        String cacheKey = CACHE_PREFIX + key;
        redisTemplate.opsForValue().set(cacheKey, value, timeout);
    }
    
    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        String cacheKey = CACHE_PREFIX + key;
        Object value = redisTemplate.opsForValue().get(cacheKey);
        return value != null ? (T) value : null;
    }
    
    /**
     * 删除缓存
     */
    public void delete(String key) {
        String cacheKey = CACHE_PREFIX + key;
        redisTemplate.delete(cacheKey);
    }
    
    /**
     * 批量删除缓存
     */
    public void deletePattern(String pattern) {
        String searchPattern = CACHE_PREFIX + pattern;
        Set<String> keys = redisTemplate.keys(searchPattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
    
    /**
     * 检查缓存是否存在
     */
    public boolean exists(String key) {
        String cacheKey = CACHE_PREFIX + key;
        return Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey));
    }
    
    /**
     * 设置缓存过期时间
     */
    public void expire(String key, Duration timeout) {
        String cacheKey = CACHE_PREFIX + key;
        redisTemplate.expire(cacheKey, timeout);
    }
    
    /**
     * 获取缓存剩余过期时间
     */
    public long getExpire(String key) {
        String cacheKey = CACHE_PREFIX + key;
        return redisTemplate.getExpire(cacheKey);
    }
}
```

## 缓存监控

### 1. 缓存监控接口

**接口地址：** `GET /monitor/cache`

**功能描述：** 获取缓存使用情况和统计信息。

**响应示例：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "redis": {
      "info": {
        "redis_version": "6.2.6",
        "used_memory": "2.5MB",
        "used_memory_human": "2.50M",
        "connected_clients": "5",
        "total_commands_processed": "12345",
        "keyspace_hits": "8901",
        "keyspace_misses": "1234",
        "hit_rate": "87.8%"
      },
      "dbSize": 156,
      "commandStats": [
        {
          "name": "get",
          "value": "5432"
        },
        {
          "name": "set", 
          "value": "2345"
        }
      ]
    },
    "caffeine": {
      "caches": [
        {
          "name": "user",
          "size": 45,
          "hitCount": 234,
          "missCount": 12,
          "hitRate": "95.1%",
          "evictionCount": 3
        }
      ]
    }
  }
}
```

### 2. 缓存监控实现

```java
@RestController
@RequestMapping("/monitor/cache")
public class CacheController {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private CacheManager cacheManager;
    
    /**
     * 获取缓存监控信息
     */
    @GetMapping
    public Result getCacheInfo() {
        Map<String, Object> result = new HashMap<>();
        
        // Redis信息
        result.put("redis", getRedisInfo());
        
        // 本地缓存信息
        result.put("caffeine", getCaffeineInfo());
        
        return Result.success(result);
    }
    
    /**
     * 获取Redis信息
     */
    private Map<String, Object> getRedisInfo() {
        Map<String, Object> redisInfo = new HashMap<>();
        
        try {
            RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
            Properties info = connection.info();
            
            Map<String, String> infoMap = new HashMap<>();
            infoMap.put("redis_version", info.getProperty("redis_version"));
            infoMap.put("used_memory", info.getProperty("used_memory"));
            infoMap.put("used_memory_human", info.getProperty("used_memory_human"));
            infoMap.put("connected_clients", info.getProperty("connected_clients"));
            infoMap.put("total_commands_processed", info.getProperty("total_commands_processed"));
            
            // 计算命中率
            long hits = Long.parseLong(info.getProperty("keyspace_hits", "0"));
            long misses = Long.parseLong(info.getProperty("keyspace_misses", "0"));
            double hitRate = hits + misses > 0 ? (double) hits / (hits + misses) * 100 : 0;
            infoMap.put("hit_rate", String.format("%.1f%%", hitRate));
            
            redisInfo.put("info", infoMap);
            redisInfo.put("dbSize", connection.dbSize());
            
            connection.close();
        } catch (Exception e) {
            log.error("获取Redis信息失败", e);
        }
        
        return redisInfo;
    }
    
    /**
     * 获取Caffeine缓存信息
     */
    private Map<String, Object> getCaffeineInfo() {
        Map<String, Object> caffeineInfo = new HashMap<>();
        List<Map<String, Object>> caches = new ArrayList<>();
        
        if (cacheManager instanceof CaffeineCacheManager) {
            CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) cacheManager;
            
            for (String cacheName : caffeineCacheManager.getCacheNames()) {
                Cache cache = caffeineCacheManager.getCache(cacheName);
                if (cache instanceof CaffeineCache) {
                    com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = 
                        ((CaffeineCache) cache).getNativeCache();
                    
                    CacheStats stats = nativeCache.stats();
                    Map<String, Object> cacheInfo = new HashMap<>();
                    cacheInfo.put("name", cacheName);
                    cacheInfo.put("size", nativeCache.estimatedSize());
                    cacheInfo.put("hitCount", stats.hitCount());
                    cacheInfo.put("missCount", stats.missCount());
                    cacheInfo.put("hitRate", String.format("%.1f%%", stats.hitRate() * 100));
                    cacheInfo.put("evictionCount", stats.evictionCount());
                    
                    caches.add(cacheInfo);
                }
            }
        }
        
        caffeineInfo.put("caches", caches);
        return caffeineInfo;
    }
    
    /**
     * 清除指定缓存
     */
    @DeleteMapping("/{cacheName}")
    public Result clearCache(@PathVariable String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            return Result.success("缓存清除成功");
        }
        return Result.error("缓存不存在");
    }
    
    /**
     * 清除所有缓存
     */
    @DeleteMapping("/all")
    public Result clearAllCache() {
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        }
        return Result.success("所有缓存清除成功");
    }
}
```

## 前端实现

### 1. 缓存监控页面

```vue
<template>
  <div class="cache-monitor">
    <!-- Redis监控 -->
    <a-card title="Redis监控" class="monitor-card">
      <template #extra>
        <a-space>
          <a-button @click="refreshRedisInfo">
            <template #icon><reload-outlined /></template>
            刷新
          </a-button>
          <a-button type="danger" @click="handleClearRedis">
            <template #icon><delete-outlined /></template>
            清空缓存
          </a-button>
        </a-space>
      </template>
      
      <a-row :gutter="16">
        <a-col :span="6">
          <a-statistic title="Redis版本" :value="redisInfo.redis_version" />
        </a-col>
        <a-col :span="6">
          <a-statistic title="内存使用" :value="redisInfo.used_memory_human" />
        </a-col>
        <a-col :span="6">
          <a-statistic title="连接数" :value="redisInfo.connected_clients" />
        </a-col>
        <a-col :span="6">
          <a-statistic title="命中率" :value="redisInfo.hit_rate" />
        </a-col>
      </a-row>
      
      <a-divider />
      
      <a-row :gutter="16">
        <a-col :span="12">
          <h4>键空间信息</h4>
          <a-descriptions :column="1" size="small">
            <a-descriptions-item label="总命令数">
              {{ redisInfo.total_commands_processed }}
            </a-descriptions-item>
            <a-descriptions-item label="命中次数">
              {{ redisInfo.keyspace_hits }}
            </a-descriptions-item>
            <a-descriptions-item label="未命中次数">
              {{ redisInfo.keyspace_misses }}
            </a-descriptions-item>
            <a-descriptions-item label="键总数">
              {{ redisDbSize }}
            </a-descriptions-item>
          </a-descriptions>
        </a-col>
        <a-col :span="12">
          <h4>命令统计</h4>
          <div ref="commandChartRef" style="height: 200px;"></div>
        </a-col>
      </a-row>
    </a-card>

    <!-- 本地缓存监控 -->
    <a-card title="本地缓存监控" class="monitor-card">
      <template #extra>
        <a-button @click="refreshCaffeineInfo">
          <template #icon><reload-outlined /></template>
          刷新
        </a-button>
      </template>
      
      <a-table
        :columns="caffeineColumns"
        :data-source="caffeineData"
        :pagination="false"
        size="small"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-button type="link" size="small" @click="handleClearCache(record.name)">
              清除
            </a-button>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 缓存操作 -->
    <a-card title="缓存操作" class="monitor-card">
      <a-space direction="vertical" style="width: 100%;">
        <a-input-group compact>
          <a-input
            v-model:value="cacheKey"
            placeholder="请输入缓存键名"
            style="width: 300px;"
          />
          <a-button type="primary" @click="handleGetCache">查询</a-button>
          <a-button @click="handleDeleteCache">删除</a-button>
        </a-input-group>
        
        <a-textarea
          v-model:value="cacheValue"
          placeholder="缓存值"
          :rows="4"
          readonly
        />
      </a-space>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { message } from 'ant-design-vue'
import { ReloadOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import * as echarts from 'echarts'
import { 
  fetchCacheInfo, 
  clearCache, 
  clearAllCache,
  getCacheValue,
  deleteCacheKey 
} from '@/service/api/cache'

// 响应式数据
const redisInfo = reactive({
  redis_version: '',
  used_memory_human: '',
  connected_clients: '',
  hit_rate: '',
  total_commands_processed: '',
  keyspace_hits: '',
  keyspace_misses: ''
})

const redisDbSize = ref(0)
const caffeineData = ref([])
const cacheKey = ref('')
const cacheValue = ref('')
const commandChartRef = ref()

// 本地缓存表格列
const caffeineColumns = [
  { title: '缓存名称', dataIndex: 'name', key: 'name' },
  { title: '缓存大小', dataIndex: 'size', key: 'size' },
  { title: '命中次数', dataIndex: 'hitCount', key: 'hitCount' },
  { title: '未命中次数', dataIndex: 'missCount', key: 'missCount' },
  { title: '命中率', dataIndex: 'hitRate', key: 'hitRate' },
  { title: '淘汰次数', dataIndex: 'evictionCount', key: 'evictionCount' },
  { title: '操作', key: 'action', width: 100 }
]

// 方法定义
const loadCacheInfo = async () => {
  try {
    const { data } = await fetchCacheInfo()
    
    // Redis信息
    if (data.redis) {
      Object.assign(redisInfo, data.redis.info)
      redisDbSize.value = data.redis.dbSize
      
      // 绘制命令统计图表
      if (data.redis.commandStats) {
        drawCommandChart(data.redis.commandStats)
      }
    }
    
    // 本地缓存信息
    if (data.caffeine) {
      caffeineData.value = data.caffeine.caches
    }
  } catch (error) {
    message.error('加载缓存信息失败')
  }
}

const drawCommandChart = (commandStats) => {
  nextTick(() => {
    const chart = echarts.init(commandChartRef.value)
    const option = {
      tooltip: {
        trigger: 'item'
      },
      series: [
        {
          type: 'pie',
          radius: '50%',
          data: commandStats.map(item => ({
            name: item.name,
            value: parseInt(item.value)
          })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    }
    chart.setOption(option)
  })
}

const refreshRedisInfo = () => {
  loadCacheInfo()
}

const refreshCaffeineInfo = () => {
  loadCacheInfo()
}

const handleClearRedis = async () => {
  try {
    await clearAllCache()
    message.success('Redis缓存清除成功')
    loadCacheInfo()
  } catch (error) {
    message.error('清除失败')
  }
}

const handleClearCache = async (cacheName) => {
  try {
    await clearCache(cacheName)
    message.success(`缓存 ${cacheName} 清除成功`)
    loadCacheInfo()
  } catch (error) {
    message.error('清除失败')
  }
}

const handleGetCache = async () => {
  if (!cacheKey.value) {
    message.warning('请输入缓存键名')
    return
  }
  
  try {
    const { data } = await getCacheValue(cacheKey.value)
    cacheValue.value = JSON.stringify(data, null, 2)
  } catch (error) {
    message.error('查询缓存失败')
    cacheValue.value = ''
  }
}

const handleDeleteCache = async () => {
  if (!cacheKey.value) {
    message.warning('请输入缓存键名')
    return
  }
  
  try {
    await deleteCacheKey(cacheKey.value)
    message.success('缓存删除成功')
    cacheValue.value = ''
  } catch (error) {
    message.error('删除缓存失败')
  }
}

// 生命周期
onMounted(() => {
  loadCacheInfo()
})
</script>

<style scoped>
.cache-monitor {
  .monitor-card {
    margin-bottom: 16px;
  }
}
</style>
```

## 缓存预热

### 1. 系统启动预热

```java
@Component
public class CacheWarmupRunner implements ApplicationRunner {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysMenuService menuService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("开始缓存预热...");

        // 预热系统配置
        warmupConfig();

        // 预热字典数据
        warmupDict();

        // 预热菜单数据
        warmupMenu();

        log.info("缓存预热完成");
    }

    private void warmupConfig() {
        try {
            List<SysConfig> configs = configService.list();
            for (SysConfig config : configs) {
                configService.selectConfigByKey(config.getConfigKey());
            }
            log.info("系统配置预热完成，共{}条", configs.size());
        } catch (Exception e) {
            log.error("系统配置预热失败", e);
        }
    }

    private void warmupDict() {
        try {
            List<SysDictType> dictTypes = dictTypeService.list();
            for (SysDictType dictType : dictTypes) {
                if ("0".equals(dictType.getStatus())) {
                    dictDataService.selectNormalDictDataByType(dictType.getDictType());
                }
            }
            log.info("字典数据预热完成，共{}个类型", dictTypes.size());
        } catch (Exception e) {
            log.error("字典数据预热失败", e);
        }
    }

    private void warmupMenu() {
        try {
            menuService.selectMenuTreeAll();
            log.info("菜单数据预热完成");
        } catch (Exception e) {
            log.error("菜单数据预热失败", e);
        }
    }
}
```

### 2. 定时预热任务

```java
@Component
public class CacheWarmupTask {

    @Autowired
    private CustomCacheManager cacheManager;

    /**
     * 每天凌晨2点预热缓存
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledWarmup() {
        log.info("开始定时缓存预热...");

        // 清除过期缓存
        clearExpiredCache();

        // 预热热点数据
        warmupHotData();

        log.info("定时缓存预热完成");
    }

    private void clearExpiredCache() {
        // 清除过期的用户缓存
        cacheManager.deletePattern("user:*");

        // 清除过期的会话缓存
        cacheManager.deletePattern("session:*");
    }

    private void warmupHotData() {
        // 预热热点配置
        warmupHotConfig();

        // 预热热点字典
        warmupHotDict();
    }
}
```

## 缓存策略

### 1. 缓存穿透防护

```java
@Component
public class CachePenetrationProtection {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String NULL_VALUE = "NULL";
    private static final Duration NULL_TTL = Duration.ofMinutes(5);

    /**
     * 防止缓存穿透的查询方法
     */
    public <T> T queryWithPassThrough(String key, Function<String, T> dbQuery,
                                     Duration timeout, Class<T> type) {
        // 1. 从缓存查询
        Object cached = redisTemplate.opsForValue().get(key);

        // 2. 缓存命中
        if (cached != null) {
            if (NULL_VALUE.equals(cached)) {
                return null; // 空值缓存
            }
            return (T) cached;
        }

        // 3. 查询数据库
        T result = dbQuery.apply(key);

        // 4. 写入缓存
        if (result == null) {
            // 缓存空值，防止缓存穿透
            redisTemplate.opsForValue().set(key, NULL_VALUE, NULL_TTL);
        } else {
            redisTemplate.opsForValue().set(key, result, timeout);
        }

        return result;
    }
}
```

### 2. 缓存雪崩防护

```java
@Component
public class CacheAvalancheProtection {

    /**
     * 随机过期时间，防止缓存雪崩
     */
    public Duration getRandomTTL(Duration baseTTL) {
        Random random = new Random();
        long baseSeconds = baseTTL.getSeconds();
        // 在基础时间上增加0-30%的随机时间
        long randomSeconds = (long) (baseSeconds * (1 + random.nextDouble() * 0.3));
        return Duration.ofSeconds(randomSeconds);
    }

    /**
     * 带随机过期时间的缓存设置
     */
    public void setWithRandomTTL(String key, Object value, Duration baseTTL) {
        Duration randomTTL = getRandomTTL(baseTTL);
        redisTemplate.opsForValue().set(key, value, randomTTL);
    }
}
```

### 3. 缓存击穿防护

```java
@Component
public class CacheBreakdownProtection {

    private final Map<String, Object> lockMap = new ConcurrentHashMap<>();

    /**
     * 防止缓存击穿的查询方法
     */
    public <T> T queryWithMutex(String key, Function<String, T> dbQuery,
                               Duration timeout, Class<T> type) {
        // 1. 从缓存查询
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return (T) cached;
        }

        // 2. 获取互斥锁
        String lockKey = "lock:" + key;
        synchronized (lockMap.computeIfAbsent(lockKey, k -> new Object())) {
            try {
                // 3. 双重检查
                cached = redisTemplate.opsForValue().get(key);
                if (cached != null) {
                    return (T) cached;
                }

                // 4. 查询数据库
                T result = dbQuery.apply(key);

                // 5. 写入缓存
                if (result != null) {
                    redisTemplate.opsForValue().set(key, result, timeout);
                }

                return result;
            } finally {
                lockMap.remove(lockKey);
            }
        }
    }
}
```

## 最佳实践

### 1. 缓存设计原则
- **合理的过期时间**：根据数据特性设置合适的TTL
- **缓存分层**：热点数据使用本地缓存，共享数据使用分布式缓存
- **缓存预热**：系统启动时预加载热点数据
- **缓存更新**：数据变更时及时更新或删除缓存

### 2. 性能优化
- **批量操作**：使用pipeline减少网络开销
- **压缩存储**：大对象使用压缩算法存储
- **连接池**：合理配置Redis连接池参数
- **监控告警**：监控缓存命中率和性能指标

### 3. 安全考虑
- **数据加密**：敏感数据缓存前进行加密
- **访问控制**：限制缓存操作的权限
- **数据脱敏**：缓存中避免存储完整的敏感信息
- **定期清理**：定期清理过期和无用的缓存数据

## 常见问题

### Q: 如何解决缓存一致性问题？

**解决方案**：
```java
// 1. 先更新数据库，再删除缓存
@Transactional
public void updateUser(SysUser user) {
    // 更新数据库
    userMapper.updateById(user);

    // 删除缓存
    cacheManager.evict("user", user.getUserId());
}

// 2. 使用延时双删策略
@Transactional
public void updateUserWithDelayDelete(SysUser user) {
    // 删除缓存
    cacheManager.evict("user", user.getUserId());

    // 更新数据库
    userMapper.updateById(user);

    // 延时删除缓存
    CompletableFuture.runAsync(() -> {
        try {
            Thread.sleep(1000); // 延时1秒
            cacheManager.evict("user", user.getUserId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });
}
```

### Q: 如何监控缓存性能？

**解决方案**：
```java
@Component
public class CacheMetrics {

    private final MeterRegistry meterRegistry;

    public CacheMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordCacheHit(String cacheName) {
        Counter.builder("cache.hit")
            .tag("cache", cacheName)
            .register(meterRegistry)
            .increment();
    }

    public void recordCacheMiss(String cacheName) {
        Counter.builder("cache.miss")
            .tag("cache", cacheName)
            .register(meterRegistry)
            .increment();
    }

    public void recordCacheEviction(String cacheName) {
        Counter.builder("cache.eviction")
            .tag("cache", cacheName)
            .register(meterRegistry)
            .increment();
    }
}
```

### Q: 如何实现分布式缓存的高可用？

**解决方案**：
```yaml
# Redis集群配置
spring:
  redis:
    cluster:
      nodes:
        - 192.168.1.10:7000
        - 192.168.1.10:7001
        - 192.168.1.11:7000
        - 192.168.1.11:7001
        - 192.168.1.12:7000
        - 192.168.1.12:7001
      max-redirects: 3
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: -1ms
      cluster:
        refresh:
          adaptive: true
          period: 30s
```
```
