package com.mars.admin.modules.base.controller;

import com.mars.admin.framework.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.mars.admin.framework.config.RedisConfig;

/**
 * 缓存监控Controller
 * 提供Redis缓存监控、缓存列表管理等功能
 *
 * @author Mars
 */
@RestController
@RequestMapping("/system/cache")
@Tag(name = "缓存监控管理", description = "缓存监控和管理相关接口")
public class CacheMonitorController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 兼容序列化器，用于处理缓存详情获取
    private final RedisConfig.CompatibleRedisSerializer compatibleSerializer = new RedisConfig.CompatibleRedisSerializer();

    /**
     * 获取缓存监控信息
     */
    @GetMapping("/monitor")
    @Operation(summary = "获取缓存监控信息", description = "获取Redis服务器信息、内存使用情况、连接数等监控数据")
    public Result<Map<String, Object>> getCacheMonitor() {
        try {
            Map<String, Object> result = new HashMap<>();

            // Redis 服务器信息
            Map<String, Object> redisInfo = new HashMap<>();
            try {
                Properties info = redisTemplate.getConnectionFactory().getConnection().info();
                redisInfo.put("status", "connected");

                // 解析 Redis INFO 命令结果
                if (info != null) {
                    redisInfo.put("redis_version", info.getProperty("redis_version", "未知"));
                    redisInfo.put("redis_mode", info.getProperty("redis_mode", "standalone"));
                    redisInfo.put("tcp_port", info.getProperty("tcp_port", "6379"));
                    redisInfo.put("uptime_in_seconds", info.getProperty("uptime_in_seconds", "0"));
                    redisInfo.put("uptime_in_days", info.getProperty("uptime_in_days", "0"));
                    redisInfo.put("connected_clients", info.getProperty("connected_clients", "0"));
                    redisInfo.put("used_memory_human", info.getProperty("used_memory_human", "0B"));
                    redisInfo.put("used_memory_peak_human", info.getProperty("used_memory_peak_human", "0B"));
                    redisInfo.put("maxmemory_human", info.getProperty("maxmemory_human", "0B"));
                    redisInfo.put("mem_fragmentation_ratio", info.getProperty("mem_fragmentation_ratio", "1.0"));
                    redisInfo.put("keyspace_hits", info.getProperty("keyspace_hits", "0"));
                    redisInfo.put("keyspace_misses", info.getProperty("keyspace_misses", "0"));
                    redisInfo.put("total_commands_processed", info.getProperty("total_commands_processed", "0"));
                    redisInfo.put("instantaneous_ops_per_sec", info.getProperty("instantaneous_ops_per_sec", "0"));
                }

                // 计算命中率
                long hits = Long.parseLong(redisInfo.getOrDefault("keyspace_hits", "0").toString());
                long misses = Long.parseLong(redisInfo.getOrDefault("keyspace_misses", "0").toString());
                long total = hits + misses;
                double hitRate = total > 0 ? (double) hits / total * 100 : 0.0;
                redisInfo.put("hit_rate", String.format("%.2f%%", hitRate));

            } catch (Exception redisException) {
                // Redis连接失败时的处理
                redisInfo.put("status", "disconnected");
                redisInfo.put("error", "Redis连接失败: " + redisException.getMessage());
                redisInfo.put("redis_version", "未知");
                redisInfo.put("hit_rate", "0.00%");
            }

            result.put("redis", redisInfo);

            // JVM 内存信息
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
            MemoryUsage nonHeapMemory = memoryBean.getNonHeapMemoryUsage();

            Map<String, Object> jvmInfo = new HashMap<>();
            jvmInfo.put("heap_used", formatBytes(heapMemory.getUsed()));
            jvmInfo.put("heap_committed", formatBytes(heapMemory.getCommitted()));
            jvmInfo.put("heap_max", formatBytes(heapMemory.getMax()));
            jvmInfo.put("non_heap_used", formatBytes(nonHeapMemory.getUsed()));
            jvmInfo.put("non_heap_committed", formatBytes(nonHeapMemory.getCommitted()));
            jvmInfo.put("heap_usage", String.format("%.2f%%", (double) heapMemory.getUsed() / heapMemory.getMax() * 100));

            result.put("jvm", jvmInfo);

            // 缓存统计信息
            Map<String, Object> cacheStats = new HashMap<>();

            // 统计不同缓存空间的键数量
            Map<String, Long> cacheSpaces = new HashMap<>();
            cacheSpaces.put("dictType", countKeysWithPattern("dictType*"));
            cacheSpaces.put("dictData", countKeysWithPattern("dictData*"));
            cacheSpaces.put("post", countKeysWithPattern("post*"));
            cacheSpaces.put("user", countKeysWithPattern("user*"));
            cacheSpaces.put("config", countKeysWithPattern("config*"));
            cacheSpaces.put("menu", countKeysWithPattern("menu*"));
            cacheSpaces.put("role", countKeysWithPattern("role*"));
            cacheSpaces.put("dept", countKeysWithPattern("dept*"));

            long totalKeys = cacheSpaces.values().stream().mapToLong(Long::longValue).sum();
            cacheStats.put("total_keys", totalKeys);
            cacheStats.put("cache_spaces", cacheSpaces);

            result.put("cache", cacheStats);

            // 服务器信息
            Map<String, Object> serverInfo = new HashMap<>();
            serverInfo.put("server_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            serverInfo.put("java_version", System.getProperty("java.version"));
            serverInfo.put("os_name", System.getProperty("os.name"));
            serverInfo.put("os_arch", System.getProperty("os.arch"));

            result.put("server", serverInfo);

            return Result.success(result);

        } catch (Exception e) {
            return Result.error("获取缓存监控信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取缓存列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取缓存列表", description = "分页获取Redis中的缓存键列表")
    public Result<Map<String, Object>> getCacheList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "缓存键模式") @RequestParam(required = false) String pattern,
            @Parameter(description = "缓存空间") @RequestParam(required = false) String cacheSpace) {

        try {
            // 构建搜索模式
            String searchPattern = "*";
            if (cacheSpace != null && !cacheSpace.trim().isEmpty()) {
                searchPattern = cacheSpace + "*";
            } else if (pattern != null && !pattern.trim().isEmpty()) {
                searchPattern = "*" + pattern + "*";
            }

            // 获取所有匹配的键
            Set<String> keys = redisTemplate.keys(searchPattern);
            if (keys == null) {
                keys = new HashSet<>();
            }

            // 转换为列表并排序
            List<String> keyList = keys.stream()
                    .sorted()
                    .collect(Collectors.toList());

            // 分页处理
            int total = keyList.size();
            int start = (current - 1) * size;
            int end = Math.min(start + size, total);

            List<String> pagedKeys = start < total ? keyList.subList(start, end) : new ArrayList<>();

            // 获取每个键的详细信息
            List<Map<String, Object>> cacheItems = new ArrayList<>();
            for (String key : pagedKeys) {
                Map<String, Object> item = getCacheItemInfo(key);
                if (item != null) {
                    cacheItems.add(item);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("records", cacheItems);
            result.put("total", total);
            result.put("current", current);
            result.put("size", size);
            result.put("pages", (int) Math.ceil((double) total / size));

            return Result.success(result);

        } catch (Exception e) {
            return Result.error("获取缓存列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取缓存详情
     */
    @GetMapping("/detail/{key}")
    @Operation(summary = "获取缓存详情", description = "获取指定缓存键的详细信息和值")
    public Result<Map<String, Object>> getCacheDetail(@Parameter(description = "缓存键") @PathVariable String key) {
        try {
            // 解码key（前端可能会进行URL编码）
            key = java.net.URLDecoder.decode(key, "UTF-8");

            Map<String, Object> detail = getCacheItemInfo(key);
            if (detail == null) {
                return Result.error("缓存键不存在: " + key);
            }

            // 使用兼容序列化器获取缓存值
            Object value = getCacheValueWithCompatibleSerializer(key);
            String formattedValue = formatCacheValueForDetail(value);
            detail.put("value", formattedValue);

            return Result.success(detail);

        } catch (Exception e) {
            return Result.error("获取缓存详情失败: " + e.getMessage());
        }
    }

    /**
     * 删除缓存
     */
    @DeleteMapping("/delete/{key}")
    @Operation(summary = "删除缓存", description = "删除指定的缓存键")
    public Result<String> deleteCache(@Parameter(description = "缓存键") @PathVariable String key) {
        try {
            // 解码key
            key = java.net.URLDecoder.decode(key, "UTF-8");

            Boolean deleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(deleted)) {
                return Result.success("缓存删除成功");
            } else {
                return Result.error("缓存键不存在或删除失败");
            }

        } catch (Exception e) {
            return Result.error("删除缓存失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除缓存
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除缓存", description = "批量删除多个缓存键")
    public Result<String> deleteCacheBatch(@Parameter(description = "缓存键列表") @RequestBody List<String> keys) {
        try {
            if (keys == null || keys.isEmpty()) {
                return Result.error("缓存键列表不能为空");
            }

            // 解码所有keys
            List<String> decodedKeys = keys.stream()
                    .map(key -> {
                        try {
                            return java.net.URLDecoder.decode(key, "UTF-8");
                        } catch (Exception e) {
                            return key;
                        }
                    })
                    .collect(Collectors.toList());

            Long deletedCount = redisTemplate.delete(decodedKeys);
            return Result.success("成功删除 " + deletedCount + " 个缓存键");

        } catch (Exception e) {
            return Result.error("批量删除缓存失败: " + e.getMessage());
        }
    }

    /**
     * 清空指定缓存空间
     */
    @DeleteMapping("/clear/{cacheSpace}")
    @Operation(summary = "清空缓存空间", description = "清空指定缓存空间的所有数据")
    public Result<String> clearCacheSpace(@Parameter(description = "缓存空间名称") @PathVariable String cacheSpace) {
        try {
            String pattern = cacheSpace + "*";
            Set<String> keys = redisTemplate.keys(pattern);

            if (keys != null && !keys.isEmpty()) {
                Long deletedCount = redisTemplate.delete(keys);
                return Result.success("成功清空缓存空间 " + cacheSpace + "，删除了 " + deletedCount + " 个缓存键");
            } else {
                return Result.success("缓存空间 " + cacheSpace + " 已经是空的");
            }

        } catch (Exception e) {
            return Result.error("清空缓存空间失败: " + e.getMessage());
        }
    }

    /**
     * 清除所有缓存（用于修复序列化问题）
     */
    @DeleteMapping("/clear-all")
    @Operation(summary = "清除所有缓存", description = "清除Redis中的所有缓存数据，用于修复序列化问题")
    public Result<String> clearAllCache() {
        try {
            // 获取所有key
            Set<String> keys = redisTemplate.keys("*");

            if (keys != null && !keys.isEmpty()) {
                Long deletedCount = redisTemplate.delete(keys);
                return Result.success("成功清除所有缓存，共删除 " + deletedCount + " 个键。请重新访问功能以生成新的缓存数据。");
            } else {
                return Result.success("缓存已经是空的，无需清除。");
            }

        } catch (Exception e) {
            return Result.error("清除所有缓存失败: " + e.getMessage());
        }
    }

    /**
     * 获取缓存统计信息
     */
    @GetMapping("/stats")
    @Operation(summary = "获取缓存统计信息", description = "获取各缓存空间的统计信息")
    public Result<Map<String, Object>> getCacheStats() {
        try {
            Map<String, Object> stats = new HashMap<>();

            // 统计各缓存空间
            List<Map<String, Object>> spaceStats = new ArrayList<>();

            String[] cacheSpaces = {"dictType", "dictData", "post", "user", "config", "menu", "role", "dept"};
            String[] spaceNames = {"字典类型", "字典数据", "岗位管理", "用户管理", "系统配置", "菜单管理", "角色管理", "部门管理"};

            for (int i = 0; i < cacheSpaces.length; i++) {
                String space = cacheSpaces[i];
                String name = spaceNames[i];
                long count = countKeysWithPattern(space + "*");

                Map<String, Object> spaceStat = new HashMap<>();
                spaceStat.put("space", space);
                spaceStat.put("name", name);
                spaceStat.put("count", count);
                spaceStat.put("color", getSpaceColor(i));

                spaceStats.add(spaceStat);
            }

            // 按键数量排序
            spaceStats.sort((a, b) -> Long.compare((Long) b.get("count"), (Long) a.get("count")));

            stats.put("spaces", spaceStats);
            stats.put("total", spaceStats.stream().mapToLong(s -> (Long) s.get("count")).sum());

            return Result.success(stats);

        } catch (Exception e) {
            return Result.error("获取缓存统计失败: " + e.getMessage());
        }
    }

    // 私有辅助方法

    /**
     * 统计匹配模式的键数量
     */
    private long countKeysWithPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            return keys != null ? keys.size() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取缓存项详细信息
     */
    private Map<String, Object> getCacheItemInfo(String key) {
        try {
            // 检查键是否存在
            if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                return null;
            }

            Map<String, Object> item = new HashMap<>();
            item.put("key", key);

            // 获取TTL
            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            item.put("ttl", ttl);
            item.put("ttl_text", formatTTL(ttl));

            // 获取类型
            String type = redisTemplate.type(key).code();
            item.put("type", type);

            // 获取大小（估算）
            try {
                Object value = redisTemplate.opsForValue().get(key);
                if (value != null) {
                    String size = estimateSize(value);
                    item.put("size", size);
                }
            } catch (Exception e) {
                item.put("size", "未知");
            }

            // 解析缓存空间
            String cacheSpace = "other";
            if (key.contains(":")) {
                cacheSpace = key.substring(0, key.indexOf(":"));
            }
            item.put("cache_space", cacheSpace);
            item.put("cache_space_name", getCacheSpaceName(cacheSpace));

            return item;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 格式化字节数
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * 格式化TTL
     */
    private String formatTTL(Long ttl) {
        if (ttl == null || ttl < 0) return "永不过期";
        if (ttl == 0) return "已过期";
        if (ttl < 60) return ttl + "秒";
        if (ttl < 3600) return (ttl / 60) + "分钟";
        if (ttl < 86400) return (ttl / 3600) + "小时";
        return (ttl / 86400) + "天";
    }

    /**
     * 估算对象大小
     */
    private String estimateSize(Object obj) {
        if (obj == null) return "0 B";
        String str = obj.toString();
        int bytes = str.getBytes().length;
        return formatBytes(bytes);
    }

    /**
     * 获取缓存空间名称
     */
    private String getCacheSpaceName(String space) {
        switch (space) {
            case "dictType": return "字典类型";
            case "dictData": return "字典数据";
            case "post": return "岗位管理";
            case "user": return "用户管理";
            case "config": return "系统配置";
            case "menu": return "菜单管理";
            case "role": return "角色管理";
            case "dept": return "部门管理";
            default: return "其他";
        }
    }

    /**
     * 获取缓存空间颜色
     */
    private String getSpaceColor(int index) {
        String[] colors = {
            "#1890ff", "#52c41a", "#722ed1", "#fa8c16",
            "#eb2f96", "#13c2c2", "#f5222d", "#a0d911"
        };
        return colors[index % colors.length];
    }

    /**
     * 使用兼容序列化器获取缓存值
     */
    private Object getCacheValueWithCompatibleSerializer(String key) {
        try {
            // 获取原始字节数据
            byte[] rawBytes = redisTemplate.getConnectionFactory().getConnection().get(key.getBytes());
            if (rawBytes == null) {
                return null;
            }

            // 首先检查是否已经是UTF-8字符串格式（如SaToken的JSON数据）
            try {
                String strContent = new String(rawBytes, "UTF-8");
                // 检查是否是有效的JSON字符串
                if (isValidJsonString(strContent)) {
                    return strContent; // 直接返回JSON字符串，在格式化时处理
                }
            } catch (Exception ignored) {
                // 忽略字符串检查异常
            }

            // 如果不是UTF-8字符串，使用兼容序列化器反序列化
            return compatibleSerializer.deserialize(rawBytes);
        } catch (Exception e) {
            // 如果所有方式都失败，返回错误信息
            return "缓存值获取失败: " + e.getMessage();
        }
    }

    /**
     * 检查字符串是否为有效的JSON格式（更宽松的检查）
     */
    private boolean isValidJsonString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }

        str = str.trim();
        // 检查是否以JSON对象或数组开头和结尾
        return (str.startsWith("{") && str.endsWith("}")) ||
               (str.startsWith("[") && str.endsWith("]")) ||
               str.startsWith("\"") && str.endsWith("\""); // JSON字符串
    }

    /**
     * 格式化缓存值用于详情显示（针对已反序列化的对象）
     */
    private String formatCacheValueForDetail(Object value) {
        if (value == null) {
            return "null";
        }

        try {
            // 如果是字符串且包含错误信息，直接返回
            if (value instanceof String && ((String) value).contains("缓存值获取失败")) {
                return (String) value;
            }

            // 如果是字符串类型，检查是否是JSON
            if (value instanceof String) {
                String strValue = (String) value;
                return formatJsonForDisplay(strValue);
            }

            // 其他对象类型，转换为格式化的JSON
            return objectToJsonString(value);

        } catch (Exception e) {
            return "数据格式化失败: " + e.getMessage() +
                   "\n数据类型: " + value.getClass().getSimpleName() +
                   "\n原始内容: " + String.valueOf(value).substring(0, Math.min(String.valueOf(value).length(), 500)) +
                   (String.valueOf(value).length() > 500 ? "..." : "");
        }
    }

    /**
     * 格式化JSON字符串用于显示（处理包含@class信息的JSON）
     */
    private String formatJsonForDisplay(String jsonStr) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) {
            return jsonStr;
        }

        try {
            // 使用更宽松的ObjectMapper来格式化JSON
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            // 忽略未知属性，避免@class等字段导致的问题
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // 尝试解析并格式化
            Object jsonObj = mapper.readValue(jsonStr, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
        } catch (Exception e) {
            // 如果标准JSON解析失败，尝试简单的字符串格式化
            try {
                // 手动格式化JSON字符串（添加换行和缩进）
                return formatJsonStringManually(jsonStr);
            } catch (Exception e2) {
                // 都失败了，返回原始字符串加上类型说明
                return "JSON字符串 (格式化失败):\n" + jsonStr;
            }
        }
    }

    /**
     * 手动格式化JSON字符串
     */
    private String formatJsonStringManually(String jsonStr) {
        if (jsonStr == null) return null;

        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        boolean inQuotes = false;
        boolean escaped = false;

        for (char c : jsonStr.toCharArray()) {
            if (escaped) {
                formatted.append(c);
                escaped = false;
                continue;
            }

            if (c == '\\') {
                formatted.append(c);
                escaped = true;
                continue;
            }

            if (c == '"') {
                inQuotes = !inQuotes;
                formatted.append(c);
                continue;
            }

            if (inQuotes) {
                formatted.append(c);
                continue;
            }

            switch (c) {
                case '{':
                case '[':
                    formatted.append(c).append('\n');
                    indentLevel++;
                    appendIndent(formatted, indentLevel);
                    break;
                case '}':
                case ']':
                    formatted.append('\n');
                    indentLevel--;
                    appendIndent(formatted, indentLevel);
                    formatted.append(c);
                    break;
                case ',':
                    formatted.append(c).append('\n');
                    appendIndent(formatted, indentLevel);
                    break;
                case ':':
                    formatted.append(c).append(' ');
                    break;
                default:
                    formatted.append(c);
            }
        }

        return formatted.toString();
    }

    /**
     * 添加缩进
     */
    private void appendIndent(StringBuilder sb, int level) {
        for (int i = 0; i < level * 2; i++) {
            sb.append(' ');
        }
    }

    /**
     * 格式化缓存值，处理各种数据类型包括序列化对象
     */
    private String formatCacheValue(Object value) {
        if (value == null) {
            return "null";
        }

        try {
            // 如果是字符串类型，直接返回
            if (value instanceof String) {
                String strValue = (String) value;
                // 检查是否是JSON格式
                if (isValidJson(strValue)) {
                    return formatJson(strValue);
                }
                return strValue;
            }

            // 如果是字节数组（Spring Cache JDK序列化数据）
            if (value instanceof byte[]) {
                byte[] bytes = (byte[]) value;
                return handleSerializedData(bytes);
            }

            // 如果直接是Java对象（新的Jackson序列化后的对象）
            return objectToJsonString(value);

        } catch (Exception e) {
            // 如果所有方法都失败，返回对象的toString()和类型信息
            return "数据解析失败: " + e.getMessage() +
                   "\n数据类型: " + value.getClass().getSimpleName() +
                   "\n原始内容: " + String.valueOf(value).substring(0, Math.min(String.valueOf(value).length(), 500)) +
                   (String.valueOf(value).length() > 500 ? "..." : "");
        }
    }

    /**
     * 处理序列化数据（优先处理JDK序列化）
     */
    private String handleSerializedData(byte[] bytes) {
        // 尝试JDK反序列化（Spring Cache默认方式）
        try {
            Object deserializedObj = deserializeJdkObject(bytes);
            return "JDK序列化数据:\n" + objectToJsonString(deserializedObj);
        } catch (Exception jdkException) {
            // JDK反序列化失败，尝试其他方式
            try {
                // 检查是否是UTF-8字符串
                String strContent = new String(bytes, "UTF-8");
                if (isValidJson(strContent)) {
                    return "UTF-8字符串数据:\n" + formatJson(strContent);
                } else if (isPrintableString(strContent)) {
                    return "UTF-8字符串数据:\n" + strContent;
                }
            } catch (Exception strException) {
                // 忽略字符串解析异常
            }

            // 都失败了，返回十六进制表示和错误信息
            return "无法解析的二进制数据:\n" +
                   "长度: " + bytes.length + " 字节\n" +
                   "JDK反序列化错误: " + jdkException.getMessage() + "\n" +
                   "十六进制内容: " + bytesToHex(bytes, Math.min(bytes.length, 200)) +
                   (bytes.length > 200 ? "..." : "");
        }
    }

    /**
     * 检查字符串是否包含可打印字符
     */
    private boolean isPrintableString(String str) {
        if (str == null || str.length() > 10000) { // 避免过长字符串
            return false;
        }
        return str.chars().allMatch(c -> c >= 32 && c <= 126 || Character.isWhitespace(c));
    }

    /**
     * 检查字符串是否为有效的JSON
     */
    private boolean isValidJson(String str) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.readTree(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 格式化JSON字符串
     */
    private String formatJson(String json) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Object obj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return json;
        }
    }

    /**
     * 将对象转换为JSON字符串
     */
    private String objectToJsonString(Object obj) {
        try {
            // 创建一个更宽松的ObjectMapper配置
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

            // 配置序列化选项以避免常见问题
            mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.setVisibility(com.fasterxml.jackson.annotation.PropertyAccessor.FIELD,
                               com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY);

            // 注册JavaTime模块处理LocalDateTime等
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            // 序列化失败时，尝试构建更友好的显示格式
            return buildUserFriendlyDisplay(obj, e);
        }
    }

    /**
     * 构建用户友好的显示格式（当JSON序列化失败时）
     */
    private String buildUserFriendlyDisplay(Object obj, Exception originalException) {
        try {
            if (obj == null) {
                return "null";
            }

            // 如果是集合类型，逐个处理元素
            if (obj instanceof java.util.Collection) {
                java.util.Collection<?> collection = (java.util.Collection<?>) obj;
                StringBuilder sb = new StringBuilder();
                sb.append("集合数据 (").append(collection.size()).append(" 个元素):\n");

                int index = 0;
                for (Object item : collection) {
                    sb.append("[").append(index++).append("] ");
                    sb.append(formatSingleObject(item)).append("\n");
                    if (index > 10) { // 限制显示数量
                        sb.append("... (还有 ").append(collection.size() - 10).append(" 个元素)\n");
                        break;
                    }
                }

                return sb.toString();
            }

            // 单个对象
            return formatSingleObject(obj);

        } catch (Exception e) {
            return "数据格式化失败 (原因: " + originalException.getMessage() + "): " +
                   obj.toString().substring(0, Math.min(obj.toString().length(), 500));
        }
    }

    /**
     * 格式化单个对象为友好的显示格式
     */
    private String formatSingleObject(Object obj) {
        if (obj == null) return "null";

        String objStr = obj.toString();

        // 如果是我们的实体对象（包含字段名），进行格式化
        if (objStr.contains("(") && objStr.contains("=") && objStr.contains(")")) {
            return formatEntityObject(objStr);
        }

        return objStr;
    }

    /**
     * 格式化实体对象的toString输出
     */
    private String formatEntityObject(String objStr) {
        try {
            // 解析类似 "SysPost(id=1, postCode=CEO, ...)" 的格式
            int startIndex = objStr.indexOf('(');
            int endIndex = objStr.lastIndexOf(')');

            if (startIndex > 0 && endIndex > startIndex) {
                String className = objStr.substring(0, startIndex);
                String fieldsStr = objStr.substring(startIndex + 1, endIndex);

                StringBuilder formatted = new StringBuilder();
                formatted.append("{\n");
                formatted.append("  \"@type\": \"").append(className).append("\",\n");

                String[] fields = fieldsStr.split(", ");
                for (int i = 0; i < fields.length; i++) {
                    String field = fields[i].trim();
                    if (field.contains("=")) {
                        String[] keyValue = field.split("=", 2);
                        String key = keyValue[0].trim();
                        String value = keyValue.length > 1 ? keyValue[1].trim() : "null";

                        // 简单的值类型判断和格式化
                        if ("null".equals(value)) {
                            formatted.append("  \"").append(key).append("\": null");
                        } else if (value.matches("\\d+")) {
                            formatted.append("  \"").append(key).append("\": ").append(value);
                        } else {
                            formatted.append("  \"").append(key).append("\": \"").append(value).append("\"");
                        }

                        if (i < fields.length - 1) {
                            formatted.append(",");
                        }
                        formatted.append("\n");
                    }
                }
                formatted.append("}");

                return formatted.toString();
            }
        } catch (Exception e) {
            // 解析失败，返回原始字符串
        }

        return objStr;
    }

    /**
     * 反序列化字节数组为对象（JDK序列化）
     */
    private Object deserializeJdkObject(byte[] bytes) throws Exception {
        java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bytes);
        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bis);
        try {
            return ois.readObject();
        } finally {
            ois.close();
            bis.close();
        }
    }

    /**
     * 反序列化字节数组为对象（通用方法，保持向后兼容）
     */
    private Object deserializeObject(byte[] bytes) throws Exception {
        return deserializeJdkObject(bytes);
    }

    /**
     * 将字节数组转换为十六进制字符串
     */
    private String bytesToHex(byte[] bytes, int maxLength) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < Math.min(bytes.length, maxLength); i++) {
            result.append(String.format("%02x ", bytes[i]));
        }
        return result.toString().trim();
    }
}
