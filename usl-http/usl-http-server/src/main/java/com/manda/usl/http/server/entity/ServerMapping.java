package com.manda.usl.http.server.entity;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.AntPathMatcher;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.manda.usl.kernel.annotation.Mapping;
import com.manda.usl.http.server.mapping.ParameterBinder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.http.common.enums.HttpStatus;
import org.smartboot.http.server.HttpRequest;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hongda.li
 */
@Data
@Slf4j
public class ServerMapping {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    private String path;

    private String methodName;

    private Method method;

    private Object instance;

    private boolean containsPathVariable;

    public ServerMapping(Object instance, Method method) {
        this.instance = instance;
        String mappingOnClass = AnnotationUtil.getAnnotationValue(instance.getClass(), Mapping.class);
        Mapping mappingOnMethod = AnnotationUtil.getAnnotation(method, Mapping.class);
        Objects.requireNonNull(mappingOnMethod);
        this.path = Optional.ofNullable(mappingOnClass)
                .map(prefix -> prefix + mappingOnMethod.value())
                .orElse(mappingOnMethod.value());
        this.methodName = mappingOnMethod.method();
        this.method = method;
        this.containsPathVariable = MATCHER.isPattern(path);
    }

    public static AntPathMatcher getMatcher() {
        return MATCHER;
    }

    public boolean matchUri(String uri) {
        return Objects.equals(path, uri) || MATCHER.match(this.path, uri);
    }

    public boolean matchMethod(String method) {
        return CharSequenceUtil.isEmpty(this.methodName) || CharSequenceUtil.equalsIgnoreCase(this.methodName, method);
    }

    public Map<String, String> extractVariable(String uri) {
        if (!MATCHER.match(path, uri)) {
            return Collections.emptyMap();
        } else {
            return MATCHER.extractUriTemplateVariables(path, uri);
        }
    }

    public Result invoke(HttpRequest request) {
        Object result;
        try {
            result = ReflectUtil.invoke(instance, method, new ParameterBinder(this, request).bind());
            if (result == null) {
                return null;
            }
            if (result instanceof Result res) {
                return res;
            } else {
                return Result.success(result);
            }
        } catch (Exception e) {
            log.error("Error in handle mapping", e);
            return Result.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
