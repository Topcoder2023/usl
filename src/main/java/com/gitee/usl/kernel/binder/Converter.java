package com.gitee.usl.kernel.binder;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface Converter<T> {
    /**
     * 转换指定目标类型的参数
     *
     * @param sourceValue 指定的目标值
     * @return 转换的结果
     */
    T convert(Object sourceValue);
}
