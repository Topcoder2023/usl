package com.gitee.usl.infra.utils;

import com.gitee.usl.api.annotation.Description;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
public class LambdaHelper {
    private LambdaHelper() {
    }

    @Description("分组并保持原序")
    public static <T, K> Collector<T, ?, Map<K, List<T>>> group(Function<? super T, ? extends K> mapping) {
        return Collectors.groupingBy(mapping, LinkedHashMap::new, Collectors.toList());
    }

    @Description("返回永远成立的断言")
    public static <T> Predicate<T> anyTrue() {
        return any -> true;
    }
}
