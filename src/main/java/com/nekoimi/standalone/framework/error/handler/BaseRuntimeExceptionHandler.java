package com.nekoimi.standalone.framework.error.handler;

import com.nekoimi.standalone.framework.contract.error.ErrorExceptionHandler;
import com.nekoimi.standalone.framework.error.ErrorDetails;
import com.nekoimi.standalone.framework.error.IErrorDetails;
import com.nekoimi.standalone.framework.error.exception.BaseRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * nekoimi  2021/7/28 上午9:06
 * <p>
 * 自定义的异常处理
 */
@Slf4j
@Component
public class BaseRuntimeExceptionHandler implements ErrorExceptionHandler<BaseRuntimeException> {
    @Override
    public Class<BaseRuntimeException> getType() {
        return BaseRuntimeException.class;
    }

    @Override
    public Mono<? extends IErrorDetails> handle(ServerWebExchange exchange, BaseRuntimeException e) {
        return Mono.fromCallable(() -> {
            IErrorDetails error = e.getError();
            StringWriter stackTrace = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stackTrace);
            e.printStackTrace(printWriter);
            String traceMessage = stackTrace.toString();
            return ErrorDetails.of(error.code(), e.getMessage(), traceMessage);
        });
    }

}
