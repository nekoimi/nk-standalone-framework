package com.nekoimi.standalone.framework.security.filter;

import cn.hutool.core.util.StrUtil;
import com.nekoimi.standalone.framework.error.exception.RequestValidationException;
import com.nekoimi.standalone.framework.security.SecurityRequestHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/17 10:02
 * <p>
 * 解析请求参数
 * 添加请求方式header
 */
@Slf4j
public class ResolverAuthTypeParameterFilter implements WebFilter {
    private static final String authTypeParameter = "auth_type";
    private final ServerWebExchangeMatcher loginPathMatcher;

    public ResolverAuthTypeParameterFilter(ServerWebExchangeMatcher matcher) {
        this.loginPathMatcher = matcher;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return loginPathMatcher.matches(exchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(r -> doParse(exchange)
                        .switchIfEmpty(Mono.error(new RequestValidationException("Query params is missing `auth_type` parameter")))
                        .flatMap(s -> chain.filter(exchange)));
    }

    private Mono<String> doParse(ServerWebExchange exchange) {
        String authType = exchange.getRequest().getQueryParams().getFirst(authTypeParameter);
        if (StrUtil.isBlank(authType)) {
            return Mono.empty();
        }
        if (!StrUtil.isNumeric(authType)) {
            return Mono.empty();
        }
        log.debug("parse auth type: {}", authType);
        // 设置认证类型header
        exchange.getRequest().mutate().header(SecurityRequestHeaders.AUTH_TYPE, authType);
        return Mono.just(authType);
    }
}
