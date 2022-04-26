package com.nekoimi.standalone.framework.listener;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.nekoimi.standalone.framework.contract.RedisMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * nekoimi  2022/3/27 11:32
 * <p>
 * 消息实现基础类
 */
public abstract class AbstractRedisMessageListener<T> implements RedisMessageListener<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Class<T> messageType() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), AbstractRedisMessageListener.class, 0);
    }

    @Override
    public void handleMessage(T message, String topic) {
        try {
            logger.debug("RedisMessage [{}] 开始执行: START {}", topic, message);
            doHandleMessage(message, topic);
            logger.debug("RedisMessage [{}] 执行完毕: OK", topic);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("RedisMessage [{}] 执行完毕: ERROR, {}", topic, e.getMessage());
        }
    }

    protected abstract void doHandleMessage(T message, String topic);
}
