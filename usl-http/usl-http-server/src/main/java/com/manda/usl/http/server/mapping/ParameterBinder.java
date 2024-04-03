package com.manda.usl.http.server.mapping;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson2.JSON;
import com.manda.usl.kernel.annotation.Body;
import com.manda.usl.http.server.entity.ServerMapping;
import com.manda.usl.kernel.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.http.server.HttpRequest;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author hongda.li
 */
@Slf4j
public class ParameterBinder {
    private final ServerMapping meta;
    private final HttpRequest request;
    private final Map<String, String[]> queryParams;
    private final Map<String, String> pathVariables;
    private final String bodyParams;

    public ParameterBinder(ServerMapping meta, HttpRequest request) {
        this.meta = meta;
        this.request = request;
        this.queryParams = request.getParameters();
        this.pathVariables = meta.extractVariable(request.getRequestURI());
        try {
            this.bodyParams = IoUtil.readUtf8(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object[] bind() {
        Parameter[] parameters = meta.getMethod().getParameters();
        if (ArrayUtil.isEmpty(parameters)) {
            return new Object[]{};
        }

        List<Object> values = new ArrayList<>(parameters.length);
        for (Parameter parameter : parameters) {
            Class<?> type = parameter.getType();

            boolean isMap = Map.class.equals(type);
            boolean isList = List.class.equals(type);
            boolean basicType = ClassUtil.isBasicType(type) || String.class.equals(type);
            Assert.isTrue(basicType || isList || isMap, "Unresolved parameter types : {}", type.getName());

            if (isMap) {
                Map<String, String> params = new HashMap<>();
                queryParams.forEach((name, value) -> {
                    if (ArrayUtil.isEmpty(value)) {
                        params.put(name, null);
                    } else {
                        params.put(name, value[0]);
                    }
                });
                values.add(params);
                continue;
            }

            Body body = AnnotationUtil.getAnnotation(parameter, Body.class);
            if (body != null) {
                if (isList) {
                    Class<?> elementType = (Class<?>) TypeUtil.getTypeArgument(parameter.getParameterizedType());
                    values.add(JSON.parseArray(bodyParams, elementType));
                } else {
                    values.add(JSON.parseObject(bodyParams, type));
                }
                continue;
            }

            Param param = AnnotationUtil.getAnnotation(parameter, Param.class);
            Assert.notNull(param, "Parameter must have @Param annotation : {}", parameter.getName());
            String paramName = param.value();

            if (basicType) {
                String paramValue = Optional.ofNullable(pathVariables.get(paramName)).orElse(request.getParameter(paramName));
                values.add(Convert.convert(type, paramValue));
            } else {
                Class<?> elementType = (Class<?>) TypeUtil.getTypeArgument(parameter.getParameterizedType());
                basicType = ClassUtil.isBasicType(elementType) || String.class.equals(elementType);
                Assert.isTrue(basicType, "Unresolved parameter types : {}", elementType.getName());
                values.add(ListUtil.toList(queryParams.get(paramName)));
            }
        }

        return values.toArray();
    }
}
