package com.nekoimi.standalone.framework.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

/**
 * <p>BaseEventListener</p>
 *
 * @author nekoimi 2022/05/17
 */
public abstract class BaseEventListener<E extends BaseEvent> implements ApplicationListener<E> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(E event) {
        logger.debug("event - start: {}", event.toString());
        try {
            doEvent(event);
        } catch (Exception e) {
            logger.error("event - error: {}", e.getMessage());
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        logger.debug("event - done");
    }

    protected abstract void doEvent(E event);
}
