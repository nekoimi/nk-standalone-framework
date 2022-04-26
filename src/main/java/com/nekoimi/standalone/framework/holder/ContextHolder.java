package com.nekoimi.standalone.framework.holder;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * nekoimi  2021/7/20 下午2:18
 */
@Component
public class ContextHolder implements ApplicationContextAware {
    private static ApplicationContext INSTANCE;

    /**
     * 获取上下文实例
     * @return
     */
    public static ApplicationContext getInstance() {
        return INSTANCE;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ContextHolder.INSTANCE = context;
    }
}
