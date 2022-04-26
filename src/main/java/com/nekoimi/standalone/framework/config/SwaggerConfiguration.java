package com.nekoimi.standalone.framework.config;

import com.nekoimi.standalone.framework.config.properties.AppProperties;
import com.nekoimi.standalone.framework.security.customizer.SwaggerSecurityAuthorizeExchangeCustomizer;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.ArrayList;
import java.util.List;

/**
 * nekoimi  2021/12/15 9:28
 */
@Configuration
@EnableSwagger2WebFlux
@ConditionalOnProperty(prefix = "app.web.swagger", name = "enabled", havingValue = "true")
public class SwaggerConfiguration {
    @Value("${spring.application.name}")
    private String name;
    @Autowired
    private AppProperties appProperties;

    /**
     * <p>Swagger路径安全访问</p>
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.web.swagger", name = "enabled", havingValue = "true")
    public SwaggerSecurityAuthorizeExchangeCustomizer swaggerSecurityAuthorizeExchangeCustomizer() {
        return new SwaggerSecurityAuthorizeExchangeCustomizer();
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(name)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("nekoimi", "#", "nekoimime@gmail.com");
        return new ApiInfoBuilder()
                .title(name + " - API接口文档")
                .description(name + " API接口文档")
                .version("1.0")
                .license("APACHE LICENSE, VERSION 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .contact(contact)
                .build();
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION, "header"));
        return apiKeys;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(this::pathSelect)
                .build());
        return securityContexts;
    }

    private boolean pathSelect(String path) {
        AntPathMatcher matcher = new AntPathMatcher();
        boolean b = !matcher.match("/", path);
        for (String s : appProperties.getWeb().getSwagger().getPermitAll()) {
            b = !matcher.match(s, path);
        }
        return b;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(HttpHeaders.AUTHORIZATION, authorizationScopes));
        return securityReferences;
    }
}

