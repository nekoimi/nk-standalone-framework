package com.nekoimi.standalone.framework.security.handler;

import com.nekoimi.standalone.framework.protocol.JsonResp;
import com.nekoimi.standalone.framework.security.AuthenticationResult;
import com.nekoimi.standalone.framework.security.token.SubjectAuthenticationToken;
import com.nekoimi.standalone.framework.utils.JsonUtils;
import com.nekoimi.standalone.framework.utils.SendRespUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/16 13:35
 * <p>
 * 登录成功
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication authentication) {
        log.debug("登录成功 -- \n {}", JsonUtils.write(authentication));
        return Mono.just(authentication)
                .cast(SubjectAuthenticationToken.class)
                .flatMap(this::transform)
                .map(JsonResp::ok)
                .flatMap(result -> SendRespUtils.sendJson(exchange.getExchange().getResponse(), result));
    }

    /**
     * 将验证结果转换成统一的JWT结果返回
     *
     * @param authentication
     * @return
     */
    Mono<AuthenticationResult> transform(SubjectAuthenticationToken authentication) {
        return Mono.just(AuthenticationResult.of("", null));
    }
}
