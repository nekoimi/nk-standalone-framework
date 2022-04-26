package com.nekoimi.standalone.framework.contract;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;

import java.lang.reflect.Type;

/**
 * nekoimi  2022/2/8 9:50
 * <p>
 * 类型转换接口
 */
public interface TypeConvertible {

    /**
     * @param toType
     * @param <T>
     * @return
     */
    default <T> T convert(Class<T> toType) {
        return Convert.convert(toType, this);
    }

    /**
     * @param toReference
     * @param <T>
     * @return
     */
    default <T> T convert(TypeReference<T> toReference) {
        return Convert.convert(toReference, this);
    }

    /**
     * @param toType
     * @param <T>
     * @return
     */
    default <T> T convert(Type toType) {
        return Convert.convert(toType, this);
    }
}
