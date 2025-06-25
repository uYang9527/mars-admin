package com.mars.admin.framework.exception;

import com.mars.admin.framework.common.ResultCode;

/**
 * 业务异常类
 *
 * @author Mars.wq [wqexpore@163.com]
 * @version 1.0
 * @date 2025-01-27
 */
public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException() {
        super();
        this.code = ResultCode.FAIL.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.FAIL.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.FAIL.getCode();
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 快速创建业务异常
     */
    public static BusinessException of(String message) {
        return new BusinessException(message);
    }

    /**
     * 快速创建业务异常
     */
    public static BusinessException of(int code, String message) {
        return new BusinessException(code, message);
    }

    /**
     * 快速创建业务异常
     */
    public static BusinessException of(ResultCode resultCode) {
        return new BusinessException(resultCode);
    }
} 