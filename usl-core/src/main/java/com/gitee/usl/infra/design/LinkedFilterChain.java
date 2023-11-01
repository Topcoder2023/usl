package com.gitee.usl.infra.design;

import java.util.Optional;

/**
 * @author hongda.li
 */
public class LinkedFilterChain<T> implements FilterChain<T> {
    private LinkedFilter<T> head;

    @Override
    public void doChain(FilterContext<T> context) {
        Optional.ofNullable(head.next()).ifPresent(filter -> filter.doFilter(context, this));
    }
}
