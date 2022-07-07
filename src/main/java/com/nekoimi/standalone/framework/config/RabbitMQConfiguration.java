package com.nekoimi.standalone.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

/**
 * <p>RabbitMQConfiguration</p>
 *
 * @author nekoimi 2022/4/29
 */
@Slf4j
@EnableRabbit
@Configuration
public class RabbitMQConfiguration {

    @Bean(value = "rabbitMessageQueueTaskExecutor", destroyMethod = "shutdown")
    public TaskExecutor rabbitMessageQueueTaskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("rabbit-mq-task-");
        return executor;
    }

    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, ObjectMapper objectMapper,
                                                                         @Qualifier("rabbitMessageQueueTaskExecutor") TaskExecutor taskExecutor) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        factory.setTaskExecutor(taskExecutor);
        factory.setPrefetchCount(1);
        // 消费端初始并发数
        factory.setConcurrentConsumers(2);
        // 消费端最大并发数
        factory.setMaxConcurrentConsumers(2);
        // 应答方式
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

    @Bean
    public MessageConverter rabbitMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);
        return messageConverter;
    }

//    @Bean
//    public DirectExchange exchange() {
//        return new DirectExchange(MQConstants.EXCHANGE, true, false);
//    }
}
