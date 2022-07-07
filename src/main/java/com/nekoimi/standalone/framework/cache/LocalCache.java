package com.nekoimi.standalone.framework.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * nekoimi  2022/4/1 16:09
 *
 * 本地缓存
 */
public class LocalCache {
    private static final Supplier<ConcurrentMap<Object, Object>> cacheSupplier;

    static {
        if (caffeineSupport()) {
            cacheSupplier = LocalCache::createCaffeine;
        } else {
            cacheSupplier = ConcurrentHashMap::new;
        }
    }

    public static <K, V> ConcurrentMap<K, V> newCache() {
        return (ConcurrentMap<K, V>) cacheSupplier.get();
    }

    private static ConcurrentMap<Object, Object> createCaffeine() {
        return com.github.benmanes.caffeine.cache.Caffeine.newBuilder().build().asMap();
    }

    private static boolean caffeineSupport() {
        try {
            Class.forName("com.github.benmanes.caffeine.cache.Cache");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
