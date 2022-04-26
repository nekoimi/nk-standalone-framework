package com.nekoimi.standalone.framework.security.jwt.impl;

import com.nekoimi.standalone.framework.security.jwt.JWTStorage;
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
public class RedisJWTStorage implements JWTStorage {
    private final AppProperties appProperties;
    private final RedisTemplate<String, String> redisTemplate;

    public RedisJWTStorage(AppProperties appProperties, RedisTemplate<String, String> redisTemplate) {
        this.appProperties = appProperties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String getRefreshTtl(String token) {
        return redisTemplate.opsForValue().get(appProperties.getAppKey() + REFRESH_TTL + token);
    }

    @Override
    public void setRefreshTtl(String token, int minutes) {
        redisTemplate.opsForValue().set(appProperties.getAppKey() + REFRESH_TTL + token, token, minutes, TimeUnit.MINUTES);
    }

    @Override
    public String getRefreshed(String token) {
        return redisTemplate.opsForValue().get(appProperties.getAppKey() + REFRESHED + token);
    }

    @Override
    public void setRefreshed(String token, String newToken, int minutes) {
        redisTemplate.opsForValue().set(appProperties.getAppKey() + REFRESHED + token, newToken, minutes, TimeUnit.MINUTES);
    }

    @Override
    public void black(String token) {
        redisTemplate.opsForSet().add(appProperties.getAppKey() + BLACK_LIST, token);
    }

    @Override
    public boolean isBlack(String token) {
        return Optional.ofNullable(redisTemplate.opsForSet().isMember(appProperties.getAppKey() + BLACK_LIST, token))
                .orElse(false);
    }
}
