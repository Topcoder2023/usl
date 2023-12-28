package com.gitee.usl.function.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.text.StrPool;
import cn.zhxu.xjson.JsonKit;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.constant.NumberConstant;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 虽然 Aviator 提供了 SequenceFunction 等系列函数
 * 但是针对 Java 原生的 List 对象处理仍然不方便，因此才有了 ListFunction 函数组
 *
 * @author hongda.li
 */
@SuppressWarnings("unused")
@FunctionGroup
public class ListFunction {
    @Function("list")
    public final List<?> list() {
        return new ArrayList<>();
    }

    @SafeVarargs
    @Function("list_of")
    public final <T> List<T> of(T... elements) {
        return ListUtil.toList(elements);
    }

    @Function("list_from")
    public <T> List<T> from(List<T> source) {
        if (source == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(source);
        }
    }

    @Function("list_get")
    public Object get(List<?> from, int index) {
        return from == null || index < 0 || index >= from.size() ? null : from.get(index);
    }

    @Function("list_set")
    public <T> T set(List<T> from, int index, T element) {
        if (from == null || index < 0 || index >= from.size()) {
            return null;
        }
        from.set(index, element);
        return element;
    }

    @Function("list_add")
    public <T> T add(List<T> from, T element) {
        if (from != null) {
            from.add(element);
        }
        return element;
    }

    @Function("list_size")
    public <T> int size(List<T> from) {
        return from == null ? 0 : from.size();
    }

    @Function("list_addAll")
    public <T> List<T> addAll(List<T> from, List<T> elements) {
        if (CollUtil.isNotEmpty(elements)) {
            from.addAll(elements);
        }
        return from;
    }

    @Function("list_addTo")
    public <T> T addTo(List<T> from, int index, T element) {
        if (from == null || index < 0 || index >= from.size()) {
            return null;
        }
        from.add(index, element);
        return element;
    }

    @Function("list_remove")
    public <T> T remove(List<T> from, int index) {
        return from == null || index < 0 || index > from.size() - 1 ? null : from.remove(index);
    }

    @Function("list_removeIf")
    public <T> List<T> removeIf(Env env, List<T> from, AviatorFunction function) {
        if (from == null || function == null) {
            return from;
        }
        return CollUtil.removeWithAddIf(from, element -> {
            AviatorObject result = function.call(env, FunctionUtils.wrapReturn(element));
            return FunctionUtils.getBooleanValue(result, env);
        });
    }

    @Function("list_clear")
    public <T> List<T> clear(List<T> from) {
        if (from != null) {
            from.clear();
        }
        return from;
    }

    @Function("list_indexOf")
    public <T> int indexOf(List<T> from, T element) {
        return from == null || element == null ? -1 : from.indexOf(element);
    }

    @Function("list_lastIndexOf")
    public <T> int lastIndexOf(List<T> from, T element) {
        return from == null || element == null ? -1 : from.lastIndexOf(element);
    }

    @Function("list_sort")
    public <T extends Comparable<? super T>> List<T> sort(List<T> from) {
        return sort(from, false);
    }

    @Function("list_resort")
    public <T extends Comparable<? super T>> List<T> resort(List<T> from) {
        return sort(from, true);
    }

    private <T extends Comparable<? super T>> List<T> sort(List<T> from, boolean reverse) {
        if (from == null) {
            return new ArrayList<>();
        }

        List<T> sorted = new ArrayList<>(from);
        sorted.sort((o1, o2) -> reverse ? CompareUtil.compare(o2, o1) : CompareUtil.compare(o1, o2));
        return sorted;
    }

    @Function("list_sortBy")
    public <T> List<T> sortBy(Env env, List<T> from, AviatorFunction function) {
        return resortBy(env, from, function, false);
    }

    @Function("list_resortBy")
    public <T> List<T> resortBy(Env env, List<T> from, AviatorFunction function) {
        return resortBy(env, from, function, true);
    }

    private <T> List<T> resortBy(Env env, List<T> from, AviatorFunction function, boolean reverse) {
        if (from == null) {
            return new ArrayList<>();
        }

        List<T> sorted = new ArrayList<>(from);
        sorted.sort((o1, o2) -> {
            AviatorObject result = function.call(env,
                    FunctionUtils.wrapReturn(reverse ? o2 : o1),
                    FunctionUtils.wrapReturn(reverse ? o1 : o2));
            return FunctionUtils.getNumberValue(result, env).intValue();
        });
        return sorted;
    }

