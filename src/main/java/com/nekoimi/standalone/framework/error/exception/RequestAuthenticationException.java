package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;

/**
 * nekoimi  2021/12/16 19:35
 */
public class RequestAuthenticationException extends BaseRuntimeException {
    public RequestAuthenticationException() {
        super(Errors.AUTHENTICATION_EXCEPTION);
    }

    public RequestAuthenticationException(String message, Object... args) {
        super(Errors.AUTHENTICATION_EXCEPTION, message, args);
    }
}
