package com.mars.admin.framework.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果类
 *
 * @param <T> 数据类型
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-07-05
 */
@Schema(name = "统一返回结果", description = "接口统一返回格式")
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功状态码
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 失败状态码
     */
    public static final int ERROR_CODE = 500;

    /**
     * 状态码
     */
    @Schema(description = "状态码", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
    private int code;

    /**
     * 返回消息
     */
    @Schema(description = "返回消息", example = "操作成功", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    /**
     * 返回数据
     */
    @Schema(description = "返回数据")
    private T data;

    /**
     * 时间戳
     */
    @Schema(description = "时间戳", example = "1706345678000")
    private long timestamp;

    /**
     * 私有构造方法
     */
    private Result() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 私有构造方法
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     */
    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功返回（无数据）
     *
     * @param <T> 泛型类型
     * @return Result
     */
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, "操作成功", null);
    }

    /**
     * 成功返回（带数据）
     *
     * @param data 数据
     * @param <T>  泛型类型
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, "操作成功", data);
    }

    /**
     * 成功返回（带消息和数据）
     *
     * @param message 消息
     * @param data    数据
     * @param <T>     泛型类型
     * @return Result
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_CODE, message, data);
    }

    /**
     * 失败返回（默认消息）
     *
     * @param <T> 泛型类型
     * @return Result
     */
    public static <T> Result<T> error() {
        return new Result<>(ERROR_CODE, "操作失败", null);
    }

    /**
     * 失败返回（带消息）
     *
     * @param message 错误消息
     * @param <T>     泛型类型
     * @return Result
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ERROR_CODE, message, null);
    }

    /**
     * 失败返回（带状态码和消息）
     *
     * @param code    状态码
     * @param message 错误消息
     * @param <T>     泛型类型
     * @return Result
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 自定义返回
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     * @param <T>     泛型类型
     * @return Result
     */
    public static <T> Result<T> of(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    /**
     * 根据布尔值返回结果
     *
     * @param success 是否成功
     * @param <T>     泛型类型
     * @return Result
     */
    public static <T> Result<T> of(boolean success) {
        return success ? success() : error();
    }

    /**
     * 根据布尔值返回结果（带消息）
     *
     * @param success      是否成功
     * @param successMsg   成功消息
     * @param errorMsg     失败消息
     * @param <T>          泛型类型
     * @return Result
     */
    public static <T> Result<T> of(boolean success, String successMsg, String errorMsg) {
        return success ? success(successMsg, null) : error(errorMsg);
    }

    /**
     * 根据状态码枚举返回结果
     *
     * @param resultCode 状态码枚举
     * @param <T>        泛型类型
     * @return Result
     */
    public static <T> Result<T> of(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 根据状态码枚举返回结果（带数据）
     *
     * @param resultCode 状态码枚举
     * @param data       数据
     * @param <T>        泛型类型
     * @return Result
     */
    public static <T> Result<T> of(ResultCode resultCode, T data) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), data);
    }

    /**
     * 判断是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return this.code == SUCCESS_CODE;
    }

    /**
     * 判断是否失败
     *
     * @return 是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }
} 