package com.manda.usl.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Env;
import com.manda.usl.kernel.annotation.Func;
import com.manda.usl.kernel.view.SharedSession;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
@Func
public class ListFunction {
    @Func("list")
    public final List<?> list() {
        return new ArrayList<>();
    }

    @SafeVarargs
    @Func("list_of")
    public final <T> List<T> of(T... elements) {
        return ListUtil.toList(elements);
    }

    @Func("list_from")
    public <T> List<T> from(List<T> source) {
        if (source == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(source);
        }
    }

    @Func("list_get")
    public Object get(List<?> from, int index) {
        return from == null || index < 0 || index >= from.size() ? null : from.get(index);
    }

    @Func("list_set")
    public <T> T set(List<T> from, int index, T element) {
        if (from == null || index < 0 || index >= from.size()) {
            return null;
        }
        from.set(index, element);
        return element;
    }

    @Func("list_add")
    public <T> T add(List<T> from, T element) {
        if (from != null) {
            from.add(element);
        }
        return element;
    }

    @Func("list_size")
    public <T> int size(List<T> from) {
        return from == null ? 0 : from.size();
    }

    @Func("list_add_all")
    public <T> List<T> addAll(List<T> from, List<T> elements) {
        if (CollUtil.isNotEmpty(elements)) {
            from.addAll(elements);
        }
        return from;
    }

    @Func("list_add_to")
    public <T> T addTo(List<T> from, int index, T element) {
        if (from == null || index < 0 || index >= from.size()) {
            return null;
        }
        from.add(index, element);
        return element;
    }

    @Func("list_remove")
    public <T> T remove(List<T> from, int index) {
        return from == null || index < 0 || index > from.size() - 1 ? null : from.remove(index);
    }

    @Func("list_remove_if")
    public <T> List<T> removeIf(Env env, List<T> from, AviatorFunction function) {
        if (from == null || function == null) {
            return from;
        }
        return CollUtil.removeWithAddIf(from, element -> {
            AviatorObject result = function.call(env, FunctionUtils.wrapReturn(element));
            return FunctionUtils.getBooleanValue(result, env);
        });
    }

    @Func("list_clear")
    public <T> List<T> clear(List<T> from) {
        if (from != null) {
            from.clear();
        }
        return from;
    }

    @Func("list_index_of")
    public <T> int indexOf(List<T> from, T element) {
        return from == null || element == null ? -1 : from.indexOf(element);
    }

    @Func("list_last_index_of")
    public <T> int lastIndexOf(List<T> from, T element) {
        return from == null || element == null ? -1 : from.lastIndexOf(element);
    }

    @Func("list_sort")
    public <T extends Comparable<? super T>> List<T> sort(List<T> from) {
        return sort(from, false);
    }

    @Func("list_resort")
    public <T extends Comparable<? super T>> List<T> resort(List<T> from) {
        return sort(from, true);
    }

    @Func("list_sort_by")
    public <T> List<T> sortBy(Env env, List<T> from, AviatorFunction function) {
        return resortBy(env, from, function, false);
    }

    @Func("list_resort_by")
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

    @Func("list_sub")
    public <T> List<T> sub(List<T> from, int start, int end) {
        return CollUtil.sub(from, start, end);
    }

    @Func("list_filter")
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

    @Func("list_union")
    public <T> List<T> union(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.union(from, to));
    }

    @Func("list_union_all")
    public <T> List<T> unionAll(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.unionAll(from, to));
    }

    @Func("list_union_distinct")
    public <T> List<T> unionDistinct(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.unionDistinct(from, to));
    }

    @Func("list_intersection")
    public <T> List<T> intersection(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.intersection(from, to));
    }

    @Func("list_intersection_distinct")
    public <T> List<T> intersectionDistinct(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.intersectionDistinct(from, to));
    }

    @Func("list_disjunction")
    public <T> List<T> disjunction(List<T> from, List<T> to) {
        return new ArrayList<>(CollUtil.disjunction(from, to));
    }

    @Func("list_contains_any")
    public <T> boolean containsAny(List<T> from, List<T> to) {
        return CollUtil.containsAny(from, to);
    }

    @Func("list_contains_all")
    public <T> boolean containsAll(List<T> from, List<T> to) {
        return CollUtil.containsAll(from, to);
    }

    @Func("list_contains")
    public <T> boolean contains(List<T> from, T element) {
        return CollUtil.contains(from, element);
    }

    @Func("list_distinct")
    public <T> List<T> distinct(List<T> from) {
        return CollUtil.distinct(from);
    }

    @Func("list_join")
    public <T> String join(List<T> from, String symbol) {
        return CollUtil.join(from, symbol);
    }

    @Func("list_all_match")
    public <T> boolean allMatch(List<T> from, AviatorFunction function) {
        Env env = SharedSession.getSession().getEnv();
        return CollUtil.allMatch(from, element -> FunctionUtils.getBooleanValue(function.call(env, FunctionUtils.wrapReturn(element)), env));
    }

    @Func("list_any_match")
    public <T> boolean anyMatch(List<T> from, AviatorFunction function) {
        Env env = SharedSession.getSession().getEnv();
        return CollUtil.anyMatch(from, element -> FunctionUtils.getBooleanValue(function.call(env, FunctionUtils.wrapReturn(element)), env));
    }

    @Func("list_to_map")
    public <T> Map<?, ?> toMap(List<T> from, AviatorFunction keyMapping, AviatorFunction valueMapping) {
        Env env = SharedSession.getSession().getEnv();
        if (from == null || keyMapping == null || valueMapping == null) {
            return new LinkedHashMap<>();
        }
        Map<Object, Object> result = new LinkedHashMap<>(from.size());
        for (T element : from) {
            AviatorObject key = keyMapping.call(env, FunctionUtils.wrapReturn(element));
            AviatorObject value = valueMapping.call(env, FunctionUtils.wrapReturn(element));
            result.put(key.getValue(env), value.getValue(env));
        }
        return result;
    }

    @Func("list_foreach")
    public <T> List<T> foreach(List<T> from, AviatorFunction function) {
        Env env = SharedSession.getSession().getEnv();
        if (from != null) {
            from.forEach(element -> function.call(env, FunctionUtils.wrapReturn(element)));
        }
        return from;
    }

    @Func("list_convert")
    public <T> List<?> convert(List<T> from, AviatorFunction function) {
        Env env = SharedSession.getSession().getEnv();
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

    @Func("list_group")
    public <E> Map<?, List<E>> group(List<E> from, AviatorFunction function) {
        Env env = SharedSession.getSession().getEnv();
        if (from == null || function == null) {
            return new LinkedHashMap<>();
        }
        return from.stream().collect(Collectors.groupingBy(element -> function.call(env, FunctionUtils.wrapReturn(element)).getValue(env)));
    }

    private <T extends Comparable<? super T>> List<T> sort(List<T> from, boolean reverse) {
        if (from == null) {
            return new ArrayList<>();
        }

        List<T> sorted = new ArrayList<>(from);
        sorted.sort((o1, o2) -> reverse ? CompareUtil.compare(o2, o1) : CompareUtil.compare(o1, o2));
        return sorted;
    }
}
