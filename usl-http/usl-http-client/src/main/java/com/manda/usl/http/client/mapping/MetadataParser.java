package com.manda.usl.http.client.mapping;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.manda.usl.http.client.entity.Metadata;
import com.manda.usl.kernel.annotation.Mapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author hongda.li
 */
public class MetadataParser {
    public static void parse(Metadata metadata) {
        Map<String, String> pathMapping = metadata.getPathMapping();
        Map<String, String> methodMapping = metadata.getMethodMapping();

        Class<?> client = metadata.getClient();
        Objects.requireNonNull(client);

        Method[] methods = ReflectUtil.getMethods(client, method -> AnnotationUtil.hasAnnotation(method, Mapping.class));
        if (ArrayUtil.isEmpty(methods)) {
            return;
        }

        for (Method method : methods) {
            parse(method, pathMapping, methodMapping);
        }
    }

    private static void parse(Method method, Map<String, String> pathMapping, Map<String, String> methodMapping) {
        Mapping mapping = AnnotationUtil.getAnnotation(method, Mapping.class);
        pathMapping.put(method.getName(), mapping.value());
        methodMapping.put(method.getName(), mapping.method());
        Assert.notBlank(mapping.method(), "Http method can't be blank : {}", method.getName());
    }
}
