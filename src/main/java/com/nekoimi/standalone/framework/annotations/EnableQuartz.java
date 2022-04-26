package com.nekoimi.standalone.framework.annotations;

import com.nekoimi.standalone.framework.config.QuartzConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * <p>开启调度任务</p>
 *
 * @author nekoimi 2022/4/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Order(Ordered.LOWEST_PRECEDENCE - 100)
@Import(QuartzConfiguration.class)
public @interface EnableQuartz {
}
