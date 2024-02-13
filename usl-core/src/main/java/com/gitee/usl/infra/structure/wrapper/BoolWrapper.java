package com.gitee.usl.infra.structure.wrapper;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class BoolWrapper implements BaseWrapper<Boolean> {
    private boolean value;

    public BoolWrapper() {
        this(false);
    }

    public BoolWrapper(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public Boolean set(Boolean value) {
        this.value = Boolean.TRUE.equals(value);
        return value;
    }

    public boolean isTrue() {
        return Boolean.TRUE.equals(this.value);
    }

    public boolean isFalse() {
        return Boolean.FALSE.equals(this.value);
    }

    public void and(boolean bool) {
        if (!this.value) {
            return;
        }
        this.value = bool;
    }

    public void or(boolean bool) {
        if (this.value) {
            return;
        }
        this.value = bool;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BoolWrapper.class.getSimpleName() + "[", "]")
                .add("value=" + value)
                .toString();
    }

}
