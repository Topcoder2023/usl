package com.gitee.usl.infra.proxy;

import com.gitee.usl.api.annotation.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Method;

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

    @Description("将方法元属性转为方法调用器")
    public Invocation<?> toInvocation(Object[] args) {
        return new Invocation<>(target, targetType, method, args);
    }
}
