package com.nekoimi.standalone.framework.security;

import com.nekoimi.standalone.framework.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * nekoimi  2021/12/19 19:22
 * <p>
 * 默认实现是基于session的
 *
 * @see org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
 * <p>鉴于微服务API全部使用JWT
 * 所以需要自定义实现Redis版本的
 * 以token为key
 * 认证后的AuthenticationToken为value
 * 保存在redis中
 */
@Slf4j
@Component
public class RedisSecurityContextRepository implements ServerSecurityContextRepository {
    private final static String AUTHENTICATION_KEY = "authentication:sub:";
    private final RedisCache redisCache;

    public RedisSecurityContextRepository(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        log.debug("security context save ...");
//        return Mono.just(context.getAuthentication())
//                .cast(SubjectAuthenticationToken.class)
//                .flatMap(subjectToken -> redisCache.set(
//                        AUTHENTICATION_KEY + subjectToken.getSub(), subjectToken.getDetails(), 10))
//                .then(Mono.empty());
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        log.debug("security context load ...");
//        return Mono.just(exchange.getRequest())
//                .map(HttpMessage::getHeaders)
//                .flatMap(headers -> Mono.justOrEmpty(headers.getFirst(HttpHeaders.AUTHORIZATION)))
//                .switchIfEmpty(Mono.error(new RequestAccessDeniedException()))
//                .flatMap(token -> {
//                    // TODO 解析Token，获取sub
//                    return Mono.just("sub");
//                }).flatMap(sub -> redisService.get(AUTHENTICATION_KEY + sub))
//                .switchIfEmpty(Mono.error(new RequestAuthenticationException("Authorization expired")))
//                .cast(UserDetails.class)
//                .flatMap(userDetails -> {
//                    SecurityContextImpl context = new SecurityContextImpl();
//                    SubjectAuthenticationToken authentication = new SubjectAuthenticationToken(
//                            userDetails.getUsername(), userDetails, userDetails.getAuthorities());
//                    context.setAuthentication(authentication);
//                    return Mono.just(context);
//                });
        return Mono.empty();
    }
}
