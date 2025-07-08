package com.mars.admin.framework.common;

/**
 * 返回状态码枚举
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-07-05
 */
public enum ResultCode {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 业务异常
     */
    FAIL(400, "业务异常"),

    /**
     * 请求未授权
     */
    UNAUTHORIZED(401, "请求未授权"),

    /**
     * 404 没找到请求
     */
    NOT_FOUND(404, "404 没找到请求"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 500 服务器异常
     */
    INTERNAL_SERVER_ERROR(500, "服务器异常"),

    /**
     * 参数校验失败
     */
    VALIDATE_FAILED(400, "参数校验失败"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXIST(404, "数据不存在"),

    /**
     * 数据已存在
     */
    DATA_ALREADY_EXIST(409, "数据已存在"),

    /**
     * 操作失败
     */
    OPERATION_FAILED(500, "操作失败"),

    /**
     * 权限不足
     */
    PERMISSION_DENIED(403, "权限不足"),

    /**
     * 请求参数错误
     */
    BAD_REQUEST(400, "请求参数错误"),

    /**
     * 系统繁忙
     */
    SYSTEM_BUSY(503, "系统繁忙，请稍后重试");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return ResultCode
     */
    public static ResultCode getByCode(int code) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.getCode() == code) {
                return resultCode;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }
} 