package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;

/**
 * nekoimi  2021/7/19 下午2:56
 */
public class IdGeneratorSystemClockException extends BaseRuntimeException {
    public IdGeneratorSystemClockException() {
        super(Errors.SYSTEM_CLOCK_EXCEPTION);
    }

    public IdGeneratorSystemClockException(String message, Object...args) {
        super(Errors.SYSTEM_CLOCK_EXCEPTION, message, args);
    }
}
