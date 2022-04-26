package com.nekoimi.standalone.framework.contract;

import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * nekoimi  2021/9/29 9:59
 *
 * 通用的Redis订阅消息监听
 */
public interface RedisMessageListener<T> {

    /**
     * 消息主题
     * @return
     */
    MessageTopic message();

    /**
     * 消息类型
     * @return
     */
    Class<T> messageType();

    /**
     * 消息处理
     * @param message
     * @param topic
     * MessageListenerAdapter的默认方法名称为：
     * @see MessageListenerAdapter#ORIGINAL_DEFAULT_LISTENER_METHOD
     * @see MessageListenerAdapter#onMessage(org.springframework.data.redis.connection.Message, byte[])
     */
    void handleMessage(T message, String topic);
}
