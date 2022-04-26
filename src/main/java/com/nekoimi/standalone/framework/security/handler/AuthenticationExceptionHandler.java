package com.nekoimi.standalone.framework.security.handler;

import com.nekoimi.standalone.framework.error.exception.RequestAuthenticationException;
import com.nekoimi.standalone.framework.utils.SendRespUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 13:21
 *
 * 没有登录认证
 */
@Slf4j
@Component
public class AuthenticationExceptionHandler implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        log.error("没有登录认证： {}", e.getMessage());
        return SendRespUtils.sendError(exchange.getResponse(), new RequestAuthenticationException());
    }
}
