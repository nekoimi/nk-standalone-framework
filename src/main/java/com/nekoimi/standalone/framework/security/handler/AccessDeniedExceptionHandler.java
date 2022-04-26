package com.nekoimi.standalone.framework.security.handler;

import com.nekoimi.standalone.framework.error.exception.RequestAccessDeniedException;
import com.nekoimi.standalone.framework.utils.SendRespUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 13:18
 *
 * 没有授权
 */
@Slf4j
@Component
public class AccessDeniedExceptionHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
        log.error("拒绝访问： {}", e.getMessage());
        return SendRespUtils.sendError(exchange.getResponse(), new RequestAccessDeniedException());
    }
}
