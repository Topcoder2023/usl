package com.gitee.usl.infra.design;

/**
 * @author hongda.li
 */
public interface Filter<F> {

    void doFilter(FilterContext<F> context, FilterChain<F> chain);
}
