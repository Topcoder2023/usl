package com.gitee.usl.infra.structure.wrapper;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface SetWrapper<T> {
    T set(T value);
}
