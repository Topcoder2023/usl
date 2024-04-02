package com.manda.usl.kernel.entity;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hongda.li
 */
public record MethodMeta(Object target, Method method) {
    public Invocation<?> toInvocation(List<?> args) {
        return new Invocation<>(target, target.getClass(), method, args);
    }
}
