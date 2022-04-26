package com.nekoimi.standalone.framework.annotations;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nekoimi.standalone.framework.json.DesensitizeStrategy;
import com.nekoimi.standalone.framework.json.StringDesensitizeSerializer;

import java.lang.annotation.*;

/**
 * nekoimi  2022/1/16 23:04
 *
 * <p>Json响应脱敏字段
 * e.g.
 *
 *     @Getter
 *     class Example {
 *         // 用户名称
 *         @JsonFieldDesensitize(value = DesensitizeStrategy.CHINESE_NAME)
 *         private String username;
 *     }
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JsonSerialize(using = StringDesensitizeSerializer.class)
public @interface JsonFieldDesensitize {

    /**
     * 脱敏策略
     * @return
     */
    DesensitizeStrategy value();
}
