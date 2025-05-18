package com.mars.easy.admin.framework.entity;


import com.mars.easy.admin.framework.enums.CommonResponseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: 程序员Mars
 * @date: 2022/8/25 11:12
 * @description: result parent类
 */
@Data
public class ApiResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "自定义响应码", example = "0000")
    public String code;

    @Schema(description = "响应信息", example = "SUCCESS")
    public String message;

    @Schema(description = "响应数据")
    public T data;

    @Schema(description = "额外参数")
    private Object param = new Object();

    {
        code = "0000";
        message = "SUCCESS";
    }

    public ApiResult() {
    }

    public ApiResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 默认的无参返回结果
     *
     * @return <T>
     */
    public static <T> ApiResult<T> success() {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.data = null;
        return apiResult;
    }

    /**
     * 有参返回结果
     *
     * @param data data
     * @return <T>
     */
    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.data = data;
        return apiResult;
    }

    public static <T> ApiResult<T> error(CommonResponseEnum responseEnum) {
        ApiResult<T> apiResult = new ApiResult<>();
        apiResult.code = String.valueOf(responseEnum.getCode());
        apiResult.message = responseEnum.getMessage();
        apiResult.data = null;
        return apiResult;
    }


}
