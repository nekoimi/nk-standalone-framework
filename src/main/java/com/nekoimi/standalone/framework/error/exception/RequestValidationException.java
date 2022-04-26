package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;

/**
 * nekoimi  2021/7/19 上午11:13
 */
public class RequestValidationException extends BaseRuntimeException {
    public RequestValidationException() {
        super(Errors.REQUEST_VALIDATE_EXCEPTION);
    }

    public RequestValidationException(String message, Object...args) {
        super(Errors.REQUEST_VALIDATE_EXCEPTION, message, args);
    }
}
