package com.gitee.usl.infra.proxy;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.base.Objects;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * USL 调用记录类
 * 包含目标对象、目标对象的类型、目标方法、目标方法的参数
 *
 * @author hongda.li
 */
public record Invocation<E>(Object target,
                            Class<E> targetType,
                            Method method,
                            Object[] args) {

    /**
     * 调用方法
     *
     * @return 调用结果
     */
    public Object invoke() {
        return ReflectUtil.invoke(target, method, args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invocation<?> that = (Invocation<?>) o;
        return Objects.equal(target, that.target)
                && Objects.equal(targetType, that.targetType)
                && Objects.equal(method, that.method)
                && Objects.equal(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(target, targetType, method, args);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Invocation.class.getSimpleName() + "[", "]")
                .add("target=" + target)
                .add("targetType=" + targetType)
                .add("method=" + method)
                .add("args=" + Arrays.toString(args))
                .toString();
    }
}
