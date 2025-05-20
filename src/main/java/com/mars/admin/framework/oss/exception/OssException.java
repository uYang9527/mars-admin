package com.mars.admin.framework.oss.exception;

import java.io.Serial;

/**
 * OSS异常类
 *
 * @author Mars
 */
public class OssException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public OssException(String msg) {
        super(msg);
    }

}
