package com.nekoimi.standalone.framework.desensitize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * nekoimi  2022/1/16 23:04
 *
 * <p>Json响应脱敏字段
 * e.g.
 * <block>
 *
 *  {@link lombok.Getter}
 *  class Example {
 *      // 用户名称
 *      {@link JsonFieldDesensitize}(value = DesensitizeStrategy.CHINESE_NAME)
 *      private String username;
 *  }
 *
 * </block>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JsonSerialize(using = StringDesensitizeSerializer.class)
public @interface JsonFieldDesensitize {

    /**
     * 脱敏策略
     *
     * @return
     */
    DesensitizeStrategy value();
}
