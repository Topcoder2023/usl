package com.gitee.usl.infra.design;

/**
 * @author hongda.li
 */
public class IfContext<I> implements Context<I> {
    private final I value;

    protected IfContext(I value) {
        this.value = value;
    }

    @Override
    public I context() {
        return this.value;
    }
}
