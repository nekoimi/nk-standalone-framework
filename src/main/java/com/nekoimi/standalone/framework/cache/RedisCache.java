package com.nekoimi.standalone.framework.cache;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.nekoimi.standalone.framework.config.properties.AppProperties;
import com.nekoimi.standalone.framework.contract.CacheKey;
import com.nekoimi.standalone.framework.utils.ClazzUtils;
import com.nekoimi.standalone.framework.utils.JsonUtils;
import com.nekoimi.standalone.framework.web.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * nekoimi  2022/3/30 10:39
 * <p>
 * Redis 缓存
 */
@Slf4j
@Component
public class RedisCache {
    @Autowired
    private AppProperties properties;
    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    /**
     * <p>拼接缓存键</p>
     *
     * @param key 原始键
     * @return
     */
    public String createKey(String key) {
        return createKey(key, List.of());
    }

    /**
     * <p>拼接缓存键</p>
     *
     * @param key     原始键
     * @param keyArgs 缓存键参数
     * @return
     */
    public String createKey(String key, List<Object> keyArgs) {
        String appKey = properties.getAppKey() == null ? "app-key" : properties.getAppKey();
        return appKey + ":" + key + StrUtil.join("-", keyArgs);
    }

    /**
     * <p>获取缓存数据</p>
     *
     * @param cacheKey 缓存键
     * @param callable callable
     * @param <T>
     * @return
     */
    public <T> Mono<T> get(CacheKey cacheKey, Callable<Mono<T>> callable) {
        return get(cacheKey, List.of(), callable);
    }

    /**
     * <p>获取缓存数据</p>
     *
     * @param cacheKey 缓存键
     * @param callable callable
     * @param keyArg   keyArg
     * @param <T>
     * @return
     */
    public <T> Mono<T> get(CacheKey cacheKey, Object keyArg, Callable<Mono<T>> callable) {
        return get(cacheKey, List.of(keyArg), callable);
    }

    /**
     * <p>获取缓存数据</p>
     *
     * @param cacheKey 缓存键
     * @param keyArgs  缓存键参数
     * @param callable callable
     * @param <T>
     * @return
     */
    public <T> Mono<T> get(CacheKey cacheKey, List<Object> keyArgs, Callable<Mono<T>> callable) {
        return Mono.fromCallable(() -> createKey(cacheKey.key(), keyArgs)).flatMap(key -> {
            log.debug("CACHE_GET: {}", key);
            return redisTemplate.hasKey(key).flatMap(b -> {
                if (b) {
                    return redisTemplate.opsForValue().get(key);
                } else {
                    Mono<T> callResult;
                    try {
                        callResult = callable.call();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return Mono.empty();
                    }
                    return callResult.flatMap(t -> {
                        // 分页结果为空的话不需要缓存
                        if (ClazzUtils.instanceOf(t.getClass(), PageResult.class)) {
                            PageResult pageResult = (PageResult) t;
                            if (pageResult.getTotal() == 0 || pageResult.getList().size() <= 0) {
                                // 需要返回空的分页数据结构
                                return Mono.justOrEmpty(JsonUtils.write(t));
                            }
                        }
                        return Mono.justOrEmpty(JsonUtils.write(t))
                                .flatMap(json -> {
                                    if (Duration.ZERO.equals(cacheKey.ttl())) {
                                        redisTemplate.opsForValue().set(key, json);
                                    } else {
                                        redisTemplate.opsForValue().set(key, json, cacheKey.ttl());
                                    }
                                    log.debug("CACHE_GET: {}, 未找到缓存，获取调用结果并缓存", key);
                                    return Mono.just(json);
                                });
                    });
                }
            });
        }).flatMap(json -> (Mono<? extends T>) Mono.fromCallable(() -> {
            if (cacheKey.getResultType() != null) {
                return (T) JsonUtils.read(json, cacheKey.getResultType());
            } else if (cacheKey.getResultRef() != null) {
                return (T) JsonUtils.read(json, cacheKey.getResultRef());
            } else if (cacheKey.getResultJavaType() != null) {
                return (T) JsonUtils.read(json, cacheKey.getResultJavaType());
            }
            return Mono.empty();
        }));
    }

    /**
     * <p>清除缓存 - 根据指定key清除</p>
     *
     * @param keys key列表
     */
    public Mono<Void> clearKeys(String... keys) {
        return Flux.fromIterable(ListUtil.of(keys))
                .map(this::createKey)
                .flatMap(redisTemplate::delete)
                .then();
    }

    /**
     * <p>清除缓存 - 根据key模糊匹配</p>
     *
     * @param keys key列表
     */
    public Mono<Void> clearKeysMatchAll(String... keys) {
        return Flux.fromIterable(ListUtil.of(keys))
                .map(this::createKey)
                .map(key -> key + "*")
                .flatMap(keyMatch -> redisTemplate.keys(keyMatch)
                        .flatMap(redisTemplate::delete)
                ).then();
    }
}
