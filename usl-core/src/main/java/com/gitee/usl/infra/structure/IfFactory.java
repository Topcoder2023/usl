package com.gitee.usl.infra.structure;

import java.util.List;

/**
 * @author hongda.li
 */
public class IfFactory<I, C extends IfContext<I>> {
    private final List<If<I, C>> ifList;
    private final If<I, C> emptyIf = new If<I, C>() {
        @Override
        public boolean condition(C context) {
            return true;
        }

        @Override
        public void accept(C c) {
            // do nothing
        }
    };

    protected IfFactory(List<If<I, C>> ifList) {
        this.ifList = ifList;
    }

    public void handleAll(C context) {
        ifList.stream().filter(item -> item.condition(context)).forEach(item -> item.accept(context));
    }

    public void handleFirst(C context) {
        this.handleFirstOrElse(context, emptyIf);
    }

    public void handleFirstOrElse(C context, If<I, C> defaultOne) {
        this.ifList.stream()
                .filter(item -> item.condition(context))
                .findFirst()
                .orElse(defaultOne)
                .accept(context);
    }
}
