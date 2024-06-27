package com.manda.usl.kernel.view;

import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author hongda.li
 */
public record Invocation<E>(Object target,
                            Class<E> targetType,
                            Method method,
                            List<?> args) {

    public Object invoke() {
        return ReflectUtil.invoke(target, method, args.toArray());
    }

}
