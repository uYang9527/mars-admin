package com.mars.admin.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SQL日志工具类
 * 用于记录和管理SQL执行日志
 */
public class SqlLogUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(SqlLogUtil.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 存储最近的SQL日志记录
    private static final List<Map<String, Object>> sqlLogs = new ArrayList<>();
    private static final int MAX_LOGS = 100;
    
    /**
     * 记录查询日志
     */
    public static void logQuery(String executor, String sql, long executionTime, String status) {
        try {
            Map<String, Object> logEntry = new ConcurrentHashMap<>();
            logEntry.put("timestamp", LocalDateTime.now().format(formatter));
            logEntry.put("executor", executor);
            logEntry.put("sql", sql);
            logEntry.put("executionTime", executionTime);
            logEntry.put("status", status);
            logEntry.put("type", extractSqlType(sql));
            
            synchronized (sqlLogs) {
                sqlLogs.add(0, logEntry); // 添加到列表开头
                if (sqlLogs.size() > MAX_LOGS) {
                    sqlLogs.remove(sqlLogs.size() - 1); // 移除最后一个
                }
            }
            
            // 记录到日志文件
            logger.info("SQL执行 [{}] - 执行者: {}, 耗时: {}ms, 状态: {}, SQL: {}", 
                       logEntry.get("timestamp"), executor, executionTime, status, 
                       sql.length() > 100 ? sql.substring(0, 100) + "..." : sql);
                       
        } catch (Exception e) {
            logger.error("记录SQL日志失败", e);
        }
    }
    
    /**
     * 获取SQL日志列表
     */
    public static List<Map<String, Object>> getSqlLogs() {
        synchronized (sqlLogs) {
            return new ArrayList<>(sqlLogs);
        }
    }
    
    /**
     * 清空SQL日志
     */
    public static void clearLogs() {
        synchronized (sqlLogs) {
            sqlLogs.clear();
        }
    }
    
    /**
     * 提取SQL类型
     */
    private static String extractSqlType(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return "UNKNOWN";
        }
        
        String upperSql = sql.trim().toUpperCase();
        if (upperSql.startsWith("SELECT")) return "SELECT";
        if (upperSql.startsWith("INSERT")) return "INSERT";
        if (upperSql.startsWith("UPDATE")) return "UPDATE";
        if (upperSql.startsWith("DELETE")) return "DELETE";
        if (upperSql.startsWith("CREATE")) return "CREATE";
        if (upperSql.startsWith("DROP")) return "DROP";
        if (upperSql.startsWith("ALTER")) return "ALTER";
        
        return "OTHER";
    }
} 