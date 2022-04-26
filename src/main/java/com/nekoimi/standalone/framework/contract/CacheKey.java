package com.nekoimi.standalone.framework.contract;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;

import java.time.Duration;

/**
 * nekoimi  2022/4/1 17:03
 */
public interface CacheKey {

    /**
     * 缓存键
     *
     * @return
     */
    String key();

    /**
     * 缓存有效期
     *
     * @return
     */
    Duration ttl();

    /**
     * <p>获取缓存返回值Result类型</p>
     *
     * @return
     */
    Class<?> getResultType();

    /**
     * <p>获取缓存返回值Result类型</p>
     *
     * @return
     */
    TypeReference<?> getResultRef();

    /**
     * <p>获取缓存返回值Result类型</p>
     *
     * @return
     */
    JavaType getResultJavaType();
}
