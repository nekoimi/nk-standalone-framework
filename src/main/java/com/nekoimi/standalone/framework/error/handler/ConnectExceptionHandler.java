package com.nekoimi.standalone.framework.error.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.nekoimi.standalone.framework.contract.error.ErrorExceptionHandler;
import com.nekoimi.standalone.framework.error.IErrorDetails;
import com.nekoimi.standalone.framework.error.Errors;
import com.nekoimi.standalone.framework.error.ErrorDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.PrintWriter;
import java.io.StringWriter;
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
    public Mono<? extends IErrorDetails> handle(ServerWebExchange exchange, ConnectException e) {
        return Mono.fromCallable(() -> {
            IErrorDetails error = Errors.CONNECT_EXCEPTION;
            StringWriter stackTrace = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stackTrace);
            e.printStackTrace(printWriter);
            String traceMessage = stackTrace.toString();
            return ErrorDetails.of(error.code(), e.getMessage(), traceMessage);
        });
    }
}
