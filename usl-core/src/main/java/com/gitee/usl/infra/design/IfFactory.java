package com.gitee.usl.infra.design;

import java.util.List;

/**
 * @author hongda.li
 */
public class IfFactory<I, C extends IfContext<I>> {
    private final List<If<I, C>> ifList;

    protected IfFactory(List<If<I, C>> ifList) {
        this.ifList = ifList;
    }

    public void handleAll(C context) {
        for (If<I, C> item : this.ifList) {
            if (item.condition(context)) {
                item.accept(context);
            }
        }
    }

    public void handleFirst(C context) {
        this.handleFirstOrElse(context, new If<I, C>() {
            @Override
            public boolean condition(C context) {
                return true;
            }

            @Override
            public void accept(C c) {
                // do nothing
            }
        });
    }

    public void handleFirstOrElse(C context, If<I, C> defaultOne) {
        this.ifList.stream()
                .filter(item -> item.condition(context))
                .findFirst()
                .orElse(defaultOne)
                .accept(context);
    }
}
