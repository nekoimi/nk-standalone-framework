package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;

/**
 * nekoimi  2021/7/19 下午2:47
 */
public class FailedToOperationErrorException extends BaseRuntimeException {
    public FailedToOperationErrorException() {
        super(Errors.FAILED_TO_OPERATION_ERROR_EXCEPTION);
    }

    public FailedToOperationErrorException(String message, Object...args) {
        super(Errors.FAILED_TO_OPERATION_ERROR_EXCEPTION, message, args);
    }
}
