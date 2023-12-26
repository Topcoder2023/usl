package com.gitee.usl.infra.proxy;

import cn.hutool.core.util.ReflectUtil;
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
@Description("调用记录类")
public final class Invocation<E> {

    @Description("调用实例")
    private final Object target;

    @Description("调用实例类型")
    private final Class<E> targetType;

    @Description("调用实例方法")
    private final Method method;

    @Description("调用实例方法参数")
    private final Object[] args;

    public Object invoke() {
        return ReflectUtil.invoke(target, method, args);
    }

}
