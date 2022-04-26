package com.nekoimi.standalone.framework.error.exception.token;


import com.nekoimi.standalone.framework.error.Errors;
import com.nekoimi.standalone.framework.error.exception.BaseRuntimeException;

/**
 * @author Nekoimi  2020/5/30 20:52
 */
public class TokenCannotBeRefreshException extends BaseRuntimeException {
    public TokenCannotBeRefreshException() {
        super(Errors.TOKEN_CANNOT_BE_REFRESH_EXCEPTION);
    }
}
