package com.nekoimi.standalone.framework.contract.error;

import com.nekoimi.standalone.framework.error.IErrorDetails;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/13 22:04
 *
 * 异常处理
 */
public interface ErrorExceptionHandler<E extends Throwable> {
    /**
     * 获取异常类型
     * @return
     */
    Class<E> getType();

    /**
     * 异常处理结果
     * @param exchange
     * @param e
     * @return
     */
    Mono<? extends IErrorDetails> handle(ServerWebExchange exchange, E e);
}
