package com.nekoimi.standalone.framework.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.nekoimi.standalone.framework.contract.CacheKey;

import java.time.Duration;

/**
 * <p>SecurityCacheKey</p>
 *
 * @author nekoimi 2022/05/23
 */
public enum SecurityCacheKey implements CacheKey {
    SECURITY_CONTEXT("security-context:", Duration.ofMinutes(10), Object.class, "安全上下文缓存"),
    ;

    // 缓存键
    private final String key;
    // 缓存有效期
    private final Duration ttl;
    // 说明
    private final String desc;
    // 返回值类型
    private final Class<?> resultType;
    // 返回值类型
    private final TypeReference<?> resultRef;
    // 返回值类型
    private final JavaType resultJavaType;

    SecurityCacheKey(String key, Duration ttl, Class<?> resultType, String desc) {
        this.key = key;
        this.ttl = ttl;
        this.resultType = resultType;
        this.resultRef = null;
        this.resultJavaType = null;
        this.desc = desc;
    }

    SecurityCacheKey(String key, Duration ttl, TypeReference<?> resultRef, String desc) {
        this.key = key;
        this.ttl = ttl;
        this.resultType = null;
        this.resultRef = resultRef;
        this.resultJavaType = null;
        this.desc = desc;
    }

    SecurityCacheKey(String key, Duration ttl, JavaType resultJavaType, String desc) {
        this.key = key;
        this.ttl = ttl;
        this.resultType = null;
        this.resultRef = null;
        this.resultJavaType = resultJavaType;
        this.desc = desc;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public Duration ttl() {
        return this.ttl;
    }

    @Override
    public Class<?> getResultType() {
        return this.resultType;
    }

    @Override
    public TypeReference<?> getResultRef() {
        return this.resultRef;
    }

    @Override
    public JavaType getResultJavaType() {
        return this.resultJavaType;
    }
}
