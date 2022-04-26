package com.nekoimi.standalone.framework.security.handler;

import com.nekoimi.standalone.framework.error.exception.RequestAuthenticationException;
import com.nekoimi.standalone.framework.utils.SendRespUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 14:06
 */
@Slf4j
@Component
public class AuthenticationFailureHandler implements ServerAuthenticationFailureHandler {
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange exchange, AuthenticationException e) {
        log.error("认证失败: {}", e.getMessage());
        return SendRespUtils.sendError(exchange.getExchange().getResponse(), new RequestAuthenticationException(e.getMessage()));
    }
}
