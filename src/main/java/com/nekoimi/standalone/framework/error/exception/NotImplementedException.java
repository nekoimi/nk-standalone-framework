package com.nekoimi.standalone.framework.error.exception;

import com.nekoimi.standalone.framework.error.Errors;


/**
 * nekoimi  2021/7/20 上午11:07
 */
public class NotImplementedException extends BaseRuntimeException {
    public NotImplementedException() {
        super(Errors.NOT_IMPLEMENTED_ERROR_EXCEPTION);
    }
}
