package com.mars.admin.modules.base.controller;

import com.mars.admin.framework.config.SqlLogProperties;
import com.mars.admin.framework.util.SqlLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * SQL日志页面控制器
 * 提供登录认证和SQL执行功能
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@Controller
@RequestMapping("/sqlLog")
public class SqlLogPageController {

    @Autowired
    private SqlLogProperties sqlLogProperties;

    @Autowired
    private DataSource dataSource;


    /**
     * 检查认证状态
     */
    @GetMapping("/checkAuth")
    @ResponseBody
    public String checkAuth(HttpSession session) {
        Boolean authenticated = (Boolean) session.getAttribute("sql_log_authenticated");
        Long loginTime = (Long) session.getAttribute("sql_log_login_time");

        if (authenticated != null && authenticated && loginTime != null) {
            // 检查是否超过30分钟（1800000毫秒）
            long currentTime = System.currentTimeMillis();
            if (currentTime - loginTime > 1800000) { // 30分钟
                // 会话超时，清除认证状态
                session.removeAttribute("sql_log_authenticated");
                session.removeAttribute("sql_log_login_time");
                return "session_expired";
            }
            return "authenticated";
        }
        return "not_authenticated";
    }

    /**
     * 登录验证
     */
    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        if (sqlLogProperties.getUsername().equals(username) &&
                sqlLogProperties.getPassword().equals(password)) {
            session.setAttribute("sql_log_authenticated", true);
            session.setAttribute("sql_log_login_time", System.currentTimeMillis());
            // 设置会话超时时间为30分钟
            session.setMaxInactiveInterval(1800);
            return "success";
        }
        return "failure";
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @ResponseBody
    public String logout(HttpSession session) {
        session.removeAttribute("sql_log_authenticated");
        session.removeAttribute("sql_log_login_time");
        session.invalidate(); // 完全销毁会话
        return "success";
    }

    /**
     * 执行SQL查询（仅支持SELECT）
     */
    @PostMapping("/execute")
    @ResponseBody
    public Map<String, Object> executeSql(@RequestBody Map<String, String> request,
                                          HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 检查认证状态
        String authStatus = checkAuth(session);
        if (!"authenticated".equals(authStatus)) {
            result.put("success", false);
            result.put("message", "session_expired".equals(authStatus) ? "会话已过期，请重新登录" : "未授权访问");
            result.put("authStatus", authStatus);
            return result;
        }

        String sql = request.get("sql");
        if (sql == null || sql.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "SQL语句不能为空");
            return result;
        }

        sql = sql.trim();

        // 安全检查：只允许SELECT查询
        if (!sql.toLowerCase().startsWith("select")) {
            result.put("success", false);
            result.put("message", "仅支持SELECT查询语句");
            return result;
        }

        // 检查危险关键字 - 使用更精确的检测，避免误判字段名
        if (containsDangerousKeywords(sql)) {
            result.put("success", false);
            result.put("message", "SQL语句包含危险操作关键字，仅支持SELECT查询");
            return result;
        }

        // 自动添加LIMIT限制
        if (!sql.toLowerCase().contains("limit")) {
            sql += " LIMIT 1000";
        }

        long startTime = System.currentTimeMillis();

        try {
            // 执行真实的数据库查询
            List<Map<String, Object>> queryResult = executeSelectQuery(sql);
            long executionTime = System.currentTimeMillis() - startTime;

            result.put("success", true);
            result.put("data", queryResult);
            result.put("executionTime", executionTime);
            result.put("message", String.format("查询成功，返回 %d 行记录", queryResult.size()));

            // 记录执行日志
            SqlLogUtil.logQuery("WEB_EXECUTOR", sql, executionTime, "SUCCESS");

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            String errorMessage = sanitizeErrorMessage(e.getMessage());

            result.put("success", false);
            result.put("message", "SQL执行失败: " + errorMessage);
            result.put("executionTime", executionTime);

            // 记录错误日志
            SqlLogUtil.logQuery("WEB_EXECUTOR", sql, executionTime, "ERROR: " + e.getMessage());
        }

