package com.gitee.usl.function.base.entity;

import java.util.Map;

/**
 * @author hongda.li
 */
public class EntryItem<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public EntryItem(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return this.value;
    }
}
