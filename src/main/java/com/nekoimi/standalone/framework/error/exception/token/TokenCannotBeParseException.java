package com.nekoimi.standalone.framework.error.exception.token;

import com.nekoimi.standalone.framework.error.Errors;
import com.nekoimi.standalone.framework.error.exception.BaseRuntimeException;
/**
 * nekoimi  2022/2/16 16:54
 */
public class TokenCannotBeParseException extends BaseRuntimeException {
    public TokenCannotBeParseException() {
        super(Errors.TOKEN_CANNOT_BE_PARSE_EXCEPTION);
    }
}
