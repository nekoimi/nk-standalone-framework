package com.nekoimi.standalone.framework.cache;

import cn.hutool.core.collection.ListUtil;
import com.nekoimi.standalone.framework.config.properties.AppProperties;
import com.nekoimi.standalone.framework.contract.CacheKey;
import com.nekoimi.standalone.framework.utils.ClazzUtils;
import com.nekoimi.standalone.framework.utils.JsonUtils;
import com.nekoimi.standalone.framework.web.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
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
    private RedisTemplate<String, String> redisTemplate;

    /**
     * <p>拼接缓存键</p>
     *
     * @param key 原始键
     * @return
     */
    public String createKey(String key) {
        String appKey = properties.getAppKey() == null ? "app-key" : properties.getAppKey();
        return appKey + ":" + key;
    }

    /**
     * <p>获取缓存数据</p>
     *
     * @param cacheKey 缓存键
     * @param callable callable
     * @param <T>
     * @return
     */
    public <T> Mono<T> getAsMono(CacheKey cacheKey, Callable<Mono<T>> callable) {
        return getAsMono(cacheKey, "", callable);
    }

    /**
     * <p>获取缓存数据</p>
     *
     * @param cacheKey 缓存键
     * @param pk       缓存键参数
     * @param callable callable
     * @param <T>
     * @return
     */
    public <T> Mono<T> getAsMono(CacheKey cacheKey, String pk, Callable<Mono<T>> callable) {
        return Mono.fromCallable(() -> createKey(cacheKey.key() + pk)).flatMap(key -> {
            log.debug("CACHE_GET: {}", key);
            return Mono.defer(() -> {
                Mono<String> tJson = null;
                Boolean exists = redisTemplate.hasKey(key);
                if (exists != null && exists) {
                    tJson = Mono.defer(() -> Mono.justOrEmpty(redisTemplate.opsForValue().get(key)));
                    log.debug("CACHE_GET: {}, 找到缓存，直接返回缓存结果", key);
                } else {
                    Mono<T> t = null;
                    try {
                        t = callable.call();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return Mono.error(e);
                    }
                    tJson = t.flatMap(Mono::justOrEmpty).flatMap(t1 -> {
                        Class<?> Tlazz = t1.getClass();
                        boolean empty = false;
                        // 分页结果为空的话不需要缓存
                        if (ClazzUtils.instanceOf(Tlazz, PageResult.class)) {
                            PageResult tp = (PageResult) t1;
                            if (tp.getTotal() == 0 || tp.getList().size() <= 0) {
                                empty = true;
                                log.debug("分页结果为空！不缓存");
                            }
                        }
                        // Collection 为空不缓存
                        if (ClazzUtils.instanceOf(Tlazz, Collection.class)) {
                            Collection c = (Collection) t1;
                            if (c.isEmpty()) {
                                empty = true;
                                log.debug("集合为空！不缓存");
                            }
                        }
                        // Map 为空不缓存
                        if (ClazzUtils.instanceOf(Tlazz, Map.class)) {
                            Map m = (Map) t1;
                            if (m.isEmpty()) {
                                empty = true;
                                log.debug("Map为空！不缓存");
                            }
                        }

                        String tWrite = JsonUtils.write(t1);

                        if (!empty) {
                            if (tWrite != null) {
                                if (Duration.ZERO.equals(cacheKey.ttl())) {
                                    redisTemplate.opsForValue().set(key, tWrite);
                                } else {
                                    redisTemplate.opsForValue().set(key, tWrite, cacheKey.ttl());
                                }
                                log.debug("CACHE_GET: {}, 未找到缓存，获取调用结果并缓存", key);
                            }
                        }

                        return Mono.justOrEmpty(tWrite);
                    });
                }
                return tJson.flatMap(json -> Mono.defer(() -> {
                    if (cacheKey.getResultType() != null) {
                        return Mono.justOrEmpty((T) JsonUtils.read(json, cacheKey.getResultType()));
                    } else if (cacheKey.getResultRef() != null) {
                        return Mono.justOrEmpty((T) JsonUtils.read(json, cacheKey.getResultRef()));
                    } else if (cacheKey.getResultJavaType() != null) {
                        return Mono.justOrEmpty((T) JsonUtils.read(json, cacheKey.getResultJavaType()));
                    }
                    return Mono.empty();
                }));
            });
        });
    }

    /**
     * <p>清除缓存 - 根据指定key清除</p>
     *
     * @param keys key列表
     */
    public Mono<Void> clearKeys(String... keys) {
        return Flux.fromIterable(ListUtil.of(keys))
                .map(this::createKey)
                .flatMap(k -> Mono.justOrEmpty(redisTemplate.delete(k)))
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
                .flatMap(keyMatch -> Flux.fromIterable(Optional.ofNullable(redisTemplate.keys(keyMatch)).orElse(new HashSet<>()))
                        .flatMap(k -> Mono.justOrEmpty(redisTemplate.delete(k)))
                ).then();
    }
}
