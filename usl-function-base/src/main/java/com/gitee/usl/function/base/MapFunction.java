package com.gitee.usl.function.base;

import cn.hutool.core.collection.CollUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.structure.EntryItem;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Env;

import java.util.*;
import java.util.stream.Collectors;

import static com.gitee.usl.grammar.runtime.function.FunctionUtils.getBooleanValue;
import static com.gitee.usl.grammar.runtime.function.FunctionUtils.wrapReturn;

/**
 * @author hongda.li
 */
@SuppressWarnings("unused")
@FunctionGroup
public class MapFunction {
    @Function("map")
    public Map<Object, Object> map() {
        return new LinkedHashMap<>(NumberConstant.EIGHT);
    }

    @Function("map_of")
    public <K, V> Map<K, V> of(K key, V value) {
        Map<K, V> from = new LinkedHashMap<>(NumberConstant.EIGHT);
        from.put(key, value);
        return from;
    }

    @Function("map_size")
    public <K, V> int size(Map<K, V> from) {
        return from == null ? 0 : from.size();
    }

    @Function("map_isEmpty")
    public <K, V> boolean isEmpty(Map<K, V> from) {
        return CollUtil.isEmpty(from);
    }

    @Function("map_containsKey")
    public <K, V> boolean containsKey(Map<K, V> from, K key) {
        return !CollUtil.isEmpty(from) && key != null && from.containsKey(key);
    }

    @Function("map_containsValue")
    public <K, V> boolean containsValue(Map<K, V> from, V value) {
        return !CollUtil.isEmpty(from) && value != null && from.containsValue(value);
    }

    @Function("map_get")
    public <K, V> V get(Map<K, V> from, K key) {
        if (CollUtil.isEmpty(from) || key == null) {
            return null;
        }
        return from.get(key);
    }

    @Function("map_getOrDefault")
    public <K, V> V getOrDefault(Map<K, V> from, K key, V defaultValue) {
        if (CollUtil.isEmpty(from) || key == null) {
            return defaultValue;
        }
        return Optional.ofNullable(from.get(key)).orElse(defaultValue);
    }

    @Function("map_put")
    public <K, V> V put(Map<K, V> from, K key, V value) {
        if (from == null || key == null) {
            return null;
        }
        from.put(key, value);
        return value;
    }

    @Function("map_putIfAbsent")
    public <K, V> V putIfAbsent(Map<K, V> from, K key, V value) {
        if (from == null || key == null) {
            return null;
        }
        from.putIfAbsent(key, value);
        return value;
    }

    @Function("map_putIfPresent")
    public <K, V> V putIfPresent(Map<K, V> from, K key, V value) {
        if (from == null || key == null) {
            return null;
        }
        from.computeIfPresent(key, (k, v) -> value);
        return value;
    }

    @Function("map_putAll")
    public <K, V> Map<K, V> putAll(Map<K, V> from, Map<K, V> target) {
        if (from == null || CollUtil.isEmpty(target)) {
            return from;
        }
        from.putAll(target);
        return from;
    }

    @Function("map_replace")
    public <K, V> V replace(Map<K, V> from, K key, V oldValue, V newValue) {
        if (from == null || key == null) {
            return null;
        }
        from.replace(key, oldValue, newValue);
        return newValue;
    }

    @Function("map_remove")
    public <K, V> V remove(Map<K, V> from, K key) {
        if (CollUtil.isEmpty(from) || key == null) {
            return null;
        }
        return from.remove(key);
    }

    @Function("map_removeIf")
    public <K, V> EntryItem<K, V> removeIf(Env env, Map<K, V> from, _Function function) {
        if (CollUtil.isEmpty(from) || function == null) {
            return null;
        }
        EntryItem<K, V> item = from.entrySet()
                .stream()
                .filter(entry -> getBooleanValue(function.execute(env, wrapReturn(entry.getKey()), wrapReturn(entry.getValue())), env))
                .findFirst()
                .map(entry -> new EntryItem<>(entry.getKey(), entry.getValue()))
                .orElse(null);
        if (item == null) {
            return null;
        }

        from.remove(item.getKey());
        return item;
    }

    @Function("map_clear")
    public <K, V> Map<K, V> clear(Map<K, V> from) {
        if (from != null) {
            from.clear();
        }
        return from;
    }

    @Function("map_keySet")
    public <K, V> List<K> keySet(Map<K, V> from) {
        if (from != null) {
            return new ArrayList<>(from.keySet());
        }
        return Collections.emptyList();
    }

    @Function("map_values")
    public <K, V> List<V> values(Map<K, V> from) {
        if (from != null) {
            return new ArrayList<>(from.values());
        }
        return Collections.emptyList();
    }

    @Function("map_entrySet")
    public <K, V> List<EntryItem<K, V>> entrySet(Map<K, V> from) {
        if (from != null) {
            return from.entrySet()
                    .stream()
                    .map(entry -> new EntryItem<>(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Function("map_foreach")
    public <K, V> Map<K, V> foreach(Env env, Map<K, V> from, _Function function) {
        if (from != null) {
            from.forEach((k, v) -> function.execute(env, wrapReturn(k), wrapReturn(v)));
        }
        return from;
    }

    @Function("map_filter")
    public <K, V> Map<K, V> filter(Env env, Map<K, V> from, _Function function) {
        if (from == null || function == null) {
            return from;
        }
        return this.filterMap(env, from, entry -> function.execute(env,
                wrapReturn(entry.getKey()),
                wrapReturn(entry.getValue())));
    }

    @Function("map_filter_key")
    public <K, V> Map<K, V> filterKey(Env env, Map<K, V> from, _Function function) {
        if (from == null || function == null) {
            return from;
        }
        return this.filterMap(env, from, entry -> function.execute(env, wrapReturn(entry.getKey())));
    }

    @Function("map_filter_value")
    public <K, V> Map<K, V> filterValue(Env env, Map<K, V> from, _Function function) {
        if (from == null || function == null) {
            return from;
        }
        return this.filterMap(env, from, entry -> function.execute(env, wrapReturn(entry.getValue())));
    }

    @Function("map_toList")
    public <K, V> List<?> toList(Env env, Map<K, V> from, _Function function) {
        if (from == null || function == null) {
            return new ArrayList<>();
        }
        return from.entrySet()
                .stream()
                .map(entry -> function.execute(env, wrapReturn(entry.getKey()), wrapReturn(entry.getValue())).getValue(env))
                .collect(Collectors.toList());
    }

    private <K, V> Map<K, V> filterMap(Env env, Map<K, V> from, java.util.function.Function<EntryItem<K, V>, _Object> mapping) {
        final Map<K, V> filter = new LinkedHashMap<>(from.size());
        from.forEach((key, value) -> {
            _Object call = mapping.apply(new EntryItem<>(key, value));
            boolean doFilter = getBooleanValue(call, env);
            if (doFilter) {
                filter.put(key, value);
            }
        });
        return filter;
    }
}
