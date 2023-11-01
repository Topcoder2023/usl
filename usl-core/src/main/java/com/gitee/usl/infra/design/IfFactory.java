package com.gitee.usl.infra.design;

import java.util.List;

/**
 * @author hongda.li
 */
public class IfFactory<I> {
    private final List<If<I>> ifList;

    protected IfFactory(List<If<I>> ifList) {
        this.ifList = ifList;
    }

    public void handleAll(IfContext<I> context) {
        for (If<I> item : this.ifList) {
            if (item.condition(context)) {
                item.accept(context);
            }
        }
    }

    public void handleFirst(IfContext<I> context) {
        this.ifList.stream()
                .filter(item -> item.condition(context))
                .findFirst()
                .ifPresent(item -> item.accept(context));
    }

    public void handleFirstOrElse(IfContext<I> context, If<I> defaultOne) {
        this.ifList.stream()
                .filter(item -> item.condition(context))
                .findFirst()
                .orElse(defaultOne)
                .accept(context);
    }
}
