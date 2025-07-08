package com.mars.admin.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * SQL复制工具类
 * 提供SQL格式化和复制到剪贴板功能
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-07-05
 */
@Slf4j
public class SqlCopyUtil {

    /**
     * 将SQL复制到系统剪贴板
     *
     * @param sql SQL语句
     * @return 是否复制成功
     */
    public static boolean copyToClipboard(String sql) {
        try {
            if (sql == null || sql.trim().isEmpty()) {
                return false;
            }

            // 检查是否支持图形界面
            if (GraphicsEnvironment.isHeadless()) {
                log.debug("当前环境不支持图形界面，无法使用剪贴板功能");
                return false;
            }

            // 格式化SQL
            String formattedSql = formatSqlForCopy(sql);
            
            // 复制到剪贴板
            StringSelection stringSelection = new StringSelection(formattedSql);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            
            log.debug("SQL已复制到剪贴板: {}", formattedSql.substring(0, Math.min(50, formattedSql.length())) + "...");
            return true;
            
        } catch (Exception e) {
            log.debug("复制SQL到剪贴板失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 格式化SQL用于复制
     */
    private static String formatSqlForCopy(String sql) {
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

    /**
     * 生成复制提示信息
     */
    public static String getCopyHint(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return "无SQL可复制";
        }
        
        String preview = sql.replaceAll("\\s+", " ").trim();
        if (preview.length() > 50) {
            preview = preview.substring(0, 47) + "...";
        }
        
        return "💡 提示: 可在IDE中使用Ctrl+C复制此SQL: " + preview;
    }
} 