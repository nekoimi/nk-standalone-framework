package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;

/**
 * nekoimi  2021/7/19 上午11:13
 */
public class RequestMethodNotAllowedException extends BaseRuntimeException {
    public RequestMethodNotAllowedException() {
        super(Errors.HTTP_STATUS_METHOD_NOT_ALLOWED);
    }

    public RequestMethodNotAllowedException(String message, Object...args) {
        super(Errors.HTTP_STATUS_METHOD_NOT_ALLOWED, message, args);
    }
}