        return result;
    }

    /**
     * 执行SELECT查询（真实数据库查询）
     */
    private List<Map<String, Object>> executeSelectQuery(String sql) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        if (dataSource == null) {
            throw new RuntimeException("数据源未配置");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            // 获取结果集元数据
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 获取列名
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnLabel(i));
            }

            // 读取数据行
            int rowCount = 0;
            while (rs.next() && rowCount < 1000) { // 限制最多返回1000行
                Map<String, Object> row = new LinkedHashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = columnNames.get(i - 1);
                    Object value = rs.getObject(i);

                    // 处理特殊数据类型
                    if (value instanceof Timestamp) {
                        value = value.toString();
                    } else if (value instanceof java.sql.Date) {
                        value = value.toString();
                    } else if (value instanceof Time) {
                        value = value.toString();
                    } else if (value instanceof Clob) {
                        Clob clob = (Clob) value;
                        value = clob.getSubString(1, (int) clob.length());
                    } else if (value instanceof Blob) {
                        value = "[BLOB数据]";
                    }

                    row.put(columnName, value);
                }

                result.add(row);
                rowCount++;
            }

        } catch (SQLException e) {
            throw new RuntimeException("SQL执行失败: " + e.getMessage(), e);
        }

        return result;
    }


    /**
     * 获取SQL日志列表
     */
    @GetMapping("/logs")
    @ResponseBody
    public Map<String, Object> getLogs(HttpSession session) {
        // 检查认证状态和会话超时
        String authStatus = checkAuth(session);
        if (!"authenticated".equals(authStatus)) {
            Map<String, Object> result = new HashMap<>();
            result.put("logs", new ArrayList<>());
            result.put("total", 0);
            result.put("message", "session_expired".equals(authStatus) ? "会话已过期，请重新登录" : "未授权访问");
            result.put("authStatus", authStatus);
            return result;
        }

        // 从SqlLogController获取日志数据
        return SqlLogController.getRecentSqlLogs(100);
    }

    /**
     * 清空SQL日志
     */
    @PostMapping("/clear")
    @ResponseBody
    public String clearLogs(HttpSession session) {
        // 检查认证状态和会话超时
        String authStatus = checkAuth(session);
        if (!"authenticated".equals(authStatus)) {
            return "session_expired".equals(authStatus) ? "session_expired" : "unauthorized";
        }

        // 从SqlLogController清空日志
        SqlLogController.clearSqlLogs();
        return "success";
    }

    /**
     * 检查SQL是否包含危险关键字
     * 使用词边界匹配，避免误判字段名
     */
    private boolean containsDangerousKeywords(String sql) {
        String lowerSql = sql.toLowerCase();

        // 定义危险关键字模式 - 使用词边界确保精确匹配
        String[] dangerousPatterns = {
            "\\binsert\\s+into\\b",     // INSERT INTO
            "\\bupdate\\s+\\w+\\s+set\\b", // UPDATE table SET
            "\\bdelete\\s+from\\b",     // DELETE FROM
            "\\bdrop\\s+(table|database|index)\\b", // DROP TABLE/DATABASE/INDEX
            "\\bcreate\\s+(table|database|index)\\b", // CREATE TABLE/DATABASE/INDEX
            "\\balter\\s+table\\b",     // ALTER TABLE
            "\\btruncate\\s+table\\b",  // TRUNCATE TABLE
            "\\bexec\\s*\\(",           // EXEC(
            "\\bexecute\\s*\\(",        // EXECUTE(
            "\\bunion\\s+select\\b",    // UNION SELECT (SQL注入常见模式)
            "--",                       // SQL注释
            "/\\*",                     // 多行注释开始
            "\\*/",                     // 多行注释结束
            "\\bxp_\\w+",              // SQL Server扩展存储过程
            "\\bsp_\\w+"               // SQL Server存储过程
        };

        // 检查每个危险模式
        for (String pattern : dangerousPatterns) {
            if (lowerSql.matches(".*" + pattern + ".*")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 清理错误消息，防止敏感信息泄露
     */
    private String sanitizeErrorMessage(String message) {
        if (message == null) return "未知错误";

        // 移除可能包含敏感信息的内容
        return message.replaceAll("(?i)(password|pwd|secret|key|token)\\s*[=:]\\s*\\S+", "[HIDDEN]")
                .replaceAll("(?i)jdbc:[^\\s]+", "[JDBC_URL]")
                .substring(0, Math.min(message.length(), 200)); // 限制错误消息长度
    }
}
