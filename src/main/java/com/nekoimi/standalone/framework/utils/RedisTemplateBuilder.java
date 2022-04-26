package com.nekoimi.standalone.framework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nekoimi.standalone.framework.cache.ConcurrentLocalCache;
import com.nekoimi.standalone.framework.holder.ContextHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentMap;

/**
 * <p>RedisTemplateBuilder</p>
 *
 * @author nekoimi 2022/4/24
 */
public class RedisTemplateBuilder {
    /**
     * <p>本地缓存，保存单例</p>
     */
    private final static ConcurrentMap<Class<?>, ReactiveRedisTemplate> redisTemplateCache = ConcurrentLocalCache.newCache();

    /**
     * <p>创建指定类型的RedisTemplate</p>
     *
     * @param type 类型
     * @param <T>
     * @return
     */
    public static <T> ReactiveRedisTemplate<String, T> build(Class<T> type) {
        return redisTemplateCache.computeIfAbsent(type, RedisTemplateBuilder::doBuild);
    }

    /**
     * @param type
     * @param <T>
     * @return
     */
    private static <T> ReactiveRedisTemplate<String, T> doBuild(Class<T> type) {
        ApplicationContext context = ContextHolder.getInstance();
        ReactiveRedisConnectionFactory redisConnectionFactory = context.getBean(ReactiveRedisConnectionFactory.class);
        Assert.notNull(redisConnectionFactory, "redis connection factory is null!");
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        Assert.notNull(objectMapper, "object mapper is null!");
        Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(type);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        RedisSerializationContext.RedisSerializationContextBuilder<String, T> builder = RedisSerializationContext.newSerializationContext();
        RedisSerializationContext<String, T> redisSerializationContext = builder
                .string(RedisSerializer.string())
                .key(RedisSerializer.string())
                .hashKey(RedisSerializer.string())
                .value(jackson2JsonRedisSerializer)
                .hashValue(jackson2JsonRedisSerializer)
                .build();
        return new ReactiveRedisTemplate<>(redisConnectionFactory, redisSerializationContext);
    }
}
