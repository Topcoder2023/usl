package com.gitee.usl.infra.structure.wrapper;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class BoolWrapper implements BaseWrapper<Boolean> {
    private boolean value;

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public Boolean set(Boolean value) {
        this.value = Boolean.TRUE.equals(value);
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BoolWrapper.class.getSimpleName() + "[", "]")
                .add("value=" + value)
                .toString();
    }
}
