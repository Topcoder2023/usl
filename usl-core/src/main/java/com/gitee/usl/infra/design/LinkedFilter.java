package com.gitee.usl.infra.design;

/**
 * @author hongda.li
 */
public abstract class LinkedFilter<F> implements Filter<F> {
    private final LinkedFilter<F> nextFilter;

    protected LinkedFilter() {
        this.nextFilter = null;
    }

    protected LinkedFilter(LinkedFilter<F> nextFilter) {
        this.nextFilter = nextFilter;
    }

    public LinkedFilter<F> next() {
        return nextFilter;
    }
}
