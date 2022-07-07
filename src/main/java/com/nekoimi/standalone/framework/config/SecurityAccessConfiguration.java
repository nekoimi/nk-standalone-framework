package com.nekoimi.standalone.framework.config;

import com.nekoimi.standalone.framework.config.properties.SecurityProperties;
import com.nekoimi.standalone.framework.constants.SecurityAllow;
import com.nekoimi.standalone.framework.security.RedisSecurityContextRepository;
import com.nekoimi.standalone.framework.security.SecurityManagerFactory;
import com.nekoimi.standalone.framework.security.contract.LoginMappingController;
import com.nekoimi.standalone.framework.security.contract.LogoutMappingController;
import com.nekoimi.standalone.framework.security.contract.SecurityAuthorizeExchangeCustomizer;
import com.nekoimi.standalone.framework.security.filter.BeforeRequestFilter;
import com.nekoimi.standalone.framework.security.filter.RequestDebugLogFilter;
import com.nekoimi.standalone.framework.security.filter.ResolverAuthTypeParameterFilter;
import com.nekoimi.standalone.framework.security.handler.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import java.util.List;

/**
 * <p>安全访问配置</p>
 *
 * @author nekoimi 2022/4/24
 */
@Slf4j
public class SecurityAccessConfiguration {
    private final SecurityProperties properties;
    private final CorsConfigurationSource configurationSource;
    private final AccessDeniedExceptionHandler accessDeniedHandler;
    private final AuthenticationExceptionHandler authenticationExceptionHandler;
    private final List<SecurityAuthorizeExchangeCustomizer> authorizeExchangeCustomizers;
    private final ServerWebExchangeMatcher loginExchangeMatcher;
    private final ServerWebExchangeMatcher logoutExchangeMatcher;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final SecurityManagerFactory securityManagerFactory;
    private final RedisSecurityContextRepository securityContextRepository;

    public SecurityAccessConfiguration(SecurityProperties properties,
                                       CorsConfigurationSource configurationSource,
                                       AccessDeniedExceptionHandler accessDeniedHandler,
                                       AuthenticationExceptionHandler authenticationExceptionHandler,
                                       List<SecurityAuthorizeExchangeCustomizer> authorizeExchangeCustomizers,
                                       LoginMappingController loginMappingController,
                                       LogoutMappingController logoutMappingController,
                                       AuthenticationSuccessHandler authenticationSuccessHandler,
                                       AuthenticationFailureHandler authenticationFailureHandler,
                                       LogoutSuccessHandler logoutSuccessHandler,
                                       SecurityManagerFactory securityManagerFactory,
                                       RedisSecurityContextRepository securityContextRepository) {
        this.properties = properties;
        this.configurationSource = configurationSource;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationExceptionHandler = authenticationExceptionHandler;
        this.authorizeExchangeCustomizers = authorizeExchangeCustomizers;
        this.loginExchangeMatcher = loginMappingController.mapping();
        this.logoutExchangeMatcher = logoutMappingController.mapping();
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.securityManagerFactory = securityManagerFactory;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        http.securityContextRepository(securityContextRepository);
        // 关闭csrf
        http.csrf().disable()
                // 开启匿名用户
                .anonymous().and()
                // 关闭Basic基础认证
                .httpBasic().disable()
                // 关闭乱七八杂的header
                .headers().disable()
                // 关闭缓存
                .requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
                // 开启跨域
                .cors().configurationSource(configurationSource).and()
                // 异常处理
                .exceptionHandling(handler -> handler.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationExceptionHandler))
                // 配置请求过滤
                .authorizeExchange(exchange -> {
                    SecurityAllow.RESOURCES_ALL.forEach(s -> exchange.pathMatchers(s).permitAll());
                    properties.getPermitAll().forEach(path -> exchange.pathMatchers(path).permitAll());
                    authorizeExchangeCustomizers.forEach(exchangeCustomizer -> exchangeCustomizer.customize(exchange));
                    exchange.matchers(loginExchangeMatcher).permitAll()
                            .matchers(logoutExchangeMatcher).permitAll()
                            .anyExchange().authenticated();
                })
                // 登录认证
                .formLogin(login -> login.requiresAuthenticationMatcher(loginExchangeMatcher)
                        .authenticationSuccessHandler(authenticationSuccessHandler)
                        .authenticationFailureHandler(authenticationFailureHandler)
                        .authenticationManager(securityManagerFactory)
                        .securityContextRepository(securityContextRepository))
                // 注销认证
                .logout(logout -> logout.requiresLogout(logoutExchangeMatcher).logoutSuccessHandler(logoutSuccessHandler))
                // 插入过滤器
                .addFilterBefore(new BeforeRequestFilter(), SecurityWebFiltersOrder.CSRF)
                .addFilterAfter(new RequestDebugLogFilter(), SecurityWebFiltersOrder.CSRF)
                .addFilterBefore(new ResolverAuthTypeParameterFilter(loginExchangeMatcher), SecurityWebFiltersOrder.HTTP_BASIC);

        // 使用综合认证Token解析器
        return replaceServerAuthenticationConverter(http.build());
    }

    /**
     * <p>使用综合认证Token解析器</p>
     *
     * @param filterChain
     * @return
     */
    private SecurityWebFilterChain replaceServerAuthenticationConverter(SecurityWebFilterChain filterChain) {
        filterChain.getWebFilters()
                .filter(webFilter -> webFilter instanceof AuthenticationWebFilter)
                .cast(AuthenticationWebFilter.class)
                .subscribe(filter -> filter.setServerAuthenticationConverter(securityManagerFactory));
        return filterChain;
    }
}
