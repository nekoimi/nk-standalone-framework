package com.nekoimi.standalone.constants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.nekoimi.standalone.framework.contract.CacheKey;

import java.time.Duration;

/**
 * nekoimi  2022/4/1 10:45
 */
public enum CacheKeys implements CacheKey {
    USER_INFO("user-info:", Duration.ofMinutes(10), Object.class, "用户信息缓存"),
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

    CacheKeys(String key, Duration ttl, Class<?> resultType, String desc) {
        this.key = key;
        this.ttl = ttl;
        this.resultType = resultType;
        this.resultRef = null;
        this.resultJavaType = null;
        this.desc = desc;
    }

    CacheKeys(String key, Duration ttl, TypeReference<?> resultRef, String desc) {
        this.key = key;
        this.ttl = ttl;
        this.resultType = null;
        this.resultRef = resultRef;
        this.resultJavaType = null;
        this.desc = desc;
    }

    CacheKeys(String key, Duration ttl, JavaType resultJavaType, String desc) {
        this.key = key;
        this.ttl = ttl;
        this.resultType = null;
        this.resultRef = null;
        this.resultJavaType = resultJavaType;
        this.desc = desc;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public Duration ttl() {
        return ttl;
    }

    @Override
    public Class<?> getResultType() {
        return resultType;
    }

    @Override
    public TypeReference<?> getResultRef() {
        return resultRef;
    }

    @Override
    public JavaType getResultJavaType() {
        return resultJavaType;
    }

    public String desc() {
        return desc;
    }
}
