package com.nekoimi.standalone.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Nekoimi  2020/5/29 下午5:54
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JWTProperties {
    private String secret;
    private Integer ttl;
    private Integer refreshTtl;
    private Integer refreshConcurrentTtl;
}
