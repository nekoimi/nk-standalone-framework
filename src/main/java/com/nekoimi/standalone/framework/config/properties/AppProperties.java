package com.nekoimi.standalone.framework.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * nekoimi  2021/7/20 上午10:49
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String appKey;
    private WebProperties web = new WebProperties();

    @Getter
    @Setter
    public static class WebProperties {
        // swagger
        private SwaggerProperties swagger = new SwaggerProperties();
    }

    @Getter
    @Setter
    public static class SwaggerProperties {
        private Boolean enabled = false;
        private List<String> permitAll = new ArrayList<>();
    }
}
