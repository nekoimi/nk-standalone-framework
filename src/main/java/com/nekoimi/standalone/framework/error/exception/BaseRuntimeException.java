package com.nekoimi.standalone.framework.error.exception;


import cn.hutool.core.util.StrUtil;
import com.nekoimi.standalone.framework.error.IErrorDetails;

/**
 * nekoimi  2021/12/6 14:37
 */
public class BaseRuntimeException extends RuntimeException {
    private IErrorDetails error;

    public BaseRuntimeException(IErrorDetails error) {
        super(error.message());
        this.error = error;
    }

    public BaseRuntimeException(IErrorDetails error, String message, Object... args) {
        super((StrUtil.isNotBlank(message) ? String.format(message, args) : error.message()));
        this.error = error;
    }

    public IErrorDetails getError() {
        return error;
    }

    public Integer getCode() {
        return error.code();
    }
}
