package com.gitee.usl.infra.proxy;

import java.lang.reflect.Method;

/**
 * 方法元属性
 * 包含方法所在对象的实例以及其实例类型
 *
 * @author hongda.li
 */
public final class MethodMeta<E> {
    private final Object target;
    private final Class<E> targetType;
    private final Method method;

    public MethodMeta(Object target, Class<E> targetType, Method method) {
        this.target = target;
        this.targetType = targetType;
        this.method = method;
    }

    public Object target() {
        return target;
    }

    public Class<E> targetType() {
        return targetType;
    }

    public Method method() {
        return method;
    }

    /**
     * 将方法元属性转为方法调用器
     *
     * @param args 方法调用的参数
     * @return 方法调用器
     */
    public Invocation<?> toInvocation(Object[] args) {
        return new Invocation<>(target, targetType, method, args);
    }
}
