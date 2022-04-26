package com.nekoimi.standalone.framework.json;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.nekoimi.standalone.framework.annotations.JsonFieldDesensitize;

import java.io.IOException;
import java.util.Objects;

/**
 * nekoimi  2022/3/29 12:02
 */
public class StringDesensitizeSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private DesensitizeStrategy strategy;

    public StringDesensitizeSerializer() {
    }

    public StringDesensitizeSerializer(DesensitizeStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void serialize(String value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeString(DesensitizedUtil.desensitized(value, DesensitizedUtil.DesensitizedType.valueOf(strategy.name())));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
        if (property != null) { // 为空直接跳过
            if (Objects.equals(property.getType().getRawClass(), String.class)) { // 非 String 类直接跳过
                JsonFieldDesensitize fieldDesensitize = property.getAnnotation(JsonFieldDesensitize.class);
                if (fieldDesensitize == null) {
                    fieldDesensitize = property.getContextAnnotation(JsonFieldDesensitize.class);
                }
                if (fieldDesensitize != null) {
                    return new StringDesensitizeSerializer(fieldDesensitize.value());
                }
            }
            return provider.findValueSerializer(property.getType(), property);
        }
        return provider.findNullValueSerializer(property);
    }
}
