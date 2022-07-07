package com.nekoimi.standalone.framework.lock;

import cn.hutool.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

/**
 * <p>ReentrantLockHelper</p>
 *
 * @author nekoimi 2022/05/10
 */
@Slf4j
@Component
public class RedisLockHelper {
    private final RedisLockRegistry redisLockRegistry;

    public RedisLockHelper(RedisLockRegistry redisLockRegistry) {
        this.redisLockRegistry = redisLockRegistry;
    }

    /**
     * <p>加锁执行</p>
     *
     * @param lockTarget 锁对象
     * @param callable   执行
     * @param <T>
     * @return
     */
    public <T> Mono<T> doInLock(Class<?> lockTarget, Callable<Mono<T>> callable) {
        Lock lock = redisLockRegistry.obtain(ClassUtil.getClassName(lockTarget, true));
        return Mono.just(0)
                .doFirst(lock::lock)
                .doFinally(ignore -> lock.unlock())
                .flatMap(ignore -> {
                    try {
                        return callable.call();
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
