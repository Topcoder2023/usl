package com.gitee.usl.function.base;

import cn.hutool.core.collection.CollUtil;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.function.base.entity.EntryItem;
import com.gitee.usl.infra.constant.NumberConstant;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
@Func
public class MapFunction {
    @Func("map")
    public Map<Object, Object> map() {
        return new LinkedHashMap<>(NumberConstant.EIGHT);
    }

    @Func("map.of")
    public <K, V> Map<K, V> of(K key, V value) {
        Map<K, V> map = new LinkedHashMap<>(NumberConstant.EIGHT);
        map.put(key, value);
        return map;
    }

    @Func("map.size")
    public <K, V> int size(Map<K, V> from) {
        return from == null ? 0 : from.size();
    }

    @Func("map.isEmpty")
    public <K, V> boolean isEmpty(Map<K, V> from) {
        return CollUtil.isEmpty(from);
    }

    @Func("map.containsKey")
    public <K, V> boolean containsKey(Map<K, V> from, K key) {
        return !CollUtil.isEmpty(from) && key != null && from.containsKey(key);
    }

    @Func("map.containsValue")
    public <K, V> boolean containsValue(Map<K, V> from, V value) {
        return !CollUtil.isEmpty(from) && value != null && from.containsValue(value);
    }

    @Func("map.get")
    public <K, V> V get(Map<K, V> from, K key) {
        if (CollUtil.isEmpty(from) || key == null) {
            return null;
        }
        return from.get(key);
    }

    @Func("map.getOrDefault")
    public <K, V> V getOrDefault(Map<K, V> from, K key, V defaultValue) {
        if (CollUtil.isEmpty(from) || key == null) {
            return defaultValue;
        }
        return Optional.ofNullable(from.get(key)).orElse(defaultValue);
    }

    @Func("map.put")
    public <K, V> V put(Map<K, V> from, K key, V value) {
        if (from == null || key == null) {
            return null;
        }
        from.put(key, value);
        return value;
    }

    @Func("map.putAll")
    public <K, V> Map<K, V> putAll(Map<K, V> from, Map<K, V> target) {
        if (from == null || CollUtil.isEmpty(target)) {
            return from;
        }
        from.putAll(target);
        return from;
    }

    @Func("map.remove")
    public <K, V> V remove(Map<K, V> from, K key) {
        if (CollUtil.isEmpty(from) || key == null) {
            return null;
        }
        return from.remove(key);
    }

    @Func("map.clear")
    public <K, V> Map<K, V> clear(Map<K, V> from) {
        if (from != null) {
            from.clear();
        }
        return from;
    }

    @Func("map.keySet")
    public <K, V> List<K> keySet(Map<K, V> from) {
        if (from != null) {
            return new ArrayList<>(from.keySet());
        }
        return Collections.emptyList();
    }

    @Func("map.values")
    public <K, V> List<V> values(Map<K, V> from) {
        if (from != null) {
            return new ArrayList<>(from.values());
        }
        return Collections.emptyList();
    }

    @Func("map.entrySet")
    public <K, V> List<EntryItem<K, V>> entrySet(Map<K, V> from) {
        if (from != null) {
            return from.entrySet()
                    .stream()
                    .map(entry -> new EntryItem<>(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
