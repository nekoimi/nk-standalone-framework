package com.nekoimi.standalone.framework.cache;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * nekoimi  2022/4/1 16:09
 *
 * 本地缓存
 */
public class ConcurrentLocalCache {
    private static final Supplier<ConcurrentMap<Object, Object>> cacheSupplier;

    static {
        if (caffeinePresent()) {
            cacheSupplier = ConcurrentLocalCache::createCaffeine;
        } else {
            cacheSupplier = ConcurrentHashMap::new;
        }
    }

    private static boolean caffeinePresent() {
        try {
            Class.forName("com.github.benmanes.caffeine.cache.Cache");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static ConcurrentMap<Object, Object> createCaffeine() {
        return Caffeine.newBuilder().build().asMap();
    }

    public static <K, V> ConcurrentMap<K, V> newCache() {
        return (ConcurrentMap<K, V>) cacheSupplier.get();
    }
}