    @Function("list_sub")
    public <T> List<T> sub(List<T> from, int start, int end) {
        return CollUtil.sub(from, start, end);
    }

    @Function("list_filter")
    public <T> List<T> filter(Env env, List<T> from, AviatorFunction function) {
        if (from == null || function == null) {
            return from;
        }
        return from.stream()
                .filter(element -> {
                    AviatorObject result = function.call(env, FunctionUtils.wrapReturn(element));
                    return FunctionUtils.getBooleanValue(result, env);
                })
                .collect(Collectors.toList());
    }

    @Function("list_union")
    public <T> List<T> union(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.union(from, to));
    }

    @Function("list_unionAll")
    public <T> List<T> unionAll(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.unionAll(from, to));
    }

    @Function("list_unionDistinct")
    public <T> List<T> unionDistinct(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.unionDistinct(from, to));
    }

    @Function("list_intersection")
    public <T> List<T> intersection(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.intersection(from, to));
    }

    @Function("list_intersectionDistinct")
    public <T> List<T> intersectionDistinct(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.intersectionDistinct(from, to));
    }

    @Function("list_disjunction")
    public <T> List<T> disjunction(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.disjunction(from, to));
    }

    @Function("list_containsAny")
    public <T> boolean containsAny(List<T> from, List<T> to) {
        return CollUtil.containsAny(from, to);
    }

    @Function("list_containsAll")
    public <T> boolean containsAll(List<T> from, List<T> to) {
        return CollUtil.containsAll(from, to);
    }

    @Function("list_contains")
    public <T> boolean contains(List<T> from, T element) {
        return CollUtil.contains(from, element);
    }

    @Function("list_distinct")
    public <T> List<T> distinct(List<T> from) {
        return CollUtil.distinct(from);
    }

    @Function("list_join")
    public <T> String join(List<T> from, String symbol) {
        return CollUtil.join(from, symbol);
    }

    @Function("list_allMatch")
    public <T> boolean allMatch(Env env, List<T> from, AviatorFunction function) {
        return CollUtil.allMatch(from, element -> FunctionUtils.getBooleanValue(function.call(env, FunctionUtils.wrapReturn(element)), env));
    }

    @Function("list_anyMatch")
    public <T> boolean anyMatch(Env env, List<T> from, AviatorFunction function) {
        return CollUtil.anyMatch(from, element -> FunctionUtils.getBooleanValue(function.call(env, FunctionUtils.wrapReturn(element)), env));
    }

    @Function("list_toMap")
    public <T> Map<?, ?> toMap(Env env, List<T> from, AviatorFunction keyMapping, AviatorFunction valueMapping) {
        if (from == null || keyMapping == null || valueMapping == null) {
            return new LinkedHashMap<>(NumberConstant.EIGHT);
        }
        Map<Object, Object> result = new LinkedHashMap<>(from.size());
        for (T element : from) {
            AviatorObject key = keyMapping.call(env, FunctionUtils.wrapReturn(element));
            AviatorObject value = valueMapping.call(env, FunctionUtils.wrapReturn(element));
            result.put(key.getValue(env), value.getValue(env));
        }
        return result;
    }

    @Function("list_foreach")
    public <T> List<T> foreach(Env env, List<T> from, AviatorFunction function) {
        if (from != null) {
            from.forEach(element -> function.call(env, FunctionUtils.wrapReturn(element)));
        }
        return from;
    }

    @Function("list_convert")
    public <T> List<?> convert(Env env, List<T> from, AviatorFunction function) {
        List<?> result;
        if (from != null && function != null) {
            result = from.stream()
                    .map(element -> function.call(env, FunctionUtils.wrapReturn(element)).getValue(env))
                    .collect(Collectors.toList());
        } else {
            result = Collections.emptyList();
        }
        return result;
    }

    @Function("list_group")
    public <E> Map<?, List<E>> group(Env env, List<E> from, AviatorFunction function) {
        if (from == null || function == null) {
            return new LinkedHashMap<>(NumberConstant.EIGHT);
        }
        return from.stream().collect(Collectors.groupingBy(element -> function.call(env, FunctionUtils.wrapReturn(element)).getValue(env)));
    }

    @Function("list_toJson")
    public String toStr(List<?> from) {
        if (from == null) {
            return StrPool.BRACKET_START + StrPool.BRACKET_END;
        } else {
            return JsonKit.toJson(from);
        }
    }
}
