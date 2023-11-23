package com.gitee.usl.infra.structure;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface Context<C> {
    /**
     * Obtain context value
     *
     * @return Context value
     */
    C context();
}
