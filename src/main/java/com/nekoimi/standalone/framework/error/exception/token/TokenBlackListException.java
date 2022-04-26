package com.nekoimi.standalone.framework.error.exception.token;

import com.nekoimi.standalone.framework.error.Errors;
import com.nekoimi.standalone.framework.error.exception.BaseRuntimeException;
/**
 * @author Nekoimi  2020/5/30 20:35
 */
public class TokenBlackListException extends BaseRuntimeException {
    public TokenBlackListException() {
        super(Errors.TOKEN_BLACK_EXCEPTION);
    }
}
