package com.nekoimi.standalone.framework.security.provider;

import cn.hutool.core.lang.Dict;
import com.nekoimi.standalone.framework.error.exception.RequestValidationException;
import com.nekoimi.standalone.framework.security.contract.AuthType;
import com.nekoimi.standalone.framework.security.contract.ReactiveAuthenticationSupportProvider;
import com.nekoimi.standalone.framework.security.token.SubjectAuthenticationToken;
import com.nekoimi.standalone.framework.utils.JsonUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.ByteArrayDecoder;
import org.springframework.core.codec.Hints;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Map;

/**
 * nekoimi  2022/1/15 10:44
 */
public abstract class AbstractReactiveAuthenticationSupportProvider implements ReactiveAuthenticationSupportProvider {
    private static final ResolvableType BYTES_TYPE = ResolvableType.forClass(byte[].class);
    private final ByteArrayDecoder decoder = new ByteArrayDecoder();

    /**
     * 认证类型标识
     *
     * @return
     */
    abstract protected AuthType authType();

    /**
     * AuthenticationToken clazz
     *
     * @return
     */
    abstract protected Class<? extends Authentication> authenticationToken();

    /**
     * do convert
     *
     * @param requestParameters 请求参数
     * @return
     */
    abstract protected Mono<? extends Authentication> doConvert(Dict requestParameters);

    /**
     * do authentication
     * @param authentication
     * @return
     */
    abstract protected Mono<SubjectAuthenticationToken> doAuthenticate(Authentication authentication);

    @Override
    public boolean support(Serializable authType) {
        return authType().match(authType);
    }

    @Override
    public boolean support(Authentication authenticationToken) {
        if (authenticationToken == null || authenticationToken() == null)
            return false;
        return authenticationToken.getClass() == authenticationToken();
    }

    @Override
    public Mono<? extends Authentication> convert(ServerWebExchange exchange) {
        return decoder.decode(exchange.getRequest().getBody(), BYTES_TYPE, null, Hints.none())
                .flatMap(bytes -> Mono.justOrEmpty(JsonUtils.readBytes(bytes, Map.class)))
                .filter(map -> !map.isEmpty())
                .switchIfEmpty(Mono.error(new RequestValidationException()))
                .last()
                .map(Dict::new)
                .flatMap(this::doConvert);
    }

    @Override
    public Mono<SubjectAuthenticationToken> authenticate(Authentication authentication) {
        return doAuthenticate(authentication);
    }
}
