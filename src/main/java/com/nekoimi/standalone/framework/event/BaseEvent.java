package com.nekoimi.standalone.framework.event;

import com.nekoimi.standalone.framework.utils.JsonUtils;
import org.springframework.context.ApplicationEvent;

/**
 * <p>BaseEvent</p>
 *
 * @author nekoimi 2022/05/20
 */
public abstract class BaseEvent<T> extends ApplicationEvent {
    private final T data;

    public BaseEvent(Object source, T data) {
        super(source);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        if (this.data instanceof CharSequence) {
            return this.data.toString();
        }
        return JsonUtils.write(this.data);
    }
}
