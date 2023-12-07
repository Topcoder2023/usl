package com.gitee.usl.function.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import cn.zhxu.xjson.JsonKit;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.infra.structure.EntryItem;
import com.gitee.usl.infra.constant.NumberConstant;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.googlecode.aviator.runtime.function.FunctionUtils.getBooleanValue;
import static com.googlecode.aviator.runtime.function.FunctionUtils.wrapReturn;

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

    @Func("map.putIfAbsent")
    public <K, V> V putIfAbsent(Map<K, V> from, K key, V value) {
        if (from == null || key == null) {
            return null;
        }
        from.putIfAbsent(key, value);
        return value;
    }

    @Func("map.putIfPresent")
    public <K, V> V putIfPresent(Map<K, V> from, K key, V value) {
        if (from == null || key == null) {
            return null;
        }
        from.computeIfPresent(key, (k, v) -> value);
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

    @Func("map.replace")
    public <K, V> V replace(Map<K, V> from, K key, V oldValue, V newValue) {
        if (from == null || key == null) {
            return null;
        }
        from.replace(key, oldValue, newValue);
        return newValue;
    }

    @Func("map.remove")
    public <K, V> V remove(Map<K, V> from, K key) {
        if (CollUtil.isEmpty(from) || key == null) {
            return null;
        }
        return from.remove(key);
    }

    @Func("map.removeIf")
    public <K, V> EntryItem<K, V> removeIf(Env env, Map<K, V> from, AviatorFunction function) {
        if (CollUtil.isEmpty(from) || function == null) {
            return null;
        }
        EntryItem<K, V> item = from.entrySet()
                .stream()
                .filter(entry -> getBooleanValue(function.call(env, wrapReturn(entry.getKey()), wrapReturn(entry.getValue())), env))
                .findFirst()
                .map(entry -> new EntryItem<>(entry.getKey(), entry.getValue()))
                .orElse(null);
        if (item == null) {
            return null;
        }

        from.remove(item.getKey());
        return item;
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

    @Func("map.foreach")
    public <K, V> Map<K, V> foreach(Env env, Map<K, V> from, AviatorFunction function) {
        if (from != null) {
            from.forEach((k, v) -> function.call(env, wrapReturn(k), wrapReturn(v)));
        }
        return from;
    }

    @Func("map.filter")
    public <K, V> Map<K, V> filter(Env env, Map<K, V> from, AviatorFunction function) {
        if (from == null || function == null) {
            return from;
        }
        return this.filterMap(env, from, entry -> function.call(env,
                wrapReturn(entry.getKey()),
                wrapReturn(entry.getValue())));
    }

    @Func("map.filter.key")
    public <K, V> Map<K, V> filterKey(Env env, Map<K, V> from, AviatorFunction function) {
        if (from == null || function == null) {
            return from;
        }
        return this.filterMap(env, from, entry -> function.call(env, wrapReturn(entry.getKey())));
    }

    @Func("map.filter.value")
    public <K, V> Map<K, V> filterValue(Env env, Map<K, V> from, AviatorFunction function) {
        if (from == null || function == null) {
            return from;
        }
        return this.filterMap(env, from, entry -> function.call(env, wrapReturn(entry.getValue())));
    }

    @Func("map.toList")
    public <K, V> List<?> toList(Env env, Map<K, V> from, AviatorFunction function) {
        if (from == null || function == null) {
            return new ArrayList<>();
        }
        return from.entrySet()
                .stream()
                .map(entry -> function.call(env, wrapReturn(entry.getKey()), wrapReturn(entry.getValue())).getValue(env))
                .collect(Collectors.toList());
    }

    @Func("map.toJson")
    public <K, V> String toStr(Map<K, V> from) {
        if (from == null) {
            return StrPool.EMPTY_JSON;
        } else {
            return JsonKit.toJson(from);
        }
    }

    private <K, V> Map<K, V> filterMap(Env env, Map<K, V> from, Function<EntryItem<K, V>, AviatorObject> mapping) {
        final Map<K, V> filter = new LinkedHashMap<>(from.size());
        from.forEach((key, value) -> {
            AviatorObject call = mapping.apply(new EntryItem<>(key, value));
            boolean doFilter = getBooleanValue(call, env);
            if (doFilter) {
                filter.put(key, value);
            }
        });
        return filter;
    }
}
