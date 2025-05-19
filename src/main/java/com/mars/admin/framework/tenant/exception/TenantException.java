package com.mars.admin.framework.tenant.exception;

import com.mars.admin.framework.core.exception.base.BaseException;

import java.io.Serial;

/**
 * 租户异常类
 *
 * @author Mars
 */
public class TenantException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TenantException(String code, Object... args) {
        super("tenant", code, args, null);
    }
}
