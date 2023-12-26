package com.gitee.usl.infra.structure.wrapper;

import cn.hutool.core.util.ObjUtil;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class ObjectWrapper<E> implements BaseWrapper<E> {
    private E value;

    public ObjectWrapper() {
    }

    public ObjectWrapper(E value) {
        this.value = value;
    }

    @Override
    public E set(E obj) {
        this.value = obj;
        return obj;
    }

    @Override
    public E get() {
        return value;
    }

    public boolean isEmpty() {
        return ObjUtil.isEmpty(this.value);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ObjectWrapper.class.getSimpleName() + "[", "]")
                .add("value=" + value)
                .toString();
    }
}
