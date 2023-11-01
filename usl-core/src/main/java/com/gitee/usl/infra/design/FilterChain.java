package com.gitee.usl.infra.design;

/**
 * @author hongda.li
 */
public interface FilterChain<F> {

    void doChain(FilterContext<F> context);
}
