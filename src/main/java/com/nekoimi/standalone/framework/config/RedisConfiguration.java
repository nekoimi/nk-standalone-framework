package com.nekoimi.standalone.framework.config;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nekoimi.standalone.framework.config.properties.AppProperties;
import com.nekoimi.standalone.framework.contract.RedisMessageListener;
import com.nekoimi.standalone.framework.utils.RedisTemplateBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * nekoimi  2021/7/2 下午2:55
 */
@Slf4j
@Configuration
@EnableCaching
@AutoConfigureAfter({RedisAutoConfiguration.class})
public class RedisConfiguration {

    /**
     * 缓存key生成策略
     *
     * @return
     */
    @Bean(name = "cacheKeyGenerator")
    public KeyGenerator keyGenerator(AppProperties properties) {
        return (target, method, params) -> {
            StringBuilder keyBuilder = new StringBuilder(properties.getAppKey());
            keyBuilder.append("-");
            keyBuilder.append(StrUtil.nullToDefault(ClassUtil.getClassName(target, true), "").toLowerCase());
            keyBuilder.append("-");
            keyBuilder.append(StrUtil.nullToDefault(method.getName(), "").toLowerCase());
            keyBuilder.append("-");
            keyBuilder.append("[");
            List<String> paramList = Arrays.stream(params)
                    .filter(Objects::nonNull)
                    .map(StrUtil::toString)
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.toList());
            keyBuilder.append(String.join(".", paramList));
            keyBuilder.append("]");
            log.debug("[CacheKey] {}", keyBuilder);
            return keyBuilder.toString();
        };
    }

    /**
     * 缓存配置
     *
     * @param connectionFactory
     * @return
     */
    @Bean(name = "redisCacheManager")
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        configurationMap.put("cacheExpireSecond", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(1)));
        configurationMap.put("cacheExpireHour", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)));
        configurationMap.put("cacheExpireDay", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofDays(1)));
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)))
                .withInitialCacheConfigurations(configurationMap)
                .build();
    }

    /**
     * 消息处理线程池
     *
     * @return
     */
    @Bean(name = "redisMessageThreadPoolTaskExecutor", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor redisMessageThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setQueueCapacity(1024);
        taskExecutor.setThreadNamePrefix("redis-mq-");
        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * <p>配置redis的消息订阅</p>
     *
     * @param connectionFactory
     * @param objectMapper
     * @param objectProvider
     * @param threadPoolTaskExecutor
     * @return
     */
    @Bean
    @ConditionalOnBean(RedisMessageListener.class)
    public ReactiveRedisMessageListenerContainer reactiveRedisMessageListenerContainer(ReactiveRedisConnectionFactory connectionFactory,
                                                                                       ObjectMapper objectMapper,
                                                                                       ObjectProvider<List<RedisMessageListener>> objectProvider,
                                                                                       @Qualifier("redisMessageThreadPoolTaskExecutor") ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        ReactiveRedisMessageListenerContainer container = new ReactiveRedisMessageListenerContainer(connectionFactory);
        List<RedisMessageListener> redisMessageListeners = objectProvider.getIfAvailable();
        if (redisMessageListeners != null && !redisMessageListeners.isEmpty()) {
            for (RedisMessageListener listener : redisMessageListeners) {
                List<ChannelTopic> topics = ListUtil.of(ChannelTopic.of(listener.message().topic()));
                RedisSerializationContext.SerializationPair<String> channelSerializer = RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string());
                Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(listener.messageType());
                jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
                RedisSerializationContext.SerializationPair<?> messageSerializer = RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer);
                container.receive(topics, channelSerializer, messageSerializer)
                        .publishOn(Schedulers.fromExecutor(threadPoolTaskExecutor))
                        .subscribeOn(Schedulers.fromExecutor(threadPoolTaskExecutor))
                        .subscribe((Consumer<ReactiveSubscription.Message<String, ?>>) message ->
                                listener.handleMessage(message.getMessage(), message.getChannel()));
            }
        }
        return container;
    }

    /**
     * 通用redisTemplate
     *
     * @return
     */
    @Bean(name = "reactiveRedisTemplate")
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate() {
        return RedisTemplateBuilder.build(Object.class);
    }

    /**
     * 对hash类型的数据操作
     */
    @Bean
    public ReactiveHashOperations<String, String, Object> hashOperations(ReactiveRedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     */
    @Bean
    public ReactiveValueOperations<String, Object> valueOperations(ReactiveRedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     */
    @Bean
    public ReactiveListOperations<String, Object> listOperations(ReactiveRedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     */
    @Bean
    public ReactiveSetOperations<String, Object> setOperations(ReactiveRedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     */
    @Bean
    public ReactiveZSetOperations<String, Object> zSetOperations(ReactiveRedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

}
