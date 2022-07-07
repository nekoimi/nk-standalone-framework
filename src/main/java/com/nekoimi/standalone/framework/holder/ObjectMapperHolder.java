package com.nekoimi.standalone.framework.holder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2021/12/14 10:53
 */
@Component
public class ObjectMapperHolder implements ApplicationContextAware {
    private static ObjectMapper INSTANCE = new ObjectMapper();

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ObjectMapperHolder.INSTANCE = context.getBean(ObjectMapper.class);
    }
}
