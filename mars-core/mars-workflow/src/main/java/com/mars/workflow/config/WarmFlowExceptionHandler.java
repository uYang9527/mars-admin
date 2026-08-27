package com.mars.workflow.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.dromara.warm.flow.core.dto.ApiResult;
import org.dromara.warm.flow.core.exception.FlowException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Warm-Flow 异常处理（返回 ApiResult.msg，避免设计器提示为空）
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "org.dromara.warm.flow")
public class WarmFlowExceptionHandler {

    @ExceptionHandler(FlowException.class)
    public ApiResult<Void> handleFlowException(FlowException e) {
        String message = resolveMessage(e.getMessage(), "流程操作失败");
        log.warn("工作流异常: {}", message);
        if (e.getCode() != null) {
            return ApiResult.fail(e.getCode().intValue(), message);
        }
        return ApiResult.fail(message);
    }

    @ExceptionHandler(NotLoginException.class)
    public ApiResult<Void> handleNotLoginException(NotLoginException e) {
        log.warn("工作流未登录: {}", e.getMessage());
        return ApiResult.fail(401, "请先登录");
    }

    @ExceptionHandler({NotPermissionException.class, NotRoleException.class})
    public ApiResult<Void> handleAuthException(RuntimeException e) {
        log.warn("工作流无权限: {}", e.getMessage());
        return ApiResult.fail(403, "没有权限访问");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<Void> handleValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        log.warn("工作流参数校验异常: {}", message);
        return ApiResult.fail(400, message);
    }

    @ExceptionHandler(BindException.class)
    public ApiResult<Void> handleBindException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        log.warn("工作流参数绑定异常: {}", message);
        return ApiResult.fail(400, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResult<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = resolveMessage(e.getMessage(), "参数错误");
        log.warn("工作流参数异常: {}", message);
        return ApiResult.fail(400, message);
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResult<Void> handleRuntimeException(RuntimeException e) {
        String message = resolveMessage(e.getMessage(), "操作失败");
        log.warn("工作流运行时异常: {}", message, e);
        return ApiResult.fail(message);
    }

    @ExceptionHandler(Exception.class)
    public ApiResult<Void> handleException(Exception e) {
        log.error("工作流系统异常", e);
        return ApiResult.fail("系统繁忙，请稍后再试");
    }

    private String resolveMessage(String message, String defaultMessage) {
        return message != null && !message.isBlank() ? message : defaultMessage;
    }
}
