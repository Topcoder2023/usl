package com.gitee.usl.infra.design;

import java.util.function.Consumer;

/**
 * @author hongda.li
 */
public interface If<I, C extends IfContext<I>> extends Consumer<C> {
    /**
     * The condition of IF statement
     *
     * @param context The context of IF statement
     * @return The result of condition
     */
    boolean condition(C context);
}
