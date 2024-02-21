package com.gitee.usl.infra.structure.wrapper;

import com.gitee.usl.infra.constant.NumberConstant;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class IntWrapper implements BaseWrapper<Integer> {
    private int value;

    public IntWrapper() {
        this(NumberConstant.ZERO);
    }

    public IntWrapper(int value) {
        this.value = value;
    }

    public Integer get() {
        return this.value;
    }

    public Integer set(Integer value) {
        this.value = value == null ? NumberConstant.ZERO : value;
        return this.value;
    }

    public void increment() {
        this.value++;
    }

    public void decrement() {
        this.value--;
    }

    public int getAndIncrement() {
        return this.value++;
    }

    public int incrementAndGet() {
        return ++this.value;
    }

    public int getAndDecrement() {
        return this.value--;
    }

    public int decrementAndGet() {
        return --this.value;
    }

    public boolean isZero() {
        return this.value == 0;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IntWrapper.class.getSimpleName() + "[", "]")
                .add("value=" + value)
                .toString();
    }
}
