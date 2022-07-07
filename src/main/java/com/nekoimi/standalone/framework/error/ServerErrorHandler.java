package com.nekoimi.standalone.framework.error;

import com.nekoimi.standalone.framework.contract.error.ErrorExceptionHandlerResolver;
import com.nekoimi.standalone.framework.contract.error.ErrorExceptionResultWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/13 22:14
 *
 * 全局异常处理器
 */
@Slf4j
@Component
public class ServerErrorHandler implements ErrorWebExceptionHandler, Ordered {
    private final ErrorExceptionResultWriter resultWriter;
    private final ErrorExceptionHandlerResolver handlerResolver;

    public ServerErrorHandler(ErrorExceptionResultWriter resultWriter,
                              ErrorExceptionHandlerResolver handlerResolver) {
        this.resultWriter = resultWriter;
        this.handlerResolver = handlerResolver;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (log.isDebugEnabled()) {
            ex.printStackTrace();
        }
        return handlerResolver.resolve(ex)
                .flatMap(handler -> (Mono<IErrorDetails>) handler.handle(exchange, ex))
                .flatMap(error -> resultWriter.writer(exchange, error))
                .doOnError(e -> log.error("server error handler -- \n" + e.getMessage(), e));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
