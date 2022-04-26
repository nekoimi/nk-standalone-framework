package com.nekoimi.standalone.framework.error.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.nekoimi.standalone.framework.contract.error.ErrorExceptionHandler;
import com.nekoimi.standalone.framework.error.ErrorDetails;
import com.nekoimi.standalone.framework.error.exception.BaseRuntimeException;
import com.nekoimi.standalone.framework.protocol.ErrorDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
    public Mono<? extends ErrorDetails> handle(ServerWebExchange exchange, BaseRuntimeException e) {
        var error = e.getError();
        return Mono.fromCallable(() -> ErrorDetailsImpl.of(error.code(), e.getMessage(), ExceptionUtil.getRootCauseMessage(e)));
    }

}
