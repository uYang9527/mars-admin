package com.mars.admin.framework.common.annotation;

import com.mars.admin.framework.enums.BusinessType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 *
 * @author 程序员Mars
 * @version 1.0
 * @date 2025-07-05
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 模块标题
     */
    String title() default "";

    /**
     * 业务操作类型
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean isSaveResponseData() default true;

    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};

    /**
     * 操作描述（支持SpEL表达式）
     */
    String description() default "";

    /**
     * 操作人员类别枚举
     */
    enum OperatorType {
        /**
         * 其它
         */
        OTHER,

        /**
         * 后台用户
         */
        MANAGE,

        /**
         * 手机端用户
         */
        MOBILE
    }
} 