package com.manda.usl.http.server.mapping;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.manda.usl.kernel.annotation.Mapping;
import com.manda.usl.http.server.entity.Filter;
import com.manda.usl.http.server.entity.ServerMapping;
import com.manda.usl.kernel.bean.BeanContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@Slf4j
public class ServerScanner {

    public static List<Filter> scanFilter() {
        List<Filter> filters = BeanContext.find(Filter.class);
        filters.sort(Comparator.comparingInt(Filter::order));
        filters.forEach(httpFilter -> log.debug("Http-Filter found : {}", httpFilter.getClass().getName()));
        return filters;
    }

    public static Map<String, ServerMapping> scanMapping() {
        Map<String, ServerMapping> metaMap = new HashMap<>();
        BeanContext.with(Mapping.class)
                .stream()
                .flatMap(bean -> {
                    Method[] methods = ReflectUtil.getMethods(bean.getClass(), method -> AnnotationUtil.hasAnnotation(method, Mapping.class));
                    if (ArrayUtil.isEmpty(methods)) {
                        return Stream.empty();
                    } else {
                        return Arrays.stream(methods).map(method -> new ServerMapping(bean, method));
                    }
                })
                .forEach(meta -> {
                    metaMap.put(meta.getPath(), meta);
                    log.debug("Mapping found : [{} - {}]", meta.getPath(), meta.getInstance().getClass().getName());
                });
        return metaMap;
    }
}
