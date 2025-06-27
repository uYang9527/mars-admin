package com.mars.admin.common.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EnableEnum {

    /**
     * 启用
     */
    ENABLE(1, "启用"),
    /**
     * 禁用
     */
    DISABLE(0, "禁用"),
    ;
    private final int code;

    @Getter
    private final String desc;

    @EnumValue
    public int getCode() {
        return code;
    }

}
