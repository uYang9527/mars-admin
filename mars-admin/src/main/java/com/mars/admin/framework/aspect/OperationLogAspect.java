package com.mars.admin.framework.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.admin.entity.SysOperLog;
import com.mars.admin.entity.SysUser;
import com.mars.admin.framework.common.annotation.OperationLog;
import com.mars.admin.framework.util.IpUtils;
import com.mars.admin.service.ISysOperLogService;
import com.mars.admin.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 操作日志记录AOP切面
 * 自动处理标注了@OperationLog注解的方法
 * 支持异步落库，提高性能
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Autowired
    private ISysOperLogService operLogService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // SpEL 表达式解析器
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 配置织入点：所有带有@OperationLog注解的方法
     */
    @Pointcut("@annotation(com.mars.admin.framework.common.annotation.OperationLog)")
    public void operationLogPointCut() {
    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行，如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param result    返回结果
     */
    @AfterReturning(value = "operationLogPointCut()", returning = "result")
    public void saveOperationLog(JoinPoint joinPoint, Object result) {
        try {
            // 获取RequestAttributes
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            
            // 获取操作
            OperationLog operationLog = getAnnotationLog(joinPoint);
            if (operationLog == null) {
                return;
            }

            // 异步记录日志
            handleLogAsync(joinPoint, operationLog, request, result, null);

        } catch (Exception exp) {
            log.error("记录操作日志异常: {}", exp.getMessage(), exp);
        }
    }

    /**
     * 拦截异常操作日志，连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常
     */
    @AfterThrowing(value = "operationLogPointCut()", throwing = "e")
    public void saveOperationExceptionLog(JoinPoint joinPoint, Throwable e) {
        try {
            // 获取RequestAttributes
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            
            // 获取操作
            OperationLog operationLog = getAnnotationLog(joinPoint);
            if (operationLog == null) {
                return;
            }

            // 异步记录异常日志
            handleLogAsync(joinPoint, operationLog, request, null, e);

        } catch (Exception exp) {
            log.error("记录操作异常日志失败: {}", exp.getMessage(), exp);
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     */
    public OperationLog getAnnotationLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method != null) {
            return method.getAnnotation(OperationLog.class);
        }
        return null;
    }

    /**
     * 异步处理操作日志
     *
     * @param joinPoint     连接点
     * @param operationLog  操作日志注解
     * @param request       请求对象
     * @param result        返回结果
     * @param e             异常对象
     */
    private void handleLogAsync(JoinPoint joinPoint, OperationLog operationLog, HttpServletRequest request, 
                               Object result, Throwable e) {
        // 预先收集所有需要的数据，避免在异步线程中访问请求上下文
        LogData logData = collectLogData(joinPoint, operationLog, request, result, e);
        
        // 异步保存到数据库
        saveOperationLogAsync(logData);
    }

    /**
     * 收集日志数据
     * 在主线程中收集所有必要的数据，避免在异步线程中访问可能已经失效的上下文
     */
    private LogData collectLogData(JoinPoint joinPoint, OperationLog operationLog, HttpServletRequest request, 
                                  Object result, Throwable e) {
        long startTime = System.currentTimeMillis();
        
        LogData logData = new LogData();
        
        // 获取当前用户信息
        SysUser currentUser = getCurrentUser();
        
        // 基本信息
        logData.status = (e == null ? 0 : 1);  // 操作状态：0=正常,1=异常
        logData.operIp = IpUtils.getIpAddr(request);
        logData.operUrl = request.getRequestURI();
        logData.requestMethod = request.getMethod();
        
        if (currentUser != null) {
            logData.operName = currentUser.getUsername();
            logData.deptName = getDeptName(currentUser);
        }
        
        if (e != null) {
            logData.errorMsg = substring(e.getMessage(), 0, 2000);
        }
        
        // 方法信息
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        logData.method = className + "." + methodName + "()";
        
        // 注解信息
        logData.title = operationLog.title();
        logData.businessType = operationLog.businessType().ordinal();
        logData.operatorType = operationLog.operatorType().ordinal();
        
        // 参数和返回值处理
        if (operationLog.isSaveRequestData()) {
            Map<String, Object> paramsMap = getRequestParams(joinPoint, operationLog.excludeParamNames());
            logData.operParam = substring(toJsonString(paramsMap), 0, 2000);
        }
        
        if (operationLog.isSaveResponseData() && result != null) {
            logData.jsonResult = substring(toJsonString(result), 0, 2000);
        }
        
        // 处理操作描述（支持SpEL表达式）
        if (!operationLog.description().isEmpty()) {
            try {
                String description = operationLog.description();
                if (logData.operParam != null && !logData.operParam.isEmpty()) {
                    logData.operParam = logData.operParam + " | 描述: " + description;
                } else {
                    logData.operParam = "描述: " + description;
                }
            } catch (Exception ex) {
                log.warn("解析SpEL表达式失败: {}", ex.getMessage());
                logData.operParam = "描述解析失败: " + operationLog.description();
            }
        }
        
        // 计算耗时
        logData.costTime = System.currentTimeMillis() - startTime;
        logData.operTime = LocalDateTime.now();
        
        return logData;
    }

    /**
     * 异步保存操作日志到数据库
     *
     * @param logData 日志数据
     * @return CompletableFuture
     */
    @Async("operationLogExecutor")
    public CompletableFuture<Void> saveOperationLogAsync(LogData logData) {
        try {
            // 创建操作日志对象
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(logData.status);
            operLog.setOperIp(logData.operIp);
            operLog.setOperUrl(logData.operUrl);
            operLog.setRequestMethod(logData.requestMethod);
            operLog.setOperName(logData.operName);
            operLog.setDeptName(logData.deptName);
            operLog.setErrorMsg(logData.errorMsg);
            operLog.setMethod(logData.method);
            operLog.setTitle(logData.title);
            operLog.setBusinessType(logData.businessType);
            operLog.setOperatorType(logData.operatorType);
            operLog.setOperParam(logData.operParam);
            operLog.setJsonResult(logData.jsonResult);
            operLog.setCostTime(logData.costTime);
            operLog.setOperTime(logData.operTime);
            
            // 保存到数据库
            operLogService.insertOperLog(operLog);
            
            log.debug("异步保存操作日志成功: {}", logData.title);
            
        } catch (Exception exp) {
            log.error("异步保存操作日志失败: {}, 错误: {}", logData.title, exp.getMessage(), exp);
        }
        
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 获取请求参数
     */
    private Map<String, Object> getRequestParams(JoinPoint joinPoint, String[] excludeParamNames) {
        Map<String, Object> paramsMap = new HashMap<>();
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] paramValues = joinPoint.getArgs();

            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                Object paramValue = paramValues[i];

                // 跳过排除的参数
                if (excludeParamNames != null && Arrays.asList(excludeParamNames).contains(paramName)) {
                    continue;
                }

                // 跳过特殊类型的参数
                if (isFilterObject(paramValue)) {
                    continue;
                }

                paramsMap.put(paramName, paramValue);
            }
        } catch (Exception e) {
            log.error("获取请求参数失败: {}", e.getMessage());
        }
        return paramsMap;
    }

    /**
     * 判断是否需要过滤的对象
     */
    private boolean isFilterObject(Object value) {
        if (value == null) {
            return true;
        }
        Class<?> clazz = value.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(jakarta.servlet.http.Part.class);
        } else if (jakarta.servlet.http.HttpServletRequest.class.isAssignableFrom(clazz)
                || jakarta.servlet.http.HttpServletResponse.class.isAssignableFrom(clazz)
                || jakarta.servlet.http.Part.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    /**
     * 解析SpEL表达式
     */
    private String parseSpelExpression(String spelExpression, JoinPoint joinPoint, Object result) {
        try {
            Expression expression = parser.parseExpression(spelExpression);
            EvaluationContext context = new StandardEvaluationContext();
            
            // 设置方法参数
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            
            // 设置返回结果
            context.setVariable("result", result);
            
            // 设置当前用户
            context.setVariable("currentUser", getCurrentUser());
            
            return expression.getValue(context, String.class);
        } catch (Exception e) {
            log.warn("SpEL表达式解析失败: {}", e.getMessage());
            return spelExpression;
        }
    }

    /**
     * 获取当前用户
     */
    private SysUser getCurrentUser() {
        try {
            if (StpUtil.isLogin()) {
                Object loginId = StpUtil.getLoginId();
                if (loginId != null) {
                    return userService.getById(Long.parseLong(loginId.toString()));
                }
            }
        } catch (Exception e) {
            log.warn("获取当前用户失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获取部门名称
     */
    private String getDeptName(SysUser user) {
        try {
            if (user != null && user.getDepts() != null && !user.getDepts().isEmpty()) {
                return user.getDepts().get(0).getDeptName();
            }
        } catch (Exception e) {
            log.warn("获取部门名称失败: {}", e.getMessage());
        }
        return "未知部门";
    }

    /**
     * 转换为JSON字符串
     */
    private String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj != null ? obj.toString() : "";
        }
    }

    /**
     * 字符串截取
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return "";
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
        if (start > end) {
            return "";
        }
        return str.substring(start, end);
    }

    /**
     * 日志数据传输对象
     * 用于在异步线程间传递日志数据
     */
    private static class LogData {
        Integer status;
        String operIp;
        String operUrl;
        String requestMethod;
        String operName;
        String deptName;
        String errorMsg;
        String method;
        String title;
        Integer businessType;
        Integer operatorType;
        String operParam;
        String jsonResult;
        Long costTime;
        LocalDateTime operTime;
    }
} 