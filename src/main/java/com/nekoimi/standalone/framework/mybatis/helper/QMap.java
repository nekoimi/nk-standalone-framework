package com.nekoimi.standalone.framework.mybatis.helper;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * nekoimi  2021/12/18 17:25
 */
public class QMap<K, V> {
    private final Map<K, V> data;

    public QMap(Map<K, V> data) {
        this.data = data;
    }

    public QMap<K, V> put(K k, V v) {
        this.data.put(k, v);
        return this;
    }

    public void forEach(BiConsumer<K, V> action) {
        this.data.forEach(action);
    }
}
