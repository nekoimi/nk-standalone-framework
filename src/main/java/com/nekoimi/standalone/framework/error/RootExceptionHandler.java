package com.nekoimi.standalone.framework.error;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.nekoimi.standalone.framework.contract.error.ErrorExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/7/21 下午3:47
 */
@Slf4j
@Component
public class RootExceptionHandler implements ErrorExceptionHandler<Exception> {
    private static final IErrorDetails error = Errors.DEFAULT_SERVER_ERROR;

    @Override
    public Class<Exception> getType() {
        return Exception.class;
    }

    @Override
    public Mono<? extends IErrorDetails> handle(ServerWebExchange exchange, Exception e) {
        log.warn("root exception handler -- \n");
        log.error(e.getMessage(), e);
        if (log.isDebugEnabled()) {
            e.printStackTrace();
        }
        return Mono.fromCallable(() -> ErrorDetails.of(error.code(), error.message(), ExceptionUtil.getRootCauseMessage(e)));
    }
}
