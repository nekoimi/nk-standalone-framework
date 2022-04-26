package com.nekoimi.standalone.framework.error.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.nekoimi.standalone.framework.contract.error.ErrorExceptionHandler;
import com.nekoimi.standalone.framework.error.ErrorDetails;
import com.nekoimi.standalone.framework.error.Errors;
import com.nekoimi.standalone.framework.protocol.ErrorDetailsImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.ConnectException;

/**
 * nekoimi  2021/12/25 14:24
 */
@Component
public class ConnectExceptionHandler implements ErrorExceptionHandler<ConnectException> {
    @Override
    public Class<ConnectException> getType() {
        return ConnectException.class;
    }

    @Override
    public Mono<? extends ErrorDetails> handle(ServerWebExchange exchange, ConnectException e) {
        return Mono.fromCallable(() -> ErrorDetailsImpl.of(Errors.CONNECT_EXCEPTION.code(), e.getMessage(), ExceptionUtil.getRootCauseMessage(e)));
    }
}
