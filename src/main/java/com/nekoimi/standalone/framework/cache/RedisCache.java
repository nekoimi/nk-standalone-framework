package com.nekoimi.standalone.framework.cache;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.nekoimi.standalone.framework.config.properties.AppProperties;
import com.nekoimi.standalone.framework.contract.CacheKey;
import com.nekoimi.standalone.framework.error.exception.FailedToOperationErrorException;
import com.nekoimi.standalone.framework.utils.ClazzUtils;
import com.nekoimi.standalone.framework.utils.JsonUtils;
import com.nekoimi.standalone.framework.web.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

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
    public <T> T get(CacheKey cacheKey, Callable<T> callable) {
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
    public <T> T get(CacheKey cacheKey, Object keyArg, Callable<T> callable) {
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
    public <T> T get(CacheKey cacheKey, List<Object> keyArgs, Callable<T> callable) {
        String key = createKey(cacheKey.key(), keyArgs);
        log.debug("CACHE_GET: {}", key);
        String json = null;
        Boolean bool = redisTemplate.hasKey(key);
        if (bool != null && bool) {
            json = redisTemplate.opsForValue().get(key);
            log.debug("CACHE_GET: {}, 找到缓存，直接返回缓存结果", key);
        } else {
            T t = null;
            try {
                t = callable.call();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                e.printStackTrace();
                throw new FailedToOperationErrorException();
            }
            if (t != null) {
                boolean emptyPageResult = false;
                // 分页结果为空的话不需要缓存
                if (ClazzUtils.instanceOf(t.getClass(), PageResult.class)) {
                    PageResult tp = (PageResult) t;
                    if (tp.getTotal() == 0 || tp.getList().size() <= 0) {
                        emptyPageResult = true;
                    }
                }
                if (!emptyPageResult) {
                    json = JsonUtils.write(t);
                    if (StrUtil.isNotEmpty(json)) {
                        if (Duration.ZERO.equals(cacheKey.ttl())) {
                            redisTemplate.opsForValue().set(key, json);
                        } else {
                            redisTemplate.opsForValue().set(key, json, cacheKey.ttl());
                        }
                        log.debug("CACHE_GET: {}, 未找到缓存，获取调用结果并缓存", key);
                    }
                }
            }
        }
        if (json != null) {
            if (cacheKey.getResultType() != null) {
                return (T) JsonUtils.read(json, cacheKey.getResultType());
            } else if (cacheKey.getResultRef() != null) {
                return (T) JsonUtils.read(json, cacheKey.getResultRef());
            } else if (cacheKey.getResultJavaType() != null) {
                return (T) JsonUtils.read(json, cacheKey.getResultJavaType());
            }
        }
        return null;
    }

    /**
     * <p>清除缓存 - 根据指定key清除</p>
     *
     * @param keys key列表
     */
    public void clearKeys(String... keys) {
        redisTemplate.delete(ListUtil.of(keys).stream().map(this::createKey).collect(Collectors.toList()));
    }

    /**
     * <p>清除缓存 - 根据key模糊匹配</p>
     *
     * @param keys key列表
     */
    public void clearKeysMatchAll(String... keys) {
        ListUtil.of(keys).stream().map(this::createKey).map(key -> key + "*").forEach(keyMatch -> {
            Set<String> keySet = redisTemplate.keys(keyMatch);
            if (keySet != null && !keySet.isEmpty()) {
                redisTemplate.delete(keySet);
            }
        });
    }
}
