package com.gitee.usl.function.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.CompareUtil;
import com.gitee.usl.api.annotation.Func;
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
@Func
public class ListFunction {
    @SafeVarargs
    @Func("list")
    public final <T> List<T> list(T... elements) {
        return Arrays.asList(elements);
    }

    @Func("list.from")
    public <T> List<T> from(List<T> source) {
        return new ArrayList<>(source);
    }

    @Func("list.get")
    public Object get(List<?> from, int index) {
        return index < 0 || index >= from.size() ? null : from.get(index);
    }

    @Func("list.set")
    public <T> T set(List<T> from, int index, T element) {
        if (index < 0 || index >= from.size()) {
            return null;
        }
        from.set(index, element);
        return element;
    }

    @Func("list.add")
    public <T> T add(List<T> from, T element) {
        from.add(element);
        return element;
    }

    @Func("list.size")
    public <T> int size(List<T> from) {
        return from.size();
    }

    @Func("list.addAll")
    public <T> List<T> addAll(List<T> from, List<T> elements) {
        if (CollUtil.isNotEmpty(elements)) {
            from.addAll(elements);
        }
        return from;
    }

    @Func("list.addTo")
    public <T> T addTo(List<T> from, int index, T element) {
        if (index < 0 || index >= from.size()) {
            return null;
        }
        from.add(index, element);
        return element;
    }

    @Func("list.remove")
    public <T> T remove(List<T> from, int index) {
        return index < 0 || index > from.size() - 1 ? null : from.remove(index);
    }

    @Func("list.removeIf")
    public <T> List<T> removeIf(Env env, List<T> from, AviatorFunction function) {
        return CollUtil.removeWithAddIf(from, element -> {
            AviatorObject result = function.call(env, FunctionUtils.wrapReturn(element));
            return FunctionUtils.getBooleanValue(result, env);
        });
    }

    @Func("list.clear")
    public <T> List<T> clear(List<T> from) {
        from.clear();
        return from;
    }

    @Func("list.indexOf")
    public <T> int indexOf(List<T> from, T element) {
        return from.indexOf(element);
    }

    @Func("list.lastIndexOf")
    public <T> int lastIndexOf(List<T> from, T element) {
        return from.lastIndexOf(element);
    }

    @Func("list.sort")
    public <T extends Comparable<? super T>> List<T> sort(List<T> from) {
        from.sort(CompareUtil::compare);
        return from;
    }

    @Func("list.sortBy")
    public <T> List<T> sortBy(Env env, List<T> from, AviatorFunction function) {
        from.sort((o1, o2) -> {
            AviatorObject result = function.call(env, FunctionUtils.wrapReturn(o1), FunctionUtils.wrapReturn(o2));
            return FunctionUtils.getNumberValue(result, env).intValue();
        });
        return from;
    }

    @Func("list.resort")
    public <T extends Comparable<? super T>> List<T> resort(List<T> from) {
        from.sort((o1, o2) -> CompareUtil.compare(o2, o1));
        return from;
    }

    @Func("list.resortBy")
    public <T> List<T> resortBy(Env env, List<T> from, AviatorFunction function) {
        from.sort((o1, o2) -> {
            AviatorObject result = function.call(env, FunctionUtils.wrapReturn(o2), FunctionUtils.wrapReturn(o1));
            return FunctionUtils.getNumberValue(result, env).intValue();
        });
        return from;
    }

    @Func("list.sub")
    public <T> List<T> sub(List<T> from, int start, int end) {
        return CollUtil.sub(from, start, end);
    }

    @Func("list.filter")
    public <T> List<T> filter(Env env, List<T> from, AviatorFunction function) {
        return from.stream()
                .filter(element -> {
                    AviatorObject result = function.call(env, FunctionUtils.wrapReturn(element));
                    return FunctionUtils.getBooleanValue(result, env);
                })
                .collect(Collectors.toList());
    }

    @Func("list.union")
    public <T> List<T> union(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.union(from, to));
    }

    @Func("list.unionAll")
    public <T> List<T> unionAll(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.unionAll(from, to));
    }

    @Func("list.unionDistinct")
    public <T> List<T> unionDistinct(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.unionDistinct(from, to));
    }

    @Func("list.intersection")
    public <T> List<T> intersection(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.intersection(from, to));
    }

    @Func("list.intersectionDistinct")
    public <T> List<T> intersectionDistinct(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.intersectionDistinct(from, to));
    }

    @Func("list.disjunction")
    public <T> List<T> disjunction(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.disjunction(from, to));
    }

    @Func("list.containsAny")
    public <T> boolean containsAny(List<T> from, List<T> to) {
        return CollUtil.containsAny(from, to);
    }

    @Func("list.containsAll")
    public <T> boolean containsAll(List<T> from, List<T> to) {
        return CollUtil.containsAll(from, to);
    }

    @Func("list.contains")
    public <T> boolean contains(List<T> from, T element) {
        return CollUtil.contains(from, element);
    }

    @Func("list.distinct")
    public <T> List<T> distinct(List<T> from) {
        return CollUtil.distinct(from);
    }

    @Func("list.join")
    public <T> String join(List<T> from, String symbol) {
        return CollUtil.join(from, symbol);
    }

    @Func("list.allMatch")
    public <T> boolean allMatch(Env env, List<T> from, AviatorFunction function) {
        return CollUtil.allMatch(from, element -> {
            AviatorObject result = function.call(env, FunctionUtils.wrapReturn(element));
            return FunctionUtils.getBooleanValue(result, env);
        });
    }

    @Func("list.anyMatch")
    public <T> boolean anyMatch(Env env, List<T> from, AviatorFunction function) {
        return CollUtil.anyMatch(from, element -> {
            AviatorObject result = function.call(env, FunctionUtils.wrapReturn(element));
            return FunctionUtils.getBooleanValue(result, env);
        });
    }

    @Func("list.toMap")
    public <T> Map<?, ?> toMap(Env env, List<T> from, AviatorFunction keyMapping, AviatorFunction valueMapping) {
        Map<Object, Object> result = new LinkedHashMap<>(from.size());
        for (T element : from) {
            AviatorObject key = keyMapping.call(env, FunctionUtils.wrapReturn(element));
            AviatorObject value = valueMapping.call(env, FunctionUtils.wrapReturn(element));
            result.put(key.getValue(env), value.getValue(env));
        }
        return result;
    }
}