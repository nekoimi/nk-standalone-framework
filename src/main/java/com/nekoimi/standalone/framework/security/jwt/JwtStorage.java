package com.nekoimi.standalone.framework.security.jwt;

import com.nekoimi.standalone.framework.config.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * nekoimi  2022/2/16 16:30
 */
@Slf4j
@Service
public class JwtStorage {
    private final String BLACK_LIST = "black_list";
    private final String REFRESH_TTL = "refresh:";
    private final String REFRESHED = "refreshed:";
    private final AppProperties appProperties;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtStorage(AppProperties appProperties, RedisTemplate<String, String> redisTemplate) {
        this.appProperties = appProperties;
        this.redisTemplate = redisTemplate;
    }

    public String getRefreshTtl(String token) {
        return redisTemplate.opsForValue().get(appProperties.getAppKey() + REFRESH_TTL + token);
    }

    public void setRefreshTtl(String token, int minutes) {
        redisTemplate.opsForValue().set(appProperties.getAppKey() + REFRESH_TTL + token, token, minutes, TimeUnit.MINUTES);
    }

    public String getRefreshed(String token) {
        return redisTemplate.opsForValue().get(appProperties.getAppKey() + REFRESHED + token);
    }

    public void setRefreshed(String token, String newToken, int minutes) {
        redisTemplate.opsForValue().set(appProperties.getAppKey() + REFRESHED + token, newToken, minutes, TimeUnit.MINUTES);
    }

    public void black(String token) {
        redisTemplate.opsForSet().add(appProperties.getAppKey() + BLACK_LIST, token);
    }

    public boolean isBlack(String token) {
        return Optional.ofNullable(redisTemplate.opsForSet().isMember(appProperties.getAppKey() + BLACK_LIST, token))
                .orElse(false);
    }
}
