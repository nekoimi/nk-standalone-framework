package com.nekoimi.standalone.framework.security.customizer;

import com.nekoimi.standalone.framework.security.contract.SecurityAuthorizeExchangeCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import java.util.ArrayList;
import java.util.List;

/**
 * nekoimi  2021/12/23 22:11
 *
 * <p>swagger 访问路径添加白名单配置</p>
 */
@Slf4j
public class SwaggerSecurityAuthorizeExchangeCustomizer implements SecurityAuthorizeExchangeCustomizer {
    private static final List<String> SWAGGER_RESOURCES_ALL = new ArrayList<>();

    static {
        SWAGGER_RESOURCES_ALL.add("/doc.html");
        SWAGGER_RESOURCES_ALL.add("/v2/api-docs");
        SWAGGER_RESOURCES_ALL.add("/webjars/**");
        SWAGGER_RESOURCES_ALL.add("/swagger-resources");
        SWAGGER_RESOURCES_ALL.add("/swagger-resources/**");
    }

    @Override
    public void customize(ServerHttpSecurity.AuthorizeExchangeSpec exchange) {
        SWAGGER_RESOURCES_ALL.forEach(s -> exchange.pathMatchers(s).permitAll());
        log.debug("swagger security config customizer done ...");
    }
}
