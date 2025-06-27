package com.mars.admin.controller;

import com.mars.admin.framework.config.SqlLogProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * SQL日志页面控制器
 * 提供SQL日志页面的认证功能
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/sqlLog")
public class SqlLogPageController {

    @Autowired
    private SqlLogProperties sqlLogProperties;

    private static final String SESSION_KEY = "SQL_LOG_AUTHENTICATED";

    /**
     * 检查认证状态
     */
    @GetMapping("/checkAuth")
    public String checkAuth(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Boolean authenticated = (Boolean) session.getAttribute(SESSION_KEY);
        
        if (authenticated != null && authenticated) {
            return "authenticated";
        }
        
        return "not_authenticated";
    }

    /**
     * 处理登录
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password, 
                       HttpServletRequest request) {
        
        if (sqlLogProperties.getUsername().equals(username) && 
            sqlLogProperties.getPassword().equals(password)) {
            
            HttpSession session = request.getSession();
            session.setAttribute(SESSION_KEY, true);
            session.setMaxInactiveInterval(3600); // 1小时有效期
            
            log.info("SQL日志系统登录成功，用户: {}", username);
            return "success";
        }
        
        log.warn("SQL日志系统登录失败，用户: {}", username);
        return "error";
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(SESSION_KEY);
        
        log.info("SQL日志系统退出登录");
        return "success";
    }
} 