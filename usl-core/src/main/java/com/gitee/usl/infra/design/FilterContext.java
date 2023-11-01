package com.gitee.usl.infra.design;

/**
 * @author hongda.li
 */
public class FilterContext<F> implements Context<F> {
    private final F value;

    public FilterContext(F value) {
        this.value = value;
    }

    @Override
    public F context() {
        return this.value;
    }
}
