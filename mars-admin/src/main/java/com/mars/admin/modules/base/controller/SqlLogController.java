package com.mars.admin.modules.base.controller;

import com.mars.admin.framework.util.SqlCopyUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * SQL日志查看控制器
 * 提供SQL日志查看和复制功能的Web接口
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@Slf4j
@RestController
@RequestMapping("/sql-log")
@Tag(name = "SQL日志管理", description = "SQL执行日志查看和复制功能")
public class SqlLogController {

    // 存储最近的SQL执行记录（最多保存100条）
    private static final ConcurrentLinkedQueue<Map<String, Object>> sqlLogs = new ConcurrentLinkedQueue<>();
    private static final int MAX_LOG_SIZE = 100;

    /**
     * 添加SQL执行记录
     */
    public static void addSqlLog(String sql, String sqlType, long elapsedTime, String tableName) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("sql", sql);
        logEntry.put("sqlType", sqlType);
        logEntry.put("elapsedTime", elapsedTime);
        logEntry.put("tableName", tableName);
        logEntry.put("timestamp", System.currentTimeMillis());
        logEntry.put("formattedSql", SqlCopyUtil.getCopyHint(sql));

        sqlLogs.offer(logEntry);

        // 保持队列大小不超过限制
        while (sqlLogs.size() > MAX_LOG_SIZE) {
            sqlLogs.poll();
        }
    }

    /**
     * 获取最近的SQL执行日志
     */
    @GetMapping("/recent")
    @Operation(summary = "获取最近的SQL执行日志", description = "获取最近执行的SQL语句列表，便于查看和复制")
    public Map<String, Object> getRecentSqlLogsApi(@RequestParam(defaultValue = "20") int limit) {
        return getRecentSqlLogs(limit);
    }

    /**
     * 获取最近的SQL执行日志（静态方法供其他控制器调用）
     */
    public static Map<String, Object> getRecentSqlLogs(int limit) {
        List<Map<String, Object>> logs = new ArrayList<>(sqlLogs);

        // 按时间倒序，取最新的记录
        logs.sort((a, b) -> Long.compare((Long) b.get("timestamp"), (Long) a.get("timestamp")));

        if (logs.size() > limit) {
            logs = logs.subList(0, limit);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("logs", logs);
        result.put("total", sqlLogs.size());
        result.put("message", "获取SQL日志成功");

        return result;
    }

    /**
     * 格式化SQL用于复制
     */
    @PostMapping("/format")
    @Operation(summary = "格式化SQL", description = "将SQL语句格式化为便于复制的格式")
    public Map<String, Object> formatSql(@RequestBody Map<String, String> request) {
        String sql = request.get("sql");

        if (sql == null || sql.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "SQL不能为空");
            return error;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("originalSql", sql);
        result.put("formattedSql", formatSqlForCopy(sql));
        result.put("copyHint", SqlCopyUtil.getCopyHint(sql));
        result.put("message", "SQL格式化成功");

        return result;
    }

    /**
     * 清空SQL日志
     */
    @DeleteMapping("/clear")
    @Operation(summary = "清空SQL日志", description = "清空所有SQL执行日志记录")
    public Map<String, Object> clearSqlLogsApi() {
        return clearSqlLogs();
    }

    /**
     * 清空SQL日志（静态方法供其他控制器调用）
     */
    public static Map<String, Object> clearSqlLogs() {
        int count = sqlLogs.size();
        sqlLogs.clear();

        Map<String, Object> result = new HashMap<>();
        result.put("message", "已清空 " + count + " 条SQL日志记录");

        return result;
    }

    /**
     * 格式化SQL用于复制
     */
    private String formatSqlForCopy(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return "";
        }

        return sql.trim()
                .replaceAll("(?i)\\bSELECT\\b", "\nSELECT")
                .replaceAll("(?i)\\bFROM\\b", "\nFROM")
                .replaceAll("(?i)\\bWHERE\\b", "\nWHERE")
                .replaceAll("(?i)\\bAND\\b", "\n  AND")
                .replaceAll("(?i)\\bOR\\b", "\n  OR")
                .replaceAll("(?i)\\bJOIN\\b", "\nJOIN")
                .replaceAll("(?i)\\bLEFT JOIN\\b", "\nLEFT JOIN")
                .replaceAll("(?i)\\bRIGHT JOIN\\b", "\nRIGHT JOIN")
                .replaceAll("(?i)\\bINNER JOIN\\b", "\nINNER JOIN")
                .replaceAll("(?i)\\bORDER BY\\b", "\nORDER BY")
                .replaceAll("(?i)\\bGROUP BY\\b", "\nGROUP BY")
                .replaceAll("(?i)\\bHAVING\\b", "\nHAVING")
                .replaceAll("(?i)\\bLIMIT\\b", "\nLIMIT")
                .replaceAll("(?i)\\bINSERT INTO\\b", "\nINSERT INTO")
                .replaceAll("(?i)\\bVALUES\\b", "\nVALUES")
                .replaceAll("(?i)\\bUPDATE\\b", "\nUPDATE")
                .replaceAll("(?i)\\bSET\\b", "\nSET")
                .replaceAll("\\n\\s*\\n", "\n")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
