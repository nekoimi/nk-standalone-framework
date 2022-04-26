package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;

/**
 * nekoimi  2021/12/16 19:25
 */
public class RequestAccessDeniedException extends BaseRuntimeException {
    public RequestAccessDeniedException() {
        super(Errors.ACCESS_DENIED_EXCEPTION);
    }

    public RequestAccessDeniedException(String message, Object... args) {
        super(Errors.ACCESS_DENIED_EXCEPTION, message, args);
    }
}
