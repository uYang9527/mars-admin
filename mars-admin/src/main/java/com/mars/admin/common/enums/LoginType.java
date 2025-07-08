package com.mars.admin.common.enums;

import lombok.Getter;

/**
 * 登录类型枚举
 *
 * @author 【全栈学习关注公众号：程序员Mars】
 * @version 1.0
 * @date 2025-01-16
 */
@Getter
public enum LoginType {
    
    /**
     * PC端登录
     */
    PC("pc", "PC端登录"),
    
    /**
     * 小程序登录
     */
    MINIPROGRAM("miniprogram", "小程序登录"),
    
    /**
     * APP登录
     */
    APP("app", "APP登录");
    
    /**
     * 登录类型代码
     */
    private final String code;
    
    /**
     * 登录类型描述
     */
    private final String description;
    
    LoginType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 根据代码获取登录类型
     *
     * @param code 登录类型代码
     * @return 登录类型枚举
     */
    public static LoginType fromCode(String code) {
        for (LoginType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("不支持的登录类型: " + code);
    }
} 