package com.gitee.usl.infra.proxy;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.structure.wrapper.IntWrapper;
import com.gitee.usl.infra.structure.wrapper.ParameterWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hongda.li
 */
@Getter
@ToString
@AllArgsConstructor
@Description("方法元属性")
public final class MethodMeta<E> {

    @Description("调用实例")
    private final Object target;

    @Description("调用实例类型")
    private final Class<E> targetType;

    @Description("调用实例方法")
    private final Method method;

    public boolean isNoArgs() {
        return method != null && method.getParameterCount() == 0;
    }

    @Description("将方法元属性转为无参调用器")
    public Invocation<?> toInvocation() {
        return this.toInvocation(null);
    }

    @Description("将方法元属性转为方法调用器")
    public Invocation<?> toInvocation(Object[] args) {
        return new Invocation<>(target, targetType, method, args);
    }

    @Description("获取方法包装参数列表")
    public List<ParameterWrapper> getParameterWrapperList() {
        if (method == null) {
            return Collections.emptyList();
        }

        @Description("形参索引")
        IntWrapper index = new IntWrapper();

        @Description("形参列表")
        Parameter[] parameters = method.getParameters();

        return Arrays.stream(parameters)
                .map(item -> {
                    ParameterWrapper wrapper = new ParameterWrapper();
                    wrapper.setIndex(index.getAndIncrement());
                    wrapper.set(item);
                    return wrapper;
                })
                .collect(Collectors.toList());
    }
}
