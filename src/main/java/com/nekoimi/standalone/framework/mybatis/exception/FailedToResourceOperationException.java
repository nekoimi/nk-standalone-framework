package com.nekoimi.standalone.framework.mybatis.exception;

import com.nekoimi.standalone.framework.error.exception.BaseRuntimeException;
import com.nekoimi.standalone.framework.mybatis.CrudErrors;
/**
 * nekoimi  2021/12/20 17:43
 */
public class FailedToResourceOperationException extends BaseRuntimeException {
    public FailedToResourceOperationException() {
        super(CrudErrors.RESOURCE_OPERATION_FAILED);
    }

    public FailedToResourceOperationException(String message, Object... args) {
        super(CrudErrors.RESOURCE_OPERATION_FAILED, message, args);
    }
}
