package com.gitee.usl.infra.proxy;

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
public record UslInvocation<E>(Object target,
                               Class<E> targetType,
                               Method method,
                               Object[] args) {

    /**
     * 复制一个新的 USL 调用记录类
     *
     * @param base 复制的基类
     * @param args 新的参数
     * @param <E>  复制的泛型
     * @return 复制结果
     */
    public static <E> UslInvocation<E> copy(UslInvocation<E> base, Object[] args) {
        return new UslInvocation<>(base.target(), base.targetType, base.method(), args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UslInvocation<?> that = (UslInvocation<?>) o;
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
        return new StringJoiner(", ", UslInvocation.class.getSimpleName() + "[", "]")
                .add("target=" + target)
                .add("targetType=" + targetType)
                .add("method=" + method)
                .add("args=" + Arrays.toString(args))
                .toString();
    }
}
