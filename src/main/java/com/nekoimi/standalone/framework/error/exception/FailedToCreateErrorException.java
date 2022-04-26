package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;

/**
 * nekoimi  2021/7/19 下午2:38
 */
public class FailedToCreateErrorException extends BaseRuntimeException {
    public FailedToCreateErrorException() {
        super(Errors.FAILED_TO_CREATE_ERROR_EXCEPTION);
    }

    public FailedToCreateErrorException(String message, Object... args) {
        super(Errors.FAILED_TO_CREATE_ERROR_EXCEPTION, message, args);
    }
}
