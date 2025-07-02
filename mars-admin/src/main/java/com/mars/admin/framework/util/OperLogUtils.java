package com.mars.admin.framework.util;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.admin.entity.SysOperLog;
import com.mars.admin.entity.User;
import com.mars.admin.framework.common.annotation.BusinessType;
import com.mars.admin.service.ISysOperLogService;
import com.mars.admin.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 操作日志记录工具类
 *
 * @author Mars
 */
@Component
@Slf4j
public class OperLogUtils {

    @Autowired
    private ISysOperLogService operLogService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 记录操作日志
     */
    public void recordLog(String title, BusinessType businessType, String content) {
        recordLog(title, businessType, content, null, null);
    }

    /**
     * 记录操作日志
     */
    public void recordLog(String title, BusinessType businessType, String content, Object requestParam) {
        recordLog(title, businessType, content, requestParam, null);
    }

    /**
     * 记录操作日志
     */
    public void recordLog(String title, BusinessType businessType, String content, Object requestParam, Object result) {
        try {
            SysOperLog operLog = new SysOperLog();
            
            // 基本信息
            operLog.setTitle(title);
            operLog.setBusinessType(businessType.ordinal());
            operLog.setOperatorType(1); // 后台用户
            operLog.setStatus(0); // 正常
            
            // 获取请求信息
            HttpServletRequest request = getRequest();
            if (request != null) {
                operLog.setRequestMethod(request.getMethod());
                operLog.setOperUrl(request.getRequestURI());
                operLog.setOperIp(getClientIp(request));
                operLog.setMethod(getCallerMethodName());
            }
            
            // 获取当前用户信息
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                operLog.setOperName(currentUser.getUsername());
                operLog.setDeptName("默认部门");
            }
            
            // 参数和结果
            if (requestParam != null) {
                operLog.setOperParam(substring(toJsonString(requestParam), 0, 2000));
            } else {
                operLog.setOperParam(content);
            }
            
            if (result != null) {
                operLog.setJsonResult(substring(toJsonString(result), 0, 2000));
            }
            
            operLog.setCostTime(0L);
            
            // 保存日志
            operLogService.insertOperLog(operLog);
            
        } catch (Exception e) {
            log.error("记录操作日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录异常日志
     */
    public void recordErrorLog(String title, BusinessType businessType, String content, Exception exception) {
        try {
            SysOperLog operLog = new SysOperLog();
            
            operLog.setTitle(title);
            operLog.setBusinessType(businessType.ordinal());
            operLog.setOperatorType(1);
            operLog.setStatus(1); // 异常
            operLog.setErrorMsg(substring(exception.getMessage(), 0, 2000));
            
            HttpServletRequest request = getRequest();
            if (request != null) {
                operLog.setRequestMethod(request.getMethod());
                operLog.setOperUrl(request.getRequestURI());
                operLog.setOperIp(getClientIp(request));
                operLog.setMethod(getCallerMethodName());
            }
            
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                operLog.setOperName(currentUser.getUsername());
                operLog.setDeptName("默认部门");
            }
            
            operLog.setOperParam(content);
            operLog.setCostTime(0L);
            
            operLogService.insertOperLog(operLog);
            
        } catch (Exception e) {
            log.error("记录异常日志失败: {}", e.getMessage(), e);
        }
    }

    private User getCurrentUser() {
        try {
            if (StpUtil.isLogin()) {
                Object loginId = StpUtil.getLoginId();
                if (loginId != null) {
                    return userService.getById(Long.parseLong(loginId.toString()));
                }
            }
        } catch (Exception e) {
            log.error("获取当前用户失败：{}", e.getMessage());
        }
        return null;
    }

    private HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getCallerMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 3) {
            StackTraceElement caller = stackTrace[3];
            return caller.getClassName() + "." + caller.getMethodName() + "()";
        }
        return "unknown";
    }

    private String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }

    private String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }
} 