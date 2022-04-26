package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;

/**
 * nekoimi  2021/7/19 下午3:16
 */
public class RequestAuthorizedException extends BaseRuntimeException {
    public RequestAuthorizedException() {
        super(Errors.HTTP_STATUS_UNAUTHORIZED);
    }

    public RequestAuthorizedException(String message, Object...args) {
        super(Errors.HTTP_STATUS_UNAUTHORIZED, message, args);
    }
}
