package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;


/**
 * nekoimi  2022/3/26 16:39
 */
public class InjectRequestAttributeErrorException extends BaseRuntimeException {
    public InjectRequestAttributeErrorException() {
        super(Errors.INJECT_REQUEST_ATTRIBUTE_ERROR_EXCEPTION);
    }

    public InjectRequestAttributeErrorException(String message, Object... args) {
        super(Errors.INJECT_REQUEST_ATTRIBUTE_ERROR_EXCEPTION, message, args);
    }
}
