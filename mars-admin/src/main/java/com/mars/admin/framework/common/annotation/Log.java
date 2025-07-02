package com.mars.admin.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志记录注解
 *
 * @author Mars
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    
    /**
     * 模块标题
     */
    String title() default "";

    /**
     * 业务类型：0-其它，1-新增，2-修改，3-删除
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别：0-其它，1-后台用户，2-手机端用户
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
} 