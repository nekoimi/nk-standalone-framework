package com.nekoimi.standalone.framework.cache;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * nekoimi  2022/4/1 16:26
 *
 * Guava Cache
 */
public abstract class GuavaLocalCache<K, V> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final LoadingCache<K, V> guavaLocalCache;
    private final CacheLoader<K, V> cacheLoader = new CacheLoader<>() {
        @Override
        public V load(K k) throws Exception {
            return loadData(k);
        }
    };

    public GuavaLocalCache() {
        guavaLocalCache = CacheBuilder.newBuilder()
                .maximumSize(10204)
                .build(cacheLoader);
    }

    public GuavaLocalCache(long maxSize) {
        guavaLocalCache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .build(cacheLoader);
    }

    public GuavaLocalCache(Duration duration) {
        this.guavaLocalCache = CacheBuilder.newBuilder()
                .expireAfterAccess(duration)
                .build(cacheLoader);
    }

    /**
     * 加载数据到缓存
     * @param key
     * @return
     */
    abstract protected V loadData(K key);

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public V get(K key) {
        V value = null;
        try {
            value = guavaLocalCache.getUnchecked(key);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
        }
        return value;
    }

    /**
     * 设置缓存
     * @param key
     * @param value
     */
    public void set(K key, V value) {
        guavaLocalCache.put(key, value);
    }

    /**
     * 刷新缓存
     * @param key
     */
    public void refresh(K key) {
        guavaLocalCache.refresh(key);
    }
}
