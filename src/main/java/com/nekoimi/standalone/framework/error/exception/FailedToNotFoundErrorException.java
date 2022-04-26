package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;

/**
 * nekoimi  2021/7/19 下午2:44
 */
public class FailedToNotFoundErrorException extends BaseRuntimeException {
    public FailedToNotFoundErrorException() {
        super(Errors.FAILED_TO_NOT_FOUND_ERROR_EXCEPTION);
    }

    public FailedToNotFoundErrorException(String message, Object...args) {
        super(Errors.FAILED_TO_NOT_FOUND_ERROR_EXCEPTION, message, args);
    }
}
